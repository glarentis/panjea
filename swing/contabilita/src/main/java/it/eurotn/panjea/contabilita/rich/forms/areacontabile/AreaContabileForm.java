package it.eurotn.panjea.contabilita.rich.forms.areacontabile;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.anagrafica.rich.search.ContrattoSpesometroSearchObject;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.anagrafica.rich.search.SedeEntitaSearchObject;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile.GestioneIva;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaBD;
import it.eurotn.panjea.contabilita.util.AreaContabileFullDTO;
import it.eurotn.panjea.partite.domain.TipoAreaPartita;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.rich.focuspolicy.PanjeaFocusTraversalPolicy;
import it.eurotn.rich.binding.ImportoBinding;
import it.eurotn.rich.binding.codice.CodiceBinder.CodicePanel;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.components.ImportoTextField;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.awt.Color;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.log4j.Logger;
import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.rules.constraint.Constraint;
import org.springframework.rules.constraint.EqualTo;
import org.springframework.rules.constraint.Not;
import org.springframework.rules.constraint.property.PropertyConstraint;
import org.springframework.rules.constraint.property.PropertyValueConstraint;

import com.jgoodies.forms.layout.FormLayout;

/**
 * Form dell'area contabile.
 *
 * @author Leonardo
 */
public class AreaContabileForm extends PanjeaAbstractForm {

	public class AnnoMovimentiChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			Integer annoCompetenzaAzienda = aziendaCorrente.getAnnoContabile();
			Integer annoCompetenza = (Integer) evt.getNewValue();
			if (ObjectUtils.equals(annoCompetenza, annoCompetenzaAzienda)) {
				fieldAnnoCompetenza.setBackground(UIManager.getDefaults().getColor("TextField.background"));
			} else {
				fieldAnnoCompetenza.setBackground(new Color(255, 50, 50));
			}
		}

	}

	/**
	 * Property change legato alla proprieta' data registrazione se viene trovato il valore della proprieta'
	 * tipoAreaContabile allora aggiorno se previsto la data documento.
	 */
	private class DataRegistrazioneChangeListener implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			updateDataDocumento();
		}
	}

	public class DocumentoChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			AreaContabileFullDTO areaContabileFullDTO = (AreaContabileFullDTO) getFormObject();
			getFormModel().getFieldMetadata("areaContabile.codice").setEnabled(
					!areaContabileFullDTO.getAreaContabile().getTipoAreaContabile().isProtocolloLikeNumDoc());
		}

	}

	private class ReadOnlyPropertyChange implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (getFormModel().getValueModel("areaContabile.id").getValue() != null) {
				getFormModel().getFieldMetadata("areaContabile.tipoAreaContabile").setReadOnly(true);
			} else {
				getFormModel().getFieldMetadata("areaContabile.tipoAreaContabile").setReadOnly(false);
			}
		}
	}

	/**
	 * Listener che controlla le variazioni applicate a {@link TipoAreaContabile} e alla bisogna disabilita i controlli
	 * per la selezione dell'entita' e per la parte iva; viene chiamato da AreaContabile page nel metodo onNew() per
	 * inizializzare lo stato del form senza nessun tipo documento selezionato e comunque per allineare correttamente lo
	 * stato quando viene eseguito il newCommand.
	 *
	 * @author adriano
	 * @version 1.0, 13/giu/07
	 */
	private class TipoAreaContabilePropertyChange implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			logger.debug("--> TipoDocumentoPropertyChange property " + evt.getPropertyName() + " value "
					+ evt.getNewValue());

			TipoAreaContabile tipoAreaContabile = (TipoAreaContabile) evt.getNewValue();

			if (tipoAreaContabile != null) {

				AreaContabileFullDTO areaContabileFullDTO = (AreaContabileFullDTO) getFormObject();
				getFormModel().getFieldMetadata("areaContabile.codice").setEnabled(
						!areaContabileFullDTO.getAreaContabile().getTipoAreaContabile().isProtocolloLikeNumDoc());

				if (!getFormModel().isReadOnly()) {
					getFormModel().getValueModel("areaContabile.documento.entita").setValue(null);
				}

				// verifica per tipoAreaContabile l'assegnazione di registroIva e numeroRegistroIva (assente) nel
				// caso in cui risultino abilitati sono da verificare ulteriormente con il valore del registro
				// protocollo, riabilitare i componenti solo se il tipoAreaContabile non e' legato ad un registro
				if (!isGestioneIvaDisabled(tipoAreaContabile)) {
					AreaContabileForm.this.visibleComponents(componentsNumeroProtocollo, true);
					AreaContabileForm.this.visibleComponents(componentsNumeroProtocolloCollegato, !tipoAreaContabile
							.getGestioneIva().equals(GestioneIva.NORMALE));
				}
				labelContratto.setVisible(tipoAreaContabile.getTipoDocumento().isGestioneContratto());
				searchPanelContratto.setVisible(tipoAreaContabile.getTipoDocumento().isGestioneContratto());

				if ((tipoAreaContabile.getTipoDocumento().getTipoEntita() != null)) {
					if ((tipoAreaContabile.getTipoDocumento().getTipoEntita().equals(TipoEntita.CLIENTE))
							|| (tipoAreaContabile.getTipoDocumento().getTipoEntita().equals(TipoEntita.FORNITORE))) {
						searchPanelEntita.setVisible(true);
						searchPanelSede.setVisible(true);
						labelSedeEntita.setVisible(true);
						searchPanelBanca.setVisible(false);
						getFormModel().getFieldFace("areaContabile.documento.entita").configure(labelEntita);
						setEntitaPrefefinitaSePresente(tipoAreaContabile);
					} else if (tipoAreaContabile.getTipoDocumento().getTipoEntita().equals(TipoEntita.BANCA)) {
						searchPanelEntita.setVisible(false);
						searchPanelSede.setVisible(false);
						labelSedeEntita.setVisible(false);
						searchPanelBanca.setVisible(true);
						getFormModel().getFieldFace("areaContabile.documento.rapportoBancarioAzienda").configure(
								labelEntita);
					} else {
						hideFields();
					}
				} else {
					hideFields();
				}

				updateDataDocumento();
				// controllo della presenza dell'esistenza di TipoAreaPartita per il TipoDocumento corrente per
				// abilitarne i controlli
				controlloPresenzaAreaRate(tipoAreaContabile.getTipoDocumento());
			}
			// HACK richiamo la validate del form model per aggirare un problema sul codice binding o sul formmodel (?)
			// che non aggiorna le validation rules in accordo con il tipo documento scelto (protocollo obbligatorio)
			getFormModel().validate();
			// panjeaFocusTraversalPolicy.focusToFirstComponent();
		}
	}

	private static Logger logger = Logger.getLogger(AreaContabileForm.class);

	private static final String FORM_ID = "areaContabileForm";
	private JLabel labelEntita = null;

	private JComponent[] componentsNumeroProtocollo = null;
	private JComponent[] componentsNumeroProtocolloCollegato = null;
	private AziendaCorrente aziendaCorrente = null;
	private IContabilitaBD contabilitaBD = null;
	private JComponent[] componentsCodicePagamento = null;
	private TipoAreaContabilePropertyChange tipoAreaContabilePropertyChange = null;
	private DataRegistrazioneChangeListener dataRegistrazioneChangeListener = null;
	private ReadOnlyPropertyChange readOnlyPropertyChange = null;
	private PropertyChangeListener annoMovimentoChangeListener = null;
	// private JComponent[] componentsSpeseIncasso = null;
	private PanjeaFocusTraversalPolicy panjeaFocusTraversalPolicy;

	private SearchPanel searchPanelBanca;
	private SearchPanel searchPanelEntita;
	private SearchPanel searchPanelSede;
	private JTextField fieldAnnoCompetenza;
	private SearchPanel searchPanelContratto;
	private JLabel labelContratto;
	private JLabel labelSedeEntita;

	private PropertyChangeListener documentoChangeListener;

	/**
	 * Costruttore AreaContabileForm con azienda corrente.
	 *
	 * @param aziendaCorrente
	 *            l'aziendaCorrente da cui recuperare le informazioni necessarie, codice valuta, anno, e altre info
	 *            dell'azienda.
	 */
	public AreaContabileForm(final AziendaCorrente aziendaCorrente) {
		super(PanjeaFormModelHelper.createFormModel(new AreaContabileFullDTO(), false, FORM_ID), FORM_ID);
		this.aziendaCorrente = aziendaCorrente;
		this.tipoAreaContabilePropertyChange = new TipoAreaContabilePropertyChange();
		this.dataRegistrazioneChangeListener = new DataRegistrazioneChangeListener();
		this.readOnlyPropertyChange = new ReadOnlyPropertyChange();
		this.annoMovimentoChangeListener = new AnnoMovimentiChangeListener();
		this.documentoChangeListener = new DocumentoChangeListener();

		// Aggiungo il value model che mi servirà solamente nella search text delle entità per cercare solo le entità
		// abilitate
		ValueModel entitaAbilitateInRicercaValueModel = new ValueHolder(Boolean.TRUE);
		DefaultFieldMetadata entitaAbilitateMetaData = new DefaultFieldMetadata(getFormModel(),
				new FormModelMediatingValueModel(entitaAbilitateInRicercaValueModel), Boolean.class, true, null);
		getFormModel().add("entitaAbilitateInRicerca", entitaAbilitateInRicercaValueModel, entitaAbilitateMetaData);
	}

	/**
	 * Verifica l'esistenza del {@link TipoAreaPartita} per il {@link TipoDocumento} corrente e rende
	 * visibili/invisibili i controlli <br>
	 * legati a {@link AreaRate}.
	 *
	 * @param tipoDocumento
	 *            il tipo documento da controllare per gestire i componenti del form
	 */
	private void controlloPresenzaAreaRate(TipoDocumento tipoDocumento) {
		logger.debug("--> Enter controlloPresenzaAreaRate");
		boolean visible = true;
		if (contabilitaBD != null) {
			// controllo lo stato dirty dato che all'apertura del form posso basarmi sul flag di areaContabileFullDTO
			// mentre invece quando cambio il tipo documento devo ricaricare dal business il tipo area partite per
			// visualizzare o nascondere i componenti
			if (getFormModel().isReadOnly()) {
				visible = ((AreaContabileFullDTO) getFormObject()).isAreaRateEnabled();
			} else {
				if (tipoDocumento.getId() != null) {
					TipoAreaPartita tipoAreaPartita = contabilitaBD
							.caricaTipoAreaPartitaPerTipoDocumento(tipoDocumento);
					// areaPartiteEnabled e' enabled se esiste tipoAreaPartita e se il suo tipo operazione e' uguale a
					// GENERA o NESSUNA
					visible = (tipoAreaPartita != null)
							&& (tipoAreaPartita.getId() != null)
							&& ((TipoAreaPartita.TipoOperazione.GENERA.equals(tipoAreaPartita.getTipoOperazione())) || (TipoAreaPartita.TipoOperazione.NESSUNA
									.equals(tipoAreaPartita.getTipoOperazione())));
					getFormModel().getValueModel("areaRateEnabled").setValue(visible);
				}
			}
		}
		setVisibleComponentsAreaPartita(visible);
		logger.debug("--> Exit controlloPresenzaAreaRate");
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"right:pref,4dlu,left:default, 10dlu, right:pref,4dlu,100dlu, 1dlu, right:50dlu,4dlu,left:120dlu , 10dlu, right:pref,4dlu,left:default",
				"4dlu,default, 3dlu,default, 3dlu,default, 3dlu,default, 3dlu,default, 3dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanelNumbered());
		builder.setLabelAttributes("r, c");

		builder.nextRow();
		builder.setRow(2);

		// -------: DATA REGISTRAZIONE
		builder.addPropertyAndLabel("areaContabile.dataRegistrazione", 1);

		// -------: TIPO AREA CONTABILE
		builder.addLabel("areaContabile.tipoAreaContabile", 5);
		Binding bindingTipoDoc = bf.createBoundSearchText("areaContabile.tipoAreaContabile", new String[] {
				"tipoDocumento.codice", "tipoDocumento.descrizione" });
		SearchPanel searchPanelTipoDocumento = (SearchPanel) builder.addBinding(bindingTipoDoc, 7, 2, 5, 1);
		searchPanelTipoDocumento.getTextFields().get("tipoDocumento.codice").setColumns(5);
		searchPanelTipoDocumento.getTextFields().get("tipoDocumento.descrizione").setColumns(18);
		builder.nextRow();

		// -------: DATA DOCUMENTO
		builder.addPropertyAndLabel("areaContabile.documento.dataDocumento", 1);

		// -------: NUMERO DOCUMENTO
		builder.addLabel("areaContabile.documento.codice", 5);
		Binding bindingCodice = bf.createBoundCodice("areaContabile.documento.codice",
				"areaContabile.tipoAreaContabile.tipoDocumento.registroProtocollo",
				"areaContabile.documento.valoreProtocollo",
				"areaContabile.tipoAreaContabile.tipoDocumento.patternNumeroDocumento", null);
		builder.addBinding(bindingCodice, 7);

		// -------: PROTOCOLLO
		PropertyConstraint propertyConstraintRigheIva = new PropertyValueConstraint(
				"areaContabile.tipoAreaContabile.tipoDocumento.righeIvaEnable", EqualTo.value(true));
		PropertyConstraint propertyConstraintLikeNumDoc = new PropertyValueConstraint(
				"areaContabile.tipoAreaContabile.protocolloLikeNumDoc", EqualTo.value(false));

		List<Constraint> constraintsProtocollo = new ArrayList<Constraint>();
		constraintsProtocollo.add(propertyConstraintRigheIva);
		constraintsProtocollo.add(propertyConstraintLikeNumDoc);
		JLabel labelProtocollo = builder.addLabel("areaContabile.codice", 9);
		CodicePanel protocolloPanel = (CodicePanel) builder.addBinding(bf.createBoundCodice("areaContabile.codice",
				"areaContabile.tipoAreaContabile.registroProtocollo", "areaContabile.valoreProtocollo",
				"areaContabile.tipoAreaContabile.patternNumeroProtocollo", constraintsProtocollo), 11);
		componentsNumeroProtocollo = new JComponent[] { labelProtocollo, protocolloPanel };

		// -------: PROTOCOLLO COLLEGATO
		PropertyConstraint propertyConstraintGestioneIva = new PropertyValueConstraint(
				"areaContabile.tipoAreaContabile.gestioneIva", new Not(EqualTo.value(GestioneIva.NORMALE)));
		List<Constraint> constraintsProtocolloCollegato = new ArrayList<Constraint>();
		constraintsProtocolloCollegato.add(propertyConstraintGestioneIva);

		JLabel labelProtocolloCollegato = builder.addLabel("areaContabile.codiceCollegato", 13);
		CodicePanel protocolloCollegatoPanel = (CodicePanel) builder.addBinding(bf.createBoundCodice(
				"areaContabile.codiceCollegato", "areaContabile.tipoAreaContabile.registroProtocolloCollegato",
				"areaContabile.valoreProtocolloCollegato", "areaContabile.tipoAreaContabile.patternNumeroProtocollo",
				constraintsProtocolloCollegato), 15);
		componentsNumeroProtocolloCollegato = new JComponent[] { labelProtocolloCollegato, protocolloCollegatoPanel };

		builder.nextRow();

		// -------: ENTITA' BANCA, SEDE
		Binding bindingEntita = bf.createBoundSearchText("areaContabile.documento.entita", new String[] { "codice",
		"anagrafica.denominazione" }, new String[] {
				"areaContabile.tipoAreaContabile.tipoDocumento.tipoEntita",
				"areaContabile.tipoAreaContabile.gestioneIvaOrdinal", "entitaAbilitateInRicerca" }, new String[] {
				EntitaByTipoSearchObject.TIPOENTITA_KEY, "gestioneIva",
				EntitaByTipoSearchObject.FILTRO_ENTITA_ABILITATO });
		searchPanelEntita = (SearchPanel) bindingEntita.getControl();
		searchPanelEntita.getTextFields().get("codice").setColumns(5);
		searchPanelEntita.getTextFields().get("anagrafica.denominazione").setColumns(18);

		Binding bindingBanca = bf.createBoundSearchText("areaContabile.documento.rapportoBancarioAzienda",
				new String[] { "numero", "descrizione" }, new String[] {}, new String[] {});
		searchPanelBanca = (SearchPanel) bindingBanca.getControl();
		searchPanelBanca.getTextFields().get("numero").setColumns(5);
		searchPanelBanca.getTextFields().get("descrizione").setColumns(18);

		labelEntita = getComponentFactory().createLabel("");
		builder.addComponent(labelEntita, 1);
		builder.addBinding(bindingEntita, 3, 6, 5, 1);
		builder.addBinding(bindingBanca, 3, 6, 5, 1);

		labelSedeEntita = builder.addLabel("areaContabile.documento.sedeEntita", 9);
		Binding sedeEntitaBinding = bf.createBoundSearchText("areaContabile.documento.sedeEntita", null,
				new String[] { "areaContabile.documento.entita" },
				new String[] { SedeEntitaSearchObject.PARAMETER_ENTITA_ID });
		searchPanelSede = (SearchPanel) builder.addBinding(sedeEntitaBinding, 11);
		// searchPanelSede.getTextFields().get(null).setColumns(18);
		builder.nextRow();

		// -------: CONTRATTO SPESOMETRO
		labelContratto = builder.addLabel("areaContabile.documento.contrattoSpesometro");
		builder.addBinding(getContrattiBinding(bf), 3, 8, 5, 1);
		builder.nextRow();

		// -------: TOT,CODICE PAGAMENTO
		builder.addLabel("areaContabile.documento.totale", 1);
		ImportoBinding importoBinding = (ImportoBinding) bf.createBoundImportoTassoCalcolatoTextField(
				"areaContabile.documento.totale", "areaContabile.documento.dataDocumento");
		ImportoTextField importoTextField = (ImportoTextField) builder.addBinding(importoBinding, 3);
		importoTextField.setColumns(10);

		Binding bindingCodPag = bf.createBoundSearchText("areaRate.codicePagamento", new String[] { "codicePagamento",
		"descrizione" });
		SearchPanel searchPanelCodicePagamento = (SearchPanel) bindingCodPag.getControl();
		searchPanelCodicePagamento.getTextFields().get("codicePagamento").setColumns(8);
		searchPanelCodicePagamento.getTextFields().get("descrizione").setColumns(18);

		JLabel labelCodPag = builder.addLabel("areaRate.codicePagamento", 5);
		JComponent compCodPag = builder.addBinding(bindingCodPag, 7, 10, 5, 1);
		componentsCodicePagamento = new JComponent[] { labelCodPag, compCodPag };

		// ------- COL 3 e 4: SPESE INCASSO
		// componentsSpeseIncasso = builder.add("areaRate.speseIncasso",
		// "align=left");
		// JTextField textFieldSpeseIncasso = (JTextField)
		// componentsSpeseIncasso[1];
		// textFieldSpeseIncasso.setColumns(16);
		builder.nextRow();

		// -------: NOTE
		JTextField note = (JTextField) builder.addPropertyAndLabel("areaContabile.note", 1, 12, 9, 1)[1];
		note.setColumns(10);

		// -------: ANNO COMPETENZA
		fieldAnnoCompetenza = (JTextField) builder.addPropertyAndLabel("areaContabile.annoMovimento", 13)[1];
		fieldAnnoCompetenza.setColumns(5);

		// HACK esegue getValue dei due attributi per evitare una ConcurrentModificationException all'interno di
		// AttributiRigaArticoloPropertyChange anche nel caso di della validazione
		getFormModel().getValueModel("areaContabile.tipoAreaContabile.protocolloLikeNumDoc").getValue();
		getFormModel().getValueModel("areaRateEnabled").getValue();

		registerPropertyChange();

		// lista di componenti che voglio saltare nella normale policy di ciclo del focus
		List<Component> componentsToSkip = new ArrayList<Component>();
		componentsToSkip.add(fieldAnnoCompetenza);

		panjeaFocusTraversalPolicy = new PanjeaFocusTraversalPolicy(getActiveWindow().getControl()
				.getFocusTraversalPolicy(), componentsToSkip);

		getFormModel().getValueModel("areaContabile.id").getValue();

		// questi property change non devo toglierli quando faccio la set formobject altrimenti non verranno eseguiti i
		// cambiamenti necessari al cambio di oggetto o nuovo
		addFormValueChangeListener("areaContabile.tipoAreaContabile", tipoAreaContabilePropertyChange);
		getFormModel().addPropertyChangeListener(readOnlyPropertyChange);

		hideFields();

		return builder.getPanel();
	}

	@Override
	protected Object createNewObject() {
		AreaContabileFullDTO areaContabileFullDTOOld = (AreaContabileFullDTO) getFormObject();
		AreaContabileFullDTO areaContabileFullDTO = new AreaContabileFullDTO();

		// se ho data documento uguale a data registrazione non lo faccio qui,
		// altrimenti mi ritrovo l'oggetto finale
		// già pronto (committable senza errori) e nel caso non abbia altri dati
		// da modificare non posso salvarlo; nella
		// page dell'areaMagazzino quando preparo un nuovo oggetto reimposto il
		// TipoAreaMagazzino
		// attivando così il propertyChange che imposta tutti i valori di
		// default per il tam scelto
		areaContabileFullDTO.getAreaContabile().setDataRegistrazione(
				areaContabileFullDTOOld.getAreaContabile().getDataRegistrazione());
		areaContabileFullDTO.getAreaContabile().setAnnoMovimento(aziendaCorrente.getAnnoContabile());

		// HACK inizializzazione di codiceValuta
		areaContabileFullDTO.getAreaContabile().getDocumento().getTotale()
		.setCodiceValuta(aziendaCorrente.getCodiceValuta());
		return areaContabileFullDTO;
	}

	/**
	 * Nasconde i controlli per la selezione dell'entita' e banca.
	 */
	private void disableEntita() {
		searchPanelEntita.setVisible(false);
		searchPanelSede.setVisible(false);
		labelSedeEntita.setVisible(false);
		getValueModel("areaContabile.documento.entita").setValue(null);
		searchPanelBanca.setVisible(false);
		getValueModel("areaContabile.documento.rapportoBancarioAzienda").setValue(null);
		labelEntita.setText("");
	}

	@Override
	public void dispose() {
		unregisterPropertyChange();
		removeFormValueChangeListener("areaContabile.tipoAreaContabile", tipoAreaContabilePropertyChange);
		getFormModel().removePropertyChangeListener(readOnlyPropertyChange);
		super.dispose();
	}

	/**
	 *
	 * @return focus policy per la testata dell'area contabile
	 */
	public PanjeaFocusTraversalPolicy getAreaContabileFocusTraversalPolicy() {
		return panjeaFocusTraversalPolicy;
	}

	/**
	 * Crea e restituisce il SearchTextBinding per l' entita aggiungendo il pulsante per la richiesta della situazione
	 * rate.
	 *
	 * @param bf
	 *            il binding factory
	 * @return Binding
	 */
	private Binding getContrattiBinding(PanjeaSwingBindingFactory bf) {
		Binding bindingContratto = bf.createBoundSearchText("areaContabile.documento.contrattoSpesometro",
				new String[] { "codice" }, new String[] { "areaContabile.documento.entita" },
				new String[] { ContrattoSpesometroSearchObject.ENTITA_KEY });
		searchPanelContratto = (SearchPanel) bindingContratto.getControl();
		return bindingContratto;
	}

	/**
	 *
	 * @return propertyChange per il cambio del tipoAreacontabile
	 */
	public PropertyChangeListener getTipoAreaContabilePropertyChange() {
		return tipoAreaContabilePropertyChange;
	}

	/**
	 * Nasconde tutti i controlli dei campi dell'area contabile che non sono strettamente necessari.<br>
	 * utile per nascondere i controlli nel caso di nuovo documento.
	 */
	public void hideFields() {
		AreaContabileForm.this.visibleComponents(componentsNumeroProtocollo, false);
		AreaContabileForm.this.visibleComponents(componentsNumeroProtocolloCollegato, false);
		AreaContabileForm.this.disableEntita();
		labelContratto.setVisible(false);
		searchPanelContratto.setVisible(false);
		setVisibleComponentsAreaPartita(false);

		// rivalido il form dopo aver nascosto i componenti per
		getFormModel().validate();
	}

	/**
	 * @param tac
	 *            tipoAreaContabile
	 * @return se non c'e' un registro iva selezionato torna true, false altrimenti.
	 */
	private boolean isGestioneIvaDisabled(TipoAreaContabile tac) {
		return !tac.getTipoDocumento().isRigheIvaEnable();
	}

	/**
	 * registra i property change.
	 */
	public void registerPropertyChange() {
		logger.debug("-->aggiungo i listener");
		addFormValueChangeListener("areaContabile.dataRegistrazione", dataRegistrazioneChangeListener);
		addFormValueChangeListener("areaContabile.annoMovimento", annoMovimentoChangeListener);
		addFormValueChangeListener("areaContabile.documento", documentoChangeListener);
		logger.debug("-->aggiunti i listener");
	}

	/**
	 * @param contabilitaBD
	 *            The contabilitaBD to set.
	 */
	public void setContabilitaBD(IContabilitaBD contabilitaBD) {
		this.contabilitaBD = contabilitaBD;
	}

	/**
	 *
	 * @param tipoAreaContabile
	 *            tipoAreaContabile su cui impostare entità predefinita
	 */
	private void setEntitaPrefefinitaSePresente(TipoAreaContabile tipoAreaContabile) {
		if (getFormModel().isReadOnly() || tipoAreaContabile.getEntitaPredefinita() == null) {
			return;
		}

		getFormModel().getValueModel("areaContabile.documento.entita").setValue(
				tipoAreaContabile.getEntitaPredefinita());
	}

	/**
	 * Rende visibili/invisibili i components bindati alle proprietà di {@link AreaRate}.
	 *
	 * @param visible
	 *            visibilità dei componenti
	 */
	private void setVisibleComponentsAreaPartita(boolean visible) {
		logger.debug("--> Enter setVisibleComponentsAreaPartita");
		for (JComponent component : componentsCodicePagamento) {
			component.setVisible(visible);
		}
		logger.debug("--> Exit setVisibleComponentsAreaPartita");
	}

	/**
	 * deregistra i property change.
	 */
	public void unregisterPropertyChange() {
		logger.debug("-->aggiungo i listener");
		removeFormValueChangeListener("areaContabile.dataRegistrazione", dataRegistrazioneChangeListener);
		removeFormValueChangeListener("areaContabile.annoMovimento", annoMovimentoChangeListener);
		logger.debug("-->aggiunti i listener");
	}

	/**
	 * setto la data documento uguale alla data registrazione inserita.
	 */
	private void updateDataDocumento() {
		if (!getFormModel().isReadOnly()) {
			Date dataReg = (Date) getValue("areaContabile.dataRegistrazione");
			TipoAreaContabile tipoAreaContabile = (TipoAreaContabile) getValue("areaContabile.tipoAreaContabile");
			if (tipoAreaContabile != null && tipoAreaContabile.getId() != null
					&& tipoAreaContabile.isDataDocLikeDataReg()) {
				// nel caso in cui non sia già valorizzata, se e' abilitata la proprieta' dataDocLikeDataReg riporto il
				// valore di dataRegistrazione in data documento
				getValueModel("areaContabile.documento.dataDocumento").setValue(dataReg);
			}
		}
	}

	/**
	 * Metodo ricorsivo che ab/disabilita l'array di components passato.
	 *
	 * @param components
	 *            i componenti su cui agire
	 * @param enabled
	 *            attivare o disattivare lo stato scelto nel method
	 */
	private void visibleComponents(Component[] components, boolean enabled) {
		for (Component component : components) {
			if (component instanceof JPanel) {
				JPanel panel = (JPanel) component;
				visibleComponents(panel.getComponents(), enabled);
			} else {
				component.setVisible(enabled);
			}
		}
	}

}
