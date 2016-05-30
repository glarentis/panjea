package it.eurotn.panjea.pagamenti.manager.interfaces;

import java.util.List;

import javax.ejb.Local;

import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento.TipoRicercaCodicePagamento;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento.TipologiaPartita;
import it.eurotn.panjea.pagamenti.service.exception.CodicePagamentoEsistenteException;
import it.eurotn.panjea.pagamenti.service.exception.CodicePagamentoNonTrovatoException;

@Local
public interface CodicePagamentoManager {

  /**
   * Esegue la cancellazione.
   *
   * @param codicePagamento
   *          codicePagamento da cancellare
   */
  void cancellaCodicePagamento(CodicePagamento codicePagamento);

  /**
   * Metodo per caricare tutto il codice di pagamento completo di strutture di calcolo con ID.
   *
   * @param id
   *          id del codice da caricare
   * @return istanza di <code>CodicePagamento</code> identificata da id
   */
  CodicePagamento caricaCodicePagamento(Integer id);

  /**
   * Metodo per caricare tutto il codice di pagamento completo di strutture di calcolo con CODICE.
   *
   * @param codice
   *          codice da caricare
   * @return istanza di <code>CodicePagamento</code> identificata da id
   */
  CodicePagamento caricaCodicePagamento(String codice);

  /**
   * Metodo per caricare tutto il codice di pagamento con la TipologiaRata (tipicamente per
   * procedure di generazione auto di rate. Es. Liquidaz. IVA)
   *
   * @param tipologiaRata
   *          tipologia rata
   * @return istanza di <code>CodicePagamento</code> identificata da id
   * @throws CodicePagamentoNonTrovatoException
   *           sollevata se il codice pagamento non esiste
   */
  CodicePagamento caricaCodicePagamento(TipologiaPartita tipologiaRata)
      throws CodicePagamentoNonTrovatoException;

  /**
   * Restituisce la lista di <code>CodicePagamento</code> eseguendo il filtro per il parametro
   * <code>filterCodice</code> e filtrando ulteriormente per il codice dell'azienda corrente.
   *
   * @param filtro
   *          valore del filtro della ricerca
   * @param tipoRicerca
   *          tipo di ricerca
   * @param includiDisabilitati
   *          include nei risultati anche i codici pagamento disabilitati
   * @return codici pagamento caricati
   */
  List<CodicePagamento> caricaCodiciPagamento(String filtro, TipoRicercaCodicePagamento tipoRicerca,
      boolean includiDisabilitati);

  /**
   * Carica tutti i codici pagamento per il POS.
   *
   * @return codici pagamento caricati
   */
  List<CodicePagamento> caricaCodiciPagamentoPos();

  /**
   * Metodo per il salvataggio di <code>CodicePagamento</code> Test OK.
   *
   * @param codicePagamento
   *          codice da salvare
   * @return istanza di <code>CodicePagamento</code> salvata
   * @throws CodicePagamentoEsistenteException
   *           eccezione generica
   */
  CodicePagamento salvaCodicePagamento(CodicePagamento codicePagamento)
      throws CodicePagamentoEsistenteException;

}
