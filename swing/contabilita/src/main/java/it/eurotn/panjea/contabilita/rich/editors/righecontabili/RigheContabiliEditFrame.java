package it.eurotn.panjea.contabilita.rich.editors.righecontabili;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.RigaContabile;
import it.eurotn.panjea.contabilita.rich.bd.ContabilitaBD;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaBD;
import it.eurotn.rich.command.AbstractDeleteCommand;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;
import it.eurotn.rich.editors.IPageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;
import it.eurotn.rich.editors.table.EEditPageMode;
import it.eurotn.rich.editors.table.EditFrame;

public class RigheContabiliEditFrame extends EditFrame<RigaContabile> {

    private class DeleteRigheCommand extends AbstractDeleteCommand {

        /**
         * Costruttore.
         *
         * @param securityControllerId
         */
        public DeleteRigheCommand() {
            super("RIGHESECURITYCONTROLLER");
        }

        @Override
        public void addCommandInterceptor(ActionCommandInterceptor l) {
            // L'editFrame aggiunge listener al pulsante di default dell'editor.
            // Nel caso specifico non voglio nessun listener.
        }

        @Override
        public Object onDelete() {
            List<RigaContabile> righeSelezionate = tableWidget.getSelectedObjects();
            if (righeSelezionate.size() == 0) {
                return null;
            }
            AreaContabile areaContabile = getContabilitaBD().cancellaRigheContabili(righeSelezionate);
            Iterator<RigaContabile> righeSelezionateIterator = righeSelezionate.iterator();
            boolean ricaricaRighe = false;
            while (righeSelezionateIterator.hasNext()) {
                RigaContabile rigaContabile = righeSelezionateIterator.next();
                if (rigaContabile.isRateiRiscontiAttivi()) {
                    ricaricaRighe = true;
                    break;
                }
            }
            if (ricaricaRighe) {
                ((RigheContabiliTablePage) pageEditor).ricaricaRighe();
            } else {
                tableWidget.removeRowsObject(righeSelezionate);
                RigaContabile rigaContabile = righeSelezionate.get(0);
                rigaContabile.setAreaContabile(areaContabile);
                // Per aggiornare l'area contabile
                if (getCurrentEditPage() instanceof FormBackedDialogPageEditor) {
                    ((FormBackedDialogPageEditor) getCurrentEditPage()).firePropertyChangeNew(
                            IPageLifecycleAdvisor.OBJECT_CHANGED, new Integer(-1), rigaContabile);
                }
            }
            return righeSelezionate;
        }

    }

    private static final long serialVersionUID = 6209304295848835850L;

    private IContabilitaBD contabilitaBD;

    /**
     * Costruttore.
     *
     * @param editView
     *            editView
     * @param pageEditor
     *            pageEditor
     * @param startQuickAction
     *            quick action
     */
    public RigheContabiliEditFrame(final EEditPageMode editView,
            final AbstractTablePageEditor<RigaContabile> pageEditor, final String startQuickAction) {
        super(editView, pageEditor, startQuickAction);
    }

    /**
     * @return the magazzinoDocumentoBD
     */
    private IContabilitaBD getContabilitaBD() {
        if (contabilitaBD == null) {
            contabilitaBD = RcpSupport.getBean(ContabilitaBD.BEAN_ID);
        }

        return contabilitaBD;
    }

    @Override
    protected void init() {
        DeleteRigheCommand deleteCommand = new DeleteRigheCommand();
        for (Entry<String, IPageEditor> entry : pageEditor.getEditPages().entrySet()) {
            ((FormBackedDialogPageEditor) entry.getValue()).setDeleteCommand(deleteCommand);
        }

        super.init();
    }
}
