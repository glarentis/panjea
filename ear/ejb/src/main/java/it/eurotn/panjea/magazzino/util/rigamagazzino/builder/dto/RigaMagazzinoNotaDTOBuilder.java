package it.eurotn.panjea.magazzino.util.rigamagazzino.builder.dto;

import java.util.List;
import java.util.Map;

import it.eurotn.panjea.magazzino.util.RigaMagazzinoDTO;
import it.eurotn.panjea.magazzino.util.RigaNotaDTO;

public class RigaMagazzinoNotaDTOBuilder implements RigaMagazzinoDTOBuilder {
    /**
     * 
     * @param rigaMagazzinoDTOResult
     *            risultati caricati dal persistence
     * @return rigaMagazzinoDTO ricavata dai risultati.
     */
    protected RigaMagazzinoDTO creaRigaMagazzino(RigaMagazzinoDTOResult rigaMagazzinoDTOResult) {
        RigaNotaDTO rigaNotaDTO = new RigaNotaDTO();
        rigaNotaDTO.setId(rigaMagazzinoDTOResult.getId());
        rigaNotaDTO.setDescrizione(rigaMagazzinoDTOResult.getDescrizioneRiga());
        rigaNotaDTO.setLivello(rigaMagazzinoDTOResult.getLivello());
        rigaNotaDTO.setRigaCollegata(rigaMagazzinoDTOResult.getIdAreaMagazzinoCollegata() != null
                || rigaMagazzinoDTOResult.getIdAreaOrdineCollegata() != null);
        rigaNotaDTO.setNota(rigaMagazzinoDTOResult.getRigaNota());
        rigaNotaDTO.setRigaAutomatica(rigaMagazzinoDTOResult.isRigaAutomatica());
        return rigaNotaDTO;
    }

    @Override
    public void fillResult(RigaMagazzinoDTOResult rigaMagazzinoDTOResult, List<RigaMagazzinoDTO> result,
            Map<String, RigaMagazzinoDTO> righeComposte) {
        result.add(creaRigaMagazzino(rigaMagazzinoDTOResult));
    }
}
