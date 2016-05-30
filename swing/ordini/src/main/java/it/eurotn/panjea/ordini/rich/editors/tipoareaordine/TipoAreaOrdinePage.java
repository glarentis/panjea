package it.eurotn.panjea.ordini.rich.editors.tipoareaordine;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.magazzino.rich.editors.tipoareamagazzino.TipoAreaMagazzinoPage;
import it.eurotn.panjea.ordini.domain.documento.TipoAreaOrdine;
import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;
import it.eurotn.panjea.ordini.rich.forms.tipoareaordine.TipoAreaOrdineForm;
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
 * @see TipoAreaMagazzinoPage
 * @author giangi
 *
 */
public class TipoAreaOrdinePage extends FormBackedDialogPageEditor {
	private class CreaAreaConfirmationDialog extends ConfirmationDialog {

		private boolean canOpen;

		/**
		 *
		 * @param title
		 *            titolo per la conferma
		 * @param message
		 *            messa ggio di conferma
		 */
		public CreaAreaConfirmationDialog(final String title, final String message) {
			super(title, message);
		}

		/**
		 *
		 * @return true se posso parire la pagine
		 */
		public boolean isCanOpen() {
			return canOpen;
		}

		@Override
		protected void onConfirm() {
			getNuovoTipoAreaOrdineCommand().execute();
			canOpen = true;
		}

	}

	/**
	 *
	 * ActionCommand per la cancellazione di una tipoAreaDocumento.
	 *
	 * @author adriano
	 * @version 1.0, 09/lug/08
	 *
	 */
	private class EliminaTipoAreaOrdineCommand extends ActionCommand {

		private static final String COMMAND_ID = "deleteCommand";

		/**
		 * Costruttore.
		 */
		public EliminaTipoAreaOrdineCommand() {
			super(COMMAND_ID);
			setSecurityControllerId(TipoAreaOrdinePage.PAGE_ID + ".controller");
			CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
					CommandConfigurer.class);
			c.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			TipoAreaOrdine tipoAreaOrdine = (TipoAreaOrdine) getForm().getFormObject();
			if (tipoAreaOrdine.getId() != null) {
				ordiniDocumentoBD.cancellaTipoAreaOrdine(tipoAreaOrdine);
				TipoAreaOrdinePage.this.firePropertyChange(JecCompositeDialogPage.SHOW_INIT_PAGE, null, null);
				TipoAreaOrdinePage.this.firePropertyChange("pageValid", true, false);
				getNuovoTipoAreaOrdineCommand().execute();
			}
		}

		@Override
		protected void onButtonAttached(AbstractButton button) {
			super.onButtonAttached(button);
			button.setName(PAGE_ID + "." + COMMAND_ID);
		}

	}

	private class NuovoTipoAreaOrdineCommand extends ActionCommand {
		private static final String COMMAND_ID = "newCommand";

		/**
		 * Costruttore.
		 */
		public NuovoTipoAreaOrdineCommand() {
			super(getPageEditorId() + "." + COMMAND_ID);
			setSecurityControllerId(getPageEditorId() + ".controller");
		}

		@Override
		protected void doExecuteCommand() {
			TipoAreaOrdine tipoAreaOrdine = new TipoAreaOrdine();
			tipoAreaOrdine.setTipoDocumento(TipoAreaOrdinePage.this.tipoDocumento);
			TipoAreaOrdinePage.super.setFormObject(tipoAreaOrdine);
		}
	}

	private static Logger logger = Logger.getLogger(TipoAreaOrdinePage.class);

	protected static final String PAGE_ID = "tipoAreaOrdinePage";
	private static final String TITLE_CONFIRMATION = "tipoAreaOrdinePage.ask.new.tipoAreaOrdine.title";
	private static final String MESSAGE_CONFIRMATION = "tipoAreaOrdinePage.ask.new.tipoAreaOrdine.message";

	private TipoDocumento tipoDocumento = null;

	private IOrdiniDocumentoBD ordiniDocumentoBD;

	private ActionCommand nuovoTipoAreaOrdineCommand = null;
	private ActionCommand eliminaTipoAreaOrdineCommand = null;

	/**
	 * costruttore.
	 */
	public TipoAreaOrdinePage() {
		super(PAGE_ID, new TipoAreaOrdineForm(new TipoAreaOrdine()));
		logger.debug("--> Enter TipoAreaOrdinePage");
	}

	@Override
	protected Object doSave() {
		logger.debug("--> Enter doSave");
		TipoAreaOrdine tipoAreaOrdine = (TipoAreaOrdine) getForm().getFormObject();
		tipoAreaOrdine.setTipoDocumento(tipoDocumento);

		TipoAreaOrdine tipoAreaOrdineSalvata = ordiniDocumentoBD.salvaTipoAreaOrdine(tipoAreaOrdine);
		super.setFormObject(tipoAreaOrdineSalvata);
		if (tipoAreaOrdine.isNew()) {
			firePropertyChange("pageValid", false, true);
		}
		logger.debug("--> Exit doSave");
		return tipoAreaOrdine.getTipoDocumento();
	}

	@Override
	protected AbstractCommand[] getCommand() {
		AbstractCommand[] abstractCommands = new AbstractCommand[] { toolbarPageEditor.getLockCommand(),
				toolbarPageEditor.getSaveCommand(), toolbarPageEditor.getUndoCommand(),
				getEliminaTipoAreaOrdineCommand() };
		return abstractCommands;
	}

	@Override
	public AbstractCommand getEditorNewCommand() {
		return null;
	}

	/**
	 *
	 * @return command per cancellare l'area.
	 */
	private AbstractCommand getEliminaTipoAreaOrdineCommand() {
		if (eliminaTipoAreaOrdineCommand == null) {
			eliminaTipoAreaOrdineCommand = new EliminaTipoAreaOrdineCommand();
			new PanjeaFormGuard(getForm().getFormModel(), eliminaTipoAreaOrdineCommand, FormGuard.ON_NOERRORS);
		}
		return eliminaTipoAreaOrdineCommand;
	}

	/**
	 *
	 * @return comando per generare un nuovo tao
	 */
	private ActionCommand getNuovoTipoAreaOrdineCommand() {
		if (nuovoTipoAreaOrdineCommand == null) {
			nuovoTipoAreaOrdineCommand = new NuovoTipoAreaOrdineCommand();
		}
		return nuovoTipoAreaOrdineCommand;
	}

	/**
	 * @return the ordiniDocumentoBD
	 */
	public IOrdiniDocumentoBD getOrdiniDocumentoBD() {
		return ordiniDocumentoBD;
	}

	/**
	 * @return the tipoDocumento
	 */
	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}

	@Override
	public void loadData() {
		logger.debug("--> Enter loadData");
		if ((tipoDocumento == null) || (tipoDocumento.getClasseTipoDocumento() == null)) {
			firePropertyChange("pageValid", true, false);
			firePropertyChange(ButtonCompositeDialogPage.PAGE_VISIBLE, true, false);
			return;
		}

		if (tipoDocumento.getClasseTipoDocumentoInstance().getTipiAree().contains(TipoAreaOrdine.class.getName())) {
			// fire property change con proprieta "pageVisible" per comunicare
			// al command della pagina corrente
			// di rendersi visibile
			firePropertyChange(ButtonCompositeDialogPage.PAGE_VISIBLE, false, true);

			TipoAreaOrdine tam = (TipoAreaOrdine) getForm().getFormObject();

			if (tam.isNew()) {
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
		// se non c'e' un tipo documeto caricato non posso caricare il tipo area
		// contabile
		if (idTipoDoc == null) {
			return false;
		}

		if (!tipoDocumento.getClasseTipoDocumentoInstance().getTipiAree().contains(TipoAreaOrdine.class.getName())) {
			return false;
		}

		TipoAreaOrdine tam = (TipoAreaOrdine) getForm().getFormObject();

		// Se non esiste l'area contabile chiedo se crearla
		if (tam.isNew()) {
			if (!PanjeaSwingUtil.hasPermission("modificaTipoAreaOrdine")) {
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
	public boolean onUndo() {
		boolean undo = super.onUndo();

		// HACK usato solamente perchè il table binding ( in questo caso quello sui layout di stampa ) non gestisce il
		// ripristino dei dati sul revert del form se si occupa anche della loro modifica ( non se gestisce solo
		// inserimento e cancellazione )
		TipoDocumento tipoDocumentoCorrente = tipoDocumento;
		tipoDocumento = null;
		setFormObject(tipoDocumentoCorrente);
		return undo;
	}

	@Override
	public void publishCreateEvent(Object obj) {
	}

	@Override
	public void refreshData() {
		logger.debug("--> Enter refreshData");
		loadData();
		logger.debug("--> Exit refreshData");
	}

	@Override
	public void setFormObject(Object object) {
		logger.debug("--> Enter setFormObject");
		TipoDocumento tipoDocumentoCorrente = (TipoDocumento) object;

		if (tipoDocumentoCorrente.getId() != null
				&& tipoDocumentoCorrente.getClasseTipoDocumentoInstance().getTipiAree()
						.contains(TipoAreaOrdine.class.getName())) {
			// la carico solamente se è abilitata
			TipoAreaOrdine tipoAreaOrdineCorrente = ordiniDocumentoBD
					.caricaTipoAreaOrdinePerTipoDocumento(tipoDocumentoCorrente.getId());
			super.setFormObject(tipoAreaOrdineCorrente);
		}

		tipoDocumento = tipoDocumentoCorrente;
		logger.debug("--> Exit setFormObject");
	}

	/**
	 * @param ordiniDocumentoBD
	 *            the ordiniDocumentoBD to set
	 */
	public void setOrdiniDocumentoBD(IOrdiniDocumentoBD ordiniDocumentoBD) {
		this.ordiniDocumentoBD = ordiniDocumentoBD;
	}

	/**
	 * @param tipoDocumento
	 *            the tipoDocumento to set
	 */
	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
}
