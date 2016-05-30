package it.eurotn.panjea.magazzino.domain;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

import it.eurotn.panjea.anagrafica.domain.Importo;

/**
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Embeddable
public class TotaliArea implements Serializable {

    private static final long serialVersionUID = -1256042849101635821L;

    /**
     * @uml.property name="speseTrasporto"
     * @uml.associationEnd
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "importoInValuta", column = @Column(name = "importoInValutaSpeseTrasporto", precision = 19, scale = 6) ),
            @AttributeOverride(name = "importoInValutaAzienda", column = @Column(name = "importoInValutaAziendaSpeseTrasporto", precision = 19, scale = 6) ),
            @AttributeOverride(name = "tassoDiCambio", column = @Column(name = "tassoDiCambioSpeseTrasporto", precision = 12, scale = 6) ),
            @AttributeOverride(name = "codiceValuta", column = @Column(name = "codiceValutaSpeseTrasporto", length = 3) ) })
    private Importo speseTrasporto;

    /**
     * @uml.property name="altreSpese"
     * @uml.associationEnd
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "importoInValuta", column = @Column(name = "importoInValutaAltreSpese", precision = 19, scale = 6) ),
            @AttributeOverride(name = "importoInValutaAzienda", column = @Column(name = "importoInValutaAziendaAltreSpese", precision = 19, scale = 6) ),
            @AttributeOverride(name = "tassoDiCambio", column = @Column(name = "tassoDiCambioAltreSpese", precision = 12, scale = 6) ),
            @AttributeOverride(name = "codiceValuta", column = @Column(name = "codiceValutaAltreSpese", length = 3) ) })
    private Importo altreSpese;

    /**
     * @uml.property name="totaleMerce"
     * @uml.associationEnd
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "importoInValuta", column = @Column(name = "importoInValutaTotaleMerce", precision = 19, scale = 6) ),
            @AttributeOverride(name = "importoInValutaAzienda", column = @Column(name = "importoInValutaAziendaTotaleMerce", precision = 19, scale = 6) ),
            @AttributeOverride(name = "tassoDiCambio", column = @Column(name = "tassoDiCambioTotaleMerce", precision = 12, scale = 6) ),
            @AttributeOverride(name = "codiceValuta", column = @Column(name = "codiceValutaTotaleMerce", length = 3) ) })
    private Importo totaleMerce;

    /**
     * Costruttore.
     * 
     */
    public TotaliArea() {
        super();
        speseTrasporto = new Importo();
        altreSpese = new Importo();
        totaleMerce = new Importo();
    }

    /**
     * @return the altreSpese
     * @uml.property name="altreSpese"
     */
    public Importo getAltreSpese() {
        return altreSpese;
    }

    /**
     * @return the speseTrasporto
     * @uml.property name="speseTrasporto"
     */
    public Importo getSpeseTrasporto() {
        return speseTrasporto;
    }

    /**
     * @return the totaleMerce
     * @uml.property name="totaleMerce"
     */
    public Importo getTotaleMerce() {
        return totaleMerce;
    }

    /**
     * @param altreSpese
     *            the altreSpese to set
     * @uml.property name="altreSpese"
     */
    public void setAltreSpese(Importo altreSpese) {
        this.altreSpese = altreSpese;
    }

    /**
     * @param speseTrasporto
     *            the speseTrasporto to set
     * @uml.property name="speseTrasporto"
     */
    public void setSpeseTrasporto(Importo speseTrasporto) {
        this.speseTrasporto = speseTrasporto;
    }

    /**
     * @param totaleMerce
     *            the totaleMerce to set
     * @uml.property name="totaleMerce"
     */
    public void setTotaleMerce(Importo totaleMerce) {
        this.totaleMerce = totaleMerce;
    }
}
