package it.eurotn.panjea.anagrafica.domain;

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
import javax.persistence.Transient;

import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Nazione;
import it.eurotn.panjea.audit.envers.AuditableProperties;

@Entity
@Audited
@Table(name = "anag_anagrafica")
@org.hibernate.annotations.Table(appliesTo = "anag_anagrafica", indexes = {
        @Index(name = "idxAziendaDenominazione", columnNames = { "codice_azienda", "denominazione" }) })
@NamedQueries({
        @NamedQuery(name = "Anagrafica.caricaBySedeAnagrafica", query = " from Anagrafica a where a.sedeAnagrafica.id = :paramIdSedeAnagrafica "),
        @NamedQuery(name = "Anagrafica.caricaAll", query = " from Anagrafica a where a.codiceAzienda = :paramCodiceAzienda "),
        @NamedQuery(name = "Anagrafica.cancellaOrfane", query = "delete from Anagrafica a where a.entita.size=0"),
        @NamedQuery(name = "Anagrafica.caricaByPartitaIva", query = " from Anagrafica a where a.codiceAzienda = :paramCodiceAzienda and a.partiteIVA like :paramPartiteIVA  ") })
@AuditableProperties(properties = { "sedeAnagrafica" })
public class Anagrafica extends EntityBase {

    private static final long serialVersionUID = 7181327871766944954L;
    public static final String REF = "Anagrafica";
    public static final String PROP_CODICE_AZIENDA = "codiceAzienda";
    public static final String PROP_DENOMINAZIONE = "denominazione";
    public static final String PROP_CODICE_FISCALE = "codiceFiscale";
    public static final String PROP_SEDE_ANAGRAFICA = "sedeAnagrafica";
    public static final String PROP_PARTITE_I_V_A = "partiteIVA";
    public static final String PROP_ID = "id";

    @Index(name = "azienda")
    @Column(name = "codice_azienda", length = 20)
    private java.lang.String codiceAzienda;

    @Index(name = "denominazione")
    @Column(name = "denominazione", length = 100)
    private java.lang.String denominazione;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "anagrafica")
    private List<Entita> entita;

    @Index(name = "partita_iva")
    @Column(name = "partita_iva", length = 25)
    private java.lang.String partiteIVA;

    @Index(name = "codice_fiscale")
    @Column(name = "codice_fiscale", length = 25)
    private java.lang.String codiceFiscale;

    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinColumn(name = "sede_anagrafica_id")
    private SedeAnagrafica sedeAnagrafica;

    @Transient
    private String denominazionePrecedente = null;

    @Embedded
    private PersonaFisica personaFisica;

    @Embedded
    private LegaleRappresentante legaleRappresentante;

    /**
     * costruttore.
     */
    public Anagrafica() {
        this.initialize();
    }

    /**
     * @return codice azienda.
     */
    public java.lang.String getCodiceAzienda() {
        return codiceAzienda;
    }

    /**
     * @return codice fiscale.
     */
    public java.lang.String getCodiceFiscale() {
        return codiceFiscale;
    }

    /**
     * @return denominazione.
     */
    public java.lang.String getDenominazione() {
        return denominazione;
    }

    /**
     * @return denominazione precedente.
     */
    public String getDenominazionePrecedente() {
        return denominazionePrecedente;
    }

    /**
     * @return Returns the entita.
     */
    public List<Entita> getEntita() {
        return entita;
    }

    /**
     * @return the legaleRappresentante
     */
    public LegaleRappresentante getLegaleRappresentante() {
        if (legaleRappresentante == null) {
            legaleRappresentante = new LegaleRappresentante();
        }

        return legaleRappresentante;
    }

    /**
     * @return partita iva.
     */
    public java.lang.String getPartiteIVA() {
        return partiteIVA;
    }

    /**
     * @return partita iva intra.
     */
    public java.lang.String getPartiteIVAIntra() {
        String partitaIvaIntra = partiteIVA;

        try {
            if (getSedeAnagrafica() != null && getSedeAnagrafica().getDatiGeografici() != null
                    && getSedeAnagrafica().getDatiGeografici().getNazione() != null) {
                Nazione nazione = getSedeAnagrafica().getDatiGeografici().getNazione();
                if (!"IT".equals(nazione.getCodice().toUpperCase()) && nazione.isIntra()) {
                    partitaIvaIntra = nazione.getCodice() + partiteIVA;
                }
            }
        } catch (Exception e) {
            partitaIvaIntra = partiteIVA;
        }

        return partitaIvaIntra;
    }

    /**
     * @return Returns the personaFisica.
     */
    public PersonaFisica getPersonaFisica() {
        if (personaFisica == null) {
            personaFisica = new PersonaFisica();
        }
        return personaFisica;
    }

    /**
     * @return sede annagrafica.
     */
    public SedeAnagrafica getSedeAnagrafica() {
        return sedeAnagrafica;
    }

    /**
     * init.
     */
    private void initialize() {
        this.sedeAnagrafica = new SedeAnagrafica();
    }

    /**
     * @param codiceAzienda
     *            the codiceAzienda to set
     */
    public void setCodiceAzienda(java.lang.String codiceAzienda) {
        this.codiceAzienda = codiceAzienda;
    }

    /**
     * @param codiceFiscale
     *            the codiceFiscale to set
     */
    public void setCodiceFiscale(java.lang.String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    /**
     * @param denominazione
     *            the denominazione to set
     */
    public void setDenominazione(java.lang.String denominazione) {
        this.denominazionePrecedente = this.denominazione;
        this.denominazione = denominazione;
    }

    /**
     * @param legaleRappresentante
     *            the legaleRappresentante to set
     */
    public void setLegaleRappresentante(LegaleRappresentante legaleRappresentante) {
        this.legaleRappresentante = legaleRappresentante;
    }

    /**
     * @param partiteIVA
     *            the partiteIVA to set
     */
    public void setPartiteIVA(java.lang.String partiteIVA) {
        this.partiteIVA = partiteIVA;
    }

    /**
     * @param personaFisica
     *            The personaFisica to set.
     */
    public void setPersonaFisica(PersonaFisica personaFisica) {
        this.personaFisica = personaFisica;
    }

    /**
     * @param sedeAnagrafica
     *            the sedeAnagrafica to set
     */
    public void setSedeAnagrafica(SedeAnagrafica sedeAnagrafica) {
        this.sedeAnagrafica = sedeAnagrafica;
    }

    /**
     * @return string.
     */
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Anagrafica[");
        buffer.append(super.toString());
        buffer.append("codiceAzienda = ").append(codiceAzienda);
        buffer.append(" codiceFiscale = ").append(codiceFiscale);
        buffer.append(" denominazione = ").append(denominazione);
        buffer.append(" partiteIVA = ").append(partiteIVA);
        buffer.append(" sedeAnagrafica = ").append(sedeAnagrafica != null ? sedeAnagrafica.getId() : null);
        buffer.append("]");
        return buffer.toString();
    }
}
