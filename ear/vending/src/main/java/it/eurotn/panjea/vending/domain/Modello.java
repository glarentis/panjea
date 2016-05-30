package it.eurotn.panjea.vending.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;

@Entity
@Audited
@Table(name = "vend_modelli")
@NamedQueries({ @NamedQuery(name = "Modello.caricaAll", query = "from Modello mod ") })
public class Modello extends EntityBase {

    private static final long serialVersionUID = 8351024740908911095L;

    @Column(length = 10)
    private String codice;

    @Column(length = 50)
    private String descrizione;

    @ManyToOne
    private TipoModello tipoModello;

    private boolean cassettaPresente;

    private boolean obbligoLetturaCassetta;

    private boolean ritiroCassetta;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "modello", cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<ProdottoModello> prodotti;

    /**
     * @return the codice
     */
    public String getCodice() {
        return codice;
    }

    /**
     * @return the descrizione
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * @return the prodotti
     */
    public final List<ProdottoModello> getProdotti() {
        return prodotti;
    }

    /**
     * @return the tipoModello
     */
    public TipoModello getTipoModello() {
        return tipoModello;
    }

    /**
     * @return the cassettaPresente
     */
    public boolean isCassettaPresente() {
        return cassettaPresente;
    }

    /**
     * @return the obbligoLetturaCassetta
     */
    public boolean isObbligoLetturaCassetta() {
        return obbligoLetturaCassetta;
    }

    /**
     * @return the ritiroCassetta
     */
    public boolean isRitiroCassetta() {
        return ritiroCassetta;
    }

    /**
     * @param cassettaPresente
     *            the cassettaPresente to set
     */
    public void setCassettaPresente(boolean cassettaPresente) {
        this.cassettaPresente = cassettaPresente;
    }

    /**
     * @param codice
     *            the codice to set
     */
    public void setCodice(String codice) {
        this.codice = codice;
    }

    /**
     * @param descrizione
     *            the descrizione to set
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * @param obbligoLetturaCassetta
     *            the obbligoLetturaCassetta to set
     */
    public void setObbligoLetturaCassetta(boolean obbligoLetturaCassetta) {
        this.obbligoLetturaCassetta = obbligoLetturaCassetta;
    }

    /**
     * @param prodotti
     *            the prodotti to set
     */
    public final void setProdotti(List<ProdottoModello> prodotti) {
        this.prodotti = prodotti;
    }

    /**
     * @param ritiroCassetta
     *            the ritiroCassetta to set
     */
    public void setRitiroCassetta(boolean ritiroCassetta) {
        this.ritiroCassetta = ritiroCassetta;
    }

    /**
     * @param tipoModello
     *            the tipoModello to set
     */
    public void setTipoModello(TipoModello tipoModello) {
        this.tipoModello = tipoModello;
    }
}
