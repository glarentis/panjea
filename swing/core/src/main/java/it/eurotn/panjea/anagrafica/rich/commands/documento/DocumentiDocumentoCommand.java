/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.commands.documento;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.rich.bd.IDocumentiBD;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

/**
 * @author leonardo
 */
public class DocumentiDocumentoCommand extends ActionCommand {

	public static final String COMMAND_ID = "documentiDocumentoCommand";
	private DocumentiDocumentoDialog documentiDocumentoDialog = null;
	private Documento documento = null;

	private IDocumentiBD documentiBD;

	/**
	 * Costruttore.
	 */
	public DocumentiDocumentoCommand() {
		super(COMMAND_ID);
		RcpSupport.configure(this);

		documentiBD = RcpSupport.getBean("documentiBD");
	}

	@Override
	protected void doExecuteCommand() {
		documentiDocumentoDialog = new DocumentiDocumentoDialog(documento, documentiBD);
		documentiDocumentoDialog.showDialog();
	}

	/**
	 * @param documento
	 *            the documento to set
	 */
	public void setDocumento(Documento documento) {
		this.documento = documento;
		setVisible(documento != null && documento.getId() != null && documento.getId().intValue() != -1);
	}

}
