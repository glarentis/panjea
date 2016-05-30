package it.eurotn.panjea.ordini.rich.editors.evasione;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;
import it.eurotn.panjea.ordini.rich.forms.evasione.ParametriRicercaEvasioneForm;
import it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaEvasione;
import it.eurotn.rich.command.AbstractSearchCommand;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

import javax.swing.AbstractButton;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

public class ParametriRicercaEvasionePage extends FormBackedDialogPageEditor implements InitializingBean {

	private class LoadEvasioneCommand extends AbstractSearchCommand {

		@Override
		protected void doExecuteCommand() {
			getBackingFormPage().getFormModel().commit();
			ParametriRicercaEvasione parametriRicerca = (ParametriRicercaEvasione) getBackingFormPage().getFormModel()
					.getFormObject();
			// Il form è condiviso per la gestione sia degli ordini cliente che fornitore,
			// Qui devo sempre usare il tipo entità cliente
			parametriRicerca.setTipoEntita(TipoEntita.CLIENTE);
			parametriRicerca.setAreeOrdine(null);
			parametriRicerca.setEvadiOrdini(false);
			parametriRicerca.setEffettuaRicerca(true);
			getForm().getBindingFactory().getFormModel().commit();
			ParametriRicercaEvasionePage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
					parametriRicerca);
		}

		@Override
		protected String getPrefixName() {
			return getPageEditorId();
		}

	}

	private class ResetParametriRicercaCommand extends ActionCommand {

		private static final String COMMAND_ID = "resetCommand";

		/**
		 * Costruttore.
		 */
		public ResetParametriRicercaCommand() {
			super(COMMAND_ID);
			this.setSecurityControllerId(getPageEditorId() + COMMAND_ID);
			RcpSupport.configure(this);

		}

		@Override
		protected void doExecuteCommand() {
			ParametriRicercaEvasionePage.this.toolbarPageEditor.getNewCommand().execute();
			ParametriRicercaEvasionePage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
					new ParametriRicercaEvasione());
		}

		@Override
		protected void onButtonAttached(AbstractButton button) {
			super.onButtonAttached(button);
			button.setName("EvasioneOrdiniResetCommand");
		}

	}

	public static final String PAGE_ID = "parametriRicercaEvasionePage";

	private ResetParametriRicercaCommand resetParametriRicercaCommand;
	private LoadEvasioneCommand loadEvasioneCommand;

	private IOrdiniDocumentoBD ordiniDocumentoBD;

	/**
	 * Costruttore.
	 */
	public ParametriRicercaEvasionePage() {
		super(PAGE_ID, new ParametriRicercaEvasioneForm());

		// Il form è condiviso per la gestione sia degli ordini cliente che fornitore,
		// ma viene usato in ImportaRighePage e parametriRicercaEvasionePage, fisso il tipo entità
		// per limitare al solo cliente la selezione di tipo area ordine ed entita'.
		ParametriRicercaEvasione param = new ParametriRicercaEvasione();
		param.setTipoEntita(TipoEntita.CLIENTE);
		setFormObject(param);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		ParametriRicercaEvasioneForm form = (ParametriRicercaEvasioneForm) getBackingFormPage();
		form.setOrdiniDocumentoBD(ordiniDocumentoBD);
	}

	@Override
	protected AbstractCommand[] getCommand() {
		AbstractCommand[] commands = new AbstractCommand[] { getResetParametriRicercaCommand(),
				getLoadEvasioneCommand() };
		return commands;
	}

	@Override
	public AbstractCommand getEditorNewCommand() {
		return getResetParametriRicercaCommand();
	}

	@Override
	public AbstractCommand getEditorSaveCommand() {
		return getLoadEvasioneCommand();
	}

	@Override
	public AbstractCommand getEditorUndoCommand() {
		return null;
	}

	/**
	 * @return the loadEvasioneCommand
	 */
	public LoadEvasioneCommand getLoadEvasioneCommand() {
		if (loadEvasioneCommand == null) {
			loadEvasioneCommand = new LoadEvasioneCommand();
		}
		return loadEvasioneCommand;
	}

	/**
	 * @return the resetParametriRicercaCommand
	 */
	public ResetParametriRicercaCommand getResetParametriRicercaCommand() {
		if (resetParametriRicercaCommand == null) {
			resetParametriRicercaCommand = new ResetParametriRicercaCommand();
		}

		return resetParametriRicercaCommand;
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

	/**
	 * @param ordiniDocumentoBD
	 *            the ordiniDocumentoBD to set
	 */
	public void setOrdiniDocumentoBD(IOrdiniDocumentoBD ordiniDocumentoBD) {
		this.ordiniDocumentoBD = ordiniDocumentoBD;
	}

}
