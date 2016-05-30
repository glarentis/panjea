/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.editors.documento;

import it.eurotn.panjea.rich.dialogs.AskDialog;
import it.eurotn.rich.command.AbstractDeleteCommand;

import java.awt.Dimension;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.dialog.ApplicationDialog;
import org.springframework.richclient.dialog.ConfirmationDialog;

/**
 * Abstract command di cancellazione di una area documento.
 * 
 * @author leonardo
 */
public abstract class AbstractEliminaDocumentoCommand extends AbstractDeleteCommand {

	private final class ConfermaEliminazioneDialog extends ConfirmationDialog {

		/**
		 * Dialogo per la conferma dell'eliminazione.
		 * 
		 * @param title
		 *            title
		 * @param message
		 *            message
		 */
		private ConfermaEliminazioneDialog(final String title, final String message) {
			super(title, message);
			this.setPreferredSize(new Dimension(400, 100));
			this.setResizable(false);
		}

		@Override
		protected void onCancel() {
			tipoCancellazione = ETipoCancellazione.NESSUNO;
			super.onCancel();
		}

		@Override
		protected void onConfirm() {
			tipoCancellazione = ETipoCancellazione.DOCUMENTO;
		}
	}

	public enum ETipoCancellazione {
		DOCUMENTO, AREADOCUMENTO, NESSUNO
	}

	private final class RichiestaEliminazioneDialog extends AskDialog {

		/**
		 * Costruttore dialog.
		 * 
		 * @param title
		 *            il titolo del dialog
		 * @param message
		 *            il messaggio da visualizzare
		 */
		private RichiestaEliminazioneDialog(final String title, final String message) {
			super(title, message);
			this.setPreferredSize(new Dimension(400, 100));
			this.setResizable(false);
		}

		/**
		 * Se scelgo NO cancello solo le aree contabili.
		 */
		@Override
		protected void onCancel() {
			tipoCancellazione = ETipoCancellazione.NESSUNO;
			super.onCancel();
		}

		/**
		 * Se scelgo SI cancello l'intero documento.
		 */
		@Override
		protected void onConfirm() {
			tipoCancellazione = ETipoCancellazione.DOCUMENTO;
		}

		/**
		 * Se scelgo ANNULLA l'operazione di cancellazione non viene eseguita.
		 */
		@Override
		protected void onNegate() {
			tipoCancellazione = ETipoCancellazione.AREADOCUMENTO;
			super.onCancel();
		}
	}

	public static final String KEY_MSG_ASK_CANCELLA_AREA_DOCUMENTO_AND_AREE_COLLEGATE = "eliminaAreaDocumentoCommand.ask.message";
	public static final String KEY_MSG_ASK_CANCELLA_DOCUMENTO = "eliminaAreaDocumentoCommand.confirm.message";
	private ETipoCancellazione tipoCancellazione = ETipoCancellazione.NESSUNO;

	/**
	 * Costruttore.
	 * 
	 * @param commandId
	 *            l'id del command
	 */
	public AbstractEliminaDocumentoCommand(final String commandId) {
		super(commandId);
	}

	@Override
	public boolean confirmOperation() {
		ApplicationDialog dialog = createDialog();
		dialog.showDialog();

		// verifico la cancellazione scelta; se NESSUNO annullo, altrimenti decido di eliminare l'intero documento
		// (DOCUMENTO) o solo la parte contabile (AREACONTABILE)
		// NB. che se non ci sono aree collegate devo cancellare l'intero documento per evitare di lasciare documenti
		// orfani di aree
		return tipoCancellazione != ETipoCancellazione.NESSUNO;
	}

	/**
	 * Crea il dialog per la cancellazione, differente a seconda che esistano aree collegate o meno.<br>
	 * 
	 * @return il dialog da mostrare all'utente
	 */
	private ApplicationDialog createDialog() {
		// chiedo all'utente se cancellare l'intero documento, solo l'area o se annullare l'operazione di eliminazione
		MessageSource messageSource = (MessageSource) Application.services().getService(MessageSource.class);
		// Prepara e visualizza il dialogo per la cancellazione di un documento dipendentemente dal fatto che esista o
		// meno una area collegata da eliminare.
		String title = messageSource.getMessage("abstractObjectTable.delete.confirm.title", null, Locale.getDefault());

		ApplicationDialog dialog = null;
		if (isAreaCollegataPresente()) {
			String message = messageSource.getMessage(KEY_MSG_ASK_CANCELLA_AREA_DOCUMENTO_AND_AREE_COLLEGATE, null,
					Locale.getDefault());
			dialog = new RichiestaEliminazioneDialog(title, message);
		} else {
			String message = messageSource.getMessage(KEY_MSG_ASK_CANCELLA_DOCUMENTO, null, Locale.getDefault());
			dialog = new ConfermaEliminazioneDialog(title, message);
		}
		return dialog;
	}

	/**
	 * Metodo in cui viene eseguita la cancellazione delle aree documento. Nel caso in cui venga scelto il tipo
	 * cancellazione DOCUMENTO viene cancellato l'intero documento, alla scelta di AREADOCUMENTO invece, viene
	 * cancellata solo l'area da cui viene chiamata la cancellazione (se dal magazzino l'area magazzino, se dalla
	 * contabilità, l'area contabile).<br>
	 * Nel caso in cui venga annullata la richiesta di cancellazione, questo metodo non viene chiamato.
	 * 
	 * @param deleteAreeCollegate
	 *            definisce se cancellare l'intero documento o solo l'area documento da cui ho richiesto la
	 *            cancellazione
	 * @return l'area documento, la lista di aree documento eliminate o null nel caso in cui annulli l'operazione
	 */
	public abstract Object doDelete(boolean deleteAreeCollegate);

	/**
	 * @return ETipoCancellazione
	 */
	public ETipoCancellazione getTipoCancellazione() {
		return tipoCancellazione;
	}

	/**
	 * Definisce se esiste una area documento collegata, da implementare per ogni area specificatamente secondo le
	 * regole di dominio delle aree disponibili per ogni documento.<br>
	 * Di default imposto a false per fare in modo di cancellare l'intero documento.
	 * 
	 * @return true o false
	 */
	protected boolean isAreaCollegataPresente() {
		return false;
	}

	@Override
	public Object onDelete() {
		logger.debug("--> Enter doExecuteCommand");
		boolean deleteAreeCollegate = (tipoCancellazione == ETipoCancellazione.DOCUMENTO) || !isAreaCollegataPresente();
		Object deletedObject = doDelete(deleteAreeCollegate);
		return deletedObject;
	}

}
