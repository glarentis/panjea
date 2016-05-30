/**
 *
 */
package it.eurotn.panjea.magazzino.rich.editors.documenti.spedizione;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.table.TableCellEditor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.closure.Closure;

import com.jidesoft.grid.TableModelWrapperUtils;

import it.eurotn.panjea.anagrafica.rich.editors.rubrica.ChooseEmailDialog;
import it.eurotn.panjea.anagrafica.util.RubricaDTO;
import it.eurotn.panjea.documenti.util.MovimentoSpedizioneDTO;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.eurotn.rich.control.table.ITable;
import it.eurotn.rich.control.table.JecAggregateTable;

/**
 * @author fattazzo
 *
 */
public class EmailSpedizioneController extends MouseAdapter implements KeyListener {

	private SpedizioneMovimentiTablePage spedizioneMovimentiTablePage;

	private long lastEvent;

	private JecAggregateTable<MovimentoSpedizioneDTO> movimentiTable;
	private DefaultBeanTableModel<MovimentoSpedizioneDTO> movimentiTableModel;

	/**
	 * Costruttore.
	 *
	 * @param spedizioneMovimentiTablePage
	 *            pagina dei movimenti
	 */
	@SuppressWarnings("unchecked")
	public EmailSpedizioneController(final SpedizioneMovimentiTablePage spedizioneMovimentiTablePage) {
		super();
		this.spedizioneMovimentiTablePage = spedizioneMovimentiTablePage;

		this.movimentiTable = (JecAggregateTable<MovimentoSpedizioneDTO>) this.spedizioneMovimentiTablePage.getTable()
				.getTable();
		movimentiTable.addMouseListener(this);
		movimentiTable.addKeyListener(this);

		movimentiTableModel = (DefaultBeanTableModel<MovimentoSpedizioneDTO>) TableModelWrapperUtils
				.getActualTableModel(movimentiTable.getModel());
	}

	@Override
	public void keyPressed(KeyEvent keyevent) {
		if (keyevent.getKeyCode() == 32) {
			int rowIndex = movimentiTable.getSelectedRow();
			int columnIndex = movimentiTable.getSelectedColumn();
			if (columnIndex == -1 || rowIndex == -1) {
				return;
			}
			if (movimentiTable.getActualColumn(columnIndex) == 6) {
				int[] indiciRighe = movimentiTable.getActualRowsAt(rowIndex, columnIndex);
				selectEmailSpedizione(indiciRighe);
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent mouseevent) {
		if (mouseevent.getWhen() == lastEvent) {
			return;
		} else {
			lastEvent = mouseevent.getWhen();
		}

		int rowIndex = movimentiTable.rowAtPoint(mouseevent.getPoint());
		int columnIndex = movimentiTable.columnAtPoint(mouseevent.getPoint());
		Rectangle rectSelezione = ((ITable<?>) movimentiTable).getIconCellRect(rowIndex, columnIndex);

		if (movimentiTable.checkColumn(mouseevent, 6) && rectSelezione.contains(mouseevent.getPoint())) {
			int[] indiciRighe = movimentiTable.getActualRowsAt(movimentiTable.rowAtPoint(mouseevent.getPoint()),
					movimentiTable.columnAtPoint(mouseevent.getPoint()));

			TableCellEditor cellEditor = movimentiTable.getCellEditor();
			if (cellEditor != null) {
				cellEditor.stopCellEditing();
			}

			selectEmailSpedizione(indiciRighe);
		}
	}

	/**
	 * Cambia lo stato di spedizione dei movimenti agli indici riga di
	 * riferimento.
	 *
	 * @param indiciRighe
	 *            indici riga
	 */
	private void selectEmailSpedizione(int[] indiciRighe) {
		for (final Integer riga : indiciRighe) {
			final MovimentoSpedizioneDTO movimento = movimentiTableModel.getObject(riga);

			ChooseEmailDialog chooseEmailDialog = RcpSupport.getBean("chooseEmailDialog");
			if (movimento.getAreaDocumento().getDocumento().getEntita() != null) {
				chooseEmailDialog.setFilter(
						movimento.getAreaDocumento().getDocumento().getEntita().getAnagrafica().getDenominazione());
			}
			chooseEmailDialog.setHideColumns(new int[] { 1, 4, 6, 9, 10, 11, 14, 15 });
			chooseEmailDialog.setOnSelectAction(new Closure() {

				@Override
				public Object call(Object obj) {
					RubricaDTO rubricaDTO = (RubricaDTO) obj;

					if (!StringUtils.isBlank(rubricaDTO.getIndirizzoMailSpedizione())) {
						movimentiTableModel.getObject(riga)
								.setIndirizzoMailMovimento(rubricaDTO.getIndirizzoMailSpedizione());
						movimento.setIndirizzoMailMovimento(rubricaDTO.getIndirizzoMailSpedizione());

						movimentiTableModel.fireTableDataChanged();
					}
					return null;
				}
			});
			chooseEmailDialog.showDialog();
		}
	}
}
