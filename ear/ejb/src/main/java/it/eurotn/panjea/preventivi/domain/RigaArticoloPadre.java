package it.eurotn.panjea.preventivi.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;

@Entity(name = "RigaArticoloPadrePreventivo")
@Audited
@DiscriminatorValue("P")
public class RigaArticoloPadre extends RigaArticoloDistinta {

	private static final long serialVersionUID = 1L;
}
