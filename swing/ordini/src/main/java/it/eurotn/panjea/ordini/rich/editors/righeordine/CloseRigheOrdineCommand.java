package it.eurotn.panjea.ordini.rich.editors.righeordine;

import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;
import it.eurotn.panjea.ordini.util.AreaOrdineFullDTO;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.util.Assert;

public class CloseRigheOrdineCommand extends ActionCommand {

	private static final String COMMAND_ID = "confermaAreaOrdineCommand";
	private AreaOrdineFullDTO areaOrdineFullDTO;
	private final IOrdiniDocumentoBD ordiniDocumentoBD;

	/**
	 * Costruttore.
	 * 
	 * @param ordiniDocumentoBD
	 *            {@link IOrdiniDocumentoBD}
	 * @param pageId
	 *            page id
	 */
	public CloseRigheOrdineCommand(final IOrdiniDocumentoBD ordiniDocumentoBD, final String pageId) {
		super(COMMAND_ID);
		this.setSecurityControllerId(pageId + ".controller");
		RcpSupport.configure(this);
		this.ordiniDocumentoBD = ordiniDocumentoBD;
	}

	@Override
	protected void doExecuteCommand() {
		Assert.notNull(areaOrdineFullDTO);

		areaOrdineFullDTO = ordiniDocumentoBD.validaRigheOrdine(areaOrdineFullDTO.getAreaOrdine(),
				areaOrdineFullDTO.getAreaRate(), true);
	}

	/**
	 * @return the areaOrdineFullDTO
	 */
	public AreaOrdineFullDTO getAreaOrdineFullDTO() {
		return areaOrdineFullDTO;
	}

	/**
	 * @param areaOrdineFullDTO
	 *            the areaOrdineFullDTO to set
	 */
	public void setAreaOrdineFullDTO(AreaOrdineFullDTO areaOrdineFullDTO) {
		this.areaOrdineFullDTO = areaOrdineFullDTO;
	}
}