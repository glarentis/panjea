package it.eurotn.panjea;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import it.eurotn.panjea.utils.FileDownloader;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

public class PanjeaUpdater {

	private static Logger logger = Logger.getLogger(PanjeaUpdater.class);
	private Path homePath;
	private String urlServer;

	private String server;

	/**
	 *
	 * @param path
	 *            percorso di avvio di Panjea
	 */
	public PanjeaUpdater(final Path path) {
		super();
		this.server = PanjeaSwingUtil.getServerUrl();
		this.homePath = path;
		StringBuilder sburlServer = new StringBuilder("http://");
		sburlServer.append(server);
		sburlServer.append(":8080/panjea/");
		urlServer = sburlServer.toString();
	}

	/**
	 * controlla se ci sono aggiornamenti. Se presenti scarica l'updater, lo
	 * avvia ed esce da panjea.
	 *
	 */
	public void checkAndApply() {
		logger.debug("--> Enter checkAndApply");
		Properties propLocali = new Properties();
		String buildIdLocal = "";
		try {
			propLocali.load(new FileInputStream(homePath.resolve("version.properties").toFile()));
			buildIdLocal = propLocali.getProperty("buildid");
		} catch (IOException e1) {
			logger.debug("-->errore nel leggere il file  " + homePath.resolve("versione.properties"), e1);
			return;
		}

		// leggo il file remoto version.properties

		String buildIdServer = "";
		try (InputStream input = new URL(new StringBuilder(urlServer).append("version.properties").toString())
				.openStream()) {
			Properties propRemote = new Properties();
			propRemote.load(input);
			buildIdServer = propRemote.getProperty("buildid");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Non riesco ad accedere al server " + urlServer + ". Assicurarsi che il server sia attivo");
			logger.error("-->errore ..impossibile accedere al server per scaricare le informazioni sulla versione", e);
			System.exit(0);
		}

		if (!buildIdLocal.equals(buildIdServer)) {
			JOptionPane.showMessageDialog(null,
					"E' stata trovata una nuova versione di Panjea. Verrà eseguito un aggiornamento del client.");
			downloadAndLaunchUpdater();
		}
		logger.debug("--> Exit checkAndApply");
	}

	/**
	 * Downloads a file from a URL
	 *
	 */
	public void downloadAndLaunchUpdater() {

		Path pathLocalUpdater = homePath.resolve("updater.jar");
		try {
			Files.deleteIfExists(pathLocalUpdater);
		} catch (IOException e1) {
			logger.error("-->errore nell'cancellare updater.jar", e1);
			e1.printStackTrace();
		}

		if (FileDownloader.download(urlServer, "updater.jar", pathLocalUpdater)) {
			ProcessBuilder pb = new ProcessBuilder("java", "-jar", "updater.jar", server, homePath.toString());
			pb.directory(homePath.toFile());
			try {
				pb.start();
				System.exit(0);
			} catch (IOException e) {
				logger.error("-->errore nell'avviare updater.jar", e);
				e.printStackTrace();
			}
		} else {
			JOptionPane.showMessageDialog(null, "Errore nello scaricare il file di aggiornamento da " + urlServer
					+ ".\n Controllare il file di log per ulteriori informazioni");
		}
	}
}
