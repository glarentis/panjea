/**
 *
 */
package it.eurotn.panjea.beniammortizzabili.rich.editors.simulazioni;

import it.eurotn.panjea.beniammortizzabili.rich.bd.IBeniAmmortizzabiliBD;
import it.eurotn.panjea.beniammortizzabili.rich.commands.NewSimulazioneCommand;
import it.eurotn.panjea.beniammortizzabili.rich.commands.OpenSimulazioneCommand;
import it.eurotn.panjea.beniammortizzabili.rich.commands.StampaSimulazioneCommand;
import it.eurotn.panjea.beniammortizzabili.rich.editors.beni.SimulazioneForm;
import it.eurotn.panjea.beniammortizzabili.rich.forms.simulazione.CalcolaSimulazioneCommand;
import it.eurotn.panjea.beniammortizzabili.rich.forms.simulazione.ConsolidaSimulazioneCommand;
import it.eurotn.panjea.beniammortizzabili.rich.forms.simulazione.PoliticheCalcoloCivilisticheWizardForm;
import it.eurotn.panjea.beniammortizzabili.rich.forms.simulazione.PoliticheCalcoloFiscaliWizardForm;
import it.eurotn.panjea.beniammortizzabili2.domain.Simulazione;
import it.eurotn.panjea.rich.forms.PanjeaFormGuard;
import it.eurotn.rich.editors.FormsBackedTabbedDialogPageEditor;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;
import org.springframework.richclient.form.FormGuard;
import org.springframework.richclient.settings.Settings;

/**
 * @author fattazzo
 */
public class SimulazionePage extends FormsBackedTabbedDialogPageEditor {

	private final IBeniAmmortizzabiliBD beniAmmortizzabiliBD = (IBeniAmmortizzabiliBD) Application.instance()
			.getApplicationContext().getBean("beniAmmortizzabiliBD");

	public static final String PAGE_ID = "simulazionePage";

	private CalcolaSimulazioneCommand calcolaSimulazioneCommand;
	private ConsolidaSimulazioneCommand consolidaSimulazioneCommand;
	private NewSimulazioneCommand newSimulazioneCommand;
	private OpenSimulazioneCommand openSimulazioneCommand;
	private CancellaSimulazioneCommand cancellaSimulazioneCommand;
	private StampaSimulazioneCommand stampaSimulazioneCommand;
	private CreaAreeContabiliSimulazioneCommand creaAreeContabiliSimulazioneCommand;
	private ConfermaAreeContabiliSimulazioneCommand confermaAreeContabiliSimulazioneCommand;

	private PoliticheCalcoloCivilisticheWizardForm politicheCivilisticheForm;
	private PoliticheCalcoloFiscaliWizardForm politicheFiscaliForm;

	/**
	 * Costruttore di default.
	 */
	public SimulazionePage() {
		super(PAGE_ID, new SimulazioneForm());

		new PanjeaFormGuard(getBackingFormPage().getFormModel(), getCalcolaSimulazioneCommand(), FormGuard.ON_NOERRORS);
	}

	@Override
	public void addForms() {

		politicheCivilisticheForm = new PoliticheCalcoloCivilisticheWizardForm(getBackingFormPage().getFormModel());
		addForm(politicheCivilisticheForm);

		politicheFiscaliForm = new PoliticheCalcoloFiscaliWizardForm(getBackingFormPage().getFormModel());
		addForm(politicheFiscaliForm);
	}

	/**
	 * @return CalcolaSimulazioneCommand
	 */
	public CalcolaSimulazioneCommand getCalcolaSimulazioneCommand() {
		if (calcolaSimulazioneCommand == null) {
			calcolaSimulazioneCommand = new CalcolaSimulazioneCommand(SimulazionePage.this, beniAmmortizzabiliBD);
		}
		return calcolaSimulazioneCommand;
	}

	/**
	 * @return CancellaSimulazioneCommand
	 */
	public CancellaSimulazioneCommand getCancellaSimulazioneCommand() {
		if (cancellaSimulazioneCommand == null) {
			cancellaSimulazioneCommand = new CancellaSimulazioneCommand(SimulazionePage.this, beniAmmortizzabiliBD);
		}
		return cancellaSimulazioneCommand;
	}

	@Override
	protected AbstractCommand[] getCommand() {
		AbstractCommand[] abstractCommands = new AbstractCommand[] { getNewSimulazioneCommand(),
				getOpenSimulazioneCommand(), getCancellaSimulazioneCommand(), getConsolidaSimulazioneCommand(),
				getCalcolaSimulazioneCommand(), getStampaSimulazioneCommand(),
				getCreaAreeContabiliSimulazioneCommand(), getConfermaAreeContabiliSimulazioneCommand() };
		return abstractCommands;
	}

	/**
	 * @return the confermaAreeContabiliSimulazioneCommand
	 */
	public ConfermaAreeContabiliSimulazioneCommand getConfermaAreeContabiliSimulazioneCommand() {
		if (confermaAreeContabiliSimulazioneCommand == null) {
			confermaAreeContabiliSimulazioneCommand = new ConfermaAreeContabiliSimulazioneCommand();
			confermaAreeContabiliSimulazioneCommand.addCommandInterceptor(new ActionCommandInterceptorAdapter() {
				@Override
				public boolean preExecution(ActionCommand command) {
					command.addParameter(ConfermaAreeContabiliSimulazioneCommand.PARAM_SIMULAZIONE_PAGE,
							SimulazionePage.this);
					return true;
				}
			});

		}

		return confermaAreeContabiliSimulazioneCommand;
	}

	/**
	 * @return ConsolidaSimulazioneCommand
	 */
	public ConsolidaSimulazioneCommand getConsolidaSimulazioneCommand() {
		if (consolidaSimulazioneCommand == null) {
			consolidaSimulazioneCommand = new ConsolidaSimulazioneCommand(this, beniAmmortizzabiliBD);
		}
		return consolidaSimulazioneCommand;
	}

	/**
	 * @return the creaAreeContabiliSimulazioneCommand
	 */
	public CreaAreeContabiliSimulazioneCommand getCreaAreeContabiliSimulazioneCommand() {
		if (creaAreeContabiliSimulazioneCommand == null) {
			creaAreeContabiliSimulazioneCommand = new CreaAreeContabiliSimulazioneCommand();
			creaAreeContabiliSimulazioneCommand.addCommandInterceptor(new ActionCommandInterceptorAdapter() {
				@Override
				public boolean preExecution(ActionCommand command) {
					command.addParameter(CreaAreeContabiliSimulazioneCommand.PARAM_SIMULAZIONE_PAGE,
							SimulazionePage.this);
					return true;
				}
			});

		}

		return creaAreeContabiliSimulazioneCommand;
	}

	/**
	 * @return NewSimulazioneCommand
	 */
	public NewSimulazioneCommand getNewSimulazioneCommand() {
		if (newSimulazioneCommand == null) {
			newSimulazioneCommand = (NewSimulazioneCommand) Application.instance().getActiveWindow()
					.getCommandManager().getCommand(NewSimulazioneCommand.COMMAND_ID);
		}
		return newSimulazioneCommand;
	}

	/**
	 * @return OpenSimulazioneCommand
	 */
	public OpenSimulazioneCommand getOpenSimulazioneCommand() {
		if (openSimulazioneCommand == null) {
			openSimulazioneCommand = (OpenSimulazioneCommand) Application.instance().getActiveWindow()
					.getCommandManager().getCommand(OpenSimulazioneCommand.COMMAND_ID);
		}
		return openSimulazioneCommand;
	}

	/**
	 * @return StampaSimulazioneCommand
	 */
	public StampaSimulazioneCommand getStampaSimulazioneCommand() {
		if (stampaSimulazioneCommand == null) {
			stampaSimulazioneCommand = new StampaSimulazioneCommand(beniAmmortizzabiliBD);
			stampaSimulazioneCommand.addCommandInterceptor(new ActionCommandInterceptor() {

				@Override
				public void postExecution(ActionCommand command) {
				}

				@Override
				public boolean preExecution(ActionCommand command) {
					Simulazione simulazione = (Simulazione) SimulazionePage.this.getBackingFormPage().getFormObject();

					if (simulazione.isNew()) {
						return false;
					} else {
						stampaSimulazioneCommand.setSimulazione(simulazione);
						return true;
					}
				}
			});
		}
		return stampaSimulazioneCommand;
	}

	@Override
	public void loadData() {

	}

	@Override
	public void onPostPageOpen() {

	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public void postSetFormObject(Object object) {
		super.postSetFormObject(object);

		Simulazione simulazione = (Simulazione) object;
		if (simulazione.getDescrizione() == null || simulazione.getDescrizione().isEmpty()) {
			getBackingFormPage().getFormModel().setEnabled(true);
		}
	}

	@Override
	public void refreshData() {

	}

	@Override
	public void restoreState(Settings settings) {
		super.restoreState(settings);
		politicheCivilisticheForm.restoreState(settings);
		politicheFiscaliForm.restoreState(settings);
	}

	@Override
	public void saveState(Settings settings) {
		super.saveState(settings);
		politicheCivilisticheForm.saveState(settings);
		politicheFiscaliForm.saveState(settings);
	}

	@Override
	public void setFormObject(Object object) {
		super.setFormObject(object);

		Simulazione simulazione = (Simulazione) object;
		getCreaAreeContabiliSimulazioneCommand().setVisible(simulazione.isAreeContabiliDaCreare());
		getConfermaAreeContabiliSimulazioneCommand().setVisible(simulazione.isAreeContabiliDaConfermare());
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		if (readOnly) {
			setTabForm(0);
		}
		getTabbedPane().setEnabled(!readOnly);
		super.setReadOnly(readOnly);

		Simulazione simulazione = (Simulazione) getForm().getFormObject();
		getCreaAreeContabiliSimulazioneCommand().setVisible(!readOnly && simulazione.isAreeContabiliDaCreare());
		getConfermaAreeContabiliSimulazioneCommand().setVisible(!readOnly && simulazione.isAreeContabiliDaConfermare());
	}
}
