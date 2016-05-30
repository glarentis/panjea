package it.eurotn.panjea.magazzino.manager.interfaces;

import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzinoLite;
import it.eurotn.panjea.magazzino.util.MovimentazioneArticolo;
import it.eurotn.panjea.magazzino.util.ParametriRicercaMovimentazione;
import it.eurotn.panjea.magazzino.util.ParametriRicercaMovimentazioneArticolo;
import it.eurotn.panjea.magazzino.util.RigaMovimentazione;
import it.eurotn.panjea.parametriricerca.domain.Periodo;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

/**
 * Manager per la movimentazione del magazzino, si occupa di interrogare le righe dei documenti di magazzino per
 * analizzare qta, prezzi e altri dati.
 *
 * @author Leonardo
 */
@Local
public interface MagazzinoMovimentazioneManager {

	/**
	 * Carica gli inventari creati in un determinato periodo per un deposito.<br/>
	 * lista vuota se non ne vengono trovati
	 *
	 * @param dataInizio
	 *            data iniziale del periodo
	 * @param dataFine
	 *            data finale del periodo
	 * @param depositoLite
	 *            deposito per l'inventario
	 * @return inventari creati nel periolo
	 */
	List<AreaMagazzino> caricaInventari(Date dataInizio, Date dataFine, DepositoLite depositoLite);

	/**
	 * Carica il primo inventario utile per la data e il deposito selezionati.
	 *
	 * @param data
	 *            Data selezionata
	 * @param deposito
	 *            <code>Deposito</code> selezionato
	 * @return <code>AreaMagazzino</code> che rappresenta l'inventario utile, <code>null</code> se non ne esiste uno
	 */
	AreaMagazzino caricaInventarioUtile(Date data, Deposito deposito);

	/**
	 * Carica la movimentazione del magazzino, per pagina.<br>
	 *
	 * @param parametriRicercaMovimentazione
	 *            sono i parametri per la movimentazione generale per il magazzino
	 * @param page
	 *            pagina
	 * @param sizeOfPage
	 *            numero di righe per pagina
	 * @return List<RigaMovimentazione>
	 */
	List<RigaMovimentazione> caricaMovimentazione(ParametriRicercaMovimentazione parametriRicercaMovimentazione,
			int page, int sizeOfPage);

	/**
	 * Carica la movimentazione (carichi,scarichi,trasferimenti) di un articolo cercando le righe dei documenti di
	 * magazzino.<br>
	 * Calcola inoltre le giacenze di magazzino (precedente,finale,attuale) dell'articolo scelto.
	 *
	 * @param parametriRicercaMovimentazioneArticolo
	 *            i parametri per cercare le righe per un determinato depostito e articolo
	 * @return {@link MovimentazioneArticolo}
	 */
	MovimentazioneArticolo caricaMovimentazioneArticolo(
			ParametriRicercaMovimentazioneArticolo parametriRicercaMovimentazioneArticolo);

	/**
	 * carica movimentazione per indici rotazione
	 *
	 * @param articolo
	 *            articolo
	 * @param deposito
	 *            deposito
	 * @param periodo
	 *            periodo
	 * @return lista movimenti
	 */
	List<Object[]> caricaMovimentazionePerIndiciRotazione(ArticoloLite articolo, DepositoLite deposito, Periodo periodo);

	/**
	 * Carica l'ultimo inventario presente per il deposito specificato.
	 *
	 * @param idDeposito
	 *            id del deposito
	 * @return ultimo inventario, <code>null</code> se non esiste
	 */
	AreaMagazzinoLite caricaUltimoInventario(Integer idDeposito);
}
