package it.eurotn.panjea.magazzino.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;

/**
 * TipoMezzoTrasporto definisce il tipo mezzo di un mezzo trasporto.
 *
 * @author Leonardo
 */
@Entity
@Audited
@Table(name = "maga_tipi_mezzo_trasporto", uniqueConstraints = @UniqueConstraint(columnNames = { "codiceAzienda",
        "codice" }) )
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "datiCommerciali")
@NamedQuery(name = "TipoMezzoTrasporto.caricaAll", query = "select tmt from TipoMezzoTrasporto tmt where tmt.codiceAzienda=:paramCodiceAzienda order by codice")
public class TipoMezzoTrasporto extends EntityBase {

    private static final long serialVersionUID = -1534858317513280639L;

    @Column(length = 10, nullable = false)
    private String codice;

    @Column(length = 10, nullable = false)
    @Index(name = "index_codiceAzienda")
    private String codiceAzienda;

    @Column(length = 30, nullable = false)
    private String descrizione;

    @ManyToOne
    @JoinColumn(name = "articolo_id")
    private ArticoloLite articolo;

    /**
     *
     */
    public TipoMezzoTrasporto() {
        super();
    }

    /**
     * @return the articolo
     */
    public ArticoloLite getArticolo() {
        return articolo;
    }

    /**
     * @return the codice
     */
    public String getCodice() {
        return codice;
    }

    /**
     * @return the codiceAzienda
     */
    public String getCodiceAzienda() {
        return codiceAzienda;
    }

    /**
     * @return the descrizione
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * @param articolo
     *            the articolo to set
     */
    public void setArticolo(ArticoloLite articolo) {
        this.articolo = articolo;
    }

    /**
     * @param codice
     *            the codice to set
     */
    public void setCodice(String codice) {
        this.codice = codice;
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

}
