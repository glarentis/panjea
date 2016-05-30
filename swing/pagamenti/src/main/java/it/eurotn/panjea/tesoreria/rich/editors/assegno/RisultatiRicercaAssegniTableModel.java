/**
 * 
 */
package it.eurotn.panjea.tesoreria.rich.editors.assegno;

import it.eurotn.panjea.tesoreria.util.AssegnoDTO;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.EditorContext;

/**
 * @author leonardo
 */
public class RisultatiRicercaAssegniTableModel extends DefaultBeanTableModel<AssegnoDTO> {

	private static final long serialVersionUID = -6139033363359978879L;

	/**
	 * Costruttore.
	 * 
	 * @param modelId
	 *            id del model della tabella
	 */
	public RisultatiRicercaAssegniTableModel(final String modelId) {
		super(modelId, new String[] { "statoCarrello", "areaAssegno.documento.dataDocumento",
				"areaAssegno.documento.codice", "areaAssegno.documento.tipoDocumento", "entitaDocumento",
				"areaAssegno.documento.totale", "areaAssegno.numeroAssegno", "areaAssegno.abi", "areaAssegno.cab",
				"entita", "areaAssegno.rapportoBancarioAzienda" }, AssegnoDTO.class);
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {
		return null;
	}

	@Override
	public EditorContext getEditorContextAt(int row, int column) {
		switch (column) {
		case 6:
			return NumeroAssegnoCellRenderer.NUMERO_ASSEGNO_CONTEXT;
		default:
			return super.getEditorContextAt(row, column);
		}
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		switch (column) {
		case 0:
			return true;
		default:
			return false;
		}
	}

	@Override
	public void setValueAt(Object value, int row, int col) {
		super.setValueAt(value, row, col);
		fireTableRowsUpdated(row, col);
	}

}
