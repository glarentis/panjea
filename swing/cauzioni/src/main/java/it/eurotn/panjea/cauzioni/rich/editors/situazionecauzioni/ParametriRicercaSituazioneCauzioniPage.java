package it.eurotn.panjea.cauzioni.rich.editors.situazionecauzioni;

import it.eurotn.panjea.cauzioni.util.parametriricerca.ParametriRicercaSituazioneCauzioni;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

public class ParametriRicercaSituazioneCauzioniPage extends FormBackedDialogPageEditor {

	private class ResetParametriRicercaCommand extends ActionCommand {

		private static final String COMMAND_ID = "resetCommand";

		/**
		 * Costruttore.
		 */
		public ResetParametriRicercaCommand() {
			super(COMMAND_ID);
			this.setSecurityControllerId(getPageEditorId() + COMMAND_ID);
			RcpSupport.configure(this);

		}

		@Override
		protected void doExecuteCommand() {
			ParametriRicercaSituazioneCauzioniPage.this.toolbarPageEditor.getNewCommand().execute();
			ParametriRicercaSituazioneCauzioniPage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
					null);
		}

	}

	private class SearchSituazioneCauzioniCommand extends ActionCommand {

		private static final String COMMAND_ID = "searchCommand";

		/**
		 * Costruttore.
		 */
		public SearchSituazioneCauzioniCommand() {
			super(COMMAND_ID);
			this.setSecurityControllerId(getPageEditorId() + COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			ParametriRicercaSituazioneCauzioni parametriRicerca = (ParametriRicercaSituazioneCauzioni) getBackingFormPage()
					.getFormObject();
			parametriRicerca.setEffettuaRicerca(true);

			getForm().getFormModel().commit();
			ParametriRicercaSituazioneCauzioniPage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
					parametriRicerca);
		}
	}

	public static final String PAGE_ID = "parametriRicercaSituazioneCauzioniPage";

	private ResetParametriRicercaCommand resetParametriRicercaCommand;

	private SearchSituazioneCauzioniCommand searchSituazioneCauzioniCommand;

	/**
	 *
	 * Costruttore.
	 */
	public ParametriRicercaSituazioneCauzioniPage() {
		super(PAGE_ID, new ParametriRicercaSituazioneCauzioniForm());
	}

	@Override
	protected AbstractCommand[] getCommand() {
		AbstractCommand[] commands = new AbstractCommand[] { getResetParametriRicercaCommand(),
				getSearchSituazioneCauzioniCommand() };
		return commands;
	}

	@Override
	public AbstractCommand getEditorNewCommand() {
		return getResetParametriRicercaCommand();
	}

	@Override
	public AbstractCommand getEditorSaveCommand() {
		return getSearchSituazioneCauzioniCommand();
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

	/**
	 * @return the searchSituazioneCauzioniCommand
	 */
	public SearchSituazioneCauzioniCommand getSearchSituazioneCauzioniCommand() {
		if (searchSituazioneCauzioniCommand == null) {
			searchSituazioneCauzioniCommand = new SearchSituazioneCauzioniCommand();
		}

		return searchSituazioneCauzioniCommand;
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
	public void refreshData() {
		loadData();
	}

}
