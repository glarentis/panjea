package it.eurotn.panjea.magazzino.rich.bd;

import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.magazzino.domain.CategoriaContabileArticolo;
import it.eurotn.panjea.magazzino.domain.CategoriaContabileDeposito;
import it.eurotn.panjea.magazzino.domain.CategoriaContabileSedeMagazzino;
import it.eurotn.panjea.magazzino.domain.DatiGenerazione.TipoGenerazione;
import it.eurotn.panjea.magazzino.domain.DepositoMagazzino;
import it.eurotn.panjea.magazzino.domain.SottoContoContabilizzazione;
import it.eurotn.panjea.magazzino.domain.SottoContoContabilizzazione.ETipoEconomico;
import it.eurotn.panjea.magazzino.service.exception.ContabilizzazioneException;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;
import it.eurotn.panjea.rich.bd.AsyncMethodInvocation;

import java.util.List;

public interface IMagazzinoContabilizzazioneBD {

	void cancellaCategoriaContabileArticolo(CategoriaContabileArticolo categoriaContabileArticolo);

	void cancellaCategoriaContabileDeposito(CategoriaContabileDeposito categoriaContabileDeposito);

	void cancellaCategoriaContabileSedeMagazzino(CategoriaContabileSedeMagazzino categoriaContabileSedeMagazzino);

	void cancellaDepositoMagazzino(Integer id);

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
	@AsyncMethodInvocation
	List<AreaMagazzinoRicerca> caricaAreeMAgazzinoDaContabilizzare(TipoGenerazione tipoGenerazione, int anno);

	List<CategoriaContabileArticolo> caricaCategorieContabileArticolo(String fieldSearch, String valueSearch);

	List<CategoriaContabileDeposito> caricaCategorieContabileDeposito(String fieldSearch, String valueSearch);

	List<CategoriaContabileSedeMagazzino> caricaCategorieContabileSedeMagazzino(String fieldSearch, String valueSearch);

	DepositoMagazzino caricaDepositoMagazzino(Integer id);

	List<SottoContoContabilizzazione> caricaSottoContiContabilizzazione(ETipoEconomico tipoEconomico);

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
	 */
	@AsyncMethodInvocation
	void contabilizzaAreeMagazzino(List<Integer> idAreeMagazzino, boolean forzaGenerazioneAreaContabile)
			throws ContabilizzazioneException;

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
	 */
	@AsyncMethodInvocation
	void contabilizzaAreeMagazzino(List<Integer> idAreeMagazzino, boolean forzaGenerazioneAreaContabile,
			int annoContabile) throws ContabilizzazioneException;

	CategoriaContabileArticolo salvaCategoriaContabileArticolo(CategoriaContabileArticolo categoriaContabileArticolo);

	CategoriaContabileDeposito salvaCategoriaContabileDeposito(CategoriaContabileDeposito categoriaContabileDeposito);

	CategoriaContabileSedeMagazzino salvaCategoriaContabileSedeMagazzino(
			CategoriaContabileSedeMagazzino categoriaContabileSedeMagazzino);

	DepositoMagazzino salvaDepositoMagazzino(DepositoMagazzino depositoMagazzino);

	SottoContoContabilizzazione salvaSottoContoContabilizzazione(SottoContoContabilizzazione sottoContoContabilizzazione);
}
