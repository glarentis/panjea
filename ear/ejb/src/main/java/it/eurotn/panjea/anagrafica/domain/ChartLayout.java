package it.eurotn.panjea.anagrafica.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@DiscriminatorValue("C")
@NamedQueries({ @NamedQuery(name = "ChartLayout.caricaChartByKey", query = " from ChartLayout c where c.chiave = :paramKey and (c.utente = :paramUser or c.global = true) order by c.name") })
public class ChartLayout extends AbstractLayout {

	private static final long serialVersionUID = -4370370385255481221L;

}
