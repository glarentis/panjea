/**
 *
 */
package it.eurotn.panjea.anagrafica.rich.bd;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.domain.Carica;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.ContrattoSpesometro;
import it.eurotn.panjea.anagrafica.domain.ConversioneUnitaMisura;
import it.eurotn.panjea.anagrafica.domain.FormaGiuridica;
import it.eurotn.panjea.anagrafica.domain.Lingua;
import it.eurotn.panjea.anagrafica.domain.Mansione;
import it.eurotn.panjea.anagrafica.domain.NotaAnagrafica;
import it.eurotn.panjea.anagrafica.domain.TipoDeposito;
import it.eurotn.panjea.anagrafica.domain.TipoSedeEntita;
import it.eurotn.panjea.anagrafica.domain.UnitaMisura;
import it.eurotn.panjea.anagrafica.domain.ZonaGeografica;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.service.interfaces.AnagraficaTabelleService;
import it.eurotn.panjea.rich.bd.AsyncMethodInvocation;

import java.util.List;

/**
 * interfaccia business delegate di {@link AnagraficaTabelleService}.
 * 
 * @author adriano
 * @version 1.0, 18/dic/07
 * 
 */
public interface IAnagraficaTabelleBD {

	/**
	 * Aggiunge il contratto al documento.
	 * 
	 * @param contratto
	 *            il contratto da aggiungere
	 * @param documento
	 *            il documento a cui aggiungere il contratto
	 * @return il documento salvato con associato il contratto
	 */
	Documento aggiungiContrattoADocumento(ContrattoSpesometro contratto, Documento documento);

	/**
	 * Cancella una carica.
	 * 
	 * @param carica
	 *            tag.
	 */
	void cancellaCarica(Carica carica);

	/**
	 * Cancella CodiceIva.
	 * 
	 * @param codiceIva
	 *            codiceIva
	 */
	void cancellaCodiceIva(CodiceIva codiceIva);

	/**
	 * Esegue la cancellazione di {@link ContrattoSpesometro}.
	 * 
	 * @param contratto
	 *            contratto da cancellare
	 */
	void cancellaContratto(ContrattoSpesometro contratto);

	/**
	 * Cancella una conversione unità di musura.
	 * 
	 * @param conversioneUnitaMisura
	 *            <code>ConversioneUnitaMisura</code> da cancellare
	 */
	void cancellaConversioneUnitaMisura(ConversioneUnitaMisura conversioneUnitaMisura);

	/**
	 * Cancella una forma giuridica.
	 * 
	 * @param formaGiuridica
	 *            Value Object della forma giuridica da eliminare @ *
	 */
	void cancellaFormaGiuridica(FormaGiuridica formaGiuridica);

	/**
	 * Cancella lingua.
	 * 
	 * @param lingua
	 *            lingua
	 */
	void cancellaLingua(Lingua lingua);

	/**
	 * cancella {@link Mansione}.
	 * 
	 * @param mansione
	 *            mansione
	 */
	void cancellaMansione(Mansione mansione);

	/**
	 * Cancella una {@link NotaAnagrafica}.
	 * 
	 * @param notaAnagrafica
	 *            nota da cancellare
	 */
	void cancellaNotaAnagrafica(NotaAnagrafica notaAnagrafica);

	/**
	 * Cancella il tipodeposito.
	 * 
	 * @param tipoDeposito
	 *            tipoDeposito
	 */
	void cancellaTipoDeposito(TipoDeposito tipoDeposito);

	/**
	 * Cancella {@link TipoSedeEntita}.
	 * 
	 * @param tipoSedeEntita
	 *            tipoSedeEntita
	 */
	void cancellaTipoSedeEntita(TipoSedeEntita tipoSedeEntita);

	/**
	 * Cancella una unità di musura.
	 * 
	 * @param unitaMisura
	 *            <code>UnitaMisura</code> da cancellare
	 */
	void cancellaUnitaMisura(UnitaMisura unitaMisura);

	/**
	 * Cancella zona geografica.
	 * 
	 * @param zonaGeografica
	 *            zona geografica
	 */
	void cancellaZonaGeografica(ZonaGeografica zonaGeografica);

	/**
	 * Recupera la lista completa di Cariche.
	 * 
	 * @param fieldSearch
	 *            .
	 * @param valueSearch
	 *            .
	 * @return Lista di <code>Carica</code> @
	 */
	@AsyncMethodInvocation
	List<Carica> caricaCariche(String fieldSearch, String valueSearch);

	/**
	 * Carica codice iva.
	 * 
	 * @param id
	 *            l'id del codice iva da caricare
	 * @return CodiceIva
	 */
	CodiceIva caricaCodiceIva(Integer id);

	/**
	 * Carica codici iva.
	 * 
	 * @param codice
	 *            codice per cui cercare codici iva
	 * @return List<CodiceIva>
	 */
	@AsyncMethodInvocation
	List<CodiceIva> caricaCodiciIva(String codice);

	/**
	 * Carica tutti i contratti o i contratti dell'entità.
	 * 
	 * @param entita
	 *            l'entità di cui caricare i contratti o null
	 * @return List<ContrattoSpesometro>
	 */
	@AsyncMethodInvocation
	List<ContrattoSpesometro> caricaContratti(EntitaLite entita);

	/**
	 * Carica il contratto identificato dall'id passato.
	 * 
	 * @param idContratto
	 *            l'id del contratto da caricare
	 * @return ContrattoSpesometro
	 */
	ContrattoSpesometro caricaContratto(Integer idContratto);

	/**
	 * Carica il contratto identificato dall'id passato.
	 * 
	 * @param idContratto
	 *            l'id del contratto da caricare
	 * @param loadCollection
	 *            indica se caricare le collection lazy per il contratto
	 * @return ContrattoSpesometro
	 */
	ContrattoSpesometro caricaContratto(Integer idContratto, boolean loadCollection);

	/**
	 * Carica, se esiste, la conversione unità di misura tra quella di origine e destinazione.
	 * 
	 * @param unitaMisuraOrigine
	 *            unita misura origine
	 * @param unitaMisuraDestinazione
	 *            unita misura destinazione
	 * @return la conversioneUnitaMisura o null
	 */
	ConversioneUnitaMisura caricaConversioneUnitaMisura(String unitaMisuraOrigine, String unitaMisuraDestinazione);

	/**
	 * Carica tutte le conversioni unità di misura.
	 * 
	 * @return {@link list} di {@link ConversioneUnitaMisura} caricate
	 */
	List<ConversioneUnitaMisura> caricaConversioniUnitaMisura();

	/**
	 * Carica i documenti collegati al contratto scelto.
	 * 
	 * @param idContratto
	 *            l'id del contratto di cui caricare i documenti
	 * @return la lista di documenti per il contratto scelto
	 */
	@AsyncMethodInvocation
	List<Documento> caricaDocumentiContratto(Integer idContratto);;

	/**
	 * Carica una forma giuridica.
	 * 
	 * @param idFormaGiuridica
	 *            Codice della forma giuridica da caricare
	 * @return Value Object della forma giuridica caricata @ * @throws ObjectNotFoundException
	 * 
	 */
	FormaGiuridica caricaFormaGiuridica(Integer idFormaGiuridica);

	/**
	 * Recupera la {@link List} di {@link FormaGiuridica}.
	 * 
	 * @param fieldSearch
	 *            .
	 * @param valueSearch
	 *            .
	 * @return List<FormaGiuridica>
	 */
	List<FormaGiuridica> caricaFormeGiuridiche(String fieldSearch, String valueSearch);

	/**
	 * Carica la lingua.
	 * 
	 * @param lingua
	 *            lingua da caricare
	 * @return Lingua
	 */
	Lingua caricaLingua(Lingua lingua);

	/**
	 * Carica la lista di Lingua.
	 * 
	 * @return List<Lingua>
	 */
	List<Lingua> caricaLingue();

	/**
	 * Carica la {@link List} di {@link Mansione}.
	 * 
	 * @param descrizione
	 *            .
	 * @return List<Mansione>
	 */
	List<Mansione> caricaMansioni(String descrizione);

	/**
	 * Carica tutte le note anagrafica.
	 * 
	 * @return note caricate
	 */
	List<NotaAnagrafica> caricaNoteAnagrafica();

	/**
	 * Carica tipi deposito.
	 * 
	 * @return List<TipoDeposito>
	 */
	List<TipoDeposito> caricaTipiDepositi();

	/**
	 * Carica {@link List} di {@link TipoSedeEntita}.
	 * 
	 * @param codice
	 *            codice da filtrare
	 * 
	 * @return List<TipoSedeEntita>
	 */
	List<TipoSedeEntita> caricaTipiSede(String codice);

	/**
	 * Carica la {@link List} di {@link TipoSedeEntita} con attributo sedePrincipale = false.
	 * 
	 * @return List<TipoSedeEntita>
	 */
	List<TipoSedeEntita> caricaTipiSedeSecondari();

	/**
	 * Carica tutte le unità di misura.
	 * 
	 * 
	 * @return <code>List</code> di <code>UnitaMisura</code> caricate
	 */
	List<UnitaMisura> caricaUnitaMisura();

	/**
	 * Carica le unità di misura con descrizione.
	 * 
	 * @param codice
	 *            codice dell'unità di misura (con carattere jolly);
	 * 
	 * @return <code>List</code> di <code>UnitaMisura</code> caricate
	 */
	List<UnitaMisura> caricaUnitaMisura(String codice);

	/**
	 * Carica una unità di misura.
	 * 
	 * @param unitaMisura
	 *            <code>UnitaMisura</code> da caricare
	 * @return <code>UnitaMisura</code> caricata
	 */
	UnitaMisura caricaUnitaMisura(UnitaMisura unitaMisura);

	/**
	 * Carica la lista di {@link ZonaGeografica}.
	 * 
	 * @param valueSearch
	 *            .
	 * @param fieldSearch
	 *            .
	 * 
	 * @return List<ZonaGeografica>
	 */
	List<ZonaGeografica> caricaZoneGeografiche(String fieldSearch, String valueSearch);

	/**
	 * Rimuove il contratto dal documento.
	 * 
	 * @param documento
	 *            il documento dal quale rimuovere il contratto
	 */
	void rimuovContrattoDaDocumento(Documento documento);

	/**
	 * Rende persistente l'istanza di <code>Carica</code> passata per parametro.
	 * 
	 * @param carica
	 *            carica da salvare
	 * @return carica salvata
	 */
	Carica salvaCarica(Carica carica);

	/**
	 * Salva il codice iva.
	 * 
	 * @param codiceIva
	 *            codice iva da salvare
	 * @return codice iva salvato
	 */
	CodiceIva salvaCodiceIva(CodiceIva codiceIva);

	/**
	 * Esegue il salvataggio di {@link ContrattoSpesometro}.
	 * 
	 * @param contratto
	 *            contrattoSpesometro da salvare
	 * @return contrattoSpesometro salvato
	 */
	ContrattoSpesometro salvaContratto(ContrattoSpesometro contratto);

	/**
	 * Salva conversioneUnitaMisura.
	 * 
	 * @param conversioneUnitaMisura
	 *            la conversioneUnitaMisura da salvare
	 * @return conversioneUnitaMisura salvata
	 */
	ConversioneUnitaMisura salvaConversioneUnitaMisura(ConversioneUnitaMisura conversioneUnitaMisura);

	/**
	 * Salva una forma giuridica.
	 * 
	 * @param formaGiuridica
	 *            forma giuridica da salvare
	 * @return forma giuridica salvata
	 */
	FormaGiuridica salvaFormaGiuridica(FormaGiuridica formaGiuridica);

	/**
	 * Salva lingua.
	 * 
	 * @param lingua
	 *            lingua da salvare
	 * @return Lingua salvata
	 */
	Lingua salvaLingua(Lingua lingua);

	/**
	 * salva {@link Mansione}.
	 * 
	 * @param mansione
	 *            mansione da salvare
	 * @return mansione salvata
	 */
	Mansione salvaMansione(Mansione mansione);

	/**
	 * Salva una {@link NotaAnagrafica}.
	 * 
	 * @param notaAnagrafica
	 *            nota da salvare
	 * @return nota salvata
	 */
	NotaAnagrafica salvaNotaAnagrafica(NotaAnagrafica notaAnagrafica);

	/**
	 * Salva tipo deposito.
	 * 
	 * @param tipoDeposito
	 *            tipo deposito da salvare
	 * @return Tipodeposito salvato
	 */
	TipoDeposito salvaTipoDeposito(TipoDeposito tipoDeposito);

	/**
	 * Salva {@link TipoSedeEntita}.
	 * 
	 * @param tipoSedeEntita
	 *            tipoSedeEntita da salvare
	 * @return tipoSedeEntita salvata
	 */
	TipoSedeEntita salvaTipoSedeEntita(TipoSedeEntita tipoSedeEntita);

	/**
	 * Salva una unità di misura.
	 * 
	 * @param unitaMisura
	 *            <code>UnitaMisura</code> da salvare
	 * @return <code>UnitaMisura</code> salvata
	 */
	UnitaMisura salvaUnitaMisura(UnitaMisura unitaMisura);

	/**
	 * Salva Zona Geografica.
	 * 
	 * @param zonaGeografica
	 *            zona Geografica da salvare
	 * @return zonaGeografica salvata
	 */
	ZonaGeografica salvaZonaGeografica(ZonaGeografica zonaGeografica);
}
