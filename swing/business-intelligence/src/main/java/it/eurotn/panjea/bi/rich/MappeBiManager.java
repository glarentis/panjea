/**
 *
 */
package it.eurotn.panjea.bi.rich;

import it.eurotn.panjea.bi.rich.bd.BusinessIntelligenceBD;
import it.eurotn.panjea.bi.rich.bd.IBusinessIntelligenceBD;
import it.eurotn.panjea.bi.util.Mappa;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.dialog.MessageAlert;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.log4j.Logger;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.util.RcpSupport;

/**
 * @author fattazzo
 *
 */
public class MappeBiManager {

	private static Logger logger = Logger.getLogger(MappeBiManager.class);

	private IBusinessIntelligenceBD businessIntelligenceBD;
	private Path userMapDir;

	/**
	 * Costruttore.
	 */
	public MappeBiManager() {
		super();
		businessIntelligenceBD = RcpSupport.getBean(BusinessIntelligenceBD.BEAN_ID);
		userMapDir = PanjeaSwingUtil.getHome().resolve("maps");
	}

	/**
	 * Carica tutta la lista delle mappe presenti.
	 *
	 * @return mappe caricate
	 */
	public List<Mappa> caricaMappe() {
		return businessIntelligenceBD.caricaMappe();
	}

	/**
	 * Cancella tutte i file delle mappe presenti nella directory dell'utente.
	 */
	public void clearUserMapDir() {
		try {
			if (Files.exists(userMapDir, new LinkOption[] { LinkOption.NOFOLLOW_LINKS })) {
				FileUtils.cleanDirectory(userMapDir.toFile());
			}
		} catch (IOException e) {
			// non faccio niente
		}
	}

	/**
	 * Aggiunge al nome del file della mappa il path.
	 *
	 * @param nomeFileMappa
	 *            nome del file della mappa
	 * @return path + nome file mappa
	 */
	public String getPathMappa(String nomeFileMappa) {

		// se la directory locale ancora non esiste la creo
		if (!Files.exists(userMapDir, new LinkOption[] { LinkOption.NOFOLLOW_LINKS })) {
			try {
				Files.createDirectories(userMapDir);
			} catch (IOException e) {
				logger.error("-->impossibile scaricare il file delle mappe nell cartella " + userMapDir, e);
				throw new RuntimeException(e);
			}
		}

		// cerco prima se nella home dell'utente esistono i files della mappa
		@SuppressWarnings("unchecked")
		Collection<File> listFiles = FileUtils.listFiles(userMapDir.toFile(),
				new RegexFileFilter(nomeFileMappa + ".*"), FileFileFilter.FILE);

		// se i file non ci sono vado a scaricarli (i file delle mappe sono shp, shx e txt, per questo controllo che
		// siano 3)
		if (listFiles.isEmpty() || listFiles.size() != 3) {
			MessageAlert alert = new MessageAlert(new DefaultMessage("Download mappa in corso...", Severity.INFO), 0);
			alert.showAlert();
			try {
				Map<String, byte[]> filesMappa = businessIntelligenceBD.caricaFilesMappa(nomeFileMappa);

				for (Entry<String, byte[]> entry : filesMappa.entrySet()) {

					File file = new File(userMapDir + File.separator + entry.getKey());
					FileUtils.writeByteArrayToFile(file, entry.getValue());
				}
			} catch (IOException e) {
			} finally {
				alert.closeAlert();
			}
		}

		return userMapDir + File.separator + nomeFileMappa;
	}
}
