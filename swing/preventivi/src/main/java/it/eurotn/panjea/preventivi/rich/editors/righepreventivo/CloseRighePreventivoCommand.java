package it.eurotn.panjea.preventivi.rich.editors.righepreventivo;

import it.eurotn.panjea.preventivi.rich.bd.IPreventiviBD;
import it.eurotn.panjea.preventivi.util.AreaPreventivoFullDTO;

public class CloseRighePreventivoCommand extends AbstractCloseRigheCommand<AreaPreventivoFullDTO> {

	private final IPreventiviBD preventiviBD;

	/**
	 * Costruttore.
	 * 
	 * @param preventiviBD
	 *            preventiviBD
	 * 
	 * @param pageId
	 *            page id
	 */
	public CloseRighePreventivoCommand(final IPreventiviBD preventiviBD, final String pageId) {
		super(pageId);
		this.preventiviBD = preventiviBD;
	}

	@Override
	protected AreaPreventivoFullDTO validaRighe() {
		return preventiviBD.validaRighePreventivo(getAreaFullDTO().getAreaPreventivo(), getAreaFullDTO().getAreaRate(),
				true);
	}

}
