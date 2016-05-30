package it.eurotn.panjea.contabilita.util;

import java.io.Serializable;
import java.util.Date;

/**
 * Contiente i dati per un protocollo mancante.
 * 
 * @author giangi
 * @version 1.0, 23/ago/2012
 * 
 */
public class RisultatoControlloProtocolli implements Serializable {
	private static final long serialVersionUID = -1952078439584036453L;
	private String registroProtocollo;
	private Integer codice;
	private Integer anno;
	private Date data;

	/**
	 * 
	 * Costruttore.
	 * 
	 * @param registroProtocollo
	 *            registro Protocollo
	 * @param codice
	 *            codice del protocollo
	 * @param anno
	 *            anno del protocollo
	 */
	public RisultatoControlloProtocolli(final String registroProtocollo, final int codice, final int anno) {
		super();
		this.registroProtocollo = registroProtocollo;
		this.codice = codice;
		this.anno = anno;
	}

	/**
	 * 
	 * Costruttore.
	 * 
	 * @param registroProtocollo
	 *            registro Protocollo
	 * @param codice
	 *            codice del protocollo
	 * @param data
	 *            data del protocollo
	 */
	public RisultatoControlloProtocolli(final String registroProtocollo, final Integer codice, final Date data,
			final int anno) {
		super();
		this.registroProtocollo = registroProtocollo;
		this.codice = codice;
		this.data = data;
		this.anno = anno;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		RisultatoControlloProtocolli other = (RisultatoControlloProtocolli) obj;
		if (anno == null) {
			if (other.anno != null) {
				return false;
			}
		} else if (!anno.equals(other.anno)) {
			return false;
		}
		if (codice == null) {
			if (other.codice != null) {
				return false;
			}
		} else if (!codice.equals(other.codice)) {
			return false;
		}
		if (registroProtocollo == null) {
			if (other.registroProtocollo != null) {
				return false;
			}
		} else if (!registroProtocollo.equals(other.registroProtocollo)) {
			return false;
		}
		return true;
	}

	/**
	 * @return Returns the anno.
	 */
	public Integer getAnno() {
		return anno;
	}

	/**
	 * @return Returns the codiceMancante.
	 */
	public Integer getCodice() {
		return codice;
	}

	/**
	 * @return Returns the data.
	 */
	public Date getData() {
		return data;
	}

	/**
	 * @return Returns the registroProtocollo.
	 */
	public String getRegistroProtocollo() {
		return registroProtocollo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((anno == null) ? 0 : anno.hashCode());
		result = prime * result + ((codice == null) ? 0 : codice.hashCode());
		result = prime * result + ((registroProtocollo == null) ? 0 : registroProtocollo.hashCode());
		return result;
	}

	/**
	 * @param data
	 *            The data to set.
	 */
	public void setData(Date data) {
		this.data = data;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RisultatoControlloProtocolliMancanti [registroProtocollo=" + registroProtocollo + ", codice=" + codice
				+ ", anno=" + anno + ",data=" + data + "]";
	}
}
