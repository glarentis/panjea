package it.eurotn.panjea.agenti.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;

/**
 * @author fattazzo
 *
 */
@Entity
@Audited
@Table(name = "agen_settings")
@NamedQueries({
        @NamedQuery(name = "AgentiSettings.caricaAll", query = "from AgentiSettings ags where ags.codiceAzienda = :codiceAzienda") })
public class AgentiSettings extends EntityBase {

    private static final long serialVersionUID = -4542682030671200L;

    @Column(length = 10, nullable = false)
    private String codiceAzienda;

    @Embedded
    private BaseProvvigionaleSettings baseProvvigionaleSettings;

    /**
     * Costruttore.
     */
    public AgentiSettings() {
        super();
    }

    /**
     * @return the baseProvvigionaleSettings
     */
    public BaseProvvigionaleSettings getBaseProvvigionaleSettings() {
        return baseProvvigionaleSettings;
    }

    /**
     * @return the codiceAzienda
     */
    public String getCodiceAzienda() {
        return codiceAzienda;
    }

    /**
     * @param baseProvvigionaleSettings
     *            the baseProvvigionaleSettings to set
     */
    public void setBaseProvvigionaleSettings(BaseProvvigionaleSettings baseProvvigionaleSettings) {
        this.baseProvvigionaleSettings = baseProvvigionaleSettings;
    }

    /**
     * @param codiceAzienda
     *            the codiceAzienda to set
     */
    public void setCodiceAzienda(String codiceAzienda) {
        this.codiceAzienda = codiceAzienda;
    }

}
