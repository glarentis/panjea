package it.eurotn.panjea.magazzino.rich.forms.tipoareamagazzinoform;

import it.eurotn.rich.control.table.DefaultBeanEditableTableModel;

/**
 * @author mattia
 */
public class DatoAccompagnatorioTableModel extends
		DefaultBeanEditableTableModel<DatoAccompagnatorioMagazzinoMetaDataPM> {

	private static final long serialVersionUID = -2L;

	/**
	 * Costruttore.
	 */
	public DatoAccompagnatorioTableModel() {
		super("datoAccompagnatorioTableModel", new String[] { "datoAccompagnatorio.name", "richiesto",
				"datoAccompagnatorio.ordinamento" }, DatoAccompagnatorioMagazzinoMetaDataPM.class);
	}

	@Override
	protected boolean isAllowInsert() {
		return false;
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return column > 0;
	}

}
