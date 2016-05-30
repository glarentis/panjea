package it.eurotn.panjea.magazzino.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.hibernate.envers.Audited;

/**
 * Specializzazione della classe <code>CategoriaSconto</code> pe rgestire le categorie sconto delle sedi entità.
 *
 * @author fattazzo
 *
 */
@Entity
@Audited
@DiscriminatorValue("S")
@NamedQueries({
        @NamedQuery(name = "CategoriaScontoSede.caricaAll", query = "from CategoriaScontoSede css where css.codiceAzienda = :paramCodiceAzienda") })
public class CategoriaScontoSede extends CategoriaSconto {

    private static final long serialVersionUID = 1803239029009108176L;

}
