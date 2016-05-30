package it.eurotn.panjea.magazzino.rich.editors.righemagazzino;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.DropMode;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandExecutor;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.command.TargetableActionCommand;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.JideTable;
import com.jidesoft.grid.TableModelWrapperUtils;

import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino.StatoAreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.StrategiaEsportazioneFlusso;
import it.eurotn.panjea.magazzino.exception.EsportaDocumentoCassaException;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.forms.rigamagazzino.AggiornaListinoCommand;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.magazzino.util.RigaArticoloDTO;
import it.eurotn.panjea.magazzino.util.RigaArticoloDistintaDTO;
import it.eurotn.panjea.magazzino.util.RigaMagazzinoDTO;
import it.eurotn.panjea.magazzino.util.RigaNotaDTO;
import it.eurotn.panjea.magazzino.util.RigaTestataDTO;
import it.eurotn.panjea.magazzino.util.rigamagazzino.builder.RigheMagazzinoDTOResult;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.rich.command.ICancellable;
import it.eurotn.rich.command.JideToggleCommand;
import it.eurotn.rich.command.support.JecGlobalCommandIds;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;
import it.eurotn.rich.editors.table.EEditPageMode;
import it.eurotn.rich.editors.table.EditFrame;
import it.eurotn.rich.report.JecReportDocumento;
import it.eurotn.rich.report.JecReportDocumentoMagazzinoBatch;

public class RigheMagazzinoTablePage extends AbstractTablePageEditor<RigaMagazzinoDTO>
        implements InitializingBean, Observer {

    private class AggiornaListinoCommandInterceptor implements ActionCommandInterceptor {
        @Override
        public void postExecution(ActionCommand actioncommand) {
        }

        @Override
        public boolean preExecution(ActionCommand actioncommand) {
            List<RigaMagazzinoDTO> righe = getTable().getRows();
            actioncommand.addParameter(AggiornaListinoCommand.PARAMETER_RIGHE_MAGAZZINO_DTO, righe);
            actioncommand.addParameter(AggiornaListinoCommand.PARAMETER_AREA_MAGAZZINO,
                    areaMagazzinoFullDTO.getAreaMagazzino());
            return true;
        }
    }

    private class AumentaLivelloRigaMagazzinoCommandInterceptor implements ActionCommandInterceptor {
        @Override
        public void postExecution(ActionCommand command) {
            loadData();
        }

        @Override
        public boolean preExecution(ActionCommand command) {
            Set<Integer> righeMagazzino = new HashSet<Integer>();
            List<RigaMagazzinoDTO> righeSelezionate = getTable().getSelectedObjects();
            for (RigaMagazzinoDTO rigaMagazzinoDTO : righeSelezionate) {
                righeMagazzino.add(rigaMagazzinoDTO.getId());
            }
            ((AumentaLivelloRigaMagazzinoCommand) command)
                    .addParameter(AumentaLivelloRigaMagazzinoCommand.RIGHE_ORDINE_PARAM, righeMagazzino);
            return righeMagazzino.size() > 0;
        }
    }

    public class CloseRigheMagazzinoCommandInterceptor implements ActionCommandInterceptor {

        @Override
        public void postExecution(ActionCommand closeRigheMagazzinoCmd) {
            AreaMagazzinoFullDTO areaMagazzinoFullDTOValidata = ((CloseRigheMagazzinoCommand) closeRigheMagazzinoCmd)
                    .getAreaMagazzinoFullDTO();

            if (getStampaDocumentoDopoConfermaToggleCommand().isSelected()) {
                JecReportDocumento jecReportDocumento = new JecReportDocumentoMagazzinoBatch(
                        areaMagazzinoFullDTO.getAreaMagazzino());
                jecReportDocumento.setStampaPrezzi(areaMagazzinoFullDTO.getAreaMagazzino().isStampaPrezzi());
                jecReportDocumento.execute();
            }
            ((RigaArticoloPage) getEditPages().get(RigaArticoloDTO.class.getName()))
                    .setAreaMagazzinoFullDTO(areaMagazzinoFullDTOValidata);
            ((RigaArticoloPage) getEditPages().get(RigaArticoloDistintaDTO.class.getName()))
                    .setAreaMagazzinoFullDTO(areaMagazzinoFullDTOValidata);
            ((RigaNotaPage) getEditPages().get(RigaNotaDTO.class.getName()))
                    .setAreaMagazzinoFullDTO(areaMagazzinoFullDTOValidata);

            setFormObject(areaMagazzinoFullDTOValidata);
            RigheMagazzinoTablePage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
                    areaMagazzinoFullDTOValidata);
            RigheMagazzinoTablePage.this.firePropertyChange(VALIDA_RIGHE_MAGAZZINO, false, true);

            // Se sono cambiate il numero di righe (ad.esempio se creo articoli trasporto o conai)
            // ricarico le righe e
            // mi posiziono sull'ultima.
            if (areaMagazzinoFullDTOValidata.getAreaMagazzino().getNumeroRighe() != getTable().getRows().size()) {
                loadData();
                getTable().selectRow(getTable().getRows().size() - 1, null);
                ((JideTable) getTable().getTable()).scrollRowToVisible(getTable().getRows().size() - 1);
            }
            updateCommands();
        }

        @Override
        public boolean preExecution(ActionCommand closeRigheMagazzinoCmd) {
            // annullo eventuali modifiche sulla pagina del detail, quando inserisco un riga viene
            // preparata la
            // successiva vuota; se confermo le righe e non faccio nulla mi ritrovo ancora preparata
            // la riga nuova
            getEditorUndoCommand().execute();

            RigaNotaAutomaticaGenerator rigaNotaAutomaticaGenerator = new RigaNotaAutomaticaGenerator();
            rigaNotaAutomaticaGenerator.genera(areaMagazzinoFullDTO.getAreaMagazzino(), getTable().getRows());

            ((CloseRigheMagazzinoCommand) closeRigheMagazzinoCmd).setAreaMagazzinoFullDTO(areaMagazzinoFullDTO);

            // NPE mail: non sono riuscito a replicare l'errore ricevuto, dovrebbe comunque essere
            // causato dal fatto che
            // l'area magazzino trattata è vuota. Controllo per l'id dell'area magazzino e per
            // valori a null in caso
            // fosse la strategia di esportazione (cosa improbabile e comunque sul database risulta
            // essere valorizzata).
            if (areaMagazzinoFullDTO.getAreaMagazzino().getId() != null) {

                StrategiaEsportazioneFlusso flusso = (areaMagazzinoFullDTO.getAreaMagazzino() != null
                        && areaMagazzinoFullDTO.getAreaMagazzino().getTipoAreaMagazzino() != null)
                                ? areaMagazzinoFullDTO.getAreaMagazzino().getTipoAreaMagazzino()
                                        .getStrategiaEsportazioneFlusso()
                                : null;

                // Esporto il documento
                if (flusso != null && flusso != StrategiaEsportazioneFlusso.NESSUNO
                        && getEsportaDocumentoToggleCommand().isSelected()) {
                    try {
                        magazzinoDocumentoBD.esportaDocumento(areaMagazzinoFullDTO.getAreaMagazzino());
                    } catch (EsportaDocumentoCassaException e) {
                        MessageDialog messagio = new MessageDialog("ERRORE NELLA ESPORTAZIONE",
                                "Articolo:" + e.getDescrizione() + ", " + e.getErrore());
                        messagio.showDialog();
                        return false;
                    }
                }
                // se ddt setto la data di inizio trasporto
                if (areaMagazzinoFullDTO.getAreaMagazzino().getDataInizioTrasporto() == null) {
                    Calendar calendarInizio = Calendar.getInstance();
                    if (getDataTrasportoPosticipataToggleCommand().isSelected()) {
                        calendarInizio.add(Calendar.DAY_OF_MONTH, 1);
                    }
                    areaMagazzinoFullDTO.getAreaMagazzino().setDataInizioTrasporto(calendarInizio.getTime());
                }
                return true;
            } else {
                logger.error("NV.MAIL NPE: CloseRigheMagazzinoCommandInterceptor preExecution verifico area magazzino "
                        + areaMagazzinoFullDTO);
                // se non ho una area magazzino salvata non posso confermare le righe quindi
                // impedisco l'esecuzione del
                // command
                return false;
            }
        }
    }

    public class ImportaRigheOrdineCommandInterceptor implements ActionCommandInterceptor {

        @Override
        public void postExecution(ActionCommand cmd) {
            try {
                getTable().removeSelectionObserver(RigheMagazzinoTablePage.this);
                RigaMagazzinoDTO rigaMagazzinoDTO = getTable().getSelectedObject();
                getTotalizzaDocumentoCommand().execute();
                getRefreshCommand().execute();
                if (rigaMagazzinoDTO != null) {
                    getTable().selectRowObject(rigaMagazzinoDTO, RigheMagazzinoTablePage.this);
                }

                // rilancio l'area magazzino perchè può essere cambiato lo stato dell'area con
                // l'inserimento
                AreaMagazzino areaMagazzino = magazzinoDocumentoBD
                        .caricaAreaMagazzino(areaMagazzinoFullDTO.getAreaMagazzino());
                areaMagazzinoFullDTO.setAreaMagazzino(areaMagazzino);
                firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, new Integer(-1), areaMagazzinoFullDTO);

            } finally {
                getTable().addSelectionObserver(RigheMagazzinoTablePage.this);
            }

        }

        @Override
        public boolean preExecution(ActionCommand command) {
            getEditorUndoCommand().execute();
            command.addParameter("areaMagazzinoFullDTO", areaMagazzinoFullDTO);
            return true;
        }
    }

    private class InserisciRaggruppamentoArticoliCommandInterceptor extends OperazioniSuRigheActionCommandInterceptor {

        @Override
        public boolean preExecution(ActionCommand actioncommand) {
            ((RigaArticoloPage) getEditPages().get(RigaArticoloDTO.class.getName())).setReadOnly(true);
            inserisciRaggruppamentoArticoliCommand.addParameter(
                    InserisciRaggruppamentoArticoliCommand.AREA_DOCUMENTO_KEY, areaMagazzinoFullDTO.getAreaMagazzino());
            inserisciRaggruppamentoArticoliCommand.addParameter(InserisciRaggruppamentoArticoliCommand.AREA_RATE_KEY,
                    areaMagazzinoFullDTO.getAreaRate());
            return true;
        }
    }

    private class OperazioniSuRigheActionCommandInterceptor implements ActionCommandInterceptor {

        @Override
        public void postExecution(ActionCommand actioncommand) {
            if (actioncommand instanceof ICancellable && ((ICancellable) actioncommand).isCancelled()) {
                return;
            }

            int delay = getTable().getDelayForSelection();
            // la refresh via timer agisce sulla tabella inserendo e selezionando un record, poi le
            // operazioni di
            // selezione e di totalizzazione toccano quello che la refresh tocca e si ha una
            // ConcurrentModificationException quindi azzero il timer e alla fine di tutte le
            // operazioni di inserimento
            // di raggruppamento ripristino il tempo di attivazione del timer.

            getTable().setDelayForSelection(0);
            onRefresh();
            getTable().selectRow(getTable().getRows().size() - 1, null);

            // Ricarico l'area magazzino perchè potrei aver cambiato stato
            boolean righeValidateBefore = areaMagazzinoFullDTO.getAreaMagazzino().getDatiValidazioneRighe().isValid();
            boolean documentoConfermatoBefore = areaMagazzinoFullDTO.getAreaMagazzino()
                    .getStatoAreaMagazzino() == StatoAreaMagazzino.CONFERMATO;
            AreaMagazzinoFullDTO areaMagazzinoResult = magazzinoDocumentoBD
                    .caricaAreaMagazzinoFullDTO(areaMagazzinoFullDTO.getAreaMagazzino());
            setFormObject(areaMagazzinoResult);
            RigheMagazzinoTablePage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
                    areaMagazzinoResult);
            RigheMagazzinoTablePage.this.firePropertyChange(VALIDA_RIGHE_MAGAZZINO, righeValidateBefore,
                    areaMagazzinoResult.getAreaMagazzino().getDatiValidazioneRighe().isValid());
            if (getTotalizzaDocumentoCommand().isTotalizzazioneAutomatica()) {
                getTotalizzaDocumentoCommand().execute();
            }
            updateCommands();
            getTable().setDelayForSelection(delay);

            if (documentoConfermatoBefore && areaMagazzinoFullDTO.getAreaMagazzino()
                    .getStatoAreaMagazzino() == StatoAreaMagazzino.CONFERMATO) {
                MessageDialog messagio = new MessageDialog("Documento invariato", "Nessuna riga articolo modificata");
                messagio.showDialog();
            }
        }

        @Override
        public boolean preExecution(ActionCommand actioncommand) {
            getEditorUndoCommand().execute();
            actioncommand.addParameter(AggiungiVariazioneCommand.AREA_DOCUMENTO_KEY,
                    areaMagazzinoFullDTO.getAreaMagazzino().getId());
            // HACK Se sto modificando o inserendo un articolo chiamo l'undo altrimenti dopo la
            // selezione viene
            // richiamato il property change che crea una nuova riga articolo
            RigheMagazzinoTablePage.this.getEditPages().get(RigaArticoloDTO.class.getName()).onUndo();
            return !getTable().getRows().isEmpty();
        }

    }

    /**
     * Si preoccupa di annullare eventuali modifiche nel dettaglio prima di eseguire il command nel caso in cui la
     * modifica sia abilitata.
     *
     * @author leonardo
     */
    private class RefreshCommandInterceptor implements ActionCommandInterceptor {
        @Override
        public void postExecution(ActionCommand command) {

        }

        @Override
        public boolean preExecution(ActionCommand command) {
            RigaArticoloPage rigaArticoloPage = ((RigaArticoloPage) getEditPages()
                    .get(RigaArticoloDTO.class.getName()));
            if (rigaArticoloPage.getEditorUndoCommand().isEnabled()) {
                rigaArticoloPage.getEditorUndoCommand().execute();
            }
            return true;
        }
    }

    public class RicalcolaAreaMagazzinoCommand extends ActionCommand {

        public static final String COMMAND_ID = "ricalcolaAreaMagazzinoCommand";

        /**
         * Costruttore.
         */
        public RicalcolaAreaMagazzinoCommand() {
            super(COMMAND_ID);
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            ConfirmationDialog confirmationDialog = new ConfirmationDialog("Attenzione", "Ricalcolare i prezzi?") {

                @Override
                protected void onConfirm() {
                    magazzinoDocumentoBD.ricalcolaPrezziMagazzino(areaMagazzinoFullDTO.getAreaMagazzino().getId());
                }
            };

            confirmationDialog.setPreferredSize(new Dimension(330, 80));
            confirmationDialog.showDialog();
        }

    }

    private class StampaEtichetteDaAreaMagazzinoCommandInterceptor implements ActionCommandInterceptor {
        @Override
        public void postExecution(ActionCommand arg0) {
        }

        @Override
        public boolean preExecution(ActionCommand command) {
            List<RigaMagazzinoDTO> righeMagazzinoDTO = new ArrayList<RigaMagazzinoDTO>();
            righeMagazzinoDTO.addAll(getTable().getRows());
            command.addParameter(StampaEtichetteDaAreaMagazzinoCommand.PARAM_AREA_MAGAZZINO,
                    areaMagazzinoFullDTO.getAreaMagazzino());
            command.addParameter(StampaEtichetteDaAreaMagazzinoCommand.PARAM_LIST_RIGHE_MAGAZZINO, righeMagazzinoDTO);

            return true;
        }
    }

    private final class TableModelListenerImplementation implements TableModelListener {
        @Override
        public void tableChanged(TableModelEvent event) {
            RigheMagazzinoTableModel model = (RigheMagazzinoTableModel) TableModelWrapperUtils
                    .getActualTableModel(getTable().getTable().getModel());
            model.aggiornaNumeroDecimali();
        }
    }

    public class TotalizzaDocumentoCommandInterceptor implements ActionCommandInterceptor {

        @Override
        public void postExecution(ActionCommand totalizzaDocumentoCmd) {
            AreaMagazzinoFullDTO areaMagazzinoFullDTOValidata = ((TotalizzaDocumentoCommand) totalizzaDocumentoCmd)
                    .getAreaMagazzinoFullDTO();
            ((RigaArticoloPage) getEditPages().get(RigaArticoloDTO.class.getName()))
                    .setAreaMagazzinoFullDTO(areaMagazzinoFullDTOValidata);
            ((RigaArticoloPage) getEditPages().get(RigaArticoloDistintaDTO.class.getName()))
                    .setAreaMagazzinoFullDTO(areaMagazzinoFullDTOValidata);
            ((RigaNotaPage) getEditPages().get(RigaNotaDTO.class.getName()))
                    .setAreaMagazzinoFullDTO(areaMagazzinoFullDTOValidata);

            setFormObject(areaMagazzinoFullDTOValidata);
            RigheMagazzinoTablePage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
                    areaMagazzinoFullDTOValidata);
            firePropertyChange(TOTALIZZAZIONE_RIGHE_MAGAZZINO, null, true);
        }

        @Override
        public boolean preExecution(ActionCommand totalizzaDocumentoCmd) {
            ((TotalizzaDocumentoCommand) totalizzaDocumentoCmd).setAreaMagazzinoFullDTO(areaMagazzinoFullDTO);
            return true;
        }

    }

    public static final String PAGE_ID = "righeMagazzinoTablePage";

    public static final String REFRESH_RIGHE_ON_NEW_AREA_MAGAZZINO = "refreshRigheOnNewAreaMagazzino";

    public static final String VALIDA_RIGHE_MAGAZZINO = "validaRigheMagazzino";
    public static final String TOTALIZZAZIONE_RIGHE_MAGAZZINO = "totalizzazioneRigheMagazzino";
    private static final String KEY_NUOVO_DOPO_CONFERMA = "keyNuovoDopoConferma";
    private static final String KEY_STAMPA_DOPO_CONFERMA = "keyStampaDopoConferma";

    private static final String KEY_ESPORTA_DOPO_CONFERMA = "keyEsportaDopoConferma";

    private RigheMagazzinoRefreshNeededController reloadDataControl = new RigheMagazzinoRefreshNeededController();
    private IMagazzinoDocumentoBD magazzinoDocumentoBD = null;
    private AreaMagazzinoFullDTO areaMagazzinoFullDTO;
    private CloseRigheMagazzinoCommand closeRigheMagazzinoCommand;
    private ActionCommand importaRigheOrdineCommand;
    private TotalizzaDocumentoCommand totalizzaDocumentoCommand;
    private AzioneDopoConfermaCommand azioneDopoConfermaCommand;
    private StampaEtichetteDaAreaMagazzinoCommand stampaEtichetteDaAreaMagazzinoCommand;
    private EsportaDocumentoToggleCommand esportaDocumentoToggleCommand;
    private StampaDocumentoDopoConfermaToggleCommand stampaDocumentoDopoConfermaToggleCommand;
    private ApriDocumentoCollegatoCommand apriDocumentoCollegatoCommand;

    private AggiungiVariazioneCommand aggiungiVariazioneCommand;

    private RicalcolaAreaMagazzinoCommand ricalcolaAreaMagazzinoCommand;

    private AggiornaListinoCommand aggiornaListinoCommand;

    private ActionCommand inserisciRaggruppamentoArticoliCommand;
    private RefreshCommandInterceptor refreshCommandInterceptor = null;

    private AggiornaListinoCommandInterceptor aggiornaListinoCommandInterceptor;
    private StampaEtichetteDaAreaMagazzinoCommandInterceptor stampaEtichetteDaAreaMagazzinoCommandInterceptor;
    private PluginManager pluginManager;
    private TableModelListenerImplementation tableModelListener;
    private JideToggleCommand dataTrasportoPosticipataToggleCommand;

    private ImportaRigheOrdineCommandInterceptor importaRigheOrdineCommandInterceptor;
    private AumentaLivelloRigaMagazzinoCommand aumentaLivelloRigaMagazzinoCommand;
    private OperazioniSuRigheActionCommandInterceptor aggiungiVariazioneCommandInterceptor;

    private AumentaLivelloRigaMagazzinoCommandInterceptor aumentaLivelloRigaMagazzinoCommandInterceptor;

    private CloseRigheMagazzinoCommandInterceptor closeRigheMagazzinoCommandInterceptor;

    private InserisciRaggruppamentoArticoliCommandInterceptor inserisciRaggruppamentoArticoliCommandInterceptor;

    private OperazioniSuRigheActionCommandInterceptor ricalcolaAreaMagazzinoCommandInterceptor;

    private TotalizzaDocumentoCommandInterceptor totalizzaDocumentoCommandInterceptor;

    /**
     * Costruttore.
     */
    protected RigheMagazzinoTablePage() {
        super(PAGE_ID, new RigheMagazzinoTableModel());
        setEnableAutoResizeRow(true);
        getTable().setDelayForSelection(300);
        getTable().addSelectionObserver(this);
        getTable().getTable().setDragEnabled(true);
        getTable().getTable().setDropMode(DropMode.INSERT_ROWS);
        this.pluginManager = RcpSupport.getBean(PluginManager.BEAN_ID);
        tableModelListener = new TableModelListenerImplementation();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        org.springframework.util.Assert.notNull(magazzinoDocumentoBD, "magazzinoDocumentoBD cannot be null");
        getTable().getTable().setTransferHandler(new RigheMagazzinoTransferHandler(magazzinoDocumentoBD, this));
    }

    @Override
    protected EditFrame<RigaMagazzinoDTO> createEditFrame() {
        return new RigheMagazzinoEditFrame(EEditPageMode.DETAIL, this, EditFrame.QUICK_ACTION_DEFAULT);
    }

    @Override
    public void dispose() {
        super.dispose();

        if (aggiungiVariazioneCommand != null) {
            aggiungiVariazioneCommand.removeCommandInterceptor(getAggiungiVariazioneCommandInterceptor());
        }
        if (stampaEtichetteDaAreaMagazzinoCommand != null) {
            stampaEtichetteDaAreaMagazzinoCommand
                    .removeCommandInterceptor(stampaEtichetteDaAreaMagazzinoCommandInterceptor);
        }
        if (aggiornaListinoCommand != null) {
            aggiornaListinoCommand.removeCommandInterceptor(getAggiornaListinoCommandInterceptor());
        }
        if (aumentaLivelloRigaMagazzinoCommand != null) {
            aumentaLivelloRigaMagazzinoCommand
                    .removeCommandInterceptor(getAumentaLivelloRigaMagazzinoCommandInterceptor());
        }
        if (closeRigheMagazzinoCommand != null) {
            closeRigheMagazzinoCommand.removeCommandInterceptor(getCloseRigheMagazzinoCommandInterceptor());
        }
        if (inserisciRaggruppamentoArticoliCommand != null) {
            inserisciRaggruppamentoArticoliCommand
                    .removeCommandInterceptor(getInserisciRaggruppamentoArticoliCommandInterceptor());
        }
        if (ricalcolaAreaMagazzinoCommand != null) {
            ricalcolaAreaMagazzinoCommand.removeCommandInterceptor(getRicalcolaAreaMagazzinoCommandInterceptor());
        }
        if (totalizzaDocumentoCommand != null) {
            totalizzaDocumentoCommand.addCommandInterceptor(getTotalizzaDocumentoCommandInterceptor());
        }

        getRefreshCommand().removeCommandInterceptor(getRefreshCommandInterceptor());

        stampaEtichetteDaAreaMagazzinoCommand = null;
    }

    /**
     *
     * @return command per l'aggiornamento del listino
     */
    private AggiornaListinoCommand getAggiornaListinoCommand() {
        if (aggiornaListinoCommand == null) {
            aggiornaListinoCommand = new AggiornaListinoCommand();
            aggiornaListinoCommand.addCommandInterceptor(getAggiornaListinoCommandInterceptor());
        }
        return aggiornaListinoCommand;
    }

    /**
     * @return AggiornaListinoCommandInterceptor
     */
    private ActionCommandInterceptor getAggiornaListinoCommandInterceptor() {
        if (aggiornaListinoCommandInterceptor == null) {
            aggiornaListinoCommandInterceptor = new AggiornaListinoCommandInterceptor();
        }
        return aggiornaListinoCommandInterceptor;
    }

    /**
     * @return the aggiungiVariazioneCommand
     */
    public AggiungiVariazioneCommand getAggiungiVariazioneCommand() {
        if (aggiungiVariazioneCommand == null) {
            aggiungiVariazioneCommand = new AggiungiVariazioneCommand();
            aggiungiVariazioneCommand.addCommandInterceptor(getAggiungiVariazioneCommandInterceptor());
        }
        return aggiungiVariazioneCommand;
    }

    /**
     * @return AggiungiVariazioneCommandInterceptor
     */
    private OperazioniSuRigheActionCommandInterceptor getAggiungiVariazioneCommandInterceptor() {
        if (aggiungiVariazioneCommandInterceptor == null) {
            aggiungiVariazioneCommandInterceptor = new OperazioniSuRigheActionCommandInterceptor();
        }
        return aggiungiVariazioneCommandInterceptor;
    }

    /**
     * @return apriDocumentoCollegatoCommand
     */
    public ApriDocumentoCollegatoCommand getApriDocumentoCollegatoCommand() {
        if (apriDocumentoCollegatoCommand == null) {
            apriDocumentoCollegatoCommand = new ApriDocumentoCollegatoCommand(this);
        }
        return apriDocumentoCollegatoCommand;
    }

    /**
     * @return Returns the areaMagazzinoFullDTO.
     */
    public AreaMagazzinoFullDTO getAreaMagazzinoFullDTO() {
        return areaMagazzinoFullDTO;
    }

    /**
     * @return command per legare una riga ordine ad una testata
     */
    private AumentaLivelloRigaMagazzinoCommand getAumentaLivelloRigaMagazzinoCommand() {
        if (aumentaLivelloRigaMagazzinoCommand == null) {
            aumentaLivelloRigaMagazzinoCommand = new AumentaLivelloRigaMagazzinoCommand(magazzinoDocumentoBD);
            aumentaLivelloRigaMagazzinoCommand
                    .addCommandInterceptor(getAumentaLivelloRigaMagazzinoCommandInterceptor());
        }
        return aumentaLivelloRigaMagazzinoCommand;
    }

    /**
     * @return AumentaLivelloRigaMagazzinoCommandInterceptor
     */
    private AumentaLivelloRigaMagazzinoCommandInterceptor getAumentaLivelloRigaMagazzinoCommandInterceptor() {
        if (aumentaLivelloRigaMagazzinoCommandInterceptor == null) {
            aumentaLivelloRigaMagazzinoCommandInterceptor = new AumentaLivelloRigaMagazzinoCommandInterceptor();
        }
        return aumentaLivelloRigaMagazzinoCommandInterceptor;
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
     *
     * @return closeRigheMagazzinoCommand
     */
    protected AbstractCommand getCloseRigheMagazzinoCommand() {
        if (closeRigheMagazzinoCommand == null) {
            closeRigheMagazzinoCommand = new CloseRigheMagazzinoCommand(magazzinoDocumentoBD, PAGE_ID);
            closeRigheMagazzinoCommand.addCommandInterceptor(getCloseRigheMagazzinoCommandInterceptor());
        }
        return closeRigheMagazzinoCommand;
    }

    /**
     * @return closeRigheMagazzinoCommandInterceptor
     */
    private CloseRigheMagazzinoCommandInterceptor getCloseRigheMagazzinoCommandInterceptor() {
        if (closeRigheMagazzinoCommandInterceptor == null) {
            closeRigheMagazzinoCommandInterceptor = new CloseRigheMagazzinoCommandInterceptor();
        }
        return closeRigheMagazzinoCommandInterceptor;
    }

    /**
     *
     * @return collegaRigheOrdineCommand
     */
    private AbstractCommand getCollegaRigheOrdineCommand() {
        return importaRigheOrdineCommand;
    }

    @Override
    public AbstractCommand[] getCommands() {
        return new AbstractCommand[] { getRicalcolaAreaMagazzinoCommand(), getApriDocumentoCollegatoCommand(),
                getDataTrasportoPosticipataToggleCommand(), getAumentaLivelloRigaMagazzinoCommand(),
                getCollegaRigheOrdineCommand(), getAggiornaListinoCommand(), getAggiungiVariazioneCommand(),
                getInserisciRaggruppamentoArticoliCommand(), getStampaEtichetteDaAreaMagazzinoCommand(),
                getTotalizzaDocumentoCommand(), getAzioneDopoConfermaCommand(), getEsportaDocumentoToggleCommand(),
                getStampaDocumentoDopoConfermaToggleCommand(), getCloseRigheMagazzinoCommand(),
                getEditorDeleteCommand(), getRefreshCommand() };
    }

    /**
     *
     * @return command toogle per verificare se posticipare la data di traposto o meno.
     */
    private JideToggleCommand getDataTrasportoPosticipataToggleCommand() {
        if (dataTrasportoPosticipataToggleCommand == null) {
            dataTrasportoPosticipataToggleCommand = new DataTrasportoPosticipataToggleCommand();
        }
        return dataTrasportoPosticipataToggleCommand;
    }

    @Override
    public AbstractCommand getEditorNewCommand() {
        return getEditPages().get(RigaArticoloDTO.class.getName()).getEditorNewCommand();
    }

    /**
     * @return the esportaDocumentoToggleCommand
     */
    public EsportaDocumentoToggleCommand getEsportaDocumentoToggleCommand() {
        if (esportaDocumentoToggleCommand == null) {
            esportaDocumentoToggleCommand = new EsportaDocumentoToggleCommand();
        }

        return esportaDocumentoToggleCommand;
    }

    /**
     * @return the importaRigheOrdineCommandInterceptor
     */
    private ImportaRigheOrdineCommandInterceptor getImportaRigheOrdineCommandInterceptor() {
        if (importaRigheOrdineCommandInterceptor == null) {
            importaRigheOrdineCommandInterceptor = new ImportaRigheOrdineCommandInterceptor();
        }

        return importaRigheOrdineCommandInterceptor;
    }

    /**
     *
     * @return comando per inserire un raggruppamento articoli
     */
    protected AbstractCommand getInserisciRaggruppamentoArticoliCommand() {
        if (inserisciRaggruppamentoArticoliCommand == null) {
            inserisciRaggruppamentoArticoliCommand = new InserisciRaggruppamentoArticoliCommand();
            inserisciRaggruppamentoArticoliCommand
                    .addCommandInterceptor(getInserisciRaggruppamentoArticoliCommandInterceptor());
        }
        return inserisciRaggruppamentoArticoliCommand;
    }

    /**
     * @return inserisciRaggruppamentoArticoliCommandInterceptor
     */
    private InserisciRaggruppamentoArticoliCommandInterceptor getInserisciRaggruppamentoArticoliCommandInterceptor() {
        if (inserisciRaggruppamentoArticoliCommandInterceptor == null) {
            inserisciRaggruppamentoArticoliCommandInterceptor = new InserisciRaggruppamentoArticoliCommandInterceptor();
        }
        return inserisciRaggruppamentoArticoliCommandInterceptor;
    }

    /**
     * @return magazzinoDocumentoBD
     */
    public IMagazzinoDocumentoBD getMagazzinoDocumentoBD() {
        return magazzinoDocumentoBD;
    }

    @Override
    public Object getManagedObject(Object pageObject) {

        if (pageObject instanceof RigaMagazzino) {

            // Controllo il nuovo stato dell'area in quanto se passato da confermato/forzato in
            // provvisorio devo
            // ricaricare le righe per togliere quelle automatiche che sono state tolte
            StatoAreaMagazzino statoAreaPrec = areaMagazzinoFullDTO.getAreaMagazzino().getStatoAreaMagazzino();
            StatoAreaMagazzino statoAreaNew = ((RigaMagazzino) pageObject).getAreaMagazzino().getStatoAreaMagazzino();
            if ((statoAreaPrec == StatoAreaMagazzino.CONFERMATO || statoAreaPrec == StatoAreaMagazzino.FORZATO)
                    && statoAreaNew == StatoAreaMagazzino.PROVVISORIO) {
                try {
                    getTable().removeSelectionObserver(this);
                    loadData();
                } finally {
                    getTable().addSelectionObserver(this);
                }

                getTable().selectRowObject(getTable().getRows().get(0), this);
                // getTable().selectRowObject(((RigaMagazzino) pageObject).creaRigaMagazzinoDTO(),
                // this);
            }

            areaMagazzinoFullDTO.setAreaMagazzino(((RigaMagazzino) pageObject).getAreaMagazzino());

            // Quando una riga magazzino cambia (insert/edit/delete), se il
            // comando di totalizzazione è automatico lo lancio
            if (getTotalizzaDocumentoCommand().isTotalizzazioneAutomatica()) {
                getTotalizzaDocumentoCommand().setAreaMagazzinoFullDTO(areaMagazzinoFullDTO);
                getTotalizzaDocumentoCommand().execute();
            }
            return areaMagazzinoFullDTO;
        } else {
            return null;
        }
    }

    @Override
    public ActionCommand getRefreshCommand() {
        ActionCommand actionCommand = super.getRefreshCommand();
        if (refreshCommandInterceptor == null) {
            actionCommand.addCommandInterceptor(getRefreshCommandInterceptor());
        }
        return actionCommand;
    }

    /**
     * @return RefreshCommandInterceptor
     */
    public RefreshCommandInterceptor getRefreshCommandInterceptor() {
        if (refreshCommandInterceptor == null) {
            refreshCommandInterceptor = new RefreshCommandInterceptor();
        }
        return refreshCommandInterceptor;
    }

    /**
     * @return the ricalcolaAreaOrdineCommand
     */
    public RicalcolaAreaMagazzinoCommand getRicalcolaAreaMagazzinoCommand() {
        if (ricalcolaAreaMagazzinoCommand == null) {
            ricalcolaAreaMagazzinoCommand = new RicalcolaAreaMagazzinoCommand();
            ricalcolaAreaMagazzinoCommand.addCommandInterceptor(getRicalcolaAreaMagazzinoCommandInterceptor());
        }
        return ricalcolaAreaMagazzinoCommand;
    }

    /**
     * @return ricalcolaAreaMagazzinoCommandInterceptor
     */
    private OperazioniSuRigheActionCommandInterceptor getRicalcolaAreaMagazzinoCommandInterceptor() {
        if (ricalcolaAreaMagazzinoCommandInterceptor == null) {
            ricalcolaAreaMagazzinoCommandInterceptor = new OperazioniSuRigheActionCommandInterceptor();
        }
        return ricalcolaAreaMagazzinoCommandInterceptor;
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
     * @return the stampaEtichetteDaAreaMagazzinoCommand
     */
    public StampaEtichetteDaAreaMagazzinoCommand getStampaEtichetteDaAreaMagazzinoCommand() {
        if (stampaEtichetteDaAreaMagazzinoCommand == null) {
            if (pluginManager.isPresente(PluginManager.PLUGIN_LOTTI)) {
                stampaEtichetteDaAreaMagazzinoCommand = RcpSupport
                        .getCommand("stampaEtichetteLottiDaAreaMagazzinoCommand");
            } else {
                stampaEtichetteDaAreaMagazzinoCommand = new StampaEtichetteDaAreaMagazzinoCommand();
            }
            stampaEtichetteDaAreaMagazzinoCommandInterceptor = new StampaEtichetteDaAreaMagazzinoCommandInterceptor();
            stampaEtichetteDaAreaMagazzinoCommand
                    .addCommandInterceptor(stampaEtichetteDaAreaMagazzinoCommandInterceptor);
        }

        return stampaEtichetteDaAreaMagazzinoCommand;
    }

    /**
     *
     * @return tablemodel della pagina
     */
    private RigheMagazzinoTableModel getTableModel() {
        return (RigheMagazzinoTableModel) TableModelWrapperUtils.getActualTableModel(getTable().getTable().getModel());
    }

    /**
     * @return the totalizzaDocumentoCommand
     */
    public TotalizzaDocumentoCommand getTotalizzaDocumentoCommand() {
        if (totalizzaDocumentoCommand == null) {
            totalizzaDocumentoCommand = new TotalizzaDocumentoCommand(this.magazzinoDocumentoBD);
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
    public RigheMagazzinoDTOResult loadTableData() {
        RigheMagazzinoDTOResult result = new RigheMagazzinoDTOResult();

        if (!areaMagazzinoFullDTO.getAreaMagazzino().isNew()) {
            result = magazzinoDocumentoBD
                    .caricaRigheMagazzinoDTObyAreaMagazzino(areaMagazzinoFullDTO.getAreaMagazzino());
        }
        return result;
    }

    @Override
    public void onPostPageOpen() {
    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    protected void onRefresh() {
        loadData();
    }

    @Override
    public void preSetFormObject(Object object) {
        if (isControlCreated() && areaMagazzinoFullDTO != null) {
            AreaMagazzinoFullDTO areaNew = (AreaMagazzinoFullDTO) object;

            if (areaNew.getAreaRate() != null) {
                CodicePagamento codicePagamentoNew = areaNew.getAreaRate().getCodicePagamento();

                // controllo per NPE da mail
                if (areaMagazzinoFullDTO.getAreaRate() != null) {
                    CodicePagamento codicePagamentoOld = areaMagazzinoFullDTO.getAreaRate().getCodicePagamento();

                    if (codicePagamentoNew != null && codicePagamentoOld != null) {
                        if (codicePagamentoNew.getPercentualeScontoCommerciale()
                                .compareTo(codicePagamentoOld.getPercentualeScontoCommerciale()) != 0) {
                            loadData();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void processTableData(Collection<RigaMagazzinoDTO> results) {
        getTableModel().removeTableModelListener(tableModelListener);
        // Devo ricaricarmi le righe solamente se cambio l'area magazzino.
        // Se l'area magazzino e' la stessa non ricarico le righe.
        // Nella setFormObject gia' setto il riferimento al nuovo oggetto.
        // Azzero mentre carico le righe
        RigheMagazzinoDTOResult righeMagazzino = (RigheMagazzinoDTOResult) results;
        RigheMagazzinoTableModel model = getTableModel();
        if (righeMagazzino != null) {
            model.setNumeroDecimaliQta(righeMagazzino.getNumeroDecimaliQta());
            model.setNumeroDecimaliPrezzo(righeMagazzino.getNumeroDecimaliPrezzo());
        }
        int selectRow = getTable().getTable().getSelectedRow();
        if (righeMagazzino != null && righeMagazzino.size() > 0 && selectRow == -1) {
            selectRow = 0;
        }
        setRows(results, false);
        getTable().selectRow(selectRow, null);
        // abilita/disabilita table page
        updateControl();
        updateCommands();
        model.addTableModelListener(tableModelListener);

        // HACK: serve in AreaMagazzinoEditorController per spostare il focus sull'area magazzino
        // quando
        // si crea un nuovo documento.
        if (areaMagazzinoFullDTO.getAreaMagazzino().isNew()) {
            RigheMagazzinoTablePage.this.firePropertyChange(REFRESH_RIGHE_ON_NEW_AREA_MAGAZZINO, false, true);
        }
    }

    @Override
    public RigheMagazzinoDTOResult refreshTableData() {

        if (areaMagazzinoFullDTO.getAreaMagazzino().isNew()) {
            return new RigheMagazzinoDTOResult();
        }

        if (reloadDataControl.righeMagazzinoSonoDaRicaricare()) {
            return magazzinoDocumentoBD.caricaRigheMagazzinoDTObyAreaMagazzino(areaMagazzinoFullDTO.getAreaMagazzino());
        }

        return null;
    }

    /**
     * metodo incaricato di aggiornare i global command con i commads della page corrente.
     */
    @SuppressWarnings("unchecked")
    protected void registerGlobalCommands() {
        logger.debug("--> Enter registerGlobalCommands");
        // Aggiorno i globalCommand
        Map<String, ActionCommandExecutor> context = new HashMap<String, ActionCommandExecutor>();
        context.put(JecGlobalCommandIds.NEW, getEditorNewCommand());
        context.put(JecGlobalCommandIds.EDIT, getEditorLockCommand());
        context.put(JecGlobalCommandIds.UNDO_MODEL, getEditorUndoCommand());
        context.put(JecGlobalCommandIds.SAVE, getEditorSaveCommand());
        context.put(JecGlobalCommandIds.DELETE, getEditorDeleteCommand());
        for (Iterator<Object> i = getActiveWindow().getSharedCommands(); i.hasNext();) {
            TargetableActionCommand globalCommand = (TargetableActionCommand) i.next();
            if (context.containsKey(globalCommand.getId())) {
                logger.debug("--> register di " + globalCommand.getId() + " " + context.get(globalCommand.getId()));
                globalCommand.setCommandExecutor(context.get(globalCommand.getId()));
            }
        }
        logger.debug("--> Exit registerGlobalCommands");
    }

    @Override
    public void restoreState(Settings settings) {

        if (settings.contains(KEY_NUOVO_DOPO_CONFERMA)) {
            getAzioneDopoConfermaCommand().setSelected(settings.getBoolean(KEY_NUOVO_DOPO_CONFERMA));
        }
        if (settings.contains(KEY_ESPORTA_DOPO_CONFERMA)) {
            getEsportaDocumentoToggleCommand().setSelected(settings.getBoolean(KEY_ESPORTA_DOPO_CONFERMA));
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
        settings.setBoolean(KEY_ESPORTA_DOPO_CONFERMA, getEsportaDocumentoToggleCommand().isSelected());
        settings.setBoolean(KEY_STAMPA_DOPO_CONFERMA, getStampaDocumentoDopoConfermaToggleCommand().isSelected());
        settings.setBoolean(TotalizzaDocumentoCommand.KEY_TOTALIZZAZIONE_AUTOMATICA,
                getTotalizzaDocumentoCommand().isTotalizzazioneAutomatica());

        super.saveState(settings);
    }

    @Override
    public void setFormObject(Object object) {
        logger.debug("--> setFormObject " + object);
        areaMagazzinoFullDTO = (AreaMagazzinoFullDTO) object;
        reloadDataControl.cambiaAreaAttuale(areaMagazzinoFullDTO);
        ((RigaArticoloPage) getEditPages().get(RigaArticoloDTO.class.getName()))
                .setAreaMagazzinoFullDTO(areaMagazzinoFullDTO);
        ((RigaArticoloPage) getEditPages().get(RigaArticoloDistintaDTO.class.getName()))
                .setAreaMagazzinoFullDTO(areaMagazzinoFullDTO);
        ((RigaNotaPage) getEditPages().get(RigaNotaDTO.class.getName())).setAreaMagazzinoFullDTO(areaMagazzinoFullDTO);
        ((RigaTestataPage) getEditPages().get(RigaTestataDTO.class.getName()))
                .setAreaMagazzinoFullDTO(areaMagazzinoFullDTO);
        updateControl();
        updateCommands();
    }

    /**
     * @param importaRigheOrdineCommand
     *            The collegaRigheOrdineCommand to set.
     */
    public void setImportaRigheOrdineCommand(ActionCommand importaRigheOrdineCommand) {
        this.importaRigheOrdineCommand = importaRigheOrdineCommand;
        this.importaRigheOrdineCommand.addCommandInterceptor(getImportaRigheOrdineCommandInterceptor());
    }

    /**
     * @param magazzinoDocumentoBD
     *            magazzinoDocumentoBD
     */
    public void setMagazzinoDocumentoBD(IMagazzinoDocumentoBD magazzinoDocumentoBD) {
        this.magazzinoDocumentoBD = magazzinoDocumentoBD;
    }

    @Override
    public void update(Observable obs, Object obj) {

        getApriDocumentoCollegatoCommand().setVisible(false);
        if (obj instanceof RigaTestataDTO) {
            RigaTestataDTO rigaTestata = (RigaTestataDTO) obj;
            getApriDocumentoCollegatoCommand()
                    .setVisible(obj instanceof RigaTestataDTO && rigaTestata.getCodiceTipoDocumentoCollegato() != null);
            getApriDocumentoCollegatoCommand()
                    .setLabel(String.format("Apri %s collegato.", rigaTestata.getCodiceTipoDocumentoCollegato()));
        }

        // quando la tabella viene svuotata devo chiamare il nuovo command altrimenti l'area
        // magazzino non viene
        // impostata sulla riga; non posso farlo sul post delete command visto che devo verificare
        // il numero di elementi
        // presenti in tabella
        if (areaMagazzinoFullDTO.getId() != null && getTable().getRows().size() == 0) {
            getEditPages().get(RigaArticoloDTO.class.getName()).getEditorNewCommand().execute();
        }
        updateCommands();
    }

    /**
     * Aggiorna tutti i comandi in base all' {@link AreaMagazzinoFullDTO} gestita.
     */
    protected void updateCommands() {
        logger.debug("--> Enter updateCommands");
        boolean enabled = getTable().getRows().size() != 0;
        getRefreshCommand().setEnabled(true);

        boolean confermaEnable = !areaMagazzinoFullDTO.getAreaMagazzino().getDatiValidazioneRighe().isValid()
                && enabled;
        boolean isNewAreaMagazzino = areaMagazzinoFullDTO.isNew();
        boolean isBloccataAreaMagazzino = areaMagazzinoFullDTO.getAreaMagazzino().isBloccata()
                || !areaMagazzinoFullDTO.getAreaMagazzino().isModificabile();
        // il tasto conferma righe deve essere inoltre disabilitato se lo stato è in fatturazione
        confermaEnable = confermaEnable && !areaMagazzinoFullDTO.getAreaMagazzino().getStatoAreaMagazzino()
                .equals(StatoAreaMagazzino.INFATTURAZIONE);
        if (isNewAreaMagazzino) {
            confermaEnable = false;
        }
        getCloseRigheMagazzinoCommand().setEnabled(confermaEnable && !isBloccataAreaMagazzino);
        getEditorDeleteCommand().setEnabled(enabled && !isBloccataAreaMagazzino);
        for (AbstractCommand command : getEditFrame().getDetailNewCommands().values()) {
            command.setEnabled(isBloccataAreaMagazzino);
        }

        boolean totalizzaEnabled = areaMagazzinoFullDTO.getAreaContabileLite() == null;
        if (!totalizzaEnabled) {
            totalizzaEnabled = areaMagazzinoFullDTO.getAreaContabileLite().isNew();
        }
        totalizzaEnabled = totalizzaEnabled && !isBloccataAreaMagazzino
                && areaMagazzinoFullDTO.getAreaMagazzino().getStatoAreaMagazzino() == StatoAreaMagazzino.PROVVISORIO;

        totalizzaEnabled = totalizzaEnabled && enabled;

        getTotalizzaDocumentoCommand().setEnabled(totalizzaEnabled);
        getInserisciRaggruppamentoArticoliCommand().setEnabled(!isNewAreaMagazzino && !isBloccataAreaMagazzino);

        // se non c'è il plugin degli ordini collegaRigheOrdineCommand è null
        AbstractCommand collegaRigheOrdineCommand = getCollegaRigheOrdineCommand();
        if (collegaRigheOrdineCommand != null) {
            collegaRigheOrdineCommand.setEnabled(!isNewAreaMagazzino && !isBloccataAreaMagazzino);
        }

        logger.debug("--> Exit updateCommands");
    }

    /**
     * abilita disabilita la table page corrente.
     */
    protected void updateControl() {
        if (areaMagazzinoFullDTO.getAreaMagazzino().getStatoAreaMagazzino() == StatoAreaMagazzino.INFATTURAZIONE) {
            setEnabled(false);
        }
        setReadOnly(areaMagazzinoFullDTO.isNew() || areaMagazzinoFullDTO.getAreaMagazzino().isBloccata()
                || !areaMagazzinoFullDTO.getAreaMagazzino().isModificabile());
        setEnabled(!areaMagazzinoFullDTO.getAreaMagazzino().isNew()
                && !areaMagazzinoFullDTO.getAreaMagazzino().isBloccata()
                && areaMagazzinoFullDTO.getAreaMagazzino().isModificabile());
    }

}
