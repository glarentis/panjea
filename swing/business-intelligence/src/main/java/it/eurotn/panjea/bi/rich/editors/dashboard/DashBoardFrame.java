/**
 *
 */
package it.eurotn.panjea.bi.rich.editors.dashboard;

import java.awt.BorderLayout;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandButtonConfigurer;
import org.springframework.richclient.command.config.ToolBarCommandButtonConfigurer;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.docking.DockableFrame;

import it.eurotn.rich.command.JECCommandGroup;
import it.eurotn.rich.dialog.DockingCompositeDialogPage;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

/**
 * @author fattazzo
 *
 */
public abstract class DashBoardFrame extends DockableFrame {

    private class CloseFrameCommand extends ActionCommand {

        public static final String COMMAND_ID = "closeFrameCommand";

        /**
         * Costruttore.
         */
        public CloseFrameCommand() {
            super(COMMAND_ID);
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            DashBoardFrame.this.getDockingManager().removeFrame(DashBoardFrame.this.getKey());
        }

        @Override
        protected void onButtonAttached(AbstractButton button) {
            super.onButtonAttached(button);
        }

    }

    private class RearrangablePropertyChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {

            boolean rearrangable = (boolean) evt.getNewValue();
            setReadonly(!rearrangable);
            ((IPageLifecycleAdvisor) getClientProperty(DockingCompositeDialogPage.PAGE_PROPERTY_NAME))
                    .setReadOnly(!rearrangable);
        }

    }

    private static final long serialVersionUID = 4480677321940309597L;

    public static final String DASHBOARD_COMPOSITE_PAGE = "dashBoardCompositePage";

    /**
     * Set dove mantengo un riferimento della lista di command group presenti, utile per liberare la
     * memoria al momento della dispose;
     */
    private JECCommandGroup footerToolbarComponent;

    private RearrangablePropertyChangeListener rearrangablePropertyChangeListener;

    private String title;

    protected AbstractButton closeFrameButton;

    /**
     * Costruttore.
     */
    public DashBoardFrame() {
        super();
        setMaximizable(false);
        setFrameIcon(null);
        setFloatable(false);
        setBorder(null);

        footerToolbarComponent = new JECCommandGroup() {
            @Override
            protected CommandButtonConfigurer getToolBarButtonConfigurer() {
                // uso quello di default perchè non voglio l'ombra sulle icone dei pulsanti
                return new ToolBarCommandButtonConfigurer();
            }
        };

        rearrangablePropertyChangeListener = new RearrangablePropertyChangeListener();
        addPropertyChangeListener(PROPERTY_REARRANGABLE, rearrangablePropertyChangeListener);
    }

    /**
     * Aggiunge un command alla toolbar di destra del frame.
     *
     * @param command
     *            command da aggiungere
     */
    public void addFooterToolBarCommand(ActionCommand command) {
        footerToolbarComponent.add(command);
    }

    /**
     * Configura il frame
     */
    public final void configureFrame() {

        doConfigure();

        setAvailableButtons(getAvailableButtons() & ~DockableFrame.BUTTON_ALL);

        // title component
        JLabel titleLabel = new JLabel(title);
        titleLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        Font font = titleLabel.getFont();
        titleLabel.setFont(new Font(font.getName(), font.getStyle(), 11));

        setTabTitle(titleLabel.getText());

        // buttons components
        JPanel buttonsPanel = new JPanel(new BorderLayout());
        buttonsPanel.setBorder(null);
        buttonsPanel.setOpaque(false);
        buttonsPanel.add(footerToolbarComponent.createToolBar(), BorderLayout.CENTER);
        CloseFrameCommand closeFrameCommand = new CloseFrameCommand();
        closeFrameButton = closeFrameCommand.createButton();
        buttonsPanel.add(closeFrameButton, BorderLayout.EAST);

        JPanel titlePane = new JPanel(new BorderLayout());
        titlePane.setBorder(null);
        JComponent headerComponent = getHeaderComponent();
        if (headerComponent != null) {
            titlePane.add(headerComponent, BorderLayout.WEST);
        }
        titlePane.add(titleLabel, BorderLayout.CENTER);
        titlePane.add(buttonsPanel, BorderLayout.EAST);

        setTitleLabelComponent(titlePane);
        ((JPanel) getContentPane().getComponent(0)).setBorder(null);
        setReadonly(!getDockingManager().isRearrangable());
    }

    @Override
    public void dispose() {
        setTitleLabelComponent(null);

        footerToolbarComponent.reset();
        footerToolbarComponent = null;

        removePropertyChangeListener(PROPERTY_REARRANGABLE, rearrangablePropertyChangeListener);
        rearrangablePropertyChangeListener = null;

        super.dispose();
    }

    protected void doConfigure() {
    }

    public JComponent getHeaderComponent() {
        return null;
    }

    protected void setReadonly(boolean readonly) {
        footerToolbarComponent.setVisible(!readonly);
        closeFrameButton.setVisible(!readonly);
    }

    /**
     * @param title
     *            the title to set
     */
    @Override
    public void setTitle(String title) {
        this.title = title;
    }

}
