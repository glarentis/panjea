/**
 *
 */
package it.eurotn.rich.form.binding.swing;

import java.awt.Color;
import java.awt.TextComponent;
import java.math.BigDecimal;
import java.util.Set;

import javax.swing.JFormattedTextField;

import org.springframework.richclient.form.binding.swing.ListBinder;
import org.springframework.richclient.form.binding.swing.NumberBinder;
import org.springframework.richclient.form.binding.swing.SwingBinderSelectionStrategy;
import org.springframework.richclient.form.binding.swing.TextComponentBinder;

import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.rich.binding.ColorComboBoxBinder;
import it.eurotn.rich.binding.FormattedTextFieldBinder;
import it.eurotn.rich.binding.ImportoBinder;
import it.eurotn.rich.binding.PanjeaEnumComboBoxBinder;

/**
 * @author Adry
 * @version 1.0, 26-mag-2006
 *
 */
public class PanjeaSwingBinderSelectionStrategy extends SwingBinderSelectionStrategy {

    /**
     *
     */
    public PanjeaSwingBinderSelectionStrategy() {
        super();
    }

    @Override
    protected void registerDefaultBinders() {
        super.registerDefaultBinders();
        TextComponentBinder textComponentBinder = new TextComponentBinder();
        textComponentBinder.setSelectAllOnFocus(false);
        registerBinderForPropertyType(String.class, textComponentBinder);

        registerBinderForControlType(TextComponent.class, textComponentBinder);

        FormattedTextFieldBinder formattedTextFieldBinder = new FormattedTextFieldBinder(BigDecimal.class);
        registerBinderForPropertyType(BigDecimal.class, formattedTextFieldBinder);

        NumberBinder numberBinder = new NumberBinder(Integer.class);
        numberBinder.setNrOfDecimals(0);
        numberBinder.setFormat("#");
        numberBinder.setUnformat("#");
        registerBinderForPropertyType(Integer.class, numberBinder);

        registerBinderForControlType(JFormattedTextField.class, new FormattedTextFieldBinder(null));

        ImportoBinder importoBinder = new ImportoBinder();
        registerBinderForPropertyType(Importo.class, importoBinder);

        registerBinderForPropertyType(Color.class, new ColorComboBoxBinder());
        registerBinderForPropertyType(Enum.class, new PanjeaEnumComboBoxBinder());
        registerBinderForPropertyType(Set.class, new ListBinder());
    }
}
