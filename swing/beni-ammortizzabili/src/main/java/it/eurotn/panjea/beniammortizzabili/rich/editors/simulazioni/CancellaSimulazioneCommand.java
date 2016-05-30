package it.eurotn.panjea.beniammortizzabili.rich.editors.simulazioni;

import it.eurotn.panjea.beniammortizzabili.rich.bd.IBeniAmmortizzabiliBD;
import it.eurotn.panjea.beniammortizzabili2.domain.Simulazione;
import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;

import java.awt.Dimension;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.dialog.CloseAction;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.RcpSupport;

/**
 * Comando che cancella la simulazione se esiste e le simulazioni collegate.
 * 
 * @author
 */
public class CancellaSimulazioneCommand extends ApplicationWindowAwareCommand {

	public static final String COMMAND_ID = "cancellaSimulazioneCommand";

	private final SimulazionePage simulazionePage;

	private final MessageSource messageSource = (MessageSource) Application.services().getService(MessageSource.class);

	private final IBeniAmmortizzabiliBD beniAmmortizzabiliBD;

	/**
	 * Costruttore.
	 * 
	 * @param paramSimulazionePage
	 *            pagina che gestisce la simulazione
	 * @param paramBeniAmmortizzabiliBD
	 *            beniAmmortizzabiliBD
	 */
	public CancellaSimulazioneCommand(final SimulazionePage paramSimulazionePage,
			final IBeniAmmortizzabiliBD paramBeniAmmortizzabiliBD) {
		super(COMMAND_ID);
		setSecurityControllerId(COMMAND_ID);
		RcpSupport.configure(this);
		this.simulazionePage = paramSimulazionePage;
		this.beniAmmortizzabiliBD = paramBeniAmmortizzabiliBD;
	}

	@Override
	protected void doExecuteCommand() {
		final Simulazione simulazione = (Simulazione) this.simulazionePage.getBackingFormPage().getFormObject();
		if ((simulazione != null && simulazione.getId() != null)) {
			String titolo;
			String messaggio;
			if (!simulazione.isConsolidata()) {

				titolo = messageSource.getMessage("cancellaSimulazione.conferma.title", new Object[] {},
						Locale.getDefault());
				messaggio = messageSource.getMessage("cancellaSimulazione.conferma.message", new Object[] {},
						Locale.getDefault());

				ConfirmationDialog dialog = new ConfirmationDialog(titolo, messaggio) {

					@Override
					protected void onConfirm() {
						beniAmmortizzabiliBD.cancellaSimulazione(simulazione);

						PanjeaLifecycleApplicationEvent deleteEvent = new PanjeaLifecycleApplicationEvent(
								LifecycleApplicationEvent.DELETED, simulazione);
						Application.instance().getApplicationContext().publishEvent(deleteEvent);
					}
				};

				dialog.setModal(true);
				dialog.setPreferredSize(new Dimension(300, 70));
				dialog.setCloseAction(CloseAction.HIDE);
				dialog.showDialog();

			} else {
				titolo = messageSource.getMessage("cancellaSimulazione.errorConsolidata.title", new Object[] {},
						Locale.getDefault());
				messaggio = messageSource.getMessage("cancellaSimulazione.errorConsolidata.message", new Object[] {},
						Locale.getDefault());

				openMessageDialog(titolo, messaggio);
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