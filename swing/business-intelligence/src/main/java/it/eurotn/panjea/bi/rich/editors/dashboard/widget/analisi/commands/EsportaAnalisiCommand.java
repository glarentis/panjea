package it.eurotn.panjea.bi.rich.editors.dashboard.widget.analisi.commands;

import it.eurotn.panjea.bi.rich.editors.dashboard.DashBoardPage;
import it.eurotn.rich.report.editor.export.EsportazioneStampaMessageAlert;

import java.io.File;

import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandConfigurer;

public class EsportaAnalisiCommand extends ActionCommand {

	private final DashBoardPage page;
	public static final String COMMAND_ID = "DWEsportaAnalisiCommand";
	private final EsportazioneStampaMessageAlert alert = new EsportazioneStampaMessageAlert();

	/**
	 * 
	 * Costruttore.
	 * 
	 * @param page
	 *            pagina legata al comando
	 */
	public EsportaAnalisiCommand(final DashBoardPage page) {
		super(COMMAND_ID);
		this.page = page;
		CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
				CommandConfigurer.class);
		this.setSecurityControllerId(COMMAND_ID);
		c.configure(this);
	}

	@Override
	protected void doExecuteCommand() {
		alert.showAlert();
		String fileEsportato = null;
		try {
			fileEsportato = page.esportaPivotPane();
		} finally {
			alert.finishExport(new File(fileEsportato));
		}

	}

}
