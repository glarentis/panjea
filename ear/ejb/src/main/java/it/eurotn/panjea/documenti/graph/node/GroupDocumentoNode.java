package it.eurotn.panjea.documenti.graph.node;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.documenti.graph.builder.AbstractAreaDocumentoBuilderBean;

public class GroupDocumentoNode extends DocumentoNode {

	private static final long serialVersionUID = 3417975221305559689L;

	/**
	 * Costruttore.
	 *
	 * @param documento
	 *            documento
	 */
	public GroupDocumentoNode(final Documento documento) {
		super();
		setDescrizioneTipoDocumento(documento.getTipoDocumento().getDescrizione());
		setCodiceDocumento(documento.getCodice().getCodice());
	}

	@Override
	public String getHTMLDescription() {
		StringBuilder sb = new StringBuilder();
		sb.append("<table width='100%' cellpadding='2' cellspacing='0' border='0'><tr>");

		// descrizione documento
		sb.append("<td align='center'><b>Sono presenti più di ");
		sb.append(AbstractAreaDocumentoBuilderBean.MAX_DOCUMENT_SIZE_NODE);
		sb.append(" documenti</b></td>");
		sb.append("</tr>");

		sb.append("<tr>");
		sb.append("<td><hr></td>");
		sb.append("</tr>");
		sb.append("</table>");

		sb.append("<table width='100%' cellpadding='2' cellspacing='0' border='0' valign='top'><tr>");
		sb.append("<td>Tipo: ");
		sb.append(descrizioneTipoDocumento);
		sb.append("</td>");
		sb.append("</tr>");
		sb.append("</table>");

		sb.append("<table width='100%' cellpadding='2' cellspacing='0' border='0' valign='top'><tr>");
		sb.append("<td>Numero: </td>");
		sb.append("<td>");
		sb.append(getCodiceDocumento());
		sb.append("</td>");
		sb.append("</tr>");
		sb.append("</table>");

		return sb.toString();
	};
}
