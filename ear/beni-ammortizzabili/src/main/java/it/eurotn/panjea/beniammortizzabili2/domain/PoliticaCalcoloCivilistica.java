/**
 * 
 */
package it.eurotn.panjea.beniammortizzabili2.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

/**
 * @author Leonardo
 * 
 */
@Embeddable
public class PoliticaCalcoloCivilistica implements Serializable {

	private static final long serialVersionUID = 5127029871282730935L;

	public static final String REF = "PoliticaCalcoloCivilistica";
	public static final String PROP_AMMORTAMENTO_ORDINARIO = "ammortamentoOrdinario";
	public static final String PROP_PERC_AMMORTAMENTO_ORDINARIO = "percAmmortamentoOrdinario";
	public static final String PROP_MAGGIORE_UTILIZZO = "maggioreUtilizzo";
	public static final String PROP_PERC_MAGGIORE_UTILIZZO = "percMaggioreUtilizzo";
	public static final String PROP_MINORE_UTILIZZO = "minoreUtilizzo";
	public static final String PROP_PERC_MINORE_UTILIZZO = "percMinoreUtilizzo";
	public static final String PROP_TOTALE_ORDINARIO = "totaleOrdinario";
	public static final String PROP_TOTALE_ANTICIPATO = "totaleAnticipato";

	@Column(name = "ammortamento_ordinario_civ")
	private boolean ammortamentoOrdinario = false;
	@Column(name = "maggiore_utilizzo_civ")
	private boolean maggioreUtilizzo = false;
	@Column(name = "minore_utilizzo_civ")
	private boolean minoreUtilizzo = false;

	@Column(name = "perc_ammortamento_ordinario_civ")
	private double percAmmortamentoOrdinario;
	@Column(name = "perc_maggiore_utilizzo_civ")
	private double percMaggioreUtilizzo;
	@Column(name = "perc_minore_utilizzo_civ")
	private double percMinoreUtilizzo;

	@Transient
	private BigDecimal totaleOrdinario;
	@Transient
	private BigDecimal totaleAnticipato;

	/**
	 * Costruttore di default.
	 */
	public PoliticaCalcoloCivilistica() {
		initialize();
	}

	/**
	 * @return the percAmmortamentoOrdinario
	 */
	public double getPercAmmortamentoOrdinario() {
		return percAmmortamentoOrdinario;
	}

	/**
	 * @return the percMaggioreUtilizzo
	 */
	public double getPercMaggioreUtilizzo() {
		return percMaggioreUtilizzo;
	}

	/**
	 * @return the percMinoreUtilizzo
	 */
	public double getPercMinoreUtilizzo() {
		return percMinoreUtilizzo;
	}

	/**
	 * @return the totaleAnticipato
	 */
	public BigDecimal getTotaleAnticipato() {
		return totaleAnticipato;
	}

	/**
	 * @return the totaleOrdinario
	 */
	public BigDecimal getTotaleOrdinario() {
		return totaleOrdinario;
	}

	/**
	 * Inizializza i valori delle variabili dell'istanza.
	 */
	private void initialize() {
		percAmmortamentoOrdinario = 0;
		percMaggioreUtilizzo = 0;
		percMinoreUtilizzo = 0;
		totaleOrdinario = BigDecimal.ZERO;
		totaleAnticipato = BigDecimal.ZERO;
	}

	/**
	 * @return the ammortamentoOrdinario
	 */
	public boolean isAmmortamentoOrdinario() {
		return ammortamentoOrdinario;
	}

	/**
	 * @return the maggioreUtilizzo
	 */
	public boolean isMaggioreUtilizzo() {
		return maggioreUtilizzo;
	}

	/**
	 * @return the minoreUtilizzo
	 */
	public boolean isMinoreUtilizzo() {
		return minoreUtilizzo;
	}

	/**
	 * @param ammortamentoOrdinario
	 *            the ammortamentoOrdinario to set
	 */
	public void setAmmortamentoOrdinario(boolean ammortamentoOrdinario) {
		this.ammortamentoOrdinario = ammortamentoOrdinario;
	}

	/**
	 * @param maggioreUtilizzo
	 *            the maggioreUtilizzo to set
	 */
	public void setMaggioreUtilizzo(boolean maggioreUtilizzo) {
		this.maggioreUtilizzo = maggioreUtilizzo;
	}

	/**
	 * @param minoreUtilizzo
	 *            the minoreUtilizzo to set
	 */
	public void setMinoreUtilizzo(boolean minoreUtilizzo) {
		this.minoreUtilizzo = minoreUtilizzo;
	}

	/**
	 * @param percAmmortamentoOrdinario
	 *            the percAmmortamentoOrdinario to set
	 */
	public void setPercAmmortamentoOrdinario(double percAmmortamentoOrdinario) {
		this.percAmmortamentoOrdinario = percAmmortamentoOrdinario;
	}

	/**
	 * @param percMaggioreUtilizzo
	 *            the percMaggioreUtilizzo to set
	 */
	public void setPercMaggioreUtilizzo(double percMaggioreUtilizzo) {
		this.percMaggioreUtilizzo = percMaggioreUtilizzo;
	}

	/**
	 * @param percMinoreUtilizzo
	 *            the percMinoreUtilizzo to set
	 */
	public void setPercMinoreUtilizzo(double percMinoreUtilizzo) {
		this.percMinoreUtilizzo = percMinoreUtilizzo;
	}

	/**
	 * @param totaleAnticipato
	 *            the totaleAnticipato to set
	 */
	public void setTotaleAnticipato(BigDecimal totaleAnticipato) {
		this.totaleAnticipato = totaleAnticipato;
	}

	/**
	 * @param totaleOrdinario
	 *            the totaleOrdinario to set
	 */
	public void setTotaleOrdinario(BigDecimal totaleOrdinario) {
		this.totaleOrdinario = totaleOrdinario;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("PoliticaCalcoloCivilistica[");
		buffer.append("ammortamentoOrdinario = ").append(ammortamentoOrdinario);
		buffer.append(" maggioreUtilizzo = ").append(maggioreUtilizzo);
		buffer.append(" minoreUtilizzo = ").append(minoreUtilizzo);
		buffer.append(" percAmmortamentoOrdinario = ").append(percAmmortamentoOrdinario);
		buffer.append(" percMaggioreUtilizzo = ").append(percMaggioreUtilizzo);
		buffer.append(" percMinoreUtilizzo = ").append(percMinoreUtilizzo);
		buffer.append("]");
		return buffer.toString();
	}

}
