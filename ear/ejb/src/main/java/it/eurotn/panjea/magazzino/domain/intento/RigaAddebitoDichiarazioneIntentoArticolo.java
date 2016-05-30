package it.eurotn.panjea.magazzino.domain.intento;

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
@DiscriminatorValue("DI")
@NamedQueries({
        @NamedQuery(name = "RigaAddebitoDichiarazioneIntentoArticolo.caricaByAreaMagazzino", query = "select r from RigaAddebitoDichiarazioneIntentoArticolo r where r.areaMagazzino.id=:idAreaMagazzino") })
public class RigaAddebitoDichiarazioneIntentoArticolo extends RigaArticoloGenerata {

    private static final long serialVersionUID = -8835932763434499901L;

}
