package it.eurotn.panjea.vending.rich.editors.casse.movimentazione;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.vending.domain.Cassa;
import it.eurotn.panjea.vending.domain.MovimentoCassa;
import it.eurotn.panjea.vending.rich.bd.IVendingAnagraficaBD;
import it.eurotn.panjea.vending.rich.bd.VendingAnagraficaBD;
import it.eurotn.rich.command.AbstractDeleteCommand;
import it.eurotn.rich.control.table.JideTableWidget;

public class MovimentazioneCassaTable extends JideTableWidget<MovimentoCassa> {

    private class AggiungiMovimentoInterceptor implements ActionCommandInterceptor {

        @Override
        public void postExecution(ActionCommand command) {
            if (((AggiungiMovimentoCassaCommand) command).isMovimentoCassaModificato()) {
                fireMovimentazioneCassaChanged();
            }
        }

        @Override
        public boolean preExecution(ActionCommand command) {
            command.addParameter(AggiungiMovimentoCassaCommand.PARAM_CASSA, cassa);
            return cassa != null;
        }
    }

    private class CancellaMovimentoCassaCommand extends AbstractDeleteCommand {

        private IVendingAnagraficaBD vendingAnagraficaBD;

        /**
         * Costruttore.
         */
        public CancellaMovimentoCassaCommand() {
            super("deleteCommand");

            vendingAnagraficaBD = RcpSupport.getBean(VendingAnagraficaBD.BEAN_ID);
        }

        @Override
        protected void onButtonAttached(AbstractButton button) {
            button.setFocusable(false);
            super.onButtonAttached(button);
        }

        @Override
        public Object onDelete() {

            MovimentoCassa movToDelete = null;

            MovimentoCassa movimentoCassa = getSelectedObject();
            if (movimentoCassa != null) {
                if (movimentoCassa.isApertura()) {
                    MessageDialog dialog = new MessageDialog("Attenzione",
                            new DefaultMessage("Non è possibile cancellare un movimento di apertura", Severity.INFO));
                    dialog.showDialog();
                } else {
                    vendingAnagraficaBD.cancellaMovimentoCassa(movimentoCassa.getId());
                    fireMovimentazioneCassaChanged();
                    movToDelete = movimentoCassa;
                }
            }

            return movToDelete;
        }

    }

    private class ModificaMovimentoInterceptor implements ActionCommandInterceptor {

        @Override
        public void postExecution(ActionCommand command) {
            if (((AggiungiMovimentoCassaCommand) command).isMovimentoCassaModificato()) {
                fireMovimentazioneCassaChanged();
            }
        }

        @Override
        public boolean preExecution(ActionCommand command) {
            command.addParameter(AggiungiMovimentoCassaCommand.PARAM_CASSA, cassa);
            command.addParameter(AggiungiMovimentoCassaCommand.PARAM_MOVIMENTO, getSelectedObject());
            return cassa != null;
        }
    }

    private AggiungiMovimentoCassaCommand aggiungiMovimentoCassaCommand;
    private AggiungiMovimentoCassaCommand modificaMovimentoCassaCommand;

    private CancellaMovimentoCassaCommand cancellaMovimentoCassaCommand;

    private Cassa cassa;

    private List<PropertyChangeListener> movimentazioneCassaListener = new ArrayList<>();

    /**
     * Costruttore.
     */
    public MovimentazioneCassaTable() {
        super("movimentazioneCassaTable", new MovimentazioneCassaTableModel());
        setPropertyCommandExecutor(getModificaMovimentoCassaCommand());
    }

    /**
     * @param listener
     *            listener da aggiungere
     */
    public void addMovimentazioneCassaListener(PropertyChangeListener listener) {
        movimentazioneCassaListener.add(listener);
    }

    /**
     * fireMovimentazioneCassaChanged.
     */
    public void fireMovimentazioneCassaChanged() {
        for (PropertyChangeListener listener : movimentazioneCassaListener) {
            PropertyChangeEvent event = new PropertyChangeEvent(this, "movimentazioneCassaChanged", 0, -1);
            listener.propertyChange(event);
        }
    }

    /**
     * @return the aggiungiMovimentoCassaCommand
     */
    private AggiungiMovimentoCassaCommand getAggiungiMovimentoCassaCommand() {
        if (aggiungiMovimentoCassaCommand == null) {
            aggiungiMovimentoCassaCommand = new AggiungiMovimentoCassaCommand();
            aggiungiMovimentoCassaCommand.addCommandInterceptor(new AggiungiMovimentoInterceptor());
        }

        return aggiungiMovimentoCassaCommand;
    }

    /**
     * @return the cancellaMovimentoCassaCommand
     */
    private CancellaMovimentoCassaCommand getCancellaMovimentoCassaCommand() {
        if (cancellaMovimentoCassaCommand == null) {
            cancellaMovimentoCassaCommand = new CancellaMovimentoCassaCommand();
        }

        return cancellaMovimentoCassaCommand;
    }

    @Override
    public JComponent getComponent() {
        JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());
        rootPanel.add(super.getComponent(), BorderLayout.CENTER);

        JPanel buttonPanel = getComponentFactory().createPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(getAggiungiMovimentoCassaCommand().createButton());
        buttonPanel.add(getCancellaMovimentoCassaCommand().createButton());
        rootPanel.add(buttonPanel, BorderLayout.NORTH);

        return rootPanel;
    }

    /**
     * @return the modificaMovimentoCassaCommand
     */
    private AggiungiMovimentoCassaCommand getModificaMovimentoCassaCommand() {
        if (modificaMovimentoCassaCommand == null) {
            modificaMovimentoCassaCommand = new AggiungiMovimentoCassaCommand();
            modificaMovimentoCassaCommand.addCommandInterceptor(new ModificaMovimentoInterceptor());
        }

        return modificaMovimentoCassaCommand;
    }

    /**
     * @param cassa
     *            the cassa to set
     */
    public void setCassa(Cassa cassa) {
        this.cassa = cassa;

        MovimentoCassa movimentoApertura = cassa.getMovimentoApertura();
        List<MovimentoCassa> movimenti = new ArrayList<>();
        if (movimentoApertura != null) {
            movimenti.add(cassa.getMovimentoApertura());
        }
        movimenti.addAll(cassa.getAltriMovimenti());
        setRows(movimenti);
    }

}
