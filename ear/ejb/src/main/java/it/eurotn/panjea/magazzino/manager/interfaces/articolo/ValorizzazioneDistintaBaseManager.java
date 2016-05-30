package it.eurotn.panjea.magazzino.manager.interfaces.articolo;

import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriValorizzazioneDistinte;
import it.eurotn.panjea.mrp.domain.Bom;
import it.eurotn.panjea.mrp.util.ArticoloConfigurazioneKey;

import java.util.Map;

import javax.ejb.Local;

@Local
public interface ValorizzazioneDistintaBaseManager {

	/**
	 *
	 * @param parametriValorizzazioneDistinte
	 *            parametri per la valorizzazione
	 * @return mappa con articolo e bom della distinta
	 */
	Map<ArticoloConfigurazioneKey, Bom> valorizzaDistinte(
			ParametriValorizzazioneDistinte parametriValorizzazioneDistinte);
}
