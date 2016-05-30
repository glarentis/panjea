package it.eurotn.panjea.magazzino.manager.manutenzionelistino.querybuilder;

public class EntitaAnagraficaArticoloFiltro extends AbstractFiltroSorgente {

	@Override
	public String getJoin() {
		return " inner join maga_codici_articolo_entita artent on artent.articolo_id=articoli.id ";
	}

	@Override
	public String getWhere() {
		return " artent.entita_id=:entita ";
	}

}
