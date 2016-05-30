package it.eurotn.panjea.fatturepa.manager.exception;

public class XMLXSDValidationException extends Exception {

    private static final long serialVersionUID = 6668346080286474289L;

    private final String errors;

    /**
     * Costruttore.
     *
     * @param errors
     *            errori presenti
     */
    public XMLXSDValidationException(final String errors) {
        super();
        this.errors = errors;
    }

    /**
     * @return the errors
     */
    public String getErrors() {
        return errors;
    }

}
