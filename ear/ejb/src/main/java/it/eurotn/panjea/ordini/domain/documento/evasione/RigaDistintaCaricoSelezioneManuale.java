package it.eurotn.panjea.ordini.domain.documento.evasione;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;

@Entity
@Audited
@DiscriminatorValue("M")
public class RigaDistintaCaricoSelezioneManuale extends RigaDistintaCarico {
    private static final long serialVersionUID = 1989406779341763506L;

    @Override
    protected void aggiornaStato() {
    }

    @Override
    public void setStato(StatoRiga stato) {
        this.stato = stato;
    }

}
