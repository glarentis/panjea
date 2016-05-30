package it.eurotn.panjea.manutenzioni.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;

@SuppressWarnings("serial")
@Entity
@Audited
@Table(name = "manu_settings")
public class ManutenzioneSettings extends EntityBase {

    @ManyToOne
    private UbicazioneInstallazione ubicazionePredefinita;

    /**
     * @return the ubicazionePredefinita
     */
    public UbicazioneInstallazione getUbicazionePredefinita() {
        return ubicazionePredefinita;
    }

    /**
     * @param ubicazionePredefinita
     *            the ubicazionePredefinita to set
     */
    public void setUbicazionePredefinita(UbicazioneInstallazione ubicazionePredefinita) {
        this.ubicazionePredefinita = ubicazionePredefinita;
    }

}