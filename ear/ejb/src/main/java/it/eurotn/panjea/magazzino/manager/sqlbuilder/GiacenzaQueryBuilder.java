package it.eurotn.panjea.magazzino.manager.sqlbuilder;

import it.eurotn.util.PanjeaEJBUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * Recupera dal file la query Sql per calcolare la giacenza, sostituisce i
 * parametri e ritorna la query. Per utilizzare questo builder, devono essere
 * chiamati in ordine:<br>
 * <ul>
 * <li><code>getSqlPrepareData()</code></li>
 * <li><code>getSqlResults()</code></li>
 * <li><code>getSqlDrop()</code></li>
 * </ul>
 *
 * @author giangi,Leonardo
 */
public class GiacenzaQueryBuilder {

	/**
	 * Costruttore.
	 */
	public GiacenzaQueryBuilder() {
		super();
	}

	/**
	 * @return alias
	 */
	public List<String> getAlias() {
		String[] aliasArray = new String[] { "idArticolo", "giacenza", "scorta" };

		return Arrays.asList(aliasArray);
	}

	/**
	 * Sql generato per il caricamento della giacenza.
	 *
	 * @param idArticolo
	 *            id articolo
	 * @param idDeposito
	 *            deposito di riferimento
	 * @param data
	 *            data di riferimento della giacenza
	 * @return sql
	 *
	 */
	public String getSqlString(Integer idArticolo, Integer idDeposito, Date data) {

		List<Integer> idArticoli = new ArrayList<Integer>();
		if (idArticolo != null) {
			idArticoli.add(idArticolo);
		}

		return getSqlString(idArticoli, idDeposito, data);
	}

	/**
	 * Sql generato per il caricamento della giacenza.
	 *
	 * @param idArticoli
	 *            id articoli
	 * @param idDeposito
	 *            deposito di riferimento
	 * @param data
	 *            data di riferimento della giacenza
	 * @return sql
	 *
	 */
	public String getSqlString(List<Integer> idArticoli, Integer idDeposito, Date data) {

		StringBuilder sb = new StringBuilder(5000);
		sb.append("select ");
		sb.append("idArticolo as idArticolo, ");
		sb.append("coalesce(max(qtaMagazzino) + sum(qtaCarico) + sum(qtaCaricoAltro) - sum(qtaScarico) - sum(qtaScaricoAltro),0) as giacenza, ");
		sb.append("coalesce(max(scorta),0) as scorta ");
		sb.append("from ");
		sb.append("( ");
		sb.append("   select ");
		sb.append("   invr.articolo_id as idArticolo, ");
		sb.append("   invr.qtaMagazzino as qtaMagazzino, ");
		sb.append("   0 as qtaCarico, ");
		sb.append("   0 as qtaScarico, ");
		sb.append("   0 as qtaCaricoAltro, ");
		sb.append("   0 as qtaScaricoAltro, ");
		sb.append("   depArt.scorta as scorta ");
		sb.append("   from maga_righe_magazzino invr ");
		sb.append("   inner join ");
		sb.append("   ( ");
		sb.append("      select ");
		sb.append("      inv.id, inv.dataRegistrazione,inv.depositoOrigine_id ");
		sb.append("      from maga_area_magazzino inv ");
		sb.append("      where inv.dataRegistrazione<=:dataValorizzazione ");
		sb.append("      and tipoOperazione=0 ");
		if (idDeposito != null) {
			sb.append("      and inv.depositoOrigine_id=:deposito_id ");
		}
		sb.append("      order by inv.dataRegistrazione desc LIMIT 1 ");
		sb.append("   ) ");
		sb.append("   invj on invj.id=invr.areaMagazzino_id ");
		sb.append("   left join maga_articolo_deposito depArt on depArt.articolo_id=invr.articolo_id and depArt.deposito_id=invj.depositoOrigine_id ");
		if (idArticoli != null && !idArticoli.isEmpty()) {
			sb.append("  where invr.articolo_id in (:idArticoli) ");
		}
		sb.append(" group by invr.articolo_id ");

		sb.append("   union ");

		sb.append("   select ");
		sb.append("   mov.articolo_id as idArticolo, ");
		sb.append("   0 as qtaMagazzino , ");
		sb.append("   sum(mov.qtaMagazzinoCarico) as qtaCarico, ");
		sb.append("   sum(mov.qtaMagazzinoScarico) as qtaScarico, ");
		sb.append("   sum(mov.qtaMagazzinoCaricoAltro) as qtaCaricoAltro, ");
		sb.append("   sum(mov.qtaMagazzinoScaricoAltro) as qtaScaricoAltro, ");
		sb.append("   depArt.scorta as scorta ");
		sb.append("   from dw_movimentimagazzino mov ");
		sb.append("   left join maga_articolo_deposito depArt on depArt.articolo_id=mov.articolo_id ");
		sb.append("   where mov.dataRegistrazione>= ");
		sb.append("   ( ");
		sb.append("      select ");
		sb.append("      invu.dataRegistrazione ");
		sb.append("      from maga_area_magazzino invu ");
		sb.append("      where invu.dataRegistrazione<=:dataValorizzazione ");
		sb.append("      and tipoOperazione=0 ");
		if (idDeposito != null) {
			sb.append("      and invu.depositoOrigine_id=:deposito_id ");
		}
		sb.append("      union  ");
		sb.append("      select '2001-01-01 00:00:00'  as dataRegistrazione ");
		sb.append("      order by dataRegistrazione desc LIMIT 1 ");
		sb.append("   ) ");
		sb.append("   and mov.dataRegistrazione<=:dataValorizzazione ");
		if (idDeposito != null) {
			sb.append("   and mov.deposito_id=:deposito_id ");
		}
		if (idArticoli != null && !idArticoli.isEmpty()) {
			sb.append("  and mov.articolo_id in (:idArticoli) ");
		}
		sb.append(" group by mov.articolo_id ");

		sb.append(" union ");

		sb.append("   select ");
		sb.append("   art.id as idArticolo, ");
		sb.append("   0 as qtaMagazzino , ");
		sb.append("   0 as qtaCarico, ");
		sb.append("   0 as qtaScarico, ");
		sb.append("   0 as qtaCaricoAltro, ");
		sb.append("   0 as qtaScaricoAltro, ");
		sb.append("   coalesce(depArt.scorta,0) as scorta ");
		sb.append("   from maga_articoli art left join maga_articolo_deposito depArt on depArt.articolo_id=art.id ");
		if (idDeposito != null) {
			sb.append(" and depArt.deposito_id = :deposito_id ");
		}
		if (idArticoli != null && !idArticoli.isEmpty()) {
			sb.append("  where art.id in (:idArticoli)");
		}

		sb.append(") ");
		sb.append("as val ");
		sb.append("group by idArticolo ");
		sb.append("order by null ");

		// avvaloro i parametri
		String query = sb
				.toString()
				.replaceAll(":deposito_id", String.valueOf(idDeposito))
				.replaceAll(":dataValorizzazione",
						PanjeaEJBUtil.addQuote(new SimpleDateFormat("yyyy-MM-dd").format(data)));
		if (idArticoli != null && !idArticoli.isEmpty()) {
			query = query.replaceAll(":idArticoli", StringUtils.join(idArticoli, ","));
		}

		return query;
	}
}
