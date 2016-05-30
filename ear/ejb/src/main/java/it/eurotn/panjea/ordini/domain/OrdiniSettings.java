/**
 *
 */
package it.eurotn.panjea.ordini.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCarico;
import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCaricoSelezioneManuale;

/**
 * @author fattazzo
 *
 */
@NamedQueries({
        @NamedQuery(name = "OrdiniSettings.caricaAll", query = "from OrdiniSettings os where os.codiceAzienda = :codiceAzienda", hints = {
                @QueryHint(name = "org.hibernate.cacheable", value = "true"),
                @QueryHint(name = "org.hibernate.cacheRegion", value = "ordiniSettings") }) })
@Entity
@Audited
@Table(name = "ordi_settings")
public class OrdiniSettings extends EntityBase {

    private static final long serialVersionUID = 4076101160999140496L;

    @Column(length = 10, nullable = false)
    private String codiceAzienda;

    private boolean creazioneMissioniAbilitata;

    private String attributiMissioni;

    private boolean selezioneMissioniDaEvadereManuale;

    {
        creazioneMissioniAbilitata = false;
        selezioneMissioniDaEvadereManuale = true;
    }

    /**
     * @return Returns the attributiMissioni.
     */
    public String getAttributiMissioni() {
        return attributiMissioni;
    }

    /**
     *
     * @return classe per la selezione delle missioni
     */
    public Class<? extends RigaDistintaCarico> getClassForRigaDistintaCarico() {
        if (selezioneMissioniDaEvadereManuale) {
            return RigaDistintaCaricoSelezioneManuale.class;
        }
        return RigaDistintaCarico.class;
    }

    /**
     * @return the codiceAzienda
     */
    public String getCodiceAzienda() {
        return codiceAzienda;
    }

    /**
     * @return the creazioneMissioniAbilitata
     */
    public boolean isCreazioneMissioniAbilitata() {
        return creazioneMissioniAbilitata;
    }

    /**
     * @return Returns the selezioneMissioniDaEvadereManuale.
     */
    public boolean isSelezioneMissioniDaEvadereManuale() {
        return selezioneMissioniDaEvadereManuale;
    }

    /**
     * @param attributiMissioni
     *            The attributiMissioni to set.
     */
    public void setAttributiMissioni(String attributiMissioni) {
        this.attributiMissioni = attributiMissioni;
    }

    /**
     * @param codiceAzienda
     *            the codiceAzienda to set
     */
    public void setCodiceAzienda(String codiceAzienda) {
        this.codiceAzienda = codiceAzienda;
    }

    /**
     * @param creazioneMissioniAbilitata
     *            the creazioneMissioniAbilitata to set
     */
    public void setCreazioneMissioniAbilitata(boolean creazioneMissioniAbilitata) {
        this.creazioneMissioniAbilitata = creazioneMissioniAbilitata;
    }

    /**
     * @param selezioneMissioniDaEvadereManuale
     *            The selezioneMissioniDaEvadereManuale to set.
     */
    public void setSelezioneMissioniDaEvadereManuale(boolean selezioneMissioniDaEvadereManuale) {
        this.selezioneMissioniDaEvadereManuale = selezioneMissioniDaEvadereManuale;
    }
}
