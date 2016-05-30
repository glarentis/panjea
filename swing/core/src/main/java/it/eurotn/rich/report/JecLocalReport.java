package it.eurotn.rich.report;

import foxtrot.AsyncWorker;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.dialog.MessageAlert;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Message;
import org.springframework.richclient.core.Severity;
import org.springframework.rules.closure.Closure;

/**
 * Estende jecLocalReport per poter eseguire i jrxml report creati localmente.
 * 
 * @author giangi
 * @version 1.0, 30/giu/2014
 */
public class JecLocalReport extends JecReport {

    private class JECLocalReportTask extends JecReportTask {
        public JECLocalReportTask(final Closure postAction) {
            super(postAction);
        }

        @Override
        public Object run() throws Exception {
            try {
                if (reportParameters == null) {
                    reportParameters = new TreeMap<String, Object>();
                }
                logger.debug("--> recupero JasperReport");
                JasperReport reportTemplate = getTemplate();
                addMessagesParameter();
                addParameters();
                logger.debug("--> processo eventuali sottoreport ");
                Set<String> subReportNames = subReports.keySet();
                JecLocalReport jecSubReport = null;
                for (String nameSubReport : subReportNames) {
                    jecSubReport = subReports.get(nameSubReport);
                    reportParameters.put(nameSubReport, jecSubReport.getTemplate());
                }
                logger.debug("--> istanza di JasperPrint ");
                jasperPrint = JasperFillManager.fillReport(reportTemplate, reportParameters, getJRDataSource());
            } catch (JRException e) {
                logger.error("-->stampa non riuscita del report " + getReportName(), e);
                System.err.println(e);
                Message message = new DefaultMessage(e.getMessage(), Severity.ERROR);
                final MessageAlert messageAlert = new MessageAlert(message, 0);
                messageAlert.addCloseCommandVisible();
                messageAlert.showAlert();
                PanjeaSwingUtil.checkAndThrowException(e);
            }
            return jasperPrint;
        }
    }

    private static Logger logger = Logger.getLogger(JecLocalReport.class);

    private static final String REPORT_HEADER = "REPORT_HEADER";
    private static final String REPORT_FOOTER = "REPORT_FOOTER";
    /*
     * private static final String REPORT_HEADERDATA = "REPORT_HEADERDATA"; private static final String
     * REPORT_FOOTERDATA = "REPORT_FOOTERDATA";
     */
    private static final String REPORT_AZIENDA = "REPORT_AZIENDA";
    private static final String REPORT_TIME = "REPORT_TIME";
    private static final String REPORT_USER = "REPORT_USER";
    private static final String REPORT_NOTE = "REPORT_NOTE";
    private static final String REPORT_RIFSQ = "REPORT_RIFSQ";

    private List<?> dataReport;

    private HeaderBean dataHeader;

    private FooterBean dataFooter;

    private Resource xmlReportResource;

    private Map<String, Object> reportParameters;

    private JasperPrint jasperPrint;

    /* definisce il subreport dell'intestazione di pagina */
    private JecLocalReport reportHeader;

    /* definisce il subreport del pi� di pagina */
    private JecLocalReport reportFooter;

    /*
     * entry key : nome parametro report principale value : istanza di JasperReport
     */
    private ResourceBundle resourceBundle;

    private String messages;

    private Map<String, JecLocalReport> subReports;

    /**
     * Costruttore.
     */
    public JecLocalReport() {
        init();
    }

    /**
     * .
     */
    private void addMessagesParameter() {
        logger.debug("--> Enter addMessages");
        try {
            if (messages != null) {
                resourceBundle = ResourceBundle.getBundle(messages);
                reportParameters.put(JRParameter.REPORT_RESOURCE_BUNDLE, resourceBundle);
            }
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        }
        logger.debug("--> Exit addMessagesParameter");
    }

    /**
     * .
     */
    private void addParameters() {
        logger.debug("--> Enter addParameters");
        reportParameters.put("ReportTitle", getReportName());
        reportParameters.put(REPORT_HEADER, this.reportHeader != null ? this.reportHeader.getTemplate() : null);
        reportParameters.put(REPORT_FOOTER, this.reportFooter != null ? this.reportFooter.getTemplate() : null);
        reportParameters.put(REPORT_AZIENDA, this.dataHeader != null ? this.dataHeader.getCodiceAzienda() : null);
        reportParameters.put(REPORT_NOTE, this.dataFooter != null ? this.dataFooter.getNote() : null);
        reportParameters.put(REPORT_RIFSQ, this.dataFooter != null ? this.dataFooter.getRifSQ() : null);
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        reportParameters.put(REPORT_TIME, date);
        reportParameters.put(REPORT_USER, this.dataHeader != null ? this.dataHeader.getUtenteCorrente() : null);
        logger.debug("--> Exit addParameters");
    }

    @Override
    public JecReport call() throws Exception {
        jrPrint = (JasperPrint) new JECLocalReportTask(null).run();
        return this;
    }

    @Override
    public void generaReport(Closure postAction) {
        AsyncWorker.post(new JECLocalReportTask(postAction));
    }

    /**
     * @return Returns the dataFooter.
     */
    public FooterBean getDataFooter() {
        return dataFooter;
    }

    /**
     * @return Returns the dataHeader.
     */
    public HeaderBean getDataHeader() {
        return dataHeader;
    }

    /**
     * @return Returns the dataReport.
     */
    public List<?> getDataReport() {
        return dataReport;
    }

    /**
     * @return Returns the jasperPrint.
     */
    public JasperPrint getJasperPrint() {
        return jasperPrint;
    }

    /**
     * Get del data source di JasperReport.
     * 
     * @return JRDataSource
     */
    protected JRDataSource getJRDataSource() {
        if (dataReport == null) {
            return new JREmptyDataSource();
        }
        return new JRBeanCollectionDataSource(dataReport);
    }

    /**
     * @return Returns the messages.
     */
    public String getMessages() {
        return messages;
    }

    /**
     * @return Returns the reportFooter.
     */
    public JecLocalReport getReportFooter() {
        return reportFooter;
    }

    /**
     * @return Returns the reportHeader.
     */
    public JecLocalReport getReportHeader() {
        return reportHeader;
    }

    /**
     * @return Returns the reportParameters.
     */
    public Map<String, Object> getReportParameters() {
        return reportParameters;
    }

    /**
     * @return Returns the resourceBundle.
     */
    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    /**
     * @return Returns the subReports.
     */
    public Map<String, JecLocalReport> getSubReports() {
        return subReports;
    }

    /**
     * @return Restituisce il template di <code>JasperReport</code> utilizzando la risorsa
     *         <code>xmlReportResource</code>. Tale risorsa � indirizzata ad un report compilato .jasper
     */
    private JasperReport getTemplate() {
        logger.debug("--> Enter getTemplate");
        JasperReport reportTemplate = null;
        try (InputStream inputStream = xmlReportResource.getInputStream()) {
            reportTemplate = (JasperReport) JRLoader.loadObject(inputStream);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        }
        logger.debug("--> Exit getTemplate");
        return reportTemplate;
    }

    /**
     * @return Returns the xmlReportResource.
     */
    public Resource getXmlReportResource() {
        return xmlReportResource;
    }

    private void init() {
        subReports = new TreeMap<String, JecLocalReport>();
        reportParameters = new TreeMap<String, Object>();
    }

    /**
     * @param dataFooter
     *            The dataFooter to set.
     */
    public void setDataFooter(FooterBean dataFooter) {
        this.dataFooter = dataFooter;
    }

    /**
     * @param dataHeader
     *            The dataHeader to set.
     */
    public void setDataHeader(HeaderBean dataHeader) {
        this.dataHeader = dataHeader;
    }

    /**
     * @param dataReport
     *            The dataReport to set.
     */
    public void setDataReport(List<?> dataReport) {
        this.dataReport = dataReport;
    }

    /**
     * @param jasperPrint
     *            The jasperPrint to set.
     */
    public void setJasperPrint(JasperPrint jasperPrint) {
        this.jasperPrint = jasperPrint;
    }

    /**
     * @param messages
     *            The messages to set.
     */
    public void setMessages(String messages) {
        this.messages = messages;
    }

    /**
     * @param reportFooter
     *            The reportFooter to set.
     */
    public void setReportFooter(JecLocalReport reportFooter) {
        this.reportFooter = reportFooter;
    }

    /**
     * @param reportHeader
     *            The reportHeader to set.
     */
    public void setReportHeader(JecLocalReport reportHeader) {
        this.reportHeader = reportHeader;
    }

    /**
     * @param reportParameters
     *            The reportParameters to set.
     */
    public void setReportParameters(Map<String, Object> reportParameters) {
        this.reportParameters = reportParameters;
    }

    /**
     * @param resourceBundle
     *            The resourceBundle to set.
     */
    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    /**
     * @param subReports
     *            The subReports to set.
     */
    public void setSubReports(Map<String, JecLocalReport> subReports) {
        this.subReports = subReports;
    }

    /**
     * @param xmlReportResource
     *            The xmlReportResource to set.
     */
    public void setXmlReportResource(Resource xmlReportResource) {
        this.xmlReportResource = xmlReportResource;
    }

}
