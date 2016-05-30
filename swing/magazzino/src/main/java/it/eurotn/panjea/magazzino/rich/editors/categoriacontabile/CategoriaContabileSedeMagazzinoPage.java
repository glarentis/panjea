package it.eurotn.panjea.magazzino.rich.editors.categoriacontabile;

import it.eurotn.panjea.magazzino.domain.CategoriaContabileSedeMagazzino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoContabilizzazioneBD;
import it.eurotn.panjea.magazzino.rich.forms.categoriacontabile.CategoriaContabileSedeMagazzinoForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

/**
 * 
 * @author angelo
 * 
 */
public class CategoriaContabileSedeMagazzinoPage extends FormBackedDialogPageEditor {

	private IMagazzinoContabilizzazioneBD magazzinoContabilizzazioneBD;

	/**
	 * Costruttore.
	 * 
	 * @param pageID
	 *            id della pagina
	 */
	public CategoriaContabileSedeMagazzinoPage(final String pageID) {
		super(pageID, new CategoriaContabileSedeMagazzinoForm(new CategoriaContabileSedeMagazzino()));
	}

	@Override
	protected Object doDelete() {
		magazzinoContabilizzazioneBD
				.cancellaCategoriaContabileSedeMagazzino((CategoriaContabileSedeMagazzino) getBackingFormPage()
						.getFormObject());
		return getBackingFormPage().getFormObject();
	}

	@Override
	protected Object doSave() {
		CategoriaContabileSedeMagazzino categoriaContabileSedeMagazzino = (CategoriaContabileSedeMagazzino) this
				.getForm().getFormObject();
		return magazzinoContabilizzazioneBD.salvaCategoriaContabileSedeMagazzino(categoriaContabileSedeMagazzino);
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
	 * @param magazzinoContabilizzazioneBD
	 *            magazzinoContabilizzazioneBD to set
	 */
	public void setMagazzinoContabilizzazioneBD(IMagazzinoContabilizzazioneBD magazzinoContabilizzazioneBD) {
		this.magazzinoContabilizzazioneBD = magazzinoContabilizzazioneBD;
	}

}
