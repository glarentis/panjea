package it.eurotn.panjea.documenti.graph.node;

import it.eurotn.panjea.tesoreria.domain.AreaEffetti;

public class AreaEffettiNode extends AreaTesoreriaNode {

	private static final long serialVersionUID = 6518554700305249399L;

	@Override
	public String getHTMLDescription() {
		return "";
	}

	@Override
	public String getTipoArea() {
		return AreaEffetti.class.getName();
	}
}
