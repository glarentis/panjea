/**
 *
 */
package it.eurotn.rich.binding;

import java.awt.BorderLayout;
import java.awt.KeyboardFocusManager;
import java.awt.ScrollPane;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.UIManager;

import org.apache.log4j.Logger;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.support.AbstractBinder;

import com.jidesoft.editor.CodeEditor;
import com.jidesoft.editor.DefaultSettings;
import com.jidesoft.editor.language.LanguageSpec;
import com.jidesoft.editor.language.LanguageSpecManager;
import com.jidesoft.editor.tokenmarker.JavaScriptTokenMarker;

/**
 * Binder di {@link HTMLEditorBinding}.
 *
 * @author adriano
 * @version 1.0, 21/nov/2008
 *
 */
public class CodeEditorBinder extends AbstractBinder {

    private static Logger logger = Logger.getLogger(CodeEditorBinder.class);

    public static final String EDITCONTROL_ID = "editControlId";

    public static final String VALUE_HOLDER_VARIABILI = "valueHolderVariabili";
    public static final String USE_ONLY_HOLDER_VARIABILI = "useOnlyvalueHolderVariabili";
    public static final String LEGEND_MAP_VARIABLE = "legendMapVariable";
    public static final String MULTILINE_EDITOR = "multilineEditor";

    /**
     * Costruttore.
     */
    public CodeEditorBinder() {
        super(String.class, new String[] { EDITCONTROL_ID, VALUE_HOLDER_VARIABILI, USE_ONLY_HOLDER_VARIABILI,
                LEGEND_MAP_VARIABLE, MULTILINE_EDITOR });
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    protected JComponent createControl(Map context) {
        JPanel codeEditorPanel = new JPanel(new BorderLayout());
        DefaultSettings defaultSettings = DefaultSettings.getDefaults();
        defaultSettings.setFont(codeEditorPanel.getFont());
        final CodeEditor codeEditor = new CodeEditor(defaultSettings);

        codeEditor.setHorizontalScrollBarPolicy(ScrollPane.SCROLLBARS_NEVER);
        codeEditor.setVerticalScrollBarPolicy(ScrollPane.SCROLLBARS_AS_NEEDED);
        codeEditor.setVirtualSpaceAllowed(false);
        codeEditor.setLineNumberVisible(false);
        codeEditor.setTabSize(4);
        codeEditor.setReplaceTabWithSpace(false);
        // setto la linea selezionata di colore bianco
        codeEditor.setLineHighlight(true);
        codeEditor.setLineHighlightColor(UIManager.getColor("TextField.background"));

        codeEditor.setTokenMarker(new JavaScriptTokenMarker());
        LanguageSpec languageSpec = LanguageSpecManager.getInstance().getLanguageSpec("JavaScript");
        if (languageSpec != null) {
            languageSpec.configureCodeEditor(codeEditor);
        }

        codeEditor.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getSource() == codeEditor && e.getKeyChar() == KeyEvent.VK_TAB) {
                    e.consume();
                    if (codeEditor.isEditable()) {
                        int caretPosition = codeEditor.getCaretPosition();
                        codeEditor.setText(codeEditor.getText().replaceAll("\t", ""));
                        codeEditor.setCaretPosition(codeEditor.getText().length() >= caretPosition - 1
                                ? caretPosition - 1 : codeEditor.getText().length() - 1);
                    }
                    if (e.isShiftDown()) {
                        KeyboardFocusManager.getCurrentKeyboardFocusManager().focusPreviousComponent();
                    } else {
                        KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent();
                    }
                } else {
                    super.keyPressed(e);
                }
            }
        });

        context.put(EDITCONTROL_ID, codeEditor);
        codeEditorPanel.add(codeEditor, BorderLayout.CENTER);
        return codeEditorPanel;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    protected Binding doBind(JComponent control, FormModel formModel, String formPropertyPath, Map context) {
        logger.debug("--> Enter doBind");
        org.springframework.util.Assert.isInstanceOf(JPanel.class, control, "Control must be an instance of JPanel.");
        CodeEditorBinding codeEditorBinding = new CodeEditorBinding(formModel, formPropertyPath, control, context);
        logger.debug("--> Exit doBind");
        return codeEditorBinding;
    }
}
