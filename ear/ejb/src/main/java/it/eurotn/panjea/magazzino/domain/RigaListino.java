package it.eurotn.panjea.magazzino.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.util.PanjeaEJBUtil;

/**
 * Rappresenta una riga di una versione listino. La riga è sempre a scaglione e associata ad un
 * articolo.
 *
 * @author fattazzo
 */
@Entity
@Audited
@Table(name = "maga_righe_listini", uniqueConstraints = @UniqueConstraint(columnNames = { "versioneListino_id",
        "articolo_id" }) )
@NamedQueries({
        @NamedQuery(name = "RigaListino.caricaByVersioneArticolo", query = "select DISTINCT riga from RigaListino riga join fetch riga.scaglioni where riga.versioneListino.id = :paramIdVersioneListino  and riga.articolo.id = :paramIdArticolo "),
        @NamedQuery(name = "RigaListino.caricaById", query = "select riga from RigaListino riga  join fetch riga.scaglioni join fetch riga.articolo art join fetch art.unitaMisura join fetch art.codiceIva join fetch riga.versioneListino ver join fetch ver.listino where riga.id = :paramId") })
public class RigaListino extends EntityBase {

    private static final long serialVersionUID = 8994176765059629721L;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private VersioneListino versioneListino;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "rigaListino")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    @OrderBy("quantita")
    private Set<ScaglioneListino> scaglioni;

    @ManyToOne()
    private ArticoloLite articolo;

    /**
     * Decimali per arrotondare il prezzo.
     */
    @Column(nullable = false)
    private Integer numeroDecimaliPrezzo;

    @Column(precision = 8, scale = 2)
    private BigDecimal percentualeRicarico;

    @Transient
    private BigDecimal importoBase;

    @Transient
    private Integer numeroDecimaliPrezzoBase;

    @Column(precision = 8, scale = 2)
    private BigDecimal prezzoChiave;

    /**
     * Costruttore.
     */
    public RigaListino() {
        super();
        this.numeroDecimaliPrezzo = 2;
    }

    /**
     * Applica la percentuale di ricarico sull'importo base.
     *
     * @param importoBaseCalcolo
     *            importo sul quale calcolare la percentuale
     */
    public void applicaPercentualeRicarico(BigDecimal importoBaseCalcolo) {
        BigDecimal baseCalcolo = ObjectUtils.defaultIfNull(importoBaseCalcolo, BigDecimal.ZERO);

        setPrezzo(importoBaseCalcolo.multiply(getPercentualeRicarico()).divide(Importo.HUNDRED).add(baseCalcolo)
                .setScale(numeroDecimaliPrezzo, RoundingMode.HALF_UP));
    }

    /**
     * Calcolala percentuale di ricarico dato il prezzo e confrontandolo con l'importoBase.
     *
     * @param importoPrezzo
     *            il prezzo di cui calcolare il ricarico rispetto all'importo base
     */
    public void calcolaPercentualeRicarico(BigDecimal importoPrezzo) {

        BigDecimal importo = ObjectUtils.defaultIfNull(importoPrezzo, BigDecimal.ZERO);
        percentualeRicarico = BigDecimal.ZERO;
        if (importoBase != null && importoBase.compareTo(BigDecimal.ZERO) != 0) {

            percentualeRicarico = ((importo.subtract(importoBase)).divide(importoBase, 2, RoundingMode.HALF_UP))
                    .multiply(Importo.HUNDRED).setScale(2, RoundingMode.HALF_UP);
        }
    }

    /***
     *
     * @param azzeraPrezzi
     *            azzera il prezzo dello scaglione
     * @return rigaListino clonata
     */
    public RigaListino clone(boolean azzeraPrezzi) {
        RigaListino nuovaRigaListino = PanjeaEJBUtil.cloneObject(this);
        nuovaRigaListino.setId(null);
        nuovaRigaListino.setVersion(null);
        nuovaRigaListino.setArticolo(null);
        for (ScaglioneListino scaglioneListino : nuovaRigaListino.getScaglioni()) {
            scaglioneListino.setId(null);
            scaglioneListino.setVersion(null);
            if (azzeraPrezzi) {
                scaglioneListino.setPrezzo(BigDecimal.ZERO);
            }
        }
        return nuovaRigaListino;
    }

    /**
     * @return articolo
     */
    public ArticoloLite getArticolo() {
        return articolo;
    }

    /**
     * @return the importoBase
     */
    public BigDecimal getImportoBase() {
        return importoBase;
    }

    /**
     * @return numeroDecimaliPrezzo
     */
    public Integer getNumeroDecimaliPrezzo() {
        return numeroDecimaliPrezzo;
    }

    /**
     * @return the numeroDecimaliPrezzoBase
     */
    public Integer getNumeroDecimaliPrezzoBase() {
        return numeroDecimaliPrezzoBase;
    }

    /**
     * @return the percentualeRicarico
     */
    public BigDecimal getPercentualeRicarico() {

        if (percentualeRicarico == null) {
            return BigDecimal.ZERO;
        }
        return percentualeRicarico;
    }

    /**
     * @return prezzo
     */
    public BigDecimal getPrezzo() {
        initScaglioni();
        return scaglioni.iterator().next().getPrezzo();
    }

    /**
     * @return Returns the prezzoChiave.
     */
    public BigDecimal getPrezzoChiave() {
        return prezzoChiave;
    }

    /**
     *
     * @return pattern per il format del prezzo
     */
    public String getPrezzoPattern() {
        String zeri = StringUtils.repeat("0", numeroDecimaliPrezzo);
        return "#,##0." + zeri + ";-#,##0." + zeri;
    }

    /**
     * @return Returns the scaglioni.
     */
    public Set<ScaglioneListino> getScaglioni() {
        initScaglioni();
        return scaglioni;
    }

    /**
     * @return versioneListino
     */
    public VersioneListino getVersioneListino() {
        return versioneListino;
    }

    /**
     * crea la lista degli scaglioni.
     */
    @PostLoad
    private void initScaglioni() {
        if (scaglioni == null) {
            scaglioni = new TreeSet<ScaglioneListino>(new ScaglioneComparator());
            ScaglioneListino scaglione = new ScaglioneListino();
            scaglione.setRigaListino(this);
            scaglioni.add(scaglione);
        }
    }

    /**
     * @param articolo
     *            the articolo to set
     */
    public void setArticolo(ArticoloLite articolo) {
        this.articolo = articolo;
    }

    /**
     *
     * @param idArticolo
     *            the idArticolo to set
     */
    public void setIdArticolo(Integer idArticolo) {
        // non andrebbe fatto ma c'è un Transformers.aliasToBean che usa la RigaListino come se
        // fosse un DTO
        this.setArticolo(new ArticoloLite());
        this.articolo.setId(idArticolo);
    }

    /**
     * @param importoBase
     *            the importoBase to set
     */
    public void setImportoBase(BigDecimal importoBase) {
        this.importoBase = importoBase;
    }

    /**
     * @param nota
     *            the nota to set
     */
    public void setNote(String nota) {
        initScaglioni();
        scaglioni.iterator().next().setNota(nota);
    }

    /**
     * @param numeroDecimaliPrezzo
     *            the numeroDecimaliPrezzo to set
     */
    public void setNumeroDecimaliPrezzo(Integer numeroDecimaliPrezzo) {
        this.numeroDecimaliPrezzo = numeroDecimaliPrezzo;
        if (scaglioni != null) {
            for (ScaglioneListino scaglioneListino : scaglioni) {
                // risetto il prezzo per arrotondarlo ai nuovi decimali
                scaglioneListino.setPrezzo(scaglioneListino.getPrezzo());
            }
        }
    }

    /**
     * @param numeroDecimaliPrezzoBase
     *            the numeroDecimaliPrezzoBase to set
     */
    public void setNumeroDecimaliPrezzoBase(Integer numeroDecimaliPrezzoBase) {
        this.numeroDecimaliPrezzoBase = numeroDecimaliPrezzoBase;
    }

    /**
     * @param percentualeRicarico
     *            the percentualeRicarico to set
     */
    public void setPercentualeRicarico(BigDecimal percentualeRicarico) {
        this.percentualeRicarico = percentualeRicarico;
    }

    /**
     * @param prezzo
     *            the prezzo to set
     */
    public void setPrezzo(BigDecimal prezzo) {
        initScaglioni();
        scaglioni.iterator().next().setRigaListino(this);
        scaglioni.iterator().next().setPrezzo(prezzo);
    }

    /**
     * @param prezzoChiave
     *            The prezzoChiave to set.
     */
    public void setPrezzoChiave(BigDecimal prezzoChiave) {
        this.prezzoChiave = prezzoChiave;
    }

    /**
     * @param scaglioni
     *            The scaglioni to set.
     */
    public void setScaglioni(Set<ScaglioneListino> scaglioni) {
        this.scaglioni = scaglioni;
    }

    /**
     *
     * @param versionArticolo
     *            the versionArticolo to set
     */
    public void setVersionArticolo(Integer versionArticolo) {
        this.articolo.setVersion(versionArticolo);
    }

    /**
     * @param versioneListino
     *            the versioneListino to set
     */
    public void setVersioneListino(VersioneListino versioneListino) {
        this.versioneListino = versioneListino;
    }
}
