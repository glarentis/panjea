package it.eurotn.panjea.ordini.util.rigaordine.builder.dto;

import it.eurotn.panjea.ordini.util.RigaNotaDTO;
import it.eurotn.panjea.ordini.util.RigaOrdineDTO;

import java.util.List;
import java.util.Map;

public class RigaOrdineNotaDTOBuilder implements RigaOrdineDTOBuilder {
	/**
	 * 
	 * @param rigaOrdineDTOResult
	 *            risultati caricati dal persistence
	 * @return RigaOrdineDTO ricavata dai risultati.
	 */
	protected RigaOrdineDTO creaRigaOrdine(RigaOrdineDTOResult rigaOrdineDTOResult) {
		RigaNotaDTO rigaNotaDTO = new RigaNotaDTO();
		rigaNotaDTO.setId(rigaOrdineDTOResult.getId());
		rigaNotaDTO.setDescrizione(rigaOrdineDTOResult.getDescrizioneRiga());
		rigaNotaDTO.setLivello(rigaOrdineDTOResult.getLivello());
		rigaNotaDTO.setNota(rigaOrdineDTOResult.getRigaNota());
		rigaNotaDTO.setEvasioneForzata(rigaOrdineDTOResult.isEvasioneForzata());
		rigaNotaDTO.setRigaAutomatica(rigaOrdineDTOResult.isRigaAutomatica());
		rigaNotaDTO.setRigaCollegata(rigaOrdineDTOResult.getIdAreaPreventivoCollegata() != null);
		return rigaNotaDTO;
	}

	@Override
	public void fillResult(RigaOrdineDTOResult rigaOrdineDTOResult, List<RigaOrdineDTO> result,
			Map<String, RigaOrdineDTO> righeComposte) {
		result.add(creaRigaOrdine(rigaOrdineDTOResult));
	}
}
