/**
 * 
 */
package it.eurotn.panjea.magazzino.interceptor.sqlbuilder;

/**
 * Builder per definire la query sql per gli scarichi di magazzino.
 * 
 * @author Leonardo
 * 
 */
public class DataWarehouseScaricoValoreSqlBuilder extends DataWarehouseScaricoSqlBuilder {

	/**
	 * Costruttore.
	 */
	public DataWarehouseScaricoValoreSqlBuilder() {
		super();
	}

	/**
	 * 
	 * @see AbstractDataWarehouseMovimentiMagazzinoSqlBuilder#AbstractDataWarehouseMovimentiMagazzinoSqlBuilder(TipoFiltro)
	 * @param filtro
	 *            .
	 */
	public DataWarehouseScaricoValoreSqlBuilder(final TipoFiltro filtro) {
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
		return "0 ,";
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
		return "6";
	}

	@Override
	public String getTipoMovimentoSql() {
		return "2, ";
	}

}
