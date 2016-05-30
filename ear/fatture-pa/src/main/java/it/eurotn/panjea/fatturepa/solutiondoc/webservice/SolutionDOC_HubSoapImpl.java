/**
 * SolutionDOC_HubSoapImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.eurotn.panjea.fatturepa.solutiondoc.webservice;

public class SolutionDOC_HubSoapImpl implements SolutionDOC_HubSoap {
	public java.lang.String checkClienteFatturaPA(java.lang.String codiceCliente, byte[] passwordServizi)
			throws java.rmi.RemoteException {
		return null;
	}

	public java.lang.String contattoHub() throws java.rmi.RemoteException {
		return null;
	}

	public java.lang.Object[] esitoInvioSdiFatturaPA(java.lang.String codiceCliente, byte[] passwordServizi,
			java.lang.String identificativoSdi, java.lang.String nomeFileFatturaPA) throws java.rmi.RemoteException {
		return null;
	}

	public GetEsitiSdiFatturaPAResponseGetEsitiSdiFatturaPAResult getEsitiSdiFatturaPA(java.lang.String codiceCliente,
			byte[] passwordServizi, java.lang.String idTransazione, java.lang.String identificativoSdi,
			java.lang.String nomeFileFatturaPA) throws java.rmi.RemoteException {
		return null;
	}

	public java.lang.Object[] getFileEsitoFatturaPA(java.lang.String codiceCliente, byte[] passwordServizi,
			java.lang.String idEsito) throws java.rmi.RemoteException {
		return null;
	}

	public java.lang.Object[] getFileFatturaPA(java.lang.String codiceCliente, byte[] passwordServizi,
			java.lang.String idTransazione, boolean estrazioneP7M) throws java.rmi.RemoteException {
		return null;
	}

	public java.lang.String getIdUnivocoFatturaPA(java.lang.String codiceCliente, byte[] passwordServizi,
			java.lang.String codicePaese, java.lang.String identificativoTrasmittente) throws java.rmi.RemoteException {
		return null;
	}

	public GetLogFatturaPAResponseGetLogFatturaPAResult getLogFatturaPA(java.lang.String codiceCliente,
			byte[] passwordServizi, java.lang.String identificativoSdi, java.lang.String nomeFileZip,
			java.lang.String nomeServizio, java.lang.String messaggio, java.util.Calendar data_Da,
			java.util.Calendar data_A) throws java.rmi.RemoteException {
		return null;
	}

	public java.lang.String getNomeFileZipFatturaPA(java.lang.String codiceCliente, byte[] passwordServizi)
			throws java.rmi.RemoteException {
		return null;
	}

	public byte[] getPacchettoDistribuzione(java.lang.String codiceCliente, byte[] passwordServizi,
			java.lang.String[] listaIdFattura, java.lang.String[] listaIdEsito, java.lang.String nomeUtente,
			java.lang.String cognomeUtente, java.lang.String codiceFiscalePaese, java.lang.String codiceFiscale,
			java.lang.String partitaIvaPaese, java.lang.String partitaIva) throws java.rmi.RemoteException {
		return null;
	}

	public byte[] getRapportoVersamento(java.lang.String codiceCliente, byte[] passwordServizi,
			java.lang.String[] listaIdRdV) throws java.rmi.RemoteException {
		return null;
	}

	public GetStatoProcessoConservazioneResponseGetStatoProcessoConservazioneResult getStatoProcessoConservazione(
			java.lang.String codiceCliente, byte[] passwordServizi, java.lang.String identificativoSdi)
			throws java.rmi.RemoteException {
		return null;
	}

	public GetTransazioniSdiFatturaPAResponseGetTransazioniSdiFatturaPAResult getTransazioniSdiFatturaPA(
			java.lang.String codiceCliente, byte[] passwordServizi, java.lang.String identificativoSdi,
			java.lang.String nomeFileZip, java.lang.String nomeFileFatturaPA, java.util.Calendar data_Da,
			java.util.Calendar data_A) throws java.rmi.RemoteException {
		return null;
	}

	public java.lang.String getUrlLoginUser(java.lang.String codiceCliente, byte[] passwordServizi,
			java.lang.String urlClient, java.lang.String username, byte[] password) throws java.rmi.RemoteException {
		return null;
	}

	public java.lang.Object[] importaFatturaPA(java.lang.String codiceCliente, byte[] passwordServizi,
			java.lang.String nomeFileZip, java.lang.String nomeFileRiepilogo) throws java.rmi.RemoteException {
		return null;
	}

	public java.lang.Object[] invioSdiFatturaPA(java.lang.String codiceCliente, byte[] passwordServizi,
			java.lang.String nomeFile, java.lang.String codicePaese, java.lang.String identificativoTrasmittente,
			boolean rinominaFile, boolean firmaTerzoIntermediario) throws java.rmi.RemoteException {
		return null;
	}

	public java.lang.String setFirmatarioFatturaPA(java.lang.String username, byte[] password,
			java.lang.String urlFirma, java.lang.String usernameFirma, java.lang.String passwordFirma,
			java.lang.String nome, java.lang.String cognome, java.lang.String societa, java.lang.String note,
			java.lang.String email, java.lang.String emailPec, java.lang.String codiceCliente, boolean consenso,
			boolean revoca, boolean soggettoGiuridico, java.lang.String codiceFiscalePaese,
			java.lang.String codiceFiscaleCodice, java.lang.String partitaIvaPaese, java.lang.String partitaIvaCodice,
			java.lang.String anagraficaTitolo, java.lang.String anagraficaCodiceEORI) throws java.rmi.RemoteException {
		return null;
	}

	public java.lang.Object[] setPacchettoDiVersamentoFatturaPA(java.lang.String username, byte[] password,
			boolean isFattura, java.lang.String urlServizio, java.lang.String usernameServizio,
			byte[] passwordServizio, java.lang.String intervalloRdV, java.lang.String intervalloPdA,
			int numPacchettiRdV, int numPacchettiPdA, boolean sendEmail, java.lang.String idCliente, int numMaxFiles)
			throws java.rmi.RemoteException {
		return null;
	}

	public java.lang.String uploadFileFatturaPA(java.lang.String codiceCliente, byte[] passwordServizi,
			java.lang.String nomeFile, byte[] buffer, long offset) throws java.rmi.RemoteException {
		return null;
	}

}
