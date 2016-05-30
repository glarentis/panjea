package it.eurotn.panjea.anagrafica.rich.search.documenti;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.rich.converter.I18NConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.converter.ConverterContext;

public class TipoDocumentoSearchObjectTableModel extends DefaultBeanTableModel<TipoDocumento> {

	private static final long serialVersionUID = -5100443062762561203L;

	private static I18NConverterContext context;

	static {
		context = new I18NConverterContext();
	}

	/**
	 * Costruttore.
	 * 
	 * @param modelId
	 *            id del modello
	 * @param columnPropertyNames
	 *            nome delle colonne
	 * @param classe
	 *            classe gestita
	 */
	public TipoDocumentoSearchObjectTableModel(final String modelId, final String[] columnPropertyNames,
			final Class<TipoDocumento> classe) {
		super(modelId, columnPropertyNames, classe);
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
