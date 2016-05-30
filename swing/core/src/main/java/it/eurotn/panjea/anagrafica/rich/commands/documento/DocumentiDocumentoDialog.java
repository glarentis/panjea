/**
 *
 */
package it.eurotn.panjea.anagrafica.rich.commands.documento;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.rich.bd.IDocumentiBD;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;

import java.awt.Dimension;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.util.RcpSupport;

/**
 * @author leonardo
 */
public class DocumentiDocumentoDialog extends PanjeaTitledPageApplicationDialog {

	private LinkDocumentiPage linkDocumentiPage = null;

	/**
	 * Costruttore.
	 * 
	 * @param documento
	 *            documento base
	 * @param documentiBD
	 *            bd documenti
	 */
	public DocumentiDocumentoDialog(final Documento documento, final IDocumentiBD documentiBD) {
		super();
		this.linkDocumentiPage = new LinkDocumentiPage();
		linkDocumentiPage.setDocumentiBD(documentiBD);
		linkDocumentiPage.setFormObject(documento);
		setDialogPage(linkDocumentiPage);
		setPreferredSize(new Dimension(600, 400));
	}

	@Override
	protected Object[] getCommandGroupMembers() {
		return (new AbstractCommand[] { getFinishCommand() });
	}

	@Override
	protected String getTitle() {
		return RcpSupport.getMessage("documentiTablePage.title");
	}

	@Override
	protected boolean isMessagePaneVisible() {
		return false;
	}

	@Override
	protected void onAboutToShow() {
		super.onAboutToShow();
		linkDocumentiPage.onPrePageOpen();
	}

	@Override
	protected void onCancel() {
		super.onCancel();
	}

	@Override
	protected boolean onFinish() {
		linkDocumentiPage.onSave();
		return true;
	}

}
