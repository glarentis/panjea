/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.editors.listino.exception;

import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.grid.NavigableModel;

/**
 * @author fattazzo
 * 
 */
public class RigheListinoListiniCollegatiTableModel extends DefaultBeanTableModel<RigaListinoCollegataPM> implements
		NavigableModel {

	private static final long serialVersionUID = 5798985481279737093L;

	/**
	 * Costruttore.
	 * 
	 * @param modelId
	 * @param columnPropertyNames
	 * @param classe
	 */
	public RigheListinoListiniCollegatiTableModel() {
		super("righeListinoListiniCollegatiTableModel", new String[] { "aggiornaListiniCollegati",
				"rigaListino.articolo", "rigaListino.versioneListino.listino", "numeroListiniCollegati" },
				RigaListinoCollegataPM.class);
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return column == 0;
	}

	@Override
	public boolean isNavigableAt(int row, int column) {
		return column == 0;
	}

	@Override
	public boolean isNavigationOn() {
		return true;
	}
}
