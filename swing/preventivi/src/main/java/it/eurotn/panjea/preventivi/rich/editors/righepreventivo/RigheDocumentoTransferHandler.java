package it.eurotn.panjea.preventivi.rich.editors.righepreventivo;

import it.eurotn.panjea.preventivi.domain.documento.interfaces.IAreaDocumentoTestata;
import it.eurotn.panjea.preventivi.rich.bd.IRigheBD;
import it.eurotn.panjea.preventivi.util.interfaces.IRigaDTO;
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

public class RigheDocumentoTransferHandler<E extends IAreaDocumentoTestata, T extends IRigaDTO> extends
		TransferHandler {

	private static final long serialVersionUID = 1L;
	private DataFlavor localObjectFlavor = null;
	private T[] transferedObjects = null;
	private AbstractTablePageEditor<?> pageLifecycleAdvisor;
	private IRigheBD<E> righeBD;
	private int index;
	private final T[] arrayVuoto;

	/**
	 * Costruttore.
	 * 
	 * @param righeBD
	 *            bd documento
	 * @param pageLifecycleAdvisor
	 *            pagina contenente la tabella
	 * 
	 * @param arrayVuoto
	 *            un array vuoto di righe
	 */
	public RigheDocumentoTransferHandler(final IRigheBD<E> righeBD,
			final AbstractTablePageEditor<?> pageLifecycleAdvisor, final T[] arrayVuoto) {
		this.righeBD = righeBD;
		this.pageLifecycleAdvisor = pageLifecycleAdvisor;
		this.arrayVuoto = arrayVuoto;
		localObjectFlavor = new ActivationDataFlavor(arrayVuoto.getClass(), DataFlavor.javaJVMLocalObjectMimeType,
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
		JecAggregateTable<T> table = (JecAggregateTable<T>) c;
		transferedObjects = table.getSelectedObjects().toArray(arrayVuoto);
		return new DataHandler(transferedObjects, localObjectFlavor.getMimeType());
	}

	@Override
	protected void exportDone(JComponent c, Transferable t, int act) {
		pageLifecycleAdvisor.loadData();
		@SuppressWarnings("unchecked")
		JecAggregateTable<T> table = (JecAggregateTable<T>) c;
		table.getSelectionModel().setSelectionInterval(index, index);
	}

	@Override
	public int getSourceActions(JComponent c) {
		return TransferHandler.COPY_OR_MOVE;
	}

	@Override
	public boolean importData(TransferHandler.TransferSupport info) {
		@SuppressWarnings("unchecked")
		JecAggregateTable<T> table = (JecAggregateTable<T>) info.getComponent();
		JTable.DropLocation dl = (JTable.DropLocation) info.getDropLocation();
		index = dl.getRow();
		try {
			int actualRow = table.getActualRowAt(index);
			@SuppressWarnings("unchecked")
			DefaultBeanTableModel<T> tableModel = (DefaultBeanTableModel<T>) TableModelWrapperUtils
					.getActualTableModel(table.getModel(), DefaultBeanTableModel.class);

			@SuppressWarnings("unchecked")
			T[] righeDaSpostareDTO = (T[]) info.getTransferable().getTransferData(localObjectFlavor);
			Set<Integer> righeDaSpostare = new HashSet<Integer>();
			for (T rigaDTO : righeDaSpostareDTO) {
				righeDaSpostare.add(rigaDTO.getId());
			}
			Integer rowDest = null;
			if (actualRow < tableModel.getRowCount()) {
				rowDest = tableModel.getObject(actualRow).getId();
			}
			righeBD.spostaRighe(righeDaSpostare, rowDest);
		} catch (Exception ufe) {
			ufe.printStackTrace();
		}
		index = table.getActualRowAt(index);
		table.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		return true;
	}
}
