package it.eurotn.panjea.magazzino.rich.forms.listino;

import it.eurotn.panjea.magazzino.domain.ScaglioneListinoStorico;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.converter.ConverterContext;

public class StoriaPrezzoTableModel extends DefaultBeanTableModel<ScaglioneListinoStorico> {

	private static final long serialVersionUID = 8836654187295810373L;

	private static ConverterContext dateContext = new ConverterContext("dataContext", "dd/MM/yyyy HH:mm:ss");
	private static ConverterContext numberContext = new NumberWithDecimalConverterContext(2, "€ ");

	/**
	 * Costruttore.
	 *
	 * @param numeroDecimaliPrezzo
	 *            numero decimali per la visualizzazione del prezzo
	 */
	public StoriaPrezzoTableModel(final int numeroDecimaliPrezzo) {
		super("storiaPrezzoTableModel", new String[] { "data", "numeroVersione", "prezzo", "note" },
				ScaglioneListinoStorico.class);
		numberContext.setUserObject(numeroDecimaliPrezzo);
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {
		switch (column) {
		case 0:
			return dateContext;
		case 2:
			ScaglioneListinoStorico storico = getElementAt(row);
			numberContext.setUserObject(storico.getNumeroDecimaliPrezzo());
			return numberContext;
		default:
			return null;
		}
	}

}