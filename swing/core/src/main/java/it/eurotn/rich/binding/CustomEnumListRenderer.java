/**
 * 
 */
package it.eurotn.rich.binding;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.richclient.list.TextValueListRenderer;

/**
 * @author Leonardo
 */
public class CustomEnumListRenderer extends TextValueListRenderer {

    private static final long serialVersionUID = -602299055033209527L;

    private MessageSourceAccessor messageSourceAccessor;

    public CustomEnumListRenderer(MessageSourceAccessor messageSourceAccessor) {
        this.messageSourceAccessor = messageSourceAccessor;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected String getTextValue(Object value) {
        if (value == null) {
            return "";
        }
        Enum valueEnum = (Enum) value;
        Class<? extends Enum> valueClass = valueEnum.getClass();
        return messageSourceAccessor.getMessage(valueClass.getName() + "." + valueEnum.name());
    }
}