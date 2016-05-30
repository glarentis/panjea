package it.eurotn.panjea.vending.rich.editors.distributore.distributore;

import javax.swing.BorderFactory;
import javax.swing.JComponent;

import org.springframework.richclient.form.binding.Binding;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.manutenzioni.rich.editors.installazioni.InstallazioneForm;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.panjea.vending.domain.Distributore;
import it.eurotn.panjea.vending.domain.Modello;
import it.eurotn.panjea.vending.domain.SistemaElettronico.TipoSistemaElettronico;
import it.eurotn.panjea.vending.rich.search.SistemiElettronicoSearchObject;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormLayoutBuilder;
import it.eurotn.rich.form.PanjeaFormModelHelper;

public class DistributoreForm extends PanjeaAbstractForm {

    private static final String FORM_ID = "distributoreForm";

    private Modello modello;

    private InstallazioneForm installazioneForm;

    /**
     * Costruttore.
     */
    public DistributoreForm() {
        super(PanjeaFormModelHelper.createFormModel(new Distributore(), false, FORM_ID), FORM_ID);
        installazioneForm = new InstallazioneForm(true);

        PanjeaSwingUtil.addValueModelToForm(TipoSistemaElettronico.GETTONIERA, getFormModel(),
                TipoSistemaElettronico.class, "tipoGettoniera", true);
        PanjeaSwingUtil.addValueModelToForm(TipoSistemaElettronico.LETTORE_BANCONOTE, getFormModel(),
                TipoSistemaElettronico.class, "tipoLettoreBanc", true);
        PanjeaSwingUtil.addValueModelToForm(TipoSistemaElettronico.SISTEMA_DI_PAGAMENTO, getFormModel(),
                TipoSistemaElettronico.class, "tipoSistemaPag", true);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout("right:pref,4dlu,fill:pref:g", "4dlu,default");
        PanjeaFormLayoutBuilder builder = new PanjeaFormLayoutBuilder(bf, layout);
        builder.setLabelAttributes("r, c");
        builder.setRow(2);

        builder.addPropertyAndLabel("codice");
        builder.nextRow();

        builder.addPropertyAndLabel("descrizione");
        builder.nextRow();

        builder.addLabel("datiVending.modello");
        Binding bindingTipoCom = bf.createBoundSearchText("datiVending.modello",
                new String[] { "codice", "descrizione" });
        SearchPanel searchPanel = (SearchPanel) bindingTipoCom.getControl();
        searchPanel.getTextFields().get("codice").setColumns(5);
        searchPanel.getTextFields().get("descrizione").setColumns(20);
        builder.addBinding(bindingTipoCom, 3);
        builder.nextRow();

        builder.addLabel("datiVending.gettoniera");
        Binding bindingSistemaPagamento = bf.createBoundSearchText("datiVending.gettoniera",
                new String[] { "codice", "descrizione" }, new String[] { "tipoGettoniera" },
                new String[] { SistemiElettronicoSearchObject.TIPO_SISTEMA_PARAM_KEY });
        SearchPanel searchPanelsistemaPagamento = (SearchPanel) bindingTipoCom.getControl();
        searchPanelsistemaPagamento.getTextFields().get("codice").setColumns(5);
        searchPanelsistemaPagamento.getTextFields().get("descrizione").setColumns(20);
        builder.addBinding(bindingSistemaPagamento, 3);
        builder.nextRow();

        builder.addLabel("datiVending.lettoreBanconote");
        bindingSistemaPagamento = bf.createBoundSearchText("datiVending.lettoreBanconote",
                new String[] { "codice", "descrizione" }, new String[] { "tipoLettoreBanc" },
                new String[] { SistemiElettronicoSearchObject.TIPO_SISTEMA_PARAM_KEY });
        searchPanelsistemaPagamento = (SearchPanel) bindingTipoCom.getControl();
        searchPanelsistemaPagamento.getTextFields().get("codice").setColumns(5);
        searchPanelsistemaPagamento.getTextFields().get("descrizione").setColumns(20);
        builder.addBinding(bindingSistemaPagamento, 3);
        builder.nextRow();

        builder.addLabel("datiVending.sistemaPagamento");
        bindingSistemaPagamento = bf.createBoundSearchText("datiVending.sistemaPagamento",
                new String[] { "codice", "descrizione" }, new String[] { "tipoSistemaPag" },
                new String[] { SistemiElettronicoSearchObject.TIPO_SISTEMA_PARAM_KEY });
        searchPanelsistemaPagamento = (SearchPanel) bindingTipoCom.getControl();
        searchPanelsistemaPagamento.getTextFields().get("codice").setColumns(5);
        searchPanelsistemaPagamento.getTextFields().get("descrizione").setColumns(20);
        builder.addBinding(bindingSistemaPagamento, 3);
        builder.nextRow();

        builder.addPropertyAndLabel("abilitato");
        builder.nextRow();

        builder.addPropertyAndLabel("barCode");
        builder.nextRow();

        builder.addPropertyAndLabel("proprietaCliente");
        builder.nextRow();

        builder.addPropertyAndLabel("datiVending.matricolaFornitore");
        builder.nextRow();

        JComponent installazioneControl = installazioneForm.getControl();
        builder.addComponentWidthSpan(installazioneControl, 1, 3);
        installazioneForm.getControl().setVisible(false);
        builder.nextRow();

        getFormModel().getFieldMetadata("proprietaCliente").setReadOnly(true);

        return builder.getPanel();
    }

    @Override
    protected Object createNewObject() {
        Distributore distributore = new Distributore();
        distributore.getDatiVending().setModello(modello);
        if (modello != null && !modello.isNew()) {
            distributore.setDescrizione(modello.getDescrizione() + " - " + modello.getCodice());
        }
        installazioneForm.getControl().setVisible(false);
        return distributore;
    }

    @Override
    public void setFormObject(Object formObject) {
        super.setFormObject(formObject);
        Distributore distributore = (Distributore) formObject;
        installazioneForm.setFormObject(distributore.getInstallazione());
        if (getValueModel("installazione").getValue() != null) {
            String codiceInstallazione = getValueModel("installazione.deposito.sedeEntita.entita.codice").getValue()
                    + " - "
                    + getValueModel("installazione.deposito.sedeEntita.entita.anagrafica.denominazione").getValue();
            installazioneForm.getControl().setBorder(BorderFactory.createTitledBorder(codiceInstallazione));
            installazioneForm.getControl().setVisible(true);
        } else {
            installazioneForm.getControl().setVisible(false);
        }
    }

    /**
     * @param modello
     *            the modello to set
     */
    public final void setModello(Modello modello) {
        this.modello = modello;
    }

}
