package it.eurotn.rich.report;

import it.eurotn.rich.dialog.InputApplicationDialog;

import java.awt.Dimension;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.ServiceUI;
import javax.print.SimpleDoc;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.JobName;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRTextExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimplePrintServiceExporterConfiguration;
import net.sf.jasperreports.export.SimpleTextReportConfiguration;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import net.sf.jasperreports.export.WriterExporterOutput;

import org.apache.axis.utils.ByteArrayOutputStream;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.dialog.MessageDialog;

public class JasperPrintExporter {

    private class ConfermaNumeroCopieStampaDialog extends InputApplicationDialog {

        private boolean confirmed = true;

        /**
         * @return confirmed state of the input dialog
         */
        public boolean isConfirmed() {
            return confirmed;
        }

        @Override
        protected void onCancel() {
            confirmed = false;
            super.onCancel();
        }
    }

    /**
     * Recupera il PrintService.
     *
     * @param jecReport
     *            jecReport
     * @return PrintService
     */
    public static PrintService getPrintService(JecReport jecReport) {
        // imposto la stampante
        PrintService printService = null;
        String printerName = jecReport.getLayoutStampa().getStampante();
        if (printerName == null) {
            // stampante di default
            printService = PrintServiceLookup.lookupDefaultPrintService();
            // se non la trovo seleziono la prima.
        } else {
            // cerco la stampante con il nome impostato.
            PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
            DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
            PrintService[] printServices;
            try {
                printServices = PrintServiceLookup.lookupPrintServices(flavor, pras);
                for (PrintService printService2 : printServices) {
                    if (printService2.getName().equals(printerName)) {
                        printService = printService2;
                        break;
                    }
                }
            } catch (Exception e) {
                MessageDialog dialog = new MessageDialog("ATTENZIONE",
                        new DefaultMessage("Servizio di stampa non disponibile.", Severity.WARNING));
                dialog.showDialog();
                return null;
            }
        }
        return printService;
    }

    /**
     * Costruttore.
     */
    public JasperPrintExporter() {
        super();
    }

    /**
     * @param jecReport
     *            report da stampare
     */
    public void export(final JecReport jecReport) {
        if (jecReport.getJrPrint() == null) {
            return;
        }
        Integer numeroCopie = getNumeroCopie(jecReport);
        if (numeroCopie == null) {
            return;
        }

        // se i report aggiuntivi vanno solo sulla prima copia stampo la prima copia e dopo aver rimosso le pagine extra
        // stampo n-1 copie rimanenti
        if (jecReport.isReportsAggiuntiviPrimaCopia() && numeroCopie > 1) {
            launch(jecReport, 1);
            // dopo aver stampato la prima rimuovo le pagine aggiuntive dal report e lo stampo per le restanti volte
            for (JecReport jecReportAggiuntiviPrimaCopia : jecReport.getReportToAddOnFirstCopy()) {
                jecReport.getJrPrint().getPages().removeAll(jecReportAggiuntiviPrimaCopia.getJrPrint().getPages());
                jecReport.setShowPrintDialog(false);
            }
            numeroCopie = numeroCopie - 1;
        }
        if (numeroCopie > 0) {
            launch(jecReport, numeroCopie);
        }
    }

    /**
     * recupera il numero di copie dal JecReport o dal dialogo di conferma numero copie.
     *
     * @param jecReport
     *            jecReport
     * @return il numero di copie o null se annullo il dialogo di conferma numero copie
     */
    private Integer getNumeroCopie(JecReport jecReport) {
        Integer numeroCopie = jecReport.getNumeroCopie();
        if (numeroCopie == null) {
            numeroCopie = 1;
        }
        if (jecReport.getLayoutStampa().getConfermaNumeroCopie()) {
            final ConfermaNumeroCopieStampaDialog dialog = new ConfermaNumeroCopieStampaDialog();

            dialog.setTitle("Conferma del numero copie per la stampa");
            dialog.setMessage(new DefaultMessage(
                    "Indicare il numero copie per il layout " + jecReport.getLayoutStampa().getReportName()));
            dialog.setInputLabelMessage("Numero copie");
            dialog.setPreferredSize(new Dimension(300, 100));
            JSpinner spinner = new JSpinner(new SpinnerNumberModel(numeroCopie.intValue(), 1, 100, 1));
            dialog.setInputField(spinner);
            dialog.showDialog();
            numeroCopie = (Integer) spinner.getValue();

            // se annullo il dialog non stampa nulla
            if (!dialog.isConfirmed()) {
                return null;
            }
        }
        return numeroCopie;
    }

    /**
     * Lancia la stampa con il numero di copie specificato.
     *
     * @param jecReport
     *            jecReport
     * @param numeroCopie
     *            numeroCopie
     */
    private void launch(JecReport jecReport, int numeroCopie) {
        PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
        printRequestAttributeSet.add(new Copies(numeroCopie));
        printRequestAttributeSet.add(new JobName("Panjea-" + jecReport.getReportName(), null));

        if (jecReport.getLayoutStampa().getSoloTesto()) {
            printText(jecReport, printRequestAttributeSet);
        } else {
            print(jecReport, printRequestAttributeSet);
        }
    }

    /**
     * Print.
     *
     * @param jecReport
     *            jecReport
     * @param printRequestAttributeSet
     *            printRequestAttributeSet
     */
    public void print(final JecReport jecReport, PrintRequestAttributeSet printRequestAttributeSet) {
        SimplePrintServiceExporterConfiguration configurazione = new SimplePrintServiceExporterConfiguration();
        configurazione.setPrintRequestAttributeSet(printRequestAttributeSet);
        configurazione.setDisplayPrintDialog(jecReport.isShowPrintDialog());
        configurazione.setPrintService(getPrintService(jecReport));
        print(jecReport, configurazione);
    }

    /**
     * Stampa con la configurazione data
     *
     * @param jecReport
     *            jecReport da stampare
     * @param configurazione
     *            configurazione da utilizzare
     */
    public void print(final JecReport jecReport, SimplePrintServiceExporterConfiguration configurazione) {
        JRPrintServiceExporter exporter = new JRPrintServiceExporter();
        exporter.setConfiguration(configurazione);
        exporter.setExporterInput(new SimpleExporterInput(jecReport.getJrPrint()));
        try {
            exporter.exportReport();
        } catch (JRException e) {
            e.printStackTrace();
        }
    }

    /**
     * PrintText.
     *
     * @param jecReport
     *            jecReport
     * @param printRequestAttributeSet
     *            printRequestAttributeSet
     * @param printService
     */
    public void printText(final JecReport jecReport, PrintRequestAttributeSet printRequestAttributeSet) {
        printText(jecReport, printRequestAttributeSet, null);
    }

    /**
     * PrintText.
     *
     * @param jecReport
     *            jecReport
     * @param printRequestAttributeSet
     *            printRequestAttributeSet
     * @param printService
     *            printService
     */
    public void printText(final JecReport jecReport, PrintRequestAttributeSet printRequestAttributeSet,
            PrintService printService) {
        try (ByteArrayOutputStream outReport = new ByteArrayOutputStream()) {
            JRTextExporter exporterTxt = new JRTextExporter();
            WriterExporterOutput out = new SimpleWriterExporterOutput(outReport, "IBM850");
            exporterTxt.setExporterOutput(out);
            exporterTxt.setExporterInput(new SimpleExporterInput(jecReport.getJrPrint()));
            SimpleTextReportConfiguration txtConfiguration = new SimpleTextReportConfiguration();
            exporterTxt.setConfiguration(txtConfiguration);
            exporterTxt.exportReport();
            DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
            PrintService service = printService;
            if (service == null) {
                service = getPrintService(jecReport);
            }
            if (jecReport.isShowPrintDialog()) {
                PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
                service = ServiceUI.printDialog(null, 200, 200, PrintServiceLookup.lookupPrintServices(flavor, pras),
                        PrintServiceLookup.lookupDefaultPrintService(), flavor, pras);
            }
            if (service != null) {
                try (InputStream inReport = new ByteArrayInputStream(outReport.toByteArray())) {
                    DocPrintJob job = service.createPrintJob();
                    DocAttributeSet das = new HashDocAttributeSet();
                    Doc doc = new SimpleDoc(inReport, flavor, das);
                    job.print(doc, printRequestAttributeSet);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
