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
public class DataWarehouseScaricoSqlBuilder extends AbstractDataWarehouseMovimentiMagazzinoSqlBuilder {

	/**
	 * Costruttore.
	 */
	public DataWarehouseScaricoSqlBuilder() {
		super();
	}

	/**
	 * 
	 * @see AbstractDataWarehouseMovimentiMagazzinoSqlBuilder#AbstractDataWarehouseMovimentiMagazzinoSqlBuilder(TipoFiltro)
	 * @param filtro
	 *            .
	 */
	public DataWarehouseScaricoSqlBuilder(final TipoFiltro filtro) {
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
		return "IF( sezioneTipoMovimento=1,maga_righe_magazzino.importoInValutaAziendaTotale*if(docu_tipi_documento.notaCreditoEnable,-1,1) ,0), ";
	}

	@Override
	public String getImportoScaricoSql() {
		return "IF (sezioneTipoMovimento=0,maga_righe_magazzino.importoInValutaAziendaTotale*if(docu_tipi_documento.notaCreditoEnable,-1,1) ,0),";
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
		return "IF (sezioneTipoMovimento=1,maga_righe_magazzino.qtaMagazzino*if(docu_tipi_documento.notaCreditoEnable,-1,1) ,0), ";
	}

	@Override
	public String getQtaMagazzinoScaricoSql() {
		return "IF( sezioneTipoMovimento=0,maga_righe_magazzino.qtaMagazzino*if(docu_tipi_documento.notaCreditoEnable,-1,1) ,0), ";
	}

	@Override
	public String getQtaScaricoAltroSql() {
		return "IF (sezioneTipoMovimento=1,maga_righe_magazzino.qta*if(docu_tipi_documento.notaCreditoEnable,-1,1) ,0),";
	}

	@Override
	public String getQtaScaricoSql() {
		return "IF( sezioneTipoMovimento=0,maga_righe_magazzino.qta*if(docu_tipi_documento.notaCreditoEnable,-1,1) ,0), ";
	}

	@Override
	public String getTipoMovimentoOriginale() {
		return "2";
	}

	@Override
	public String getTipoMovimentoSql() {
		return "2, ";
	}

}
