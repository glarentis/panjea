package it.eurotn.panjea.lotti.manager.interfaces;

import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.lotti.domain.Lotto;
import it.eurotn.panjea.lotti.domain.LottoInterno;
import it.eurotn.panjea.lotti.domain.RigaLotto;
import it.eurotn.panjea.lotti.manager.LottiManagerBean.RimanenzaLotto;
import it.eurotn.panjea.lotti.util.MovimentazioneLotto;
import it.eurotn.panjea.lotti.util.StatisticaLotto;
import it.eurotn.panjea.magazzino.domain.Articolo.TipoLotto;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.TipoMovimento;
import it.eurotn.panjea.magazzino.util.CategoriaLite;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.Local;

@Local
public interface LottiManager {

	/**
	 * Cancella tutti i lottit che non sono più utilizzati.
	 */
	void cancellaLottiNonUtilizzati();

	/**
	 * Carica tutti gli articoli che hanno un lotto con codice uguale a quello passato come parametro.
	 *
	 * @param lotto
	 *            lotto di ricerca
	 * @return lista di articoli
	 */
	List<ArticoloLite> caricaArticoliByCodiceLotto(Lotto lotto);

	/**
	 * Carica tutti gli articoli che hanno un lotto con codice uguale a quello passato come parametro.
	 *
	 * @param lotto
	 *            lotto di ricerca
	 * @param filtraDataScadenza
	 *            utilizza la data scadenza del lotto per la ricerca
	 * @return lista di articoli
	 */
	List<ArticoloLite> caricaArticoliByCodiceLotto(Lotto lotto, boolean filtraDataScadenza);

	/**
	 * Carica tutti i lotti per l'articolo e deposito richiesto.
	 *
	 * @param articolo
	 *            articolo
	 * @param deposito
	 *            deposito
	 * @param rimanenzaLotto
	 *            carica solo i lotti con la rimanenza specificata
	 * @param dataScadenza data scadenza lotti
	 * @param codiceLotto codice lotto
	 * @param dataInizioRicercaScadenza data inizio ricerca
	 *
	 * @return lista di lotti caricati
	 */
	List<Lotto> caricaLotti(ArticoloLite articolo, DepositoLite deposito, RimanenzaLotto rimanenzaLotto,Date dataScadenza,String codiceLotto, Date dataInizioRicercaScadenza);

	/**
	 * Carica tutti i lotti in base ai parametri specificati.
	 *
	 * @param articolo
	 *            articolo di riferimento
	 * @param deposito
	 *            deposito di riferimento.
	 * @param tipoMovimento
	 *            {@link TipoMovimento}
	 * @param storno
	 *            storno
	 * @param codice
	 *            .
	 * @param dataScadenza data scadenza lotti
	 * @param dataInizioRicercaScadenza data inizio ricerca
	 * @param cercaSoloLottiAperti cerca solo i lotti aperti indipendentemente dal tipo movimento richiesto
	 * @return lista di lotti caricati
	 */
	List<Lotto> caricaLotti(ArticoloLite articolo, DepositoLite deposito, TipoMovimento tipoMovimento, boolean storno,
			String codice,Date dataScadenza,Date dataInizioRicercaScadenza,boolean cercaSoloLottiAperti);

	/**
	 * Carica tutti i {@link Lotto} relativi all'articolo ignorando il tipoLotto su articolo ma usando quello passato
	 * come parametro.
	 *
	 * @param articolo
	 *            articolo
	 * @param tipoLotto
	 *            tipo lotto
	 * @param codice
	 *            .
	 * @return lista di lotti caricati
	 */
	List<Lotto> caricaLotti(ArticoloLite articolo, TipoLotto tipoLotto, String codice);

	/**
	 * Carica il lotto specificato. Viene inizializzato anche l'articolo.
	 *
	 * @param lotto
	 *            lotto da caricare
	 * @return lotto caricato
	 */
	Lotto caricaLotto(Lotto lotto);

	/**
	 * Carica il lotto con i paramentri specificati.
	 *
	 * @param codice
	 *            codice del lotto
	 * @param dataScadenza
	 *            data scadenza del lotto
	 * @param articolo
	 *            articolo di riferimento
	 * @return lotto caricato, <code>null</code> se il lotto non esiste
	 */
	Lotto caricaLotto(String codice, Date dataScadenza, ArticoloLite articolo);

	/**
	 * Carica la movimentazione dei lotti specificati.
	 *
	 * @param idLotti
	 *            lotti di riferimento
	 * @return movimentazione dei lotti
	 */
	List<MovimentazioneLotto> caricaMovimentazioneLotti(Set<Integer> idLotti);

	/**
	 * Carica la movimentazione del lotto.
	 *
	 * @param lotto
	 *            lotto di riferimento
	 * @return movimentazione del lotto
	 */
	List<MovimentazioneLotto> caricaMovimentazioneLotto(Lotto lotto);

	/**
	 * Carica la movimentazione del lotto.
	 *
	 * @param lotto
	 *            lotto di riferimento
	 * @return movimentazione del lotto
	 */
	List<MovimentazioneLotto> caricaMovimentazioneLottoInterno(LottoInterno lotto);

	/**
	 * Carica le righeLotto di una area magazzino.
	 *
	 * @param areaMagazzino
	 *            l'area magazzino di cui caricare le righe lotto.
	 * @return le righe lotto dell'area magazzino
	 */
	List<RigaLotto> caricaRigheLotto(AreaMagazzino areaMagazzino);

	/**
	 * Carica le righeLotto di una riga articolo.
	 *
	 * @param rigaArticolo
	 *            riga articolo di cui caricare le righe lotto.
	 * @return le righe lotto della riga articolo
	 */
	List<RigaLotto> caricaRigheLotto(RigaArticolo rigaArticolo);

	/**
	 * Carica la situazione dei lotti per l'articolo specificato.
	 *
	 * @param articoloLite
	 *            articolo
	 * @return situazione dei lotti
	 */
	List<StatisticaLotto> caricaSituazioneLotti(ArticoloLite articoloLite);

	/**
	 * Carica la situazione dei lotti fino alla data di riferimento.
	 *
	 * @param articoloLite
	 *            articolo di cui caricare la situazione. Se <code>null</code> verrà caricata la situazione di tutti gli
	 *            articoli
	 * @param deposito
	 *            deposito
	 * @param data
	 *            data di riferimento
	 * @param categorieArticolo
	 *            categorie articolo da considerare, tutte se <code>null</code> o vuota
	 * @param rimanenzaLotto rimanenza lotto
	 * @param dataScadenza data scadenza lotti
	 * @param codiceLotto codice lotto
	 * @param dataInizioRicercaScadenza data di inizio ricerca per data scandeza lotto
	 * @return situazione dei lotti
	 */
	List<StatisticaLotto> caricaSituazioneLotti(ArticoloLite articoloLite, DepositoLite deposito, Date data,
			List<CategoriaLite> categorieArticolo, RimanenzaLotto rimanenzaLotto, Date dataScadenza, String codiceLotto, Date dataInizioRicercaScadenza);

	/**
	 * Salva un {@link Lotto}.
	 *
	 * @param lotto
	 *            Lotto da salvare
	 * @return Lotto salvato
	 */
	Lotto salvaLotto(Lotto lotto);
}
