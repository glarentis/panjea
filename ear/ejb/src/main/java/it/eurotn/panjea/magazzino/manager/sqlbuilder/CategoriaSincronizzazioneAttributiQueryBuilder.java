package it.eurotn.panjea.magazzino.manager.sqlbuilder;

import it.eurotn.panjea.magazzino.domain.FormulaTrasformazione;

/**
 * Crea le stringhe per le query di sincronizzazione degli attributi.
 *
 * @author giangi
 *
 */
public class CategoriaSincronizzazioneAttributiQueryBuilder {

	/**
	 * Stringa per la query di aggiornamento degli attributi nelle sotto categorie.
	 *
	 * @param valoreChanged
	 *            valoreChanged
	 * @param insInRigaChanged
	 *            insInRigaChanged
	 * @param ordineChanged
	 *            ordineChanged
	 * @param rigaChanged
	 *            rigaChanged
	 * @param stampaChanged
	 *            stampaChanged
	 * @param consSottoCategoriaChanged
	 *            consSottoCategoriaChanged
	 * @param tipoAggChanged
	 *            tipoAggChanged
	 * @param formulaChanged
	 *            formulaChanged
	 * @param obbligatorioChanged
	 *            obbligatorioChanged
	 * @param updatableChanged
	 *            updatableChanged
	 * @return hql
	 */
	public String getHQLUpdateAttributiCategoria(boolean valoreChanged, boolean insInRigaChanged,
			boolean ordineChanged, boolean rigaChanged, boolean stampaChanged, boolean consSottoCategoriaChanged,
			boolean tipoAggChanged, boolean formulaChanged, boolean obbligatorioChanged, boolean updatableChanged) {

		String separator = "";

		StringBuilder sb = new StringBuilder();
		sb.append("update AttributoCategoria ac ");
		sb.append("set ");
		if (valoreChanged) {
			sb.append("        ac.valore = :valore ");
			separator = ", ";
		}
		if (insInRigaChanged) {
			sb.append(separator);
			sb.append("		ac.inserimentoInRiga = :inserimentoInRiga ");
			separator = ", ";
		}
		if (ordineChanged) {
			sb.append(separator);
			sb.append("		ac.ordine = :ordine ");
			separator = ", ";
		}
		if (rigaChanged) {
			sb.append(separator);
			sb.append("		ac.riga = :riga ");
			separator = ", ";
		}
		if (stampaChanged) {
			sb.append(separator);
			sb.append("		ac.stampa = :stampa ");
			separator = ", ";
		}
		if (consSottoCategoriaChanged) {
			sb.append(separator);
			sb.append("		ac.consideraSottoCategorie = :consideraSottoCategorie ");
			separator = ", ";
		}
		if (tipoAggChanged) {
			sb.append(separator);
			sb.append("		ac.tipoAggiornamento = :tipoAggiornamento ");
			separator = ", ";
		}
		if (formulaChanged) {
			sb.append(separator);
			sb.append("		ac.formula = :formula ");
			separator = ", ";
		}
		if (obbligatorioChanged) {
			sb.append(separator);
			sb.append("		ac.obbligatorio = :obbligatorio ");
			separator = ", ";
		}
		if (updatableChanged) {
			sb.append(separator);
			sb.append("		ac.updatable = :updatable ");
		}
		sb.append(" where ac.categoria in (:categorie) and ac.tipoAttributo = :tipoAttributo ");
		return sb.toString();
	}

	/**
	 * @return String per la query di cancellazione di tutti gli attributi articolo non più presenti negli attributi
	 *         della categoria
	 */
	public String getSqlDeleteAttributi() {
		// carico la lista di attributi da cancellare
		StringBuffer sb = new StringBuffer();
		sb.append("delete aa from maga_attributi_articoli aa left join maga_attributi_categorie ac on aa.tipoAttributo_id = ac.tipoAttributo_id and ac.categoria_id = ?1 ");
		sb.append("										  inner join maga_articoli art on aa.articolo_id = art.id ");
		sb.append("where art.categoria_id= ?1 and ac.id is null ");
		return sb.toString();
	}

	/**
	 *
	 * @return Stringa per la query di inserimento
	 */
	public String getSqlInsertAttributiArticoliMancanti() {
		StringBuffer sb = new StringBuffer();
		sb.append("INSERT INTO maga_attributi_articoli (id,version,userInsert,valore,articolo_id,tipoAttributo_id,timeStamp,inserimentoInRiga,ordine,stampa,riga,formula_id,obbligatorio,updatable,ricalcolaInEvasione) ");
		sb.append("select null, ");
		sb.append("	  0, ");
		sb.append("	  ?2, ");
		sb.append("	  ac.valore, ");
		sb.append("	  art.id, ");
		sb.append("	  ac.tipoAttributo_id, ");
		sb.append("	  UNIX_TIMESTAMP(Now()), ");
		sb.append("	  ac.inserimentoInRiga, ");
		sb.append("	  ac.ordine, ");
		sb.append("	  ac.stampa, ");
		sb.append("	  ac.riga, ");
		sb.append("	  ac.formula_id, ");
		sb.append("	  true, ");
		sb.append("	  ac.updatable, ");
		sb.append("     ac.ricalcolaInEvasione ");
		sb.append("from maga_articoli art left join maga_attributi_categorie ac on ac.categoria_id = art.categoria_id ");
		sb.append("				   left join maga_attributi_articoli aa on art.id=aa.articolo_id and ac.tipoAttributo_id = aa.tipoAttributo_id ");
		sb.append("where art.categoria_id= ?1 and aa.id is null ");
		return sb.toString();
	}

	/**
	 *
	 * @return Stringa per la query di inserimento degli attributi nella categoria
	 */
	public String getSqlInsertAttributiCategoria() {
		StringBuilder sb = new StringBuilder();
		sb.append("insert into maga_attributi_categorie ( id, timeStamp, userInsert, version, inserimentoInRiga, ordine, riga, stampa, consideraSottoCategorie, tipoAggiornamento, valore, formula_id, tipoAttributo_id, categoria_id,ricalcolaInEvasione,updatable ) ");
		sb.append("(select null,ac.timeStamp,ac.userInsert,0,ac.inserimentoInRiga,ac.ordine,ac.riga,ac.stampa,ac.consideraSottoCategorie,ac.tipoAggiornamento,ac.valore,ac.formula_id,ac.tipoAttributo_id,?1,true,ac.updatable from maga_attributi_categorie ac where ac.id = ?2 and (select count(ac2.id) from maga_attributi_categorie ac2 where ac2.tipoAttributo_id = ac.tipoAttributo_id and ac2.categoria_id = ?1) = 0)");
		return sb.toString();
	}

	/**
	 *
	 * @return Stringa per la query di update degli 'attributi
	 */
	public String getSQLOverrideAttributiArticoli() {
		StringBuilder sb = new StringBuilder();
		sb.append("update maga_attributi_articoli aa inner join maga_articoli art on aa.articolo_id = art.id ");
		sb.append("	     				    inner join maga_attributi_categorie ac on ac.tipoAttributo_id = aa.tipoAttributo_id and ac.categoria_id = ?1 ");
		sb.append("set aa.inserimentoInRiga=ac.inserimentoInRiga, ");
		sb.append("    aa.ordine=ac.ordine, ");
		sb.append("    aa.riga=ac.riga, ");
		sb.append("    aa.stampa=ac.stampa, ");
		sb.append("    aa.formula_id=ac.formula_id, ");
		sb.append("    aa.obbligatorio=ac.obbligatorio, ");
		sb.append("    aa.ricalcolaInEvasione=ac.ricalcolaInEvasione, ");
		sb.append("    aa.updatable=ac.updatable ");
		sb.append("where art.categoria_id = ?1 and ac.tipoAttributo_id = ?2 ");
		return sb.toString();
	}

	/**
	 *
	 * @return Stringa per la query di update dell'attributo Formula
	 * @param formula
	 *            formula
	 */
	public String getSQLOverrideFormulaAttributiArticoli(FormulaTrasformazione formula) {
		StringBuilder sb = new StringBuilder();
		sb.append("update maga_attributi_articoli aa inner join maga_articoli art on aa.articolo_id = art.id ");
		sb.append("	     				    inner join maga_attributi_categorie ac on ac.tipoAttributo_id = aa.tipoAttributo_id and ac.categoria_id = ?1 ");
		sb.append("set aa.formula_id=ac.formula_id ");
		sb.append("where art.categoria_id = ?1 and ac.tipoAttributo_id = ?2 ");
		if (formula == null) {
			sb.append(" and aa.formula_id is null ");
		} else {
			sb.append(" and aa.formula_id = ?3 ");
		}
		return sb.toString();
	}

	/**
	 *
	 * @return Stringa per la query di update dell'attributo inserimentoInRiga
	 */
	public String getSQLOverrideInsInRigaAttributiArticoli() {
		StringBuilder sb = new StringBuilder();
		sb.append("update maga_attributi_articoli aa inner join maga_articoli art on aa.articolo_id = art.id ");
		sb.append("	     				    inner join maga_attributi_categorie ac on ac.tipoAttributo_id = aa.tipoAttributo_id and ac.categoria_id = ?1 ");
		sb.append(" set aa.inserimentoInRiga=ac.inserimentoInRiga ");
		sb.append("where art.categoria_id = ?1 and ac.tipoAttributo_id = ?2 and aa.inserimentoInRiga = ?3 ");
		return sb.toString();
	}

	/**
	 *
	 * @return Stringa per la query di update dell'attributo Obbligatorio
	 */
	public String getSQLOverrideObbligatorioAttributiArticoli() {
		StringBuilder sb = new StringBuilder();
		sb.append("update maga_attributi_articoli aa inner join maga_articoli art on aa.articolo_id = art.id ");
		sb.append("	     				    inner join maga_attributi_categorie ac on ac.tipoAttributo_id = aa.tipoAttributo_id and ac.categoria_id = ?1 ");
		sb.append(" set aa.obbligatorio=ac.obbligatorio ");
		sb.append("where art.categoria_id = ?1 and ac.tipoAttributo_id = ?2 and aa.obbligatorio = ?3 ");
		return sb.toString();
	}

	/**
	 *
	 * @return Stringa per la query di update dell'attributo Ordine
	 */
	public String getSQLOverrideOrdineAttributiArticoli() {
		StringBuilder sb = new StringBuilder();
		sb.append("update maga_attributi_articoli aa inner join maga_articoli art on aa.articolo_id = art.id ");
		sb.append("	     				    inner join maga_attributi_categorie ac on ac.tipoAttributo_id = aa.tipoAttributo_id and ac.categoria_id = ?1 ");
		sb.append(" set aa.ordine=ac.ordine ");
		sb.append("where art.categoria_id = ?1 and ac.tipoAttributo_id = ?2 and aa.ordine = ?3 ");
		return sb.toString();
	}

	/**
	 *
	 * @return Stringa per la query di update dell'attributo Riga
	 */
	public String getSQLOverrideRigaAttributiArticoli() {
		StringBuilder sb = new StringBuilder();
		sb.append("update maga_attributi_articoli aa inner join maga_articoli art on aa.articolo_id = art.id ");
		sb.append("	     				    inner join maga_attributi_categorie ac on ac.tipoAttributo_id = aa.tipoAttributo_id and ac.categoria_id = ?1 ");
		sb.append(" set aa.riga=ac.riga ");
		sb.append("where art.categoria_id = ?1 and ac.tipoAttributo_id = ?2 and aa.riga = ?3 ");
		return sb.toString();
	}

	/**
	 *
	 * @return Stringa per la query di update dell'attributo Stampa
	 */
	public String getSQLOverrideStampaAttributiArticoli() {
		StringBuilder sb = new StringBuilder();
		sb.append("update maga_attributi_articoli aa inner join maga_articoli art on aa.articolo_id = art.id ");
		sb.append("	     				    inner join maga_attributi_categorie ac on ac.tipoAttributo_id = aa.tipoAttributo_id and ac.categoria_id = ?1 ");
		sb.append(" set aa.stampa=ac.stampa ");
		sb.append("where art.categoria_id = ?1 and ac.tipoAttributo_id = ?2 and aa.stampa = ?3 ");
		return sb.toString();
	}

	/**
	 *
	 * @return Stringa per la query di update dell'attributo Valore
	 */
	public String getSQLOverrideUpdatableAttributiArticoli() {
		StringBuilder sb = new StringBuilder();
		sb.append("update maga_attributi_articoli aa inner join maga_articoli art on aa.articolo_id = art.id ");
		sb.append("	     				    inner join maga_attributi_categorie ac on ac.tipoAttributo_id = aa.tipoAttributo_id and ac.categoria_id = ?1 ");
		sb.append(" set aa.updatable=ac.updatable ");
		sb.append("where art.categoria_id = ?1 and ac.tipoAttributo_id = ?2 and aa.inserimentoInRiga = ?3 ");
		return sb.toString();
	}

	/**
	 *
	 * @return Stringa per la query di update dell'attributo Valore
	 */
	public String getSQLOverrideValoreAttributiArticoli() {
		StringBuilder sb = new StringBuilder();
		sb.append("update maga_attributi_articoli aa inner join maga_articoli art on aa.articolo_id = art.id ");
		sb.append("	     				    inner join maga_attributi_categorie ac on ac.tipoAttributo_id = aa.tipoAttributo_id and ac.categoria_id = ?1 ");
		sb.append("set aa.valore= ac.valore ");
		sb.append("where art.categoria_id = ?1 and ac.tipoAttributo_id = ?2 and aa.valore = ?3 ");
		return sb.toString();
	}

	/**
	 *
	 * @return Stringa per la query di update dell'attributo ricalcola in evasione
	 */
	public String getSQLRicalcolaInEvasioneAttributiArticoli() {
		StringBuilder sb = new StringBuilder();
		sb.append("update maga_attributi_articoli aa inner join maga_articoli art on aa.articolo_id = art.id ");
		sb.append("	     				    inner join maga_attributi_categorie ac on ac.tipoAttributo_id = aa.tipoAttributo_id and ac.categoria_id = ?1 ");
		sb.append(" set aa.ricalcolaInEvasione=ac.ricalcolaInEvasione ");
		sb.append("where art.categoria_id = ?1 and ac.tipoAttributo_id = ?2 and aa.ricalcolaInEvasione = ?3 ");
		return sb.toString();
	}
}
