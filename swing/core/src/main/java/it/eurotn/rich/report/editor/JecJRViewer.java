package it.eurotn.rich.report.editor;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JRHyperlinkListener;
import net.sf.jasperreports.view.JRViewer;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandButtonConfigurer;
import org.springframework.richclient.factory.ButtonFactory;
import org.springframework.richclient.util.RcpSupport;

public class JecJRViewer extends JRViewer {

    public class NativeWrappedActionCommand extends ActionCommand {

        private final JButton button;

        /**
         * Costruttore.
         *
         * @param id
         *            id dell'action
         * @param button
         *            button da associare all'action.
         */
        public NativeWrappedActionCommand(final String id, final JButton button) {
            super(id);
            RcpSupport.configure(this);
            this.setSecurityControllerId(id);
            this.button = button;
            this.setEnabled(button.isEnabled());
        }

        @Override
        public AbstractButton createButton(String faceDescriptorId, ButtonFactory buttonFactory,
                CommandButtonConfigurer buttonConfigurer) {
            return super.createButton(faceDescriptorId, buttonFactory, buttonConfigurer);
        }

        @Override
        protected void doExecuteCommand() {
            button.doClick();
        }
    }

    private class NativeWrapperToggleButtonActionCommand extends ActionCommand {
        private final JToggleButton button;

        /**
         * Costruttore.
         *
         * @param id
         *            id dell'action
         * @param button
         *            button da associare all'action.
         */
        public NativeWrapperToggleButtonActionCommand(final String id, final JToggleButton button) {
            super(id);
            RcpSupport.configure(this);
            this.setSecurityControllerId(id);
            this.button = button;
            this.setEnabled(button.isEnabled());
        }

        @Override
        protected void doExecuteCommand() {
            button.doClick();
        }
    }

    private static final long serialVersionUID = 1681970388399342434L;

    private static final String SAVE_ACTION = "JecJRViewer.save";

    private static final String FIRST_ACTION = "JecJRViewer.first";
    private static final String PREVIOUS_ACTION = "JecJRViewer.previous";
    private static final String NEXT_ACTION = "JecJRViewer.next";
    private static final String LAST_ACTION = "JecJRViewer.last";

    private static final String PAGE_DEFAULT_ACTION = "JecJRViewer.pageDefault";
    private static final String PAGE_ALL_ACTION = "JecJRViewer.pageAll";
    private static final String PAGE_WIDTH_ACTION = "JecJRViewer.pageWidth";

    private static final String ZOOM_IN_ACTION = "JecJRViewer.zoomIn";
    private static final String ZOOM_OUT_ACTION = "JecJRViewer.zoomOut";

    /**
     * Costruttore.
     *
     * @param jrPrint
     *            jrprint del report.
     */
    public JecJRViewer(final JasperPrint jrPrint) {
        this(jrPrint, null);
    }

    /**
     * Costruttore.
     *
     * @param jrPrint
     *            jrprint del report.
     * @param hyperlinkListener
     *            listener hyperlink
     */
    public JecJRViewer(final JasperPrint jrPrint, final JRHyperlinkListener hyperlinkListener) {
        super(jrPrint);
        this.addHyperlinkListener(hyperlinkListener);
    }

    /**
     *
     * @return command
     */
    public ActionCommand getActualSizeCommand() {
        return new NativeWrapperToggleButtonActionCommand(PAGE_DEFAULT_ACTION, btnActualSize);
    }

    /**
     *
     * @return command per navigare alla prima pagina del report
     */
    public ActionCommand getFirstPageCommand() {
        ActionCommand command = new NativeWrappedActionCommand(FIRST_ACTION, btnFirst);
        command.setEnabled(true);
        return command;
    }

    /**
     *
     * @return command
     */
    public ActionCommand getFitPageCommand() {
        return new NativeWrapperToggleButtonActionCommand(PAGE_ALL_ACTION, btnFitPage);
    }

    /**
     *
     * @return command
     */
    public ActionCommand getFitWidthCommand() {
        return new NativeWrapperToggleButtonActionCommand(PAGE_WIDTH_ACTION, btnFitWidth);
    }

    /**
     *
     * @return @return command per navigare all'ultima pagina del report
     */
    public ActionCommand getLastPageCommand() {
        ActionCommand command = new NativeWrappedActionCommand(LAST_ACTION, btnLast);
        command.setEnabled(true);
        return command;
    }

    /**
     *
     * @return command per navigare alla pagina succ. del report
     */
    public ActionCommand getNextPageCommand() {
        ActionCommand command = new NativeWrappedActionCommand(NEXT_ACTION, btnNext);
        command.setEnabled(true);
        return command;
    }

    /**
     *
     * @return command per navigare alla pagina precedente sdel report
     */
    public ActionCommand getPreviousPageCommand() {
        ActionCommand command = new NativeWrappedActionCommand(PREVIOUS_ACTION, btnPrevious);
        command.setEnabled(true);
        return command;
    }

    /**
     *
     * @return command per salvare il report nei vari formati
     */
    public ActionCommand getSaveCommand() {
        return new NativeWrappedActionCommand(SAVE_ACTION, btnSave);
    }

    /**
     * @return toolbar
     */
    public JPanel getToolbarPanel() {
        return tlbToolBar;
    }

    /**
     *
     * @return combo zoom
     */
    public JComponent getZoomBar() {
        return cmbZoom;
    }

    /**
     *
     * @return action command
     */
    public ActionCommand getZoomInCommand() {
        return new NativeWrappedActionCommand(ZOOM_IN_ACTION, btnZoomIn);
    }

    /**
     *
     * @return action command
     */
    public ActionCommand getZoomOutCommand() {
        return new NativeWrappedActionCommand(ZOOM_OUT_ACTION, btnZoomOut);
    }

    /**
     * set toolbar
     *
     * @param toolbarComponent
     *            toolbar
     */
    public void setToolbar(JComponent toolbarComponent) {
        tlbToolBar.removeAll();
        tlbToolBar.add(toolbarComponent);
    }

}