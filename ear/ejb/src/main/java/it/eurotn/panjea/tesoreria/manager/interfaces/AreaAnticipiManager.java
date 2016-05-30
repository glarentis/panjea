package it.eurotn.panjea.tesoreria.manager.interfaces;

import it.eurotn.panjea.anagrafica.domain.RapportoBancarioAzienda;
import it.eurotn.panjea.tesoreria.domain.AreaAnticipo;
import it.eurotn.panjea.tesoreria.domain.AreaEffetti;
import it.eurotn.panjea.tesoreria.util.SituazioneRigaAnticipo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;

/**
 * 
 * Gestore area anticipi.
 * 
 * @author vittorio
 * @version 1.0, 26/nov/2009
 * 
 */
@Local
public interface AreaAnticipiManager extends IAreaTesoreriaDAO {

	/**
	 * Carica il totale dell'importo anticipato per il rapporto bancario e data valuta specificati.
	 * 
	 * @param areaEffetti
	 *            areaEffetti
	 * @param rapportoBancarioAzienda
	 *            rapporto bancario
	 * @param dataValuta
	 *            data valuta
	 * 
	 * @return totale importo anticipato, 0 se non presente
	 */
	BigDecimal caricaImportoAnticipato(AreaEffetti areaEffetti, RapportoBancarioAzienda rapportoBancarioAzienda,
			Date dataValuta);

	/**
	 * Crea un'area anticipo con le righe anticipo.
	 * 
	 * @param situazioneRigaAnticipo
	 *            da collegare all'area
	 * @return l'areaAnticipo
	 */
	AreaAnticipo creaAreaAnticipo(List<SituazioneRigaAnticipo> situazioneRigaAnticipo);

}
