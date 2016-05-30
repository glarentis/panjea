package it.eurotn.panjea.rich.factory.table;

import java.text.Format;
import java.text.SimpleDateFormat;

import com.jidesoft.grid.EditorContext;

public class CustomTableDateCellRenderer extends AbstractCustomTableCellRenderer {

    public static final EditorContext CONTEXT = new EditorContext("data");

    private static final long serialVersionUID = 5259483910333613873L;

    private final Format format;

    public CustomTableDateCellRenderer() {
        this("dd/MM/yyyy");
    }

    public CustomTableDateCellRenderer(String dateFormat) {
        super();
        format = new SimpleDateFormat(dateFormat);
    }

    @Override
    public String getIconKey(Object value, boolean isSelected, boolean hasFocus) {
        return null;
    }

    @Override
    public String getRendererText(Object value, boolean isSelected, boolean hasFocus) {
        String data = null;
        if (value != null) {
            try {
                data = format.format(value);
            } catch (RuntimeException e) {
                data = "Data non valida";
            }
        }
        return data;
    }
}
