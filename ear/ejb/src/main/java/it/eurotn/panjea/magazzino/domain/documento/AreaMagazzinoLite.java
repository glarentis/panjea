package it.eurotn.panjea.magazzino.domain.documento;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.classedocumento.impl.ClasseFattura;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.magazzino.domain.DatiGenerazione;
import it.eurotn.panjea.magazzino.domain.RigaArticoloLite;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino.StatoAreaMagazzino;

/**
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Entity
@Audited
@Table(name = "maga_area_magazzino")
public class AreaMagazzinoLite extends EntityBase {

    private static final long serialVersionUID = -246604627995219829L;

    /**
     * @uml.property name="documento"
     * @uml.associationEnd
     */
    @OneToOne
    @Fetch(FetchMode.JOIN)
    private Documento documento;

    /**
     * @uml.property name="dataRegistrazione"
     */
    @Index(name = "dataRegistrazione")
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dataRegistrazione;

    /**
     * @uml.property name="statoAreaMagazzino"
     * @uml.associationEnd
     */
    private StatoAreaMagazzino statoAreaMagazzino;

    /**
     * @uml.property name="depositoDestinazione"
     * @uml.associationEnd
     */
    @ManyToOne
    private DepositoLite depositoDestinazione;

    /**
     * @uml.property name="depositoOrigine"
     * @uml.associationEnd
     */
    @ManyToOne
    private DepositoLite depositoOrigine;

    /**
     * @uml.property name="tipoAreaMagazzino"
     * @uml.associationEnd
     */
    @ManyToOne
    private TipoAreaMagazzino tipoAreaMagazzino;
    /**
     * @uml.property name="datiGenerazione"
     * @uml.associationEnd
     */
    @Embedded
    private DatiGenerazione datiGenerazione;

    /**
     * @uml.property name="annoMovimento"
     */
    @Index(name = "annoMovimento")
    private int annoMovimento;

    /**
     * @uml.property name="trasportoCura"
     */
    private String trasportoCura;

    private Integer idZonaGeografica;

    /**
     * Contiene la lista di tutte le righe articolo che non sono valide in base alle regole di validazione.
     *
     * @uml.property name="righeNonValide"
     */
    @Transient
    private List<RigaArticoloLite> righeNonValide;

    /**
     * Costruttore di default.
     */
    public AreaMagazzinoLite() {
        initialize();
    }

    /**
     * Aggiunge una riga non valida alla lista.
     *
     * @param rigaArticoloLite
     *            Riga non valida
     */
    public void addToRigheNonValide(RigaArticoloLite rigaArticoloLite) {
        righeNonValide.add(rigaArticoloLite);
    }

    /**
     *
     * @return proxt per l'area magazzino
     */
    public AreaMagazzino creaProxyAreaMagazzino() {
        AreaMagazzino areaMagazzino = new AreaMagazzino();
        areaMagazzino.setId(getId());
        return areaMagazzino;
    }

    /**
     * @return the annoMovimento
     * @uml.property name="annoMovimento"
     */
    public int getAnnoMovimento() {
        return annoMovimento;
    }

    /**
     * @return dataRegistrazione
     * @uml.property name="dataRegistrazione"
     */
    public Date getDataRegistrazione() {
        return dataRegistrazione;
    }

    /**
     * @return datiGenerazione
     * @uml.property name="datiGenerazione"
     */
    public DatiGenerazione getDatiGenerazione() {
        return datiGenerazione;
    }

    /**
     * @return depositoDestinazione
     * @uml.property name="depositoDestinazione"
     */
    public DepositoLite getDepositoDestinazione() {
        return depositoDestinazione;
    }

    /**
     * @return depositoOrigine
     * @uml.property name="depositoOrigine"
     */
    public DepositoLite getDepositoOrigine() {
        return depositoOrigine;
    }

    /**
     * @return documento
     * @uml.property name="documento"
     */
    public Documento getDocumento() {
        return documento;
    }

    /**
     * @return Returns the idZonaGeografica.
     */
    public Integer getIdZonaGeografica() {
        return idZonaGeografica;
    }

    /**
     * @return righeNonValide
     * @uml.property name="righeNonValide"
     */
    public List<RigaArticoloLite> getRigheNonValide() {
        return righeNonValide;
    }

    /**
     * @return statoAreaMagazzino
     * @uml.property name="statoAreaMagazzino"
     */
    public StatoAreaMagazzino getStatoAreaMagazzino() {
        return statoAreaMagazzino;
    }

    /**
     * @return tipoAreaMagazzino
     * @uml.property name="tipoAreaMagazzino"
     */
    public TipoAreaMagazzino getTipoAreaMagazzino() {
        return tipoAreaMagazzino;
    }

    /**
     * @return the trasportoCura
     * @uml.property name="trasportoCura"
     */
    public String getTrasportoCura() {
        return trasportoCura;
    }

    /**
     * Inizializza i valori di default.
     */
    private void initialize() {
        this.righeNonValide = new ArrayList<RigaArticoloLite>();
        this.documento = new Documento();
        this.tipoAreaMagazzino = new TipoAreaMagazzino();
    }

    /**
     * @return <code>true</code> se si tratta di una fattura a PA.
     */
    public boolean isFatturaPA() {
        return getDocumento() != null
                && getDocumento().getTipoDocumento().getClasseTipoDocumentoInstance() instanceof ClasseFattura
                && getDocumento().getEntita() != null && getDocumento().getEntita().isFatturazionePA()
                && (getStatoAreaMagazzino() == StatoAreaMagazzino.CONFERMATO
                        || getStatoAreaMagazzino() == StatoAreaMagazzino.FORZATO);
    }

    /**
     * @param annoMovimento
     *            the annoMovimento to set
     * @uml.property name="annoMovimento"
     */
    public void setAnnoMovimento(int annoMovimento) {
        this.annoMovimento = annoMovimento;
    }

    /**
     * @param codiceDocumento
     *            the codiceDocumento to set
     */
    public void setCodiceDocumento(String codiceDocumento) {
        this.documento.getCodice().setCodice(codiceDocumento);
    }

    /**
     * @param dataDocumento
     *            the dataDocumento to set
     */
    public void setDataDocumento(Date dataDocumento) {
        this.documento.setDataDocumento(dataDocumento);
    }

    /**
     * @param dataRegistrazione
     *            the dataRegistrazione to set
     * @uml.property name="dataRegistrazione"
     */
    public void setDataRegistrazione(Date dataRegistrazione) {
        this.dataRegistrazione = dataRegistrazione;
    }

    /**
     * @param datiGenerazione
     *            the datiGenerazione to set
     * @uml.property name="datiGenerazione"
     */
    public void setDatiGenerazione(DatiGenerazione datiGenerazione) {
        this.datiGenerazione = datiGenerazione;
    }

    /**
     * @param depositoDestinazione
     *            the depositoDestinazione to set
     * @uml.property name="depositoDestinazione"
     */
    public void setDepositoDestinazione(DepositoLite depositoDestinazione) {
        this.depositoDestinazione = depositoDestinazione;
    }

    /**
     * @param depositoOrigine
     *            the depositoOrigine to set
     * @uml.property name="depositoOrigine"
     */
    public void setDepositoOrigine(DepositoLite depositoOrigine) {
        this.depositoOrigine = depositoOrigine;
    }

    /**
     * @param documento
     *            the documento to set
     * @uml.property name="documento"
     */
    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

    /**
     * @param idDocumento
     *            the idDocumento to set
     */
    public void setIdDocumento(Integer idDocumento) {
        this.documento = new Documento();
        this.documento.setId(idDocumento);
    }

    /**
     * @param idZonaGeografica
     *            The idZonaGeografica to set.
     */
    public void setIdZonaGeografica(Integer idZonaGeografica) {
        this.idZonaGeografica = idZonaGeografica;
    }

    /**
     * @param righeNonValide
     *            the righeNonValide to set
     * @uml.property name="righeNonValide"
     */
    public void setRigheNonValide(List<RigaArticoloLite> righeNonValide) {
        this.righeNonValide = righeNonValide;
    }

    /**
     * @param statoAreaMagazzino
     *            the statoAreaMagazzino
     * @uml.property name="statoAreaMagazzino"
     */
    public void setStatoAreaMagazzino(StatoAreaMagazzino statoAreaMagazzino) {
        this.statoAreaMagazzino = statoAreaMagazzino;
    }

    /**
     * @param tipoAreaMagazzino
     *            the tipoAreaMagazzino
     * @uml.property name="tipoAreaMagazzino"
     */
    public void setTipoAreaMagazzino(TipoAreaMagazzino tipoAreaMagazzino) {
        this.tipoAreaMagazzino = tipoAreaMagazzino;
    }

    /**
     * @param trasportoCura
     *            the trasportoCura to set
     * @uml.property name="trasportoCura"
     */
    public void setTrasportoCura(String trasportoCura) {
        this.trasportoCura = trasportoCura;
    }
}
