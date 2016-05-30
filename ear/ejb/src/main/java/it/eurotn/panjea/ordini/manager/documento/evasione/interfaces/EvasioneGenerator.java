package it.eurotn.panjea.ordini.manager.documento.evasione.interfaces;

import it.eurotn.panjea.lotti.exception.EvasioneLottiException;
import it.eurotn.panjea.lotti.exception.LottiException;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCarico;
import it.eurotn.panjea.ordini.exception.TipoAreaPartitaDestinazioneRichiestaException;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

@Local
public interface EvasioneGenerator {

	/**
	 * Evade una lista di righe su un documento di destinazione.
	 * 
	 * @param righeEvasione
	 *            righe da evadere
	 * @param documentoEvasione
	 *            documento di destinazione
	 * @throws EvasioneLottiException
	 *             rlanciata se ho lotti obbligatori e non li ho nelle righeEvasione.
	 */
	void evadiOrdini(List<RigaDistintaCarico> righeEvasione, AreaMagazzino documentoEvasione)
			throws EvasioneLottiException;

	/**
	 * Evade una lista di righe creando i documenti di destinazione.
	 * 
	 * @param righeEvasione
	 *            righe da evadere
	 * @param dataEvasione
	 *            data del documento di destinazione
	 * @return documenti creati
	 * @throws TipoAreaPartitaDestinazioneRichiestaException
	 *             sollevata se l'ordine che si sta evadendo non prevede un tipo area partita e il documento di
	 *             destinazione si
	 * @throws LottiException
	 *             errore sui lotti
	 */
	List<AreaMagazzinoFullDTO> evadiOrdini(List<RigaDistintaCarico> righeEvasione, Date dataEvasione)
			throws TipoAreaPartitaDestinazioneRichiestaException, LottiException;

}