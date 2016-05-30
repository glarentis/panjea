/**
 * 
 */
package it.eurotn.panjea.contabilita.rich.editors.tabelle;

import it.eurotn.panjea.contabilita.domain.RegistroIva;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD;
import it.eurotn.panjea.contabilita.rich.forms.RegistroIvaForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

/**
 * @author Leonardo
 * 
 */
public class RegistroIvaPage extends FormBackedDialogPageEditor {

	private IContabilitaAnagraficaBD contabilitaAnagraficaBD;

	/**
	 * Costruttore.
	 * 
	 * @param parentPageId
	 *            id della pagina
	 */
	public RegistroIvaPage(final String parentPageId) {
		super(parentPageId, new RegistroIvaForm());
	}

	@Override
	protected Object doDelete() {
		contabilitaAnagraficaBD.cancellaRegistroIva((RegistroIva) getBackingFormPage().getFormObject());
		return getBackingFormPage().getFormObject();
	}

	@Override
	protected Object doSave() {
		RegistroIva registroIva = (RegistroIva) getBackingFormPage().getFormObject();
		registroIva = contabilitaAnagraficaBD.salvaRegistroIva(registroIva);
		return registroIva;
	}

	@Override
	protected AbstractCommand[] getCommand() {
		return toolbarPageEditor.getDefaultCommand(true);
	}

	/**
	 * @return the contabilitaAnagraficaBD
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
	 *            the contabilitaAnagraficaBD to set
	 */
	public void setContabilitaAnagraficaBD(IContabilitaAnagraficaBD contabilitaAnagraficaBD) {
		this.contabilitaAnagraficaBD = contabilitaAnagraficaBD;
	}

}
