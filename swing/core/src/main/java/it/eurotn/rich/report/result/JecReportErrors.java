package it.eurotn.rich.report.result;

import java.util.ArrayList;
import java.util.List;

public class JecReportErrors {

    private List<String> reportsMaxPageException;
    private List<String> reportsGenericException;
    private List<String> reportsNonTrovatiException;

    {
        reportsMaxPageException = new ArrayList<String>();
        reportsGenericException = new ArrayList<String>();
        reportsNonTrovatiException = new ArrayList<String>();
    }

    /**
     * @param reportErrors
     *            aggiunge tutti gli errori presenti
     */
    public void addErrors(JecReportErrors reportErrors) {

        if (reportErrors.getReportsGenericException() != null) {
            reportsGenericException.addAll(reportErrors.getReportsGenericException());
        }

        if (reportErrors.getReportsMaxPageException() != null) {
            reportsMaxPageException.addAll(reportErrors.getReportsMaxPageException());
        }

        if (reportErrors.getReportsNonTrovatiException() != null) {
            reportsNonTrovatiException.addAll(reportErrors.getReportsNonTrovatiException());
        }
    }

    /**
     * @return the reportsGenericException
     */
    public List<String> getReportsGenericException() {
        return reportsGenericException;
    }

    /**
     * @return the reportsMaxPageException
     */
    public List<String> getReportsMaxPageException() {
        return reportsMaxPageException;
    }

    /**
     * @return the reportsNonTrovatiException
     */
    public List<String> getReportsNonTrovatiException() {
        return reportsNonTrovatiException;
    }

    /**
     * @return <code>true</code> se nessun report ha avuto problemi di generazione
     */
    public boolean hasErrors() {
        return (reportsMaxPageException != null && !reportsMaxPageException.isEmpty())
                || (reportsGenericException != null && !reportsGenericException.isEmpty())
                || (reportsNonTrovatiException != null && !reportsNonTrovatiException.isEmpty());
    }
}
