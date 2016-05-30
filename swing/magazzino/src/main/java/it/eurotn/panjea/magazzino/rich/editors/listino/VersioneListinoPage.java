/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.editors.listino;

import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.magazzino.domain.VersioneListino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

/**
 * @author fattazzo
 * 
 */
public class VersioneListinoPage extends FormBackedDialogPageEditor {

	public static final String PAGE_ID = "versioneListinoPage";

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	private Listino listino;

	/**
	 * Costruttore di default.
	 */
	public VersioneListinoPage() {
		super(PAGE_ID, new VersioneListinoForm());
	}

	@Override
	protected Object doDelete() {
		VersioneListino versioneListino = (VersioneListino) getBackingFormPage().getFormObject();
		magazzinoAnagraficaBD.cancellaVersioneListino(versioneListino);
		return super.doDelete();
	}

	@Override
	protected Object doSave() {
		VersioneListino versioneListino = (VersioneListino) getBackingFormPage().getFormObject();
		boolean updateListino = versioneListino.isNew();
		versioneListino.setListino(listino);
		versioneListino = magazzinoAnagraficaBD.salvaVersioneListino(versioneListino);
		if (updateListino) {
			this.listino = magazzinoAnagraficaBD.caricaListino(listino, true);
			versioneListino.setListino(listino);
		}
		return versioneListino;
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
	 * @param listino
	 *            listino corrente
	 */
	public void setListino(Listino listino) {
		this.listino = listino;
	}

	/**
	 * @param magazzinoAnagraficaBD
	 *            setter
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}
}
