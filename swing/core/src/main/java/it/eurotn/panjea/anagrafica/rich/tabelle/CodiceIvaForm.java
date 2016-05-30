package it.eurotn.panjea.anagrafica.rich.tabelle;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.rich.search.CodiceIvaSearchObject;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

public class CodiceIvaForm extends PanjeaAbstractForm {

    private static final String FORM_ID = "codiceIvaForm";
    private static final String FORM_MODEL_ID = "codiceIvaFormModel";

    /**
     * Costruttore.
     *
     * @param codiceIva
     *            codice iva
     */
    public CodiceIvaForm(final CodiceIva codiceIva) {
        super(PanjeaFormModelHelper.createFormModel(codiceIva, false, FORM_MODEL_ID), FORM_ID);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout("right:pref,4dlu,left:default, 10dlu, left:default,4dlu,left:default",
                "2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new
                                                                               // FormDebugPanel()
        builder.setLabelAttributes("r, c");

        builder.nextRow();
        builder.setRow(2);

        ((JTextField) builder.addPropertyAndLabel("codice", 1)[1]).setColumns(10);
        builder.nextRow();

        ((JTextField) builder.addPropertyAndLabel("descrizioneInterna", 1)[1]).setColumns(30);
        builder.nextRow();

        ((JTextField) builder.addPropertyAndLabel("descrizioneRegistro", 1)[1]).setColumns(30);
        builder.nextRow();

        ((JTextField) builder.addPropertyAndLabel("descrizioneDocumenti", 1)[1]).setColumns(30);
        builder.nextRow();

        Binding bindingPercAppl = bf.createBoundPercentageText("percApplicazione");
        builder.addLabel("percApplicazione", 1);
        builder.addBinding(bindingPercAppl, 3);

        Binding bindingPercInd = bf.createBoundPercentageText("percIndetraibilita");
        builder.addLabel("percIndetraibilita", 5);
        builder.addBinding(bindingPercInd, 7);
        builder.nextRow();

        ((JTextField) builder.addPropertyAndLabel("codiceEsportazioneDocumento", 1)[1]).setColumns(2);
        builder.nextRow();

        builder.addPropertyAndLabel("indicatoreVolumeAffari", 1);
        builder.addPropertyAndLabel("tipoCaratteristica", 5);
        builder.nextRow();

        builder.addPropertyAndLabel("tipoCodiceIva", 1);
        builder.addPropertyAndLabel("tipoTotalizzazione", 5);

        builder.nextRow();

        Binding bindingVentilazione = bf.createBoundSearchText("codiceIvaSostituzioneVentilazione",
                new String[] { "codice" });
        builder.addLabel("codiceIvaSostituzioneVentilazione", 1);
        SearchPanel searchPanelCodiceSostituzione = (SearchPanel) builder.addBinding(bindingVentilazione, 3);
        searchPanelCodiceSostituzione.getTextFields().get("codice").setColumns(5);

        Binding bindingCodIvaCollegato = bf.createBoundSearchText("codiceIvaCollegato", new String[] { "codice" });
        builder.addLabel("codiceIvaCollegato", 5);
        SearchPanel searchPanelCodiceCollegato = (SearchPanel) builder.addBinding(bindingCodIvaCollegato, 7);
        searchPanelCodiceCollegato.getTextFields().get("codice").setColumns(5);

        builder.nextRow();

        builder.addPropertyAndLabel("liquidazionePeriodica", 1);
        builder.addPropertyAndLabel("liquidazioneAnnuale", 5);

        builder.nextRow();

        builder.addPropertyAndLabel("ivaSospesa", 1);

        builder.nextRow();
        builder.addPropertyAndLabel("splitPayment", 1);
        PanjeaSwingUtil.addValueModelToForm(Boolean.TRUE, getFormModel(), Boolean.class, "splitPaymentSearch", true);
        Binding bindingCodIvaSplitPayment = bf.createBoundSearchText("codiceIvaSplitPayment", new String[] { "codice" },
                new String[] { "splitPaymentSearch" }, new String[] { CodiceIvaSearchObject.CODICI_SPLIT_PAYMENT });
        builder.addLabel("splitPaymentIvaCollegata", 5);
        SearchPanel searchCodIvaSplitPayment = (SearchPanel) builder.addBinding(bindingCodIvaSplitPayment, 7);
        searchCodIvaSplitPayment.getTextFields().get("codice").setColumns(5);

        builder.nextRow();

        builder.addHorizontalSeparator("Spesometro", 7);
        builder.nextRow();

        builder.addPropertyAndLabel("includiSpesometro", 1);
        builder.addPropertyAndLabel("tipologiaSpesometro", 5);

        builder.nextRow();
        builder.addPropertyAndLabel("palmareAbilitato");

        return builder.getPanel();
    }

}
