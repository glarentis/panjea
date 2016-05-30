package it.eurotn.panjea.manutenzioni.domain.documento;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.documenti.domain.ITipoAreaDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;

@Entity
@Audited
@Table(name = "manu_tipi_area_installazioni")
public class TipoAreaInstallazione extends EntityBase implements java.io.Serializable, ITipoAreaDocumento {

    private static final long serialVersionUID = -3659628695399399854L;

    private String descrizionePerStampa;

    @ManyToOne
    private TipoDocumento tipoDocumento;

    @Override
    public String getDescrizionePerStampa() {
        return descrizionePerStampa;
    }

    @Override
    public String getFormulaStandardNumeroCopie() {
        return null;
    }

    @Override
    public String getReportPath() {
        return "vending/installazioni";
    }

    @Override
    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    /**
     * @param descrizionePerStampa
     *            The descrizionePerStampa to set.
     */
    public void setDescrizionePerStampa(String descrizionePerStampa) {
        this.descrizionePerStampa = descrizionePerStampa;
    }

    /**
     * @param tipoDocumento
     *            The tipoDocumento to set.
     */
    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

}
