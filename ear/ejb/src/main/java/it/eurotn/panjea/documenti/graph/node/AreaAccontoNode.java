package it.eurotn.panjea.documenti.graph.node;

import it.eurotn.panjea.tesoreria.domain.AreaAcconto;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class AreaAccontoNode extends AreaTesoreriaNode {

	private static final long serialVersionUID = -8789983674102727804L;

	private BigDecimal importoUtilizzato;

	private String note;

	@Override
	public String getHTMLDescription() {
		StringBuilder sb = new StringBuilder();
		sb.append("<table width='100%' cellpadding='2' cellspacing='0' border='0'>");
		sb.append("<tr><td><b>Area Acconto</b></td></tr>");

		sb.append("<table width='100%' cellpadding='2' cellspacing='0' border='0' valign='top'><tr>");
		sb.append("<td>Imporot utilizzato</td>");
		sb.append("<td>");
		sb.append(new DecimalFormat("#,##0.00####").format(importoUtilizzato));
		sb.append("</td>");
		sb.append("</tr>");

		sb.append("<tr>");
		sb.append("<td>Note</td>");
		sb.append("<td>");
		sb.append(getNote());
		sb.append("</td>");
		sb.append("</tr>");
		sb.append("</table>");

		return sb.toString();
	}

	/**
	 * @return Returns the note.
	 */
	private String getNote() {
		if (note == null) {
			note = "";
		}
		return note;
	}

	@Override
	public String getTipoArea() {
		return AreaAcconto.class.getName();
	}

	/**
	 * @param importoUtilizzato
	 *            The importoUtilizzato to set.
	 */
	public void setImportoUtilizzato(BigDecimal importoUtilizzato) {
		this.importoUtilizzato = importoUtilizzato;
	}

	/**
	 * @param note
	 *            The note to set.
	 */
	public void setNote(String note) {
		this.note = note;
	}
}
