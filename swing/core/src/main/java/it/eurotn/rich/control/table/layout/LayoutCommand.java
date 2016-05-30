package it.eurotn.rich.control.table.layout;

import it.eurotn.panjea.anagrafica.domain.TableLayout;

import javax.swing.AbstractButton;
import javax.swing.SwingConstants;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandButtonConfigurer;
import org.springframework.richclient.dialog.CloseAction;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.factory.ButtonFactory;
import org.springframework.richclient.util.RcpSupport;

public class LayoutCommand extends ActionCommand {

    private final class DeleteConfirmationDialog extends ConfirmationDialog {

        private boolean deleteLayout = Boolean.FALSE;

        /**
         * Costruttore.
         * 
         * @param title
         *            titolo
         * @param message
         *            messaggio
         */
        private DeleteConfirmationDialog(final String title, final String message) {
            super(title, message);
        }

        /**
         * @return <code>true</code> se il layout è stato cancellato
         */
        public boolean isDeleteLayout() {
            return deleteLayout;
        }

        @Override
        protected void onConfirm() {
            deleteLayout = layoutManager.cancella(getTableLayout());
        }
    }

    private final TableLayout layout;

    private CaricaTableLayoutCommand caricaCommand;

    private DefaultTableLayoutManager layoutManager;

    /**
     * Costruttore.
     * 
     * @param layout
     *            layout
     * @param layoutManager
     *            layout manager
     */
    public LayoutCommand(final TableLayout layout, final DefaultTableLayoutManager layoutManager) {
        super();
        this.layout = layout;
        this.layoutManager = layoutManager;
        caricaCommand = new CaricaTableLayoutCommand(layoutManager, layout);
    }

    @Override
    public AbstractButton createButton(String faceDescriptorId, ButtonFactory buttonFactory,
            CommandButtonConfigurer buttonConfigurer) {
        AbstractButton abstractButton = super.createButton(faceDescriptorId, buttonFactory, buttonConfigurer);

        abstractButton.setText(layout.getName());
        if (layout.isGlobal()) {
            abstractButton.setIcon(RcpSupport.getIcon("tableLayoutGlobal"));
        } else {
            abstractButton.setIcon(RcpSupport.getIcon("tableLayoutUser"));
        }
        abstractButton.setHorizontalAlignment(SwingConstants.LEFT);

        return abstractButton;
    }

    /**
     * Esegue la cancellazione del layout.
     * 
     * @return <code>true</code> se il layout è stato cancellato
     */
    public boolean deleteLayout() {

        DeleteConfirmationDialog dialog = new DeleteConfirmationDialog("Attenzione",
                "Cancellare il layout " + getTableLayout().getName() + "?");

        dialog.setCloseAction(CloseAction.HIDE);
        dialog.showDialog();

        boolean deleteLayout = dialog.isDeleteLayout();

        return deleteLayout;
    }

    @Override
    protected void doExecuteCommand() {
        caricaCommand.execute();
    }

    /**
     * @return Returns the layout.
     */
    public TableLayout getLayout() {
        return layout;
    }

    /**
     * @return the layout
     */
    public TableLayout getTableLayout() {
        return layout;
    }

    /**
     * Carica il layout.
     */
    public void loadLayout() {
        caricaCommand.execute();
    }
}
