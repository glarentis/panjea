package it.eurotn.panjea.rich.editors.dms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jdesktop.swingx.VerticalLayout;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.dms.domain.DmsSettings;
import it.eurotn.panjea.dms.domain.TipoDocumentoDmsSettings;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.TableEditableBinding;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

public class DmsSettingsForm extends PanjeaAbstractForm {

    public static final String FORM_ID = "dmsSettingsForm";

    /**
     * Costruttore.
     */
    public DmsSettingsForm() {
        super(PanjeaFormModelHelper.createFormModel(new DmsSettings(), false, FORM_ID), FORM_ID);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout("right:pref,4dlu,fill:200dlu,4dlu,left:default:grow",
                "4dlu,default,4dlu,fill:14dlu,4dlu,fill:14dlu,4dlu,fill:14dlu,4dlu,default,4dlu,default,4dlu,400dlu");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
        builder.setLabelAttributes("r, c");
        builder.setRow(2);

        builder.addPropertyAndLabel("serviceUrl");
        builder.nextRow();

        builder.addLabel("articoliFolder", 1);
        List<String> variabiliArticoli = new ArrayList<String>();
        for (String variabile : DmsSettings.ARTICOLI_VARIABLES) {
            variabiliArticoli.add(variabile);
        }
        ValueHolder valueHolder = new ValueHolder(variabiliArticoli);
        builder.addBinding(bf.createBoundCodeEditor("articoliFolderPattern", valueHolder, true, true, false), 3);
        builder.nextRow();

        builder.addLabel("emailFolder", 1);
        List<String> variabiliEmail = new ArrayList<String>();
        for (String variabile : DmsSettings.EMAIL_VARIABLES) {
            variabiliEmail.add(variabile);
        }
        valueHolder = new ValueHolder(variabiliEmail);
        builder.addBinding(bf.createBoundCodeEditor("emailFolderPattern", valueHolder, true, true, false), 3);
        builder.nextRow();

        builder.addLabel("entitaFolder", 1);
        List<String> variabiliEntita = new ArrayList<String>();
        for (String variabile : DmsSettings.ENTITA_VARIABLES) {
            variabiliEntita.add(variabile);
        }
        valueHolder = new ValueHolder(variabiliEntita);
        builder.addBinding(bf.createBoundCodeEditor("entitaFolderPattern", valueHolder, true, true, false), 3);
        builder.nextRow();

        builder.addPropertyAndLabel("altroFolder");
        builder.nextRow();

        builder.addHorizontalSeparator("Tipi documento", 3);
        builder.nextRow();

        builder.setComponentAttributes("f, t");
        TipoDocumentoDmsSettingsTableModel tableModel = new TipoDocumentoDmsSettingsTableModel();
        TableEditableBinding<TipoDocumentoDmsSettings> tipiDocBinding = new TableEditableBinding<TipoDocumentoDmsSettings>(
                getFormModel(), "tipiDocumentoDmsSettings", Set.class, tableModel);
        tipiDocBinding.getControl().setPreferredSize(new Dimension(300, 200));
        builder.addBinding(tipiDocBinding, 1, 14, 3, 1);

        builder.setComponentAttributes("l, t");
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.add(new JLabel("LEGENDA"), BorderLayout.CENTER);
        titlePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));

        JPanel variablesPanel = getComponentFactory().createPanel(new VerticalLayout(5));
        for (String var : DmsSettings.DOCUMENTI_VARIABLES) {
            StringBuilder sb = new StringBuilder(60);
            sb.append("<html><b>");
            sb.append(var);
            sb.append(":</b> ");
            sb.append(RcpSupport.getMessage(var));
            sb.append("</html>");
            variablesPanel.add(new JLabel(sb.toString()));
        }
        JPanel panel = getComponentFactory().createPanel(new BorderLayout());
        panel.add(titlePanel, BorderLayout.NORTH);
        panel.add(variablesPanel, BorderLayout.CENTER);
        builder.addComponent(panel, 5, 14);

        return builder.getPanel();
    }

}
