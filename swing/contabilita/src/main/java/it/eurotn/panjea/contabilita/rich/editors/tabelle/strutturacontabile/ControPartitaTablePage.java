package it.eurotn.panjea.contabilita.rich.editors.tabelle.strutturacontabile;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.locking.IDefProperty;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.contabilita.domain.ControPartita;
import it.eurotn.panjea.contabilita.domain.ETipologiaConto;
import it.eurotn.panjea.contabilita.domain.ETipologiaContoControPartita;
import it.eurotn.panjea.contabilita.domain.StrutturaContabile;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD;
import it.eurotn.panjea.contabilita.rich.editors.tabelle.ControPartitaPage;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;
import it.eurotn.rich.editors.IFormPageEditor;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;

/**
 * Classe per la gestione della tabella delle contro partite della struttura contabile.
 *
 * @author fattazzo
 * @version 1.0, 03/set/07
 *
 */
public class ControPartitaTablePage extends AbstractTablePageEditor<ControPartita> {

    /**
     * Dialog di modifica dei record tabella.
     */
    protected class ControPartitaDialog extends PanjeaTitledPageApplicationDialog {
        private boolean commit = false;

        /**
         * Costruttore.
         *
         * @param controPartita
         *            contro partita da gestire
         */
        public ControPartitaDialog(final ControPartita controPartita) {
            super();
            setPreferredSize(new Dimension(1000, 400));
            FormBackedDialogPageEditor page = new ControPartitaPage(contabilitaAnagraficaBD);
            org.springframework.util.Assert.isInstanceOf(IFormPageEditor.class, page,
                    "La dialog page deve implementare IFormPageEditor");
            page.onPostPageOpen();

            if (controPartita.isNew()) {
                page.onNew();
            }
            page.setFormObject(controPartita);
            this.setDialogPage(page);
            setTitle(getMessage("controPartitaDialog.title"));
        }

        /**
         * @return Returns the commit.
         */
        public boolean isCommit() {
            return commit;
        }

        @Override
        protected void onAboutToShow() {
            IFormPageEditor page = (IFormPageEditor) this.getDialogPage();

            // bug : se non risetto il command sulla base di isCommittable trovo
            // il finish abilitato
            // nella condizione in cui il form non � committable.
            this.getFinishCommand().setEnabled(page.isCommittable());
            IDefProperty object = (IDefProperty) page.getForm().getFormObject();
            if (object.getId() != null) {
                page.onLock();
            }
        }

        @Override
        protected void onCancel() {
            IFormPageEditor page = (IFormPageEditor) this.getDialogPage();
            page.onUndo();
            super.onCancel();
        }

        @Override
        protected boolean onFinish() {
            IFormPageEditor page = (IFormPageEditor) this.getDialogPage();
            boolean finished = false;

            if (((ControPartitaPage) page).isControPartitaValid()) {
                finished = page.onSave();
                if (finished) {
                    getTable().replaceOrAddRowObject((ControPartita) page.getForm().getFormObject(),
                            (ControPartita) page.getForm().getFormObject(), null);
                    updateCommand();
                }

                commit = true;
            } else {
                String title = getMessageSource().getMessage(PAGE_ID + DIALOG_ERROR_VALIDATION_TITLE, new Object[] {},
                        Locale.getDefault());
                String message = getMessageSource().getMessage(PAGE_ID + DIALOG_ERROR_VALIDATION_MESSAGE,
                        new Object[] {}, Locale.getDefault());
                MessageDialog dialog = new MessageDialog(title, message);
                dialog.showDialog();
            }

            return finished;
        }
    }

    private class DeleteControPartitaCommand extends ActionCommand {

        public static final String COMMAND_ID = "deleteCommand";

        /**
         * Costruttore.
         *
         */
        public DeleteControPartitaCommand() {
            super(COMMAND_ID);
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            ControPartita controPartita = getTable().getSelectedObject();
            if (controPartita != null) {
                contabilitaAnagraficaBD.cancellaControPartita(controPartita);
                getTable().removeRowObject(controPartita);
                updateCommand();
            }
        }

    }

    private class ModificaControPartitaCommand extends ActionCommand {

        public static final String COMMAND_ID = "lockCommand";

        /**
         * Costruttore.
         *
         */
        public ModificaControPartitaCommand() {
            super(COMMAND_ID);
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            ControPartita controPartita = getTable().getSelectedObject();
            ControPartitaDialog dialog = new ControPartitaDialog(controPartita);
            dialog.showDialog();
        }

    }

    private class NuovaControPartitaCommand extends ActionCommand {

        public static final String COMMAND_ID = "newCommand";

        /**
         * Costruttore.
         *
         */
        public NuovaControPartitaCommand() {
            super(COMMAND_ID);
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            ControPartita controPartita = new ControPartita();
            controPartita.setTipoDocumento(tipoDocumento);
            controPartita.setEntita(entita);
            controPartita.setTipologiaContoControPartita(ETipologiaContoControPartita.SOTTOCONTO);
            ControPartitaDialog dialog = new ControPartitaDialog(controPartita);
            dialog.showDialog();
        }

    }

    private static final String PAGE_ID = "controPartiteTablePage";

    private static final String DIALOG_ERROR_VALIDATION_TITLE = ".errorValidationDialog.title";
    private static final String DIALOG_ERROR_VALIDATION_MESSAGE = ".errorValidationDialog.message";

    private IContabilitaAnagraficaBD contabilitaAnagraficaBD = null;
    private TipoDocumento tipoDocumento = null;
    private EntitaLite entita = null;

    private DeleteControPartitaCommand deleteControPartitaCommand;
    private NuovaControPartitaCommand nuovaControPartitaCommand;
    private ModificaControPartitaCommand modificaControPartitaCommand;

    private List<ControPartita> listControPartite = new ArrayList<ControPartita>();

    private boolean controPartiteAllowed = false;

    /**
     * Costruttore.
     *
     * @param tipoDocumento
     *            tipo documento di riferimento per il caricamento della struttura
     * @param contabilitaAnagraficaBD
     *            contabilitaAnagraficaBD
     */
    public ControPartitaTablePage(final TipoDocumento tipoDocumento,
            final IContabilitaAnagraficaBD contabilitaAnagraficaBD) {
        super(PAGE_ID, new String[] { "ordine", "descrizione", "formula", "descrizioneDare", "descrizioneAvere" },
                ControPartita.class);
        this.contabilitaAnagraficaBD = contabilitaAnagraficaBD;
        this.tipoDocumento = tipoDocumento;
        getTable().setPropertyCommandExecutor(getModificaControPartitaCommand());
    }

    /**
     * Metodo di utilità per ripulire la tabella.
     */
    public void clearData() {
        setRows(new ArrayList<ControPartita>());
        controPartiteAllowed = false;
        updateCommand();
    }

    @Override
    public AbstractCommand[] getCommands() {
        return new AbstractCommand[] { getNuovaControPartitaCommand(), getModificaControPartitaCommand(),
                getDeleteControPartitaCommand(), getRefreshCommand() };
    }

    /**
     * @return the deleteControPartitaCommand
     */
    public DeleteControPartitaCommand getDeleteControPartitaCommand() {
        if (deleteControPartitaCommand == null) {
            deleteControPartitaCommand = new DeleteControPartitaCommand();
        }

        return deleteControPartitaCommand;
    }

    /**
     * @return the modificaControPartitaCommand
     */
    public ModificaControPartitaCommand getModificaControPartitaCommand() {
        if (modificaControPartitaCommand == null) {
            modificaControPartitaCommand = new ModificaControPartitaCommand();
        }

        return modificaControPartitaCommand;
    }

    /**
     * @return the nuovaControPartitaCommand
     */
    public NuovaControPartitaCommand getNuovaControPartitaCommand() {
        if (nuovaControPartitaCommand == null) {
            nuovaControPartitaCommand = new NuovaControPartitaCommand();
        }

        return nuovaControPartitaCommand;
    }

    @Override
    public Collection<ControPartita> loadTableData() {
        controPartiteAllowed = false;
        List<StrutturaContabile> struttureContabili = contabilitaAnagraficaBD.caricaStrutturaContabile(tipoDocumento,
                entita);
        for (StrutturaContabile struttCont : struttureContabili) {
            if (struttCont.getTipologiaConto() == ETipologiaConto.CONTRO_PARTITA) {
                controPartiteAllowed = true;
            }
        }
        listControPartite = new ArrayList<ControPartita>();
        if (controPartiteAllowed) {
            listControPartite = contabilitaAnagraficaBD.caricaControPartite(tipoDocumento, entita);
        }
        return listControPartite;

    }

    @Override
    public void onPostPageOpen() {
        // non faccio niente
    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public void processTableData(Collection<ControPartita> results) {
        super.processTableData(results);

        updateCommand();
    }

    @Override
    public Collection<ControPartita> refreshTableData() {
        return loadTableData();
    }

    /**
     * @param entita
     *            entità
     */
    public void setEntita(EntitaLite entita) {
        this.entita = entita;
    }

    @Override
    public void setFormObject(Object arg0) {
        // non utilizzata, i dati vengono ripuliti dall'esterno tramite la
        // clearData o agComponentgiornati con la
        // loadData
    }

    /**
     * Aggiorna lo stato dei command.
     */
    private void updateCommand() {
        boolean enableCommand = !getTable().getRows().isEmpty();

        getDeleteControPartitaCommand().setEnabled(enableCommand);
        getModificaControPartitaCommand().setEnabled(enableCommand);
        getNuovaControPartitaCommand().setEnabled(controPartiteAllowed);
    }
}
