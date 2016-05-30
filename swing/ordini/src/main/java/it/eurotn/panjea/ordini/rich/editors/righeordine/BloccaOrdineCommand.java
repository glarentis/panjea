package it.eurotn.panjea.ordini.rich.editors.righeordine;

import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;
import it.eurotn.panjea.ordini.rich.bd.OrdiniDocumentoBD;
import it.eurotn.panjea.ordini.util.AreaOrdineFullDTO;

import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;

public class BloccaOrdineCommand extends ApplicationWindowAwareCommand {

	public static final String COMMAND_ID = "bloccaOrdineCommand";
	public static final String AREA_ORDINE_FULL_PARAM = "areaOrdineParam";

	private AreaOrdineFullDTO areaOrdineFullDTO;

	private IOrdiniDocumentoBD ordiniDocumentoBD;

	/**
	 * Costruttore.
	 * 
	 */
	public BloccaOrdineCommand() {
		super(COMMAND_ID);
		RcpSupport.configure(this);

		this.ordiniDocumentoBD = RcpSupport.getBean(OrdiniDocumentoBD.BEAN_ID);
	}

	@Override
	protected void doExecuteCommand() {

		areaOrdineFullDTO = (AreaOrdineFullDTO) getParameter(AREA_ORDINE_FULL_PARAM);

		AreaOrdine areaOrdine = areaOrdineFullDTO.getAreaOrdine();
		areaOrdine = ordiniDocumentoBD.bloccaOrdine(areaOrdine.getId(), true);

		areaOrdineFullDTO.setAreaOrdine(areaOrdine);
	}

	/**
	 * @return the areaOrdineFullDTO
	 */
	public AreaOrdineFullDTO getAreaOrdineFullDTO() {
		return areaOrdineFullDTO;
	}

}
