package it.eurotn.rich.report.exception;

public class ReportNonTrovatoException extends JecReportException {

    private static final long serialVersionUID = 1L;

    /**
     * Costruttore.
     *
     * @param reportPath
     *            report path
     */
    public ReportNonTrovatoException(final String reportPath) {
        super(reportPath);
    }
}
