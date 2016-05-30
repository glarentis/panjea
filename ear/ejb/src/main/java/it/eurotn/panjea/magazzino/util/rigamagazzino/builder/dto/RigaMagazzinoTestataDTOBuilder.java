package it.eurotn.panjea.magazzino.util.rigamagazzino.builder.dto;

import java.util.List;
import java.util.Map;

import it.eurotn.panjea.magazzino.util.RigaMagazzinoDTO;
import it.eurotn.panjea.magazzino.util.RigaTestataDTO;

public class RigaMagazzinoTestataDTOBuilder implements RigaMagazzinoDTOBuilder {

    /**
     * 
     * @param rigaMagazzinoDTOResult
     *            risultati caricati dal persistence
     * @return rigaMagazzinoDTO ricavata dai risultati.
     */
    protected RigaMagazzinoDTO creaRigaMagazzino(RigaMagazzinoDTOResult rigaMagazzinoDTOResult) {
        RigaTestataDTO rigaTestataDTO = new RigaTestataDTO();
        rigaTestataDTO.setId(rigaMagazzinoDTOResult.getId());
        rigaTestataDTO.setDescrizione(rigaMagazzinoDTOResult.getDescrizioneRiga());
        rigaTestataDTO.setLivello(rigaMagazzinoDTOResult.getLivello());
        rigaTestataDTO.setRigaCollegata(rigaMagazzinoDTOResult.getIdAreaMagazzinoCollegata() != null
                || rigaMagazzinoDTOResult.getIdAreaOrdineCollegata() != null);
        rigaTestataDTO.setCodiceTipoDocumentoCollegato(rigaMagazzinoDTOResult.getCodiceTipoDocumentoCollegato());
        rigaTestataDTO.setIdAreaMagazzinoCollegata(rigaMagazzinoDTOResult.getIdAreaMagazzinoCollegata());
        rigaTestataDTO.setIdAreaOrdineCollegata(rigaMagazzinoDTOResult.getIdAreaOrdineCollegata());
        return rigaTestataDTO;
    }

    @Override
    public void fillResult(RigaMagazzinoDTOResult rigaMagazzinoDTOResult, List<RigaMagazzinoDTO> result,
            Map<String, RigaMagazzinoDTO> righeComposte) {
        result.add(creaRigaMagazzino(rigaMagazzinoDTOResult));
    }
}
