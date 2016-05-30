package it.eurotn.rich.report.exception;

import it.eurotn.rich.report.result.JecReportErrors;

public class JecReportErrorsException extends Exception {

    private JecReportErrors reportErrors;

    /**
     * Costruttore.
     *
     * @param reportErrors
     *            errori
     */
    public JecReportErrorsException(final JecReportErrors reportErrors) {
        super();
        this.reportErrors = reportErrors;
    }

    /**
     * @return the reportErrors
     */
    public JecReportErrors getReportErrors() {
        return reportErrors;
    }
}
