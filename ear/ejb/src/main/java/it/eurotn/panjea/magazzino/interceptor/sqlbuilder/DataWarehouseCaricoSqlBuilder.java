/**
 * 
 */
package it.eurotn.panjea.magazzino.interceptor.sqlbuilder;

/**
 * Builder per definire la query sql per i carichi di magazzino.
 * 
 * @author Leonardo
 */
public class DataWarehouseCaricoSqlBuilder extends AbstractDataWarehouseMovimentiMagazzinoSqlBuilder {

	/**
	 * Costruttore.
	 */
	public DataWarehouseCaricoSqlBuilder() {
		super();
	}

	/**
	 * 
	 * @see AbstractDataWarehouseMovimentiMagazzinoSqlBuilder#AbstractDataWarehouseMovimentiMagazzinoSqlBuilder(TipoFiltro)
	 * @param filtro
	 *            .
	 */
	public DataWarehouseCaricoSqlBuilder(final TipoFiltro filtro) {
		super(filtro);
	}

	@Override
	public String getImportoCaricoAltroSql() {
		return "IF (sezioneTipoMovimento=1,maga_righe_magazzino.importoInValutaAziendaTotale*if(docu_tipi_documento.notaCreditoEnable,-1,1),0),";
	}

	@Override
	public String getImportoCaricoSql() {
		return "IF (sezioneTipoMovimento=0,maga_righe_magazzino.importoInValutaAziendaTotale*if(docu_tipi_documento.notaCreditoEnable,-1,1),0),";
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
		return "IF(sezioneTipoMovimento=1,maga_righe_magazzino.qta*if(docu_tipi_documento.notaCreditoEnable,-1,1),0), ";
	}

	@Override
	public String getQtaCaricoSql() {
		return "IF(sezioneTipoMovimento=0,maga_righe_magazzino.qta*if(docu_tipi_documento.notaCreditoEnable,-1,1),0), ";
	}

	@Override
	public String getQtaMagazzinoCaricoAltroSql() {
		return "IF(sezioneTipoMovimento=1,maga_righe_magazzino.qtaMagazzino*if(docu_tipi_documento.notaCreditoEnable,-1,1),0), ";
	}

	@Override
	public String getQtaMagazzinoCaricoSql() {
		return "IF( sezioneTipoMovimento=0,maga_righe_magazzino.qtaMagazzino*if(docu_tipi_documento.notaCreditoEnable,-1,1),0), ";
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
		return "1";
	}

	@Override
	public String getTipoMovimentoSql() {
		return "1, ";
	}

}
