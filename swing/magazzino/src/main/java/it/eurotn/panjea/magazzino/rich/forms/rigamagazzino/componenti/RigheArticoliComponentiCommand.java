/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.forms.rigamagazzino.componenti;

import it.eurotn.panjea.magazzino.domain.IRigaArticoloDocumento;

import java.awt.Dimension;

import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;

/**
 * @author fattazzo
 * 
 */
public class RigheArticoliComponentiCommand extends ApplicationWindowAwareCommand {

	public static final String COMMAND_ID = "righeArticoliFigliCommand";
	private RigheArticoliComponentiDialog righeDialog = null;
	private IRigaArticoloDocumento rigaArticoloDocumento = null;
	private boolean openReadOnly;

	/**
	 * Costruttore.
	 */
	public RigheArticoliComponentiCommand() {
		super(COMMAND_ID);
		this.setSecurityControllerId(COMMAND_ID);
		RcpSupport.configure(this);
	}

	@Override
	protected void doExecuteCommand() {
		if (rigaArticoloDocumento != null) {
			RigheArticoliComponentiDialog dialogRigheFiglie = getRigheDialog();
			dialogRigheFiglie.setRigaArticoloDocumento(rigaArticoloDocumento);
			dialogRigheFiglie.setReadOnly(openReadOnly);
			dialogRigheFiglie.setPreferredSize(new Dimension(600, 400));
			dialogRigheFiglie.showDialog();

			rigaArticoloDocumento = dialogRigheFiglie.getRigaArticoloDocumento();
			if (rigaArticoloDocumento != null) {
				rigaArticoloDocumento.setQta(rigaArticoloDocumento.getQta());
				rigaArticoloDocumento.setQtaMagazzino(rigaArticoloDocumento.getQta());
			}
		}
	}

	/**
	 * @return the rigaArticoloDocumento
	 */
	public IRigaArticoloDocumento getRigaArticoloDocumento() {
		return rigaArticoloDocumento;
	}

	/**
	 * Restituisce il dialog da utilizzare per la visualizzazione/modifica dei componenti.
	 * 
	 * @return RigheArticoliComponentiDialog
	 */
	private RigheArticoliComponentiDialog getRigheArticoliComponentiDialog() {
		if (righeDialog == null) {
			righeDialog = new RigheArticoliComponentiDialog();
		}

		return righeDialog;
	}

	/**
	 * @return the righeDialog
	 */
	private RigheArticoliComponentiDialog getRigheDialog() {
		if (righeDialog == null) {
			righeDialog = getRigheArticoliComponentiDialog();
		}

		return righeDialog;
	}

	/**
	 * setta il dialogo in sola lettura quando viene aperto.
	 * 
	 * @param readOnly
	 *            readOnly
	 */
	public void setReadOnly(boolean readOnly) {
		openReadOnly = readOnly;
	}

	/**
	 * @param rigaArticoloDocumento
	 *            the rigaArticoloDocumento to set
	 */
	public void setRigaArticoloDocumento(IRigaArticoloDocumento rigaArticoloDocumento) {
		setEnabled(rigaArticoloDocumento.getComponenti() != null);
		this.rigaArticoloDocumento = rigaArticoloDocumento;
	}

}
