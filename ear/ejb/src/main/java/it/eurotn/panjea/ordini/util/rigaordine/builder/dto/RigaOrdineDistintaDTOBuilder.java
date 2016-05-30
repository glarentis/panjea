package it.eurotn.panjea.ordini.util.rigaordine.builder.dto;

import it.eurotn.panjea.ordini.util.RigaArticoloDTO;
import it.eurotn.panjea.ordini.util.RigaArticoloDistintaDTO;
import it.eurotn.panjea.ordini.util.RigaOrdineDTO;

public class RigaOrdineDistintaDTOBuilder extends RigaOrdineArticoloDTOBuilder {
	@Override
	protected RigaArticoloDTO creaRigaArticolo() {
		return new RigaArticoloDistintaDTO();
	}

	@Override
	public RigaOrdineDTO creaRigaOrdine(RigaOrdineDTOResult rigaOrdineDTOResult) {
		RigaArticoloDistintaDTO rigaArticoloDTO = (RigaArticoloDistintaDTO) super.creaRigaOrdine(rigaOrdineDTOResult);
		rigaArticoloDTO.setIdArticoloPadre(rigaOrdineDTOResult.getIdArticoloPadre());
		rigaArticoloDTO.setCodicePadre(rigaOrdineDTOResult.getCodiceArticoloPadre());
		rigaArticoloDTO.setCodiceEntitaPadre(rigaOrdineDTOResult.getCodiceArticoloPadreEntita());
		rigaArticoloDTO.setDescrizioneArticoloPadre(rigaOrdineDTOResult.getDescrizioneArticoloPadre());
		return rigaArticoloDTO;
	}
}
