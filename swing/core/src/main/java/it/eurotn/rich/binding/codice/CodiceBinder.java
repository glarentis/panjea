package it.eurotn.rich.binding.codice;

import java.awt.BorderLayout;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.support.AbstractBinder;

import it.eurotn.panjea.anagrafica.documenti.domain.CodiceDocumento;

/**
 * @author Aracno
 * @version 1.0, 06/ott/06
 */
public class CodiceBinder extends AbstractBinder {

    public class CodicePanel extends JPanel {

        private static final long serialVersionUID = 5898506835393717421L;
        private JTextField txtCodice = null;
        private JButton button = null;

        /**
         *
         * Costruttore.
         */
        public CodicePanel() {
            super(new BorderLayout());
            initControl();
        }

        /**
         * @return JButton
         */
        public JButton getButton() {
            return button;
        }

        /**
         * @return TextField codice
         */
        public JTextField getTextFieldCodice() {
            return txtCodice;
        }

        private void initControl() {

            txtCodice = getComponentFactory().createTextField();

            txtCodice.setColumns(10);

            add(txtCodice, BorderLayout.CENTER);

            button = getComponentFactory().createButton("");
            button.setFocusable(false);
            button.setVisible(false);

            JPanel panelButton = getComponentFactory().createPanel(new BorderLayout());
            panelButton.add(button, BorderLayout.WEST);

            add(button, BorderLayout.EAST);
        }

    }

    public static final String PROTOCOLLOFORMPROPERTY_KEY = "protocolloPropertyPath";
    public static final String VALOREPROTOCOLLO_FORMPROPERTY_KEY = "valoreProtocolloPropertyPath";
    public static final String PATTERN_FORMPROPERTY_KEY = "patternPropertyPath";
    public static final String EDIT_ENABLE = "editEnable";
    public static final String REQUIRED = "required";

    public static final String ADDITIONAL_CONSTRAINT = "additionalConstraints";

    /**
     * Costruttore.
     */
    public CodiceBinder() {
        this(CodiceDocumento.class);
    }

    /**
     * @param requiredSourceClass
     *            requiredSourceClass
     */
    @SuppressWarnings("rawtypes")
    public CodiceBinder(final Class requiredSourceClass) {
        this(requiredSourceClass, new String[] { PROTOCOLLOFORMPROPERTY_KEY, VALOREPROTOCOLLO_FORMPROPERTY_KEY,
                PATTERN_FORMPROPERTY_KEY, ADDITIONAL_CONSTRAINT, EDIT_ENABLE, REQUIRED });
    }

    /**
     * @param requiredSourceClass
     *            requiredSourceClass
     * @param supportedContextKeys
     *            supportedContextKeys
     */
    @SuppressWarnings("rawtypes")
    public CodiceBinder(final Class requiredSourceClass, final String[] supportedContextKeys) {
        super(requiredSourceClass, supportedContextKeys);
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected JComponent createControl(Map context) {
        return new CodicePanel();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    protected Binding doBind(JComponent control, FormModel formModel, final String formPropertyPath, Map context) {
        org.springframework.util.Assert.isTrue(control instanceof JPanel, "Control must be an instance of JPanel.");
        return new CodiceBinding((JPanel) control, formModel, formPropertyPath, context);
    }

}
