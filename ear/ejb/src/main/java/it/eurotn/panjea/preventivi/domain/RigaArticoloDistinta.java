package it.eurotn.panjea.preventivi.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.hibernate.envers.Audited;

@Entity(name = "RigaArticoloDistintaPreventivo")
@Audited
@DiscriminatorValue("D")
@NamedQueries({ @NamedQuery(name = "RigaArticoloDistintaPreventivo.caricaComponenti", query = "select rc from RigaArticoloComponenteOrdine rc join fetch rc.areaOrdine where rc.rigaDistintaCollegata.id=:idDistinta and rc.rigaPadre is null order by rc.articolo.codice") })
public class RigaArticoloDistinta extends RigaArticolo {

	private static final long serialVersionUID = 7457706694635960316L;

}
