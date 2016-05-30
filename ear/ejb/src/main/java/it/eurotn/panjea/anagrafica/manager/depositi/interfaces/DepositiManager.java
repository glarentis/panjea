
package it.eurotn.panjea.anagrafica.manager.depositi.interfaces;

import java.util.List;

import javax.ejb.Local;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.anagrafica.domain.SedeAzienda;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.manager.depositi.ParametriRicercaDepositi;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;

@Local
public interface DepositiManager {

    /**
     * Cancella un deposito.
     *
     * @param deposito
     *            Value Object del deposito da cancellare
     * @throws DAOException
     */
    void cancellaDeposito(Deposito deposito) throws DAOException;

    /**
     * Carica i depositi per tutte le sedi dell'azienda.
     *
     * @return lista di Depositi
     */
    List<Deposito> caricaDepositi();

    /**
     * Carica i depositi per l'entità.
     *
     * @param entita
     *            entita di riferimento
     * @return lista di Depositi
     */
    List<Deposito> caricaDepositi(EntitaLite entita);

    /**
     * Carica i depositi per la sede.
     *
     * @param sedeAzienda
     *            . L'ID deve essere settato
     * @return lista di Depositi
     * @throws AnagraficaServiceException
     *             eccezione generale
     */
    List<Deposito> caricaDepositi(SedeAzienda sedeAzienda) throws AnagraficaServiceException;

    /**
     * Carica i depositi per tutte le sedi dell'azienda.
     *
     * @param fieldSearch
     *            .
     * @param valueSearch
     *            .
     * @return lista di Depositi
     */
    List<Deposito> caricaDepositi(String fieldSearch, String valueSearch);

    /**
     * Ricerca i depositi dell'azienda.
     *
     * @return List<DepositoLite>
     */
    List<DepositoLite> caricaDepositiAzienda();

    /**
     * Ricerca i depositi dell'azienda.
     *
     * @param fieldSearch
     *            .
     * @param valueSearch
     *            .
     * @param soloConMezziTrasporto
     *            carica solo i depositi associati ad un mezzo di trasporto
     * @return List<DepositoLite>
     */
    List<DepositoLite> caricaDepositiAzienda(String fieldSearch, String valueSearch, boolean soloConMezziTrasporto);

    /**
     * Recupera {@link Deposito} identificato da idDeposito.
     *
     * @param idDeposito
     *            id del deposito da caricare
     * @return deposito caricato
     */
    Deposito caricaDeposito(Integer idDeposito);

    /**
     *
     * @return deposito principale dell'azienda loggata
     */
    Deposito caricaDepositoPrincipale();

    /**
     * Ricerca i depositi in base ai parametri di ricerca.
     *
     * @param parametri
     *            parametri di ricerca
     * @return depositi trovati
     */
    List<Deposito> ricercaDepositi(ParametriRicercaDepositi parametri);

    /**
     * Salva un deposito.
     *
     * @param deposito
     *            da salvare
     * @return <code>Deposito</code> salvato
     */
    Deposito salvaDeposito(Deposito deposito);

}
