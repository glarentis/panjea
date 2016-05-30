package it.eurotn.panjea.fatturepa.rich.editors.ricerca.statoarea;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.springframework.binding.form.FormModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.fatturepa.domain.AreaNotificaFatturaPA;
import it.eurotn.panjea.fatturepa.domain.NotificaFatturaPA;
import it.eurotn.panjea.fatturepa.domain.StatoFatturaPA;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.components.htmleditor.HTMLEditorPane;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;
import net.atlanticbb.tantlinger.ui.text.WysiwygHTMLEditorKit;

public class AreaNotificaFatturaPAForm extends PanjeaAbstractForm {

    private class NotificaChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {

            updateControls();
        }

    }

    private static final String FORM_ID = "areaNotificaFatturaPAForm";

    private NotificaChangeListener notificaChangeListener;

    private HTMLEditorPane esitoHMLViewer;

    private JPanel esitoPanel;

    /**
     * Costruttore.
     */
    public AreaNotificaFatturaPAForm() {
        super(PanjeaFormModelHelper.createFormModel(new AreaNotificaFatturaPA(), false, FORM_ID), FORM_ID);

        this.notificaChangeListener = new NotificaChangeListener();
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout("right:pref,4dlu,fill:190dlu,10dlu,right:pref,4dlu,fill:50dlu",
                "2dlu,default,2dlu,default,2dlu,default,2dlu,fill:330dlu");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
        builder.setLabelAttributes("r, c");
        builder.nextRow();
        builder.setRow(2);

        builder.addPropertyAndLabel("notificaFatturaPA.statoFattura", 1);
        builder.addPropertyAndLabel("notificaFatturaPA.data", 5);
        builder.nextRow();

        builder.addHorizontalSeparator("Dettaglio", 1, 7);
        builder.nextRow();

        esitoPanel = getComponentFactory().createPanel(new CardLayout());

        HTMLEditorPane esitoEditor = (HTMLEditorPane) ((JPanel) bf.createBoundHTMLEditor("notificaFatturaPA.datiEsito")
                .getControl()).getComponent(0);
        esitoEditor.setPreferredSize(new Dimension(100, 200));
        esitoEditor.setMaximumSize(new Dimension(100, 200));
        esitoPanel.add(bf.createBoundCodeEditor("notificaFatturaPA.datiEsito", new ValueHolder(new ArrayList<String>()),
                true, false, true).getControl(), "editor");

        esitoHMLViewer = (HTMLEditorPane) ((JPanel) bf.createBoundHTMLEditor("notificaFatturaPA.datiEsitoHTML")
                .getControl()).getComponent(0);
        esitoHMLViewer.setReadOnly(true);
        esitoHMLViewer.getMenuBar().setVisible(false);
        esitoHMLViewer.getFormatToolBar().setVisible(false);
        esitoHMLViewer.setPreferredSize(new Dimension(100, 200));
        esitoHMLViewer.setMaximumSize(new Dimension(100, 200));
        esitoPanel.add(new JScrollPane(esitoHMLViewer), "viewer");
        builder.addComponent(esitoPanel, 1, 8, 7, 1);

        addFormObjectChangeListener(notificaChangeListener);
        getFormModel().addPropertyChangeListener(FormModel.READONLY_PROPERTY, notificaChangeListener);

        return builder.getPanel();
    }

    @Override
    protected Object createNewObject() {
        AreaNotificaFatturaPA areaNotificaFatturaPA = new AreaNotificaFatturaPA();
        areaNotificaFatturaPA.setNotificaFatturaPA(new NotificaFatturaPA());
        areaNotificaFatturaPA.getNotificaFatturaPA().setStatoFattura(StatoFatturaPA._DI);
        return areaNotificaFatturaPA;
    }

    @Override
    public void dispose() {
        removeFormObjectChangeListener(notificaChangeListener);
        getFormModel().removePropertyChangeListener(FormModel.READONLY_PROPERTY, notificaChangeListener);
        notificaChangeListener = null;

        super.dispose();
    }

    @Override
    public void setFormObject(Object formObject) {
        super.setFormObject(formObject);

        updateControls();
    }

    private void updateControls() {

        String esito = (String) getFormModel().getValueModel("notificaFatturaPA.datiEsitoHTML").getValue();

        // devo ripulire il contenuto perchè se applico un html con uno stile e poi uno senza in quest'ultimo lo style
        // non viene cancellato
        esitoHMLViewer.getWysEditor().setEditorKitForContentType("text/html", new WysiwygHTMLEditorKit());
        esitoHMLViewer.getWysEditor().setContentType("text/html");
        esitoHMLViewer.insertHTML(esitoHMLViewer.getWysEditor(), "<div></div>", 0);
        esitoHMLViewer.setText(esito);

        CardLayout cl = (CardLayout) (esitoPanel.getLayout());
        if (getFormModel().isReadOnly()) {
            cl.show(esitoPanel, "viewer");
        } else {
            cl.show(esitoPanel, "editor");
        }
    }

}
