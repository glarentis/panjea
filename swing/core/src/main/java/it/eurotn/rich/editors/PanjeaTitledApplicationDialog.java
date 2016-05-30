package it.eurotn.rich.editors;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Message;
import org.springframework.richclient.dialog.ApplicationDialog;
import org.springframework.richclient.dialog.CloseAction;
import org.springframework.richclient.dialog.TitlePane;

import com.jidesoft.swing.PartialLineBorder;

/**
 * ApplicationDialog personalizzata per restringere lo spazio occupato dall'header title e per
 * bloccare i messaggi di errore dall'essere visualizzati.
 *
 * @author Leonardo
 */
public abstract class PanjeaTitledApplicationDialog extends ApplicationDialog {

    private final TitlePane titlePane = new TitlePane(1);

    private Message description = new DefaultMessage("Title pane description");

    private JComponent pageControl;

    private JComponent contentPane;

    /**
     * Costruttore.
     */
    public PanjeaTitledApplicationDialog() {
        super();
    }

    /**
     * Costruttore.
     *
     * @param title
     *            titolo per il dialog
     * @param parent
     *            il parent che apre il dialog
     */
    public PanjeaTitledApplicationDialog(final String title, final Component parent) {
        super(title, parent);
    }

    /**
     * Costruttore.
     *
     * @param title
     *            titolo per il dialog
     * @param parent
     *            il parent che apre il dialog
     * @param closeAction
     *            l'action alla chiusura del dialog
     */
    public PanjeaTitledApplicationDialog(final String title, final Component parent, final CloseAction closeAction) {
        super(title, parent, closeAction);
    }

    @Override
    protected void addDialogComponents() {
        JComponent dialogContentPane = createDialogContentPane();
        getDialog().getContentPane().add(dialogContentPane, BorderLayout.CENTER);
        getDialog().getContentPane().add(createButtonBar(), BorderLayout.SOUTH);
    }

    /**
     * @param listener
     *            property change da aggiungere
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        titlePane.addPropertyChangeListener(listener);
    }

    /**
     * @param propertyName
     *            nome della proprietà su cui aggiungere il property change
     * @param listener
     *            property change da aggiungere
     */
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        titlePane.addPropertyChangeListener(propertyName, listener);
    }

    @Override
    protected JComponent createDialogContentPane() {
        pageControl = new JPanel(new BorderLayout());
        Border contentPaneBorder = BorderFactory.createEmptyBorder(10, 0, 0, 0);
        if (isMessagePaneVisible()) {
            JPanel titlePaneContainer = new JPanel(new BorderLayout());
            setMessage(getDescription());
            titlePaneContainer.add(titlePane.getControl());
            PartialLineBorder partialLineBorder = new PartialLineBorder(Color.GRAY, 1, SwingConstants.SOUTH);
            titlePaneContainer.setBorder(partialLineBorder);
            pageControl.add(titlePaneContainer, BorderLayout.NORTH);

            contentPaneBorder = BorderFactory.createEmptyBorder(0, 0, 0, 0);
        }
        contentPane = createTitledDialogContentPane();
        contentPane.setBorder(contentPaneBorder);
        if (getPreferredSize() != null) {
            contentPane.setPreferredSize(getPreferredSize());
        }
        pageControl.add(contentPane);
        return pageControl;
    }

    /**
     * @return i componenti del dialog
     */
    protected abstract JComponent createTitledDialogContentPane();

    protected Message getDescription() {
        return description;
    }

    /**
     * @return the message
     */
    public Message getMessage() {
        return titlePane.getMessage();
    }

    protected Image getTitlePaneImage() {
        return null;
    }

    protected String getTitlePaneTitle() {
        return titlePane.getTitle();
    }

    protected boolean isMessagePaneVisible() {
        return true;
    }

    /**
     * @return is message showing
     */
    public boolean isMessageShowing() {
        return titlePane.isMessageShowing();
    }

    /**
     * @param listener
     *            listener to remove
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        titlePane.removePropertyChangeListener(listener);
    }

    /**
     * @param propertyName
     *            property name
     * @param listener
     *            listener to remove
     */
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        titlePane.removePropertyChangeListener(propertyName, listener);
    }

    /**
     * @param shortDescription
     *            caption to set
     */
    public void setCaption(String shortDescription) {
        throw new UnsupportedOperationException("What can I do with a caption?");
    }

    protected void setContentPane(JComponent comp) {
        if (isControlCreated()) {
            pageControl.remove(contentPane);
            this.contentPane = comp;
            pageControl.add(contentPane);
            pageControl.revalidate();
            pageControl.repaint();
        } else {
            throw new IllegalStateException("Cannot set content pane until control is created");
        }
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = new DefaultMessage(description);
        setMessage(this.description);
    }

    /**
     * @param image
     *            the image to set
     */
    public void setImage(Image image) {
        setTitlePaneImage(image);
    }

    /**
     * @param message
     *            the message to set
     */
    public void setMessage(Message message) {
        if (message != null && !DefaultMessage.EMPTY_MESSAGE.equals(message)) {
            titlePane.setMessage(message);
        } else {
            titlePane.setMessage(null);
        }
    }

    /**
     * @param image
     *            title pane image
     */
    public void setTitlePaneImage(Image image) {
        titlePane.setImage(image);
    }

    /**
     * @param titleAreaText
     *            title pane title
     */
    public void setTitlePaneTitle(String titleAreaText) {
        if ("displayName".equals(titleAreaText)) {
            titlePane.setTitle("");
        } else {
            titlePane.setTitle(titleAreaText);
        }
    }
}
