package it.eurotn.panjea.preventivi.util.rigapreventivo.builder.dto;

import it.eurotn.panjea.preventivi.util.RigaPreventivoDTO;
import it.eurotn.panjea.preventivi.util.RigaTestataDTO;

import java.util.List;
import java.util.Map;

public class RigaPreventivoTestataDTOBuilder implements RigaPreventivoDTOBuilder {

	/**
	 * 
	 * @param rigaPreventivoDTOResult
	 *            risultati caricati dal persistence
	 * @return RigaPreventivoDTO ricavata dai risultati.
	 */
	protected RigaPreventivoDTO creaRigaPreventivo(RigaPreventivoDTOResult rigaPreventivoDTOResult) {
		RigaTestataDTO rigaTestataDTO = new RigaTestataDTO();
		rigaTestataDTO.setId(rigaPreventivoDTOResult.getId());
		rigaTestataDTO.setDescrizione(rigaPreventivoDTOResult.getDescrizioneRiga());
		rigaTestataDTO.setLivello(rigaPreventivoDTOResult.getLivello());
		return rigaTestataDTO;
	}

	@Override
	public void fillResult(RigaPreventivoDTOResult rigaPreventivoDTOResult, List<RigaPreventivoDTO> result,
			Map<String, RigaPreventivoDTO> righeComposte) {
		result.add(creaRigaPreventivo(rigaPreventivoDTOResult));

	}

}
