package it.eurotn.panjea.magazzino.rich.editors.prefatturazione;

import it.eurotn.panjea.magazzino.util.MovimentoPreFatturazioneDTO;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.grid.EditorContext;

public class MovimentiPreFatturazioneTableModel extends DefaultBeanTableModel<MovimentoPreFatturazioneDTO> {

	private static final long serialVersionUID = 7065236822067016566L;

	/**
	 *
	 * Costruttore.
	 *
	 */
	public MovimentiPreFatturazioneTableModel() {
		super("movimentiPreFatturazioneTableModel", new String[] { "areaMagazzino.tipoAreaMagazzino.tipoDocumento",
				"areaMagazzino.documento.codice", "areaMagazzino.documento.totale",
				"areaMagazzino.documento.entita.codice", "areaMagazzino.documento.entita.anagrafica.denominazione",
				"areaMagazzino.documento.sedeEntita", "areaRate.codicePagamento", "tipoPagamento", "zonaGeografica",
				"agente" }, MovimentoPreFatturazioneDTO.class);
	}

	@Override
	public EditorContext getEditorContextAt(int row, int column) {

		// switch (column) {
		// case 1:
		// return NumeroDocumentoNonAssegnatoCellRenderer.NUMERO_DOC_NON_ASSEGNATO_CONTEXT;
		// default:
		return super.getEditorContextAt(row, column);
		// }
	}
}
