/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.editors.schedearticolo;

import it.eurotn.panjea.magazzino.util.ArticoloRicerca;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

/**
 * @author fattazzo
 * 
 */
public class ArticoliSchedeTableModel extends DefaultBeanTableModel<ArticoloRicerca> {

	private static final long serialVersionUID = -50862283164481979L;

	/**
	 * Costruttore.
	 * 
	 */
	public ArticoliSchedeTableModel() {
		super("articoliSchedeTableModel", new String[] { "codice", "descrizione" }, ArticoloRicerca.class);
	}

}
