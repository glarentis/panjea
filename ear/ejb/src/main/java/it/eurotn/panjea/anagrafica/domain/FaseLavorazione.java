/**
 *
 */
package it.eurotn.panjea.anagrafica.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;

/**
 * @author leonardo
 */
@Entity
@Audited
@Table(name = "maga_fasi_lavorazione", uniqueConstraints = @UniqueConstraint(columnNames = { "codiceazienda",
        "codice" }) )
@NamedQuery(name = "FaseLavorazione.caricaAll", query = "select fl from FaseLavorazione fl where fl.codiceAzienda=:paramCodiceAzienda order by fl.codice")
public class FaseLavorazione extends EntityBase {

    private static final long serialVersionUID = 7019806405694771169L;

    @Column(length = 100, nullable = false)
    private String codice;

    @Column(length = 200)
    private String descrizione;

    @Column(length = 10, nullable = false)
    @Index(name = "index_codiceAzienda")
    private String codiceAzienda = null;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "faseLavorazione")
    private Set<FaseLavorazioneArticolo> fasiArticolo;

    private Integer ordinamento;

    {
        ordinamento = 0;
    }

    /**
     * Costruttore.
     */
    public FaseLavorazione() {
        super();
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

    public Set<FaseLavorazioneArticolo> getFasiArticolo() {
        return fasiArticolo;
    }

    /**
     * @return the ordinamento
     */
    public Integer getOrdinamento() {
        return ordinamento;
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

    public void setFasiArticolo(Set<FaseLavorazioneArticolo> fasiArticolo) {
        this.fasiArticolo = fasiArticolo;
    }

    /**
     * @param ordinamento
     *            the ordinamento to set
     */
    public void setOrdinamento(Integer ordinamento) {
        this.ordinamento = ordinamento;
    }

    @Override
    public String toString() {
        return "FaseLavorazione [codice=" + codice + ", descrizione=" + descrizione + "]";
    }

}
