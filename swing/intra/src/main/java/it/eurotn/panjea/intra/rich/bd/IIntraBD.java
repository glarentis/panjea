/**
 *
 */
package it.eurotn.panjea.intra.rich.bd;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.intra.domain.AreaIntra;
import it.eurotn.panjea.intra.domain.DichiarazioneIntra;
import it.eurotn.panjea.intra.domain.DichiarazioneIntra.TipoDichiarazione;
import it.eurotn.panjea.intra.domain.IntraSettings;
import it.eurotn.panjea.intra.domain.RigaSezioneIntra;
import it.eurotn.panjea.intra.domain.Servizio;
import it.eurotn.panjea.intra.domain.TotaliDichiarazione;
import it.eurotn.panjea.intra.domain.dichiarazione.FileDichiarazione;
import it.eurotn.panjea.intra.manager.ParametriRicercaAreaIntra;
import it.eurotn.panjea.rich.bd.AsyncMethodInvocation;

import java.util.List;

/**
 * @author leonardo
 */
public interface IIntraBD {

	/**
	 * 
	 * @param idFileDichiarazione
	 *            .
	 * @param nome
	 *            .
	 * @return .
	 */
	FileDichiarazione aggiornaNomeFileDichiarazione(int idFileDichiarazione, String nome);

	/**
	 * associa la nomenclaura all'articolo. il file deve essere csv con codice articolo,codice
	 * nomenclatura,peso,nazione,ModalitaErogazione
	 * 
	 * @param file
	 *            .
	 * @return log
	 */
	@AsyncMethodInvocation
	String associaNomenclatura(byte[] file);

	/**
	 * calcola i totali per la dichiarazione Intra.
	 * 
	 * @param dichiarazioneIntra
	 *            dichiarazione
	 * @return totali dichiarazione
	 */
	@AsyncMethodInvocation
	TotaliDichiarazione calcolaTotaliDichiarazione(DichiarazioneIntra dichiarazioneIntra);

	/**
	 * Cancella una dichiarazione intra.
	 * 
	 * @param dichiarazioneIntra
	 *            dichiarazione da cancellare.
	 */
	void cancellaDichiarazioneIntra(DichiarazioneIntra dichiarazioneIntra);

	/**
	 * 
	 * @param id
	 *            id del {@link FileDichiarazione} da cancellare
	 */
	void cancellaFileDichiarazioni(int id);

	/**
	 * 
	 * @param rigaSezioneIntra
	 *            riga da cancellare
	 */
	void cancellaRigaSezioneDichiarazione(RigaSezioneIntra rigaSezioneIntra);

	/**
	 * Cancella il servizio.
	 * 
	 * @param servizio
	 *            servizio da cancellare
	 */
	void cancellaServizio(Servizio servizio);

	/**
	 * Carica l'area intra legata al documento.
	 * 
	 * @param documento
	 *            documento interessato
	 * @return areaIntra per il documento
	 */
	AreaIntra caricaAreaIntraByDocumento(Documento documento);

	/**
	 * 
	 * @param id
	 *            id della dichiarazione
	 * @return dichiarazione caricata
	 */
	DichiarazioneIntra caricaDichiarazioneIntra(int id);

	/**
	 * 
	 * @return lista di dichiarazioniIntra
	 */
	List<DichiarazioneIntra> caricaDichiarazioniIntra();

	/**
	 * 
	 * @return lista di dichiarazioniIntra da presentare
	 */
	List<DichiarazioneIntra> caricaDichiarazioniIntraDaPresentare();

	/**
	 * @param parametri
	 *            parametri di ricerca
	 * @return lista di documenti senza aree intra.
	 */
	@AsyncMethodInvocation
	List<Documento> caricaDocumentiSenzaIntra(ParametriRicercaAreaIntra parametri);

	/**
	 * 
	 * @param id
	 * @return file delle dichiarazioni generati
	 */
	List<FileDichiarazione> caricaFileDichiarazioni();

	/**
	 * Carica il settings della gestione intra.<br/>
	 * Se non esiste ne crea uno, lo salva e lo restituisce.
	 * 
	 * @return <code>IntraSettings</code> caricato
	 */
	IntraSettings caricaIntraSettings();

	/**
	 * Carica le righe della dichiarazione intra per la sezione.
	 * 
	 * @param <T>
	 * 
	 * @param dichiarazioneIntra
	 *            dichiarazione
	 * @param classeSezione
	 *            sezione della dichiarazione
	 * @return righe della sezione per la dichiarazione
	 * @param <T>
	 *            tipo
	 */
	<T extends RigaSezioneIntra> List<T> caricaRigheSezioniDichiarazione(DichiarazioneIntra dichiarazioneIntra,
			Class<T> classeSezione);

	/**
	 * Carica i servizi/nomenclature richiesti.
	 * 
	 * @param classServizio
	 *            classe richiesta
	 * @param valueSearch
	 *            filtro
	 * @param fieldSearch
	 *            campo filtro
	 * 
	 * @return le nomenclature richieste o tutte se class null
	 */
	List<?> caricaServizi(Class<?> classServizio, String fieldSearch, String valueSearch);

	/**
	 * Carica un servizio ricercandolo per ID.
	 * 
	 * @param servizio
	 *            servizio da caricare.
	 * @return servizio ricercato
	 */
	Servizio caricaServizio(Servizio servizio);

	/**
	 * Da una dichiarazione intra genera tutti i dati (righe e riepiloghi). Se la dichiarazione utilizzata come
	 * parametro ha delle righe queste verranno cancellate
	 * 
	 * @param dichiarazioneIntra
	 *            testata della dichiarazione da compilare
	 * @return dichiarazione con tutti i dati generati (frontespizio e righe).
	 */
	@AsyncMethodInvocation
	DichiarazioneIntra compilaDichiarazioneIntra(DichiarazioneIntra dichiarazioneIntra);

	/**
	 * Crea una dichiarazione intra in base all'ultima dichiarazione creata.
	 * 
	 * @param tipoDichiarazione
	 *            tipologia della dichiarazione
	 * @return nuova dichiarazione con i dati inizializzati in base all'ultima dichiarazione
	 */
	DichiarazioneIntra creaDichiarazioneIntra(TipoDichiarazione tipoDichiarazione);

	/**
	 * genera le aree intra per i documenti.<br/>
	 * <b>NB</b>Considera solamente i documenti con areamagazzino
	 * 
	 * @param documenti
	 *            documenti per i quali generare le aree intra
	 */
	@AsyncMethodInvocation
	void generaAreeIntra(List<Integer> documenti);

	/**
	 * 
	 * @param dichiarazioni
	 *            lista di dichiarazioni da includere nel file
	 * @param salvaRisultati
	 *            true salva i risultati,false genera solamente il file.
	 * @return file scambi.cee
	 */
	@AsyncMethodInvocation
	FileDichiarazione generaFileEsportazione(List<Integer> dichiarazioni, boolean salvaRisultati);

	/**
	 * Importa il file di nomenclatura esportato con l'intraWeb.
	 * 
	 * @param file
	 *            file esportato da intraweb
	 */
	@AsyncMethodInvocation
	void importaNomenclatura(byte[] file);

	/**
	 * Importa il file di servizi esportato con l'intraWeb.
	 * 
	 * @param file
	 *            file esportato da intraweb
	 */
	@AsyncMethodInvocation
	void importaServizi(byte[] file);

	/**
	 * 
	 * @param parametri
	 *            parametri di ricerca
	 * @return areeIntra trovate
	 */
	@AsyncMethodInvocation
	List<AreaContabile> ricercaAreeContabiliConIntra(ParametriRicercaAreaIntra parametri);

	/**
	 * Salva l'area intra.
	 * 
	 * @param areaIntra
	 *            l'area da salvare
	 * @return l'area intra salvata
	 */
	AreaIntra salvaAreaIntra(AreaIntra areaIntra);

	/**
	 * Salva una dichiarazione intra.
	 * 
	 * @param dichiarazioneIntra
	 *            dichiarazione da salvare
	 * @return dichiarazione salvata.
	 */
	DichiarazioneIntra salvaDichiarazioneIntra(DichiarazioneIntra dichiarazioneIntra);

	/**
	 * Salva un {@link IntraSettings}.
	 * 
	 * @param intraSettingsToSave
	 *            il settings dell'intra da salvare
	 * @return <code>IntraSettings</code> salvato
	 */
	IntraSettings salvaIntraSettings(IntraSettings intraSettingsToSave);

	/**
	 * @param riga
	 *            riga da salvare
	 * @return riga salvata.
	 */
	RigaSezioneIntra salvaRigaSezioneDichiarazione(RigaSezioneIntra riga);

	/**
	 * Salva il servizio.
	 * 
	 * @param servizio
	 *            servizio da salvare
	 * @return servizio salvata
	 */
	Servizio salvaServizio(Servizio servizio);

	/**
	 * spedisce il file della dichiarazione precedentemente generato.
	 * 
	 * @param fileDichiarazione
	 *            fileDichiarazione da spedire
	 */
	@AsyncMethodInvocation
	void spedisciFileEsportazione(FileDichiarazione fileDichiarazione);

	/**
	 * Genera e spedisce il file della dichiarazione.
	 * 
	 * @param dichiarazioni
	 *            dichiarazioni da spedire
	 */
	@AsyncMethodInvocation
	void spedisciFileEsportazione(List<Integer> dichiarazioni);
}
