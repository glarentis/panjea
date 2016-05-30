package it.eurotn.panjea.cosaro.sync.exporter;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.Preference;
import it.eurotn.panjea.exporter.exception.FileCreationException;
import it.eurotn.panjea.lotti.domain.RigaLotto;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.service.interfaces.PanjeaMessage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.persistence.Query;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.beanio.BeanWriter;
import org.beanio.StreamFactory;

public abstract class AbstractDataDocumentoCosaroExporter {

	protected enum CosaroExporterAction {
		WRITE, DELETE
	}

	public static final String COSARO_DIR_TEMPLATE = "cosaroDirTemplate";

	public static final String COSARO_DIR_EXPORT = "cosaroDirExport";

	public static final String COSARO_EXPORT_TIPI_DOCUMENTO_LIST = "cosaroExportTipiDocumentoList";

	private static Logger logger = Logger.getLogger(AbstractDataDocumentoCosaroExporter.class);

	@EJB
	protected PanjeaDAO panjeaDAO;

	@EJB
	private PanjeaMessage panjeaMessage;

	/**
	 * 
	 * @param areaMagazzino
	 *            areaMagazzino
	 * @throws FileCreationException
	 *             FileCreationException
	 */
	public void esporta(AreaMagazzino areaMagazzino) throws FileCreationException {
		if (getAction().equals(CosaroExporterAction.WRITE)) {
			areaMagazzino = panjeaDAO.loadLazy(AreaMagazzino.class, areaMagazzino.getId());
		}
		List<? extends RigaMagazzino> righeMagazzino = areaMagazzino.getRigheMagazzino();
		if (getAction().equals(CosaroExporterAction.WRITE) && (righeMagazzino == null || righeMagazzino.isEmpty())) {
			return;
		}
		TipoDocumento tipoDocumento = areaMagazzino.getDocumento().getTipoDocumento();

		String fileName = getFileName(areaMagazzino);
		// estensione: carico (CAR) o vendita (VEN) a seconda del tipo documento del documento, se non viene trovato il
		// tipo documento non esporto nulla
		String extension = getFileExtension(tipoDocumento.getCodice());
		if (extension == null) {
			return;
		}

		// prima scrivo la testata
		StreamFactory factory = StreamFactory.newInstance();
		File fileTracciatoDocumento = getFilePathForTemplate("DOCUMENTO.xml");

		if (fileTracciatoDocumento == null) {
			return;
		}
		factory.load(fileTracciatoDocumento);

		File fileTmpForExport = null;
		try {
			fileTmpForExport = File.createTempFile("tmp", "." + extension);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		BeanWriter out = factory.createWriter("documento", fileTmpForExport);

		out.write(areaMagazzino);
		if (getAction().equals(CosaroExporterAction.WRITE)) {
			writeRigheMagazzino(areaMagazzino, out);
		}

		out.flush();
		out.close();

		File fileForExport = getFileForExport(fileName + "." + extension);
		if (fileForExport == null) {
			return;
		}
		try {
			if (fileForExport.exists()) {
				fileForExport.delete();
			}
			// chiamando fileTmpForExport.renameTo(fileForExport)mi viene restituito false, uso FileUtils che funziona e
			// mi restituisce una eccezione nel caso in cui non riesca a muovere il file
			FileUtils.moveFile(fileTmpForExport, fileForExport);
		} catch (IOException e) {
			logger.error("--> errore nello spostare il file provvisorio nella cosaroDirExport= ", e);
			throw new RuntimeException("--> errore nello spostare il file provvisorio nella cosaroDirExport= ", e);
		}
	}

	/**
	 * @return CosaroExporterAction associata all'implementazione corrente
	 */
	protected abstract CosaroExporterAction getAction();

	/**
	 * Restituisce l'estensione per il file a seconda del tipo documento.<br>
	 * La lista di documenti è definita CAR=TD1,TD2,TD3,...#VEN=TD4,TD5,TD6,...<br>
	 * 
	 * @param codiceTipoDocumento
	 *            tipoDocumento
	 * @return estensione, CAR o VEN
	 * @throws FileCreationException
	 *             FileCreationException
	 */
	protected String getFileExtension(String codiceTipoDocumento) throws FileCreationException {
		// Recupero la cartella dove sono i file di template
		Preference preference = null;
		try {
			Query query = panjeaDAO.prepareNamedQuery("Preference.caricaPerChiave");
			query.setParameter("paramChiave", COSARO_EXPORT_TIPI_DOCUMENTO_LIST);
			preference = (Preference) panjeaDAO.getSingleResult(query);
		} catch (Exception e) {
			logger.error("--> Errore ricerca preference con key cosaroExportTipiDocumentoList", e);
			panjeaMessage.send("Nelle preferenze generali manca la chiave cosaroExportTipiDocumentoList",
					PanjeaMessage.MESSAGE_GENERIC_SELECTOR);
			return null;
		}

		// la lista di documenti è definita CAR=TD1,TD2,TD3,...#VEN=TD4,TD5,TD6,...
		String tipiDoc = preference.getValore();
		String[] tipi = tipiDoc.split("#");
		String tipiCarico = tipi[0];
		tipiCarico = tipiCarico.replaceAll("CAR=", "");

		String tipiVendita = tipi[1];
		tipiVendita = tipiVendita.replaceAll("VEN=", "");

		String extension = null;
		String[] carichi = tipiCarico.split(",");
		for (String string : carichi) {
			if (codiceTipoDocumento != null && string != null && string.equals(codiceTipoDocumento)) {
				extension = "CAR";
			}
		}

		String[] vendite = tipiVendita.split(",");
		for (String string : vendite) {
			if (codiceTipoDocumento != null && string != null && string.equals(codiceTipoDocumento)) {
				extension = "VEN";
			}
		}

		return extension;
	}

	/**
	 * Costruisce il nome del file per l'esportazione.<br/>
	 * Il pattern del nome viene caricato dalle preference con chiave GulliverFileName.<br/>
	 * . Viene sostituito $progressivo$ con il valore di un numeratore<br>
	 * . Il numeratore viene preso dai numeratori aziendali e il nome deve essere:gulliver+tipoEsportazione:<br/>
	 * Esempio: il numeratore dell'esportazione delle sedi sarà gulliversedi.<br/>
	 * Il numeratore viene azzerato quando arriva a 999999.
	 * 
	 * @param fileName
	 *            fileName
	 * @return file per l'esportazione
	 * @throws FileCreationException
	 *             rilanciata se non è possibile creare il file per l'esportazione.
	 */
	protected File getFileForExport(String fileName) throws FileCreationException {
		File file = null;

		// Recupero la cartella dove esportare i file
		Preference preference = null;
		try {
			Query query = panjeaDAO.prepareNamedQuery("Preference.caricaPerChiave");
			query.setParameter("paramChiave", COSARO_DIR_EXPORT);
			preference = (Preference) panjeaDAO.getSingleResult(query);
		} catch (Exception e) {
			logger.error("--> Errore ricerca preference con key cosaroDirExport", e);

			panjeaMessage.send("Nelle preferenze generali manca la chiave " + COSARO_DIR_EXPORT,
					PanjeaMessage.MESSAGE_GENERIC_SELECTOR);
			return null;
		}

		// Costruisco il nome del file
		String pathName = preference.getValore() + fileName;
		file = new File(pathName);
		return file;
	}

	/**
	 * Costruisce il file name e lo restituisce; {numeroDocumento}_{codiceEntita}.
	 * 
	 * @param areaMagazzino
	 *            l'area magazzino di cui costruire il fileName per l'export
	 * @return {numeroDocumento}_{codiceEntita}
	 */
	protected String getFileName(AreaMagazzino areaMagazzino) {
		String codiceDocumento = areaMagazzino.getDocumento().getCodice().getCodice();
		String codiceEntita = "";
		if (areaMagazzino.getDocumento().getEntita() != null) {
			codiceEntita = "_" + areaMagazzino.getDocumento().getEntita().getCodice();
		}
		String fileName = codiceDocumento + codiceEntita;
		return fileName;
	}

	/**
	 * @param fileName
	 *            fileName
	 * @return file di template per il tipo di esportazione
	 * @throws FileCreationException
	 *             rilanciata se manca la chiave di configurazione od il file di template.
	 */
	protected File getFilePathForTemplate(String fileName) throws FileCreationException {
		// Recupero la cartella dove sono i file di template
		Preference preference = null;
		try {
			Query query = panjeaDAO.prepareNamedQuery("Preference.caricaPerChiave");
			query.setParameter("paramChiave", COSARO_DIR_TEMPLATE);
			preference = (Preference) panjeaDAO.getSingleResult(query);
		} catch (Exception e) {
			logger.error("--> Errore ricerca preference con key " + COSARO_DIR_TEMPLATE, e);
			panjeaMessage.send("Nelle preferenze generali manca la chiave " + COSARO_DIR_TEMPLATE,
					PanjeaMessage.MESSAGE_GENERIC_SELECTOR);
			return null;
		}

		// Costruisco il file
		String pathName = preference.getValore() + fileName;
		File file = new File(pathName);
		if (!file.exists()) {
			logger.error("--> File di template per l'esportazione macante. Percorso del file " + file.getAbsolutePath());
			panjeaMessage.send(
					"File di template per l'esportazione di tipo " + getClass().getSimpleName().replace("Exporter", "")
							+ " macante. Percorso del file " + file.getAbsolutePath(),
					PanjeaMessage.MESSAGE_GENERIC_SELECTOR);
			return null;
		}
		return file;
	}

	/**
	 * Trasforma le righe magazzino in righe documento cosaro, pronte per l'esportazione.
	 * 
	 * @param righeMagazzino
	 *            righeMagazzino
	 * @return le righe documento cosaro da esportare
	 */
	private List<RigaDocumentoCosaro> transformRigheMagazzino(List<? extends RigaMagazzino> righeMagazzino) {
		List<RigaDocumentoCosaro> righe = new ArrayList<RigaDocumentoCosaro>();
		for (RigaMagazzino rigaMagazzino : righeMagazzino) {
			if (rigaMagazzino instanceof RigaArticolo) {

				RigaArticolo rigaArticolo = (RigaArticolo) rigaMagazzino;
				Set<RigaLotto> righeLotto = rigaArticolo.getRigheLotto();

				if (righeLotto != null && !righeLotto.isEmpty()) {
					for (RigaLotto rigaLotto : righeLotto) {
						RigaDocumentoCosaro rigaDocumentoCosaro = new RigaDocumentoCosaro(rigaArticolo, rigaLotto);
						righe.add(rigaDocumentoCosaro);
					}
				} else {
					RigaDocumentoCosaro rigaDocumentoCosaro = new RigaDocumentoCosaro(rigaArticolo, null);
					righe.add(rigaDocumentoCosaro);
				}
			}
		}
		return righe;
	}

	/**
	 * Scrive sul writer le righe magazzino.
	 * 
	 * @param areaMagazzino
	 *            l'area magazzino da cui prendere le righe
	 * @param out
	 *            il writer da utilizzare
	 */
	protected void writeRigheMagazzino(AreaMagazzino areaMagazzino, BeanWriter out) {
		if (areaMagazzino.getRigheMagazzino() != null) {
			List<RigaDocumentoCosaro> righe = transformRigheMagazzino(areaMagazzino.getRigheMagazzino());

			// poi scrivo le righe
			for (RigaDocumentoCosaro riga : righe) {
				out.write(riga);
			}
		}
	}
}
