package it.eurotn.panjea.preventivi.rich.editors.righepreventivo;

import java.awt.Dimension;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Set;

import javax.swing.DropMode;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.JideTable;

import it.eurotn.panjea.magazzino.rich.editors.righemagazzino.AzioneDopoConfermaCommand;
import it.eurotn.panjea.magazzino.rich.editors.righemagazzino.StampaDocumentoDopoConfermaToggleCommand;
import it.eurotn.panjea.preventivi.domain.RigaPreventivo;
import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo;
import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo.StatoAreaPreventivo;
import it.eurotn.panjea.preventivi.rich.bd.IPreventiviBD;
import it.eurotn.panjea.preventivi.util.AreaPreventivoFullDTO;
import it.eurotn.panjea.preventivi.util.RigaArticoloDTO;
import it.eurotn.panjea.preventivi.util.RigaNotaDTO;
import it.eurotn.panjea.preventivi.util.RigaPreventivoDTO;
import it.eurotn.panjea.preventivi.util.RigaTestataDTO;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.IPageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;
import it.eurotn.rich.editors.table.EEditPageMode;
import it.eurotn.rich.editors.table.EditFrame;
import it.eurotn.rich.report.JecReportDocumento;
import it.eurotn.rich.report.JecReportDocumentoBatch;

public class RighePreventivoTablePage extends AbstractTablePageEditor<RigaPreventivoDTO> {

    private class AggiungiVariazioneCommandInterceptor extends OperazioniSuRigheActionCommandInterceptor {
        @Override
        protected void addParameters() {
            aggiungiVariazioneCommand.addParameter(AggiungiVariazioneCommand.AREA_DOCUMENTO_KEY,
                    areaPreventivoFullDTO.getAreaPreventivo().getId());
        }
    }

    private class AumentaLivelloRigaPreventivoCommandInterceptor implements ActionCommandInterceptor {

        @Override
        public void postExecution(ActionCommand command) {
            loadData();
        }

        @Override
        public boolean preExecution(ActionCommand command) {
            Set<Integer> idRigheSelezionate = new HashSet<Integer>();
            List<RigaPreventivoDTO> righeSelezionate = getTable().getSelectedObjects();
            for (RigaPreventivoDTO rigaDTO : righeSelezionate) {
                idRigheSelezionate.add(rigaDTO.getId());
            }
            ((AumentaLivelloRigaCommand) command).addParameter(AumentaLivelloRigaCommand.RIGHE_DA_SPOSTARE,
                    idRigheSelezionate);
            return idRigheSelezionate.size() > 0;
        }
    }

    public class CloseRighePreventivoCommandInterceptor implements ActionCommandInterceptor {

        @Override
        public void postExecution(ActionCommand closeRigheOrdineCmd) {
            AreaPreventivoFullDTO areaPreventivoFullDTOValidata = ((CloseRighePreventivoCommand) closeRigheOrdineCmd)
                    .getAreaFullDTO();

            if (getStampaDocumentoDopoConfermaToggleCommand().isSelected()) {
                JecReportDocumento jecReportDocumento = new JecReportDocumentoBatch(
                        areaPreventivoFullDTO.getAreaPreventivo());
                jecReportDocumento.execute();
            }

            setAreaPreventivoFullDTOOnPages(areaPreventivoFullDTOValidata);
            setFormObject(areaPreventivoFullDTOValidata);

            RighePreventivoTablePage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
                    areaPreventivoFullDTOValidata);
            RighePreventivoTablePage.this.firePropertyChange(VALIDA_RIGHE_PREVENTIVO, false, true);

            // se sono cambiate il numero di righe ( es note automatiche ) ricarico le righe
            if (areaPreventivoFullDTOValidata.getAreaPreventivo().getNumeroRighe() != getTable().getRows().size()) {
                loadData();
                getTable().selectRow(getTable().getRows().size() - 1, null);
                ((JideTable) getTable().getTable()).scrollRowToVisible(getTable().getRows().size() - 1);
            }

            updateCommands();
        }

        @Override
        public boolean preExecution(ActionCommand closeRigheOrdineCmd) {
            ((CloseRighePreventivoCommand) closeRigheOrdineCmd).setAreaFullDTO(areaPreventivoFullDTO);
            // annullo eventuali modifiche sulla pagina del detail, quando inserisco un riga viene
            // preparata la
            // successiva vuota; se confermo le righe e non faccio nulla mi ritrovo ancora preparata
            // la riga nuova
            getEditorUndoCommand().execute();

            RigaNotaAutomaticaGenerator<AreaPreventivo> rigaNotaAutomaticaGenerator = new RigaNotaAutomaticaGenerator<AreaPreventivo>(
                    preventiviBD);
            rigaNotaAutomaticaGenerator.genera(areaPreventivoFullDTO.getAreaPreventivo(), getTable().getRows());

            return true;
        }

    }

    public class EvadiRighePreventivoCommandInterceptor implements ActionCommandInterceptor {

        @Override
        public void postExecution(ActionCommand arg0) {
            loadData();

            AreaPreventivo areaPreventivo = preventiviBD
                    .caricaAreaPreventivo(areaPreventivoFullDTO.getAreaPreventivo());
            areaPreventivoFullDTO.setAreaPreventivo(areaPreventivo);
            setFormObject(areaPreventivoFullDTO);
            RighePreventivoTablePage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
                    areaPreventivoFullDTO);
        }

        @Override
        public boolean preExecution(ActionCommand command) {
            command.addParameter(EvadiRighePreventivoCommand.PARAM_AREA_PREVENTIVO,
                    areaPreventivoFullDTO.getAreaPreventivo());
            return true;
        }

    }

    private class InserisciRaggruppamentoArticoliCommandInterceptor extends OperazioniSuRigheActionCommandInterceptor {
        @Override
        protected void addParameters() {
            inserisciRaggruppamentoArticoliCommand.addParameter(
                    InserisciRaggruppamentoArticoliRighePreventivoCommand.AREA_RATE_KEY,
                    areaPreventivoFullDTO.getAreaRate());

            inserisciRaggruppamentoArticoliCommand.addParameter(
                    InserisciRaggruppamentoArticoliRighePreventivoCommand.AREA_DOCUMENTO_KEY,
                    areaPreventivoFullDTO.getAreaPreventivo());
        }
    }

    private abstract class OperazioniSuRigheActionCommandInterceptor implements ActionCommandInterceptor {

        /**
         * Aggiunge i parametri al command nella preExecution.
         */
        protected abstract void addParameters();

        @Override
        public void postExecution(ActionCommand actioncommand) {
            // HACK Se sto modificando o inserendo un articolo chiamo l'undo altrimenti dopo la
            // selezione viene
            // richiamato il property change che crea una nuova riga articolo
            RighePreventivoTablePage.this.getEditPages().get(RigaArticoloDTO.class.getName()).onUndo();

            int delay = getTable().getDelayForSelection();
            // la refresh via timer agisce sulla tabella inserendo e selezionando un record, poi le
            // operazioni
            // di selezione e di totalizzazione toccano quello che la refresh tocca e si ha una
            // ConcurrentModificationException quindi azzero il timer e alla fine di tutte le
            // operazioni di
            // inserimento
            // di raggruppamento ripristino il tempo di attivazione del timer.

            getTable().setDelayForSelection(0);
            onRefresh();
            // Ricarico l'area ordine perchè potrei aver cambiato stato
            boolean righeValidateBefore = areaPreventivoFullDTO.getAreaPreventivo().getDatiValidazioneRighe().isValid();
            AreaPreventivoFullDTO areaPreventivoResult = preventiviBD
                    .caricaAreaPreventivoFullDTO(areaPreventivoFullDTO.getAreaPreventivo());
            setFormObject(areaPreventivoResult);
            RighePreventivoTablePage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
                    areaPreventivoResult);
            RighePreventivoTablePage.this.firePropertyChange(VALIDA_RIGHE_PREVENTIVO, righeValidateBefore,
                    areaPreventivoResult.getAreaPreventivo().getDatiValidazioneRighe().isValid());
            updateCommands();
            if (getTotalizzaDocumentoCommand().isTotalizzazioneAutomatica()) {
                getTotalizzaDocumentoCommand().execute();
            }
            getTable().setDelayForSelection(delay);
        }

        @Override
        public boolean preExecution(ActionCommand actioncommand) {
            getEditorUndoCommand().execute();
            addParameters();
            return !getTable().getRows().isEmpty();
        }
    }

    public class RicalcolaAreaPreventivoCommand extends ActionCommand {

        public static final String COMMAND_ID = "ricalcolaAreaPreventivoCommand";

        /**
         * Costruttore.
         */
        public RicalcolaAreaPreventivoCommand() {
            super(COMMAND_ID);
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            ConfirmationDialog confirmationDialog = new ConfirmationDialog("Attenzione", "Ricalcolare i prezzi?") {

                @Override
                protected void onConfirm() {
                    int idAreaPreventivo = areaPreventivoFullDTO.getAreaPreventivo().getId();
                    areaPreventivoFullDTO = preventiviBD.ricalcolaPrezziPreventivo(idAreaPreventivo);

                    setFormObject(areaPreventivoFullDTO);
                    refreshData();
                    RighePreventivoTablePage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
                            areaPreventivoFullDTO);
                }
            };
            confirmationDialog.setPreferredSize(new Dimension(330, 80));
            confirmationDialog.showDialog();
        }

    }

    public class TotalizzaDocumentoCommandInterceptor implements ActionCommandInterceptor {

        @Override
        public void postExecution(ActionCommand totalizzaDocumentoCmd) {
            AreaPreventivoFullDTO areaPreventivoFullDTOValidata = ((TotalizzaDocumentoCommand) totalizzaDocumentoCmd)
                    .getAreaPreventivoFullDTO();
            setAreaPreventivoFullDTOOnPages(areaPreventivoFullDTOValidata);
            setFormObject(areaPreventivoFullDTOValidata);
            RighePreventivoTablePage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
                    areaPreventivoFullDTOValidata);
            RighePreventivoTablePage.this.firePropertyChange(VALIDA_RIGHE_PREVENTIVO, null, true);
        }

        @Override
        public boolean preExecution(ActionCommand totalizzaDocumentoCmd) {
            ((TotalizzaDocumentoCommand) totalizzaDocumentoCmd).setAreaPreventivoFullDTO(areaPreventivoFullDTO);
            return true;
        }

    }

    public static final String PAGE_ID = "righePreventivoTablePage";
    public static final String VALIDA_RIGHE_PREVENTIVO = "validaRighePreventivo";

    private static final String KEY_NUOVO_DOPO_CONFERMA = "keyNuovoPreventivoDopoConferma";
    private static final String KEY_STAMPA_DOPO_CONFERMA = "keyStampaPreventivoDopoConferma";
    private AreaPreventivoFullDTO areaPreventivoFullDTO;
    private IPreventiviBD preventiviBD;
    private AzioneDopoConfermaCommand azioneDopoConfermaCommand;

    private RigheDocumentoEditFrame<RigaPreventivo, RigaPreventivoDTO> righePreventivoEditFrame;
    private TotalizzaDocumentoCommand totalizzaDocumentoCommand;
    private TotalizzaDocumentoCommandInterceptor totalizzaDocumentoCommandInterceptor;
    private CloseRighePreventivoCommand closeRighePreventivoCommand;
    private CloseRighePreventivoCommandInterceptor closeRighePreventivoCommandInterceptor;
    private InserisciRaggruppamentoArticoliRighePreventivoCommand inserisciRaggruppamentoArticoliCommand;
    private EvadiRighePreventivoCommand evadiRighePreventivoCommand;

    private EvadiRighePreventivoCommandInterceptor evadiRighePreventivoCommandInterceptor;

    private InserisciRaggruppamentoArticoliCommandInterceptor inserisciRaggruppamentoArticoliCommandInterceptor;

    private AggiungiVariazioneCommand aggiungiVariazioneCommand;
    private OperazioniSuRigheActionCommandInterceptor aggiungiVariazioneCommandInterceptor;
    private RicalcolaAreaPreventivoCommand ricalcolaAreaPreventivoCommand;

    private OperazioniSuRigheActionCommandInterceptor ricalcolaAreaPreventivoCommandInterceptor;

    private StampaDocumentoDopoConfermaToggleCommand stampaDocumentoDopoConfermaToggleCommand;

    private AumentaLivelloRigaPreventivoCommandInterceptor aumentaLivelloRigaPreventivoCommandInterceptor;

    private AumentaLivelloRigaCommand aumentaLivelloRigaCommand;

    /**
     * costruttore.
     */
    public RighePreventivoTablePage() {
        super(PAGE_ID, new RighePreventivoTableModel());
        setEnableAutoResizeRow(true);
        getTable().setDelayForSelection(300);
        getTable().addSelectionObserver(this);
        getTable().getTable().setDragEnabled(true);
        getTable().getTable().setDropMode(DropMode.INSERT_ROWS);
    }

    @Override
    protected EditFrame<RigaPreventivoDTO> createEditFrame() {
        righePreventivoEditFrame = new RigheDocumentoEditFrame<RigaPreventivo, RigaPreventivoDTO>(EEditPageMode.DETAIL,
                this, EditFrame.QUICK_ACTION_DEFAULT);
        return righePreventivoEditFrame;
    }

    @Override
    public void dispose() {
        if (aggiungiVariazioneCommand != null) {
            aggiungiVariazioneCommand.removeCommandInterceptor(getAggiungiVariazioneCommandInterceptor());
        }
        if (ricalcolaAreaPreventivoCommand != null) {
            ricalcolaAreaPreventivoCommand.removeCommandInterceptor(getRicalcolaAreaPreventivoCommandInterceptor());
        }
        if (aumentaLivelloRigaCommand != null) {
            aumentaLivelloRigaCommand.removeCommandInterceptor(getAumentaLivelloRigaPreventivoCommandInterceptor());
        }
        if (closeRighePreventivoCommand != null) {
            closeRighePreventivoCommand.removeCommandInterceptor(getCloseRighePreventivoCommandInterceptor());
        }
        if (inserisciRaggruppamentoArticoliCommand != null) {
            inserisciRaggruppamentoArticoliCommand
                    .removeCommandInterceptor(getInserisciRaggruppamentoArticoliCommandInterceptor());
        }
        if (totalizzaDocumentoCommand != null) {
            totalizzaDocumentoCommand.removeCommandInterceptor(getTotalizzaDocumentoCommandInterceptor());
        }

        if (evadiRighePreventivoCommand != null) {
            evadiRighePreventivoCommand.removeCommandInterceptor(getEvadiRighePreventivoCommandInterceptor());
        }
        super.dispose();
        // righeOrdineEditFrame = null;
    }

    /**
     * @return comando per inserire un raggruppamento articoli
     */
    protected AbstractCommand getAggiungiVariazioneCommand() {
        if (aggiungiVariazioneCommand == null) {
            aggiungiVariazioneCommand = new AggiungiVariazioneCommand();
            aggiungiVariazioneCommand.addCommandInterceptor(getAggiungiVariazioneCommandInterceptor());
        }
        return aggiungiVariazioneCommand;
    }

    /**
     *
     * @return OperazioniSuRigheActionCommandInterceptor
     */
    private OperazioniSuRigheActionCommandInterceptor getAggiungiVariazioneCommandInterceptor() {
        if (aggiungiVariazioneCommandInterceptor == null) {
            aggiungiVariazioneCommandInterceptor = new AggiungiVariazioneCommandInterceptor();
        }
        return aggiungiVariazioneCommandInterceptor;
    }

    /**
     * @return the areaPreventivoFullDTO
     */
    public AreaPreventivoFullDTO getAreaPreventivoFullDTO() {
        return areaPreventivoFullDTO;
    }

    /**
     *
     * @return aumentaLivelloRigaCommand
     */
    private AumentaLivelloRigaCommand getAumentaLivelloRigaCommand() {
        if (aumentaLivelloRigaCommand == null) {
            aumentaLivelloRigaCommand = new AumentaLivelloRigaCommand(preventiviBD);
            aumentaLivelloRigaCommand.addCommandInterceptor(getAumentaLivelloRigaPreventivoCommandInterceptor());
        }
        return aumentaLivelloRigaCommand;
    }

    /**
     *
     * @return aumentaLivelloRigaPreventivoCommandInterceptor
     */
    private AumentaLivelloRigaPreventivoCommandInterceptor getAumentaLivelloRigaPreventivoCommandInterceptor() {
        if (aumentaLivelloRigaPreventivoCommandInterceptor == null) {
            this.aumentaLivelloRigaPreventivoCommandInterceptor = new AumentaLivelloRigaPreventivoCommandInterceptor();
        }
        return aumentaLivelloRigaPreventivoCommandInterceptor;
    }

    /**
     * @return the azioneDopoConfermaCommand
     */
    public AzioneDopoConfermaCommand getAzioneDopoConfermaCommand() {
        if (azioneDopoConfermaCommand == null) {
            azioneDopoConfermaCommand = new AzioneDopoConfermaCommand();
        }
        return azioneDopoConfermaCommand;
    }

    /**
     * @return the closeRigheOrdineCommand
     */
    public CloseRighePreventivoCommand getCloseRighePreventivoCommand() {
        if (closeRighePreventivoCommand == null) {
            closeRighePreventivoCommand = new CloseRighePreventivoCommand(preventiviBD, PAGE_ID);
            closeRighePreventivoCommand.addCommandInterceptor(getCloseRighePreventivoCommandInterceptor());
        }
        return closeRighePreventivoCommand;
    }

    /**
     * @return closeRigheOrdineCommandInterceptor
     */
    private CloseRighePreventivoCommandInterceptor getCloseRighePreventivoCommandInterceptor() {
        if (closeRighePreventivoCommandInterceptor == null) {
            closeRighePreventivoCommandInterceptor = new CloseRighePreventivoCommandInterceptor();
        }
        return closeRighePreventivoCommandInterceptor;
    }

    @Override
    public AbstractCommand[] getCommands() {
        AbstractCommand[] commands = new AbstractCommand[] { getEvadiRighePreventivoCommand(), getEditorDeleteCommand(),
                getAumentaLivelloRigaCommand(), getRicalcolaAreaPreventivoCommand(), getAggiungiVariazioneCommand(),
                getInserisciRaggruppamentoArticoliCommand(), getTotalizzaDocumentoCommand(),
                getAzioneDopoConfermaCommand(), getStampaDocumentoDopoConfermaToggleCommand(),
                getCloseRighePreventivoCommand(), getRefreshCommand() };
        return commands;
    }

    @Override
    public AbstractCommand getEditorNewCommand() {
        return getNewCommands().get(RigaArticoloDTO.class.getName());
    }

    /**
     * @return the evadiRighePreventivoCommand
     */
    private EvadiRighePreventivoCommand getEvadiRighePreventivoCommand() {
        if (evadiRighePreventivoCommand == null) {
            evadiRighePreventivoCommand = new EvadiRighePreventivoCommand();
            evadiRighePreventivoCommand.addCommandInterceptor(getEvadiRighePreventivoCommandInterceptor());
        }

        return evadiRighePreventivoCommand;
    }

    /**
     * @return the evadiRighePreventivoCommandInterceptor
     */
    private EvadiRighePreventivoCommandInterceptor getEvadiRighePreventivoCommandInterceptor() {
        if (evadiRighePreventivoCommandInterceptor == null) {
            evadiRighePreventivoCommandInterceptor = new EvadiRighePreventivoCommandInterceptor();
        }

        return evadiRighePreventivoCommandInterceptor;
    }

    /**
     * @return comando per inserire un raggruppamento articoli
     */
    protected AbstractCommand getInserisciRaggruppamentoArticoliCommand() {
        if (inserisciRaggruppamentoArticoliCommand == null) {
            inserisciRaggruppamentoArticoliCommand = new InserisciRaggruppamentoArticoliRighePreventivoCommand();
            inserisciRaggruppamentoArticoliCommand
                    .addCommandInterceptor(getInserisciRaggruppamentoArticoliCommandInterceptor());
        }
        return inserisciRaggruppamentoArticoliCommand;
    }

    /**
     *
     * @return inserisciRaggruppamentoArticoliCommandInterceptor
     */
    private InserisciRaggruppamentoArticoliCommandInterceptor getInserisciRaggruppamentoArticoliCommandInterceptor() {
        if (inserisciRaggruppamentoArticoliCommandInterceptor == null) {
            inserisciRaggruppamentoArticoliCommandInterceptor = new InserisciRaggruppamentoArticoliCommandInterceptor();
        }
        return inserisciRaggruppamentoArticoliCommandInterceptor;
    }

    @Override
    public Object getManagedObject(Object pageObject) {

        if (pageObject instanceof RigaPreventivo) {
            areaPreventivoFullDTO.setAreaPreventivo(((RigaPreventivo) pageObject).getAreaPreventivo());
            // Quando una riga magazzino cambia (insert/edit/delete), se il
            // comando di totalizzazione è automatico lo lancio
            if (getTotalizzaDocumentoCommand().isTotalizzazioneAutomatica()) {
                getTotalizzaDocumentoCommand().setAreaPreventivoFullDTO(areaPreventivoFullDTO);
                getTotalizzaDocumentoCommand().execute();
            }
            return areaPreventivoFullDTO;
        }

        return null;
    }

    /**
     *
     * @return ricalcolaAreaPreventivoCommand
     */
    private ActionCommand getRicalcolaAreaPreventivoCommand() {
        if (ricalcolaAreaPreventivoCommand == null) {
            ricalcolaAreaPreventivoCommand = new RicalcolaAreaPreventivoCommand();
            ricalcolaAreaPreventivoCommand.addCommandInterceptor(getRicalcolaAreaPreventivoCommandInterceptor());
        }
        return ricalcolaAreaPreventivoCommand;
    }

    /**
     *
     * @return ricalcolaAreaPreventivoCommandInterceptor
     */
    private ActionCommandInterceptor getRicalcolaAreaPreventivoCommandInterceptor() {
        if (ricalcolaAreaPreventivoCommandInterceptor == null) {
            ricalcolaAreaPreventivoCommandInterceptor = new OperazioniSuRigheActionCommandInterceptor() {
                @Override
                protected void addParameters() {
                }
            };
        }
        return ricalcolaAreaPreventivoCommandInterceptor;
    }

    /**
     * @return the stampaDocumentoDopoConfermaToggleCommand
     */
    public StampaDocumentoDopoConfermaToggleCommand getStampaDocumentoDopoConfermaToggleCommand() {
        if (stampaDocumentoDopoConfermaToggleCommand == null) {
            stampaDocumentoDopoConfermaToggleCommand = new StampaDocumentoDopoConfermaToggleCommand();
        }

        return stampaDocumentoDopoConfermaToggleCommand;
    }

    /**
     * @return the totalizzaDocumentoCommand
     */
    public TotalizzaDocumentoCommand getTotalizzaDocumentoCommand() {
        if (totalizzaDocumentoCommand == null) {
            totalizzaDocumentoCommand = new TotalizzaDocumentoCommand(this.preventiviBD);
            totalizzaDocumentoCommand.addCommandInterceptor(getTotalizzaDocumentoCommandInterceptor());
        }
        return totalizzaDocumentoCommand;
    }

    /**
     * @return totalizzaDocumentoCommandInterceptor
     */
    private TotalizzaDocumentoCommandInterceptor getTotalizzaDocumentoCommandInterceptor() {
        if (totalizzaDocumentoCommandInterceptor == null) {
            totalizzaDocumentoCommandInterceptor = new TotalizzaDocumentoCommandInterceptor();
        }
        return totalizzaDocumentoCommandInterceptor;
    }

    @Override
    public Collection<RigaPreventivoDTO> loadTableData() {
        if (getAreaPreventivoFullDTO().getId() != null) {
            return preventiviBD.caricaRighePreventivoDTO(getAreaPreventivoFullDTO().getAreaPreventivo());
        }

        return Collections.emptyList();
    }

    @Override
    public void onPostPageOpen() {
    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public void processTableData(Collection<RigaPreventivoDTO> results) {
        // Devo ricaricarmi le righe solamente se cambio l'area preventivo.
        // Se l'area e' la stessa non ricarico le righe.
        // Nella setFormObject gia' setto il riferimento al nuovo oggetto.
        // Azzero mentre carico le righe
        setEnabled(true);
        int selectRow = getTable().getTable().getSelectedRow();
        if (results != null && results.size() > 0 && selectRow == -1) {
            selectRow = 0;
        }
        setRows(results, false);
        getTable().selectRow(selectRow, null);
        // abilita/disabilita table page
        updateControl();
        updateCommands();
    }

    @Override
    public Collection<RigaPreventivoDTO> refreshTableData() {
        if (getAreaPreventivoFullDTO().getId() == null) {
            return Collections.emptyList();
        }

        return null;
    }

    @Override
    public void restoreState(Settings settings) {
        if (settings.contains(KEY_NUOVO_DOPO_CONFERMA)) {
            getAzioneDopoConfermaCommand().setSelected(settings.getBoolean(KEY_NUOVO_DOPO_CONFERMA));
        }
        if (settings.contains(KEY_STAMPA_DOPO_CONFERMA)) {
            getStampaDocumentoDopoConfermaToggleCommand().setSelected(settings.getBoolean(KEY_STAMPA_DOPO_CONFERMA));
        }
        if (settings.contains(TotalizzaDocumentoCommand.KEY_TOTALIZZAZIONE_AUTOMATICA)) {
            getTotalizzaDocumentoCommand().setTotalizzazioneAutomatica(
                    settings.getBoolean(TotalizzaDocumentoCommand.KEY_TOTALIZZAZIONE_AUTOMATICA));
        }
        super.restoreState(settings);
    }

    @Override
    public void saveState(Settings settings) {
        settings.setBoolean(KEY_NUOVO_DOPO_CONFERMA, getAzioneDopoConfermaCommand().isSelected());
        // settings.setBoolean(KEY_STAMPA_DOPO_CONFERMA,
        // getStampaDocumentoDopoConfermaToggleCommand().isSelected());
        settings.setBoolean(TotalizzaDocumentoCommand.KEY_TOTALIZZAZIONE_AUTOMATICA,
                getTotalizzaDocumentoCommand().isTotalizzazioneAutomatica());
        super.saveState(settings);
    }

    /**
     * @param areaPreventivoFullDTO
     *            the areaPreventivoFullDTO to set
     */
    public void setAreaPreventivoFullDTO(AreaPreventivoFullDTO areaPreventivoFullDTO) {
        this.areaPreventivoFullDTO = areaPreventivoFullDTO;
    }

    /**
     *
     * @param areaDTO
     *            areaPreventivoFullDTO
     */
    private void setAreaPreventivoFullDTOOnPages(AreaPreventivoFullDTO areaDTO) {
        RigaArticoloPage rigaArticoloPage = (RigaArticoloPage) getEditPages().get(RigaArticoloDTO.class.getName());
        rigaArticoloPage.setAreaPreventivoFullDTO(areaDTO);

        RigaNotaPage rigaNotaPage = (RigaNotaPage) getEditPages().get(RigaNotaDTO.class.getName());
        rigaNotaPage.setAreaPreventivoFullDTO(areaDTO);

        RigaTestataPage rigaTestataPage = (RigaTestataPage) getEditPages().get(RigaTestataDTO.class.getName());
        rigaTestataPage.setAreaPreventivoFullDTO(areaDTO);
    }

    @Override
    public void setFormObject(Object object) {
        this.setAreaPreventivoFullDTO((AreaPreventivoFullDTO) object);

        for (Entry<String, IPageEditor> entryPages : getEditPages().entrySet()) {

            if (entryPages.getKey().equals(RigaArticoloDTO.class.getName())) {
                ((RigaArticoloPage) entryPages.getValue()).setAreaPreventivoFullDTO(this.getAreaPreventivoFullDTO());
            } else {
                ((AbstractRigaPreventivoPage<?>) entryPages.getValue())
                        .setAreaPreventivoFullDTO(getAreaPreventivoFullDTO());
            }
        }
        updateControl();
        updateCommands();
    }

    /**
     * @param preventiviBD
     *            the preventiviBD to set
     */
    public void setPreventiviBD(IPreventiviBD preventiviBD) {
        this.preventiviBD = preventiviBD;
        getTable().getTable().setTransferHandler(new RigheDocumentoTransferHandler<AreaPreventivo, RigaPreventivoDTO>(
                preventiviBD, this, new RigaPreventivoDTO[0]));
    }

    @Override
    public void update(Observable o, Object obj) {
        updateCommands();
    }

    /**
     * Aggiorna i comandi della pagina.
     */
    protected void updateCommands() {
        logger.debug("--> Enter updateCommands");
        boolean enabled = getTable().getRows().size() != 0;
        getEditorDeleteCommand().setEnabled(enabled);
        getRefreshCommand().setEnabled(true);
        getEvadiRighePreventivoCommand().setEnabled(getAreaPreventivoFullDTO().getAreaPreventivo()
                .getStatoAreaPreventivo() == StatoAreaPreventivo.ACCETTATO);

        boolean confermaEnable = !getAreaPreventivoFullDTO().getAreaPreventivo().getDatiValidazioneRighe().isValid()
                && enabled;
        boolean isNewAreaPreventivo = getAreaPreventivoFullDTO().isNew();
        if (isNewAreaPreventivo) {
            confermaEnable = false;
        }

        getCloseRighePreventivoCommand().setEnabled(confermaEnable);

        boolean totalizzaEnabled = true;

        totalizzaEnabled = totalizzaEnabled && enabled;

        boolean statiEditEnable = getAreaPreventivoFullDTO().getAreaPreventivo()
                .getStatoAreaPreventivo() == StatoAreaPreventivo.PROVVISORIO
                || getAreaPreventivoFullDTO().getAreaPreventivo()
                        .getStatoAreaPreventivo() == StatoAreaPreventivo.IN_ATTESA;

        getTotalizzaDocumentoCommand().setVisible(totalizzaEnabled && statiEditEnable);
        getTotalizzaDocumentoCommand().setEnabled(totalizzaEnabled && statiEditEnable);
        getInserisciRaggruppamentoArticoliCommand().setEnabled(!isNewAreaPreventivo && statiEditEnable);
        getAggiungiVariazioneCommand().setEnabled(!isNewAreaPreventivo && statiEditEnable);
        getRicalcolaAreaPreventivoCommand().setEnabled(!isNewAreaPreventivo && statiEditEnable);
        getAumentaLivelloRigaCommand().setEnabled(!isNewAreaPreventivo && statiEditEnable);
        logger.debug("--> Exit updateCommands");
    }

    /**
     * ..
     */
    protected void updateControl() {
        boolean readonly = getAreaPreventivoFullDTO().isNew()
                || getAreaPreventivoFullDTO().getAreaPreventivo().isAccettato();
        setReadOnly(readonly);
    }

}
