package it.eurotn.panjea.ordini.util.rigaordine.builder.dto;

import it.eurotn.panjea.ordini.util.RigaOrdineDTO;
import it.eurotn.panjea.ordini.util.RigaTestataDTO;

import java.util.List;
import java.util.Map;

public class RigaOrdineTestataDTOBuilder implements RigaOrdineDTOBuilder {

	/**
	 * 
	 * @param rigaMagazzinoDTOResult
	 *            risultati caricati dal persistence
	 * @return rigaMagazzinoDTO ricavata dai risultati.
	 */
	protected RigaOrdineDTO creaRigaOrdine(RigaOrdineDTOResult rigaOrdineDTOResult) {
		RigaTestataDTO rigaTestataDTO = new RigaTestataDTO();
		rigaTestataDTO.setId(rigaOrdineDTOResult.getId());
		rigaTestataDTO.setDescrizione(rigaOrdineDTOResult.getDescrizioneRiga());
		rigaTestataDTO.setLivello(rigaOrdineDTOResult.getLivello());
		rigaTestataDTO.setRigaCollegata(rigaOrdineDTOResult.getIdAreaPreventivoCollegata() != null);
		rigaTestataDTO.setCodiceTipoDocumentoCollegato(rigaOrdineDTOResult.getCodiceTipoDocumentoCollegato());
		rigaTestataDTO.setIdAreaPreventivoCollegata(rigaOrdineDTOResult.getIdAreaPreventivoCollegata());
		rigaTestataDTO.setRigaCollegata(rigaOrdineDTOResult.getIdAreaPreventivoCollegata() != null);
		return rigaTestataDTO;
	}

	@Override
	public void fillResult(RigaOrdineDTOResult rigaOrdineDTOResult, List<RigaOrdineDTO> result,
			Map<String, RigaOrdineDTO> righeComposte) {
		result.add(creaRigaOrdine(rigaOrdineDTOResult));
	}

}
