/**
 *
 */
package it.eurotn.panjea.anagrafica.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;

/**
 * Classe di dominio di SedeAzienda.
 *
 * @author adriano
 * @version 1.0, 13/dic/07
 */
@Entity
@Audited
@Table(name = "anag_sedi_azienda")
@NamedQueries({
        @NamedQuery(name = "SedeAzienda.caricaByAzienda", query = " from SedeAzienda sa where sa.azienda.id = :paramIdAzienda "),
        @NamedQuery(name = "SedeAzienda.caricaAll", query = " from SedeAzienda sa ") })
public class SedeAzienda extends EntityBase {

    private static final long serialVersionUID = 1L;

    public static final String REF = "SedeAzienda";
    public static final String PROP_SEDE = "sede";
    public static final String PROP_TIPO_SEDE = "tipoSede";
    public static final String PROP_ABILITATO = "abilitato";
    public static final String PROP_AZIENDA = "azienda";
    public static final String PROP_ID = "id";

    @ManyToOne(fetch = FetchType.LAZY)
    private Azienda azienda;

    @ManyToOne
    private TipoSedeEntita tipoSede;

    @Column(nullable = false)
    private Boolean abilitato;

    @ManyToOne(cascade = CascadeType.MERGE)
    private SedeAnagrafica sede;

    @OneToMany(mappedBy = "sedeDeposito", cascade = CascadeType.REMOVE)
    private List<Deposito> depositi;

    /**
     * Costruttore.
     */
    public SedeAzienda() {
        initialize();
    }

    /**
     * @return Returns the abilitato.
     */
    public Boolean getAbilitato() {
        return abilitato;
    }

    /**
     * @return Returns the azienda.
     */
    public Azienda getAzienda() {
        return azienda;
    }

    /**
     * @return Returns the depositi.
     */
    public List<Deposito> getDepositi() {
        return depositi;
    }

    /**
     * @return descrizoine
     */
    public String getDescrizione() {
        StringBuilder sb = new StringBuilder();

        if (sede != null) {
            sb.append(sede.getDescrizione());
            sb.append(" - ");
            sb.append(sede.getIndirizzo());
            if (sede.getDatiGeografici().getLocalita() != null) {
                sb.append(" ");
                sb.append(sede.getDatiGeografici().getLocalita().getDescrizione());
            }
        }

        return sb.toString();
    }

    /**
     * @return Returns the sede.
     */
    public SedeAnagrafica getSede() {
        return sede;
    }

    /**
     * @return Returns the tipoSede.
     */
    public TipoSedeEntita getTipoSede() {
        return tipoSede;
    }

    /**
     * Inizializza i valori di default.
     */
    private void initialize() {
        if (azienda == null) {
            azienda = new Azienda();
        }
        if (depositi == null) {
            this.depositi = new ArrayList<Deposito>();
        }
        if (tipoSede == null) {
            tipoSede = new TipoSedeEntita();
        }
        if (sede == null) {
            sede = new SedeAnagrafica();
        }
        if (abilitato == null) {
            this.abilitato = true;
        }
    }

    /**
     * @param abilitato
     *            The abilitato to set.
     */
    public void setAbilitato(Boolean abilitato) {
        this.abilitato = abilitato;
    }

    /**
     * @param azienda
     *            The azienda to set.
     */
    public void setAzienda(Azienda azienda) {
        this.azienda = azienda;
    }

    /**
     * @param depositi
     *            The depositi to set.
     */
    public void setDepositi(List<Deposito> depositi) {
        this.depositi = depositi;
    }

    /**
     * @param sede
     *            The sede to set.
     */
    public void setSede(SedeAnagrafica sede) {
        this.sede = sede;
    }

    /**
     * @param tipoSede
     *            The tipoSede to set.
     */
    public void setTipoSede(TipoSedeEntita tipoSede) {
        this.tipoSede = tipoSede;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("SedeAzienda[");
        buffer.append(super.toString());
        buffer.append(" abilitato = ").append(abilitato);
        // buffer.append(" azienda = ").append(azienda != null ? azienda.getId()
        // : null);
        // buffer.append(" sede = ").append(sede != null ? sede.getId() : null);
        buffer.append(" tipoSede = ").append(tipoSede != null ? tipoSede.getId() : null);
        buffer.append("]");
        return buffer.toString();
    }

}
