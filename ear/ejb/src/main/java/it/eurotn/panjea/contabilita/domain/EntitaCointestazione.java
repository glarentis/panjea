package it.eurotn.panjea.contabilita.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;

@SuppressWarnings("serial")
@Entity
@Audited
@Table(name = "cont_entita_cointestazione")
@NamedQueries({
        @NamedQuery(name = "EntitaCointestazione.caricaByAreaContabile", query = "select distinct ec from EntitaCointestazione ec where ec.areaContabile.id = :paramIdAreaContabile "),
        @NamedQuery(name = "EntitaCointestazione.eliminaByAreaContabile", query = "delete from EntitaCointestazione ec where ec.areaContabile.id = :paramIdAreaContabile ") })
public class EntitaCointestazione extends EntityBase {

    @ManyToOne
    private EntitaLite entita;

    @ManyToOne
    private AreaContabile areaContabile;

    /**
     * @return the areaContabile
     */
    public AreaContabile getAreaContabile() {
        return areaContabile;
    }

    /**
     * @return the entita
     */
    public EntitaLite getEntita() {
        return entita;
    }

    /**
     * @param areaContabile
     *            the areaContabile to set
     */
    public void setAreaContabile(AreaContabile areaContabile) {
        this.areaContabile = areaContabile;
    }

    /**
     * @param entita
     *            the entita to set
     */
    public void setEntita(EntitaLite entita) {
        this.entita = entita;
    }
}