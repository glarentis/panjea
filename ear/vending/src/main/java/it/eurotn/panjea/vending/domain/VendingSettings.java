package it.eurotn.panjea.vending.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;

@SuppressWarnings("serial")
@Entity
@Audited
@Table(name = "vend_settings")
@NamedQueries({ @NamedQuery(name = "VendingSettings.caricaAll", query = "from VendingSettings vs", hints = {
        @QueryHint(name = "org.hibernate.cacheable", value = "true"),
        @QueryHint(name = "org.hibernate.cacheRegion", value = "vendingSettings") }) })
public class VendingSettings extends EntityBase {

    @ManyToOne
    private TipoAreaMagazzino evadtsTipoDocumentoImportazione;

    @OneToMany(mappedBy = "vendingSettings", fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
    private List<EvaDtsImportFolder> importFolder;

    /**
     * @return the evadtsTipoDocumentoImportazione
     */
    public TipoAreaMagazzino getEvadtsTipoDocumentoImportazione() {
        return evadtsTipoDocumentoImportazione;
    }

    /**
     * @return the importFolder
     */
    public List<EvaDtsImportFolder> getImportFolder() {
        return importFolder;
    }

    /**
     * @param evadtsTipoDocumentoImportazione
     *            the evadtsTipoDocumentoImportazione to set
     */
    public void setEvadtsTipoDocumentoImportazione(TipoAreaMagazzino evadtsTipoDocumentoImportazione) {
        this.evadtsTipoDocumentoImportazione = evadtsTipoDocumentoImportazione;
    }

    /**
     * @param importFolder
     *            the importFolder to set
     */
    public void setImportFolder(List<EvaDtsImportFolder> importFolder) {
        this.importFolder = importFolder;
    }

}