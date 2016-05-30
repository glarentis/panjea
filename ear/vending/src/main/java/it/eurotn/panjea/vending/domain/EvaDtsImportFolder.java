package it.eurotn.panjea.vending.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;

@Entity
@Audited
@Table(name = "vend_settings_evadtds_import_folder")
public class EvaDtsImportFolder extends EntityBase {

    public enum EvaDtsFieldIDContent {
        CODICE_INSTALLAZIONE, MATRICOLA, IDEVADTS
    }

    public enum EvaDtsFieldIDName {
        ID101, ID106
    }

    private static final long serialVersionUID = 2700316694596236142L;

    @ManyToOne
    private VendingSettings vendingSettings;

    @Column(length = 500, nullable = false)
    private String folder;

    @Column(nullable = false)
    private EvaDtsFieldIDName fieldIDName;

    @Column(nullable = false)
    private EvaDtsFieldIDContent fieldIDContent;

    // Indica se il valore del campo id contiene 2 volte il valore
    private boolean gestioneValoreIDDoppio;

    private boolean calcolaCampoCA302;

    /**
     * Costruttore.
     */
    public EvaDtsImportFolder() {
        super();
        fieldIDName = EvaDtsFieldIDName.ID101;
        gestioneValoreIDDoppio = false;
        fieldIDContent = EvaDtsFieldIDContent.MATRICOLA;
    }

    /**
     * @return the fieldIDContent
     */
    public EvaDtsFieldIDContent getFieldIDContent() {
        return fieldIDContent;
    }

    /**
     * @return the fieldIDName
     */
    public EvaDtsFieldIDName getFieldIDName() {
        return fieldIDName;
    }

    /**
     * @return the folder
     */
    public String getFolder() {
        return folder;
    }

    /**
     * @return the vendingSettings
     */
    public VendingSettings getVendingSettings() {
        return vendingSettings;
    }

    /**
     * @return the calcolaCampoCA302
     */
    public boolean isCalcolaCampoCA302() {
        return calcolaCampoCA302;
    }

    /**
     * @return the gestioneValoreIDDoppio
     */
    public boolean isGestioneValoreIDDoppio() {
        return gestioneValoreIDDoppio;
    }

    /**
     * @param calcolaCampoCA302
     *            the calcolaCampoCA302 to set
     */
    public void setCalcolaCampoCA302(boolean calcolaCampoCA302) {
        this.calcolaCampoCA302 = calcolaCampoCA302;
    }

    /**
     * @param fieldIDContent
     *            the fieldIDContent to set
     */
    public void setFieldIDContent(EvaDtsFieldIDContent fieldIDContent) {
        this.fieldIDContent = fieldIDContent;
    }

    /**
     * @param fieldIDName
     *            the fieldIDName to set
     */
    public void setFieldIDName(EvaDtsFieldIDName fieldIDName) {
        this.fieldIDName = fieldIDName;
    }

    /**
     * @param folder
     *            the folder to set
     */
    public void setFolder(String folder) {
        this.folder = folder;
    }

    /**
     * @param gestioneValoreIDDoppio
     *            the gestioneValoreIDDoppio to set
     */
    public void setGestioneValoreIDDoppio(boolean gestioneValoreIDDoppio) {
        this.gestioneValoreIDDoppio = gestioneValoreIDDoppio;
    }

    /**
     * @param vendingSettings
     *            the vendingSettings to set
     */
    public void setVendingSettings(VendingSettings vendingSettings) {
        this.vendingSettings = vendingSettings;
    }

}
