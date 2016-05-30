package it.eurotn.rich.editors;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.springframework.richclient.components.Focussable;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Message;
import org.springframework.richclient.dialog.CloseAction;
import org.springframework.richclient.dialog.DialogPage;
import org.springframework.richclient.dialog.FormBackedDialogPage;
import org.springframework.richclient.dialog.TitledApplicationDialog;
import org.springframework.richclient.form.Form;
import org.springframework.util.Assert;

import com.jidesoft.plaf.xerto.FrameBorder;

import it.eurotn.panjea.utils.PanjeaDialogPageUtils;

/**
 * TitledPageApplicationDialog personalizzata che estende PanjeaTitledApplicationDialog per eliminare i messaggi di
 * validazione dal dialogo restringendo lo spazio occupato dal title header
 *
 * @author Leonardo
 */
public abstract class PanjeaTitledPageApplicationDialog extends PanjeaTitledApplicationDialog {

    protected DialogPage dialogPage = null;
    private Image titlePaneImage = null;
    private String titlePaneTitle = null;
    private final PropertyChangeListener dialogPagePropertyChangeHandler = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (DialogPage.PAGE_COMPLETE_PROPERTY.equals(evt.getPropertyName())) {
                setEnabled(dialogPage.isPageComplete());
            } else {
                update();
            }
        }
    };

    /**
     * Default constructor. Make sure to call {@link #setDialogPage(DialogPage)} prior to using this dialog.
     */
    public PanjeaTitledPageApplicationDialog() {
        super();
    }

    /**
     *
     * Costruttore.
     *
     * @param dialogPage
     *            pagina da visualizzare nel dialog
     */
    public PanjeaTitledPageApplicationDialog(final DialogPage dialogPage) {
        super();
        setDialogPage(dialogPage);
    }

    /**
     *
     * Costruttore.
     *
     * @param dialogPage
     *            pagina da visualizzare nel dialog
     * @param parent
     *            parent window
     */
    public PanjeaTitledPageApplicationDialog(final DialogPage dialogPage, final Window parent) {
        super(dialogPage.getTitle(), parent);
        setDialogPage(dialogPage);
    }

    /**
     *
     * Costruttore.
     *
     * @param dialogPage
     *            pagina da visualizzare nel dialog
     * @param parent
     *            parent window
     * @param closeAction
     *            close action
     */
    public PanjeaTitledPageApplicationDialog(final DialogPage dialogPage, final Window parent,
            final CloseAction closeAction) {
        super(dialogPage.getTitle(), parent, closeAction);
        setDialogPage(dialogPage);
    }

    /**
     *
     * Costruttore.
     *
     * @param form
     *            form
     * @param parent
     *            parent window
     */
    public PanjeaTitledPageApplicationDialog(final Form form, final Window parent) {
        this(new FormBackedDialogPage(form), parent);
    }

    @Override
    protected JComponent createTitledDialogContentPane() {
        dialogPage.addPropertyChangeListener(dialogPagePropertyChangeHandler);
        update();
        JPanel panel = getComponentFactory().createPanel(new BorderLayout());

        JComponent dialogPageControl = dialogPage.getControl();
        // se non e' una FormBackedDialogPageEditor devo aggiungere il pannello
        // messagable, il FrameBorder e un
        // emptyBorder per far risultare i componenti standard come per la
        // FormBacked stessa in un dialogo
        if (!(dialogPage instanceof FormBackedDialogPageEditor) && isMessagePaneVisible()) {
            JComponent messagablePanel = PanjeaDialogPageUtils.createFormBackedMessagablePanel(dialogPage);
            messagablePanel.setBorder(new FrameBorder());
            panel.add(messagablePanel, BorderLayout.PAGE_START);
            dialogPageControl.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        }
        // visto che ogni volta che apro il dialog passa di qui, rimuovo i
        // componenti che contengono
        // la toolbar solo se trovo il componente con nome toolBar
        JComponent compToolbar = (JComponent) dialogPageControl.getComponent(0);
        if (compToolbar.getName() != null && "toolBar".equals(compToolbar.getName())) {
            dialogPageControl.remove(compToolbar);
        }
        panel.add(dialogPageControl, BorderLayout.CENTER);
        return panel;
    }

    @Override
    protected Message getDescription() {
        return new DefaultMessage(dialogPage.getDescription());
    }

    protected DialogPage getDialogPage() {
        return dialogPage;
    }

    @Override
    protected void onAboutToShow() {
        if (getDialogPage() != null && getDialogPage() instanceof Focussable) {
            ((Focussable) getDialogPage()).grabFocus();
        }
    }

    protected void setDialogPage(DialogPage dialogPage) {
        Assert.notNull(dialogPage, "The single dialog page to display is required");
        this.dialogPage = dialogPage;
    }

    /**
     * Sets the image to use in the title pane. Normally the image is provided by the current dialog page, but this
     * method allows for overriding this.
     * <p>
     * If the image passed is null, the image of the dialog page will be used.
     *
     * @param image
     *            the image
     * @see TitledApplicationDialog#setTitlePaneImage(Image)
     */
    @Override
    public void setTitlePaneImage(Image image) {
        titlePaneImage = image;
        super.setTitlePaneImage(image);
    }

    /**
     * Sets the title to use in the title pane. Normally the title is provided by the current dialog page, but this
     * method allows for overriding this.
     * <p>
     * If the title passed is null, the title of the dialog page will be used.
     *
     * @param title
     *            the title
     * @see TitledApplicationDialog#setTitlePaneTitle(String)
     */
    @Override
    public void setTitlePaneTitle(String title) {
        titlePaneTitle = title;
        super.setTitlePaneTitle(title);
    }

    protected void update() {
        setTitle(dialogPage.getTitle());

        updateTitlePane();
        updateMessagePane();
    }

    protected void updateMessagePane() {
        setMessage(dialogPage.getMessage());
    }

    protected void updateTitlePane() {
        super.setTitlePaneTitle(titlePaneTitle != null ? titlePaneTitle : dialogPage.getTitle());
        super.setTitlePaneImage(titlePaneImage != null ? titlePaneImage : dialogPage.getImage());
        setDescription(dialogPage.getDescription());
    }
}
