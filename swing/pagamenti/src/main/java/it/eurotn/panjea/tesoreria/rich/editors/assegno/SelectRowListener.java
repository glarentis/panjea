package it.eurotn.panjea.tesoreria.rich.editors.assegno;

import it.eurotn.panjea.tesoreria.domain.AreaAssegno;
import it.eurotn.panjea.tesoreria.rich.bd.ITesoreriaBD;
import it.eurotn.panjea.tesoreria.rich.bd.TesoreriaBD;
import it.eurotn.panjea.tesoreria.rich.commands.OpenPreviewAssegnoCommand;
import it.eurotn.panjea.tesoreria.util.AssegnoDTO;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;

import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.TableModelWrapperUtils;

public class SelectRowListener extends MouseAdapter implements KeyListener {

	private JTable table;
	private DefaultBeanTableModel<AssegnoDTO> tableModel;
	private long lastEvent;

	private OpenPreviewAssegnoCommand openPreviewAssegnoCommand;

	private ITesoreriaBD tesoreriaBD;

	/**
	 * Costruttore.
	 * 
	 * @param table
	 *            tabella da agganciare al listener
	 */
	@SuppressWarnings("unchecked")
	public SelectRowListener(final JTable table) {
		super();
		this.table = table;
		table.addMouseListener(this);
		table.addKeyListener(this);
		tableModel = (DefaultBeanTableModel<AssegnoDTO>) TableModelWrapperUtils.getActualTableModel(table.getModel());
		openPreviewAssegnoCommand = new OpenPreviewAssegnoCommand();
		tesoreriaBD = RcpSupport.getBean(TesoreriaBD.BEAN_ID);
	}

	@Override
	public void keyPressed(KeyEvent keyevent) {
	}

	@Override
	public void keyReleased(KeyEvent keyevent) {
		if (keyevent.getKeyCode() == 32) {
			int rowIndex = table.getSelectedRow();
			int columnIndex = table.getSelectedColumn();
			if (columnIndex == -1 || rowIndex == -1) {
				return;
			}

			if (table.getColumnName(columnIndex).equals(RcpSupport.getMessage("numeroAssegno"))) {
				int rowIndexConver = table.convertRowIndexToModel(table.convertRowIndexToModel(rowIndex));

				AssegnoDTO assegnoDTO = tableModel.getElementAt(rowIndexConver);
				AreaAssegno areaAssegno = tesoreriaBD.caricaImmagineAssegno(assegnoDTO.getAreaAssegno());
				openPreviewAssegnoCommand.addParameter(OpenPreviewAssegnoCommand.IMMAGINE_ASSEGNO_PARAMETER,
						areaAssegno.getImmagineAssegno());
				openPreviewAssegnoCommand.execute();

				tableModel.fireTableDataChanged();
				table.setRowSelectionInterval(rowIndex, rowIndex);
			}

		}
	}

	@Override
	public void keyTyped(KeyEvent keyevent) {
	}

	@Override
	public void mouseClicked(MouseEvent mouseevent) {
		// Non so perchè l'evento viene chiamato due volte. (ved. MouseClicked
		// su eventMulticaster).
		// controllo che l'evento sia generato in tempi diversi

		if (mouseevent.getWhen() == lastEvent) {
			return;
		} else {
			lastEvent = mouseevent.getWhen();
		}

		int rowIndex = table.rowAtPoint(mouseevent.getPoint());
		int columnIndex = table.columnAtPoint(mouseevent.getPoint());

		if (table.getColumnName(columnIndex).equals(RcpSupport.getMessage("numeroAssegno"))) {
			int rowIndexConvert = table.convertRowIndexToModel(table.convertRowIndexToModel(rowIndex));

			AssegnoDTO assegnoDTO = tableModel.getElementAt(rowIndexConvert);
			AreaAssegno areaAssegno = tesoreriaBD.caricaImmagineAssegno(assegnoDTO.getAreaAssegno());
			openPreviewAssegnoCommand.addParameter(OpenPreviewAssegnoCommand.IMMAGINE_ASSEGNO_PARAMETER,
					areaAssegno.getImmagineAssegno());
			openPreviewAssegnoCommand.execute();

			tableModel.fireTableDataChanged();
			table.setRowSelectionInterval(rowIndex, rowIndex);
		}
	}
}
