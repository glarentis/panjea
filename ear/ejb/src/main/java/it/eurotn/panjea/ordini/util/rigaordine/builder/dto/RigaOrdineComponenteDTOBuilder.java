package it.eurotn.panjea.ordini.util.rigaordine.builder.dto;

import it.eurotn.panjea.ordini.util.RigaArticoloComponenteDTO;
import it.eurotn.panjea.ordini.util.RigaArticoloDTO;
import it.eurotn.panjea.ordini.util.RigaOrdineDTO;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class RigaOrdineComponenteDTOBuilder extends RigaOrdineArticoloDTOBuilder {
	@Override
	protected RigaArticoloDTO creaRigaArticolo() {
		return new RigaArticoloComponenteDTO();
	}

	@Override
	public RigaOrdineDTO creaRigaOrdine(RigaOrdineDTOResult rigaOrdineDTOResult) {
		RigaArticoloComponenteDTO rigaArticoloDTO = (RigaArticoloComponenteDTO) super
				.creaRigaOrdine(rigaOrdineDTOResult);
		Random random = new Random();
		int generatedId = random.nextInt();
		rigaArticoloDTO.setId(generatedId);
		rigaArticoloDTO.setIdArticolo(rigaOrdineDTOResult.getIdArticoloPadre());
		rigaArticoloDTO.setCodice(rigaOrdineDTOResult.getCodiceArticoloPadre());
		rigaArticoloDTO.setCodiceEntita(rigaOrdineDTOResult.getCodiceArticoloPadreEntita());
		rigaArticoloDTO.setDescrizioneArticolo(rigaOrdineDTOResult.getDescrizioneArticoloPadre());
		rigaArticoloDTO.setDescrizione(rigaOrdineDTOResult.getDescrizioneArticoloPadre());

		rigaArticoloDTO.setIdArticoloPadre(rigaOrdineDTOResult.getIdArticoloPadre());
		rigaArticoloDTO.setCodicePadre(rigaOrdineDTOResult.getCodiceArticoloPadre());
		rigaArticoloDTO.setCodiceEntitaPadre(rigaOrdineDTOResult.getCodiceArticoloPadreEntita());
		rigaArticoloDTO.setDescrizioneArticoloPadre(rigaOrdineDTOResult.getDescrizioneArticoloPadre());
		rigaArticoloDTO.setIdAreaOrdine(rigaOrdineDTOResult.getIdAreaOrdine());
		return rigaArticoloDTO;
	}

	@Override
	public void fillResult(RigaOrdineDTOResult rigaOrdineDTOResult, List<RigaOrdineDTO> result,
			Map<String, RigaOrdineDTO> righeComposte) {
		return;
	}
}
