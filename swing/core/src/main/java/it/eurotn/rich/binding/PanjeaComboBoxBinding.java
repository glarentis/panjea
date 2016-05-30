package it.eurotn.rich.binding;

import java.awt.Color;

import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;

import org.springframework.binding.form.FormModel;

public class PanjeaComboBoxBinding extends org.springframework.richclient.form.binding.swing.ComboBoxBinding {

    /**
     * Costruttore.
     *
     * @param formModel
     * @param formPropertyPath
     */
    public PanjeaComboBoxBinding(FormModel formModel, String formPropertyPath) {
        super(formModel, formPropertyPath);
    }

    /**
     *
     * Costruttore.
     *
     * @param comboBox
     * @param formModel
     * @param formPropertyPath
     */
    public PanjeaComboBoxBinding(JComboBox comboBox, FormModel formModel, String formPropertyPath) {
        super(comboBox, formModel, formPropertyPath);
    }

    /**
     * Costruttore.
     *
     * @param comboBox
     * @param formModel
     * @param formPropertyPath
     * @param requiredSourceClass
     */
    public PanjeaComboBoxBinding(JComboBox comboBox, FormModel formModel, String formPropertyPath,
            Class requiredSourceClass) {
        super(comboBox, formModel, formPropertyPath, requiredSourceClass);
    }

    @Override
    protected void doBindControl(ListModel bindingModel) {
        super.doBindControl(bindingModel);
        ((JTextField) getComboBox().getEditor().getEditorComponent()).setDisabledTextColor(Color.black);
    }

    @Override
    protected void enabledChanged() {
        super.enabledChanged();

        ColorUIResource color;
        if (!UIManager.getLookAndFeel().getName().equals("Nimbus")) {
            if (!isReadOnly()) {
                color = ((ColorUIResource) UIManager.get("TextField.background"));
            } else {
                color = ((ColorUIResource) UIManager.get("TextField.inactiveBackground"));
            }
            getComboBox().getEditor().getEditorComponent().setBackground(color);
        }
    }

}
