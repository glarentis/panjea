package it.eurotn.rich.editors;

import java.awt.BorderLayout;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.springframework.richclient.core.Message;
import org.springframework.richclient.dialog.DefaultMessageAreaModel;
import org.springframework.richclient.dialog.Messagable;
import org.springframework.richclient.factory.AbstractControlFactory;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.swing.PartialLineBorder;

/**
 * Pannello Messagable per visualizzare i messaggi di validazione a diversi livelli di debug e l'icona di stato bloccato
 * dell'oggetto con cui si sta lavorando
 *
 * @author Leonardo
 */
public class FormBackedMessagablePanel extends AbstractControlFactory implements Messagable, PropertyChangeListener {

    private static final String LOCKED_ICON = "locked.icon";
    private static final String RELEASED_ICON = "released.icon";
    private DefaultMessageAreaModel messageAreaModel = null;
    private JLabel messageLabel = null;
    private JLabel lockIcon = null;

    /**
     * Costruttore.
     *
     */
    public FormBackedMessagablePanel() {
        init(this);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        messageAreaModel.addPropertyChangeListener(listener);
    }

    @Override
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        messageAreaModel.addPropertyChangeListener(propertyName, listener);
    }

    /**
     * Crea un pannello che contiene una icona (stato di lock) e una label per la visualizzazione dei messaggi.
     */
    @Override
    protected JComponent createControl() {
        JPanel panel = getComponentFactory().createPanel(new BorderLayout());
        PartialLineBorder partialLineBorder = new PartialLineBorder(Color.GRAY, 1, SwingConstants.NORTH);
        panel.setBorder(partialLineBorder);

        if (messageLabel == null) {
            this.messageLabel = getComponentFactory().createLabel("");
            this.lockIcon = new JLabel(getIconSource().getIcon(RELEASED_ICON));
            this.messageAreaModel.renderMessage(messageLabel);
        }

        messageLabel.setOpaque(false);
        lockIcon.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        messageLabel.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 3));
        panel.add(lockIcon, BorderLayout.LINE_START);
        panel.add(messageLabel, BorderLayout.CENTER);
        return panel;
    }

    private void init(Messagable delegateFor) {
        this.messageAreaModel = new PanjeaDefaultMessageAreaModel(delegateFor);
        this.messageAreaModel.addPropertyChangeListener(this);
    }

    /**
     * Aggiorna i messaggi
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (messageLabel == null) {
            this.messageLabel = getComponentFactory().createLabel(" ");
        }
        messageAreaModel.getMessage().renderMessage(messageLabel);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        messageAreaModel.removePropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        messageAreaModel.removePropertyChangeListener(propertyName, listener);
    }

    @Override
    public void setMessage(Message message) {
        boolean locked = message.getMessage().indexOf(ToolbarPageEditor.LOCK_MESSAGE) != -1;
        boolean released = message.getMessage().indexOf(ToolbarPageEditor.RELEASE_MESSAGE) != -1;
        if (messageLabel != null) {
            if (!locked && !released) {
                messageAreaModel.setMessage(message);
                messageAreaModel.renderMessage(messageLabel);
            } else {
                Icon icon = null;
                // se e' bloccato significa che e' in modifica
                if (locked) {
                    icon = getIconSource().getIcon(LOCKED_ICON);
                    this.lockIcon.setText(RcpSupport.getMessage(ToolbarPageEditor.LOCK_MESSAGE));
                }
                // se e' released significa che non sono in modifica
                if (released) {
                    icon = getIconSource().getIcon(RELEASED_ICON);
                    this.lockIcon.setText("");
                }
                this.lockIcon.setIcon(icon);
            }
        }
    }

}
