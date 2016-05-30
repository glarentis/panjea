/**
 * 
 */
package it.eurotn.panjea.iva.rich.editors.righeiva;

import org.springframework.richclient.dialog.CloseAction;
import org.springframework.richclient.dialog.ConfirmationDialog;

/**
 * Dialog per forzare la validazione della parte iva nel caso in cui il totale delle righe non risulti quadrato con
 * quello del documento.
 * 
 * @author Leonardo
 */
public class ValidaRigheIvaConfimationDialog extends ConfirmationDialog {

	private boolean force = false;

	/**
	 * Costruttore di default per il dialogo della conferma righe iva.
	 * 
	 * @param title
	 *            titolo per il dialog
	 * @param message
	 *            il messaggio del dialog
	 */
	public ValidaRigheIvaConfimationDialog(final String title, final String message) {
		super(title, message);
		setCloseAction(CloseAction.HIDE);
	}

	/**
	 * @return Returns the force.
	 */
	public boolean isForce() {
		return force;
	}

	@Override
	protected void onConfirm() {
		this.force = true;
	}

}
