package it.eurotn.panjea.magazzino.rich.editors.righemagazzino;

import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.util.RigaMagazzinoDTO;
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

public class RigheMagazzinoTransferHandler extends TransferHandler {
	private static final long serialVersionUID = 2888882476718808624L;

	private DataFlavor localObjectFlavor = null;
	private RigaMagazzinoDTO[] transferedObjects = null;
	private IMagazzinoDocumentoBD magazzinoDocumentoBD;
	private AbstractTablePageEditor<?> pageLifecycleAdvisor;

	private int index;

	/**
	 * Costruttore.
	 * 
	 * @param magazzinoDocumentoBD
	 *            bd documento
	 * @param pageLifecycleAdvisor
	 *            pagina contenente la tabella
	 */
	public RigheMagazzinoTransferHandler(final IMagazzinoDocumentoBD magazzinoDocumentoBD,
			final AbstractTablePageEditor<?> pageLifecycleAdvisor) {
		this.magazzinoDocumentoBD = magazzinoDocumentoBD;
		this.pageLifecycleAdvisor = pageLifecycleAdvisor;
		localObjectFlavor = new ActivationDataFlavor(RigaMagazzinoDTO[].class, DataFlavor.javaJVMLocalObjectMimeType,
				"indici delle righe da spostare");
	}

	@Override
	public boolean canImport(TransferHandler.TransferSupport info) {
		JTable t = (JTable) info.getComponent();
		boolean b = info.isDrop() && info.isDataFlavorSupported(localObjectFlavor);
		t.setCursor(b ? DragSource.DefaultMoveDrop : DragSource.DefaultMoveNoDrop);
		return b;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Transferable createTransferable(JComponent c) {
		JecAggregateTable<RigaMagazzinoDTO> table = (JecAggregateTable<RigaMagazzinoDTO>) c;
		transferedObjects = table.getSelectedObjects().toArray(new RigaMagazzinoDTO[] {});
		return new DataHandler(transferedObjects, localObjectFlavor.getMimeType());
	}

	@Override
	protected void exportDone(JComponent c, Transferable t, int act) {
		pageLifecycleAdvisor.loadData();
		@SuppressWarnings("unchecked")
		JecAggregateTable<RigaMagazzinoDTO> table = (JecAggregateTable<RigaMagazzinoDTO>) c;
		table.getSelectionModel().setSelectionInterval(index, index);
	}

	@Override
	public int getSourceActions(JComponent c) {
		return TransferHandler.COPY_OR_MOVE;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean importData(TransferHandler.TransferSupport info) {
		JecAggregateTable<RigaMagazzinoDTO> table = (JecAggregateTable<RigaMagazzinoDTO>) info.getComponent();
		JTable.DropLocation dl = (JTable.DropLocation) info.getDropLocation();
		index = dl.getRow();
		int indexFrom = table.getSelectedRow();

		try {
			int actualRow = table.getActualRowAt(index);
			DefaultBeanTableModel<RigaMagazzinoDTO> tableModel = (DefaultBeanTableModel<RigaMagazzinoDTO>) TableModelWrapperUtils
					.getActualTableModel(table.getModel(), DefaultBeanTableModel.class);

			RigaMagazzinoDTO[] righeDaSpostareDTO = (RigaMagazzinoDTO[]) info.getTransferable().getTransferData(
					localObjectFlavor);
			Set<Integer> righeDaSpostare = new HashSet<Integer>();
			for (RigaMagazzinoDTO rigaMagazzinoDTO : righeDaSpostareDTO) {
				righeDaSpostare.add(rigaMagazzinoDTO.getId());
			}
			Integer rowDest = null;
			if (actualRow < tableModel.getRowCount()) {
				rowDest = tableModel.getObject(actualRow).getId();
			}
			magazzinoDocumentoBD.spostaRighe(righeDaSpostare, rowDest);
		} catch (Exception ufe) {
			ufe.printStackTrace();
		}
		index = table.getActualRowAt(index);

		// Quando sposto la riga in basso l'indice finale va modificato
		// perché quello che ho qui è il valore con ancora la riga presente nella posizione originale.
		// Dal momento che quella riga verrà rimossa l'indice corretto è una posizione in meno.
		if (index > indexFrom) {
			index--;
		}

		table.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		return true;
	}
}