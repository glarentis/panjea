package it.eurotn.panjea.intra.rich.pages;

import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.intra.domain.AreaIntra;
import it.eurotn.panjea.intra.domain.Nomenclatura;
import it.eurotn.panjea.intra.domain.RigaBeneIntra;
import it.eurotn.panjea.intra.rich.search.ServizioSearchObject;
import it.eurotn.panjea.magazzino.domain.Articolo.ETipoArticolo;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanEditableTableModel;
import it.eurotn.rich.control.table.editor.SearchContext;

import java.math.BigDecimal;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.EditorContext;

public class RigheBeneTableModel extends DefaultBeanEditableTableModel<RigaBeneIntra> {

	private static final long serialVersionUID = 8090508990362368834L;
	private AreaIntra areaIntra;

	private static final SearchContext NOMENCLATURA_EDITOR_CONTEXT = new SearchContext("codice");
	private static final EditorContext IMPORTO_CONTEXT = new EditorContext("numeroDecimaliEditorContext", 2);
	private static EditorContext massaEditorContext;
	private static ConverterContext massaContext;

	static {
		NOMENCLATURA_EDITOR_CONTEXT.addPropertyFilterValue(ServizioSearchObject.PARAM_TIPO_ARTICOLO,
				ServizioSearchObject.PARAM_TIPO_ARTICOLO, ETipoArticolo.FISICO);
		massaContext = new NumberWithDecimalConverterContext();
		massaContext.setUserObject(2);
		massaEditorContext = new EditorContext("massaEditorContext", 3);
	}

	/**
	 * Costruttore.
	 * 
	 * @param areaIntra
	 *            areaIntra che gestice la riga
	 * 
	 */
	public RigheBeneTableModel(final AreaIntra areaIntra) {
		super("righeIntra", new String[] { "nomenclatura", "massa", "importo", "um", "nomenclatura.umsupplementare" },
				RigaBeneIntra.class);
		this.areaIntra = areaIntra;
	}

	@Override
	protected RigaBeneIntra createNewObject() {
		RigaBeneIntra rigaBeneIntra = new RigaBeneIntra();
		// il codice valuta deve essere la valuta della nazione del cliente.

		Importo importo = new Importo();
		if (areaIntra != null && areaIntra.getDocumento() != null) {
			importo.setCodiceValuta(areaIntra.getDocumento().getEntita().getAnagrafica().getSedeAnagrafica()
					.getDatiGeografici().getNazione().getCodiceValuta());
		}
		importo.setImportoInValuta(BigDecimal.ZERO);
		importo.setImportoInValutaAzienda(BigDecimal.ZERO);
		rigaBeneIntra.setImporto(importo);
		rigaBeneIntra.setAreaIntra(areaIntra);
		return rigaBeneIntra;
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {
		switch (column) {
		case 1:
			massaContext.setUserObject(3);
			return massaContext;
		default:
			return super.getConverterContextAt(row, column);
		}
	}

	@Override
	public EditorContext getEditorContextAt(int row, int col) {
		switch (col) {
		case 0:
			return NOMENCLATURA_EDITOR_CONTEXT;
		case 1:
			return massaEditorContext;
		case 2:
			return IMPORTO_CONTEXT;
		default:
			break;
		}
		return super.getEditorContextAt(row, col);
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		switch (column) {
		case 3:
		case 4:
			return false;
		default:
			return true;
		}
	}

	/**
	 * @param areaIntra
	 *            The areaIntra to set.
	 */
	public void setAreaIntra(AreaIntra areaIntra) {
		this.areaIntra = areaIntra;
	}

	@Override
	public void setValueAt(Object value, int row, int column) {
		super.setValueAt(value, row, column);
		if (column == 0) {
			Nomenclatura nomenclatura = (Nomenclatura) value;
			// Devo caricare la nomenclatura e modificare il peso
			if (nomenclatura != null && nomenclatura.getUmsupplementare() != null) {
				setValueAt(nomenclatura.getUmsupplementare(), row, 4);
			}
		}
	}

}
