package it.eurotn.panjea.vending.rich.editors.vendingsettings;

import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.magazzino.rich.search.TipoAreaMagazzinoSearchObject;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.panjea.vending.domain.EvaDtsImportFolder;
import it.eurotn.panjea.vending.domain.VendingSettings;
import it.eurotn.rich.binding.TableEditableBinding;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormLayoutBuilder;
import it.eurotn.rich.form.PanjeaFormModelHelper;

public class VendingSettingsForm extends PanjeaAbstractForm {

    private static final String FORM_ID = "vendingSettingsForm";

    /**
     * Costruttore.
     */
    public VendingSettingsForm() {
        super(PanjeaFormModelHelper.createFormModel(new VendingSettings(), false, FORM_ID), FORM_ID);

        PanjeaSwingUtil.addValueModelToForm(Boolean.TRUE, getFormModel(), Boolean.class, "gestioneVending", true);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout("right:pref,4dlu,left:pref,100dlu",
                "4dlu,default,1dlu,default,10dlu,default,1dlu,100dlu");
        PanjeaFormLayoutBuilder builder = new PanjeaFormLayoutBuilder(bf, layout);
        builder.setLabelAttributes("r, c");
        builder.setRow(2);

        builder.addHorizontalSeparator("Importazione EVA DTS", 4);
        builder.nextRow();

        builder.addLabel("evadtsTipoDocumentoImportazione");
        Binding bindingTipoDoc = bf.createBoundSearchText("evadtsTipoDocumentoImportazione",
                new String[] { "tipoDocumento.codice", "tipoDocumento.descrizione" },
                new String[] { "gestioneVending" },
                new String[] { TipoAreaMagazzinoSearchObject.PARAMETRO_GESTIONE_VENDING });
        SearchPanel tipoDocumentoSearchPanel = (SearchPanel) builder.addBinding(bindingTipoDoc, 3);
        tipoDocumentoSearchPanel.getTextFields().get("tipoDocumento.codice").setColumns(5);
        tipoDocumentoSearchPanel.getTextFields().get("tipoDocumento.descrizione").setColumns(23);
        builder.nextRow();

        builder.addComponent(new JLabel(RcpSupport.getMessage("evaDtsImportFolder")), 1, 4, 1);
        builder.nextRow();

        EvaDtsImportFolderTableModel evaDtsImportFolderTableModel = new EvaDtsImportFolderTableModel();
        TableEditableBinding<EvaDtsImportFolder> evaDtsImportFolderBinding = new TableEditableBinding<>(getFormModel(),
                "importFolder", List.class, evaDtsImportFolderTableModel);
        builder.addBinding(evaDtsImportFolderBinding, 1, 4, 1);
        evaDtsImportFolderBinding.getTableWidget().setEditable(false);

        return builder.getPanel();
    }

}