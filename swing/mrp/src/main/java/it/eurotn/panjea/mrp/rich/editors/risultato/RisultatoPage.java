package it.eurotn.panjea.mrp.rich.editors.risultato;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListModel;

import org.springframework.binding.value.support.ListListModel;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.TableModelWrapperUtils;

import it.eurotn.panjea.mrp.domain.ConflittoMrp;
import it.eurotn.panjea.mrp.domain.RisultatoMrpFlat;
import it.eurotn.panjea.mrp.rich.bd.IMrpBD;
import it.eurotn.panjea.mrp.rich.editors.risultato.command.CalcolaMrpCommand;
import it.eurotn.panjea.mrp.rich.editors.risultato.command.GeneraOrdiniCommand;
import it.eurotn.panjea.mrp.util.ParametriMrpRisultato;
import it.eurotn.rich.command.AbstractDeleteCommand;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.table.EditFrame;

public class RisultatoPage extends AbstractTablePageEditor<RisultatoMrpFlat> {

    private class ConflictDialog extends ConfirmationDialog {
        private boolean result = false;

        public ConflictDialog() {
            super("Conferma creazione",
                    "Ci sono degli articoli utilizzati nel calcolo da un altro utente. Vuoi continuare?");
        }

        public boolean getResult() {
            return result;
        }

        @Override
        protected void onConfirm() {
            result = true;
        }
    }

    private class DeleteCommand extends AbstractDeleteCommand {

        public DeleteCommand() {
            super("cancellaRisultatiSecurityController");
        }

        @Override
        public Object onDelete() {
            if (getTable().getSelectedObjects().size() > 0) {
                mrpBD.cancellaRigheRisultati(getTable().getSelectedObjects());
                getTable().removeRowsObject(getTable().getSelectedObjects());
            }
            return null;
        }
    }

    private class MrpActionCommandInteceptor implements ActionCommandInterceptor {

        private String titleKey = null;
        private String descriptionKey = null;

        private boolean confirmed = false;

        /**
         * Costruttore.
         *
         * @param titleKey
         *            titolo del dialogo di conferma
         * @param descriptionKey
         *            descrizione del dialogo di conferma
         */
        public MrpActionCommandInteceptor(final String titleKey, final String descriptionKey) {
            super();
            this.titleKey = titleKey;
            this.descriptionKey = descriptionKey;
        }

        @Override
        public void postExecution(ActionCommand arg0) {

        }

        @Override
        public boolean preExecution(ActionCommand command) {
            confirmed = false;
            command.addParameter(CalcolaMrpCommand.PARAMETRI_MRP, parametriMrpRisultato);

            // se le righe visibili sono diverse dal totale delle righe oppure i parametri sono
            // filtrati per area passo
            // alla generazione solo gli id necessari
            Integer[] risultatiId = null;
            List<RisultatoMrpFlat> righeVisualizzate = getTable().getVisibleObjects();

            for (RisultatoMrpFlat risultatoMrpFlat : righeVisualizzate) {
                if (risultatoMrpFlat.getConflitti() != null) {
                    ConflictDialog dialog = new ConflictDialog();
                    dialog.showDialog();
                    if (!dialog.getResult()) {
                        return false;
                    }
                }
            }

            boolean righeFiltered = (righeVisualizzate.size() != getTable().getRows().size())
                    || parametriMrpRisultato.isEscludiOrdinati();

            if (righeFiltered) {
                risultatiId = new Integer[righeVisualizzate.size()];
                for (int i = 0; i < righeVisualizzate.size(); i++) {
                    risultatiId[i] = righeVisualizzate.get(i).getId();
                }
            }
            command.addParameter(GeneraOrdiniCommand.PARAM_RISULTATI_ID, risultatiId);

            ConfirmationDialog confirmationDialog = new ConfirmationDialog(
                    RcpSupport.getMessage(titleKey), RcpSupport.getMessage(descriptionKey)) {

                @Override
                protected void onConfirm() {
                    confirmed = true;

                }
            };
            confirmationDialog.showDialog();
            return confirmed;
        }
    }

    private class SvuotaCommand extends ActionCommand {

        /**
         * Costruttore.
         */
        public SvuotaCommand() {
            super("svuotaCommand");
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            mrpBD.svuotaRigheRisultati();
            loadData();
        }

    }

    public static final String PAGE_ID = "risultatoPage";

    private IMrpBD mrpBD;

    private ParametriMrpRisultato parametriMrpRisultato;

    private DeleteCommand deleteCommand;

    private JList<String> conflitti;

    private GeneraOrdiniCommand generaOrdiniCommand = null;
    private MrpActionCommandInteceptor generaOrdiniActionCommandInteceptor = null;

    /**
     * Costruttore.
     */
    public RisultatoPage() {
        super(PAGE_ID, new RisultatoTableModel());
    }

    @Override
    protected EditFrame<RisultatoMrpFlat> createEditFrame() {
        return new EditFrameRisultatoMrpFlat(this, mrpBD);
    }

    @Override
    public AbstractCommand[] getCommands() {
        return new AbstractCommand[] { new SvuotaCommand(), getGeneraOrdiniCommand() };
    }

    @Override
    public AbstractCommand getEditorDeleteCommand() {
        if (deleteCommand == null) {
            deleteCommand = new DeleteCommand();
        }
        return deleteCommand;
    }

    /**
     * @return generaOrdiniActionCommandInteceptor
     */
    private MrpActionCommandInteceptor getGeneraOrdiniActionCommandInteceptor() {
        if (generaOrdiniActionCommandInteceptor == null) {
            generaOrdiniActionCommandInteceptor = new MrpActionCommandInteceptor(
                    "parametriMrpRisultato.confirmationDialog.title",
                    "parametriMrpRisultato.confirmationDialog.description");
        }
        return generaOrdiniActionCommandInteceptor;
    }

    /**
     * @return generaOrdiniCommand
     */
    private GeneraOrdiniCommand getGeneraOrdiniCommand() {
        if (generaOrdiniCommand == null) {
            generaOrdiniCommand = new GeneraOrdiniCommand();
            generaOrdiniCommand.addCommandInterceptor(getGeneraOrdiniActionCommandInteceptor());
        }
        return generaOrdiniCommand;
    }

    @Override
    public JComponent getHeaderControl() {
        conflitti = new JList<>();
        conflitti.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        return conflitti;
    }

    /**
     * @return Returns the mrpBD.
     */
    public IMrpBD getMrpBD() {
        return mrpBD;
    }

    @Override
    public Collection<RisultatoMrpFlat> loadTableData() {
        List<RisultatoMrpFlat> result = mrpBD.caricaRisultatoMrp(parametriMrpRisultato);
        Set<String> conflittiSet = new HashSet<>();
        for (RisultatoMrpFlat risultatoMrpFlat : result) {
            if (risultatoMrpFlat.getConflitti() != null) {
                for (ConflittoMrp conflittoMrp : risultatoMrpFlat.getConflitti()) {
                    conflittiSet.add(conflittoMrp.getUser().toUpperCase());
                }
            }
        }
        conflitti.setVisible(!conflittiSet.isEmpty());
        @SuppressWarnings("unchecked")
        ListModel<String> model = new ListListModel(new ArrayList<>(conflittiSet));
        conflitti.setModel(model);
        return result;
    }

    @Override
    public void onPostPageOpen() {

    }

    @Override
    public Collection<RisultatoMrpFlat> refreshTableData() {
        return loadTableData();
    }

    @Override
    public void setFormObject(Object object) {
        parametriMrpRisultato = (ParametriMrpRisultato) object;
        RisultatoTableModel model = (RisultatoTableModel) TableModelWrapperUtils
                .getActualTableModel(getTable().getTable().getModel());
        if (parametriMrpRisultato.isEvidenziaOrdine()) {
            model.setIdAreaOrdine(parametriMrpRisultato.getAreaOrdine().getId());
        } else {
            model.setIdAreaOrdine(null);
        }
    }

    /**
     * @param mrpBD
     *            The mrpBD to set.
     */
    public void setMrpBD(IMrpBD mrpBD) {
        this.mrpBD = mrpBD;
    }
}
