package it.eurotn.panjea.beniammortizzabili.rich.commands;

import it.eurotn.panjea.beniammortizzabili.rich.bd.IBeniAmmortizzabiliBD;
import it.eurotn.panjea.beniammortizzabili.rich.editors.beni.SimulazioneSelectionDialog;
import it.eurotn.panjea.beniammortizzabili2.domain.Simulazione;
import it.eurotn.rich.command.OpenEditorCommand;

import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.dialog.CloseAction;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class OpenSimulazioneCommand extends OpenEditorCommand {

	public static final String COMMAND_ID = "openSimulazioneCommand";

	private static Logger logger = Logger.getLogger(OpenSimulazioneCommand.class);

	private final IBeniAmmortizzabiliBD beniAmmortizzabiliBD = (IBeniAmmortizzabiliBD) Application.instance()
			.getApplicationContext().getBean("beniAmmortizzabiliBD");

	private final MessageSource messageSource = (MessageSource) Application.services().getService(MessageSource.class);

	@Override
	protected void doExecuteCommand() {

		String titolo = messageSource.getMessage("selezioneSimulazioneDialog.title", new String[] {},
				Locale.getDefault());

		List<Simulazione> listSimulazioni = beniAmmortizzabiliBD.caricaSimulazioni();

		SimulazioneSelectionDialog selectionDialog = new SimulazioneSelectionDialog(titolo, null, listSimulazioni);
		selectionDialog.setCloseAction(CloseAction.HIDE);
		selectionDialog.showDialog();

		if (selectionDialog.getSimulazioneSelected() != null) {
			Simulazione simulazioneSelected = selectionDialog.getSimulazioneSelected();
			simulazioneSelected = beniAmmortizzabiliBD.caricaSimulazione(simulazioneSelected);
			logger.debug("--> selezionata la simulazione " + simulazioneSelected.getId());
			logger.debug("--> la simulazione contiene " + simulazioneSelected.getPoliticheCalcolo().size()
					+ " politiche di calcolo");

			LifecycleApplicationEvent event = new OpenEditorEvent(simulazioneSelected);
			Application.instance().getApplicationContext().publishEvent(event);
		} else {
			LifecycleApplicationEvent event = new OpenEditorEvent(new Simulazione());
			Application.instance().getApplicationContext().publishEvent(event);
		}
	}

}
