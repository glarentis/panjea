package it.eurotn.panjea.magazzino.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;

/**
 * La classe gestisce le varie combinazioni fra <code>CategoriaScontoSede</code> e <code>CategoriaScontoArticolo</code>
 * e il relativo sconto assegnato.<br>
 * Esiste un ordine tra le varie combinazioni di categorie e in ordine di importanza è il seguente:<br>
 * CATEGORIA SEDE - CATEGORIA ARTICOLO<br>
 * CATEGORIA SEDE - tutti gli articoli<br>
 * tutte le sedi - CATEGORIA ARTICOLO
 *
 * @author fattazzo
 */
@Entity
@Audited
@Table(name = "maga_sconti_maggiorazioni")
@NamedQueries({
        @NamedQuery(name = "ScontoMaggiorazione.caricaAll", query = "from ScontoMaggiorazione s where s.codiceAzienda = :paramCodiceAzienda") })
public class ScontoMaggiorazione extends EntityBase {

    private static final long serialVersionUID = -6113429364424717916L;

    /**
     * @uml.property name="codiceAzienda"
     */
    @Column(length = 10, nullable = false)
    @Index(name = "index_codiceAzienda")
    private String codiceAzienda = null;

    /**
     * @uml.property name="categoriaScontoSede"
     * @uml.associationEnd
     */
    @ManyToOne
    private CategoriaScontoSede categoriaScontoSede;

    /**
     * @uml.property name="categoriaScontoArticolo"
     * @uml.associationEnd
     */
    @ManyToOne
    private CategoriaScontoArticolo categoriaScontoArticolo;

    /**
     * @uml.property name="sconto"
     * @uml.associationEnd
     */
    @ManyToOne
    private Sconto sconto;

    /**
     * Costruttore.
     * 
     */
    public ScontoMaggiorazione() {
        super();
        initialize();
    }

    /**
     * @return categoriaScontoArticolo
     * @uml.property name="categoriaScontoArticolo"
     */
    public CategoriaScontoArticolo getCategoriaScontoArticolo() {
        return categoriaScontoArticolo;
    }

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
     * @return sconto
     * @uml.property name="sconto"
     */
    public Sconto getSconto() {
        return sconto;
    }

    /**
     * Inizializza i valori di default.
     */
    private void initialize() {
        this.categoriaScontoArticolo = new CategoriaScontoArticolo();
        this.categoriaScontoSede = new CategoriaScontoSede();
        this.sconto = new Sconto();
    }

    /**
     * @param categoriaScontoArticolo
     *            the categoriaScontoArticolo to set
     * @uml.property name="categoriaScontoArticolo"
     */
    public void setCategoriaScontoArticolo(CategoriaScontoArticolo categoriaScontoArticolo) {
        this.categoriaScontoArticolo = categoriaScontoArticolo;
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
     * @param sconto
     *            the sconto to set
     * @uml.property name="sconto"
     */
    public void setSconto(Sconto sconto) {
        this.sconto = sconto;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("ScontoMaggiorazione[");
        buffer.append("codice categoriaScontoArticolo = ")
                .append(categoriaScontoArticolo != null ? categoriaScontoArticolo.getId() : null);
        buffer.append(" codice categoriaScontoSede = ")
                .append(categoriaScontoSede != null ? categoriaScontoSede.getId() : null);
        buffer.append(" codice sconto = ").append(sconto.getCodice());
        buffer.append("]");
        return buffer.toString();
    }
}
