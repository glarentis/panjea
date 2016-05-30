package it.eurotn.panjea.magazzino.domain;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.UnitaMisura;
import it.eurotn.panjea.magazzino.domain.descrizionilingua.DescrizioneLinguaTipoAttributo;
import it.eurotn.panjea.magazzino.domain.descrizionilingua.IDescrizioneLingua;

/**
 * @author fattazzo
 */
@Entity
@Audited
@Table(name = "maga_tipo_attributo", uniqueConstraints = @UniqueConstraint(columnNames = { "codice",
        "codiceazienda" }) )
@org.hibernate.annotations.Table(appliesTo = "maga_tipo_attributo", indexes = {
        @Index(name = "IdxCodice", columnNames = { "codice", "codiceazienda" }) })
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPOLOGIA", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("A")
@NamedQueries({
        @NamedQuery(name = "TipoAttributo.caricaAll", query = "select DISTINCT ta from TipoAttributo ta LEFT JOIN FETCH ta.nomiLingua where ta.codiceAzienda = :paramCodiceAzienda"),
        @NamedQuery(name = "TipoAttributo.caricaByCodice", query = "select DISTINCT ta from TipoAttributo ta LEFT JOIN FETCH ta.nomiLingua where ta.codiceAzienda = :paramCodiceAzienda and ta.codice=:paramCodice") })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "tipoAttributo")
public class TipoAttributo extends EntityBase implements IDescrizioneFactory {

    /**
     * @author giangi
     * @version 1.0, 10/nov/2010
     */
    public enum ETipoDatoTipoAttributo {
        NUMERICO(Double.class), STRINGA(String.class), BOOLEANO(Boolean.class);

        private Class<?> javaType;

        /**
         * Costruttore.
         *
         * @param javaType
         */
        private ETipoDatoTipoAttributo(final Class<?> javaType) {
            this.javaType = javaType;
        }

        /**
         * @return the javaType
         */
        public Class<?> getJavaType() {
            return javaType;
        }
    }

    private static final long serialVersionUID = -5030832402767605558L;

    /**
     * Pattern per la ricerca tramite RegEx dell'attributo all'intersonyno di una stringa.
     */
    public static final String PATTERN_SEARCH = "\\$[^\\$\\$]+\\$";

    public static final String SEPARATORE_CODICE_FORMULA = "$";

    public static final String ATTRIBUTO_QTA_CODICE_FORMULA = SEPARATORE_CODICE_FORMULA + "qta"
            + SEPARATORE_CODICE_FORMULA;

    /**
     * Questi tipi attributi sono calcolati quindi non vengono inseriti dall'utente.
     */
    public static final String[] TIPIATTRIBUTOCALCOLATI = new String[] { "qta" };

    @Column(length = 10, nullable = false)
    @Index(name = "index_codiceAzienda")
    private String codiceAzienda = null;

    @Column(length = 10)
    private String codice;

    /**
     * Nome in lingua aziendale.
     */
    private String nomeLinguaAziendale;

    @Column(nullable = false)
    private ETipoDatoTipoAttributo tipoDato;

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    @MapKey(name = "codiceLingua")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    @Fetch(FetchMode.JOIN)
    private Map<String, DescrizioneLinguaTipoAttributo> nomiLingua;

    @ManyToOne
    private UnitaMisura unitaMisura;

    private Integer numeroDecimali;

    private TotalizzatoreTipoAttributo totalizzatoreTipoAttributo;

    /**
     * Inizializza i valori di default.
     */
    {
        this.numeroDecimali = 0;
        this.totalizzatoreTipoAttributo = TotalizzatoreTipoAttributo.NESSUNO;
    }

    /**
     * Aggiunge una descrizione in lingua.
     *
     * @param nomeLinguaTipoAttributo
     *            descrizione
     */
    public void addNomeLingua(DescrizioneLinguaTipoAttributo nomeLinguaTipoAttributo) {
        if (nomiLingua == null) {
            nomiLingua = new HashMap<String, DescrizioneLinguaTipoAttributo>();
        }
        nomiLingua.put(nomeLinguaTipoAttributo.getCodiceLingua(), nomeLinguaTipoAttributo);
    }

    @Override
    public IDescrizioneLingua createDescrizioneLingua() {
        return new DescrizioneLinguaTipoAttributo();
    }

    /**
     * @return nome in lingua aziendale
     */
    public String getCodice() {
        return codice;
    }

    /**
     * @return codiceAzienda
     */
    public String getCodiceAzienda() {
        return codiceAzienda;
    }

    /**
     * Restituisce il valore del codice per inserirlo nella formula.<br>
     *
     * @return codice
     */
    public String getCodiceFormula() {
        return SEPARATORE_CODICE_FORMULA + codice + SEPARATORE_CODICE_FORMULA;
    }

    /**
     * Restituisce la descrizione per la lingua specificata dal metodo getCodiceLingua se esiste altrimenti quella
     * aziendale.
     *
     * @param lingua
     *            lingua per la descrizione
     *
     * @return descrizione in lingua
     */
    public String getDescrizione(String lingua) {
        StringBuilder descrizione = new StringBuilder();

        if (lingua != null && nomiLingua.containsKey(lingua)) {
            descrizione.append(nomiLingua.get(lingua).getDescrizione());
        } else {
            descrizione.append(nomeLinguaAziendale);
        }

        if (unitaMisura != null) {
            descrizione.append(" [" + unitaMisura.getCodice() + "]");
        }
        return descrizione.toString();
    }

    /**
     * @return descrizione per la lingua aziendale.
     */
    public String getNome() {
        return nomeLinguaAziendale;
    }

    /**
     * @return nomiLingua
     */
    public Map<String, DescrizioneLinguaTipoAttributo> getNomiLingua() {
        return nomiLingua;
    }

    /**
     * @return pattern da utilizzare per la formattazione del tipo attributo se numerico, '' altrimenti
     */
    public String getNumberFormatPattern() {

        if (tipoDato == null || tipoDato != ETipoDatoTipoAttributo.NUMERICO) {
            return "";
        }

        int dec = numeroDecimali != null ? numeroDecimali : 0;

        return "###,###,###,##0" + (dec != 0 ? "." : "") + "0000000000000000000000000".substring(0, dec);
    }

    /**
     * @return the numeroDecimali
     */
    public Integer getNumeroDecimali() {
        if (numeroDecimali == null) {
            return 0;
        }
        return numeroDecimali;
    }

    /**
     * @return tipoDato
     */
    public ETipoDatoTipoAttributo getTipoDato() {
        return tipoDato;
    }

    /**
     * @return the totalizzatoreTipoAttributo
     */
    public TotalizzatoreTipoAttributo getTotalizzatoreTipoAttributo() {
        return totalizzatoreTipoAttributo;
    }

    /**
     * @return Returns the unitaMisura.
     */
    public UnitaMisura getUnitaMisura() {
        return unitaMisura;
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
     * @param nome
     *            the nome to set
     */
    public void setNome(String nome) {
        this.nomeLinguaAziendale = nome;
    }

    /**
     * @param nomiLingua
     *            the nomiLingua to set
     */
    public void setNomiLingua(Map<String, DescrizioneLinguaTipoAttributo> nomiLingua) {
        this.nomiLingua = nomiLingua;
    }

    /**
     * @param numeroDecimali
     *            the numeroDecimali to set
     */
    public void setNumeroDecimali(Integer numeroDecimali) {
        this.numeroDecimali = numeroDecimali;
    }

    /**
     * @param tipoDato
     *            the tipoDato to set
     */
    public void setTipoDato(ETipoDatoTipoAttributo tipoDato) {
        this.tipoDato = tipoDato;
    }

    /**
     * @param totalizzatoreTipoAttributo
     *            the totalizzatoreTipoAttributo to set
     */
    public void setTotalizzatoreTipoAttributo(TotalizzatoreTipoAttributo totalizzatoreTipoAttributo) {
        this.totalizzatoreTipoAttributo = totalizzatoreTipoAttributo;
    }

    /**
     * @param unitaMisura
     *            The unitaMisura to set.
     */
    public void setUnitaMisura(UnitaMisura unitaMisura) {
        this.unitaMisura = unitaMisura;
    }
}
