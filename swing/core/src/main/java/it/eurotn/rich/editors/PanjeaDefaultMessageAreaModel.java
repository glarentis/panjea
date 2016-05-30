package it.eurotn.rich.editors;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.text.JTextComponent;

import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.dialog.DefaultMessageAreaModel;
import org.springframework.richclient.dialog.Messagable;
import org.springframework.richclient.image.EmptyIcon;

public class PanjeaDefaultMessageAreaModel extends DefaultMessageAreaModel {

    /**
     * Costruttore.
     *
     * @param delegate
     *            messagable
     */
    public PanjeaDefaultMessageAreaModel(final Messagable delegate) {
        super(delegate);
    }

    @Override
    public void renderMessage(JComponent component) {

        if (component instanceof JTextComponent) {
            ((JTextComponent) component).setText(this.getMessage().getMessage());
        } else if (component instanceof JLabel) {
            JLabel label = (JLabel) component;
            label.setText(this.getMessage().getMessage());
            if (((DefaultMessage) this.getMessage()).getIcon() == null) {
                label.setIcon(EmptyIcon.SMALL);
            } else {
                label.setIcon(((DefaultMessage) this.getMessage()).getIcon());
            }
        } else {
            throw new IllegalArgumentException(
                    (new StringBuilder()).append("Unsupported component type ").append(component).toString());
        }
    }
}
