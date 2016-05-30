/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.editors.noteautomatiche;

import it.eurotn.panjea.anagrafica.domain.NotaAutomatica;
import it.eurotn.panjea.rich.converter.I18NConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.converter.ConverterContext;

/**
 * @author fattazzo
 * 
 */
public class GestioneNoteAutomaticheTableModel extends DefaultBeanTableModel<NotaAutomatica> {

	private static final long serialVersionUID = -882576728199640972L;

	private static I18NConverterContext context;

	static {
		context = new I18NConverterContext();
	}

	/**
	 * Costruttore.
	 * 
	 * @param modelId
	 * @param columnPropertyNames
	 * @param classe
	 */
	public GestioneNoteAutomaticheTableModel() {
		super("gestioneNoteAutomaticheTableModel", new String[] { "classeTipoDocumento", "tipoDocumento", "entita",
				"sedeEntita", "dataInizio", "dataFine", "ripetiAnnualmente", "nota" }, NotaAutomatica.class);
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {
		switch (column) {
		case 0:
			return context;
		default:
			return null;
		}
	}
}
