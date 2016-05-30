package it.eurotn.panjea.magazzino.rich.forms.rigamagazzino.componenti;

import it.eurotn.panjea.magazzino.domain.IRigaArticoloDocumento;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;

import org.springframework.richclient.util.RcpSupport;

public class RigheArticoliComponentiDialog extends PanjeaTitledPageApplicationDialog {

	private IRigaArticoloDocumento rigaArticoloDocumento = null;

	public static final String STR_ZERI = "0000000000000000000000000";

	private RigheArticoliComponentiDistintaTablePage righeArticoliDistintaTablePage;

	/**
	 * Costruttore.
	 *
	 */
	public RigheArticoliComponentiDialog() {
		super();
		righeArticoliDistintaTablePage = RcpSupport.getBean(RigheArticoliComponentiDistintaTablePage.PAGE_ID);
		setDialogPage(righeArticoliDistintaTablePage);
	}

	/**
	 * Restituisce la rigaArticoloDocumento della dialog page e quindi con i valori di quantità inseriti dall'utente.
	 *
	 * @return the rigaArticoloDocumento to get
	 */
	public IRigaArticoloDocumento getRigaArticoloDocumento() {
		if (rigaArticoloDocumento == null) {
			return null;
		}
		return righeArticoliDistintaTablePage.getRigaArticoloDocumento();
	}

	/**
	 * @return table page utilizzata dalla tabella
	 */
	private AbstractTablePageEditor<?> getTablePage() {
		return (AbstractTablePageEditor<?>) getDialogPage();
	}

	@Override
	protected void onAboutToShow() {
		final IRigaArticoloDocumento rigaArticoloDocumentoDaModificare = (IRigaArticoloDocumento) PanjeaSwingUtil
				.cloneObject(rigaArticoloDocumento);
		AbstractTablePageEditor<?> tablePage = getTablePage();
		tablePage.preSetFormObject(rigaArticoloDocumentoDaModificare);
		tablePage.setFormObject(rigaArticoloDocumentoDaModificare);
		tablePage.postSetFormObject(rigaArticoloDocumentoDaModificare);
		tablePage.loadData();
		tablePage.getTable().getTable().changeSelection(0, 1, false, false);
		tablePage.getTable().getTable().requestFocusInWindow();
	}

	@Override
	protected void onCancel() {
		super.onCancel();
		rigaArticoloDocumento = null;
	}

	@Override
	protected boolean onFinish() {
		// se sono in editazione e confermo, senza fermare il cell editing, il valore in editazione non viene
		// considerato
		if (getTablePage().getTable().getTable().getCellEditor() != null) {
			getTablePage().getTable().getTable().getCellEditor().stopCellEditing();
		}
		rigaArticoloDocumento = (IRigaArticoloDocumento) getTablePage().getManagedObject(null);
		return true;
	}

	/**
	 * @param openReadOnly
	 *            setta la tabella in readOnly
	 */
	public void setReadOnly(boolean openReadOnly) {
		AbstractTablePageEditor<?> tablePage = (AbstractTablePageEditor<?>) getDialogPage();
		tablePage.setReadOnly(openReadOnly);
	}

	/**
	 * @param rigaArticoloDocumento
	 *            the rigaArticoloDocumento to set
	 */
	public void setRigaArticoloDocumento(IRigaArticoloDocumento rigaArticoloDocumento) {
		this.rigaArticoloDocumento = rigaArticoloDocumento;
	}

}
