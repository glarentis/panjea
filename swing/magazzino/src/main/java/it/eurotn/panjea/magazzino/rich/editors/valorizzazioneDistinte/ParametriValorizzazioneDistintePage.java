package it.eurotn.panjea.magazzino.rich.editors.valorizzazioneDistinte;

import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriValorizzazioneDistinte;
import it.eurotn.panjea.rich.forms.PanjeaFormGuard;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;
import it.eurotn.rich.form.PanjeaAbstractForm;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.core.Guarded;
import org.springframework.richclient.util.RcpSupport;

public class ParametriValorizzazioneDistintePage extends FormBackedDialogPageEditor {

	private class CercaCommand extends ActionCommand implements Guarded {

		/**
		 * Costruttore.
		 *
		 */
		public CercaCommand() {
			super("searchCommand");
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			ParametriValorizzazioneDistinte parametriRicerca = (ParametriValorizzazioneDistinte) getBackingFormPage()
					.getFormObject();
			parametriRicerca.setEffettuaRicerca(true);
			getForm().getFormModel().commit();
			ParametriValorizzazioneDistintePage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
					parametriRicerca);
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
			RcpSupport.configure(this);

		}

		@Override
		protected void doExecuteCommand() {
			ParametriValorizzazioneDistintePage.this.toolbarPageEditor.getNewCommand().execute();
			ParametriValorizzazioneDistintePage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
					ParametriValorizzazioneDistintePage.this.getBackingFormPage().getFormObject());
		}

	}

	private CercaCommand cercaCommand;
	private ResetParametriRicercaCommand resetParametriRicercaCommand;

	/**
	 * Costruttore.
	 */
	public ParametriValorizzazioneDistintePage() {
		super("ParametriValorizzazioneDistintePage", new ParametriValorizzazioneDistinteForm());
		new PanjeaFormGuard(getBackingFormPage().getFormModel(), getCercaCommand());
	}

	@Override
	public void dispose() {
		super.dispose();

		cercaCommand = null;
		resetParametriRicercaCommand = null;
	}

	/**
	 * @return the cercaCommand
	 */
	public CercaCommand getCercaCommand() {
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

	/**
	 * @return the resetParametriRicercaCommand
	 */
	public ResetParametriRicercaCommand getResetParametriRicercaCommand() {
		if (resetParametriRicercaCommand == null) {
			resetParametriRicercaCommand = new ResetParametriRicercaCommand();
		}

		return resetParametriRicercaCommand;
	}

	@Override
	public void loadData() {
		ParametriValorizzazioneDistinte parametriRicerca = (ParametriValorizzazioneDistinte) getBackingFormPage()
				.getFormObject();
		if (!parametriRicerca.isEffettuaRicerca()) {
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
	 * è abilitata di default nella form backed dialog page.
	 *
	 * @return true
	 */
	@Override
	public boolean onSave() {
		return true;
	}

	/**
	 * Sovrascrivo questo metodo per non eseguire l'undo command premendo ctrl + Z che è abilitato di default nella form
	 * backed dialog page.
	 *
	 * @return true
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
