package it.eurotn.panjea.lotti.exception;

import it.eurotn.panjea.magazzino.domain.ArticoloLite;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LottoNonPresenteException extends LottiException {

	private static final long serialVersionUID = -1002172647085666526L;

	private String codiceLotto;
	private Date dataScadenzaLotto;

	private String codiceArticolo;
	private String descrizioneArticolo;

	/**
	 * Costruttore.
	 * 
	 * @param codiceLotto
	 *            codice lotto
	 * @param dataScadenzaLotto
	 *            data scadenza del lotto
	 * @param articolo
	 *            articolo di riferimento
	 */
	public LottoNonPresenteException(final String codiceLotto, final Date dataScadenzaLotto, final ArticoloLite articolo) {
		super();
		this.codiceLotto = codiceLotto;
		this.dataScadenzaLotto = dataScadenzaLotto;
		this.codiceArticolo = articolo.getCodice();
		this.descrizioneArticolo = articolo.getDescrizione();
	}

	@Override
	public String getHTMLMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append("<html>");
		sb.append("Non è stato possibile trovare il lotto con i seguenti criteri di ricerca:<br>");
		sb.append("<ul>");
		sb.append("<li>");
		sb.append("<b>Articolo: </b>");
		sb.append(codiceArticolo + " - " + descrizioneArticolo);
		sb.append("</li>");
		sb.append("<li>");
		sb.append("<b>Codice lotto: </b>");
		sb.append(codiceLotto);
		sb.append("</li>");
		sb.append("<li>");
		sb.append("<b>Scadenza lotto: </b>");
		if (dataScadenzaLotto != null) {
			sb.append(new SimpleDateFormat("dd/MM/yyyy").format(dataScadenzaLotto));
		}
		sb.append("</li>");
		sb.append("</ul>");

		return sb.toString();
	}

}
