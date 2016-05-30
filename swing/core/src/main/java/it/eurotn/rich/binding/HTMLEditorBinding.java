/**
 *
 */
package it.eurotn.rich.binding;

import it.eurotn.rich.components.htmleditor.HTMLEditorPane;

import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.springframework.binding.form.FormModel;
import org.springframework.binding.value.swing.AsYouTypeTextComponentAdapter;
import org.springframework.richclient.form.binding.support.CustomBinding;

/**
 * Binding per il componente.
 * 
 * @author adriano
 * @version 1.0, 21/nov/2008
 */
public class HTMLEditorBinding extends CustomBinding {

    private static Logger logger = Logger.getLogger(HTMLEditorBinding.class);

    private final JPanel panel;
    private final HTMLEditorPane editor;

    /**
     * Costruttore.
     * 
     * @param formModel
     *            form model
     * @param formPropertyPath
     *            formPropertyPath
     * @param component
     *            componente del binding
     * @param context
     *            contesto
     */
    public HTMLEditorBinding(final FormModel formModel, final String formPropertyPath, final JComponent component,
            final Map<String, Object> context) {
        super(formModel, formPropertyPath, String.class);
        panel = (JPanel) component;
        editor = (HTMLEditorPane) context.get(HTMLEditorBinder.EDITCONTROL_ID);
        editor.getWysEditor().setName(formModel.getId() + "." + formPropertyPath);
    }

    @Override
    protected JComponent doBindControl() {
        logger.debug("--> Enter doBindControl");
        new AsYouTypeTextComponentAdapter(editor.getWysEditor(), getValueModel());
        logger.debug("--> Exit doBindControl");
        return panel;
    }

    @Override
    protected void enabledChanged() {
        editor.setEnabled(isEnabled());
    }

    @Override
    protected void readOnlyChanged() {
        editor.setReadOnly(isReadOnly());
    }

    @Override
    protected void valueModelChanged(Object newValue) {
        // non faccio nulla dato che installo AsYouTypeTextComponentAdapter
    }

}
