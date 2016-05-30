package it.eurotn.panjea.manutenzioni.domain.documento;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.IStatoDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.ITipoAreaDocumento;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.audit.envers.AuditableProperties;
import it.eurotn.panjea.documenti.domain.StatoSpedizione;

@Entity
@Audited
@Table(name = "manu_area_installazioni")
@AuditableProperties(properties = { "documento" })
public class AreaInstallazione extends EntityBase implements IAreaDocumento {
    public enum StatoAreaInstallazione implements IStatoDocumento {
        CONFERMATO(false), PROVVISORIO(true);
        private boolean provvisorio;

        /**
         * Costruttore.
         *
         * @param provvisorio
         *            indica se è uno stato provvisorio
         */
        private StatoAreaInstallazione(final boolean provvisorio) {
            this.provvisorio = provvisorio;
        }

        @Override
        public boolean isProvvisorio() {
            return provvisorio;
        }
    }

    private static final long serialVersionUID = -6478497851353495509L;

    @Formula("(select am.id from maga_area_magazzino am WHERE am.documento_id=documento_id)")
    @NotAudited
    private Integer idAreaMagazzino;

    @OneToMany(mappedBy = "areaInstallazione", fetch = FetchType.LAZY)
    private List<RigaInstallazione> righeInstallazione;

    @OneToOne
    private Documento documento;

    @Column(length = 300)
    private String note;

    @ManyToOne
    private TipoAreaInstallazione tipoAreaInstallazione;

    @ManyToOne
    private DepositoLite depositoOrigine;

    private StatoAreaInstallazione stato;

    /**
     * Costruttore
     */
    public AreaInstallazione() {
        stato = StatoAreaInstallazione.PROVVISORIO;
        documento = new Documento();
        tipoAreaInstallazione = new TipoAreaInstallazione();
    }

    @Override
    public Map<String, Object> fillVariables() {
        return new HashMap<String, Object>();
    }

    /**
     * @return Returns the depositoOrigine.
     */
    public DepositoLite getDepositoOrigine() {
        return depositoOrigine;
    }

    @Override
    public Documento getDocumento() {
        return documento;
    }

    /**
     * @return Returns the idAreaMagazzino.
     */
    public Integer getIdAreaMagazzino() {
        return idAreaMagazzino;
    }

    /**
     * @return Returns the note.
     */
    public String getNote() {
        return note;
    }

    /**
     * @return Returns the righeInstallazione.
     */
    public List<RigaInstallazione> getRigheInstallazione() {
        return righeInstallazione;
    }

    @Override
    public IStatoDocumento getStato() {
        return stato;
    }

    @Override
    public StatoSpedizione getStatoSpedizione() {
        return null;
    }

    @Override
    public ITipoAreaDocumento getTipoAreaDocumento() {
        return tipoAreaInstallazione;
    }

    /**
     * @param depositoOrigine
     *            The depositoOrigine to set.
     */
    public void setDepositoOrigine(DepositoLite depositoOrigine) {
        this.depositoOrigine = depositoOrigine;
    }

    @Override
    public void setDocumento(Documento documentoParam) {
        documento = documentoParam;
    }

    /**
     * @param idAreaMagazzino
     *            The idAreaMagazzino to set.
     */
    public void setIdAreaMagazzino(Integer idAreaMagazzino) {
        this.idAreaMagazzino = idAreaMagazzino;
    }

    /**
     * @param note
     *            The note to set.
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * @param righeInstallazione
     *            The righeInstallazione to set.
     */
    public void setRigheInstallazione(List<RigaInstallazione> righeInstallazione) {
        this.righeInstallazione = righeInstallazione;
    }

    @Override
    public void setStatoSpedizione(StatoSpedizione statoSpedizione) {
        // stato spedizione non usato
    }

    @Override
    public void setTipoAreaDocumento(ITipoAreaDocumento tipoAreaDocumento) {
        this.tipoAreaInstallazione = (TipoAreaInstallazione) tipoAreaDocumento;
    }

}
