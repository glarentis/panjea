package it.eurotn.panjea.ordini.rich.editors.righeordine;

import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;
import it.eurotn.panjea.ordini.util.RigaOrdineDTO;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.eurotn.rich.control.table.JecAggregateTable;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.awt.Cursor;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DragSource;
import java.util.HashSet;
import java.util.Set;

import javax.activation.ActivationDataFlavor;
import javax.activation.DataHandler;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;

import com.jidesoft.grid.TableModelWrapperUtils;

public class RigheOrdineTransferHandler extends TransferHandler {

	private static final long serialVersionUID = 46707784283225592L;
	private DataFlavor localObjectFlavor = null;
	private RigaOrdineDTO[] transferedObjects = null;
	private AbstractTablePageEditor<?> pageLifecycleAdvisor;
	private IOrdiniDocumentoBD ordiniDocumentoBD;
	private int index;

	/**
	 * Costruttore.
	 * 
	 * @param ordiniDocumentoBD
	 *            bd documento
	 * @param pageLifecycleAdvisor
	 *            pagina contenente la tabella
	 */
	public RigheOrdineTransferHandler(final IOrdiniDocumentoBD ordiniDocumentoBD,
			final AbstractTablePageEditor<?> pageLifecycleAdvisor) {
		this.ordiniDocumentoBD = ordiniDocumentoBD;
		this.pageLifecycleAdvisor = pageLifecycleAdvisor;
		localObjectFlavor = new ActivationDataFlavor(RigaOrdineDTO[].class, DataFlavor.javaJVMLocalObjectMimeType,
				"indici delle righe da spostare");
	}

	@Override
	public boolean canImport(TransferHandler.TransferSupport info) {
		JTable t = (JTable) info.getComponent();
		boolean b = info.isDrop() && info.isDataFlavorSupported(localObjectFlavor);
		t.setCursor(b ? DragSource.DefaultMoveDrop : DragSource.DefaultMoveNoDrop);
		return b;
	}

	@Override
	protected Transferable createTransferable(JComponent c) {
		@SuppressWarnings("unchecked")
		JecAggregateTable<RigaOrdineDTO> table = (JecAggregateTable<RigaOrdineDTO>) c;
		transferedObjects = table.getSelectedObjects().toArray(new RigaOrdineDTO[] {});
		return new DataHandler(transferedObjects, localObjectFlavor.getMimeType());
	}

	@Override
	protected void exportDone(JComponent c, Transferable t, int act) {
		pageLifecycleAdvisor.loadData();
		@SuppressWarnings("unchecked")
		JecAggregateTable<RigaOrdineDTO> table = (JecAggregateTable<RigaOrdineDTO>) c;
		table.getSelectionModel().setSelectionInterval(index, index);
	}

	@Override
	public int getSourceActions(JComponent c) {
		return TransferHandler.COPY_OR_MOVE;
	}

	@Override
	public boolean importData(TransferHandler.TransferSupport info) {
		@SuppressWarnings("unchecked")
		JecAggregateTable<RigaOrdineDTO> table = (JecAggregateTable<RigaOrdineDTO>) info.getComponent();
		JTable.DropLocation dl = (JTable.DropLocation) info.getDropLocation();
		index = dl.getRow();
		try {
			int actualRow = table.getActualRowAt(index);
			@SuppressWarnings("unchecked")
			DefaultBeanTableModel<RigaOrdineDTO> tableModel = (DefaultBeanTableModel<RigaOrdineDTO>) TableModelWrapperUtils
					.getActualTableModel(table.getModel(), DefaultBeanTableModel.class);

			RigaOrdineDTO[] righeDaSpostareDTO = (RigaOrdineDTO[]) info.getTransferable().getTransferData(
					localObjectFlavor);
			Set<Integer> righeDaSpostare = new HashSet<Integer>();
			for (RigaOrdineDTO rigaOrdineDTO : righeDaSpostareDTO) {
				righeDaSpostare.add(rigaOrdineDTO.getId());
			}
			Integer rowDest = null;
			if (actualRow < tableModel.getRowCount()) {
				rowDest = tableModel.getObject(actualRow).getId();
			}
			ordiniDocumentoBD.spostaRighe(righeDaSpostare, rowDest);
		} catch (Exception ufe) {
			ufe.printStackTrace();
		}
		index = table.getActualRowAt(index);
		table.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		return true;
	}
}
