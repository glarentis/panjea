package it.eurotn.panjea.magazzino.rich.articoli.marchice;

import it.eurotn.rich.report.ReportManager;
import it.eurotn.rich.report.ReportManager.ResourceType;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import javax.swing.ImageIcon;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.SortedList;

public class ArticoloMarchiCEDAO implements IArticoloMarchiCEDAO {

	public static final String BEAN_ID = "articoloMarchiCEDAO";

	private ReportManager reportManager;

	private static final String BASE_URI = "Magazzino";

	private static final String IMAGES_DIR = "Immagini";
	private static final String MARCHICE_DIR = "MarchiCEE";

	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

	@Override
	public void cancellaMarchioCE(String codiceArticolo, String imageName) {
		reportManager
		.deleteImage("/" + reportManager.getPathCodiceAzienda() + ReportManager.PRIVATE_PATH + "Magazzino/"
				+ IMAGES_DIR + "/" + MARCHICE_DIR + "/" + codiceArticolo.toLowerCase() + "/" + imageName);
	}

	@Override
	public Set<ImageIcon> caricaMarchiCE(String codiceArticolo) {
		return reportManager.getImages(BASE_URI + "/" + IMAGES_DIR + "/" + MARCHICE_DIR + "/"
				+ codiceArticolo.toLowerCase());
	}

	@Override
	public String caricaPathMarchioCECorrente(String codiceArticolo, Date data) {

		String dataRiferimento = dateFormat.format(data);

		String pathArticolo = BASE_URI + "/" + IMAGES_DIR + "/" + MARCHICE_DIR + "/" + codiceArticolo.toLowerCase();

		Set<String> immagini = reportManager.listImages(pathArticolo);

		EventList<String> immaginiEvent = new BasicEventList<String>();
		immaginiEvent.addAll(immagini);
		SortedList<String> immaginiSorted = new SortedList<String>(immaginiEvent);

		String marchioCECorrente = null;
		for (String path : immaginiSorted) {
			String dataPath = path.substring(path.length() - 8, path.length());
			if (dataPath.compareTo(dataRiferimento) <= 0) {
				marchioCECorrente = path;
			} else {
				break;
			}
		}
		return marchioCECorrente;
	}

	@Override
	public boolean checkMarchioCE(String codiceArticolo, Date dataDecorrenza) {

		String imageName = dateFormat.format(dataDecorrenza);
		String parentFolderName = BASE_URI + "/" + IMAGES_DIR + "/" + MARCHICE_DIR + "/" + codiceArticolo.toLowerCase();

		return reportManager.imageExist(parentFolderName, imageName, ResourceType.PRIVATA);
	}

	@Override
	public void salvaMarchioCE(String codiceArticolo, Date dataDecorrenza, String imageFilePath) {

		codiceArticolo = codiceArticolo.toLowerCase();

		// verifico se esiste la cartella delle immagini e dei marchi CE
		if (!reportManager.folderExist(BASE_URI, IMAGES_DIR, ResourceType.PRIVATA)) {
			reportManager.createFolder(IMAGES_DIR, ReportManager.PRIVATE_PATH + BASE_URI);

			reportManager.createFolder(MARCHICE_DIR, ReportManager.PRIVATE_PATH + BASE_URI + "/" + IMAGES_DIR);
		}

		// verifico se esiste la cartella del codice articolo richiesto
		if (!reportManager.folderExist(BASE_URI + "/" + IMAGES_DIR + "/" + MARCHICE_DIR, codiceArticolo,
				ResourceType.PRIVATA)) {
			reportManager.createFolder(codiceArticolo, ReportManager.PRIVATE_PATH + BASE_URI + "/" + IMAGES_DIR + "/"
					+ MARCHICE_DIR);
		}

		File imageFile = new File(imageFilePath);
		String parentResourcePathImage = BASE_URI + "/" + IMAGES_DIR + "/" + MARCHICE_DIR + "/" + codiceArticolo;
		reportManager.uploadImage(parentResourcePathImage, dateFormat.format(dataDecorrenza), imageFile);
	}

	/**
	 * @param reportManager
	 *            the reportManager to set
	 */
	public void setReportManager(ReportManager reportManager) {
		this.reportManager = reportManager;
	}
}
