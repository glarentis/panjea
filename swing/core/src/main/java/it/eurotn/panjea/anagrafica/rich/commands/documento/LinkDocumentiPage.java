/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.commands.documento;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.rich.bd.IDocumentiBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

/**
 * @author leonardo
 */
public class LinkDocumentiPage extends FormBackedDialogPageEditor {

	private Documento documento = null;

	private IDocumentiBD documentiBD = null;

	/**
	 * 
	 * Costruttore.
	 * 
	 */
	public LinkDocumentiPage() {
		super("linkDocumentiPage", new LinkDocumentoForm());
	}

	@Override
	protected Object doSave() {
		documento = (Documento) getForm().getFormObject();
		documento = documentiBD.salvaDocumento(documento);
		return documento;
	}

	@Override
	public void loadData() {
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public void refreshData() {
		loadData();
	}

	/**
	 * @param documentiBD
	 *            The documentiBD to set.
	 */
	public void setDocumentiBD(IDocumentiBD documentiBD) {
		this.documentiBD = documentiBD;
	}

	@Override
	public void setFormObject(Object object) {
		this.documento = documentiBD.caricaDocumento(((Documento) object).getId(), true);
		super.setFormObject(this.documento);
	}

}