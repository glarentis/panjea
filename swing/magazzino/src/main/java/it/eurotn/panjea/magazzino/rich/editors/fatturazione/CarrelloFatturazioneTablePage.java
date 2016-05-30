package it.eurotn.panjea.magazzino.rich.editors.fatturazione;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzinoLite;
import it.eurotn.panjea.magazzino.exception.RigaArticoloNonValidaException;
import it.eurotn.panjea.magazzino.exception.SedeNonAppartieneAdEntitaException;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.forms.fatturazione.ParametriCreazioneDocumentoFatturazioneForm;
import it.eurotn.panjea.magazzino.service.exception.SedePerRifatturazioneAssenteException;
import it.eurotn.panjea.magazzino.util.ParametriCreazioneDocumentoFatturazione;
import it.eurotn.panjea.magazzino.util.PreFatturazioneDTO;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaFatturazione;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.dialog.MessageAlert;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Observable;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Message;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

/**
 * Pagina che contiene tutte le aree magazzino per la fatturazione.
 *
 * @author fattazzo
 *
 */
public class CarrelloFatturazioneTablePage extends AbstractTablePageEditor<AreaMagazzinoLitePM> {

	private class DataDocumentoChangeListener implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			Date date = (Date) evt.getNewValue();
			updateEnabledGeneraFatturazioneCommand(date);
		}
	}

	private class EseguiFatturazioneCommand extends ApplicationWindowAwareCommand {

		private static final String COMMAND_ID = "eseguiFatturazioneCommand";
		public static final String MOVIMENTI_IN_FATTURAZIONE_TROVATI_TITLE = PAGE_ID
				+ ".movimenti.in.fatturazione.trovati.title";
		public static final String MOVIMENTI_IN_FATTURAZIONE_TROVATI_MESSAGE = PAGE_ID
				+ ".movimenti.in.fatturazione.trovati.message";

		private boolean canGenerate = false;

		/**
		 * Costruttore di default.
		 */
		public EseguiFatturazioneCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {

			canGenerate = true;
			Date date = magazzinoDocumentoBD.caricaDataMovimentiInFatturazione();

			// se esistono dei movimenti in fatturazione chiedo se cancellarli
			if (date != null) {
				ConfirmationDialog dialog = new ConfirmationDialog(getMessage(MOVIMENTI_IN_FATTURAZIONE_TROVATI_TITLE),
						getMessage(MOVIMENTI_IN_FATTURAZIONE_TROVATI_MESSAGE)) {

					@Override
					protected void onCancel() {
						canGenerate = false;
						super.onCancel();
					}

					@Override
					protected void onConfirm() {
						canGenerate = true;
					}
				};
				dialog.setResizable(false);
				dialog.showDialog();
			}

			if (canGenerate) {

				MessageAlert generazioneAlert = new MessageAlert(new DefaultMessage(
						"Generazione fatturazione in corso, attendere...", Severity.INFO), 0);
				generazioneAlert.showAlert();

				try {
					CarrelloFatturazioneTablePage.this.firePropertyChange(FATTURA_MOVIMENTI, false, true);

					ParametriCreazioneDocumentoFatturazione parametriCreazioneDocumentoFatturazione = (ParametriCreazioneDocumentoFatturazione) parametriCreazioneDocumentoFatturazioneForm
							.getFormObject();

					List<AreaMagazzinoLite> listAreeDaFatturare = new ArrayList<AreaMagazzinoLite>();
					for (AreaMagazzinoLitePM areaMagazzinoLitePM : risultatiRicercaFatturazioneModel
							.getAreePMSelezionate()) {
						listAreeDaFatturare.add(areaMagazzinoLitePM.getAreaMagazzinoLite());
					}
					try {
						magazzinoDocumentoBD.generaFatturazioneDifferitaTemporanea(listAreeDaFatturare,
								risultatiRicercaFatturazioneModel.getParametriRicercaFatturazione()
								.getTipoDocumentoDestinazione(), parametriCreazioneDocumentoFatturazione
								.getDataDocumento(), parametriCreazioneDocumentoFatturazione.getNote(),
								risultatiRicercaFatturazioneModel.getParametriRicercaFatturazione()
								.getSedePerRifatturazione());

						getSvuotaCarrelloFatturazioneCommand().execute();

						ParametriRicercaFatturazione parametri = new ParametriRicercaFatturazione();
						parametri.setEffettuaRicerca(false);
						LifecycleApplicationEvent event = new OpenEditorEvent(parametri);
						Application.instance().getApplicationContext().publishEvent(event);
						CarrelloFatturazioneTablePage.this.firePropertyChange(PROPERTY_DOCUMENTO_FATTURAZIONE_GENERATO,
								false, parametri);
						event = new OpenEditorEvent("fatturazioniEditor");
						Application.instance().getApplicationContext().publishEvent(event);

					} catch (RigaArticoloNonValidaException e) {
						rigaArticoloNonValidaExceptionHandler(e);
					} catch (SedePerRifatturazioneAssenteException e) {
						sedePerRifatturazioneAssenteExceptionHandler(e, this);
					} catch (SedeNonAppartieneAdEntitaException e) {
						sedeNonAppartieneAdEntitaExceptionHandler(e);
					}
				} catch (Exception e) {
					logger.warn("eccezione non gestita");
					PanjeaSwingUtil.checkAndThrowException(e);
				} finally {
					CarrelloFatturazioneTablePage.this.firePropertyChange(FATTURA_MOVIMENTI, true, false);

					generazioneAlert.closeAlert();
				}
			}
		}

	}

	private class EseguiPreFatturazioneCommand extends ApplicationWindowAwareCommand {

		private static final String COMMAND_ID = "eseguiPreFatturazioneCommand";

		/**
		 * Costruttore.
		 */
		public EseguiPreFatturazioneCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {

			MessageAlert generazioneAlert = new MessageAlert(new DefaultMessage(
					"Generazione pre fatturazione in corso, attendere...", Severity.INFO), 0);
			generazioneAlert.showAlert();

			try {
				ParametriCreazioneDocumentoFatturazione parametriCreazioneDocumentoFatturazione = (ParametriCreazioneDocumentoFatturazione) parametriCreazioneDocumentoFatturazioneForm
						.getFormObject();

				List<AreaMagazzinoLite> listAreeDaFatturare = new ArrayList<AreaMagazzinoLite>();
				for (AreaMagazzinoLitePM areaMagazzinoLitePM : risultatiRicercaFatturazioneModel.getAreePMSelezionate()) {
					listAreeDaFatturare.add(areaMagazzinoLitePM.getAreaMagazzinoLite());
				}

				PreFatturazioneDTO preFatturazioneDTO = magazzinoDocumentoBD.generaPreFatturazione(listAreeDaFatturare,
						risultatiRicercaFatturazioneModel.getParametriRicercaFatturazione()
						.getTipoDocumentoDestinazione(), parametriCreazioneDocumentoFatturazione
						.getDataDocumento(), parametriCreazioneDocumentoFatturazione.getNote(),
						risultatiRicercaFatturazioneModel.getParametriRicercaFatturazione().getSedePerRifatturazione());

				LifecycleApplicationEvent event = new OpenEditorEvent(preFatturazioneDTO);
				Application.instance().getApplicationContext().publishEvent(event);

			} catch (RigaArticoloNonValidaException e) {
				rigaArticoloNonValidaExceptionHandler(e);
			} catch (SedePerRifatturazioneAssenteException e) {
				sedePerRifatturazioneAssenteExceptionHandler(e, this);
			} catch (SedeNonAppartieneAdEntitaException e) {
				sedeNonAppartieneAdEntitaExceptionHandler(e);
			} finally {
				generazioneAlert.closeAlert();
			}

		}

	}

	private class ListChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName().equals("rimuoviAreeSelezionate") || evt.getPropertyName().equals("areaAggiunta")
					|| evt.getPropertyName().equals("areaRimossa")) {
				// abilito o meno il command di generazione della fatturazione
				Date date = (Date) parametriCreazioneDocumentoFatturazioneForm.getFormModel()
						.getValueModel("dataDocumento").getValue();
				updateEnabledGeneraFatturazioneCommand(date);
			}
		}
	}

	public class RimuoviCommand extends ApplicationWindowAwareCommand {

		private final String pageId;

		public static final String COMMAND_ID = "rimuoviCommand";
		public static final String RIMUOVI_MESSAGE_DIALOG_STRING_KEY = "rimuovi.confirm.message";
		public static final String RIMUOVI_TITLE_DIALOG_STRING_KEY = "rimuovi.confirm.title";

		/**
		 * Costruttore di default.
		 *
		 * @param pageId
		 *            pageId
		 */
		public RimuoviCommand(final String pageId) {
			super(COMMAND_ID);
			this.pageId = pageId;
			setEnabled(false);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			ConfirmationDialog confirmationDialog = new ConfirmationDialog(getMessage(RIMUOVI_TITLE_DIALOG_STRING_KEY),
					getMessage(RIMUOVI_MESSAGE_DIALOG_STRING_KEY)) {

				@Override
				protected void onConfirm() {
					try {
						// elimino l'area selezionata dalla tabella
						AreaMagazzinoLitePM areaMagazzinoLitePM = getTable().getSelectedObject();
						risultatiRicercaFatturazioneModel.removeAreaSelezionata(areaMagazzinoLitePM);
						getTable().removeRowObject(areaMagazzinoLitePM);
					} catch (RuntimeException ex) {
						this.getCancelCommand().execute();
						throw ex;
					}
				}
			};
			confirmationDialog.setPreferredSize(new Dimension(250, 50));
			confirmationDialog.setResizable(false);
			confirmationDialog.showDialog();
		}

		@Override
		public String getSecurityControllerId() {
			return pageId + "." + COMMAND_ID;
		}
	}

	/**
	 * Command per svuotare il carrello da tutte le aree presenti, notifica alla page.
	 * {@link RisultatiRicercaFatturazioneTablePage} che sono stati cancellati dalla tabella tutti gli elementi presenti
	 *
	 * @author fattazzo
	 */
	private class SvuotaCarrelloFatturazioneCommand extends ApplicationWindowAwareCommand {

		private static final String COMMAND_ID = "svuotaCarrelloFatturazioneCommand";

		/**
		 * Costruttore di default.
		 */
		public SvuotaCarrelloFatturazioneCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			// svuota la tabella di pagamenti
			loadData();

			// pulisce le liste nel modello
			risultatiRicercaFatturazioneModel.rimuoviAreeSelezionate();
		}
	}

	public static final String PAGE_ID = "carrelloFatturazioneTablePage";
	public static final String PROPERTY_DOCUMENTO_FATTURAZIONE_GENERATO = "propertyDocumentoFatturazioneGenerato";
	public static final String FATTURA_MOVIMENTI = "fatturaMovimenti";

	private IMagazzinoDocumentoBD magazzinoDocumentoBD;
	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	private RisultatiRicercaFatturazioneModel risultatiRicercaFatturazioneModel;

	private ParametriCreazioneDocumentoFatturazioneForm parametriCreazioneDocumentoFatturazioneForm;
	private final JLabel labelValidDocumenti = new JLabel();

	private SvuotaCarrelloFatturazioneCommand svuotaCarrelloFatturazioneCommand;
	private RimuoviCommand rimuoviCommand;
	private EseguiFatturazioneCommand eseguiFatturazioneCommand;
	private EseguiPreFatturazioneCommand eseguiPreFatturazioneCommand;

	private static final Icon ICON_DOCUMENTI_NON_VALIDI;
	private static final Icon ICON_DOCUMENTI_VALIDI;

	static {
		ICON_DOCUMENTI_NON_VALIDI = RcpSupport.getIcon("labelValidDocumenti.notValid");
		ICON_DOCUMENTI_VALIDI = RcpSupport.getIcon("labelValidDocumenti.valid");
	}

	/**
	 * Costruttore.
	 *
	 */
	protected CarrelloFatturazioneTablePage() {
		super(PAGE_ID, new CarrelloFatturazioneTableModel());
		setShowTitlePane(false);
		getTable().getTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				// abilito o disabilito il comando rimuovi
				getRimuoviCommand().setEnabled(
						getTable().getSelectedObject() != null && getTable().getSelectedObjects().size() == 1);
			}
		});
	}

	/**
	 * Aggiunge nel carrello le aree indicate.
	 *
	 * @param aree
	 *            aree da aggiungere
	 */
	public void aggiungiAreeMagazzino(Collection<AreaMagazzinoLitePM> aree) {

		if (aree == null || aree.isEmpty()) {
			return;
		}

		try {
			// prendo come riferimento il codice valuta della prima area
			String codiceValutaAree = aree.iterator().next().getAreaMagazzinoLite().getDocumento().getTotale()
					.getCodiceValuta();

			if (getCodiceValuta() != null && !getCodiceValuta().equals(codiceValutaAree)) {
				String title = RcpSupport.getMessage("valutaDocumentoNonCoerente.ask.title");
				String message = RcpSupport.getMessage("valutaDocumentoNonCoerente.ask.message");
				MessageDialog alert = new MessageDialog(title, message);
				alert.showDialog();
				return;
			}

			for (AreaMagazzinoLitePM areaMagazzinoLitePM : aree) {
				risultatiRicercaFatturazioneModel.aggiungiAreaSelezionata(areaMagazzinoLitePM);
				getTable().addRowObject(areaMagazzinoLitePM, null);
				// set selection
				getTable().selectRow(0, null);
			}
		} catch (Exception e1) {
			logger.error("--> exc nell'import del Transferable ", e1);
		}
	}

	/**
	 * Restituisce il codice valuta dei movimenti contenuti nel carrello. <code>null</code> se il carrello è vuoto.
	 *
	 * @return codice valuta
	 */
	public String getCodiceValuta() {

		if (getTable().getRows().isEmpty()) {
			return null;
		} else {
			return getTable().getRows().get(0).getAreaMagazzinoLite().getDocumento().getTotale().getCodiceValuta();
		}
	}

	@Override
	public AbstractCommand[] getCommands() {
		return new AbstractCommand[] { getEseguiPreFatturazioneCommand(), getEseguiFatturazioneCommand(),
				getRimuoviCommand(), getSvuotaCarrelloFatturazioneCommand() };
	}

	/**
	 *
	 * @return eseguiFatturazioneCommand
	 */
	private EseguiFatturazioneCommand getEseguiFatturazioneCommand() {
		if (eseguiFatturazioneCommand == null) {
			eseguiFatturazioneCommand = new EseguiFatturazioneCommand();
			eseguiFatturazioneCommand.setEnabled(false);
		}

		return eseguiFatturazioneCommand;
	}

	/**
	 *
	 * @return eseguiPreFatturazioneCommand
	 */
	private EseguiPreFatturazioneCommand getEseguiPreFatturazioneCommand() {
		if (eseguiPreFatturazioneCommand == null) {
			eseguiPreFatturazioneCommand = new EseguiPreFatturazioneCommand();
			eseguiPreFatturazioneCommand.setEnabled(false);
		}

		return eseguiPreFatturazioneCommand;
	}

	@Override
	public JComponent getHeaderControl() {
		parametriCreazioneDocumentoFatturazioneForm = new ParametriCreazioneDocumentoFatturazioneForm();

		parametriCreazioneDocumentoFatturazioneForm.getFormModel().getValueModel("dataDocumento")
				.addValueChangeListener(new DataDocumentoChangeListener());

		JComponent headerDatiDocumento = parametriCreazioneDocumentoFatturazioneForm.getControl();

		JPanel panel = getComponentFactory().createPanel(new FlowLayout(FlowLayout.LEFT));
		panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		panel.add(headerDatiDocumento);
		labelValidDocumenti.setIcon(ICON_DOCUMENTI_NON_VALIDI);
		panel.add(labelValidDocumenti);

		return panel;
	}

	/**
	 * @return rimuoviCommand
	 */
	public RimuoviCommand getRimuoviCommand() {
		if (rimuoviCommand == null) {
			rimuoviCommand = new RimuoviCommand(PAGE_ID);
		}

		return rimuoviCommand;
	}

	/**
	 * @return svuotaCarrelloFatturazioneCommand
	 */
	private SvuotaCarrelloFatturazioneCommand getSvuotaCarrelloFatturazioneCommand() {
		if (svuotaCarrelloFatturazioneCommand == null) {
			svuotaCarrelloFatturazioneCommand = new SvuotaCarrelloFatturazioneCommand();
		}
		return svuotaCarrelloFatturazioneCommand;
	}

	@Override
	public List<AreaMagazzinoLitePM> loadTableData() {
		return new ArrayList<AreaMagazzinoLitePM>();
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public List<AreaMagazzinoLitePM> refreshTableData() {
		return null;
	}

	/**
	 * Gestisce l'eccezione {@link RigaArticoloNonValidaException}.
	 *
	 * @param e
	 *            eccazione
	 */
	private void rigaArticoloNonValidaExceptionHandler(RigaArticoloNonValidaException e) {
		// Dopo aver messo i documenti nel carrello sono state modificate
		// delle righe con valori non validi.
		// Non posso quindi fatturare, rimetto i documenti nella ricerca e
		// visualizzo le righe non
		// valide
		Message messaggio = new DefaultMessage(
				"Sono presenti delle righe non valide.\nIl contenuto del carrello verrà riportato nei risultati della ricerca per rivalidare le righe.",
				Severity.ERROR);
		MessageDialog dialog = new MessageDialog("Attenzione", messaggio);
		dialog.showDialog();
		// Creo i parametri ricerca.
		ParametriRicercaFatturazione parametri = new ParametriRicercaFatturazione();
		parametri.setEffettuaRicerca(true);
		for (AreaMagazzinoLitePM area : getTable().getRows()) {
			parametri.addToAreeMagazzino(area.getAreaMagazzinoLite());
		}
		CarrelloFatturazioneTablePage.this.firePropertyChange(PROPERTY_DOCUMENTO_FATTURAZIONE_GENERATO, false,
				parametri);
		getSvuotaCarrelloFatturazioneCommand().execute();
	}

	/**
	 * Gestisce l'eccezione {@link SedeNonAppartieneAdEntitaException}.
	 *
	 * @param ex
	 *            exeption
	 */
	private void sedeNonAppartieneAdEntitaExceptionHandler(SedeNonAppartieneAdEntitaException ex) {
		SedeNonAppartieneAdEntitaExceptionDialog dialog = new SedeNonAppartieneAdEntitaExceptionDialog(ex);
		dialog.showDialog();
	}

	/**
	 * Gestisce l'eccezione {@link SedePerRifatturazioneAssenteException}.
	 *
	 * @param e
	 *            eccezione
	 */
	private void sedePerRifatturazioneAssenteExceptionHandler(SedePerRifatturazioneAssenteException e,
			ApplicationWindowAwareCommand command) {
		SedePerRifatturazioneExceptionDialog sedePerRifatturazioneDialog = new SedePerRifatturazioneExceptionDialog(e);
		sedePerRifatturazioneDialog.showDialog();
		if (sedePerRifatturazioneDialog.isConfirm()) {

			List<SedeEntita> listSediEntita = new ArrayList<SedeEntita>();
			for (AreaMagazzino areaMagazzino : e.getAreeMagazzinoSenzaSedeRifatturazione()) {
				listSediEntita.add(areaMagazzino.getDocumento().getSedeEntita());
			}

			// associo alle sedi dei movimenti la sede per rifatturazione
			magazzinoAnagraficaBD.associaSediASedePerRifatturazione(listSediEntita, e.getSedePerRifatturazione());

			// lancio di nuovo la generazione della fatturazione
			command.execute();
		}
		sedePerRifatturazioneDialog = null;
	}

	@Override
	public void setFormObject(Object object) {
	}

	/**
	 * @param magazzinoAnagraficaBD
	 *            the magazzinoAnagraficaBD to set
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}

	/**
	 *
	 * @param magazzinoDocumentoBD
	 *            the magazzinoDocumentoBD to set
	 */
	public void setMagazzinoDocumentoBD(IMagazzinoDocumentoBD magazzinoDocumentoBD) {
		this.magazzinoDocumentoBD = magazzinoDocumentoBD;
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);

		parametriCreazioneDocumentoFatturazioneForm.getFormModel().setReadOnly(readOnly);

		getEseguiFatturazioneCommand().setEnabled(!readOnly);
		getEseguiPreFatturazioneCommand().setEnabled(!readOnly);
		if (!readOnly) {
			// in questo modo l'enable del pulsante viene settato correttamente
			// dal listener presente sulla proprietà dataDocumento
			Date date = (Date) parametriCreazioneDocumentoFatturazioneForm.getFormModel()
					.getValueModel("dataDocumento").getValue();
			parametriCreazioneDocumentoFatturazioneForm.getFormModel().getValueModel("dataDocumento").setValue(null);
			parametriCreazioneDocumentoFatturazioneForm.getFormModel().getValueModel("dataDocumento").setValue(date);
		}
	}

	/**
	 * @param risultatiRicercaFatturazioneModel
	 *            the risultatiRicercaFatturazioneModel to set
	 */
	public void setRisultatiRicercaFatturazioneModel(RisultatiRicercaFatturazioneModel risultatiRicercaFatturazioneModel) {
		this.risultatiRicercaFatturazioneModel = risultatiRicercaFatturazioneModel;
		this.risultatiRicercaFatturazioneModel.addPropertyChangeListener(new ListChangeListener());
	}

	@Override
	public void update(Observable observable, Object obj) {
		super.update(observable, obj);
	}

	/**
	 * Abilita o disablita lo stato enabled di EseguiFatturazioneCommand a seconda della data e dei dati contenuti nel
	 * carrello.
	 *
	 * @param date
	 *            la data per cui validare i documenti aggiunti nel carrello
	 */
	private void updateEnabledGeneraFatturazioneCommand(Date date) {
		((CarrelloFatturazioneTableModel) TableModelWrapperUtils.getActualTableModel(getTable().getTable().getModel(),
				CarrelloFatturazioneTableModel.class)).setDataRiferimento(date);
		CarrelloFatturazioneTablePage.this.getTable().getTable().repaint();

		boolean isValid = risultatiRicercaFatturazioneModel.isAreeSelezionateValid(date);

		getEseguiFatturazioneCommand().setEnabled(isValid);
		getEseguiPreFatturazioneCommand().setEnabled(isValid);

		if (isValid) {
			labelValidDocumenti.setIcon(ICON_DOCUMENTI_VALIDI);
		} else {
			labelValidDocumenti.setIcon(ICON_DOCUMENTI_NON_VALIDI);
		}
	}

}
