package it.eurotn.panjea.magazzino.manager.documento.fatturazione.interfaces;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.magazzino.domain.SedeMagazzinoLite;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.magazzino.exception.RigaArticoloNonValidaException;
import it.eurotn.panjea.magazzino.manager.documento.fatturazione.AreaMagazzinoFatturazione;
import it.eurotn.panjea.magazzino.service.exception.SedePerRifatturazioneAssenteException;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

@Local
public interface DocumentoFatturazioneGenerator {

	/**
	 * Genera il documento di fatturazione.
	 *
	 * @param listAree
	 *            aree da fatturare
	 * @param tipoAreaMagazzinoDestinazione
	 *            tipo area magazzino del documento di fatturazione
	 * @param dataDocumentoDestinazione
	 *            data del documento di fatturazione
	 * @param sedeEntitaDestinazione
	 *            sede entità del documento di fatturazione
	 * @param entitaDestinazione
	 *            entità del documento di fatturazione
	 * @param numeroDocumento
	 *            numero del documento di fatturazione
	 * @param noteFatturazione
	 *            note del documento di fatturazione
	 * @param uuidContabilizzazione
	 *            uuid di contabilizzazione
	 * @param utente
	 *            utente di generazione
	 * @throws RigaArticoloNonValidaException
	 *             exception
	 */
	void generaDocumentoFatturazione(List<AreaMagazzinoFatturazione> listAree,
			TipoAreaMagazzino tipoAreaMagazzinoDestinazione, Date dataDocumentoDestinazione,
			SedeEntita sedeEntitaDestinazione, EntitaLite entitaDestinazione, int numeroDocumento,
			String noteFatturazione, String uuidContabilizzazione, String utente) throws RigaArticoloNonValidaException;

	/**
	 * Genera il documento di rifatturazione.
	 *
	 * @param listAree
	 *            aree da fatturare
	 * @param tipoAreaMagazzinoDestinazione
	 *            tipo area magazzino del documento di fatturazione
	 * @param dataDocumentoDestinazione
	 *            data del documento di fatturazione
	 * @param noteFatturazione
	 *            note del documento di fatturazione
	 * @param sedePerRifatturazione
	 *            sede per rifatturazione
	 * @param uuidContabilizzazione
	 *            uuid di contabilizzazione
	 * @param utente
	 *            utente di generazione
	 * @throws RigaArticoloNonValidaException
	 *             exception
	 * @throws SedePerRifatturazioneAssenteException
	 *             exception
	 */
	void generaDocumentoRifatturazione(List<AreaMagazzinoFatturazione> listAree,
			TipoAreaMagazzino tipoAreaMagazzinoDestinazione, Date dataDocumentoDestinazione, String noteFatturazione,
			SedeMagazzinoLite sedePerRifatturazione, String uuidContabilizzazione, String utente)
			throws RigaArticoloNonValidaException, SedePerRifatturazioneAssenteException;
}
