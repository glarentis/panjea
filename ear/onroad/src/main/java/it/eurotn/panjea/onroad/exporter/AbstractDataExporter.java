package it.eurotn.panjea.onroad.exporter;

import it.eurotn.panjea.anagrafica.domain.Preference;
import it.eurotn.panjea.exporter.exception.FileCreationException;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import java.io.File;
import java.io.IOException;

import javax.ejb.EJB;
import javax.persistence.Query;

import org.apache.log4j.Logger;

public abstract class AbstractDataExporter {

	private static Logger logger = Logger.getLogger(AbstractDataExporter.class);

	@EJB
	protected PanjeaDAO panjeaDAO;

	/**
	 * Costruisce il nome del file per l'esportazione.<br/>
	 * Il pattern del nome viene caricato dalle preference con chiave GulliverFileName.<br/>
	 * . Viene sostituito $progressivo$ con il valore di un numeratore<br>
	 * . Il numeratore viene preso dai numeratori aziendali e il nome deve essere:gulliver+tipoEsportazione:<br/>
	 * Esempio: il numeratore dell'esportazione delle sedi sarà gulliversedi.<br/>
	 * Il numeratore viene azzerato quando arriva a 999999.
	 * 
	 * @return file per l'esportazione
	 * @throws FileCreationException
	 *             rilanciata se non è possibile creare il file per l'esportazione.
	 */
	protected File getFileForExport() throws FileCreationException {
		File file = null;

		// Recupero la cartella dove esportare i file
		Preference preference = null;
		try {
			Query query = panjeaDAO.prepareNamedQuery("Preference.caricaPerChiave");
			query.setParameter("paramChiave", "onRoadDirExport");
			preference = (Preference) panjeaDAO.getSingleResult(query);
		} catch (Exception e) {
			logger.error("--> Errore ricerca preference con key onRoadDirExport", e);
			throw new FileCreationException("Nelle preferenze generali manca la chiave onRoadDirExport");
		}

		// Costruisco il nome del file
		String fileName = preference.getValore()
				+ getClass().getSimpleName().replace("Exporter", "").replace("OnRoad", "") + ".TOT";
		file = new File(fileName);
		try {
			file.createNewFile();
		} catch (IOException e) {
			logger.error(
					"--> Errore nel creare il file per l'esportazione. Percorso del file " + file.getAbsolutePath(), e);
			throw new FileCreationException("Errore nel creare il file per l'esportazione. Percorso del file "
					+ file.getAbsolutePath());
		}
		return file;
	}

	/**
	 * 
	 * @return file di template per il tipo di esportazione
	 * @throws FileCreationException
	 *             rilanciata se manca la chiave di configurazione od il file di template.
	 */
	protected File getFilePathForTemplate() throws FileCreationException {
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
		String fileName = preference.getValore()
				+ getClass().getSimpleName().replace("Exporter", "").replace("OnRoad", "") + ".xml";
		File file = new File(fileName);
		if (!file.exists()) {
			logger.error("--> File di template per l'esportazione macante. Percorso del file " + file.getAbsolutePath());
			throw new FileCreationException("File di template per l'esportazione di tipo "
					+ getClass().getSimpleName().replace("Exporter", "") + " macante. Percorso del file "
					+ file.getAbsolutePath());
		}
		return file;
	}

}
