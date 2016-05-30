package it.eurotn.panjea.magazzino.interceptor.sqlbuilder;

public class DataWarehouseDepositiSqlBuilder {

	public enum TipoFiltro {
		DEPOSITO_ID, SEDE_ID, NESSUNO
	}

	public static final String PARAM_FILTRO_DEPOSITO_ID = "deposito_id";
	public static final String PARAM_FILTRO_SEDE_ID = "sede_id";
	public static final String PARAM_DESCRIZIONE_SEDE = "descSede";
	private TipoFiltro tipoFiltro = null;

	/**
	 * Costruttore.
	 * 
	 * @param tipoFiltro
	 *            tipoFiltro
	 */
	public DataWarehouseDepositiSqlBuilder(final TipoFiltro tipoFiltro) {
		super();
		this.tipoFiltro = tipoFiltro;
	}

	/**
	 * 
	 * @return sql per la cancellazione
	 */
	public String getSqlDelete() {
		StringBuffer sb = new StringBuffer();
		if (tipoFiltro.equals(TipoFiltro.DEPOSITO_ID)) {
			sb.append("delete ");
			sb.append("from dw_depositi ");
			sb.append("where id=:" + PARAM_FILTRO_DEPOSITO_ID);
		} else if (tipoFiltro.equals(TipoFiltro.NESSUNO)) {
			sb.append("truncate dw_depositi");
		}
		return sb.toString();
	}

	/**
	 * Sql di inserimento dei depositi, devo definire PARAM_FILTRO_DEPOSITO_ID se tipoFiltro è DEPOSITO_ID.
	 * 
	 * @return sql per inserimento
	 */
	public String getSqlInsert() {
		StringBuffer sb = new StringBuffer();
		sb.append("insert into dw_depositi ");
		sb.append("select ");
		sb.append("anag_depositi.id, ");
		sb.append("anag_depositi.descrizione, ");
		sb.append("anag_depositi.codice, ");
		sb.append("anag_sedi_anagrafica.id, ");
		sb.append("anag_sedi_anagrafica.descrizione, ");
		sb.append("anag_tipo_deposito.codice ");
		sb.append("from anag_depositi ");
		sb.append("inner join anag_sedi_azienda on anag_sedi_azienda.id=anag_depositi.sedeDeposito_id ");
		sb.append("inner join anag_sedi_anagrafica on anag_sedi_azienda.sede_id=anag_sedi_anagrafica.id ");
		sb.append("inner join anag_tipo_deposito on anag_tipo_deposito.id=anag_depositi.tipoDeposito_id ");
		if (tipoFiltro.equals(TipoFiltro.DEPOSITO_ID)) {
			sb.append("where anag_depositi.id=:" + PARAM_FILTRO_DEPOSITO_ID);
		}
		return sb.toString();
	}

	/**
	 * Aggiornamento della descrizione di un deposito, bisogna impostare PARAM_DESCRIZIONE_DEPOSITO e
	 * PARAM_FILTRO_SEDE_ID.
	 * 
	 * @return sql per l'aggiornamento
	 */
	public String getSqlUpdate() {
		StringBuffer sb = new StringBuffer();
		sb.append("update dw_depositi ");
		sb.append("set sede_descrizione=:" + PARAM_DESCRIZIONE_SEDE);
		sb.append(" where sede_id=:" + PARAM_FILTRO_SEDE_ID);
		return sb.toString();
	}

}
