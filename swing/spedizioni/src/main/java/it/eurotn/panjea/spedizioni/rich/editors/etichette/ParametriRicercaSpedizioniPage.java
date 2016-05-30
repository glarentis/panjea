/**
 *
 */
package it.eurotn.panjea.spedizioni.rich.editors.etichette;

import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.forms.areamagazzino.ParametriRicercaAreaMagazzinoForm;
import it.eurotn.panjea.magazzino.util.ParametriRicercaAreaMagazzino;
import it.eurotn.panjea.rich.forms.PanjeaFormGuard;
import it.eurotn.panjea.spedizioni.util.ParametriRicercaSpedizioni;
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

/**
 * Page per l'inserimento dei {@link ParametriRicercaAreaMagazzino} attraverso il Form
 * {@link ParametriRicercaAreaMagazzinoForm}.
 *
 * @author adriano
 * @version 1.0, 30/set/2008
 */
public class ParametriRicercaSpedizioniPage extends FormBackedDialogPageEditor implements InitializingBean {

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
			ParametriRicercaSpedizioniPage.this.toolbarPageEditor.getNewCommand().execute();
			// ((ParametriRicercaAreaMagazzinoForm)
			// ParametriRicercaAreaMagazzinoPage.this.getBackingFormPage())
			// .selectTipiDocumentoAbilitati();
			ParametriRicercaSpedizioniPage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null, null);
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
			CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
					CommandConfigurer.class);
			this.setSecurityControllerId(getPageEditorId() + COMMAND_ID);
			c.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			ParametriRicercaSpedizioniPage.logger.debug("--> Enter doExecuteCommand");
			ParametriRicercaSpedizioni parametriRicerca = (ParametriRicercaSpedizioni) getBackingFormPage()
					.getFormObject();
			parametriRicerca.setEffettuaRicerca(true);
			getForm().getFormModel().commit();
			ParametriRicercaSpedizioniPage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
					parametriRicerca);
		}
	}

	private static final String PAGE_ID = "parametriRicercaAreaMagazzinoPage";
	private static Logger logger = Logger.getLogger(ParametriRicercaSpedizioniPage.class);
	private ResetParametriRicercaCommand resetParametriRicercaCommand;
	private SearchAreaMagazzinoCommand searchAreaMagazzinoCommand;
	private IMagazzinoDocumentoBD magazzinoDocumentoBD;
	private PanjeaFormGuard searchAreaMagazzinoFormGuard;

	private AziendaCorrente aziendaCorrente;

	/**
	 * Costruttore.
	 */
	public ParametriRicercaSpedizioniPage() {
		super(PAGE_ID, new ParametriRicercaSpedizioniForm(new ParametriRicercaSpedizioni()));
		searchAreaMagazzinoFormGuard = new PanjeaFormGuard(getBackingFormPage().getFormModel(),
				getSearchAreaMagazzinoCommand());
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(magazzinoDocumentoBD, "magazzinoDocumentoBD deve essere assegnato ");
		ParametriRicercaSpedizioniForm parametriRicercaSpedizioniForm = (ParametriRicercaSpedizioniForm) getBackingFormPage();
		parametriRicercaSpedizioniForm.setMagazzinoDocumentoBD(magazzinoDocumentoBD);
		parametriRicercaSpedizioniForm.setAziendaCorrente(aziendaCorrente);
	}

	@Override
	public void dispose() {
		super.dispose();

		searchAreaMagazzinoFormGuard.clear();
		searchAreaMagazzinoFormGuard = null;

		resetParametriRicercaCommand = null;
		searchAreaMagazzinoCommand = null;
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
	 * vedi bug 440 Sovrascrivo il metodo ritornando null per evitare il normale comportamento di this
	 * (FormBackedDialogPageEditor). Questo metodo e' usato nel metodo onNew() e se ritorna il valore di default
	 * (getBackingFormPage().getFormObject()) viene lanciata una propertychange e quindi la page
	 * RisultatiRicercaControlloMovimentoContabilitaPage esegue una ricerca e solo dopo viene lanciato il propertychange
	 * con oggetto a null per azzerare le righe visualizzate (vedi doExecuteCommand di this.resetRicercaCommand)
	 *
	 * @return nuovo oggetto
	 */
	@Override
	protected Object getNewEditorObject() {
		return null;
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
	public boolean isDirty() {
		return false;
	}

	@Override
	public void loadData() {
		if (!((ParametriRicercaSpedizioni) getBackingFormPage().getFormObject()).isEffettuaRicerca()) {
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

	/**
	 * @param aziendaCorrente
	 *            the aziendaCorrente to set
	 */
	public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
		this.aziendaCorrente = aziendaCorrente;
	}

	/**
	 * @param magazzinoDocumentoBD
	 *            The magazzinoDocumentoBD to set.
	 */
	public void setMagazzinoDocumentoBD(IMagazzinoDocumentoBD magazzinoDocumentoBD) {
		this.magazzinoDocumentoBD = magazzinoDocumentoBD;
	}

}
