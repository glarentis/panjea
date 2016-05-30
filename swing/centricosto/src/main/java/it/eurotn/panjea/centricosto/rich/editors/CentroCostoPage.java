package it.eurotn.panjea.centricosto.rich.editors;

import it.eurotn.panjea.centricosto.domain.CentroCosto;
import it.eurotn.panjea.centricosto.rich.bd.ICentriCostoBD;
import it.eurotn.panjea.centricosto.rich.forms.CentroCostoForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

public class CentroCostoPage extends FormBackedDialogPageEditor {

	public static final String PAGE_ID = "centroCostoPage";
	private ICentriCostoBD centriCostoBD;

	/**
	 * Costruttore.
	 * 
	 * @param parentPageId
	 * @param backingFormPage
	 */
	public CentroCostoPage() {
		super(PAGE_ID, new CentroCostoForm());
	}

	@Override
	protected Object doDelete() {
		centriCostoBD.cancellaCentroCosto((CentroCosto) getBackingFormPage().getFormObject());
		return getBackingFormPage().getFormObject();
	}

	@Override
	protected Object doSave() {
		return centriCostoBD.salvaCentroCosto((CentroCosto) getBackingFormPage().getFormObject());
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
		return false;
	}

	@Override
	public void refreshData() {
	}

	/**
	 * @param centriCostoBD
	 *            The centriCostoBD to set.
	 */
	public void setCentriCostoBD(ICentriCostoBD centriCostoBD) {
		this.centriCostoBD = centriCostoBD;
	}

}
