package it.eurotn.panjea.contabilita.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 
 * Descrive lo stile e le condizioni con il quale devo visualizzare il sottoconto nell'estratto conto.
 * 
 * @author giangi
 * @version 1.0, 01/apr/2011
 * 
 */
@Embeddable
public class StileSottoConto implements Serializable {
	private static final long serialVersionUID = -2495771018845530544L;
	public static final String MAGGIORE = ">";
	public static final String MAGGIORE_UGUALE = ">=";
	public static final String MINORE = "<";
	public static final String MINOREUGUALE = "<=";
	public static final String UGUALE = "=";
	public static final String[] ALL_CONDITION = new String[] { UGUALE, MAGGIORE, MAGGIORE_UGUALE, MINORE, MINOREUGUALE };

	@Column(length = 10)
	private String backGroundColor = "";
	@Column(length = 2)
	private String condizione = UGUALE;
	private BigDecimal importo;
	private boolean abilitato = false;

	/**
	 * @return Returns the backGroundColor.
	 */
	public String getBackGroundColor() {
		return backGroundColor;
	}

	/**
	 * @return Returns the condition.
	 */
	public String getCondizione() {
		return condizione;
	}

	/**
	 * @return Returns the importo.
	 */
	public BigDecimal getImporto() {
		return importo;
	}

	/**
	 * @return Returns the abilitato.
	 */
	public boolean isAbilitato() {
		return abilitato;
	}

	/**
	 * @param abilitato
	 *            The abilitato to set.
	 */
	public void setAbilitato(boolean abilitato) {
		this.abilitato = abilitato;
	}

	/**
	 * @param backGroundColor
	 *            The backGroundColor to set.
	 */
	public void setBackGroundColor(String backGroundColor) {
		this.backGroundColor = backGroundColor;
	}

	/**
	 * @param condition
	 *            The condition to set.
	 */
	public void setCondizione(String condition) {
		this.condizione = condition;
	}

	/**
	 * @param importo
	 *            The importo to set.
	 */
	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}

}
