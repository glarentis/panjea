/**
 * 
 */
package it.eurotn.panjea.auvend.util;

/**
 * Classe di utilità che rappresenta la parte delle imposte delle fatture di AuVend.
 * 
 * @author adriano
 * @version 1.0, 06/feb/2009
 * 
 */
public class FatturaIva {

	private Double imponibile;

	private Double aliquota;

	private Double imposta;

	/**
	 * @return Returns the aliquota.
	 */
	public Double getAliquota() {
		return aliquota;
	}

	/**
	 * @return Returns the imponibile.
	 */
	public Double getImponibile() {
		return imponibile;
	}

	/**
	 * @return Returns the imposta.
	 */
	public Double getImposta() {
		return imposta;
	}

	/**
	 * @param aliquota
	 *            The aliquota to set.
	 */
	public void setAliquota(Double aliquota) {
		this.aliquota = aliquota;
	}

	/**
	 * @param imponibile
	 *            The imponibile to set.
	 */
	public void setImponibile(Double imponibile) {
		this.imponibile = imponibile;
	}

	/**
	 * @param imposta
	 *            The imposta to set.
	 */
	public void setImposta(Double imposta) {
		this.imposta = imposta;
	}

	/**
	 * Constructs a <code>String</code> with all attributes in name = value format.
	 * 
	 * @return a <code>String</code> representation of this object.
	 */
	@Override
	public String toString() {

		StringBuffer retValue = new StringBuffer();

		retValue.append("FatturaImposta[ ").append(super.toString());
		retValue.append(" aliquota = ").append(this.aliquota);
		retValue.append(" imponibile = ").append(this.imponibile);
		retValue.append(" imposta = ").append(this.imposta);
		retValue.append(" ]");

		return retValue.toString();
	}

}
