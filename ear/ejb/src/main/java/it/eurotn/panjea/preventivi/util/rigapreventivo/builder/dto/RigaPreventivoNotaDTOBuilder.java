package it.eurotn.panjea.preventivi.util.rigapreventivo.builder.dto;

import it.eurotn.panjea.preventivi.util.RigaNotaDTO;
import it.eurotn.panjea.preventivi.util.RigaPreventivoDTO;

import java.util.List;
import java.util.Map;

public class RigaPreventivoNotaDTOBuilder implements RigaPreventivoDTOBuilder {

	/**
	 * 
	 * @param rigaPreventivoDTOResult
	 *            risultati caricati dal persistence
	 * @return RigaPreventivoDTO ricavata dai risultati.
	 */
	protected RigaPreventivoDTO creaRigaPreventivo(RigaPreventivoDTOResult rigaPreventivoDTOResult) {
		RigaNotaDTO rigaNotaDTO = new RigaNotaDTO();
		rigaNotaDTO.setId(rigaPreventivoDTOResult.getId());
		rigaNotaDTO.setDescrizione(rigaPreventivoDTOResult.getDescrizioneRiga());
		rigaNotaDTO.setLivello(rigaPreventivoDTOResult.getLivello());
		rigaNotaDTO.setNota(rigaPreventivoDTOResult.getRigaNota());
		rigaNotaDTO.setRigaAutomatica(rigaPreventivoDTOResult.isRigaAutomatica());
		return rigaNotaDTO;
	}

	@Override
	public void fillResult(RigaPreventivoDTOResult rigaPreventivoDTOResult, List<RigaPreventivoDTO> result,
			Map<String, RigaPreventivoDTO> righeComposte) {
		result.add(creaRigaPreventivo(rigaPreventivoDTOResult));
	}

}
