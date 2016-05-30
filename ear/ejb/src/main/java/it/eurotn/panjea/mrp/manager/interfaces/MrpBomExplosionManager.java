package it.eurotn.panjea.mrp.manager.interfaces;

import it.eurotn.panjea.mrp.domain.Bom;
import it.eurotn.panjea.mrp.util.ArticoloConfigurazioneKey;

import java.util.Map;
import java.util.Set;

import javax.ejb.Local;

@Local
public interface MrpBomExplosionManager {

	int COLUMN_ID_CONFIGURAZIONE = 0;
	int COLUMN_ID_DISTINTA = 1;
	int COLUMN_ID_ARTICOLO = 2;
	int COLUMN_FORMULA = 3;
	int COLUMN_ID = 4;
	int COLUMN_ID_COMPONENTE_PADRE = 5;
	int COLUMN_CODICI_ATTRIBUTI = 6;
	int COLUMN_VALORI_ATTRIBUTI = 7;
	int COLUMN_QTA_ATTREZZAGGIO_DISTINTA = 8;
	int COLUMN_QTA_ATTREZZAGGIO_ARTICOLO = 9;

	/**
	 * Esplode tutte le distinte.
	 * 
	 * @return mappa con chiave id articolo (distinta) e valore la struttura della bom
	 */
	Map<ArticoloConfigurazioneKey, Bom> esplodiBoms();

	/**
	 * Esplode tutte le distinte e le configurazioni utilizzate.
	 * 
	 * @param configurazioniUtilizzate
	 *            configurazioni utilizzate
	 * @return mappa con chiave id articolo (distinta) e valore la struttura della bom
	 */
	Map<ArticoloConfigurazioneKey, Bom> esplodiBoms(Set<Integer> configurazioniUtilizzate);

}
