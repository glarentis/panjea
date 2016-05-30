package it.eurotn.panjea.magazzino.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;

/**
 *
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Entity
@Audited
@Table(name = "maga_traporto_cura")
@NamedQueries({
        @NamedQuery(name = "TrasportoCura.caricaByDescrizione", query = "select tc from TrasportoCura tc where tc.codiceAzienda=:paramCodiceAzienda and tc.descrizione like :paramDescrizione", hints = {
                @QueryHint(name = "org.hibernate.cacheable", value = "true"),
                @QueryHint(name = "org.hibernate.cacheRegion", value = "trasportoCura") }) })
public class TrasportoCura extends EntityBase {

    private static final long serialVersionUID = 1L;

    @Column(nullable = false, unique = true, length = 45)
    private String descrizione;

    @Column(length = 10, nullable = false)
    @Index(name = "index_codiceAzienda")
    private String codiceAzienda = null;

    private boolean mittente;

    /**
     * Costruttore.
     */
    public TrasportoCura() {
        super();
        this.mittente = false;
    }

    /**
     * @return codiceAzienda
     */
    public String getCodiceAzienda() {
        return codiceAzienda;
    }

    /**
     * @return descrizione
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * @return the mittente
     */
    public boolean isMittente() {
        return mittente;
    }

    /**
     * @param codiceAzienda
     *            the codiceAzienda to set
     */
    public void setCodiceAzienda(String codiceAzienda) {
        this.codiceAzienda = codiceAzienda;
    }

    /**
     * @param descrizione
     *            the descrizione to set
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * @param mittente
     *            the mittente to set
     */
    public void setMittente(boolean mittente) {
        this.mittente = mittente;
    }
}
