package it.eurotn.panjea.magazzino.rich.editors.importazioni;

import it.eurotn.panjea.magazzino.importer.util.RigaImport;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.grid.HierarchicalTableModel;

public class ImportazioniAMRigaTableModel extends DefaultBeanTableModel<RigaImport> implements HierarchicalTableModel {

	private static final long serialVersionUID = -4736078919439842853L;

	/**
	 * Costruttore.
	 */
	public ImportazioniAMRigaTableModel() {
		super("importazioniAMRigaTableModel", new String[] { "valid", "numeroRiga", "codiceArticolo",
				"codiceArticoloEntita", "codiceLotto", "dataScadenzaLotto", "qta", "prezzoUnitario", "prezzoNetto",
				"sconto" }, RigaImport.class);
	}

	@Override
	public Object getChildValueAt(int arg0) {
		return null;
	}

	@Override
	public boolean hasChild(int arg0) {
		return false;
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
