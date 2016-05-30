/**
 *
 */
package it.eurotn.rich.report.jasperserver.hyperlink;

import java.util.List;

import net.sf.jasperreports.engine.JRPrintHyperlinkParameter;

/**
 * La classe si occupa di eseguire il report contenuto nel parametro <code>_report</code> e di visualizzarlo in un nuovo
 * editor.
 *
 * @author fattazzo
 *
 */
public class ReportExecutor implements IHyperlinkExecutor {

    /*
     * (non-Javadoc)
     * 
     * @see it.eurotn.rich.report.jasperserver.hyperlink.IHyperlinkExecutor#execute(java.util.List)
     */
    @Override
    public void execute(List<JRPrintHyperlinkParameter> list) {
        //
        // ReportManager reportManager = RcpSupport.getBean(ReportManager.BEAN_ID);
        //
        // JecJasperServerReport jasperServerReport = (JecJasperServerReport) Application.instance()
        // .getApplicationContext().getBean(JecJasperServerReport.BEAN_NAME);
        // String reportName = "";
        //
        // String reportPath = null;
        // Map<Object, Object> parametriReport = new HashMap<Object, Object>();
        //
        // for (JRPrintHyperlinkParameter parametro : list) {
        //
        // if ("_report".equals(parametro.getName())) {
        // reportPath = ((String) parametro.getValue()).split(":")[0];
        //
        // // siccome il valore del parametro report è composto con
        // // /nomeazienda/standard/.../.../...
        // // tolgo il nome azienda e il tipo perchè nel metodo openReport vengono automenticamente aggiunti.
        // int idxbarra = reportPath.indexOf("/", 2);
        //
        // if (idxbarra != -1) {
        // reportPath = reportPath.substring(idxbarra + 1);
        // }
        //
        // idxbarra = reportPath.indexOf("/", 2);
        //
        // if (idxbarra != -1) {
        // reportPath = reportPath.substring(idxbarra + 1);
        // }
        //
        // Set<String> reports = reportManager.listReport(reportPath.substring(0, reportPath.lastIndexOf("/")));
        // for (String report : reports) {
        // if (report.equals(reportPath.substring(reportPath.lastIndexOf("/") + 1))) {
        // reportName = report;
        // break;
        // }
        // }
        //
        // } else {
        // parametriReport.put(parametro.getName(), parametro.getValue());
        // }
        // }
        //
        // // TODO internazionalizzare il nome del report sul properties per il titolo dell'editor che si apre
        // jasperServerReport.setReportName(reportName);
        // jasperServerReport.openReport(reportPath, parametriReport, null, null, 1, null);
    }
}
