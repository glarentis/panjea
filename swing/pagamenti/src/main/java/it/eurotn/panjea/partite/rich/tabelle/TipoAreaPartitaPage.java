/**
 *
 */
package it.eurotn.panjea.partite.rich.tabelle;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.partite.domain.TipoAreaPartita;
import it.eurotn.panjea.partite.domain.TipoAreaPartita.TipoPartita;
import it.eurotn.panjea.partite.rich.bd.IPartiteBD;
import it.eurotn.panjea.rich.forms.PanjeaFormGuard;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.dialog.ButtonCompositeDialogPage;
import it.eurotn.rich.dialog.JecCompositeDialogPage;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import javax.swing.AbstractButton;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.form.FormGuard;

/**
 * Visualizza il form per l'area pagamenti nel tipo documento
 * 
 * <br>
 * se la classe del documento non supporta l'Area pagamenti la pagina si nasconde.<br>
 * se l'area pagamenti è supportata ma non ancora creata disabilita la pagina e sulla sua apertura chiede conferma se
 * creare una nuova area partite. <br>
 * <b>NB</b>Il pulsante nuovo non è stato inserito perchè può esistere solamente un solo {@link TipoAreaPartita} per
 * {@link TipoDocumento} <br>
 * 
 * @see {@link TipoAreaPartita}, {@link TipoDocumento}
 * 
 * @author adriano
 * @version 1.0, 08/lug/08
 * 
 */
public class TipoAreaPartitaPage extends FormBackedDialogPageEditor {

	private class CreaAreaConfirmationDialog extends ConfirmationDialog {

		private boolean canOpen;

		/**
		 * Costruttore di default.
		 * 
		 * @param title
		 *            titolo per il dialog
		 * @param message
		 *            il messaggio da presentare per il dialog
		 */
		public CreaAreaConfirmationDialog(final String title, final String message) {
			super(title, message);
		}

		/**
		 * @return canOpen dialog
		 */
		public boolean isCanOpen() {
			return canOpen;
		}

		@Override
		protected void onConfirm() {
			getNuovoTipoAreaPartitaCommand().execute();
			canOpen = true;
		}

	}

	/**
	 * ActionCommand per la cancellazione di {@link TipoAreaPartita}.
	 * 
	 * @author adriano
	 */
	private class EliminaTipoAreaPartitaCommand extends ActionCommand {

		private static final String COMMAND_ID = "deleteCommand";

		/**
		 * Costruttore di default.
		 */
		public EliminaTipoAreaPartitaCommand() {
			super(COMMAND_ID);
			setSecurityControllerId(TipoAreaPartitaPage.PAGE_ID + ".controller");
			CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
					CommandConfigurer.class);
			c.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			TipoAreaPartitaPage.this.eliminaTipoAreaPartita();
		}

		@Override
		protected void onButtonAttached(AbstractButton button) {
			super.onButtonAttached(button);
			button.setName(PAGE_ID + "." + COMMAND_ID);
		}

	}

	/**
	 * ActionCommand per la creazione di un nuovo tipo area partite.
	 * 
	 * @author Leonardo
	 */
	private class NuovoTipoAreaPartiteCommand extends ActionCommand {

		private static final String COMMAND_ID = "newCommand";

		/**
		 * Costruttore di default.
		 */
		public NuovoTipoAreaPartiteCommand() {
			super(getPageEditorId() + "." + COMMAND_ID);
			setSecurityControllerId(getPageEditorId() + ".controller");
		}

		@Override
		protected void doExecuteCommand() {
			logger.debug("--> Enter doExecuteCommand");
			TipoAreaPartita tipoAreaPartita = new TipoAreaPartita();
			tipoAreaPartita.setTipoDocumento(TipoAreaPartitaPage.this.tipoDocumento);
			TipoAreaPartitaPage.super.setFormObject(tipoAreaPartita);
			logger.debug("--> Exit doExecuteCommand");
		}
	}

	private TipoDocumento tipoDocumento = null;
	private static Logger logger = Logger.getLogger(TipoAreaPartitaPage.class);
	private static final String PAGE_ID = "tipoAreaPartitaPage";
	private static final String TITLE_CONFIRMATION = "tipoAreaPartitaPage.ask.newTipoAreaPartita.title";
	private static final String MESSAGE_CONFIRMATION = "tipoAreaPartitaPage.ask.newTipoAreaPartita.message";
	private IPartiteBD partiteBD;
	private ActionCommand nuovoTipoAreaPartiteCommand = null;
	private ActionCommand eliminaTipoAreaPartiteCommand = null;

	/**
	 * Costruttore di default.
	 */
	public TipoAreaPartitaPage() {
		super(PAGE_ID, new TipoAreaPartitaForm(new TipoAreaPartita()));
	}

	@Override
	protected Object doSave() {
		logger.debug("--> Enter doSave");
		TipoAreaPartita tipoAreaPartita = (TipoAreaPartita) getForm().getFormObject();
		tipoAreaPartita.setTipoDocumento(tipoDocumento);

		TipoAreaPartita tipoAreaPartitaSalvata = partiteBD.salvaTipoAreaPartita(tipoAreaPartita);
		super.setFormObject(tipoAreaPartitaSalvata);
		if (tipoAreaPartita.isNew()) {
			firePropertyChange("pageValid", false, true);
		}
		logger.debug("--> Exit doSave");
		return tipoAreaPartitaSalvata.getTipoDocumento();
	}

	/**
	 * Esecuzione della cancellazione di {@link TipoPartita} del Form tramite il BusinessDelegate.
	 */
	private void eliminaTipoAreaPartita() {
		TipoAreaPartita tipoAreaPartita = (TipoAreaPartita) getForm().getFormObject();
		if (tipoAreaPartita.getId() != null) {
			partiteBD.cancellaTipoAreaPartita(tipoAreaPartita);
			// rilancia il propertyChange di SHOW_INIT_PAGE e successivamente
			// quello
			// per invalidare la Page corrente
			firePropertyChange(JecCompositeDialogPage.SHOW_INIT_PAGE, null, null);
			firePropertyChange("pageValid", true, false);
			getNuovoTipoAreaPartitaCommand().execute();
		}
	}

	@Override
	protected AbstractCommand[] getCommand() {
		AbstractCommand[] abstractCommands = new AbstractCommand[] { toolbarPageEditor.getLockCommand(),
				toolbarPageEditor.getSaveCommand(), toolbarPageEditor.getUndoCommand(),
				getEliminaTipoAreaPartitaCommand() };
		return abstractCommands;
	}

	@Override
	public AbstractCommand getEditorNewCommand() {
		return null;
	}

	/**
	 * @return restituisce il command per l'eliminazione del tipo area partita
	 */
	private ActionCommand getEliminaTipoAreaPartitaCommand() {
		if (eliminaTipoAreaPartiteCommand == null) {
			eliminaTipoAreaPartiteCommand = new EliminaTipoAreaPartitaCommand();
			new PanjeaFormGuard(getForm().getFormModel(), eliminaTipoAreaPartiteCommand, FormGuard.ON_NOERRORS);
		}
		return eliminaTipoAreaPartiteCommand;
	}

	/**
	 * @return restituisce il command per la creazione di un nuovo tipo area partita
	 */
	private ActionCommand getNuovoTipoAreaPartitaCommand() {
		if (nuovoTipoAreaPartiteCommand == null) {
			nuovoTipoAreaPartiteCommand = new NuovoTipoAreaPartiteCommand();
		}
		return nuovoTipoAreaPartiteCommand;
	}

	/**
	 * Non carica i dati perchè vengono caricati nella setFormObject. <br>
	 * Essendo lazy la loadData viene chiamata solamente quando la pagina viene visualizzata, invece il controllo se il
	 * tipoArea è abilitato o presente lo devo fare quando setto l'oggetto(viene eseguito sulla
	 * {@link TipoAreaPartitaPage#setFormObject(Object)}.
	 */
	@Override
	public void loadData() {
		logger.debug("--> Enter loadData");

		if ((tipoDocumento == null) || (tipoDocumento.getClasseTipoDocumento() == null)) {
			logger.debug("--> tipoDocumento o classeTipoDocumento null, invalido la pagina e rendo invisibile il command ");
			firePropertyChange("pageValid", true, false);
			firePropertyChange(ButtonCompositeDialogPage.PAGE_VISIBLE, true, false);
			return;
		}

		if (tipoDocumento.getClasseTipoDocumentoInstance().getTipiAree().contains(TipoAreaPartita.class.getName())) {
			logger.debug("--> Setto il visible della pagina a TRUE");
			// fire property change con proprieta "pageVisible" per comunicare
			// al command della pagina corrente
			// di rendersi visibile
			firePropertyChange(ButtonCompositeDialogPage.PAGE_VISIBLE, false, true);

			TipoAreaPartita tap = (TipoAreaPartita) getForm().getFormObject();

			if (tap.isNew()) {
				firePropertyChange("pageValid", true, false);
			} else {
				firePropertyChange("pageValid", false, true);
			}
		} else {
			firePropertyChange(ButtonCompositeDialogPage.PAGE_VISIBLE, true, false);
		}
		logger.debug("--> Exit loadData");
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		logger.debug("--> Enter onPrePageOpen");
		boolean canOpen = true;
		Integer idTipoDoc = tipoDocumento.getId();
		// se non c'e' un tipo documet caricato non posso caricare il tipo area
		// contabile
		if (idTipoDoc == null) {
			logger.debug("--> Nessun tipo documento caricato, non carico la " + getId());
			return false;
		}

		if (!tipoDocumento.getClasseTipoDocumentoInstance().getTipiAree().contains(TipoAreaPartita.class.getName())) {
			return false;
		}

		TipoAreaPartita tap = (TipoAreaPartita) getForm().getFormObject();

		// Se non esiste l'area contabile chiedo se crearla
		if (tap.isNew()) {
			if (!PanjeaSwingUtil.hasPermission("modificaTipoAreaPartita")) {
				throw new java.lang.SecurityException();
			}
			String title = getMessage(TITLE_CONFIRMATION);
			String message = getMessage(MESSAGE_CONFIRMATION);
			CreaAreaConfirmationDialog confirmationDialog = new CreaAreaConfirmationDialog(title, message);
			confirmationDialog.showDialog();
			canOpen = confirmationDialog.isCanOpen();
		}
		logger.debug("--> Exit onPrePageOpen con risultato " + canOpen);
		return canOpen;
	}

	@Override
	public void publishCreateEvent(Object obj) {
	}

	@Override
	public void refreshData() {
		// nothing to do
	}

	/*
	 * Riceve il {@link TipoDocumento} da gestire e rende visibile/abilitata la pagina se supporta/manca il tipo area
	 * pagamento.
	 */
	@Override
	public void setFormObject(Object object) {
		TipoDocumento tipoDocumentoCorrente = (TipoDocumento) object;

		if (tipoDocumentoCorrente.getId() != null
				&& tipoDocumentoCorrente.getClasseTipoDocumentoInstance().getTipiAree()
						.contains(TipoAreaPartita.class.getName())) {
			// la carico solamente se è abilitata
			TipoAreaPartita tipoAreaPartitaCorrente = partiteBD
					.caricaTipoAreaPartitaPerTipoDocumento(tipoDocumentoCorrente);
			super.setFormObject(tipoAreaPartitaCorrente);
		}
		tipoDocumento = tipoDocumentoCorrente;
	}

	/**
	 * @param partiteBD
	 *            The partiteBD to set.
	 */
	public void setPartiteBD(IPartiteBD partiteBD) {
		this.partiteBD = partiteBD;
	}

}
