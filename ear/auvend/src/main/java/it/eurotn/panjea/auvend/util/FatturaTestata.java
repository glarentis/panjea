/**
 * 
 */
package it.eurotn.panjea.auvend.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe di utilita' che rappresenta la testata della fattura di AuVend.
 * 
 * @author adriano
 * @version 1.0, 27/gen/2009
 * 
 */
public class FatturaTestata {

	private int numeroTerminale;

	private Date dataFattura;

	private int numeroFattura;

	private String codiceClienteAuVend;

	private String codiceClientePanjea;

	private Double totaleFattura;

	private Double totaleImponibile;

	private Double totaleImposta;

	private Map<Double, FatturaIva> fattureIva;

	/**
	 * Costruttore.
	 * 
	 */
	public FatturaTestata() {
		super();
		fattureIva = new HashMap<Double, FatturaIva>();
	}

	/**
	 * 
	 * @param fatturaIva
	 *            aggiunge una {@link FatturaIva} alla testata
	 */
	public void addFatturaIva(FatturaIva fatturaIva) {
		if (fattureIva.containsKey(fatturaIva.getAliquota())) {
			FatturaIva fatturaImpostaInMap = fattureIva.get(fatturaIva.getAliquota());
			fatturaImpostaInMap.setImponibile(fatturaImpostaInMap.getImponibile().doubleValue()
					+ fatturaIva.getImponibile().doubleValue());
			fatturaImpostaInMap.setImposta(fatturaImpostaInMap.getImposta().doubleValue()
					+ fatturaIva.getImposta().doubleValue());
			fattureIva.put(fatturaImpostaInMap.getAliquota(), fatturaImpostaInMap);
		} else {
			fattureIva.put(fatturaIva.getAliquota(), fatturaIva);
		}
	}

	/**
	 * @return Returns the codiceClienteAuVend.
	 */
	public String getCodiceClienteAuVend() {
		return codiceClienteAuVend;
	}

	/**
	 * @return Returns the codiceClientePanjea.
	 */
	public String getCodiceClientePanjea() {
		return codiceClientePanjea;
	}

	/**
	 * @return Returns the dataFattura.
	 */
	public Date getDataFattura() {
		return dataFattura;
	}

	/**
	 * @return Returns the imposte.
	 */
	public Map<Double, FatturaIva> getFattureIva() {
		return fattureIva;
	}

	/**
	 * @return Returns the numeroFattura.
	 */
	public int getNumeroFattura() {
		return numeroFattura;
	}

	/**
	 * @return Returns the numeroTerminale.
	 */
	public int getNumeroTerminale() {
		return numeroTerminale;
	}

	/**
	 * @return Returns the totaleFattura.
	 */
	public Double getTotaleFattura() {
		return totaleFattura;
	}

	/**
	 * @return Returns the totaleImponibile.
	 */
	public Double getTotaleImponibile() {
		return totaleImponibile;
	}

	/**
	 * @return Returns the totaleImposta.
	 */
	public Double getTotaleImposta() {
		return totaleImposta;
	}

	/**
	 * @param codiceClienteAuVend
	 *            The codiceClienteAuVend to set.
	 */
	public void setCodiceClienteAuVend(String codiceClienteAuVend) {
		this.codiceClienteAuVend = codiceClienteAuVend;
	}

	/**
	 * @param codiceClientePanjea
	 *            The codiceClientePanjea to set.
	 */
	public void setCodiceClientePanjea(String codiceClientePanjea) {
		this.codiceClientePanjea = codiceClientePanjea;
	}

	/**
	 * @param dataFattura
	 *            The dataFattura to set.
	 */
	public void setDataFattura(Date dataFattura) {
		this.dataFattura = dataFattura;
	}

	/**
	 * @param imposte
	 *            The imposte to set.
	 */
	public void setImposte(Map<Double, FatturaIva> imposte) {
		this.fattureIva = imposte;
	}

	/**
	 * @param numeroFattura
	 *            The numeroFattura to set.
	 */
	public void setNumeroFattura(int numeroFattura) {
		this.numeroFattura = numeroFattura;
	}

	/**
	 * @param numeroTerminale
	 *            The numeroTerminale to set.
	 */
	public void setNumeroTerminale(int numeroTerminale) {
		this.numeroTerminale = numeroTerminale;
	}

	/**
	 * @param totaleFattura
	 *            The totaleFattura to set.
	 */
	public void setTotaleFattura(Double totaleFattura) {
		this.totaleFattura = totaleFattura;
	}

	/**
	 * @param totaleImponibile
	 *            The totaleImponibile to set.
	 */
	public void setTotaleImponibile(Double totaleImponibile) {
		this.totaleImponibile = totaleImponibile;
	}

	/**
	 * @param totaleImposta
	 *            The totaleImposta to set.
	 */
	public void setTotaleImposta(Double totaleImposta) {
		this.totaleImposta = totaleImposta;
	}

	/**
	 * Constructs a <code>String</code> with all attributes in name = value format.
	 * 
	 * @return a <code>String</code> representation of this object.
	 */
	@Override
	public String toString() {

		StringBuffer retValue = new StringBuffer();

		retValue.append("FatturaTestata[ ").append(super.toString());
		retValue.append(" codiceClienteAuVend = ").append(this.codiceClienteAuVend);
		retValue.append(" codiceClientePanjea = ").append(this.codiceClientePanjea);
		retValue.append(" dataFattura = ").append(this.dataFattura);
		retValue.append(" numeroFattura = ").append(this.numeroFattura);
		retValue.append(" numeroTerminale = ").append(this.numeroTerminale);
		retValue.append(" totaleFattura = ").append(this.totaleFattura);
		retValue.append(" totaleImponibile = ").append(this.totaleImponibile);
		retValue.append(" totaleImposta = ").append(this.totaleImposta);
		retValue.append(" ]");

		return retValue.toString();
	}

}
