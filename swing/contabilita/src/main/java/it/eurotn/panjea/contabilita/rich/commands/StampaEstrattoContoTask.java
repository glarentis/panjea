package it.eurotn.panjea.contabilita.rich.commands;

import java.util.Map;
import java.util.concurrent.Callable;

import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.JobName;

import org.springframework.richclient.util.RcpSupport;

import it.eurotn.rich.report.ReportManager;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;

public class StampaEstrattoContoTask implements Callable<String> {

    private String nomeStampante;
    private Map<Object, Object> parametri;
    private int idSottoConto;
    private String sottoContoCodice;

    /**
     *
     * Costruttore.
     *
     * @param nomeStampante
     *            nome stampante
     * @param parametri
     *            parametri di stampa
     * @param idSottoConto
     *            id sotto conto
     * @param sottoContoCodice
     *            codice sotto conto
     */
    public StampaEstrattoContoTask(final String nomeStampante, final Map<Object, Object> parametri,
            final int idSottoConto, final String sottoContoCodice) {
        super();
        this.nomeStampante = nomeStampante;
        this.parametri = parametri;
        this.idSottoConto = idSottoConto;
        this.sottoContoCodice = sottoContoCodice;
    }

    @Override
    public String call() throws Exception {
        JRPrintServiceExporter exporter = new net.sf.jasperreports.engine.export.JRPrintServiceExporter();
        PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
        DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(flavor, pras);
        ReportManager reportManager = RcpSupport.getBean(ReportManager.BEAN_ID);
        for (PrintService printService : printServices) {
            if (printService.getName().equals(nomeStampante)) {
                exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, printService);
                break;
            }
        }
        parametri.put("idSottoConto", idSottoConto);
        JasperPrint estrattoContoJasperPrint = reportManager.runReport("Contabilita/Anagrafica/estrattoConto",
                parametri);

        if (!estrattoContoJasperPrint.getPages().isEmpty()) {
            PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
            printRequestAttributeSet.add(new JobName(sottoContoCodice.replace(".", "_"), null));

            exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET,
                    printRequestAttributeSet);
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, estrattoContoJasperPrint);
            exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, false);
            exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG_ONLY_ONCE, true);
            try {
                exporter.exportReport();
            } catch (JRException e) {
                e.printStackTrace();
            }
        }
        return sottoContoCodice;
    }

}
