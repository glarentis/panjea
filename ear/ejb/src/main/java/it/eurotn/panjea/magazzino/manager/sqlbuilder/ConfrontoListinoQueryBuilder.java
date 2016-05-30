/**
 *
 */
package it.eurotn.panjea.magazzino.manager.sqlbuilder;

import it.eurotn.panjea.magazzino.domain.Categoria;
import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.magazzino.domain.ScaglioneListino;
import it.eurotn.panjea.magazzino.util.CategoriaLite;
import it.eurotn.panjea.magazzino.util.ParametriRicercaValorizzazione;
import it.eurotn.panjea.magazzino.util.ParametriRicercaValorizzazione.EModalitaValorizzazione;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaConfrontoListino;
import it.eurotn.panjea.magazzino.util.parametriricerca.TipoConfronto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * @author fattazzo
 * 
 */
public class ConfrontoListinoQueryBuilder {

	private String codiceAzienda;
	private ParametriRicercaConfrontoListino parametriRicercaConfrontoListino;

	private String tableName;

	private List<String> dropTables;

	/**
	 * Costruttore.
	 * 
	 * @param parametriRicercaConfrontoListino
	 *            parametri di ricerca
	 * @param codiceAzienda
	 *            codice azienda
	 */
	public ConfrontoListinoQueryBuilder(final ParametriRicercaConfrontoListino parametriRicercaConfrontoListino,
			final String codiceAzienda) {
		super();
		dropTables = new ArrayList<String>();
		tableName = createConfrontoTableName();
		this.codiceAzienda = codiceAzienda;
		this.parametriRicercaConfrontoListino = parametriRicercaConfrontoListino;
	}

	/**
	 * Genera casualmente una stringa che rappresenta il nome della tabella temporanea di confronto.
	 * 
	 * @return il nome della tabella
	 */
	private String createConfrontoTableName() {
		Random random = new Random();
		long r1 = random.nextLong();
		long r2 = random.nextLong();
		String hash1 = Long.toHexString(r1);
		String hash2 = Long.toHexString(r2);
		String randomTableName = "confrontolistini" + hash1 + hash2;
		return randomTableName;
	}

	/**
	 * Restituisce la query per l'inserimento del confronto basato sul costo aqzienda.
	 * 
	 * @param numeroConfronto
	 *            numero di confronto
	 * @return SQL
	 */
	private Collection<String> getSQLConfrontoCostoAzienda(int numeroConfronto) {
		List<String> sqls = new ArrayList<String>();

		ParametriRicercaValorizzazione parametriValorizzazione = new ParametriRicercaValorizzazione();
		parametriValorizzazione.setCalcolaGiacenza(false);
		List<Categoria> categorie = new ArrayList<Categoria>();
		if (parametriRicercaConfrontoListino.getCategorie() != null) {
			for (CategoriaLite categoria : parametriRicercaConfrontoListino.getCategorie()) {
				categorie.add(categoria.createCategoria());
			}
		}
		parametriValorizzazione.setCategorie(categorie);
		parametriValorizzazione.setTutteCategorie(parametriRicercaConfrontoListino.isTutteCategorie());
		parametriValorizzazione.setConsideraArticoliDisabilitati(true);
		parametriValorizzazione.setData(Calendar.getInstance().getTime());
		parametriValorizzazione.setModalitaValorizzazione(EModalitaValorizzazione.ULTIMO_COSTO_AZIENDA);
		ValorizzazioneUltimoCostoAziendaQueryBuilder valorizzazioneQueryBuilder = new ValorizzazioneUltimoCostoAziendaQueryBuilder(
				parametriValorizzazione, codiceAzienda);

		// creo la tabella di valorizzazione all'ultimo costo azienda
		sqls.addAll(valorizzazioneQueryBuilder.getSqlPrepareData());

		// inserisco i valori
		StringBuilder sb = new StringBuilder();
		sb.append("update ");
		sb.append(tableName);
		sb.append(" conf inner join ");
		sb.append(valorizzazioneQueryBuilder.getTableName());
		sb.append(" as val on val.idArticolo = conf.idArticolo ");
		sb.append(" inner join maga_articoli art on art.id = val.idArticolo ");
		sb.append("set conf.prezzoConfronto" + numeroConfronto + " = val.costo, ");
		sb.append("        conf.numeroDecimaliPrezzo" + numeroConfronto + " = art.numeroDecimaliPrezzo;");
		sqls.add(sb.toString());

		dropTables.add(valorizzazioneQueryBuilder.getTableName());

		return sqls;
	}

	/**
	 * Restituisce la query per l'inserimento del confronto basato sul listino.<br>
	 * NB! non eseguo il filtro sulle eventuali categorie richieste perchè tanto la query iniziale non le ha inserite,
	 * per cui la on della join mi esclude tutti gli articoli del listino non inseriti inizialmente a causa del filtro.
	 * 
	 * @param listino
	 *            listino
	 * @param numeroConfronto
	 *            numero di confronto
	 * @return SQL
	 */
	private String getSQLConfrontoListino(Listino listino, int numeroConfronto) {
		StringBuilder sb = new StringBuilder();
		sb.append("update ");
		sb.append(tableName);
		sb.append(" conf inner join ( select riga.articolo_id,sca.prezzo,riga.numeroDecimaliPrezzo from maga_righe_listini riga ");
		sb.append("                                  inner join maga_scaglioni_listini sca on riga.id = sca.rigaListino_id and sca.quantita=");
		sb.append(ScaglioneListino.MAX_SCAGLIONE);
		sb.append("		                           inner join (select id from maga_versioni_listino where listino_id = ");
		sb.append(listino.getId());
		sb.append(" and dataVigore <= '");
		sb.append(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));
		sb.append("' order by dataVigore desc LIMIT 1) vers on vers.id = riga.versioneListino_id) as righe on righe.articolo_id = conf.idArticolo ");
		sb.append("set conf.prezzoConfronto" + numeroConfronto + " = righe.prezzo, ");
		sb.append("        conf.numeroDecimaliPrezzo" + numeroConfronto + " = righe.numeroDecimaliPrezzo; ");

		return sb.toString();
	}

	/**
	 * Restituisce l'SQL che crea la tabella temporanea in base al numero di confronti richiesti.
	 * 
	 * @return query di creazione
	 */
	private String getSQLCreateTable() {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE ");
		sb.append(tableName);
		sb.append("( idArticolo int(11) DEFAULT NULL, ");
		sb.append("   codiceArticolo varchar(30) DEFAULT NULL, ");
		sb.append("   descrizioneArticolo varchar(255) DEFAULT NULL, ");
		sb.append("   idCategoria int(11) DEFAULT NULL, ");
		sb.append("   codiceCategoria varchar(255) DEFAULT NULL, ");
		sb.append("   descrizioneCategoria varchar(255) DEFAULT NULL, ");
		sb.append("   prezzoBase DECIMAL(19,6) DEFAULT 0, ");
		sb.append("   numeroDecimaliPrezzoBase int, ");
		for (int i = 1; i < parametriRicercaConfrontoListino.getConfronti().size() + 1; i++) {
			sb.append("   descrizioneConfronto" + i + " varchar(30) DEFAULT NULL, ");
			sb.append("   prezzoConfronto" + i + " DECIMAL(19,6) DEFAULT 0, ");
			sb.append("   numeroDecimaliPrezzo" + i + " int, ");
		}
		sb.append("   PRIMARY KEY (idArticolo) ");
		sb.append(") ENGINE=MyIsam DEFAULT CHARSET=latin1");
		return sb.toString();
	}

	/**
	 * Restituisce la query di drop della tabella temporanea generata.
	 * 
	 * @return query di drop della tabella temporanea
	 */
	public Collection<String> getSQLDropTable() {
		dropTables.add(tableName);
		List<String> sqls = new ArrayList<String>();
		for (String table : dropTables) {
			sqls.add("DROP TABLE IF EXISTS " + table + "; ");
		}
		return sqls;
	}

	/**
	 * Restituisce l'SQL per l'inserimento dei dati del confronto base.
	 * 
	 * @return sql creato
	 */
	private Collection<String> getSQLInsertConfrontoBase() {
		List<String> sqls = new ArrayList<String>();

		StringBuilder sb = null;
		switch (parametriRicercaConfrontoListino.getConfrontoBase().getConfronto()) {
		case ULTIMO_COSTO_AZIENDALE:
			ParametriRicercaValorizzazione parametriValorizzazione = new ParametriRicercaValorizzazione();
			parametriValorizzazione.setCalcolaGiacenza(false);
			List<Categoria> categorie = new ArrayList<Categoria>();
			if (parametriRicercaConfrontoListino.getCategorie() != null) {
				for (CategoriaLite categoria : parametriRicercaConfrontoListino.getCategorie()) {
					categorie.add(categoria.createCategoria());
				}
			}
			parametriValorizzazione.setCategorie(categorie);
			parametriValorizzazione.setTutteCategorie(parametriRicercaConfrontoListino.isTutteCategorie());
			parametriValorizzazione.setConsideraArticoliDisabilitati(true);
			parametriValorizzazione.setData(Calendar.getInstance().getTime());
			parametriValorizzazione.setModalitaValorizzazione(EModalitaValorizzazione.ULTIMO_COSTO_AZIENDA);
			ValorizzazioneUltimoCostoAziendaQueryBuilder valorizzazioneQueryBuilder = new ValorizzazioneUltimoCostoAziendaQueryBuilder(
					parametriValorizzazione, codiceAzienda);

			// creo la tabella di valorizzazione all'ultimo costo azienda
			sqls.addAll(valorizzazioneQueryBuilder.getSqlPrepareData());

			// inserisco i valori
			sb = new StringBuilder();
			sb.append("INSERT INTO ");
			sb.append(tableName);
			sb.append(" (idArticolo,codiceArticolo,descrizioneArticolo,idCategoria,codiceCategoria,descrizioneCategoria,prezzoBase,numeroDecimaliPrezzoBase) ");
			sb.append(" select distinct val.idArticolo,val.codiceArticolo,val.descrizioneArticolo,val.idCategoria,val.codiceCategoria,val.descrizioneCategoria,val.costo,art.numeroDecimaliPrezzo from "
					+ valorizzazioneQueryBuilder.getTableName()
					+ " val inner join maga_articoli art on art.id = val.idArticolo;");
			sqls.add(sb.toString());

			dropTables.add(valorizzazioneQueryBuilder.getTableName());
			break;
		default:
			sb = new StringBuilder();
			sb.append("INSERT INTO ");
			sb.append(tableName);
			sb.append(" (idArticolo,codiceArticolo,descrizioneArticolo,idCategoria,codiceCategoria,descrizioneCategoria,prezzoBase,numeroDecimaliPrezzoBase) ");
			sb.append("select art.id,art.codice,art.descrizioneLinguaAziendale,cat.id,cat.codice,cat.descrizioneLinguaAziendale,sca.prezzo,riga.numeroDecimaliPrezzo ");
			sb.append("from maga_righe_listini riga inner join maga_articoli art on art.id = riga.articolo_id ");
			sb.append("                       inner join maga_scaglioni_listini sca on riga.id = sca.rigaListino_id and sca.quantita=");
			sb.append(ScaglioneListino.MAX_SCAGLIONE);
			sb.append("					    inner join maga_categorie cat on cat.id = art.categoria_id ");
			sb.append("						inner join (select id from maga_versioni_listino where listino_id = ");
			sb.append(parametriRicercaConfrontoListino.getConfrontoBase().getListino().getId());
			sb.append(" and dataVigore <= '");
			sb.append(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));
			sb.append("' order by dataVigore desc LIMIT 1) vers on vers.id = riga.versioneListino_id");
			if (!parametriRicercaConfrontoListino.isTutteCategorie()) {
				sb.append(" where cat.id in (");
				sb.append(parametriRicercaConfrontoListino.getCategorieSql());
				sb.append(")");
			}
			sb.append(";");
			sqls.add(sb.toString());
			break;
		}

		return sqls;
	}

	/**
	 * Restituisce l'SQL che crea la tabella temporanea in base al numero di confronti richiesti.
	 * 
	 * @return query di creazione
	 */
	public Collection<String> getSqlInsertData() {
		List<String> sqls = new ArrayList<String>();
		sqls.addAll(getSQLInsertConfrontoBase());

		int numeroConfronto = 1;
		for (TipoConfronto tipoConfronto : parametriRicercaConfrontoListino.getConfronti()) {
			switch (tipoConfronto.getConfronto()) {
			case LISTINO:
				sqls.add(getSQLConfrontoListino(tipoConfronto.getListino(), numeroConfronto));
				break;
			default:
				sqls.addAll(getSQLConfrontoCostoAzienda(numeroConfronto));
				break;
			}
			numeroConfronto++;
		}
		sqls.addAll(getSQLUpdateDescrizioneConfronti());
		return sqls;
	}

	/**
	 * Restituisce l'SQL che crea la tabella temporanea in base al numero di confronti richiesti.
	 * 
	 * @return query di creazione
	 */
	public Collection<String> getSqlPrepareData() {
		List<String> sqls = new ArrayList<String>();
		sqls.add(getSQLCreateTable());
		return sqls;
	}

	/**
	 * Restituisce l'SQL per creare le righe confronto.
	 * 
	 * @return SQL creato
	 */
	public String getSQLResults() {

		// sql per ottenere il numero massimo di decimali prezzo presenti in tutti i confronti
		StringBuilder sbDecimali = new StringBuilder();
		sbDecimali.append("select greatest(coalesce(max(conf.numeroDecimaliPrezzoBase),0) ");
		for (int i = 1; i < parametriRicercaConfrontoListino.getConfronti().size() + 1; i++) {
			sbDecimali.append(",coalesce(max(conf.numeroDecimaliPrezzo" + i + "),0)");
		}
		sbDecimali.append(") from ");
		sbDecimali.append(tableName);
		sbDecimali.append(" conf ");

		StringBuilder sb = new StringBuilder();
		sb.append("select idArticolo as idArticolo, ");
		sb.append("       	  codiceArticolo as codiceArticolo, ");
		sb.append("       	  descrizioneArticolo as descrizioneArticolo,idCategoria as idCategoria, ");
		sb.append("       	  codiceCategoria as codiceCategoria, ");
		sb.append("       	  descrizioneCategoria as descrizioneCategoria, ");
		sb.append("       	  prezzoBase as prezzoBase, ");
		for (int i = 1; i < parametriRicercaConfrontoListino.getConfronti().size() + 1; i++) {
			sb.append("   descrizioneConfronto" + i + " as descrizioneConfronto" + i + ", ");
			sb.append("   prezzoConfronto" + i + " as prezzoConfronto" + i + ", ");
		}
		sb.append("(" + sbDecimali.toString() + ") as numeroDecimaliPrezzo, ");
		sb.append(parametriRicercaConfrontoListino.getConfronti().size() + " as numeroConfronti ");
		sb.append("from ");
		sb.append(tableName);
		sb.append(" conf ");
		if (parametriRicercaConfrontoListino.getEntita() != null) {
			// aggiungo la join per prendere solo gli articoli dell'entità richiesta
			sb.append("inner join maga_codici_articolo_entita codEnt on codEnt.articolo_id = conf.idArticolo ");
			sb.append("where codEnt.entita_id = ");
			sb.append(parametriRicercaConfrontoListino.getEntita().getId());
		}
		return sb.toString();
	}

	/**
	 * Restituisce l'SQL per l'aggiornamento della descrizione dei confronti.
	 * 
	 * @return SQL creato
	 */
	private Collection<String> getSQLUpdateDescrizioneConfronti() {
		List<String> sqls = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		for (int i = 1; i < parametriRicercaConfrontoListino.getConfronti().size() + 1; i++) {
			sb = new StringBuilder();
			sb.append("update ");
			sb.append(tableName);
			sb.append(" set descrizioneConfronto" + i + " = ");
			switch (parametriRicercaConfrontoListino.getConfronti().get(i - 1).getConfronto()) {
			case ULTIMO_COSTO_AZIENDALE:
				sb.append("'UCA';");
				break;
			default:
				sb.append("'");
				sb.append(parametriRicercaConfrontoListino.getConfronti().get(i - 1).getListino().getCodice());
				sb.append("';");
				break;
			}
			sqls.add(sb.toString());
		}

		return sqls;
	}

}
