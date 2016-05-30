/**
 *
 */
package it.eurotn.panjea.contabilita.service.exception;

import it.eurotn.panjea.contabilita.domain.RegistroIva.TipoRegistro;

/**
 * @author leonardo
 * 
 */
public class RegistroIvaAcquistiAssentePerVentilazione extends RegistroIvaAssenteException {

	private static final long serialVersionUID = 8383248637770443455L;

	/**
	 * Costruttore.
	 * 
	 * @param numeroRegistro
	 *            numeroRegistro
	 */
	public RegistroIvaAcquistiAssentePerVentilazione(final Integer numeroRegistro) {
		super(TipoRegistro.ACQUISTO, numeroRegistro);
	}

	/**
	 * Costruttore.
	 * 
	 * @param message
	 *            message
	 */
	public RegistroIvaAcquistiAssentePerVentilazione(final String message) {
		super(message);
	}

	/**
	 * Costruttore.
	 * 
	 * @param message
	 *            message
	 * @param cause
	 *            cause
	 */
	public RegistroIvaAcquistiAssentePerVentilazione(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Costruttore.
	 * 
	 * @param cause
	 *            cause
	 */
	public RegistroIvaAcquistiAssentePerVentilazione(final Throwable cause) {
		super(cause);
	}

}
