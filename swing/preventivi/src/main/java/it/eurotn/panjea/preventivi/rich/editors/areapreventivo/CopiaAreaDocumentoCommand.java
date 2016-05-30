package it.eurotn.panjea.preventivi.rich.editors.areapreventivo;

import it.eurotn.panjea.preventivi.rich.bd.ICopiaDocumentoBD;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class CopiaAreaDocumentoCommand extends ActionCommand {

	public static final String COMMAND_ID = "copiaAreaDocumentoCommand";

	public static final String PARAM_AREA_ID = "areaId";

	private final ICopiaDocumentoBD areaDocumentoBD;

	/**
	 * @param areaDocumentoBD
	 *            areaDocumentoBD.
	 * 
	 */
	public CopiaAreaDocumentoCommand(final ICopiaDocumentoBD areaDocumentoBD) {
		super(COMMAND_ID);
		RcpSupport.configure(this);
		this.areaDocumentoBD = areaDocumentoBD;
	}

	@Override
	protected void doExecuteCommand() {

		Integer idArea = (Integer) getParameter(PARAM_AREA_ID, null);

		if (idArea == null) {
			logger.error("--> ID area nullo, impossibile creare la copia");
			return;
		}

		Object copia = areaDocumentoBD.copiaArea(idArea);
		LifecycleApplicationEvent event = new OpenEditorEvent(copia);
		Application.instance().getApplicationContext().publishEvent(event);
	}

}
