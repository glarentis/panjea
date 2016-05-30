/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.editors.confrontolistino;

import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaConfrontoListino;
import it.eurotn.panjea.rich.forms.PanjeaFormGuard;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

import javax.swing.AbstractButton;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

/**
 * @author fattazzo
 * 
 */
public class ParametriRicercaConfrontoListinoPage extends FormBackedDialogPageEditor {

	private class ResetParametriRicercaCommand extends ActionCommand {

		private static final String COMMAND_ID = "resetCommand";

		/**
		 * Costruttore.
		 * 
		 */
		public ResetParametriRicercaCommand() {
			super(COMMAND_ID);
			this.setSecurityControllerId(getPageEditorId() + COMMAND_ID);
			RcpSupport.configure(this);

		}

		@Override
		protected void doExecuteCommand() {
			ParametriRicercaConfrontoListinoPage.this.toolbarPageEditor.getNewCommand().execute();
			ParametriRicercaConfrontoListinoPage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
					new ParametriRicercaConfrontoListino());
		}

	}

	private class SearchAreaMagazzinoCommand extends ActionCommand {

		private static final String COMMAND_ID = "searchCommand";

		/**
		 * Costruttore.
		 * 
		 */
		public SearchAreaMagazzinoCommand() {
			super(COMMAND_ID);
			this.setSecurityControllerId(getPageEditorId() + COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		public void attach(AbstractButton button) {
			super.attach(button);
			button.setName("searchConfrontoListinoCommand");
		}

		@Override
		protected void doExecuteCommand() {
			ParametriRicercaConfrontoListino parametriRicerca = (ParametriRicercaConfrontoListino) getBackingFormPage()
					.getFormObject();
			parametriRicerca.setEffettuaRicerca(true);
			getForm().getFormModel().commit();
			ParametriRicercaConfrontoListinoPage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
					parametriRicerca);
		}
	}

	public static final String PAGE_ID = "parametriRicercaConfrontoListinoPage";

	private ResetParametriRicercaCommand resetParametriRicercaCommand;
	private SearchAreaMagazzinoCommand searchAreaMagazzinoCommand;

	/**
	 * Costruttore.
	 */
	public ParametriRicercaConfrontoListinoPage() {
		super(PAGE_ID, new ParametriRicercaConfrontoListinoForm());
		new PanjeaFormGuard(getBackingFormPage().getFormModel(), getSearchAreaMagazzinoCommand());
	}

	@Override
	protected AbstractCommand[] getCommand() {
		AbstractCommand[] commands = new AbstractCommand[] { getResetParametriRicercaCommand(),
				getSearchAreaMagazzinoCommand() };
		return commands;
	}

	@Override
	public AbstractCommand getEditorNewCommand() {
		return getResetParametriRicercaCommand();
	}

	@Override
	public AbstractCommand getEditorSaveCommand() {
		return getSearchAreaMagazzinoCommand();
	}

	/**
	 * @return resetParametriRicercaCommand
	 */
	protected ResetParametriRicercaCommand getResetParametriRicercaCommand() {
		if (resetParametriRicercaCommand == null) {
			resetParametriRicercaCommand = new ResetParametriRicercaCommand();
		}
		return resetParametriRicercaCommand;
	}

	/**
	 * @return searchAreaMagazzinoCommand
	 */
	protected SearchAreaMagazzinoCommand getSearchAreaMagazzinoCommand() {
		if (searchAreaMagazzinoCommand == null) {
			searchAreaMagazzinoCommand = new SearchAreaMagazzinoCommand();
		}
		return searchAreaMagazzinoCommand;
	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPostPageOpen() {
		// TODO Auto-generated method stub

	}

	@Override
	public void refreshData() {
		// TODO Auto-generated method stub

	}

}
