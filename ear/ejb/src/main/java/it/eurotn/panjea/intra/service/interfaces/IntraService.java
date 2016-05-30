package it.eurotn.panjea.intra.service.interfaces;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.service.exception.PreferenceNotFoundException;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.intra.domain.AreaIntra;
import it.eurotn.panjea.intra.domain.DichiarazioneIntra;
import it.eurotn.panjea.intra.domain.DichiarazioneIntra.TipoDichiarazione;
import it.eurotn.panjea.intra.domain.GruppoCondizioneConsegna;
import it.eurotn.panjea.intra.domain.IntraSettings;
import it.eurotn.panjea.intra.domain.NaturaTransazione;
import it.eurotn.panjea.intra.domain.RigaSezioneIntra;
import it.eurotn.panjea.intra.domain.Servizio;
import it.eurotn.panjea.intra.domain.TotaliDichiarazione;
import it.eurotn.panjea.intra.domain.dichiarazione.FileDichiarazione;
import it.eurotn.panjea.intra.manager.ParametriRicercaAreaIntra;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface IntraService {

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
	String associaNomenclatura(byte[] file);

	/**
	 * calcola i totali per la dichiarazione Intra.
	 * 
	 * @param id
	 *            id dichiarazione
	 * @return totali della dichiarazione
	 */
	TotaliDichiarazione calcolaTotaliDichiarazione(Integer id);

	/**
	 * Cancella l'area intra.
	 * 
	 * @param areaIntra
	 *            l'area da cancellare
	 */
	void cancellaAreaIntra(AreaIntra areaIntra);

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
	 *            id fileDic da cancellare
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
	List<Documento> caricaDocumentiSenzaIntra(ParametriRicercaAreaIntra parametri);

	/**
	 * 
	 * @return file delle dichiarazioni generati
	 */
	List<FileDichiarazione> caricaFileDichiarazioni();

	/**
	 * Carica la lista di {@link GruppoCondizioneConsegna}.
	 * 
	 * @return List<GruppoCondizioneConsegna>
	 */
	List<GruppoCondizioneConsegna> caricaGruppiCondizioneConsegna();

	/**
	 * Carica il settings della gestione intra.<br/>
	 * Se non esiste ne crea uno, lo salva e lo restituisce.
	 * 
	 * @return <code>IntraSettings</code> caricato
	 */
	IntraSettings caricaIntraSettings();

	/**
	 * Carica la lista di {@link NaturaTransazione}.
	 * 
	 * @return List<NaturaTransazione>
	 */
	List<NaturaTransazione> caricaNatureTransazione();

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
	 * @param fieldSearch
	 *            campo filtro
	 * @param valueSearch
	 *            filtro
	 * 
	 * @return le nomenclature richieste o tutte se class null
	 */
	List<?> caricaServizi(Class<?> classServizio, String fieldSearch, String valueSearch);

	/**
	 * Carica il servizio.
	 * 
	 * @param servizio
	 *            servizio da caricare.
	 * @return servizio caricato
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
	DichiarazioneIntra compilaDichiarazioneIntra(DichiarazioneIntra dichiarazioneIntra);

	/**
	 * Crea una dichiarazione intra in base all'ultima dichiarazione creata.
	 * 
	 * @param tipodDichiarazione
	 *            tiplogia della dichiarazione da creare.
	 * 
	 * @return nuova dichiarazione con i dati inizializzati in base all'ultima dichiarazione
	 */
	DichiarazioneIntra creaDichiarazioneIntra(TipoDichiarazione tipodDichiarazione);

	/**
	 * genera le aree intra per i documenti.<br/>
	 * <b>NB</b>Considera solamente i documenti con areamagazzino
	 * 
	 * @param documenti
	 *            documenti per i quali generare le aree intra
	 */
	void generaAreeIntra(List<Integer> documenti);

	/**
	 * 
	 * @param dichiarazioni
	 *            lista di dichiarazioni da includere nel file
	 * @param salvaRisultati
	 *            true salva i risultati,false genera solamente il file.
	 * @return file scambi.cee
	 * @throws PreferenceNotFoundException
	 *             rilanciata se non trova la preferenza dirTemplate
	 */
	FileDichiarazione generaFileEsportazione(List<Integer> dichiarazioni, boolean salvaRisultati)
			throws PreferenceNotFoundException;

	/**
	 * Importa il file di nomenclatura esportato con l'intraWeb.
	 * 
	 * @param file
	 *            file esportato da intraweb
	 */
	void importaNomenclatura(byte[] file);

	/**
	 * Importa il file di servizi esportato con l'intraWeb.
	 * 
	 * @param file
	 *            file esportato da intraweb
	 */
	void importaServizi(byte[] file);

	/**
	 * 
	 * @param parametri
	 *            parametri di ricerca
	 * @return areeIntra trovate
	 */
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
	 * @return servizio salvato
	 */
	Servizio salvaServizio(Servizio servizio);

	/**
	 * spedisce il file della dichiarazione precedentemente generato.
	 * 
	 * @param id
	 *            id fileDichiarazione da spedire
	 */
	void spedisciFileEsportazione(int id);

	/**
	 * Genera e spedisce il file della dichiarazione.
	 * 
	 * @param dichiarazioni
	 *            dichiarazioni da spedire
	 * @throws PreferenceNotFoundException
	 *             rilanciata se non trova la preferenza dirTemplate
	 */
	void spedisciFileEsportazione(List<Integer> dichiarazioni) throws PreferenceNotFoundException;
}
