package it.eurotn.rich.components.htmleditor;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class TabToSpaceFilter extends DocumentFilter {

    private static final Logger LOGGER = Logger.getLogger(TabToSpaceFilter.class);

    private int tabSize = 4;

    /**
     * Costruttore.
     */
    public TabToSpaceFilter() {
        super();
    }

    /**
     * Costruttore.
     *
     * @param tabSize
     *            tab size
     */
    public TabToSpaceFilter(final int tabSize) {
        super();
        this.tabSize = tabSize;
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
            throws BadLocationException {

        if ("\t".equals(StringUtils.defaultString(text))) {
            try {
                String softTab = "";
                for (int i = 1; i <= tabSize; ++i) {
                    softTab = softTab + "\u00a0";
                }
                StringBuilder sb = new StringBuilder(text);
                int pos = 0;
                while ((pos = sb.indexOf("\t", pos)) != -1) {
                    sb.replace(pos, pos + 1, softTab);
                    pos += softTab.length();
                }
                text = sb.toString();
            } catch (Exception ex) {
                LOGGER.error("-->errore durante la sostituzione del tab sull'html editor", ex);
            }
        }

        super.replace(fb, offset, length, text, attrs);
    }

}