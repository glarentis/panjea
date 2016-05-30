package it.eurotn.panjea.magazzino.manager.inventari;

import it.eurotn.panjea.magazzino.manager.sqlbuilder.GiacenzaQueryBuilder;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class ImportazioneArticoliInventarioQueryBuilder {

	private final String tableName;

	private String fileToImport;
	private Integer idDeposito;
	private String utente;

	/**
	 * Costruttore.
	 * 
	 * @param fileToImport
	 *            file degli articoli da importare
	 * @param idDeposito
	 *            deposito di riferimento
	 * @param utente
	 *            utente che sta eseguendo l'importazione
	 * 
	 */
	public ImportazioneArticoliInventarioQueryBuilder(final String fileToImport, final Integer idDeposito,
			final String utente) {
		super();
		this.fileToImport = fileToImport;
		this.idDeposito = idDeposito;
		this.utente = utente;
		this.tableName = createTableName();
	}

	/**
	 * Genera casualmente una stringa che rappresenta il nome della tabella temporanea per le importazioni.
	 * 
	 * @return il nome della tabella
	 */
	protected String createTableName() {
		Random random = new Random();
		long r1 = random.nextLong();
		long r2 = random.nextLong();
		String hash1 = Long.toHexString(r1);
		String hash2 = Long.toHexString(r2);
		String randomTableName = "imp_art_inv" + hash1 + hash2;
		return randomTableName;
	}

	/**
	 * Restituisce l'sql per il caricamento degli articoli che non sono presenti nella preparazione inventario.
	 * 
	 * @return sql creato
	 */
	public String getSqlArticoliInventarioMancanti() {
		return "select imp.codiceArticolo from " + tableName
				+ " imp left join maga_articoli art on art.id=imp.idArticolo where art.id is null";
	}

	/**
	 * Sql per la cancellazione della tabella temporanea.
	 * 
	 * @return sql creato
	 */
	public String getSqlDrop() {
		return "DROP TABLE " + tableName;
	}

	/**
	 * Ritorna l'array di String che rappresenta la lista di query sql per inserire gli articoli nell'inventario
	 * preparato.
	 * 
	 * @return La stringa sql da eseguire
	 */
	public Collection<String> getSqlPrepareData() {
		StringBuffer fileContent = new StringBuffer();
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader in = null;
		try {
			// leggo il file per l'sql della giacenza
			is = GiacenzaQueryBuilder.class.getClassLoader().getResourceAsStream(
					"it/eurotn/panjea/magazzino/manager/sqlbuilder/ImportazioneArticoliInventario.sql");
			isr = new InputStreamReader(is);
			in = new BufferedReader(isr);
			String line = "";

			while ((line = in.readLine()) != null) {
				line = replaceParameter(line);
				fileContent.append(line);
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (isr != null) {
					isr.close();
				}
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				throw new RuntimeException("Errore nel chiudere lo stream sulla risorsa", e);
			}
		}
		String[] line = fileContent.toString().split(";");
		List<String> query = new ArrayList<String>(Arrays.asList(line));
		return query;
	}

	/**
	 * Restituisce l'sql per l'aggiornamento degli importi in base al prezzo delle righe delle aree magazzino richieste.
	 * 
	 * @param idAreeMagazzino
	 *            area magazzino di riferimento
	 * @param idDepositoParam
	 *            deposito di riferimento
	 * @return sql generato
	 */
	public String getSqlUpdateAree(List<Integer> idAreeMagazzino, Integer idDepositoParam) {

		StringBuilder sb = new StringBuilder();
		sb.append("update maga_righe_magazzino riga inner join maga_inventari_articolo inv on riga.articolo_id = inv.articolo_id ");
		sb.append("set riga.importoInValutaNetto = inv.importo, ");
		sb.append("    riga.importoInValutaAziendaNetto = round(inv.importo/riga.tassoDiCambioNetto,riga.numeroDecimaliPrezzo), ");
		sb.append("    riga.importoInValuta = inv.importo, ");
		sb.append("    riga.importoInValutaAzienda = round(inv.importo/riga.tassoDiCambio,riga.numeroDecimaliPrezzo), ");
		sb.append("    riga.importoInValutaTotale = inv.importo*riga.qta, ");
		sb.append("    riga.importoInValutaAziendaTotale = round(inv.importo*riga.qta/riga.tassoDiCambioTotale,2) ");
		sb.append("where inv.deposito_id = " + idDepositoParam);
		sb.append("             and inv.importo is not null ");

		sb.append(" and riga.areaMagazzino_id in ( ");
		String idAree = "";
		for (Integer idAreaMaga : idAreeMagazzino) {
			idAree = idAree + ((idAree.isEmpty()) ? "" : ",");
			idAree = idAree + idAreaMaga;
		}
		sb.append(idAree);
		sb.append(" )");

		return sb.toString();
	}

	/**
	 * sostituisce i parametri nel file.
	 * 
	 * @param line
	 *            comando della query
	 * @return comando della query con i parametri settati
	 */
	protected String replaceParameter(String line) {
		line = line.replace("$TBL_IMPORT_ARTICOLI_TMP$", tableName);
		line = line.replace("$FILE_TO_IMPORT$", fileToImport);
		line = line.replace("$IDDEPOSITO$", idDeposito.toString());
		line = line.replace("$user$", utente);
		return line;
	}
}
