package it.eurotn.panjea.ordini.rich.editors.areaordine;

import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;
import it.eurotn.panjea.ordini.rich.bd.OrdiniDocumentoBD;
import it.eurotn.panjea.ordini.util.AreaOrdineFullDTO;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class CopiaAreaOrdineCommand extends ActionCommand {

	public static final String COMMAND_ID = "copiaAreaOrdineCommand";

	public static final String PARAM_AREA_ORDINE_ID = "areaOrdineId";

	private IOrdiniDocumentoBD ordiniDocumentoBD;

	/**
	 * Costruttore.
	 * 
	 */
	public CopiaAreaOrdineCommand() {
		super(COMMAND_ID);
		RcpSupport.configure(this);

		ordiniDocumentoBD = RcpSupport.getBean(OrdiniDocumentoBD.BEAN_ID);
	}

	@Override
	protected void doExecuteCommand() {

		Integer idAreaOrdine = (Integer) getParameter(PARAM_AREA_ORDINE_ID, null);

		if (idAreaOrdine == null) {
			logger.error("--> ID area ordine nullo, impossibile creare la copia dell'ordine");
			return;
		}

		AreaOrdineFullDTO areaOrdineFullDTO = ordiniDocumentoBD.copiaOrdine(idAreaOrdine);
		LifecycleApplicationEvent event = new OpenEditorEvent(areaOrdineFullDTO);
		Application.instance().getApplicationContext().publishEvent(event);
	}

}
