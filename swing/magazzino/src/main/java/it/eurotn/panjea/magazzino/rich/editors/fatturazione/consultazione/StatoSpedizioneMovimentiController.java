/**
 *
 */
package it.eurotn.panjea.magazzino.rich.editors.fatturazione.consultazione;

import it.eurotn.panjea.documenti.domain.StatoSpedizione;
import it.eurotn.panjea.magazzino.util.MovimentoFatturazioneDTO;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.eurotn.rich.control.table.JecAggregateTable;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.jidesoft.grid.TableModelWrapperUtils;

/**
 * @author fattazzo
 *
 */
public class StatoSpedizioneMovimentiController extends MouseAdapter implements KeyListener {

	private MovimentiFatturazioneTablePage movimentiFatturazioneTablePage;

	private long lastEvent;

	private JecAggregateTable<MovimentoFatturazioneDTO> movimentiTable;
	private DefaultBeanTableModel<MovimentoFatturazioneDTO> movimentiTableModel;

	/**
	 * Costruttore.
	 *
	 * @param movimentiFatturazioneTablePage
	 *            pagina dei movimenti
	 */
	@SuppressWarnings("unchecked")
	public StatoSpedizioneMovimentiController(final MovimentiFatturazioneTablePage movimentiFatturazioneTablePage) {
		super();
		this.movimentiFatturazioneTablePage = movimentiFatturazioneTablePage;

		this.movimentiTable = (JecAggregateTable<MovimentoFatturazioneDTO>) this.movimentiFatturazioneTablePage
				.getTable().getTable();
		movimentiTable.addMouseListener(this);
		movimentiTable.addKeyListener(this);

		movimentiTableModel = (DefaultBeanTableModel<MovimentoFatturazioneDTO>) TableModelWrapperUtils
				.getActualTableModel(movimentiTable.getModel());
	}

	/**
	 * Cambia lo stato di spedizione dei movimenti agli indici riga di riferimento.
	 *
	 * @param indiciRighe
	 *            indici riga
	 */
	private void changeStatoSpedizione(int[] indiciRighe) {
		for (Integer riga : indiciRighe) {
			MovimentoFatturazioneDTO movimento = movimentiTableModel.getObject(riga);
			StatoSpedizione statoSpedizione = movimentiFatturazioneTablePage.getMagazzinoDocumentoBD()
					.cambiaStatoSpedizioneMovimento(movimento.getAreaMagazzino());

			movimentiTableModel.getObject(riga).getAreaMagazzino().setStatoSpedizione(statoSpedizione);
			movimento.getAreaMagazzino().setStatoSpedizione(statoSpedizione);

			movimentiTableModel.fireTableDataChanged();
			// movimentiTable.repaint();
		}
	}

	@Override
	public void keyPressed(KeyEvent keyevent) {
		if (keyevent.getKeyCode() == 32) {
			int rowIndex = movimentiTable.getSelectedRow();
			int columnIndex = movimentiTable.getSelectedColumn();
			if (columnIndex == -1 || rowIndex == -1) {
				return;
			}
			if (movimentiTable.getActualColumn(columnIndex) == 0) {
				int[] indiciRighe = movimentiTable.getActualRowsAt(rowIndex, columnIndex);
				changeStatoSpedizione(indiciRighe);
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
		if (movimentiTable.checkColumn(mouseevent, 0)) {
			int[] indiciRighe = movimentiTable.getActualRowsAt(movimentiTable.rowAtPoint(mouseevent.getPoint()),
					movimentiTable.columnAtPoint(mouseevent.getPoint()));
			changeStatoSpedizione(indiciRighe);
		}
	}
}
