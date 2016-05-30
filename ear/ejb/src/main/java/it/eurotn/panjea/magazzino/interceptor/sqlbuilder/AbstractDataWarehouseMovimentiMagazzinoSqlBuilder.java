/**
 *
 */
package it.eurotn.panjea.magazzino.interceptor.sqlbuilder;

import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.TipoMovimento;

import java.util.HashSet;
import java.util.Set;

/**
 * AbstractBuilder che compone una query Sql per inserire nella tabella dw_movimentimagazzino i dati relativi dei
 * movimenti di magazzino. La query � diversa per valori di importi e quantit� per ogni builder specializzato.
 *
 * @author Leonardo
 */
public abstract class AbstractDataWarehouseMovimentiMagazzinoSqlBuilder {

	/**
	 * Indica se devo filtrare la sincronizzazione per una singola area magazzino o per data.
	 */
	public enum TipoFiltro {
		AREA_MAGAZZINO, TIMESTAMP
	}

	/**
	 * @param tipoMovimento
	 *            tipo movimento
	 * @return builder creati
	 */
	public static Set<AbstractDataWarehouseMovimentiMagazzinoSqlBuilder> createBuilder(TipoMovimento tipoMovimento) {
		Set<AbstractDataWarehouseMovimentiMagazzinoSqlBuilder> builders = new HashSet<AbstractDataWarehouseMovimentiMagazzinoSqlBuilder>();
		switch (tipoMovimento) {
		case CARICO:
			builders.add(new DataWarehouseCaricoSqlBuilder());
			break;
		case CARICO_VALORE:
			builders.add(new DataWarehouseCaricoValoreSqlBuilder());
			break;
		case SCARICO:
			builders.add(new DataWarehouseScaricoSqlBuilder());
			break;
		case SCARICO_VALORE:
			builders.add(new DataWarehouseScaricoValoreSqlBuilder());
			break;
		case TRASFERIMENTO:
			builders.add(new DataWarehouseCaricoTrasferimentoSqlBuilder());
			builders.add(new DataWarehouseScaricoTrasferimentoSqlBuilder());
			break;
		case NESSUNO:
			builders.add(new DataWarehouseFatturatoSqlBuilder());
			break;
		case CARICO_PRODUZIONE:
			builders.add(new DataWarehouseCaricoDistintaSqlBuilder());
			builders.add(new DataWarehouseScaricoDistintaSqlBuilder());
			break;
		default:
			throw new RuntimeException("Tipo movimento non gestito.");
		}
		return builders;
	}

	/**
	 * @uml.property name="filtro"
	 * @uml.associationEnd
	 */
	private TipoFiltro filtro;

	/**
	 * Costruisce il queryBuilder con il filtro impostato su {@link TipoFiltro#AREA_MAGAZZINO}.
	 */
	public AbstractDataWarehouseMovimentiMagazzinoSqlBuilder() {
		this(TipoFiltro.AREA_MAGAZZINO);
	}

	/**
	 * Costruisce il queryBuilder con la modalità di filtro interessata.
	 *
	 * @param filtro
	 *            se {@link TipoFiltro#AREA_MAGAZZINO} inserisce nella query un parametro areaMagazzino_id <br/>
	 *            se {@link TipoFiltro#TIMESTAMP} inserisce un parametri dateUpdate
	 */
	public AbstractDataWarehouseMovimentiMagazzinoSqlBuilder(final TipoFiltro filtro) {
		super();
		this.filtro = filtro;
	}

	/**
	 * Restituisce la stringa sql con il valore da associare alla colonna `importoCaricoAltro` della tabella
	 * dw_movimentimagazzino.
	 *
	 * @return String
	 */
	public abstract String getImportoCaricoAltroSql();

	/**
	 * Restituisce la stringa sql con il valore da associare alla colonna `importoCarico` della tabella
	 * dw_movimentimagazzino.
	 *
	 * @return String
	 */
	public abstract String getImportoCaricoSql();

	/**
	 * Restituisce la stringa sql con il valore da associare alla colonna `importoScaricoAltro` della tabella
	 * dw_movimentimagazzino.
	 *
	 * @return String
	 */
	public abstract String getImportoScaricoAltroSql();

	/**
	 * Restituisce la stringa sql con il valore da associare alla colonna `importoScarico` della tabella
	 * dw_movimentimagazzino.
	 *
	 * @return String
	 */
	public abstract String getImportoScaricoSql();

	/**
	 * Restituisce la stringa sql con la left join sul deposito per la tabella dw_movimentimagazzino.
	 *
	 * @return String
	 */
	public abstract String getLeftJoinDepositoSql();

	/**
	 * Restituisce la stringa sql con il valore da associare alla colonna `qtaCaricoAltro` della tabella
	 * dw_movimentimagazzino.
	 *
	 * @return String
	 */
	public abstract String getQtaCaricoAltroSql();

	/**
	 * Restituisce la stringa sql con il valore da associare alla colonna `qtaCarico` della tabella
	 * dw_movimentimagazzino.
	 *
	 * @return String
	 */
	public abstract String getQtaCaricoSql();

	/**
	 * Restituisce la stringa sql con il valore da associare alla colonna `qtaMagazzinoCaricoAltro` della tabella
	 * dw_movimentimagazzino.
	 *
	 * @return String
	 */
	public abstract String getQtaMagazzinoCaricoAltroSql();

	/**
	 * Restituisce la stringa sql con il valore da associare alla colonna `qtaMagazzinoCarico` della tabella
	 * dw_movimentimagazzino.
	 *
	 * @return String
	 */
	public abstract String getQtaMagazzinoCaricoSql();

	/**
	 * Restituisce la stringa sql con il valore da associare alla colonna `qtaMagazzinoScaricoAltro` della tabella
	 * dw_movimentimagazzino.<BR>
	 *
	 * @return String
	 */
	public abstract String getQtaMagazzinoScaricoAltroSql();

	/**
	 * Restituisce la stringa sql con il valore da associare alla colonna `qtaMagazzinoScarico` della tabella
	 * dw_movimentimagazzino.
	 *
	 * @return String
	 */
	public abstract String getQtaMagazzinoScaricoSql();

	/**
	 * Restituisce la stringa sql con il valore da associare alla colonna `qtaScaricoAltro` della tabella
	 * dw_movimentimagazzino.
	 *
	 * @return String
	 */
	public abstract String getQtaScaricoAltroSql();

	/**
	 * Restituisce la stringa sql con il valore da associare alla colonna `qtaScarico` della tabella
	 * dw_movimentimagazzino.
	 *
	 * @return String
	 */
	public abstract String getQtaScaricoSql();

	/**
	 * Costruisce la query per l'inserimento dei dati del documento di magazzino (solo per tipi movimento di
	 * CARICO,SCARICO e TRASFERIMENTO) nella tabella dw_movimentimagazzino.
	 *
	 * @return La queryString sql
	 */
	public String getSql() {
		StringBuffer sb = new StringBuffer();
		sb.append("insert into dw_movimentimagazzino (");
		sb.append("`codiceAzienda`,`tipoDocumentoCodice`,`tipoDocumentoId`,`tipoDocumentoDescrizione`,`classeTipoDocumento`,");
		sb.append("`notaCreditoEnable`,`dataDocumento`,`numeroDocumento`,`numeroDocumentoOrder`,`dataRegistrazione`,`deposito_id`,");
		sb.append("`listinoAlternativo`,`listino`,`sedeEntita_id`,`tipoOperazione`,`addebitoSpeseIncasso`,");
		sb.append("`idRiga`,`importoCarico`,`importoScarico`,`qtaCarico`,`qtaScarico`,");
		sb.append("`articoloLibero`,`unitaMisura`,`sezioneTipoMovimento`,`sezioneTipoMovimentoValore`,`tipoMovimento`,");
		sb.append("`articolo_id`,`areaMagazzino_id`,`qtaCaricoAltro`,`qtaScaricoAltro`,`importoCaricoAltro`,");
		sb.append("`importoScaricoAltro`,`tipoMovimentoOriginale`,`qtaMagazzinoCarico`,`qtaMagazzinoScarico`,`qtaMagazzinoCaricoAltro`,");
		sb.append("`qtaMagazzinoScaricoAltro`,`annoMovimento`, `qtaFatturatoCarico`, `qtaFatturatoScarico`, `importoFatturatoCarico`, ");
		sb.append("`importoFatturatoScarico`,`agente_id`,`percProvvigione`,`importoProvvigione`,`omaggio`, ");
		sb.append("`sedeVettore_id`,`pesoLordo`,`volume`,`numeroColli`,`aspettoEsteriore`,`causaleTrasporto`,`tipoPorto`,`trasportoCura`,`baseProvv`) ");

		sb.append("select ");
		sb.append("docu_tipi_documento.codiceAzienda, ");
		sb.append("docu_tipi_documento.codice, ");
		sb.append("docu_tipi_documento.id, ");
		sb.append("docu_tipi_documento.descrizione, ");
		sb.append("docu_tipi_documento.classeTipoDocumento, ");
		sb.append("docu_tipi_documento.notaCreditoEnable, ");
		sb.append("docu_documenti.dataDocumento, ");
		sb.append("docu_documenti.codice, ");
		sb.append("docu_documenti.codiceOrder, ");
		sb.append("maga_area_magazzino.dataRegistrazione, ");
		sb.append("deposito.id, ");
		sb.append("listinoAlternativo.codice, ");
		sb.append("listino.codice, ");
		sb.append("docu_documenti.sedeEntita_id, ");
		sb.append("maga_area_magazzino.tipoOperazione, ");
		sb.append("maga_area_magazzino.addebitoSpeseIncasso, ");
		sb.append("maga_righe_magazzino.id, ");
		sb.append(getImportoCaricoSql());
		sb.append(getImportoScaricoSql());
		sb.append(getQtaCaricoSql());
		sb.append(getQtaScaricoSql());
		sb.append("maga_righe_magazzino.articoloLibero, ");
		sb.append("maga_righe_magazzino.unitaMisura, ");
		sb.append("CASE maga_tipi_area_magazzino.sezioneTipoMovimento WHEN 0 THEN 'CARICO' ELSE 'ALTRO CARICO' END, ");
		sb.append("maga_tipi_area_magazzino.sezioneTipoMovimento, ");
		sb.append(getTipoMovimentoSql());
		sb.append("maga_righe_magazzino.articolo_id, ");
		sb.append("maga_area_magazzino.id, ");
		sb.append(getQtaCaricoAltroSql());
		sb.append(getQtaScaricoAltroSql());
		sb.append(getImportoCaricoAltroSql());
		sb.append(getImportoScaricoAltroSql());
		sb.append("maga_tipi_area_magazzino.tipoMovimento, ");
		sb.append(getQtaMagazzinoCaricoSql());
		sb.append(getQtaMagazzinoScaricoSql());
		sb.append(getQtaMagazzinoCaricoAltroSql());
		sb.append(getQtaMagazzinoScaricoAltroSql());
		sb.append("maga_area_magazzino.annoMovimento,");
		sb.append("IF( maga_tipi_area_magazzino.valoriFatturato=true AND docu_tipi_documento.tipoEntita=1,IF(docu_tipi_documento.notaCreditoEnable =true,maga_righe_magazzino.qta*-1,maga_righe_magazzino.qta),0), ");
		sb.append("IF( maga_tipi_area_magazzino.valoriFatturato=true AND docu_tipi_documento.tipoEntita=0,IF(docu_tipi_documento.notaCreditoEnable =true,maga_righe_magazzino.qta*-1,maga_righe_magazzino.qta),0), ");
		sb.append("IF( maga_tipi_area_magazzino.valoriFatturato=true AND docu_tipi_documento.tipoEntita=1,IF(docu_tipi_documento.notaCreditoEnable =true,maga_righe_magazzino.importoInValutaAziendaTotale*-1,maga_righe_magazzino.importoInValutaAziendaTotale),0), ");
		sb.append("IF( maga_tipi_area_magazzino.valoriFatturato=true AND docu_tipi_documento.tipoEntita=0,IF(docu_tipi_documento.notaCreditoEnable =true,maga_righe_magazzino.importoInValutaAziendaTotale*-1,maga_righe_magazzino.importoInValutaAziendaTotale),0), ");
		sb.append("agente_id, ");
		sb.append("maga_righe_magazzino.percProvvigione, ");
		sb.append("IF( maga_tipi_area_magazzino.valoriFatturato=true AND docu_tipi_documento.tipoEntita=0,IF(docu_tipi_documento.notaCreditoEnable =true,ifnull(round(((maga_righe_magazzino.prezzoNettoBaseProvvigionale*maga_righe_magazzino.qta*-1)*percProvvigione/100),2),0),ifnull(round(maga_righe_magazzino.prezzoNettoBaseProvvigionale*maga_righe_magazzino.qta*percProvvigione/100,2),0)),0), ");
		sb.append("CASE maga_righe_magazzino.tipoOmaggio WHEN 0 then false else true end, ");
		sb.append("maga_area_magazzino.sedeVettore_id, ");
		sb.append("maga_area_magazzino.pesoLordo, ");
		sb.append("maga_area_magazzino.volume, ");
		sb.append("maga_area_magazzino.numeroColli, ");
		sb.append("maga_area_magazzino.aspettoEsteriore, ");
		sb.append("maga_area_magazzino.causaleTrasporto, ");
		sb.append("maga_area_magazzino.tipoPorto, ");
		sb.append("maga_area_magazzino.trasportoCura, ");
		sb.append("round((coalesce(maga_righe_magazzino.prezzoNettoBaseProvvigionale,0) * maga_righe_magazzino.qta),maga_righe_magazzino.numeroDecimaliPrezzo) ");
		sb.append(" from maga_righe_magazzino ");
		sb.append("inner join maga_area_magazzino on maga_area_magazzino.id=maga_righe_magazzino.areaMagazzino_id ");
		sb.append("inner join maga_tipi_area_magazzino on ");
		sb.append("maga_tipi_area_magazzino.id=maga_area_magazzino.tipoAreaMagazzino_id ");
		sb.append("inner join docu_tipi_documento ");
		sb.append("on docu_tipi_documento.id=maga_tipi_area_magazzino.tipoDocumento_id ");
		sb.append("inner join docu_documenti on docu_documenti.id=maga_area_magazzino.documento_id ");
		sb.append("left join maga_listini listinoAlternativo on ");
		sb.append("listinoAlternativo.id=maga_area_magazzino.listinoAlternativo_id ");
		sb.append("left join maga_listini listino on listino.id=maga_area_magazzino.listino_id ");
		sb.append(getLeftJoinDepositoSql());
		sb.append(getWhereTipoArticolo());
		switch (filtro) {
		case AREA_MAGAZZINO:
			sb.append(" and areaMagazzino_id=:areaMagazzino_id");
			break;
		case TIMESTAMP:
			sb.append(" and maga_area_magazzino.timeStamp>=:dateUpdate and (maga_area_magazzino.statoAreaMagazzino=0 or maga_area_magazzino.statoAreaMagazzino=2) ");
			sb.append(" and maga_tipi_area_magazzino.tipoMovimento=");
			sb.append(getTipoMovimentoOriginale());
		default:
			break;
		}
		return sb.toString();
	}

	/**
	 *
	 * @return tipo movimento originale associato al tipoDocumento del queryBuilder
	 */
	public abstract String getTipoMovimentoOriginale();

	/**
	 * Restituisce la stringa sql con il valore da associare alla colonna `tipoMovimento` della tabella
	 * dw_movimentimagazzino.
	 *
	 * @return String
	 */
	public abstract String getTipoMovimentoSql();

	/**
	 *
	 * @return string per filtrare la tipologia di articolo
	 */
	protected String getWhereTipoArticolo() {
		return " where maga_righe_magazzino.TIPO_RIGA<>'C' AND  maga_righe_magazzino.TIPO_RIGA<>'T' AND maga_righe_magazzino.TIPO_RIGA<>'N'";
	}

}
