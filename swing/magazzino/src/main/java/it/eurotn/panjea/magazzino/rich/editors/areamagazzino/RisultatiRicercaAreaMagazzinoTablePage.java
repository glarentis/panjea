package it.eurotn.panjea.magazzino.rich.editors.areamagazzino;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationEvent;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.list.ListMultipleSelectionGuard;
import org.springframework.richclient.list.ListSelectionValueModelAdapter;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

import it.eurotn.panjea.anagrafica.rich.editors.documento.AbstractEliminaDocumentoCommand.ETipoCancellazione;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino.StatoAreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzinoLite;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.commands.EliminaAreaMagazzinoCommand;
import it.eurotn.panjea.magazzino.rich.commands.documento.GestisciRigheNonValideCommand;
import it.eurotn.panjea.magazzino.rich.commands.documento.SpedizioneDocumentiCommand;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;
import it.eurotn.panjea.magazzino.util.ParametriRicercaAreaMagazzino;
import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;
import it.eurotn.panjea.rich.editors.documento.StampaAreeDocumentoCommand;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.eurotn.rich.editors.AbstractTablePageEditor;

/**
 * TablePage per la visualizzazione dei risultati della ricerca di {@link AreaMagazzino}.
 *
 * @author adriano
 * @version 1.0, 30/set/2008
 */
public class RisultatiRicercaAreaMagazzinoTablePage extends AbstractTablePageEditor<AreaMagazzinoRicerca> {

    private class EliminaAreaMagazzinoActionCommandInterceptor implements ActionCommandInterceptor {

        @Override
        public void postExecution(ActionCommand command) {
            if (!((EliminaAreaMagazzinoCommand) command).getTipoCancellazione().equals(ETipoCancellazione.NESSUNO)) {
                // Cancello dalla tabella
                for (AreaMagazzinoRicerca areaMagazzinoRicerca : ((EliminaAreaMagazzinoCommand) command)
                        .getAreeMagazzinoRicerca()) {
                    getTable().removeRowObject(areaMagazzinoRicerca);
                }
            }
        }

        @Override
        public boolean preExecution(ActionCommand command) {
            List<AreaMagazzinoRicerca> areeRicerca = getTable().getSelectedObjects();
            ((EliminaAreaMagazzinoCommand) command).setAreeMagazzinoRicerca(areeRicerca);
            return true;
        }
    }

    private class OpenAreaMagazzinoEditor extends ApplicationWindowAwareCommand {

        private static final String COMMAND_ID = "openAreaMagazzinoCommand";

        /**
         * Costruttore.
         */
        public OpenAreaMagazzinoEditor() {
            super(COMMAND_ID);
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            RisultatiRicercaAreaMagazzinoTablePage.this.openAreaMagazzinoEditor();
        }
    }

    /**
     * Interceptor per settare le aree magazzino da verificare al command prima dell'esecuzione.
     *
     * @author Leonardo
     */
    private class RigheMagazzinoNonValideCommandInterceptor implements ActionCommandInterceptor {

        @Override
        public void postExecution(ActionCommand command) {

        }

        @Override
        public boolean preExecution(ActionCommand command) {
            List<AreaMagazzinoLite> list = new ArrayList<AreaMagazzinoLite>();
            for (AreaMagazzinoRicerca areaMagazzinoRicerca : getTable().getRows()) {
                AreaMagazzinoLite areaMagazzinoLite = new AreaMagazzinoLite();
                areaMagazzinoLite.setId(areaMagazzinoRicerca.getIdAreaMagazzino());
                // setto la versione altrimenti hibernate vede
                // l'areaMagazzinoLite transient e chiede di salvarla
                areaMagazzinoLite.setVersion(0);
                list.add(areaMagazzinoLite);
            }
            getGestisciRigheNonValideCommand().setAreeMagazzino(list);
            return true;
        }
    }

    private class SpedizioneMagazzinoCommandInterceptor extends ActionCommandInterceptorAdapter {

        @Override
        public boolean preExecution(ActionCommand command) {

            List<AreaMagazzinoRicerca> selectedObjects = getTable().getSelectedObjects();

            if (selectedObjects != null) {
                List<Integer> idDocumenti = new ArrayList<Integer>();
                for (AreaMagazzinoRicerca areaMagazzinoRicerca : selectedObjects) {
                    if (areaMagazzinoRicerca.getStato() == StatoAreaMagazzino.CONFERMATO
                            || areaMagazzinoRicerca.getStato() == StatoAreaMagazzino.FORZATO) {
                        idDocumenti.add(areaMagazzinoRicerca.getDocumento().getId());
                    }
                }
                command.addParameter(SpedizioneDocumentiCommand.PARAM_ID_DOCUMENTI_DA_SPEDIRE, idDocumenti);
            }

            return selectedObjects != null && !selectedObjects.isEmpty();
        }
    }

    private static Logger logger = Logger.getLogger(RisultatiRicercaAreaMagazzinoTablePage.class);
    public static final String PAGE_ID = "risultatiRicercaAreaMagazzinoTablePage";
    protected ParametriRicercaAreaMagazzino parametriRicercaAreaMagazzino = null;
    private IMagazzinoDocumentoBD magazzinoDocumentoBD = null;
    private OpenAreaMagazzinoEditor openAreaMagazzinoEditor = null;
    private EliminaAreaMagazzinoCommand eliminaAreaMagazzinoCommand = null;

    private GestisciRigheNonValideCommand gestisciRigheNonValideCommand = null;

    private SpedizioneDocumentiCommand<AreaMagazzino> stampaAreeMagazzinoCommand;

    private ActionCommandInterceptor stampaAreeMagazzinoCommandInterceptor;
    private EliminaAreaMagazzinoActionCommandInterceptor eliminaAreaMagazzinoActionCommandInterceptor;

    private SpedizioneDocumentiCommand<AreaMagazzino> spedizioneDocumentiCommand;
    private SpedizioneMagazzinoCommandInterceptor spedizioneOrdiniCommandInterceptor;

    // Adapter per la selezione della tabella
    private ListSelectionValueModelAdapter listSelectionAdapter;

    private ListMultipleSelectionGuard listMultipleSelectionGuard;

    /**
     * Costruttore.
     */
    public RisultatiRicercaAreaMagazzinoTablePage() {
        super(PAGE_ID, new AreaMagazzinoRicercaTableModel());
    }

    /**
     * Costruttore.
     *
     * @param pageId
     *            id della pagina
     * @param tableModel
     *            table model
     */
    public RisultatiRicercaAreaMagazzinoTablePage(final String pageId,
            final DefaultBeanTableModel<AreaMagazzinoRicerca> tableModel) {
        super(pageId, tableModel);
    }

    @Override
    public void dispose() {
        getStampaAreeMagazzinoCommand().removeCommandInterceptor(stampaAreeMagazzinoCommandInterceptor);
        stampaAreeMagazzinoCommandInterceptor = null;

        listSelectionAdapter.removeValueChangeListener(listMultipleSelectionGuard);
        getTable().getTable().getSelectionModel().removeListSelectionListener(listSelectionAdapter);

        getSpedizioneDocumentiCommand().removeCommandInterceptor(spedizioneOrdiniCommandInterceptor);
        super.dispose();
    }

    @Override
    public AbstractCommand[] getCommands() {
        AbstractCommand[] abstractCommands = new AbstractCommand[] { getGestisciRigheNonValideCommand(),
                getOpenAreaMagazzinoEditor(), getSpedizioneDocumentiCommand(), getStampaAreeMagazzinoCommand(),
                getEliminaCommand() };
        return abstractCommands;
    }

    @Override
    public AbstractCommand getEditorDeleteCommand() {
        return getEliminaCommand();
    }

    /**
     * @return the EliminaAreaMagazzinoActionCommandInterceptor to get
     */
    private EliminaAreaMagazzinoActionCommandInterceptor getEliminaAreaMagazzinoActionInterceptor() {
        if (eliminaAreaMagazzinoActionCommandInterceptor == null) {
            eliminaAreaMagazzinoActionCommandInterceptor = new EliminaAreaMagazzinoActionCommandInterceptor();
        }
        return eliminaAreaMagazzinoActionCommandInterceptor;
    }

    /**
     *
     * @return command
     */
    public EliminaAreaMagazzinoCommand getEliminaCommand() {
        if (eliminaAreaMagazzinoCommand == null) {
            eliminaAreaMagazzinoCommand = new EliminaAreaMagazzinoCommand(PAGE_ID);
            eliminaAreaMagazzinoCommand.setMagazzinoDocumentoBD(magazzinoDocumentoBD);
            new ListMultipleSelectionGuard(getListSelectionAdapter(), eliminaAreaMagazzinoCommand);
            eliminaAreaMagazzinoCommand.addCommandInterceptor(getEliminaAreaMagazzinoActionInterceptor());
        }
        return eliminaAreaMagazzinoCommand;
    }

    /**
     *
     * @return comando per aprire la gestione delle regole per le righe articolo
     */
    public GestisciRigheNonValideCommand getGestisciRigheNonValideCommand() {
        if (gestisciRigheNonValideCommand == null) {
            gestisciRigheNonValideCommand = new GestisciRigheNonValideCommand(PAGE_ID);
            gestisciRigheNonValideCommand.addCommandInterceptor(new RigheMagazzinoNonValideCommandInterceptor());
        }
        return gestisciRigheNonValideCommand;
    }

    /**
     * Inizializzazione lazy.
     *
     * @return adapter per la selezione della tabella
     */
    private ListSelectionValueModelAdapter getListSelectionAdapter() {
        if (listSelectionAdapter == null) {
            listSelectionAdapter = new ListSelectionValueModelAdapter(getTable().getTable().getSelectionModel());
        }
        return listSelectionAdapter;
    }

    /**
     *
     * @return command
     */
    public OpenAreaMagazzinoEditor getOpenAreaMagazzinoEditor() {
        if (openAreaMagazzinoEditor == null) {
            openAreaMagazzinoEditor = new OpenAreaMagazzinoEditor();
            listMultipleSelectionGuard = new ListMultipleSelectionGuard(getListSelectionAdapter(),
                    openAreaMagazzinoEditor);
            getTable().setPropertyCommandExecutor(openAreaMagazzinoEditor);
        }
        return openAreaMagazzinoEditor;
    }

    /**
     * @return the spedizioneDocumentiCommand
     */
    private SpedizioneDocumentiCommand<AreaMagazzino> getSpedizioneDocumentiCommand() {
        if (spedizioneDocumentiCommand == null) {
            spedizioneDocumentiCommand = new SpedizioneDocumentiCommand<AreaMagazzino>(AreaMagazzino.class);
            spedizioneOrdiniCommandInterceptor = new SpedizioneMagazzinoCommandInterceptor();
            spedizioneDocumentiCommand.addCommandInterceptor(spedizioneOrdiniCommandInterceptor);
        }

        return spedizioneDocumentiCommand;
    }

    /**
     *
     * @return command
     */
    public SpedizioneDocumentiCommand<AreaMagazzino> getStampaAreeMagazzinoCommand() {
        if (stampaAreeMagazzinoCommand == null) {
            stampaAreeMagazzinoCommand = new SpedizioneDocumentiCommand<AreaMagazzino>(
                    StampaAreeDocumentoCommand.COMMAND_ID, AreaMagazzino.class);
            new ListMultipleSelectionGuard(getListSelectionAdapter(), stampaAreeMagazzinoCommand);
            stampaAreeMagazzinoCommandInterceptor = new ActionCommandInterceptor() {

                @Override
                public void postExecution(ActionCommand command) {
                }

                @Override
                public boolean preExecution(ActionCommand command) {
                    List<AreaMagazzinoRicerca> selectedObjects = getTable().getSelectedObjects();

                    if (selectedObjects != null) {
                        List<Integer> idDocumenti = new ArrayList<Integer>();
                        for (AreaMagazzinoRicerca areaMagazzinoRicerca : selectedObjects) {
                            if (areaMagazzinoRicerca.getStato() == StatoAreaMagazzino.CONFERMATO
                                    || areaMagazzinoRicerca.getStato() == StatoAreaMagazzino.FORZATO) {
                                idDocumenti.add(areaMagazzinoRicerca.getDocumento().getId());
                            }
                        }
                        command.addParameter(SpedizioneDocumentiCommand.PARAM_ID_DOCUMENTI_DA_SPEDIRE, idDocumenti);
                        command.addParameter(SpedizioneDocumentiCommand.PARAM_FORZA_STAMPA, true);
                    }

                    return selectedObjects != null && !selectedObjects.isEmpty();
                }
            };
            stampaAreeMagazzinoCommand.addCommandInterceptor(stampaAreeMagazzinoCommandInterceptor);
        }

        return stampaAreeMagazzinoCommand;
    }

    @Override
    public List<AreaMagazzinoRicerca> loadTableData() {
        List<AreaMagazzinoRicerca> areeMagazzino = Collections.emptyList();

        if (parametriRicercaAreaMagazzino.getAreeMagazzino() != null
                && !parametriRicercaAreaMagazzino.getAreeMagazzino().isEmpty()) {
            areeMagazzino = parametriRicercaAreaMagazzino.getAreeMagazzino();
        } else {
            if (parametriRicercaAreaMagazzino.isEffettuaRicerca()) {
                areeMagazzino = magazzinoDocumentoBD.ricercaAreeMagazzino(parametriRicercaAreaMagazzino);
            }
        }
        return areeMagazzino;
    }

    @Override
    public void onEditorEvent(ApplicationEvent event) {
        // se è una cancellazione di un area magazzino la trasformo in un
        // areamagazzinoricerca per cancellarla
        PanjeaLifecycleApplicationEvent panjeaEvent = (PanjeaLifecycleApplicationEvent) event;
        if (panjeaEvent.getEventType().equals(LifecycleApplicationEvent.DELETED)
                && panjeaEvent.getSource() instanceof AreaMagazzino) {
            AreaMagazzinoRicerca areaMagazzinoRicerca = new AreaMagazzinoRicerca();
            areaMagazzinoRicerca.setIdAreaMagazzino(((AreaMagazzino) panjeaEvent.getSource()).getId());
            getTable().removeRowObject(areaMagazzinoRicerca);
        } else {
            super.onEditorEvent(event);
        }
    }

    @Override
    public void onPostPageOpen() {
        // nothing to do
    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    /**
     * Lancia l'evento per aprire l'area di magazzino selezionata.
     */
    private void openAreaMagazzinoEditor() {
        logger.debug("--> Enter openAreaMagazzinoEditor");
        AreaMagazzinoRicerca areaMagazzinoRicerca = getTable().getSelectedObject();
        if (areaMagazzinoRicerca == null) {
            return;
        }
        AreaMagazzino areaMagazzino = new AreaMagazzino();
        areaMagazzino.setId(areaMagazzinoRicerca.getIdAreaMagazzino());
        // areaMagazzino.setDepositoOrigine(areaMagazzinoRicerca.getDepositoOrigine());

        AreaMagazzinoFullDTO areaMagazzinoFullDTO = magazzinoDocumentoBD.caricaAreaMagazzinoFullDTO(areaMagazzino);
        LifecycleApplicationEvent event = new OpenEditorEvent(areaMagazzinoFullDTO);
        Application.instance().getApplicationContext().publishEvent(event);
        logger.debug("--> Exit openAreaMagazzinoEditor");
    }

    @Override
    public List<AreaMagazzinoRicerca> refreshTableData() {
        return loadTableData();
    }

    @Override
    public void setFormObject(Object object) {
        if (object instanceof ParametriRicercaAreaMagazzino) {
            this.parametriRicercaAreaMagazzino = (ParametriRicercaAreaMagazzino) object;
        } else {
            this.parametriRicercaAreaMagazzino = new ParametriRicercaAreaMagazzino();
        }
        getTable().setTableHeader(parametriRicercaAreaMagazzino);
    }

    /**
     * @param magazzinoDocumentoBD
     *            The magazzinoDocumentoBD to set.
     */
    public void setMagazzinoDocumentoBD(IMagazzinoDocumentoBD magazzinoDocumentoBD) {
        this.magazzinoDocumentoBD = magazzinoDocumentoBD;
    }
}
