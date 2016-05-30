package it.eurotn.panjea.aton.importer.manager;

import it.eurotn.dao.exception.DuplicateKeyObjectException;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentoDuplicateException;
import it.eurotn.panjea.anagrafica.domain.Preference;
import it.eurotn.panjea.aton.domain.OrdiniImportati;
import it.eurotn.panjea.aton.importer.manager.interfaces.ImportaOrdini;
import it.eurotn.panjea.exporter.exception.FileCreationException;
import it.eurotn.panjea.exporter.exception.ImportException;
import it.eurotn.panjea.ordini.domain.NotaOrdineImportata;
import it.eurotn.panjea.ordini.domain.OrdineImportato;
import it.eurotn.panjea.ordini.domain.OrdineImportato.EProvenienza;
import it.eurotn.panjea.ordini.domain.RigaOrdineImportata;
import it.eurotn.panjea.ordini.manager.interfaces.ImportazioneOrdiniManager;
import it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaOrdiniImportati;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.service.interfaces.PanjeaMessage;

import java.io.File;
import java.io.FileNotFoundException;
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
import org.beanio.BeanIOException;
import org.beanio.BeanReader;
import org.beanio.BeanReaderErrorHandler;
import org.beanio.BeanReaderException;
import org.beanio.BeanReaderIOException;
import org.beanio.StreamFactory;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.ejb.TransactionTimeout;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.AtonImportaOrdini")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AtonImportaOrdini")
public class ImportaOrdiniBean implements ImportaOrdini {

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
			throw new RuntimeException(ex);
		}
	}

	private static Logger logger = Logger.getLogger(ImportaOrdiniBean.class);

	@EJB
	private PanjeaDAO panjeaDAO;

	@EJB
	private PanjeaMessage panjeaMessage;

	@EJB
	private ImportazioneOrdiniManager importazioneOrdiniManager;

	public static final String TESTATE_FILE_NAME = "TESTAT";
	public static final String RIGHE_FILE_NAME = "RIGHE";
	public static final String NOTE_FILE_NAME = "NOTE";

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public String getAtonImportDir() {
		// Recupero la cartelll di importazione
		javax.persistence.Query query = panjeaDAO.prepareNamedQuery("Preference.caricaPerChiave");
		query.setParameter("paramChiave", "atonDirImport");
		Preference preference = null;
		String result = "";
		try {
			preference = (Preference) panjeaDAO.getSingleResult(query);
			result = preference.getValore();
		} catch (NoResultException pnf) {
			logger.error("--> preferenza non trovata: atonDirImport", pnf);
			panjeaMessage.send("Impostare nelle preferenze globali la chiave atonDirImport",
					PanjeaMessage.MESSAGE_GENERIC_SELECTOR);
		} catch (Exception e) {
			logger.error("--> Errore ricerca preference con key atonDirImport", e);
			throw new RuntimeException("Errore ricerca preference con key atonDirImport", e);
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
			query.setParameter("paramChiave", "atonDirTemplate");
			preference = (Preference) panjeaDAO.getSingleResult(query);
		} catch (Exception e) {
			logger.error("--> Errore ricerca preference con key atonDirTemplate", e);
			throw new FileCreationException("Nelle preferenze generali manca la chiave atonDirTemplate");
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
	public List<OrdineImportato> importa(String pathFile) throws ImportException {

		OrdiniImportati ordiniImportati = importaTestateOrdini(pathFile);
		pathFile = pathFile.replace("TESTAT", "RIGHE");
		ordiniImportati = importaRighe(ordiniImportati, pathFile);

		pathFile = pathFile.replace("RIGHE", "NOTE");
		ordiniImportati = importaNote(ordiniImportati, pathFile);

		List<OrdineImportato> ordiniDaCalcolare = new ArrayList<OrdineImportato>();

		for (OrdineImportato ordineImportato : ordiniImportati.getOrdini()) {
			try {
				OrdineImportato ordineDaCalcolare = panjeaDAO.save(ordineImportato);
				ordiniDaCalcolare.add(ordineDaCalcolare);
			} catch (DuplicateKeyObjectException e) {
				// Ordine duplicato, riutilizzo la DocumentoDuplicato exception
				// creando un documento fake
				Documento documento = new Documento();
				documento.getCodice().setCodice(ordineImportato.getNumero());
				throw new RuntimeException(new DocumentoDuplicateException("Documento duplicato ", documento));
			} catch (Exception e) {
				logger.error("-->errore nell'importare l'ordine " + ordineImportato, e);
				throw new RuntimeException(e);
			}
		}
		ParametriRicercaOrdiniImportati parametri = new ParametriRicercaOrdiniImportati();
		parametri.setProvenienza(EProvenienza.ATON);
		importazioneOrdiniManager.aggiornaRigheOrdineImportate(parametri);
		return ordiniDaCalcolare;
	}

	/**
	 * 
	 * @param ordiniImportati
	 *            ordini con le testate
	 * @param pathFile
	 *            file con le note
	 * @return ordini e righe ordine con le note associate
	 * @throws ImportException
	 *             eccezione su importazione
	 */
	private OrdiniImportati importaNote(OrdiniImportati ordiniImportati, String pathFile) throws ImportException {

		try {
			File fileNote = new File(pathFile);
			StreamFactory factoryNota = StreamFactory.newInstance();
			factoryNota.load(getFilePathForTemplate(NOTE_FILE_NAME));
			BeanReader inNota = factoryNota.createReader("note", fileNote);
			inNota.setErrorHandler(new FileReaderErrorHandler());

			NotaOrdineImportata nota;
			while ((nota = (NotaOrdineImportata) inNota.read()) != null) {
				ordiniImportati.aggiungiNota(nota);
			}
			inNota.close();
		} catch (FileCreationException ex) {
			throw new RuntimeException(ex);
		} catch (BeanReaderIOException e) {
			throw new ImportException(e, pathFile);
		} catch (BeanIOException e) {
			if (e.getCause() instanceof FileNotFoundException) {
				if (logger.isDebugEnabled()) {
					logger.debug("--> File delle note non presente, continuo... file delle note cercato:  " + pathFile);
				}
			}
		}
		return ordiniImportati;
	}

	/**
	 * 
	 * @param ordiniImportati
	 *            ordini con le testate
	 * @param pathFile
	 *            file con le righe
	 * @return ordini con le righe importate dal file
	 * @throws ImportException
	 *             eccezione su importazione
	 */
	private OrdiniImportati importaRighe(OrdiniImportati ordiniImportati, String pathFile) throws ImportException {
		try {
			File fileRighe = new File(pathFile);
			StreamFactory factoryRiga = StreamFactory.newInstance();
			factoryRiga.load(getFilePathForTemplate(RIGHE_FILE_NAME));
			BeanReader inRiga = factoryRiga.createReader("righe", fileRighe);
			inRiga.setErrorHandler(new FileReaderErrorHandler());

			RigaOrdineImportata riga;
			while ((riga = (RigaOrdineImportata) inRiga.read()) != null) {
				ordiniImportati.aggiungiRiga(riga);
			}
			inRiga.close();
		} catch (FileCreationException ex) {
			throw new RuntimeException(ex);
		} catch (BeanReaderIOException e) {
			throw new ImportException(e, pathFile);
		}
		return ordiniImportati;
	}

	/**
	 * 
	 * @param pathFile
	 *            path della cartella contenente i file di aton
	 * @return lista degli ordini importati con le testate
	 * @throws ImportException
	 *             rilanciata se ci sono problemi nella struttura del file.
	 */
	private OrdiniImportati importaTestateOrdini(String pathFile) throws ImportException {
		OrdiniImportati ordiniImportati = new OrdiniImportati();
		File fileTestata = new File(pathFile);
		try {
			StreamFactory factory = StreamFactory.newInstance();
			factory.load(getFilePathForTemplate(TESTATE_FILE_NAME));
			BeanReader in = factory.createReader("testate", fileTestata);
			in.setErrorHandler(new FileReaderErrorHandler());

			OrdineImportato ordine;
			while ((ordine = (OrdineImportato) in.read()) != null) {
				ordiniImportati.aggiungiTestata(ordine);
			}
			in.close();
		} catch (FileCreationException ex) {
			throw new RuntimeException(ex);
		} catch (BeanReaderIOException e) {
			throw new ImportException(e, fileTestata.getName());
		} catch (BeanIOException e) {
			throw new ImportException(e, fileTestata.getName());
		}
		return ordiniImportati;
	}
}
