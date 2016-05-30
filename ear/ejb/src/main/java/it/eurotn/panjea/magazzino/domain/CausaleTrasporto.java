package it.eurotn.panjea.magazzino.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.intra.domain.NaturaTransazione;

/**
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Entity
@Audited
@Table(name = "maga_causali_trasporto")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "datiCommerciali")
public class CausaleTrasporto extends EntityBase {

    private static final long serialVersionUID = 8861053669238951245L;

    @Column(nullable = false, unique = true, length = 40)
    private String descrizione;

    @Enumerated
    private NaturaTransazione naturaTransazione;

    @Column(length = 10, nullable = false)
    @Index(name = "index_codiceAzienda")
    private String codiceAzienda = null;

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
     * @return the naturaTransazione
     */
    public NaturaTransazione getNaturaTransazione() {
        return naturaTransazione;
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
     * @param naturaTransazione
     *            the naturaTransazione to set
     */
    public void setNaturaTransazione(NaturaTransazione naturaTransazione) {
        this.naturaTransazione = naturaTransazione;
    }
}
