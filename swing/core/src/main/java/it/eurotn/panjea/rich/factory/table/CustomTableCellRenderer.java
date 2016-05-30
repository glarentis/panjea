package it.eurotn.panjea.rich.factory.table;

public class CustomTableCellRenderer extends AbstractCustomTableCellRenderer {

    private static final long serialVersionUID = -8121922857685767078L;

    public CustomTableCellRenderer() {
        super();
    }

    public CustomTableCellRenderer(int horizontalAlignment) {
        super(horizontalAlignment);
    }

    @Override
    public String getIconKey(Object value, boolean isSelected, boolean hasFocus) {
        return null;
    }

    @Override
    public String getRendererText(Object value, boolean isSelected, boolean hasFocus) {
        if (value != null) {
            return value.toString();
        } else {
            return "";
        }
    }

}
