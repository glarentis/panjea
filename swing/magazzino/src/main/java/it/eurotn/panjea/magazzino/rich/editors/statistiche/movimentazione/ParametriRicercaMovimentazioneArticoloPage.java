/**
 *
 */
package it.eurotn.panjea.magazzino.rich.editors.statistiche.movimentazione;

import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.magazzino.rich.forms.statistiche.movimentazione.ParametriRicercaMovimentazioneArticoloForm;
import it.eurotn.panjea.magazzino.util.ParametriRicercaMovimentazioneArticolo;
import it.eurotn.panjea.rich.forms.PanjeaFormGuard;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;
import it.eurotn.rich.form.PanjeaAbstractForm;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandConfigurer;

/**
 * @author fattazzo
 *
 */
public class ParametriRicercaMovimentazioneArticoloPage extends FormBackedDialogPageEditor implements InitializingBean {

	private class LoadMovimentazioneCommand extends ActionCommand {

		private static final String COMMAND_ID = "searchCommand";

		/**
		 * Costruttore.
		 *
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
			ParametriRicercaMovimentazioneArticolo parametriRicercaMovimentazioneArticolo = (ParametriRicercaMovimentazioneArticolo) getBackingFormPage()
					.getFormModel().getFormObject();
			parametriRicercaMovimentazioneArticolo.setEffettuaRicerca(true);
			getForm().getBindingFactory().getFormModel().commit();
			ParametriRicercaMovimentazioneArticoloPage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED,
					null, parametriRicercaMovimentazioneArticolo);
		}
	}

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
			// lancio null Object_changed per ripulire i risultati
			ParametriRicercaMovimentazioneArticoloPage.this.toolbarPageEditor.getNewCommand().execute();
			ParametriRicercaMovimentazioneArticoloPage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED,
					null, null);
			((ParametriRicercaMovimentazioneArticoloForm) getBackingFormPage()).getFormModel().setReadOnly(false);
		}

	}

	public static final String PAGE_ID = "parametriRicercaMovimentazioneArticoloPage";
	private IAnagraficaBD anagraficaBD;
	private AziendaCorrente aziendaCorrente;

	private ResetParametriRicercaCommand resetParametriRicercaCommand;
	private LoadMovimentazioneCommand loadValorizzazioneCommand;

	/**
	 * Costruttore.
	 *
	 */
	public ParametriRicercaMovimentazioneArticoloPage() {
		super(PAGE_ID, new ParametriRicercaMovimentazioneArticoloForm(new ParametriRicercaMovimentazioneArticolo()));
		new PanjeaFormGuard(getBackingFormPage().getFormModel(), getLoadMovimentazioneCommand());
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		ParametriRicercaMovimentazioneArticoloForm form = (ParametriRicercaMovimentazioneArticoloForm) getBackingFormPage();
		org.springframework.util.Assert.notNull(anagraficaBD, "anagraficaBD cannot be null !");
		org.springframework.util.Assert.notNull(aziendaCorrente, "aziendaCorrente cannot be null !");
		form.setAziendaCorrente(aziendaCorrente);
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
	 * @return loadValorizzazioneCommand
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
	 * @return object
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

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public void loadData() {
	}

	@Override
	public void onPostPageOpen() {
		ParametriRicercaMovimentazioneArticolo parametriRicercaMovimentazioneArticolo = (ParametriRicercaMovimentazioneArticolo) getForm()
				.getFormObject();
		if (!parametriRicercaMovimentazioneArticolo.isEffettuaRicerca()) {
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
	 * è abilitata di default nella form backed dialog page.
	 *
	 * @return <code>true</code>
	 */
	@Override
	public boolean onSave() {
		return true;
	}

	/**
	 * Sovrascrivo questo metodo per non eseguire l'undo command premendo ctrl + Z che � abilitato di default nella form
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
	 * @param anagraficaBD
	 *            the anagraficaBD to set
	 */
	public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
		this.anagraficaBD = anagraficaBD;
	}

	/**
	 * @param aziendaCorrente
	 *            the aziendaCorrente to set
	 */
	public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
		this.aziendaCorrente = aziendaCorrente;
	}

}
