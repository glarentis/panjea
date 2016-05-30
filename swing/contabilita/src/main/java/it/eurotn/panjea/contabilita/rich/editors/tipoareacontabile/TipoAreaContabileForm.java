/**
 *
 */
package it.eurotn.panjea.contabilita.rich.editors.tipoareacontabile;

import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;
import org.springframework.binding.form.FormModel;
import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.settings.SettingsException;
import org.springframework.richclient.settings.SettingsManager;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.codice.generator.interfaces.VariabiliCodiceManager;
import it.eurotn.panjea.anagrafica.rich.bd.AnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.contabilita.domain.RegistroIva;
import it.eurotn.panjea.contabilita.domain.RegistroIva.TipoRegistro;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile.GestioneIva;
import it.eurotn.panjea.protocolli.domain.Protocollo;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

/**
 * Property change per rendere visibili o meno tutti i controlli dell'area iva se abilitata.
 */
public class TipoAreaContabileForm extends PanjeaAbstractForm {

    private class GestioneAreaIvaListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (!getFormModel().isEnabled()) {
                return;
            }
            if (TipoAreaContabileForm.LOGGER.isDebugEnabled()) {
                TipoAreaContabileForm.LOGGER
                        .debug("--> gestioneAreaIva value change " + evt.getPropertyName() + ": " + evt.getNewValue());
            }
            if (FormModel.ENABLED_PROPERTY.equals(evt.getPropertyName())) {
                boolean visible = ((Boolean) evt.getNewValue()).booleanValue();
                for (JComponent component : componentsAreaIva) {
                    component.setVisible(visible);
                }
                // devo riaggiornare lo stato visibile dei componenti per
                // gestione iva intra e art 17 visto che appena
                // prima ho nascosto / visualizzato tutti i componenti per
                // l'area iva
                GestioneIva gestioneIva = (GestioneIva) getValue("gestioneIva");
                boolean visibleIntraArt17 = (gestioneIva != null && gestioneIva != GestioneIva.NORMALE) && visible;
                for (JComponent component : componentsGestioneIvaIntraArt17) {
                    component.setVisible(visibleIntraArt17);
                }
            }
            LOGGER.debug("--> Exit GestioneAreaIvaListener propertyChange");
        }
    }

    /**
     * PropertyChange per rendere visibili/invisibili i controlli per i dati riguardanti gli incassi pagamenti.
     */
    private class GestioneIncassoPagamentoListeners implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (!getFormModel().isEnabled()) {
                return;
            }
            if (TipoAreaContabileForm.LOGGER.isDebugEnabled()) {
                TipoAreaContabileForm.LOGGER
                        .debug("--> Enter GestioneIncassoPagamento.propertyChange " + evt.getNewValue());
            }
            if (FormModel.ENABLED_PROPERTY.equals(evt.getPropertyName())) {
                boolean visible = ((Boolean) evt.getNewValue()).booleanValue();
                updateDatiIncassoPagamentoVisibility(visible);
            }
            TipoAreaContabileForm.LOGGER.debug("--> Exit GestioneIncassoPagamento.propertyChange");
        }

    }

    /**
     * Property change associato al valore di gestione iva, se INTRA o ART17 devo visualizzare registro iva e protocollo
     * collegati, inoltre devo filtrare registro solo per tipoRegistro ACQUISTO e registro iva collegato per
     * tipoRegistro VENDITA. Per gestione iva NORMALE invece i parametri collegati non sono visibili e non c'e' filtro
     * sul campo registro iva.
     *
     * @author Leonardo
     */
    private class GestioneIvaChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (!getFormModel().isEnabled()) {
                return;
            }
            GestioneIva gestioneIva = (GestioneIva) evt.getNewValue();
            boolean visible = gestioneIva != null && gestioneIva != GestioneIva.NORMALE;
            for (JComponent component : componentsGestioneIvaIntraArt17) {
                component.setVisible(visible);
            }

            if (!visible) {
                getFormModel().getValueModel("registroIvaCollegato").setValue(null);
            } else {
                // Controllo che l'eventuale registro iva settato sia di
                // acquisto
                RegistroIva registroIva = (RegistroIva) getFormModel().getValueModel("registroIva").getValue();
                if (registroIva != null && registroIva.getTipoRegistro() != TipoRegistro.ACQUISTO) {
                    getFormModel().getValueModel("registroIva").setValue(new RegistroIva());
                }
            }
        }
    }

    /**
     * Form value changed che verifica il registro iva e a seconda del valore modifica la tipologia corrispettivo
     * legata, modifica inoltre la visibilita' del componente tipologia corrispettivo.
     *
     * @author Leonardo
     */
    private class RegistroIvaChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            RegistroIva registroIva = (RegistroIva) evt.getNewValue();
            updateTipologiaCorrispettivoVisibility(registroIva);
        }
    }

    private static final Logger LOGGER = Logger.getLogger(TipoAreaContabileForm.class);

    public static final String COMBO_BOX_VALUE_MANUALE = "protocollo.manuale.label";
    public static final String SEPARATOR_AREA_IVA = "area.iva.label";
    public static final String SEPARATOR_AREA_SCADENZA = "area.scadenze.label";
    public static final String SEPARATOR_AREA_CONTABILE = "area.contabile.label";
    public static final String SEPARATOR_AREA_CONTABILE_DA_INCASSO_PAGAMENTI = "area.contabileDaIncassoPagamenti.label";
    private static final String FORM_ID = "tipoAreaContabileForm";
    private SettingsManager settingsManager = RcpSupport.getBean("settingManagerLocal");

    private List<JComponent> componentsAreaIva = null;

    private List<JComponent> componentsGestioneIvaIntraArt17 = null;
    private List<JComponent> componentsIncassiPagamenti = null;
    private JComponent separatorAreaIva = null;

    private JComponent[] comboTipologiaCorrispettivo = null;

    private IAnagraficaBD anagraficaBD;

    /**
     * Costruttore.
     *
     * @param tipoAreaContabile
     *            tipo area contabile da gestire
     */
    public TipoAreaContabileForm(final TipoAreaContabile tipoAreaContabile) {
        super(PanjeaFormModelHelper.createFormModel(tipoAreaContabile, false, FORM_ID), FORM_ID);

        this.anagraficaBD = RcpSupport.getBean(AnagraficaBD.BEAN_ID);

        // Aggiungo il value model che mi servirà solamente nella search text
        // delle entità per cercare solo le entità abilitate
        ValueModel entitaAbilitateInRicercaValueModel = new ValueHolder(Boolean.TRUE);
        DefaultFieldMetadata entitaAbilitateMetaData = new DefaultFieldMetadata(getFormModel(),
                new FormModelMediatingValueModel(entitaAbilitateInRicercaValueModel), Boolean.class, true, null);
        getFormModel().add("entitaAbilitateInRicerca", entitaAbilitateInRicercaValueModel, entitaAbilitateMetaData);
    }

    /**
     * Aggiunge un {@link PropertyChangeListener} per ogni fieldmetadata modificato dal defaultController di questa
     * pagina.
     */
    private void addListeners() {
        getFormModel().getFieldMetadata("gestioneIva").addPropertyChangeListener(new GestioneAreaIvaListener());
        getFormModel().getValueModel("gestioneIva").addValueChangeListener(new GestioneIvaChangeListener());
        getFormModel().getValueModel("registroIva").addValueChangeListener(new RegistroIvaChangeListener());
        getFormModel().getFieldMetadata("contoCassa")
                .addPropertyChangeListener(new GestioneIncassoPagamentoListeners());

    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout(
                "right:pref,4dlu,left:default, 10dlu, right:pref,4dlu,left:default, 10dlu, right:pref, fill:default:grow",
                "3dlu,default,3dlu,default,3dlu,default,3dlu,default,3dlu,default,3dlu,default,3dlu,default,3dlu,default,3dlu,default,3dlu,14dlu,3dlu,default,3dlu,default,3dlu,default,3dlu,default,3dlu,default,3dlu,default, fill:default:grow");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel());
        builder.setLabelAttributes("r, c");
        componentsAreaIva = new ArrayList<JComponent>();
        componentsGestioneIvaIntraArt17 = new ArrayList<JComponent>();
        componentsIncassiPagamenti = new ArrayList<JComponent>();

        builder.nextRow();
        builder.setRow(2);

        JLabel tdcodlabel = builder.addLabel("tipoDocumento.codice", getComponentFactory().createLabel(""), 1, 2);
        tdcodlabel.setFont(new Font(tdcodlabel.getFont().getName(), Font.BOLD, tdcodlabel.getFont().getSize()));
        JTextField tdcod = (JTextField) builder.addNestedPropertyReadOnly("tipoDocumento", "codice", 3, 2);
        tdcod.setColumns(18);
        tdcod.setFont(new Font(tdcod.getFont().getName(), Font.BOLD, tdcod.getFont().getSize()));

        JLabel tddesclabel = builder.addLabel("tipoDocumento.descrizione", getComponentFactory().createLabel(""), 5, 2);
        tddesclabel.setFont(new Font(tddesclabel.getFont().getName(), Font.BOLD, tddesclabel.getFont().getSize()));
        JTextField tddesc = (JTextField) builder.addNestedPropertyReadOnly("tipoDocumento", "descrizione", 7, 2);
        tddesc.setColumns(18);
        tddesc.setFont(new Font(tddesc.getFont().getName(), Font.BOLD, tddesc.getFont().getSize()));
        builder.nextRow();

        builder.addHorizontalSeparator(getMessage(SEPARATOR_AREA_CONTABILE), 7);
        builder.nextRow();

        builder.addPropertyAndLabel("stampaGiornale", 1, 6);
        builder.addPropertyAndLabel("dataDocLikeDataReg", 5, 6);
        builder.nextRow();

        builder.addPropertyAndLabel("ritenutaAcconto", 1, 8);
        builder.addPropertyAndLabel("tipoRitenutaAcconto", 5, 8);
        builder.nextRow();

        // inizio creazione components area iva
        separatorAreaIva = getComponentFactory().createLabeledSeparator(getMessage(SEPARATOR_AREA_IVA));
        componentsAreaIva.add(separatorAreaIva);
        builder.addComponent(separatorAreaIva, 1, 10, 7, 1);
        builder.nextRow();

        // prima riga, gestione iva NORMALE,INTRA,ART17
        JComponent[] comboGestioneIva = builder.addPropertyAndLabel("gestioneIva", 1, 12);
        componentsAreaIva.add(comboGestioneIva[0]);
        componentsAreaIva.add(comboGestioneIva[1]);

        // prima riga, flag stampa registro iva
        JComponent[] checkBoxStampaRegistro = builder.addPropertyAndLabel("stampaRegistroIva", 5, 12);
        componentsAreaIva.add(checkBoxStampaRegistro[0]);
        componentsAreaIva.add(checkBoxStampaRegistro[1]);
        builder.nextRow();

        // seconda riga, scelta registro iva
        Binding bindingRegistroIva = bf.createBoundSearchText("registroIva", new String[] { "numero", "descrizione" },
                new String[] { "registroIva", "gestioneIva" }, new String[] { "registroIva", "gestioneIva" });
        JLabel labelRegistroIva = builder.addLabel("registroIva", 1, 14);
        SearchPanel searchPanelRegistroIva = (SearchPanel) builder.addBinding(bindingRegistroIva, 3, 14);
        searchPanelRegistroIva.getTextFields().get("numero").setColumns(4);
        searchPanelRegistroIva.getTextFields().get("descrizione").setColumns(10);
        componentsAreaIva.add(labelRegistroIva);
        componentsAreaIva.add(searchPanelRegistroIva);

        Binding bindingRegistroIvaCollegato = bf.createBoundSearchText("registroIvaCollegato",
                new String[] { "numero", "descrizione" }, new String[] { "registroIvaCollegato", "gestioneIva" },
                new String[] { "registroIvaCollegato", "gestioneIva" });
        JLabel labelRegistroIvaCollegato = builder.addLabel("registroIvaCollegato", 5, 14);
        SearchPanel searchRegistriIvaCollegato = (SearchPanel) builder.addBinding(bindingRegistroIvaCollegato, 7, 14);
        searchRegistriIvaCollegato.getTextFields().get("numero").setColumns(4);
        searchRegistriIvaCollegato.getTextFields().get("descrizione").setColumns(10);
        componentsAreaIva.add(labelRegistroIvaCollegato);
        componentsAreaIva.add(searchRegistriIvaCollegato);
        componentsGestioneIvaIntraArt17.add(labelRegistroIvaCollegato);
        componentsGestioneIvaIntraArt17.add(searchRegistriIvaCollegato);
        builder.nextRow();

        JComponent[] componentsProtocolloLikeNumDoc = builder.addPropertyAndLabel("protocolloLikeNumDoc", 1, 16);
        componentsAreaIva.add(componentsProtocolloLikeNumDoc[0]);
        componentsAreaIva.add(componentsProtocolloLikeNumDoc[1]);
        builder.nextRow();

        Binding protBinding = bf.createBoundSearchText("registroProtocollo", null, Protocollo.class);
        JLabel protocolloLabel = builder.addLabel("registroProtocollo", 1, 18);
        SearchPanel searchPanelProtocollo = (SearchPanel) builder.addBinding(protBinding, 3, 18);
        componentsAreaIva.add(protocolloLabel);
        componentsAreaIva.add(searchPanelProtocollo);

        Binding protCollegatoBinding = bf.createBoundSearchText("registroProtocolloCollegato", null, Protocollo.class);
        JLabel protocolloCollegatoLabel = builder.addLabel("registroProtocolloCollegato", 5, 18);
        SearchPanel searchPanelProtocolloCollegato = (SearchPanel) builder.addBinding(protCollegatoBinding, 7, 18);
        componentsAreaIva.add(protocolloCollegatoLabel);
        componentsAreaIva.add(searchPanelProtocolloCollegato);
        componentsGestioneIvaIntraArt17.add(protocolloCollegatoLabel);
        componentsGestioneIvaIntraArt17.add(searchPanelProtocolloCollegato);
        builder.nextRow();

        JLabel patternNumeroProtocolloLabel = builder.addLabel("patternNumeroProtocollo", 1, 20);
        String[] variabiliPatternCodiceDocumento = anagraficaBD.getVariabiliPatternCodiceDocumento();
        List<String> variabiliList = new ArrayList<String>();
        for (String variabile : variabiliPatternCodiceDocumento) {
            variabiliList.add(VariabiliCodiceManager.VAR_SEPARATOR + variabile + VariabiliCodiceManager.VAR_SEPARATOR);
        }
        ValueHolder valueHolder = new ValueHolder(variabiliList);
        builder.setComponentAttributes("f,f");
        JComponent patternNumeroProtocolloComponent = builder.addBinding(
                bf.createBoundCodeEditor("patternNumeroProtocollo", valueHolder, true, true, false), 3, 20, 3, 1);
        builder.setComponentAttributes("l,c");
        componentsAreaIva.add(patternNumeroProtocolloLabel);
        componentsAreaIva.add(patternNumeroProtocolloComponent);

        // quarta riga scelta tipologia corrispettivo visualizzata solo se il
        // tipoRegistro del registro iva
        // scelto e' TipoRegistroIva.CORRISPETTIVO
        comboTipologiaCorrispettivo = builder.addPropertyAndLabel("tipologiaCorrispettivo", 1, 22);
        componentsAreaIva.add(comboTipologiaCorrispettivo[0]);
        componentsAreaIva.add(comboTipologiaCorrispettivo[1]);
        builder.nextRow();

        JComponent[] validazioneIvaAutomatica = builder.addPropertyAndLabel("validazioneAreaIvaAutomatica", 1, 24);
        componentsAreaIva.add(validazioneIvaAutomatica[0]);
        componentsAreaIva.add(validazioneIvaAutomatica[1]);
        builder.nextRow();

        builder.addPropertyAndLabel("statoAreaContabileGenerata", 1, 26);
        builder.nextRow();

        JComponent separatorAreaContabileDaPagamenti = getComponentFactory()
                .createLabeledSeparator(getMessage(SEPARATOR_AREA_CONTABILE_DA_INCASSO_PAGAMENTI));
        builder.addComponent(separatorAreaContabileDaPagamenti, 1, 28, 7, 1);
        componentsIncassiPagamenti.add(separatorAreaContabileDaPagamenti);
        builder.nextRow();

        JComponent[] contoCassaComponents = builder.addPropertyAndLabel("contoCassa", 1, 30);
        ((SearchPanel) contoCassaComponents[1]).getTextFields().get("descrizione").setColumns(14);
        componentsIncassiPagamenti.add(contoCassaComponents[0]);
        componentsIncassiPagamenti.add(contoCassaComponents[1]);

        JComponent[] raggruppamentoContoCassaComponents = builder.addPropertyAndLabel("raggruppamentoContoCassa", 5,
                30);
        componentsIncassiPagamenti.add(raggruppamentoContoCassaComponents[0]);
        componentsIncassiPagamenti.add(raggruppamentoContoCassaComponents[1]);

        addListeners();

        // aggiorno manualmente la visibilita' dei componenti a causa del fatto
        // che la prima volta non viene attivato il
        // listener legati alle property value changed
        boolean areaIvaEnabled = (Boolean) getValue("tipoDocumento.righeIvaEnable");
        for (JComponent component : componentsAreaIva) {
            component.setVisible(areaIvaEnabled);
        }
        GestioneIva gestioneIva = (GestioneIva) getValue("gestioneIva");
        boolean isIvaIntraArt17 = gestioneIva != null && gestioneIva != GestioneIva.NORMALE;
        for (JComponent component : componentsGestioneIvaIntraArt17) {
            component.setVisible(isIvaIntraArt17);
        }

        if (isEntitaPredefinitaAbilitata()) {
            Binding bindingEntita = bf.createBoundSearchText("entitaPredefinita",
                    new String[] { "codice", "anagrafica.denominazione" },
                    new String[] { "tipoDocumento.tipoEntita", "entitaAbilitateInRicerca" },
                    new String[] { EntitaByTipoSearchObject.TIPOENTITA_KEY,
                            EntitaByTipoSearchObject.FILTRO_ENTITA_ABILITATO });
            builder.addLabel("entitaPredefinita", 1, 32);
            SearchPanel searchPanelEntita = (SearchPanel) builder.addBinding(bindingEntita, 3, 32, 3, 1);
            searchPanelEntita.getTextFields().get("codice").setColumns(5);
            searchPanelEntita.getTextFields().get("anagrafica.denominazione").setColumns(20);
        }

        updateTipologiaCorrispettivoVisibility((RegistroIva) getValue("registroIva"));
        return builder.getPanel();
    }

    /**
     *
     * @return true se debug mode attivo
     */
    private boolean isDebugMode() {
        try {
            return settingsManager.getUserSettings().getBoolean("debugMode");
        } catch (SettingsException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("--> Chiave debug non trovata.", e);
            }
            return false;
        }
    }

    /**
     *
     * @return true se i campi per l'entità predefinita devono essere visualizzati
     */
    private boolean isEntitaPredefinitaAbilitata() {
        return isDebugMode() || "cosaro".equalsIgnoreCase(PanjeaSwingUtil.getUtenteCorrente().getCodiceAzienda());
    }

    /**
     * Rende visibili o invisibili i controlli collegati relativi alla gestione iva INTRA e ART17 protocollo collegato e
     * registro iva collegato.
     *
     * @param visible
     *            se visualizzare o nascondere i controlli
     */
    private void updateDatiIncassoPagamentoVisibility(boolean visible) {
        for (JComponent component : componentsIncassiPagamenti) {
            component.setVisible(visible);
        }
    }

    /**
     * Modifica la visibilita' del componente tipologia corrispettivo a seconda del registro iva solo nel caso in cui i
     * controlli sono stati creati.
     *
     * @param registroIva
     *            il registro iva per controllare se visualizzare i componenti
     */
    public void updateTipologiaCorrispettivoVisibility(RegistroIva registroIva) {
        boolean isCorrispettivo = registroIva != null && registroIva.getTipoRegistro() != null
                && registroIva.getTipoRegistro().compareTo(TipoRegistro.CORRISPETTIVO) == 0;
        comboTipologiaCorrispettivo[0].setVisible(isCorrispettivo);
        comboTipologiaCorrispettivo[1].setVisible(isCorrispettivo);
    }
}
