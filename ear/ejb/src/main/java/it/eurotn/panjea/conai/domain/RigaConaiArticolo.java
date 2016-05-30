package it.eurotn.panjea.conai.domain;

import it.eurotn.panjea.magazzino.domain.RigaArticoloGenerata;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.hibernate.envers.Audited;

@Entity
@Audited
@DiscriminatorValue("CN")
@NamedQueries({ @NamedQuery(name = "RigaConaiArticolo.caricaByAreaMagazzino", query = "select r from RigaConaiArticolo r where r.areaMagazzino=:areaMagazzino") })
public class RigaConaiArticolo extends RigaArticoloGenerata {

	private static final long serialVersionUID = -7634907147807185050L;

}
