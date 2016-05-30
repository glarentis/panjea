/**
 *
 */
package it.eurotn.panjea.magazzino.rich.editors.contratto;

import it.eurotn.panjea.magazzino.domain.Contratto;
import it.eurotn.panjea.magazzino.domain.RigaContratto;
import it.eurotn.panjea.magazzino.rich.bd.IContrattoBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.table.EditFrame;

import java.util.Collection;
import java.util.List;

import javax.swing.JComponent;

import org.springframework.richclient.settings.Settings;

import com.jidesoft.docking.DockContext;
import com.jidesoft.docking.DockableFrame;

/**
 * Table page che gestisce la visualizzazione e l'inserimento delle righe del contratto.
 * 
 * @author fattazzo
 */
public class RigheContrattoTablePage extends AbstractTablePageEditor<RigaContratto> {

	// private class TableCustomizerPage extends JideTableCustomizer {
	// @Override
	// protected void initSortableTable(SortableTable sortableTable) {
	// super.initSortableTable(sortableTable);
	// SortableTableModel tableModel = (SortableTableModel) TableModelWrapperUtils.getActualTableModel(
	// sortableTable.getModel(), SortableTableModel.class);
	// tableModel.setAlwaysUseComparators(true);
	// }
	// }

	public static final String PAGE_ID = "righeContrattoTablePage";
	private IContrattoBD contrattoBD = null;
	private Contratto contrattoCorrente = null;
	private boolean isContrattoChanged = true;

	/**
	 * Costruttore di default.
	 */
	public RigheContrattoTablePage() {
		super(PAGE_ID, new RigheContrattoTableModel(PAGE_ID));
	}

	@Override
	protected JComponent createControl() {
		JComponent result = super.createControl();

		DockableFrame frameTable = holderPanel.getDockingManager().getFrame(TABELLA_FRAME_ID);
		frameTable.getContext().setInitMode(DockContext.STATE_FRAMEDOCKED);
		frameTable.getContext().setInitSide(DockContext.DOCK_SIDE_WEST);
		frameTable.getContext().setCurrentDockSide(DockContext.DOCK_SIDE_WEST);

		DockableFrame frameEdit = holderPanel.getDockingManager().getFrame(EditFrame.EDIT_FRAME_ID);
		frameEdit.getContext().setInitMode(DockContext.STATE_FRAMEDOCKED);
		frameEdit.getContext().setInitSide(DockContext.DOCK_SIDE_EAST);
		frameEdit.getContext().setCurrentDockSide(DockContext.DOCK_SIDE_EAST);
		holderPanel.getDockingManager().resetToDefault();
		return result;
	}

	@Override
	protected EditFrame<RigaContratto> createEditFrame() {
		EditFrame<RigaContratto> editFrame = super.createEditFrame();
		editFrame.getEditViewTypeCommand().setVisible(false);
		editFrame.getContext().setInitMode(DockContext.STATE_FRAMEDOCKED);
		editFrame.getContext().setInitSide(DockContext.DOCK_SIDE_EAST);
		editFrame.getContext().setCurrentDockSide(DockContext.DOCK_SIDE_EAST);
		holderPanel.getDockingManager().resetToDefault();
		return editFrame;
	}

	/**
	 * Controlla se i contratti passati come parametro sono cambiati.
	 * 
	 * @param contrattoOld
	 *            contratto old
	 * @param contrattoNew
	 *            contratto new
	 * @return <code>true</code> se sono cambiati
	 */
	private boolean isContrattoChanged(Contratto contrattoOld, Contratto contrattoNew) {
		boolean isFirstOpen = contrattoOld == null;
		boolean isNew = contrattoOld != null && contrattoNew.isNew();
		boolean isChanged = contrattoOld != null && !contrattoOld.isNew() && !contrattoNew.isNew()
				&& !contrattoOld.getId().equals(contrattoNew.getId());
		return isFirstOpen || isNew || isChanged;
	}

	@Override
	public Collection<RigaContratto> loadTableData() {
		List<RigaContratto> righe = null;

		if (isContrattoChanged) {
			righe = contrattoBD.caricaRigheContratto(contrattoCorrente);
		}

		return righe;
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public Collection<RigaContratto> refreshTableData() {
		return contrattoBD.caricaRigheContratto(contrattoCorrente);
	}

	@Override
	public void restoreState(Settings settings) {
		super.restoreState(settings);
		DockableFrame editFrame = holderPanel.getDockingManager().getFrame(EditFrame.EDIT_FRAME_ID);
		editFrame.getContext().setInitMode(DockContext.STATE_FRAMEDOCKED);
		editFrame.getContext().setInitSide(DockContext.DOCK_SIDE_EAST);
		editFrame.getContext().setCurrentDockSide(DockContext.DOCK_SIDE_EAST);
		holderPanel.getDockingManager().resetToDefault();
	}

	/**
	 * 
	 * @param contrattoBD
	 *            setter of contrattoBD
	 */
	public void setContrattoBD(IContrattoBD contrattoBD) {
		this.contrattoBD = contrattoBD;
	}

	@Override
	public void setFormObject(Object object) {
		org.springframework.util.Assert.isInstanceOf(Contratto.class, object);
		isContrattoChanged = isContrattoChanged(contrattoCorrente, (Contratto) object);
		contrattoCorrente = (Contratto) object;
		((RigaContrattoPage) getEditPages().get(EditFrame.DEFAULT_OBJECT_CLASS_NAME)).setContratto(contrattoCorrente);
		setReadOnly(contrattoCorrente.isNew());
	}
}
