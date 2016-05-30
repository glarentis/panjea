package it.eurotn.panjea.anagrafica.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;

/**
 * Classe di dominio che mappa un codice iva.
 *
 * @author giangi
 */
@NamedQueries({
        @NamedQuery(name = "CodiceIva.caricaByCodice", query = "from CodiceIva a where a.codice like :paramCodice and a.codiceAzienda= :codiceAzienda order by a.codice", hints = {
                @QueryHint(name = "org.hibernate.cacheable", value = "true"),
                @QueryHint(name = "org.hibernate.cacheRegion", value = "codiceIva") }),
        @NamedQuery(name = "CodiceIva.caricaByCodiceEUROPA", query = "from CodiceIva a where a.codiceEuropa = :paramCodiceEuropa and a.codiceAzienda= :codiceAzienda"),
        @NamedQuery(name = "CodiceIva.caricaIvaSplitPaymentArticolo", query = "select art.codiceIva.codiceIvaSplitPayment from Articolo art  where art.id=:idArticolo") })
@Entity
@Audited
@Table(name = "anag_codici_iva", uniqueConstraints = @UniqueConstraint(columnNames = { "codiceAzienda", "codice" }) )
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "codiceIva")
public class CodiceIva extends EntityBase {

    public enum IndicatoreVolumeAffari {
        NO, SI, VENDITA_BENE_STRUMENTALE
    }

    public enum TipoCaratteristica {
        MERCE, NONMERCE, BENEAMMORTIZZABILE
    }

    public enum TipoCodiceIva {
        NORMALE, ESENTE, NONIMPONIBILE, NONSOGGETTO, ESCLUSO
    }

    public enum TipoTotalizzazione {
        NONTOTALIZZA, IMPONIBILE, ESENTE, NONIMPONIBILE, ESCLUSO
    }

    private static final long serialVersionUID = 3447301878761699277L;

    @Column(length = 10, nullable = false)
    @Index(name = "codiceIvaCodiceAzienda")
    private String codiceAzienda;

    @Column(length = 6, nullable = false)
    @Index(name = "codice")
    private String codice;

    @Column(length = 6)
    @Index(name = "codiceEuropa")
    private String codiceEuropa;

    private boolean palmareAbilitato;

    @Column(length = 60)
    private String descrizioneInterna;

    @Column(length = 60)
    private String descrizioneRegistro;

    @Column(length = 60)
    private String descrizioneDocumenti;

    private BigDecimal percApplicazione;

    private BigDecimal percIndetraibilita;

    private Boolean liquidazionePeriodica;

    private Boolean liquidazioneAnnuale;

    private Boolean splitPayment;

    @Enumerated
    private TipoCodiceIva tipoCodiceIva;

    @Enumerated
    private TipoCaratteristica tipoCaratteristica;

    @Enumerated
    private TipoTotalizzazione tipoTotalizzazione;

    @Enumerated
    private IndicatoreVolumeAffari indicatoreVolumeAffari;

    @OneToOne(fetch = FetchType.LAZY)
    private CodiceIva codiceIvaSostituzioneVentilazione;

    @OneToOne(fetch = FetchType.LAZY)
    private CodiceIva codiceIvaCollegato;

    @OneToOne(fetch = FetchType.LAZY)
    private CodiceIva codiceIvaSplitPayment;

    @Column(length = 1)
    private String codiceEsportazioneDocumento;

    private boolean ivaSospesa;

    // ----------------------- dati spesometro
    // -------------------------------------------------------------
    private boolean includiSpesometro;

    private TipologiaCodiceIvaSpesometro tipologiaSpesometro;
    // -------------------------------------------------------------------------------------------------------------

    {
        this.ivaSospesa = false;
        this.splitPayment = false;

        this.tipologiaSpesometro = TipologiaCodiceIvaSpesometro.MERCE;
    }

    /**
     * Default constructor.
     */
    public CodiceIva() {
        super();
    }

    /**
     * HACK costruttore utilizzato per istanziare il codiceIvaSostituzioneVentilazione senza
     * incorrere in un loop infinito.
     *
     * @param codiceIvaSostituzioneVentilazione
     *            codiceIvaSostituzioneVentilazione
     * @param codiceIvaCollegato
     *            codiceIvaCollegato
     */
    public CodiceIva(final CodiceIva codiceIvaSostituzioneVentilazione, final CodiceIva codiceIvaCollegato) {
        this.codiceIvaSostituzioneVentilazione = codiceIvaSostituzioneVentilazione;
        this.codiceIvaCollegato = codiceIvaCollegato;
        this.ivaSospesa = false;
    }

    /**
     * Dato un valore restituisce il valore con l'va applicata.
     *
     * @param valore
     *            valore al quale applicare l'iva
     * @return valore con l'iva applicata
     */
    public BigDecimal applica(BigDecimal valore) {
        valore = valore.add(percApplicazione.divide(Importo.HUNDRED).multiply(valore));
        return valore.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Dato un valore restituisce il valore con l'va applicata.
     *
     * @param valore
     *            valore al quale applicare l'iva
     * @param numDecimali
     *            numero decimali per l'arrotondamento
     * @return valore con l'iva applicata
     */
    public BigDecimal applica(BigDecimal valore, int numDecimali) {
        valore = valore.add(percApplicazione.divide(Importo.HUNDRED).multiply(valore));
        return valore.setScale(numDecimali, BigDecimal.ROUND_HALF_UP);
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
     * @return the codiceEsportazioneDocumento
     */
    public String getCodiceEsportazioneDocumento() {
        return codiceEsportazioneDocumento;
    }

    /**
     * @return the codiceEuropa
     */
    public String getCodiceEuropa() {
        return codiceEuropa;
    }

    /**
     * @return the codiceIvaCollegato
     */
    public CodiceIva getCodiceIvaCollegato() {
        return codiceIvaCollegato;
    }

    /**
     * @return the codiceIvaSostituzioneVentilazione
     */
    public CodiceIva getCodiceIvaSostituzioneVentilazione() {
        return codiceIvaSostituzioneVentilazione;
    }

    /**
     * @return Returns the codiceIvaSplitPayment.
     */
    public CodiceIva getCodiceIvaSplitPayment() {
        return codiceIvaSplitPayment;
    }

    /**
     * @return the descrizioneDocumenti
     */
    public String getDescrizioneDocumenti() {
        return descrizioneDocumenti;
    }

    /**
     * @return the descrizioneInterna
     */
    public String getDescrizioneInterna() {
        return descrizioneInterna;
    }

    /**
     * @return the descrizioneRegistro
     */
    public String getDescrizioneRegistro() {
        return descrizioneRegistro;
    }

    /**
     * @return the indicatoreVolumeAffari
     */
    public IndicatoreVolumeAffari getIndicatoreVolumeAffari() {
        return indicatoreVolumeAffari;
    }

    /**
     * @return the liquidazioneAnnuale
     */
    public Boolean getLiquidazioneAnnuale() {
        return liquidazioneAnnuale;
    }

    /**
     * @return the liquidazionePeriodica
     */
    public Boolean getLiquidazionePeriodica() {
        return liquidazionePeriodica;
    }

    /**
     * @return the percApplicazione
     */
    public BigDecimal getPercApplicazione() {
        return percApplicazione;
    }

    /**
     * @return the percIndetraibilita
     */
    public BigDecimal getPercIndetraibilita() {
        return percIndetraibilita;
    }

    /**
     * @return Returns the splitPayment.
     */
    public Boolean getSplitPayment() {
        if (splitPayment == null) {
            splitPayment = false;
        }
        return splitPayment;
    }

    /**
     * @return the tipoCaratteristica
     */
    public TipoCaratteristica getTipoCaratteristica() {
        return tipoCaratteristica;
    }

    /**
     * @return the tipoCodiceIva
     */
    public TipoCodiceIva getTipoCodiceIva() {
        return tipoCodiceIva;
    }

    /**
     * @return the tipologiaSpesometro
     */
    public TipologiaCodiceIvaSpesometro getTipologiaSpesometro() {
        return tipologiaSpesometro;
    }

    /**
     * @return the tipoTotalizzazione
     */
    public TipoTotalizzazione getTipoTotalizzazione() {
        return tipoTotalizzazione;
    }

    /**
     * @return Returns the includiSpesometro.
     */
    public boolean isIncludiSpesometro() {
        return includiSpesometro;
    }

    /**
     * @return the ivaSospesa
     */
    public boolean isIvaSospesa() {
        return ivaSospesa;
    }

    /**
     * @return Returns the palmareAbilitato.
     */
    public boolean isPalmareAbilitato() {
        return palmareAbilitato;
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
     * @param codiceEsportazioneDocumento
     *            the codiceEsportazioneDocumento to set
     */
    public void setCodiceEsportazioneDocumento(String codiceEsportazioneDocumento) {
        this.codiceEsportazioneDocumento = codiceEsportazioneDocumento;
    }

    /**
     * @param codiceEuropa
     *            the codiceEuropa to set
     */
    public void setCodiceEuropa(String codiceEuropa) {
        this.codiceEuropa = codiceEuropa;
    }

    /**
     * @param codiceIvaCollegato
     *            the codiceIvaCollegato to set
     */
    public void setCodiceIvaCollegato(CodiceIva codiceIvaCollegato) {
        this.codiceIvaCollegato = codiceIvaCollegato;
    }

    /**
     * @param codiceIvaSostituzioneVentilazione
     *            the codiceIvaSostituzioneVentilazione to set
     */
    public void setCodiceIvaSostituzioneVentilazione(CodiceIva codiceIvaSostituzioneVentilazione) {
        this.codiceIvaSostituzioneVentilazione = codiceIvaSostituzioneVentilazione;
    }

    /**
     * @param codiceIvaSplitPayment
     *            The codiceIvaSplitPayment to set.
     */
    public void setCodiceIvaSplitPayment(CodiceIva codiceIvaSplitPayment) {
        this.codiceIvaSplitPayment = codiceIvaSplitPayment;
    }

    /**
     * @param descrizioneDocumenti
     *            the descrizioneDocumenti to set
     */
    public void setDescrizioneDocumenti(String descrizioneDocumenti) {
        this.descrizioneDocumenti = descrizioneDocumenti;
    }

    /**
     * @param descrizioneInterna
     *            the descrizioneInterna to set
     */
    public void setDescrizioneInterna(String descrizioneInterna) {
        this.descrizioneInterna = descrizioneInterna;
    }

    /**
     * @param descrizioneRegistro
     *            the descrizioneRegistro to set
     */
    public void setDescrizioneRegistro(String descrizioneRegistro) {
        this.descrizioneRegistro = descrizioneRegistro;
    }

    /**
     * @param includiSpesometro
     *            The includiSpesometro to set.
     */
    public void setIncludiSpesometro(boolean includiSpesometro) {
        this.includiSpesometro = includiSpesometro;
    }

    /**
     * @param indicatoreVolumeAffari
     *            the indicatoreVolumeAffari to set
     */
    public void setIndicatoreVolumeAffari(IndicatoreVolumeAffari indicatoreVolumeAffari) {
        this.indicatoreVolumeAffari = indicatoreVolumeAffari;
    }

    /**
     * @param ivaSospesa
     *            the ivaSospesa to set
     */
    public void setIvaSospesa(boolean ivaSospesa) {
        this.ivaSospesa = ivaSospesa;
    }

    /**
     * @param liquidazioneAnnuale
     *            the liquidazioneAnnuale to set
     */
    public void setLiquidazioneAnnuale(Boolean liquidazioneAnnuale) {
        this.liquidazioneAnnuale = liquidazioneAnnuale;
    }

    /**
     * @param liquidazionePeriodica
     *            the liquidazionePeriodica to set
     */
    public void setLiquidazionePeriodica(Boolean liquidazionePeriodica) {
        this.liquidazionePeriodica = liquidazionePeriodica;
    }

    /**
     * @param palmareAbilitato
     *            The palmareAbilitato to set.
     */
    public void setPalmareAbilitato(boolean palmareAbilitato) {
        this.palmareAbilitato = palmareAbilitato;
    }

    /**
     * @param percApplicazione
     *            the percApplicazione to set
     */
    public void setPercApplicazione(BigDecimal percApplicazione) {
        this.percApplicazione = percApplicazione;
    }

    /**
     * @param percIndetraibilita
     *            the percIndetraibilita to set
     */
    public void setPercIndetraibilita(BigDecimal percIndetraibilita) {
        this.percIndetraibilita = percIndetraibilita;
    }

    /**
     * @param splitPayment
     *            The splitPayment to set.
     */
    public void setSplitPayment(Boolean splitPayment) {
        this.splitPayment = splitPayment;
    }

    /**
     * @param tipoCaratteristica
     *            the tipoCaratteristica to set
     */
    public void setTipoCaratteristica(TipoCaratteristica tipoCaratteristica) {
        this.tipoCaratteristica = tipoCaratteristica;
    }

    /**
     * @param tipoCodiceIva
     *            the tipoCodiceIva to set
     */
    public void setTipoCodiceIva(TipoCodiceIva tipoCodiceIva) {
        this.tipoCodiceIva = tipoCodiceIva;
    }

    /**
     * @param tipologiaSpesometro
     *            the tipologiaSpesometro to set
     */
    public void setTipologiaSpesometro(TipologiaCodiceIvaSpesometro tipologiaSpesometro) {
        this.tipologiaSpesometro = tipologiaSpesometro;
    }

    /**
     * @param tipoTotalizzazione
     *            the tipoTotalizzazione to set
     */
    public void setTipoTotalizzazione(TipoTotalizzazione tipoTotalizzazione) {
        this.tipoTotalizzazione = tipoTotalizzazione;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("CodiceIva[");
        buffer.append(super.toString());
        buffer.append("codice = ").append(codice);
        buffer.append(" codiceAzienda = ").append(codiceAzienda);
        buffer.append(" codiceEuropa = ").append(codiceEuropa);
        buffer.append(" descrizioneDocumenti = ").append(descrizioneDocumenti);
        buffer.append(" descrizioneInterna = ").append(descrizioneInterna);
        buffer.append(" descrizioneRegistro = ").append(descrizioneRegistro);
        buffer.append(" indetraibilita = ").append(percIndetraibilita);
        buffer.append(" liquidazioneAnnuale = ").append(liquidazioneAnnuale);
        buffer.append(" liquidazionePeriodica = ").append(liquidazionePeriodica);
        buffer.append(" percApplicazione = ").append(percApplicazione);
        buffer.append(" tipoCaratteristica = ").append(tipoCaratteristica);
        buffer.append(" tipoCodiceIva = ").append(tipoCodiceIva);
        buffer.append(" tipoTotalizzazione = ").append(tipoTotalizzazione);
        buffer.append("]");
        return buffer.toString();
    }
}
