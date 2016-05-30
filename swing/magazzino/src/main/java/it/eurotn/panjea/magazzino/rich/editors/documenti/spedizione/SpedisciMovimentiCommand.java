package it.eurotn.panjea.magazzino.rich.editors.documenti.spedizione;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingWorker;

import org.apache.log4j.Logger;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.TableModelWrapperUtils;

import it.eurotn.panjea.anagrafica.domain.SedeAnagrafica.TipoSpedizioneDocumenti;
import it.eurotn.panjea.documenti.util.MovimentoSpedizioneDTO;
import it.eurotn.panjea.magazzino.domain.TemplateSpedizioneMovimenti;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.editors.documenti.spedizione.runnable.SendMailFactory;
import it.eurotn.panjea.magazzino.rich.editors.documenti.spedizione.runnable.StampaRunnable;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.eurotn.rich.control.table.JideTableWidget;

public class SpedisciMovimentiCommand extends ActionCommand {
    private static final Logger LOGGER = Logger.getLogger(SpedisciMovimentiCommand.class);

    public static final String PARAM_TIPO_LAYOUT = "tipoLayout";

    private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

    private JideTableWidget<MovimentoSpedizioneDTO> table;

    private TemplateSpedizioneMovimenti templateSpedizione;

    private ScheduledExecutorService executor = null;

    /**
     * Costruttore.
     *
     * @param jideTableWidget
     *            table widget dei movimenti
     */
    public SpedisciMovimentiCommand(final JideTableWidget<MovimentoSpedizioneDTO> jideTableWidget) {
        super("spedisciMovimentiCommand");
        RcpSupport.configure(this);
        this.magazzinoAnagraficaBD = RcpSupport.getBean(MagazzinoAnagraficaBD.BEAN_ID);
        this.table = jideTableWidget;
    }

    private void clearEsitiSpedizione() {
        @SuppressWarnings("unchecked")
        DefaultBeanTableModel<MovimentoSpedizioneDTO> movimentiTableModel = (DefaultBeanTableModel<MovimentoSpedizioneDTO>) TableModelWrapperUtils
                .getActualTableModel(getTable().getTable().getModel());
        for (int i = 0; i < movimentiTableModel.getRowCount(); i++) {
            movimentiTableModel.getObject(i).setEsitoSpedizione("");
        }
        movimentiTableModel.fireTableDataChanged();
    }

    @Override
    protected void doExecuteCommand() {

        final TipoLayout tipoLayout = (TipoLayout) getParameter(PARAM_TIPO_LAYOUT);

        templateSpedizione = magazzinoAnagraficaBD.caricaTemplateSpedizioneMovimenti();

        // seleziono tutto perchè così la getSelectedObjects() mi restituisce i movimenti come
        // ordinati in griglia
        getTable().selectAll();
        final List<MovimentoSpedizioneDTO> movimentiDaSpedire = getTable().getSelectedObjects();
        getTable().unSelectAll();

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {

                table.stopCellEditor();

                if (!verificaDati()) {
                    return null;
                }

                // azzero tutti gli eventuali esisti precedenti dalla tabella
                clearEsitiSpedizione();

                setEnabled(false);

                executor = Executors.newSingleThreadScheduledExecutor();
                try {

                    for (MovimentoSpedizioneDTO movimento : movimentiDaSpedire) {
                        switch (tipoLayout) {
                        case INTERNO:
                            // se è interno stampo SEMPRE anche se è impostato il tipo spedizione mail
                            executor.execute(new StampaRunnable(movimento, getTable(), true));
                            break;
                        case CLIENTE:
                            // nel caso del layout cliente prendo il tipo spedizione impostato
                            executor.execute(SendMailFactory.getInstance(movimento.getTipoSpedizioneDocumenti(),
                                    movimento, getTable(), templateSpedizione, tipoLayout));
                            break;
                        case CLIENTE_E_INTERNO:
                            // in questo caso unisco le 2 casistiche precedenti creando la spedizione per il layout
                            // cliente e la
                            // stampa del layout interno
                            executor.execute(SendMailFactory.getInstance(movimento.getTipoSpedizioneDocumenti(),
                                    movimento, getTable(), templateSpedizione, tipoLayout));

                            executor.execute(new StampaRunnable(movimento, getTable(), true));
                            break;
                        default:
                            throw new IllegalArgumentException("Il layout selezionato non è attualmente gestito.");
                        }
                    }
                } catch (Exception e) {
                    LOGGER.error("--> errore durante la spedizione documenti: " + e);
                } finally {
                    executor.shutdown();
                    try {
                        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
                        setEnabled(true);
                    } catch (InterruptedException e) {
                        LOGGER.error("-->timeout durante la spedizione dei documenti ", e);
                    }
                }

                return null;
            }
        };
        worker.execute();

        // table.stopCellEditor();
        // TipoLayout tipoLayout = (TipoLayout) getParameter(PARAM_TIPO_LAYOUT);
        // this.templateSpedizione = magazzinoAnagraficaBD.caricaTemplateSpedizioneMovimenti();
        //
        // if (!verificaDati()) {
        // return;
        // }
        //
        // // seleziono tutto perchè così la getSelectedObjects() mi restituisce i movimenti come
        // // ordinati in griglia
        // getTable().selectAll();
        // List<MovimentoSpedizioneDTO> movimentiDaSpedire = getTable().getSelectedObjects();
        // getTable().unSelectAll();
        //
        // // azzero tutti gli eventuali esisti precedenti dalla tabella
        // clearEsitiSpedizione();
        //
        // setEnabled(false);
        //
        // executor = Executors.newSingleThreadScheduledExecutor();
        // try {
        //
        // for (MovimentoSpedizioneDTO movimento : movimentiDaSpedire) {
        // switch (tipoLayout) {
        // case INTERNO:
        // // se è interno stampo SEMPRE anche se è impostato il tipo spedizione mail
        // executor.execute(new StampaRunnable(movimento, getTable(), true));
        // break;
        // case CLIENTE:
        // // nel caso del layout cliente prendo il tipo spedizione impostato
        // executor.execute(SendMailFactory.getInstance(movimento.getTipoSpedizioneDocumenti(), movimento,
        // getTable(), templateSpedizione, tipoLayout));
        // break;
        // case CLIENTE_E_INTERNO:
        // // in questo caso unisco le 2 casistiche precedenti creando la spedizione per il layout cliente e la
        // // stampa del layout interno
        // executor.execute(SendMailFactory.getInstance(movimento.getTipoSpedizioneDocumenti(), movimento,
        // getTable(), templateSpedizione, tipoLayout));
        //
        // executor.execute(new StampaRunnable(movimento, getTable(), true));
        // break;
        // default:
        // throw new IllegalArgumentException("Il layout selezionato non è attualmente gestito.");
        // }
        // }
        // } catch (Exception e) {
        // LOGGER.error("--> errore durante la spedizione documenti: " + e);
        // } finally {
        // executor.shutdown();
        // try {
        // executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        // setEnabled(true);
        // } catch (InterruptedException e) {
        // LOGGER.error("-->timeout durante la spedizione dei documenti ", e);
        // }
        // }
    }

    /**
     * @return the table
     */
    private JideTableWidget<MovimentoSpedizioneDTO> getTable() {
        return table;
    }

    /**
     * annulla la spedizione in corso.
     */
    public void terminaSpedizione() {
        if (executor != null && !executor.isTerminated()) {
            executor.shutdownNow();
            setEnabled(true);
        }
    }

    /**
     * Verifica che siano configurati tutti i dati per la spedizione ( template, mail, ecc....).
     */
    private boolean verificaDati() {

        List<MovimentoSpedizioneDTO> movimentiDaSpedire = getTable().getVisibleObjects();
        for (MovimentoSpedizioneDTO movimento : movimentiDaSpedire) {
            if (movimento.getTipoSpedizioneDocumenti() == TipoSpedizioneDocumenti.EMAIL) {
                if (!templateSpedizione.isValid()) {
                    new MessageDialog("ATTENZIONE",
                            "Il template di spedizione dei documenti deve contene un oggetto e un testo.").showDialog();
                    return false;
                }

                if (!movimento.isDatiSpedizioneValidi()) {
                    new MessageDialog("ATTENZIONE",
                            "Alcuni documenti con spedizione di tipo Email non sono configurati correttamente.")
                                    .showDialog();
                    return false;
                }
            }

        }
        return true;
    }
}