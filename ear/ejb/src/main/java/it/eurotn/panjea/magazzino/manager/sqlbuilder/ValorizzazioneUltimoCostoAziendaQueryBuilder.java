package it.eurotn.panjea.magazzino.manager.sqlbuilder;

import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.magazzino.util.ParametriRicercaValorizzazione;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * Recupera dal file la query Sql per calcolare la valorizzazione, sostituisce i parametri e ritorna la query. Per
 * utilizzare questo builder, devono essere chiamati in ordine:<br>
 * <ul>
 * <li><code>getSqlPrepareData()</code></li>
 * <li><code>getSqlResults()</code></li>
 * <li><code>getSqlDrop()</code></li>
 * </ul>
 *
 * @author giangi,Leonardo
 */
public class ValorizzazioneUltimoCostoAziendaQueryBuilder {

	private static Logger logger = Logger.getLogger(ValorizzazioneUltimoCostoAziendaQueryBuilder.class);

	private static final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS ";

	private final DateFormat dataFormat;
	{
		dataFormat = new SimpleDateFormat("yyyy-MM-dd");
	}

	private String tableName = null;

	private Date date = null;

	protected List<DepositoLite> depositi = null;

	private String codiceAzienda = null;

	private boolean consideraArticoliDisabilitati;

	protected List<Integer> idArticoliLite;

	protected ParametriRicercaValorizzazione parametriRicercaValorizzazione;

	/**
	 * @param parametriRicercaValorizzazione
	 *            parametri per la valorizzazione
	 * @param codiceAzienda
	 *            codice azienda
	 */
	public ValorizzazioneUltimoCostoAziendaQueryBuilder(
			final ParametriRicercaValorizzazione parametriRicercaValorizzazione, final String codiceAzienda) {
		super();
		this.date = parametriRicercaValorizzazione.getData();
		this.idArticoliLite = ObjectUtils.defaultIfNull(parametriRicercaValorizzazione.getArticoliLiteId(),
				new ArrayList<Integer>());
		this.depositi = parametriRicercaValorizzazione.getDepositiLite();
		this.codiceAzienda = codiceAzienda;
		this.consideraArticoliDisabilitati = parametriRicercaValorizzazione.isConsideraArticoliDisabilitati();
		this.tableName = createValorizzazioneTableName();
		this.parametriRicercaValorizzazione = parametriRicercaValorizzazione;
	}

	/**
	 * Genera casualmente una stringa che rappresenta il nome della tabella temporanea di valorizzazione.
	 *
	 * @return il nome della tabella
	 */
	private String createValorizzazioneTableName() {
		Random random = new Random();
		long r1 = random.nextLong();
		long r2 = random.nextLong();
		String hash1 = Long.toHexString(r1);
		String hash2 = Long.toHexString(r2);
		String randomTableName = "valorizzazione" + hash1 + hash2;
		return randomTableName;
	}

	/**
	 * Getter di codiceAzienda.
	 *
	 * @return codiceAzienda
	 */
	protected String getCodiceAzienda() {
		return codiceAzienda;
	}

	/**
	 * Getter di date.
	 *
	 * @return date
	 */
	protected Date getDate() {
		return date;
	}

	/**
	 * Restituisce l'sql per eseguire il drop della tabella temporanea di valorizzazione da cui estraggo la giacenza
	 * articolo.
	 *
	 * @return la stringa sql della drop della tabella temporanea
	 */
	private String getDropTableValorizzazione() {
		return DROP_TABLE_SQL + tableName;
	}

	protected String getPathFileSql() {
		return "it/eurotn/panjea/magazzino/manager/sqlbuilder/UltimoCostoAzienda.sql";
	}

	/**
	 * Restituisce la lista di query sql lette dal file Giacenza.sql che calcolano solo la giacenza sulla tabella
	 * temporanea che deve già essere stata creata.
	 *
	 * @return Collection<String>
	 */
	private Collection<String> getSqlCalcolaGiacenza() {
		logger.debug("--> Enter getSqlGiacenza");
		StringBuffer fileContent = new StringBuffer();
		try (InputStream is = GiacenzaQueryBuilder.class.getClassLoader().getResourceAsStream(
				"it/eurotn/panjea/magazzino/manager/sqlbuilder/Giacenza.sql");
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader in = new BufferedReader(isr);) {
			// leggo il file per l'sql della giacenza
			String line = "";

			while ((line = in.readLine()) != null) {
				line = replaceParameter(date, tableName, getWhereSql(), line);
				fileContent.append(line);
			}

		} catch (Exception e) {
			logger.error("--> errore nell'aprire il file contenente la query", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit getSqlGiacenza");

		String[] line = fileContent.toString().split(";");
		List<String> queries = new ArrayList<String>(Arrays.asList(line));
		return queries;
	}

	/**
	 * Restituisce la lista di query sql lette dal file CreateTableWithData.sql che crea e inserisce i dati iniziali
	 * della tabella temporanea per il calcolo della giacenza.
	 *
	 * @return Collection<String>
	 */
	private Collection<String> getSqlCreateTableAndInsertData() {
		logger.debug("--> Enter getCreateTableAndInsertData");
		StringBuffer fileContent = new StringBuffer();
		try (InputStream is = GiacenzaQueryBuilder.class.getClassLoader().getResourceAsStream(
				"it/eurotn/panjea/magazzino/manager/sqlbuilder/CreateTableWithData.sql");
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader in = new BufferedReader(isr);) {
			// leggo il file per l'sql della giacenza
			String line = "";
			while ((line = in.readLine()) != null) {
				line = replaceParameter(date, tableName, getWhereSql(), line);
				fileContent.append(line);
			}
		} catch (Exception e) {
			logger.error("--> errore nell'aprire il file contenente la query", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit getSql");

		String[] line = fileContent.toString().split(";");
		List<String> queries = new ArrayList<String>(Arrays.asList(line));
		return queries;
	}

	/**
	 * Restituisce la query di drop della tabella temporanea generata e gestita da <code>this</code>.
	 *
	 * @return query di drop della tabella temporanea
	 */
	public String getSqlDrop() {
		return getDropTableValorizzazione();
	}

	/**
	 * Ritorna la String che rappresenta la query sql per calcolare la valorizzazione; le query vengono recuperate da 2
	 * file sql<br/>
	 * <ul>
	 * <li>
	 * <code>it.eurotn.panjea.magazzino.manager.sqlbuilder.Giacenza.sql</code></li>
	 * <li>
	 * <code>it.eurotn.panjea.magazzino.manager.sqlbuilder.Valorizzazione.sql</code></li>
	 * </ul>
	 * Uso un array per eseguire ogni query singolarmente; sembra esserci qualche problema nell'eseguirle tutte assieme.
	 *
	 * @return La stringa sql da eseguire
	 */
	public Collection<String> getSqlPrepareData() {
		logger.debug("--> Enter getSql");
		StringBuffer fileContent = new StringBuffer();
		try (InputStream is = GiacenzaQueryBuilder.class.getClassLoader().getResourceAsStream(getPathFileSql());
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader bufferedReader = new BufferedReader(isr);) {
			String line = "";
			while ((line = bufferedReader.readLine()) != null) {
				line = replaceParameter(getDate(), getTableName(), getWhereSql(), line);
				fileContent.append(line);
			}
		} catch (Exception e) {
			logger.error("-->errore nel leggere la riga del file", e);
			throw new RuntimeException(e);
		}

		// creo la tabella e inserisco i dati
		// richiamo la super per avere la query della giacenza che prepara la
		// tabella di valorizzazione
		Collection<String> sqlsGiac = getSqlCreateTableAndInsertData();

		// aggiorno l'attributo numeroPezzi della tabella creata
		sqlsGiac.add(getSQLUpdateNumeroPezziArticolo());

		// aggiungo le query per calcolare la giacenza solo in caso calcola
		// giacenza sia true
		if (parametriRicercaValorizzazione.isCalcolaGiacenza()) {
			sqlsGiac.addAll(getSqlCalcolaGiacenza());
		}

		// se c'è il filtro delle categorie tolgo quelle non richieste
		if (!parametriRicercaValorizzazione.getCategorieSql().isEmpty()) {
			sqlsGiac.add("delete from `" + getTableName() + "` where idCategoria not in ("
					+ parametriRicercaValorizzazione.getCategorieSql() + ");");
		}

		// Se non devo mantenere gli articoli con giacenza = 0 aggiungo la query per toglierli
		if (!parametriRicercaValorizzazione.isConsideraGiacenzaZero()) {
			sqlsGiac.add("delete from `" + getTableName() + "` where giacenza = 0;");
		}
		// // Rimuovo gli articoli non movimentati
		// if (!parametriRicercaValorizzazione.isConsideraMovimentatiZero()) {
		// sqlsGiac.add("delete from `"
		// + getTableName()
		// +
		// "` where qtaCarico is null and qtaScarico is null and qtaCaricoAltro is null and qtaScaricoAltro is null and giacenza = 0;");
		// }

		if (!fileContent.toString().isEmpty()) {
			String[] line = fileContent.toString().split(";");
			sqlsGiac.addAll(Arrays.asList(line));
		}
		logger.debug("--> Exit getSQLUpdateNumeroPezziArticolo");
		return sqlsGiac;
	}

	/**
	 * Restituisce l'sql per eseguire la select della valorizzazione sulla tabella temporanea.
	 *
	 * @return la stringa sql della select per la valorizzazione
	 */
	public String getSqlResults() {
		return new String(
				"select idArticolo,codiceArticolo,descrizioneArticolo,campoLibero,numeroPezzi,idCategoria,codiceCategoria,descrizioneCategoria,codiceFornitoreAbituale,idDeposito,codiceDeposito,descrizioneDeposito,data_inventario as dataInventario,area_inventario_id as inventarioId,qtaInventario,giacenza,qtaCarico as qtaMagazzinoCarico,qtaScarico as qtaMagazzinoScarico,qtaCaricoAltro as qtaMagazzinoCaricoAltro,qtaScaricoAltro as qtaMagazzinoScaricoAltro,costo,area_costoUltimo_id as costoUltimoMovId,importoCarico,importoScarico,if(giacenza > 0,CAST(giacenza*costo as DECIMAL(19,6)),CAST(0 as DECIMAL(19,6))) as valore,scorta from "
						+ getTableName() + " ORDER BY idDeposito,idCategoria,idArticolo;");
	}

	/**
	 * @return sql per aggiornare il valore dell'attributo numeroPezzi degli articoli
	 */
	private String getSQLUpdateNumeroPezziArticolo() {
		StringBuilder sb = new StringBuilder();
		sb.append("update $TABLE_VALORIZZAZIONE$  val inner join maga_attributi_articoli att on val.idArticolo = att.articolo_id ");
		sb.append("                                                                                   inner join maga_tipo_attributo tipoAtt on tipoAtt.id = att.tipoAttributo_id ");
		sb.append("set val.numeroPezzi = att.valore ");
		sb.append("where tipoAtt.codice = 'nrpezzi' ");
		return replaceParameter(Calendar.getInstance().getTime(), getTableName(), "", sb.toString());
	}

	/**
	 * Getter di tableName.
	 *
	 * @return tableName
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * Restituisce l'sql che restringe la ricerca della giacenza all'articolo e al deposito scelti.
	 *
	 * @return la stringa dell'sql per aggiungere la where alla query per il calcolo della giacenza
	 */
	protected String getWhereSql() {
		StringBuilder sb = new StringBuilder();

		// codice azienda
		sb.append(" where art.codiceAzienda='");
		sb.append(getCodiceAzienda());
		sb.append("' ");

		// depositi
		if (depositi != null && !depositi.isEmpty()) {
			sb.append(" and dep.id in ( ");
			String idDeps = "";
			for (DepositoLite depositoLite : depositi) {
				idDeps = idDeps + ((idDeps.isEmpty()) ? "" : ",");
				idDeps = idDeps + depositoLite.getId();
			}
			sb.append(idDeps);
			sb.append(" )");
		}

		// articolo
		if (!idArticoliLite.isEmpty()) {
			if (idArticoliLite.size() == 1) {
				sb.append(" and art.id=");
				sb.append(idArticoliLite.get(0));
			} else {
				sb.append(" and art.id in (");
				sb.append(StringUtils.join(idArticoliLite.iterator(), ","));
				sb.append(")");
			}
		}

		// considera articoli disabilitati
		if (!consideraArticoliDisabilitati) {
			sb.append(" and art.abilitato = true ");
		}

		return sb.toString();
	}

	/**
	 * Sostituisce nel file i segnaposto con i valori utilizzati.
	 *
	 * @param data
	 *            la data da sostituire al relativo segnaposto #DATA_REGISTRAZIONE#
	 * @param valorizzazioneTableName
	 *            il nome della tabella da sostituire al relativo segnaposto #TABLE_VALORIZZAZIONE#
	 * @param whereForGiacenza
	 *            la where da aggiungere al posto di #WHERE_GIACENZA_ARTICOLO_DEPOSITO#
	 * @param line
	 *            la stringa a cui applicare la replace dei segnaposto
	 * @return la stringa con i valori assegnati
	 */
	protected String replaceParameter(Date data, String valorizzazioneTableName, String whereForGiacenza, String line) {
		line = line.replace("$DATA_REGISTRAZIONE$", dataFormat.format(getDate()));
		line = line.replace("$TABLE_VALORIZZAZIONE$", valorizzazioneTableName);
		line = line.replace("$WHERE_GIACENZA_ARTICOLO_DEPOSITO$", whereForGiacenza);
		if (idArticoliLite.size() < 100 && idArticoliLite.size() > 0) {
			line = line.replace("MyIsam", "MEMORY");
		}
		return line;
	}

}
