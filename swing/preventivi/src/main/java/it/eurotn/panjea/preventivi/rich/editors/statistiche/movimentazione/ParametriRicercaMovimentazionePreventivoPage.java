package it.eurotn.panjea.preventivi.rich.editors.statistiche.movimentazione;

import it.eurotn.panjea.preventivi.rich.bd.IPreventiviBD;
import it.eurotn.panjea.preventivi.util.parametriricerca.ParametriRicercaMovimentazione;
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

public class ParametriRicercaMovimentazionePreventivoPage extends FormBackedDialogPageEditor implements
InitializingBean {

	private class LoadMovimentazioneCommand extends ActionCommand {

		private static final String COMMAND_ID = "searchCommand";

		/**
		 * Costruttore.
		 */
		public LoadMovimentazioneCommand() {
			super(COMMAND_ID);
			CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
					CommandConfigurer.class);
			this.setSecurityControllerId(getPageEditorId() + COMMAND_ID);
			c.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			getBackingFormPage().getFormModel().commit();
			ParametriRicercaMovimentazione parametriRicercaMovimentazione = (ParametriRicercaMovimentazione) getBackingFormPage()
					.getFormModel().getFormObject();
			parametriRicercaMovimentazione.setEffettuaRicerca(true);
			getForm().getBindingFactory().getFormModel().commit();
			ParametriRicercaMovimentazionePreventivoPage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED,
					null, parametriRicercaMovimentazione);
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
			ParametriRicercaMovimentazionePreventivoPage.logger.debug("--> Reset command");
			// lancio null Object_changed per ripulire i risultati
			ParametriRicercaMovimentazionePreventivoPage.this.toolbarPageEditor.getNewCommand().execute();
			ParametriRicercaMovimentazionePreventivoPage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED,
					null, null);
			((ParametriRicercaMovimentazionePreventiviForm) getBackingFormPage()).getFormModel().setReadOnly(false);
		}

	}

	public static final String PAGE_ID = "parametriRicercaMovimentazionePreventivoPage";
	private static Logger logger = Logger.getLogger(ParametriRicercaMovimentazionePreventivoPage.class);

	private IPreventiviBD preventiviBD;

	private ResetParametriRicercaCommand resetParametriRicercaCommand;
	private LoadMovimentazioneCommand loadValorizzazioneCommand;

	/**
	 * Costruttore.
	 */
	public ParametriRicercaMovimentazionePreventivoPage() {
		super(PAGE_ID, new ParametriRicercaMovimentazionePreventiviForm(new ParametriRicercaMovimentazione()));
		new PanjeaFormGuard(getBackingFormPage().getFormModel(), getLoadMovimentazioneCommand());
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		org.springframework.util.Assert.notNull(preventiviBD, "preventiviBD cannot be null !");
		ParametriRicercaMovimentazionePreventiviForm form = (ParametriRicercaMovimentazionePreventiviForm) getBackingFormPage();
		form.setPreventiviBD(preventiviBD);
	}

	@Override
	protected AbstractCommand[] getCommand() {
		AbstractCommand[] commands = new AbstractCommand[] { getResetParametriRicercaCommand(),
				getLoadMovimentazioneCommand() };
		return commands;
	}

	@Override
	public AbstractCommand getEditorNewCommand() {
		return getResetParametriRicercaCommand();
	}

	@Override
	public AbstractCommand getEditorSaveCommand() {
		return getLoadMovimentazioneCommand();
	}

	/**
	 * @return LoadMovimentazioneCommand
	 */
	protected LoadMovimentazioneCommand getLoadMovimentazioneCommand() {
		if (loadValorizzazioneCommand == null) {
			loadValorizzazioneCommand = new LoadMovimentazioneCommand();
		}
		return loadValorizzazioneCommand;
	}

	/**
	 * vedi bug 440 Sovrascrivo il metodo ritornando null per evitare il normale comportamento di this
	 * (FormBackedDialogPageEditor). Questo metodo e' usato nel metodo onNew() e se ritorna il valore di default
	 * (getBackingFormPage().getFormObject()) viene lanciata una propertychange e quindi la page
	 * RisultatiRicercaControlloMovimentoContabilitaPage esegue una ricerca e solo dopo viene lanciato il propertychange
	 * con oggetto a null per azzerare le righe visualizzate (vedi doExecuteCommand di this.resetRicercaCommand).
	 *
	 * @return nuovo oggetto
	 */
	@Override
	protected Object getNewEditorObject() {
		return null;
	}

	/**
	 * @return ResetParametriRicercaCommand
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
		ParametriRicercaMovimentazione parametriRicercaMovimentazione = (ParametriRicercaMovimentazione) getForm()
				.getFormObject();
		if (!parametriRicercaMovimentazione.isEffettuaRicerca()) {
			// richiamo la execute del ResetParametriRicercaCommand perche' appena apro
			// la pagina posso subito inserire i parametri
			getResetParametriRicercaCommand().execute();
		} else {
			// richiamo la execute di EseguiRicercaCommand per effettuare
			// immediatamente la ricerca all'apertuta della Page
			getLoadMovimentazioneCommand().execute();
		}
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
	 * @return boolean
	 */
	@Override
	public boolean onSave() {
		return true;
	}

	/**
	 * Sovrascrivo questo metodo per non eseguire l'undo command premendo ctrl + Z che e' abilitato di default nella
	 * form backed dialog page.
	 *
	 * @return boolean
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
	 * @param preventiviBD
	 *            the preventiviBD to set
	 */
	public void setPreventiviBD(IPreventiviBD preventiviBD) {
		this.preventiviBD = preventiviBD;
	}

}
