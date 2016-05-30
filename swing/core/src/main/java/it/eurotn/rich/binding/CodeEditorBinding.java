/**
 *
 */
package it.eurotn.rich.binding;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.ColorUIResource;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jdesktop.swingx.VerticalLayout;
import org.springframework.binding.form.FormModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.binding.support.CustomBinding;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.editor.CodeEditor;
import com.jidesoft.editor.ListDataCodeEditorIntelliHints;
import com.jidesoft.editor.tokenmarker.JavaScriptTokenMarker;
import com.jidesoft.popup.JidePopup;

/**
 * Binding per il componente.
 *
 * @author adriano
 * @version 1.0, 21/nov/2008
 */
public class CodeEditorBinding extends CustomBinding {

    private class VariabiliChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            variableBinding.setCompletionList((List<String>) evt.getNewValue());
        }

    }

    private class VariableBinding extends ListDataCodeEditorIntelliHints<String> {

        public VariableBinding(final CodeEditor codeEditor, final List<String> variabili) {
            super(codeEditor, variabili);
        }

        @SuppressWarnings("rawtypes")
        @Override
        protected JList createList() {
            JList list = super.createList();
            list.setName("codeEditorList." + formModel.getId() + "." + formPropertyPath);
            return list;
        }

    }

    private final JPanel panel;
    private final CodeEditor codeEditor;
    private JidePopup legendVariablesPopup;
    private VariableBinding variableBinding;

    /**
     * @param formModel
     *            form model
     * @param formPropertyPath
     *            proprietà
     * @param component
     *            componente
     * @param context
     *            contesto
     */
    public CodeEditorBinding(final FormModel formModel, final String formPropertyPath, final JComponent component,
            final Map<String, Object> context) {
        super(formModel, formPropertyPath, String.class);
        panel = (JPanel) component;
        codeEditor = (CodeEditor) context.get(CodeEditorBinder.EDITCONTROL_ID);
        codeEditor.setName("codeEditor." + formModel.getId() + "." + formPropertyPath);
        codeEditor.getPainter().setName("codeEditorPainter." + formModel.getId() + "." + formPropertyPath);

        applyContext(context);

        codeEditor.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void changedUpdate(DocumentEvent e) {
                documentChanged(e.getDocument());
            }

            private void documentChanged(Document document) {
                String valore;
                try {
                    valore = document.getText(0, document.getLength());
                } catch (BadLocationException e1) {
                    valore = "";
                }

                controlValueChanged(valore);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                documentChanged(e.getDocument());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                documentChanged(e.getDocument());
            }
        });
    }

    private void applyContext(final Map<String, Object> context) {
        ValueHolder valueHolder = (ValueHolder) context.get(CodeEditorBinder.VALUE_HOLDER_VARIABILI);
        valueHolder.addValueChangeListener(new VariabiliChangeListener());
        @SuppressWarnings("unchecked")
        Collection<String> variabiliExtra = (Collection<String>) valueHolder.getValue();

        Set<String> stringSet = new TreeSet<String>(variabiliExtra);
        // se non devo usare solo le variabili configurate nel context, aggiungo anche quelle di default per javascript
        if (!((Boolean) ObjectUtils.defaultIfNull(context.get(CodeEditorBinder.USE_ONLY_HOLDER_VARIABILI), false))) {
            stringSet.addAll(JavaScriptTokenMarker.getKeywords().keyWordSet());
        }

        String[] strings = stringSet.toArray(new String[stringSet.size()]);
        Arrays.sort(strings);
        variableBinding = new VariableBinding(codeEditor, (List<String>) valueHolder.getValue());

        // controllo se devo visualizzare la label della leggenda per le variabili
        Boolean visualizzaLegenda = (Boolean) ObjectUtils
                .defaultIfNull(context.get(CodeEditorBinder.LEGEND_MAP_VARIABLE), false);
        if (visualizzaLegenda) {
            final JLabel labelLegend = getComponentFactory().createLabel("");
            labelLegend.setIcon(RcpSupport.getIcon("propertyCommand.icon"));
            createLegendaPopupMenu(variabiliExtra, labelLegend);
            labelLegend.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    legendVariablesPopup.setOwner(labelLegend);
                    legendVariablesPopup.showPopup();
                }
            });
            panel.add(labelLegend, BorderLayout.EAST);
        }

        // controllo se deve essere multiline o riga singola
        Boolean multiline = (Boolean) ObjectUtils.defaultIfNull(context.get(CodeEditorBinder.MULTILINE_EDITOR), false);
        if (!multiline) {
            // tolgo le scrollbar verticali
            codeEditor.setVerticalScrollBarPolicy(2);
            // sull'enter tolgo il "\n"
            codeEditor.addKeyListener(new KeyAdapter() {

                @Override
                public void keyTyped(KeyEvent e) {
                    if (e.getSource() == codeEditor && codeEditor.isEditable() && e.getKeyChar() == KeyEvent.VK_ENTER) {
                        e.consume();
                        int caretPosition = codeEditor.getCaretPosition();
                        codeEditor.setText(codeEditor.getText().replaceAll("\n", ""));
                        codeEditor.setCaretPosition(codeEditor.getText().length() >= caretPosition - 1
                                ? caretPosition - 1 : codeEditor.getText().length() - 1);
                    } else {
                        super.keyPressed(e);
                    }
                }
            });
        }
    }

    private void createLegendaPopupMenu(Collection<String> variabili, JComponent owner) {
        legendVariablesPopup = new JidePopup();
        legendVariablesPopup.setResizable(false);
        legendVariablesPopup.setMovable(false);
        legendVariablesPopup.setOwner(owner);
        legendVariablesPopup.getContentPane().setLayout(new VerticalLayout(10));
        legendVariablesPopup.setBorder(BorderFactory.createEmptyBorder());

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.add(new JLabel("LEGENDA"), BorderLayout.CENTER);
        titlePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));

        JPanel variablesPanel = getComponentFactory().createPanel(new VerticalLayout(5));
        for (String var : variabili) {
            StringBuilder sb = new StringBuilder(60);
            sb.append("<html><b>");
            sb.append(var);
            sb.append(":</b> ");
            sb.append(RcpSupport.getMessage(var));
            sb.append("</html>");
            variablesPanel.add(new JLabel(sb.toString()));
        }

        legendVariablesPopup.add(titlePanel);
        legendVariablesPopup.add(variablesPanel);
    }

    @Override
    protected JComponent doBindControl() {
        final ValueModel valueModel = getValueModel();
        try {
            String codice = StringUtils.defaultString(((String) valueModel.getValue()));
            codeEditor.setText(codice);
        } catch (ClassCastException e) {
            IllegalArgumentException ex = new IllegalArgumentException("Class cast exception converting '"
                    + getProperty() + "' property value to string - did you install a type converter?");
            ex.initCause(e);
            throw ex;
        }
        return panel;
    }

    @Override
    protected void enabledChanged() {
        codeEditor.setEnabled(isEnabled());
    }

    @Override
    protected void readOnlyChanged() {
        codeEditor.setEditable(!isReadOnly());

        if (isReadOnly()) {
            codeEditor.getPainter().setBackground(((ColorUIResource) UIManager.get("TextField.inactiveBackground")));
        } else {
            codeEditor.getPainter().setBackground(new Color(253, 254, 226));
        }
    }

    @Override
    protected void valueModelChanged(Object newValue) {
        // non faccio nulla dato che installo AsYouTypeTextComponentAdapter
        codeEditor.setText((String) newValue);
    }

}
