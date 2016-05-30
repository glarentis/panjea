package it.eurotn.panjea.beniammortizzabili.rich.commands;

import it.eurotn.panjea.beniammortizzabili.rich.bd.IBeniAmmortizzabiliBD;
import it.eurotn.panjea.beniammortizzabili.rich.editors.beni.SimulazioneSelectionDialog;
import it.eurotn.panjea.beniammortizzabili2.domain.Simulazione;

import java.awt.Dimension;
import java.awt.Window;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.dialog.CloseAction;
import org.springframework.richclient.dialog.InputApplicationDialog;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;
import com.toedter.calendar.JCalendar;

/**
 * Apre l'editor della simulazione con una nuova simulazione.
 * 
 * @author fattazzo
 * 
 */
public class NewSimulazioneCommand extends ApplicationWindowAwareCommand {

	public static final String COMMAND_ID = "newSimulazioneCommand";

	private final IBeniAmmortizzabiliBD beniAmmortizzabiliBD = (IBeniAmmortizzabiliBD) Application.instance()
			.getApplicationContext().getBean("beniAmmortizzabiliBD");

	private final MessageSource messageSource = (MessageSource) Application.services().getService(MessageSource.class);

	/**
	 * Costruttore di default.
	 */
	public NewSimulazioneCommand() {
		super();
		this.setSecurityControllerId(COMMAND_ID);
		RcpSupport.configure(this);
	}

	@Override
	protected void doExecuteCommand() {

		String titleAnnoDialog = messageSource.getMessage("selezioneAnnoSimulazioneDialog.title", new String[] {},
				Locale.getDefault());
		InputApplicationDialog dialog = new InputApplicationDialog(titleAnnoDialog, (Window) null) {

			@Override
			protected void onCancel() {
				super.onCancel();
			}

			@SuppressWarnings("unchecked")
			@Override
			protected boolean onFinish() {
				Date dataSimulazione = ((JCalendar) getInputField()).getDate();
				Calendar calDataSimulazione = Calendar.getInstance();
				calDataSimulazione.setTime(dataSimulazione);

				Object obj = beniAmmortizzabiliBD.verificaNuovaSimulazione(calDataSimulazione.get(Calendar.YEAR));

				if (obj instanceof List) {
					List<Simulazione> listSim = (List<Simulazione>) obj;
					Simulazione simulazioneRiferimento = null;
					if (listSim.size() > 0) {
						String titolo = getMessage("selezioneSimulazioneDialog.title", new Object[] {});
						SimulazioneSelectionDialog selectionDialog = new SimulazioneSelectionDialog(titolo, null,
								listSim);
						selectionDialog.setCloseAction(CloseAction.HIDE);
						selectionDialog.showDialog();

						if (selectionDialog.getSimulazioneSelected() != null) {
							simulazioneRiferimento = selectionDialog.getSimulazioneSelected();
						}
					}
					Simulazione simulazioneLoad;

					simulazioneLoad = beniAmmortizzabiliBD.creaSimulazione(null, calDataSimulazione.getTime(),
							simulazioneRiferimento);

					LifecycleApplicationEvent event = new OpenEditorEvent(simulazioneLoad);
					Application.instance().getApplicationContext().publishEvent(event);
				} else {
					if (obj instanceof Integer) {
						String titolo = null;
						String message = null;
						switch (((Integer) obj).intValue()) {
						case 0:
							// l'anno della nuova simulazione e < dell'ultimo anno consolidato
							titolo = getMessage("errorNuovaSimulazioneDialog.annoMinore.title", new Object[] {});
							message = getMessage("errorNuovaSimulazioneDialog.annoMinore.message", new Object[] {});
							break;
						case 1:
							// non esiste nessuna simulazione non consolidata nell'anno precedente
							titolo = getMessage("errorNuovaSimulazioneDialog.nessunaSimConsolidata.title",
									new Object[] {});
							message = getMessage("errorNuovaSimulazioneDialog.nessunaSimConsolidata.message",
									new Object[] {});
							break;
						default:
							break;
						}
						MessageDialog dialog = new MessageDialog(titolo, message);
						dialog.setModal(true);
						dialog.setPreferredSize(new Dimension(300, 70));
						dialog.setCloseAction(CloseAction.DISPOSE);
						dialog.showDialog();
					}
				}
				return true;
			}

		};
		dialog.setInputLabelMessage(messageSource.getMessage("selezioneAnnoSimulazioneDialog.label", new String[] {},
				Locale.getDefault()));

		// setto come data per la nuova simulazione l'ultimo giorno dell'anno corrente
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 31);
		calendar.set(Calendar.MONTH, 11);

		JCalendar dataControl = new JCalendar(calendar.getTime());
		dialog.setInputField(dataControl);
		dialog.setModal(true);
		dialog.setCloseAction(CloseAction.DISPOSE);
		dialog.showDialog();
	}
}
