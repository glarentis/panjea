package it.eurotn.panjea.anagrafica.rich.editors.azienda;

import it.eurotn.panjea.anagrafica.domain.Azienda;
import it.eurotn.panjea.anagrafica.domain.SedeAnagrafica;
import it.eurotn.panjea.anagrafica.domain.SedeAzienda;
import it.eurotn.panjea.anagrafica.domain.TipoSedeEntita;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.panjea.anagrafica.service.exception.SedeAnagraficaOrphanException;
import it.eurotn.panjea.anagrafica.service.exception.SedeEntitaPrincipaleAlreadyExistException;
import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import java.awt.Component;
import java.awt.Window;
import java.util.List;
import java.util.Locale;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.dialog.CloseAction;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.selection.dialog.ListSelectionDialog;
import org.springframework.richclient.util.RcpSupport;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;

/**
 * 
 * Page per SedeAzienda.
 * 
 * @author adriano
 * @version 1.0, 02/gen/08
 * 
 */
public class SedeAziendaPage extends FormBackedDialogPageEditor implements InitializingBean {
	/**
	 * Comando per creare una nuova sede entita selezionando prima una sede anagrafica esistente.
	 * 
	 * @author Aracno
	 * @version 1.0, 28/lug/06
	 */
	private class NewSelectionSedeAnagraficaCommad extends ActionCommand {

		/**
		 * Costruttore.
		 * 
		 */
		public NewSelectionSedeAnagraficaCommad() {
			super(NEW_SELECTION_SEDE_ANAGRAFICA_COMMAND);
			setSecurityControllerId(PAGE_ID + "." + NEW_SELECTION_SEDE_ANAGRAFICA_COMMAND);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			MessageSourceAccessor messageSourceAccessor = (MessageSourceAccessor) ApplicationServicesLocator.services()
					.getService(MessageSourceAccessor.class);
			String titolo = messageSourceAccessor.getMessage("selezioneSedeAnagraficaDialog.title", new Object[] {},
					Locale.getDefault());

			List<SedeAnagrafica> listSediAnagrafiche = anagraficaBD.caricaSediAnagraficaAzienda(azienda);
			ListSelectionDialog selectionDialog = new ListSelectionDialog(titolo, (Window) null, listSediAnagrafiche) {

				@Override
				protected void onSelect(Object selection) {
					getEditorNewCommand().execute();
					SedeAzienda nuovaSedeAzienda = (SedeAzienda) getForm().getFormObject();
					nuovaSedeAzienda.setSede((SedeAnagrafica) selection);
					setFormObject(nuovaSedeAzienda);
				}
			};

			selectionDialog.setRenderer(new DefaultListCellRenderer() {
				/**
				 * Comment for <code>serialVersionUID</code>
				 */
				private static final long serialVersionUID = -6600465552132434943L;

				@Override
				public Component getListCellRendererComponent(JList arg0, Object arg1, int arg2, boolean arg3,
						boolean arg4) {
					JLabel label = (JLabel) super.getListCellRendererComponent(arg0, arg1, arg2, arg3, arg4);
					label.setText(((SedeAnagrafica) arg1).getDescrizione() + " - "
							+ ((SedeAnagrafica) arg1).getIndirizzo() + " - "
							+ ((SedeAnagrafica) arg1).getDatiGeografici().getDescrizioneLocalita());
					return label;
				}
			});
			selectionDialog.showDialog();
		}
	}

	private final Logger logger = Logger.getLogger(SedeAziendaPage.class);

	private final IAnagraficaBD anagraficaBD;
	private IAnagraficaTabelleBD anagraficaTabelleBD;

	private Azienda azienda;
	private SedeAzienda sedeAziendaPrincipale = null;
	private static final String PAGE_ID = "sediAziendaTablePage.sedeAziendaPage";
	private static final String CAMBIA_SEDE_PRINCILALE_DIALOG_TITLE = "cambiaSedePrincipaleDialog.title";
	private static final String CAMBIA_SEDE_PRINCILALE_DIALOG_MESSAGE = "cambiaSedePrincipaleDialog.message";
	private boolean isConfirmationDialogDeleteOrphanConfirmed = false;
	private static final String NEW_SELECTION_SEDE_ANAGRAFICA_COMMAND = "newSelectionSedeAnagraficaCommand";
	private NewSelectionSedeAnagraficaCommad newSelectionSedeAnagraficaCommad;

	/**
	 * Costruttore.
	 * 
	 * @param azienda
	 *            azienda
	 * @param anagraficaBD
	 *            anagraficaBD
	 */
	public SedeAziendaPage(final Azienda azienda, final IAnagraficaBD anagraficaBD) {
		super(PAGE_ID, new SedeAziendaForm(new SedeAzienda()));
		this.azienda = azienda;
		this.anagraficaBD = anagraficaBD;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		azienda.getAbilitata();
	}

	/**
	 * @param sedeAzienda
	 *            sede azienda
	 * @return <code>true</code> se la sede è principale
	 */
	private boolean checkIfSedePrincipale(SedeAzienda sedeAzienda) {
		if (sedeAzienda.getTipoSede().isSedePrincipale()) {
			return true;
		}
		return false;
	}

	/**
	 * Gestisce il cambio della sede principale.
	 * 
	 * @param e
	 *            {@link SedeEntitaPrincipaleAlreadyExistException}
	 * @return object
	 */
	private Object createAndShowCambiaSedePrincipaleDialog(SedeEntitaPrincipaleAlreadyExistException e) {

		SedeAzienda sedeAziendaPrincipaleAlreadyExist = (SedeAzienda) e.getSedeEntitaPrincipale();
		MessageSourceAccessor messageSourceAccessor = (MessageSourceAccessor) ApplicationServicesLocator.services()
				.getService(MessageSourceAccessor.class);

		String titolo = messageSourceAccessor.getMessage(CAMBIA_SEDE_PRINCILALE_DIALOG_TITLE, new Object[] {},
				Locale.getDefault());

		Object[] parameters = new Object[] { sedeAziendaPrincipaleAlreadyExist.getSede().getIndirizzo(),
				sedeAziendaPrincipaleAlreadyExist.getSede().getDatiGeografici().getDescrizioneLocalita(),
				sedeAziendaPrincipaleAlreadyExist.getSede().getDatiGeografici().getDescrizioneNazione() };
		String messaggio = messageSourceAccessor.getMessage(CAMBIA_SEDE_PRINCILALE_DIALOG_MESSAGE, parameters,
				Locale.getDefault());

		EventList<TipoSedeEntita> listaTipiSedeAzienda = new BasicEventList<TipoSedeEntita>();
		List<TipoSedeEntita> tipiSedeSecondari = anagraficaTabelleBD.caricaTipiSedeSecondari();
		listaTipiSedeAzienda.addAll(tipiSedeSecondari);
		FilterList<TipoSedeEntita> filterList = new FilterList<TipoSedeEntita>(listaTipiSedeAzienda);

		ListSelectionDialog listSelectionDialog = new ListSelectionDialog(titolo, (Window) null, filterList) {

			@Override
			protected void onSelect(Object selection) {
				SedeAzienda sedeAziendaResult = (SedeAzienda) getForm().getFormObject();
				TipoSedeEntita tipoSedeEntita = (TipoSedeEntita) selection;
				anagraficaBD.cambiaSedePrincipaleAzienda(sedeAziendaResult, tipoSedeEntita);

				sedeAziendaPrincipale = anagraficaBD.caricaSedePrincipaleAzienda(azienda);
				// TODO logger.debug("--> sede entit� principale con id " + sedeAziendaPrincipale.getId());
				getForm().setFormObject(sedeAziendaPrincipale);

				PanjeaLifecycleApplicationEvent event = new PanjeaLifecycleApplicationEvent(
						LifecycleApplicationEvent.MODIFIED, sedeAziendaPrincipale);
				Application.instance().getApplicationContext().publishEvent(event);
			}

		};
		listSelectionDialog.setRenderer(new DefaultListCellRenderer() {
			/**
			 * Comment for <code>serialVersionUID</code>
			 */
			private static final long serialVersionUID = -6600465552132434943L;

			@Override
			public Component getListCellRendererComponent(JList arg0, Object arg1, int arg2, boolean arg3, boolean arg4) {
				JLabel label = (JLabel) super.getListCellRendererComponent(arg0, arg1, arg2, arg3, arg4);
				label.setText(((TipoSedeEntita) arg1).getCodice() + " - " + ((TipoSedeEntita) arg1).getDescrizione());
				return label;
			}
		});

		listSelectionDialog.setDescription(messaggio);
		listSelectionDialog.setCloseAction(CloseAction.DISPOSE);
		listSelectionDialog.showDialog();
		return sedeAziendaPrincipale;
	}

	/**
	 * Cancella la sede azienda.
	 * 
	 * @param sedeAzienda
	 *            sede da cancellare
	 * @throws SedeAnagraficaOrphanException
	 *             SedeAnagraficaOrphanExceptino
	 */
	private void deleteSede(SedeAzienda sedeAzienda) throws SedeAnagraficaOrphanException {
		anagraficaBD.cancellaSedeAzienda(sedeAzienda);
		PanjeaLifecycleApplicationEvent event = new PanjeaLifecycleApplicationEvent(LifecycleApplicationEvent.DELETED,
				sedeAzienda);
		Application.instance().getApplicationContext().publishEvent(event);
	}

	@Override
	protected Object doDelete() {
		// // verifica se sede principale
		SedeAzienda sedeAzienda = (SedeAzienda) getBackingFormPage().getFormObject();
		boolean isSedePrincipale = checkIfSedePrincipale(sedeAzienda);
		if (isSedePrincipale) {
			// blocco la cancellazione della sede se e' una sede entita principale
			showAlertSedePrincipaleDialog();
			return null;
		}
		// altrimenti cancella sedeEntita
		try {
			deleteSede(sedeAzienda);
		} catch (SedeAnagraficaOrphanException e) {
			// nel caso in cui non ci sono altre sedi che usano la sede anagrafica chiede di eliminare anche quella
			if (!showConfirmationDialogDeleteOrphan(sedeAzienda, e)) {
				return null;
			}
		}
		return sedeAzienda;
	}

	@Override
	protected Object doSave() {
		try {
			SedeAzienda sedeAziendaDaSalvare = (SedeAzienda) getForm().getFormObject();
			if (sedeAziendaDaSalvare.isNew()) {
				sedeAziendaDaSalvare.setAzienda(this.azienda);
			}
			SedeAzienda sedeAziendaResult = anagraficaBD.salvaSedeAzienda(sedeAziendaDaSalvare);

			PanjeaLifecycleApplicationEvent event = new PanjeaLifecycleApplicationEvent(
					LifecycleApplicationEvent.MODIFIED, (sedeAziendaResult));
			Application.instance().getApplicationContext().publishEvent(event);
			return sedeAziendaResult;
		} catch (SedeEntitaPrincipaleAlreadyExistException e) {
			// risetto la proprietà perchè così il form risulta ancora dirty e si riabilita in saveCommand
			Object object = getForm().getValueModel("abilitato").getValue();
			getForm().getValueModel("abilitato").setValue(false);
			getForm().commit();
			getForm().getValueModel("abilitato").setValue(object);
			return createAndShowCambiaSedePrincipaleDialog(e);
		} catch (Exception e) {
			logger.error("--> Errore nel salvare la sede dell'azienda", e);
			return null;
		}
	}

	@Override
	protected AbstractCommand[] getCommand() {
		// getNewSelectionSedeAnagraficaCommand()
		AbstractCommand[] abstractCommands = new AbstractCommand[] { getNewSelectionSedeAnagraficaCommand(),
				toolbarPageEditor.getNewCommand(), toolbarPageEditor.getSaveCommand(),
				toolbarPageEditor.getLockCommand(), toolbarPageEditor.getUndoCommand(),
				toolbarPageEditor.getDeleteCommand() };
		return abstractCommands;
	}

	/**
	 * 
	 * @return newSelectionSedeAnagraficaCommad
	 */
	public NewSelectionSedeAnagraficaCommad getNewSelectionSedeAnagraficaCommand() {
		if (newSelectionSedeAnagraficaCommad == null) {
			newSelectionSedeAnagraficaCommad = new NewSelectionSedeAnagraficaCommad();
		}
		return newSelectionSedeAnagraficaCommad;
	}

	@Override
	public void loadData() {

	}

	@Override
	public void onPostPageOpen() {

	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public void refreshData() {

	}

	/**
	 * @param anagraficaTabelleBD
	 *            The anagraficaTabelleBD to set.
	 */
	public void setAnagraficaTabelleBD(IAnagraficaTabelleBD anagraficaTabelleBD) {
		this.anagraficaTabelleBD = anagraficaTabelleBD;
	}

	/**
	 * @param azienda
	 *            the azienda to set
	 */
	public void setAzienda(Azienda azienda) {
		this.azienda = azienda;
	}

	/**
	 * Visualizza il dialog per la conferma della cancellazione della sede.
	 */
	private void showAlertSedePrincipaleDialog() {
		MessageSourceAccessor messageSourceAccessor = (MessageSourceAccessor) ApplicationServicesLocator.services()
				.getService(MessageSourceAccessor.class);
		Object[] parameters = new Object[] { messageSourceAccessor.getMessage(azienda.getDomainClassName(),
				new Object[] {}, Locale.getDefault()) };
		String titolo = messageSourceAccessor.getMessage("sedeEntita.deleteSedePrincipale.confirm.title",
				new Object[] {}, Locale.getDefault());
		String messaggio = messageSourceAccessor.getMessage("sedeEntita.deleteSedePrincipale.confirm.message",
				parameters, Locale.getDefault());
		MessageDialog dialog = new MessageDialog(titolo, messaggio);
		dialog.setCloseAction(CloseAction.DISPOSE);
		dialog.setModal(true);
		dialog.showDialog();
	}

	/**
	 * Visualizza il dialog per la conferma della cancellazione della sede orfana.
	 * 
	 * @param sedeAzienda
	 *            sede azienda
	 * @param e
	 *            {@link SedeAnagraficaOrphanException}
	 * @return <code>true</code> se è confermata la cancellazione
	 */
	private boolean showConfirmationDialogDeleteOrphan(final SedeAzienda sedeAzienda, SedeAnagraficaOrphanException e) {

		final MessageSourceAccessor messageSourceAccessor = (MessageSourceAccessor) ApplicationServicesLocator
				.services().getService(MessageSourceAccessor.class);
		SedeAnagrafica sedeAnagraficaorphan = (SedeAnagrafica) e.getSedeAnagraficaOrphan();
		Object[] parameters = new Object[] {
				messageSourceAccessor.getMessage(azienda.getDomainClassName(), new Object[] {}, Locale.getDefault()),
				azienda.getDenominazione(), sedeAnagraficaorphan.getDescrizione(),
				sedeAnagraficaorphan.getDatiGeografici().getDescrizioneLocalita(),
				sedeAnagraficaorphan.getDatiGeografici().getDescrizioneLivelloAmministrativo2(),
				sedeAnagraficaorphan.getDatiGeografici().getDescrizioneNazione() };

		String titolo = messageSourceAccessor.getMessage("sedeEntita.sedeAnagraficaOrphan.delete.confirm.title",
				new Object[] {}, Locale.getDefault());
		String messaggio = messageSourceAccessor.getMessage("sedeEntita.sedeAnagraficaOrphan.delete.confirm.message",
				parameters, Locale.getDefault());
		ConfirmationDialog dialog = new ConfirmationDialog(titolo, messaggio) {

			@Override
			protected void onConfirm() {
				anagraficaBD.cancellaSedeAzienda(sedeAzienda, true);
				PanjeaLifecycleApplicationEvent event = new PanjeaLifecycleApplicationEvent(
						LifecycleApplicationEvent.DELETED, sedeAzienda);
				Application.instance().getApplicationContext().publishEvent(event);
				isConfirmationDialogDeleteOrphanConfirmed = true;
			}

		};
		dialog.showDialog();
		return isConfirmationDialogDeleteOrphanConfirmed;
	}
}
