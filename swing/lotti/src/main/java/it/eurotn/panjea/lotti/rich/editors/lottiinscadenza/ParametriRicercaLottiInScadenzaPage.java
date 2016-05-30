/**
 * 
 */
package it.eurotn.panjea.lotti.rich.editors.lottiinscadenza;

import it.eurotn.panjea.lotti.util.ParametriRicercaScadenzaLotti;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.rich.forms.PanjeaFormGuard;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.util.Assert;

/**
 * @author fattazzo
 * 
 */
public class ParametriRicercaLottiInScadenzaPage extends FormBackedDialogPageEditor implements InitializingBean {

	private class LoadLottiInScadenzaCommand extends ActionCommand {

		private static final String COMMAND_ID = "searchCommand";

		/**
		 * Costruttore.
		 * 
		 */
		public LoadLottiInScadenzaCommand() {
			super(COMMAND_ID);
			CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
					CommandConfigurer.class);
			this.setSecurityControllerId(getPageEditorId() + COMMAND_ID);
			c.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			getBackingFormPage().getFormModel().commit();
			ParametriRicercaScadenzaLotti parametriRicerca = (ParametriRicercaScadenzaLotti) getBackingFormPage()
					.getFormModel().getFormObject();
			parametriRicerca.setEffettuaRicerca(true);
			getForm().getBindingFactory().getFormModel().commit();
			ParametriRicercaLottiInScadenzaPage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
					parametriRicerca);
		}

	}

	public static final String PAGE_ID = "parametriRicercaLottiInScadenzaPage";

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	private ParametriRicercaLottiInScadenzaForm form = null;

	private LoadLottiInScadenzaCommand loadLottiInScadenzaCommand;

	/**
	 * Costruttore.
	 * 
	 */
	public ParametriRicercaLottiInScadenzaPage() {
		super(PAGE_ID, new ParametriRicercaLottiInScadenzaForm());
		new PanjeaFormGuard(getBackingFormPage().getFormModel(), getLoadLottiInScadenzaCommand());
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(magazzinoAnagraficaBD, "magazzinoAnagraficaBD deve essere assegnato ");
		form = (ParametriRicercaLottiInScadenzaForm) getBackingFormPage();
		form.setMagazzinoAnagraficaBD(magazzinoAnagraficaBD);
	}

	@Override
	protected AbstractCommand[] getCommand() {
		AbstractCommand[] commands = new AbstractCommand[] { getLoadLottiInScadenzaCommand() };
		return commands;
	}

	@Override
	public AbstractCommand getEditorSaveCommand() {
		return getLoadLottiInScadenzaCommand();
	}

	/**
	 * @return loadValorizzazioneCommand
	 */
	protected LoadLottiInScadenzaCommand getLoadLottiInScadenzaCommand() {
		if (loadLottiInScadenzaCommand == null) {
			loadLottiInScadenzaCommand = new LoadLottiInScadenzaCommand();
		}
		return loadLottiInScadenzaCommand;
	}

	@Override
	protected boolean insertControlInScrollPane() {
		return false;
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public void loadData() {
		// nothing to do
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
		// nothing to do
	}

	/**
	 * @param magazzinoAnagraficaBD
	 *            the magazzinoAnagraficaBD to set
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}
}
