package it.eurotn.panjea.magazzino.domain.omaggio;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.hibernate.envers.Audited;

import it.eurotn.panjea.magazzino.domain.RigaArticoloGenerata;

/**
 * @author leonardo
 */
@Entity
@Audited
@DiscriminatorValue("OM")
@NamedQueries({
        @NamedQuery(name = "RigaOmaggioArticolo.caricaByAreaMagazzino", query = "select r from RigaOmaggioArticolo r where r.areaMagazzino=:areaMagazzino") })
public class RigaOmaggioArticolo extends RigaArticoloGenerata {

    private static final long serialVersionUID = -1931092696253108625L;

}
