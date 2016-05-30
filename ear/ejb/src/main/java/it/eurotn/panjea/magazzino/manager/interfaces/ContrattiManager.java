package it.eurotn.panjea.magazzino.manager.interfaces;

import it.eurotn.panjea.anagrafica.domain.Contatto;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.magazzino.domain.Categoria;
import it.eurotn.panjea.magazzino.domain.CategoriaMagazzino;
import it.eurotn.panjea.magazzino.domain.CategoriaSedeMagazzino;
import it.eurotn.panjea.magazzino.domain.Contratto;
import it.eurotn.panjea.magazzino.domain.RigaContratto;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino;
import it.eurotn.panjea.magazzino.domain.SedeMagazzinoLite;
import it.eurotn.panjea.magazzino.util.ContrattoProspettoDTO;
import it.eurotn.panjea.magazzino.util.ContrattoStampaDTO;
import it.eurotn.panjea.magazzino.util.ParametriRicercaContratti;
import it.eurotn.panjea.magazzino.util.RigaContrattoCalcolo;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaStampaContratti;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;

/**
 * Interfaccia di ContattiManager: il manager si fa carico delle operazione CRUD sulle classi {@link Contatto} e della
 * gestione delle associazione di questo con {@link Categoria}, {@link CategoriaMagazzino}. <br/>
 * E' incaricato della gestione del CRUD {@link RigaContratto} <br/>
 * E' gestito anche il CRUD di {@link RigaContratto}
 *
 * @author adriano
 * @version 1.0, 17/giu/08
 */
@Local
public interface ContrattiManager {

	/**
	 * Associa al {@link Contratto} la {@link CategoriaSedeMagazzino}.
	 *
	 * @param contratto
	 *            il contratto
	 * @param categoriaSedeMagazzino
	 *            la categoria da associare
	 * @return Contratto
	 */
	Contratto associaCategoriaSedeContratto(Contratto contratto, CategoriaSedeMagazzino categoriaSedeMagazzino);

	/**
	 * Associa al {@link Contratto} l'{@link EntitaLite}.
	 *
	 * @param contratto
	 *            contratto
	 * @param entitaLite
	 *            entità da associare
	 * @return Contratto
	 */
	Contratto associaEntitaContratto(Contratto contratto, EntitaLite entitaLite);

	/**
	 * Associa al {@link Contratto} la {@link SedeMagazzinoLite}.
	 *
	 * @param contratto
	 *            il contratto
	 * @param sedeMagazzinoLite
	 *            la sede magazzino da associare
	 * @return Contratto
	 */
	Contratto associaSedeContratto(Contratto contratto, SedeMagazzinoLite sedeMagazzinoLite);

	/**
	 * Cancella l'oggetto contratto.
	 *
	 * @param contratto
	 *            il contratto da cancellare
	 */
	void cancellaContratto(Contratto contratto);

	/**
	 * Esegue la cancellazione di {@link RigaContratto}.
	 *
	 * @param rigaContratto
	 *            la riga da cancellare
	 */
	void cancellaRigaContratto(RigaContratto rigaContratto);

	/**
	 * Carica e restituisce tutti i {@link Contratto} che rispondono ai valori di {@link ParametriRicercaContratti}.
	 *
	 * @param parametriRicercaContratti
	 *            i parametri per filtrare i contratti
	 * @return List<Contratto>
	 */
	List<Contratto> caricaContratti(ParametriRicercaContratti parametriRicercaContratti);

	/**
	 * Carica un {@link Contratto} e lo restituisce. se l'argomento loadLazy e' true initializza le sue
	 * {@link Collection}.
	 *
	 * @param contratto
	 *            il contratto da caricare
	 * @param loadCollection
	 *            definisce se caricare le collection lazy di contratto
	 * @return {@link Contratto}
	 */
	Contratto caricaContratto(Contratto contratto, boolean loadCollection);

	/**
	 * Carica tutti i contratti legati alla sede o a tutte le sedi se il parametro è uguale a <code>null</code>.
	 *
	 * @param idSedeEntita
	 *            id della sede, <code>null</code> per tutte le sedi
	 * @param data
	 *            data per la ricerca dei contratti
	 * @return contratti trovate
	 */
	List<ContrattoProspettoDTO> caricaContrattoProspetto(Integer idSedeEntita, Date data);

	/**
	 * Carica {@link RigaContratto} e lo restituisce.
	 *
	 * @param rigaContratto
	 *            da caricare
	 * @return {@link RigaContratto}
	 */
	RigaContratto caricaRigaContratto(RigaContratto rigaContratto);

	/**
	 * Carica la list di {@link RigaContratto} associata all'oggetto {@link Contratto} passato come argomento.
	 *
	 * @param contratto
	 *            il contratto di cui caricare le righe
	 * @return List<RigaContratto>
	 */
	List<RigaContratto> caricaRigheContratto(Contratto contratto);

	/**
	 * Carica le righe contratto presenti ad una determinata data associate ad articolo e sede (o tutti gli articoli o
	 * tutte le sedi).<br>
	 * Vendono ordinate per data inizio contratto.
	 *
	 * @param idArticolo
	 *            articolo legato al contratto. Se nullo carica tutte le righe senza filtrare per articolo
	 * @param idEntita
	 *            id entità legata al contratto
	 * @param idSedeEntita
	 *            id sede entita legata al contratto (NB. passiamo solamente l'id per evitare di caricare altri dati. Le
	 *            prestazioni in questo metodo devono essere massimo)
	 * @param idCategoriaSedeMagazzino
	 *            id categoria sede magazzino
	 * @param idCategoriaCommercialeArticolo
	 *            id categoria commerciale
	 * @param data
	 *            la data
	 * @param codiceValuta
	 *            codice della valuta di riferimento
	 * @param idAgente
	 *            id agente per il calcolo dell provvigione
	 * @param tutteLeRighe
	 *            le righe. Restituisce tutte le righe contratti, gli altri parametri devono essere messi a null
	 * @return lista di <code>RigaContrattoCalcolo</code> valida
	 */
	List<RigaContrattoCalcolo> caricaRigheContrattoCalcolo(Integer idArticolo, Integer idEntita, Integer idSedeEntita,
			Integer idCategoriaSedeMagazzino, Integer idCategoriaCommercialeArticolo, Date data, String codiceValuta,
			Integer idAgente, boolean tutteLeRighe);

	/**
	 * Carica la stampa dei contratti.
	 *
	 * @param parametri
	 *            parametri di ricerca
	 * @return stampa contratti
	 */
	List<ContrattoStampaDTO> caricaStampaContratti(ParametriRicercaStampaContratti parametri);

	/**
	 * Rimuove dal {@link Contratto} la {@link CategoriaSedeMagazzino}.
	 *
	 * @param contratto
	 *            il contratto da cui rimuovere la categoria
	 * @param categoriaSedeMagazzino
	 *            la categoria da rimuovere
	 * @return Contratto
	 */
	Contratto rimuoviCategoriaSedeContratto(Contratto contratto, CategoriaSedeMagazzino categoriaSedeMagazzino);

	/**
	 * Rimuove dal {@link Contratto} l'{@link EntitaLite}.
	 *
	 * @param contratto
	 *            contratto
	 * @param entitaLite
	 *            entita da rimuovere
	 * @return contratto
	 */
	Contratto rimuoviEntitaContratto(Contratto contratto, EntitaLite entitaLite);

	/**
	 * Rimuove dal {@link Contratto} la {@link SedeMagazzinoLite}.
	 *
	 * @param contratto
	 *            il contratto da cui rimuovere la sede
	 * @param sedeMagazzinoLite
	 *            la sede da rimuovere
	 * @return Contratto
	 */
	Contratto rimuoviSedeContratto(Contratto contratto, SedeMagazzinoLite sedeMagazzinoLite);

	/**
	 * Salva un istanza di {@link Contratto}. il salvataggio prevede il controllo degli attributi tuttiClienti e
	 * tuttiArticoli per invalidare le {@link Collection} verso {@link RigaContratto}, {@link SedeMagazzino} e
	 * {@link CategoriaMagazzino}.
	 *
	 * @param contratto
	 *            il contratto da salvare
	 * @param loadCollection
	 *            se inizializzare le collection lazy di contratto
	 * @return {@link Contratto} salvato
	 */
	Contratto salvaContratto(Contratto contratto, boolean loadCollection);

	/**
	 * Salva un istanza di {@link RigaContratto} e ne ritorna l'istanza resa persistente.
	 *
	 * @param rigaContratto
	 *            la riga da salvare
	 * @return {@link RigaContratto}
	 */
	RigaContratto salvaRigaContratto(RigaContratto rigaContratto);
}
