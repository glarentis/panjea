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
@Table(name = "vend_tipi_modello")
@NamedQueries({ @NamedQuery(name = "TipoModello.caricaAll", query = "from TipoModello tm ") })
public class TipoModello extends EntityBase {

    private static final long serialVersionUID = -6144072214787707516L;

    @Column(length = 10)
    private String codice;

    @Column(length = 50)
    private String descrizione;

    @ManyToOne
    private TipoComunicazione tipoComunicazione;

    private boolean caldo;

    private boolean gelato;

    private boolean freddo;

    private boolean snack;

    private boolean snackRefrigerati;

    private boolean kit;

    private boolean acqua;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tipoModello", cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<ProdottoTipoModello> prodotti;

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
    public final List<ProdottoTipoModello> getProdotti() {
        return prodotti;
    }

    /**
     * @return the tipoComunicazione
     */
    public TipoComunicazione getTipoComunicazione() {
        return tipoComunicazione;
    }

    /**
     * @return the acqua
     */
    public boolean isAcqua() {
        return acqua;
    }

    /**
     * @return the caldo
     */
    public boolean isCaldo() {
        return caldo;
    }

    /**
     * @return the freddo
     */
    public boolean isFreddo() {
        return freddo;
    }

    /**
     * @return Returns the gelato.
     */
    public boolean isGelato() {
        return gelato;
    }

    /**
     * @return the kit
     */
    public boolean isKit() {
        return kit;
    }

    /**
     * @return the snack
     */
    public boolean isSnack() {
        return snack;
    }

    /**
     * @return the snackRefrigerati
     */
    public boolean isSnackRefrigerati() {
        return snackRefrigerati;
    }

    /**
     * @param acqua
     *            the acqua to set
     */
    public void setAcqua(boolean acqua) {
        this.acqua = acqua;
    }

    /**
     * @param caldo
     *            the caldo to set
     */
    public void setCaldo(boolean caldo) {
        this.caldo = caldo;
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
     * @param freddo
     *            the freddo to set
     */
    public void setFreddo(boolean freddo) {
        this.freddo = freddo;
    }

    /**
     * @param gelato
     *            The gelato to set.
     */
    public void setGelato(boolean gelato) {
        this.gelato = gelato;
    }

    /**
     * @param kit
     *            the kit to set
     */
    public void setKit(boolean kit) {
        this.kit = kit;
    }

    /**
     * @param prodotti
     *            the prodotti to set
     */
    public final void setProdotti(List<ProdottoTipoModello> prodotti) {
        this.prodotti = prodotti;
    }

    /**
     * @param snack
     *            the snack to set
     */
    public void setSnack(boolean snack) {
        this.snack = snack;
    }

    /**
     * @param snackRefrigerati
     *            the snackRefrigerati to set
     */
    public void setSnackRefrigerati(boolean snackRefrigerati) {
        this.snackRefrigerati = snackRefrigerati;
    }

    /**
     * @param tipoComunicazione
     *            the tipoComunicazione to set
     */
    public void setTipoComunicazione(TipoComunicazione tipoComunicazione) {
        this.tipoComunicazione = tipoComunicazione;
    }

}
