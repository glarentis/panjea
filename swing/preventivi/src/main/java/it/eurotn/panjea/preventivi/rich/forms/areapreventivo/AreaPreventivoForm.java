/**
 *
 */
package it.eurotn.panjea.preventivi.rich.forms.areapreventivo;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.anagrafica.rich.search.SedeEntitaSearchObject;
import it.eurotn.panjea.preventivi.rich.forms.interfaces.IRequestFocus;
import it.eurotn.panjea.preventivi.util.AreaPreventivoFullDTO;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.rich.focuspolicy.PanjeaFocusTraversalPolicy;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

/**
 * @author mattia
 *
 */
public class AreaPreventivoForm extends PanjeaAbstractForm implements IRequestFocus {

    private class DataDocumentoChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (getFormModel().isReadOnly()) {
                return;
            }
            Date dataDocumento = (Date) evt.getNewValue();
            Integer durataValidita = (Integer) getValueModel("areaPreventivo.tipoAreaPreventivo.durataValiditaDefault")
                    .getValue();

            if (dataDocumento != null && durataValidita != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dataDocumento);
                calendar.add(Calendar.MONTH, durataValidita.intValue());
                Date dataScadenza = calendar.getTime();
                getValueModel("areaPreventivo.dataScadenza").setValue(dataScadenza);
            }
        }

    }

    private class DataRegistrazioneChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (getFormModel().isReadOnly()) {
                return;
            }
            Date dataRegistrazione = (Date) evt.getNewValue();
            Date dataScadenzaCorrente = (Date) getValueModel("areaPreventivo.dataScadenza").getValue();
            if (dataScadenzaCorrente == null) {
                getValueModel("areaPreventivo.dataScadenza").setValue(dataRegistrazione);
            }
        }
    }

    private class FormModelAreaPreventivoChangeListener implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            Integer idAreaPreventivo = (Integer) getFormModel().getValueModel("areaPreventivo.id").getValue();
            getFormModel().getFieldMetadata("areaPreventivo.tipoAreaPreventivo").setReadOnly(idAreaPreventivo != null);
        }
    }

    public static final String FORM_ID = "areaPreventivoForm";

    public static final String FORMMODEL_ID = "areaPreventivoFormModel";

    private DataDocumentoChangeListener dataDocumentoChangeListener;

    private JComponent dataRegistrazioneComponent = null;

    private AziendaCorrente aziendaCorrente = null;

    private JTextField fieldAnnoCompetenza = null;

    private PanjeaFocusTraversalPolicy panjeaFocusTraversalPolicy = null;
    private FormModelAreaPreventivoChangeListener enabledChangeListener = null;

    private DataRegistrazioneChangeListener datRegistrazioneChangeListener = null;

    /**
     * costruttore.
     */
    public AreaPreventivoForm() {
        super(PanjeaFormModelHelper.createFormModel(new AreaPreventivoFullDTO(), false, FORMMODEL_ID), FORM_ID);

        // Aggiungo il value model che mi servirà solamente nella search text
        // delle entità per includere le entità potenziali
        PanjeaSwingUtil.addValueModelToForm(Boolean.TRUE, getFormModel(), Boolean.class, "entitaPotenzialiPerRicerca",
                true);
    }

    /**
     * Crea e restituisce il binding per le note di testata.
     * 
     * @param bf
     *            BindingFactory
     * @return binding
     */
    private Binding createBindingForNote(PanjeaSwingBindingFactory bf) {
        Binding noteBinding = bf.createBinding("areaPreventivo.areaPreventivoNote.noteTestata");
        return noteBinding;
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout(
                "right:pref,4dlu,left:default, 10dlu, right:pref,4dlu,left:default, left:max(30dlu;pref), 10dlu, left:default,4dlu,left:50dlu,right:default,4dlu,fill:default",
                "4dlu,default, 3dlu,default, 3dlu,default, 3dlu,default, 3dlu,default");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new
                                                                               // FormDebugPanelNumbered());
        builder.setLabelAttributes("r, c");

        builder.nextRow();
        builder.setRow(2);

        // ### ROW 2: data registrazione e tipo area magazzino
        dataRegistrazioneComponent = builder.addPropertyAndLabel("areaPreventivo.dataRegistrazione", 1, 2)[1];
        builder.addPropertyAndLabel("areaPreventivo.dataConsegna", 5, 2);

        builder.addLabel("areaPreventivo.tipoAreaPreventivo", 10);
        Binding bindingTipoDoc = bf.createBoundSearchText("areaPreventivo.tipoAreaPreventivo",
                new String[] { "tipoDocumento.codice", "tipoDocumento.descrizione" });
        // builder.setComponentAttributes("l,c");
        SearchPanel tipoDocumentoSearchPanel = (SearchPanel) builder.addBinding(bindingTipoDoc, 12, 2, 4, 1);
        tipoDocumentoSearchPanel.getTextFields().get("tipoDocumento.codice").setColumns(5);
        tipoDocumentoSearchPanel.getTextFields().get("tipoDocumento.descrizione").setColumns(23);
        builder.nextRow();

        // // ### ROW 4: data documento, numero documento e riferimenti ordine
        builder.addPropertyAndLabel("areaPreventivo.documento.dataDocumento", 1, 4);
        builder.addPropertyAndLabel("areaPreventivo.dataScadenza", 5, 4);

        builder.addLabel("areaPreventivo.documento.codice", 10);
        Binding bindingCodice = bf.createBoundCodice("areaPreventivo.documento.codice",
                "areaPreventivo.tipoAreaPreventivo.tipoDocumento.registroProtocollo",
                "areaPreventivo.documento.valoreProtocollo",
                "areaPreventivo.tipoAreaPreventivo.tipoDocumento.patternNumeroDocumento", null);
        builder.addBinding(bindingCodice, 12, 4, 2, 1);

        builder.nextRow();

        // // ### entita e sede
        builder.addLabel("areaPreventivo.documento.entita", 1, 6);
        Binding bindEntita = getEntitaBinding(bf);
        builder.addBinding(bindEntita, 3, 6, 6, 1);

        builder.addLabel("areaPreventivo.documento.sedeEntita", 10, 6);
        Binding sedeEntitaBinding = bf.createBoundSearchText("areaPreventivo.documento.sedeEntita", null,
                new String[] { "areaPreventivo.documento.entita" },
                new String[] { SedeEntitaSearchObject.PARAMETER_ENTITA_ID });
        SearchPanel searchPanelSede = (SearchPanel) builder.addBinding(sedeEntitaBinding, 12, 6, 4, 1);
        searchPanelSede.getTextFields().get(null).setColumns(30);
        builder.nextRow();

        // // ### note, anno competenza
        builder.setComponentAttributes("f,c");
        builder.addLabel("areaPreventivo.areaPreventivoNote.noteTestata", 1);
        builder.addBinding(createBindingForNote(bf), 3, 8, 6, 1);
        builder.setComponentAttributes("l,c");

        fieldAnnoCompetenza = (JTextField) builder.addPropertyAndLabel("areaPreventivo.annoMovimento", 10)[1];
        fieldAnnoCompetenza.setColumns(5);

        builder.addPropertyAndLabel("areaPreventivo.dataAccettazione", 13, 8);

        // Initializzo il valueModel
        getValueModel("areaPreventivo");
        getValueModel("areaPreventivo.id");
        getValueModel("areaPreventivo.tipoAreaPreventivo");
        getValueModel("areaPreventivo.documento.sedeEntita");
        getValueModel("areaPreventivo.inserimentoBloccato");
        installChangeListeners();

        // lista di componenti che voglio saltare nella normale policy di ciclo del focus
        List<Component> componentsToSkip = new ArrayList<Component>();
        componentsToSkip.add(fieldAnnoCompetenza);

        panjeaFocusTraversalPolicy = new PanjeaFocusTraversalPolicy(
                getActiveWindow().getControl().getFocusTraversalPolicy(), componentsToSkip);

        return builder.getPanel();
    }

    @Override
    protected Object createNewObject() {
        logger.debug("--> Enter createNewObject");
        // recupera il precedente oggetto dal Form e ne recupera i valori
        AreaPreventivoFullDTO areaPreventivoFullDTO = (AreaPreventivoFullDTO) getFormObject();
        areaPreventivoFullDTO = areaPreventivoFullDTO.getInitializedNewObject();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        areaPreventivoFullDTO.getAreaPreventivo().setDataRegistrazione(calendar.getTime());
        areaPreventivoFullDTO.getAreaPreventivo().setDataScadenza(calendar.getTime());

        String codiceValutaAzienda = aziendaCorrente.getCodiceValuta();
        // HACK inizializzazione di codiceValuta
        areaPreventivoFullDTO.getAreaPreventivo().getDocumento().getTotale().setCodiceValuta(codiceValutaAzienda);
        areaPreventivoFullDTO.getAreaPreventivo().getTotaliArea().getSpeseTrasporto()
                .setCodiceValuta(codiceValutaAzienda);
        areaPreventivoFullDTO.getAreaPreventivo().getTotaliArea().getAltreSpese().setCodiceValuta(codiceValutaAzienda);
        areaPreventivoFullDTO.getAreaPreventivo().getTotaliArea().getTotaleMerce().setCodiceValuta(codiceValutaAzienda);

        if (areaPreventivoFullDTO.getAreaPreventivo().getAnnoMovimento() == -1) {
            areaPreventivoFullDTO.getAreaPreventivo().setAnnoMovimento(aziendaCorrente.getAnnoMagazzino());
        }
        return areaPreventivoFullDTO;
    }

    @Override
    public void dispose() {
        getFormModel().removePropertyChangeListener(getFormModelAreaPreventivoChangeListener());
        super.dispose();
        getFormModel().getFieldMetadata("areaPreventivo.dataRegistrazione")
                .removePropertyChangeListener(getDatRegistrazioneChangeListener());
        getFormModel().getFieldMetadata("areaPreventivo.documento.dataDocumento")
                .removePropertyChangeListener(getDataDocumentoChangeListener());
    }

    /**
     * @return focus policy per la testata dell'area ordine
     */
    public PanjeaFocusTraversalPolicy getAreaPreventivoFocusTraversalPolicy() {
        return panjeaFocusTraversalPolicy;
    }

    /**
     * 
     * @return dataDocumentoChangeListener
     */
    private DataDocumentoChangeListener getDataDocumentoChangeListener() {
        if (dataDocumentoChangeListener == null) {
            dataDocumentoChangeListener = new DataDocumentoChangeListener();
        }
        return dataDocumentoChangeListener;
    }

    /**
     * @return datRegistrazioneChangeListener
     */
    private DataRegistrazioneChangeListener getDatRegistrazioneChangeListener() {
        if (datRegistrazioneChangeListener == null) {
            datRegistrazioneChangeListener = new DataRegistrazioneChangeListener();
        }
        return datRegistrazioneChangeListener;
    }

    /**
     * crea e restituisce il SearchTextBinding di Entita.
     * 
     * @param bf
     *            BindingFactory
     * @return binding
     */
    private Binding getEntitaBinding(PanjeaSwingBindingFactory bf) {
        Binding bindingEntita = bf.createBoundSearchText("areaPreventivo.documento.entita",
                new String[] { "codice", "anagrafica.denominazione" },
                new String[] { "areaPreventivo.tipoAreaPreventivo.tipoDocumento.tipoEntita",
                        "entitaPotenzialiPerRicerca" },
                new String[] { EntitaByTipoSearchObject.TIPOENTITA_KEY,
                        EntitaByTipoSearchObject.INCLUDI_ENTITA_POTENZIALI });
        SearchPanel searchPanel = (SearchPanel) bindingEntita.getControl();
        searchPanel.getTextFields().get("codice").setColumns(5);
        searchPanel.getTextFields().get("anagrafica.denominazione").setColumns(20);
        return bindingEntita;
    }

    /**
     * @return enabledChangeListener
     */
    private FormModelAreaPreventivoChangeListener getFormModelAreaPreventivoChangeListener() {
        if (enabledChangeListener == null) {
            enabledChangeListener = new FormModelAreaPreventivoChangeListener();
        }
        return enabledChangeListener;
    }

    /**
     * Installa i listener sul form model o sulle proprietà del form.
     */
    private void installChangeListeners() {
        getFormModel().addPropertyChangeListener(getFormModelAreaPreventivoChangeListener());

        getFormModel().getValueModel("areaPreventivo.dataRegistrazione")
                .addValueChangeListener(getDatRegistrazioneChangeListener());

        getFormModel().getValueModel("areaPreventivo.documento.dataDocumento")
                .addValueChangeListener(getDataDocumentoChangeListener());
    }

    /**
     * Metodo che assegna il focus al componente.
     */
    @Override
    public void requestFocusForFormComponent() {
        dataRegistrazioneComponent.getComponent(0).requestFocusInWindow();
    }

    /**
     * @param aziendaCorrente
     *            The aziendaCorrente to set.
     */
    public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
        this.aziendaCorrente = aziendaCorrente;
    }

}
