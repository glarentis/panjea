/**
 * 
 */
package it.eurotn.rich.binding;

import java.awt.BorderLayout;
import java.math.BigDecimal;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.components.BigDecimalTextField;
import org.springframework.richclient.form.binding.swing.NumberBinding;

/**
 * 
 * @author fattazzo
 * @version 1.0, 26/apr/07
 * 
 */
public class PercentageNumberBinding extends NumberBinding {

    private final String leftDecoration;

    private final String rightDecoration;

    /**
     * Costruttore.
     * 
     * @param requiredClass
     *            classe del binding
     * @param component
     *            componente di editazione
     * @param readOnly
     *            readonly
     * @param leftDecoration
     *            decorazione posta a sinistra del componente di editazione
     * @param rightDecoration
     *            decorazione posta a destra del componente di editazione
     * @param shiftFactor
     *            shiftFactor
     * @param shiftScale
     *            shiftScale
     * @param formModel
     *            formModel
     * @param formPropertyPath
     *            formPropertyPath
     * @param nrOfDecimals
     *            numero di decimali
     * @param nrOfNonDecimals
     *            numero interi
     */
    public PercentageNumberBinding(final Class<?> requiredClass, final BigDecimalTextField component,
            final boolean readOnly, final String leftDecoration, final String rightDecoration,
            final BigDecimal shiftFactor, final int shiftScale, final FormModel formModel,
            final String formPropertyPath, final int nrOfDecimals, final int nrOfNonDecimals) {
        super(requiredClass, component, readOnly, leftDecoration, rightDecoration, shiftFactor, shiftScale, formModel,
                formPropertyPath);
        this.leftDecoration = leftDecoration;
        this.rightDecoration = rightDecoration;
    }

    /**
     * 
     * 
     * @return a decorated component which contains the inputfield.
     */
    private JComponent createPanelWithDecoration() {
        JPanel panel = new JPanel(new BorderLayout()) {
            private static final long serialVersionUID = 7406107795457095448L;

            @Override
            public void requestFocus() {
                PercentageNumberBinding.this.numberField.requestFocus();
            }

        };
        if (this.leftDecoration != null) {
            panel.add(new JLabel(this.leftDecoration), BorderLayout.WEST);
        }

        numberField.setColumns(getColumsNumberField());
        numberField.setName(formPropertyPath);
        panel.add(this.numberField, BorderLayout.CENTER);

        if (this.rightDecoration != null) {
            JLabel rightDecorationLabel = new JLabel(this.rightDecoration);
            rightDecorationLabel.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));
            panel.add(rightDecorationLabel, BorderLayout.EAST);
        }
        return panel;
    }

    /**
     * Sovrascrivo il metodo perchè nel caso siano presenti delle decoration creo un pannello con un BorderLayout
     * anzichè un FormLayout per far si che se non è stato inserito alcun valore il numberField non assuma larghezza 0 e
     * setto la sua larghezza in base alle cifre decimali e non decimali del valore.
     * 
     * @return controlli creati
     * 
     */
    @Override
    protected JComponent doBindControl() {
        valueModelChanged(getValue());
        this.numberField.addUserInputListener(this);
        if ((this.leftDecoration == null) && (this.rightDecoration == null)) {
            return this.numberField;
        }

        return createPanelWithDecoration();
    }

    /**
     * Colonne del componente di editazione. Di default sono 5 ( 1 per il segno e 5 per la percentuale ).
     * 
     * @return numero colonne
     */
    private int getColumsNumberField() {
        return 5;
    }

}
