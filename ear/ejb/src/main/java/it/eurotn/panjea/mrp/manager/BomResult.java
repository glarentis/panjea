package it.eurotn.panjea.mrp.manager;

import it.eurotn.panjea.mrp.domain.Bom;
import it.eurotn.panjea.mrp.util.ArticoloConfigurazioneKey;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BomResult {

	private Map<ArticoloConfigurazioneKey, Bom> result;

	/**
	 * Costruttore.
	 */
	public BomResult() {
		result = new HashMap<>();
	}

	/**
	 * Restituisce il Bom associato all'articolo o un nuovo bum se non esiste.
	 * 
	 * @param idArticolo
	 *            idArticolo l'articolo di cui recuperare il bom
	 * @param idConfigurazione
	 *            idConfigurazione
	 * @param qtaAttrezzaggio
	 *            qta di attrezzaggio collegato direttamente al componente/articolo
	 * @param qtaAttrezzaggioFasi
	 *            qta di attrezzaggio collegato direttamente alle fasi del componente/articolo
	 * @return Bom
	 */
	public synchronized Bom getBom(int idArticolo, Integer idConfigurazione, Double qtaAttrezzaggio,
			Double qtaAttrezzaggioFasi) {
		ArticoloConfigurazioneKey key = new ArticoloConfigurazioneKey(idArticolo, idConfigurazione);
		Bom bom = result.get(key);
		if (bom == null) {
			bom = new Bom(idArticolo, idConfigurazione, qtaAttrezzaggio, qtaAttrezzaggioFasi);
			result.put(key, bom);
		}
		return bom;
	}

	/**
	 * @return Map<String, Bom>
	 */
	public Map<ArticoloConfigurazioneKey, Bom> getResult() {
		return Collections.unmodifiableMap(result);
	}

	/**
	 * 
	 * @param key
	 *            idArticolo e idConfigurazione
	 * @param bom
	 *            bom da inserire in mappa
	 */
	public synchronized void put(ArticoloConfigurazioneKey key, Bom bom) {
		result.put(key, bom);
	}

	/**
	 * 
	 * @param bomExplose
	 *            elementi da aggiungere
	 */
	public synchronized void putAll(Map<ArticoloConfigurazioneKey, Bom> bomExplose) {
		result.putAll(bomExplose);
	}
}
