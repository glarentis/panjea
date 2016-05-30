/**
 *
 */
package it.eurotn.panjea.magazzino.manager.articolo;

import it.eurotn.codice.generator.interfaces.ProtocolloGenerator;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.AttributoArticolo;
import it.eurotn.panjea.magazzino.domain.GenerazioneCodiceArticoloData;
import it.eurotn.panjea.magazzino.domain.TipoAttributo;
import it.eurotn.panjea.magazzino.domain.TipoAttributo.ETipoDatoTipoAttributo;
import it.eurotn.panjea.magazzino.exception.GenerazioneCodiceException;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * Classe che si occupa di generare un valore stringa in base a una maschera definita sulla categoria e a un articolo.
 *
 * @author fattazzo
 */
public class ArticoloMascheraBuilder {
	private static Logger logger = Logger.getLogger(ArticoloMascheraBuilder.class);

	/**
	 *
	 * @param articolo
	 *            articolo per il quale generare codice e descrizione
	 * @param protocolloGenerator
	 *            generatore dei protocolli
	 * @return articolo con codice e descrizione avvalorati se esiste una maschera sulla categoria *
	 * @throws GenerazioneCodiceException
	 */
	public Articolo applicaValoriMaschera(Articolo articolo, ProtocolloGenerator protocolloGenerator) {
		logger.debug("--> Enter generaCodice con numeratore ");

		String numeratore = "";
		// calcolo il numeratore se serve
		if (articolo.getCategoria().getGenerazioneCodiceArticoloData().getNumeratore() != null
				&& !articolo.getCategoria().getGenerazioneCodiceArticoloData().getNumeratore().isEmpty()) {
			numeratore = protocolloGenerator.nextCodice(articolo.getCategoria().getGenerazioneCodiceArticoloData()
					.getNumeratore(), Integer.MAX_VALUE);
			Integer numCaratteriNumeratore = articolo.getCategoria().getGenerazioneCodiceArticoloData()
					.getNumCaratteriNumeratore();
			if (numCaratteriNumeratore > 0) {
				numeratore = StringUtils.leftPad(numeratore, numCaratteriNumeratore, "0");
			}
		}

		// la maschera per il codice la recupero dal codice articolo se non è vuoto.
		// se il codice è vuoto la recupero dalla categoria
		String mascheraCodice = articolo.getCodice();
		if (mascheraCodice.isEmpty()) {
			mascheraCodice = (String) ObjectUtils.defaultIfNull(articolo.getCategoria()
					.getGenerazioneCodiceArticoloData().getMascheraCodiceArticolo(), "");
		}
		if (!mascheraCodice.isEmpty() && mascheraCodice.contains("$")) {
			articolo.setCodice(replaceMaschera(mascheraCodice, numeratore, articolo.getAttributiArticolo()));
		}

		String mascheraDescrizione = articolo.getDescrizione();
		if (mascheraDescrizione.isEmpty()) {
			mascheraDescrizione = (String) ObjectUtils.defaultIfNull(articolo.getCategoria()
					.getGenerazioneCodiceArticoloData().getMascheraDescrizioneArticolo(), "");
		}

		// genero la descrizione con la maschera della descrizione se presente.
		if (!mascheraDescrizione.isEmpty() && mascheraDescrizione.contains("$")) {
			articolo.setDescrizione(replaceMaschera(mascheraDescrizione, numeratore, articolo.getAttributiArticolo()));
		}
		// //Generro le descrizioni per le varie lingue
		// Map<String, DescrizioneLinguaArticolo> descrizioniLinguaArticolo = new TreeMap<String,
		// DescrizioneLinguaArticolo>();
		// DescrizioneLinguaArticolo descrizioneLinguaArticolo;
		// for (String codiceLingua : articolo.getCategoria().getInformazioniLingua().keySet()) {
		// // generazione descrizione in lingua per ogni occorenza di Lingua
		// // InformazioneLinguaCategoria descrizioneLinguaCategoria = paramArticolo.getCategoria()
		// // .getInformazioniLingua().get(codiceLingua);
		// descrizioneLinguaArticolo = new DescrizioneLinguaArticolo();
		// descrizioneLinguaArticolo.setCodiceLingua(codiceLingua);
		// descrizioneLinguaArticolo.setDescrizione(articoloManagerBuilder.generaValoreMaschera(paramArticolo,
		// paramArticolo.getCategoria().getGenerazioneCodiceArticoloData().getMascheraDescrizioneArticolo(),
		// ETipoAttributoGenerazione.DESCRIZIONE, codiceLingua));
		// // descrizioneLinguaArticolo.setDescrizione(generaDescrizione(paramArticolo, descrizioneLinguaCategoria
		// // .getMascheraDescrizioneArticolo(), codiceLingua));
		// descrizioniLinguaArticolo.put(descrizioneLinguaArticolo.getCodiceLingua(), descrizioneLinguaArticolo);
		// }
		logger.debug("--> Exit generaDescrizioniLingua");
		return articolo;
	}

	private String replaceMaschera(String maschera, String numeratore, List<AttributoArticolo> attributi) {
		String result = maschera;
		result = result.replace(TipoAttributo.SEPARATORE_CODICE_FORMULA + GenerazioneCodiceArticoloData.NUMERATORE
				+ TipoAttributo.SEPARATORE_CODICE_FORMULA, numeratore);

		for (AttributoArticolo attributo : attributi) {
			String valoreNonFormattato = (String) ObjectUtils.defaultIfNull(attributo.getValore(), "");

			if (attributo.getTipoAttributo().getTipoDato() == ETipoDatoTipoAttributo.NUMERICO) {
				BigDecimal valoreNonFormattatoNumerico = attributo.getValoreTipizzato(BigDecimal.class);
				valoreNonFormattato = (valoreNonFormattatoNumerico != null ? valoreNonFormattatoNumerico.toString()
						: "");
			}
			result = result.replace(attributo.getTipoAttributo().getCodiceFormula(), valoreNonFormattato);
		}
		return result;
	}

}
