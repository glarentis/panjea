package it.eurotn.panjea.magazzino.manager.interfaces;

import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.Giacenza;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino.StatoAreaMagazzino;
import it.eurotn.panjea.magazzino.util.DisponibilitaArticolo;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;

/**
 * Calcola le giacenze per un articolo.
 *
 * @author Leonardo
 */
@Local
public interface GiacenzaManager {

	/**
	 *
	 * @param articoli
	 *            articoli per i quali calcolare la disp.
	 * @param idDeposito
	 *            deposito da filtrare
	 * @param dataDisponibilita
	 *            calcola la disponibilità fino ad una determinata data.
	 * @return mappa con nome deposito contenente mappa con id articolo e lista di movimenti.
	 */
	Map<String, Map<Integer, List<DisponibilitaArticolo>>> calcolaDisponibilita(List<Articolo> articoli,
			Integer idDeposito, Date dataDisponibilita);

	/**
	 * Calcola la disponibilità per l'articolo.
	 *
	 * @param articolo
	 *            articolo interessato
	 * @param dataConsegna
	 *            data per la quale calcoalre la disp.
	 * @param idDeposito
	 *            deposito
	 * @return disponibilità articolo per quel deposito e data.
	 */
	double calcolaDisponibilitaArticolo(Articolo articolo, Date dataConsegna, Integer idDeposito);

	/**
	 * Calcola la giacenza ad una determinata data per tutti gli articoli di un deposito.<br/>
	 *
	 * @param data
	 *            data per la giacenza
	 * @param depositoLite
	 *            deposito interessato alla giacenza
	 * @return mappa contenente l'articolo come chiave e la sua giacenza come valore
	 */
	Map<ArticoloLite, Double> calcolaGiacenze(DepositoLite depositoLite, Date data);

	/**
	 *
	 * @param data
	 *            data per la giacenza
	 * @param depositoLite
	 *            deposito interessato alla giacenza
	 * @param articoli
	 *            articoli da filtrare
	 * @return mappa contenente l'articolo come chiave e la sua giacenza come valore
	 */
	Map<ArticoloLite, Double> calcolaGiacenze(DepositoLite depositoLite, List<Integer> articoli, Date data);

	/**
	 * Calcola le giacenze per ogni articolo e deposito.
	 *
	 * @param depositoLite
	 *            deposito
	 * @param data
	 *            ata per il calcolo
	 * @return giacenze
	 */
	List<Giacenza> calcolaGiacenzeFlat(DepositoLite depositoLite, Date data);

	/**
	 * Calcola la giacenza ad una det. data per una lista di articoli
	 *
	 * @param depositoLite
	 *            deposito
	 * @param articoli
	 *            articoli da filtrare
	 * @param data
	 *            data delal giacenza
	 * @return giacenze
	 */
	List<Giacenza> calcolaGiacenzeFlat(DepositoLite depositoLite, List<Integer> articoli, Date data);

	/**
	 * Calcola la giacenza ad una determinata data per un articolo.<br/>
	 * Viene calcolata la giacenza al primo inventario utile (la somma delle qta per articolo nel movimento di
	 * inventario) e aggiunta la qta movimentata dalla data dell'inventario fino alla data richiesta.<br/>
	 * La giacenza viene calcolata dalle query sql presenti nel file
	 * <code>it.eurotn.panjea.magazzino.manager.sqlbuilder.Giacenza.sql</code><br>
	 * Considera solamente i movimenti in stato {@link StatoAreaMagazzino#CONFERMATO} e
	 * {@link StatoAreaMagazzino#VERIFICATO}
	 *
	 * @param articolo
	 *            articolo interessato al calcolo
	 * @param data
	 *            data per la giacenza
	 * @param depositoLite
	 *            deposito interessato alla giacenza
	 * @param codiceAzienda
	 *            codice azienda interessata al calcolo
	 * @return giacenza dell'articolo e del deposito
	 */
	Giacenza calcoloGiacenza(ArticoloLite articolo, Date data, DepositoLite depositoLite, String codiceAzienda);

}
