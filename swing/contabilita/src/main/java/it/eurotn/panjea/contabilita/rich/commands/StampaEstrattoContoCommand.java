package it.eurotn.panjea.contabilita.rich.commands;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.JobName;

import org.apache.log4j.Logger;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.RcpSupport;

import foxtrot.AsyncTask;
import foxtrot.AsyncWorker;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.rich.bd.ContabilitaAnagraficaBD;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD;
import it.eurotn.panjea.contabilita.rich.forms.ParametriRicercaEstrattoContiForm;
import it.eurotn.panjea.contabilita.util.ParametriRicercaEstrattoConti;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.components.messagealert.ProgressBarMessageAlert;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;
import it.eurotn.rich.report.ReportManager;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;

public class StampaEstrattoContoCommand extends ApplicationWindowAwareCommand {

    private static final Logger LOGGER = Logger.getLogger(StampaEstrattoContoCommand.class);

    public static final String COMMAND_ID = "stampaEstrattoContoCommand";

    private JRPrintServiceExporter exporter = null;

    private AziendaCorrente aziendaCorrente;
    private ReportManager reportManager;

    private ParametriRicercaEstrattoContiForm parametriRicercaForm;

    private ParametriRicercaEstrattoConti parametriRicercaEstrattoConto = null;

    private ProgressBarMessageAlert stampaMessageAlert;

    /**
     * Crea i parametri per il report in base ai parametri di ricerca.
     *
     * @return parametri creati
     */
    private Map<Object, Object> createReportParameters() {
        Map<Object, Object> parametri = new HashMap<Object, Object>();
        parametri.put("azienda", aziendaCorrente.getCodice());
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
        // stati area
        parametri.put("statiArea", parametriRicercaEstrattoConto.getStatiAreaContabileForQuery());

        return parametri;
    }

    @Override
    protected void doExecuteCommand() {
        LOGGER.debug("--> Enter doExecuteCommand");
        parametriRicercaForm = new ParametriRicercaEstrattoContiForm();
        aziendaCorrente = RcpSupport.getBean(AziendaCorrente.BEAN_ID);
        reportManager = RcpSupport.getBean(ReportManager.BEAN_ID);
        IContabilitaAnagraficaBD contabilitaAnagraficaBD = RcpSupport.getBean(ContabilitaAnagraficaBD.BEAN_ID);
        parametriRicercaForm.setAziendaCorrente(aziendaCorrente);
        parametriRicercaForm.setContabilitaBD(contabilitaAnagraficaBD);
        exporter = new net.sf.jasperreports.engine.export.JRPrintServiceExporter();

        PanjeaTitledPageApplicationDialog pageDialog = new PanjeaTitledPageApplicationDialog(parametriRicercaForm,
                null) {

            @Override
            protected void onCancel() {
                super.onCancel();
                parametriRicercaEstrattoConto = null;
            }

            @Override
            protected boolean onFinish() {
                parametriRicercaForm.commit();
                parametriRicercaEstrattoConto = (ParametriRicercaEstrattoConti) parametriRicercaForm.getFormObject();
                return true;
            }
        };
        pageDialog.getDialog().setTitle("");
        pageDialog.showDialog();

        if (parametriRicercaEstrattoConto != null) {
            if (parametriRicercaEstrattoConto.getStampante() == null) {
                MessageDialog message = new MessageDialog("Dati mancanti",
                        new DefaultMessage("Selezionare una stampante", Severity.WARNING));
                message.showDialog();
                return;
            }
            // cerco la stampante con il nome impostato.
            PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
            DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
            PrintService[] printServices = PrintServiceLookup.lookupPrintServices(flavor, pras);
            for (PrintService printService : printServices) {
                if (printService.getName().equals(parametriRicercaEstrattoConto.getStampante())) {
                    exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, printService);
                    break;
                }
            }

            stampaMessageAlert = new ProgressBarMessageAlert("Stampa estratto conto");
            stampaMessageAlert.setDescrizioneOperazione("Sottoconto: ");
            stampaMessageAlert.showAlert();
            stampaMessageAlert.setNumeroOperazioni(parametriRicercaEstrattoConto.getSottoConti().size());

            AsyncWorker.post(new AsyncTask() {

                @Override
                public void failure(Throwable arg0) {
                    stampaMessageAlert.closeAlert();
                }

                @Override
                public Object run() throws Exception {
                    Map<Object, Object> parametri = createReportParameters();
                    for (SottoConto sottoConto : parametriRicercaEstrattoConto.getSottoConti()) {
                        stampaMessageAlert.setDescrizioneOperazione("Sottoconto: " + sottoConto.getSottoContoCodice());

                        if (sottoConto.getConto().getTipoConto() == null) {
                            stampaMessageAlert.incrementaOperazione();
                            continue;
                        }
                        parametri.put("idSottoConto", sottoConto.getId());
                        JasperPrint estrattoContoJasperPrint = reportManager
                                .runReport("Contabilita/Anagrafica/estrattoConto", parametri);

                        if (!estrattoContoJasperPrint.getPages().isEmpty()) {
                            stampa(estrattoContoJasperPrint, sottoConto.getSottoContoCodice().replace(".", "_"));
                        }
                        stampaMessageAlert.incrementaOperazione();
                    }
                    return null;
                }

                @Override
                public void success(Object arg0) {
                    stampaMessageAlert.closeAlert();
                }
            });
        }
        LOGGER.debug("--> Exit doExecuteCommand");
    }

    /**
     *
     * @param jasperPrint
     *            jasperPrint da stampare
     * @param nomeCoda
     *            nome della coda di stampa
     */
    private void stampa(JasperPrint jasperPrint, String nomeCoda) {
        PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
        printRequestAttributeSet.add(new JobName(nomeCoda, null));

        exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet);
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, false);
        exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG_ONLY_ONCE, true);
        try {
            exporter.exportReport();
        } catch (JRException e) {
            e.printStackTrace();
        }
    }
}
