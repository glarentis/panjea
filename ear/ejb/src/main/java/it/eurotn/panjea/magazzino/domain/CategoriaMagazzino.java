package it.eurotn.panjea.magazzino.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;

/**
 * Rappresenta una categoria articolo che può essere associata a più entità. Contiene inoltre il listino e l'eventuale
 * listino alternativo da applicare alla categoria.
 *
 * @author fattazzo
 */
@Entity
@Audited
@Table(name = "maga_categorie_magazzino")
public class CategoriaMagazzino extends EntityBase {

    private static final long serialVersionUID = -6771350512568991397L;

    /**
     * @uml.property name="codiceAzienda"
     */
    @Column(length = 10, nullable = false)
    @Index(name = "index_codiceAzienda")
    private String codiceAzienda = null;

    /**
     * @uml.property name="descrizione"
     */
    @Column(length = 100, nullable = false)
    private String descrizione;

    /**
     * @uml.property name="listino"
     * @uml.associationEnd
     */
    @ManyToOne
    private Listino listino;

    /**
     * @uml.property name="listinoAlternativo"
     * @uml.associationEnd
     */
    @ManyToOne
    private Listino listinoAlternativo;

    /**
     * @uml.property name="categoriaScontoSede"
     * @uml.associationEnd
     */
    @ManyToOne
    private CategoriaScontoSede categoriaScontoSede;

    /**
     * @uml.property name="entitas"
     */
    @OneToMany(fetch = FetchType.EAGER)
    private List<EntitaLite> entitas;

    /**
     * @return categoriaScontoSede
     * @uml.property name="categoriaScontoSede"
     */
    public CategoriaScontoSede getCategoriaScontoSede() {
        return categoriaScontoSede;
    }

    /**
     * @return codiceAzienda
     * @uml.property name="codiceAzienda"
     */
    public String getCodiceAzienda() {
        return codiceAzienda;
    }

    /**
     * @return descrizione
     * @uml.property name="descrizione"
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * @return entita
     * @uml.property name="entitas"
     */
    public List<EntitaLite> getEntitas() {
        return entitas;
    }

    /**
     * @return listino
     * @uml.property name="listino"
     */
    public Listino getListino() {
        return listino;
    }

    /**
     * @return listinoAlternativo
     * @uml.property name="listinoAlternativo"
     */
    public Listino getListinoAlternativo() {
        return listinoAlternativo;
    }

    /**
     * @param categoriaScontoSede
     *            the categoriaScontoSede to set
     * @uml.property name="categoriaScontoSede"
     */
    public void setCategoriaScontoSede(CategoriaScontoSede categoriaScontoSede) {
        this.categoriaScontoSede = categoriaScontoSede;
    }

    /**
     * @param codiceAzienda
     *            the codiceAzienda to set
     * @uml.property name="codiceAzienda"
     */
    public void setCodiceAzienda(String codiceAzienda) {
        this.codiceAzienda = codiceAzienda;
    }

    /**
     * @param descrizione
     *            the descrizione to set
     * @uml.property name="descrizione"
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * @param entitas
     *            the entitas to set
     * @uml.property name="entitas"
     */
    public void setEntitas(List<EntitaLite> entitas) {
        this.entitas = entitas;
    }

    /**
     * @param listino
     *            the listino to set
     * @uml.property name="listino"
     */
    public void setListino(Listino listino) {
        this.listino = listino;
    }

    /**
     * @param listinoAlternativo
     *            the listinoAlternativo to set
     * @uml.property name="listinoAlternativo"
     */
    public void setListinoAlternativo(Listino listinoAlternativo) {
        this.listinoAlternativo = listinoAlternativo;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("CategoriaListino[");
        buffer.append("codiceAzienda = ").append(codiceAzienda);
        buffer.append(" descrizione = ").append(descrizione);
        buffer.append(" entitas = ").append(entitas);
        buffer.append(" listino = ").append(listino);
        buffer.append(" listinoAlternativo = ").append(listinoAlternativo);
        buffer.append("]");
        return buffer.toString();
    }
}
