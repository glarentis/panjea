package it.eurotn.panjea.magazzino.manager.manutenzionelistino.querybuilder;

public class ListinoFiltro extends AbstractFiltroSorgente {

	@Override
	public String getJoin() {
		StringBuilder sb = new StringBuilder(300);
		sb.append("inner join maga_righe_listini righeListino on righeListino.articolo_id=articoli.id ");
		sb.append("inner join maga_versioni_listino versioni on versioni.id=righeListino.versioneListino_id ");
		sb.append("inner join maga_scaglioni_listini sc on sc.rigaListino_id = righeListino.id ");
		return sb.toString();
	}

	@Override
	public String getWhere() {
		return " versioni.id=:versioneListino ";
	}

}
