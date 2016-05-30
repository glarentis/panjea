package it.eurotn.rich.report;

import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.exceptions.PanjeaRuntimeException;
import it.eurotn.rich.dialog.MessageAlert;
import it.eurotn.rich.report.exception.JecReportErrorsException;
import it.eurotn.rich.report.exception.JecReportException;
import it.eurotn.rich.report.result.JecReportErrors;
import it.eurotn.rich.report.result.JecReportResult;

import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.swing.SwingUtilities;

import net.sf.jasperreports.export.SimplePrintServiceExporterConfiguration;

import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Message;
import org.springframework.richclient.core.Severity;

/**
 *
 * Classe di utilità per i reports.
 *
 * @author giangi
 * @version 1.0, 15/set/2014
 *
 */
public final class JecReports {

    /**
     * Genera più report.
     *
     * @param reports
     *            reports da generare
     * @return lista di reports generati ordinati (nell'ordine come ricevuti)
     */
    public static JecReportResult generaReports(List<JecReport> reports) {

        ExecutorService executor = Executors.newFixedThreadPool(1);
        List<Future<JecReport>> reportsRunning = new ArrayList<Future<JecReport>>();
        int order = 0;
        for (JecReport report : reports) {
            report.setOrdineStampa(order++);
            Future<JecReport> reportFuture = executor.submit(report);
            reportsRunning.add(reportFuture);
        }

        JecReportResult result = new JecReportResult();
        for (Future<JecReport> reportRunning : reportsRunning) {
            try {
                result.addReportGenerato(reportRunning.get());
            } catch (InterruptedException | ExecutionException e) {

                if (e.getCause().getCause() instanceof JecReportException) {
                    result.addReportException((JecReportException) e.getCause().getCause());
                }
            }
        }
        executor.shutdown();

        return result;
    }

    /**
     * Stampa o mette in preview i report in base alle impostazioni del layout. Viene creato un unico report per ogni
     * LayoutDiStampa e mandato in stampa ongi x pagine per non creare file troppo grandi.
     *
     * @param reportsDaElaborare
     *            lista di report da elaborare
     * @param forcePrint
     *            forza la stampa senza passare da un eventuale preview
     * @param showPrintDialog
     *            true se voglio visualizzare il dialogo di stampa, in questo case i report vengono stampati
     *            direttamente sulla stampante sel.
     * @param throwLayoutMancantiException
     *            indica se in caso di layout mancanti l'eccezione venga gestita dal metodo o rilanciata
     */
    public static void stampaReports(final List<JecReport> reportsDaElaborare, boolean forcePrint,
            boolean showPrintDialog, boolean throwLayoutMancantiException) {
        // Raggruppo i report per le proprietà dei layout di stampa.
        Map<String, List<JecReport>> reportRaggruppati = new HashMap<String, List<JecReport>>();
        Set<JecReport> layoutMancanti = new HashSet<JecReport>();

        SimplePrintServiceExporterConfiguration configurazione = null;

        for (JecReport jecReport : reportsDaElaborare) {
            jecReport.caricaLayoutStampa();
            if (jecReport.getLayoutStampa() == null) {
                layoutMancanti.add(jecReport);
                continue;
            }
            if (showPrintDialog) {
                // PrintService service = PrintServiceLookup.lookupDefaultPrintService();
                jecReport.getLayoutStampa().setStampante("");
                jecReport.getLayoutStampa().setPreview(false);
                if (configurazione == null) {
                    configurazione = new SimplePrintServiceExporterConfiguration();
                    PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
                    printRequestAttributeSet.add(new Copies(1));
                    printRequestAttributeSet.add(new JobName("Panjea-Stampa Batch ", null));
                    PrinterJob job = PrinterJob.getPrinterJob();
                    if (!job.printDialog(printRequestAttributeSet)) {
                        return;
                    }
                    // per un bug del printDialog i margini impostati non vengono sempre rispettati quindi li rimuovo
                    // dai parametri utilizzando in questo modo quelli di default della stampante
                    printRequestAttributeSet.remove(MediaPrintableArea.class);

                    configurazione.setPrintRequestAttributeSet(printRequestAttributeSet);
                    configurazione.setDisplayPageDialogOnlyOnce(false);
                    configurazione.setDisplayPageDialog(false);
                    configurazione.setPrintService(job.getPrintService());
                }
            }

            List<JecReport> reportsLayoutUguale = reportRaggruppati
                    .get(jecReport.getLayoutStampa().getChiaveCaratteristicheStampa());
            if (reportsLayoutUguale == null) {
                reportsLayoutUguale = new ArrayList<JecReport>();
                reportRaggruppati.put(jecReport.getLayoutStampa().getChiaveCaratteristicheStampa(),
                        reportsLayoutUguale);
            }
            reportsLayoutUguale.add(jecReport);
        }

        final JecReportErrors jecReportErrors = new JecReportErrors();

        // Elaboro i report. L'ordinamento
        // non altera l'ordine con i quali vengono stampati i report perchè li metto in una lista.
        for (Entry<String, List<JecReport>> entryReportDaElaborare : reportRaggruppati.entrySet()) {
            List<JecReport> reportsBatchDaElaborare = entryReportDaElaborare.getValue();
            int index = 0;
            int start = index * BATCH_SIZE;
            int end = Math.min(start + BATCH_SIZE, reportsBatchDaElaborare.size());
            // se vado in preview li stampo tutti assieme
            if (reportsBatchDaElaborare.get(0).getLayoutStampa().getPreview() && !forcePrint) {
                end = reportsBatchDaElaborare.size();
            }
            do {
                JecReportErrors erroriStampa = stampaReportsPerCaratteristicheStampa(
                        reportsBatchDaElaborare.subList(start, end), forcePrint, configurazione);
                jecReportErrors.addErrors(erroriStampa);
                index++;
                start = index * BATCH_SIZE;
                end = Math.min(start + BATCH_SIZE, reportsBatchDaElaborare.size());

                if (reportsBatchDaElaborare.get(0).getLayoutStampa().getPreview() && !forcePrint) {
                    // sono in preview quindi li ho già stampati tutti e faccio in modo di uscire
                    start = reportsBatchDaElaborare.size();
                }
            } while (start < reportsBatchDaElaborare.size());
        }

        if (!layoutMancanti.isEmpty()) {
            StringBuilder sb = new StringBuilder(500);
            sb.append("Mancano delle configurazione per i layout dei seguenti documenti.\n");
            for (JecReport jecReport : layoutMancanti) {
                sb.append(jecReport.getReportName());
                sb.append("\n");
            }

            if (throwLayoutMancantiException) {
                throw new GenericException(sb.toString());
            } else {
                Message message = new DefaultMessage(sb.toString(), Severity.WARNING);
                MessageAlert alert = new MessageAlert(message);
                alert.showAlert();
            }
        }

        if (jecReportErrors.hasErrors()) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    throw new PanjeaRuntimeException(new JecReportErrorsException(jecReportErrors));
                }
            });
        }
    }

    private static JecReportErrors stampaReportsPerCaratteristicheStampa(final List<JecReport> reportsDaElaborare,
            boolean forcePrint, SimplePrintServiceExporterConfiguration configurazione) {
        JecReportResult reportGenerati = generaReports(reportsDaElaborare);

        if (reportGenerati.getReportGenerati() != null && !reportGenerati.getReportGenerati().isEmpty()) {
            LinkedList<JecReport> reportGeneratiCoda = new LinkedList<JecReport>(reportGenerati.getReportGenerati());

            JecReport report = reportGeneratiCoda.poll();
            JecReport reportSecondario = reportGeneratiCoda.poll();
            while (reportSecondario != null) {
                report.getJrPrint().getPages().addAll(reportSecondario.getJrPrint().getPages());
                report.setForceUseReportName(true);
                // visto che viene raggruppato per stampante, per più documenti, imposto come report name il nome della
                // stampante invece del layout di stampa
                if (report.getLayoutStampa().getStampante() != null) {
                    report.setReportName(report.getLayoutStampa().getStampante());
                }
                reportSecondario = reportGeneratiCoda.poll();
            }
            report.getLayoutStampa().setConfermaNumeroCopie(false);
            if (forcePrint) {
                report.getLayoutStampa().setPreview(false);
            }
            if (configurazione != null) {
                JasperPrintExporter exp = new JasperPrintExporter();

                if (report.getLayoutStampa().getSoloTesto()) {
                    exp.printText(report, configurazione.getPrintRequestAttributeSet(),
                            configurazione.getPrintService());
                } else {
                    exp.print(report, configurazione);
                }

            } else {
                report.postExecute(null).call(null);
            }
        }

        return reportGenerati.getReportErrors();
    }

    private static final int BATCH_SIZE = 20;

    private JecReports() {
    }
}
