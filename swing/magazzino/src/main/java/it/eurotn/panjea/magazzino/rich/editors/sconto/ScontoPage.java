/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.editors.sconto;

import it.eurotn.panjea.magazzino.domain.Sconto;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.forms.ScontoForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

/**
 * @author fattazzo
 * 
 */
public class ScontoPage extends FormBackedDialogPageEditor {

	public static final String PAGE_ID = "scontoPage";

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	/**
	 * Costruttore.
	 */
	public ScontoPage() {
		super(PAGE_ID, new ScontoForm());
	}

	@Override
	protected Object doDelete() {
		magazzinoAnagraficaBD.cancellaSconto((Sconto) getBackingFormPage().getFormObject());
		return getBackingFormPage().getFormObject();
	}

	@Override
	protected Object doSave() {
		Sconto sconto = (Sconto) getBackingFormPage().getFormObject();
		return magazzinoAnagraficaBD.salvaSconto(sconto);
	}

	@Override
	protected AbstractCommand[] getCommand() {
		return toolbarPageEditor.getDefaultCommand(true);
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
	 * @param magazzinoAnagraficaBD
	 *            the magazzinoAnagraficaBD
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}

}
