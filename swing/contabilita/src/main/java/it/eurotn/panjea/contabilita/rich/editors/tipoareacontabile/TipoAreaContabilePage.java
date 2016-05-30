/**
 *
 */
package it.eurotn.panjea.contabilita.rich.editors.tipoareacontabile;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD;
import it.eurotn.panjea.contabilita.rich.editors.tabelle.strutturacontabile.StrutturaContabileControPartitePage;
import it.eurotn.panjea.rich.forms.PanjeaFormGuard;
import it.eurotn.rich.dialog.ButtonCompositeDialogPage;
import it.eurotn.rich.dialog.JecCompositeDialogPage;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;

import java.awt.Dimension;

import javax.swing.AbstractButton;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.dialog.DialogPage;
import org.springframework.richclient.form.FormGuard;

/**
 * @author adriano
 * @version 1.0, 21/mag/07
 */

public class TipoAreaContabilePage extends FormBackedDialogPageEditor {

	private class CreaAreaConfirmationDialog extends ConfirmationDialog {
		private boolean canOpen;

		/**
		 * Costruttore.
		 * 
		 * @param title
		 *            titolo
		 * @param message
		 *            messaggio
		 */
		public CreaAreaConfirmationDialog(final String title, final String message) {
			super(title, message);
		}

		/**
		 * @return indica se è possibile la nuova tipo area contabile
		 */
		public boolean isCanOpen() {
			return canOpen;
		}

		@Override
		protected void onConfirm() {
			getNuovoTipoAreaContabileCommand().execute();
			canOpen = true;
		}

	}

	/**
	 * ActionCommand per la cancellazione di {@link TipoAreaContabile}.
	 * 
	 * @author adriano
	 * @version 1.0, 09/lug/08
	 */
	private class EliminaTipoAreaContabileCommand extends ActionCommand {

		private static final String COMMAND_ID = "deleteCommand";

		/**
		 * Costruttore.
		 * 
		 */
		public EliminaTipoAreaContabileCommand() {
			super(COMMAND_ID);
			setSecurityControllerId(getPageEditorId() + ".controller");
			CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
					CommandConfigurer.class);
			c.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			TipoAreaContabilePage.this.eliminaTipoAreaContabile();
		}

		@Override
		protected void onButtonAttached(AbstractButton button) {
			super.onButtonAttached(button);
			button.setName(PAGE_ID + "." + COMMAND_ID);
		}
	}

	/**
	 * @author Leonardo
	 */
	private class NuovoTipoAreaContabileCommand extends ActionCommand {

		private static final String COMMAND_ID = "newCommand";

		/**
		 * Costruttore.
		 * 
		 */
		public NuovoTipoAreaContabileCommand() {
			super(getPageEditorId() + "." + COMMAND_ID);
			setSecurityControllerId(getPageEditorId() + ".controller");
		}

		@Override
		protected void doExecuteCommand() {
			logger.debug("--> Enter doExecuteCommand");
			TipoAreaContabile tipoAreaContabile = new TipoAreaContabile();
			tipoAreaContabile.setTipoDocumento(TipoAreaContabilePage.this.tipoDocumento);
			TipoAreaContabilePage.super.setFormObject(tipoAreaContabile);
			logger.debug("--> Exit doExecuteCommand");
		}
	}

	/**
	 * ActionCommand per l'apertura di StrutturaContabileCompositePage.
	 */
	private class StrutturaContabileCommand extends ActionCommand {

		private static final String COMMAND_ID = "strutturaContabileCommand";

		/**
		 * Costruttore.
		 * 
		 */
		public StrutturaContabileCommand() {
			super(COMMAND_ID);
			setSecurityControllerId(PAGE_ID + ".controller");
			CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
					CommandConfigurer.class);
			c.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			StrutturaContabileControPartitePage strutturaContabileControPartitePage = new StrutturaContabileControPartitePage(
					(TipoAreaContabile) getBackingFormPage().getFormObject(),
					TipoAreaContabilePage.this.contabilitaAnagraficaBD);
			StrutturaContabileDialog titledPageApplicationDialog = new StrutturaContabileDialog(
					strutturaContabileControPartitePage);
			titledPageApplicationDialog.setPreferredSize(new Dimension(700, 500));
			titledPageApplicationDialog.showDialog();
		}
	}

	/**
	 * Dialogo di modifica di una struttura contabile, prende una dialog page come contenuto del dialog.
	 * 
	 * @author Leonardo
	 */
	private class StrutturaContabileDialog extends PanjeaTitledPageApplicationDialog {

		private static final String ID_DIALOG = "strutturaContabileDialog";
		private static final String FINISH_COMMAND_ID = "finishCommand";

		/**
		 * Costruttore.
		 * 
		 * @param dialogPage
		 *            page
		 */
		public StrutturaContabileDialog(final DialogPage dialogPage) {
			super(dialogPage);
			setTitle(getMessage(ID_DIALOG + ".title"));
		}

		@Override
		protected Object[] getCommandGroupMembers() {
			return new AbstractCommand[] { getFinishCommand() };
		}

		@Override
		protected String getFinishCommandId() {
			return FINISH_COMMAND_ID;
		}

		@Override
		protected boolean onFinish() {
			((StrutturaContabileControPartitePage) getDialogPage()).saveOrDeleteCodiceIvaPrevalente();
			return true;
		}

	}

	protected static Logger logger = Logger.getLogger(TipoAreaContabilePage.class);
	private static final String TITLE_CONFIRMATION = "tipoAreaContabilePage.ask.new.tipoAreaContabile.title";
	private static final String MESSAGE_CONFIRMATION = "tipoAreaContabilePage.ask.new.tipoAreaContabile.message";
	private static final String PAGE_ID = "tipoAreaContabilePage";
	private IContabilitaAnagraficaBD contabilitaAnagraficaBD = null;
	private TipoDocumento tipoDocumento = null;
	private ActionCommand nuovoTipoAreaContabileCommand = null;
	private ActionCommand eliminaTipoAreaContabileCommand = null;
	private StrutturaContabileCommand strutturaContabileCommand = null;

	/**
	 * Costruttore.
	 * 
	 * @param tipoAreaContabile
	 *            tipo area contabile da gestire
	 */
	public TipoAreaContabilePage(final TipoAreaContabile tipoAreaContabile) {
		super(PAGE_ID, new TipoAreaContabileForm(tipoAreaContabile));
	}

	@Override
	protected Object doSave() {
		logger.debug("--> Enter doSave");
		TipoAreaContabile tipoAreaContabile = (TipoAreaContabile) getForm().getFormObject();
		tipoAreaContabile.setTipoDocumento(tipoDocumento);

		if (tipoAreaContabile.getRegistroProtocollo() != null) {
			String[] protocolloSplit = tipoAreaContabile.getRegistroProtocollo().split(" - ");
			tipoAreaContabile.setRegistroProtocollo(protocolloSplit[0]);
		}

		if (tipoAreaContabile.getRegistroProtocolloCollegato() != null) {
			String[] protocolloSplit = tipoAreaContabile.getRegistroProtocolloCollegato().split(" - ");
			tipoAreaContabile.setRegistroProtocolloCollegato(protocolloSplit[0]);
		}

		TipoAreaContabile tipoAreaContabileSalvato = contabilitaAnagraficaBD.salvaTipoAreaContabile(tipoAreaContabile);

		// richiamo il setFormObject della super per mettere nel form
		// l'areaContabile.
		// sulla doSave restituisco l'oggetto gestito dal form, questa richiama
		// la setFormObject che però in questo caso non ricarica
		// il tipoareaContabile. quindi imposto il nuovo tipoAreaContabile in
		// questo punto
		super.setFormObject(tipoAreaContabileSalvato);

		if (tipoAreaContabile.isNew()) {
			firePropertyChange("pageValid", false, true);
		}

		getStrutturaContabileCommand().setEnabled(tipoAreaContabileSalvato != null);

		logger.debug("--> Exit doSave");
		return tipoAreaContabileSalvato.getTipoDocumento();
	}

	protected void eliminaTipoAreaContabile() {
		TipoAreaContabile tipoAreaContabile = (TipoAreaContabile) getForm().getFormObject();
		if (tipoAreaContabile.getId() != null) {
			contabilitaAnagraficaBD.cancellaTipoAreaContabile(tipoAreaContabile);
			firePropertyChange(JecCompositeDialogPage.SHOW_INIT_PAGE, null, null);
			firePropertyChange("pageValid", true, false);
			getNuovoTipoAreaContabileCommand().execute();
		}
	}

	@Override
	protected AbstractCommand[] getCommand() {
		AbstractCommand[] abstractCommands = new AbstractCommand[] { toolbarPageEditor.getLockCommand(),
				toolbarPageEditor.getSaveCommand(), toolbarPageEditor.getUndoCommand(),
				getEliminaTipoAreaContabileCommand(), getStrutturaContabileCommand() };
		return abstractCommands;
	}

	/**
	 * @return Returns the contabilitaAnagraficaBD.
	 */
	public IContabilitaAnagraficaBD getContabilitaAnagraficaBD() {
		return contabilitaAnagraficaBD;
	}

	/*
	 * Sovrascrivo per evitare che venga associato il new command dato che questo non viene visualizzato su questo
	 * formbacked
	 */
	@Override
	public AbstractCommand getEditorNewCommand() {
		return null;
	}

	private AbstractCommand getEliminaTipoAreaContabileCommand() {
		if (eliminaTipoAreaContabileCommand == null) {
			eliminaTipoAreaContabileCommand = new EliminaTipoAreaContabileCommand();
			new PanjeaFormGuard(getForm().getFormModel(), eliminaTipoAreaContabileCommand, FormGuard.ON_NOERRORS);
		}
		return eliminaTipoAreaContabileCommand;
	}

	private AbstractCommand getNuovoTipoAreaContabileCommand() {
		if (nuovoTipoAreaContabileCommand == null) {
			nuovoTipoAreaContabileCommand = new NuovoTipoAreaContabileCommand();
		}
		return nuovoTipoAreaContabileCommand;
	}

	private StrutturaContabileCommand getStrutturaContabileCommand() {
		if (strutturaContabileCommand == null) {
			strutturaContabileCommand = new StrutturaContabileCommand();
		}
		return strutturaContabileCommand;
	}

	@Override
	public void loadData() {
		logger.debug("--> Enter loadData");
		if ((tipoDocumento == null) || (tipoDocumento.getClasseTipoDocumento() == null)) {
			logger.debug("--> tipoDocumento o classeTipoDocumento null, invalido la pagina e rendo invisibile il command ");
			firePropertyChange("pageValid", true, false);
			firePropertyChange(ButtonCompositeDialogPage.PAGE_VISIBLE, true, false);
			return;
		}

		if (tipoDocumento.getClasseTipoDocumentoInstance().getTipiAree().contains(TipoAreaContabile.class.getName())) {
			logger.debug("--> Setto il visible della pagina a TRUE");
			// fire property change con proprieta "pageVisible" per comunicare
			// al command della pagina corrente
			// di rendersi visibile
			firePropertyChange(ButtonCompositeDialogPage.PAGE_VISIBLE, false, true);

			TipoAreaContabile tac = (TipoAreaContabile) getForm().getFormObject();

			if (tac.isNew()) {
				firePropertyChange("pageValid", true, false);
				getStrutturaContabileCommand().setEnabled(false);
			} else {
				firePropertyChange("pageValid", false, true);
				getStrutturaContabileCommand().setEnabled(true);
			}
		} else {
			firePropertyChange(ButtonCompositeDialogPage.PAGE_VISIBLE, true, false);
		}
		logger.debug("--> Exit loadData");
	}

	@Override
	public void onPostPageOpen() {
		// if (getDefaultController() != null) {
		// // setto sempre il formModel , non ho problemi perche' e' sempre lo
		// stesso
		// getDefaultController().setFormModel(this.getForm().getFormModel());
		// getDefaultController().register();
		// }

	}

	/**
	 * Verica se ho il l'area contabile esiste, in talvaso chiede di crearla.
	 * 
	 * @return <code>true</code> se è possibile chiudere la pagina
	 */
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

		if (!tipoDocumento.getClasseTipoDocumentoInstance().getTipiAree().contains(TipoAreaContabile.class.getName())) {
			return false;
		}

		TipoAreaContabile tac = (TipoAreaContabile) getForm().getFormObject();

		// Se non esiste l'area contabile chiedo se crearla
		if (tac.isNew()) {
			String title = getMessage(TITLE_CONFIRMATION);
			String message = getMessage(MESSAGE_CONFIRMATION);
			CreaAreaConfirmationDialog confirmationDialog = new CreaAreaConfirmationDialog(title, message);
			confirmationDialog.showDialog();
			canOpen = confirmationDialog.isCanOpen();
		}
		logger.debug("--> Exit onPrePageOpen con risultato " + canOpen);
		return canOpen;
	}

	/**
	 * HACK: il tipo area contabile contiene il tipo documento che ? quello che viene ricercato e va aggiornato
	 * nell'editor. Dato che la formbackedDialogPageEditor si preoccupa di notificare alla vista search result
	 * l'inserimento/aggiornamento di un record impedisco l'aggiornamento della search result. L'id controllato da essa
	 * ? l'id dell'area che ? null , ma l'oggetto ritornato ? il tipo documento. Il framework non capisce che l'id
	 * controllato non appartiene all'entit? dell'editor e quindi non permetto la chiamata al metodo
	 * super.publishCreateEvent (mi ritroverei con l'inserimento di un secondo record quando in realt? st? modificando
	 * un tipo documento esistente). posso farlo grazie al fatto che la search result quando visualizza un oggetto
	 * nell'editor relativo lo ricarica.
	 * 
	 * @param obj
	 *            object
	 */
	@Override
	public void publishCreateEvent(Object obj) {
	}

	@Override
	public void refreshData() {
		logger.debug("--> Enter refreshData");
		loadData();
		logger.debug("--> Exit refreshData");
	}

	/**
	 * @param contabilitaAnagraficaBD
	 *            The contabilitaAnagraficaBD to set.
	 */
	public void setContabilitaAnagraficaBD(IContabilitaAnagraficaBD contabilitaAnagraficaBD) {
		this.contabilitaAnagraficaBD = contabilitaAnagraficaBD;
	}

	/**
	 * L'oggetto contenuto nell'editor è il TipoDocumento.<br>
	 * Carico l'area contabile per il tipoDocumento, poi nella loadData verifico se l'area è presente o supportata
	 * 
	 * @param object
	 *            object
	 */
	@Override
	public void setFormObject(Object object) {
		logger.debug("--> Enter setFormObject");
		TipoDocumento tipoDocumentoCorrente = (TipoDocumento) object;

		if (tipoDocumentoCorrente.getId() != null
				&& tipoDocumentoCorrente.getClasseTipoDocumentoInstance().getTipiAree()
						.contains(TipoAreaContabile.class.getName())) {
			// la carico solamente se è abilitata
			TipoAreaContabile tipoAreaContabileCorrente = contabilitaAnagraficaBD
					.caricaTipoAreaContabilePerTipoDocumento(tipoDocumentoCorrente.getId());
			super.setFormObject(tipoAreaContabileCorrente);
		}
		tipoDocumento = tipoDocumentoCorrente;
		logger.debug("--> Exit setFormObject");
	}
}
