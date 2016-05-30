package it.eurotn.panjea.magazzino.domain.moduloprezzo;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.ZonaGeografica;
import it.eurotn.panjea.magazzino.domain.TipoMezzoTrasporto;

/**
 * Listino che determina il prezzo di un certo tipo mezzo per una definita zona geografica.
 *
 * @author Leonardo
 */
@Entity
@Audited
@Table(name = "maga_listino_tipo_mezzo_zona_geografica", uniqueConstraints = @UniqueConstraint(columnNames = {
        "codiceAzienda", "tipoMezzoTrasporto_id", "zonaGeografica_id" }) )
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "datiCommerciali")
@NamedQueries({
        @NamedQuery(name = "ListinoTipoMezzoZonaGeografica.caricaAll", query = "select ltmzg from ListinoTipoMezzoZonaGeografica ltmzg where ltmzg.codiceAzienda = :paramCodiceAzienda"),
        @NamedQuery(name = "ListinoTipoMezzoZonaGeografica.caricaByTipoMezzoEZona", query = "select listino from ListinoTipoMezzoZonaGeografica listino where listino.codiceAzienda = :paramCodiceAzienda and listino.tipoMezzoTrasporto.id = :paramTipoMezzoId and listino.zonaGeografica.id = :paramZonaGeograficaId") })
public class ListinoTipoMezzoZonaGeografica extends EntityBase {

    private static final long serialVersionUID = 2349713645208022757L;

    @ManyToOne
    private TipoMezzoTrasporto tipoMezzoTrasporto;

    @ManyToOne
    private ZonaGeografica zonaGeografica;

    @Column(precision = 19, scale = 6)
    private BigDecimal prezzo;

    @Column(length = 10, nullable = false)
    @Index(name = "index_codiceAzienda")
    private String codiceAzienda;

    /**
     * Decimali per arrotondare il prezzo.
     */
    @Column(nullable = false)
    private Integer numeroDecimaliPrezzo;

    /**
     * Costruttore di default.
     */
    public ListinoTipoMezzoZonaGeografica() {
        super();
        initialize();
    }

    /**
     * @return the codiceAzienda
     */
    public String getCodiceAzienda() {
        return codiceAzienda;
    }

    /**
     * @return the numeroDecimaliPrezzo
     */
    public Integer getNumeroDecimaliPrezzo() {
        return numeroDecimaliPrezzo;
    }

    /**
     * @return the prezzo
     */
    public BigDecimal getPrezzo() {
        return prezzo;
    }

    /**
     * @return the tipoMezzoTrasporto
     */
    public TipoMezzoTrasporto getTipoMezzoTrasporto() {
        return tipoMezzoTrasporto;
    }

    /**
     * @return the zonaGeografica
     */
    public ZonaGeografica getZonaGeografica() {
        return zonaGeografica;
    }

    /**
     * Init degli attributi di listino.
     */
    private void initialize() {
        numeroDecimaliPrezzo = new Integer(2);
    }

    /**
     * @param codiceAzienda
     *            the codiceAzienda to set
     */
    public void setCodiceAzienda(String codiceAzienda) {
        this.codiceAzienda = codiceAzienda;
    }

    /**
     * @param numeroDecimaliPrezzo
     *            the numeroDecimaliPrezzo to set
     */
    public void setNumeroDecimaliPrezzo(Integer numeroDecimaliPrezzo) {
        this.numeroDecimaliPrezzo = numeroDecimaliPrezzo;
    }

    /**
     * @param prezzo
     *            the prezzo to set
     */
    public void setPrezzo(BigDecimal prezzo) {
        // arrotondo il prezzo per sicurezza
        this.prezzo = prezzo;
        if (prezzo != null) {
            this.prezzo = prezzo.setScale(numeroDecimaliPrezzo, RoundingMode.HALF_UP);
        }
    }

    /**
     * @param tipoMezzoTrasporto
     *            the tipoMezzoTrasporto to set
     */
    public void setTipoMezzoTrasporto(TipoMezzoTrasporto tipoMezzoTrasporto) {
        this.tipoMezzoTrasporto = tipoMezzoTrasporto;
    }

    /**
     * @param zonaGeografica
     *            the zonaGeografica to set
     */
    public void setZonaGeografica(ZonaGeografica zonaGeografica) {
        this.zonaGeografica = zonaGeografica;
    }

}
