package it.eurotn.panjea.manutenzioni.service.interfaces;

import java.util.List;

import javax.ejb.Remote;

import it.eurotn.panjea.manutenzioni.domain.ProdottoCollegato;

@Remote
public interface ProdottiCollegatiService {

    /**
     * Cancella un {@link ProdottoCollegato}.
     *
     * @param id
     *            id ProdottoCollegato da cancellare
     */
    void cancellaProdottoCollegato(Integer id);

    /**
     * Carica tutti i {@link ProdottoCollegato} presenti.
     *
     * @return {@link ProdottoCollegato} caricati
     */
    List<ProdottoCollegato> caricaProdottiCollegati();

    /**
     * Carica un {@link ProdottoCollegato} in base al suo id.
     *
     * @param id
     *            id
     * @return {@link ProdottoCollegato} caricato
     */
    ProdottoCollegato caricaProdottoCollegatoById(Integer id);

    /**
     * Salva un {@link ProdottoCollegato}.
     *
     * @param prodottoCollegato
     *            {@link ProdottoCollegato} da salvare
     * @return {@link ProdottoCollegato} salvato
     */
    ProdottoCollegato salvaProdottoCollegato(ProdottoCollegato prodottoCollegato);

}
