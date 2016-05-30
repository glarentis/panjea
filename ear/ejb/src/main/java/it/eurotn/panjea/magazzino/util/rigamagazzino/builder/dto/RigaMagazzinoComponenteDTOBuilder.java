package it.eurotn.panjea.magazzino.util.rigamagazzino.builder.dto;

import java.util.List;
import java.util.Map;
import java.util.Random;

import it.eurotn.panjea.magazzino.util.RigaArticoloComponenteDTO;
import it.eurotn.panjea.magazzino.util.RigaArticoloDTO;
import it.eurotn.panjea.magazzino.util.RigaArticoloDistintaDTO;
import it.eurotn.panjea.magazzino.util.RigaMagazzinoDTO;

public class RigaMagazzinoComponenteDTOBuilder extends RigaMagazzinoDistintaDTOBuilder {
    @Override
    protected RigaArticoloDTO creaRigaArticolo() {
        return new RigaArticoloComponenteDTO();
    }

    @Override
    public RigaMagazzinoDTO creaRigaMagazzino(RigaMagazzinoDTOResult rigaMagazzinoDTOResult) {
        RigaArticoloDistintaDTO rigaArticoloDTO = (RigaArticoloDistintaDTO) super.creaRigaMagazzino(
                rigaMagazzinoDTOResult);
        Random random = new Random();
        int generatedId = random.nextInt();
        rigaArticoloDTO.setId(generatedId);
        rigaArticoloDTO.setIdArticolo(rigaMagazzinoDTOResult.getIdArticoloPadre());
        rigaArticoloDTO.setCodice(rigaMagazzinoDTOResult.getCodiceArticoloPadre());
        rigaArticoloDTO.setCodiceEntita(rigaMagazzinoDTOResult.getCodiceArticoloPadreEntita());
        rigaArticoloDTO.setDescrizioneArticolo(rigaMagazzinoDTOResult.getDescrizioneArticoloPadre());
        rigaArticoloDTO.setDescrizione(rigaMagazzinoDTOResult.getDescrizioneArticoloPadre());
        rigaArticoloDTO.setIdArticoloPadre(null);
        rigaArticoloDTO.setCodicePadre(null);
        rigaArticoloDTO.setCodiceEntitaPadre(null);
        rigaArticoloDTO.setDescrizioneArticoloPadre(null);
        rigaArticoloDTO.setIdAreaMagazzinoCollegata(rigaMagazzinoDTOResult.getIdAreaMagazzinoCollegata());
        rigaArticoloDTO.setIdAreaOrdineCollegata(rigaMagazzinoDTOResult.getIdAreaOrdineCollegata());
        ((RigaArticoloComponenteDTO) rigaArticoloDTO).setIdAreaMagazzino(rigaMagazzinoDTOResult.getIdAreaMagazzino());
        return rigaArticoloDTO;
    }

    @Override
    public void fillResult(RigaMagazzinoDTOResult rigaMagazzinoDTOResult, List<RigaMagazzinoDTO> result,
            Map<String, RigaMagazzinoDTO> righeComposte) {
        return;
    }
}
