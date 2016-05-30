package it.eurotn.panjea.documenti.graph.node;

public class AreaTesoreriaNode extends AreaDocumentoNode {

	private static final long serialVersionUID = -7136617838220438790L;

	private String tipoArea;

	/**
	 * Costruttore.
	 */
	public AreaTesoreriaNode() {
		super();
	}

	/**
	 * Costruttore.
	 * 
	 * @param tipoArea
	 *            tipo area
	 */
	public AreaTesoreriaNode(final String tipoArea) {
		super();
		this.tipoArea = tipoArea;
	}

	@Override
	public String getHTMLDescription() {
		StringBuilder sb = new StringBuilder();
		sb.append("<table cellpadding='2' cellspacing='0' border='0'>");
		sb.append("<tr><td><b>" + getTipoArea() + "</b></td></tr>");
		return sb.toString();
	}

	@Override
	public String getTipoArea() {
		return tipoArea;
	}

}
