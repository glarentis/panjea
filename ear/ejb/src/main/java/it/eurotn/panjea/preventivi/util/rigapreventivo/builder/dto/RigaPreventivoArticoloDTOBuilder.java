package it.eurotn.panjea.preventivi.util.rigapreventivo.builder.dto;

import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.magazzino.domain.Sconto;
import it.eurotn.panjea.preventivi.util.RigaArticoloDTO;
import it.eurotn.panjea.preventivi.util.RigaPreventivoDTO;

import java.util.List;
import java.util.Map;

public class RigaPreventivoArticoloDTOBuilder implements RigaPreventivoDTOBuilder {

	/**
	 * 
	 * @param rigaPreventivoDTOResult
	 *            risultati caricati dal persistence
	 * @return RigaPreventivoDTO ricavata dai risultati.
	 */
	protected RigaPreventivoDTO creaRigaPreventivo(RigaPreventivoDTOResult rigaPreventivoDTOResult) {

		Importo importoPrezzoTotale = new Importo();
		importoPrezzoTotale.setCodiceValuta(rigaPreventivoDTOResult.getCodiceValutaPrezzoTotale());
		importoPrezzoTotale.setImportoInValuta(rigaPreventivoDTOResult.getImportoInValutaPrezzoTotale());
		importoPrezzoTotale.setImportoInValutaAzienda(rigaPreventivoDTOResult.getImportoInValutaAziendaPrezzoTotale());
		importoPrezzoTotale.setTassoDiCambio(rigaPreventivoDTOResult.getTassoDiCambioPrezzoTotale());

		Importo importoPrezzoNetto = new Importo();
		importoPrezzoNetto.setCodiceValuta(rigaPreventivoDTOResult.getCodiceValutaPrezzoNetto());
		importoPrezzoNetto.setImportoInValuta(rigaPreventivoDTOResult.getImportoInValutaPrezzoNetto());
		importoPrezzoNetto.setImportoInValutaAzienda(rigaPreventivoDTOResult.getImportoInValutaAziendaPrezzoNetto());
		importoPrezzoNetto.setTassoDiCambio(rigaPreventivoDTOResult.getTassoDiCambioPrezzoNetto());

		Importo importoPrezzoUnitario = new Importo();
		importoPrezzoUnitario.setCodiceValuta(rigaPreventivoDTOResult.getCodiceValutaPrezzoUnitario());
		importoPrezzoUnitario.setImportoInValuta(rigaPreventivoDTOResult.getImportoInValutaPrezzoUnitario());
		importoPrezzoUnitario.setImportoInValutaAzienda(rigaPreventivoDTOResult
				.getImportoInValutaAziendaPrezzoUnitario());
		importoPrezzoUnitario.setTassoDiCambio(rigaPreventivoDTOResult.getTassoDiCambioPrezzoUnitario());

		RigaArticoloDTO rigaArticoloDTO = new RigaArticoloDTO();

		rigaArticoloDTO.setId(rigaPreventivoDTOResult.getId());
		rigaArticoloDTO.setIdArticolo(rigaPreventivoDTOResult.getIdArticolo());
		rigaArticoloDTO.setCodice(rigaPreventivoDTOResult.getCodiceArticolo());
		rigaArticoloDTO.setCodiceEntita(rigaPreventivoDTOResult.getCodiceArticoloEntita());
		rigaArticoloDTO.setDescrizioneArticolo(rigaPreventivoDTOResult.getDescrizioneArticolo());
		// rigaArticoloDTO.setBarcodeArticolo(rigaMagazzinoDTOResult.getBarcodeArticolo());

		rigaArticoloDTO.setDescrizione(rigaPreventivoDTOResult.getDescrizioneRiga());
		rigaArticoloDTO.setNumeroDecimaliPrezzo(rigaPreventivoDTOResult.getNumeroDecimaliPrezzo());
		// rigaArticoloDTO.setNumeroDecimaliQta(rigaOrdineDTOResult.getNumeroDecimaliQta());
		rigaArticoloDTO.setPrezzoNetto(importoPrezzoNetto);
		rigaArticoloDTO.setPrezzoTotale(importoPrezzoTotale);
		rigaArticoloDTO.setPrezzoUnitario(importoPrezzoUnitario);
		rigaArticoloDTO.setQta(rigaPreventivoDTOResult.getQtaRiga());
		rigaArticoloDTO.setQtaChiusa(rigaPreventivoDTOResult.getQtaChiusa());
		Sconto variazioni = new Sconto();
		variazioni.setSconto1(rigaPreventivoDTOResult.getVariazione1());
		variazioni.setSconto2(rigaPreventivoDTOResult.getVariazione2());
		variazioni.setSconto3(rigaPreventivoDTOResult.getVariazione3());
		variazioni.setSconto4(rigaPreventivoDTOResult.getVariazione4());
		rigaArticoloDTO.setSconto(variazioni);
		rigaArticoloDTO.setLivello(rigaPreventivoDTOResult.getLivello());
		rigaArticoloDTO.setNoteRiga(rigaPreventivoDTOResult.getNoteRiga());

		return rigaArticoloDTO;
	}

	@Override
	public void fillResult(RigaPreventivoDTOResult rigaPreventivoDTOResult, List<RigaPreventivoDTO> result,
			Map<String, RigaPreventivoDTO> righeComposte) {
		result.add(creaRigaPreventivo(rigaPreventivoDTOResult));
	}

}
