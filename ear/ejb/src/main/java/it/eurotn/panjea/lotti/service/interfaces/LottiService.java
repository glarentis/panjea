package it.eurotn.panjea.lotti.service.interfaces;

import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.lotti.domain.Lotto;
import it.eurotn.panjea.lotti.domain.LottoInterno;
import it.eurotn.panjea.lotti.domain.RigaLotto;
import it.eurotn.panjea.lotti.manager.rimanenzefinali.RimanenzeFinaliDTO;
import it.eurotn.panjea.lotti.util.GiacenzaLotto;
import it.eurotn.panjea.lotti.util.MovimentazioneLotto;
import it.eurotn.panjea.lotti.util.ParametriRicercaScadenzaLotti;
import it.eurotn.panjea.lotti.util.StatisticaLotto;
import it.eurotn.panjea.magazzino.domain.Articolo.TipoLotto;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.TipoMovimento;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Remote;

@Remote
public interface LottiService {

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
			String codice,Date dataScadenza, Date dataInizioRicercaScadenza,boolean cercaSoloLottiAperti);

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
	 * Carica la lista di tutti i lotti aperti in scadenza.
	 *
	 * @param parametri
	 *            parametri di ricerca
	 * @return lista di lotti caricati
	 */
	List<StatisticaLotto> caricaLottiInScadenza(ParametriRicercaScadenzaLotti parametri);

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
	 * @param idArticolo
	 *            id dell'articolo di riferimento
	 * @return lotto caricato, <code>null</code> se il lotto non esiste
	 */
	Lotto caricaLotto(String codice, Date dataScadenza, int idArticolo);

	/**
	 * Carica la movimentazione dei lotti specificati.
	 *
	 * @param param
	 *            parametri
	 * @return movimentazione dei lotti
	 */
	List<MovimentazioneLotto> caricaMovimentazioneLotti(Map<Object, Object> param);

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
	 * Carica le rimanenze finali.
	 *
	 * @param params
	 *            parametri di ricerca
	 * @return rimanenze
	 */
	List<RimanenzeFinaliDTO> caricaRimanenzeFinali(Map<Object, Object> params);

	/**
	 * Carica la situazione dei lotti per l'articolo specificato.
	 *
	 * @param articoloLite
	 *            articolo
	 * @return situazione dei lotti
	 */
	List<StatisticaLotto> caricaSituazioneLotti(ArticoloLite articoloLite);

	/**
	 * Salva un {@link Lotto}.
	 *
	 * @param lotto
	 *            Lotto da salvare
	 * @return Lotto salvato
	 */
	Lotto salvaLotto(Lotto lotto);

	/**
	 * Verifica la giacenza dei lotti restituendo tutti quelli in cui la rimanenza è diversa dalla giacenza
	 * dell'articolo in magazzino.
	 *
	 * @param parametri
	 *            parametri di ricerca
	 * @return giacenze non valide trovate
	 */
	Set<GiacenzaLotto> verificaGiacenzeLotti(Map<Object, Object> parametri);
}
