package it.eurotn.panjea.manutenzioni.rich.editors.operatore;

import java.awt.Dimension;

import javax.swing.JComponent;

import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.binding.Binding;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.magazzino.rich.search.MezzoTrasportoSearchObject;
import it.eurotn.panjea.manutenzioni.domain.Operatore;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.DatiGeograficiBinding;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormLayoutBuilder;
import it.eurotn.rich.form.PanjeaFormModelHelper;

public class OperatoreForm extends PanjeaAbstractForm {

    private static final String FORM_ID = "operatoreForm";

    /**
     * Costruttore.
     *
     */
    public OperatoreForm() {
        super(PanjeaFormModelHelper.createFormModel(new Operatore(), false, FORM_ID), FORM_ID);

        // Aggiungo il value model che mi servirà solamente nella search text dei mezzi di trasporto
        // per poter scegliere
        // solo quelli senza deposito
        ValueModel mezziSenzaCaricatoreValueModel = new ValueHolder(Boolean.TRUE);
        DefaultFieldMetadata mezziSenzaCaricatoreData = new DefaultFieldMetadata(getFormModel(),
                new FormModelMediatingValueModel(mezziSenzaCaricatoreValueModel), Boolean.class, true, null);
        getFormModel().add(MezzoTrasportoSearchObject.SENZA_CARICATORE_KEY, mezziSenzaCaricatoreValueModel,
                mezziSenzaCaricatoreData);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout("right:pref,4dlu,left:80dlu, 10dlu, right:pref,4dlu,left:80dlu,80dlu",
                "4dlu,default");
        PanjeaFormLayoutBuilder builder = new PanjeaFormLayoutBuilder(bf, layout);
        builder.setLabelAttributes("r, c");
        builder.setRow(2);

        builder.addPropertyAndLabel("codice");
        builder.nextRow();

        builder.addPropertyAndLabel("nome");
        builder.addPropertyAndLabel("cognome", 5);
        builder.nextRow();

        builder.addPropertyAndLabel("tecnico");
        builder.addPropertyAndLabel("caricatore", 5);
        builder.nextRow();

        builder.addLabel("codiceFiscale", 1);
        builder.addBinding(bf.createBoundCodiceFiscale("codiceFiscale", "nome", "cognome", null, null, null), 3, 5, 1)
                .setPreferredSize(new Dimension(250, 22));
        builder.nextRow();

        builder.addPropertyAndLabel("telefono");
        builder.addPropertyAndLabel("cellulare", 5);
        builder.nextRow();

        builder.addLabel("mezzoTrasporto");
        SearchPanel searchPanelDeposito = (SearchPanel) builder
                .addBinding(bf.createBoundSearchText("mezzoTrasporto", new String[] { "targa", "descrizione" },
                        new String[] { MezzoTrasportoSearchObject.SENZA_CARICATORE_KEY },
                        new String[] { MezzoTrasportoSearchObject.SENZA_CARICATORE_KEY }), 3, 5, 1);
        searchPanelDeposito.getTextFields().get("targa").setColumns(10);
        searchPanelDeposito.getTextFields().get("descrizione").setColumns(26);
        builder.nextRow();

        builder.addHorizontalSeparator("estremiDiNascitaSeparator", 8);
        builder.nextRow();

        DatiGeograficiBinding bindingDatiGeografici = (DatiGeograficiBinding) bf
                .createDatiGeograficiBinding("datiGeograficiNascita", "right:70dlu");
        builder.addBinding(bindingDatiGeografici, 1, 8, 1);
        builder.nextRow();

        builder.addHorizontalSeparator("estremiDiResidenzaSeparator", 8);
        builder.nextRow();

        builder.addPropertyAndLabel("viaResidenza", 1, 5, 1);
        builder.nextRow();

        DatiGeograficiBinding bindingDatiGeograficiResidenza = (DatiGeograficiBinding) bf
                .createDatiGeograficiBinding("datiGeograficiResidenza", "right:70dlu");
        builder.addBinding(bindingDatiGeograficiResidenza, 1, 8, 1);
        builder.nextRow();

        builder.addHorizontalSeparator("Documenti di identità", 8);
        builder.nextRow();

        builder.addPropertyAndLabel("documentoIdentitaTipo");
        builder.addPropertyAndLabel("documentoIdentitaNumero", 5);
        builder.nextRow();

        builder.addHorizontalSeparator("Patente", 8);
        builder.nextRow();

        builder.addPropertyAndLabel("patenteNumero");
        builder.addPropertyAndLabel("patenteScadenza", 5);
        builder.nextRow();
        builder.addLabel("tipoAreaMagazzino", 6);
        Binding bindingTipoDoc = bf.createBoundSearchText("tipoAreaMagazzinoFattura",
                new String[] { "tipoDocumento.codice", "tipoDocumento.descrizione" });
        SearchPanel tipoDocumentoSearchPanel = (SearchPanel) builder.addBinding(bindingTipoDoc, 3, 6, 1);
        tipoDocumentoSearchPanel.getTextFields().get("tipoDocumento.codice").setColumns(5);
        tipoDocumentoSearchPanel.getTextFields().get("tipoDocumento.descrizione").setColumns(23);

        return builder.getPanel();
    }

}
