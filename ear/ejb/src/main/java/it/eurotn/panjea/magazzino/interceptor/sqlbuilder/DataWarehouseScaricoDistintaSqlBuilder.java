package it.eurotn.panjea.magazzino.interceptor.sqlbuilder;

public class DataWarehouseScaricoDistintaSqlBuilder extends DataWarehouseScaricoSqlBuilder {

	/**
	 * Costruttore.
	 */
	public DataWarehouseScaricoDistintaSqlBuilder() {
		super();
	}

	/**
	 * Costruttore.
	 * 
	 * @param filtro
	 *            TipoFiltro
	 */
	public DataWarehouseScaricoDistintaSqlBuilder(final TipoFiltro filtro) {
		super(filtro);
	}

	@Override
	public String getLeftJoinDepositoSql() {
		return "left join anag_depositi deposito on deposito.id=maga_area_magazzino.depositoDestinazione_id ";
	}

	@Override
	public String getTipoMovimentoOriginale() {
		return "7";
	}

	@Override
	public String getTipoMovimentoSql() {
		return "7, ";
	}

	@Override
	protected String getWhereTipoArticolo() {
		return "where maga_righe_magazzino.TIPO_RIGA='C'";
	}
}
