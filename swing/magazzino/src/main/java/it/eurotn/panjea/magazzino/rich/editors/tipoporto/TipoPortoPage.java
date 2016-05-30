package it.eurotn.panjea.magazzino.rich.editors.tipoporto;

import it.eurotn.panjea.magazzino.domain.TipoPorto;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.forms.tipoporto.TipoPortoForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

/**
 * 
 * @author angelo
 * 
 */
public class TipoPortoPage extends FormBackedDialogPageEditor {

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	/**
	 * Costruttore.
	 * 
	 * @param pageID
	 *            id della pagina
	 */
	public TipoPortoPage(final String pageID) {
		super(pageID, new TipoPortoForm(new TipoPorto()));
	}

	@Override
	protected Object doDelete() {
		magazzinoAnagraficaBD.cancellaTipoPorto((TipoPorto) getBackingFormPage().getFormObject());
		return getBackingFormPage().getFormObject();
	}

	@Override
	protected Object doSave() {
		TipoPorto tipoPorto = (TipoPorto) this.getForm().getFormObject();
		return magazzinoAnagraficaBD.salvaTipoPorto(tipoPorto);
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
