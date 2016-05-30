package it.eurotn.panjea.magazzino.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import it.eurotn.util.PanjeaEJBUtil;

@Entity
@Audited
@Table(name = "maga_attributi_categorie")
@NamedQueries({
        @NamedQuery(name = "AttributoCategoria.caricaByCategorie", query = "select a from AttributoCategoria a where a.categoria in (:categorie)") })
public class AttributoCategoria extends AttributoMagazzino {

    public enum TipoAggiornamento {
        NESSUNO, TUTTI, UGUALI
    }

    private static final long serialVersionUID = -5621715913611196933L;

    @ManyToOne(optional = false)
    private Categoria categoria;

    private boolean consideraSottoCategorie;

    private TipoAggiornamento tipoAggiornamento;

    /**
     * Costruttore.
     *
     */
    public AttributoCategoria() {
        setTipoAttributo(new TipoAttributo());
        this.consideraSottoCategorie = Boolean.TRUE;
        this.tipoAggiornamento = TipoAggiornamento.TUTTI;
    }

    /**
     *
     * Costruttore.
     *
     * @param attributoForCopy
     *            attributo dal quale copiare i valori
     */
    public AttributoCategoria(final AttributoCategoria attributoForCopy) {
        super();
        PanjeaEJBUtil.copyProperties(this, attributoForCopy);
        setCategoria(null);
        setId(null);
        setVersion(null);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (this == obj) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AttributoCategoria other = (AttributoCategoria) obj;
        if (categoria == null) {
            if (other.categoria != null) {
                return false;
            }
        } else if (categoria.getId() != null && other.categoria != null && other.categoria.getId() != null
                && !categoria.getId().equals(other.categoria.getId())) {
            return false;
        }
        if (getTipoAttributo() == null) {
            if (other.getTipoAttributo() != null) {
                return false;
            }
        } else if (!getTipoAttributo().equals(other.getTipoAttributo())) {
            return false;
        }
        return true;
    }

    /**
     * @return the categoria
     */
    public Categoria getCategoria() {
        return categoria;
    }

    /**
     * @return the tipoAggiornamento
     */
    public TipoAggiornamento getTipoAggiornamento() {
        return tipoAggiornamento;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((categoria == null) ? 0 : categoria.hashCode());
        result = prime * result + ((getTipoAttributo() == null) ? 0 : getTipoAttributo().hashCode());
        return result;
    }

    /**
     * @return the consideraSottoCategorie
     */
    public boolean isConsideraSottoCategorie() {
        return consideraSottoCategorie;
    }

    /**
     * Verifica se la formula dell'attributo è cambiata.
     *
     * @param attributoCategoria
     *            attributo da confrontare
     * @return <code>true</code> se la formula è cambiata
     */
    public boolean isFormulaChanged(AttributoCategoria attributoCategoria) {

        if (this.getFormula() == null) {
            return attributoCategoria.getFormula() != null;
        } else {
            return !this.getFormula().equals(attributoCategoria.getFormula());
        }
    }

    /**
     * Verifica se il valore dell'attributo è cambiato.
     *
     * @param attributoCategoria
     *            attributo da confrontare
     * @return <code>true</code> se il valore è cambiato
     */
    public boolean isValoreAttributoChanged(AttributoCategoria attributoCategoria) {

        if (this.getValore() == null) {
            return attributoCategoria.getValore() != null;
        } else {
            return !this.getValore().equals(attributoCategoria.getValore());
        }
    }

    /**
     * @param categoria
     *            the categoria to set
     */
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    /**
     * @param consideraSottoCategorie
     *            the consideraSottoCategorie to set
     */
    public void setConsideraSottoCategorie(boolean consideraSottoCategorie) {
        this.consideraSottoCategorie = consideraSottoCategorie;
    }

    /**
     * @param tipoAggiornamento
     *            the tipoAggiornamento to set
     */
    public void setTipoAggiornamento(TipoAggiornamento tipoAggiornamento) {
        this.tipoAggiornamento = tipoAggiornamento;
    }
}
