package it.eurotn.panjea.magazzino.service.interfaces;

import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino.StatoAreaMagazzino;
import it.eurotn.panjea.magazzino.util.IndiceGiacenzaArticolo;
import it.eurotn.panjea.magazzino.util.MovimentazioneArticolo;
import it.eurotn.panjea.magazzino.util.ParametriRicercaMovimentazione;
import it.eurotn.panjea.magazzino.util.ParametriRicercaMovimentazioneArticolo;
import it.eurotn.panjea.magazzino.util.ParametriRicercaValorizzazione;
import it.eurotn.panjea.magazzino.util.RigaMovimentazione;
import it.eurotn.panjea.magazzino.util.StatisticheArticolo;
import it.eurotn.panjea.magazzino.util.ValorizzazioneArticolo;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriCalcoloIndiciRotazioneGiacenza;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriValorizzazioneDistinte;
import it.eurotn.panjea.mrp.domain.Bom;
import it.eurotn.panjea.mrp.util.ArticoloConfigurazioneKey;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

@Remote
public interface MagazzinoStatisticheService {

	/**
	 * Calcola la disponibilità ad una determinata data per un articolo.<br/>
	 * Viene calcolata la giacenza al primo inventario utile (la somma delle qta per articolo nel movimento di
	 * inventario) e aggiunta la qta movimentata dalla data dell'inventario fino alla data richiesta.<br/>
	 * La giacenza viene calcolata dalle query sql presenti nel file
	 * <code>it.eurotn.panjea.magazzino.manager.sqlbuilder.Giacenza.sql</code><br>
	 * Considera solamente i movimenti in stato {@link StatoAreaMagazzino#CONFERMATO} e
	 * {@link StatoAreaMagazzino#VERIFICATO} Vengono poi aggiunte le qta ordinate da cliente/fornitore o ordini
	 * produzione
	 *
	 * @param idArticolo
	 *            articolo interessato al calcolo
	 * @param data
	 *            data per la disp
	 * @param idDeposito
	 *            deposito interessato alla giacenza
	 * @return giacenza dell'articolo e del deposito
	 */
	Double calcolaDisponibilitaArticolo(int idArticolo, Date data, int idDeposito);

	/**
	 *
	 * @param parametri
	 *            parametriCalcolo
	 * @return indici di rotazione articoli
	 */
	List<IndiceGiacenzaArticolo> calcolaIndiciRotazione(ParametriCalcoloIndiciRotazioneGiacenza parametri);

	/**
	 *
	 * @param parametriRicercaMovimentazione
	 *            parametri di recerca per la movimentazione.
	 * @param page
	 *            pagina.
	 * @param sizeOfPage
	 *            grandeza della pagina.
	 * @return lista {@link RigaMovimentazione}.
	 */
	List<RigaMovimentazione> caricaMovimentazione(ParametriRicercaMovimentazione parametriRicercaMovimentazione,
			int page, int sizeOfPage);

	/**
	 *
	 * @param parametriRicercaMovimentazioneArticolo
	 *            .
	 * @return {@link MovimentazioneArticolo}
	 */
	MovimentazioneArticolo caricaMovimentazioneArticolo(
			ParametriRicercaMovimentazioneArticolo parametriRicercaMovimentazioneArticolo);

	/**
	 *
	 * @param articolo
	 *            {@link Articolo}
	 * @param anno
	 *            numero del anno.
	 * @return {@link StatisticheArticolo}
	 */
	StatisticheArticolo caricaStatisticheArticolo(Articolo articolo, Integer anno);

	/**
	 *
	 * @param parametri
	 *            .
	 * @return lista {@link ValorizzazioneArticolo}
	 */
	List<ValorizzazioneArticolo> caricaValorizzazione(Map<Object, Object> parametri);

	/**
	 *
	 * @param parametriRicercaValorizzazione
	 *            {@link ParametriRicercaValorizzazione}
	 * @return {@link ValorizzazioneArticolo}
	 */
	List<ValorizzazioneArticolo> caricaValorizzazione(ParametriRicercaValorizzazione parametriRicercaValorizzazione);

	/**
	 *
	 * @param parametriValorizzazioneDistinte
	 *            parametri per la valorizzazione
	 * @return mappa con articolo e bom della distinta
	 */
	Map<ArticoloConfigurazioneKey, Bom> valorizzaDistinte(
			ParametriValorizzazioneDistinte parametriValorizzazioneDistinte);
}
