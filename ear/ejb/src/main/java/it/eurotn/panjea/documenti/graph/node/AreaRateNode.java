package it.eurotn.panjea.documenti.graph.node;

import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rate.domain.Rata;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class AreaRateNode extends AreaDocumentoNode {

	private static final long serialVersionUID = -1861041417023665893L;

	private String codiceCodicePagamento;
	private String descrizioneCodicePagamento;

	private List<Rata> rate;

	@Override
	public String getHTMLDescription() {
		StringBuilder sb = new StringBuilder();
		sb.append("<table cellpadding='2' cellspacing='0' border='0'>");
		sb.append("<tr><td><b>Area Rate</b></td></tr></table>");

		if (codiceCodicePagamento != null && !codiceCodicePagamento.isEmpty()) {
			sb.append("<table cellpadding='2' cellspacing='0' border='0' valign='top'><tr>");
			sb.append("<td>Cod. pag.</td>");
			sb.append("<td colspan='2'>");
			sb.append(codiceCodicePagamento);
			sb.append(" - ");
			sb.append(descrizioneCodicePagamento);
			sb.append("</td>");
			sb.append("</tr>");
			sb.append("</table>");
		}

		for (Rata rata : rate) {
			sb.append("<table width='100%' cellpadding='2' cellspacing='0' border='0' valign='top'><tr>");
			sb.append("<td>Rata " + rata.getNumeroRata() + "</td>");
			sb.append("<td>");
			sb.append(new DecimalFormat("#,##0.00").format(rata.getImporto().getImportoInValuta()));
			sb.append(" " + rata.getImporto().getCodiceValuta());
			sb.append("</td>");
			sb.append("<td>");
			if (rata.getDataScadenza() != null) {
				sb.append(new SimpleDateFormat("dd/MM/yyyy").format(rata.getDataScadenza()));
			} else {
				sb.append("senza scad.");
			}
			sb.append("</td>");
			sb.append("</tr>");
			sb.append("</table>");
		}

		return sb.toString();
	}

	@Override
	public String getTipoArea() {
		return AreaRate.class.getName();
	}

	/**
	 * @param codiceCodicePagamento
	 *            The codiceCodicePagamento to set.
	 */
	public void setCodiceCodicePagamento(String codiceCodicePagamento) {
		this.codiceCodicePagamento = codiceCodicePagamento;
	}

	/**
	 * @param descrizioneCodicePagamento
	 *            The descrizioneCodicePagamento to set.
	 */
	public void setDescrizioneCodicePagamento(String descrizioneCodicePagamento) {
		this.descrizioneCodicePagamento = descrizioneCodicePagamento;
	}

	/**
	 * @param rate
	 *            The rate to set.
	 */
	public void setRate(List<Rata> rate) {
		this.rate = rate;
	}

}
