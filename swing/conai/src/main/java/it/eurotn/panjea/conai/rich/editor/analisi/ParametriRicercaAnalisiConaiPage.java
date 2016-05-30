package it.eurotn.panjea.conai.rich.editor.analisi;

import it.eurotn.panjea.conai.util.parametriricerca.ParametriRicercaAnalisi;
import it.eurotn.panjea.rich.forms.PanjeaFormGuard;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;
import it.eurotn.rich.form.PanjeaAbstractForm;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

public class ParametriRicercaAnalisiConaiPage extends FormBackedDialogPageEditor {

	private class LoadAnalisiCommand extends ActionCommand {

		private static final String COMMAND_ID = "searchCommand";

		/**
		 * Costruttore.
		 *
		 */
		public LoadAnalisiCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			getBackingFormPage().getFormModel().commit();
			ParametriRicercaAnalisi parametri = (ParametriRicercaAnalisi) getBackingFormPage().getFormModel()
					.getFormObject();
			parametri.setEffettuaRicerca(true);
			getForm().getBindingFactory().getFormModel().commit();
			ParametriRicercaAnalisiConaiPage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
					parametri);
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
			RcpSupport.configure(this);

		}

		@Override
		protected void doExecuteCommand() {
			// lancio null Object_changed per ripulire i risultati
			ParametriRicercaAnalisiConaiPage.this.toolbarPageEditor.getNewCommand().execute();
			ParametriRicercaAnalisiConaiPage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null, null);
			((ParametriRicercaAnalisiConaiForm) getBackingFormPage()).getFormModel().setReadOnly(false);
		}

	}

	public static final String PAGE_ID = "parametriRicercaAnalisiConaiPage";

	private ResetParametriRicercaCommand resetParametriRicercaCommand;
	private LoadAnalisiCommand loadAnalisiCommand;

	/**
	 * Costruttore.
	 */
	public ParametriRicercaAnalisiConaiPage() {
		super(PAGE_ID, new ParametriRicercaAnalisiConaiForm());
		new PanjeaFormGuard(getBackingFormPage().getFormModel(), getLoadAnalisiCommand());
	}

	@Override
	protected AbstractCommand[] getCommand() {
		AbstractCommand[] commands = new AbstractCommand[] { getResetParametriRicercaCommand(), getLoadAnalisiCommand() };
		return commands;
	}

	@Override
	public AbstractCommand getEditorNewCommand() {
		return getResetParametriRicercaCommand();
	}

	@Override
	public AbstractCommand getEditorSaveCommand() {
		return getLoadAnalisiCommand();
	}

	/**
	 * @return loadAnalisiCommand
	 */
	protected LoadAnalisiCommand getLoadAnalisiCommand() {
		if (loadAnalisiCommand == null) {
			loadAnalisiCommand = new LoadAnalisiCommand();
		}
		return loadAnalisiCommand;
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
		getResetParametriRicercaCommand().execute();
	}

	@Override
	public void onPostPageOpen() {
		ParametriRicercaAnalisi parametri = (ParametriRicercaAnalisi) getForm().getFormObject();
		if (!parametri.isEffettuaRicerca()) {
			// richiamo la execute del ResetParametriRicercaCommand perche' appena apro
			// la pagina posso subito inserire i parametri
			getResetParametriRicercaCommand().execute();
		} else {
			// richiamo la execute di EseguiRicercaCommand per effettuare
			// immediatamente la ricerca all'apertuta della Page
			getLoadAnalisiCommand().execute();
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

}
