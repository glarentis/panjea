package it.eurotn.panjea.vending.rich.editors.lettureselezionatrice;

import java.awt.Dimension;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.converter.ObjectConverterManager;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.vending.domain.LetturaSelezionatrice;
import it.eurotn.panjea.vending.domain.RigaLetturaSelezionatrice;
import it.eurotn.rich.binding.TableEditableBinding;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormLayoutBuilder;
import it.eurotn.rich.form.PanjeaFormModelHelper;

public class LetturaSelezionatriceForm extends PanjeaAbstractForm {

    private static final String FORM_ID = "letturaSelezionatriceForm";

    private JLabel rifornimentoLabel = null;

    private RigheLetturaTableModel righeLetturaTableModel;

    /**
     * Costruttore.
     */
    public LetturaSelezionatriceForm() {
        super(PanjeaFormModelHelper.createFormModel(new LetturaSelezionatrice(), false, FORM_ID), FORM_ID);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout(
                "right:pref,4dlu,fill:60dlu, 10dlu, right:pref,4dlu,fill:60dlu, 10dlu, right:pref,4dlu,fill:60dlu",
                "4dlu,default");
        PanjeaFormLayoutBuilder builder = new PanjeaFormLayoutBuilder(bf, layout);
        builder.setLabelAttributes("r, c");
        builder.setRow(2);

        builder.addPropertyAndLabel("progressivo", 1);
        builder.addPropertyAndLabel("data", 5);
        builder.addPropertyAndLabel("numeroSacchetto", 9);
        builder.nextRow();

        builder.addLabel("rifornimento", 1);
        rifornimentoLabel = new JLabel(RcpSupport.getIcon(Documento.class.getName()));
        builder.addComponent(rifornimentoLabel, 3, 4, 5, 1, "l,c");
        builder.addPropertyAndLabel("dataRifornimento", 9);
        builder.nextRow();

        builder.addPropertyAndLabel("reso");
        builder.nextRow();

        builder.addLabel("installazione", 1);
        SearchPanel searchInstallazionePanel = (SearchPanel) builder.addBinding(
                bf.createBoundSearchText("installazione", new String[] { "codice", "descrizione" }), 3, 9, 1);
        searchInstallazionePanel.getTextFields().get("codice").setColumns(10);
        searchInstallazionePanel.getTextFields().get("descrizione").setColumns(20);
        builder.nextRow();

        builder.addLabel("distributore", 1);
        SearchPanel searchDistributore = (SearchPanel) builder.addBinding(bf.createBoundSearchText("distributore",
                new String[] { "codice", "descrizione", "datiVending.modello" }), 3, 9, 1);
        searchDistributore.getTextFields().get("codice").setColumns(6);
        searchDistributore.getTextFields().get("descrizione").setColumns(15);
        searchDistributore.getTextFields().get("datiVending.modello").setColumns(9);
        builder.nextRow();

        builder.addLabel("caricatore", 1);
        SearchPanel searchOperatore = (SearchPanel) builder.addBinding(
                bf.createBoundSearchText("caricatore", new String[] { "codice", "denominazione" }), 3, 9, 1);
        searchOperatore.getTextFields().get("codice").setColumns(10);
        searchOperatore.getTextFields().get("denominazione").setColumns(20);
        builder.nextRow();

        builder.addLabel("cassaDestinazione");
        SearchPanel searchCassa = (SearchPanel) builder.addBinding(
                bf.createBoundSearchText("cassaDestinazione", new String[] { "codice", "descrizione" }), 3, 9, 1);
        searchCassa.getTextFields().get("codice").setColumns(10);
        builder.nextRow();

        builder.addLabel("cassaOrigine");
        searchCassa = (SearchPanel) builder.addBinding(
                bf.createBoundSearchText("cassaOrigine", new String[] { "codice", "descrizione" }), 3, 9, 1);
        searchCassa.getTextFields().get("codice").setColumns(10);
        builder.nextRow();

        builder.addHorizontalSeparator("Lettura gettoni", 11);
        builder.nextRow();

        righeLetturaTableModel = new RigheLetturaTableModel();
        TableEditableBinding<RigaLetturaSelezionatrice> righeLetturaBinding = new TableEditableBinding<>(getFormModel(),
                "righe", Set.class, righeLetturaTableModel);
        builder.addBinding(righeLetturaBinding, 1, 11, 1);
        righeLetturaBinding.getControl().setPreferredSize(new Dimension(100, 200));

        return builder.getPanel();
    }

    @Override
    public void setFormObject(Object formObject) {
        super.setFormObject(formObject);

        LetturaSelezionatrice lettura = (LetturaSelezionatrice) formObject;
        rifornimentoLabel.setText(ObjectConverterManager.toString(lettura.getRifornimento()));
    }

}