package it.eurotn.panjea.magazzino.rich.editors.documenti.spedizione;

import it.eurotn.panjea.documenti.util.MovimentoSpedizioneDTO;
import it.eurotn.panjea.magazzino.rich.editors.documenti.spedizione.renderer.EmailSpedizioneCellRenderer;
import it.eurotn.panjea.magazzino.rich.editors.documenti.spedizione.renderer.EsitoSpedizioneCellRenderer;
import it.eurotn.rich.control.table.DefaultBeanEditableTableModel;

import com.jidesoft.grid.EditorContext;

public class SpedizioneMovimentiTableModel extends DefaultBeanEditableTableModel<MovimentoSpedizioneDTO> {

	private static final long serialVersionUID = -4726070846917406451L;

	/**
	 * Costruttore.
	 */
	public SpedizioneMovimentiTableModel() {
		super("spedizioneMovimentiTableModel", new String[] { "tipoDocumento", "documento.codice", "documento.entita",
				"documento.sedeEntita", "sedeSpedizione", "tipoSpedizioneDocumenti", "indirizzoMailMovimento",
				"datiMailUtente", "esitoSpedizione" }, MovimentoSpedizioneDTO.class);
	}

	@Override
	public EditorContext getEditorContextAt(int row, int column) {

		switch (column) {
		case 6:
			return EmailSpedizioneCellRenderer.EMAIL_SPEDIZIONE_CONTEXT;
		case 8:
			return EsitoSpedizioneCellRenderer.ESITO_SPEDIZIONE_CONTEXT;
		default:
			return super.getEditorContextAt(row, column);
		}
	}

	@Override
	protected boolean isAllowInsert() {
		return false;
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return column >= 5 && column <= 7;
	}

}
