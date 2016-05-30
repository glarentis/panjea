package it.eurotn.panjea.vending.domain.documento;

import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino.ETipologiaCodiceIvaAlternativo;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.manutenzioni.domain.Installazione;
import it.eurotn.panjea.manutenzioni.domain.Operatore;
import it.eurotn.panjea.manutenzioni.domain.documento.DatiVendingArea;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.vending.domain.Distributore;
import it.eurotn.panjea.vending.domain.evadts.RilevazioneEvaDts;
import it.eurotn.util.PanjeaEJBUtil;

@SuppressWarnings("serial")
@Entity
@Audited
@Table(name = "vend_area_rifornimento")
public class AreaRifornimento extends EntityBase {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "areaRifornimento")
    private Set<RilevazioneEvaDts> rilevazioneEvaDts;

    @ManyToOne(fetch = FetchType.LAZY)
    private AreaMagazzino areaMagazzino;

    @ManyToOne(fetch = FetchType.LAZY)
    private AreaOrdine areaOrdine;

    @ManyToOne(fetch = FetchType.LAZY)
    private Distributore distributore;

    @ManyToOne(fetch = FetchType.LAZY)
    private Operatore operatore;

    @ManyToOne(fetch = FetchType.LAZY)
    private Installazione installazione;

    private Integer numeroSacchetto;

    @Column(precision = 19, scale = 6)
    private BigDecimal incasso;

    @Column(precision = 19, scale = 6)
    private BigDecimal reso;

    @Embedded
    private DatiVendingArea datiVendingArea;

    /**
     * Costruttore.
     */
    public AreaRifornimento() {
    }

    /**
     * Copia area rifornimento e la associa all'area magazzino (rimuovendo un eventuale collegamento
     * all'area ordine)
     *
     * @param am
     *            area Magazzino da associare
     * @return copia dell'area rif. con @areaMagazzino associata
     */
    public AreaRifornimento copia(AreaMagazzino am) {
        AreaRifornimento nuovaAreaRifornimento = new AreaRifornimento();
        PanjeaEJBUtil.copyProperties(nuovaAreaRifornimento, this);
        nuovaAreaRifornimento.setAreaOrdine(null);
        nuovaAreaRifornimento.setAreaMagazzino(am);
        nuovaAreaRifornimento.getDatiVendingArea().getBattute();
        nuovaAreaRifornimento.getDatiVendingArea().getLettureContatore();
        return nuovaAreaRifornimento;
    }

    /**
     * @return Returns the areaMagazzino.
     */
    public AreaMagazzino getAreaMagazzino() {
        return areaMagazzino;
    }

    /**
     * @return Returns the areaOrdine.
     */
    public AreaOrdine getAreaOrdine() {
        return areaOrdine;
    }

    /**
     * @return the datiVendingArea
     */
    public DatiVendingArea getDatiVendingArea() {
        if (datiVendingArea == null) {
            datiVendingArea = new DatiVendingArea();
        }
        return datiVendingArea;
    }

    /**
     * @return the distributore
     */
    public final Distributore getDistributore() {
        return distributore;
    }

    /**
     *
     * @return documento dell'area rifornimento
     */
    public Documento getDocumento() {
        if (areaMagazzino != null) {
            return areaMagazzino.getDocumento();
        } else if (areaOrdine != null) {
            return areaOrdine.getDocumento();
        }
        throw new IllegalStateException("areaMagazzino e areaOrdine a null");
    }

    /**
     * @return the incasso
     */
    public BigDecimal getIncasso() {
        return incasso;
    }

    /**
     * @return Returns the installazione.
     */
    public Installazione getInstallazione() {
        return installazione;
    }

    /**
     * @return the numeroSacchetto
     */
    public Integer getNumeroSacchetto() {
        return numeroSacchetto;
    }

    /**
     * @return the operatore
     */
    public final Operatore getOperatore() {
        return operatore;
    }

    /**
     * @return the reso
     */
    public BigDecimal getReso() {
        return reso;
    }

    /**
     *
     * @return rilevazione Eva fatta durante il rifornimento
     */
    public RilevazioneEvaDts getRilevazioneEvaDts() {
        if (CollectionUtils.isEmpty(rilevazioneEvaDts)) {
            return null;
        }
        return rilevazioneEvaDts.iterator().next();
    }

    /**
     * @param areaMagazzino
     *            The areaMagazzino to set.
     */
    public void setAreaMagazzino(AreaMagazzino areaMagazzino) {
        this.areaMagazzino = areaMagazzino;
    }

    /**
     * @param areaOrdine
     *            The areaOrdine to set.
     */
    public void setAreaOrdine(AreaOrdine areaOrdine) {
        this.areaOrdine = areaOrdine;
    }

    /**
     *
     * @param codIvaAlternativo
     *            codIvaAlternativo
     */
    public void setCodiceIvaAlternativo(CodiceIva codIvaAlternativo) {
        if (areaMagazzino != null) {
            areaMagazzino.setCodiceIvaAlternativo(codIvaAlternativo);
        }
        if (areaOrdine != null) {
            areaOrdine.setCodiceIvaAlternativo(codIvaAlternativo);
        }

    }

    /**
     * @param datiVendingArea
     *            the datiVendingArea to set
     */
    public void setDatiVendingArea(DatiVendingArea datiVendingArea) {
        this.datiVendingArea = datiVendingArea;
    }

    /**
     * deposito destinazione
     *
     * @param deposito
     *            depositoDestinazione
     */
    public void setDepositoDestinazione(DepositoLite deposito) {
        if (areaMagazzino != null) {
            areaMagazzino.setDepositoDestinazione(deposito);
        }
    }

    /**
     *
     * @param deposito
     *            deposito origine
     */
    public void setDepositoOrigine(DepositoLite deposito) {
        if (areaMagazzino != null) {
            areaMagazzino.setDepositoOrigine(deposito);
        }
        if (areaOrdine != null) {
            areaOrdine.setDepositoOrigine(deposito);
        }
    }

    /**
     * @param distributore
     *            the distributore to set
     */
    public final void setDistributore(Distributore distributore) {
        this.distributore = distributore;
    }

    /**
     * @param incasso
     *            the incasso to set
     */
    public void setIncasso(BigDecimal incasso) {
        this.incasso = incasso;
    }

    /**
     * @param installazione
     *            The installazione to set.
     */
    public void setInstallazione(Installazione installazione) {
        this.installazione = installazione;
    }

    /**
     *
     * @param listino
     *            listino da settare
     */
    public void setListino(Listino listino) {
        if (areaMagazzino != null) {
            areaMagazzino.setListino(listino);
        }
        if (areaOrdine != null) {
            areaOrdine.setListino(listino);
        }
    }

    /**
     *
     * @param listino
     *            listino Alternativo da settare
     */
    public void setListinoAlternativo(Listino listino) {
        if (areaMagazzino != null) {
            areaMagazzino.setListinoAlternativo(listino);
        }
        if (areaOrdine != null) {
            areaOrdine.setListinoAlternativo(listino);
        }
    }

    /**
     * @param numeroSacchetto
     *            the numeroSacchetto to set
     */
    public void setNumeroSacchetto(Integer numeroSacchetto) {
        this.numeroSacchetto = numeroSacchetto;
    }

    /**
     * @param operatore
     *            the operatore to set
     */
    public final void setOperatore(Operatore operatore) {
        this.operatore = operatore;
    }

    /**
     * @param reso
     *            the reso to set
     */
    public void setReso(BigDecimal reso) {
        this.reso = reso;
    }

    /**
     * @param rilevazioneEvaDts
     *            the rilevazioneEvaDts to set
     */
    public void setRilevazioneEvaDts(Set<RilevazioneEvaDts> rilevazioneEvaDts) {
        this.rilevazioneEvaDts = rilevazioneEvaDts;
    }

    /**
     *
     * @param tipologia
     *            tipologia
     */
    public void setTipologiaCodiceIvaAlternativo(ETipologiaCodiceIvaAlternativo tipologia) {
        if (areaMagazzino != null) {
            areaMagazzino.setTipologiaCodiceIvaAlternativo(tipologia);
        }
        if (areaOrdine != null) {
            areaOrdine.setTipologiaCodiceIvaAlternativo(tipologia);
        }
    }

}