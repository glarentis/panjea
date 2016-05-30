package it.eurotn.panjea.magazzino.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.hibernate.envers.Audited;

/**
 * Specializzazione della classe <code>CategoriaSconto</code> per gestire le categorie sconto degli articoli.
 *
 * @author fattazzo
 *
 */
@Entity
@Audited
@DiscriminatorValue("A")
@NamedQueries({
        @NamedQuery(name = "CategoriaScontoArticolo.caricaAll", query = "from CategoriaScontoArticolo csa where csa.codiceAzienda = :paramCodiceAzienda") })
public class CategoriaScontoArticolo extends CategoriaSconto {

    private static final long serialVersionUID = -4019888063560366712L;

}
