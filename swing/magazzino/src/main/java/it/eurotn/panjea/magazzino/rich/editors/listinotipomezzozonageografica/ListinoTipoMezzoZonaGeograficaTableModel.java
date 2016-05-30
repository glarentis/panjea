package it.eurotn.panjea.magazzino.rich.editors.listinotipomezzozonageografica;

import it.eurotn.panjea.magazzino.domain.moduloprezzo.ListinoTipoMezzoZonaGeografica;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.converter.ConverterContext;

public class ListinoTipoMezzoZonaGeograficaTableModel extends DefaultBeanTableModel<ListinoTipoMezzoZonaGeografica> {

	private static final long serialVersionUID = -1342467781305022001L;
	private final ConverterContext numberContext = new NumberWithDecimalConverterContext();

	/**
	 * Costruttore di default.
	 * 
	 * @param id
	 *            l'id della pagina
	 */
	public ListinoTipoMezzoZonaGeograficaTableModel(final String id) {
		super(id, new String[] { "prezzo", "tipoMezzoTrasporto", "zonaGeografica" },
				ListinoTipoMezzoZonaGeografica.class);
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {
		switch (column) {
		case 0:
			numberContext.setUserObject(2);
			return numberContext;
		default:
			return null;
		}
	}

}
