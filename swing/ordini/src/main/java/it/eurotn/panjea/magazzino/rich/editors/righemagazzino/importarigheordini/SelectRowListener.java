package it.eurotn.panjea.magazzino.rich.editors.righemagazzino.importarigheordini;

import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCarico;
import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCarico.StatoRiga;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import org.springframework.core.ReflectiveVisitorHelper;
import org.springframework.richclient.dialog.MessageDialog;

import com.jidesoft.grid.DefaultGroupRow;
import com.jidesoft.grid.DefaultGroupTableModel;
import com.jidesoft.grid.GroupTable;
import com.jidesoft.grid.IndexReferenceRow;
import com.jidesoft.grid.TableModelWrapperUtils;

public class SelectRowListener extends MouseAdapter implements KeyListener {
	public class Visit {
		private int rowSelected;

		/**
		 * Recupera lo stato di un riga raggruppata in base allo stato delle righe contenute.<br/>
		 * 
		 * 
		 * @param rowGroup
		 *            riga raggrupata
		 * @return stato della riga gruppo. Se almeno una riga è selezionabile il gruppo diventa selezionabile.
		 */
		private StatoRiga getStatoRigaRaggruppata(DefaultGroupRow rowGroup) {
			StatoRiga statoRiga = StatoRiga.SELEZIONATA;
			for (Object indexRow : rowGroup.getChildren()) {
				if (indexRow instanceof DefaultGroupRow) {
					statoRiga = getStatoRigaRaggruppata((DefaultGroupRow) indexRow);
					if (statoRiga == StatoRiga.SELEZIONABILE) {
						break;
					}
				} else {
					RigaDistintaCarico rigaEvasione = tableModel
							.getObject(((IndexReferenceRow) indexRow).getRowIndex());
					statoRiga = rigaEvasione.getStato();
					if (statoRiga == StatoRiga.SELEZIONABILE) {
						break;
					}
				}
			}
			return statoRiga;
		}

		private void selezionaRiga(RigaDistintaCarico rigaEvasione) {
			StatoRiga stato = rigaEvasione.getStato();
			switch (stato) {
			case NEL_CARRELLO:
				stato = StatoRiga.SELEZIONABILE;
				rigaEvasione.setQtaDaEvadere(0.0);
				break;
			case SELEZIONABILE:
				rigaEvasione.setQtaDaEvadere(rigaEvasione.getQtaResidua());
				if (rigaEvasione.getQtaDaEvadere() != 0.0) {
					stato = StatoRiga.NEL_CARRELLO;
				} else {
					MessageDialog alert = new MessageDialog("Impossibile selezionare",
							"Non posso evadere una riga con qta a zero");
					alert.showDialog();
				}
				break;
			default:
				throw new IllegalStateException();
			}
			rigaEvasione.setStato(stato);
		}

		/**
		 * 
		 * @param rowGroup
		 *            riga raggruppata selezionata
		 */
		private void selezionaRighe(DefaultGroupRow rowGroup) {
			StatoRiga stato = getStatoRigaRaggruppata(rowGroup);
			// switch (stato) {
			// case NEL_CARRELLO:
			// stato = StatoRiga.SELEZIONABILE;
			// break;
			// case SELEZIONABILE:
			// stato = StatoRiga.NEL_CARRELLO;
			// break;
			// default:
			// stato = StatoRiga.SELEZIONABILE;
			// }

			for (Object indexRow : rowGroup.getChildren()) {
				if (indexRow instanceof DefaultGroupRow) {
					selezionaRighe((DefaultGroupRow) indexRow);
				} else {
					RigaDistintaCarico rigaDistintaCarico = tableModel.getObject(((IndexReferenceRow) indexRow)
							.getRowIndex());
					if (stato == rigaDistintaCarico.getStato()) {
						rigaDistintaCarico.setStato(stato);
						selezionaRiga(rigaDistintaCarico);
					}
				}
			}
		}

		/**
		 * @param rowSelected
		 *            The rowSelected to set.
		 */
		public void setRowSelected(int rowSelected) {
			this.rowSelected = rowSelected;
		}

		/**
		 * 
		 * @param forzata
		 *            valore selezionato
		 */
		public void visit(Boolean forzata) {
			RigaDistintaCarico rigaEvasione = tableModel.getObject(TableModelWrapperUtils.getActualRowAt(
					table.getModel(), rowSelected));
			rigaEvasione.setForzata(!rigaEvasione.getForzata());
		}

		/**
		 * 
		 * @param row
		 *            rigaselezionata
		 */
		public void visit(DefaultGroupRow row) {
			selezionaRighe(row);
		}

		/**
		 * 
		 * @param stato
		 *            stato della riga selezionata
		 */
		public void visit(StatoRiga stato) {
			RigaDistintaCarico rigaEvasione = tableModel.getObject(TableModelWrapperUtils.getActualRowAt(
					table.getModel(), rowSelected));
			selezionaRiga(rigaEvasione);
		}

	}

	private GroupTable table;
	private DefaultBeanTableModel<RigaDistintaCarico> tableModel;
	private DefaultGroupTableModel groupTableModel;
	private long lastEvent;
	private ReflectiveVisitorHelper visitorHelper;

	private Visit visitor;

	/**
	 * Costruttore.
	 * 
	 * @param table
	 *            tabella da agganciare al listener
	 */
	@SuppressWarnings("unchecked")
	public SelectRowListener(final GroupTable table) {
		super();
		this.table = table;
		table.addMouseListener(this);
		table.addKeyListener(this);
		tableModel = (DefaultBeanTableModel<RigaDistintaCarico>) TableModelWrapperUtils.getActualTableModel(table
				.getModel());
		groupTableModel = (DefaultGroupTableModel) TableModelWrapperUtils.getActualTableModel(table.getModel(),
				DefaultGroupTableModel.class);
		visitorHelper = new ReflectiveVisitorHelper();
		visitor = new Visit();
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
			int columnIndexConvert = table.convertRowIndexToModel(table.convertColumnIndexToModel(columnIndex));
			int rowIndexConver = table.convertRowIndexToModel(table.convertRowIndexToModel(rowIndex));
			Object value = table.getModel().getValueAt(rowIndexConver, columnIndexConvert);
			visitor.setRowSelected(table.getSelectedRow());
			visitorHelper.invokeVisit(visitor, value);
			groupTableModel.refresh();
			table.setColumnSelectionInterval(columnIndex, columnIndex);
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
		int columnIndexConvert = table.convertRowIndexToModel(table.convertColumnIndexToModel(columnIndex));
		int rowIndexConvert = table.convertRowIndexToModel(table.convertRowIndexToModel(rowIndex));
		Object value = table.getModel().getValueAt(rowIndexConvert, columnIndexConvert);
		// se premo su una riga raggruppata devo controllare di non aver premuto sull'icona per espandere/raggruppare.
		// se la x è <13 sto clikkando sul + per decomprimere/comprimere l'albero.
		Rectangle rectSelezione = null;
		Rectangle rec = table.getCellRect(rowIndex, columnIndex, false);
		if (value instanceof DefaultGroupRow) {
			DefaultGroupRow groupRow = (DefaultGroupRow) value;
			// Creo il rettangolo "utile" per la selezione
			// trovo il rettangolo della cella
			rectSelezione = new Rectangle(rec.x + (16 * (groupRow.getLevel() + 1)), rec.y, 16, 16);
		} else {
			rectSelezione = new Rectangle(rec.x, rec.y, rec.width, 16);
		}
		if (rectSelezione.contains(mouseevent.getPoint())) {
			visitor.setRowSelected(rowIndex);
			visitorHelper.invokeVisit(visitor, value);
			groupTableModel.refresh();
			table.setColumnSelectionInterval(columnIndex, columnIndex);
		}
	}
}
