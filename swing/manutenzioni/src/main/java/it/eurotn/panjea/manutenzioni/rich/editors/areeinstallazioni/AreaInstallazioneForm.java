package it.eurotn.panjea.manutenzioni.rich.editors.areeinstallazioni;

import java.math.BigDecimal;

import javax.swing.JComponent;

import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.anagrafica.rich.search.DepositoSearchObject;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.anagrafica.rich.search.SedeEntitaSearchObject;
import it.eurotn.panjea.manutenzioni.domain.documento.AreaInstallazione;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.binding.codice.CodiceBinder.CodicePanel;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormLayoutBuilder;
import it.eurotn.rich.form.PanjeaFormModelHelper;

public class AreaInstallazioneForm extends PanjeaAbstractForm {

    private static final String FORM_ID = "areaInstallazioneForm";

    /**
     * Costruttore.
     */
    public AreaInstallazioneForm() {
        super(PanjeaFormModelHelper.createFormModel(new AreaInstallazione(), false, FORM_ID), FORM_ID);
        PanjeaSwingUtil.addValueModelToForm(Boolean.TRUE, getFormModel(), Boolean.class, "soloDepAzienda", true);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout("right:pref,4dlu,left:pref,10dlu,right:pref,4dlu,left:pref", "4dlu,default");
        PanjeaFormLayoutBuilder builder = new PanjeaFormLayoutBuilder(bf, layout);
        builder.setLabelAttributes("r, c");
        builder.setRow(2);
        builder.addLabel("documento.tipoDocumento");
        Binding bindingTipoDoc = bf.createBoundSearchText("tipoAreaDocumento",
                new String[] { "tipoDocumento.codice", "tipoDocumento.descrizione" });
        SearchPanel tipoDocumentoSearchPanel = (SearchPanel) builder.addBinding(bindingTipoDoc, 3);
        tipoDocumentoSearchPanel.getTextFields().get("tipoDocumento.codice").setColumns(5);
        tipoDocumentoSearchPanel.getTextFields().get("tipoDocumento.descrizione").setColumns(23);
        builder.addLabel("documento.codice", 5);
        Binding bindingCodice = bf.createBoundCodice("documento.codice",
                "tipoAreaDocumento.tipoDocumento.registroProtocollo", "documento.valoreProtocollo",
                "tipoAreaDocumento.tipoDocumento.patternNumeroDocumento", null);
        CodicePanel codicePanel = (CodicePanel) builder.addBinding(bindingCodice, 7);
        codicePanel.getTextFieldCodice().setColumns(15);
        builder.nextRow();
        builder.addPropertyAndLabel("documento.dataDocumento");

        builder.addLabel("depositoOrigine", 5);
        Binding bindDepOrigine = bf.createBoundSearchText("depositoOrigine", new String[] { "codice", "descrizione" },
                new String[] { "soloDepAzienda" }, new String[] { DepositoSearchObject.SOLO_DEPOSITO_AZIENDA_PARAM });
        SearchPanel searchPanelDepositoOrigine = (SearchPanel) builder.addBinding(bindDepOrigine, 7);
        searchPanelDepositoOrigine.getTextFields().get("codice").setColumns(5);
        searchPanelDepositoOrigine.getTextFields().get("descrizione").setColumns(23);

        builder.nextRow();
        builder.addLabel("documento.entita");
        Binding bindingEntita = bf.createBoundSearchText("documento.entita",
                new String[] { "codice", "anagrafica.denominazione" },
                new String[] { "tipoAreaDocumento.tipoDocumento.tipoEntita" },
                new String[] { EntitaByTipoSearchObject.TIPOENTITA_KEY });
        SearchPanel searchPanel = (SearchPanel) bindingEntita.getControl();
        searchPanel.getTextFields().get("codice").setColumns(5);
        searchPanel.getTextFields().get("anagrafica.denominazione").setColumns(20);
        builder.addBinding(bindingEntita, 3);

        builder.addLabel("documento.sedeEntita", 5);
        Binding sedeEntitaBinding = bf.createBoundSearchText("documento.sedeEntita", null,
                new String[] { "documento.entita" }, new String[] { SedeEntitaSearchObject.PARAMETER_ENTITA_ID });
        SearchPanel searchPanelSede = (SearchPanel) builder.addBinding(sedeEntitaBinding, 7, 1, 1);
        searchPanelSede.getTextFields().get(null).setColumns(30);
        builder.nextRow();
        builder.addPropertyAndLabel("note", 1, 5, 1);

        getValueModel("documento.entita").addValueChangeListener(
                new EntitaPropertyChange(getFormModel(), "documento.entita", "documento.sedeEntita"));
        getValueModel("tipoAreaDocumento")
                .addValueChangeListener(new TipoAreaInstallazionePropertyChange(getFormModel()));
        return builder.getPanel();
    }

    @Override
    protected Object createNewObject() {
        AziendaCorrente acorrente = RcpSupport.getBean(AziendaCorrente.BEAN_ID);
        AreaInstallazione areaInstallazione = new AreaInstallazione();
        areaInstallazione.getDocumento().getTotale().setCodiceValuta(acorrente.getCodiceValuta());
        areaInstallazione.getDocumento().getTotale().setTassoDiCambio(BigDecimal.ONE);
        return areaInstallazione;
    }

}