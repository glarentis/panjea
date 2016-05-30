/**
 *
 */
package it.eurotn.rich.binding;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.binding.support.CustomBinding;

import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;

/**
 *
 * @author giangi
 * @version 1.0, 25/ott/06
 *
 */
public class JCalendarBinding extends CustomBinding {

    private final JDateChooser chooser;

    private Boolean showOkCancel;

    public JCalendarBinding(JDateChooser chooser, FormModel formModel, String formPropertyPath) {
        super(formModel, formPropertyPath, Date.class);
        this.chooser = chooser;
    }

    @Override
    protected JComponent doBindControl() {
        chooser.setDate((Date) getValue());
        chooser.addPropertyChangeListener("date", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                controlValueChanged(evt.getNewValue());
            }
        });
        // TODO: implement ValueCommitPolicies (copied from
        // TextComponentBinding)
        // new AsYouTypeTextComponentAdapter(textComponent, valueModel);
        chooser.getDateEditor().getUiComponent().setName(formModel.getId() + "." + formPropertyPath);
        return chooser;
    }

    @Override
    protected void enabledChanged() {
        chooser.setEnabled(isEnabled());
        chooser.getCalendarButton().setEnabled(!isReadOnly());
    }

    public Boolean getShowOkCancel() {
        return showOkCancel;
    }

    @Override
    protected void readOnlyChanged() {
        // ATTENZIONE: casto il dateEditor a JTextFieldDateEditor per
        // prestazioni ( altrimenti dovrei usare il metodo
        // getUiComponent e ciclase finchè non trovo il componente che gestisce
        // la data ). Se viene implementato
        // un nuovo JDateChooser il cast potrebbe sollevare una eccezione.
        ((JTextFieldDateEditor) chooser.getDateEditor()).setEditable(!isReadOnly());
        ColorUIResource color;
        // Hack per Nimbus

        if (!UIManager.getLookAndFeel().getName().equals("Nimbus")) {
            if (!isReadOnly()) {
                color = ((ColorUIResource) UIManager.get("TextField.background"));
            } else {
                color = ((ColorUIResource) UIManager.get("TextField.inactiveBackground"));
            }
            ((JTextFieldDateEditor) chooser.getDateEditor()).setBackground(color);
        }
        chooser.getCalendarButton().setEnabled(!isReadOnly());
    }

    public void setShowOkCancel(Boolean showOkCancel) {
        this.showOkCancel = showOkCancel;
    }

    @Override
    protected void valueModelChanged(Object newValue) {
        chooser.setDate((Date) newValue);
    }

}