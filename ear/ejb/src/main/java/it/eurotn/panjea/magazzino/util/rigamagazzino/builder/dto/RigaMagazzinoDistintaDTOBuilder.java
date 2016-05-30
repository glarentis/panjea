package it.eurotn.panjea.magazzino.util.rigamagazzino.builder.dto;

import it.eurotn.panjea.magazzino.util.RigaArticoloDTO;
import it.eurotn.panjea.magazzino.util.RigaArticoloDistintaDTO;
import it.eurotn.panjea.magazzino.util.RigaMagazzinoDTO;

public class RigaMagazzinoDistintaDTOBuilder extends RigaMagazzinoArticoloDTOBuilder {
    @Override
    protected RigaArticoloDTO creaRigaArticolo() {
        return new RigaArticoloDistintaDTO();
    }

    @Override
    public RigaMagazzinoDTO creaRigaMagazzino(RigaMagazzinoDTOResult rigaMagazzinoDTOResult) {
        RigaArticoloDistintaDTO rigaArticoloDTO = (RigaArticoloDistintaDTO) super.creaRigaMagazzino(
                rigaMagazzinoDTOResult);
        rigaArticoloDTO.setIdArticoloPadre(rigaMagazzinoDTOResult.getIdArticoloPadre());
        rigaArticoloDTO.setCodicePadre(rigaMagazzinoDTOResult.getCodiceArticoloPadre());
        rigaArticoloDTO.setCodiceEntitaPadre(rigaMagazzinoDTOResult.getCodiceArticoloPadreEntita());
        rigaArticoloDTO.setDescrizioneArticoloPadre(rigaMagazzinoDTOResult.getDescrizioneArticoloPadre());
        return rigaArticoloDTO;
    }
}
