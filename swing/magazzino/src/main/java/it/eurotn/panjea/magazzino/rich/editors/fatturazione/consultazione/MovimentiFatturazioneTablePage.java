package it.eurotn.panjea.magazzino.rich.editors.fatturazione.consultazione;

import java.awt.BorderLayout;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.apache.commons.lang3.StringUtils;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

import it.eurotn.panjea.magazzino.domain.DatiGenerazione;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.util.MovimentoFatturazioneDTO;
import it.eurotn.panjea.rich.commands.StampaRVCommand;
import it.eurotn.panjea.rich.commands.StampaRVCommandRaggruppato;
import it.eurotn.panjea.rich.editors.documento.StampaAreeDocumentoCommand;
import it.eurotn.panjea.rich.editors.documentograph.OpenDocumentoGraphEditorCommand;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.report.ReportManager;

/**
 * Visualizza tutti i movimenti generati da una fatturazione.
 *
 * @author fattazzo
 */
public class MovimentiFatturazioneTablePage extends AbstractTablePageEditor<MovimentoFatturazioneDTO>
        implements Observer {

    private class AnteprimaStampaCommand extends ActionCommand {

        public static final String COMMAND_ID = PAGE_ID + ".anteprimaStampaCommand";

        /**
         * Costruttore.
         */
        public AnteprimaStampaCommand() {
            super(COMMAND_ID);
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            MovimentiFatturazioneTablePage.this
                    .firePropertyChange(MovimentiFatturazioneTablePage.ANTEPRIMA_DOCUMENTO_PROPERTY, null, "change");
        }
    }

    private class ConfermaMovimentiActionCommandInterceptor extends ActionCommandInterceptorAdapter {

        private ConfermaMovimentiDialog dialog;

        @Override
        public void postExecution(ActionCommand command) {
            super.postExecution(command);

            MovimentiFatturazioneTablePage.this.firePropertyChange(MOVIMENTI_IN_FATTURAZIONE, true, false);

            DatiGenerazione dati = ((ConfermaMovimentiCommand) command).getDatiGenerazione();
            if (dati != null) {
                LifecycleApplicationEvent event = new OpenEditorEvent(dati);
                Application.instance().getApplicationContext().publishEvent(event);
            }

            progressConfermaPanel.removeAll();

            dialog.setCloseEnable(true);
            dialog.onCancel();
        }

        @Override
        public boolean preExecution(ActionCommand command) {

            command.addParameter(ConfermaMovimentiCommand.PARAM_MAGAZZINO_DOCUMENTO_BD, magazzinoDocumentoBD);
            command.addParameter(ConfermaMovimentiCommand.PARAM_UTENTE, datiGenerazione.getUtente());

            dialog = new ConfermaMovimentiDialog();
            progressConfermaPanel.add(dialog.createDialogContentPane(), BorderLayout.CENTER);

            MovimentiFatturazioneTablePage.this.firePropertyChange(MOVIMENTI_IN_FATTURAZIONE, false, true);

            return true;
        }

    }

    private class DocumentoGraphInterceptor extends ActionCommandInterceptorAdapter {

        @Override
        public boolean preExecution(ActionCommand command) {

            MovimentoFatturazioneDTO movimentoSelezionato = getTable().getSelectedObject();

            if (movimentoSelezionato != null) {
                command.addParameter(OpenDocumentoGraphEditorCommand.PARAM_ID_DOCUMENTO,
                        movimentoSelezionato.getAreaMagazzino().getDocumento().getId());
            }

            return movimentoSelezionato != null;
        }
    }

    private class OpenDocumentoCommand extends ActionCommand {

        @Override
        protected void doExecuteCommand() {

            MovimentoFatturazioneDTO areaSelezionata = getTable().getSelectedObject();

            // apro il documento sono se ce n'è uno selezionato e se non è
            // temporaneo
            if (areaSelezionata != null && datiGenerazione != null && datiGenerazione.getDataCreazione() != null) {
                LifecycleApplicationEvent event = new OpenEditorEvent(areaSelezionata.getAreaMagazzino());
                Application.instance().getApplicationContext().publishEvent(event);
            }
        }

    }

    private class SpedizioneMovimentiCommandInterceptor extends ActionCommandInterceptorAdapter {

        @Override
        public void postExecution(ActionCommand command) {
            super.postExecution(command);
        }

        @Override
        public boolean preExecution(ActionCommand command) {
            ((SpedizioneDocumentiFatturazioneCommand) command).setMovimentiDaSpedire(getTable().getSelectedObjects());
            return super.preExecution(command);
        }
    }

    private class StampaAreeDocumentoActionCommandInterceptor implements ActionCommandInterceptor {
        @Override
        public void postExecution(ActionCommand command) {
        }

        @Override
        public boolean preExecution(ActionCommand command) {
            List<AreaMagazzino> list = new ArrayList<AreaMagazzino>();

            for (MovimentoFatturazioneDTO areaMagazzinoDTO : getTable().getSelectedObjects()) {
                list.add(areaMagazzinoDTO.getAreaMagazzino());
            }
            stampaAreeDocumentoCommand.addParameter(StampaAreeDocumentoCommand.PARAM_AREE_DA_STAMPARE, list);
            stampaAreeDocumentoCommand.addParameter(StampaAreeDocumentoCommand.PARAM_FORZA_STAMPA, Boolean.TRUE);
            return true;
        }
    }

    private class StampaRVActionCommandInterceptor extends ActionCommandInterceptorAdapter {
        @Override
        public boolean preExecution(ActionCommand command) {
            if (getTable().getRows().size() == getTable().getSelectedObjects().size()) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                command.addParameter(StampaRVCommand.PARAM_DATI_GENERAZIONE,
                        dateFormat.format(datiGenerazione.getDataCreazione()));
            } else {
                StringBuilder sb = new StringBuilder(200);
                for (MovimentoFatturazioneDTO movimentoFatturazioneDTO : getTable().getSelectedObjects()) {
                    sb.append(movimentoFatturazioneDTO.getAreaMagazzino().getId());
                    sb.append(",");

                    String idAree = StringUtils.chop(sb.toString());
                    command.addParameter(StampaRVCommand.PARAM_AREE_MAGAZZINO, idAree);
                }
            }
            return true;
        }
    }

    public static final String PAGE_ID = "movimentiFatturazioneTablePage";
    public static final String MOVIMENTI_IN_FATTURAZIONE = "movimentiInFatturazione";

    public static final String ANTEPRIMA_DOCUMENTO_PROPERTY = "previewProperty";

    private IMagazzinoDocumentoBD magazzinoDocumentoBD = null;
    private AnteprimaStampaCommand anteprimaStampaCommand = null;
    private ActionCommand stampaRVCommand = null;
    private ActionCommand stampaRVCommandRaggruppato = null;
    private StampaAreeDocumentoCommand stampaAreeDocumentoCommand = null;
    private ConfermaMovimentiCommand confermaMovimentiCommand = null;
    private OpenDocumentoGraphEditorCommand openDocumentoGraphEditorCommand = null;
    private SpedizioneDocumentiFatturazioneCommand spedizioneDocumentiCommand = null;

    private ConfermaMovimentiActionCommandInterceptor confermaMovimentiActionCommandInterceptor = null;
    private StampaRVActionCommandInterceptor stampaRVActionCommandInterceptor = null;
    private StampaAreeDocumentoActionCommandInterceptor stampaAreeMagazzinoActionCommandInterceptor = null;
    private SpedizioneMovimentiCommandInterceptor spedizioneMovimentiCommandInterceptor = null;

    private ReportManager reportManager = null;
    private DatiGenerazione datiGenerazione = null;

    private JPanel progressConfermaPanel = null;

    /**
     * Costruttore.
     */
    protected MovimentiFatturazioneTablePage() {
        super(PAGE_ID, new MovimentiFatturazioneTableModel());
        getTable().setDelayForSelection(500);
        getTable().setPropertyCommandExecutor(new OpenDocumentoCommand());

        new StatoSpedizioneMovimentiController(this);
    }

    @Override
    public void dispose() {
        if (confermaMovimentiCommand != null) {
            confermaMovimentiCommand.removeCommandInterceptor(getConfermaMovimentiActionCommandInterceptor());
        }
        if (stampaAreeDocumentoCommand != null) {
            stampaAreeDocumentoCommand.removeCommandInterceptor(getStampaAreeMagazzinoActionCommandInterceptor());
        }
        if (stampaRVCommand != null) {
            stampaRVCommand.removeCommandInterceptor(stampaRVActionCommandInterceptor);
        }
        if (stampaRVCommandRaggruppato != null) {
            stampaRVCommandRaggruppato.removeCommandInterceptor(stampaRVActionCommandInterceptor);
        }
        if (spedizioneDocumentiCommand != null) {
            spedizioneDocumentiCommand.removeCommandInterceptor(spedizioneMovimentiCommandInterceptor);
        }
        stampaRVCommandRaggruppato = null;
        stampaRVCommand = null;
        stampaAreeMagazzinoActionCommandInterceptor = null;
        confermaMovimentiActionCommandInterceptor = null;
        spedizioneDocumentiCommand = null;
        spedizioneMovimentiCommandInterceptor = null;
        super.dispose();
    }

    /**
     * @return the anteprimaStampaCommand
     */
    public AnteprimaStampaCommand getAnteprimaStampaCommand() {
        if (anteprimaStampaCommand == null) {
            anteprimaStampaCommand = new AnteprimaStampaCommand();
        }
        return anteprimaStampaCommand;
    }

    @Override
    public AbstractCommand[] getCommands() {
        return new AbstractCommand[] { getSpedizioneDocumentiCommand(), getStampaRVRaggruppateCommand(),
                getStampaRVCommand(), getConfermaMovimentiCommand(), getStampaAreeDocumentoCommand(),
                getAnteprimaStampaCommand(), getRefreshCommand(), getOpenDocumentoGraphEditorCommand() };
    }

    /**
     * @return confermaMovimentiActionCommandInterceptor
     */
    private ConfermaMovimentiActionCommandInterceptor getConfermaMovimentiActionCommandInterceptor() {
        if (confermaMovimentiActionCommandInterceptor == null) {
            confermaMovimentiActionCommandInterceptor = new ConfermaMovimentiActionCommandInterceptor();
        }
        return confermaMovimentiActionCommandInterceptor;
    }

    /**
     * @return the confermaMovimentiCommand
     */
    public ConfermaMovimentiCommand getConfermaMovimentiCommand() {
        if (confermaMovimentiCommand == null) {
            confermaMovimentiCommand = new ConfermaMovimentiCommand();
            confermaMovimentiCommand.addCommandInterceptor(getConfermaMovimentiActionCommandInterceptor());
            confermaMovimentiCommand.setVisible(false);
        }
        return confermaMovimentiCommand;
    }

    @Override
    public JComponent getFooterControl() {
        progressConfermaPanel = new JPanel(new BorderLayout());
        return progressConfermaPanel;
    }

    /**
     * @return the magazzinoDocumentoBD
     */
    public IMagazzinoDocumentoBD getMagazzinoDocumentoBD() {
        return magazzinoDocumentoBD;
    }

    /**
     * @return Returns the openDocumentoGraphEditorCommand.
     */
    private OpenDocumentoGraphEditorCommand getOpenDocumentoGraphEditorCommand() {
        if (openDocumentoGraphEditorCommand == null) {
            openDocumentoGraphEditorCommand = new OpenDocumentoGraphEditorCommand();
            openDocumentoGraphEditorCommand.addCommandInterceptor(new DocumentoGraphInterceptor());
        }

        return openDocumentoGraphEditorCommand;
    }

    /**
     * @return the spedizioneDocumentiCommand
     */
    private SpedizioneDocumentiFatturazioneCommand getSpedizioneDocumentiCommand() {
        if (spedizioneDocumentiCommand == null) {
            spedizioneDocumentiCommand = new SpedizioneDocumentiFatturazioneCommand();
            spedizioneMovimentiCommandInterceptor = new SpedizioneMovimentiCommandInterceptor();
            spedizioneDocumentiCommand.addCommandInterceptor(spedizioneMovimentiCommandInterceptor);
        }

        return spedizioneDocumentiCommand;
    }

    /**
     * @return stampaAreeMagazzinoCommand
     */
    public StampaAreeDocumentoCommand getStampaAreeDocumentoCommand() {
        if (stampaAreeDocumentoCommand == null) {
            stampaAreeDocumentoCommand = new StampaAreeDocumentoCommand(reportManager);
            stampaAreeDocumentoCommand.addCommandInterceptor(getStampaAreeMagazzinoActionCommandInterceptor());
        }

        return stampaAreeDocumentoCommand;
    }

    /**
     * @return stampaAreeMagazzinoActionCommandInterceptor
     */
    private StampaAreeDocumentoActionCommandInterceptor getStampaAreeMagazzinoActionCommandInterceptor() {
        if (stampaAreeMagazzinoActionCommandInterceptor == null) {
            stampaAreeMagazzinoActionCommandInterceptor = new StampaAreeDocumentoActionCommandInterceptor();
        }
        return stampaAreeMagazzinoActionCommandInterceptor;
    }

    /**
     * @return the stampaRVCommand
     */
    public ActionCommand getStampaRVCommand() {
        if (stampaRVCommand == null) {
            stampaRVActionCommandInterceptor = new StampaRVActionCommandInterceptor();
            stampaRVCommand = new StampaRVCommand();
            stampaRVCommand.addCommandInterceptor(stampaRVActionCommandInterceptor);
        }
        return stampaRVCommand;
    }

    /**
     * @return the stampaRVCommand
     */
    public ActionCommand getStampaRVRaggruppateCommand() {
        if (stampaRVCommandRaggruppato == null) {
            stampaRVActionCommandInterceptor = new StampaRVActionCommandInterceptor();
            stampaRVCommandRaggruppato = new StampaRVCommandRaggruppato();
            stampaRVCommandRaggruppato.addCommandInterceptor(stampaRVActionCommandInterceptor);
        }
        return stampaRVCommandRaggruppato;
    }

    @Override
    public Collection<MovimentoFatturazioneDTO> loadTableData() {
        List<MovimentoFatturazioneDTO> rows = new ArrayList<MovimentoFatturazioneDTO>();

        if (datiGenerazione != null) {
            rows = magazzinoDocumentoBD.caricaMovimentPerFatturazione(datiGenerazione.getDataCreazione(),
                    datiGenerazione.getUtente());
        }

        return rows;
    }

    @Override
    public void onPostPageOpen() {
    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public void processTableData(Collection<MovimentoFatturazioneDTO> results) {
        super.processTableData(results);

        getConfermaMovimentiCommand().setVisible(datiGenerazione != null && datiGenerazione.getDataCreazione() == null);
        getStampaRVCommand().setVisible(datiGenerazione != null && datiGenerazione.getDataCreazione() != null);
        getSpedizioneDocumentiCommand()
                .setVisible(datiGenerazione != null && datiGenerazione.getDataCreazione() != null);
    }

    @Override
    public Collection<MovimentoFatturazioneDTO> refreshTableData() {
        return loadTableData();
    }

    @Override
    public void setFormObject(Object object) {
    }

    /**
     * @param magazzinoDocumentoBD
     *            the magazzinoDocumentoBD to set
     */
    public void setMagazzinoDocumentoBD(IMagazzinoDocumentoBD magazzinoDocumentoBD) {
        this.magazzinoDocumentoBD = magazzinoDocumentoBD;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);

        getStampaRVCommand().setEnabled(!readOnly);
        getConfermaMovimentiCommand().setEnabled(!readOnly);
        getStampaAreeDocumentoCommand().setEnabled(!readOnly);
        getAnteprimaStampaCommand().setEnabled(!readOnly);
        getRefreshCommand().setEnabled(!readOnly);
        getSpedizioneDocumentiCommand().setEnabled(!readOnly);

        getTable().getTable().setEnabled(!readOnly);
    }

    /**
     * @param reportManager
     *            the reportManager to set
     */
    public void setReportManager(ReportManager reportManager) {
        this.reportManager = reportManager;
    }

    @Override
    public void update(Observable observable, Object obj) {
        super.update(observable, obj);

        if (obj == null || obj instanceof DatiGenerazione) {
            this.datiGenerazione = (DatiGenerazione) obj;
            // uso la refreshData perchè se il caricamento è in corso e la richiamo viene fermato il task e fatto
            // partire quello nuovo
            refreshData();
        }
    }

}
