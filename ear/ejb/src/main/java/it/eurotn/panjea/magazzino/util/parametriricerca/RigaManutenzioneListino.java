package it.eurotn.panjea.magazzino.util.parametriricerca;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.magazzino.domain.Listino.ETipoListino;

/**
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Entity
@Table(name = "maga_riga_manutenzione_listino")
@NamedQueries({
        @NamedQuery(name = "RigaManutenzioneListino.nuovoNumero", query = "select max(numero) from RigaManutenzioneListino where userManutenzione=:userManutenzione"),
        @NamedQuery(name = "RigaManutenzioneListino.caricaListinoScaglioni", query = "select distinct rml.listino from RigaManutenzioneListino rml where rml.userManutenzione=:userManutenzione and rml.listino.tipoListino = 1"),
        @NamedQuery(name = "RigaManutenzioneListino.count", query = "select count(id) from RigaManutenzioneListino where userManutenzione=:userManutenzione") })
public class RigaManutenzioneListino extends EntityBase implements Serializable {

    /**
     * @author giangi
     * @version 1.0, 10/nov/2010
     */
    public enum ProvenienzaDecimali {
        PARAMETRI, MANUALE, DESTINAZIONE, PROVENIENZA, ANAGRAFICA
    }

    /**
     * @author giangi
     * @version 1.0, 10/nov/2010
     */
    public enum StatoRigaManutenzioneListino {
        VALIDO, NONVALIDO
    }

    private static final long serialVersionUID = -2848305004503302425L;

    @ManyToOne(fetch = FetchType.EAGER)
    private ArticoloLite articolo;

    @Column(length = 10, nullable = false)
    @Index(name = "index_codiceAzienda")
    private String codiceAzienda;

    private Integer numero;
    @Column(length = 1000)
    private String descrizione;
    @Column(length = 255)
    private String descrizioneFiltri;
    private Integer numeroDecimaliOriginale;
    private int numeroDecimali;
    @Column(precision = 19, scale = 6)
    private BigDecimal valoreOriginale;
    @Column(precision = 19, scale = 6)
    private BigDecimal valore;
    @Column(length = 30, nullable = false)
    private String userManutenzione;
    private ProvenienzaDecimali provenienzaDecimali;

    private Double quantita;

    @ManyToOne(fetch = FetchType.EAGER)
    private Listino listino;

    /**
     * Default constructor.
     */
    public RigaManutenzioneListino() {
        super();
        init();
    }

    /**
     * @return the articolo
     * @uml.property name="articolo"
     */
    public ArticoloLite getArticolo() {
        return articolo;
    }

    /**
     * @return the codiceAzienda
     * @uml.property name="codiceAzienda"
     */
    public String getCodiceAzienda() {
        return codiceAzienda;
    }

    /**
     * @return the descrizione
     * @uml.property name="descrizione"
     */
    public String getDescrizione() {
        if (descrizione == null) {
            return "";
        }
        return descrizione;
    }

    /**
     * @return the descrizioneFiltri
     * @uml.property name="descrizioneFiltri"
     */
    public String getDescrizioneFiltri() {
        return descrizioneFiltri;
    }

    /**
     * @return the listino
     */
    public Listino getListino() {
        return listino;
    }

    /**
     * @return the numero
     * @uml.property name="numero"
     */
    public Integer getNumero() {
        return numero;
    }

    /**
     * @return the numeroDecimali
     * @uml.property name="numeroDecimali"
     */
    public int getNumeroDecimali() {
        return numeroDecimali;
    }

    /**
     * @return the numeroDecimaliOriginale
     * @uml.property name="numeroDecimaliOriginale"
     */
    public Integer getNumeroDecimaliOriginale() {
        return numeroDecimaliOriginale;
    }

    /**
     * @return the provenienzaDecimali
     * @uml.property name="provenienzaDecimali"
     */
    public ProvenienzaDecimali getProvenienzaDecimali() {
        return provenienzaDecimali;
    }

    /**
     * @return the quantita
     */
    public Double getQuantita() {
        return quantita;
    }

    /**
     * @return the statoRigaManutenzioneListino
     */
    public StatoRigaManutenzioneListino getStatoRigaManutenzioneListino() {
        StatoRigaManutenzioneListino statoRigaManutenzioneListino = StatoRigaManutenzioneListino.VALIDO;
        if (valore == null || (valore != null && valore.compareTo(BigDecimal.ZERO) < 0)) {
            statoRigaManutenzioneListino = StatoRigaManutenzioneListino.NONVALIDO;
        }
        return statoRigaManutenzioneListino;
    }

    /**
     * @return the userManutenzione
     * @uml.property name="userManutenzione"
     */
    public String getUserManutenzione() {
        return userManutenzione;
    }

    /**
     * @return the valore
     * @uml.property name="valore"
     */
    public BigDecimal getValore() {
        return valore;
    }

    /**
     * @return the valoreOriginale
     * @uml.property name="valoreOriginale"
     */
    public BigDecimal getValoreOriginale() {
        return valoreOriginale;
    }

    /**
     * Init degli oggetti.
     */
    private void init() {
        this.articolo = new ArticoloLite();
        this.numeroDecimali = 0;
    }

    /**
     * @param articolo
     *            the articolo to set
     * @uml.property name="articolo"
     */
    public void setArticolo(ArticoloLite articolo) {
        this.articolo = articolo;
    }

    /**
     * @param codiceArticolo
     *            the codiceArticolo to set
     */
    public void setCodiceArticolo(String codiceArticolo) {
        this.articolo.setCodice(codiceArticolo);
    }

    /**
     * @param codiceAzienda
     *            the codiceAzienda to set
     * @uml.property name="codiceAzienda"
     */
    public void setCodiceAzienda(String codiceAzienda) {
        this.codiceAzienda = codiceAzienda;
    }

    /**
     * @param descrizione
     *            the descrizione to set
     * @uml.property name="descrizione"
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * @param descrizioneArticolo
     *            the descrizioneArticolo to set
     */
    public void setDescrizioneArticolo(String descrizioneArticolo) {
        this.articolo.setDescrizione(descrizioneArticolo);
    }

    /**
     * @param descrizioneFiltri
     *            the descrizioneFiltri to set
     * @uml.property name="descrizioneFiltri"
     */
    public void setDescrizioneFiltri(String descrizioneFiltri) {
        this.descrizioneFiltri = descrizioneFiltri;
    }

    /**
     * @param idArticolo
     *            the idArticolo to set
     */
    public void setIdArticolo(Integer idArticolo) {
        this.articolo.setId(idArticolo);
    }

    /**
     * @param idListino
     *            the idListino to set
     */
    public void setIdListino(Integer idListino) {
        if (idListino != null) {
            this.listino = new Listino();
            this.listino.setId(idListino);
        }
    }

    /**
     * @param numero
     *            the numero to set
     * @uml.property name="numero"
     */
    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    /**
     * @param numeroDecimali
     *            the numeroDecimali to set
     * @uml.property name="numeroDecimali"
     */
    public void setNumeroDecimali(int numeroDecimali) {
        this.numeroDecimali = numeroDecimali;
    }

    /**
     * @param numeroDecimaliOriginale
     *            the numeroDecimaliOriginale to set
     * @uml.property name="numeroDecimaliOriginale"
     */
    public void setNumeroDecimaliOriginale(Integer numeroDecimaliOriginale) {
        this.numeroDecimaliOriginale = numeroDecimaliOriginale;
    }

    /**
     * @param numeroDecimaliQta
     *            the numeroDecimaliQta to set
     */
    public void setNumeroDecimaliQta(int numeroDecimaliQta) {
        this.articolo.setNumeroDecimaliQta(numeroDecimaliQta);
    }

    /**
     * @param provenienzaDecimali
     *            the provenienzaDecimali to set
     * @uml.property name="provenienzaDecimali"
     */
    public void setProvenienzaDecimali(ProvenienzaDecimali provenienzaDecimali) {
        this.provenienzaDecimali = provenienzaDecimali;
    }

    /**
     * @param quantita
     *            the quantita to set
     */
    public void setQuantita(Double quantita) {
        this.quantita = quantita;
    }

    /**
     * @param tipoListino
     *            the tipoListino to set
     */
    public void setTipoListino(Integer tipoListino) {
        if (tipoListino != null) {
            this.listino.setTipoListino(ETipoListino.values()[tipoListino]);
        }
    }

    /**
     * @param userManutenzione
     *            the userManutenzione to set
     * @uml.property name="userManutenzione"
     */
    public void setUserManutenzione(String userManutenzione) {
        this.userManutenzione = userManutenzione;
    }

    /**
     * @param valore
     *            the valore to set
     * @uml.property name="valore"
     */
    public void setValore(BigDecimal valore) {
        this.valore = valore;
    }

    /**
     * @param valoreOriginale
     *            the valoreOriginale to set
     * @uml.property name="valoreOriginale"
     */
    public void setValoreOriginale(BigDecimal valoreOriginale) {
        this.valoreOriginale = valoreOriginale;
    }

    /**
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
    public void setVersioneListino(Integer versioneListino) {
        if (versioneListino != null) {
            this.listino.setVersion(versioneListino);
        }
    }

}
