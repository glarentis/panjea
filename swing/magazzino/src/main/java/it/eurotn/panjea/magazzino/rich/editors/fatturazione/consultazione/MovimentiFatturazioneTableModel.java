package it.eurotn.panjea.magazzino.rich.editors.fatturazione.consultazione;

import it.eurotn.panjea.magazzino.util.MovimentoFatturazioneDTO;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.grid.EditorContext;

public class MovimentiFatturazioneTableModel extends DefaultBeanTableModel<MovimentoFatturazioneDTO> {

	private static final long serialVersionUID = 7065236822067016566L;

	/**
	 *
	 * Costruttore.
	 *
	 */
	public MovimentiFatturazioneTableModel() {
		super("movimentiFatturazioneTableModel", new String[] { "areaMagazzino.statoSpedizione",
				"areaMagazzino.tipoAreaMagazzino.tipoDocumento", "areaMagazzino.documento.codice",
				"areaMagazzino.documento.entita.codice", "areaMagazzino.documento.entita.anagrafica.denominazione",
				"areaMagazzino.documento.sedeEntita", "areaRate.codicePagamento", "zonaGeografica", "agente" },
				MovimentoFatturazioneDTO.class);
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
