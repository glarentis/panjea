package it.eurotn.panjea.magazzino.manager.documento.interfaces;

import it.eurotn.panjea.magazzino.domain.SedeMagazzinoLite;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzinoLite;
import it.eurotn.panjea.magazzino.exception.RigaArticoloNonValidaException;
import it.eurotn.panjea.magazzino.exception.SedeNonAppartieneAdEntitaException;
import it.eurotn.panjea.magazzino.manager.documento.fatturazione.AreaMagazzinoFatturazione;
import it.eurotn.panjea.magazzino.service.exception.SedePerRifatturazioneAssenteException;

import java.util.Date;
import java.util.List;

public interface FatturazioneDifferitaGenerator {

	/**
	 * Carica le aree magazzino da fatturare. Vengono caricati solo i dati che servono creando oggetti di tipo
	 * {@link AreaMagazzinoFatturazione}.
	 *
	 * @param areeDaFatturare
	 *            aree da fatturare
	 * @return aree caricate
	 */
	List<AreaMagazzinoFatturazione> caricaAreeMagazzinoFatturazione(List<AreaMagazzinoLite> areeDaFatturare);

	/**
	 * Genera una fatturazione temporanea. <br./>
	 * Crea un documento di magazzino con le righe dei documenti di origine.<br/>
	 * I documenti di origine vengono raggruppati per entità, pagamento, data, sede.<br/>
	 * Se un documento di origine ha il flag di non raggruppare viene creato un documento di destinazione singolo.<br/>
	 * L'area di magazzino creata ha come deposito di origine il deposito principale dell'azienda.<br/>
	 *
	 * @param areeDaFatturare
	 *            lista di areeMagazzino da fatturare
	 * @param dataDocumentoDestinazione
	 *            dataDocumentoDestinazione
	 * @param noteFatturazione
	 *            note da inserire come dati dela fatturazione differita
	 * @param sedePerRifatturazione
	 *            sede per la rifatturazione
	 * @param utente
	 *            utente di generazione
	 * @throws RigaArticoloNonValidaException
	 *             lanciata quando una riga non è valida
	 * @throws SedePerRifatturazioneAssenteException
	 *             lanciata quando almeno un'area non ha una sede per rifatturazione
	 */
	void genera(List<AreaMagazzinoLite> areeDaFatturare, Date dataDocumentoDestinazione, String noteFatturazione,
			SedeMagazzinoLite sedePerRifatturazione, String utente) throws RigaArticoloNonValidaException,
			SedePerRifatturazioneAssenteException, SedeNonAppartieneAdEntitaException;
}
