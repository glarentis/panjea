/**
 *
 */
package it.eurotn.panjea.anagrafica.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.entity.annotation.EntityConverter;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Cap;

/**
 * Classe di dominio di Filiale.
 *
 * @author adriano
 * @version 1.0, 15/dic/07
 */
@Entity
@Audited
@Table(name = "anag_filiali", uniqueConstraints = { @UniqueConstraint(columnNames = { "banca_id", "codice" }) })
@NamedQueries({
        @NamedQuery(name = "Filiale.caricaAll", query = " from Filiale f left join fetch f.cap inner join fetch f.banca ") })
@EntityConverter(properties = "codice,descrizione")
public class Filiale extends EntityBase {

    private static final long serialVersionUID = 1628503777630517647L;
    public static final String REF = "Filiale";
    public static final String PROP_CODICE = "codice";
    public static final String PROP_BANCA = "banca";
    public static final String PROP_ID = "id";
    public static final String PROP_DESCRIZIONE = "descrizione";
    public static final String PROP_INDIRIZZO = "indirizzo";

    @Column(name = "codice", length = 5, nullable = false)
    @Index(name = "codice_idx")
    private String codice;

    @Column(length = 200, nullable = false)
    @Index(name = "descrizione")
    private String descrizione;

    @ManyToOne(optional = false)
    @JoinColumn(name = "banca_id")
    private Banca banca;

    @Column(length = 200)
    private String indirizzo;

    @ManyToOne
    @JoinColumn(name = "cap_id")
    private Cap cap;

    /**
     * Costruttore.
     * 
     */
    public Filiale() {
        super();
    }

    /**
     * @return Returns the banca.
     */
    public Banca getBanca() {
        return banca;
    }

    /**
     * @return cap
     */
    public Cap getCap() {
        return cap;
    }

    /**
     * @return Returns the codice.
     */
    public String getCodice() {
        return codice;
    }

    /**
     * @return Returns the descrizione.
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * @return indirizzo
     */
    public String getIndirizzo() {
        return indirizzo;
    }

    /**
     * @param banca
     *            The banca to set.
     */
    public void setBanca(Banca banca) {
        this.banca = banca;
    }

    /**
     * @param cap
     *            the cap to set
     */
    public void setCap(Cap cap) {
        this.cap = cap;
    }

    /**
     * @param codice
     *            The codice to set.
     */
    public void setCodice(String codice) {
        this.codice = codice;
    }

    /**
     * @param descrizione
     *            The descrizione to set.
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * @param indirizzo
     *            the indirizzo to set
     */
    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Filiale[");
        buffer.append(super.toString());
        buffer.append(" banca = ").append(banca != null ? banca.getId() : null);
        buffer.append(" codice = ").append(codice);
        buffer.append(" descrizione = ").append(descrizione);
        buffer.append("]");
        return buffer.toString();
    }
}
