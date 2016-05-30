/**
 *
 */
package it.eurotn.rich.report;

import it.eurotn.panjea.anagrafica.domain.ParametriMail;
import it.eurotn.panjea.rich.stampe.ILayoutStampeManager;
import it.eurotn.panjea.rich.stampe.LayoutStampeManager;
import it.eurotn.panjea.stampe.domain.LayoutStampa;
import it.eurotn.panjea.stampe.manager.FormulaNumeroCopieCalculator;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.command.JECCommandGroup;
import it.eurotn.rich.dialog.MessageAlert;
import it.eurotn.rich.report.exception.JecReportException;
import it.eurotn.rich.report.result.JecReportResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Message;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.closure.Closure;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

import foxtrot.AsyncTask;
import foxtrot.AsyncWorker;

/**
 *
 */
public class JecReport implements Callable<JecReport>, Comparable<JecReport> {

    public class JecReportTask extends AsyncTask {

        private Closure postExecuteAction;

        /**
         * Costruttore.
         *
         * @param postExecuteAction
         *            azione da fare quando il report è stato generato
         */
        public JecReportTask(final Closure postExecuteAction) {
            this.postExecuteAction = postExecuteAction;
        }

        @Override
        public void failure(Throwable error) {
            if (stampaInCorsoMessageAlert != null && stampaInCorsoMessageAlert.isVisible()) {
                stampaInCorsoMessageAlert.closeAlert();
            }

            if (error instanceof JecReportException) {
                ((JecReportException) error).setDescription(getDisplayName());
            }
            PanjeaSwingUtil.checkAndThrowException(error);
        }

        @Override
        public Object run() throws Exception {
            try {
                return reportManager.runReport(reportPath, parameters);
            } catch (JecReportException e) {
                failure(e);
                return null;
            }
        }

        @Override
        public void success(Object arg0) {
            jrPrint = (JasperPrint) arg0;

            JecReportResult reportsAggiuntivi = JecReports.generaReports(reportToAddAlways);
            // tengo i report da aggiungere solo sulla prima pagina generati nella variabile di classe in modo da
            // poterli recuperare in seguito
            JecReportResult reportToAddOnFirstCopyResult = JecReports.generaReports(reportToAddOnFirstCopy);

            // se i report aggiuntivi hanno un'eccezione considero non valido tutto il report quindi sollevo l'eccezione
            // sul report principale
            if (reportsAggiuntivi.getReportErrors().hasErrors()
                    || reportToAddOnFirstCopyResult.getReportErrors().hasErrors()) {
                JecReportException exception = new JecReportException(getReportPath());
                exception.setDescription(getDisplayName());

                PanjeaSwingUtil.checkAndThrowException(exception);
            }

            reportToAddOnFirstCopy = reportToAddOnFirstCopyResult.getReportGenerati();
            for (JecReport jecReport : reportsAggiuntivi.getReportGenerati()) {
                jrPrint.getPages().addAll(jecReport.getJrPrint().getPages());
            }
            for (JecReport jecReport : reportToAddOnFirstCopy) {
                jrPrint.getPages().addAll(jecReport.getJrPrint().getPages());
            }

            if (postExecuteAction != null) {
                postExecuteAction.call(JecReport.this);
            }
        }
    }

    public class OpenReportClosure implements Closure {

        private Closure postCustomAction;

        /**
         * Costruttore.
         *
         * @param postCustomAction
         *            custom action on post open report
         */
        public OpenReportClosure(final Closure postCustomAction) {
            super();
            this.postCustomAction = postCustomAction;
        }

        @Override
        public Object call(Object arg0) {
            if (stampaInCorsoMessageAlert != null && stampaInCorsoMessageAlert.isVisible()) {
                stampaInCorsoMessageAlert.closeAlert();
            }
            if (layoutStampa.getPreview()) {
                // anteprima
                LifecycleApplicationEvent event = new OpenEditorEvent(JecReport.this);
                Application.instance().getApplicationContext().publishEvent(event);
            } else {
                JasperPrintExporter printExporter = new JasperPrintExporter();
                printExporter.export(JecReport.this);
            }
            if (postCustomAction != null) {
                postCustomAction.call(JecReport.this);
            }
            return null;
        }
    }

    private static Logger logger = Logger.getLogger(JecReport.class);

    protected JECCommandGroup exportCommandGroup;
    protected ReportManager reportManager;
    protected ILayoutStampeManager layoutStampeManager;

    protected boolean showPrintDialog;
    protected Map<Object, Object> parameters;

    protected JasperPrint jrPrint;
    protected LayoutStampa layoutStampa;
    private String reportName;
    protected String reportPath;
    protected ParametriMail parametriMail;
    private List<JecReport> reportToAddAlways;
    private List<JecReport> reportToAddOnFirstCopy;
    private MessageAlert stampaInCorsoMessageAlert;
    /**
     * Nel caso in cui un report voglia utilizzare il report name invece del display name personalizzato; ad es.nel
     * JECReportDocumento.
     */
    protected boolean forceUseReportName;
    private Integer ordineStampa;

    {
        reportToAddAlways = new ArrayList<JecReport>();
        reportToAddOnFirstCopy = new ArrayList<JecReport>();
        reportManager = RcpSupport.getBean(ReportManager.BEAN_ID);
        layoutStampeManager = RcpSupport.getBean(LayoutStampeManager.BEAN_ID);
        showPrintDialog = false;
        parametriMail = new ParametriMail();
        parameters = new HashMap<Object, Object>();
        forceUseReportName = false;
    }

    /**
     * Costruttore.
     */
    protected JecReport() {
        super();
    }

    /**
     * @param jrPrint
     *            jrPrint creato esternamente.
     */
    public JecReport(final JasperPrint jrPrint) {
        super();
        this.jrPrint = jrPrint;
    }

    /**
     * @param reportPath
     *            path del report da gestinre
     * @param parameters
     *            eventuali parametri.Null se il report è senza parametri
     */
    public JecReport(final String reportPath, final Map<Object, Object> parameters) {
        super();
        this.parameters = parameters;
        this.reportPath = reportPath;
        configuraParametri();
    }

    /**
     * Aggiungo un report. Il contenuto del report verrà aggiungo alle pagine del report principale.
     *
     * @param report
     *            report da aggiungere.
     */
    public void addReport(JecReport report) {
        addReport(report, false);
    }

    /**
     * Aggiungo un report. Il contenuto del report verrà aggiungo alle pagine del report principale.
     *
     * @param reportToAdd
     *            report da aggiungere
     * @param onlyOnFirstCopy
     *            il report viene aggiunto solamente sulla prima copia.
     */
    public void addReport(JecReport reportToAdd, boolean onlyOnFirstCopy) {
        if (onlyOnFirstCopy) {
            reportToAddOnFirstCopy.add(reportToAdd);
        } else {
            reportToAddAlways.add(reportToAdd);
        }
    }

    @Override
    public JecReport call() throws Exception {
        caricaLayoutStampa();
        jrPrint = (JasperPrint) new JecReportTask(null).run();
        return this;
    }

    /**
     * Carica il layout di stampa.
     */
    public void caricaLayoutStampa() {
        if (layoutStampa == null) {
            layoutStampa = layoutStampeManager.caricaLayoutStampe(getReportName());
            if (layoutStampa == null) {
                // genero il layout di default
                layoutStampa = new LayoutStampa();
                layoutStampa.setReportName(reportName);
            }
        }
    }

    @Override
    public int compareTo(JecReport o) {
        return this.getOrdineStampa().compareTo(o.getOrdineStampa());
    }

    /**
     * Inizializza i parametri del report.
     */
    protected void configuraParametri() {
        if (this.parameters == null) {
            this.parameters = new HashMap<Object, Object>();
        }
        if (!parameters.containsKey("azienda")) {
            this.parameters.put("azienda", reportManager.getCodiceAzienda().toLowerCase());
        }
    }

    /**
     * Esegure il report ed esegue l'azione in base alla configurazione del report.
     */
    public void execute() {
        execute(null);
    }

    /**
     * Esegue ed esporta il report.
     *
     * @param postAction
     *            esegue il report,esegue l'azione di default per il report e richiama la closure.
     */
    public void execute(Closure postAction) {
        Message message = new DefaultMessage("Stampa del documento \n" + getReportName() + "\n in corso...",
                Severity.INFO);
        stampaInCorsoMessageAlert = new MessageAlert(message, 0);
        stampaInCorsoMessageAlert.showAlert();
        caricaLayoutStampa();
        if (jrPrint == null) {
            generaReport(postExecute(postAction));
        } else {
            postExecute(postAction).call(null);
        }
    }

    /**
     * Genera il report con i parametri impostati.
     *
     * @param postAction
     *            azione da eseguire quando il report è stato generato
     */
    public void generaReport(Closure postAction) {
        caricaLayoutStampa();
        AsyncWorker.post(new JecReportTask(postAction));
    }

    /**
     * @return the name to display
     */
    public String getDisplayName() {
        return getReportName();
    }

    /**
     * @return Returns the exportCommandGroup.
     */
    public JECCommandGroup getExportCommandGroup() {
        return exportCommandGroup;
    }

    /**
     * @return Returns the jrPrint.
     */
    public synchronized JasperPrint getJrPrint() {
        return jrPrint;
    }

    /**
     * @return Returns the layoutStampa.
     */
    public LayoutStampa getLayoutStampa() {
        return layoutStampa;
    }

    /**
     * @return calcola il numero copie dal layout di stampa
     */
    public int getNumeroCopie() {
        return FormulaNumeroCopieCalculator.calcolaNumeroCopie(layoutStampa.getFormulaNumeroCopie(),
                new HashMap<String, Object>());
    }

    /**
     * @return Returns the ordineStampa.
     */
    public Integer getOrdineStampa() {
        return ordineStampa;
    }

    /**
     * @return Returns the parameters.
     */
    public Map<Object, Object> getParameters() {
        return parameters;
    }

    /**
     * @return Returns the parametriMail.
     */
    public ParametriMail getParametriMail() {
        return parametriMail;
    }

    /**
     * @return Returns the reportExportPath.
     */
    public String getReportExportPath() {
        return reportName;
    }

    /**
     * @return Returns the reportName.
     */
    public String getReportName() {
        return reportName;
    }

    /**
     * @return Returns the reportPath.
     */
    public String getReportPath() {
        return reportPath;
    }

    /**
     * @return List<JecReport>
     */
    public List<JecReport> getReportToAddOnFirstCopy() {
        return reportToAddOnFirstCopy;
    }

    /**
     * @return reportToAddOnFirstCopy
     */
    public boolean isReportsAggiuntiviPrimaCopia() {
        return reportToAddOnFirstCopy != null && reportToAddOnFirstCopy.size() > 0;
    }

    /**
     * @return Returns the showPrintDialog.
     */
    public boolean isShowPrintDialog() {
        return showPrintDialog;
    }

    /**
     * Esecuzione della post-azione.
     *
     * @param postCustomAction
     *            postCustomAction
     * @return Closure
     */
    public Closure postExecute(final Closure postCustomAction) {
        return new OpenReportClosure(postCustomAction);
    }

    /**
     * @param exportCommandGroup
     *            The exportCommandGroup to set.
     */
    public void setExportCommandGroup(JECCommandGroup exportCommandGroup) {
        this.exportCommandGroup = exportCommandGroup;
    }

    /**
     * @param forceUseReportName
     *            the forceUseReportName to set
     */
    public void setForceUseReportName(boolean forceUseReportName) {
        this.forceUseReportName = forceUseReportName;
    }

    /**
     * @param layoutStampa
     *            The layoutStampa to set.
     */
    public void setLayoutStampa(LayoutStampa layoutStampa) {
        this.layoutStampa = layoutStampa;
    }

    /**
     * @param ordineStampa
     *            The ordineStampa to set.
     */
    public void setOrdineStampa(Integer ordineStampa) {
        this.ordineStampa = ordineStampa;
    }

    /**
     * @param parametriMail
     *            The parametriMail to set.
     */
    public void setParametriMail(ParametriMail parametriMail) {
        this.parametriMail = parametriMail;
    }

    /**
     * @param reportName
     *            The reportName to set.
     */
    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    /**
     * @param showPrintDialog
     *            The showPrintDialog to set.
     */
    public void setShowPrintDialog(boolean showPrintDialog) {
        this.showPrintDialog = showPrintDialog;
    }
}
