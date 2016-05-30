package it.eurotn.panjea.vending.rest.manager.palmari.exception;

public class ImportazioneException extends Exception {
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 7692623984803944072L;
    private String log;

    /**
     * @param log
     *            log eccezione
     */
    public ImportazioneException(final String log) {
        super();
        this.log = log;
    }

    /**
     * @return Returns the serialversionuid.
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    @Override
    public String getMessage() {
        return log;
    }

}
