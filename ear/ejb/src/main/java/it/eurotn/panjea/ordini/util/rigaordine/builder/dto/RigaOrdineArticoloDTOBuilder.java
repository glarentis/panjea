package it.eurotn.panjea.ordini.util.rigaordine.builder.dto;

import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.magazzino.domain.Sconto;
import it.eurotn.panjea.ordini.util.RigaArticoloDTO;
import it.eurotn.panjea.ordini.util.RigaOrdineDTO;

import java.util.List;
import java.util.Map;

public class RigaOrdineArticoloDTOBuilder implements RigaOrdineDTOBuilder {

	/**
	 * 
	 * @return riga articolo
	 */
	protected RigaArticoloDTO creaRigaArticolo() {
		return new RigaArticoloDTO();
	}

	/**
	 * 
	 * @param rigaOrdineDTOResult
	 *            risultati caricati dal persistence
	 * @return RigaOrdineDTO ricavata dai risultati.
	 */
	protected RigaOrdineDTO creaRigaOrdine(RigaOrdineDTOResult rigaOrdineDTOResult) {

		Importo importoPrezzoTotale = new Importo();
		importoPrezzoTotale.setCodiceValuta(rigaOrdineDTOResult.getCodiceValutaPrezzoTotale());
		importoPrezzoTotale.setImportoInValuta(rigaOrdineDTOResult.getImportoInValutaPrezzoTotale());
		importoPrezzoTotale.setImportoInValutaAzienda(rigaOrdineDTOResult.getImportoInValutaAziendaPrezzoTotale());
		importoPrezzoTotale.setTassoDiCambio(rigaOrdineDTOResult.getTassoDiCambioPrezzoTotale());

		Importo importoPrezzoNetto = new Importo();
		importoPrezzoNetto.setCodiceValuta(rigaOrdineDTOResult.getCodiceValutaPrezzoNetto());
		importoPrezzoNetto.setImportoInValuta(rigaOrdineDTOResult.getImportoInValutaPrezzoNetto());
		importoPrezzoNetto.setImportoInValutaAzienda(rigaOrdineDTOResult.getImportoInValutaAziendaPrezzoNetto());
		importoPrezzoNetto.setTassoDiCambio(rigaOrdineDTOResult.getTassoDiCambioPrezzoNetto());

		Importo importoPrezzoUnitario = new Importo();
		importoPrezzoUnitario.setCodiceValuta(rigaOrdineDTOResult.getCodiceValutaPrezzoUnitario());
		importoPrezzoUnitario.setImportoInValuta(rigaOrdineDTOResult.getImportoInValutaPrezzoUnitario());
		importoPrezzoUnitario.setImportoInValutaAzienda(rigaOrdineDTOResult.getImportoInValutaAziendaPrezzoUnitario());
		importoPrezzoUnitario.setTassoDiCambio(rigaOrdineDTOResult.getTassoDiCambioPrezzoUnitario());

		RigaArticoloDTO rigaArticoloDTO = creaRigaArticolo();

		rigaArticoloDTO.setId(rigaOrdineDTOResult.getId());
		rigaArticoloDTO.setIdArticolo(rigaOrdineDTOResult.getIdArticolo());
		rigaArticoloDTO.setCodice(rigaOrdineDTOResult.getCodiceArticolo());
		rigaArticoloDTO.setCodiceEntita(rigaOrdineDTOResult.getCodiceArticoloEntita());
		rigaArticoloDTO.setDescrizioneArticolo(rigaOrdineDTOResult.getDescrizioneArticolo());
		// rigaArticoloDTO.setBarcodeArticolo(rigaMagazzinoDTOResult.getBarcodeArticolo());

		rigaArticoloDTO.setDescrizione(rigaOrdineDTOResult.getDescrizioneRiga());
		rigaArticoloDTO.setNumeroDecimaliPrezzo(rigaOrdineDTOResult.getNumeroDecimaliPrezzo());
		// rigaArticoloDTO.setNumeroDecimaliQta(rigaOrdineDTOResult.getNumeroDecimaliQta());
		rigaArticoloDTO.setPrezzoNetto(importoPrezzoNetto);
		rigaArticoloDTO.setPrezzoTotale(importoPrezzoTotale);
		rigaArticoloDTO.setPrezzoUnitario(importoPrezzoUnitario);
		rigaArticoloDTO.setQta(rigaOrdineDTOResult.getQtaRiga());
		rigaArticoloDTO.setQtaChiusa(rigaOrdineDTOResult.getQtaChiusa());
		Sconto variazioni = new Sconto();
		variazioni.setSconto1(rigaOrdineDTOResult.getVariazione1());
		variazioni.setSconto2(rigaOrdineDTOResult.getVariazione2());
		variazioni.setSconto3(rigaOrdineDTOResult.getVariazione3());
		variazioni.setSconto4(rigaOrdineDTOResult.getVariazione4());
		rigaArticoloDTO.setSconto(variazioni);
		rigaArticoloDTO.setDataConsegna(rigaOrdineDTOResult.getDataConsegna());
		rigaArticoloDTO.setLivello(rigaOrdineDTOResult.getLivello());
		rigaArticoloDTO.setEvasioneForzata(rigaOrdineDTOResult.isEvasioneForzata());
		rigaArticoloDTO.setNoteRiga(rigaOrdineDTOResult.getNoteRiga());

		rigaArticoloDTO.setRigaCollegata(rigaOrdineDTOResult.getIdAreaPreventivoCollegata() != null);

		// rigaArticoloDTO.setCodiceIva(new CodiceIva());
		// if (rigaMagazzinoDTOResult.getIdCodiceIva() != null) {
		// rigaArticoloDTO.getCodiceIva().setId(rigaMagazzinoDTOResult.getIdCodiceIva());
		// rigaArticoloDTO.getCodiceIva().setCodice(rigaMagazzinoDTOResult.getCodiceCodiceIva());
		// rigaArticoloDTO.getCodiceIva().setPercApplicazione(rigaMagazzinoDTOResult.getPercApplicazioneCodiceIva());
		// }
		return rigaArticoloDTO;
	}

	@Override
	public void fillResult(RigaOrdineDTOResult rigaOrdineDTOResult, List<RigaOrdineDTO> result,
			Map<String, RigaOrdineDTO> righeComposte) {
		result.add(creaRigaOrdine(rigaOrdineDTOResult));
	}
}
