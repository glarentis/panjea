package it.eurotn.panjea.magazzino.util.rigamagazzino.builder.dto;

import java.util.HashMap;
import java.util.Map;

public class RigaMagazzinoDTOFactoryBuilder {
    private static Map<String, RigaMagazzinoDTOBuilder> builder = new HashMap<String, RigaMagazzinoDTOBuilder>();

    static {
        builder.put("N", new RigaMagazzinoNotaDTOBuilder());
        builder.put("T", new RigaMagazzinoTestataDTOBuilder());
        builder.put("A", new RigaMagazzinoArticoloDTOBuilder());
        builder.put("D", new RigaMagazzinoDistintaDTOBuilder());
        builder.put("C", new RigaMagazzinoComponenteDTOBuilder());
        builder.put("CN", new RigaMagazzinoArticoloDTOBuilder());
        builder.put("OM", new RigaMagazzinoArticoloDTOBuilder());
        builder.put("TR", new RigaMagazzinoArticoloDTOBuilder());
        builder.put("DI", new RigaMagazzinoArticoloDTOBuilder());
    }

    /**
     * 
     * @param rigaMagazzinoDTOResult
     *            tiopRiga per il quale ritornare il builder
     * @return builder per creare il tipo riga richiesto
     */
    public RigaMagazzinoDTOBuilder getBuilder(RigaMagazzinoDTOResult rigaMagazzinoDTOResult) {
        // i componenti delle distinte non hanno dto
        return builder.get(rigaMagazzinoDTOResult.getTipoRiga());
    }
}
