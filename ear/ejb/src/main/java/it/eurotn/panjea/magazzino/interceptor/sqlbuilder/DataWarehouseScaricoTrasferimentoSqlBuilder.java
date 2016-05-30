/**
 * 
 */
package it.eurotn.panjea.magazzino.interceptor.sqlbuilder;

/**
 * Builder per definire la query sql per gli scarichi nel caso di trasferimento di magazzino. Nota che questo builder e'
 * uguale a quello base degli scarichi.
 * 
 * @author Leonardo
 */
public class DataWarehouseScaricoTrasferimentoSqlBuilder extends DataWarehouseScaricoSqlBuilder {

	/**
	 * Costruttore.
	 */
	public DataWarehouseScaricoTrasferimentoSqlBuilder() {
		super();
	}

	/**
	 * 
	 * @see AbstractDataWarehouseMovimentiMagazzinoSqlBuilder#AbstractDataWarehouseMovimentiMagazzinoSqlBuilder(TipoFiltro)
	 * @param filtro
	 *            .
	 */
	public DataWarehouseScaricoTrasferimentoSqlBuilder(final TipoFiltro filtro) {
		super(filtro);
	}

	@Override
	public String getTipoMovimentoOriginale() {
		return "3";
	}
}
