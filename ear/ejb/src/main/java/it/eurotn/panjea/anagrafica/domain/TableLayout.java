package it.eurotn.panjea.anagrafica.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@DiscriminatorValue("T")
@NamedQueries({ @NamedQuery(name = "TableLayout.caricaTableByKey", query = " from TableLayout a where a.chiave = :paramKey and (a.utente = :paramUser or a.global = true) order by a.name") })
public class TableLayout extends AbstractLayout {

	private static final long serialVersionUID = -1971775667404949350L;

	private Boolean visualizzaNumeriRiga;
	private Boolean estendiColonne;

	/**
	 * @return Returns the estendiColonne.
	 */
	public Boolean getEstendiColonne() {
		if (estendiColonne == null) {
			return false;
		}
		return estendiColonne;
	}

	/**
	 * @return Returns the visualizzaNumeriRiga.
	 */
	public Boolean getVisualizzaNumeriRiga() {
		if (visualizzaNumeriRiga == null) {
			return true;
		}
		return visualizzaNumeriRiga;
	}

	/**
	 * @param estendiColonne
	 *            The estendiColonne to set.
	 */
	public void setEstendiColonne(Boolean estendiColonne) {
		this.estendiColonne = estendiColonne;
	}

	/**
	 * @param visualizzaNumeriRiga
	 *            The visualizzaNumeriRiga to set.
	 */
	public void setVisualizzaNumeriRiga(Boolean visualizzaNumeriRiga) {
		this.visualizzaNumeriRiga = visualizzaNumeriRiga;
	}

}
