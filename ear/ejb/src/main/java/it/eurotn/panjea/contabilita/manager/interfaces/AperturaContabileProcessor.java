package it.eurotn.panjea.contabilita.manager.interfaces;

import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.TipoDocumentoBase;
import it.eurotn.panjea.contabilita.service.exception.AperturaEsistenteException;
import it.eurotn.panjea.contabilita.service.exception.ChiusuraAssenteException;
import it.eurotn.panjea.contabilita.service.exception.ContiBaseException;

import java.util.Date;

import javax.ejb.Local;

@Local
public interface AperturaContabileProcessor {

	/**
	 * Carica il documento di chiusura dell'anno precedente. Se non esiste ritorna NULL.
	 * 
	 * @param annoEsercizio
	 * @return documento di chisura dell'anno precedente. NULL se non ho documenti di chiusura
	 * @throws ChiusuraAssenteException
	 */
	AreaContabile caricaDocumentoChisuraPrecedentePresente(Integer annoEsercizio) throws ChiusuraAssenteException;

	void eseguiApertura(Integer annoEsercizio, Date dataDocumentoChiusura) throws AperturaEsistenteException,
			TipoDocumentoBaseException, ContiBaseException, ChiusuraAssenteException;

	/**
	 * Verifica se ci sono dei documenti di apertura presenti nell'anno di esercizio richiesto.
	 * 
	 * @throws DocumentoChiusuraException
	 */
	void verificaDocumentiAperturaPresenti(Integer annoEsercizio) throws AperturaEsistenteException;

	/**
	 * Verifica l'esistenza dei tipi conto per la chiusura.
	 * 
	 * @throws TipiDocumentoBaseException
	 */
	void verificaTipiContiApertura() throws ContiBaseException;

	/**
	 * Verifica l'esistenza del {@link TipoDocumentoBase} per la chiusura contabile.<br/>
	 * .
	 * 
	 * @throws TipiDocumentoBaseException
	 */
	void verificaTipoDocumentoBaseApertura() throws TipoDocumentoBaseException;

}
