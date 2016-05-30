package it.eurotn.panjea.magazzino.manager.interfaces;

import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.service.exception.ContiBaseException;
import it.eurotn.panjea.magazzino.domain.CategoriaContabileArticolo;
import it.eurotn.panjea.magazzino.domain.CategoriaContabileDeposito;
import it.eurotn.panjea.magazzino.domain.CategoriaContabileSedeMagazzino;
import it.eurotn.panjea.magazzino.domain.DatiGenerazione.TipoGenerazione;
import it.eurotn.panjea.magazzino.domain.SottoContoContabilizzazione;
import it.eurotn.panjea.magazzino.domain.SottoContoContabilizzazione.ETipoEconomico;
import it.eurotn.panjea.magazzino.service.exception.ContabilizzazioneException;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;

import java.util.List;

import javax.ejb.Local;

@Local
public interface MagazzinoContabilizzazioneManager {

	/**
	 * 
	 * @param categoriaContabileArticolo
	 *            categoriaContabile da cancellare
	 */
	void cancellaCategoriaContabileArticolo(CategoriaContabileArticolo categoriaContabileArticolo);

	/**
	 * 
	 * @param categoriaContabileDeposito
	 *            categoriaContabileDeposito da cancellare
	 */
	void cancellaCategoriaContabileDeposito(CategoriaContabileDeposito categoriaContabileDeposito);

	/**
	 * 
	 * @param categoriaContabileSedeMagazzino
	 *            categoriaContabileSedeMagazzino da cancellare
	 */
	void cancellaCategoriaContabileSedeMagazzino(CategoriaContabileSedeMagazzino categoriaContabileSedeMagazzino);

	/**
	 * .
	 * 
	 * @param id
	 *            id del sottoconto da cancellare
	 */
	void cancellaSottoContoContabilizzazione(Integer id);

	/**
	 * Carica tutte le aree magazzino che prevedono un {@link AreaContabile} ma che non è ancora stata creata.
	 * 
	 * @param tipoGenerazione
	 *            tipo generazione
	 * @param anno
	 *            anno di riferimento
	 * @return lista di aree trovate
	 */
	List<AreaMagazzinoRicerca> caricaAreeMAgazzinoDaContabilizzare(TipoGenerazione tipoGenerazione, int anno);

	/**
	 * @param fieldSearch
	 *            .
	 * @param valueSearch
	 *            .
	 * @return carica tutte le CategoriaContabileArticolo
	 */
	List<CategoriaContabileArticolo> caricaCategorieContabileArticolo(String fieldSearch, String valueSearch);

	/**
	 * 
	 * @param fieldSearch
	 *            .
	 * @param valueSearch
	 *            .
	 * @return tutte le CategoriaContabileDeposito
	 */
	List<CategoriaContabileDeposito> caricaCategorieContabileDeposito(String fieldSearch, String valueSearch);

	/**
	 * 
	 * @param fieldSearch
	 *            .
	 * @param valueSearch
	 *            .
	 * @return tutte le CategoriaContabileSedeMagazzino
	 */
	List<CategoriaContabileSedeMagazzino> caricaCategorieContabileSedeMagazzino(String fieldSearch, String valueSearch);

	/**
	 * 
	 * @param tipoEconomico
	 *            tipologia del conto economico (ricavo o costo)
	 * @return SottoContoContabilizzazione per il tipoEconomico ricavo o costo
	 */
	List<SottoContoContabilizzazione> caricaSottoContiContabilizzazione(ETipoEconomico tipoEconomico);

	/**
	 * 
	 * @param id
	 *            id da caricare
	 * @return sottocontoContabilizzazione
	 */
	SottoContoContabilizzazione caricaSottoContoContabilizzazione(Integer id);

	/**
	 * Contabilizza tutte le aree magazzino con l'UUID specificato. La creazione dell'area contabile viene gestita dal
	 * flag generaAreaContabile presente dul tipo area magazzino del documento di destinazione. Se il parametro
	 * forzaGenerazioneAreaContabile viene impostato a <code>true</code> l'area contabile verrà creata sempre.
	 * 
	 * @param idAreeMagazzino
	 *            lista di aree magazzino da contabilizzare
	 * @param forzaGenerazioneAreaContabile
	 *            forza la generazione dell'area contabile
	 * @throws ContabilizzazioneException
	 *             ContabilizzazioneException
	 * @throws ContiBaseException
	 *             lanciata quando manca il conto base spese incasso
	 */
	void contabilizzaAreeMagazzino(List<Integer> idAreeMagazzino, boolean forzaGenerazioneAreaContabile)
			throws ContabilizzazioneException, ContiBaseException;

	/**
	 * Contabilizza tutte le aree magazzino con l'UUID specificato. La creazione dell'area contabile viene gestita dal
	 * flag generaAreaContabile presente dul tipo area magazzino del documento di destinazione. Se il parametro
	 * forzaGenerazioneAreaContabile viene impostato a <code>true</code> l'area contabile verrà creata sempre.
	 * 
	 * @param idAreeMagazzino
	 *            lista di aree magazzino da contabilizzare
	 * @param forzaGenerazioneAreaContabile
	 *            forza la generazione dell'area contabile
	 * @param annoContabile
	 *            anno contabile
	 * @throws ContabilizzazioneException
	 *             ContabilizzazioneException
	 * @throws ContiBaseException
	 *             lanciata quando manca il conto base spese incasso
	 */
	void contabilizzaAreeMagazzino(List<Integer> idAreeMagazzino, boolean forzaGenerazioneAreaContabile,
			int annoContabile) throws ContabilizzazioneException, ContiBaseException;

	/**
	 * Contabilizza tutte le aree magazzino con l'UUID specificato. La creazione dell'area contabile viene gestita dal
	 * flag generaAreaContabile presente dul tipo area magazzino del documento di destinazione. Se il parametro
	 * forzaGenerazioneAreaContabile viene impostato a <code>true</code> l'area contabile verrà creata sempre.
	 * 
	 * @param uuid
	 *            UUID di riferimento
	 * @param messageSelector
	 *            definisce il messageSelector da utilizzare per pubblicare i messaggi sulla coda. Se il messageSelector
	 *            è <code>null</code> non verrà pubblicato nessun messaggio sulla coda
	 * @param forzaGenerazioneAreaContabile
	 *            forza la generazione dell'area contabile
	 * @throws ContabilizzazioneException
	 *             ContabilizzazioneException
	 * @throws ContiBaseException
	 *             lanciata quando manca il conto base spese incasso
	 */
	void contabilizzaAreeMagazzino(String uuid, String messageSelector, boolean forzaGenerazioneAreaContabile)
			throws ContabilizzazioneException, ContiBaseException;

	/**
	 * Contabilizza tutte le aree magazzino con l'UUID specificato. La creazione dell'area contabile viene gestita dal
	 * flag generaAreaContabile presente dul tipo area magazzino del documento di destinazione. Se il parametro
	 * forzaGenerazioneAreaContabile viene impostato a <code>true</code> l'area contabile verrà creata sempre.
	 * 
	 * @param uuid
	 *            UUID di riferimento
	 * @param messageSelector
	 *            definisce il messageSelector da utilizzare per pubblicare i messaggi sulla coda. Se il messageSelector
	 *            è <code>null</code> non verrà pubblicato nessun messaggio sulla coda
	 * @param forzaGenerazioneAreaContabile
	 *            forza la generazione dell'area contabile
	 * @param annoContabile
	 *            anno contabile
	 * @throws ContabilizzazioneException
	 *             ContabilizzazioneException
	 * @throws ContiBaseException
	 *             lanciata quando manca il conto base spese incasso
	 */
	void contabilizzaAreeMagazzino(String uuid, String messageSelector, boolean forzaGenerazioneAreaContabile,
			int annoContabile) throws ContabilizzazioneException, ContiBaseException;

	/**
	 * 
	 * @param categoriaContabileArticolo
	 *            categoria contabile articolo da salvare
	 * @return categoria salvata
	 */
	CategoriaContabileArticolo salvaCategoriaContabileArticolo(CategoriaContabileArticolo categoriaContabileArticolo);

	/**
	 * 
	 * @param categoriaContabileDeposito
	 *            categoriaContabileDeposito da salvare
	 * @return categoriaContabileDeposito salvata
	 */
	CategoriaContabileDeposito salvaCategoriaContabileDeposito(CategoriaContabileDeposito categoriaContabileDeposito);

	/**
	 * 
	 * @param categoriaContabileSedeMagazzino
	 *            categoriaContabileSedeMagazzino da salvare
	 * @return categoriaContabileSedeMagazzino salvata
	 */
	CategoriaContabileSedeMagazzino salvaCategoriaContabileSedeMagazzino(
			CategoriaContabileSedeMagazzino categoriaContabileSedeMagazzino);

	/**
	 * 
	 * @param sottoContoContabilizzazione
	 *            sottoContoContabilizzazione da salvare
	 * @return sottoContoContabilizzazione salvato
	 */
	SottoContoContabilizzazione salvaSottoContoContabilizzazione(SottoContoContabilizzazione sottoContoContabilizzazione);

}
