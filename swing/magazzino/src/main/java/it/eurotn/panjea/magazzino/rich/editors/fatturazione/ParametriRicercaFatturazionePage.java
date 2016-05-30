/**
 *
 */
package it.eurotn.panjea.magazzino.rich.editors.fatturazione;

import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.forms.fatturazione.ParametriRicercaFatturazioneForm;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaFatturazione;
import it.eurotn.panjea.rich.forms.PanjeaFormGuard;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandConfigurer;

/**
 * @author fattazzo
 */
public class ParametriRicercaFatturazionePage extends FormBackedDialogPageEditor implements InitializingBean {

	private class LoadFatturazioneCommand extends ActionCommand {

		private static final String COMMAND_ID = "searchCommand";

		/**
		 * Costruttore.
		 */
		public LoadFatturazioneCommand() {
			super(COMMAND_ID);
			CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
					CommandConfigurer.class);
			this.setSecurityControllerId(getPageEditorId() + COMMAND_ID);
			c.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			getBackingFormPage().getFormModel().commit();
			ParametriRicercaFatturazione parametriRicerca = (ParametriRicercaFatturazione) getBackingFormPage()
					.getFormModel().getFormObject();
			parametriRicerca.setEffettuaRicerca(true);
			getForm().getBindingFactory().getFormModel().commit();
			ParametriRicercaFatturazionePage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
					parametriRicerca);
		}

	}

	private class ResetParametriRicercaCommand extends ActionCommand {

		private static final String COMMAND_ID = "resetCommand";

		/**
		 * Costruttore.
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
			ParametriRicercaFatturazionePage.this.toolbarPageEditor.getNewCommand().execute();
			ParametriRicercaFatturazionePage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
					new ParametriRicercaFatturazione());
		}

	}

	public static final String PAGE_ID = "parametriRicercaFatturazionePage";

	private IMagazzinoDocumentoBD magazzinoDocumentoBD;

	private ResetParametriRicercaCommand resetParametriRicercaCommand;
	private LoadFatturazioneCommand loadFatturazioneCommand;

	/**
	 * Costruttore.
	 */
	public ParametriRicercaFatturazionePage() {
		super(PAGE_ID, new ParametriRicercaFatturazioneForm());
		new PanjeaFormGuard(getBackingFormPage().getFormModel(), getLoadFatturazioneCommand());
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		((ParametriRicercaFatturazioneForm) getBackingFormPage()).setMagazzinoDocumentoBD(this.magazzinoDocumentoBD);
	}

	@Override
	protected AbstractCommand[] getCommand() {
		AbstractCommand[] commands = new AbstractCommand[] { getResetParametriRicercaCommand(),
				getLoadFatturazioneCommand() };
		return commands;
	}

	@Override
	public AbstractCommand getEditorNewCommand() {
		return getResetParametriRicercaCommand();
	}

	@Override
	public AbstractCommand getEditorSaveCommand() {
		return getLoadFatturazioneCommand();
	}

	/**
	 * @return loadFatturazioneCommand
	 */
	protected LoadFatturazioneCommand getLoadFatturazioneCommand() {
		if (loadFatturazioneCommand == null) {
			loadFatturazioneCommand = new LoadFatturazioneCommand();
		}
		return loadFatturazioneCommand;
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

	@Override
	public boolean isDirty() {
		return false;
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
	}

	@Override
	public void setFormObject(Object object) {
		super.setFormObject(object);
	}

	/**
	 * @param magazzinoDocumentoBD
	 *            the magazzinoDocumentoBD to set
	 */
	public void setMagazzinoDocumentoBD(IMagazzinoDocumentoBD magazzinoDocumentoBD) {
		this.magazzinoDocumentoBD = magazzinoDocumentoBD;
	}
}
