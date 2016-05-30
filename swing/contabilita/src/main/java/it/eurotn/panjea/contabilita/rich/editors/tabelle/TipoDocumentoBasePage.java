/**
 * 
 */
package it.eurotn.panjea.contabilita.rich.editors.tabelle;

import it.eurotn.panjea.contabilita.domain.TipoDocumentoBase;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD;
import it.eurotn.panjea.contabilita.rich.forms.TipoDocumentoBaseForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

/**
 * PageEditor di {@link TipoDocumentoBase}.
 * 
 * @author adriano
 * @version 1.0, 27/ago/07
 * 
 */
public class TipoDocumentoBasePage extends FormBackedDialogPageEditor {

	private static final String PAGE_ID = "tipoDocumentoBasePage";

	private IContabilitaAnagraficaBD contabilitaAnagraficaBD;

	/**
	 * Costruttore.
	 */
	public TipoDocumentoBasePage() {
		super(PAGE_ID, new TipoDocumentoBaseForm(new TipoDocumentoBase()));
	}

	@Override
	protected Object doDelete() {
		contabilitaAnagraficaBD.cancellaTipoDocumentoBase((TipoDocumentoBase) getBackingFormPage().getFormObject());
		return getBackingFormPage().getFormObject();
	}

	@Override
	protected Object doSave() {
		TipoDocumentoBase tipoDocumentoBase = (TipoDocumentoBase) getBackingFormPage().getFormObject();
		tipoDocumentoBase = contabilitaAnagraficaBD.salvaTipoDocumentoBase(tipoDocumentoBase);
		return tipoDocumentoBase;
	}

	@Override
	protected AbstractCommand[] getCommand() {
		return toolbarPageEditor.getDefaultCommand(true);
	}

	/**
	 * @return Returns the contabilitaAnagraficaBD.
	 */
	public IContabilitaAnagraficaBD getContabilitaAnagraficaBD() {
		return contabilitaAnagraficaBD;
	}

	@Override
	public void loadData() {

	}

	@Override
	public void onPostPageOpen() {

	}

	@Override
	public boolean onPrePageOpen() {

		return true;
	}

	@Override
	public void refreshData() {

	}

	/**
	 * @param contabilitaAnagraficaBD
	 *            The contabilitaAnagraficaBD to set.
	 */
	public void setContabilitaAnagraficaBD(IContabilitaAnagraficaBD contabilitaAnagraficaBD) {
		this.contabilitaAnagraficaBD = contabilitaAnagraficaBD;
	}

}
