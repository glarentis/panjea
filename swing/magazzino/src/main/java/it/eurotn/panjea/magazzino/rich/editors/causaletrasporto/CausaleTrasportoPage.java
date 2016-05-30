package it.eurotn.panjea.magazzino.rich.editors.causaletrasporto;

import it.eurotn.panjea.magazzino.domain.CausaleTrasporto;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.forms.causaletrasporto.CausaleTrasportoForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

/**
 * 
 * @author angelo
 * 
 */
public class CausaleTrasportoPage extends FormBackedDialogPageEditor {

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	/**
	 * Costruttore.
	 * 
	 * @param pageID
	 *            id della pagina
	 */
	public CausaleTrasportoPage(final String pageID) {
		super(pageID, new CausaleTrasportoForm(new CausaleTrasporto()));
	}

	@Override
	protected Object doDelete() {
		magazzinoAnagraficaBD.cancellaCausaleTrasporto((CausaleTrasporto) getBackingFormPage().getFormObject());
		return getBackingFormPage().getFormObject();
	}

	@Override
	protected Object doSave() {
		CausaleTrasporto causaleTrasporto = (CausaleTrasporto) this.getForm().getFormObject();
		return magazzinoAnagraficaBD.salvaCausaleTraporto(causaleTrasporto);
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
