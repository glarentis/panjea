/**
 * 
 */
package it.eurotn.panjea.magazzino.manager.schedearticolo;

import it.eurotn.panjea.magazzino.util.ArticoloRicerca;

import java.io.File;
import java.text.DecimalFormat;

/**
 * @author fattazzo
 * 
 */
public final class StampaSchedaArticoloBuilder {

	public static final String OUTPUT_FORMAT = "PDF";

	public static final String OUTPUT_BASE_PATH_KEY = "schedeArticoloAllegatiPath";

	/**
	 * Restituisce il nome del file da utilizzare per creare della scheda dell'articolo indicato.
	 * 
	 * @param articolo
	 *            articolo
	 * @return nome del file
	 */
	public static String getStampaSchedaArticoloFileName(ArticoloRicerca articolo) {
		StringBuilder sb = new StringBuilder();
		sb.append(articolo.getCodice());
		sb.append("_");
		sb.append(articolo.getId());
		sb.append(".");
		sb.append(OUTPUT_FORMAT.toLowerCase());

		return sb.toString();
	}

	/**
	 * Restituisce il path di salvataggio della scheda articolo in base ai parametri.
	 * 
	 * @param baseDir
	 *            directory di partenza
	 * @param anno
	 *            annp
	 * @param mese
	 *            mese
	 * @return path
	 */
	public static String getStampeSchedaArticoloExportPath(String baseDir, Integer anno, Integer mese) {

		// il path è formato dalla dir base / anno / mese
		StringBuilder sb = new StringBuilder(baseDir);
		sb.append(File.separator);
		sb.append(anno);
		sb.append(File.separator);
		sb.append(new DecimalFormat("00").format(mese));

		return sb.toString();
	}

	/**
	 * Costruttore.
	 */
	private StampaSchedaArticoloBuilder() {
		super();
	}

}
