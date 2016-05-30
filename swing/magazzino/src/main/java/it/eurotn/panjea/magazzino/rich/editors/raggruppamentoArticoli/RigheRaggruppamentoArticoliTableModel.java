package it.eurotn.panjea.magazzino.rich.editors.raggruppamentoArticoli;

import it.eurotn.panjea.magazzino.domain.RigaRaggruppamentoArticoli;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.converter.ConverterContext;

public class RigheRaggruppamentoArticoliTableModel extends DefaultBeanTableModel<RigaRaggruppamentoArticoli> {
	private static final long serialVersionUID = 1159068613225932567L;
	private static ConverterContext numberEditorContext = new NumberWithDecimalConverterContext(6);

	/**
	 * Costruttore.
	 * 
	 * @param modelId
	 *            id del modello.
	 */
	public RigheRaggruppamentoArticoliTableModel(final String modelId) {
		super(modelId, new String[] { "articolo", "qta" }, RigaRaggruppamentoArticoli.class);
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {
		if (column == 1) {
			return numberEditorContext;
		}
		return super.getConverterContextAt(row, column);
	}

}
