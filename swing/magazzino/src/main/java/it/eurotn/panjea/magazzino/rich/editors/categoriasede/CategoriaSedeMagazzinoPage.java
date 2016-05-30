package it.eurotn.panjea.magazzino.rich.editors.categoriasede;

import it.eurotn.panjea.magazzino.domain.CategoriaSedeMagazzino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.forms.categoriasede.CategoriaSedeMagazzinoForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

/**
 * 
 * @author angelo
 * 
 */
public class CategoriaSedeMagazzinoPage extends FormBackedDialogPageEditor {

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	/**
	 * Costruttore.
	 * 
	 * @param pageID
	 *            id della pagina
	 */
	public CategoriaSedeMagazzinoPage(final String pageID) {
		super(pageID, new CategoriaSedeMagazzinoForm(new CategoriaSedeMagazzino()));
	}

	@Override
	protected Object doDelete() {
		magazzinoAnagraficaBD.cancellaCategorieSediMagazzino((CategoriaSedeMagazzino) getBackingFormPage()
				.getFormObject());
		return getBackingFormPage().getFormObject();
	}

	@Override
	protected Object doSave() {
		CategoriaSedeMagazzino categoriaSedeMagazzino = (CategoriaSedeMagazzino) this.getForm().getFormObject();
		return magazzinoAnagraficaBD.salvaCategoriaSedeMagazzino(categoriaSedeMagazzino);
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
	 *            magazzinoAnagraficaBD to set
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}

}
