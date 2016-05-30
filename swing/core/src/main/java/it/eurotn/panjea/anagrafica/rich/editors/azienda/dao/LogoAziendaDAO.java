package it.eurotn.panjea.anagrafica.rich.editors.azienda.dao;

import it.eurotn.rich.report.ReportManager;
import it.eurotn.rich.report.ReportManager.ResourceType;

import java.io.File;
import java.util.Set;

import javax.swing.ImageIcon;

import org.apache.log4j.Logger;

public class LogoAziendaDAO implements ILogoAziendaDAO {

	private static Logger logger = Logger.getLogger(LogoAziendaDAO.class);

	public static final String BEAN_ID = "logoAziendaDAO";

	private ReportManager reportManager;

	private static final String IMAGES_DIR = "Immagini";

	private static final String LOGO_FINE_NAME = "logo";

	@Override
	public ImageIcon caricaLogo() {

		ImageIcon logoAzienda = null;

		Set<ImageIcon> images = reportManager.getImages(IMAGES_DIR);
		if (images != null && !images.isEmpty()) {
			// cerco tra tutte le immagini presenti quella che si riferisce al logo dell'azienda
			for (ImageIcon imageIcon : images) {
				if (imageIcon.getDescription().equals(LOGO_FINE_NAME)) {
					logoAzienda = imageIcon;
					break;
				}
			}
		}

		return logoAzienda;
	}

	@Override
	public void salvaLogo(String imageFilePath) {

		// verifico se esiste la cartella delle immagini
		if (!reportManager.folderExist("", IMAGES_DIR, ResourceType.PRIVATA)) {
			try {
				reportManager.createFolder("Private", "");
			} catch (Exception e) {
				// non faccio niente perchè la directory private potrebbe già esistere in caso mi darà un'errore dopo
				// nel creare quella delle immagini
				logger.debug("--> Directory Private esistente");
			}
			reportManager.createFolder(IMAGES_DIR, "/Private");
		}

		File imageFile = new File(imageFilePath);
		reportManager.uploadImage(IMAGES_DIR, LOGO_FINE_NAME, imageFile);
	}

	/**
	 * @param reportManager
	 *            the reportManager to set
	 */
	public void setReportManager(ReportManager reportManager) {
		this.reportManager = reportManager;
	}

}
