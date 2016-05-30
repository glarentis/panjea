package it.eurotn.panjea.onroad.importer.manager;

import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.domain.Cliente;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.Preference;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Cap;
import it.eurotn.panjea.anagrafica.domain.datigeografici.DatiGeografici;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo1;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo2;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Localita;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Nazione;
import it.eurotn.panjea.anagrafica.manager.datigeografici.interfaces.DatiGeograficiManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.EntitaManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.SediEntitaManager;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficheDuplicateException;
import it.eurotn.panjea.exporter.exception.FileCreationException;
import it.eurotn.panjea.exporter.exception.ImportException;
import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino;
import it.eurotn.panjea.magazzino.manager.interfaces.SediMagazzinoManager;
import it.eurotn.panjea.onroad.domain.ClienteOnRoad;
import it.eurotn.panjea.onroad.domain.wrapper.ClientiOnRoad;
import it.eurotn.panjea.onroad.importer.manager.interfaces.ImportaClienti;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.pagamenti.domain.SedePagamento;
import it.eurotn.panjea.pagamenti.manager.interfaces.SediPagamentoManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.service.interfaces.PanjeaMessage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.beanio.BeanReader;
import org.beanio.BeanReaderErrorHandler;
import org.beanio.BeanReaderException;
import org.beanio.BeanReaderIOException;
import org.beanio.StreamFactory;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.ejb.TransactionTimeout;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.OnRoadImportaClienti")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.OnRoadImportaClienti")
public class ImportaClientiBean implements ImportaClienti {

	private class FileReaderErrorHandler implements BeanReaderErrorHandler {

		/**
		 * Costruttore.
		 */
		public FileReaderErrorHandler() {
			super();
		}

		@Override
		public void handleError(BeanReaderException ex) throws Exception {
			if (ex.getRecordContext() != null && ex.getRecordContext().getRecordText().getBytes().length == 1
					&& ex.getRecordContext().getRecordText().getBytes()[0] == 26) {
				// salto perchè i file di aton finiscono sempre con una riga che contiene un crattere identificato
				// da 26
				if (logger.isDebugEnabled()) {
					logger.debug("--> Riga non valida ( carattere ascii di chiusura file ), salto");
				}
				return;
			}
			logger.error("-->errore nel leggere il record del file alla riga "
					+ (ex.getRecordContext() != null ? ex.getRecordContext().getRecordText() : ex), ex);
			throw new RuntimeException(ex.getCause());
		}
	}

	private static Logger logger = Logger.getLogger(ImportaClientiBean.class);

	@EJB
	private PanjeaDAO panjeaDAO;

	@EJB
	private PanjeaMessage panjeaMessage;

	@EJB
	private EntitaManager entitaManager;

	@EJB
	private SediEntitaManager sediEntitaManager;

	@EJB
	private SediMagazzinoManager sediMagazzinoManager;

	@EJB
	private SediPagamentoManager sediPagamentoManager;

	@EJB
	private DatiGeograficiManager datiGeograficiManager;

	public static final String CLIENTI_FILE_NAME = "NCLIENT";

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public String getAtonImportDir() {
		// Recupero la cartelll di importazione
		javax.persistence.Query query = panjeaDAO.prepareNamedQuery("Preference.caricaPerChiave");
		query.setParameter("paramChiave", "onRoadDirImport");
		Preference preference = null;
		String result = "";
		try {
			preference = (Preference) panjeaDAO.getSingleResult(query);
			result = preference.getValore();
		} catch (NoResultException pnf) {
			logger.error("--> preferenza non trovata: onRoadDirImport", pnf);
			panjeaMessage.send("Impostare nelle preferenze globali la chiave onRoadDirImport",
					PanjeaMessage.MESSAGE_GENERIC_SELECTOR);
		} catch (Exception e) {
			logger.error("--> Errore ricerca preference con key onRoadDirImport", e);
			throw new RuntimeException("Errore ricerca preference con key onRoadDirImport", e);
		}
		return result;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public String getAtonPrefixImport() {
		// Recupero la cartella di importazione
		javax.persistence.Query query = panjeaDAO.prepareNamedQuery("Preference.caricaPerChiave");
		query.setParameter("paramChiave", "atonPrefixImport");
		Preference preference = null;
		try {
			preference = (Preference) panjeaDAO.getSingleResult(query);
		} catch (Exception e) {
			return "";
		}
		return preference.getValore();
	}

	private Cliente getCliente(ClienteOnRoad clienteOnRoad) throws AnagraficheDuplicateException {

		List<Cap> caps = null;
		if (clienteOnRoad.getCap() != null && !clienteOnRoad.getCap().isEmpty()) {
			DatiGeografici capRicerca = new DatiGeografici();
			capRicerca.setCodiceCap(clienteOnRoad.getCap());
			caps = datiGeograficiManager.caricaCap(capRicerca);
		}

		List<Localita> localitas = null;
		if (clienteOnRoad.getLocalita() != null && !clienteOnRoad.getLocalita().isEmpty()) {
			DatiGeografici localitaRicerca = new DatiGeografici();
			localitaRicerca.setCodiceLocalita(clienteOnRoad.getLocalita());
			localitas = datiGeograficiManager.caricaLocalita(localitaRicerca);
		}

		// DatiGeografici provinciaRicerca = new DatiGeografici();
		// provinciaRicerca.setCodiceCap(clienteOnRoad.getProvincia());
		// List<LivelloAmministrativo2> province = datiGeograficiManager.caricaLivelloAmministrativo2(provinciaRicerca);

		Cap cap = caps != null && caps.size() > 0 ? caps.get(0) : null;
		Localita localita = localitas != null && localitas.size() > 0 ? localitas.get(0) : null;
		LivelloAmministrativo2 provincia = null;
		LivelloAmministrativo1 regione = null;
		Nazione nazione = null;
		if (cap != null) {
			regione = cap.getLivelloAmministrativo1();
			provincia = cap.getLivelloAmministrativo2();
			nazione = cap.getNazione();
		} else if (localita != null) {
			regione = localita.getLivelloAmministrativo1();
			provincia = localita.getLivelloAmministrativo2();
			nazione = localita.getNazione();
		}

		Cliente cliente = new Cliente();
		cliente.getAnagrafica().getSedeAnagrafica().getDatiGeografici().setCap(cap);
		cliente.getAnagrafica().getSedeAnagrafica().getDatiGeografici().setLocalita(localita);
		cliente.getAnagrafica().getSedeAnagrafica().getDatiGeografici().setLivelloAmministrativo1(regione);
		cliente.getAnagrafica().getSedeAnagrafica().getDatiGeografici().setLivelloAmministrativo2(provincia);
		cliente.getAnagrafica().getSedeAnagrafica().getDatiGeografici().setNazione(nazione);

		cliente.getAnagrafica().getSedeAnagrafica().setIndirizzo(clienteOnRoad.getIndirizzo());
		cliente.getAnagrafica().getSedeAnagrafica().setTelefono(clienteOnRoad.getTelefono());

		// lascio che venga creato un nuovo codice per il cliente, potrebbero esserci conflitti
		cliente.setCodice(null);

		cliente.getAnagrafica().setCodiceFiscale(clienteOnRoad.getCodiceFiscale());
		cliente.getAnagrafica().setPartiteIVA(clienteOnRoad.getPartitaIva());
		cliente.getAnagrafica()
				.setDenominazione(clienteOnRoad.getRagioneSociale() + clienteOnRoad.getRagioneSociale1());

		Entita entitaSalvata = null;
		try {
			entitaSalvata = entitaManager.salvaEntita(cliente);
		} catch (AnagraficheDuplicateException e1) {
			throw e1;
		} catch (Exception e1) {
			throw new RuntimeException(e1);
		}

		SedeEntita sedePrincipale = null;
		try {
			sedePrincipale = sediEntitaManager.caricaSedePrincipaleEntita(entitaSalvata.getId());
		} catch (AnagraficaServiceException e1) {

		}
		sedePrincipale.setCodiceImportazione(clienteOnRoad.getCodiceCliente());
		sedePrincipale.setCodice(clienteOnRoad.getCodiceDestinatario());
		sedePrincipale.getBloccoSede().setBlocco(clienteOnRoad.isSedeBloccata());
		sedePrincipale.setLingua(clienteOnRoad.getLingua());

		if (clienteOnRoad.getCodiceAgente() != null && !clienteOnRoad.getCodiceAgente().isEmpty()) {
			try {
				Query queryAgente = panjeaDAO
						.prepareQuery("select a from AgenteLite a where a.codice=:paramCodiceAgente");
				queryAgente.setParameter("paramCodiceAgente", new Integer(clienteOnRoad.getCodiceAgente()));
				AgenteLite agente = (AgenteLite) panjeaDAO.getSingleResult(queryAgente);
				sedePrincipale.setAgente(agente);
			} catch (ObjectNotFoundException e) {
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		if (clienteOnRoad.getCodiceListino() != null && !clienteOnRoad.getCodiceListino().isEmpty()) {
			try {
				SedeMagazzino sedeMagazzino = sediMagazzinoManager.caricaSedeMagazzinoPrincipale(entitaSalvata);

				Query queryListino = panjeaDAO
						.prepareQuery("select l from Listino l where l.codice=:paramCodiceListino");
				queryListino.setParameter("paramCodiceListino", clienteOnRoad.getCodiceListino());
				Listino listino = (Listino) panjeaDAO.getSingleResult(queryListino);
				sedeMagazzino.setListino(listino);

				sediMagazzinoManager.salvaSedeMagazzino(sedeMagazzino);
			} catch (ObjectNotFoundException e) {

			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		if (clienteOnRoad.getCodiceListino() != null && !clienteOnRoad.getCodiceListino().isEmpty()) {
			try {
				SedePagamento sedePagamento = sediPagamentoManager.caricaSedePagamentoPrincipaleEntita(entitaSalvata
						.getEntitaLite());

				Query queryCodicePagamento = panjeaDAO
						.prepareQuery("select cp from CodicePagamento cp where cp.codicePagamento=:paramCodicePagamento");
				queryCodicePagamento.setParameter("paramCodicePagamento", clienteOnRoad.getCodiceListino());
				CodicePagamento codicePagamento = (CodicePagamento) panjeaDAO.getSingleResult(queryCodicePagamento);

				sedePagamento.setCodicePagamento(codicePagamento);

				sediPagamentoManager.salvaSedePagamento(sedePagamento);
			} catch (ObjectNotFoundException e) {

			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		return cliente;
	}

	/**
	 * 
	 * @param templateName
	 *            nome del template
	 * @return file di template per il tipo di esportazione
	 * @throws FileCreationException
	 *             rilanciata se manca la chiave di configurazione od il file di template.
	 */
	private File getFilePathForTemplate(String templateName) throws FileCreationException {
		// Recupero la cartella dove sono i file di template
		Preference preference = null;
		try {
			Query query = panjeaDAO.prepareNamedQuery("Preference.caricaPerChiave");
			query.setParameter("paramChiave", "onRoadDirTemplate");
			preference = (Preference) panjeaDAO.getSingleResult(query);
		} catch (Exception e) {
			logger.error("--> Errore ricerca preference con key onRoadDirTemplate", e);
			throw new FileCreationException("Nelle preferenze generali manca la chiave onRoadDirTemplate");
		}

		// Costruisco il file
		String fileName = preference.getValore() + templateName + ".xml";
		File file = new File(fileName);
		if (!file.exists()) {
			logger.error("--> File di template per l'esportazione macante. Percorso del file " + file.getAbsolutePath());
			throw new FileCreationException("File di template per l'esportazione di tipo "
					+ getClass().getSimpleName().replace("Exporter", "") + " macante. Percorso del file "
					+ file.getAbsolutePath());
		}
		return file;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@TransactionTimeout(value = 7200)
	public ClientiOnRoad importa(String pathFile) throws ImportException {

		ClientiOnRoad clientiOnRoad = new ClientiOnRoad();

		List<ClienteOnRoad> clientiImportati = importaClienti(pathFile);

		for (ClienteOnRoad clienteImportato : clientiImportati) {

			System.out.println("clienteImportato = " + clienteImportato);
			Cliente cliente = null;
			try {
				cliente = getCliente(clienteImportato);
				clientiOnRoad.addClienteImportato(cliente);
			} catch (AnagraficheDuplicateException e) {
				clientiOnRoad.addClienteDuplicato(clienteImportato);
			}
		}

		return clientiOnRoad;
	}

	/**
	 * 
	 * @param pathFile
	 *            path della cartella contenente i file di aton
	 * @return lista degli ordini importati con le testate
	 * @throws ImportException
	 *             rilanciata se ci sono problemi nella struttura del file.
	 */
	private List<ClienteOnRoad> importaClienti(String pathFile) throws ImportException {
		List<ClienteOnRoad> clinetiImportati = new ArrayList<ClienteOnRoad>();
		File fileClienti = new File(pathFile);
		if (fileClienti.exists()) {
			try {
				StreamFactory factory = StreamFactory.newInstance();
				factory.load(getFilePathForTemplate(CLIENTI_FILE_NAME));
				BeanReader in = factory.createReader("clienti", fileClienti);
				in.setErrorHandler(new FileReaderErrorHandler());

				ClienteOnRoad clienteRoad;
				while ((clienteRoad = (ClienteOnRoad) in.read()) != null) {
					clinetiImportati.add(clienteRoad);
				}
				in.close();
			} catch (FileCreationException ex) {
				throw new RuntimeException(ex);
			} catch (BeanReaderIOException e) {
				throw new ImportException(e, fileClienti.getName());
			}
		}
		return clinetiImportati;
	}
}
