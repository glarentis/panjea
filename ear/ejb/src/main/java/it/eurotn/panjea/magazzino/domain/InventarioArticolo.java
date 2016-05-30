package it.eurotn.panjea.magazzino.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;

@Entity
@Table(name = "maga_inventari_articolo")
@NamedQueries({
        @NamedQuery(name = "InventarioArticolo.caricaAllDTO", query = "select new it.eurotn.panjea.magazzino.util.InventarioArticoloDTO(inv.data, inv.deposito) from InventarioArticolo inv group by inv.data, inv.deposito order by inv.data"),
        @NamedQuery(name = "InventarioArticolo.caricaInventario", query = "select distinct inv from InventarioArticolo inv inner join fetch inv.articolo art inner join fetch art.categoria left join fetch art.unitaMisura um left join fetch inv.righeInventarioArticolo righe where inv.data = :data and inv.deposito.id = :deposito and (inv.giacenzaCalcolata != 0 or :tutteGiacenza = true) order by inv.articolo.codice"),
        @NamedQuery(name = "InventarioArticolo.cancellaInventarioDeposito", query = "delete from InventarioArticolo inv where inv.data = :data and inv.deposito = :deposito") })
public class InventarioArticolo extends EntityBase {

    private static final long serialVersionUID = -3679700165872541623L;

    @Column
    @Temporal(TemporalType.DATE)
    private Date data;

    @ManyToOne
    private DepositoLite deposito;

    @ManyToOne
    private ArticoloLite articolo;

    private Double giacenzaCalcolata;

    @Column(precision = 19, scale = 6)
    private BigDecimal importo;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "inventarioArticolo", cascade = CascadeType.ALL)
    @org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<RigaInventarioArticolo> righeInventarioArticolo;

    @Transient
    private Integer numeroRighe;

    @Transient
    private BigDecimal qtaRighe;

    /**
     * Costruttore.
     */
    public InventarioArticolo() {
        super();
        this.articolo = new ArticoloLite();
        this.articolo.setCategoria(new Categoria());
        this.righeInventarioArticolo = new ArrayList<RigaInventarioArticolo>();
        this.giacenzaCalcolata = 0.0;
    }

    /**
     * @return the articolo
     */
    public ArticoloLite getArticolo() {
        return articolo;
    }

    /**
     * @return the data
     */
    public Date getData() {
        return data;
    }

    /**
     * @return the deposito
     */
    public DepositoLite getDeposito() {
        return deposito;
    }

    /**
     * @return the giacenzaCalcolata
     */
    public Double getGiacenzaCalcolata() {
        return giacenzaCalcolata;
    }

    /**
     * @return the giacenzaReale
     */
    public BigDecimal getGiacenzaReale() {
        return qtaRighe;
    }

    /**
     * @return Returns the importo.
     */
    public BigDecimal getImporto() {
        return importo;
    }

    /**
     * @return Returns the numeroRighe.
     */
    public Integer getNumeroRighe() {
        return numeroRighe;
    }

    /**
     * @return Returns the qtaRighe.
     */
    public BigDecimal getQtaRighe() {
        return qtaRighe;
    }

    /**
     * @return rettifica della quantità ( giacenza reale - giacenza calcolata )
     */
    public BigDecimal getRettifica() {
        if (qtaRighe == null) {
            qtaRighe = BigDecimal.ZERO;
        }
        return qtaRighe.subtract(BigDecimal.valueOf(giacenzaCalcolata));
    }

    /**
     * @return the righeInventarioArticolo
     */
    public List<RigaInventarioArticolo> getRigheInventarioArticolo() {
        return righeInventarioArticolo;
    }

    /**
     * @param articolo
     *            the articolo to set
     */
    public void setArticolo(ArticoloLite articolo) {
        this.articolo = articolo;
    }

    /**
     * @param codiceArticolo
     *            the codiceArticolo to set
     */
    public void setCodiceArticolo(String codiceArticolo) {
        this.articolo.setCodice(codiceArticolo);
    }

    /**
     * @param codiceCategoria
     *            the codiceCategoria to set
     */
    public void setCodiceCategoria(String codiceCategoria) {
        this.articolo.getCategoria().setCodice(codiceCategoria);
    }

    /**
     * @param data
     *            the data to set
     */
    public void setData(Date data) {
        this.data = data;
    }

    /**
     * @param deposito
     *            the deposito to set
     */
    public void setDeposito(DepositoLite deposito) {
        this.deposito = deposito;
    }

    /**
     * @param descrizioneArticolo
     *            the descrizioneArticolo to set
     */
    public void setDescrizioneArticolo(String descrizioneArticolo) {
        this.articolo.setDescrizione(descrizioneArticolo);
    }

    /**
     * @param descrizioneCategoria
     *            the descrizioneCategoria to set
     */
    public void setDescrizioneCategoria(String descrizioneCategoria) {
        this.articolo.getCategoria().setDescrizione(descrizioneCategoria);
    }

    /**
     * @param giacenzaCalcolata
     *            the giacenzaCalcolata to set
     */
    public void setGiacenzaCalcolata(Double giacenzaCalcolata) {
        this.giacenzaCalcolata = giacenzaCalcolata;
    }

    /**
     * @param giacenzaReale
     *            the giacenzaReale to set
     */
    public void setGiacenzaReale(BigDecimal giacenzaReale) {

    }

    /**
     * @param idArticolo
     *            the idArticolo to set
     */
    public void setIdArticolo(Integer idArticolo) {
        this.articolo.setId(idArticolo);
    }

    /**
     * @param idCategoria
     *            the idCategoria to set
     */
    public void setIdCategoria(Integer idCategoria) {
        this.articolo.getCategoria().setId(idCategoria);
    }

    /**
     * @param importo
     *            The importo to set.
     */
    public void setImporto(BigDecimal importo) {
        this.importo = importo;
    }

    /**
     * @param numeroRighe
     *            The numeroRighe to set.
     */
    public void setNumeroRighe(Long numeroRighe) {
        this.numeroRighe = numeroRighe.intValue();
    }

    /**
     * @param qtaRighe
     *            The qtaRighe to set.
     */
    public void setQtaRighe(BigDecimal qtaRighe) {
        this.qtaRighe = qtaRighe;
    }

    /**
     * @param righeInventarioArticolo
     *            the righeInventarioArticolo to set
     */
    public void setRigheInventarioArticolo(List<RigaInventarioArticolo> righeInventarioArticolo) {
        this.righeInventarioArticolo = righeInventarioArticolo;
    }

}
