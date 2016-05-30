package it.eurotn.panjea.partite.domain;

import it.eurotn.entity.EntityBase;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

/**
 * 
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Entity
@Audited
@Table(name = "part_strategia_calcolo_partita")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO_CALCOLO", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("A")
public abstract class StrategiaCalcoloPartita extends EntityBase {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 1L;

	// Utilizzata per visualizzare nella gui il tipo di stategia
	public static final String FORMULA = "strategiaCalcoloPartitaFormula";
	public static final String CONTI = "strategiaCalcoloPartitaConti";
	// L'ORDINE NON DEVE ESSERE MODIFICATO
	/**
	 * @uml.property name="sTRATEGIE"
	 * @uml.associationEnd multiplicity="(0 -1)"
	 */
	public static final StrategiaCalcoloPartita[] STRATEGIE = { new StrategiaCalcoloPartitaFormula(),
			new StrategiaCalcoloPartitaConti() };

	/**
	 * @return codice della strategia
	 */
	public abstract String getCodiceStrategia();
}
