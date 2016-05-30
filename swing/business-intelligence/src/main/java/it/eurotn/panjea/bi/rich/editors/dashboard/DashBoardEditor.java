package it.eurotn.panjea.bi.rich.editors.dashboard;

import it.eurotn.panjea.bi.domain.dashboard.DashBoard;
import it.eurotn.panjea.bi.rich.bd.IBusinessIntelligenceBD;
import it.eurotn.rich.dialog.JecCompositeDialogPage;
import it.eurotn.rich.editors.AbstractEditorDialogPage;

public class DashBoardEditor extends AbstractEditorDialogPage {

	private DashBoardCompositePage page;

	private IBusinessIntelligenceBD businessIntelligenceBD;

	@Override
	protected JecCompositeDialogPage createCompositeDialogPage() {
		page = new DashBoardCompositePage();
		return page;
	}

	@Override
	public String getDisplayName() {
		return ((DashBoard) getEditorInput()).getNome();
	}

	@Override
	public String getId() {
		return ((DashBoard) getEditorInput()).getNome();
	}

	@Override
	public void initialize(Object editorObject) {
		DashBoard dashBoard = (DashBoard) editorObject;
		dashBoard = businessIntelligenceBD.caricaDashBoard(dashBoard.getNome());
		super.initialize(dashBoard);
	}

	/**
	 * @param businessIntelligenceBD
	 *            the businessIntelligenceBD to set
	 */
	public void setBusinessIntelligenceBD(IBusinessIntelligenceBD businessIntelligenceBD) {
		this.businessIntelligenceBD = businessIntelligenceBD;
	}

}
