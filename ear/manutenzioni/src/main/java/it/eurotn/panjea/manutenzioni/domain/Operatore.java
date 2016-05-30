package it.eurotn.panjea.manutenzioni.domain;

import java.util.Date;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.entity.annotation.EntityConverter;
import it.eurotn.panjea.anagrafica.domain.datigeografici.DatiGeografici;
import it.eurotn.panjea.magazzino.domain.MezzoTrasporto;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;

@Entity
@Audited
@Table(name = "manu_operatori", uniqueConstraints = @UniqueConstraint(columnNames = { "codice" }) )
@EntityConverter(properties = "codice,nome,cognome")
public class Operatore extends EntityBase {

    private static final long serialVersionUID = -2555731628515929451L;

    @Column(length = 20)
    private String codiceAzienda;

    @Column(length = 10)
    private String codice;

    @Column(length = 30)
    private String nome;
    @Column(length = 30)
    private String cognome;

    @ManyToOne
    private TipoAreaMagazzino tipoAreaMagazzinoFattura;

    @Embedded
    @AssociationOverrides({
            @AssociationOverride(name = "nazione", joinColumns = @JoinColumn(name = "nazioneNascita_id") ),
            @AssociationOverride(name = "livelloAmministrativo1", joinColumns = @JoinColumn(name = "lvlNascita1_id") ),
            @AssociationOverride(name = "livelloAmministrativo2", joinColumns = @JoinColumn(name = "lvlNascita2_id") ),
            @AssociationOverride(name = "livelloAmministrativo3", joinColumns = @JoinColumn(name = "lvlNascita3_id") ),
            @AssociationOverride(name = "livelloAmministrativo4", joinColumns = @JoinColumn(name = "lvlNascita4_id") ),
            @AssociationOverride(name = "localita", joinColumns = @JoinColumn(name = "localitaNascita_id") ),
            @AssociationOverride(name = "cap", joinColumns = @JoinColumn(name = "capNascita_id") ) })
    private DatiGeografici datiGeograficiNascita;

    @Embedded
    @AssociationOverrides({
            @AssociationOverride(name = "nazione", joinColumns = @JoinColumn(name = "nazioneResidenza_id") ),
            @AssociationOverride(name = "livelloAmministrativo1", joinColumns = @JoinColumn(name = "lvlResidenza1_id") ),
            @AssociationOverride(name = "livelloAmministrativo2", joinColumns = @JoinColumn(name = "lvlResidenza2_id") ),
            @AssociationOverride(name = "livelloAmministrativo3", joinColumns = @JoinColumn(name = "lvlResidenza3_id") ),
            @AssociationOverride(name = "livelloAmministrativo4", joinColumns = @JoinColumn(name = "lvlResidenza4_id") ),
            @AssociationOverride(name = "localita", joinColumns = @JoinColumn(name = "localitaResidenza_id") ),
            @AssociationOverride(name = "cap", joinColumns = @JoinColumn(name = "capResidenza_id") ) })
    private DatiGeografici datiGeograficiResidenza;

    @Column(name = "viaResidenza", length = 50)
    private String viaResidenza;

    @Column(length = 50)
    private String documentoIdentitaTipo;

    @Column(length = 15)
    private String documentoIdentitaNumero;

    @Column(length = 15)
    private String patenteNumero;
    private Date patenteScadenza;

    @Column(length = 25)
    private String codiceFiscale;
    @Column(length = 20)
    private String telefono;

    @Column(length = 20)
    private String cellulare;

    @ManyToOne
    private MezzoTrasporto mezzoTrasporto;
    private boolean caricatore;

    private boolean tecnico;

    /**
     * Costruttore.
     */
    public Operatore() {
        caricatore = false;
        tecnico = false;
    }

    /**
     * @return the cellulare
     */
    public String getCellulare() {
        return cellulare;
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
     * @return the codiceFiscale
     */
    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    /**
     * @return the cognome
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * @return the datiGeograficiNascita
     */
    public DatiGeografici getDatiGeograficiNascita() {
        if (datiGeograficiNascita == null) {
            datiGeograficiNascita = new DatiGeografici();
        }
        return datiGeograficiNascita;
    }

    /**
     * @return the datiGeograficiResidenza
     */
    public DatiGeografici getDatiGeograficiResidenza() {
        if (datiGeograficiResidenza == null) {
            datiGeograficiResidenza = new DatiGeografici();
        }
        return datiGeograficiResidenza;
    }

    /**
     *
     * @return denominazione
     */
    public String getDenominazione() {
        return ObjectUtils.defaultIfNull(nome, "") + " " + ObjectUtils.defaultIfNull(cognome, "");
    }

    /**
     * @return the documentoIdentitaNumero
     */
    public String getDocumentoIdentitaNumero() {
        return documentoIdentitaNumero;
    }

    /**
     * @return the documentoIdentitaTipo
     */
    public String getDocumentoIdentitaTipo() {
        return documentoIdentitaTipo;
    }

    /**
     * @return the mezzoTrasporto
     */
    public MezzoTrasporto getMezzoTrasporto() {
        return mezzoTrasporto;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @return the patenteNumero
     */
    public String getPatenteNumero() {
        return patenteNumero;
    }

    /**
     * @return the patenteScadenza
     */
    public Date getPatenteScadenza() {
        return patenteScadenza;
    }

    /**
     * @return the telefono
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * @return Returns the tipoAreaMagazzinoFattura.
     */
    public TipoAreaMagazzino getTipoAreaMagazzinoFattura() {
        return tipoAreaMagazzinoFattura;
    }

    /**
     * @return the viaResidenza
     */
    public String getViaResidenza() {
        return viaResidenza;
    }

    /**
     * @return the caricatore
     */
    public boolean isCaricatore() {
        return caricatore;
    }

    /**
     * @return the tecnico
     */
    public boolean isTecnico() {
        return tecnico;
    }

    /**
     * @param caricatore
     *            the caricatore to set
     */
    public void setCaricatore(boolean caricatore) {
        this.caricatore = caricatore;
    }

    /**
     * @param cellulare
     *            the cellulare to set
     */
    public void setCellulare(String cellulare) {
        this.cellulare = cellulare;
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
     * @param codiceFiscale
     *            the codiceFiscale to set
     */
    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    /**
     * @param cognome
     *            the cognome to set
     */
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    /**
     * @param datiGeograficiNascita
     *            the datiGeograficiNascita to set
     */
    public void setDatiGeograficiNascita(DatiGeografici datiGeograficiNascita) {
        this.datiGeograficiNascita = datiGeograficiNascita;
    }

    /**
     * @param datiGeograficiResidenza
     *            the datiGeograficiResidenza to set
     */
    public void setDatiGeograficiResidenza(DatiGeografici datiGeograficiResidenza) {
        this.datiGeograficiResidenza = datiGeograficiResidenza;
    }

    /**
     * @param documentoIdentitaNumero
     *            the documentoIdentitaNumero to set
     */
    public void setDocumentoIdentitaNumero(String documentoIdentitaNumero) {
        this.documentoIdentitaNumero = documentoIdentitaNumero;
    }

    /**
     * @param documentoIdentitaTipo
     *            the documentoIdentitaTipo to set
     */
    public void setDocumentoIdentitaTipo(String documentoIdentitaTipo) {
        this.documentoIdentitaTipo = documentoIdentitaTipo;
    }

    /**
     * @param mezzoTrasporto
     *            the mezzoTrasporto to set
     */
    public void setMezzoTrasporto(MezzoTrasporto mezzoTrasporto) {
        this.mezzoTrasporto = mezzoTrasporto;
    }

    /**
     * @param nome
     *            the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @param patenteNumero
     *            the patenteNumero to set
     */
    public void setPatenteNumero(String patenteNumero) {
        this.patenteNumero = patenteNumero;
    }

    /**
     * @param patenteScadenza
     *            the patenteScadenza to set
     */
    public void setPatenteScadenza(Date patenteScadenza) {
        this.patenteScadenza = patenteScadenza;
    }

    /**
     * @param tecnico
     *            the tecnico to set
     */
    public void setTecnico(boolean tecnico) {
        this.tecnico = tecnico;
    }

    /**
     * @param telefono
     *            the telefono to set
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * @param tipoAreaMagazzinoFattura
     *            The tipoAreaMagazzinoFattura to set.
     */
    public void setTipoAreaMagazzinoFattura(TipoAreaMagazzino tipoAreaMagazzinoFattura) {
        this.tipoAreaMagazzinoFattura = tipoAreaMagazzinoFattura;
    }

    /**
     * @param viaResidenza
     *            the viaResidenza to set
     */
    public void setViaResidenza(String viaResidenza) {
        this.viaResidenza = viaResidenza;
    }
}
