package it.eurotn.panjea.magazzino.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;

@Entity
@Audited
@DiscriminatorValue("P")
public class ConfigurazioneDistintaPersonalizzata extends ConfigurazioneDistinta {
    private static final long serialVersionUID = 6438155526434157844L;

    /**
     * Costruttore.
     */
    public ConfigurazioneDistintaPersonalizzata() {
        setNome("PERSONALIZZATA");
    }

}
