package it.eurotn.panjea.rich.factory;

import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;

import javax.swing.SwingConstants;

import org.springframework.richclient.components.PatchedJFormattedTextField;

import it.eurotn.panjea.util.DefaultNumberFormatterFactory;

public class PanjeaPatchedJFormattedTextField extends PatchedJFormattedTextField {
    private static final long serialVersionUID = -7268516307241015192L;

    /**
     * determina se è in corso il setter del text di TextField.
     */
    private boolean settingText = false;

    private boolean allowNullValue;

    {
        this.allowNullValue = false;
    }

    /**
     *
     * Costruttore.
     *
     */
    public PanjeaPatchedJFormattedTextField() {
        this(null);
        setHorizontalAlignment(SwingConstants.RIGHT);
    }

    /**
     * Costruttore.
     *
     * @param formatterFactory
     *            factory per il formato
     */
    public PanjeaPatchedJFormattedTextField(final AbstractFormatterFactory formatterFactory) {
        super(formatterFactory);
        setHorizontalAlignment(SwingConstants.RIGHT);
    }

    /**
     * @return Returns the settingText.
     */
    public boolean isSettingText() {
        return settingText;
    }

    @Override
    protected void processFocusEvent(FocusEvent e) {
        super.processFocusEvent(e);
        if (e.getID() == FocusEvent.FOCUS_GAINED) {
            selectAll();
        }
    }

    @Override
    protected void processKeyEvent(KeyEvent keyevent) {
        switch (keyevent.getKeyChar()) {
        case KeyEvent.VK_PERIOD:
            keyevent.setKeyCode(44);
            keyevent.setKeyChar(',');
            break;
        case KeyEvent.VK_DELETE:
        case KeyEvent.VK_BACK_SPACE:
            if (allowNullValue && getSelectedText() != null && getSelectedText().equals(getText())) {
                setValue(null);
            }
        default:
            break;
        }
        super.processKeyEvent(keyevent);
    }

    @Override
    public void setEditable(boolean b) {
        super.setEditable(b);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }

    @Override
    public void setFormatterFactory(AbstractFormatterFactory tf) {
        super.setFormatterFactory(tf);
        if (tf instanceof DefaultNumberFormatterFactory) {
            this.allowNullValue = ((DefaultNumberFormatterFactory) tf).isAllowNullValue();
        }
    }

    @Override
    public void setText(String text) {
        try {
            settingText = true;
            super.setText(text);
        } finally {
            settingText = false;
        }

    }

}