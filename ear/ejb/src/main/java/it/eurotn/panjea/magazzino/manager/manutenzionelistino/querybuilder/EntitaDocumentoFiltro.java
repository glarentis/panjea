package it.eurotn.panjea.magazzino.manager.manutenzionelistino.querybuilder;

public class EntitaDocumentoFiltro extends AbstractFiltroSorgente {

	@Override
	public String getJoin() {
		return " inner join maga_righe_magazzino righe on righe.articolo_id=articoli.id inner join maga_area_magazzino am on am.id=righe.areaMagazzino_id inner join docu_documenti doc on doc.id=am.documento_id ";
	}

	@Override
	public String getWhere() {
		return " righe.TIPO_RIGA='A' and doc.entita_id=:entita ";
	}

}
