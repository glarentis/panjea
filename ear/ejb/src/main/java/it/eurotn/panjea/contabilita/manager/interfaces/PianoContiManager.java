package it.eurotn.panjea.contabilita.manager.interfaces;

import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.RapportoBancario;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioAzienda;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.contabilita.domain.ContiBase;
import it.eurotn.panjea.contabilita.domain.Conto;
import it.eurotn.panjea.contabilita.domain.Conto.SottotipoConto;
import it.eurotn.panjea.contabilita.domain.ContoBase;
import it.eurotn.panjea.contabilita.domain.ETipoContoBase;
import it.eurotn.panjea.contabilita.domain.Mastro;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.contabilita.service.exception.ContiBaseException;
import it.eurotn.panjea.contabilita.service.exception.ContoRapportoBancarioAssenteException;
import it.eurotn.panjea.contabilita.service.exception.TipoContoBaseEsistenteException;
import it.eurotn.panjea.contabilita.util.ParametriRicercaSottoConti;

import java.util.List;

import javax.ejb.Local;

@Local
public interface PianoContiManager {

	/**
	 * Esegue la cancellazione dell'argomento <code>Conto</code>.
	 * 
	 * @param conto
	 */
	void cancellaConto(Conto conto);

	/**
	 * Cancella un <code>ContoBase</code>.
	 * 
	 * @param contoBase
	 *            <code>ContoBase</code> da cancellare
	 */
	void cancellaContoBase(ContoBase contoBase);

	/**
	 * Esegue la cancellazione dell'argomento <code>Mastro</code>.
	 * 
	 * @param mastro
	 */
	void cancellaMastro(Mastro mastro);

	/**
	 * Esegue la cancellazione dell'argomento <code>SottoConto</code>.
	 * 
	 * @param sottoConto
	 */
	void cancellaSottoConto(SottoConto sottoConto);

	/**
	 * Carica tutti i conti base.
	 * 
	 * @return <code>List</code> di <code>ContoBase</code> caricati
	 * @throws ContabilitaException
	 *             ContabilitaException
	 */
	List<ContoBase> caricaContiBase() throws ContabilitaException;

	/**
	 * Restituisce l'istanza di <code>Conto</code> identificata dell'argomento id.
	 * 
	 * @param idConto
	 *            idConto
	 * @return Conto
	 * @throws ContabilitaException
	 *             ContabilitaException
	 */
	Conto caricaConto(Integer idConto) throws ContabilitaException;

	/**
	 * Carica e restituisce il {@link SottoConto} per l'anticipo fatture collegato al {@link RapportoBancario} passato
	 * come argomento.
	 * 
	 * @param rapportoBancario
	 *            rapportoBancario
	 * @return SottoConto
	 */
	SottoConto caricaContoAnticipoFatturePerRapportoBancario(RapportoBancarioAzienda rapportoBancario);

	/**
	 * Carica un conto base dalla sua descrizione.
	 * 
	 * @param descrizione
	 *            descrizione
	 * @return <code>ContoBase</code> caricato
	 * @throws ContabilitaException
	 *             ContabilitaException
	 */
	ContoBase caricaContoBase(String descrizione) throws ContabilitaException;

	/**
	 * Carica e restituisce il {@link SottoConto} per gli effetti attivi collegato al {@link RapportoBancario} passato
	 * come argomento.
	 * 
	 * @param rapportoBancario
	 *            rapportoBancario
	 * @return SottoConto
	 */
	SottoConto caricaContoEffettiAttiviPerRapportoBancario(RapportoBancarioAzienda rapportoBancario);

	/**
	 * Carica e restituisce il {@link SottoConto} collegato al {@link RapportoBancario} passato come argomento.
	 * 
	 * @param rapportoBancario
	 * @return SottoConto
	 * @throws ContoRapportoBancarioAssenteException
	 */
	SottoConto caricaContoPerRapportoBancario(RapportoBancario rapportoBancario)
			throws ContoRapportoBancarioAssenteException;

	/**
	 * carica il {@link SottoConto} legato al {@link ETipoContoBase} passato come argomento.
	 * 
	 * @param tipoContoBase
	 * @return
	 * @throws ContabilitaException
	 * @throws ContiBaseException
	 */
	SottoConto caricaContoPerTipoContoBase(ETipoContoBase tipoContoBase) throws ContabilitaException,
			ContiBaseException;

	/**
	 * Restituisce una collezione di tutte la classi di tipo <code>Mastro</code>.
	 * 
	 * @return
	 * @throws ContabilitaException
	 */
	List<Mastro> caricaMastri() throws ContabilitaException;

	/**
	 * Restituisce l'istanza di <code>Mastro</code> identificata dall'argomento id.
	 * 
	 * @param mastro
	 * @return
	 */
	Mastro caricaMastro(Integer idMastro) throws ContabilitaException;

	/**
	 * Restituisce l'istanza di <code>Mastro</code> dato il suo codice e l'azienda associata.
	 * 
	 * @param codiceMastro
	 * @param codiceAzienda
	 * @return
	 */
	Mastro caricaMastroByCodice(String codiceMastro, String codiceAzienda);

	/**
	 * Restituisce l'istanza di <code>SottoConto</code> identificata dell'argomento id.
	 * 
	 * @param idSottoConto
	 * @return
	 */
	SottoConto caricaSottoConto(Integer idSottoConto) throws ContabilitaException;

	/**
	 * Carica un sottoconto tramite il suo codice.
	 * 
	 * @param codiceSottoConto
	 *            codice del sottoconto
	 * @return <code>SottoConto</code> caricato
	 */
	SottoConto caricaSottoConto(String codiceSottoConto) throws ContabilitaException;

	/**
	 * 
	 * @param codiceConto
	 * @param codiceSottoConto
	 * @return
	 */
	SottoConto caricaSottoContoPerCodiceImportazione(String codiceMastro, String codiceConto,
			String codiceImportazioneSottoConto) throws ContabilitaException;

	/**
	 * Restituisce il {@link SottoConto} di {@link EntitaLite}.
	 * 
	 * @param entitaLite
	 * @return {@link SottoConto} di entitaLite
	 * @throws ContabilitaException
	 */
	SottoConto caricaSottoContoPerEntita(EntitaLite entitaLite) throws ContabilitaException;

	/**
	 * Restituisce il {@link SottoConto} identificato.
	 * 
	 * @param codiceEntita
	 * @return
	 * @throws ContabilitaException
	 */
	SottoConto caricaSottoContoPerEntita(SottotipoConto sottotipoConto, Integer codiceEntita)
			throws ContabilitaException;

	/**
	 * 
	 * @return @Conti per l'azienda loggata.
	 * @throws ContabilitaException
	 */
	ContiBase caricaTipiContoBase() throws ContabilitaException;

	/**
	 * Esegue la creazione del {@link SottoConto} per {@link Entita}.
	 * 
	 * @param entita
	 *            entita per la quale creare il sottoconto
	 * @return create per l'entità
	 * @throws ContabilitaException
	 *             eccezione generica
	 */
	SottoConto creaSottoContoPerEntita(Entita entita) throws ContabilitaException;

	/**
	 * Esegue la ricerca di tutti i {@link Conto}.
	 * 
	 * @return List<Conto>
	 */
	List<Conto> ricercaConti();

	/**
	 * Esegue la ricerca dei {@link Conto} per {@link SottotipoConto} e per codiceConto.
	 * 
	 * @param codiceConto
	 *            definito secondo questa codifica MMM.CCC
	 * @param sottoTipoConto
	 * @return
	 * @throws ContabilitaException
	 */
	List<Conto> ricercaConti(String codiceConto, SottotipoConto sottoTipoConto) throws ContabilitaException;

	List<Conto> ricercaConti(String codiceMastro, String codiceConto, SottotipoConto sottotipoConto);

	/**
	 * Restituisce una collezione di {@link SottoConto}.
	 * 
	 * @return
	 * @throws ContabilitaException
	 */
	List<SottoConto> ricercaSottoConti() throws ContabilitaException;

	/**
	 * Restituisce una collezione di {@link SottoConto} filtrata per codice/descrizione, per {@link SottotipoConto}.
	 * 
	 * @param parametriRicercaSottoConti
	 * @return
	 * @throws ContabilitaException
	 */
	List<SottoConto> ricercaSottoConti(ParametriRicercaSottoConti parametriRicercaSottoConti)
			throws ContabilitaException;

	/**
	 * Restituisce una collezione di {@link SottoConto} filtrata per l'argomento <code>codice</code>.
	 * 
	 * @param codiceSottoConto
	 *            definito secondo questa codifica MMM.CCC.SSSSSS, viene perci� ripartito e l'effettiva ricerca avviene
	 *            sul codice del mastro, codice del conto ed infine sottoconto
	 * @return
	 * @throws ContabilitaException
	 */
	List<SottoConto> ricercaSottoConti(String codiceSottoConto) throws ContabilitaException;

	List<SottoConto> ricercaSottoConti(String codiceMastro, String codiceConto, String codiceSottoConto);

	/**
	 * Restituisce una collezione di {@link SottoConto} ordinati per mastro conto e sottoconto.
	 * 
	 * @return
	 * @throws ContabilitaException
	 */
	List<SottoConto> ricercaSottoContiOrdinati() throws ContabilitaException;

	/**
	 * Restituisce una collezione di {@link SottoConto} filtrata per codice/descrizione, per {@link SottotipoConto}
	 * ottimizzata.
	 * 
	 * @param parametriRicercaSottoConti
	 *            parametri di ricerca
	 * @return lista di sottoconti
	 */
	List<SottoConto> ricercaSottoContiSearchObject(ParametriRicercaSottoConti parametriRicercaSottoConti);

	/**
	 * Esegue il salvataggio dell'argomento <code>Conto</code> e lo restituisce aggiornato.
	 * 
	 * @param conto
	 * @return
	 */
	Conto salvaConto(Conto conto);

	/**
	 * Salva un <code>ContoBase</code>.
	 * 
	 * @param contoBase
	 *            <code>ContoBase</code> da salvare
	 * @return <code>ContoBase</code> salvato
	 */
	ContoBase salvaContoBase(ContoBase contoBase) throws TipoContoBaseEsistenteException, ContabilitaException;

	/**
	 * Esegue il salvataggio dell'argomento <code>Mastro</code> e lo restituisce aggiornato.
	 * 
	 * @param mastro
	 * @return
	 */
	Mastro salvaMastro(Mastro mastro);

	/**
	 * Esegue il salvataggio dell'argomento <code>SottoConto</code> e lo restituisce aggiornato.
	 * 
	 * @param sottoConto
	 * @return
	 */
	SottoConto salvaSottoConto(SottoConto sottoConto);
}
