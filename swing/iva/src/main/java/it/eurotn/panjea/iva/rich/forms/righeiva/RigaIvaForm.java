package it.eurotn.panjea.iva.rich.forms.righeiva;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;

import javax.swing.JComponent;

import org.apache.log4j.Logger;
import org.springframework.richclient.form.builder.TableFormBuilder;

import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.iva.domain.RigaIva;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.ImportoBinding;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.binding.searchtext.SearchTextBinding;
import it.eurotn.rich.components.ImportoTextField;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

public class RigaIvaForm extends PanjeaAbstractForm {

    /**
     * Property change che aggiorna il valore di imposta e se tipo documento ha gestione iva intra o art.17 valorizza il
     * codice iva collegato e aggiorna il valore di imposta collegata.
     *
     * @author Leonardo
     */
    private class CodiceIvaChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            logger.debug("--> Enter CodiceIvaChangeListener.propertyChange");
            // non serve controllare se readOnly visto che rimuovo i property change sulla preSetFormObject e li
            // riabilito sulla postSetFormObject

            // basta che riaggiorno il value di imposta e imposta collegata dato che il calcolo viene eseguito sulla
            // classe RigaIva
            Importo impostaAggiornata = (Importo) getValue("impostaVisualizzata");
            getValueModel("impostaVisualizzata").setValue(impostaAggiornata.clone());

            getFormModel().getValueModel("codiceIvaCollegato").setValue(null);
            getValueModel("impostaCollegataVisualizzata.importoInValuta").setValue(BigDecimal.ZERO);
            getValueModel("impostaCollegataVisualizzata.importoInValutaAzienda").setValue(BigDecimal.ZERO);

            // se la gestione intra o art17 e' abilitata allora setto il codice iva collegato e calcolo l'imposta
            // collegata
            if (isIntraAbilitato() && evt.getNewValue() != null) {
                CodiceIva codiceIva = (CodiceIva) evt.getNewValue();
                CodiceIva codiceIvaCollegato = codiceIva.getCodiceIvaCollegato();

                if (codiceIvaCollegato != null && !codiceIvaCollegato.isNew()) {
                    getFormModel().getValueModel("codiceIvaCollegato").setValue(codiceIvaCollegato);

                    // aggiorno il valore di imposta collegata richiamando il value model aggiornato
                    Importo impostaCollegataAggiornata = (Importo) getValue("impostaCollegataVisualizzata");
                    getValueModel("impostaCollegataVisualizzata").setValue(impostaCollegataAggiornata.clone());
                }
            }
            getFormModel().isDirty();
            logger.debug("--> Exit CodiceIvaChangeListener.propertyChange");
        }
    }

    /**
     * Change listener su imposta che aggiorna semplicemente il valore del campo e se la gestione alternativa e'
     * abilitata aggiorna anche il valore di imposta collegata.
     *
     * @author Leonardo
     */
    private class ImponibileChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            logger.debug("--> Enter ImponibileChangeListener.propertyChange");
            // non serve controllare se readOnly visto che rimuovo i property change sulla preSetFormObject e li
            // riabilito sulla postSetFormObject

            // eseguo il ricalcolo di imposta e impostaCollegata e forzo l'aggiornamento del ValueModel di
            // impostaVisualizzata

            // il form object rimane alla versione committata del form, il nuovo valore effettivo dell'imponibile e'
            // nell'evento evt.getNewValue()
            BigDecimal newImponibile = (BigDecimal) evt.getNewValue();
            RigaIva rigaIva = (RigaIva) getFormModel().getFormObject();

            Importo importo = rigaIva.getImponibileVisualizzato();
            importo.setImportoInValuta(newImponibile);
            // importo.setImportoInValutaAzienda(newImponibile);
            importo.calcolaImportoValutaAzienda(2);

            // calcola tutti i valori per la rigaIva
            if (rigaIva.isNotaCredito()) {
                rigaIva.setImponibile(importo.negate());
            } else {
                rigaIva.setImponibile(importo);
            }

            // ma non aggiorna il form model; per fargli aggiornare i campi devo forzare la rilettura delle proprieta';
            // devo puntare a importoInValuta e importoInValutaAzienda altrimenti non si aggiorna.
            // Importo impostaAggiornata = (Importo)
            // getValue("impostaVisualizzata");
            // getValueModel("impostaVisualizzata").setValue(impostaAggiornata.clone());
            // getValueModel("impostaVisualizzata").setValue(rigaIva.getImposta());
            rigaIva.calcolaImposta();
            getValueModel("impostaVisualizzata").setValue(rigaIva.getImpostaVisualizzata());

            if (isIntraAbilitato()) {
                Importo impostaCollegataAggiornata = (Importo) getValue("impostaCollegataVisualizzata");
                getValueModel("impostaCollegataVisualizzata").setValue(impostaCollegataAggiornata.clone());
            }
            logger.debug("--> Exit ImponibileChangeListener.propertyChange");
        }
    }

    /**
     * Property change su change form object che aggiorna la visibilita' dei componenti intra/art17 a seconda del tipo
     * documento.
     *
     * @author Leonardo
     */
    private class RigaIvaChangeListener implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            updateIntraComponentsVisibility(isIntraAbilitato());
        }
    }

    private static Logger logger = Logger.getLogger(RigaIvaForm.class);

    private static final String FORM_ID = "rigaIvaForm";
    private JComponent[] compsCodCollegato;
    private JComponent[] compsImpostaCollegata;
    private JComponent separator;
    private CodiceIvaChangeListener codiceIvaChangeListener = null;
    private RigaIvaChangeListener rigaIvaChangListener = null;
    private ImponibileChangeListener imponibileChangeListener = null;
    private AbstractAreaIvaModel areaIvaModel;

    private ImportoTextField imponibileTextField;

    /**
     * Default constructor.
     */
    public RigaIvaForm() {
        super(PanjeaFormModelHelper.createFormModel(new RigaIva(), false, FORM_ID), FORM_ID);
    }

    /**
     * aggiunta dei {@link PropertyChangeListener} per iol mantenimento corretto dei dati gestiti in questo form.
     */
    public void activateListeners() {
        getFormModel().getValueModel("codiceIva").addValueChangeListener(getCodiceIvaChangeListener());
        getFormModel().getValueModel("imponibileVisualizzato.importoInValuta")
                .addValueChangeListener(getImponibileChangeListener());

        // forza la creazione dei ValueModel per evitare un
        // ConcurrentModificationException
        getFormModel().getValueModel("imponibile.importoInValutaAzienda").getValue();
        getFormModel().getValueModel("imponibile.importoInValuta").getValue();
        getFormModel().getValueModel("imposta.importoInValutaAzienda").getValue();
        getFormModel().getValueModel("imposta.importoInValuta").getValue();
        getFormModel().getValueModel("impostaCollegata.importoInValuta").getValue();
        getFormModel().getValueModel("impostaCollegata.importoInValutaAzienda").getValue();

        getFormModel().getValueModel("imponibileVisualizzato.importoInValuta").getValue();
        getFormModel().getValueModel("imponibileVisualizzato.importoInValutaAzienda").getValue();
        getFormModel().getValueModel("impostaVisualizzata.importoInValuta").getValue();
        getFormModel().getValueModel("impostaVisualizzata.importoInValutaAzienda").getValue();
        getFormModel().getValueModel("impostaCollegataVisualizzata.importoInValuta").getValue();
        getFormModel().getValueModel("impostaCollegataVisualizzata.importoInValutaAzienda").getValue();
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        TableFormBuilder builder = new TableFormBuilder(bf);
        builder.setLabelAttributes("colGrId=label colSpec=right:pref");

        builder.row();
        // ------------------------------- PRIMA RIGA

        ImportoBinding importoBinding = (ImportoBinding) bf.createBoundImportoTextField("imponibileVisualizzato");
        builder.add(importoBinding, "colSpan=1 align=left");
        imponibileTextField = importoBinding.getImportoTextField();
        importoBinding.getImportoTextField().setColumns(10);
        builder.row();
        // ------------------------------- SECONDA RIGA
        SearchTextBinding searchTextBinding;
        if (((RigaIva) getFormObject()).getAreaIva().getAreaContabile().getId() != null) {
            logger.debug("--> Filtro la ricerca codice iva per tipologia corrispettivo");
            searchTextBinding = (SearchTextBinding) bf.createBoundSearchText("codiceIva",
                    new String[] { "codice", "descrizioneInterna" },
                    new String[] { "areaIva.areaContabile.tipoAreaContabile.tipologiaCorrispettivo" },
                    new String[] { "tipologiaCorrispettivo" });
        } else {
            logger.debug("--> Non filtro la ricerca codice iva per tipologia corrispettivo");
            searchTextBinding = (SearchTextBinding) bf.createBoundSearchText("codiceIva",
                    new String[] { "codice", "descrizioneInterna" });
        }
        SearchPanel searchPanel = (SearchPanel) builder.add(searchTextBinding, "align=left")[1];

        searchPanel.getTextFields().get("codice").setColumns(5);
        searchPanel.getTextFields().get("descrizioneInterna").setColumns(16);

        builder.row();

        // ------------------------------- TERZA RIGA
        ImportoBinding importoBinding2 = (ImportoBinding) bf.createBoundImportoTextField("impostaVisualizzata");
        builder.add(importoBinding2, "colSpan=1 align=left");
        importoBinding2.getImportoTextField().setColumns(10);

        separator = getComponentFactory().createLabeledSeparator("Dati registro IVA collegato");
        builder.row();
        builder.getLayoutBuilder().cell(separator);
        builder.row();

        // ------------------------------- QUARTA RIGA
        compsCodCollegato = builder.add(
                bf.createBoundSearchText("codiceIvaCollegato", new String[] { "codice", "descrizioneInterna" }),
                "align=left");
        SearchPanel searchPanelCodiceCollegato = (SearchPanel) compsCodCollegato[1];
        searchPanelCodiceCollegato.getTextFields().get("codice").setColumns(5);
        searchPanelCodiceCollegato.getTextFields().get("descrizioneInterna").setColumns(16);
        getFormModel().getFieldMetadata("codiceIvaCollegato").setReadOnly(true);

        builder.row();
        // ------------------------------- QUINTA RIGA
        ImportoBinding importoBinding3 = (ImportoBinding) bf
                .createBoundImportoTextField("impostaCollegataVisualizzata");
        compsImpostaCollegata = builder.add(importoBinding3, "colSpan=1 align=left");
        importoBinding3.getImportoTextField().setColumns(10);

        builder.row();

        addFormObjectChangeListener(getRigaIvaChangListener());
        activateListeners();

        return builder.getForm();
    }

    /*
     * Sovrascrivo per creare una nuova riga iva con l'area contabile corrente dell'editor dato che per la ricerca di
     * codici iva mi serve avere l'areaContabile per passare al search object il parametro tipologiaCorrispettivo su
     * tipoAreaContabile su areaContabile di riga iva
     */
    @Override
    protected Object createNewObject() {
        return areaIvaModel.creaNuovaRiga();
    }

    /**
     * Disattiva i propertyChangeListeners.
     */
    public void deactivateListeners() {
        getFormModel().getValueModel("codiceIva").removeValueChangeListener(getCodiceIvaChangeListener());
        getFormModel().getValueModel("imponibileVisualizzato.importoInValuta")
                .removeValueChangeListener(getImponibileChangeListener());
    }

    /**
     * @return the codiceIvaChangeListener
     */
    private PropertyChangeListener getCodiceIvaChangeListener() {
        if (codiceIvaChangeListener == null) {
            codiceIvaChangeListener = new CodiceIvaChangeListener();
        }
        return codiceIvaChangeListener;
    }

    /**
     * @return the imponibileChangeListener
     */
    private PropertyChangeListener getImponibileChangeListener() {
        if (imponibileChangeListener == null) {
            imponibileChangeListener = new ImponibileChangeListener();
        }
        return imponibileChangeListener;
    }

    /**
     * @return the imponibileTextField
     */
    public ImportoTextField getImponibileTextField() {
        return imponibileTextField;
    }

    /**
     * @return the rigaIvaChangListener
     */
    private PropertyChangeListener getRigaIvaChangListener() {
        if (rigaIvaChangListener == null) {
            rigaIvaChangListener = new RigaIvaChangeListener();
        }
        return rigaIvaChangListener;
    }

    /**
     * testa se la gestione iva INTRA o ART.17 e' abilitata.
     *
     * @return true or false
     */
    public boolean isIntraAbilitato() {
        return areaIvaModel.isIntraAbilitato();
    }

    /**
     * @param areaIvaModel
     *            The areaIvaModel to set.
     */
    public void setAreaIvaModel(AbstractAreaIvaModel areaIvaModel) {
        this.areaIvaModel = areaIvaModel;
    }

    /**
     * Aggiorna la visibilita' dei componenti intra/art17.
     *
     * @param visible
     *            visible
     */
    public void updateIntraComponentsVisibility(boolean visible) {
        if (!isControlCreated()) {
            // controlli non creati esco
            return;
        }
        if (compsCodCollegato != null) {
            for (JComponent component : compsCodCollegato) {
                component.setVisible(visible);
            }
        }
        if (compsImpostaCollegata != null) {
            for (JComponent component : compsImpostaCollegata) {
                component.setVisible(visible);
            }
        }
        if (separator != null) {
            separator.setVisible(visible);
        }
    }
}
