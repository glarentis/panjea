/**
 *
 */
package it.eurotn.rich.binding;

import it.eurotn.panjea.rich.factory.PanjeaComponentFactory;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Map;

import javax.swing.JComponent;
import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.binding.Binder;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.support.AbstractBinder;
import org.springframework.util.Assert;

import com.toedter.calendar.IDateEditor;
import com.toedter.calendar.JDateChooser;

/**
 *
 * @author giangi
 * @version 1.0, 25/ott/06
 */
public class JXMLGregorianCalendarBinder extends AbstractBinder implements Binder {

    public static final String SHOW_OK_CANCEL_KEY = "showOkCancel";
    private String dateFormat;
    private String maskFormat;

    {
        dateFormat = "dd/MM/yyyy";
        maskFormat = "dd/MM/yyyy";
    }

    /**
     * Costruttore.
     */
    public JXMLGregorianCalendarBinder() {
        this(new String[] { SHOW_OK_CANCEL_KEY });
    }

    /**
     *
     * Costruttore.
     *
     * @param supportedContextKeys
     *            supportedContextKeys
     */
    public JXMLGregorianCalendarBinder(final String[] supportedContextKeys) {
        super(XMLGregorianCalendar.class, supportedContextKeys);
    }

    @Override
    protected JComponent createControl(Map context) {

        IDateEditor textFieldDateEditor = new PanjeaTextFieldDateEditor(dateFormat, maskFormat, '_');

        if (maskFormat != null && dateFormat != null) {
            final JDateChooser calendar = ((PanjeaComponentFactory) getComponentFactory())
                    .createDateChooser(textFieldDateEditor);
            /*
             * aggiungo un focusListeners per assegnare il focus al componente UI del suo DefaultTextEditor
             */
            calendar.addFocusListener(new FocusListener() {

                @Override
                public void focusGained(FocusEvent arg0) {
                    JComponent component = calendar.getDateEditor().getUiComponent();
                    component.requestFocusInWindow();
                }

                @Override
                public void focusLost(FocusEvent arg0) {
                    // nothing to do
                }

            });
            calendar.cleanup();
            return calendar;

        } else {
            final JDateChooser calendar = ((PanjeaComponentFactory) getComponentFactory()).createDateChooser();
            calendar.cleanup();
            return calendar;
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected Binding doBind(JComponent control, FormModel formModel, String formPropertyPath, Map context) {
        Assert.isTrue(control instanceof JDateChooser, "Control must be an instance of DateField.");
        JXMLGregorianCalendarBinding binding = new JXMLGregorianCalendarBinding((JDateChooser) control, formModel,
                formPropertyPath);
        return binding;
    }

    /**
     * @return Returns the dateFormat.
     */
    public String getDateFormat() {
        return dateFormat;
    }

    /**
     * @return Returns the maskFormat.
     */
    public String getMaskFormat() {
        return this.maskFormat;
    }

    /**
     * @param dateFormat
     *            The dateFormat to set.
     */
    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    /**
     * @param maskFormat
     *            The maskFormat to set.
     */
    public void setMaskFormat(String maskFormat) {
        this.maskFormat = maskFormat;
    }
}
