/**
 * 
 */
package it.eurotn.rich.binding;

import java.util.Map;

import javax.swing.JComponent;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.components.BigDecimalTextField;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.swing.NumberBinder;
import org.springframework.util.Assert;

/**
 * 
 * @author fattazzo
 * @version 1.0, 26/apr/07
 * 
 */
public class PercentageNumberBinder extends NumberBinder {

    @Override
    protected Binding doBind(JComponent control, FormModel formModel, String formPropertyPath, Map context) {
        Assert.isTrue(control instanceof BigDecimalTextField, "Control must be an instance of BigDecimalTextField.");
        // istanzio il PercentageNumberBinding
        return new PercentageNumberBinding(getRequiredSourceClass(), (BigDecimalTextField) control, readOnly,
                this.leftDecoration, this.rightDecoration, this.shiftFactor, this.nrOfDecimals + this.nrOfNonDecimals,
                formModel, formPropertyPath, this.nrOfDecimals, this.nrOfNonDecimals);
    }

}
