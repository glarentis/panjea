/**
 *
 */
package it.eurotn.panjea.magazzino.rich.editors.statistiche.movimentazione;

import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.forms.statistiche.movimentazione.ParametriRicercaMovimentazioneMagazzinoForm;
import it.eurotn.panjea.magazzino.util.ParametriRicercaMovimentazione;
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

/**
 * @author Leonardo
 *
 */
public class ParametriRicercaMovimentazioneMagazzinoPage extends FormBackedDialogPageEditor implements InitializingBean {

	private class LoadMovimentazioneCommand extends ActionCommand {

		private static final String COMMAND_ID = "searchCommand";

		/**
		 * construtore.
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
			ParametriRicercaMovimentazioneMagazzinoPage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED,
					null, parametriRicercaMovimentazione);
		}
	}

	private class ResetParametriRicercaCommand extends ActionCommand {

		private static final String COMMAND_ID = "resetCommand";

		/**
		 * construtore.
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
			ParametriRicercaMovimentazioneMagazzinoPage.logger.debug("--> Reset command");
			// lancio null Object_changed per ripulire i risultati
			ParametriRicercaMovimentazioneMagazzinoPage.this.toolbarPageEditor.getNewCommand().execute();
			ParametriRicercaMovimentazioneMagazzinoPage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED,
					null, null);
			((ParametriRicercaMovimentazioneMagazzinoForm) getBackingFormPage()).getFormModel().setReadOnly(false);
		}

	}

	public static final String PAGE_ID = "parametriRicercaMovimentazioneMagazzinoPage";
	private static Logger logger = Logger.getLogger(ParametriRicercaMovimentazioneMagazzinoPage.class);
	private IAnagraficaBD anagraficaBD;
	private IMagazzinoDocumentoBD magazzinoDocumentoBD;
	private ResetParametriRicercaCommand resetParametriRicercaCommand;
	private LoadMovimentazioneCommand loadValorizzazioneCommand;

	/**
	 * construtore.
	 */
	public ParametriRicercaMovimentazioneMagazzinoPage() {
		super(PAGE_ID, new ParametriRicercaMovimentazioneMagazzinoForm(new ParametriRicercaMovimentazione()));
		new PanjeaFormGuard(getBackingFormPage().getFormModel(), getLoadMovimentazioneCommand());
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		org.springframework.util.Assert.notNull(anagraficaBD, "anagraficaBD cannot be null !");
		org.springframework.util.Assert.notNull(magazzinoDocumentoBD, "magazzinoDocumentoBD cannot be null !");
		ParametriRicercaMovimentazioneMagazzinoForm form = (ParametriRicercaMovimentazioneMagazzinoForm) getBackingFormPage();
		form.setMagazzinoDocumentoBD(magazzinoDocumentoBD);
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
	 *
	 * @return loadmovimentazione command.
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
	 * con oggetto a null per azzerare le righe visualizzate (vedi doExecuteCommand di this.resetRicercaCommand)
	 *
	 * @return <code>null</code>
	 */
	@Override
	protected Object getNewEditorObject() {
		return null;
	}

	/**
	 *
	 * @return command reset.
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

	/**
	 * carica i dati.
	 */
	@Override
	public void loadData() {
	}

	@Override
	public void onPostPageOpen() {
		ParametriRicercaMovimentazione parametriRicercaMovimentazione = (ParametriRicercaMovimentazione) getForm()
				.getFormObject();

		if (parametriRicercaMovimentazione.isEffettuaRicerca()) {
			// effetto immediatamente la ricerca all'apertuta della Page
			getLoadMovimentazioneCommand().execute();

		} else {
			// richiamo la execute del ResetParametriRicercaCommand perche' appena apro
			// la pagina posso subito inserire i parametri
			getResetParametriRicercaCommand().execute();
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
	 * @param anagraficaBD
	 *            the anagraficaBD to set
	 */
	public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
		this.anagraficaBD = anagraficaBD;
	}

	/**
	 * @param magazzinoDocumentoBD
	 *            The magazzinoDocumentoBD to set.
	 */
	public void setMagazzinoDocumentoBD(IMagazzinoDocumentoBD magazzinoDocumentoBD) {
		this.magazzinoDocumentoBD = magazzinoDocumentoBD;
	}

}
