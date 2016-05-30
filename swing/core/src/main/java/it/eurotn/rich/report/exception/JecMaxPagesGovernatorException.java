package it.eurotn.rich.report.exception;

public class JecMaxPagesGovernatorException extends JecReportException {

    private static final long serialVersionUID = -6827015622022981104L;

    /**
     * Costruttore.
     *
     * @param reportPath
     *            report path
     */
    public JecMaxPagesGovernatorException(final String reportPath) {
        super(reportPath);
    }

}
