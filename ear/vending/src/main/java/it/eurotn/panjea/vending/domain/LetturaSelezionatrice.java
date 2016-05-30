package it.eurotn.panjea.vending.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Index;
import org.hibernate.envers.NotAudited;

import it.eurotn.entity.EntityBase;
import it.eurotn.entity.IEntityCodiceAzienda;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.manutenzioni.domain.Installazione;
import it.eurotn.panjea.manutenzioni.domain.Operatore;

@Entity
@Table(name = "vend_letture_selezionatrice")
@org.hibernate.annotations.Table(appliesTo = "vend_letture_selezionatrice", indexes = {
        @Index(name = "IdxProgressivo", columnNames = { "progressivo" }),
        @Index(name = "IdxCodiceInstallazione", columnNames = { "codiceInstallazione" }),
        @Index(name = "IdxCodiceDistributore", columnNames = { "codiceDistributore" }),
        @Index(name = "IdxCodiceCaricatore", columnNames = { "codiceCaricatore" }) })
public class LetturaSelezionatrice extends EntityBase implements IEntityCodiceAzienda {

    public enum StatoLettura {
        RIFORNIMENTO_ASSOCIATO, RIFORNIMENTO_DA_CREARE, NON_VALIDA, CASSA, TRASFERIMENTO_CASSA
    }

    private static final long serialVersionUID = -8940880631377439962L;

    @Column(length = 10, nullable = false)
    private String codiceAzienda = null;

    private Integer progressivo;

    private Date data;

    @NotAudited
    @ManyToOne(fetch = FetchType.LAZY)
    private AreaMagazzino rifornimento;

    private String codiceInstallazione;

    private String codiceDistributore;

    private Date dataRifornimento;

    private String codiceCaricatore;

    private String codiceCassaOrigine;
    private String codiceCassaDestinazione;

    private Integer numeroSacchetto;

    // dati utilizzati nella ricerca
    @ManyToOne(fetch = FetchType.LAZY)
    private Installazione installazione;

    @ManyToOne(fetch = FetchType.LAZY)
    private Distributore distributore;

    @ManyToOne(fetch = FetchType.LAZY)
    private Operatore caricatore;

    @ManyToOne(fetch = FetchType.LAZY)
    private Cassa cassaOrigine;

    @ManyToOne(fetch = FetchType.LAZY)
    private Cassa cassaDestinazione;

    @Column(precision = 10, scale = 2)
    private BigDecimal reso;

    @Transient
    private StatoLettura statoLettura;

    // contiene l'importo di tutte le sue righe
    @Transient
    private BigDecimal importo;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "letturaSelezionatrice")
    private List<RigaLetturaSelezionatrice> righe;

    {
        reso = BigDecimal.ZERO;
    }

    /**
     * @return the caricatore
     */
    public Operatore getCaricatore() {
        return caricatore;
    }

    /**
     * @return the cassaDestinazione
     */
    public Cassa getCassaDestinazione() {
        return cassaDestinazione;
    }

    /**
     * @return the cassaOrigine
     */
    public Cassa getCassaOrigine() {
        return cassaOrigine;
    }

    /**
     * @return the codiceAzienda
     */
    @Override
    public String getCodiceAzienda() {
        return codiceAzienda;
    }

    /**
     * @return the codiceCaricatore
     */
    public String getCodiceCaricatore() {
        return codiceCaricatore;
    }

    /**
     * @return the codiceCassaDestinazione
     */
    public String getCodiceCassaDestinazione() {
        return codiceCassaDestinazione;
    }

    /**
     * @return the codiceCassaOrigine
     */
    public String getCodiceCassaOrigine() {
        return codiceCassaOrigine;
    }

    /**
     * @return the codiceDistributore
     */
    public String getCodiceDistributore() {
        return codiceDistributore;
    }

    /**
     * @return the codiceInstallazione
     */
    public String getCodiceInstallazione() {
        return codiceInstallazione;
    }

    /**
     * @return the data
     */
    public Date getData() {
        return data;
    }

    /**
     * @return the dataRifornimento
     */
    public Date getDataRifornimento() {
        return dataRifornimento;
    }

    /**
     * @return the distributore
     */
    public Distributore getDistributore() {
        return distributore;
    }

    /**
     * @return the importo
     */
    public BigDecimal getImporto() {
        return importo;
    }

    /**
     * @return the installazione
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
     * @return the progressivo
     */
    public Integer getProgressivo() {
        return progressivo;
    }

    /**
     * @return the reso
     */
    public BigDecimal getReso() {
        return reso;
    }

    /**
     * @return the rifornimento
     */
    public AreaMagazzino getRifornimento() {
        return rifornimento;
    }

    /**
     * @return the righe
     */
    public List<RigaLetturaSelezionatrice> getRighe() {
        return righe;
    }

    /**
     * @return the statoLettura
     */
    public StatoLettura getStatoLettura() {
        // lazy perchè vado a mettere la proprietà in una table
        if (statoLettura == null) {
            statoLettura = StatoLettura.NON_VALIDA;
            if (cassaDestinazione == null || cassaDestinazione.isNew() || dataRifornimento == null) {
                return statoLettura;
            }

            if (rifornimento != null && !rifornimento.isNew()) {
                statoLettura = StatoLettura.RIFORNIMENTO_ASSOCIATO;
                return statoLettura;
            }

            if (!installazione.isNew() && !distributore.isNew() && !caricatore.isNew()) {
                statoLettura = StatoLettura.RIFORNIMENTO_DA_CREARE;
                return statoLettura;
            }

            if (cassaOrigine != null && !cassaOrigine.isNew()) {
                statoLettura = StatoLettura.TRASFERIMENTO_CASSA;
            } else {
                statoLettura = StatoLettura.CASSA;
            }
            return statoLettura;
        }

        return statoLettura;
    }

    /**
     * @param caricatore
     *            the caricatore to set
     */
    public void setCaricatore(Operatore caricatore) {
        this.caricatore = caricatore;
        if (caricatore == null || caricatore.isNew()) {
            this.codiceCaricatore = null;
        }

    }

    /**
     * @param cassaDestinazione
     *            the cassaDestinazione to set
     */
    public void setCassaDestinazione(Cassa cassaDestinazione) {
        this.cassaDestinazione = cassaDestinazione;
        if (cassaDestinazione == null || cassaDestinazione.isNew()) {
            this.codiceCassaDestinazione = null;
        }
    }

    /**
     * @param cassaOrigine
     *            the cassaOrigine to set
     */
    public void setCassaOrigine(Cassa cassaOrigine) {
        this.cassaOrigine = cassaOrigine;
        if (cassaOrigine == null || cassaOrigine.isNew()) {
            this.codiceCassaOrigine = null;
        }
    }

    /**
     * @param codiceAzienda
     *            the codiceAzienda to set
     */
    @Override
    public void setCodiceAzienda(String codiceAzienda) {
        this.codiceAzienda = codiceAzienda;
    }

    /**
     * @param codiceCaricatore
     *            the codiceCaricatore to set
     */
    public void setCodiceCaricatore(String codiceCaricatore) {
        this.codiceCaricatore = codiceCaricatore;
    }

    /**
     * @param codiceCassaDestinazione
     *            the codiceCassaDestinazione to set
     */
    public void setCodiceCassaDestinazione(String codiceCassaDestinazione) {
        this.codiceCassaDestinazione = codiceCassaDestinazione;
    }

    /**
     * @param codiceCassaOrigine
     *            the codiceCassaOrigine to set
     */
    public void setCodiceCassaOrigine(String codiceCassaOrigine) {
        this.codiceCassaOrigine = codiceCassaOrigine;
    }

    /**
     * @param codiceDistributore
     *            the codiceDistributore to set
     */
    public void setCodiceDistributore(String codiceDistributore) {
        this.codiceDistributore = codiceDistributore;
    }

    /**
     * @param codiceInstallazione
     *            the codiceInstallazione to set
     */
    public void setCodiceInstallazione(String codiceInstallazione) {
        this.codiceInstallazione = codiceInstallazione;
    }

    /**
     * @param data
     *            the data to set
     */
    public void setData(Date data) {
        this.data = data;
    }

    /**
     * @param dataRifornimento
     *            the dataRifornimento to set
     */
    public void setDataRifornimento(Date dataRifornimento) {
        this.dataRifornimento = dataRifornimento;
    }

    /**
     * @param distributore
     *            the distributore to set
     */
    public void setDistributore(Distributore distributore) {
        this.distributore = distributore;
        if (distributore == null || distributore.isNew()) {
            this.codiceDistributore = null;
        }
    }

    /**
     * @param importo
     *            the importo to set
     */
    public void setImporto(BigDecimal importo) {
        this.importo = importo;
    }

    /**
     * @param installazione
     *            the installazione to set
     */
    public void setInstallazione(Installazione installazione) {
        this.installazione = installazione;
        if (installazione == null || installazione.isNew()) {
            this.codiceInstallazione = null;
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
     * @param progressivo
     *            the progressivo to set
     */
    public void setProgressivo(Integer progressivo) {
        this.progressivo = progressivo;
    }

    /**
     * @param reso
     *            the reso to set
     */
    public void setReso(BigDecimal reso) {
        this.reso = reso;
    }

    /**
     * @param rifornimento
     *            the rifornimento to set
     */
    public void setRifornimento(AreaMagazzino rifornimento) {
        this.rifornimento = rifornimento;
    }

    /**
     * @param righe
     *            the righe to set
     */
    public void setRighe(List<RigaLetturaSelezionatrice> righe) {
        this.righe = righe;
    }
}
