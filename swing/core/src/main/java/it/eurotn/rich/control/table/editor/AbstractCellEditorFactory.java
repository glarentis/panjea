package it.eurotn.rich.control.table.editor;

import com.jidesoft.grid.CellEditorFactory;
import com.jidesoft.grid.EditorContext;

public abstract class AbstractCellEditorFactory implements CellEditorFactory {
    private Class<?> type;
    private EditorContext editorContext;

    /**
     * @return Returns the context.
     */
    public EditorContext getEditorContext() {
        return editorContext;
    }

    /**
     * @return Returns the type.
     */
    public Class<?> getType() {
        return type;
    }

    /**
     * @param context
     *            The context to set.
     */
    public void setEditorContext(EditorContext context) {
        this.editorContext = context;
    }

    /**
     * @param type
     *            The type to set.
     */
    public void setType(Class<?> type) {
        this.type = type;
    }
}
