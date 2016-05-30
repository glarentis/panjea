package it.eurotn.panjea.anagrafica.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.datigeografici.DatiGeografici;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;

/**
 * Classe di domain che rappresenta i depositi dell'azienda.
 *
 * @author adriano
 * @version 1.0, 13/dic/07
 */
@Entity
@Audited
@Table(name = "anag_depositi")
@NamedQueries({
        @NamedQuery(name = "Deposito.caricaDepositoPrincipaleAzienda", query = " select dep from Deposito dep where dep.sedeDeposito.azienda.id = :paramIdAzienda and dep.principale = true  ", hints = {
                @QueryHint(name = "org.hibernate.cacheable", value = "true"),
                @QueryHint(name = "org.hibernate.cacheRegion", value = "depositi") }),
        @NamedQuery(name = "Deposito.caricaDepositoPerCodice", query = "select dep from Deposito dep where dep.sedeDeposito.azienda.codice = :paramCodiceAzienda and dep.codice = :paramCodiceDeposito ") })
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "depositi")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO", discriminatorType = DiscriminatorType.STRING, length = 2)
@DiscriminatorValue("DE")
public class Deposito extends EntityBase {
    private static final long serialVersionUID = -5571311524249372061L;
    public static final String REF = "Deposito";
    public static final String PROP_INDIRIZZO = "indirizzo";
    public static final String PROP_SEDEDEPOSITO = "sedeDeposito";
    public static final String PROP_ATTIVO = "attivo";
    public static final String PROP_ID = "id";
    public static final String PROP_DESCRIZIONE = "descrizione";
    public static final String PROP_CODICE = "codice";
    public static final String PROP_PRINCIPALE = "principale";
    public static final String PROP_TIPODEPOSITO = "tipoDeposito";

    @Column(length = 10, nullable = false)
    @Index(name = "codice")
    private String codice;

    @Column(length = 130)
    private String descrizione;

    @Column(length = 30)
    private String indirizzo;

    @Column(length = 10, nullable = false)
    @Index(name = "index_codiceAzienda")
    private String codiceAzienda = null;

    @Column
    private Boolean attivo;

    @Embedded
    private DatiGeografici datiGeografici;

    @ManyToOne(optional = false)
    private SedeAzienda sedeDeposito;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private TipoDeposito tipoDeposito;

    @Column
    private boolean principale;

    @ManyToOne
    private EntitaLite entita;

    @ManyToOne
    private SedeEntita sedeEntita;

    /**
     * Costruttore.
     */
    public Deposito() {
        this.initialize();
    }

    @Override
    public int compareTo(EntityBase obj) {
        if (obj instanceof Deposito) {
            Deposito objCompare = (Deposito) obj;
            String baseValue = codice + descrizione;
            String compareValue = objCompare.getCodice() + objCompare.getDescrizione();
            return baseValue.compareTo(compareValue);
        }
        return 1;
    }

    /**
     * crea un depositoLite apartire di un deposito.
     *
     * @return depositoLite
     */
    public DepositoLite creaLite() {
        DepositoLite depositoLite = new DepositoLite();
        depositoLite.setCodice(this.getCodice());
        depositoLite.setCodiceAzienda(this.getCodiceAzienda());
        depositoLite.setDescrizione(this.getDescrizione());
        depositoLite.setId(this.getId());
        depositoLite.setPrincipale(this.isPrincipale());
        depositoLite.setTipoDeposito(this.getTipoDeposito());
        depositoLite.setVersion(this.getVersion());
        depositoLite.setUserInsert(this.getUserInsert());
        return depositoLite;

    }

    /**
     * @return Returns the attivo.
     */
    public Boolean getAttivo() {
        return attivo;
    }

    /**
     * @return Returns the codice.
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
     * @return the datiGeografici
     */
    public DatiGeografici getDatiGeografici() {
        if (datiGeografici == null) {
            datiGeografici = new DatiGeografici();
        }

        return datiGeografici;
    }

    /**
     * Crea un deposito lite dal deposito.
     *
     * @return {@link DepositoLite} creato
     */
    public DepositoLite getDepositoLite() {
        DepositoLite depositoLite = new DepositoLite();
        depositoLite.setCodice(this.getCodice());
        depositoLite.setCodiceAzienda(this.getCodiceAzienda());
        depositoLite.setDescrizione(this.getDescrizione());
        depositoLite.setId(this.getId());
        depositoLite.setPrincipale(this.isPrincipale());
        depositoLite.setTipoDeposito(this.getTipoDeposito());
        depositoLite.setVersion(this.getVersion());
        return depositoLite;
    }

    /**
     * @return Returns the descrizione.
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * @return the entita
     */
    public EntitaLite getEntita() {
        return entita;
    }

    /**
     * @return Returns the indirizzo.
     */
    public String getIndirizzo() {
        return indirizzo;
    }

    /**
     * @return Returns the sedeDeposito.
     */
    public SedeAzienda getSedeDeposito() {
        return sedeDeposito;
    }

    /**
     * @return the sedeEntita
     */
    public SedeEntita getSedeEntita() {
        return sedeEntita;
    }

    /**
     * @return TipoDeposito.
     */
    public TipoDeposito getTipoDeposito() {
        return tipoDeposito;
    }

    /**
     * init dei valori.
     */
    private void initialize() {
        this.attivo = Boolean.TRUE;
        this.sedeDeposito = new SedeAzienda();
        this.datiGeografici = new DatiGeografici();
    }

    /**
     * @return Returns the principale.
     */
    public boolean isPrincipale() {
        return principale;
    }

    /**
     * @param attivo
     *            The attivo to set.
     */
    public void setAttivo(Boolean attivo) {
        this.attivo = attivo;
    }

    /**
     * @param codice
     *            The codice to set.
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
     * @param datiGeografici
     *            the datiGeografici to set
     */
    public void setDatiGeografici(DatiGeografici datiGeografici) {
        this.datiGeografici = datiGeografici;
    }

    /**
     * @param descrizione
     *            The descrizione to set.
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * @param entita
     *            the entita to set
     */
    public void setEntita(EntitaLite entita) {
        this.entita = entita;
    }

    /**
     * @param indirizzo
     *            The indirizzo to set.
     */
    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    /**
     * @param principale
     *            The principale to set.
     */
    public void setPrincipale(boolean principale) {
        this.principale = principale;
    }

    /**
     * @param sedeDeposito
     *            The sedeDeposito to set.
     */
    public void setSedeDeposito(SedeAzienda sedeDeposito) {
        this.sedeDeposito = sedeDeposito;
    }

    /**
     * @param sedeEntita
     *            the sedeEntita to set
     */
    public void setSedeEntita(SedeEntita sedeEntita) {
        this.sedeEntita = sedeEntita;
    }

    /**
     * @param tipoDeposito
     *            tipoDeposito
     */
    public void setTipoDeposito(TipoDeposito tipoDeposito) {
        this.tipoDeposito = tipoDeposito;
    }

    /**
     * @return toString della classe
     */
    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("Deposito[");
        buffer.append(super.toString());
        buffer.append(" codice = ").append(codice);
        buffer.append(" attivo = ").append(attivo);
        buffer.append(" descrizione = ").append(descrizione);
        buffer.append(" indirizzo = ").append(indirizzo);
        buffer.append(" sedeDeposito = ").append(sedeDeposito != null ? sedeDeposito.getId() : null);
        buffer.append(" tipoDeposito = ").append(tipoDeposito != null ? tipoDeposito.getId() : null);
        buffer.append(" principale = ").append(principale);
        buffer.append("]");
        return buffer.toString();
    }
}
