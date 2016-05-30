package it.eurotn.panjea.manutenzioni.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.entity.annotation.EntityConverter;
import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;

@Entity
@Audited
@NamedQueries({
        @NamedQuery(name = "Installazione.caricaUbicazioni", query = "select i.ubicazione from Installazione i group by i.ubicazione") })
@Table(name = "manu_installazioni", uniqueConstraints = @UniqueConstraint(columnNames = "codice") )
@EntityConverter(properties = "codice,descrizione")
public class Installazione extends EntityBase {
    private static final long serialVersionUID = 6178779414609978081L;

    @ManyToOne(fetch = FetchType.LAZY)
    private ArticoloMI articolo;

    @ManyToOne(fetch = FetchType.LAZY)
    private TipoAreaMagazzino tipoAreaMagazzino;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "listino_id")
    private Listino listino;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "listino_alternativo_id")
    private Listino listinoAlternativo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deposito_id", nullable = false)
    private DepositoInstallazione deposito;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pozzetto_id", nullable = true)
    private Deposito pozzetto;

    @Embedded
    private DatiInstallazione datiInstallazione;

    @Column(length = 10, nullable = false)
    private String codice;

    @Column(length = 130)
    private String descrizione;

    @ManyToOne(fetch = FetchType.LAZY)
    private UbicazioneInstallazione ubicazione;

    @Column(length = 500)
    private String note;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "installazione", cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<ProdottoInstallazione> prodotti;

    /**
     * @return Returns the serialversionuid.
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    /**
     * @return Returns the articolo.
     */
    public ArticoloMI getArticolo() {
        return articolo;
    }

    /**
     * @return Returns the codice.
     */
    public String getCodice() {
        return codice;
    }

    /**
     * @return Returns the datiInstallazione.
     */
    public DatiInstallazione getDatiInstallazione() {
        if (datiInstallazione == null) {
            datiInstallazione = new DatiInstallazione();
        }
        return datiInstallazione;
    }

    /**
     * @return Returns the deposito.
     */
    public DepositoInstallazione getDeposito() {
        return deposito;
    }

    /**
     * @return Returns the descrizione.
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * @return Returns the listino.
     */
    public Listino getListino() {
        return listino;
    }

    /**
     * @return the listinoAlternativo
     */
    public Listino getListinoAlternativo() {
        return listinoAlternativo;
    }

    /**
     * @return Returns the note.
     */
    public String getNote() {
        return note;
    }

    /**
     * @return the pozzetto
     */
    public Deposito getPozzetto() {
        return pozzetto;
    }

    /**
     * @return the prodotti
     */
    public List<ProdottoInstallazione> getProdotti() {
        return prodotti;
    }

    /**
     * @return Returns the tipoAreaMagazzino.
     */
    public TipoAreaMagazzino getTipoAreaMagazzino() {
        return tipoAreaMagazzino;
    }

    /**
     * @return the ubicazione
     */
    public UbicazioneInstallazione getUbicazione() {
        return ubicazione;
    }

    /**
     * @param articolo
     *            The articolo to set.
     */
    public void setArticolo(ArticoloMI articolo) {
        this.articolo = articolo;
    }

    /**
     * @param codice
     *            The codice to set.
     */
    public void setCodice(String codice) {
        this.codice = codice;
    }

    /**
     * @param datiInstallazione
     *            The datiInstallazione to set.
     */
    public void setDatiInstallazione(DatiInstallazione datiInstallazione) {
        this.datiInstallazione = datiInstallazione;
    }

    /**
     * @param deposito
     *            The deposito to set.
     */
    public void setDeposito(DepositoInstallazione deposito) {
        this.deposito = deposito;
    }

    /**
     * @param descrizione
     *            The descrizione to set.
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * @param listino
     *            The listino to set.
     */
    public void setListino(Listino listino) {
        this.listino = listino;
    }

    /**
     * @param listinoAlternativo
     *            the listinoAlternativo to set
     */
    public void setListinoAlternativo(Listino listinoAlternativo) {
        this.listinoAlternativo = listinoAlternativo;
    }

    /**
     * @param note
     *            The note to set.
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * @param pozzetto
     *            the pozzetto to set
     */
    public void setPozzetto(Deposito pozzetto) {
        this.pozzetto = pozzetto;
    }

    /**
     * @param prodotti
     *            the prodotti to set
     */
    public void setProdotti(List<ProdottoInstallazione> prodotti) {
        this.prodotti = prodotti;
    }

    /**
     * @param tipoAreaMagazzino
     *            The tipoAreaMagazzino to set.
     */
    public void setTipoAreaMagazzino(TipoAreaMagazzino tipoAreaMagazzino) {
        this.tipoAreaMagazzino = tipoAreaMagazzino;
    }

    /**
     * @param ubicazione
     *            the ubicazione to set
     */
    public void setUbicazione(UbicazioneInstallazione ubicazione) {
        this.ubicazione = ubicazione;
    }

}
