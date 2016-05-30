package it.eurotn.panjea.intra.rich.pages;

import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.intra.domain.AreaIntra;
import it.eurotn.panjea.intra.domain.ModalitaErogazione;
import it.eurotn.panjea.intra.domain.RigaServizioIntra;
import it.eurotn.panjea.intra.rich.search.ServizioSearchObject;
import it.eurotn.panjea.magazzino.domain.Articolo.ETipoArticolo;
import it.eurotn.rich.control.table.DefaultBeanEditableTableModel;
import it.eurotn.rich.control.table.editor.SearchContext;

import java.math.BigDecimal;

import com.jidesoft.grid.EditorContext;

public class RigheServiziTableModel extends DefaultBeanEditableTableModel<RigaServizioIntra> {
	private static final long serialVersionUID = 7079205706555152593L;

	private AreaIntra areaIntra;

	private static final SearchContext SERVIZIO_EDITOR_CONTEXT = new SearchContext("codice");
	private static final EditorContext IMPORTO_CONTEXT = new EditorContext("numeroDecimaliEditorContext", 2);

	static {
		SERVIZIO_EDITOR_CONTEXT.addPropertyFilterValue(ServizioSearchObject.PARAM_TIPO_ARTICOLO,
				ServizioSearchObject.PARAM_TIPO_ARTICOLO, ETipoArticolo.SERVIZI);
	}

	/**
	 * 
	 * Costruttore.
	 * 
	 * @param areaIntra
	 *            areaIntra della riga
	 */
	public RigheServiziTableModel(final AreaIntra areaIntra) {
		super("righeIntra", new String[] { "servizio", "importo", "modalitaErogazione" }, RigaServizioIntra.class);
		this.areaIntra = areaIntra;
	}

	@Override
	protected RigaServizioIntra createNewObject() {
		RigaServizioIntra rigaServizioIntra = new RigaServizioIntra();
		// il codice valuta deve essere la valuta della nazione del cliente.

		Importo importo = new Importo();
		if (areaIntra != null && areaIntra.getDocumento() != null) {
			importo.setCodiceValuta(areaIntra.getDocumento().getEntita().getAnagrafica().getSedeAnagrafica()
					.getDatiGeografici().getNazione().getCodiceValuta());
		}
		importo.setImportoInValuta(BigDecimal.ZERO);
		importo.setImportoInValutaAzienda(BigDecimal.ZERO);
		rigaServizioIntra.setImporto(importo);
		rigaServizioIntra.setAreaIntra(areaIntra);
		rigaServizioIntra.setModalitaErogazione(ModalitaErogazione.ISTANTANEA);
		return rigaServizioIntra;
	}

	@Override
	public EditorContext getEditorContextAt(int row, int col) {
		switch (col) {
		case 0:
			return SERVIZIO_EDITOR_CONTEXT;
		case 4:
			return IMPORTO_CONTEXT;
		default:
			break;
		}
		return super.getEditorContextAt(row, col);
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return true;
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
	}

}
