package it.eurotn.panjea.magazzino.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;

/**
 * @author leonardo
 */
@Entity
@Audited
@DiscriminatorValue("G")
public class RigaArticoloGenerata extends RigaArticolo {

    private static final long serialVersionUID = 2251185292141524427L;

}
