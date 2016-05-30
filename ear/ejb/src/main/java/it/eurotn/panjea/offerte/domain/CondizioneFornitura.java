package it.eurotn.panjea.offerte.domain;

import java.io.Serializable;

import javax.persistence.Embeddable;

/**
 * 
 * @author Leonardo
 */
@Embeddable
public class CondizioneFornitura implements Serializable {

	private static final long serialVersionUID = 8099883208831856520L;
	private Integer validitaOfferta;
	private String iva;
	private String resa;
	private Integer consegna;
	private String imballo;
	private String pagamento;
	private Integer garanzia;
	private String caratteristicheTecniche;
	private String pagamentiDiversi;

	/**
	 * 
	 */
	public CondizioneFornitura() {
		super();
		initialize();
	}

	/**
	 * @return the caratteristicheTecniche
	 */
	public String getCaratteristicheTecniche() {
		return caratteristicheTecniche;
	}

	/**
	 * @return the consegna
	 */
	public Integer getConsegna() {
		return consegna;
	}

	/**
	 * @return the garanzia
	 */
	public Integer getGaranzia() {
		return garanzia;
	}

	/**
	 * @return the imballo
	 */
	public String getImballo() {
		return imballo;
	}

	/**
	 * @return the iva
	 */
	public String getIva() {
		return iva;
	}

	/**
	 * @return the pagamentiDiversi
	 */
	public String getPagamentiDiversi() {
		return pagamentiDiversi;
	}

	/**
	 * @return the pagamento
	 */
	public String getPagamento() {
		return pagamento;
	}

	/**
	 * @return the resa
	 */
	public String getResa() {
		return resa;
	}

	/**
	 * @return the validitaOfferta
	 */
	public Integer getValiditaOfferta() {
		return validitaOfferta;
	}

	/**
	 * Inizializza i valori di default.
	 */
	private void initialize() {
		this.iva = "A Vs. carico";
		this.validitaOfferta = 30;
		this.resa = "Franco Vs. magazzino";
		this.imballo = "Incluso";
		this.caratteristicheTecniche = "Come da specifiche tecniche allegate";
		this.pagamentiDiversi = "Per ordini emessi a mezzo societa' di leasing,per motivi amministrativi, lo stesso non potra' da noi essere processato, fino al ricevimento dell'ordine ufficiale del leasing stesso";
	}

	/**
	 * @param caratteristicheTecniche
	 *            the caratteristicheTecniche to set
	 */
	public void setCaratteristicheTecniche(String caratteristicheTecniche) {
		this.caratteristicheTecniche = caratteristicheTecniche;
	}

	/**
	 * @param consegna
	 *            the consegna to set
	 */
	public void setConsegna(Integer consegna) {
		this.consegna = consegna;
	}

	/**
	 * @param garanzia
	 *            the garanzia to set
	 */
	public void setGaranzia(Integer garanzia) {
		this.garanzia = garanzia;
	}

	/**
	 * @param imballo
	 *            the imballo to set
	 */
	public void setImballo(String imballo) {
		this.imballo = imballo;
	}

	/**
	 * @param iva
	 *            the iva to set
	 */
	public void setIva(String iva) {
		this.iva = iva;
	}

	/**
	 * @param pagamentiDiversi
	 *            the pagamentiDiversi to set
	 */
	public void setPagamentiDiversi(String pagamentiDiversi) {
		this.pagamentiDiversi = pagamentiDiversi;
	}

	/**
	 * @param pagamento
	 *            the pagamento to set
	 */
	public void setPagamento(String pagamento) {
		this.pagamento = pagamento;
	}

	/**
	 * @param resa
	 *            the resa to set
	 */
	public void setResa(String resa) {
		this.resa = resa;
	}

	/**
	 * @param validitaOfferta
	 *            the validitaOfferta to set
	 */
	public void setValiditaOfferta(Integer validitaOfferta) {
		this.validitaOfferta = validitaOfferta;
	}

}
