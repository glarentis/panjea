package it.eurotn.panjea.ordini.rich.editors.righeordine;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.DropMode;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.JideTable;

import it.eurotn.panjea.magazzino.rich.editors.righemagazzino.AzioneDopoConfermaCommand;
import it.eurotn.panjea.magazzino.rich.editors.righemagazzino.InserisciRaggruppamentoArticoliCommand;
import it.eurotn.panjea.magazzino.rich.editors.righemagazzino.StampaDocumentoDopoConfermaToggleCommand;
import it.eurotn.panjea.ordini.domain.RigaOrdine;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine.StatoAreaOrdine;
import it.eurotn.panjea.ordini.domain.documento.TipoAreaOrdine;
import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;
import it.eurotn.panjea.ordini.rich.editors.righeinserimento.RigheInserimentoPage;
import it.eurotn.panjea.ordini.util.AreaOrdineFullDTO;
import it.eurotn.panjea.ordini.util.RigaArticoloDTO;
import it.eurotn.panjea.ordini.util.RigaNotaDTO;
import it.eurotn.panjea.ordini.util.RigaOrdineDTO;
import it.eurotn.panjea.ordini.util.RigaTestataDTO;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.IPageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;
import it.eurotn.rich.editors.table.EEditPageMode;
import it.eurotn.rich.editors.table.EditFrame;
import it.eurotn.rich.report.JecReportDocumento;
import it.eurotn.rich.report.JecReportDocumentoBatch;

public class RigheOrdineTablePage extends AbstractTablePageEditor<RigaOrdineDTO>implements Observer, InitializingBean {

    public class AttivaRigheInserimentoCommand extends ActionCommand {

        /**
         * Costruttore.
         */
        public AttivaRigheInserimentoCommand() {
            super("attivaRigheInserimentoCommand");
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            RigheOrdineTablePage.this.firePropertyChange(RigheInserimentoPage.ATTIVAZIONE_RIGHE_INSERIMENTO, false,
                    true);
        }

    }

    private class AumentaLivelloRigaOrdineCommandInterceptor implements ActionCommandInterceptor {

        @Override
        public void postExecution(ActionCommand command) {
            loadData();
        }

        @Override
        public boolean preExecution(ActionCommand command) {
            Set<Integer> righeOrdine = new HashSet<Integer>();
            List<RigaOrdineDTO> righeSelezionate = getTable().getSelectedObjects();
            for (RigaOrdineDTO rigaOrdineDTO : righeSelezionate) {
                righeOrdine.add(rigaOrdineDTO.getId());
            }
            ((AumentaLivelloRigaOrdineCommand) command).addParameter(AumentaLivelloRigaOrdineCommand.RIGHE_ORDINE_PARAM,
                    righeOrdine);
            return righeOrdine.size() > 0;
        }
    }

    private class BloccaOrdineCommandInterceptor implements ActionCommandInterceptor {

        @Override
        public void postExecution(ActionCommand command) {
            AreaOrdineFullDTO areaOrdineFullDTOBloccata = ((BloccaOrdineCommand) command).getAreaOrdineFullDTO();

            setFormObject(areaOrdineFullDTOBloccata);
            RigheOrdineTablePage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
                    areaOrdineFullDTOBloccata);
            updateCommands();
        }

        @Override
        public boolean preExecution(ActionCommand command) {

            if (getTable().getRows().isEmpty()) {
                MessageDialog dialog = new MessageDialog("ATTENZIONE", "Impossibile bloccare un ordine senza righe.");
                dialog.showDialog();
                return false;
            } else {
                command.addParameter(BloccaOrdineCommand.AREA_ORDINE_FULL_PARAM, areaOrdineFullDTO);
                return true;
            }
        }

    }

    public class CloseRigheOrdineCommandInterceptor implements ActionCommandInterceptor {

        @Override
        public void postExecution(ActionCommand closeRigheOrdineCmd) {
            AreaOrdineFullDTO areaOrdineFullDTOValidata = ((CloseRigheOrdineCommand) closeRigheOrdineCmd)
                    .getAreaOrdineFullDTO();

            if (getStampaDocumentoDopoConfermaToggleCommand().isSelected()) {
                JecReportDocumento jecReportDocumento = new JecReportDocumentoBatch(areaOrdineFullDTO.getAreaOrdine());
                jecReportDocumento.execute();
            }

            ((RigaArticoloPage) getEditPages().get(RigaArticoloDTO.class.getName()))
                    .setAreaOrdineFullDTO(areaOrdineFullDTOValidata);
            ((RigaNotaPage) getEditPages().get(RigaNotaDTO.class.getName()))
                    .setAreaOrdineFullDTO(areaOrdineFullDTOValidata);

            setFormObject(areaOrdineFullDTOValidata);
            RigheOrdineTablePage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
                    areaOrdineFullDTOValidata);
            RigheOrdineTablePage.this.firePropertyChange(VALIDA_RIGHE_ORDINE, false, true);

            // se sono cambiate il numero di righe ( es note automatiche )
            // ricarico le righe
            if (areaOrdineFullDTOValidata.getAreaOrdine().getNumeroRighe() != getTable().getRows().size()) {
                loadData();
                getTable().selectRow(getTable().getRows().size() - 1, null);
                ((JideTable) getTable().getTable()).scrollRowToVisible(getTable().getRows().size() - 1);
            }

            updateCommands();
        }

        @Override
        public boolean preExecution(ActionCommand closeRigheOrdineCmd) {
            (closeRigheOrdineCommand).setAreaOrdineFullDTO(areaOrdineFullDTO);
            // annullo eventuali modifiche sulla pagina del detail, quando
            // inserisco un riga viene preparata la
            // successiva vuota; se confermo le righe e non faccio nulla mi
            // ritrovo ancora preparata la riga nuova
            getEditorUndoCommand().execute();

            RigaNotaAutomaticaGenerator rigaNotaAutomaticaGenerator = new RigaNotaAutomaticaGenerator();
            rigaNotaAutomaticaGenerator.genera(areaOrdineFullDTO.getAreaOrdine(), getTable().getRows());

            return true;
        }

    }

    private class ConfigurazioneDistintaChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            updateCommands();

            TipoAreaOrdine tipoAreaOrdine = areaOrdineFullDTO.getAreaOrdine().getTipoAreaOrdine();
            boolean isOrdineProduzione = tipoAreaOrdine != null ? tipoAreaOrdine.isOrdineProduzione() : false;

            if (isOrdineProduzione) {
                loadData();
            }
        }
    }

    private class EvadiAreaOrdineCommandInterceptor extends ActionCommandInterceptorAdapter {

        @Override
        public boolean preExecution(ActionCommand command) {
            command.addParameter(EvadiAreaOrdineCommand.PARAM_AREA_ORDINE, areaOrdineFullDTO.getAreaOrdine());
            return true;
        }
    }

    private class InserisciRaggruppamentoArticoliCommandInterceptor implements ActionCommandInterceptor {
        @Override
        public void postExecution(ActionCommand actioncommand) {
            // HACK Se sto modificando o inserendo un articolo chiamo l'undo
            // altrimenti dopo la selezione viene
            // richiamato il property change che crea una nuova riga articolo
            RigheOrdineTablePage.this.getEditPages().get(RigaArticoloDTO.class.getName()).onUndo();

            int delay = getTable().getDelayForSelection();// NO
            // la refresh via timer agisce sulla tabella inserendo e
            // selezionando un record, poi le operazioni
            // di
            // selezione e di totalizzazione toccano quello che la refresh tocca
            // e si ha una
            // ConcurrentModificationException quindi azzero il timer e alla
            // fine di tutte le operazioni di
            // inserimento
            // di raggruppamento ripristino il tempo di attivazione del timer.

            getTable().setDelayForSelection(0);
            onRefresh();
            getTable().selectRow(getTable().getRows().size() - 1, null);
            // Ricarico l'area ordine perchè potrei aver cambiato stato
            boolean righeValidateBefore = areaOrdineFullDTO.getAreaOrdine().getDatiValidazioneRighe().isValid();
            AreaOrdineFullDTO areaOrdineResult = ordiniDocumentoBD
                    .caricaAreaOrdineFullDTO(areaOrdineFullDTO.getAreaOrdine());
            setFormObject(areaOrdineResult);
            RigheOrdineTablePage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null, areaOrdineResult);
            RigheOrdineTablePage.this.firePropertyChange(VALIDA_RIGHE_ORDINE, righeValidateBefore,
                    areaOrdineResult.getAreaOrdine().getDatiValidazioneRighe().isValid());
            updateCommands();
            if (getTotalizzaDocumentoCommand().isTotalizzazioneAutomatica()) {
                getTotalizzaDocumentoCommand().execute();
            }
            getTable().setDelayForSelection(delay);
        }

        @Override
        public boolean preExecution(ActionCommand actioncommand) {
            getEditorUndoCommand().execute();
            inserisciRaggruppamentoArticoliCommand.addParameter(
                    InserisciRaggruppamentoArticoliCommand.AREA_DOCUMENTO_KEY, areaOrdineFullDTO.getAreaOrdine());
            inserisciRaggruppamentoArticoliCommand.addParameter(InserisciRaggruppamentoArticoliCommand.AREA_RATE_KEY,
                    areaOrdineFullDTO.getAreaRate());
            return true;
        }
    }

    private class OperazioniSuRigheActionCommandInterceptor implements ActionCommandInterceptor {

        @Override
        public void postExecution(ActionCommand actioncommand) {
            // HACK Se sto modificando o inserendo un articolo chiamo l'undo
            // altrimenti dopo la selezione viene
            // richiamato il property change che crea una nuova riga articolo
            RigheOrdineTablePage.this.getEditPages().get(RigaArticoloDTO.class.getName()).onUndo();

            int delay = getTable().getDelayForSelection();
            // la refresh via timer agisce sulla tabella inserendo e
            // selezionando un record, poi le operazioni
            // di selezione e di totalizzazione toccano quello che la refresh
            // tocca e si ha una
            // ConcurrentModificationException quindi azzero il timer e alla
            // fine di tutte le operazioni di
            // inserimento di raggruppamento ripristino il tempo di attivazione
            // del timer.

            getTable().setDelayForSelection(0);
            onRefresh();
            // Ricarico l'area ordine perchè potrei aver cambiato stato
            boolean righeValidateBefore = areaOrdineFullDTO.getAreaOrdine().getDatiValidazioneRighe().isValid();
            AreaOrdineFullDTO areaOrdineResult = ordiniDocumentoBD
                    .caricaAreaOrdineFullDTO(areaOrdineFullDTO.getAreaOrdine());
            setFormObject(areaOrdineResult);
            RigheOrdineTablePage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null, areaOrdineResult);
            RigheOrdineTablePage.this.firePropertyChange(VALIDA_RIGHE_ORDINE, righeValidateBefore,
                    areaOrdineResult.getAreaOrdine().getDatiValidazioneRighe().isValid());
            updateCommands();
            if (getTotalizzaDocumentoCommand().isTotalizzazioneAutomatica()) {
                getTotalizzaDocumentoCommand().execute();
            }
            getTable().setDelayForSelection(delay);
        }

        @Override
        public boolean preExecution(ActionCommand actioncommand) {
            getEditorUndoCommand().execute();
            aggiungiVariazioneCommand.addParameter(AggiungiVariazioneCommand.AREA_DOCUMENTO_KEY,
                    areaOrdineFullDTO.getAreaOrdine().getId());
            return !getTable().getRows().isEmpty();
        }
    }

    /**
     * Si preoccupa di annullare eventuali modifiche nel dettaglio prima di eseguire il command nel
     * caso in cui la modifica sia abilitata.
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

    public class RicalcolaAreaOrdineCommand extends ActionCommand {

        public static final String COMMAND_ID = "ricalcolaAreaOrdineCommand";

        /**
         * Costruttore.
         */
        public RicalcolaAreaOrdineCommand() {
            super(COMMAND_ID);
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            ConfirmationDialog confirmationDialog = new ConfirmationDialog("Attenzione",
                    "Ricalcolare i prezzi? <br>NB: verranno ricalcolate solo le righe non ancora evase") {

                @Override
                protected void onConfirm() {
                    areaOrdineFullDTO = ordiniDocumentoBD
                            .ricalcolaPrezziOrdine(areaOrdineFullDTO.getAreaOrdine().getId());

                    setFormObject(areaOrdineFullDTO);
                    refreshData();
                    RigheOrdineTablePage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
                            areaOrdineFullDTO);
                }
            };
            confirmationDialog.setPreferredSize(new Dimension(330, 80));
            confirmationDialog.showDialog();
        }

    }

    public class TotalizzaDocumentoCommandInterceptor implements ActionCommandInterceptor {

        @Override
        public void postExecution(ActionCommand totalizzaDocumentoCmd) {
            AreaOrdineFullDTO areaOrdineFullDTOValidata = ((TotalizzaDocumentoCommand) totalizzaDocumentoCmd)
                    .getAreaOrdineFullDTO();
            ((RigaArticoloPage) getEditPages().get(RigaArticoloDTO.class.getName()))
                    .setAreaOrdineFullDTO(areaOrdineFullDTOValidata);
            ((RigaNotaPage) getEditPages().get(RigaNotaDTO.class.getName()))
                    .setAreaOrdineFullDTO(areaOrdineFullDTOValidata);

            setFormObject(areaOrdineFullDTOValidata);
            RigheOrdineTablePage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
                    areaOrdineFullDTOValidata);
            RigheOrdineTablePage.this.firePropertyChange(VALIDA_RIGHE_ORDINE, null, true);
        }

        @Override
        public boolean preExecution(ActionCommand totalizzaDocumentoCmd) {
            ((TotalizzaDocumentoCommand) totalizzaDocumentoCmd).setAreaOrdineFullDTO(areaOrdineFullDTO);
            return true;
        }

    }

    public static final String PAGE_ID = "righeOrdineTablePage";

    public static final String VALIDA_RIGHE_ORDINE = "validaRigheOrdine";

    private static final String KEY_NUOVO_DOPO_CONFERMA = "keyNuovoOrdineDopoConferma";
    private static final String KEY_STAMPA_DOPO_CONFERMA = "keyStampaOrdineDopoConferma";
    private AreaOrdineFullDTO areaOrdineFullDTO;
    private IOrdiniDocumentoBD ordiniDocumentoBD;
    private AzioneDopoConfermaCommand azioneDopoConfermaCommand;
    private StampaDocumentoDopoConfermaToggleCommand stampaDocumentoDopoConfermaToggleCommand;
    private TotalizzaDocumentoCommand totalizzaDocumentoCommand;
    private CloseRigheOrdineCommand closeRigheOrdineCommand;

    private ActionCommand inserisciRaggruppamentoArticoliCommand;
    private ActionCommand aggiungiVariazioneCommand;
    private RigheOrdineEditFrame righeOrdineEditFrame;
    private AumentaLivelloRigaOrdineCommand aumentaLivelloRigaOrdineCommand;
    private BloccaOrdineCommand bloccaOrdineCommand;
    private ApriDocumentoCollegatoCommand apriDocumentoCollegatoCommand;

    private EvadiAreaOrdineCommand evadiAreaOrdineCommand;

    private RicalcolaAreaOrdineCommand ricalcolaAreaOrdineCommand;
    private AttivaRigheInserimentoCommand attivaRigheInserimentoCommand;

    private boolean isDataConsegnaAreaOrdineChanged = false;
    private OperazioniSuRigheActionCommandInterceptor aggiungiVariazioneCommandInterceptor;
    private AumentaLivelloRigaOrdineCommandInterceptor aumentaLivelloRigaOrdineCommandInterceptor;
    private BloccaOrdineCommandInterceptor bloccaOrdineCommandInterceptor;
    private CloseRigheOrdineCommandInterceptor closeRigheOrdineCommandInterceptor;
    private InserisciRaggruppamentoArticoliCommandInterceptor inserisciRaggruppamentoArticoliCommandInterceptor;
    private OperazioniSuRigheActionCommandInterceptor ricalcolaAreaOrdineCommandInterceptor;
    private RefreshCommandInterceptor refreshCommandInterceptor = null;

    private TotalizzaDocumentoCommandInterceptor totalizzaDocumentoCommandInterceptor;

    private ConfigurazioneDistintaChangeListener configurazioneDistintaChangeListener;

    /**
     * Costruttore.
     */
    protected RigheOrdineTablePage() {
        super(PAGE_ID, new RigheOrdineTableModel());
        setEnableAutoResizeRow(true);
        getTable().setDelayForSelection(300);
        getTable().addSelectionObserver(this);
        getTable().getTable().setDragEnabled(true);
        getTable().getTable().setDropMode(DropMode.INSERT_ROWS);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        RigaArticoloPage rigaArticoloPage = (RigaArticoloPage) getEditPages().get(RigaArticoloDTO.class.getName());
        rigaArticoloPage.addPropertyChangeListener(RigaArticoloPage.CONFIGURATION_DISTINTA_CHANGED,
                getConfigurazioneDistintaChangeListener());
    }

    @Override
    protected EditFrame<RigaOrdineDTO> createEditFrame() {
        righeOrdineEditFrame = new RigheOrdineEditFrame(EEditPageMode.DETAIL, this, EditFrame.QUICK_ACTION_DEFAULT);
        return righeOrdineEditFrame;
    }

    @Override
    public void dispose() {
        RigaArticoloPage rigaArticoloPage = (RigaArticoloPage) getEditPages().get(RigaArticoloDTO.class.getName());
        rigaArticoloPage.removePropertyChangeListener(RigaArticoloPage.CONFIGURATION_DISTINTA_CHANGED,
                getConfigurazioneDistintaChangeListener());

        if (aggiungiVariazioneCommand != null) {
            aggiungiVariazioneCommand.removeCommandInterceptor(getAggiungiVariazioneCommandInterceptor());
        }
        if (ricalcolaAreaOrdineCommand != null) {
            ricalcolaAreaOrdineCommand.removeCommandInterceptor(getRicalcolaAreaOrdineCommandInterceptor());
        }
        if (aumentaLivelloRigaOrdineCommand != null) {
            aumentaLivelloRigaOrdineCommand.removeCommandInterceptor(getAumentaLivelloRigaOrdineCommandInterceptor());
        }
        if (bloccaOrdineCommand != null) {
            bloccaOrdineCommand.removeCommandInterceptor(getBloccaOrdineCommandInterceptor());
        }
        if (closeRigheOrdineCommand != null) {
            closeRigheOrdineCommand.removeCommandInterceptor(getCloseRigheOrdineCommandInterceptor());
        }
        if (inserisciRaggruppamentoArticoliCommand != null) {
            inserisciRaggruppamentoArticoliCommand
                    .removeCommandInterceptor(getInserisciRaggruppamentoArticoliCommandInterceptor());
        }
        if (totalizzaDocumentoCommand != null) {
            totalizzaDocumentoCommand.removeCommandInterceptor(getTotalizzaDocumentoCommandInterceptor());
        }

        getRefreshCommand().removeCommandInterceptor(getRefreshCommandInterceptor());

        super.dispose();
        righeOrdineEditFrame = null;
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
     * @return aggiungiVariazioneCommandInterceptor
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
    private ApriDocumentoCollegatoCommand getApriDocumentoCollegatoCommand() {
        if (apriDocumentoCollegatoCommand == null) {
            apriDocumentoCollegatoCommand = new ApriDocumentoCollegatoCommand(this);
        }
        return apriDocumentoCollegatoCommand;
    }

    /**
     * @return Returns the areaOrdineFullDTO.
     */
    public AreaOrdineFullDTO getAreaOrdineFullDTO() {
        return areaOrdineFullDTO;
    }

    /**
     * @return the attivaRigheInserimentoCommand
     */
    private AttivaRigheInserimentoCommand getAttivaRigheInserimentoCommand() {
        if (attivaRigheInserimentoCommand == null) {
            attivaRigheInserimentoCommand = new AttivaRigheInserimentoCommand();
        }

        return attivaRigheInserimentoCommand;
    }

    /**
     * @return command per legare una riga ordine ad una testata
     */
    private AumentaLivelloRigaOrdineCommand getAumentaLivelloRigaOrdineCommand() {
        if (aumentaLivelloRigaOrdineCommand == null) {
            aumentaLivelloRigaOrdineCommand = new AumentaLivelloRigaOrdineCommand(ordiniDocumentoBD);
            aumentaLivelloRigaOrdineCommand.addCommandInterceptor(getAumentaLivelloRigaOrdineCommandInterceptor());
        }
        return aumentaLivelloRigaOrdineCommand;
    }

    /**
     * @return AumentaLivelloRigaOrdineCommandInterceptor
     */
    private AumentaLivelloRigaOrdineCommandInterceptor getAumentaLivelloRigaOrdineCommandInterceptor() {
        if (aumentaLivelloRigaOrdineCommandInterceptor == null) {
            aumentaLivelloRigaOrdineCommandInterceptor = new AumentaLivelloRigaOrdineCommandInterceptor();
        }
        return aumentaLivelloRigaOrdineCommandInterceptor;
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
     * @return the bloccaOrdineCommand
     */
    public BloccaOrdineCommand getBloccaOrdineCommand() {
        if (bloccaOrdineCommand == null) {
            bloccaOrdineCommand = new BloccaOrdineCommand();
            bloccaOrdineCommand.addCommandInterceptor(getBloccaOrdineCommandInterceptor());
        }
        return bloccaOrdineCommand;
    }

    /**
     * @return BloccaOrdineCommandInterceptor
     */
    private BloccaOrdineCommandInterceptor getBloccaOrdineCommandInterceptor() {
        if (bloccaOrdineCommandInterceptor == null) {
            bloccaOrdineCommandInterceptor = new BloccaOrdineCommandInterceptor();
        }
        return bloccaOrdineCommandInterceptor;
    }

    /**
     * @return the closeRigheOrdineCommand
     */
    public CloseRigheOrdineCommand getCloseRigheOrdineCommand() {
        if (closeRigheOrdineCommand == null) {
            closeRigheOrdineCommand = new CloseRigheOrdineCommand(ordiniDocumentoBD, PAGE_ID);
            closeRigheOrdineCommand.addCommandInterceptor(getCloseRigheOrdineCommandInterceptor());
        }
        return closeRigheOrdineCommand;
    }

    /**
     * @return closeRigheOrdineCommandInterceptor
     */
    private CloseRigheOrdineCommandInterceptor getCloseRigheOrdineCommandInterceptor() {
        if (closeRigheOrdineCommandInterceptor == null) {
            closeRigheOrdineCommandInterceptor = new CloseRigheOrdineCommandInterceptor();
        }
        return closeRigheOrdineCommandInterceptor;
    }

    @Override
    public AbstractCommand[] getCommands() {
        AbstractCommand[] commands = new AbstractCommand[] { getAttivaRigheInserimentoCommand(), getDividiRigaCommand(),
                getApriDocumentoCollegatoCommand(), getEvadiAreaOrdineCommand(), getAumentaLivelloRigaOrdineCommand(),
                getRicalcolaAreaOrdineCommand(), getAggiungiVariazioneCommand(),
                getInserisciRaggruppamentoArticoliCommand(), getTotalizzaDocumentoCommand(),
                getAzioneDopoConfermaCommand(), getStampaDocumentoDopoConfermaToggleCommand(), getBloccaOrdineCommand(),
                getCloseRigheOrdineCommand(), getEditorDeleteCommand(), getRefreshCommand() };
        return commands;
    }

    /**
     * @return configurazioneDistintaChangeListener
     */
    private ConfigurazioneDistintaChangeListener getConfigurazioneDistintaChangeListener() {
        if (configurazioneDistintaChangeListener == null) {
            configurazioneDistintaChangeListener = new ConfigurazioneDistintaChangeListener();
        }
        return configurazioneDistintaChangeListener;
    }

    private AbstractCommand getDividiRigaCommand() {
        DividiRigaCommand command = new DividiRigaCommand(ordiniDocumentoBD);
        command.addCommandInterceptor(new ActionCommandInterceptor() {

            @Override
            public void postExecution(ActionCommand cmd) {
                getRefreshCommand().execute();
            }

            @Override
            public boolean preExecution(ActionCommand cmd) {
                cmd.addParameter(DividiRigaCommand.RIGA_SELEZIONATA,
                        RigheOrdineTablePage.this.getTable().getSelectedObject());
                return true;
            }
        });
        return command;
    }

    @Override
    public AbstractCommand getEditorNewCommand() {
        return getNewCommands().get(RigaArticoloDTO.class.getName());
    }

    /**
     * @return the evadiAreaOrdineCommand
     */
    private EvadiAreaOrdineCommand getEvadiAreaOrdineCommand() {
        if (evadiAreaOrdineCommand == null) {
            evadiAreaOrdineCommand = new EvadiAreaOrdineCommand();
            evadiAreaOrdineCommand.addCommandInterceptor(new EvadiAreaOrdineCommandInterceptor());
        }

        return evadiAreaOrdineCommand;
    }

    /**
     * @return comando per inserire un raggruppamento articoli
     */
    protected AbstractCommand getInserisciRaggruppamentoArticoliCommand() {
        if (inserisciRaggruppamentoArticoliCommand == null) {
            inserisciRaggruppamentoArticoliCommand = new InserisciRaggruppamentoArticoliRigheOrdineCommand();
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

    @Override
    public Object getManagedObject(Object pageObject) {

        if (pageObject instanceof RigaOrdine) {
            areaOrdineFullDTO.setAreaOrdine(((RigaOrdine) pageObject).getAreaOrdine());
            areaOrdineFullDTO.setInserimentoRigheMassivo(false);
            // Quando una riga magazzino cambia (insert/edit/delete), se il
            // comando di totalizzazione è automatico lo lancio
            if (getTotalizzaDocumentoCommand().isTotalizzazioneAutomatica()) {
                getTotalizzaDocumentoCommand().setAreaOrdineFullDTO(areaOrdineFullDTO);
                getTotalizzaDocumentoCommand().execute();
            }
            return areaOrdineFullDTO;
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
    public RicalcolaAreaOrdineCommand getRicalcolaAreaOrdineCommand() {
        if (ricalcolaAreaOrdineCommand == null) {
            ricalcolaAreaOrdineCommand = new RicalcolaAreaOrdineCommand();
            ricalcolaAreaOrdineCommand.addCommandInterceptor(getRicalcolaAreaOrdineCommandInterceptor());
        }
        return ricalcolaAreaOrdineCommand;
    }

    /**
     * @return aggiungiVariazioneCommandInterceptor
     */
    private OperazioniSuRigheActionCommandInterceptor getRicalcolaAreaOrdineCommandInterceptor() {
        if (ricalcolaAreaOrdineCommandInterceptor == null) {
            ricalcolaAreaOrdineCommandInterceptor = new OperazioniSuRigheActionCommandInterceptor();
        }
        return ricalcolaAreaOrdineCommandInterceptor;
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
            totalizzaDocumentoCommand = new TotalizzaDocumentoCommand(this.ordiniDocumentoBD);
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
    public Collection<RigaOrdineDTO> loadTableData() {
        if (!areaOrdineFullDTO.getAreaOrdine().isNew()) {
            return ordiniDocumentoBD.caricaRigheOrdineDTO(areaOrdineFullDTO.getAreaOrdine());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public void onPostPageOpen() {
    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public void preSetFormObject(Object object) {
        final Date dataConsegnaOld = this.areaOrdineFullDTO != null
                ? this.areaOrdineFullDTO.getAreaOrdine().getDataConsegna() : null;
        Integer idAreaOld = this.areaOrdineFullDTO != null ? this.areaOrdineFullDTO.getId() : null;

        final AreaOrdine areaOrdineNew = ((AreaOrdineFullDTO) object).getAreaOrdine();
        Integer idAreaNew = areaOrdineNew.getId();
        Date dataConsegnaNew = areaOrdineNew.getDataConsegna();

        // l'area ordine deve essere la stessa altrimenti significa che sto
        // caricando un'altra area ordine e in questo
        // caso non devo reimpostare la data consegna delle righe
        boolean isStessaArea = idAreaOld != null && idAreaNew != null && idAreaOld.equals(idAreaNew);

        // se la data di consegna è cambiata devo aggiornare la dataConsegna
        // delle righe
        isDataConsegnaAreaOrdineChanged = isStessaArea && dataConsegnaOld != null && dataConsegnaNew != null
                && !dataConsegnaOld.equals(dataConsegnaNew);

        if (isDataConsegnaAreaOrdineChanged) {
            ConfirmationDialog messageDialog = new ConfirmationDialog("Attenzione",
                    "<b>Si</b>: modifica la data di consegna delle sole righe ordine con valore "
                            + new SimpleDateFormat("dd/MM/yyyy").format(dataConsegnaOld) + "<br>"
                            + "<b>No</b>: modifica la data di consegna di tutte le righe<br><br>"
                            + "<b>Nota</b>: sono escluse dalla modifica le righe in stato chiuso") {

                @Override
                protected void onCancel() {
                    // se chiudo il dialogo con NO, non passo una data di
                    // riferimento e quindi aggiorno tutte le
                    // righe
                    ordiniDocumentoBD.aggiornaDataConsegna(areaOrdineNew, null);
                    super.onCancel();
                }

                @Override
                protected void onConfirm() {
                    // se premo SI, allora voglio aggiornare solo le righe con
                    // data consegna uguale alla
                    // dataConsegna della testata prima del salvataggio, quindi
                    // passo la data di riferimento
                    ordiniDocumentoBD.aggiornaDataConsegna(areaOrdineNew, dataConsegnaOld);
                }

            };
            messageDialog.setPreferredSize(new Dimension(380, 150));
            messageDialog.showDialog();

        }
        super.preSetFormObject(object);
    }

    @Override
    public void processTableData(Collection<RigaOrdineDTO> results) {
        // Devo ricaricarmi le righe solamente se cambio l'area magazzino.
        // Se l'area magazzino e' la stessa non ricarico le righe.
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
    public Collection<RigaOrdineDTO> refreshTableData() {
        if (areaOrdineFullDTO.getId() == null) {
            return Collections.emptyList();
        } else if (isDataConsegnaAreaOrdineChanged) {
            // se la data di consegna della testata è stata modificata (vedi
            // presetFormObject) allora devo reimpostare
            // il flag a false e ricaricare le righe
            isDataConsegnaAreaOrdineChanged = false;
            return loadTableData();
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
        settings.setBoolean(KEY_STAMPA_DOPO_CONFERMA, getStampaDocumentoDopoConfermaToggleCommand().isSelected());
        settings.setBoolean(TotalizzaDocumentoCommand.KEY_TOTALIZZAZIONE_AUTOMATICA,
                getTotalizzaDocumentoCommand().isTotalizzazioneAutomatica());
        super.saveState(settings);
    }

    @Override
    public void setFormObject(Object object) {
        this.areaOrdineFullDTO = (AreaOrdineFullDTO) object;

        for (Entry<String, IPageEditor> entryPages : getEditPages().entrySet()) {

            if (entryPages.getKey().equals(RigaArticoloDTO.class.getName())) {
                ((RigaArticoloPage) entryPages.getValue()).setAreaOrdineFullDTO(this.areaOrdineFullDTO);
            } else if (entryPages.getKey().equals(RigaNotaDTO.class.getName())) {
                ((RigaNotaPage) entryPages.getValue()).setAreaOrdineFullDTO(this.areaOrdineFullDTO);
            } else if (entryPages.getKey().equals(RigaTestataDTO.class.getName())) {
                ((RigaTestataPage) entryPages.getValue()).setAreaOrdineFullDTO(this.areaOrdineFullDTO);
            }
        }
        updateControl();
        updateCommands();

        setVisible(!this.areaOrdineFullDTO.isInserimentoRigheMassivo());
    }

    /**
     * @param ordiniDocumentoBD
     *            the ordiniDocumentoBD to set
     */
    public void setOrdiniDocumentoBD(IOrdiniDocumentoBD ordiniDocumentoBD) {
        this.ordiniDocumentoBD = ordiniDocumentoBD;
        getTable().getTable().setTransferHandler(new RigheOrdineTransferHandler(ordiniDocumentoBD, this));
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

        boolean confermaEnable = !areaOrdineFullDTO.getAreaOrdine().getDatiValidazioneRighe().isValid() && enabled;
        boolean isNewAreaOrdine = areaOrdineFullDTO.isNew();
        if (isNewAreaOrdine) {
            confermaEnable = false;
        }
        getCloseRigheOrdineCommand().setEnabled(confermaEnable);

        boolean totalizzaEnabled = enabled;

        getTotalizzaDocumentoCommand().setVisible(totalizzaEnabled);
        getTotalizzaDocumentoCommand().setEnabled(totalizzaEnabled);
        getInserisciRaggruppamentoArticoliCommand()
                .setEnabled(!isNewAreaOrdine && !areaOrdineFullDTO.getAreaOrdine().isEvaso());

        getBloccaOrdineCommand().setEnabled(!getTable().getRows().isEmpty()
                && areaOrdineFullDTO.getAreaOrdine().getStatoAreaOrdine() != StatoAreaOrdine.BLOCCATO);
        logger.debug("--> Exit updateCommands");
    }

    /**
     * abilita disabilita la table page corrente.
     */
    protected void updateControl() {
        boolean ordineVuoto = (areaOrdineFullDTO.isNew());
        boolean ordineEvaso = areaOrdineFullDTO.getAreaOrdine().getStatoAreaOrdine() == StatoAreaOrdine.CONFERMATO
                && areaOrdineFullDTO.getAreaOrdine().isEvaso();
        setReadOnly(ordineVuoto || ordineEvaso);
    }

}
