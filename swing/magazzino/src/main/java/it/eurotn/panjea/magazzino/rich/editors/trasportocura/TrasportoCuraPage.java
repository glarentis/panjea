package it.eurotn.panjea.magazzino.rich.editors.trasportocura;

import it.eurotn.panjea.magazzino.domain.TrasportoCura;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.forms.trasportocura.TrasportoCuraForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

public class TrasportoCuraPage extends FormBackedDialogPageEditor {

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	/**
	 * Costruttore. F
	 * 
	 * @param pageID
	 *            id della pagina
	 */
	public TrasportoCuraPage(final String pageID) {
		super(pageID, new TrasportoCuraForm(new TrasportoCura()));
	}

	@Override
	protected Object doDelete() {
		magazzinoAnagraficaBD.cancellaTrasportoCura((TrasportoCura) getBackingFormPage().getFormObject());
		return getBackingFormPage().getFormObject();
	}

	@Override
	protected Object doSave() {
		TrasportoCura trasportoCura = (TrasportoCura) this.getForm().getFormObject();
		return magazzinoAnagraficaBD.salvaTrasportoCura(trasportoCura);
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
