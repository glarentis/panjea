package it.eurotn.rich.report.result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.eurotn.rich.report.JecReport;
import it.eurotn.rich.report.exception.JecMaxPagesGovernatorException;
import it.eurotn.rich.report.exception.JecReportException;
import it.eurotn.rich.report.exception.ReportNonTrovatoException;

public class JecReportResult {

    private List<JecReport> reportGenerati;

    private JecReportErrors reportErrors;

    {
        reportGenerati = new ArrayList<JecReport>();

        reportErrors = new JecReportErrors();
    }

    /**
     * Costruttore.
     */
    public JecReportResult() {
        super();
    }

    /**
     * Aggiunge l'errore dell'eccezione ai report con errore.
     *
     * @param exception
     *            eccezione
     */
    public void addReportException(JecReportException exception) {

        if (exception instanceof JecMaxPagesGovernatorException) {
            reportErrors.getReportsMaxPageException().add(exception.getDescription());
        } else if (exception instanceof ReportNonTrovatoException) {
            reportErrors.getReportsNonTrovatiException().add(exception.getDescription());
        } else {
            reportErrors.getReportsGenericException().add(exception.getDescription());
        }
    }

    /**
     * Aggiunge un report generato correttamente.
     *
     * @param report
     *            report
     */
    public void addReportGenerato(JecReport report) {
        reportGenerati.add(report);
    }

    /**
     * @return the reportErrors
     */
    public JecReportErrors getReportErrors() {
        return reportErrors;
    }

    /**
     * @return the reportGenerati
     */
    public List<JecReport> getReportGenerati() {
        // riordina per ordineStampa (dato che non è prevedibile l'ordine di esecuzione dell' executor)
        Collections.sort(reportGenerati);
        return Collections.unmodifiableList(reportGenerati);
    }

}
