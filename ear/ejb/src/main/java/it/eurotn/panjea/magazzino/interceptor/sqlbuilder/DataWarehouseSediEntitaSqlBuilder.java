package it.eurotn.panjea.magazzino.interceptor.sqlbuilder;

public class DataWarehouseSediEntitaSqlBuilder {

	public enum TipoFiltro {
		SEDE_ANAGRAFICA_ID, NESSUNO
	}

	public static final String PARAM_FILTRO_SEDE_ANAGRAFICA_ID = "sedeAnagrafica_id";
	private TipoFiltro tipoFiltro = null;

	/**
	 * Costruttore.
	 *
	 * @param tipoFiltro
	 *            tipoFiltro
	 */
	public DataWarehouseSediEntitaSqlBuilder(final TipoFiltro tipoFiltro) {
		super();
		this.tipoFiltro = tipoFiltro;
	}

	/**
	 * Restituisce la query da eseguire per l'inserimento delle sedi entita nella tabella dw_sedientita.
	 *
	 * @return la query sql da eseguire
	 */
	public String getSql() {
		StringBuffer sb = new StringBuffer();
		sb.append("insert into dw_sedientita ");
		sb.append("select ");
		sb.append("anagrafica.codice_azienda, ");
		sb.append("sedient.id, ");
		sb.append("sedient.sede_anagrafica_id, ");
		sb.append("ent.TIPO_ANAGRAFICA, ");
		sb.append("anagrafica.denominazione, ");
		sb.append("ent.codice, ");
		sb.append("nazione.codice as nazione, ");
		sb.append("lvl1.nome as livelloAmministrativo1, ");
		sb.append("lvl2.nome as livelloAmministrativo2, ");
		sb.append("lvl3.nome as livelloAmministrativo3, ");
		sb.append("lvl4.nome as livelloAmministrativo4, ");
		sb.append("localita.descrizione as localita, ");
		sb.append("cap.descrizione as cap, ");
		sb.append("ent.id as entita_id, ");
		sb.append("ent.capoArea, ");
		sb.append("agenticapoarea.id, ");
		sb.append("agenticapoarea.codice, ");
		sb.append("anagraficaagenticapoarea.denominazione, ");
		sb.append("sedient.codice, ");
		sb.append("sedian.descrizione ");
		sb.append("from anag_anagrafica anagrafica ");
		sb.append("inner join anag_entita ent on ent.anagrafica_id=anagrafica.id ");
		sb.append("inner join anag_sedi_entita sedient on sedient.entita_id=ent.id ");
		sb.append("inner join anag_sedi_anagrafica sedian on sedian.id=sedient.sede_anagrafica_id ");
		sb.append("left join geog_nazioni nazione on nazione.id=sedian.nazione_id ");
		sb.append("left join geog_livello_1 lvl1 on lvl1.id=sedian.lvl1_id ");
		sb.append("left join geog_livello_2 lvl2 on lvl2.id=sedian.lvl2_id ");
		sb.append("left join geog_livello_3 lvl3 on lvl3.id=sedian.lvl3_id ");
		sb.append("left join geog_livello_4 lvl4 on lvl4.id=sedian.lvl4_id ");
		sb.append("left join geog_localita localita on localita.id=sedian.localita_id ");
		sb.append("left join geog_cap cap on cap.id=sedian.cap_localita_id ");
		sb.append("left join anag_entita agenticapoarea on agenticapoarea.id=ent.capoarea_id ");
		sb.append("left join anag_anagrafica anagraficaagenticapoarea on anagraficaagenticapoarea.id=agenticapoarea.anagrafica_id ");
		if (tipoFiltro.equals(TipoFiltro.SEDE_ANAGRAFICA_ID)) {
			sb.append("where sedian.id=:" + PARAM_FILTRO_SEDE_ANAGRAFICA_ID);
		}
		return sb.toString();
	}

	/**
	 * @return sql per la cancellazione delle sedi dalla tabella dw_sedientita
	 */
	public String getSqlDelete() {
		StringBuffer sb = new StringBuffer();
		if (tipoFiltro.equals(TipoFiltro.SEDE_ANAGRAFICA_ID)) {
			sb.append("delete from dw_sedientita where sede_anagrafica_id=:" + PARAM_FILTRO_SEDE_ANAGRAFICA_ID);
		} else if (tipoFiltro.equals(TipoFiltro.NESSUNO)) {
			sb.append("truncate dw_sedientita");
		}
		return sb.toString();
	}

	/**
	 *
	 * @param codiceAzienda
	 *            codice dell'azienda corrente
	 * @return Stringa per inserire la sede non presente
	 */
	public String getSqlSedeNonPresente(String codiceAzienda) {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO dw_sedientita (codice_azienda");
		sb.append(",sede_entita_id,sede_anagrafica_id,TIPO_ANAGRAFICA,denominazione,codice,nazione,livelloAmministrativo1,livelloAmministrativo2,livelloAmministrativo3,livelloAmministrativo4,localita ,cap    ,entita_id,capoArea,capoarea_id,capoarea_codice,capoarea_denominazione,codice_sede,descrizione_sede ) VALUES ");
		sb.append("('");
		sb.append(codiceAzienda);
		sb.append("'       ,0             ,0             ,'C'            ,'NON PRESENTE '  ,0  ,'IT'   ,null                  ,null                  ,null                  ,null                  ,'','0',1        ,false   ,null       ,null           ,null                  ,'1'        ,'NON PRESENTE') ");
		return sb.toString();
	}

}
