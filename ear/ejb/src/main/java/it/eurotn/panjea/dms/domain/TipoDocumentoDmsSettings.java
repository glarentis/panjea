package it.eurotn.panjea.dms.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;

@Entity
@Audited
@Table(name = "dms_settings_tipi_documento")
public class TipoDocumentoDmsSettings extends EntityBase {

    private static final long serialVersionUID = -423917879145427043L;

    @ManyToOne
    private DmsSettings dmsSettings;

    @ManyToOne
    private TipoDocumento tipoDocumento;

    private String folderPattern;

    /**
     * @return the dmsSettings
     */
    public DmsSettings getDmsSettings() {
        return dmsSettings;
    }

    /**
     * @return the folderPattern
     */
    public String getFolderPattern() {
        return folderPattern;
    }

    /**
     * @return the tipoDocumento
     */
    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    /**
     * @param dmsSettings
     *            the dmsSettings to set
     */
    public void setDmsSettings(DmsSettings dmsSettings) {
        this.dmsSettings = dmsSettings;
    }

    /**
     * @param folderPattern
     *            the folderPattern to set
     */
    public void setFolderPattern(String folderPattern) {
        this.folderPattern = folderPattern;
    }

    /**
     * @param tipoDocumento
     *            the tipoDocumento to set
     */
    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }
}
