package it.eurotn.panjea.fatturepa.solutiondoc.webservice.manager;

import it.eurotn.panjea.fatturepa.domain.StatoFatturaPA;

import java.util.Map;

import javax.ejb.Local;

@Local
public interface SolutionDocFatturaPAManager {

	/**
	 * Consente di verificare i parametri di autenticazione al servizio.
	 *
	 * @return valore ‘0’, altrimenti stringa errore 'Errore --> ...'
	 */
	String checkClienteFatturaPA();

	/**
	 * Consente di verificare la connessione al servizio. Ritorna una stringa con il valore ‘0’.
	 *
	 * @return valore ‘0’ , altrimenti stringa errore 'Errore --> ...'
	 */
	String contattoHub();

	/**
	 * Servizio di riepilogo file Esiti delle FatturaPA inviate al Sistema di Interscambio (Sdi).
	 *
	 * @param identificativoSdI
	 *            Identificativo ritornato dal servizio Sdi in fase di Invio della fattura
	 * @param nomeXMLFatturaPA
	 *            Nome file FatturaPA, nel formato IT01234567890_11111.xml.p7m. (Opzionale)
	 * @return mappa contenente come chiave lo stato della notifica e come valore l'id assegnato dal servizio
	 */
	Map<StatoFatturaPA, String> getEsitiSdIFatturaPA(String identificativoSdI, String nomeXMLFatturaPA);

	/**
	 * Servizio di Download Esito messaggio delle FatturaPA inviate al Sistema di Interscambio (Sdi). Ritorna un array
	 * con il nome file e il file Esito altrimenti una stringa di Errore.
	 *
	 * @param idEsito
	 *            Identificativo Esito
	 * @return [Array di Stringhe] lista dati esiti o lista errori<br>
	 *         (Posizione 1) Nome File, se in errore la stringa ‘Errore --> ...descrizione errore..’ (Posizione 2)
	 *         Flusso byte File Esito (per ricostruire il file esito in locale)
	 */
	byte[] getFileEsitoFatturaPA(String idEsito);

	/**
	 * Consente la creazione nome file univoco da assegnare al file ZIP, contenente la fattura XML, per la successiva
	 * fase di caricamento.
	 *
	 * @return Nome File .zip, altrimenti stringa errore 'Errore --> ...'
	 */
	String getNomeFileZipFatturaPA();

	/**
	 * Il servizio permette l’importazione di una lista di Fatture ed Esiti precedentemente inviate allo Sdi tramite un
	 * canale esterno (esempio invio tramite PEC). Le Fatture ed Esiti importati verranno successivamente processati per
	 * la conservazione sostitutiva.
	 *
	 * @param nomeZip
	 *            Nome file ZIP precedentemente caricato a sistema.
	 * @param nomeXmlDefinizioneFiles
	 *            Nome file XML di riepilogo dei documenti da importare, contenuto nello zip.
	 * @return [Array di Stringhe] lista dei documenti importati: Nome File, se in errore la stringa ‘Errore -->
	 *         ...descrizione errore in fase di importazione..’
	 */
	Object[] importaFatturaPA(String nomeZip, String nomeXmlDefinizioneFiles);

	/**
	 * Servizio di invio file, precedentemente caricato a sistema (ZIP), al Sistema di Interscambio (Sdi).<br>
	 * La seguente chiamata provvede a verificare la validità del file caricato precedentemente nell’HUB e nel caso di
	 * verifica positiva procede con l’inoltro al servizio di interscambio Sdi, il quale una volta ricevuto il file .zip
	 * con la fattura darà un messaggio di avvenuta ricezione o rifiuto.<br>
	 * A questo stadio di trasmissione non vi è ancora l’esito del destinatario della FatturaPA che verrà trasmesso solo
	 * su ccessivamente tramite gli appositi end point.<br>
	 * Alla richiesta del servizio vi è l’opzione da parte del Client di richiedere la rinomina del file e l’opzione di
	 * firma del file del tipo CAdES-BES (p7m).<br>
	 * Viene ritornato l'esito di invio (identificativo invio e data ricezione) o una lista di errori.<br>
	 * Nota: se l’opzione Firma è false, il file FatturaPA deve avere estensione p7m (firmati), altrimenti deve avere
	 * estensione xml.
	 *
	 * @param nomeZip
	 *            Nome File Zip Caricato
	 * @param codicePaese
	 *            Codice Paese standard ISO 3166-1 alpha-2 di 2 caratteri. Esempio: IT(Opzionale)
	 * @param identificativo
	 *            Identificativo Trasmittente
	 * @param rinomina
	 *            Specifica se i file FatturaPA devono essere rinominati secondo lo standard
	 * @param firmaIntermediario
	 *            Specifica se i file FatturaPA devono essere firmati con terzo intermediario con firma del tipo con
	 *            firma CAdES-BES (p7m).
	 * @return [Array di Stringhe] lista dati invio a Sdi o lista errori<br>
	 *         (posizione 1) Identificativo Invio SdI, se in errore la stringa ‘Errore --> ...descrizione errore..’<br>
	 *         (posizione 2) Data Ricezione
	 */
	Object[] invioSdiFatturaPA(String nomeZip, String codicePaese, String identificativo, boolean rinomina,
			boolean firmaIntermediario);

	/**
	 * Consente il caricamento del flusso dati file nel server per la successiva verifica e invio al servizio Sdi.<br>
	 * Il File da inviare allo Sdi deve avere una dimensione massima di 5 Megabyte.<br>
	 * Il file XML FatturaPA contenuto nello ZIP può essere non firmato, nel caso in cui si richieda la sua firma
	 * successivamente, o file firmato del tipo CAdES-BES con estensione .xml.p7m.<br>
	 * <br>
	 * Il file zip deve contenere i file FatturaPA e relativi Esiti ed un file XML di riepilogo degli stessi, nel quale
	 * vengono riportati i dati necessari per l’importazione delle Fatture quali idSdi e dataSdi. Il file XML deve avere
	 * la seguente formattazione:<br>
	 *
	 * <pre>
	 * {@code
	 * <?xml version="1.0" standalone="yes"?>
	 * <ImportaFatturaPA>
	 * 		<Documento>
	 * 			<Tipo>Fattura</Tipo>
	 * 			<NomeFile>IT04025310485_00001.XML.p7m</NomeFile>
	 * 			<IdSdi>1605855</IdSdi>
	 * 			<DataSdi>01/01/2015 07:00:00</DataSdi>
	 * 		</Documento>
	 * 		<Documento>
	 * 			<Tipo>Esito</Tipo>
	 * 			<NomeFile>IT04025310485_00001_NE_001.xml</NomeFile>
	 * 			<IdSdi>1605855</IdSdi>
	 * 			<DataSdi />
	 * 		</Documento>
	 * </ImportaFatturaPA>
	 * }
	 * </pre>
	 *
	 * Ogni nodo <Documento> specifica il file da importare e i dati relativi ad esso.
	 *
	 * @param nomeFile
	 *            Nome File da caricare
	 * @param dataFile
	 *            Flusso byte file da caricare
	 * @return Nome File caricato, altrimenti stringa errore 'Errore --> ...'
	 */
	String uploadFileFatturaPA(String nomeFile, byte[] dataFile);
}
