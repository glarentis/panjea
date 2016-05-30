package it.eurotn.panjea.manutenzioni.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;

@SuppressWarnings("serial")
@Entity
@Audited
@Table(name = "manu_ubicazioni_installazione")
public class UbicazioneInstallazione extends EntityBase {

    @Column(length = 300)
    private String descrizione;

    /**
     * @return the descrizione
     */
    public final String getDescrizione() {
        return descrizione;
    }

    /**
     * @param descrizione
     *            the descrizione to set
     */
    public final void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

}