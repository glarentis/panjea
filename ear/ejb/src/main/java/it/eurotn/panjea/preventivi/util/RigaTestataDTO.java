package it.eurotn.panjea.preventivi.util;

import it.eurotn.panjea.preventivi.domain.RigaPreventivo;
import it.eurotn.panjea.preventivi.domain.RigaTestata;
import it.eurotn.util.PanjeaEJBUtil;

public class RigaTestataDTO extends RigaPreventivoDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -370339482885457931L;

	@Override
	public RigaPreventivo getRigaPreventivo() {
		RigaPreventivo riga = new RigaTestata();
		PanjeaEJBUtil.copyProperties(riga, this);
		return riga;
	}
}
