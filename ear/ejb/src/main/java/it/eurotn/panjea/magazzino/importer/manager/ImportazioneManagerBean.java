/**
 * 
 */
package it.eurotn.panjea.magazzino.importer.manager;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.manager.interfaces.EntitaManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.SediEntitaManager;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;
import it.eurotn.panjea.exporter.exception.ImportException;
import it.eurotn.panjea.magazzino.domain.DatiGenerazione;
import it.eurotn.panjea.magazzino.domain.DatiGenerazione.TipoGenerazione;
import it.eurotn.panjea.magazzino.importer.domain.AbstractBeanIOImporter;
import it.eurotn.panjea.magazzino.importer.domain.AbstractImporter;
import it.eurotn.panjea.magazzino.importer.manager.interfaces.GenerazioneImportazioneManager;
import it.eurotn.panjea.magazzino.importer.manager.interfaces.ImportazioneManager;
import it.eurotn.panjea.magazzino.importer.util.DocumentoImport;
import it.eurotn.panjea.magazzino.importer.util.RigaImport;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoManager;
import it.eurotn.panjea.magazzino.manager.interfaces.SediMagazzinoManager;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;
import it.eurotn.panjea.magazzino.util.ParametriRicercaAreaMagazzino;
import it.eurotn.panjea.magazzino.util.SedeAreaMagazzinoDTO;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * @author fattazzo
 * 
 */
@Stateless(name = "Panjea.ImportazioneManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.ImportazioneManager")
public class ImportazioneManagerBean implements ImportazioneManager {

	private static Logger logger = Logger.getLogger(ImportazioneManagerBean.class);

	@Resource
	private SessionContext context;

	@EJB
	private PanjeaDAO panjeaDAO;

	@EJB
	private EntitaManager entitaManager;

	@EJB
	private SediEntitaManager sediEntitaManager;

	@EJB
	private SediMagazzinoManager sediMagazzinoManager;

	@EJB
	private GenerazioneImportazioneManager generazioneImportazioneManager;

	@EJB
	private AreaMagazzinoManager areaMagazzinoManager;

	/**
	 * Aggiorna il documento importato inserendo i link alle tabelle di panjea<br/>
	 * 
	 * @param parametri
	 *            parametri per l'aggiornamento. viene considerata la provenienza degli ordini
	 */
	private void aggiornaDocumento(DocumentoImport documento) {

		if (documento.getTipoAreaMagazzino() != null) {

			// carica l'entita se non c'è l'id e se il codice non è vuoto
			if (documento.getIdEntita() == null && documento.getCodiceEntita() != null) {
				EntitaLite entitaLite = null;

				entitaLite = entitaManager.caricaEntita(documento.getTipoAreaMagazzino().getTipoDocumento()
						.getTipoEntita(), documento.getCodiceEntita());
				documento.setIdEntita(entitaLite != null ? entitaLite.getId() : null);

				// carico la sede dell'entità
				if (entitaLite != null && documento.getIdSede() == null) {
					SedeEntita sedeEntita = null;
					// in questo caso mi comporto diversamente a seconda della presenza della proprietà codiceSede.
					// se non esiste carico la sede principale dell'entità
					if (StringUtils.isBlank(documento.getCodiceSede())) {
						try {
							sedeEntita = sediEntitaManager.caricaSedePrincipaleEntita(entitaLite.creaProxyEntita());
						} catch (AnagraficaServiceException e) {
							sedeEntita = null;
						}
					} else {
						// se esiste carico la sede dato il suo codice
						sedeEntita = sediEntitaManager.caricaSedeEntita(entitaLite.creaProxyEntita(),
								documento.getCodiceSede());
					}
					documento.setIdSede(sedeEntita != null ? sedeEntita.getId() : null);
				}
			}
		}

		// codice articolo - id articolo
		Map<String, Integer> articoliCodiceMap = caricaArticoliByCodice();
		// codice articolo fornitore - id articolo
		Map<String, Integer> articoliCodiceEntitaMap = caricaArticoliByCodiceEntita(documento.getIdEntita());

		for (RigaImport riga : documento.getRighe()) {
			aggiornaRiga(riga, articoliCodiceMap, articoliCodiceEntitaMap);
		}
	}

	private void aggiornaRiga(RigaImport rigaImport, Map<String, Integer> articoliCodiceMap,
			Map<String, Integer> articoliCodiceEntitaMap) {

		if (rigaImport.getIdArticolo() == null) {
			// se è definito il codice articolo lo carico per quello
			if (!StringUtils.isBlank(rigaImport.getCodiceArticolo())) {
				rigaImport.setIdArticolo(articoliCodiceMap.get(rigaImport.getCodiceArticolo()));
			} else if (!StringUtils.isBlank(rigaImport.getCodiceArticoloEntita())) {
				// altrimenti cerco di caricarlo in base al codice entita
				rigaImport.setIdArticolo(articoliCodiceEntitaMap.get(rigaImport.getCodiceArticoloEntita()));
			}
		}
	}

	@SuppressWarnings("unchecked")
	private Map<String, Integer> caricaArticoliByCodice() {
		StringBuilder sb = new StringBuilder(
				"select a.codice,a.id from Articolo a where a.codiceAzienda = :paramCodiceAzienda");

		Query query = panjeaDAO.prepareQuery(sb.toString());
		query.setParameter("paramCodiceAzienda", getCodiceAzienda());

		List<Object[]> rows = null;
		try {
			rows = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			rows = new ArrayList<Object[]>();
		}

		Map<String, Integer> result = new HashMap<String, Integer>();
		for (Object[] row : rows) {
			result.put((String) row[0], (Integer) row[1]);
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Integer> caricaArticoliByCodiceEntita(Integer idEntita) {
		Map<String, Integer> result = new HashMap<String, Integer>();

		if (idEntita != null) {
			StringBuilder sb = new StringBuilder(
					"select cae.codice, cae.articolo.id  from CodiceArticoloEntita cae where cae.entita.id = :paramIdEntita");

			Query query = panjeaDAO.prepareQuery(sb.toString());
			query.setParameter("paramIdEntita", idEntita);

			List<Object[]> rows = null;
			try {
				rows = panjeaDAO.getResultList(query);
			} catch (DAOException e) {
				rows = new ArrayList<Object[]>();
			}

			for (Object[] row : rows) {
				result.put((String) row[0], (Integer) row[1]);
			}
		}

		return result;
	}

	@Override
	public Collection<DocumentoImport> caricaDocumenti(String codiceImporter, byte[] fileImport) {
		logger.debug("--> Enter caricaDocumenti");

		AbstractImporter importer = caricaImporter(codiceImporter);
		// per ora usiamo solo beanIOImporter, cambiare in futuro se si useranno altri tipi
		((AbstractBeanIOImporter) importer).setByteArray(fileImport);

		Collection<DocumentoImport> result = null;
		try {
			result = importer.caricaDocumenti();

			// aggiorno i dati importati
			for (DocumentoImport documentoImport : result) {
				aggiornaDocumento(documentoImport);
				documentoImport.valida();
			}

		} catch (ImportException e) {
			logger.error("-->errore durante il caricamento dei documenti.", e);
			throw new RuntimeException("errore durante il caricamento dei documenti.", e);
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> caricaImporter() {
		logger.debug("--> Enter caricaImporter");
		List<String> importers = new ArrayList<String>();
		Query query = panjeaDAO.prepareNamedQuery("AbstractImporter.caricaAllCodici");

		try {
			importers = panjeaDAO.getResultList(query);
		} catch (Exception e) {
			logger.error("--> errore durante il caricamento degli importer.", e);
			throw new RuntimeException("errore durante il caricamento degli importer.", e);
		}

		logger.debug("--> Exit caricaImporter");
		return importers;
	}

	private AbstractImporter caricaImporter(String codice) {
		logger.debug("--> Enter caricaImporter");
		Query query = panjeaDAO.prepareNamedQuery("AbstractImporter.caricaByCodice");
		query.setParameter("paramCodice", codice);

		AbstractImporter importer = null;
		try {
			importer = (AbstractImporter) panjeaDAO.getSingleResult(query);
		} catch (Exception e) {
			logger.error("--> errore durante il caricamento degli importer.", e);
			throw new RuntimeException("errore durante il caricamento degli importer.", e);
		}

		logger.debug("--> Exit caricaImporter");
		return importer;
	}

	/**
	 * Crea i dati di generazione per l'importazione in corso.
	 * 
	 * @param dataGenerazione
	 *            data di generazione
	 * @param codiceImporter
	 *            codice dell'importer
	 * @return dati utili a raggruppare i documenti di destinazione
	 */
	private DatiGenerazione creaDatiGenerazione(Date dataGenerazione, String codiceImporter) {
		DatiGenerazione datiGenerazione = new DatiGenerazione();
		datiGenerazione.setDataGenerazione(dataGenerazione);
		datiGenerazione.setUtente(getJecPrincipal().getUserName());
		datiGenerazione.setNote("Importazione " + codiceImporter);
		datiGenerazione.setTipoGenerazione(TipoGenerazione.ESTERNO);
		return datiGenerazione;
	}

	/**
	 * 
	 * @return codiceAzienda
	 */
	private String getCodiceAzienda() {
		return ((JecPrincipal) context.getCallerPrincipal()).getCodiceAzienda();
	}

	/**
	 * Restituisce JecPrincipal dal {@link SessionContext}.
	 * 
	 * @return JecPrincipal
	 */
	private JecPrincipal getJecPrincipal() {
		return (JecPrincipal) context.getCallerPrincipal();

	}

	@Override
	public List<AreaMagazzinoRicerca> importaDocumenti(Collection<DocumentoImport> documenti, String codiceImporter) {

		AbstractImporter importer = caricaImporter(codiceImporter);

		// importo una sola entità quindi carico subito la sede.
		SedeEntita sedeEntita = null;
		try {
			sedeEntita = sediEntitaManager.caricaSedeEntita(documenti.iterator().next().getIdSede());
		} catch (AnagraficaServiceException e) {
			e.printStackTrace();
		}
		SedeAreaMagazzinoDTO sedeAreaMagazzinoDTO = areaMagazzinoManager.caricaSedeAreaMagazzinoDTO(sedeEntita);

		// creo i dati generazione da settare alle aree generate
		DatiGenerazione datiGenerazione = creaDatiGenerazione(Calendar.getInstance().getTime(), importer.getCodice());
		for (DocumentoImport documentoImport : documenti) {
			// rivalido il documento per assicurarmi che sia ok
			documentoImport.valida();
			if (!documentoImport.isValid()) {
				continue;
			}

			generazioneImportazioneManager.generaDocumento(documentoImport, sedeEntita, sedeAreaMagazzinoDTO,
					datiGenerazione);
		}

		ParametriRicercaAreaMagazzino parametriRicercaAreaMagazzino = new ParametriRicercaAreaMagazzino();
		// Calendar calendar = Calendar.getInstance();
		// calendar.setTime(dataEvasione);
		// parametriRicercaAreaMagazzino.setAnnoCompetenza(calendar.get(Calendar.YEAR));
		parametriRicercaAreaMagazzino.setDataGenerazione(datiGenerazione.getDataGenerazione());
		parametriRicercaAreaMagazzino.getTipiGenerazione().add(TipoGenerazione.ESTERNO);
		parametriRicercaAreaMagazzino.setEffettuaRicerca(true);

		List<AreaMagazzinoRicerca> areeRicerca = areaMagazzinoManager
				.ricercaAreeMagazzino(parametriRicercaAreaMagazzino);
		return areeRicerca;
	}
}
