package it.eurotn.panjea.fatturepa.rich.editors.ricerca;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Observable;

import javax.swing.CellEditor;

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

import com.jidesoft.grid.CellEditorFactory;
import com.jidesoft.grid.CellEditorManager;
import com.jidesoft.grid.CellRendererManager;
import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

import it.eurotn.panjea.fatturepa.domain.StatoFatturaPA;
import it.eurotn.panjea.fatturepa.rich.bd.IFatturePABD;
import it.eurotn.panjea.fatturepa.rich.commands.CreaEFirmaXMLFatturePACommand;
import it.eurotn.panjea.fatturepa.rich.commands.SendXMLFatturaPAtoSDICommand;
import it.eurotn.panjea.fatturepa.rich.editors.ricerca.statoarea.StatoFatturaPACellRenderer;
import it.eurotn.panjea.fatturepa.rich.editors.ricerca.statoarea.StatoFatturaPAController;
import it.eurotn.panjea.fatturepa.rich.editors.ricerca.xmlaction.FileXMLFatturaCellEditorRenderer;
import it.eurotn.panjea.fatturepa.util.AreaMagazzinoFatturaPARicerca;
import it.eurotn.panjea.fatturepa.util.ParametriRicercaFatturePA;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;
import it.eurotn.panjea.rich.editors.documento.StampaAreeDocumentoCommand;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.report.ReportManager;

public class RisultatiRicercaFatturePATablePage extends AbstractTablePageEditor<AreaMagazzinoFatturaPARicerca> {

    private class CreaEFirmaXMLFatturePACommandInterceptor extends ActionCommandInterceptorAdapter {

        @Override
        public void postExecution(ActionCommand command) {
            refreshData();
        }

        @Override
        public boolean preExecution(ActionCommand command) {

            List<Integer> idAree = new ArrayList<Integer>();
            for (AreaMagazzinoFatturaPARicerca area : getTable().getSelectedObjects()) {
                idAree.add(area.getIdAreaMagazzino());
            }

            command.addParameter(CreaEFirmaXMLFatturePACommand.PARAM_ID_AREE_MAGAZZINO, idAree);
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
            this.setSecurityControllerId(RisultatiRicercaFatturePATablePage.this.getId() + COMMAND_ID);
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            RisultatiRicercaFatturePATablePage.this.openAreaMagazzinoEditor();
        }
    }

    private class SendXMLFatturaPatoSDICommandInterceptor extends ActionCommandInterceptorAdapter {

        @Override
        public void postExecution(ActionCommand command) {
            refreshData();
        }

        @Override
        public boolean preExecution(ActionCommand command) {

            Integer idArea = null;
            if (getTable().getSelectedObject() != null) {
                idArea = getTable().getSelectedObject().getIdAreaMagazzino();
            }

            command.addParameter(SendXMLFatturaPAtoSDICommand.PARAM_ID_AREA_MAGAZZINO, idArea);
            return idArea != null;
        }
    }

    public static final String PAGE_ID = "risultatiRicercaFatturePATablePage";
    protected ParametriRicercaFatturePA parametriRicercaFatturePA = null;

    private IFatturePABD fatturePABD = null;
    private IMagazzinoDocumentoBD magazzinoDocumentoBD = null;

    private OpenAreaMagazzinoEditor openAreaMagazzinoEditor = null;

    private ReportManager reportManager;
    private StampaAreeDocumentoCommand stampaAreeMagazzinoCommand;

    private CreaEFirmaXMLFatturePACommand creaEFirmaXMLFatturePACommand;
    private CreaEFirmaXMLFatturePACommandInterceptor creaEFirmaXMLFatturePACommandInterceptor;

    private SendXMLFatturaPAtoSDICommand sendXMLFatturaPAtoSDICommand;
    private SendXMLFatturaPatoSDICommandInterceptor sendXMLFatturaPatoSDICommandInterceptor;

    private ActionCommandInterceptor stampaAreeMagazzinoCommandInterceptor;

    // Adapter per la selezione della tabella
    private ListSelectionValueModelAdapter listSelectionAdapter;

    private ListMultipleSelectionGuard listMultipleSelectionGuard;

    /**
     * Costruttore.
     */
    public RisultatiRicercaFatturePATablePage() {
        super(PAGE_ID, new RisultatiRicercaFatturePATableModel());

        // registro qui il renderer sull'esito di spedizione senza appesantire l'xml
        CellRendererManager.registerRenderer(String.class, new FileXMLFatturaCellEditorRenderer(this),
                FileXMLFatturaCellEditorRenderer.CONTEXT);
        CellEditorManager.registerEditor(String.class, new CellEditorFactory() {

            @Override
            public CellEditor create() {
                return new FileXMLFatturaCellEditorRenderer(RisultatiRicercaFatturePATablePage.this);
            }
        }, FileXMLFatturaCellEditorRenderer.CONTEXT);
        CellRendererManager.registerRenderer(StatoFatturaPA.class, new StatoFatturaPACellRenderer());

        new StatoFatturaPAController(this);
    }

    @Override
    public void dispose() {
        getStampaAreeMagazzinoCommand().removeCommandInterceptor(stampaAreeMagazzinoCommandInterceptor);
        stampaAreeMagazzinoCommandInterceptor = null;

        getCreaEFirmaXMLFatturePACommand().removeCommandInterceptor(creaEFirmaXMLFatturePACommandInterceptor);
        creaEFirmaXMLFatturePACommand = null;

        getSendXMLFatturaPAtoSDICommand().removeCommandInterceptor(sendXMLFatturaPatoSDICommandInterceptor);
        sendXMLFatturaPAtoSDICommand = null;

        listSelectionAdapter.removeValueChangeListener(listMultipleSelectionGuard);
        getTable().getTable().getSelectionModel().removeListSelectionListener(listSelectionAdapter);
        super.dispose();
    }

    @Override
    public AbstractCommand[] getCommands() {
        return new AbstractCommand[] { getCreaEFirmaXMLFatturePACommand(), getSendXMLFatturaPAtoSDICommand(),
                getOpenAreaMagazzinoEditor(), getStampaAreeMagazzinoCommand() };
    }

    /**
     * @return the creaEFirmaXMLFatturePACommand
     */
    private CreaEFirmaXMLFatturePACommand getCreaEFirmaXMLFatturePACommand() {
        if (creaEFirmaXMLFatturePACommand == null) {
            creaEFirmaXMLFatturePACommand = new CreaEFirmaXMLFatturePACommand();
            creaEFirmaXMLFatturePACommandInterceptor = new CreaEFirmaXMLFatturePACommandInterceptor();
            creaEFirmaXMLFatturePACommand.addCommandInterceptor(creaEFirmaXMLFatturePACommandInterceptor);
        }

        return creaEFirmaXMLFatturePACommand;
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
     * @return the sendXMLFatturaPAtoSDICommand
     */
    private SendXMLFatturaPAtoSDICommand getSendXMLFatturaPAtoSDICommand() {
        if (sendXMLFatturaPAtoSDICommand == null) {
            sendXMLFatturaPAtoSDICommand = new SendXMLFatturaPAtoSDICommand();
            sendXMLFatturaPatoSDICommandInterceptor = new SendXMLFatturaPatoSDICommandInterceptor();
            sendXMLFatturaPAtoSDICommand.addCommandInterceptor(sendXMLFatturaPatoSDICommandInterceptor);
        }

        return sendXMLFatturaPAtoSDICommand;
    }

    /**
     *
     * @return command
     */
    public StampaAreeDocumentoCommand getStampaAreeMagazzinoCommand() {
        if (stampaAreeMagazzinoCommand == null) {
            stampaAreeMagazzinoCommand = new StampaAreeDocumentoCommand(this.reportManager);
            new ListMultipleSelectionGuard(getListSelectionAdapter(), stampaAreeMagazzinoCommand);
            stampaAreeMagazzinoCommandInterceptor = new ActionCommandInterceptorAdapter() {

                @Override
                public boolean preExecution(ActionCommand command) {
                    List<AreaMagazzino> list = new ArrayList<AreaMagazzino>();
                    for (AreaMagazzinoFatturaPARicerca areaMagazzinoRicerca : getTable().getSelectedObjects()) {
                        AreaMagazzino areaMagazzinoStampa = new AreaMagazzino();
                        areaMagazzinoStampa.setId(areaMagazzinoRicerca.getIdAreaMagazzino());
                        areaMagazzinoStampa.setTipoAreaMagazzino(areaMagazzinoRicerca.getTipoAreaMagazzino());
                        areaMagazzinoStampa.setDocumento(areaMagazzinoRicerca.getDocumento());
                        list.add(areaMagazzinoStampa);
                    }
                    command.addParameter(StampaAreeDocumentoCommand.PARAM_AREE_DA_STAMPARE, list);
                    return true;
                }
            };
            stampaAreeMagazzinoCommand.addCommandInterceptor(stampaAreeMagazzinoCommandInterceptor);
        }

        return stampaAreeMagazzinoCommand;
    }

    @Override
    public List<AreaMagazzinoFatturaPARicerca> loadTableData() {
        List<AreaMagazzinoFatturaPARicerca> areeMagazzino = Collections.emptyList();

        if (parametriRicercaFatturePA.isEffettuaRicerca()) {
            areeMagazzino = fatturePABD.ricercaFatturePA(parametriRicercaFatturePA);
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
            AreaMagazzinoFatturaPARicerca areaMagazzinoRicerca = new AreaMagazzinoFatturaPARicerca();
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
        AreaMagazzinoFatturaPARicerca areaMagazzinoRicerca = getTable().getSelectedObject();
        if (areaMagazzinoRicerca == null) {
            return;
        }
        AreaMagazzino areaMagazzino = new AreaMagazzino();
        areaMagazzino.setId(areaMagazzinoRicerca.getIdAreaMagazzino());

        AreaMagazzinoFullDTO areaMagazzinoFullDTO = magazzinoDocumentoBD.caricaAreaMagazzinoFullDTO(areaMagazzino);
        LifecycleApplicationEvent event = new OpenEditorEvent(areaMagazzinoFullDTO);
        Application.instance().getApplicationContext().publishEvent(event);
        logger.debug("--> Exit openAreaMagazzinoEditor");
    }

    @Override
    public void processTableData(Collection<AreaMagazzinoFatturaPARicerca> results) {
        AreaMagazzinoFatturaPARicerca selectedObject = getTable().getSelectedObject();
        setRows(results);
        if (selectedObject != null && results.contains(selectedObject)) {
            getTable().selectRowObject(selectedObject, null);
        }
    }

    @Override
    public List<AreaMagazzinoFatturaPARicerca> refreshTableData() {
        return loadTableData();
    }

    /**
     * @param fatturePABD
     *            the fatturePABD to set
     */
    public void setFatturePABD(IFatturePABD fatturePABD) {
        this.fatturePABD = fatturePABD;
    }

    @Override
    public void setFormObject(Object object) {
        if (object instanceof ParametriRicercaFatturePA) {
            this.parametriRicercaFatturePA = (ParametriRicercaFatturePA) object;
        } else {
            this.parametriRicercaFatturePA = new ParametriRicercaFatturePA();
        }
    }

    /**
     * @param magazzinoDocumentoBD
     *            The magazzinoDocumentoBD to set.
     */
    public void setMagazzinoDocumentoBD(IMagazzinoDocumentoBD magazzinoDocumentoBD) {
        this.magazzinoDocumentoBD = magazzinoDocumentoBD;
    }

    /**
     * setter for Report manager.
     *
     * @param reportManager
     *            report Manager
     */
    public void setReportManager(ReportManager reportManager) {
        this.reportManager = reportManager;
    }

    @Override
    public void update(Observable observable, Object obj) {
        super.update(observable, obj);

        if (obj != null) {
            AreaMagazzinoFatturaPARicerca area = (AreaMagazzinoFatturaPARicerca) obj;
            sendXMLFatturaPAtoSDICommand.setEnabled(area.getStatoFatturaPA() == null
                    || area.getStatoFatturaPA() == StatoFatturaPA._DI || !area.isStatoFatturaEsitoPositivo());
        }
    }
}
