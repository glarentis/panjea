package it.eurotn.panjea.fatturepa.rich.editors.aziendapa;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.fatturepa.domain.DatiIscrizioneRea.StatoLiquidazione;
import it.eurotn.panjea.fatturepa.domain.DatiIscrizioneRea.TipologiaSoci;
import it.eurotn.panjea.fatturepa.rich.bd.FatturePAAnagraficaBD;
import it.eurotn.panjea.fatturepa.rich.bd.IFatturePAAnagraficaBD;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

/**
 * @author fattazzo
 *
 */
public class AziendaPAForm extends PanjeaAbstractForm {

    private class DatiIscrizioneReaListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {

            if (getFormModel().isReadOnly()) {
                return;
            }

            getFormModel().validate();

            Boolean reaEnable = (Boolean) getValueModel("datiIscrizioneRea.enable").getValue();

            reaNumero.setEditable(reaEnable);
            reaProvincia.setEditable(reaEnable);
            reaCapitale.setEditable(reaEnable);
            reaSoci.setEnabled(reaEnable);
            reaLiquidazione.setEnabled(reaEnable);
        }

    }

    private class RappresentanteFiscaleListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {

            if (getFormModel().isReadOnly()) {
                return;
            }

            getFormModel().validate();

            Boolean rappFiscEnable = (Boolean) getValueModel("rappresentanteFiscale.enable").getValue();

            rappFiscIdentificativoFiscale.setEditable(rappFiscEnable);
            rappFiscDenominazione.setEditable(rappFiscEnable);
            rappFiscTitolo.setEditable(rappFiscEnable);
            rappFiscNome.setEditable(rappFiscEnable);
            rappFiscCognome.setEditable(rappFiscEnable);
            rappFiscNazioneSearchPanel.getTextFields().get("codice").setEnabled(rappFiscEnable);
        }

    }

    public static final String FORM_ID = "aziendaPAForm";

    private RappresentanteFiscaleListener rappresentanteFiscaleListener;
    private DatiIscrizioneReaListener datiIscrizioneReaListener;

    private SearchPanel rappFiscNazioneSearchPanel;
    private JTextField rappFiscIdentificativoFiscale;
    private JTextField rappFiscDenominazione;
    private JTextField rappFiscTitolo;
    private JTextField rappFiscNome;
    private JTextField rappFiscCognome;

    private JTextField reaProvincia;
    private JTextField reaNumero;
    private JTextField reaCapitale;
    private JComboBox<TipologiaSoci> reaSoci;
    private JComboBox<StatoLiquidazione> reaLiquidazione;

    /**
     * Costruttore.
     */
    public AziendaPAForm() {
        super(PanjeaFormModelHelper.createFormModel(
                ((IFatturePAAnagraficaBD) RcpSupport.getBean(FatturePAAnagraficaBD.BEAN_ID)).caricaAziendaFatturaPA(),
                false, FORM_ID), FORM_ID);
        rappresentanteFiscaleListener = new RappresentanteFiscaleListener();
        datiIscrizioneReaListener = new DatiIscrizioneReaListener();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout(
                "right:pref,4dlu,fill:80dlu,10dlu,right:pref,4dlu,fill:80dlu,10dlu,right:pref,4dlu,fill:80dlu,left:default:grow",
                "4dlu,default");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
        builder.setLabelAttributes("r, c");
        builder.setRow(2);

        builder.addHorizontalSeparator("datiAziendali", 12);
        builder.nextRow();

        builder.addPropertyAndLabel("denominazione", 1, 4, 5);
        builder.addPropertyAndLabel("descrizioneTitolare", 9);
        builder.nextRow();

        builder.addPropertyAndLabel("codiceFiscale", 1);
        builder.addPropertyAndLabel("codiceIdentificativoFiscale", 5);
        builder.addPropertyAndLabel("descrizioneCodiceEori", 9);
        builder.nextRow();

        builder.addPropertyAndLabel("descrizioneAlbo", 1);
        builder.addPropertyAndLabel("provinciaAlbo", 5);
        builder.nextRow();

        builder.addPropertyAndLabel("descrizioneNumeroIscrizioneAlbo", 1);
        builder.addPropertyAndLabel("dataIscrizioneAlbo", 5);
        builder.nextRow();

        builder.addLabel("regimeFiscale", 1);
        builder.addBinding(bf.createBoundSearchText("regimeFiscale", new String[] { "descrizione" }), 3, 12, 5, 1);
        builder.nextRow();

        builder.addHorizontalSeparator("Legale rappresentante", 12);
        builder.nextRow();

        builder.addComponent(new JLabel("Dati configurati sul legale rappresentante dell'azienda"), 1, 18, 5, 1);
        builder.nextRow();

        builder.addPropertyAndLabel("legaleRappresentanteCodiceNazione", 1);
        builder.addPropertyAndLabel("legaleRappresentanteDenominazione", 5, 20, 5);
        builder.nextRow();

        builder.addPropertyAndLabel("legaleRappresentanteCodiceFiscale", 1);
        builder.addPropertyAndLabel("legaleRappresentanteIdentificativoFiscale", 5);
        builder.addPropertyAndLabel("legaleRappresentanteCodiceEori", 9);
        builder.nextRow();

        builder.addPropertyAndLabel("legaleRappresentanteTitolo", 1);
        builder.addPropertyAndLabel("legaleRappresentanteNome", 5);
        builder.addPropertyAndLabel("legaleRappresentanteCognome", 9);
        builder.nextRow();

        builder.addHorizontalSeparator("Sede", 12);
        builder.nextRow();

        builder.addComponent(new JLabel("Dati configurati sulla sede principale dell'azienda"), 1, 28, 5, 1);
        builder.nextRow();

        builder.addPropertyAndLabel("sedeIndirizzo", 1, 30, 5);
        builder.addPropertyAndLabel("sedeNumeroCivico", 9);
        builder.nextRow();

        builder.addPropertyAndLabel("sedeNazione", 1);
        builder.addPropertyAndLabel("sedeProvincia", 5);
        builder.nextRow();

        builder.addPropertyAndLabel("sedeComune", 1);
        builder.addPropertyAndLabel("sedeCAP", 5);
        builder.nextRow();

        builder.addHorizontalSeparator("Rappresentante fiscale", 12);
        builder.nextRow();

        builder.addComponent(
                new JLabel(
                        "Dati obbligatori qualora il cedente/prestatore si avvalga di un rappresentante fiscale in Italia ai sensi del DPR 633 del 1972 e successive modifiche ed integrazioni."),
                1, 38, 12, 1);
        builder.nextRow();

        builder.addPropertyAndLabel("rappresentanteFiscale.enable", 1);
        builder.nextRow();

        Binding rappFiscNazioneBinding = bf.createBoundSearchText("rappresentanteFiscale.nazione",
                new String[] { "codice" });
        builder.addLabel("nazione", 1);
        rappFiscNazioneSearchPanel = (SearchPanel) builder.addBinding(rappFiscNazioneBinding, 3);
        rappFiscNazioneSearchPanel.getTextFields().get("codice").setColumns(3);
        rappFiscDenominazione = (JTextField) builder.addPropertyAndLabel("rappresentanteFiscale.denominazione", 5)[1];
        rappFiscIdentificativoFiscale = (JTextField) builder
                .addPropertyAndLabel("rappresentanteFiscale.codiceIdentificativoFiscale", 9)[1];
        builder.nextRow();

        rappFiscTitolo = (JTextField) builder.addPropertyAndLabel("rappresentanteFiscale.descrizioneTitolare", 1)[1];
        rappFiscNome = (JTextField) builder.addPropertyAndLabel("rappresentanteFiscale.nome", 5)[1];
        rappFiscCognome = (JTextField) builder.addPropertyAndLabel("rappresentanteFiscale.cognome", 9)[1];
        builder.nextRow();

        builder.addHorizontalSeparator("Dati iscrizione Rea", 12);
        builder.nextRow();

        builder.addComponent(
                new JLabel(
                        "Dati obbligatori nei casi di società soggette al vincolo dell’iscrizione nel registro delle imprese ai sensi dell'art. 2250 del codice civile."),
                1, 48, 12, 1);
        builder.nextRow();

        builder.addPropertyAndLabel("datiIscrizioneRea.enable", 1);
        builder.nextRow();

        reaProvincia = (JTextField) builder.addPropertyAndLabel("datiIscrizioneRea.provincia", 1)[1];
        reaNumero = (JTextField) builder.addPropertyAndLabel("datiIscrizioneRea.numeroRea", 5)[1];
        reaCapitale = (JTextField) builder.addPropertyAndLabel("datiIscrizioneRea.importoCapitaleSociale", 9)[1];
        builder.nextRow();

        reaSoci = (JComboBox<TipologiaSoci>) builder.addPropertyAndLabel("datiIscrizioneRea.tipologiaSoci", 1)[1];
        reaLiquidazione = (JComboBox<StatoLiquidazione>) builder
                .addPropertyAndLabel("datiIscrizioneRea.statoLiquidazione", 5)[1];
        builder.nextRow();

        builder.addHorizontalSeparator("Contatto", 12);
        builder.nextRow();

        builder.addPropertyAndLabel("contatto.telefono", 1);
        builder.addPropertyAndLabel("contatto.fax", 5);
        builder.addPropertyAndLabel("contatto.email", 9);
        builder.nextRow();

        getValueModel("rappresentanteFiscale.enable").addValueChangeListener(rappresentanteFiscaleListener);
        // mi registro alle proprietà nome,cognome e denominazione perchè sono legate tra loro sulle regole di
        // validazione e se non chiamo il validate al loro cambiamento non vengono testate le regole presenti sul plugin
        // rules source
        getValueModel("rappresentanteFiscale.nome").addValueChangeListener(rappresentanteFiscaleListener);
        getValueModel("rappresentanteFiscale.cognome").addValueChangeListener(rappresentanteFiscaleListener);
        getValueModel("rappresentanteFiscale.denominazione").addValueChangeListener(rappresentanteFiscaleListener);
        addFormObjectChangeListener(rappresentanteFiscaleListener);
        getFormModel().addPropertyChangeListener(FormModel.READONLY_PROPERTY, rappresentanteFiscaleListener);

        getValueModel("datiIscrizioneRea.enable").addValueChangeListener(datiIscrizioneReaListener);
        // mi registro alle proprietà nome,cognome e denominazione perchè sono legate tra loro sulle regole di
        // validazione e se non chiamo il validate al loro cambiamento non vengono testate le regole presenti sul plugin
        // rules source
        getValueModel("datiIscrizioneRea.provincia").addValueChangeListener(datiIscrizioneReaListener);
        getValueModel("datiIscrizioneRea.numeroRea").addValueChangeListener(datiIscrizioneReaListener);
        getValueModel("datiIscrizioneRea.importoCapitaleSociale").addValueChangeListener(datiIscrizioneReaListener);
        getValueModel("datiIscrizioneRea.tipologiaSoci").addValueChangeListener(datiIscrizioneReaListener);
        getValueModel("datiIscrizioneRea.statoLiquidazione").addValueChangeListener(datiIscrizioneReaListener);
        addFormObjectChangeListener(datiIscrizioneReaListener);
        getFormModel().addPropertyChangeListener(FormModel.READONLY_PROPERTY, datiIscrizioneReaListener);

        return builder.getPanel();
    }

    @Override
    public void dispose() {
        getValueModel("rappresentanteFiscale.enable").removeValueChangeListener(rappresentanteFiscaleListener);
        getValueModel("rappresentanteFiscale.nome").removeValueChangeListener(rappresentanteFiscaleListener);
        getValueModel("rappresentanteFiscale.cognome").removeValueChangeListener(rappresentanteFiscaleListener);
        getValueModel("rappresentanteFiscale.denominazione").removeValueChangeListener(rappresentanteFiscaleListener);
        removeFormObjectChangeListener(rappresentanteFiscaleListener);
        getFormModel().removePropertyChangeListener(FormModel.READONLY_PROPERTY, rappresentanteFiscaleListener);
        rappresentanteFiscaleListener = null;

        getValueModel("datiIscrizioneRea.enable").removeValueChangeListener(datiIscrizioneReaListener);
        getValueModel("datiIscrizioneRea.provincia").removeValueChangeListener(datiIscrizioneReaListener);
        getValueModel("datiIscrizioneRea.numeroRea").removeValueChangeListener(datiIscrizioneReaListener);
        getValueModel("datiIscrizioneRea.importoCapitaleSociale").removeValueChangeListener(datiIscrizioneReaListener);
        getValueModel("datiIscrizioneRea.tipologiaSoci").removeValueChangeListener(datiIscrizioneReaListener);
        getValueModel("datiIscrizioneRea.statoLiquidazione").removeValueChangeListener(datiIscrizioneReaListener);
        removeFormObjectChangeListener(datiIscrizioneReaListener);
        getFormModel().removePropertyChangeListener(FormModel.READONLY_PROPERTY, datiIscrizioneReaListener);
        datiIscrizioneReaListener = null;

        super.dispose();
    }

}
