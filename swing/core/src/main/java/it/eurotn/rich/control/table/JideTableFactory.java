package it.eurotn.rich.control.table;

import it.eurotn.rich.control.table.editor.AbstractCellEditorFactory;

import java.util.List;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.factory.TableFactory;

import com.jidesoft.grid.CellEditorManager;
import com.jidesoft.grid.CellRendererManager;
import com.jidesoft.grid.ContextSensitiveCellRenderer;
import com.jidesoft.grid.EditorContextSupport;
import com.jidesoft.grid.HyperlinkTableCellEditorRenderer;
import com.jidesoft.grid.JideTable;

public class JideTableFactory implements TableFactory, InitializingBean {

    private List<AbstractCellEditorFactory> editors;
    private List<TableCellRenderer> renderers;

    @SuppressWarnings("deprecation")
    @Override
    public void afterPropertiesSet() throws Exception {

        for (TableCellRenderer cellRenderer : renderers) {

            Class<?> type = null;
            if (cellRenderer instanceof HyperlinkTableCellEditorRenderer) {
                type = ((HyperlinkTableCellEditorRenderer) cellRenderer).getType();

                // registro alche l'editor se è un'hyperlink
                if (((EditorContextSupport) cellRenderer).getEditorContext() != null) {
                    CellEditorManager.registerEditor(type, (HyperlinkTableCellEditorRenderer) cellRenderer,
                            ((EditorContextSupport) cellRenderer).getEditorContext());
                } else {
                    CellEditorManager.registerEditor(type, (HyperlinkTableCellEditorRenderer) cellRenderer, null);
                }
            } else if (cellRenderer instanceof ContextSensitiveCellRenderer) {
                type = ((ContextSensitiveCellRenderer) cellRenderer).getType();
            }

            if (type != null) {
                // registro il renderer
                if (cellRenderer instanceof EditorContextSupport
                        && ((EditorContextSupport) cellRenderer).getEditorContext() != null) {
                    CellRendererManager.registerRenderer(type, cellRenderer,
                            ((EditorContextSupport) cellRenderer).getEditorContext());
                } else {
                    CellRendererManager.registerRenderer(type, cellRenderer, null);
                }
            }
        }

        for (AbstractCellEditorFactory editor : editors) {
            if (editor.getEditorContext() != null) {
                CellEditorManager.registerEditor(editor.getType(), editor, editor.getEditorContext());
            } else {
                CellEditorManager.registerEditor(editor.getType(), editor, null);
            }
        }
    }

    @Override
    public JTable createTable() {
        return new JideTable();
    }

    @Override
    public JTable createTable(TableModel arg0) {
        return new JideTable(arg0);
    }

    /**
     * @param editors
     *            the editors to set
     */
    public void setEditors(List<AbstractCellEditorFactory> editors) {
        this.editors = editors;
    }

    /**
     * @param renderers
     *            the renderers to set
     */
    public void setRenderers(List<TableCellRenderer> renderers) {
        this.renderers = renderers;
    }
}
