/**
 *
 */
package it.eurotn.panjea.spedizioni.rich.editors.rendicontazione;

import it.eurotn.panjea.rich.forms.PanjeaFormGuard;
import it.eurotn.panjea.spedizioni.util.ParametriRicercaRendicontazione;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;
import it.eurotn.rich.form.PanjeaAbstractForm;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandConfigurer;

/**
 * Page per l'inserimento dei {@link ParametriRicercaRendicontazione} attraverso il Form
 * {@link ParametriRicercaRendicontazioneForm}.
 *
 * @author adriano
 * @version 1.0, 30/set/2008
 */
public class ParametriRicercaRendicontazionePage extends FormBackedDialogPageEditor {

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
			ParametriRicercaRendicontazionePage.this.toolbarPageEditor.getNewCommand().execute();
			// ((ParametriRicercaAreaMagazzinoForm)
			// ParametriRicercaAreaMagazzinoPage.this.getBackingFormPage())
			// .selectTipiDocumentoAbilitati();
			ParametriRicercaRendicontazionePage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
					null);
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
			ParametriRicercaRendicontazionePage.logger.debug("--> Enter doExecuteCommand");
			ParametriRicercaRendicontazione parametriRicerca = (ParametriRicercaRendicontazione) getBackingFormPage()
					.getFormObject();
			parametriRicerca.setEffettuaRicerca(true);
			getForm().getFormModel().commit();
			ParametriRicercaRendicontazionePage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
					parametriRicerca);
		}
	}

	private static final String PAGE_ID = "parametriRicercaAreaMagazzinoPage";
	private static Logger logger = Logger.getLogger(ParametriRicercaRendicontazionePage.class);
	private ResetParametriRicercaCommand resetParametriRicercaCommand;
	private SearchAreaMagazzinoCommand searchAreaMagazzinoCommand;

	private PanjeaFormGuard searchAreaMagazzinoFormGuard;

	/**
	 * Costruttore.
	 */
	public ParametriRicercaRendicontazionePage() {
		super(PAGE_ID, new ParametriRicercaRendicontazioneForm(new ParametriRicercaRendicontazione()));
		searchAreaMagazzinoFormGuard = new PanjeaFormGuard(getBackingFormPage().getFormModel(),
				getSearchAreaMagazzinoCommand());
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
		if (!((ParametriRicercaRendicontazione) getBackingFormPage().getFormObject()).isEffettuaRicerca()) {
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
