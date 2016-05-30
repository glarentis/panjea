package it.eurotn.panjea.anagrafica.service.interfaces;

import it.eurotn.panjea.anagrafica.domain.datigeografici.Cap;
import it.eurotn.panjea.anagrafica.domain.datigeografici.DatiGeografici;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo1;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo2;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo3;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo4;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Localita;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Nazione;
import it.eurotn.panjea.anagrafica.domain.datigeografici.SuddivisioneAmministrativa;
import it.eurotn.panjea.anagrafica.domain.datigeografici.SuddivisioneAmministrativa.NumeroLivelloAmministrativo;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface DatiGeograficiService {

	/**
	 * Cancella il cap.
	 * 
	 * @param cap
	 *            il cap da cancellare
	 */
	void cancellaCap(Cap cap);

	/**
	 * Cancella la localita.
	 * 
	 * @param localita
	 *            il localita da cancellare
	 */
	void cancellaLocalita(Localita localita);

	/**
	 * Cancella la nazione.
	 * 
	 * @param nazione
	 *            la nazione da cancellare
	 */
	void cancellaNazione(Nazione nazione);

	/**
	 * Cancella il livello amministrativo corrente.
	 * 
	 * @param suddivisioneAmministrativa
	 *            il livello da eliminare
	 */
	void cancellaSuddivisioneAmministrativa(SuddivisioneAmministrativa suddivisioneAmministrativa);

	/**
	 * Carica i cap disponibili per i datiGeografici correnti.<br>
	 * I valori impostati dei dati geografici vengono utilizzati per filtrare i cap.
	 * 
	 * @param datiGeografici
	 *            i dati geografici utili per filtrare le località da restituire
	 * @return List<Cap> filtrati per dati geografici
	 */
	List<Cap> caricaCap(DatiGeografici datiGeografici);

	/**
	 * Carica il cap con l'id corrente.
	 * 
	 * @param idCap
	 *            l'id del cap da caricare
	 * @return Cap
	 */
	Cap caricaCap(Integer idCap);

	/**
	 * Carica la lista di livelloAmministrativo1 per i datiGeografici correnti.<br>
	 * Il livello 1 è il più generale (nel caso dell'italia equivale alle regioni).
	 * 
	 * @param datiGeografici
	 *            i dati geografici utili per filtrare il primo livello amministrativo
	 * @return List<LivelloAmministrativo1>
	 */
	List<LivelloAmministrativo1> caricaLivelloAmministrativo1(DatiGeografici datiGeografici);

	/**
	 * Carica la lista di livelloAmministrativo2 per i datiGeografici correnti.<br>
	 * Il livello 2 è più dettagliato rispetto al livello 1 (nel caso dell'italia equivale alle province).
	 * 
	 * @param datiGeografici
	 *            i dati geografici utili per filtrare il secondo livello amministrativo
	 * @return List<LivelloAmministrativo2>
	 */
	List<LivelloAmministrativo2> caricaLivelloAmministrativo2(DatiGeografici datiGeografici);

	/**
	 * Carica la lista di livelloAmministrativo3 per i datiGeografici correnti.<br>
	 * Il livello 3 è più dettagliato rispetto al livello 2 (nel caso dell'italia equivale ai comuni).
	 * 
	 * @param datiGeografici
	 *            i dati geografici utili per filtrare il terzo livello amministrativo
	 * @return List<LivelloAmministrativo3>
	 */
	List<LivelloAmministrativo3> caricaLivelloAmministrativo3(DatiGeografici datiGeografici);

	/**
	 * Carica la lista di livelloAmministrativo4 per i datiGeografici correnti.<br>
	 * Il livello 4 è il più dettagliato dei livelli (nel caso dell'italia non è definito).
	 * 
	 * @param datiGeografici
	 *            i dati geografici utili per filtrare il quarto livello amministrativo
	 * @return List<LivelloAmministrativo4>
	 */
	List<LivelloAmministrativo4> caricaLivelloAmministrativo4(DatiGeografici datiGeografici);

	/**
	 * Carica le località disponibili per i datiGeografici correnti.<br>
	 * I valori impostati dei dati geografici vengono utilizzati per filtrare le località.
	 * 
	 * @param datiGeografici
	 *            i dati geografici utili per filtrare le località da restituire
	 * @return List<Localita> filtrate per dati geografici
	 */
	List<Localita> caricaLocalita(DatiGeografici datiGeografici);

	/**
	 * Carica la località con l'id corrente.
	 * 
	 * @param idLocalita
	 *            l'id della località da caricare
	 * @return Localita
	 */
	Localita caricaLocalita(Integer idLocalita);

	/**
	 * Carica le nazioni presenti.
	 * 
	 * @param codice
	 *            codice da filtrare
	 * 
	 * @return Set<Nazione>
	 */
	List<Nazione> caricaNazioni(String codice);

	/**
	 * Carica la lista di {@link SuddivisioneAmministrativa} scelta via {@link NumeroLivelloAmministrativo}.
	 * 
	 * @param datiGeografici
	 *            i dati geografici per filtrare i dati del livello amministrativo scelto
	 * @param lvl
	 *            il livello richiesto da caricare
	 * @param filtro
	 *            valore da filtrare
	 * @return List<SuddivisioneAmministrativa>
	 */
	List<SuddivisioneAmministrativa> caricaSuddivisioniAmministrative(DatiGeografici datiGeografici,
			SuddivisioneAmministrativa.NumeroLivelloAmministrativo lvl, String filtro);

	/**
	 * Salva il cap corrente.
	 * 
	 * @param cap
	 *            il cap da salvare
	 * @return Cap
	 */
	Cap salvaCap(Cap cap);

	/**
	 * Salva il cap con la lista di località associate.
	 * 
	 * @param cap
	 *            il cap da salvare
	 * @param listLocalita
	 *            la lista di località da associare al cap
	 * @return Cap
	 */
	Cap salvaCap(Cap cap, List<Localita> listLocalita);

	/**
	 * Salva il cap e allinea le località associate.
	 * 
	 * @param cap
	 *            il cap da salvare
	 * @param localitaAggiunte
	 *            le località da aggiungere
	 * @param localitaRimosse
	 *            le località da rimuovere
	 * @return Cap
	 */
	Cap salvaCap(Cap cap, List<Localita> localitaAggiunte, List<Localita> localitaRimosse);

	/**
	 * Salva la località corrente.
	 * 
	 * @param localita
	 *            la localita da salvare
	 * @return Localita
	 */
	Localita salvaLocalita(Localita localita);

	/**
	 * Salva la località corrente associando i cap.
	 * 
	 * @param localita
	 *            la localita da salvare
	 * @param caps
	 *            i cap da associare alla località
	 * @return Localita
	 */
	Localita salvaLocalita(Localita localita, List<Cap> caps);

	/**
	 * Salva la località corrente allineando la lista di cap associati.
	 * 
	 * @param localita
	 *            la località da salvare
	 * @param capsAggiunti
	 *            i cap da aggiungere alla località
	 * @param capsRimossi
	 *            i cap da rimuovere dalla località
	 * @return Localita
	 */
	Localita salvaLocalita(Localita localita, List<Cap> capsAggiunti, List<Cap> capsRimossi);

	/**
	 * Salva la nazione.
	 * 
	 * @param nazione
	 *            la nazione da salvare
	 * @return Nazione
	 */
	Nazione salvaNazione(Nazione nazione);

	/**
	 * Salva la suddivisione amministrativa.
	 * 
	 * @param suddivisioneAmministrativa
	 *            il livello amministrativo da salvare
	 * @return SuddivisioneAmministrativa
	 */
	SuddivisioneAmministrativa salvaSuddivisioneAmministrativa(SuddivisioneAmministrativa suddivisioneAmministrativa);
}
