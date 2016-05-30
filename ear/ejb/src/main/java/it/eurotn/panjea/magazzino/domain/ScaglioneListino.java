package it.eurotn.panjea.magazzino.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import it.eurotn.entity.EntityBase;

@Entity
@Audited
@Table(name = "maga_scaglioni_listini")
public class ScaglioneListino extends EntityBase {
    private static final long serialVersionUID = -5039510422774058141L;

    public static final Double MAX_SCAGLIONE = 999999999.0;
    public static final String MAX_SCAGLIONE_LABEL = "OLTRE ";
    private Double quantita;

    {
        quantita = MAX_SCAGLIONE;
        prezzo = BigDecimal.ZERO;
        note = new ArrayList<ScaglioneListinoNota>();
    }

    @Column(precision = 19, scale = 6)
    private BigDecimal prezzo;

    @ManyToOne(optional = false)
    private RigaListino rigaListino;

    @OneToMany(mappedBy = "scaglioneListino", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotAudited
    private List<ScaglioneListinoNota> note;

    /**
     * @return Returns the nota.
     */
    public String getNota() {
        String nota = "";
        if (this.note != null && !this.note.isEmpty()) {
            nota = this.note.get(0).getNote();
        }
        return nota;
    }

    /**
     * @return Returns the prezzo.
     */
    public BigDecimal getPrezzo() {
        return prezzo;
    }

    /**
     * @return Returns the quantita.
     */
    public Double getQuantita() {
        return quantita;
    }

    /**
     * @return Returns the rigaListino.
     */
    public RigaListino getRigaListino() {
        return rigaListino;
    }

    /**
     * @param nota
     *            the nota to set
     */
    public void setNota(String nota) {
        ScaglioneListinoNota scaglioneListinoNota = new ScaglioneListinoNota();
        scaglioneListinoNota.setNote(nota);
        scaglioneListinoNota.setScaglioneListino(this);
        if (this.note.isEmpty()) {
            this.note.add(scaglioneListinoNota);
        } else {
            this.note.set(0, scaglioneListinoNota);
        }
    }

    /**
     * @param prezzo
     *            The prezzo to set.
     */
    public void setPrezzo(BigDecimal prezzo) {
        this.prezzo = prezzo;
        int numeroDec = 0;
        if (rigaListino != null && rigaListino.getNumeroDecimaliPrezzo() != null) {
            numeroDec = rigaListino.getNumeroDecimaliPrezzo();
        }
        if (prezzo != null) {
            this.prezzo = this.prezzo.setScale(numeroDec, RoundingMode.HALF_UP);
        }
    }

    /**
     * @param quantita
     *            The quantita to set.
     */
    public void setQuantita(Double quantita) {
        this.quantita = quantita;
    }

    /**
     * @param rigaListino
     *            The rigaListino to set.
     */
    public void setRigaListino(RigaListino rigaListino) {
        this.rigaListino = rigaListino;
    }

    @Override
    public String toString() {
        return "ScaglioneListino [quantita=" + quantita + ", prezzo=" + prezzo + "]";
    }
}
