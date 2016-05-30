package it.eurotn.panjea.magazzino.rich.editors.articolo.statistiche;

import it.eurotn.panjea.magazzino.util.DisponibilitaArticolo;

import java.sql.Date;
import java.util.List;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class DisponibilitaTableModel extends AbstractTableModel {
	private List<DisponibilitaArticolo> disponibilita;
	private double giacenza;

	/**
	 *
	 * @param disponibilita
	 *            disponibilita da visualizzare.
	 * @param giacenza
	 *            giacenza attuale dell'articolo.
	 */
	public DisponibilitaTableModel(final List<DisponibilitaArticolo> disponibilita, final Double giacenza) {
		super();
		this.disponibilita = disponibilita;
		if (giacenza == null) {
			this.giacenza = 0.0;
		} else {
			this.giacenza = giacenza;
		}
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
		case 0:
		case 1:
		case 2:
		case 3:
		case 4:
			return double.class;
		case 5:
			return Date.class;
		case 6:
		case 7:
			return Integer.class;
		default:
			return super.getColumnClass(columnIndex);
		}
	}

	@Override
	public int getColumnCount() {
		return 8;
	}

	@Override
	public String getColumnName(int column) {
		switch (column) {
		case 0:
			return "CAR INC";
		case 1:
			return "FABB INC";
		case 2:
			return "CAR";
		case 3:
			return "FABB";
		case 4:
			return "DISP";
		case 5:
			return "DATA";
		case 6:
			return "MESE";
		case 7:
			return "ANNO";
		default:
			return super.getColumnName(column);
		}
	}

	@Override
	public int getRowCount() {
		return disponibilita.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return disponibilita.get(rowIndex).getCaricoTotale();
		case 1:
			return disponibilita.get(rowIndex).getFabbisognoTotale();
		case 2:
			return disponibilita.get(rowIndex).getCarico();
		case 3:
			return disponibilita.get(rowIndex).getFabbisogno();
		case 4:
			return disponibilita.get(rowIndex).getDisponibilita(giacenza);
		case 5:
			return disponibilita.get(rowIndex).getDataConsegna();
		case 6:
			return disponibilita.get(rowIndex).getMese();
		case 7:
			return disponibilita.get(rowIndex).getAnno();
		default:
			return "test";
		}
	}
}
