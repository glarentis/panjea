/**
 * 
 */
package it.eurotn.panjea.tesoreria.domain;

import java.util.Comparator;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.hibernate.envers.Audited;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GroupingList;

/**
 * Classe responsabile degli effetti.
 * 
 * @author vittorio
 * @version 1.0, 26/nov/2009
 */
@SuppressWarnings("unchecked")
@Entity
@Audited
@DiscriminatorValue("EF")
@NamedQueries({ @NamedQuery(name = "AreaEffetti.carica", query = "select distinct ae from AreaEffetti ae inner join fetch ae.effetti effs inner join fetch effs.pagamenti pags inner join fetch pags.rata rata inner join fetch rata.areaRate ar inner join fetch ar.documento doc inner join fetch doc.entita ent inner join fetch ent.anagrafica anag inner join fetch anag.sedeAnagrafica sa where ae.id = :paramIdAreaEffetti") })
public class AreaEffetti extends AreaChiusure {

	/**
	 * @author giangi
	 * @version 1.0, 10/nov/2010
	 */
	public enum RaggruppamentoEffetti {
		DATA_VALUTA, DATA_SCADENZA
	}

	private static final Comparator<Effetto>[] RAGGRUPPAMENTI_COMPARATOR = new Comparator[] {
			new DataValutaEffettoComparator(), new DataScadenzaEffettoComparator() };
	private static final long serialVersionUID = 5532930682389092949L;

	@OneToMany(mappedBy = "areaEffetti", fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE })
	@OrderBy(value = "dataValuta")
	private Set<Effetto> effetti;

	/**
	 * @return the effetti
	 */
	public Set<Effetto> getEffetti() {
		return effetti;
	}

	/**
	 * @param raggruppamentoEffetti
	 *            raggruppamento da utilizzare
	 * @return effetti raggruppati per raggruppamentoEffetti
	 */
	public GroupingList<Effetto> getEffettiRaggrupati(RaggruppamentoEffetti raggruppamentoEffetti) {
		EventList<Effetto> eventList = new BasicEventList<Effetto>();
		eventList.addAll(getEffetti());
		return new GroupingList<Effetto>(eventList, RAGGRUPPAMENTI_COMPARATOR[raggruppamentoEffetti.ordinal()]);
	}

}
