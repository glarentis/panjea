package it.eurotn.rich.report.exception;

public class JecReportException extends Exception {

    private static final long serialVersionUID = 4529693801329125177L;

    private String reportPath;

    private String description;

    /**
     * Costruttore.
     *
     * @param reportPath
     *            report path
     */
    public JecReportException(final String reportPath) {
        super();
        this.reportPath = reportPath;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the reportPath
     */
    public String getReportPath() {
        return reportPath;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

}
