/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.editors.listino;

import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.forms.listino.ListinoForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.command.AbstractCommand;

/**
 * Visualizza l'anagrafica di un listino.
 * 
 * @author fattazzo
 */
public class ListinoPage extends FormBackedDialogPageEditor implements InitializingBean {

	public static final String PAGE_ID = "listinoPage";

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	/**
	 * Costruttore.
	 */
	public ListinoPage() {
		super(PAGE_ID, new ListinoForm());
	}

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@Override
	protected Object doDelete() {
		Listino listino = (Listino) getBackingFormPage().getFormObject();
		magazzinoAnagraficaBD.cancellaListino(listino);
		return listino;
	}

	@Override
	protected Object doSave() {
		Listino listino = (Listino) getBackingFormPage().getFormObject();
		listino = magazzinoAnagraficaBD.salvaListino(listino);
		return listino;
	}

	@Override
	protected AbstractCommand[] getCommand() {
		AbstractCommand[] abstractCommands = new AbstractCommand[] { toolbarPageEditor.getNewCommand(),
				toolbarPageEditor.getLockCommand(), toolbarPageEditor.getSaveCommand(),
				toolbarPageEditor.getUndoCommand(), toolbarPageEditor.getDeleteCommand() };
		return abstractCommands;
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
	 * 
	 * @param magazzinoAnagraficaBD
	 *            The anagraficaMagazzinoBD to set
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}
}
