package it.eurotn.panjea.spedizioni.manager;

import it.eurotn.codice.generator.interfaces.ProtocolloGenerator;
import it.eurotn.locking.ILock;
import it.eurotn.locking.service.interfaces.LockingServiceRemote;
import it.eurotn.panjea.anagrafica.domain.Vettore;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Cap;
import it.eurotn.panjea.anagrafica.manager.interfaces.AziendeManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.EntitaManager;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;
import it.eurotn.panjea.anagrafica.util.AziendaAnagraficaDTO;
import it.eurotn.panjea.exporter.exception.FileCreationException;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoManager;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.service.interfaces.PanjeaMessage;
import it.eurotn.panjea.spedizioni.domain.Segnacollo;
import it.eurotn.panjea.spedizioni.exception.SpedizioniVettoreException;
import it.eurotn.panjea.spedizioni.manager.interfaces.SpedizioniManager;
import it.eurotn.panjea.spedizioni.util.AreaMagazzinoRendicontazione;
import it.eurotn.panjea.spedizioni.util.AreaMagazzinoSpedizione;
import it.eurotn.panjea.spedizioni.util.ParametriCreazioneEtichette;
import it.eurotn.security.JecPrincipal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.beanio.BeanReader;
import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.SpedizioniManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.SpedizioniManager")
public class SpedizioniManagerBean implements SpedizioniManager {

	private static final Integer VALUE_MAX = 9999999;

	public static final String USER_LOCK_RENDICONTAZIONE = "Rendicontazione spedizioni";

	private static Logger logger = Logger.getLogger(SpedizioniManagerBean.class);

	@Resource
	protected SessionContext sessionContext;

	@EJB
	private PanjeaDAO panjeaDAO;

	@EJB
	private EntitaManager entitaManager;

	@EJB
	private AziendeManager aziendeManager;

	@EJB
	private AreaMagazzinoManager areaMagazzinoManager;

	@EJB
	private PanjeaMessage panjeaMessage;

	@EJB
	private LockingServiceRemote lockingService;

	@EJB
	@IgnoreDependency
	private ProtocolloGenerator protocolloGenerator;

	@Override
	public byte[] generaEtichette(AreaMagazzino areaMagazzino, ParametriCreazioneEtichette parametriCreazioneEtichette)
			throws FileCreationException, SpedizioniVettoreException {

		// carico il vettore per avere in linea tutti i dati spedizione e l'azienda
		Vettore vettore;
		AziendaAnagraficaDTO azienda;
		try {
			vettore = (Vettore) entitaManager.caricaEntita(areaMagazzino.getVettore(), false);
			azienda = aziendeManager.caricaAziendaAnagrafica(getPrincipal().getCodiceAzienda());
		} catch (AnagraficaServiceException e) {
			throw new RuntimeException(e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("--> Applico i parametri di creazione delle etichette all'area magazzino.");
		}
		areaMagazzino = areaMagazzinoManager.caricaAreaMagazzino(areaMagazzino);

		Cap capEntita = areaMagazzino.getDocumento().getSedeEntita().getSede().getDatiGeografici().getCap();
		Cap capAzienda = azienda.getSedeAzienda().getSede().getDatiGeografici().getCap();
		if (capAzienda == null || capAzienda.getDescrizione().isEmpty() || capEntita == null
				|| capEntita.getDescrizione().isEmpty()) {
			throw new SpedizioniVettoreException("Cap assente per l'entità del documento selezionato.");
		}

		areaMagazzino.applyParametriCreazioneEtichette(parametriCreazioneEtichette);
		try {
			areaMagazzino = areaMagazzinoManager.salvaAreaMagazzino(areaMagazzino, false);
		} catch (Exception e) {
			// faccio il catch della eccezione generica perchè non mi verrà mai rilanciata una documento duplicato,
			// documento assente, ecc....
			throw new RuntimeException("Errore durante il salvataggio dell'area magazzino.", e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("--> Creo le aree magazzino spedizione da scrivere nel file.");
		}
		List<AreaMagazzinoSpedizione> aree = new ArrayList<AreaMagazzinoSpedizione>();
		Integer numeroColli = parametriCreazioneEtichette.getNumeroColli();
		for (int i = 0; i < numeroColli; i++) {
			Integer numeroSegnacollo = getNumeroSegnacollo(vettore.getDatiSpedizioni().getNumeratore());
			AreaMagazzinoSpedizione areaSped = new AreaMagazzinoSpedizione(areaMagazzino, vettore, numeroSegnacollo,
					i + 1, azienda);
			aree.add(areaSped);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("--> Carico il template per la generazione del file etichette.");
		}
		StreamFactory factory = StreamFactory.newInstance();
		factory.load(getFilePathForTemplateEtichette(vettore));
		ByteArrayOutputStream osWriter = new ByteArrayOutputStream();
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(osWriter));
		BeanWriter out = factory.createWriter("etichetteIn", writer);

		for (AreaMagazzinoSpedizione areaSped : aree) {
			if (logger.isDebugEnabled()) {
				logger.debug("--> Scrivo area magazzino spedizione con ID " + areaSped.getAreaMagazzino().getId());
			}
			out.write(areaSped);
		}
		out.flush();
		out.close();

		return osWriter.toByteArray();
	}

	@Override
	public byte[] generaRendicontazione(List<AreaMagazzinoRicerca> areeMagazzinoRicerca, Vettore vettore)
			throws SpedizioniVettoreException, FileCreationException {

		if (areeMagazzinoRicerca == null || areeMagazzinoRicerca.isEmpty()) {
			throw new SpedizioniVettoreException("Selezionare almeno un documento da rendicontare.");
		}

		AziendaAnagraficaDTO azienda;
		try {
			azienda = aziendeManager.caricaAziendaAnagrafica(getPrincipal().getCodiceAzienda());
		} catch (AnagraficaServiceException e) {
			throw new RuntimeException(e);
		}

		StreamFactory factory = StreamFactory.newInstance();
		factory.load(getFilePathForTemplateRendicontazione(vettore));
		ByteArrayOutputStream osWriter = new ByteArrayOutputStream();
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(osWriter));
		BeanWriter out = factory.createWriter("rendicontazione", writer);

		if (logger.isDebugEnabled()) {
			logger.debug("--> Rilascio tutti gli eventuali lock precendenti della generazione rendicondazione.");
		}
		lockingService.releaseByUser(USER_LOCK_RENDICONTAZIONE);

		Collection<ILock> locks = new ArrayList<ILock>(lockingService.getLockAll());

		for (AreaMagazzinoRicerca areaMagazzinoRicerca : areeMagazzinoRicerca) {

			AreaMagazzino areaMagazzino;
			try {
				areaMagazzino = panjeaDAO.load(AreaMagazzino.class, areaMagazzinoRicerca.getIdAreaMagazzino());
			} catch (Exception e) {
				logger.error(
						"--> Errore durante il caricamento dell'area magazzino con ID = "
								+ areaMagazzinoRicerca.getIdAreaMagazzino(), e);
				throw new SpedizioniVettoreException("Errore durante la generazione del file di rendicontazione.", e);
			}

			if (logger.isDebugEnabled()) {
				logger.debug("--> Rimuovo se esiste il lock all'area magazzino e la blocco con l'utente della rendicontazione.");
			}
			// mi costruisco la chiave del lock
			String keyArea = getPrincipal().getCodiceAzienda() + AreaMagazzino.class.getName()
					+ areaMagazzino.getId().toString();
			for (ILock lock : locks) {
				if (lock.getKeyLock().equals(keyArea)) {
					try {
						lockingService.release(lock);
					} catch (Exception e) {
						// non faccio niente perchè il lock sono sicuro che esiste
						throw new RuntimeException("Lock non trovato");
					}
					break;
				}
			}
			try {
				lockingService.lock(USER_LOCK_RENDICONTAZIONE, getPrincipal().getCodiceAzienda(),
						AreaMagazzino.class.getName(), areaMagazzino.getId(), areaMagazzino.getVersion());
			} catch (Exception e) {
				// faccio il cacth dell'exception perchè sò che il lock non esiste perchè l'ho tolto prima e perchè non
				// potrà esserci una stale in quanto ho appena ricaricato l'area
				throw new RuntimeException("Errore durante il lock del documento.");
			}

			if (logger.isDebugEnabled()) {
				logger.debug("--> Scrivo sul file di rendicontazione l'area magazzino con ID = "
						+ areaMagazzinoRicerca.getIdAreaMagazzino());
			}
			AreaMagazzinoRendicontazione area = new AreaMagazzinoRendicontazione(azienda, areaMagazzino, vettore);
			out.write(area);
		}
		out.flush();
		out.close();

		return osWriter.toByteArray();
	}

	/**
	 * @param vettore
	 *            vettore di riferimento
	 * @return file di template per l'esportazione delle etichette
	 * @throws FileCreationException
	 *             sollevata se viene generato un errore durante la lettura del file di template
	 * @throws SpedizioniVettoreException
	 *             sollevata se tutti i path di esportazione/template non sono configurati sul vettore
	 */
	private File getFilePathForTemplateEtichette(Vettore vettore) throws FileCreationException,
			SpedizioniVettoreException {
		// Verifico che il vettore abbia impostato tutti dati per le esportazioni.
		if (!vettore.getDatiSpedizioni().isPathFileConfigured()) {
			throw new SpedizioniVettoreException("Dati spedizione sul vettore non corretti.");
		}

		// Costruisco il file
		File file = new File(vettore.getDatiSpedizioni().getPathFileTemplateEtichette());
		if (!file.exists()) {
			logger.error("--> File di template per le etichette macante. Percorso del file " + file.getAbsolutePath());
			throw new FileCreationException("File di template per le etichette macante. Percorso del file "
					+ file.getAbsolutePath());
		}
		return file;
	}

	/**
	 * @param vettore
	 *            vettore di riferimento
	 * @return file di template per la generazione della rendicontazione
	 * @throws FileCreationException
	 *             sollevata se viene generato un errore durante la lettura del file di template
	 * @throws SpedizioniVettoreException
	 *             sollevata se tutti i path di esportazione/template non sono configurati sul vettore
	 */
	private File getFilePathForTemplateRendicontazione(Vettore vettore) throws FileCreationException,
			SpedizioniVettoreException {
		// Verifico che il vettore abbia impostato tutti dati per le esportazioni.
		if (!vettore.getDatiSpedizioni().isPathFileConfigured()) {
			throw new SpedizioniVettoreException("Dati spedizione sul vettore non corretti.");
		}

		// Costruisco il file
		File file = new File(vettore.getDatiSpedizioni().getPathFileTemplateRendiconto());
		if (!file.exists()) {
			logger.error("--> File di template per la rendicontazione macante. Percorso del file "
					+ file.getAbsolutePath());
			throw new FileCreationException("File di template per la rendicontazione macante. Percorso del file "
					+ file.getAbsolutePath());
		}
		return file;
	}

	/**
	 * Recupera il numero segnacollo da un numeratore.
	 * 
	 * @param numeratore
	 *            numeratore da utilizzare
	 * @return numero segnacollo generato
	 * @throws SpedizioniVettoreException
	 *             rilanciata se non esiste il numeratore
	 */
	private Integer getNumeroSegnacollo(String numeratore) throws SpedizioniVettoreException {
		// Recupero il valore del numeratore
		String codice;
		try {
			codice = protocolloGenerator.nextCodice(numeratore, VALUE_MAX);
		} catch (Exception e1) {
			logger.error("-->errore nel recuperare il numeratore per l'esportazione. Numeratore " + numeratore, e1);
			panjeaMessage.send("Numeratore inesistente. Creare il numeratore con nome  " + numeratore,
					PanjeaMessage.MESSAGE_GENERIC_SELECTOR);
			throw new SpedizioniVettoreException("Numeratore inesistente. Creare il numeratore con nome  " + numeratore);
		}
		return new Integer(codice);
	}

	/**
	 * Restituisce il codice azienda dell'utente loggato.
	 * 
	 * @return Codice azienda
	 */
	private JecPrincipal getPrincipal() {
		return (JecPrincipal) sessionContext.getCallerPrincipal();
	}

	@Override
	public void leggiRisultatiEtichette(AreaMagazzino areaMagazzino, byte[] data) throws SpedizioniVettoreException,
			FileCreationException {

		// carico il vettore per avere in linea tutti i dati spedizione e l'azienda
		Vettore vettore;
		try {
			vettore = (Vettore) entitaManager.caricaEntita(areaMagazzino.getVettore(), false);
		} catch (AnagraficaServiceException e) {
			throw new RuntimeException(e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("--> Carico l'area magazzino da aggiornare.");
		}
		areaMagazzino = areaMagazzinoManager.caricaAreaMagazzino(areaMagazzino);

		if (logger.isDebugEnabled()) {
			logger.debug("--> Rimuovo i precedenti segnacolli dall'area magazzino");
		}
		areaMagazzino.getSegnacolli().clear();

		StreamFactory factory = StreamFactory.newInstance();
		factory.load(getFilePathForTemplateEtichette(vettore));

		Reader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(data)));
		BeanReader beanReader = factory.createReader("etichetteIn", reader);

		AreaMagazzinoSpedizione areaMagazzinoSpedizione = null;
		try {
			while ((areaMagazzinoSpedizione = (AreaMagazzinoSpedizione) beanReader.read()) != null) {

				if (!areaMagazzinoSpedizione.getMessaggioErrore().isEmpty()) {
					throw new SpedizioniVettoreException(
							"Errore durante il recupero del file delle etichette. \n Messaggio di errore: "
									+ areaMagazzinoSpedizione.getMessaggioErrore());
				}

				if (logger.isDebugEnabled()) {
					logger.debug("--> Creo il segnacollo " + areaMagazzinoSpedizione.getNumeroSegnacollo());
				}
				Segnacollo segnacollo = new Segnacollo(areaMagazzinoSpedizione);
				areaMagazzino.getSegnacolli().add(segnacollo);
			}

			areaMagazzinoManager.salvaAreaMagazzino(areaMagazzino, false);
		} catch (Exception e) {
			// faccio il catch della eccezione generica perchè non mi verrà mai rilanciata una documento duplicato,
			// documento assente, ecc....
			throw new RuntimeException("Errore durante il salvataggio dell'area magazzino.", e);
		}
	}

	@Override
	public void rendicontaAreeMagazzino(List<AreaMagazzinoRicerca> areeMagazzino) throws SpedizioniVettoreException {

		for (AreaMagazzinoRicerca areaMagazzinoRicerca : areeMagazzino) {

			AreaMagazzino area;
			try {
				area = panjeaDAO.load(AreaMagazzino.class, areaMagazzinoRicerca.getIdAreaMagazzino());

				area.getDatiSpedizioniDocumento().setRendicontatoAlVettore(true);

				panjeaDAO.save(area);
			} catch (Exception e) {
				logger.error(
						"-->errore durante il salvataggio dell'area magazzino ID = "
								+ areaMagazzinoRicerca.getIdAreaMagazzino(), e);
				throw new SpedizioniVettoreException(
						"Errore durante l'aggiornamento della rendicontazione delle spedizioni.", e);
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("--> Rilascio tutti i lock per le aree magazzino rendicontate.");
		}
		lockingService.releaseByUser(USER_LOCK_RENDICONTAZIONE);
	}

}
