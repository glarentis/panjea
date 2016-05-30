package it.eurotn.panjea.magazzino.interceptor.sqlbuilder;

public class DataWarehouseCaricoDistintaSqlBuilder extends DataWarehouseCaricoSqlBuilder {

	/**
	 * 
	 * Costruttore.
	 * 
	 */
	public DataWarehouseCaricoDistintaSqlBuilder() {
		super();
	}

	/**
	 * 
	 * @see AbstractDataWarehouseMovimentiMagazzinoSqlBuilder#AbstractDataWarehouseMovimentiMagazzinoSqlBuilder(TipoFiltro)
	 * @param filtro
	 *            .
	 */
	public DataWarehouseCaricoDistintaSqlBuilder(final TipoFiltro filtro) {
		super(filtro);
	}

	@Override
	public String getTipoMovimentoOriginale() {
		return "7";
	}

	@Override
	public String getTipoMovimentoSql() {
		return "7,";
	}

}
