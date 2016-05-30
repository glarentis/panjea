package it.eurotn.panjea.preventivi.rich.editors.areapreventivo;

import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.preventivi.rich.bd.IPreventiviBD;
import it.eurotn.panjea.preventivi.rich.forms.areapreventivo.ParametriRicercaAreaPreventivoForm;
import it.eurotn.panjea.preventivi.util.parametriricerca.ParametriRicercaAreaPreventivo;
import it.eurotn.panjea.rich.forms.PanjeaFormGuard;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;
import it.eurotn.rich.form.PanjeaAbstractForm;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.util.Assert;

public class ParametriRicercaAreaPreventivoPage extends FormBackedDialogPageEditor implements InitializingBean {

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
			ParametriRicercaAreaPreventivoForm form = (ParametriRicercaAreaPreventivoForm) getBackingFormPage();
			toolbarPageEditor.getNewCommand().execute();
			ParametriRicercaAreaPreventivo parametri = form.createNewObject();
			form.setFormObject(parametri);
			firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null, form.getFormObject());
		}

	}

	private class SearchAreaPreventivoCommand extends ActionCommand {

		private static final String COMMAND_ID = "searchCommand";

		/**
		 * Costruttore.
		 */
		public SearchAreaPreventivoCommand() {
			super(COMMAND_ID);
			CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
					CommandConfigurer.class);
			setSecurityControllerId(getPageEditorId() + COMMAND_ID);
			c.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			ParametriRicercaAreaPreventivoPage.logger.debug("--> Enter doExecuteCommand");
			ParametriRicercaAreaPreventivo parametriRicerca = (ParametriRicercaAreaPreventivo) getBackingFormPage()
					.getFormObject();
			parametriRicerca.setEffettuaRicerca(true);
			getForm().getFormModel().commit();
			ParametriRicercaAreaPreventivoPage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
					parametriRicerca);
		}
	}

	private AziendaCorrente aziendaCorrente;
	private static final String PAGE_ID = "parametriRicercaAreaPreventivoPage";
	private static Logger logger = Logger.getLogger(ParametriRicercaAreaPreventivoPage.class);
	private ResetParametriRicercaCommand resetParametriRicercaCommand;
	private SearchAreaPreventivoCommand searchAreaPreventivoCommand;
	private IPreventiviBD preventiviBD;

	/**
	 * Costruttore.
	 */
	public ParametriRicercaAreaPreventivoPage() {
		super(PAGE_ID, new ParametriRicercaAreaPreventivoForm(new ParametriRicercaAreaPreventivo()));
		new PanjeaFormGuard(getBackingFormPage().getFormModel(), getSearchAreaPreventivoCommand());
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(preventiviBD, "preventiviBD deve essere assegnato ");
		Assert.notNull(aziendaCorrente, "aziendaCorrente deve essere assegnato");
		ParametriRicercaAreaPreventivoForm parametriRicercaAreaPreventiviForm = (ParametriRicercaAreaPreventivoForm) getBackingFormPage();
		parametriRicercaAreaPreventiviForm.setPreventiviBD(preventiviBD);
		parametriRicercaAreaPreventiviForm.setAziendaCorrente(aziendaCorrente);
	}

	@Override
	protected AbstractCommand[] getCommand() {
		AbstractCommand[] commands = new AbstractCommand[] { getResetParametriRicercaCommand(),
				getSearchAreaPreventivoCommand() };
		return commands;
	}

	@Override
	public AbstractCommand getEditorNewCommand() {
		return getResetParametriRicercaCommand();
	}

	@Override
	public AbstractCommand getEditorSaveCommand() {
		return getSearchAreaPreventivoCommand();
	}

	/**
	 * vedi bug 440 Sovrascrivo il metodo ritornando null per evitare il normale comportamento di this
	 * (FormBackedDialogPageEditor). Questo metodo e' usato nel metodo onNew() e se ritorna il valore di default
	 * (getBackingFormPage().getFormObject()) viene lanciata una propertychange e quindi la page
	 * RisultatiRicercaControlloMovimentoContabilitaPage esegue una ricerca e solo dopo viene lanciato il propertychange
	 * con oggetto a null per azzerare le righe visualizzate (vedi doExecuteCommand di this.resetRicercaCommand).
	 *
	 * @return new object
	 */
	@Override
	protected Object getNewEditorObject() {
		return null;
	}

	/**
	 * @return the resetParametriRicercaCommand
	 **/
	protected ResetParametriRicercaCommand getResetParametriRicercaCommand() {
		if (resetParametriRicercaCommand == null) {
			resetParametriRicercaCommand = new ResetParametriRicercaCommand();
		}
		return resetParametriRicercaCommand;
	}

	/**
	 * @return the searchAreaOrdineCommand
	 */
	protected SearchAreaPreventivoCommand getSearchAreaPreventivoCommand() {
		if (searchAreaPreventivoCommand == null) {
			searchAreaPreventivoCommand = new SearchAreaPreventivoCommand();
		}
		return searchAreaPreventivoCommand;
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public void loadData() {
		if (!((ParametriRicercaAreaPreventivo) getBackingFormPage().getFormObject()).isEffettuaRicerca()) {
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
	 * @return <code>true</code>
	 */
	@Override
	public boolean onSave() {
		return true;
	}

	/**
	 * Sovrascrivo questo metodo per non eseguire l'undo command premendo ctrl + Z che è abilitato di default nella form
	 * backed dialog page.
	 *
	 * @return <code>true</code>
	 */
	@Override
	public boolean onUndo() {
		return true;
	}

	@Override
	public void refreshData() {
		loadData();
	}

	/**
	 * @param aziendaCorrente
	 *            The aziendaCorrente to set.
	 */
	public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
		this.aziendaCorrente = aziendaCorrente;
	}

	/**
	 * @param preventiviBD
	 *            the preventiviBD to set
	 */
	public void setPreventiviBD(IPreventiviBD preventiviBD) {
		this.preventiviBD = preventiviBD;
	}

}
