/**
 * SolutionDOC_HubSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.eurotn.panjea.fatturepa.solutiondoc.webservice;

public interface SolutionDOC_HubSoap extends java.rmi.Remote {

	/**
	 * <b>Check Servizio Hub</b><br>
	 * Verifica Autenticazione Cliente Servizio HUB.<br>
	 * Ritorna stringa '0' se attivo altrimenti una stringa errore 'Errore --> ...'.
	 */
	public java.lang.String checkClienteFatturaPA(java.lang.String codiceCliente, byte[] passwordServizi)
			throws java.rmi.RemoteException;

	/**
	 * <b>Check Servizio Hub</b><br>
	 * Verifica servizio HUB raggiungibile.<br>
	 * Ritorna stringa '0' se attivo.
	 */
	public java.lang.String contattoHub() throws java.rmi.RemoteException;

	/**
	 * <b>FatturaPA Servizio Hub</b><br>
	 * Servizio di Esito invio file FatturaPA precedentemente caricato a sistema (ZIP), al Sistema di Interscambio
	 * (Sdi).<br>
	 * Viene ritornata una lista con l'esito di invio per ogni FatturaPA o una lista di errori.<br>
	 * Specificare identificativo Sdi e il nome univoco della FatturaPA se si vuole l'esito di una specifica fattura,
	 * altrimenti per tutte le FatturaPA inserire il solo identificativo Sdi.
	 */
	public java.lang.Object[] esitoInvioSdiFatturaPA(java.lang.String codiceCliente, byte[] passwordServizi,
			java.lang.String identificativoSdi, java.lang.String nomeFileFatturaPA) throws java.rmi.RemoteException;

	/**
	 * <b>Riepilogo FatturaPA Servizio Hub</b><br>
	 * Servizio di Esito invio file FatturaPA precedentemente caricato a sistema (ZIP), al Sistema di Interscambio
	 * (Sdi).<br>
	 * Viene ritornata una lista con l'esito di invio per ogni FatturaPA o una lista di errori.<br>
	 * Specificare identificativo Sdi e il nome univoco della FatturaPA se si vuole l'esito di una specifica fattura,
	 * altrimenti per tutte le FatturaPA inserire il solo identificativo Sdi.
	 */
	public GetEsitiSdiFatturaPAResponseGetEsitiSdiFatturaPAResult getEsitiSdiFatturaPA(java.lang.String codiceCliente,
			byte[] passwordServizi, java.lang.String idTransazione, java.lang.String identificativoSdi,
			java.lang.String nomeFileFatturaPA) throws java.rmi.RemoteException;

	/**
	 * <b>Riepilogo FatturaPA Servizio Hub</b><br>
	 * Servizio di Download File Esito FatturaPA.<br>
	 * Ritorna un array con il nome file e il file Esito altrimenti una stringa di Errore.
	 */
	public java.lang.Object[] getFileEsitoFatturaPA(java.lang.String codiceCliente, byte[] passwordServizi,
			java.lang.String idEsito) throws java.rmi.RemoteException;

	/**
	 * <b>Riepilogo FatturaPA Servizio Hub</b><br>
	 * Servizio di Download File FatturaPA.<br>
	 * Ritorna un array con il nome file e il file FatturaPA altrimenti una stringa di Errore.
	 */
	public java.lang.Object[] getFileFatturaPA(java.lang.String codiceCliente, byte[] passwordServizi,
			java.lang.String idTransazione, boolean estrazioneP7M) throws java.rmi.RemoteException;

	/**
	 * <b>FatturaPA Servizio Hub</b><br>
	 * Creazione nome file secondo nomenclatura richiesta per la trasmissione del File FatturaPA.<br>
	 * Ritorna la stringa come in esempio: 'ITRSSFNC78A01G224J_00001' altrimenti una stringa errore 'Errore --> ...'.
	 */
	public java.lang.String getIdUnivocoFatturaPA(java.lang.String codiceCliente, byte[] passwordServizi,
			java.lang.String codicePaese, java.lang.String identificativoTrasmittente) throws java.rmi.RemoteException;

	/**
	 * <b>Riepilogo FatturaPA Servizio Hub</b><br>
	 * Servizio di riepilogo Log funzionalità FatturaPA.
	 */
	public GetLogFatturaPAResponseGetLogFatturaPAResult getLogFatturaPA(java.lang.String codiceCliente,
			byte[] passwordServizi, java.lang.String identificativoSdi, java.lang.String nomeFileZip,
			java.lang.String nomeServizio, java.lang.String messaggio, java.util.Calendar data_Da,
			java.util.Calendar data_A) throws java.rmi.RemoteException;

	/**
	 * <b>FatturaPA Servizio Hub</b><br>
	 * Creazione nome file univoco da assegnare al file ZIP, contenente le fatture XML, per la successiva fase di
	 * caricamento.<br>
	 * Ritorna la stringa come in esempio: '20140514_113521_3376d9ca-7d06-4145-a757-b5f8bd2156fa.zip' altrimenti una
	 * stringa errore 'Errore --> ...'.
	 */
	public java.lang.String getNomeFileZipFatturaPA(java.lang.String codiceCliente, byte[] passwordServizi)
			throws java.rmi.RemoteException;

	/**
	 * <b>Conservazione FatturaPA Servizio Hub</b><br>
	 * Servizio per la generazione del Pacchetto di Distribuzione.<br>
	 * La chiusura del processo di Conservazione avviene quando il sistema genera il Pacchetto di Archiviazione, nello
	 * specifico una volta al mese.<br>
	 * Per esibire a norma un documento è ncessario richiedere la generazione di un Pacchetto di Versamento.
	 */
	public byte[] getPacchettoDistribuzione(java.lang.String codiceCliente, byte[] passwordServizi,
			java.lang.String[] listaIdFattura, java.lang.String[] listaIdEsito, java.lang.String nomeUtente,
			java.lang.String cognomeUtente, java.lang.String codiceFiscalePaese, java.lang.String codiceFiscale,
			java.lang.String partitaIvaPaese, java.lang.String partitaIva) throws java.rmi.RemoteException;

	/**
	 * <b>Conservazione FatturaPA Servizio Hub</b><br>
	 * Servizio per la generazione del Rapporto di Versamento.<br>
	 * Documento informatico che attesta l'avvenuta presa in carico da parte del sistema di conservazione dei pacchetti
	 * di versamento inviati dal produttore.
	 */
	public byte[] getRapportoVersamento(java.lang.String codiceCliente, byte[] passwordServizi,
			java.lang.String[] listaIdRdV) throws java.rmi.RemoteException;

	/**
	 * <b>Conservazione FatturaPA Servizio Hub</b><br>
	 * Servizio di riepilogo lista Stato Processo di Conservazione relativo ad una Fattura e agli esiti ad essa
	 * associati.
	 */
	public GetStatoProcessoConservazioneResponseGetStatoProcessoConservazioneResult getStatoProcessoConservazione(
			java.lang.String codiceCliente, byte[] passwordServizi, java.lang.String identificativoSdi)
			throws java.rmi.RemoteException;

	/**
	 * <b>Riepilogo FatturaPA Servizio Hub</b><br>
	 * Servizio di riepilogo lista file FatturaPA inviate al Sistema di Interscambio (Sdi).<br>
	 * Se non vengono passati i parametri di filtro opzionali (identificativo Sdi, nome file ZIP, nome file FatturaPA,
	 * Data invio) la lista ritorna tutte le fatture inviate.
	 */
	public GetTransazioniSdiFatturaPAResponseGetTransazioniSdiFatturaPAResult getTransazioniSdiFatturaPA(
			java.lang.String codiceCliente, byte[] passwordServizi, java.lang.String identificativoSdi,
			java.lang.String nomeFileZip, java.lang.String nomeFileFatturaPA, java.util.Calendar data_Da,
			java.util.Calendar data_A) throws java.rmi.RemoteException;

	/**
	 * <b>Url Autenticazione Client SolutionDOC®</b><br>
	 * Creazione Url Query String per autenticazione Login Client SolutionDOC® es:
	 * https://legal.solutiondocondemand.com/
	 * SolutionDOC_client/login.aspx?auth=00CC5533BFB89201A6BE9D3F701ADAD048FA9D1529B3ECA6. <br>
	 * Il parametro UrlClient è opzionale se omesso viene ritornata la sola Query string es:
	 * ?auth=00CC5533BFB89201A6BE9D3F701ADAD048FA9D1529B3ECA6.<br>
	 * Ritorna stringa Autenticazione Login altrimenti una stringa errore 'Errore --> ...'.
	 */
	public java.lang.String getUrlLoginUser(java.lang.String codiceCliente, byte[] passwordServizi,
			java.lang.String urlClient, java.lang.String username, byte[] password) throws java.rmi.RemoteException;

	/**
	 * <b>FatturaPA Servizio Hub</b><br>
	 * Servizio di importazione per la successiva Conservazione delle Fatture ed Esiti inviati alla PA.<br>
	 * I files da importare devono essere contenuti in un file ZIP, ogni fattura può avere uno o più esiti associati,
	 * inoltre va riportato un file XML di riepilogo dei documenti da importare con i dati di invio quali idSdi e
	 * dataSdi.<br>
	 * Viene ritornato l'esito di importazione o una lista di errori.
	 */
	public java.lang.Object[] importaFatturaPA(java.lang.String codiceCliente, byte[] passwordServizi,
			java.lang.String nomeFileZip, java.lang.String nomeFileRiepilogo) throws java.rmi.RemoteException;

	/**
	 * <b>FatturaPA Servizio Hub</b><br>
	 * Servizio di invio file FatturaPA precedentemente caricato a sistema (ZIP), al Sistema di Interscambio (Sdi).<br>
	 * Vengono effettuati i controlli necessari alla validazione della firma e formattazione XML e inviati al Servizio
	 * di Interscambio.<br>
	 * (opzionale) I file FatturaPA possono essere rinominati in modo univoco secondo specifica prima dell'invio.<br>
	 * (opzionale) I file FatturaPA possono essere firmati con firma CAdES-BES (.p7m) prima dell'invio.<br>
	 * Viene ritornato l'esito di invio o una lista di errori.
	 */
	public java.lang.Object[] invioSdiFatturaPA(java.lang.String codiceCliente, byte[] passwordServizi,
			java.lang.String nomeFile, java.lang.String codicePaese, java.lang.String identificativoTrasmittente,
			boolean rinominaFile, boolean firmaTerzoIntermediario) throws java.rmi.RemoteException;

	/**
	 * <b>Conservazione FatturaPA Servizio Hub</b><br>
	 * Creazione di un nuovo Soggetto per la Firma Digitale Automatica.<br>
	 * Ritorna l'identificativo del Firmatario creato oppure una stringa di Errore.
	 */
	public java.lang.String setFirmatarioFatturaPA(java.lang.String username, byte[] password,
			java.lang.String urlFirma, java.lang.String usernameFirma, java.lang.String passwordFirma,
			java.lang.String nome, java.lang.String cognome, java.lang.String societa, java.lang.String note,
			java.lang.String email, java.lang.String emailPec, java.lang.String codiceCliente, boolean consenso,
			boolean revoca, boolean soggettoGiuridico, java.lang.String codiceFiscalePaese,
			java.lang.String codiceFiscaleCodice, java.lang.String partitaIvaPaese, java.lang.String partitaIvaCodice,
			java.lang.String anagraficaTitolo, java.lang.String anagraficaCodiceEORI) throws java.rmi.RemoteException;

	/**
	 * <b>Conservazione FatturaPA Servizio Hub</b><br>
	 * Creazione Pacchetti di Versamento per Conservazione FatturaPA
	 */
	public java.lang.Object[] setPacchettoDiVersamentoFatturaPA(java.lang.String username, byte[] password,
			boolean isFattura, java.lang.String urlServizio, java.lang.String usernameServizio,
			byte[] passwordServizio, java.lang.String intervalloRdV, java.lang.String intervalloPdA,
			int numPacchettiRdV, int numPacchettiPdA, boolean sendEmail, java.lang.String idCliente, int numMaxFiles)
			throws java.rmi.RemoteException;

	/**
	 * <b>FatturaPA Servizio Hub</b><br>
	 * Caricamento flusso dati file nel server per la successiva verifica e invio al servizio Sdi.<br>
	 * Il File da inviare allo Sdi deve avere una dimensione massima di di 5 Megabyte.<br>
	 * I file XML FatturaPA contenuti nello ZIP possono essere non firmati , nel caso in cui si richieda la loro firma
	 * successivamente, o file firmati del tipo CAdES-BES .xml.p7m.
	 */
	public java.lang.String uploadFileFatturaPA(java.lang.String codiceCliente, byte[] passwordServizi,
			java.lang.String nomeFile, byte[] buffer, long offset) throws java.rmi.RemoteException;
}
