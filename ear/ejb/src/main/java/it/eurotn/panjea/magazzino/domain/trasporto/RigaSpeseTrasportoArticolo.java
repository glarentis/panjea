package it.eurotn.panjea.magazzino.domain.trasporto;

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
@DiscriminatorValue("TR")
@NamedQueries({
        @NamedQuery(name = "RigaSpeseTrasportoArticolo.caricaByAreaMagazzino", query = "select r from RigaSpeseTrasportoArticolo r where r.areaMagazzino=:areaMagazzino") })
public class RigaSpeseTrasportoArticolo extends RigaArticoloGenerata {

    private static final long serialVersionUID = -3435370471422879254L;

}
