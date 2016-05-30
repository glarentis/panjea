package it.eurotn.panjea.magazzino.rich.editors.categoriacontabile;

import it.eurotn.panjea.magazzino.domain.CategoriaContabileDeposito;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoContabilizzazioneBD;
import it.eurotn.panjea.magazzino.rich.forms.categoriacontabile.CategoriaContabileDepositoForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

/**
 * 
 * @author angelo
 * 
 */

public class CategoriaContabileDepositoPage extends FormBackedDialogPageEditor {

	private IMagazzinoContabilizzazioneBD magazzinoContabilizzazioneBD;

	/**
	 * Costruttore.
	 * 
	 * @param pageID
	 *            id della pagina
	 */
	public CategoriaContabileDepositoPage(final String pageID) {
		super(pageID, new CategoriaContabileDepositoForm(new CategoriaContabileDeposito()));
	}

	@Override
	protected Object doDelete() {
		magazzinoContabilizzazioneBD
				.cancellaCategoriaContabileDeposito((CategoriaContabileDeposito) getBackingFormPage().getFormObject());
		return getBackingFormPage().getFormObject();
	}

	@Override
	protected Object doSave() {
		CategoriaContabileDeposito categoriaContabileDeposito = (CategoriaContabileDeposito) this.getForm()
				.getFormObject();
		return magazzinoContabilizzazioneBD.salvaCategoriaContabileDeposito(categoriaContabileDeposito);
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
