package it.eurotn.panjea.contabilita.rich.editors;

import java.awt.FlowLayout;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.list.ListMultipleSelectionGuard;
import org.springframework.richclient.list.ListSelectionValueModelAdapter;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.contabilita.domain.EstrattoConto;
import it.eurotn.panjea.contabilita.domain.RigaContabileEstrattoConto;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaBD;
import it.eurotn.panjea.contabilita.util.AreaContabileFullDTO;
import it.eurotn.panjea.contabilita.util.ParametriRicercaEstrattoConto;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.editors.AbstractTablePageEditor;

public class RisultatiRicercaEstrattoContoTablePage extends AbstractTablePageEditor<RigaContabileEstrattoConto> {

    private class OpenAreaContabileEditorCommand extends ApplicationWindowAwareCommand {

        private static final String COMMAND_ID = "openAreaContabileCommand";

        /**
         * Costruttore.
         */
        public OpenAreaContabileEditorCommand() {
            super(COMMAND_ID);
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            RisultatiRicercaEstrattoContoTablePage.this.openAreaContabileEditor();
        }
    }

    private class StampaEstrattoContoCommand extends it.eurotn.rich.report.StampaCommand {

        /**
         * Costruttore.
         */
        public StampaEstrattoContoCommand() {
            super("stampaEstrattoContoCommand");
        }

        @Override
        protected Map<Object, Object> getParametri() {

            HashMap<Object, Object> parametri = new HashMap<Object, Object>();
            parametri.put("descAzienda", aziendaCorrente.getDenominazione());
            parametri.put("utente", PanjeaSwingUtil.getUtenteCorrente().getUserName());
            // anno
            parametri.put("anno", parametriRicercaEstrattoConto.getAnnoCompetenza());
            // periodo
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            parametri.put("dataInizio", null);
            parametri.put("dataFine", null);
            if (parametriRicercaEstrattoConto.getDataRegistrazione().getDataIniziale() != null) {
                parametri.put("dataInizio",
                        dateFormat.format(parametriRicercaEstrattoConto.getDataRegistrazione().getDataIniziale()));
            }
            if (parametriRicercaEstrattoConto.getDataRegistrazione().getDataFinale() != null) {
                parametri.put("dataFine",
                        dateFormat.format(parametriRicercaEstrattoConto.getDataRegistrazione().getDataFinale()));
            }
            // sottoconto
            if (parametriRicercaEstrattoConto.getSottoConto() != null) {
                parametri.put("idSottoConto", parametriRicercaEstrattoConto.getSottoConto().getId());
            }
            // centro di costo
            if (parametriRicercaEstrattoConto.getCentroCosto() != null) {
                parametri.put("idCentroCosto", parametriRicercaEstrattoConto.getCentroCosto().getId());
            }
            // stati area
            parametri.put("statiArea", parametriRicercaEstrattoConto.getStatiAreaContabileForQuery());

            // tipi documento
            parametri.put("htmlParameters", parametriRicercaEstrattoConto.getHtmlParameters());
            if (parametriRicercaEstrattoConto.getTipiDocumento() != null
                    && parametriRicercaEstrattoConto.getTipiDocumento().size() > 0) {
                Set<TipoDocumento> tipiDocumento = parametriRicercaEstrattoConto.getTipiDocumento();
                List<Integer> idTipiDocumento = new ArrayList<Integer>();
                for (TipoDocumento tipoDocumento : tipiDocumento) {
                    idTipiDocumento.add(tipoDocumento.getId());
                }
                parametri.put("tipiDocumento", idTipiDocumento);
            }
            return parametri;
        }

        @Override
        protected String getReportName() {
            return "Estratto conto";
        }

        @Override
        protected String getReportPath() {
            return "Contabilita/Anagrafica/estrattoConto";
        }
    }

    private static Logger logger = Logger.getLogger(RisultatiRicercaEstrattoContoTablePage.class);
    public static final String PAGE_ID = "risultatiRicercaEstrattoContoTablePage";

    private IContabilitaBD contabilitaBD = null;
    private OpenAreaContabileEditorCommand openAreaContabileEditorCommand = null;
    private ParametriRicercaEstrattoConto parametriRicercaEstrattoConto = null;
    private JTextField textFieldSaldoPrecedente = null;
    private JTextField textFieldSaldoFinale = null;
    private JLabel labelSaldoPrecedente = null;
    private AziendaCorrente aziendaCorrente = null;
    private EstrattoConto estrattoConto = null;

    // Adapter per la selezione della tabella
    private ListSelectionValueModelAdapter listSelectionAdapter;
    private ListMultipleSelectionGuard listMultipleSelectionGuard;

    private AbstractCommand stampaEstrattoContoConmmand;
    private JTextField textFieldSaldoPeriodo;

    /**
     * Costruttore.
     */
    public RisultatiRicercaEstrattoContoTablePage() {
        super(PAGE_ID, new RigaContabileEstrattoContoTableModel());
    }

    @Override
    public void dispose() {
        try {
            listSelectionAdapter.removeValueChangeListener(listMultipleSelectionGuard);
            getTable().getTable().getSelectionModel().removeListSelectionListener(listSelectionAdapter);
        } catch (Exception e) {
            logger.error("-->errore nella dispose.NV", e);
        }
        super.dispose();
    }

    @Override
    public AbstractCommand[] getCommands() {
        return new AbstractCommand[] { getOpenAreaContabileEditorCommand(), getStampaEstrattoContoConmmand() };
    }

    @Override
    public JComponent getHeaderControl() {

        textFieldSaldoPrecedente = getComponentFactory().createTextField();
        textFieldSaldoPrecedente.setColumns(10);
        textFieldSaldoPrecedente.setFocusable(false);

        textFieldSaldoFinale = getComponentFactory().createTextField();
        textFieldSaldoFinale.setColumns(10);
        textFieldSaldoFinale.setFocusable(false);

        textFieldSaldoPeriodo = getComponentFactory().createTextField();
        textFieldSaldoPeriodo.setColumns(10);
        textFieldSaldoPeriodo.setFocusable(false);

        JComponent pannelloSuperiore = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        labelSaldoPrecedente = getComponentFactory().createLabel(getMessage("saldoPrecedente"));
        pannelloSuperiore.add(labelSaldoPrecedente);
        pannelloSuperiore.add(textFieldSaldoPrecedente);

        JLabel labelSaldoFinale = getComponentFactory().createLabel(getMessage("saldoFinale"));
        pannelloSuperiore.add(labelSaldoFinale);
        pannelloSuperiore.add(textFieldSaldoFinale);

        JLabel labelSaldoPeriodo = getComponentFactory().createLabel(getMessage("saldoPeriodo"));
        pannelloSuperiore.add(labelSaldoPeriodo);
        pannelloSuperiore.add(textFieldSaldoPeriodo);

        return pannelloSuperiore;
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
     * @return command
     */
    private OpenAreaContabileEditorCommand getOpenAreaContabileEditorCommand() {
        if (openAreaContabileEditorCommand == null) {
            openAreaContabileEditorCommand = new OpenAreaContabileEditorCommand();
            listMultipleSelectionGuard = new ListMultipleSelectionGuard(getListSelectionAdapter(),
                    openAreaContabileEditorCommand);
            getTable().setPropertyCommandExecutor(openAreaContabileEditorCommand);
        }
        return openAreaContabileEditorCommand;
    }

    /**
     * @return the stampaEstrattoContoConmmand
     */
    public AbstractCommand getStampaEstrattoContoConmmand() {
        if (stampaEstrattoContoConmmand == null) {
            stampaEstrattoContoConmmand = new StampaEstrattoContoCommand();
        }

        return stampaEstrattoContoConmmand;
    }

    @Override
    public Collection<RigaContabileEstrattoConto> loadTableData() {

        List<RigaContabileEstrattoConto> righeContabiliEstrattoConto = Collections.emptyList();

        if (this.parametriRicercaEstrattoConto.isEffettuaRicerca()) {
            estrattoConto = contabilitaBD.caricaEstrattoConto(parametriRicercaEstrattoConto);
            RigaContabileEstrattoContoTableModel tableModel = (RigaContabileEstrattoContoTableModel) TableModelWrapperUtils
                    .getActualTableModel(getTable().getTable().getModel());
            tableModel.setSottoconto(parametriRicercaEstrattoConto.getSottoConto());

            righeContabiliEstrattoConto = estrattoConto.getRigheEstrattoConto();
        }

        return righeContabiliEstrattoConto;
    }

    @Override
    public void onPostPageOpen() {
    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public void onRefresh() {
    }

    /**
     * Richiede l'apertura dell'editor dell'areaContabile se ho selezionato un oggetto dalla
     * tabella.
     */
    private void openAreaContabileEditor() {
        if (getTable().getSelectedObject() != null) {

            AreaContabileFullDTO areaContabileFullDTO = contabilitaBD
                    .caricaAreaContabileFullDTO((getTable().getSelectedObject()).getIdAreaContabile());
            logger.debug("--> Apro l'area contabile con id " + areaContabileFullDTO.getId());
            LifecycleApplicationEvent event = new OpenEditorEvent(areaContabileFullDTO);
            Application.instance().getApplicationContext().publishEvent(event);
        }
    }

    @Override
    public void processTableData(Collection<RigaContabileEstrattoConto> results) {
        textFieldSaldoPrecedente.setText("");
        textFieldSaldoFinale.setText("");
        textFieldSaldoPeriodo.setText("");

        super.processTableData(results);

        // npe mail
        Date dataIniziale = this.parametriRicercaEstrattoConto.getDataRegistrazione().getDataIniziale();
        if (dataIniziale != null && dataIniziale.equals(aziendaCorrente.getDataInizioEsercizio())) {
            labelSaldoPrecedente.setText(getMessage("saldoIniziale"));
        } else {
            labelSaldoPrecedente.setText(getMessage("saldoPrecedente"));
        }

        if (estrattoConto != null) {
            // aggiorno il valore del saldo precedente e del saldo finale
            NumberFormat numberFormat = new DecimalFormat("#,###,###,##0.00;-#,###,###,##0.00");
            textFieldSaldoPrecedente.setText(numberFormat.format(estrattoConto.getSaldoPrecedente()));
            textFieldSaldoFinale.setText(numberFormat.format(estrattoConto.getSaldoFinale()));
            textFieldSaldoPeriodo.setText(numberFormat.format(estrattoConto.getSaldoPeriodo()));
        }

        getStampaEstrattoContoConmmand()
                .setEnabled(this.parametriRicercaEstrattoConto.isEffettuaRicerca() && !results.isEmpty());
    }

    /*
     * Questo metodo viene chiamato dalla composite compresa nell'editor quando la page
     * ParametriRicercaEstrattoContoPage notifica il cambio del l'oggetto via firePropertyChange
     * sulla proprieta' IPageLifecycleAdvisor.OBJECT_CHANGED settando la pagina a dirty. arriva
     * cosi' il nuovo ParametriRicercaEstrattoConto via setFormObject ed eseguo quindi la ricerca,
     * se null sono nel caso di una nuova ricerca quindi ripulisco i risultati
     */
    @Override
    public Collection<RigaContabileEstrattoConto> refreshTableData() {
        return loadTableData();
    }

    /**
     * @param aziendaCorrente
     *            the aziendaCorrente to set
     */
    public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
        this.aziendaCorrente = aziendaCorrente;
    }

    /**
     * @param contabilitaBD
     *            the contabilitaBD to set
     */
    public void setContabilitaBD(IContabilitaBD contabilitaBD) {
        this.contabilitaBD = contabilitaBD;
    }

    @Override
    public void setFormObject(Object object) {
        if (object instanceof ParametriRicercaEstrattoConto) {
            this.parametriRicercaEstrattoConto = (ParametriRicercaEstrattoConto) object;
        } else {
            this.parametriRicercaEstrattoConto = new ParametriRicercaEstrattoConto();
        }
    }
}
