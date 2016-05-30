/**
 *
 */
package it.eurotn.panjea.contabilita.rich.editors.ritenuteacconto.situazione;

import it.eurotn.panjea.contabilita.util.ParametriSituazioneRitenuteAcconto;
import it.eurotn.panjea.rich.forms.PanjeaFormGuard;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;
import it.eurotn.rich.form.PanjeaAbstractForm;

import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.core.Guarded;

public class ParametriRicercaSituazioneRitenutePage extends FormBackedDialogPageEditor {

	private class CercaCommand extends ActionCommand implements Guarded {

		/**
		 * Costruttore.
		 *
		 */
		public CercaCommand() {
			super("searchCommand");
			CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
					CommandConfigurer.class);
			this.setSecurityControllerId(getPageEditorId() + "searchCommand");
			c.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			ParametriSituazioneRitenuteAcconto parametriRicerca = (ParametriSituazioneRitenuteAcconto) getBackingFormPage()
					.getFormObject();
			parametriRicerca.setEffettuaRicerca(true);

			getForm().getFormModel().commit();
			ParametriRicercaSituazioneRitenutePage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
					getBackingFormPage().getFormObject());
		}
	}

	private class ResetParametriRicercaCommand extends ActionCommand {

		private static final String COMMAND_ID = "resetCommand";

		/**
		 * Costruttore.
		 *
		 */
		public ResetParametriRicercaCommand() {
			super(COMMAND_ID);
			CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
					CommandConfigurer.class);
			this.setSecurityControllerId(getPageEditorId() + COMMAND_ID);
			c.configure(this);

		}

		@Override
		protected void doExecuteCommand() {
			ParametriRicercaSituazioneRitenutePage.this.toolbarPageEditor.getNewCommand().execute();
		}

	}

	private static final String ID_PAGE = "parametriRicercaSituazioneRitenutePage";

	private AbstractCommand cercaCommand;
	private AbstractCommand resetRicercaCommand;

	/**
	 *
	 * Costruttore.
	 *
	 */
	public ParametriRicercaSituazioneRitenutePage() {
		super(ID_PAGE, new ParametriRicercaSituazioneRitenuteForm());
		new PanjeaFormGuard(getBackingFormPage().getFormModel(), getCercaCommand());
	}

	@Override
	public void dispose() {
		super.dispose();

		cercaCommand = null;

		resetRicercaCommand = null;
	}

	private AbstractCommand getCercaCommand() {
		if (cercaCommand == null) {
			cercaCommand = new CercaCommand();
		}
		return cercaCommand;
	}

	@Override
	protected AbstractCommand[] getCommand() {
		AbstractCommand[] abstractCommands = new AbstractCommand[] { getResetParametriRicercaCommand(),
				getCercaCommand() };
		return abstractCommands;
	}

	@Override
	public AbstractCommand getEditorNewCommand() {
		return getResetParametriRicercaCommand();
	}

	@Override
	public AbstractCommand getEditorSaveCommand() {
		return getCercaCommand();
	}

	private AbstractCommand getResetParametriRicercaCommand() {
		if (resetRicercaCommand == null) {
			resetRicercaCommand = new ResetParametriRicercaCommand();
		}
		return resetRicercaCommand;
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public void loadData() {
		ParametriSituazioneRitenuteAcconto parametri = (ParametriSituazioneRitenuteAcconto) getBackingFormPage()
				.getFormObject();
		if (!parametri.isEffettuaRicerca()) {
			getResetParametriRicercaCommand().execute();
		}
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		((PanjeaAbstractForm) getBackingFormPage()).getFormModel().setReadOnly(false);
		return true;
	}

	/**
	 * Sovrascrivo questo metodo per non eseguire nulla ed evitare il salvataggio premendo la combinazione ctrl + S che
	 * e' abilitata di default nella form backed dialog page.
	 *
	 * @return onSave
	 */
	@Override
	public boolean onSave() {
		return true;
	}

	/**
	 * Sovrascrivo questo metodo per non eseguire l'undo command premendo ctrl + Z che e' abilitato di default nella
	 * form backed dialog page.
	 *
	 * @return onUndo
	 */
	@Override
	public boolean onUndo() {
		return true;
	}

	@Override
	public void refreshData() {
		loadData();
	}

}
