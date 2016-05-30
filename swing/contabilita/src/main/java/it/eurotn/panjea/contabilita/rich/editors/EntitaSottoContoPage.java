/**
 *
 */
package it.eurotn.panjea.contabilita.rich.editors;

import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.contabilita.domain.Conto.SottotipoConto;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD;
import it.eurotn.panjea.contabilita.rich.forms.EntitaSottoContoForm;
import it.eurotn.panjea.contabilita.rich.pm.EntitaSottoContoPM;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.dialog.MessageDialog;

/**
 * @author adriano
 * @version 1.0, 11/giu/07
 */
public class EntitaSottoContoPage extends FormBackedDialogPageEditor {

	private class EliminaSottoContoCommand extends ActionCommand {

		private static final String COMMAND_ID = "deleteCommand";

		/**
		 * Constructor.
		 */
		public EliminaSottoContoCommand() {
			super(COMMAND_ID);
			setSecurityControllerId(getPageEditorId() + "." + COMMAND_ID);
		}

		@Override
		protected void doExecuteCommand() {
			final EntitaSottoContoPM entitaSottoContoPMToDelete = (EntitaSottoContoPM) getBackingFormPage()
					.getFormObject();
			MessageSourceAccessor messageSourceAccessor = (MessageSourceAccessor) ApplicationServicesLocator.services()
					.getService(MessageSourceAccessor.class);
			Object[] parameters = new Object[] {
					messageSourceAccessor.getMessage(entitaSottoContoPMToDelete.getDomainClassName(), new Object[] {},
							Locale.getDefault()),
					entitaSottoContoPMToDelete.getSottoConto().getConto().getDescrizione() };
			String titolo = messageSourceAccessor.getMessage("entitaSottoContoPM.delete.confirm.title",
					new Object[] {}, Locale.getDefault());
			String messaggio = messageSourceAccessor.getMessage("entitaSottoContoPM.delete.confirm.message",
					parameters, Locale.getDefault());
			ConfirmationDialog dialog = new ConfirmationDialog(titolo, messaggio) {

				@Override
				protected void onConfirm() {
					EntitaSottoContoPage.this.contabilitaAnagraficaBD.cancellaSottoConto(entitaSottoContoPMToDelete
							.getSottoConto());
					EntitaSottoContoPage.this.refreshData();
				}
			};
			dialog.showDialog();
		}
	}

	private static Logger logger = Logger.getLogger(EntitaSottoContoPage.class);

	private static final String PAGE_ID = "entitaSottoContoPage";
	private IContabilitaAnagraficaBD contabilitaAnagraficaBD = null;
	private IAnagraficaBD anagraficaBD = null;
	private Entita entita = null;
	private SottotipoConto sottoTipoConto = null;

	private EliminaSottoContoCommand eliminaSottoContoCommand = null;

	/**
	 * Constructor.
	 */
	public EntitaSottoContoPage() {
		super(PAGE_ID, new EntitaSottoContoForm(new EntitaSottoContoPM()));
	}

	@Override
	protected Object doSave() {
		EntitaSottoContoPM entitaSottoContoPM = (EntitaSottoContoPM) getForm().getFormObject();
		SottoConto sottoConto = contabilitaAnagraficaBD.creaSottoContoPerEntita(entita);
		entitaSottoContoPM.setSottoConto(sottoConto);
		super.setFormObject(entitaSottoContoPM);
		return entita;
	}

	/**
	 * @return Returns the anagraficaBD.
	 */
	public IAnagraficaBD getAnagraficaBD() {
		return anagraficaBD;
	}

	@Override
	protected AbstractCommand[] getCommand() {
		AbstractCommand[] abstractCommands = new AbstractCommand[] { toolbarPageEditor.getLockCommand(),
				toolbarPageEditor.getSaveCommand(), toolbarPageEditor.getUndoCommand(), getEliminaSottoContoCommand() };
		return abstractCommands;
	}

	/**
	 * @return Returns the contabilitaAnagraficaBD.
	 */
	public IContabilitaAnagraficaBD getContabilitaAnagraficaBD() {
		return contabilitaAnagraficaBD;
	}

	/**
	 * @return eliminaSottoContoCommand
	 */
	private ActionCommand getEliminaSottoContoCommand() {
		if (eliminaSottoContoCommand == null) {
			eliminaSottoContoCommand = new EliminaSottoContoCommand();
			getCommandConfigurer().configure(eliminaSottoContoCommand);
		}
		return eliminaSottoContoCommand;
	}

	/**
	 * @return Returns the sottoTipoConto.
	 */
	public String getSottoTipoConto() {
		return sottoTipoConto.name();
	}

	@Override
	public void loadData() {
		EntitaSottoContoPM entitaSottoContoPM = new EntitaSottoContoPM();
		entitaSottoContoPM.setSottoTipoConto(sottoTipoConto);
		logger.debug("--> sottoTipoConto " + sottoTipoConto);
		SottoConto sottoConto = contabilitaAnagraficaBD.caricaSottoContoPerEntita(sottoTipoConto, entita.getCodice());
		logger.debug("--> sottoConto caricato " + sottoConto);
		entitaSottoContoPM.setSottoConto(sottoConto);
		super.setFormObject(entitaSottoContoPM);
	}

	@Override
	public void onPostPageOpen() {

	}

	@Override
	public boolean onPrePageOpen() {
		boolean initializePage = true;
		if (entita.isNew()) {
			initializePage = false;
			MessageSourceAccessor messageSourceAccessor = (MessageSourceAccessor) ApplicationServicesLocator.services()
					.getService(MessageSourceAccessor.class);
			String titolo = messageSourceAccessor.getMessage("entita.null.conto.messageDialog.title", new Object[] {},
					Locale.getDefault());
			String messaggio = messageSourceAccessor.getMessage("entita.null.conto.messageDialog.message",
					new Object[] { messageSourceAccessor.getMessage(entita.getDomainClassName(), new Object[] {},
							Locale.getDefault()) }, Locale.getDefault());
			new MessageDialog(titolo, messaggio).showDialog();
		}
		return initializePage;
	}

	@Override
	public void refreshData() {
		loadData();
	}

	/**
	 * @param anagraficaBD
	 *            The anagraficaBD to set.
	 */
	public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
		this.anagraficaBD = anagraficaBD;
	}

	/**
	 * @param contabilitaAnagraficaBD
	 *            The contabilitaAnagraficaBD to set.
	 */
	public void setContabilitaAnagraficaBD(IContabilitaAnagraficaBD contabilitaAnagraficaBD) {
		this.contabilitaAnagraficaBD = contabilitaAnagraficaBD;
	}

	@Override
	public void setFormObject(Object object) {
		entita = (Entita) object;
	}

	/**
	 * @param sottoTipoConto
	 *            the sottoTipoConto to set
	 */
	public void setSottoTipoConto(String sottoTipoConto) {
		logger.debug("--> Enter setSottoTipoConto");
		if (sottoTipoConto.toLowerCase().equals("cliente")) {
			this.sottoTipoConto = SottotipoConto.CLIENTE;
		} else if (sottoTipoConto.toLowerCase().equals("fornitore")) {
			this.sottoTipoConto = SottotipoConto.FORNITORE;
		}
	}
}
