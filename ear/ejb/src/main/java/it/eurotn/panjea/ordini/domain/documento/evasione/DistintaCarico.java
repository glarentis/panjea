package it.eurotn.panjea.ordini.domain.documento.evasione;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.ordini.domain.RigaArticolo;

@Audited
@Entity
@Table(name = "ordi_distinta_carico")
public class DistintaCarico extends EntityBase {
    private static final long serialVersionUID = -2213735815265230626L;

    private Date dataCreazione;

    @OneToMany(mappedBy = "distintaCarico", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<RigaDistintaCarico> righeEvasione;

    /**
     *
     * Costruttore.
     */
    public DistintaCarico() {
        dataCreazione = Calendar.getInstance().getTime();
    }

    /**
     * Costruttore.
     *
     * @param dataCreazioneParam
     *            data creazione distinta carico
     */
    public DistintaCarico(final Date dataCreazioneParam) {
        dataCreazione = dataCreazioneParam;
    }

    /**
     * Aggiunge una riga distinta carico settando le proprietà base dalla riga articolo
     *
     * @param rigaDistintaCarico
     *            rigaDistintaCarico
     * @param rigaArticolo
     *            rigaArticolo
     */
    public void addRigaMissione(RigaDistintaCarico rigaDistintaCarico, RigaArticolo rigaArticolo) {
        if (rigaDistintaCarico.isMissioneAllowed()) {
            rigaDistintaCarico.setRigaArticolo(rigaArticolo);
            rigaDistintaCarico.setDistintaCarico(this);
            rigaDistintaCarico.setQtaEvasa(0.0);
            righeEvasione.add(rigaDistintaCarico);
        }
        for (RigaDistintaCarico rigaSost : rigaDistintaCarico.getRigheSostituzione()) {
            addRigaMissione(rigaSost, rigaArticolo);
        }

    }

    /**
     * @return Returns the dataCreazione.
     */
    public Date getDataCreazione() {
        return dataCreazione;
    }

    /**
     * @return Returns the righeEvasione.
     */
    public List<RigaDistintaCarico> getRigheEvasione() {
        return righeEvasione;
    }

    /**
     * @param righeEvasione
     *            The righeEvasione to set.
     */
    public void setRigheEvasione(List<RigaDistintaCarico> righeEvasione) {
        this.righeEvasione = righeEvasione;
    }
}
