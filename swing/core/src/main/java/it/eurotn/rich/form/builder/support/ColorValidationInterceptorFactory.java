/**
 * 
 */
package it.eurotn.rich.form.builder.support;

import java.awt.*;

import javax.swing.*;
import javax.swing.text.JTextComponent;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.core.Guarded;
import org.springframework.richclient.form.builder.*;
import org.springframework.richclient.form.builder.support.ValidationInterceptor;
import org.springframework.util.Assert;

/**
 * Copia brutale della classe
 * <code>org.springframework.richclient.form.builder.support.ColorValidationInterceptorFactory</code> che si limita ad
 * allargare l'area di interesse dell'interceptor anche al componente <code>SearchTextField</code>
 * 
 * @author adriano
 * @version 1.0, 07/dic/06
 * 
 */
public class ColorValidationInterceptorFactory implements FormComponentInterceptorFactory {

    private static final Color DEFAULT_ERROR_COLOR = new Color(255, 240, 240);

    private Color errorColor = DEFAULT_ERROR_COLOR;

    /**
     * 
     */
    public ColorValidationInterceptorFactory() {
    }

    public void setErrorColor(Color errorColor) {
        Assert.notNull(errorColor);
        this.errorColor = errorColor;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.richclient.form.builder.FormComponentInterceptorFactory#getInterceptor(org.springframework
     * .binding.form.FormModel)
     */
    @Override
    public FormComponentInterceptor getInterceptor(FormModel formModel) {
        return new ColorValidationInterceptor(formModel);
    }

    private class ColorValidationInterceptor extends ValidationInterceptor {

        public ColorValidationInterceptor(FormModel formModel) {
            super(formModel);
        }

        /**
         * 
         */
        @Override
        public void processComponent(String propertyName, JComponent component) {
            JComponent innerComp = getInnerComponent(component);
            if (innerComp instanceof JTextComponent) {
                ColorChanger colorChanger = new ColorChanger(component);
                registerGuarded(propertyName, colorChanger);
            } else if (innerComp instanceof JPanel) {
                /* processa ulteriormente il componente contenuto per verificare che si tratti di JTextField */
                JPanel panel = (JPanel) innerComp;
                for (Component componentInPanel : panel.getComponents()) {
                    if (componentInPanel instanceof JTextField) {
                        JTextField textField = (JTextField) componentInPanel;
                        ColorChanger colorChanger = new ColorChanger(textField);
                        registerGuarded(propertyName, colorChanger);

                    }
                }
            }
        }

    }

    private class ColorChanger implements Guarded {
        private Color normalColor;

        private JComponent component;

        public ColorChanger(JComponent component) {
            this.component = component;
            this.normalColor = component.getBackground();
        }

        @Override
        public boolean isEnabled() {
            return false;
        }

        @Override
        public void setEnabled(boolean enabled) {
            component.setBackground(enabled ? normalColor : errorColor);
        }
    }

}
