package it.eurotn.panjea.manutenzioni.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

import it.eurotn.panjea.magazzino.domain.Articolo;

@Entity
@Audited
@DiscriminatorValue("MI")
public class ArticoloMI extends Articolo {

    private static final long serialVersionUID = -3122812955193162854L;

    @Transient
    private Installazione installazione;

    @Column(columnDefinition = "bit default 0")
    private boolean proprietaCliente;

    {
        this.numeroDecimaliQta = 0;
        proprietaCliente = false;
    }

    /**
     * @return Returns the installazione.
     */
    public Installazione getInstallazione() {
        return installazione;
    }

    /**
     * @return the proprietaCliente
     */
    public final boolean isProprietaCliente() {
        return proprietaCliente;
    }

    /**
     * @param installazione
     *            The installazione to set.
     */
    public void setInstallazione(Installazione installazione) {
        this.installazione = installazione;
    }

    /**
     * @param proprietaCliente
     *            the proprietaCliente to set
     */
    public final void setProprietaCliente(boolean proprietaCliente) {
        this.proprietaCliente = proprietaCliente;
    }

}
