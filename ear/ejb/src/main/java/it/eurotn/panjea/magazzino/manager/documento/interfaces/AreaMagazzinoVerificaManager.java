package it.eurotn.panjea.magazzino.manager.documento.interfaces;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.magazzino.service.exception.DocumentiEsistentiPerAreaMagazzinoException;
import it.eurotn.panjea.magazzino.service.exception.DocumentoAssenteAvvisaException;
import it.eurotn.panjea.magazzino.service.exception.DocumentoAssenteBloccaException;
import it.eurotn.panjea.magazzino.service.exception.DocumentoAssenteNonAvvisareException;
import it.eurotn.panjea.rate.domain.AreaRate;

import java.util.List;

import javax.ejb.Local;

/**
 * 
 * Manager incaricato di eseguire i controlli di verifica legati all' AreaMagazzino.
 * 
 * @author adriano
 * @version 1.0, 01/set/2008
 * 
 */
@Local
public interface AreaMagazzinoVerificaManager {

	/**
	 * Verifica se l'area magazzino deva cambiare stato.
	 * 
	 * @param areaMagazzino
	 *            areaMagazzino
	 * @param areaRate
	 *            areaRate
	 * @return <code>true</code> se deve cambiare lo stato, <code>false</code> altrimenti
	 */
	boolean checkCambioStato(AreaMagazzino areaMagazzino, AreaRate areaRate);

	/**
	 * Verifica se deva invalidare l'area magazzino.
	 * 
	 * @param areaMagazzino
	 *            areaMagazzino
	 * @return <code>true</code> se deve invalidare l'area, <code>false</code> no
	 */
	AreaMagazzino checkInvalidaAreaMagazzino(AreaMagazzino areaMagazzino);

	/**
	 * Ricerca di tutti gli oggetti {@link Documento} che rispondono agli attributi contenuti nell'argomento documento <br>
	 * che non hanno associata un {@link AreaMagazzino} <br>
	 * Gli attributi presi in considerazione solo quelli chiave di dominio della classe {@link Documento} <br>
	 * il parametro soloAttributiNotNull determina se impostare la condizione where solo sugli attributi valorizzati o
	 * meno. <br>
	 * 
	 * @param documento
	 *            documento
	 * @param soloAttributiNotNull
	 *            soloAttributiNotNull
	 * @return lista dei documenti caricati
	 */
	List<Documento> ricercaDocumentiSenzaAreaMagazzino(Documento documento, boolean soloAttributiNotNull);

	/**
	 * metodo incaricato di verificare l'associazione con la classe di dominio {@link Documento}. <br>
	 * viene eseguita la ricerca dei documenti esistenti attraverso gli attributi chiave di dominio di {@link Documento}
	 * e viene <br>
	 * poi valutata il tipo di azione da compiere presente in {@link TipoAreaMagazzino} nel caso il documento non
	 * esista.<br>
	 * 
	 * @param areaMagazzino
	 *            areaMagazzino
	 * @throws DocumentoAssenteNonAvvisareException
	 *             eccezione per la gestione del documento mancante.
	 * @throws DocumentoAssenteAvvisaException
	 *             eccezione per la gestione del documento mancante.
	 * @throws DocumentoAssenteBloccaException
	 *             eccezione per la gestione del documento mancante.
	 * @throws DocumentiEsistentiPerAreaMagazzinoException
	 *             lanciata quando ci sono già delle aree magazzino collegate al documento.
	 */
	void verificaAssociazioneConDocumento(AreaMagazzino areaMagazzino) throws DocumentoAssenteNonAvvisareException,
			DocumentoAssenteAvvisaException, DocumentoAssenteBloccaException,
			DocumentiEsistentiPerAreaMagazzinoException;

}
