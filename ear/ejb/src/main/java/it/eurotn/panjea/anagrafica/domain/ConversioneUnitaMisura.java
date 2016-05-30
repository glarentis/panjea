/**
 *
 */
package it.eurotn.panjea.anagrafica.domain;

import it.eurotn.entity.EntityBase;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.apache.log4j.Logger;
import org.hibernate.envers.Audited;

/**
 * @author leonardo
 */
@Entity
@Audited
@Table(name = "anag_formula_conversione_unita_misura")
@NamedQueries({
		@NamedQuery(name = "ConversioneUnitaMisura.caricaAll", query = "from ConversioneUnitaMisura where codiceAzienda=:paramCodiceAzienda"),
		@NamedQuery(name = "ConversioneUnitaMisura.caricaByUnitaMisuraOrigineDestinazione", query = "select c from ConversioneUnitaMisura c where c.unitaMisuraOrigine.codice=:paramCodiceUnitaMisuraOrigine and c.unitaMisuraDestinazione.codice=:paramCodiceUnitaMisuraDestinazione and c.codiceAzienda=:paramCodiceAzienda") })
public class ConversioneUnitaMisura extends EntityBase {

	private static Logger logger = Logger.getLogger(ConversioneUnitaMisura.class);
	private static final long serialVersionUID = -667848540753905258L;

	public static final String ORIGINAL_VALUE_VARIABLE = "$valoreOrigine$";

	@ManyToOne
	private UnitaMisura unitaMisuraOrigine;

	@ManyToOne
	private UnitaMisura unitaMisuraDestinazione;

	@Column(nullable = false)
	private String formula;

	@Column(nullable = false, length = 10)
	private String codiceAzienda;

	/**
	 * Costruttore.
	 */
	public ConversioneUnitaMisura() {
		super();
		init();
	}

	/**
	 * Converte il valore dall'unita' di misura di origine all' unita' di misura di destinazione, secondo la formula
	 * impostata con l'engine script specificato.
	 * 
	 * @param value
	 *            il valore da convertire
	 * @param numeroDecimali
	 *            il numero decimali da impostare
	 * @param engine
	 *            lo script engine con cui interpretare la formula
	 * 
	 * @return il valore convertito da unità di misura di origine a destinazione
	 */
	public Object converti(BigDecimal value, Integer numeroDecimali, ScriptEngine engine) {
		Object result = null;
		Bindings variabiliFormula = engine.createBindings();
		variabiliFormula.put("result", result);
		variabiliFormula.put(ORIGINAL_VALUE_VARIABLE, value);
		if (formula != null && formula.trim().length() > 0) {
			// controllo se la formula contiene la variabile "result"
			if (!formula.contains("result")) {
				formula = "result = " + formula;
			}
		}

		try {
			logger.debug("--> calcolo la formula " + formula);
			engine.eval(formula, variabiliFormula);
			Object resultObject = variabiliFormula.get("result");
			logger.debug("--> Exit calcola con risultato " + resultObject);
			if (resultObject instanceof BigDecimal) {
				result = ((BigDecimal) resultObject).setScale(numeroDecimali, RoundingMode.HALF_UP);
			} else {
				if (resultObject instanceof Double) {
					result = BigDecimal.valueOf((Double) resultObject).setScale(numeroDecimali, RoundingMode.HALF_UP);
				} else {
					if (resultObject instanceof Integer) {
						result = new BigDecimal((Integer) resultObject).setScale(numeroDecimali, RoundingMode.HALF_UP);
					}
				}
			}

		} catch (ScriptException se) {
			throw new RuntimeException(se.getMessage());
		}
		if (logger.isDebugEnabled()) {
			logger.debug("--> Exit calcola con risultato " + result);
		}
		return result;
	}

	/**
	 * @return the codiceAzienda
	 */
	public String getCodiceAzienda() {
		return codiceAzienda;
	}

	/**
	 * @return the formula
	 */
	public String getFormula() {
		return formula;
	}

	/**
	 * @return the unitaMisuraDestinazione
	 */
	public UnitaMisura getUnitaMisuraDestinazione() {
		return unitaMisuraDestinazione;
	}

	/**
	 * @return the unitaMisuraOrigine
	 */
	public UnitaMisura getUnitaMisuraOrigine() {
		return unitaMisuraOrigine;
	}

	/**
	 * Init delle proprietà.
	 */
	private void init() {
		this.unitaMisuraOrigine = new UnitaMisura();
		this.unitaMisuraDestinazione = new UnitaMisura();
	}

	/**
	 * @param codiceAzienda
	 *            the codiceAzienda to set
	 */
	public void setCodiceAzienda(String codiceAzienda) {
		this.codiceAzienda = codiceAzienda;
	}

	/**
	 * @param formula
	 *            the formula to set
	 */
	public void setFormula(String formula) {
		this.formula = formula;
	}

	/**
	 * @param unitaMisuraDestinazione
	 *            the unitaMisuraDestinazione to set
	 */
	public void setUnitaMisuraDestinazione(UnitaMisura unitaMisuraDestinazione) {
		this.unitaMisuraDestinazione = unitaMisuraDestinazione;
	}

	/**
	 * @param unitaMisuraOrigine
	 *            the unitaMisuraOrigine to set
	 */
	public void setUnitaMisuraOrigine(UnitaMisura unitaMisuraOrigine) {
		this.unitaMisuraOrigine = unitaMisuraOrigine;
	}

}
