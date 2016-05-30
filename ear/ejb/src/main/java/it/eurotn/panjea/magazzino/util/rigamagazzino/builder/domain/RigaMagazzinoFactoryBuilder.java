package it.eurotn.panjea.magazzino.util.rigamagazzino.builder.domain;

import java.util.HashMap;
import java.util.Map;

import it.eurotn.panjea.conai.domain.RigaConaiArticolo;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.RigaArticoloComponente;
import it.eurotn.panjea.magazzino.domain.RigaArticoloDistinta;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.RigaNota;
import it.eurotn.panjea.magazzino.domain.RigaTestata;
import it.eurotn.panjea.magazzino.domain.intento.RigaAddebitoDichiarazioneIntentoArticolo;
import it.eurotn.panjea.magazzino.domain.omaggio.RigaOmaggioArticolo;
import it.eurotn.panjea.magazzino.domain.trasporto.RigaSpeseTrasportoArticolo;

public class RigaMagazzinoFactoryBuilder {
    private static Map<String, RigaMagazzinoBuilder> builder = new HashMap<String, RigaMagazzinoBuilder>();

    static {
        builder.put(RigaNota.class.getName(), new RigaMagazzinoBuilder());
        builder.put(RigaTestata.class.getName(), new RigaMagazzinoBuilder());
        builder.put(RigaArticolo.class.getName(), new RigaMagazzinoBuilder());
        builder.put(RigaArticoloDistinta.class.getName(), new RigaMagazzinoBuilder());
        builder.put(RigaArticoloComponente.class.getName(), new RigaMagazzinoComponenteBuilder());
        builder.put(RigaConaiArticolo.class.getName(), new RigaMagazzinoBuilder());
        builder.put(RigaOmaggioArticolo.class.getName(), new RigaMagazzinoBuilder());
        builder.put(RigaSpeseTrasportoArticolo.class.getName(), new RigaMagazzinoBuilder());
        builder.put(RigaAddebitoDichiarazioneIntentoArticolo.class.getName(), new RigaMagazzinoBuilder());
    }

    /**
     * @param rigaMagazzinoResult
     *            tiopRiga per il quale ritornare il builder
     * @return builder per creare il tipo riga richiesto
     */
    public RigaMagazzinoBuilder getBuilder(RigaMagazzino rigaMagazzinoResult) {
        return builder.get(rigaMagazzinoResult.getClass().getName());
    }
}
