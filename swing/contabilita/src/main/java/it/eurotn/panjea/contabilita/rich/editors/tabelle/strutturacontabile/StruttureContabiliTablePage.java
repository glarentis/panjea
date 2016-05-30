package it.eurotn.panjea.contabilita.rich.editors.tabelle.strutturacontabile;

import java.awt.Dimension;
import java.util.Collection;
import java.util.Locale;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.CommandGroup;
import org.springframework.richclient.dialog.ApplicationDialog;
import org.springframework.richclient.dialog.DialogPage;
import org.springframework.richclient.dialog.InputApplicationDialog;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.closure.Closure;

import it.eurotn.locking.IDefProperty;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.contabilita.domain.ETipologiaConto;
import it.eurotn.panjea.contabilita.domain.StrutturaContabile;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD;
import it.eurotn.panjea.contabilita.rich.editors.tabelle.StrutturaContabilePage;
import it.eurotn.panjea.contabilita.rich.pm.StrutturaContabilePM;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.IFormPageEditor;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;

/**
 * Gestisce la tabella delle scritture contabili del tipo area documento.
 *
 * @author fattazzo
 * @version 1.0, 29/ago/07
 *
 */
public class StruttureContabiliTablePage extends AbstractTablePageEditor<StrutturaContabile> {

    private final class ControPartitaInputDialog extends InputApplicationDialog {

        private static final String ID_DIALOG = "controPartitaInputDialog";
        private StrutturaContabile strutturaContabile;

        /**
         * Costruttore.
         *
         * @param strutturaContabile
         *            struttura da gestire
         */
        private ControPartitaInputDialog(final StrutturaContabile strutturaContabile) {
            super(strutturaContabile, "ordine", true);
            this.strutturaContabile = strutturaContabile;
            setInputLabelMessage(RcpSupport.getMessage(PAGE_ID + ".controPartitaInputDialog.ordine.label"));

            setFinishAction(new Closure() {

                @Override
                public Object call(Object newOrder) {
                    Integer newORderInt = Integer.valueOf((String) newOrder);
                    strutturaContabile.setOrdine(newORderInt);
                    StrutturaContabile strutturaContabileSalvata = contabilitaAnagraficaBD
                            .salvaStrutturaContabile(ControPartitaInputDialog.this.strutturaContabile);

                    getTable().replaceOrAddRowObject(strutturaContabileSalvata, strutturaContabileSalvata, null);
                    updateCommand();
                    StruttureContabiliTablePage.this.firePropertyChange(CONTROPARTITA_STRUTTURA_CONTABILE_CHANGE, null,
                            strutturaContabileSalvata);
                    return null;
                }
            });
            setTitle(getMessage(ID_DIALOG + ".title.label"));
        }
    }

    private class DeleteStrutturaContropartitaCommand extends ActionCommand {

        public static final String COMMAND_ID = "deleteCommand";

        /**
         * Costruttore.
         *
         */
        public DeleteStrutturaContropartitaCommand() {
            super(COMMAND_ID);
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            StrutturaContabile strutturaContabile = getTable().getSelectedObject();
            if (strutturaContabile != null) {
                contabilitaAnagraficaBD.cancellaStrutturaContabile(strutturaContabile);
                getTable().removeRowObject(strutturaContabile);
                updateCommand();

                // se ho cancellato una contro partita riabilito il pulsante
                if (strutturaContabile.getTipologiaConto() == ETipologiaConto.CONTRO_PARTITA) {
                    getNuovaControPartitaCommand().setEnabled(true);
                    StruttureContabiliTablePage.this.firePropertyChange(CONTROPARTITA_STRUTTURA_CONTABILE_CHANGE, null,
                            null);
                }
            }
        }

    }

    private class ModificaStrutturaContropartitaCommand extends ActionCommand {

        public static final String COMMAND_ID = "lockCommand";

        /**
         * Costruttore.
         *
         */
        public ModificaStrutturaContropartitaCommand() {
            super(COMMAND_ID);
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            StrutturaContabile selectedObject = getTable().getSelectedObject();
            if (selectedObject == null) {
                return;
            }

            ApplicationDialog dialog = null;
            if (selectedObject.getTipologiaConto() == ETipologiaConto.CONTRO_PARTITA) {
                dialog = new ControPartitaInputDialog(selectedObject);
            } else {
                StrutturaContabilePM strutturaContabilePM = new StrutturaContabilePM(selectedObject);
                dialog = new StrutturaContabileDialog(strutturaContabilePM);
            }
            dialog.showDialog();
        }
    }

    private class NuovaControPartitaCommand extends ActionCommand {

        private static final String NUOVA_CONTRO_PARTITA_COMMAND_ID = ".nuovaControPartitaCommand";

        /**
         * Costruttore.
         *
         */
        public NuovaControPartitaCommand() {
            super(PAGE_ID + NUOVA_CONTRO_PARTITA_COMMAND_ID);
            setSecurityControllerId(PAGE_ID + NUOVA_CONTRO_PARTITA_COMMAND_ID);
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            StrutturaContabile strutturaContabile = new StrutturaContabile();
            strutturaContabile.setTipoDocumento(tipoDocumento);
            strutturaContabile.setEntita(entita);
            strutturaContabile.setTipologiaConto(ETipologiaConto.CONTRO_PARTITA);
            strutturaContabile
                    .setDare(getMessageSource().getMessage(CONTRO_PARTITA_LABEL, new Object[] {}, Locale.getDefault()));
            strutturaContabile.setAvere(
                    getMessageSource().getMessage(CONTRO_PARTITA_LABEL, new Object[] {}, Locale.getDefault()));

            ControPartitaInputDialog dialog = new ControPartitaInputDialog(strutturaContabile);
            dialog.showDialog();

            this.setEnabled(false);
        }
    }

    private class NuovaStrutturaCommand extends ActionCommand {

        public static final String COMMAND_ID = "newCommand";

        /**
         * Costruttore.
         *
         */
        public NuovaStrutturaCommand() {
            super(COMMAND_ID);
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            StrutturaContabile strutturaContabile = new StrutturaContabile();
            strutturaContabile.setTipoDocumento(tipoDocumento);
            strutturaContabile.setEntita(entita);
            StrutturaContabilePM strutturaContabilePM = new StrutturaContabilePM(strutturaContabile);
            StrutturaContabileDialog dialog = new StrutturaContabileDialog(strutturaContabilePM);
            dialog.showDialog();
        }
    }

    /**
     * Dialog di modifica dei record tabella.
     *
     * @author Leonardo
     */
    protected class StrutturaContabileDialog extends PanjeaTitledPageApplicationDialog {

        private boolean commit = false;

        /**
         * Costruttore.
         *
         * @param object
         *            struttura contabile da gestire
         */
        public StrutturaContabileDialog(final StrutturaContabilePM object) {
            super();
            setPreferredSize(new Dimension(1000, 400));
            DialogPage page = new StrutturaContabilePage(contabilitaAnagraficaBD);
            org.springframework.util.Assert.isInstanceOf(IFormPageEditor.class, page,
                    "La dialog page deve implementare IFormPageEditor");

            // sia che l'oggetto sia nuovo o meno, devo settare la
            // strutturaContabile dall'esterno dato
            // che ad essa devo settare il tipoDocumento che mi serve per le
            // verifiche del
            // caso nella strutturaContabileForm
            ((IFormPageEditor) page).setFormObject(object);
            this.setDialogPage(page);
            this.setTitle(getMessage("strutturaContabileDialog.title"));
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
            StrutturaContabilePM property = (StrutturaContabilePM) page.getForm().getFormObject();
            boolean finished = false;

            if (property.isValid()) {
                finished = page.onSave();
                if (finished) {
                    getTable().replaceOrAddRowObject(property.getStrutturaContabile(), property.getStrutturaContabile(),
                            null);
                    updateCommand();
                }
                commit = true;
            } else {
                String title = getMessage(PAGE_ID + DIALOG_ERROR_VALIDATION_TITLE);
                String message = getMessage(PAGE_ID + DIALOG_ERROR_VALIDATION_MESSAGE);
                MessageDialog dialog = new MessageDialog(title, message);
                dialog.showDialog();
            }
            return finished;
        }
    }

    private static final String PAGE_ID = "struttureContabiliTablePage";
    private static final String NUOVO_COMMAND_ID = ".newCommand";
    private static final String CONTRO_PARTITA_LABEL = PAGE_ID + ".controPartita.label";

    private static final String DIALOG_ERROR_VALIDATION_MESSAGE = ".errorValidationDialog.message";

    private static final String DIALOG_ERROR_VALIDATION_TITLE = ".errorValidationDialog.title";
    public static final String CONTROPARTITA_STRUTTURA_CONTABILE_CHANGE = "controPartitaStrutturaContabileChange";
    private EntitaLite entita = null;
    private final IContabilitaAnagraficaBD contabilitaAnagraficaBD;

    private final TipoDocumento tipoDocumento;

    private NuovaControPartitaCommand nuovaControPartitaCommand;
    private NuovaStrutturaCommand nuovaStrutturaCommand;
    private ModificaStrutturaContropartitaCommand modificaStrutturaContropartitaCommand;
    private DeleteStrutturaContropartitaCommand deleteStrutturaContropartitaCommand;

    /**
     * Costruttore.
     *
     * @param tipoDocumento
     *            tipo documento di riferimento per il caricamento della struttura
     * @param contabilitaAnagraficaBD
     *            contabilitaAnagraficaBD
     */
    public StruttureContabiliTablePage(final TipoDocumento tipoDocumento,
            final IContabilitaAnagraficaBD contabilitaAnagraficaBD) {
        super(PAGE_ID, new String[] { "ordine", "formula", "descrizioneDare", "descrizioneAvere" },
                StrutturaContabile.class);
        this.tipoDocumento = tipoDocumento;
        this.contabilitaAnagraficaBD = contabilitaAnagraficaBD;
        this.entita = null;
        getTable().setPropertyCommandExecutor(getModificaStrutturaContropartitaCommand());
    }

    @Override
    public AbstractCommand[] getCommands() {
        CommandGroup commandGroup = new CommandGroup(PAGE_ID + NUOVO_COMMAND_ID);
        RcpSupport.configure(commandGroup);
        commandGroup.add(getNuovaStrutturaCommand());
        commandGroup.add(getNuovaControPartitaCommand());

        return new AbstractCommand[] { commandGroup, getModificaStrutturaContropartitaCommand(),
                getDeleteStrutturaContropartitaCommand(), getRefreshCommand() };
    }

    /**
     * @return the deleteStrutturaContropartitaCommand
     */
    public DeleteStrutturaContropartitaCommand getDeleteStrutturaContropartitaCommand() {
        if (deleteStrutturaContropartitaCommand == null) {
            deleteStrutturaContropartitaCommand = new DeleteStrutturaContropartitaCommand();
        }

        return deleteStrutturaContropartitaCommand;
    }

    @Override
    public Object getManagedObject(Object pageObject) {

        if (pageObject instanceof StrutturaContabilePM) {
            return ((StrutturaContabilePM) pageObject).getStrutturaContabile();
        } else {
            return pageObject;
        }
    }

    /**
     * @return the modificaStrutturaContropartitaCommand
     */
    public ModificaStrutturaContropartitaCommand getModificaStrutturaContropartitaCommand() {
        if (modificaStrutturaContropartitaCommand == null) {
            modificaStrutturaContropartitaCommand = new ModificaStrutturaContropartitaCommand();
        }

        return modificaStrutturaContropartitaCommand;
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

    /**
     * @return the nuovaStrutturaCommand
     */
    public NuovaStrutturaCommand getNuovaStrutturaCommand() {
        if (nuovaStrutturaCommand == null) {
            nuovaStrutturaCommand = new NuovaStrutturaCommand();
        }

        return nuovaStrutturaCommand;
    }

    @Override
    public Collection<StrutturaContabile> loadTableData() {
        return contabilitaAnagraficaBD.caricaStrutturaContabile(tipoDocumento, entita);
    }

    @Override
    public void onPostPageOpen() {
        // Non facico niente
    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public void processTableData(Collection<StrutturaContabile> results) {
        super.processTableData(results);

        updateCommand();

        getNuovaControPartitaCommand().setEnabled(true);
        // se trova una riga di tipo Contropartita disabilita il command che
        // permette di creare una contropartita
        for (StrutturaContabile strutturaContabile : results) {
            if (strutturaContabile.getTipologiaConto() == ETipologiaConto.CONTRO_PARTITA) {
                getNuovaControPartitaCommand().setEnabled(false);
            }
        }
    }

    @Override
    public Collection<StrutturaContabile> refreshTableData() {
        return loadTableData();
    }

    /**
     * @param entita
     *            entita di riferimento
     */
    public void setEntita(EntitaLite entita) {
        this.entita = entita;
    }

    @Override
    public void setFormObject(Object object) {
        // non utilizzata, i dati vengono ripuliti dall'esterno tramite la
        // clearData o aggiornati con la loadData
    }

    /**
     * Aggiorna lo stato dei pulsanti in base alla struttura contabile gestita.
     */
    private void updateCommand() {
        boolean enableCommand = !getTable().getRows().isEmpty();

        getDeleteStrutturaContropartitaCommand().setEnabled(enableCommand);
        getModificaStrutturaContropartitaCommand().setEnabled(enableCommand);
    }
}
