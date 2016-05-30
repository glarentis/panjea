package it.eurotn.panjea.magazzino.interceptor.sqlbuilder;

public class DataWarehouseFatturatoSqlBuilder extends AbstractDataWarehouseMovimentiMagazzinoSqlBuilder {

	/**
	 * Costruttore.
	 */
	public DataWarehouseFatturatoSqlBuilder() {
		super();
	}

	/**
	 * Costruttore.
	 * 
	 * @param filtro
	 *            TipoFiltro
	 */
	public DataWarehouseFatturatoSqlBuilder(final TipoFiltro filtro) {
		super(filtro);
	}

	@Override
	public String getImportoCaricoAltroSql() {
		return "0, ";
	}

	@Override
	public String getImportoCaricoSql() {
		return "0, ";
	}

	@Override
	public String getImportoScaricoAltroSql() {
		return "0, ";
	}

	@Override
	public String getImportoScaricoSql() {
		return "0, ";
	}

	@Override
	public String getLeftJoinDepositoSql() {
		return "left join anag_depositi deposito on deposito.id=maga_area_magazzino.depositoOrigine_id ";
	}

	@Override
	public String getQtaCaricoAltroSql() {
		return "0, ";
	}

	@Override
	public String getQtaCaricoSql() {
		return "0, ";
	}

	@Override
	public String getQtaMagazzinoCaricoAltroSql() {
		return "0, ";
	}

	@Override
	public String getQtaMagazzinoCaricoSql() {
		return "0, ";
	}

	@Override
	public String getQtaMagazzinoScaricoAltroSql() {
		return "0, ";
	}

	@Override
	public String getQtaMagazzinoScaricoSql() {
		return "0, ";
	}

	@Override
	public String getQtaScaricoAltroSql() {
		return "0, ";
	}

	@Override
	public String getQtaScaricoSql() {
		return "0, ";
	}

	@Override
	public String getTipoMovimentoOriginale() {
		return "0";
	}

	@Override
	public String getTipoMovimentoSql() {
		return "0, ";
	}

}
