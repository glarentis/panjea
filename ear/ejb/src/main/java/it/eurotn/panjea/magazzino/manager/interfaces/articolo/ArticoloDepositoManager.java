package it.eurotn.panjea.magazzino.manager.interfaces.articolo;

import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.magazzino.domain.ArticoloDeposito;

import java.util.List;

import javax.ejb.Local;

/**
 * Gestisce il CRUD di {@link ArticoloDeposito}.
 * 
 * @author Leonardo
 */
@Local
public interface ArticoloDepositoManager {

	/**
	 * Cancella l' {@link ArticoloDeposito} scelto.
	 * 
	 * @param articoloDeposito
	 *            il {@link ArticoloDeposito} da cancellare
	 */
	void cancellaArticoloDeposito(ArticoloDeposito articoloDeposito);

	/**
	 * Carica la lista di {@link ArticoloDeposito} del deposito scelto.
	 * 
	 * @param deposito
	 *            deposito di cui caricare gli {@link ArticoloDeposito}
	 * @return List<ArticoloDeposito>
	 */
	List<ArticoloDeposito> caricaArticoliDeposito(Deposito deposito);

	/**
	 * Carica la lista di {@link ArticoloDeposito} dell'articolo scelto.
	 * 
	 * @param idArticolo
	 *            l'id dell'articolo di cui caricare gli {@link ArticoloDeposito}
	 * @return List<ArticoloDeposito>
	 */
	List<ArticoloDeposito> caricaArticoliDeposito(Integer idArticolo);

	/**
	 * Carica il {@link ArticoloDeposito} dell'articolo e deposito scelti.
	 * 
	 * @param idArticolo
	 *            l'id dell'articolo di cui caricare il {@link ArticoloDeposito}
	 * @param idDeposito
	 *            l'id del deposito di cui caricare l' {@link ArticoloDeposito}
	 * @return {@link ArticoloDeposito} caricato, <code>null</code> se non esiste
	 */
	ArticoloDeposito caricaArticoloDeposito(Integer idArticolo, Integer idDeposito);

	/**
	 * Salva l' {@link ArticoloDeposito} creato.
	 * 
	 * @param articoloDeposito
	 *            l' {@link ArticoloDeposito} da salvare
	 * @return {@link ArticoloDeposito} salvato
	 */
	ArticoloDeposito salvaArticoloDeposito(ArticoloDeposito articoloDeposito);

}
