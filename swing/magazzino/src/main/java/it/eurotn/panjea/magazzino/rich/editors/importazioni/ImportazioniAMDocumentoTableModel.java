package it.eurotn.panjea.magazzino.rich.editors.importazioni;

import it.eurotn.panjea.magazzino.importer.util.DocumentoImport;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.HierarchicalTableModel;

public class ImportazioniAMDocumentoTableModel extends DefaultBeanTableModel<DocumentoImport> implements
		HierarchicalTableModel {

	private static final long serialVersionUID = -4736078919439842853L;

	private static ConverterContext numberQtaContext;

	static {
		numberQtaContext = new NumberWithDecimalConverterContext();
		numberQtaContext.setUserObject(new Integer(2));
	}

	/**
	 * Costruttore.
	 */
	public ImportazioniAMDocumentoTableModel() {
		super("importazioniAMDocumentoTableModel", new String[] { "valid", "numeroDocumento", "dataDocumento",
				"codiceEntita", "tipoAreaMagazzino", "pesoNetto", "pesoLordo", "pallet", "numeroColli", "volume" },
				DocumentoImport.class);
	}

	@Override
	public Object getChildValueAt(int arg0) {
		return getElementAt(arg0).getRighe();
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {
		switch (column) {
		case 5:
		case 6:
		case 9:
			return numberQtaContext;
		default:
			return null;
		}
	}

	@Override
	public boolean hasChild(int arg0) {
		return !getElementAt(arg0).getRighe().isEmpty();
	}

	@Override
	public boolean isExpandable(int arg0) {
		return true;
	}

	@Override
	public boolean isHierarchical(int arg0) {
		return true;
	}

}
