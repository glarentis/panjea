package it.eurotn.rich.report;

/**
 * Rilanciata se la stampa di un report genera un errore.<BR>
 * Solitamente identifica un report che non riesce ad aprire perchè non compila<BR>
 * oppure ci sono parametri passati al report non corretti
 * 
 * @author giangi
 * 
 */
public class JecJasperServerException extends RuntimeException {
    private static final long serialVersionUID = -5010137623094330807L;

    public JecJasperServerException() {
        super();
    }

    public JecJasperServerException(Throwable cause) {
        super(cause);
    }

}
