package it.eurotn.panjea.magazzino.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;

@Entity
@Audited
@Table(name = "maga_raggruppamento_articoli")
@NamedQueries({
        @NamedQuery(name = "RaggruppamentoArticoli.caricaAll", query = "select r from RaggruppamentoArticoli r ") })
public class RaggruppamentoArticoli extends EntityBase {
    private static final long serialVersionUID = 2542261525383285841L;

    @Column(length = 30, nullable = false)
    @Index(name = "descrizione")
    private String descrizione = null;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "raggruppamento", cascade = CascadeType.ALL)
    @org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<RigaRaggruppamentoArticoli> righeRaggruppamentoArticoli;

    /**
     * 
     * Costruttore.
     */
    public RaggruppamentoArticoli() {
        righeRaggruppamentoArticoli = new HashSet<RigaRaggruppamentoArticoli>();
    }

    /**
     * @return Returns the descrizione.
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * @return Returns the righeRaggruppamentoArticoli.
     */
    public Set<RigaRaggruppamentoArticoli> getRigheRaggruppamentoArticoli() {
        return righeRaggruppamentoArticoli;
    }

    /**
     * @param descrizione
     *            The descrizione to set.
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * @param righeRaggruppamentoArticoli
     *            The righeRaggruppamentoArticoli to set.
     */
    public void setRigheRaggruppamentoArticoli(Set<RigaRaggruppamentoArticoli> righeRaggruppamentoArticoli) {
        this.righeRaggruppamentoArticoli = righeRaggruppamentoArticoli;
    }

    @Override
    public String toString() {
        return "RaggruppamentoArticoli [descrizione=" + descrizione + "]";
    }

}
