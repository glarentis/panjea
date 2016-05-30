/**
 *
 */
package it.eurotn.panjea.preventivi.rich.editors.tipoareapreventivo;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.preventivi.domain.documento.TipoAreaPreventivo;
import it.eurotn.panjea.preventivi.rich.bd.IPreventiviBD;
import it.eurotn.panjea.preventivi.rich.forms.tipoareapreventivo.TipoAreaPreventivoForm;
import it.eurotn.panjea.rich.forms.PanjeaFormGuard;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.dialog.ButtonCompositeDialogPage;
import it.eurotn.rich.dialog.JecCompositeDialogPage;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import javax.swing.AbstractButton;

import org.apache.log4j.Logger;
import org.springframework.binding.form.ValidatingFormModel;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.form.FormGuard;

/**
 * @author mattia
 *
 */
public class TipoAreaPreventivoPage extends FormBackedDialogPageEditor {

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
			getNuovoTipoAreaPreventivoCommand().execute();

			// valido il form model e se non ci sono errori salvo il tipo area
			ValidatingFormModel formModel = TipoAreaPreventivoPage.this.getBackingFormPage().getFormModel();
			formModel.validate();
			if (!formModel.getHasErrors()) {
				TipoAreaPreventivoPage.this.onSave();
				TipoAreaPreventivoPage.this.firePropertyChange("pageValid", false, true);
			}

			canOpen = true;
		}

	}

	private class EliminaTipoAreaPreventivoCommand extends ActionCommand {

		private static final String COMMAND_ID = "deleteCommand";

		/**
		 * Costruttore.
		 */
		public EliminaTipoAreaPreventivoCommand() {
			super(COMMAND_ID);
			setSecurityControllerId(PAGE_ID + ".controller");
			CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
					CommandConfigurer.class);
			c.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			TipoAreaPreventivo tipoAreaPreventivo = (TipoAreaPreventivo) getForm().getFormObject();
			if (tipoAreaPreventivo.getId() != null) {
				preventiviBD.cancellaTipoAreaPreventivo(tipoAreaPreventivo);
				TipoAreaPreventivoPage.this.firePropertyChange(JecCompositeDialogPage.SHOW_INIT_PAGE, null, null);
				TipoAreaPreventivoPage.this.firePropertyChange("pageValid", true, false);
				getNuovoTipoAreaPreventivoCommand().execute();
			}
		}

		@Override
		protected void onButtonAttached(AbstractButton button) {
			super.onButtonAttached(button);
			button.setName(PAGE_ID + "." + COMMAND_ID);
		}

	}

	private class NuovoTipoAreaPreventivoCommand extends ActionCommand {
		private static final String COMMAND_ID = "newCommand";

		/**
		 * Costruttore.
		 */
		public NuovoTipoAreaPreventivoCommand() {
			super(getPageEditorId() + "." + COMMAND_ID);
			setSecurityControllerId(getPageEditorId() + ".controller");
		}

		@Override
		protected void doExecuteCommand() {
			TipoAreaPreventivo tipoAreaPreventivo = new TipoAreaPreventivo();
			tipoAreaPreventivo.setTipoDocumento(TipoAreaPreventivoPage.this.tipoDocumento);
			TipoAreaPreventivoPage.super.setFormObject(tipoAreaPreventivo);
		}
	}

	private static Logger logger = Logger.getLogger(TipoAreaPreventivoPage.class);

	protected static final String PAGE_ID = "tipoAreaPreventivoPage";
	private static final String TITLE_CONFIRMATION = "tipoAreaPreventivoPage.ask.new.tipoAreaPreventivo.title";
	private static final String MESSAGE_CONFIRMATION = "tipoAreaPreventivoPage.ask.new.tipoAreaPreventivo.message";
	private static final String PERMESSO_MODIFICA_TIPO_AREA = "modificaTipoAreaPreventivo";

	private TipoDocumento tipoDocumento = null;

	private IPreventiviBD preventiviBD;

	private ActionCommand nuovoTipoAreaPreventivoCommand = null;
	private ActionCommand eliminaTipoAreaPreventivoCommand = null;

	/**
	 * TipoAreaPreventivoPage.
	 */
	public TipoAreaPreventivoPage() {
		super(PAGE_ID, new TipoAreaPreventivoForm(new TipoAreaPreventivo()));
		logger.debug("--> Enter TipoAreaPreventivoPage");
	}

	@Override
	protected Object doSave() {
		logger.debug("--> Enter doSave");
		TipoAreaPreventivo tipoAreaPreventivo = (TipoAreaPreventivo) getForm().getFormObject();
		tipoAreaPreventivo.setTipoDocumento(tipoDocumento);

		TipoAreaPreventivo tipoAreaPreventivoSalvata = preventiviBD.salvaTipoAreaPreventivo(tipoAreaPreventivo);
		super.setFormObject(tipoAreaPreventivoSalvata);
		if (tipoAreaPreventivoSalvata.isNew()) {
			firePropertyChange("pageValid", false, true);
		}
		logger.debug("--> Exit doSave");
		return tipoAreaPreventivoSalvata.getTipoDocumento();
	}

	@Override
	protected AbstractCommand[] getCommand() {
		AbstractCommand[] abstractCommands = new AbstractCommand[] { toolbarPageEditor.getLockCommand(),
				toolbarPageEditor.getSaveCommand(), toolbarPageEditor.getUndoCommand(),
				getEliminaTipoAreaPreventivoCommand() };
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
	private AbstractCommand getEliminaTipoAreaPreventivoCommand() {
		if (eliminaTipoAreaPreventivoCommand == null) {
			eliminaTipoAreaPreventivoCommand = new EliminaTipoAreaPreventivoCommand();
			new PanjeaFormGuard(getForm().getFormModel(), eliminaTipoAreaPreventivoCommand, FormGuard.ON_NOERRORS);
		}
		return eliminaTipoAreaPreventivoCommand;
	}

	/**
	 *
	 * @return comando per generare un nuovo tao
	 */
	private ActionCommand getNuovoTipoAreaPreventivoCommand() {
		if (nuovoTipoAreaPreventivoCommand == null) {
			nuovoTipoAreaPreventivoCommand = new NuovoTipoAreaPreventivoCommand();
		}
		return nuovoTipoAreaPreventivoCommand;
	}

	/**
	 * @return the ordiniDocumentoBD
	 */
	public IPreventiviBD getPreventiviDocumentoBD() {
		return preventiviBD;
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

		if (tipoDocumento.getClasseTipoDocumentoInstance().getTipiAree().contains(TipoAreaPreventivo.class.getName())) {
			// fire property change con proprieta "pageVisible" per comunicare
			// al command della pagina corrente
			// di rendersi visibile
			firePropertyChange(ButtonCompositeDialogPage.PAGE_VISIBLE, false, true);

			EntityBase formObject = (EntityBase) getForm().getFormObject();
			boolean isFormObjectNew = formObject.isNew();
			firePropertyChange("pageValid", isFormObjectNew, !isFormObjectNew);
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
		// se non c'e' un tipo documeto caricato non posso caricare tipi area
		Integer idTipoDoc = tipoDocumento.getId();
		if (idTipoDoc == null) {
			return false;
		}

		if (!tipoDocumento.getClasseTipoDocumentoInstance().getTipiAree().contains(TipoAreaPreventivo.class.getName())) {
			return false;
		}

		boolean canOpen = true;

		// Se non esiste l'area chiedo se crearla
		EntityBase tipoArea = (EntityBase) getForm().getFormObject();
		if (tipoArea.isNew()) {
			if (!PanjeaSwingUtil.hasPermission(PERMESSO_MODIFICA_TIPO_AREA)) {
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
				.contains(TipoAreaPreventivo.class.getName())) {
			TipoAreaPreventivo tipoAreaCorrente = preventiviBD
					.caricaTipoAreaPreventivoPerTipoDocumento(tipoDocumentoCorrente.getId());
			super.setFormObject(tipoAreaCorrente);
		}

		tipoDocumento = tipoDocumentoCorrente;
		logger.debug("--> Exit setFormObject");
	}

	/**
	 * @param preventiviBD
	 *            the preventiviBD to set
	 */
	public void setPreventiviBD(IPreventiviBD preventiviBD) {
		this.preventiviBD = preventiviBD;
	}

	/**
	 * @param tipoDocumento
	 *            the tipoDocumento to set
	 */
	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

}
