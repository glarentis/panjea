package it.eurotn.panjea.beniammortizzabili.rich.forms.simulazione;

import it.eurotn.panjea.beniammortizzabili.rich.bd.IBeniAmmortizzabiliBD;
import it.eurotn.panjea.beniammortizzabili.rich.editors.simulazioni.SimulazionePage;
import it.eurotn.panjea.beniammortizzabili2.domain.PoliticaCalcolo;
import it.eurotn.panjea.beniammortizzabili2.domain.Simulazione;

import java.awt.Dimension;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.dialog.CloseAction;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.dialog.MessageDialog;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class ConsolidaSimulazioneCommand extends ApplicationWindowAwareCommand {

	public static final String COMMAND_ID = "consolidaSimulazioneCommand";

	private final SimulazionePage simulazionePage;

	private final MessageSource messageSource = (MessageSource) Application.services().getService(MessageSource.class);

	private final IBeniAmmortizzabiliBD beniAmmortizzabiliBD;

	/**
	 * Costruttore di default.
	 *
	 * @param paramSimulazionePage
	 *            {@link SimulazionePage}
	 * @param paramBeniAmmortizzabiliBD
	 *            BD per le operazioni di storage
	 */
	public ConsolidaSimulazioneCommand(final SimulazionePage paramSimulazionePage,
			final IBeniAmmortizzabiliBD paramBeniAmmortizzabiliBD) {
		super(COMMAND_ID);
		setSecurityControllerId(COMMAND_ID);
		CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
				CommandConfigurer.class);
		c.configure(this);
		this.simulazionePage = paramSimulazionePage;
		this.beniAmmortizzabiliBD = paramBeniAmmortizzabiliBD;
	}

	@Override
	protected void doExecuteCommand() {
		String title = null;
		String message = null;
		final Simulazione simulazione = (Simulazione) this.simulazionePage.getBackingFormPage().getFormObject();

		if (simulazione.isConsolidata()) {
			title = messageSource.getMessage("consolidaSimulazione.alreadyConsolidata.title", new Object[] {},
					Locale.getDefault());
			message = messageSource.getMessage("consolidaSimulazione.alreadyConsolidata.message", new Object[] {},
					Locale.getDefault());
			openMessageDialog(title, message);
			return;
		}

		if (!simulazione.isAllowConsolida()) {
			title = messageSource.getMessage("consolidaSimulazione.notAllowConsolidata.title", new Object[] {},
					Locale.getDefault());
			message = messageSource.getMessage("consolidaSimulazione.notAllowConsolidata.message", new Object[] {},
					Locale.getDefault());
			openMessageDialog(title, message);
		} else {
			boolean isDirty = false;
			for (PoliticaCalcolo politicaCalcolo : simulazione.getPoliticheCalcolo()) {
				if (politicaCalcolo.isDirty()) {
					isDirty = true;
					break;
				}
			}

			if (isDirty) {
				title = messageSource.getMessage("consolidaSimulazione.isDirty.title", new Object[] {},
						Locale.getDefault());
				message = messageSource.getMessage("consolidaSimulazione.isDirty.message", new Object[] {},
						Locale.getDefault());
				openMessageDialog(title, message);

			} else {
				title = messageSource.getMessage("consolidaSimulazione.consolida.title", new Object[] {},
						Locale.getDefault());
				message = messageSource.getMessage("consolidaSimulazione.consolida.message",
						new Object[] { simulazione.getDescrizione() }, Locale.getDefault());
				ConfirmationDialog dialog = new ConfirmationDialog(title, message) {

					@Override
					protected void onConfirm() {
						beniAmmortizzabiliBD.consolidaSimulazione(simulazione);

						LifecycleApplicationEvent event = new OpenEditorEvent(
								beniAmmortizzabiliBD.caricaSimulazione(simulazione));
						Application.instance().getApplicationContext().publishEvent(event);
					}

				};
				dialog.setModal(true);
				dialog.setPreferredSize(new Dimension(300, 70));
				dialog.setCloseAction(CloseAction.DISPOSE);
				dialog.showDialog();
			}
		}
	}

	/**
	 * Metodo di comodità che apre un dialogo con titolo e messaggio passati come parametri.
	 *
	 * @param title
	 *            titolo del dialogo
	 * @param message
	 *            messaggio del dialogo
	 */
	private void openMessageDialog(String title, String message) {
		MessageDialog dialog = new MessageDialog(title, message);
		dialog.setModal(true);
		dialog.setPreferredSize(new Dimension(300, 70));
		dialog.setCloseAction(CloseAction.DISPOSE);
		dialog.showDialog();
	}

}