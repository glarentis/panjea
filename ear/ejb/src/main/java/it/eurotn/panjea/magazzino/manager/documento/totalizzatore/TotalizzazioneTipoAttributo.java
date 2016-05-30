package it.eurotn.panjea.magazzino.manager.documento.totalizzatore;

import it.eurotn.panjea.anagrafica.domain.ConversioneUnitaMisura;
import it.eurotn.panjea.anagrafica.util.ConversioniUnitaMisura;
import it.eurotn.panjea.magazzino.domain.TotalizzatoreTipoAttributo;
import it.eurotn.panjea.magazzino.service.exception.ConversioneUnitaMisuraAssenteException;
import it.eurotn.panjea.magazzino.util.RigaAttributoTotalizzazioneDTO;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class TotalizzazioneTipoAttributo {

	private ConversioniUnitaMisura conversioniUnitaMisura = null;
	private ScriptEngineManager manager = null;
	private ScriptEngine engine = null;

	/**
	 * Prepara il totalizzatore per i tipi attributo con integrata l' utilità per la conversione tra le diverse unità di
	 * misura;<br>
	 * se non ci sono elementi definiti e l'unità di misura di un tipo attributo è diversa da quella di default per il
	 * tipoTotalizzazione associato, viene sollevata una eccezione.
	 * 
	 * @param listConversioniUm
	 *            la lista di conversioni tra unità di misura disponibili
	 */
	public TotalizzazioneTipoAttributo(final List<ConversioneUnitaMisura> listConversioniUm) {
		super();
		// inizializzo script engine javascript
		manager = new ScriptEngineManager();
		engine = manager.getEngineByName("js");
		conversioniUnitaMisura = new ConversioniUnitaMisura(listConversioniUm);
	}

	/**
	 * Totalizza la lista di righe attributo secondo i totalizzatori Tipo Attributo trovati.
	 * 
	 * @param attribuitiRigaValore
	 *            le righe associate ad un totalizzatore tipo attributo
	 * @return la mappa Map<TotalizzatoreTipoAttributo, BigDecimal> con i totali per TotalizzatoreTipoAttributo
	 * @throws ConversioneUnitaMisuraAssenteException
	 *             se non è definita la conversione tra due unità di misura
	 */
	public Map<TotalizzatoreTipoAttributo, BigDecimal> totalizza(
			List<RigaAttributoTotalizzazioneDTO> attribuitiRigaValore) throws ConversioneUnitaMisuraAssenteException {

		// preparo la map di risultati per totalizzatore
		Map<TotalizzatoreTipoAttributo, BigDecimal> totaliTipoAttributo = new HashMap<TotalizzatoreTipoAttributo, BigDecimal>();

		for (RigaAttributoTotalizzazioneDTO rigaAttributoTotalizzazioneDTO : attribuitiRigaValore) {

			// nella riga si trova value, tipoTotalizzatore, numero decimali,
			// unità di misura
			BigDecimal val = rigaAttributoTotalizzazioneDTO.getValore();
			if (val == null) {
				val = BigDecimal.ZERO;
			}
			TotalizzatoreTipoAttributo totalizzatoreTipoAttributo = rigaAttributoTotalizzazioneDTO
					.getTotalizzatoreTipoAttributo();
			Integer numeroDecimali = rigaAttributoTotalizzazioneDTO.getNumeroDecimali();
			String unitaMisuraOrigine = rigaAttributoTotalizzazioneDTO.getCodiceUnitaMisura();

			ConversioneUnitaMisura conversioneUnitaMisura = conversioniUnitaMisura.getConversione(unitaMisuraOrigine,
					totalizzatoreTipoAttributo.getUnitaMisuraDefault());

			// converto solo se l'unità di misura della riga è diversa
			// dall'unità di misura di default per il tipo totalizzazione
			if (!unitaMisuraOrigine.equalsIgnoreCase(totalizzatoreTipoAttributo.getUnitaMisuraDefault())) {
				// se non trovo la conversione per le unità di misura lancio una eccezione
				if (conversioneUnitaMisura == null) {
					ConversioneUnitaMisuraAssenteException conversioneUnitaMisuraAssenteException = conversioniUnitaMisura
							.getConversioneUnitaMisuraAssenteException(unitaMisuraOrigine,
									totalizzatoreTipoAttributo.getUnitaMisuraDefault());
					throw conversioneUnitaMisuraAssenteException;
				}

				// converto il valore all'unità di misura di default
				val = (BigDecimal) conversioneUnitaMisura.converti(val, numeroDecimali, engine);
			}

			BigDecimal totTmp = totaliTipoAttributo.get(totalizzatoreTipoAttributo);
			if (totTmp == null) {
				totTmp = BigDecimal.ZERO;
			}
			totTmp = totTmp.add(val);
			totaliTipoAttributo.put(totalizzatoreTipoAttributo, totTmp);
		}

		return totaliTipoAttributo;
	}

}
