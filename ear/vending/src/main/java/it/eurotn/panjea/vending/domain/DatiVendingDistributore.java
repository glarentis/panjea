package it.eurotn.panjea.vending.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;

@Entity
@Audited
@Table(name = "vend_dati_vending_distributore")
public class DatiVendingDistributore extends EntityBase {

    private static final long serialVersionUID = -6616840219381756370L;

    @ManyToOne
    private Modello modello;

    private boolean cassettaAbitata;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "datiVendingDistributore", cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<ProdottoDistributore> prodotti;

    @Column(length = 20)
    private String matricolaFornitore;

    @Column(length = 30)
    private String idEvaDts;

    @ManyToOne(fetch = FetchType.LAZY)
    private SistemaElettronico gettoniera;

    @ManyToOne(fetch = FetchType.LAZY)
    private SistemaElettronico lettoreBanconote;

    @ManyToOne(fetch = FetchType.LAZY)
    private SistemaElettronico sistemaPagamento;

    /**
     * @return the gettoniera
     */
    public SistemaElettronico getGettoniera() {
        return gettoniera;
    }

    /**
     * @return the idEvaDts
     */
    public String getIdEvaDts() {
        return idEvaDts;
    }

    /**
     * @return the lettoreBanconote
     */
    public SistemaElettronico getLettoreBanconote() {
        return lettoreBanconote;
    }

    /**
     * @return the matricolaFornitore
     */
    public String getMatricolaFornitore() {
        return matricolaFornitore;
    }

    /**
     * @return the modello
     */
    public Modello getModello() {
        return modello;
    }

    /**
     * @return the prodotti
     */
    public final List<ProdottoDistributore> getProdotti() {
        return prodotti;
    }

    /**
     * @return the sistemaPagamento
     */
    public SistemaElettronico getSistemaPagamento() {
        return sistemaPagamento;
    }

    /**
     * @return the cassettaAbitata
     */
    public boolean isCassettaAbitata() {
        return cassettaAbitata;
    }

    /**
     * @param cassettaAbitata
     *            the cassettaAbitata to set
     */
    public void setCassettaAbitata(boolean cassettaAbitata) {
        this.cassettaAbitata = cassettaAbitata;
    }

    /**
     * @param gettoniera
     *            the gettoniera to set
     */
    public void setGettoniera(SistemaElettronico gettoniera) {
        this.gettoniera = gettoniera;
    }

    /**
     * @param idEvaDts
     *            the idEvaDts to set
     */
    public void setIdEvaDts(String idEvaDts) {
        this.idEvaDts = idEvaDts;
    }

    /**
     * @param lettoreBanconote
     *            the lettoreBanconote to set
     */
    public void setLettoreBanconote(SistemaElettronico lettoreBanconote) {
        this.lettoreBanconote = lettoreBanconote;
    }

    /**
     * @param matricolaFornitore
     *            the matricolaFornitore to set
     */
    public void setMatricolaFornitore(String matricolaFornitore) {
        this.matricolaFornitore = matricolaFornitore;
    }

    /**
     * @param modello
     *            the modello to set
     */
    public void setModello(Modello modello) {
        this.modello = modello;
    }

    /**
     * @param prodotti
     *            the prodotti to set
     */
    public final void setProdotti(List<ProdottoDistributore> prodotti) {
        this.prodotti = prodotti;
    }

    /**
     * @param sistemaPagamento
     *            the sistemaPagamento to set
     */
    public void setSistemaPagamento(SistemaElettronico sistemaPagamento) {
        this.sistemaPagamento = sistemaPagamento;
    }
}
