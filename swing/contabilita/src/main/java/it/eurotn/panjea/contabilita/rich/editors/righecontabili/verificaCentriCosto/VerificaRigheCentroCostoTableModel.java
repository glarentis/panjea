package it.eurotn.panjea.contabilita.rich.editors.righecontabili.verificaCentriCosto;

import it.eurotn.panjea.centricosto.domain.CentroCosto;
import it.eurotn.panjea.contabilita.domain.RigaCentroCosto;
import it.eurotn.panjea.contabilita.domain.RigaContabile;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanEditableTableModel;
import it.eurotn.rich.control.table.editor.SearchContext;

import java.math.BigDecimal;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.EditorContext;

public class VerificaRigheCentroCostoTableModel extends DefaultBeanEditableTableModel<RigaCentroCosto> {

	private static final long serialVersionUID = 7223754450132263896L;
	private static ConverterContext context;
	private static final SearchContext CENTRO_COSTO_EDITOR_CONTEXT = new SearchContext("descrizione");
	private RigaContabile rc;
	private boolean modificato;

	static {
		CENTRO_COSTO_EDITOR_CONTEXT.setSearchObjectClassKey(CentroCosto.class);
	}

	static {
		context = new NumberWithDecimalConverterContext();
		context.setUserObject(2);
	}

	/**
	 * Costruttore.
	 *
	 * @param rc
	 *            riga contabile dei centri di costo
	 */
	public VerificaRigheCentroCostoTableModel(final RigaContabile rc) {
		super("righeCentrocosto", new String[] { "centroCosto", "importo", "nota" }, RigaCentroCosto.class);
		this.rc = rc;
		modificato = false;
	}

	@Override
	protected RigaCentroCosto createNewObject() {
		RigaCentroCosto cc = super.createNewObject();
		cc.setRigaContabile(rc);
		return cc;
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {
		if (column == 1) {
			return context;
		}
		return null;
	};

	@Override
	public EditorContext getEditorContextAt(int i, int j) {
		if (j == 0) {
			return CENTRO_COSTO_EDITOR_CONTEXT;
		}
		return super.getEditorContextAt(i, j);
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return true;
	}

	/**
	 * @return Returns the modificato.
	 */
	public boolean isModificato() {
		return modificato;
	}

	@Override
	public void setValueAt(Object value, int row, int column) {
		if (column == 0 && BigDecimal.ZERO.equals(getValueAt(row, 1)) && rc != null) {
			BigDecimal totaleCentriCosto = BigDecimal.ZERO;
			for (RigaCentroCosto rigaPresente : getObjects()) {
				totaleCentriCosto = totaleCentriCosto.add(rigaPresente.getImporto());
			}
			setValueAt(rc.getImporto().subtract(totaleCentriCosto), row, 1);
		}
		modificato = true;
		super.setValueAt(value, row, column);
	}

}
