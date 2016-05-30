package it.eurotn.panjea.fatturepa.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;

@Entity
@Audited
@Table(name = "ftpa_area_magazzino_notifiche_fattura")
@NamedQueries({
        @NamedQuery(name = "AreaNotificaFatturaPA.deleteByIdAreaMagazzinoFatturaPa", query = "delete from AreaNotificaFatturaPA notPA where notPA.areaMagazzinoFatturaPA.id = :paramIdAreaMagazzinoFatturaPA") })
public class AreaNotificaFatturaPA extends EntityBase {

    private static final long serialVersionUID = -7399628433019728687L;

    @ManyToOne
    private AreaMagazzinoFatturaPA areaMagazzinoFatturaPA;

    @ManyToOne
    private NotificaFatturaPA notificaFatturaPA;

    /**
     * @return the areaMagazzinoFatturaPA
     */
    public AreaMagazzinoFatturaPA getAreaMagazzinoFatturaPA() {
        return areaMagazzinoFatturaPA;
    }

    /**
     * @return the notificaFatturaPA
     */
    public NotificaFatturaPA getNotificaFatturaPA() {
        return notificaFatturaPA;
    }

    /**
     * @param areaMagazzinoFatturaPA
     *            the areaMagazzinoFatturaPA to set
     */
    public void setAreaMagazzinoFatturaPA(AreaMagazzinoFatturaPA areaMagazzinoFatturaPA) {
        this.areaMagazzinoFatturaPA = areaMagazzinoFatturaPA;
    }

    /**
     * @param notificaFatturaPA
     *            the notificaFatturaPA to set
     */
    public void setNotificaFatturaPA(NotificaFatturaPA notificaFatturaPA) {
        this.notificaFatturaPA = notificaFatturaPA;
    }
}
