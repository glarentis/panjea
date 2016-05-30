package it.eurotn.rich.control.table.editor;

import java.awt.Component;
import java.awt.ScrollPane;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.CellEditor;
import javax.swing.InputMap;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.TableCellEditor;

import com.jidesoft.editor.CodeEditor;
import com.jidesoft.editor.ListDataCodeEditorIntelliHints;
import com.jidesoft.editor.language.LanguageSpec;
import com.jidesoft.editor.language.LanguageSpecManager;
import com.jidesoft.editor.tokenmarker.JavaScriptTokenMarker;
import com.jidesoft.grid.ContextSensitiveCellEditor;
import com.jidesoft.grid.EditorContext;

public class CodeEditorContextSensiviteEditorFactory extends AbstractCellEditorFactory {

    public class CodeEditorCellEditor extends ContextSensitiveCellEditor implements TableCellEditor {

        private static final long serialVersionUID = 6972509123533519879L;

        private CodeEditor codeEditor;

        /**
         * Costruttore.
         */
        public CodeEditorCellEditor() {
            super();
            codeEditor = new CodeEditor() {
                private static final long serialVersionUID = 2170065220333098895L;

                @Override
                public void insertBreak() {
                    CodeEditorCellEditor.this.stopCellEditing();
                }

                @Override
                protected boolean processKeyBinding(KeyStroke ks, KeyEvent e, int condition, boolean pressed) {
                    InputMap map = codeEditor.getInputMap(condition);
                    ActionMap am = codeEditor.getActionMap();
                    if (map != null && am != null && isEnabled()) {
                        Object binding = map.get(ks);
                        Action action = (binding == null) ? null : am.get(binding);
                        if (action != null) {
                            return SwingUtilities.notifyAction(action, ks, e, codeEditor, e.getModifiers());
                        }
                    }
                    return false;
                }
            };
            codeEditor.setVirtualSpaceAllowed(false);
            codeEditor.setTokenMarker(new JavaScriptTokenMarker());
            codeEditor.setFont(UIManager.getDefaults().getFont("TabbedPane.font"));
            LanguageSpec languageSpec = LanguageSpecManager.getInstance().getLanguageSpec("JavaScript");
            if (languageSpec != null) {
                languageSpec.configureCodeEditor(codeEditor);
            }
            codeEditor.setHorizontalScrollBarPolicy(ScrollPane.SCROLLBARS_NEVER);
            codeEditor.setVerticalScrollBarPolicy(ScrollPane.SCROLLBARS_NEVER);
            new ListDataCodeEditorIntelliHints<String>(codeEditor, new String[] {});

            codeEditor.setLineNumberVisible(false);
        }

        @Override
        public Object getCellEditorValue() {
            return codeEditor.getText();
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
                int column) {
            codeEditor.setText((String) value);

            String[] variables = new String[] {};
            if (getEditorContext() != null) {
                Object userObj = getEditorContext().getUserObject();
                if (userObj != null) {
                    variables = (String[]) userObj;
                }
            }
            new ListDataCodeEditorIntelliHints<String>(codeEditor, variables);

            return codeEditor;
        }
    }

    public static final EditorContext CONTEXT = new EditorContext("codeEditorCellEditor");

    @Override
    public CellEditor create() {
        return new CodeEditorCellEditor();
    }

}
