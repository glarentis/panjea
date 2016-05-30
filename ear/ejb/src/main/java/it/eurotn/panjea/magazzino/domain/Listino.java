package it.eurotn.panjea.magazzino.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.entity.annotation.EntityConverter;

/**
 * Rappresenta un listino.
 *
 * @author fattazzo
 */
@Entity
@Audited
@Table(name = "maga_listini")
@NamedQueries({
        @NamedQuery(name = "Listino.caricaAll", query = "from Listino l where l.codiceAzienda = :paramCodiceAzienda"),
        @NamedQuery(name = "Listino.caricaByTipo", query = "from Listino l where l.tipoListino = :tipoListino and l.codiceAzienda = :paramCodiceAzienda"),
        @NamedQuery(name = "Listino.caricaByListinoBase", query = "select l from Listino l where l.listinoBase.id = :idListinoBase"),
        @NamedQuery(name = "Listino.countListini", query = "select count(l.id) from Listino l where l.codiceAzienda = :paramCodiceAzienda") })
@EntityConverter(properties = "codice,descrizione")
public class Listino extends EntityBase {

    /**
     * @author giangi
     * @version 1.0, 10/nov/2010
     */
    public enum ETipoListino {
        NORMALE, SCAGLIONE
    }

    private static final long serialVersionUID = 3676983195995021863L;

    @Column(length = 10, nullable = false)
    @Index(name = "index_codiceAzienda")
    private String codiceAzienda = null;

    @Column(length = 20, nullable = false)
    private String codice;

    @Column(length = 100, nullable = false)
    private String descrizione;

    @Column(nullable = false)
    private String codiceValuta;

    /**
     * Indica se il listino deve essere preso in considerazione per l'aggiornamento dall'ultimo
     * costo.
     */
    private Boolean aggiornaDaUltimoCosto = false;

    /**
     * Indica se i prezzi del listino sono comprensivi di iva.
     */
    private boolean iva;

    private ETipoListino tipoListino;

    @OneToMany(mappedBy = "listino", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @OrderBy(value = "dataVigore")
    private List<VersioneListino> versioniListino;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "listino")
    private List<SedeMagazzino> sedi;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "listinoAlternativo")
    private List<SedeMagazzino> sediAlternative;

    @ManyToOne(optional = true)
    private Listino listinoBase;

    /**
     * Costruttore.
     */
    public Listino() {
        tipoListino = ETipoListino.NORMALE;
    }

    /**
     * @return the aggiornaDaUltimoCosto
     */
    public Boolean getAggiornaDaUltimoCosto() {
        return aggiornaDaUltimoCosto;
    }

    /**
     * @return codice
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
     * @return codiceValuta
     */
    public String getCodiceValuta() {
        return codiceValuta;
    }

    /**
     * @return descrizione
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * @return the listinoBase
     */
    public Listino getListinoBase() {
        return listinoBase;
    }

    /**
     * @return the sedi
     */
    public List<SedeMagazzino> getSedi() {
        return sedi;
    }

    /**
     * @return the sediAlternative
     */
    public List<SedeMagazzino> getSediAlternative() {
        return sediAlternative;
    }

    /**
     * @return tipoListino
     */
    public ETipoListino getTipoListino() {
        return tipoListino;
    }

    /**
     * @return versioniListino
     */
    public List<VersioneListino> getVersioniListino() {
        return versioniListino;
    }

    /**
     * @return iva
     */
    public boolean isIva() {
        return iva;
    }

    /**
     * @param aggiornaDaUltimoCosto
     *            the aggiornaDaUltimoCosto to set
     */
    public void setAggiornaDaUltimoCosto(Boolean aggiornaDaUltimoCosto) {
        this.aggiornaDaUltimoCosto = aggiornaDaUltimoCosto;
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
     * @param codiceValuta
     *            the codiceValuta to set
     */
    public void setCodiceValuta(String codiceValuta) {
        this.codiceValuta = codiceValuta;
    }

    /**
     * @param descrizione
     *            the descrizione to set
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * @param iva
     *            the iva to set
     */
    public void setIva(boolean iva) {
        this.iva = iva;
    }

    /**
     * @param listinoBase
     *            the listinoBase to set
     */
    public void setListinoBase(Listino listinoBase) {
        this.listinoBase = listinoBase;
    }

    /**
     * @param sedi
     *            the sedi to set
     */
    public void setSedi(List<SedeMagazzino> sedi) {
        this.sedi = sedi;
    }

    /**
     * @param sediAlternative
     *            the sediAlternative to set
     */
    public void setSediAlternative(List<SedeMagazzino> sediAlternative) {
        this.sediAlternative = sediAlternative;
    }

    /**
     * @param tipoListino
     *            the tipoListino to set
     */
    public void setTipoListino(ETipoListino tipoListino) {
        this.tipoListino = tipoListino;
        if (tipoListino == ETipoListino.SCAGLIONE) {
            listinoBase = null;
            aggiornaDaUltimoCosto = false;
        }
    }

    /**
     * @param versioniListino
     *            the versioniListino to set
     */
    public void setVersioniListino(List<VersioneListino> versioniListino) {
        this.versioniListino = versioniListino;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("Listino[");
        buffer.append("codice = ").append(codice);
        buffer.append(" codiceAzienda = ").append(codiceAzienda);
        buffer.append(" codiceValuta = ").append(codiceValuta);
        buffer.append(" descrizione = ").append(descrizione);
        buffer.append(" iva = ").append(iva);
        buffer.append(" tipoListino = ").append(tipoListino);
        buffer.append("]");
        return buffer.toString();
    }

}
