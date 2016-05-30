/**
 * 
 */
package it.eurotn.panjea.contabilita.rich.editors.righecontabili;

import it.eurotn.panjea.contabilita.rich.bd.IContabilitaBD;
import it.eurotn.panjea.contabilita.util.AreaContabileFullDTO;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.dialog.CloseAction;
import org.springframework.richclient.dialog.ConfirmationDialog;

/**
 * Command per segnalare che ho concluso l'inserimento delle righe contabili e che quindi voglio verificare la
 * quadratura D-A=0.
 * 
 * @author Leonardo
 */
public class CloseRigheContabiliCommand extends ActionCommand {

	/**
	 * Dialogo per chiedere all'utente conferma della volonta' di forzare la validazione dell'area contabile (confermare
	 * le righe anche se D-A!=0).
	 * 
	 * @author Leonardo
	 */
	private class ValidaConfirmationDialog extends ConfirmationDialog {

		private boolean force = false;

		/**
		 * 
		 * @param title
		 *            titolo
		 * @param message
		 *            messaggio
		 */
		public ValidaConfirmationDialog(final String title, final String message) {
			super(title, message);
			setCloseAction(CloseAction.HIDE);
		}

		/**
		 * 
		 * @return true se si vuole forzare la validazione
		 */
		public boolean isForce() {
			return force;
		}

		@Override
		protected void onConfirm() {
			force = true;
		}
	}

	private static final String ID = ".closeRigheContabiliCommand";
	private RigheContabiliTableModel righeContabiliTableModel;
	private IContabilitaBD contabilitaBD;
	private AreaContabileFullDTO areaContabileFullDTO;

	/**
	 * 
	 * @param contabilitaBD
	 *            bd contabilità
	 * @param righeContabiliTableModel
	 *            table model righe contabili
	 */
	public CloseRigheContabiliCommand(final IContabilitaBD contabilitaBD,
			final RigheContabiliTableModel righeContabiliTableModel) {
		super(RigheContabiliTablePage.PAGE_ID + ID);
		CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
				CommandConfigurer.class);
		this.setSecurityControllerId(RigheContabiliTablePage.PAGE_ID + ".controller");
		c.configure(this);
		this.contabilitaBD = contabilitaBD;
		this.righeContabiliTableModel = righeContabiliTableModel;
	}

	@Override
	protected void doExecuteCommand() {
		if (righeContabiliTableModel.isRigheQuadrate()) {
			// se il controllo di quadratura e' positivo valido l'area contabile
			validaRigheContabili();
		} else {
			MessageSource messageSource = (MessageSource) Application.services().getService(MessageSource.class);
			// se le righe risultano essere squadrate chiedo se comunque si vuol
			// validare le righe contabili
			String title = messageSource.getMessage("righeContabiliTablePage.closeRigheContabiliMessage.title", null,
					Locale.getDefault());
			String message = messageSource.getMessage("righeContabiliTablePage.closeRigheContabiliMessage.message",
					null, Locale.getDefault());
			ValidaConfirmationDialog dialog = new ValidaConfirmationDialog(title, message);
			dialog.showDialog();
			if (dialog.isForce()) {
				// se forza la conferma salvo l'area contabile come validata ma
				// squadrata
				validaRigheContabili();
			}
			dialog = null;
		}

	}

	/**
	 * @return Returns the areaContabileFullDTO.
	 */
	public AreaContabileFullDTO getAreaContabileFullDTO() {
		return areaContabileFullDTO;
	}

	/**
	 * @param areaContabileFullDTO
	 *            The areaContabileFullDTO to set.
	 */
	public void setAreaContabileFullDTO(AreaContabileFullDTO areaContabileFullDTO) {
		this.areaContabileFullDTO = areaContabileFullDTO;
	}

	@Override
	public void setEnabled(boolean enabled) {
		if (areaContabileFullDTO != null && areaContabileFullDTO.getAreaContabile().isValidRigheContabili()) {
			enabled = false;
		}
		super.setEnabled(enabled);
	}

	/**
	 * Setta come validate le righe contabili e salva l'area contabile.
	 */
	private void validaRigheContabili() {
		logger.debug("--> Enter validaRigheContabiliAreaContabile");
		this.areaContabileFullDTO = contabilitaBD.validaRigheContabili(areaContabileFullDTO.getAreaContabile());
		logger.debug("--> Exit validaRigheContabiliAreaContabile");
	}

}