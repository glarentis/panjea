package it.eurotn.panjea.preventivi.rich.editors.righepreventivo;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.util.Assert;

public abstract class AbstractCloseRigheCommand<E> extends ActionCommand {

	private static final String COMMAND_ID = "confermaAreaDocumentoCommand";
	private E areaFullDTO;

	/**
	 * Costruttore.
	 * 
	 * @param pageId
	 *            page id
	 */
	public AbstractCloseRigheCommand(final String pageId) {
		super(COMMAND_ID);
		this.setSecurityControllerId(pageId + ".controller");
		RcpSupport.configure(this);
	}

	@Override
	protected void doExecuteCommand() {
		Assert.notNull(areaFullDTO);
		this.areaFullDTO = validaRighe();
	}

	/**
	 * @return the areaOrdineFullDTO
	 */
	public E getAreaFullDTO() {
		return areaFullDTO;
	}

	/**
	 * @param areaOrdineFullDTO
	 *            the areaOrdineFullDTO to set
	 */
	public void setAreaFullDTO(E areaOrdineFullDTO) {
		this.areaFullDTO = areaOrdineFullDTO;
	}

	/**
	 * 
	 * @return Area fullDTO validata.
	 */
	protected abstract E validaRighe();

}
