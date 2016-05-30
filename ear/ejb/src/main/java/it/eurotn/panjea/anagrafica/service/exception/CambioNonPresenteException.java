package it.eurotn.panjea.anagrafica.service.exception;

import java.util.Date;

/**
 * 
 * Lanciata quando non ho nessun cambio presente per la valuta in una determinata data.
 * 
 * @author giangi
 * @version 1.0, 25/lug/2011
 * 
 */
public class CambioNonPresenteException extends Exception {
	private static final long serialVersionUID = -6963907287823848191L;
	private Date data;
	private String codiceValuta;

	/**
	 * 
	 * Costruttore.
	 * 
	 * @param data
	 *            data alla quale è stato richiesto il cambio
	 * @param codiceValuta
	 *            codice della valuta della quale è stato richiesto il cambio.
	 */
	public CambioNonPresenteException(final Date data, final String codiceValuta) {
		super();
		this.data = data;
		this.codiceValuta = codiceValuta;
	}

	/**
	 * @return Returns the codiceValuta.
	 */
	public String getCodiceValuta() {
		return codiceValuta;
	}

	/**
	 * @return Returns the data.
	 */
	public Date getData() {
		return data;
	}

}
