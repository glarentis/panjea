package it.eurotn.panjea.magazzino.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

/**
 * Contiene le proprietà per un attributo legato ad un articolo.<BR>
 * Nel caso l'attributo sia legato ad un {@link TipoVariante} in valore viene inserito il valore della variante scelta
 * in lingua azienda
 *
 * @author fattazzo
 */
@Entity
@Audited
@Table(name = "maga_attributi_articoli")
@NamedQueries({
        @NamedQuery(name = "AttributoArticolo.caricaByArticolo", query = "select a from AttributoArticolo a where a.articolo.id = :paramIdArticolo order by  a.ordine") })
public class AttributoArticolo extends AttributoMagazzino {

    private static final long serialVersionUID = -9189975998499894699L;

    @ManyToOne(optional = false)
    private Articolo articolo;

    /**
     * Costruttore.
     */
    public AttributoArticolo() {
        setTipoAttributo(new TipoAttributo());
    }

    /**
     * @return articolo
     */
    public Articolo getArticolo() {
        return articolo;
    }

    /**
     * @param articolo
     *            the articolo to set
     */
    public void setArticolo(Articolo articolo) {
        this.articolo = articolo;
    }

}
