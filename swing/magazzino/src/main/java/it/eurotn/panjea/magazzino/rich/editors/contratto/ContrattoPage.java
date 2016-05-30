/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.editors.contratto;

import it.eurotn.panjea.magazzino.domain.Contratto;
import it.eurotn.panjea.magazzino.rich.bd.IContrattoBD;
import it.eurotn.panjea.magazzino.rich.forms.contratto.ContrattoForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.apache.log4j.Logger;
import org.springframework.richclient.command.AbstractCommand;

/**
 * Page per la gestione del Form di {@link ContrattoVariazione}.
 * 
 * @author adriano
 * @version 1.0, 19/giu/08
 */
public class ContrattoPage extends FormBackedDialogPageEditor {

	private static Logger logger = Logger.getLogger(ContrattoPage.class);

	private static final String PAGE_ID = "contrattoPage";

	private IContrattoBD contrattoBD;

	/**
	 * Costruttore di default.
	 */
	public ContrattoPage() {
		super(PAGE_ID, new ContrattoForm());
	}

	@Override
	protected Object doDelete() {
		Contratto contratto = (Contratto) getBackingFormPage().getFormObject();
		contrattoBD.cancellaContratto(contratto);
		return contratto;
	}

	@Override
	protected Object doSave() {
		logger.debug("--> Enter doSave");
		Contratto contratto = (Contratto) getBackingFormPage().getFormObject();
		contratto.setRigheContratto(null);
		contratto = contrattoBD.salvaContratto(contratto, true);
		logger.debug("--> Exit doSave");
		return contratto;
	}

	@Override
	protected AbstractCommand[] getCommand() {
		AbstractCommand[] commands = new AbstractCommand[] { toolbarPageEditor.getNewCommand(),
				toolbarPageEditor.getLockCommand(), toolbarPageEditor.getSaveCommand(),
				toolbarPageEditor.getUndoCommand(), toolbarPageEditor.getDeleteCommand() };
		return commands;
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
	 * @param contrattoBD
	 *            The contrattoBD to set.
	 */
	public void setContrattoBD(IContrattoBD contrattoBD) {
		this.contrattoBD = contrattoBD;
	}
}
