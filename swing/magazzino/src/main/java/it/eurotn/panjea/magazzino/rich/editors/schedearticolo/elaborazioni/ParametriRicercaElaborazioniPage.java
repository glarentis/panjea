/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.editors.schedearticolo.elaborazioni;

import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaElaborazioni;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

/**
 * @author fattazzo
 * 
 */
public class ParametriRicercaElaborazioniPage extends FormBackedDialogPageEditor {

	private class ResetParametriCommand extends ActionCommand {

		private static final String COMMAND_ID = "resetCommand";

		/**
		 * Costruttore.
		 * 
		 */
		public ResetParametriCommand() {
			super(COMMAND_ID);
			this.setSecurityControllerId(getPageEditorId() + COMMAND_ID);
			RcpSupport.configure(this);

		}

		@Override
		protected void doExecuteCommand() {
			ParametriRicercaElaborazioniPage.this.toolbarPageEditor.getNewCommand().execute();
			ParametriRicercaElaborazioniPage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
					getBackingFormPage().getFormObject());
		}

	}

	private class SearchElaborazioniCommand extends ActionCommand {

		private static final String COMMAND_ID = "searchCommand";

		/**
		 * Costruttore.
		 * 
		 */
		public SearchElaborazioniCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			ParametriRicercaElaborazioni parametriRicerca = (ParametriRicercaElaborazioni) getBackingFormPage()
					.getFormObject();
			parametriRicerca.setEffettuaRicerca(true);
			getForm().getFormModel().commit();
			ParametriRicercaElaborazioniPage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
					parametriRicerca);
		}
	}

	public static final String PAGE_ID = "parametriRicercaElaborazioniPage";

	private ResetParametriCommand resetParametriCommand;
	private SearchElaborazioniCommand searchElaborazioniCommand;

	/**
	 * Costruttore.
	 * 
	 * @param parentPageId
	 * @param backingFormPage
	 */
	public ParametriRicercaElaborazioniPage() {
		super(PAGE_ID, new ParametriRicercaElaborazioniForm());
	}

	@Override
	protected AbstractCommand[] getCommand() {
		AbstractCommand[] commands = new AbstractCommand[] { getResetParametriCommand(), getSearchElaborazioniCommand() };
		return commands;
	}

	/**
	 * @return the resetParametriCommand
	 */
	private ResetParametriCommand getResetParametriCommand() {
		if (resetParametriCommand == null) {
			resetParametriCommand = new ResetParametriCommand();
		}

		return resetParametriCommand;
	}

	/**
	 * @return the searchElaborazioniCommand
	 */
	private SearchElaborazioniCommand getSearchElaborazioniCommand() {
		if (searchElaborazioniCommand == null) {
			searchElaborazioniCommand = new SearchElaborazioniCommand();
		}

		return searchElaborazioniCommand;
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
