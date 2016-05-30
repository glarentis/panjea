/**
 *
 */
package it.eurotn.rich.binding;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

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
public class JXMLGregorianCalendarBinding extends CustomBinding {

    /*
     * Converts java.util.Date to javax.xml.datatype.XMLGregorianCalendar
     */
    public static XMLGregorianCalendar toXMLGregorianCalendar(Date date) {
        XMLGregorianCalendar xmlCalendar = null;
        try {
            GregorianCalendar gCalendar = new GregorianCalendar();
            gCalendar.setTime(date);
            xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar);
        } catch (DatatypeConfigurationException ex) {
            xmlCalendar = null;
        }
        return xmlCalendar;
    }

    private final JDateChooser chooser;

    private Boolean showOkCancel;

    /**
     *
     * Costruttore.
     *
     * @param chooser
     *            date chooser
     * @param formModel
     *            form model
     * @param formPropertyPath
     *            property path
     */
    public JXMLGregorianCalendarBinding(final JDateChooser chooser, final FormModel formModel,
            final String formPropertyPath) {
        super(formModel, formPropertyPath, XMLGregorianCalendar.class);
        this.chooser = chooser;
    }

    @Override
    protected JComponent doBindControl() {
        chooser.setDate(
                getValue() == null ? null : ((XMLGregorianCalendar) getValue()).toGregorianCalendar().getTime());
        chooser.addPropertyChangeListener("date", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getNewValue() != null) {
                    controlValueChanged(toXMLGregorianCalendar((Date) evt.getNewValue()));
                }
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
        // chooser.setEnabled(isEnabled());
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
        chooser.setDate(((XMLGregorianCalendar) newValue).toGregorianCalendar().getTime());
    }

}