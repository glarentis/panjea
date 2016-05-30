package it.eurotn.panjea.auvend.exception;

import it.eurotn.panjea.auvend.domain.TipoDocumentoBaseAuVend;
import it.eurotn.panjea.exception.PanjeaPropertyException;

/**
 * Eccezione per segnalare la presenza di piu' tipi documenti per tipo operazione. questo non e' ammesso per i <br>
 * tipi operazione RECUPERO_CARICO e RECUPERO_RIFORNIMENTO.
 *
 * @author adriano
 * @version 1.0, 27/gen/2009
 *
 */
public class TipoDocumentoPerTipoOperazioneNotUniqueException extends Exception implements PanjeaPropertyException {

    private static final long serialVersionUID = 1L;

    private TipoDocumentoBaseAuVend tipoDocumentoBaseAuVend;

    /**
     * Costruttore.
     *
     * @param message
     *            messaggio
     * @param cause
     *            causa
     * @param tipoDocumentoBaseAuVend
     *            tipo documento base
     */
    public TipoDocumentoPerTipoOperazioneNotUniqueException(final String message, final Throwable cause,
            final TipoDocumentoBaseAuVend tipoDocumentoBaseAuVend) {
        super(message, cause);
        this.tipoDocumentoBaseAuVend = tipoDocumentoBaseAuVend;
    }

    /**
     * Costruttore.
     *
     * @param message
     *            messaggio
     * @param tipoDocumentoBaseAuVend
     *            tipo documento base
     */
    public TipoDocumentoPerTipoOperazioneNotUniqueException(final String message,
            final TipoDocumentoBaseAuVend tipoDocumentoBaseAuVend) {
        super(message);
        this.tipoDocumentoBaseAuVend = tipoDocumentoBaseAuVend;
    }

    /**
     * Costruttore.
     *
     * @param cause
     *            causa
     * @param tipoDocumentoBaseAuVend
     *            tipo documento base
     */
    public TipoDocumentoPerTipoOperazioneNotUniqueException(final Throwable cause,
            final TipoDocumentoBaseAuVend tipoDocumentoBaseAuVend) {
        super(cause);
        this.tipoDocumentoBaseAuVend = tipoDocumentoBaseAuVend;
    }

    @Override
    public String[] getPropertyVaules() {
        return new String[] { tipoDocumentoBaseAuVend.getTipoOperazione().name(),
                tipoDocumentoBaseAuVend.getTipoAreaMagazzino().getTipoDocumento().getDescrizione() };
    }

    /**
     * @return Returns the tipoDocumentoBaseAuVend.
     */
    public TipoDocumentoBaseAuVend getTipoDocumentoBaseAuVend() {
        return tipoDocumentoBaseAuVend;
    }

}
