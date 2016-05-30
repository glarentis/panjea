package it.eurotn.panjea.rich.factory.table;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.richclient.application.Application;

public class CustomEnumCellRenderer extends AbstractCustomTableCellRenderer {

    private static final long serialVersionUID = 9152198299675095982L;

    @Override
    public String getIconKey(Object value, boolean isSelected, boolean hasFocus) {
        return null;
    }

    @Override
    public String getRendererText(Object value, boolean isSelected, boolean hasFocus) {
        if (value != null) {
            MessageSource messageSource = (MessageSource) Application.services().getService(MessageSource.class);

            if (value instanceof String) {
                String val = (String) value;
                return val;
            } else {
                Enum enum1 = (Enum) value;
                return messageSource.getMessage(enum1.getClass().getName() + "." + enum1.name(), new Object[] {},
                        Locale.getDefault());
            }
        } else {
            return "";
        }
    }
}
