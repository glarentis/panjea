/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.editors.contratto;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.domain.ContrattoSpesometro;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.panjea.anagrafica.rich.editors.documento.DocumentiDocumentoTablePage;
import it.eurotn.panjea.anagrafica.rich.forms.contratto.ContrattoSpesometroForm;

/**
 * @author leonardo
 * 
 */
public class DocumentiContrattoTablePage extends DocumentiDocumentoTablePage {

	private IAnagraficaTabelleBD anagraficaTabelleBD = null;
	private ContrattoSpesometroForm contrattoSpesometroForm = null;

	/**
	 * Costruttore.
	 */
	public DocumentiContrattoTablePage() {
		super();
	}

	@Override
	public Documento doAddDocumento(Documento documento) {
		ContrattoSpesometro contratto = (ContrattoSpesometro) contrattoSpesometroForm.getFormObject();
		Documento docToAdd = anagraficaTabelleBD.aggiungiContrattoADocumento(contratto, documento);
		return docToAdd;
	}

	@Override
	public Documento doRemoveDocumento(Documento documento) {
		anagraficaTabelleBD.rimuovContrattoDaDocumento(documento);
		return documento;
	}

	/**
	 * @return the anagraficaTabelleBD
	 */
	public IAnagraficaTabelleBD getAnagraficaTabelleBD() {
		return anagraficaTabelleBD;
	}

	/**
	 * @param anagraficaTabelleBD
	 *            the anagraficaTabelleBD to set
	 */
	public void setAnagraficaTabelleBD(IAnagraficaTabelleBD anagraficaTabelleBD) {
		this.anagraficaTabelleBD = anagraficaTabelleBD;
	}

	/**
	 * @param contrattoSpesometroForm
	 *            the contrattoSpesometroForm to set
	 */
	public void setContrattoSpesometroForm(ContrattoSpesometroForm contrattoSpesometroForm) {
		this.contrattoSpesometroForm = contrattoSpesometroForm;
	}

}
