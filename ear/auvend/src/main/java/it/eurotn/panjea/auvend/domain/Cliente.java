package it.eurotn.panjea.auvend.domain;

import java.io.Serializable;

/**
 * Cliente contenete i dati sia del cliente Auvend che del cliente di Panjea.
 * 
 * @author giangi
 * 
 */
public class Cliente implements Serializable {
	private static final long serialVersionUID = -223946261187964951L;
	private String codiceAuvend;
	private String denominazioneAuvend;
	private String codicePanjea;
	private Integer idPanjea;

	/**
	 * 
	 * Costruttore.
	 * 
	 * @param codiceAuvend
	 *            codiceAuvend
	 * @param codicePanjea
	 *            codicePanjea
	 * @param denominazioneAuvend
	 *            denominazioneAuvend
	 * @param idPanjea
	 *            idPanjea
	 */
	public Cliente(final String codiceAuvend, final String codicePanjea, final String denominazioneAuvend,
			final Integer idPanjea) {
		super();
		this.codiceAuvend = codiceAuvend;
		this.codicePanjea = (codicePanjea != null && codicePanjea.equals("0")) ? null : codicePanjea;
		this.denominazioneAuvend = denominazioneAuvend;
		this.idPanjea = idPanjea;
	}

	/**
	 * @return codiceAuvend
	 */
	public String getCodiceAuvend() {
		return codiceAuvend;
	}

	/**
	 * @return codicePanjea
	 */
	public String getCodicePanjea() {
		return codicePanjea;
	}

	/**
	 * @return denominazioneAuvend
	 */
	public String getDenominazioneAuvend() {
		return denominazioneAuvend;
	}

	/**
	 * @return idPanjea
	 */
	public Integer getIdPanjea() {
		return idPanjea;
	}

}
