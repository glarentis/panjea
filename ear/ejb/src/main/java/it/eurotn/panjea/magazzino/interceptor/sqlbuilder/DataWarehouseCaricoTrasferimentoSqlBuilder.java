/**
 * 
 */
package it.eurotn.panjea.magazzino.interceptor.sqlbuilder;

/**
 * Builder per definire la query sql per i carichi nel caso di trasferimento di magazzino.
 * 
 * @author Leonardo
 */
public class DataWarehouseCaricoTrasferimentoSqlBuilder extends DataWarehouseCaricoSqlBuilder {

	/**
	 * Costruttore.
	 */
	public DataWarehouseCaricoTrasferimentoSqlBuilder() {
		super();
	}

	/**
	 * 
	 * @see AbstractDataWarehouseMovimentiMagazzinoSqlBuilder#AbstractDataWarehouseMovimentiMagazzinoSqlBuilder(TipoFiltro)
	 * @param filtro
	 *            .
	 */
	public DataWarehouseCaricoTrasferimentoSqlBuilder(final TipoFiltro filtro) {
		super(filtro);
	}

	@Override
	public String getLeftJoinDepositoSql() {
		return "left join anag_depositi deposito on deposito.id=maga_area_magazzino.depositoDestinazione_id ";
	}

	@Override
	public String getTipoMovimentoOriginale() {
		return "3";
	}

}
