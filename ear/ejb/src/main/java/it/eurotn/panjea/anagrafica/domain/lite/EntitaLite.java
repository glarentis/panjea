/**
 *
 */
package it.eurotn.panjea.anagrafica.domain.lite;

import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.entity.annotation.ExcludeFromQueryBuilder;
import it.eurotn.panjea.agenti.domain.Agente;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.Cliente;
import it.eurotn.panjea.anagrafica.domain.ClientePotenziale;
import it.eurotn.panjea.anagrafica.domain.ContrattoSpesometro;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.Fornitore;
import it.eurotn.panjea.anagrafica.domain.SedeAnagrafica;
import it.eurotn.panjea.anagrafica.domain.Vettore;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Cap;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo1;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo2;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo3;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo4;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Localita;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Nazione;
import it.eurotn.panjea.anagrafica.util.EntitaDocumento;
import it.eurotn.panjea.conai.domain.ConaiEsenzione;
import it.eurotn.panjea.magazzino.domain.BloccoSede;
import it.eurotn.util.PanjeaEJBUtil;

/**
 * EntitaLite di base da cui estendere entita particolari.
 *
 * @author adriano
 * @version 1.0, 05/giu/07
 */
@Entity
@Audited
@Table(name = "anag_entita")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO_ANAGRAFICA", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("E")
@NamedQueries({
        @NamedQuery(name = "EntitaLite.caricaByAnagrafica", query = " from EntitaLite e where e.anagrafica.id = :paramIdAnagrafica "), })
public class EntitaLite extends EntityBase {

    private static Logger logger = Logger.getLogger(EntitaLite.class);

    private static final long serialVersionUID = 1L;

    @Column(length = 15)
    private String codiceEsterno;

    private Integer codice;

    @Column(name = "abilitato")
    private boolean abilitato;

    private boolean raggruppaEffetti;

    @ManyToOne
    @JoinColumn(name = "anagrafica_id")
    private AnagraficaLite anagrafica;

    @Column(precision = 12, scale = 6)
    private BigDecimal fido;

    private boolean assortimentoArticoli;

    @Column(length = 1000)
    private String noteStampa;

    @Transient
    private String tipo;

    @ExcludeFromQueryBuilder
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "entita")
    private Set<ContrattoSpesometro> contratti;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "entita")
    private Set<ConaiEsenzione> esenzioni;

    @Embedded
    private BloccoSede bloccoSede;

    // campi necessari per la fatturazione alle pubbliche amministrazioni
    @Column(length = 17)
    private String codiceEori;

    @Column(length = 50)
    private String codiceIdentificativoFiscale;

    private boolean fatturazionePA;

    @Lob
    private String noteFatturaPA;

    /**
     * Default constructor.
     */
    public EntitaLite() {
        super();
        initialize();
    }

    /**
     * Resitituisce l'entita come istanza della classe specificata.
     *
     * @param clazz
     *            classe
     * @return entita restituita
     */
    public EntitaLite convertToEntitaLite(Class<EntitaLite> clazz) {

        EntitaLite entitaLite = null;
        try {
            entitaLite = clazz.newInstance();
        } catch (Exception e) {
            logger.error("--> errore, impossibile istanziare la classe " + clazz.getName(), e);
            throw new RuntimeException("errore, impossibile istanziare la classe " + clazz.getName(), e);
        }
        PanjeaEJBUtil.copyProperties(entitaLite, this);

        return entitaLite;
    }

    /**
     * Metodo che crea una entita documento da this.
     *
     * @return {@link EntitaDocumento}
     */
    public EntitaDocumento creaEntitaDocumento() {
        EntitaDocumento entitaDocumento = new EntitaDocumento();
        entitaDocumento.setId(getId());
        entitaDocumento.setCodice(getCodice());
        entitaDocumento.setDescrizione(getAnagrafica().getDenominazione());
        if ("C".equals(getTipo())) {
            entitaDocumento.setTipoEntita(TipoEntita.CLIENTE);
        } else if ("F".equals(getTipo())) {
            entitaDocumento.setTipoEntita(TipoEntita.FORNITORE);
        } else if ("V".equals(getTipo())) {
            entitaDocumento.setTipoEntita(TipoEntita.VETTORE);
        } else if ("CP".equals(getTipo())) {
            entitaDocumento.setTipoEntita(TipoEntita.CLIENTE_POTENZIALE);
        } else if ("A".equals(getTipo())) {
            entitaDocumento.setTipoEntita(TipoEntita.AGENTE);
        }

        return entitaDocumento;
    }

    /**
     * Metodo che crea un proxy dell'entita' da this lite.
     *
     * @return Entita
     */
    public Entita creaProxyEntita() {
        Entita entita = null;
        if ("C".equals(getTipo())) {
            entita = new Cliente();
        } else if ("F".equals(getTipo())) {
            entita = new Fornitore();
        } else if ("V".equals(getTipo())) {
            entita = new Vettore();
        } else if ("CP".equals(getTipo())) {
            entita = new ClientePotenziale();
        } else if ("A".equals(getTipo())) {
            entita = new Agente();
        } else {
            throw new UnsupportedOperationException("Il tipo " + getTipo() + " non è supportato per il tipo entità");
        }
        entita.setId(getId());
        entita.setVersion(getVersion());
        entita.setCodice(getCodice());
        entita.getAnagrafica().setId(getAnagrafica().getId());
        entita.getAnagrafica().setVersion(getAnagrafica().getVersion());
        entita.getAnagrafica().setSedeAnagrafica(getAnagrafica().getSedeAnagrafica());
        entita.getAnagrafica().setDenominazione(getAnagrafica().getDenominazione());
        return entita;
    }

    /**
     * @return Returns the anagrafica.
     */
    public AnagraficaLite getAnagrafica() {
        return anagrafica;
    }

    /**
     * @return the bloccoSede
     */
    public BloccoSede getBloccoSede() {
        if (bloccoSede == null) {
            bloccoSede = new BloccoSede();
        }

        return bloccoSede;
    }

    /**
     * @return the codice
     */
    public Integer getCodice() {
        return codice;
    }

    /**
     * @return the codiceEori
     */
    public String getCodiceEori() {
        return codiceEori;
    }

    public String getCodiceEsterno() {
        return codiceEsterno;
    }

    /**
     * @return the codiceIdentificativoFiscale
     */
    public String getCodiceIdentificativoFiscale() {
        return codiceIdentificativoFiscale;
    }

    /**
     * @return the contratti
     */
    public Set<ContrattoSpesometro> getContratti() {
        return contratti;
    }

    /**
     * @return la string che rappresenta il riassunto dei dati geografici principali, località e indirizzo.
     */
    public String getDescrizioneDatiGeografici() {

        String localita = anagrafica.getSedeAnagrafica().getDatiGeografici().getDescrizioneLocalita();
        String indirizzo = anagrafica.getSedeAnagrafica().getIndirizzo();
        String separatore = (localita == null || indirizzo == null) ? "" : " - ";

        localita = (localita == null) ? "" : localita;
        indirizzo = (indirizzo == null) ? "" : indirizzo;

        return localita + separatore + indirizzo;
    }

    /**
     * @return the esenzioni
     */
    public Set<ConaiEsenzione> getEsenzioni() {
        return esenzioni;
    }

    /**
     * @return the fido
     */
    public BigDecimal getFido() {
        return fido;
    }

    /**
     * @return the noteFatturaPA
     */
    public String getNoteFatturaPA() {
        return noteFatturaPA;
    }

    /**
     * @return the noteStampa
     */
    public String getNoteStampa() {
        return noteStampa;
    }

    /**
     * Implementato nelle classi ereditate ne definisce il valore per l'ordinamento.
     *
     * @return int
     */
    public int getOrdine() {
        throw new UnsupportedOperationException("Ordine nullo");
    }

    /**
     * Implementato nelle classi ereditate ne definisce il valore per il tipo.
     *
     * @return Tipo dell'entità (discriminatore)
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Inizializza gli oggetti che non devono rimanere null.
     */
    private void initialize() {
        this.anagrafica = new AnagraficaLite();
        this.anagrafica.setSedeAnagrafica(new SedeAnagrafica());
        this.anagrafica.getSedeAnagrafica().getDatiGeografici().setLocalita(new Localita());
        this.anagrafica.getSedeAnagrafica().getDatiGeografici().setNazione(new Nazione());
        this.anagrafica.getSedeAnagrafica().getDatiGeografici().setCap(new Cap());
        this.anagrafica.getSedeAnagrafica().getDatiGeografici().setLivelloAmministrativo1(new LivelloAmministrativo1());
        this.anagrafica.getSedeAnagrafica().getDatiGeografici().setLivelloAmministrativo2(new LivelloAmministrativo2());
        this.anagrafica.getSedeAnagrafica().getDatiGeografici().setLivelloAmministrativo3(new LivelloAmministrativo3());
        this.anagrafica.getSedeAnagrafica().getDatiGeografici().setLivelloAmministrativo4(new LivelloAmministrativo4());
    }

    /**
     * @return Returns the abilitato.
     */
    public boolean isAbilitato() {
        return abilitato;
    }

    /**
     * @return the assortimentoArticoli
     */
    public boolean isAssortimentoArticoli() {
        return assortimentoArticoli;
    }

    /**
     * @return the fatturazionePA
     */
    public boolean isFatturazionePA() {
        return fatturazionePA;
    }

    /**
     * @return Returns the raggruppaEffetti.
     */
    public boolean isRaggruppaEffetti() {
        return raggruppaEffetti;
    }

    /**
     * @param abilitato
     *            The abilitato to set.
     */
    public void setAbilitato(boolean abilitato) {
        this.abilitato = abilitato;
    }

    /**
     * @param anagrafica
     *            The anagrafica to set.
     */
    public void setAnagrafica(AnagraficaLite anagrafica) {
        this.anagrafica = anagrafica;
    }

    /**
     * @param assortimentoArticoli
     *            the assortimentoArticoli to set
     */
    public void setAssortimentoArticoli(boolean assortimentoArticoli) {
        this.assortimentoArticoli = assortimentoArticoli;
    }

    /**
     * @param bloccoSede
     *            the bloccoSede to set
     */
    public void setBloccoSede(BloccoSede bloccoSede) {
        this.bloccoSede = bloccoSede;
    }

    /**
     * @param codice
     *            the codice to set
     */
    public void setCodice(Integer codice) {
        this.codice = codice;
    }

    /**
     * @param codiceEori
     *            the codiceEori to set
     */
    public void setCodiceEori(String codiceEori) {
        this.codiceEori = codiceEori;
    }

    public void setCodiceEsterno(String codiceEsterno) {
        this.codiceEsterno = codiceEsterno;
    }

    /**
     * @param codiceFiscale
     *            The codiceFiscale to set.
     */
    public void setCodiceFiscale(String codiceFiscale) {
        this.getAnagrafica().setCodiceFiscale(codiceFiscale);
    }

    /**
     * @param codiceIdentificativoFiscale
     *            the codiceIdentificativoFiscale to set
     */
    public void setCodiceIdentificativoFiscale(String codiceIdentificativoFiscale) {
        this.codiceIdentificativoFiscale = codiceIdentificativoFiscale;
    }

    /**
     * @param contratti
     *            the contratti to set
     */
    public void setContratti(Set<ContrattoSpesometro> contratti) {
        this.contratti = contratti;
    }

    /**
     * @param denominazione
     *            The denominazione to set.
     */
    public void setDenominazione(String denominazione) {
        this.getAnagrafica().setDenominazione(denominazione);
    }

    /**
     * @param descrizioneCAP
     *            the descrizioneCAP to set
     */
    public void setDescrizioneCAP(String descrizioneCAP) {
        this.getAnagrafica().getSedeAnagrafica().getDatiGeografici().getCap().setDescrizione(descrizioneCAP);
    }

    /**
     * @param descrizioneLocalita
     *            the descrizioneLocalita to set
     */
    public void setDescrizioneLocalita(String descrizioneLocalita) {
        this.getAnagrafica().getSedeAnagrafica().getDatiGeografici().getLocalita().setDescrizione(descrizioneLocalita);
    }

    /**
     * @param descrizioneNazione
     *            the descrizioneNazione to set
     */
    public void setDescrizioneNazione(String descrizioneNazione) {
        this.getAnagrafica().getSedeAnagrafica().getDatiGeografici().getNazione().setDescrizione(descrizioneNazione);
    }

    /**
     * @param esenzioni
     *            the esenzioni to set
     */
    public void setEsenzioni(Set<ConaiEsenzione> esenzioni) {
        this.esenzioni = esenzioni;
    }

    /**
     * @param fatturazionePA
     *            the fatturazionePA to set
     */
    public void setFatturazionePA(boolean fatturazionePA) {
        this.fatturazionePA = fatturazionePA;
    }

    /**
     * @param fax
     *            The fax to set.
     */
    public void setFaxSede(String fax) {
        this.getAnagrafica().getSedeAnagrafica().setFax(fax);
    }

    /**
     * @param fido
     *            the fido to set
     */
    public void setFido(BigDecimal fido) {
        this.fido = fido;
    }

    /**
     * @param idAnagrafica
     *            The idAnagrafica to set.
     */
    public void setIdAnagrafica(Integer idAnagrafica) {
        this.getAnagrafica().setId(idAnagrafica);
    }

    /**
     * @param idSedeAnagrafica
     *            The idSedeAnagrafica to set.
     */
    public void setIdSedeAnagrafica(Integer idSedeAnagrafica) {
        this.getAnagrafica().getSedeAnagrafica().setId(idSedeAnagrafica);
    }

    /**
     * @param indirizzoSede
     *            The indirizzoSede to set.
     */
    public void setIndirizzoSede(String indirizzoSede) {
        this.getAnagrafica().getSedeAnagrafica().setIndirizzo(indirizzoSede);
    }

    /**
     * @param liv1Nome
     *            the liv1Nome to set
     */
    public void setLiv1Nome(String liv1Nome) {
        this.getAnagrafica().getSedeAnagrafica().getDatiGeografici().getLivelloAmministrativo1().setNome(liv1Nome);
    }

    /**
     * @param liv2Nome
     *            the liv2Nome to set
     */
    public void setLiv2Nome(String liv2Nome) {
        this.getAnagrafica().getSedeAnagrafica().getDatiGeografici().getLivelloAmministrativo2().setNome(liv2Nome);
    }

    /**
     * @param liv3Nome
     *            the liv3Nome to set
     */
    public void setLiv3Nome(String liv3Nome) {
        this.getAnagrafica().getSedeAnagrafica().getDatiGeografici().getLivelloAmministrativo3().setNome(liv3Nome);
    }

    /**
     * @param liv4Nome
     *            the liv4Nome to set
     */
    public void setLiv4Nome(String liv4Nome) {
        this.getAnagrafica().getSedeAnagrafica().getDatiGeografici().getLivelloAmministrativo4().setNome(liv4Nome);
    }

    /**
     * @param noteFatturaPA
     *            the noteFatturaPA to set
     */
    public void setNoteFatturaPA(String noteFatturaPA) {
        this.noteFatturaPA = noteFatturaPA;
    }

    /**
     * @param noteStampa
     *            the noteStampa to set
     */
    public void setNoteStampa(String noteStampa) {
        this.noteStampa = noteStampa;
    }

    /**
     * @param partitaIVA
     *            The partitaIVA to set.
     */
    public void setPartitaIVA(String partitaIVA) {
        this.getAnagrafica().setPartiteIVA(partitaIVA);
    }

    /**
     * @param raggruppaEffetti
     *            The raggruppaEffetti to set.
     */
    public void setRaggruppaEffetti(boolean raggruppaEffetti) {
        this.raggruppaEffetti = raggruppaEffetti;
    }

    /**
     * @param telefono
     *            The telefono to set.
     */
    public void setTelefonoSede(String telefono) {
        this.getAnagrafica().getSedeAnagrafica().setTelefono(telefono);
    }

    /**
     * @param tipo
     *            the tipo to set
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * @param versionAnagrafica
     *            The versionAnagrafica to set.
     */
    public void setVersionAnagrafica(Integer versionAnagrafica) {
        this.getAnagrafica().setVersion(versionAnagrafica);
    }

    /**
     * @param versionSedeAnagrafica
     *            The versionSedeAnagrafica to set.
     */
    public void setVersionSedeAnagrafica(Integer versionSedeAnagrafica) {
        this.getAnagrafica().getSedeAnagrafica().setVersion(versionSedeAnagrafica);
    }
}
