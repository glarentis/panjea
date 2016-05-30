package it.eurotn.panjea.documenti.graph.node;

import it.eurotn.panjea.tesoreria.domain.AreaAssegno;

public class AreaAssegnoNode extends AreaTesoreriaNode {

	private static final long serialVersionUID = 6512602935585643915L;

	private String numeroAssegno;

	private String abi;

	private String cab;

	@Override
	public String getHTMLDescription() {
		StringBuilder sb = new StringBuilder();
		sb.append("<table width='100%' cellpadding='2' cellspacing='0' border='0'>");
		sb.append("<tr><td><b>Area Assegno</b></td></tr>");

		sb.append("<table width='100%' cellpadding='2' cellspacing='0' border='0'>");
		sb.append("<tr><td>Abi ");
		sb.append(abi);
		sb.append("</td>");
		sb.append("<td>Cab ");
		sb.append(cab);
		sb.append("</td></tr>");
		sb.append("<tr><td colspan='2'>N. ");
		sb.append(numeroAssegno);
		sb.append("</td>");
		sb.append("</tr>");

		return sb.toString();
	}

	@Override
	public String getTipoArea() {
		return AreaAssegno.class.getName();
	}

	/**
	 * @param abi
	 *            The abi to set.
	 */
	public void setAbi(String abi) {
		this.abi = abi;
	}

	/**
	 * @param cab
	 *            The cab to set.
	 */
	public void setCab(String cab) {
		this.cab = cab;
	}

	/**
	 * @param numeroAssegno
	 *            The numeroAssegno to set.
	 */
	public void setNumeroAssegno(String numeroAssegno) {
		this.numeroAssegno = numeroAssegno;
	}
}
