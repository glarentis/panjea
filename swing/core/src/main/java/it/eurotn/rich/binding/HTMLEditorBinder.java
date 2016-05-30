/**
 *
 */
package it.eurotn.rich.binding;

import java.awt.BorderLayout;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.support.AbstractBinder;

import it.eurotn.rich.components.htmleditor.HTMLEditorPane;

/**
 * Binder di {@link HTMLEditorBinding}.
 *
 * @author adriano
 * @version 1.0, 21/nov/2008
 *
 */
public class HTMLEditorBinder extends AbstractBinder {

    private static Logger logger = Logger.getLogger(HTMLEditorBinder.class);

    public static final String EDITCONTROL_ID = "editControlId";

    /**
     * Costruttore.
     */
    public HTMLEditorBinder() {
        super(String.class, new String[] { EDITCONTROL_ID });
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    protected JComponent createControl(Map context) {
        JPanel htmlPanel = new JPanel(new BorderLayout());
        HTMLEditorPane editor = new HTMLEditorPane();
        context.put(EDITCONTROL_ID, editor);
        htmlPanel.add(editor, BorderLayout.CENTER);
        return htmlPanel;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    protected Binding doBind(JComponent control, FormModel formModel, String formPropertyPath, Map context) {
        logger.debug("--> Enter doBind");
        org.springframework.util.Assert.isInstanceOf(JPanel.class, control, "Control must be an instance of JPanel.");
        HTMLEditorBinding htmlEditorBinding = new HTMLEditorBinding(formModel, formPropertyPath, control, context);
        logger.debug("--> Exit doBind");
        return htmlEditorBinding;
    }
}
