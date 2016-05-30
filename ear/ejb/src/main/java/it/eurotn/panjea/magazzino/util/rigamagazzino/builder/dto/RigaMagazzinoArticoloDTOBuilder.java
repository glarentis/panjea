package it.eurotn.panjea.magazzino.util.rigamagazzino.builder.dto;

import java.util.List;
import java.util.Map;

import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.magazzino.domain.Sconto;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.StrategiaTotalizzazioneDocumento;
import it.eurotn.panjea.magazzino.util.RigaArticoloDTO;
import it.eurotn.panjea.magazzino.util.RigaMagazzinoDTO;

public class RigaMagazzinoArticoloDTOBuilder implements RigaMagazzinoDTOBuilder {

    /**
     * 
     * @return riga articolo
     */
    protected RigaArticoloDTO creaRigaArticolo() {
        return new RigaArticoloDTO();
    }

    /**
     * @param rigaMagazzinoDTOResult
     *            risultati caricati dal persistence
     * @return rigaMagazzinoDTO ricavata dai risultati.
     */
    protected RigaMagazzinoDTO creaRigaMagazzino(RigaMagazzinoDTOResult rigaMagazzinoDTOResult) {
        Importo importoPrezzoTotale = new Importo();
        importoPrezzoTotale.setCodiceValuta(rigaMagazzinoDTOResult.getCodiceValutaPrezzoTotale());
        importoPrezzoTotale.setImportoInValuta(rigaMagazzinoDTOResult.getImportoInValutaPrezzoTotale());
        importoPrezzoTotale.setImportoInValutaAzienda(rigaMagazzinoDTOResult.getImportoInValutaAziendaPrezzoTotale());
        importoPrezzoTotale.setTassoDiCambio(rigaMagazzinoDTOResult.getTassoDiCambioPrezzoTotale());

        Importo importoPrezzoNetto = new Importo();
        importoPrezzoNetto.setCodiceValuta(rigaMagazzinoDTOResult.getCodiceValutaPrezzoNetto());
        importoPrezzoNetto.setImportoInValuta(rigaMagazzinoDTOResult.getImportoInValutaPrezzoNetto());
        importoPrezzoNetto.setImportoInValutaAzienda(rigaMagazzinoDTOResult.getImportoInValutaAziendaPrezzoNetto());
        importoPrezzoNetto.setTassoDiCambio(rigaMagazzinoDTOResult.getTassoDiCambioPrezzoNetto());

        Importo importoPrezzoUnitario = new Importo();
        importoPrezzoUnitario.setCodiceValuta(rigaMagazzinoDTOResult.getCodiceValutaPrezzoUnitario());
        importoPrezzoUnitario.setImportoInValuta(rigaMagazzinoDTOResult.getImportoInValutaPrezzoUnitario());
        importoPrezzoUnitario
                .setImportoInValutaAzienda(rigaMagazzinoDTOResult.getImportoInValutaAziendaPrezzoUnitario());
        importoPrezzoUnitario.setTassoDiCambio(rigaMagazzinoDTOResult.getTassoDiCambioPrezzoUnitario());

        Importo importoReale = importoPrezzoUnitario.clone();
        if (rigaMagazzinoDTOResult.isIvata() && rigaMagazzinoDTOResult
                .getStrategiaTotalizzazioneDocumento() == StrategiaTotalizzazioneDocumento.SCONTRINO) {
            importoReale.setImportoInValuta(
                    importoReale.getImportoInValuta().add(rigaMagazzinoDTOResult.getImportoInValutaUnitarioImposta()));
            importoReale.setImportoInValutaAzienda(importoReale.getImportoInValutaAzienda()
                    .add(rigaMagazzinoDTOResult.getImportoInValutaAziendaUnitarioImposta()));
        }

        RigaArticoloDTO rigaArticoloDTO = creaRigaArticolo();

        rigaArticoloDTO.setId(rigaMagazzinoDTOResult.getId());
        rigaArticoloDTO.setIdArticolo(rigaMagazzinoDTOResult.getIdArticolo());
        rigaArticoloDTO.setCodice(rigaMagazzinoDTOResult.getCodiceArticolo());
        rigaArticoloDTO.setCodiceEntita(rigaMagazzinoDTOResult.getCodiceArticoloEntita());
        rigaArticoloDTO.setDescrizioneArticolo(rigaMagazzinoDTOResult.getDescrizioneArticolo());
        rigaArticoloDTO.setBarcodeArticolo(rigaMagazzinoDTOResult.getBarcodeArticolo());

        rigaArticoloDTO.setDescrizione(rigaMagazzinoDTOResult.getDescrizioneRiga());
        rigaArticoloDTO.setNumeroDecimaliPrezzo(rigaMagazzinoDTOResult.getNumeroDecimaliPrezzo());
        rigaArticoloDTO.setNumeroDecimaliQta(rigaMagazzinoDTOResult.getNumeroDecimaliQta());
        rigaArticoloDTO.setPrezzoNetto(importoPrezzoNetto);
        rigaArticoloDTO.setPrezzoTotale(importoPrezzoTotale);
        rigaArticoloDTO.setPrezzoUnitario(importoPrezzoUnitario);
        rigaArticoloDTO.setPrezzoUnitarioReale(importoReale);
        rigaArticoloDTO.setQta(rigaMagazzinoDTOResult.getQtaRiga());
        rigaArticoloDTO.setQtaChiusa(rigaMagazzinoDTOResult.getQtaChiusa());
        Sconto variazioni = new Sconto();
        variazioni.setSconto1(rigaMagazzinoDTOResult.getVariazione1());
        variazioni.setSconto2(rigaMagazzinoDTOResult.getVariazione2());
        variazioni.setSconto3(rigaMagazzinoDTOResult.getVariazione3());
        variazioni.setSconto4(rigaMagazzinoDTOResult.getVariazione4());
        rigaArticoloDTO.setSconto(variazioni);
        rigaArticoloDTO.setLivello(rigaMagazzinoDTOResult.getLivello());
        rigaArticoloDTO.setRigaCollegata(rigaMagazzinoDTOResult.getIdAreaMagazzinoCollegata() != null
                || rigaMagazzinoDTOResult.getIdAreaOrdineCollegata() != null);
        rigaArticoloDTO.setNoteRiga(rigaMagazzinoDTOResult.getNoteRiga());

        rigaArticoloDTO.setCodiceIva(new CodiceIva());
        if (rigaMagazzinoDTOResult.getIdCodiceIva() != null) {
            rigaArticoloDTO.getCodiceIva().setId(rigaMagazzinoDTOResult.getIdCodiceIva());
            rigaArticoloDTO.getCodiceIva().setCodice(rigaMagazzinoDTOResult.getCodiceCodiceIva());
            rigaArticoloDTO.getCodiceIva().setPercApplicazione(rigaMagazzinoDTOResult.getPercApplicazioneCodiceIva());
        }
        return rigaArticoloDTO;
    }

    @Override
    public void fillResult(RigaMagazzinoDTOResult rigaMagazzinoDTOResult, List<RigaMagazzinoDTO> result,
            Map<String, RigaMagazzinoDTO> righeComposte) {
        result.add(creaRigaMagazzino(rigaMagazzinoDTOResult));
    }

}
