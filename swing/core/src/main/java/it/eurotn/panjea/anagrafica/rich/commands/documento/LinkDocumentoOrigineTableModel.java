package it.eurotn.panjea.anagrafica.rich.commands.documento;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.LinkDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.LinkDocumento.TIPO_COLLEGAMENTO;
import it.eurotn.rich.control.table.DefaultBeanEditableTableModel;
import it.eurotn.rich.control.table.editor.SearchContext;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.EditorContext;

public class LinkDocumentoOrigineTableModel extends DefaultBeanEditableTableModel<LinkDocumento> {
	private static final long serialVersionUID = -7568916517998428122L;
	private static final SearchContext CODICE_DOCUMENTO_EDITOR_CONTEXT = new SearchContext("codice");
	private static final SearchContext DATA_DOCUMENTO_EDITOR_CONTEXT = new SearchContext("dataDocumento");

	private static final ConverterContext CODICE_DOCUMENTO_CONVERTER_CONTEXT = new ConverterContext(
			"documentoCodiceContext", "codice");
	private static final ConverterContext DATA_DOCUMENTO_CONVERTER_CONTEXT = new ConverterContext(
			"documentoCodiceContext", "dataDocumento");
	private Documento documento;

	/**
	 * 
	 * Costruttore.
	 * 
	 * @param documento
	 *            documento base.
	 */
	public LinkDocumentoOrigineTableModel(final Documento documento) {
		super("linkDocumentoTableModel", new String[] { "documentoDestinazione", "documentoDestinazione",
				"documentoDestinazione.tipoDocumento", "documentoDestinazione.entita", "tipoCollegamento" },
				LinkDocumento.class);
		this.documento = documento;
	}

	@Override
	protected LinkDocumento createNewObject() {
		if (documento == null) {
			return super.createNewObject();
		}

		LinkDocumento linkDocumento = new LinkDocumento();
		linkDocumento.setDocumentoOrigine(documento);
		if (documento.getTipoDocumento().isNotaAddebitoEnable() || documento.getTipoDocumento().isNotaCreditoEnable()) {
			linkDocumento.setTipoCollegamento(TIPO_COLLEGAMENTO.STORNO);
		}
		return linkDocumento;
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int col) {
		switch (col) {
		case 0:
			return CODICE_DOCUMENTO_CONVERTER_CONTEXT;
		case 1:
			return DATA_DOCUMENTO_CONVERTER_CONTEXT;
		default:
			break;
		}
		return super.getConverterContextAt(col, row);
	}

	@Override
	public EditorContext getEditorContextAt(int row, int col) {
		switch (col) {
		case 0:
			return CODICE_DOCUMENTO_EDITOR_CONTEXT;
		case 1:
			return DATA_DOCUMENTO_EDITOR_CONTEXT;
		default:
			break;
		}
		return super.getEditorContextAt(row, col);
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return true;
	}
}
