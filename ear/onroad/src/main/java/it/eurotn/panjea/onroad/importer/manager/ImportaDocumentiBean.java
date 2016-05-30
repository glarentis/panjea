package it.eurotn.panjea.onroad.importer.manager;

import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.domain.Preference;
import it.eurotn.panjea.exporter.exception.FileCreationException;
import it.eurotn.panjea.exporter.exception.ImportException;
import it.eurotn.panjea.iva.domain.AreaIva;
import it.eurotn.panjea.iva.manager.interfaces.AreaIvaManager;
import it.eurotn.panjea.lotti.exception.RimanenzaLottiNonValidaException;
import it.eurotn.panjea.lotti.exception.RimanenzeLottiNonValideException;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.exception.TotaleDocumentoNonCoerenteException;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.StatiAreaMagazzinoManager;
import it.eurotn.panjea.magazzino.service.exception.QtaLottiMaggioreException;
import it.eurotn.panjea.magazzino.service.interfaces.MagazzinoDocumentoService;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.onroad.domain.DocumentiImportati;
import it.eurotn.panjea.onroad.domain.DocumentoOnRoad;
import it.eurotn.panjea.onroad.domain.RigaDocumentoOnroad;
import it.eurotn.panjea.onroad.domain.RigaIvaOnRoad;
import it.eurotn.panjea.onroad.domain.wrapper.DocumentiOnRoad;
import it.eurotn.panjea.onroad.importer.manager.interfaces.ImportaDocumenti;
import it.eurotn.panjea.onroad.importer.manager.interfaces.OnroadDocumentoTransformer;
import it.eurotn.panjea.onroad.importer.manager.interfaces.OnroadRigaIvaTransformer;
import it.eurotn.panjea.onroad.importer.manager.interfaces.OnroadRigaTransformer;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rate.manager.interfaces.AreaRateManager;
import it.eurotn.panjea.rate.manager.interfaces.RateGenerator;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.service.interfaces.PanjeaMessage;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

@Stateless(name = "Panjea.OnRoadImportaDocumenti")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.OnRoadImportaDocumenti")
public class ImportaDocumentiBean implements ImportaDocumenti {

	private class FileReaderErrorHandler implements BeanReaderErrorHandler {

		/**
		 * Costruttore.
		 */
		public FileReaderErrorHandler() {
			super();
		}

		@Override
		public void handleError(BeanReaderException ex) throws Exception {
			if (ex.getRecordContext().getRecordText().getBytes().length == 1
					&& ex.getRecordContext().getRecordText().getBytes()[0] == 26) {
				// salto perchè i file di aton finiscono sempre con una riga che contiene un crattere identificato
				// da 26
				if (logger.isDebugEnabled()) {
					logger.debug("--> Riga non valida ( carattere ascii di chiusura file ), salto");
				}
				return;
			}
			logger.error("-->errore nel leggere il record del file alla riga " + ex.getRecordContext().getRecordText(),
					ex);
			throw new RuntimeException(ex.getCause());
		}
	}

	private static Logger logger = Logger.getLogger(ImportaDocumentiBean.class);

	@EJB
	private PanjeaDAO panjeaDAO;

	@EJB
	private PanjeaMessage panjeaMessage;

	@EJB
	private OnroadDocumentoTransformer documentoOnroadTransformer;

	@EJB
	private OnroadRigaTransformer onroadRigaTransformer;

	@EJB
	private OnroadRigaIvaTransformer onroadRigaIvaTransformer;

	@EJB
	private AreaIvaManager areaIvaManager;

	@EJB
	protected AreaRateManager areaRateManager;

	@EJB
	protected RateGenerator rateGenerator;

	@EJB
	private MagazzinoDocumentoService magazzinoDocumentoService;

	@EJB
	protected StatiAreaMagazzinoManager statiAreaMagazzinoManager;

	public static final String TESTATE_FILE_NAME = "TESTAT";
	public static final String RIGHE_FILE_NAME = "RIGHE";
	public static final String RIGHE_IVA_FILE_NAME = "ALIQUO";

	/**
	 * @param codiceAgente
	 *            il codice dell'agente da cercare
	 * @return AgenteLite con il codice specificato
	 */
	private AgenteLite caricaAgente(String codiceAgente) {
		AgenteLite agente = null;
		try {
			Query queryAgente = panjeaDAO.prepareQuery("select a from AgenteLite a where a.codice=:paramCodiceAgente");
			queryAgente.setParameter("paramCodiceAgente", new Integer(codiceAgente));
			agente = (AgenteLite) panjeaDAO.getSingleResult(queryAgente);
		} catch (ObjectNotFoundException e) {
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return agente;
	}

	/**
	 * @return Map<String, String> la mappa di codice tipo documento onroad e il rispettivo codice tipo documento panjea
	 */
	private Map<String, String> caricaConversioniTipiDocumentoOnRoadToPanjea() {
		// "FT=TVFA#NOT=DDTTV#NVA=DDTTV#NOT=DDTTV#DDT=CFTV#CT{NUMEROFORNITORE}=DDTTV"
		String conversione = null;
		Preference preference = null;
		try {
			Query query = panjeaDAO.prepareNamedQuery("Preference.caricaPerChiave");
			query.setParameter("paramChiave", "onRoadConversioneTipiDocumento");
			preference = (Preference) panjeaDAO.getSingleResult(query);
			conversione = preference.getValore();
		} catch (ObjectNotFoundException pnf) {
			logger.warn("--> preferenza non trovata: onRoadConversioneTipiDocumento");
			panjeaMessage
					.send("Impostare nelle preferenze globali la chiave onRoadConversioneTipiDocumento tdonRoad1=tdpanjea1#tdonRoad2=tdpanjea2",
							PanjeaMessage.MESSAGE_GENERIC_SELECTOR);
		} catch (Exception e) {
			logger.error("--> Chiave con key onRoadConversioneTipiDocumento mancante", e);
			throw new RuntimeException("Errore ricerca preference con key onRoadDirImport", e);
		}

		Map<String, String> conversioniOnroadToPanjea = new HashMap<String, String>();
		if (conversione != null) {
			String[] tipidocOnRoadToPanjea = conversione.split("#");
			for (String tipoDocOnRoadToPanjea : tipidocOnRoadToPanjea) {
				String[] conv = tipoDocOnRoadToPanjea.split("=");
				conversioniOnroadToPanjea.put(conv[0], conv[1]);
			}
		}
		return conversioniOnroadToPanjea;
	}

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
	public DocumentiOnRoad importa(String pathFile) throws ImportException {

		// costruisce Documenti OnRoad dai file TESTAT,RIGHE,ALIQUO
		DocumentiImportati documentiImportati = importaTestateDocumenti(pathFile);
		pathFile = pathFile.replace("TESTAT", "RIGHE");
		documentiImportati = importaRighe(documentiImportati, pathFile);

		pathFile = pathFile.replace("RIGHE", "ALIQUO");
		documentiImportati = importaRigheIva(documentiImportati, pathFile);

		// i documenti con le righe documento e righe iva associate
		Collection<DocumentoOnRoad> documenti = documentiImportati.getDocumenti();

		// la mappa con i rispettivi tipi documento onroad/panjea
		Map<String, String> conversioniTipiDocumentoOnRoadToPanjea = caricaConversioniTipiDocumentoOnRoadToPanjea();
		if (conversioniTipiDocumentoOnRoadToPanjea.isEmpty()) {
			return null;
		}

		DocumentiOnRoad documentiOnRoad = new DocumentiOnRoad();
		RimanenzeLottiNonValideException importaDocumentiException = new RimanenzeLottiNonValideException();

		// per ogni documento onroad creo una area magazzino
		for (DocumentoOnRoad documentoOnRoad : documenti) {
			AreaMagazzinoFullDTO areaMagazzinoFullDTO = documentoOnroadTransformer.trasforma(documentoOnRoad,
					conversioniTipiDocumentoOnRoadToPanjea);

			if (areaMagazzinoFullDTO == null) {
				continue;
			}

			// l'agente mi serve per la riga articolo
			AgenteLite agente = caricaAgente(documentoOnRoad.getCodiceUtente());

			// righe articolo con lotti, se le quantita' non sono corrette devo avvisare l'utente con la lista di tutte
			// le righe e devo annullare il salvataggio delle aree magazzino
			List<RigaDocumentoOnroad> righeOnRoad = documentoOnRoad.getRighe();
			for (RigaDocumentoOnroad rigaDocumentoOnroad : righeOnRoad) {
				try {
					onroadRigaTransformer.trasforma(rigaDocumentoOnroad, areaMagazzinoFullDTO.getAreaMagazzino(),
							agente);
				} catch (QtaLottiMaggioreException e) {
					System.out.println("QtaLottiMaggioreException");
					// importaDocumentiException.addRimanenzaNonValida(e);
				} catch (RimanenzaLottiNonValidaException e) {
					RigaArticolo ra = e.getRigaArticolo();
					Double qta = ra.getQta();
					System.out.println(qta);
					System.out.println("RimanenzaLottiNonValidaException");
					importaDocumentiException.addRimanenzaNonValida(e);
				}
			}

			// righe iva
			List<RigaIvaOnRoad> righeIvaOnRoad = documentoOnRoad.getRigheIva();
			AreaIva areaIva = areaMagazzinoFullDTO.getAreaIva();
			if (righeIvaOnRoad.size() > 0
					&& areaMagazzinoFullDTO.getAreaMagazzino().getDocumento().getTipoDocumento().isRigheIvaEnable()) {
				areaIva.setAreaContabile(null);
				areaIva = areaIvaManager.validaAreaIva(areaIva);

				for (RigaIvaOnRoad rigaIvaOnRoad : righeIvaOnRoad) {
					onroadRigaIvaTransformer.trasforma(rigaIvaOnRoad, areaIva);
				}
			}

			// rate
			if (areaMagazzinoFullDTO.getAreaRate() != null
					&& areaMagazzinoFullDTO.getAreaRate().isGenerazioneRateAllowed()) {
				AreaRate areaConRateGenerate = rateGenerator.generaRate(areaMagazzinoFullDTO.getAreaMagazzino(),
						areaMagazzinoFullDTO.getAreaRate());
				areaMagazzinoFullDTO.setAreaRate(areaConRateGenerate);
				areaRateManager.validaAreaRate(areaMagazzinoFullDTO.getAreaRate(),
						areaMagazzinoFullDTO.getAreaMagazzino());
			}

			// i carichi onroad non hanno prezzi, ricalcolo in base al documento
			// la sincronizzazione con il datawarehouse
			if (documentoOnRoad.getTipoDocumento().equals("DDT")) {
				areaMagazzinoFullDTO = magazzinoDocumentoService.ricalcolaPrezziMagazzino(areaMagazzinoFullDTO.getId());

				// sul carico cmq sia non ho rate e area contabile non forzo lo stato
				try {
					areaMagazzinoFullDTO = magazzinoDocumentoService.validaRigheMagazzino(
							areaMagazzinoFullDTO.getAreaMagazzino(), areaMagazzinoFullDTO.getAreaRate(), false, false);
				} catch (TotaleDocumentoNonCoerenteException e) {
					logger.warn("Totale documento non coerente");
				}
			} else {
				// questa chiamata mi attiva gli interceptor per inserire il documento nel datawarehouse
				statiAreaMagazzinoManager.cambiaStatoDaProvvisorioInConfermato(areaMagazzinoFullDTO.getAreaMagazzino());
			}

			documentiOnRoad.addDocumento(areaMagazzinoFullDTO, documentoOnRoad.getTipoDocumento());
		}

		if (!importaDocumentiException.isEmpty()) {
			throw importaDocumentiException;
		}
		return documentiOnRoad;
	}

	/**
	 * 
	 * @param documentiImportati
	 *            ordini con le testate
	 * @param pathFile
	 *            file con le righe
	 * @return ordini con le righe importate dal file
	 * @throws ImportException
	 *             eccezione su importazione
	 */
	private DocumentiImportati importaRighe(DocumentiImportati documentiImportati, String pathFile)
			throws ImportException {
		try {
			File fileRighe = new File(pathFile);
			StreamFactory factoryRiga = StreamFactory.newInstance();
			factoryRiga.load(getFilePathForTemplate(RIGHE_FILE_NAME));
			BeanReader inRiga = factoryRiga.createReader("righe", fileRighe);
			inRiga.setErrorHandler(new FileReaderErrorHandler());

			RigaDocumentoOnroad riga;
			while ((riga = (RigaDocumentoOnroad) inRiga.read()) != null) {
				documentiImportati.aggiungiRigaDocumento(riga);
			}
			inRiga.close();
		} catch (FileCreationException ex) {
			throw new RuntimeException(ex);
		} catch (BeanReaderIOException e) {
			throw new ImportException(e, pathFile);
		}
		return documentiImportati;
	}

	/**
	 * 
	 * @param documentiImportati
	 *            ordini con le testate
	 * @param pathFile
	 *            file con le note
	 * @return ordini e righe ordine con le note associate
	 * @throws ImportException
	 *             eccezione su importazione
	 */
	private DocumentiImportati importaRigheIva(DocumentiImportati documentiImportati, String pathFile)
			throws ImportException {
		try {
			File fileRigheIva = new File(pathFile);
			if (fileRigheIva.exists()) {
				StreamFactory factoryRiga = StreamFactory.newInstance();
				factoryRiga.load(getFilePathForTemplate(RIGHE_IVA_FILE_NAME));
				BeanReader inRiga = factoryRiga.createReader("aliquo", fileRigheIva);
				inRiga.setErrorHandler(new FileReaderErrorHandler());

				RigaIvaOnRoad rigaIva;
				while ((rigaIva = (RigaIvaOnRoad) inRiga.read()) != null) {
					documentiImportati.aggiungiRigaIvaDocumento(rigaIva);
				}
				inRiga.close();
			}
		} catch (FileCreationException ex) {
			throw new RuntimeException(ex);
		} catch (BeanReaderIOException e) {
			throw new ImportException(e, pathFile);
		}
		return documentiImportati;
	}

	/**
	 * 
	 * @param pathFile
	 *            path della cartella contenente i file di aton
	 * @return lista degli ordini importati con le testate
	 * @throws ImportException
	 *             rilanciata se ci sono problemi nella struttura del file.
	 */
	private DocumentiImportati importaTestateDocumenti(String pathFile) throws ImportException {
		DocumentiImportati documentiImportati = new DocumentiImportati();
		File fileTestata = new File(pathFile);
		try {
			StreamFactory factory = StreamFactory.newInstance();
			factory.load(getFilePathForTemplate(TESTATE_FILE_NAME));
			BeanReader in = factory.createReader("testate", fileTestata);
			in.setErrorHandler(new FileReaderErrorHandler());

			DocumentoOnRoad ordine;
			while ((ordine = (DocumentoOnRoad) in.read()) != null) {
				documentiImportati.aggiungiTestata(ordine);
			}
			in.close();
		} catch (FileCreationException ex) {
			throw new RuntimeException(ex);
		} catch (BeanReaderIOException e) {
			throw new ImportException(e, fileTestata.getName());
		}
		return documentiImportati;
	}
}
