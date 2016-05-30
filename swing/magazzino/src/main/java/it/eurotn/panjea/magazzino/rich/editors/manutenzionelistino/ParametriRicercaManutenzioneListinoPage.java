package it.eurotn.panjea.magazzino.rich.editors.manutenzionelistino;

import it.eurotn.panjea.magazzino.domain.VersioneListino;
import it.eurotn.panjea.magazzino.exception.ListinoManutenzioneNonValidoException;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.forms.manutenzionelistino.ParametriAggiornaManutenzioneListinoForm;
import it.eurotn.panjea.magazzino.rich.forms.manutenzionelistino.ParametriRicercaManutenzioneListinoForm;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaManutenzioneListino;
import it.eurotn.panjea.rich.forms.PanjeaFormGuard;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;
import it.eurotn.rich.form.PanjeaAbstractForm;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.converter.ObjectConverterManager;

public class ParametriRicercaManutenzioneListinoPage extends FormBackedDialogPageEditor implements InitializingBean {

	private class CercaRigheManutenzioneListinoCommand extends ActionCommand {

		private static final String COMMAND_ID = "searchCommand";

		/**
		 * Dati i parametri visualizza la lista di righe manutenzione listino.
		 */
		public CercaRigheManutenzioneListinoCommand() {
			super(COMMAND_ID);
			this.setSecurityControllerId(getPageEditorId() + COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			getBackingFormPage().getFormModel().commit();
			ParametriRicercaManutenzioneListino parametriRicercaManutenzioneListino = (ParametriRicercaManutenzioneListino) getBackingFormPage()
					.getFormModel().getFormObject();
			VersioneListino versioneListinoDestinazione = (VersioneListino) parametriManutezioneForm
					.getValue("versioneListino");
			parametriRicercaManutenzioneListino.setVersioneListinoDestinazione(versioneListinoDestinazione);

			try {
				magazzinoAnagraficaBD.inserisciRigheRicercaManutenzioneListino(parametriRicercaManutenzioneListino);
				parametriRicercaManutenzioneListino.setEffettuaRicerca(true);

				ParametriRicercaManutenzioneListinoPage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED,
						null, parametriRicercaManutenzioneListino);
			} catch (ListinoManutenzioneNonValidoException e) {
				StringBuilder sbMessage = new StringBuilder(200);
				if (e.getListinoValido() != null) {
					sbMessage
							.append("La manutenzione listino corrente prevede <br> la modifica solamente delle righe del listino: <br><b>");
					sbMessage.append(ObjectConverterManager.toString(e.getListinoValido()));
					sbMessage.append("</b><br>");
				} else {
					sbMessage
							.append("Non è possibile aggiungere un listino a scaglioni alla manutenzione <br>corrente. Cancellare le righe presenti per continuare.");
				}
				MessageDialog dialog = new MessageDialog("ATTENZIONE", new DefaultMessage(sbMessage.toString(),
						Severity.INFO));
				dialog.showDialog();
			}
		}
	}

	private class ResetParametriRicercaCommand extends ActionCommand {

		private static final String COMMAND_ID = "resetCommand";

		/**
		 * Reset dei parametri.
		 */
		public ResetParametriRicercaCommand() {
			super(COMMAND_ID);
			CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
					CommandConfigurer.class);
			this.setSecurityControllerId(getPageEditorId() + COMMAND_ID);
			c.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			ParametriRicercaManutenzioneListinoPage.logger.debug("--> Reset command");
			ParametriRicercaManutenzioneListinoPage.this.toolbarPageEditor.getNewCommand().execute();
			((ParametriRicercaManutenzioneListinoForm) getBackingFormPage()).getFormModel().setReadOnly(false);
		}
	}

	public static final String PAGE_ID = "parametriRicercaManutenzioneListinoPage";
	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD = null;
	protected static Logger logger = Logger.getLogger(ParametriRicercaManutenzioneListinoPage.class);
	private CercaRigheManutenzioneListinoCommand cercaRigheManutenzioneListinoCommand = null;
	private ResetParametriRicercaCommand resetParametriRicercaCommand = null;
	private ParametriAggiornaManutenzioneListinoForm parametriManutezioneForm;

	/**
	 * Costruttore di default,inizializza un nuovo form.
	 */
	public ParametriRicercaManutenzioneListinoPage() {
		super(PAGE_ID, new ParametriRicercaManutenzioneListinoForm());
		new PanjeaFormGuard(getBackingFormPage().getFormModel(), getCercaRigheManutenzioneListinoCommand());
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		org.springframework.util.Assert.notNull(magazzinoAnagraficaBD, "magazzinoAnagraficaBD cannot be null !");
		((ParametriRicercaManutenzioneListinoForm) getBackingFormPage())
				.setMagazzinoAnagraficaBD(magazzinoAnagraficaBD);
	}

	/**
	 * 
	 * @return command per la ricerca
	 */
	protected CercaRigheManutenzioneListinoCommand getCercaRigheManutenzioneListinoCommand() {
		if (cercaRigheManutenzioneListinoCommand == null) {
			cercaRigheManutenzioneListinoCommand = new CercaRigheManutenzioneListinoCommand();
		}
		return cercaRigheManutenzioneListinoCommand;
	}

	@Override
	protected AbstractCommand[] getCommand() {
		AbstractCommand[] commands = new AbstractCommand[] { getResetParametriRicercaCommand(),
				getCercaRigheManutenzioneListinoCommand() };
		return commands;
	}

	@Override
	public AbstractCommand getEditorNewCommand() {
		return getResetParametriRicercaCommand();
	}

	@Override
	public AbstractCommand getEditorSaveCommand() {
		return getCercaRigheManutenzioneListinoCommand();
	}

	/**
	 * @return null...non ho il comando null
	 */
	@Override
	protected Object getNewEditorObject() {
		return null;
	}

	/**
	 * 
	 * @return command per il reset dei parametri
	 */
	protected ResetParametriRicercaCommand getResetParametriRicercaCommand() {
		if (resetParametriRicercaCommand == null) {
			resetParametriRicercaCommand = new ResetParametriRicercaCommand();
		}
		return resetParametriRicercaCommand;
	}

	@Override
	protected boolean insertControlInScrollPane() {
		return false;
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public void loadData() {

	}

	@Override
	public void onPostPageOpen() {
		ParametriRicercaManutenzioneListino parametriRicercaMovimentazione = (ParametriRicercaManutenzioneListino) getForm()
				.getFormObject();
		if (!parametriRicercaMovimentazione.isEffettuaRicerca()) {
			// richiamo la execute del ResetParametriRicercaCommand perche' appena apro
			// la pagina posso subito inserire i parametri
			getResetParametriRicercaCommand().execute();
		} else {
			// richiamo la execute di EseguiRicercaCommand per effettuare
			// immediatamente la ricerca all'apertuta della Page
			getCercaRigheManutenzioneListinoCommand().execute();
		}
	}

	@Override
	public boolean onPrePageOpen() {
		((PanjeaAbstractForm) getBackingFormPage()).getFormModel().setReadOnly(false);
		return true;
	}

	/**
	 * Sovrascrivo questo metodo per non eseguire nulla ed evitare il salvataggio premendo la combinazione ctrl + S che
	 * e' abilitata di default nella form backed dialog page.
	 * 
	 * @return boolean
	 */
	@Override
	public boolean onSave() {
		return true;
	}

	/**
	 * Sovrascrivo questo metodo per non eseguire l'undo command premendo ctrl + Z che e' abilitato di default nella
	 * form backed dialog page.
	 * 
	 * @return boolean
	 */
	@Override
	public boolean onUndo() {
		return true;
	}

	@Override
	public void refreshData() {

	}

	/**
	 * Set magazzinoAnagraficaBD.
	 * 
	 * @param magazzinoAnagraficaBD
	 *            il bd da utilizzare per accedere alle operazioni sui tipi mezzo trasporto
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}

	/**
	 * @param parametriManutezioneForm
	 *            The parametriManutezioneForm to set.
	 */
	public void setParametriManutezioneForm(ParametriAggiornaManutenzioneListinoForm parametriManutezioneForm) {
		this.parametriManutezioneForm = parametriManutezioneForm;
	}
}
