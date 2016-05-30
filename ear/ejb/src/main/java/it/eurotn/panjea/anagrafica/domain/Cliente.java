package it.eurotn.panjea.anagrafica.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.audit.envers.AuditableProperties;

@Entity
@Audited
@DiscriminatorValue("C")
@AuditableProperties(properties = { "anagrafica", "anagrafica.sedeAnagrafica" })
public class Cliente extends Entita {

    private static final long serialVersionUID = -699663566604282962L;

    /**
     * @uml.property name="ordine"
     */
    private static int ordine = 1;

    @Column(columnDefinition = "bit default 0")
    private boolean fatturaPalmare;

    {
        fatturaPalmare = false;
    }

    /**
     *
     */
    public Cliente() {
        super();
    }

    /**
     * @return ordine di visualizzazione nella ricerca entità
     */
    @Override
    public int getOrdine() {
        return ordine;
    }

    @Override
    public TipoEntita getTipo() {
        return TipoEntita.CLIENTE;
    }

    /**
     * @return the fatturaPalmare
     */
    public boolean isFatturaPalmare() {
        return fatturaPalmare;
    }

    /**
     * @param fatturaPalmare
     *            the fatturaPalmare to set
     */
    public void setFatturaPalmare(boolean fatturaPalmare) {
        this.fatturaPalmare = fatturaPalmare;
    }

}