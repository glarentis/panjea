/**
 * 
 */
package it.eurotn.rich.text;

import java.awt.event.*;
import java.util.List;

import javax.swing.*;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.builder.*;
import org.springframework.richclient.form.builder.support.AbstractFormComponentInterceptor;

/**
 * 
 * 
 * @author fattazzo
 * @version 1.0, 02/gen/08
 * 
 */
public class ChangeFormatOnEditingTextComponentInterceptorFactory implements FormComponentInterceptorFactory {

    private List<java.lang.Class> listClassToChange;

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.richclient.form.builder.FormComponentInterceptorFactory#getInterceptor(org.springframework
     * .binding.form.FormModel)
     */
    @Override
    public FormComponentInterceptor getInterceptor(FormModel formModel) {
        return new ChangeFormatOnEditingTextFormComponentInterceptor(formModel, listClassToChange);
    }

    public void setListClassToChange(List<Class> listClassToChange) {
        this.listClassToChange = listClassToChange;
    }

    public class ChangeFormatOnEditingTextFormComponentInterceptor extends AbstractFormComponentInterceptor {

        private List<Class> listClassToChange;

        public ChangeFormatOnEditingTextFormComponentInterceptor(FormModel formModel, List<Class> listClassToChange) {
            super(formModel);
            this.listClassToChange = listClassToChange;
        }

        @Override
        public void processComponent(final String propertyName, JComponent component) {
            final JComponent component2 = getInnerComponent(component);

            Class propertyClass = getFormModel().getFieldMetadata(propertyName).getPropertyType();
            if (listClassToChange.contains(propertyClass)) {
                component2.addKeyListener(new ComponentKeyListener());
            }
        }
    }

    private class ComponentKeyListener implements KeyListener {

        @Override
        public void keyPressed(KeyEvent arg0) {

        }

        @Override
        public void keyReleased(KeyEvent arg0) {

        }

        @Override
        public void keyTyped(KeyEvent event) {
            if (event.getKeyChar() == '.') {
                event.setKeyChar(',');
            }
        }

    }
}
