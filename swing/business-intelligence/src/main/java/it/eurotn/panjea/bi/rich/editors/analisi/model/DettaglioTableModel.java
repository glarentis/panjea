package it.eurotn.panjea.bi.rich.editors.analisi.model;

import it.eurotn.panjea.anagrafica.rich.factory.table.CustomSedeEntitaCellRenderer;
import it.eurotn.panjea.anagrafica.rich.factory.table.CustomTipoDocumentoCellRenderer;
import it.eurotn.panjea.magazzino.rich.factory.table.CustomArticoloRenderer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import org.springframework.context.MessageSource;
import org.springframework.richclient.application.Application;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.ContextSensitiveTableModel;
import com.jidesoft.grid.EditorContext;

public class DettaglioTableModel extends AbstractTableModel implements ContextSensitiveTableModel {

	private static final long serialVersionUID = 3017173541980574076L;

	private final MessageSource messageSource = (MessageSource) Application.services().getService(MessageSource.class);

	private List<Object> data;
	private final List<TableModelListener> listeners;
	public static final int NUMBER_OF_COLUMN = 10;

	public static final int MOV_TIPO_DOCUMENTO = 0;
	public static final int MOV_DATA_DOCUMENTO = 1;
	public static final int MOV_NUMERO_DOCUMENTO = 2;
	public static final int MOV_DATA_REGISTRAZIONE = 3;
	public static final int ART_ARTICOLO = 4;
	public static final int SEDI = 5;
	public static final int MOV_QTA = 6;
	public static final int MOV_QTA_FATTURATO = 7;
	public static final int MOV_IMPORTO = 8;
	public static final int MOV_IMPORTO_FATTURATO = 9;
	public static final int MOV_ID_AREAMAGAZZINO = 10;
	private static String[] dbColumn = new String[NUMBER_OF_COLUMN];

	static {
		dbColumn[MOV_TIPO_DOCUMENTO] = "mov.tipoDocumento";
		dbColumn[MOV_DATA_DOCUMENTO] = "mov.dataDocumento";
		dbColumn[MOV_NUMERO_DOCUMENTO] = "mov.numeroDocumento";
		dbColumn[MOV_DATA_REGISTRAZIONE] = "mov.dataRegistrazione";
		dbColumn[ART_ARTICOLO] = "art.articolo";
		dbColumn[SEDI] = "sedi";
		dbColumn[MOV_QTA] = "mov.qta";
		dbColumn[MOV_QTA_FATTURATO] = "mov.qtaFatturato";
		dbColumn[MOV_IMPORTO] = "mov.importo";
		dbColumn[MOV_IMPORTO_FATTURATO] = "mov.fatturato";
		// dbColumn[MOV_ID_AREAMAGAZZINO] = "mov.idAreaMagazzino";
	}

	/**
	 * Costruttore.
	 */
	public DettaglioTableModel() {
		listeners = new ArrayList<TableModelListener>();
	}

	@Override
	public void addTableModelListener(TableModelListener l) {
		listeners.add(l);
	}

	@Override
	public Class<?> getCellClassAt(int arg0, int columnIndex) {
		switch (columnIndex) {
		case MOV_NUMERO_DOCUMENTO:
			return int.class;
		case MOV_DATA_DOCUMENTO:
			return Date.class;
		case MOV_DATA_REGISTRAZIONE:
			return Date.class;
		default:
			return String.class;
		}
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
		case MOV_NUMERO_DOCUMENTO:
			return int.class;
		case MOV_DATA_DOCUMENTO:
			return Date.class;
		case MOV_DATA_REGISTRAZIONE:
			return Date.class;
		case MOV_QTA:
			return Double.class;
		case MOV_QTA_FATTURATO:
			return Double.class;
		case MOV_IMPORTO:
			return BigDecimal.class;
		case MOV_IMPORTO_FATTURATO:
			return BigDecimal.class;
		default:
			return String.class;
		}
	}

	@Override
	public int getColumnCount() {
		return NUMBER_OF_COLUMN;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return messageSource.getMessage(dbColumn[columnIndex], new Object[] {}, Locale.getDefault());
	}

	@Override
	public ConverterContext getConverterContextAt(int arg0, int arg1) {

		return null;
	}

	@Override
	public EditorContext getEditorContextAt(int arg0, int columnIndex) {

		switch (columnIndex) {
		case MOV_TIPO_DOCUMENTO:
			return CustomTipoDocumentoCellRenderer.CONTEXT;
		case ART_ARTICOLO:
			return CustomArticoloRenderer.CONTEXT;
		case SEDI:
			return CustomSedeEntitaCellRenderer.CONTEXT;
		default:
			return null;
		}
	}

	/**
	 * Ritorna l'id dell'area magazzino alla riga corrispondente.
	 * 
	 * @param rowIndex
	 *            indice della riga
	 * @return id area magazzino
	 */
	public int getIdAreaMAgazzino(int rowIndex) {
		Object[] riga = (Object[]) data.get(rowIndex);
		return (Integer) riga[MOV_ID_AREAMAGAZZINO];
	}

	@Override
	public int getRowCount() {
		if (data == null) {
			return 0;
		} else {
			return data.size();
		}
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object[] riga = (Object[]) data.get(rowIndex);
		return riga[columnIndex];
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		listeners.remove(l);
	}

	/**
	 * setter per la lista del modello.
	 * 
	 * @param data
	 *            lista con i dati del dettaglio
	 */
	public void setData(List<Object> data) {
		this.data = data;
		TableModelEvent e = new TableModelEvent(this);
		for (TableModelListener listener : listeners) {
			listener.tableChanged(e);
		}
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

	}

}
