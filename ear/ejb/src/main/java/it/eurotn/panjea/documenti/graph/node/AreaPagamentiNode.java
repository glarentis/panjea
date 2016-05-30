package it.eurotn.panjea.documenti.graph.node;

import it.eurotn.panjea.tesoreria.domain.AreaPagamenti;
import it.eurotn.panjea.tesoreria.domain.Pagamento;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class AreaPagamentiNode extends AreaTesoreriaNode {

	private static final long serialVersionUID = 3130315302516237829L;

	private List<Pagamento> pagamenti;

	@Override
	public String getHTMLDescription() {
		StringBuilder sb = new StringBuilder();
		sb.append("<table cellpadding='2' cellspacing='0' border='0'>");
		sb.append("<tr><td><b>Area Pagamenti</b></td></tr>");

		for (Pagamento pagamento : pagamenti) {
			sb.append("<table width='100%' cellpadding='2' cellspacing='0' border='0' valign='top'><tr>");
			sb.append("<td>Pagamento</td>");
			sb.append("<td>");
			sb.append(new DecimalFormat("#,##0.00").format(pagamento.getImporto().getImportoInValuta()));
			sb.append(" " + pagamento.getImporto().getCodiceValuta());
			sb.append("</td>");
			sb.append("<td>");
			if (pagamento.getDataPagamento() != null) {
				sb.append(new SimpleDateFormat("dd/MM/yyyy").format(pagamento.getDataPagamento()));
			} else {
				sb.append("in lavorazione");
			}
			sb.append("</td>");
			sb.append("</tr>");

			sb.append("<tr>");
			sb.append("<td></td>");
			sb.append("<td>Rata n. ");
			sb.append(pagamento.getRata().getNumeroRata());
			sb.append("</td>");
			sb.append("</tr>");
			sb.append("</table>");
		}

		return sb.toString();
	}

	@Override
	public String getTipoArea() {
		return AreaPagamenti.class.getName();
	}

	/**
	 * @param pagamenti
	 *            The pagamenti to set.
	 */
	public void setPagamenti(List<Pagamento> pagamenti) {
		this.pagamenti = pagamenti;
	}
}
