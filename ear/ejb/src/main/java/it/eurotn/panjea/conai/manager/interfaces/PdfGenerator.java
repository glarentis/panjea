package it.eurotn.panjea.conai.manager.interfaces;

import it.eurotn.panjea.conai.domain.ConaiParametriCreazione;
import it.eurotn.panjea.conai.domain.StatisticaTipoImballo;

import java.util.List;

import javax.ejb.Local;

@Local
public interface PdfGenerator {
	/**
	 * Genera il file per il materiale interessato.
	 * 
	 * @param parametri
	 *            parametri con i quali generare il file
	 * @param set
	 *            peso per ogni tipo imballo
	 * @return array di byte con il file pdf generato
	 */
	byte[] generaFile(ConaiParametriCreazione parametri, List<StatisticaTipoImballo> set);
}
