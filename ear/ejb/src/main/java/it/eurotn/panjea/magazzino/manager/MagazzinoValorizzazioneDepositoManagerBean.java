package it.eurotn.panjea.magazzino.manager;

import it.eurotn.panjea.magazzino.domain.Categoria;
import it.eurotn.panjea.magazzino.manager.interfaces.MagazzinoValorizzazioneDepositoManager;
import it.eurotn.panjea.magazzino.util.ParametriRicercaValorizzazione;
import it.eurotn.panjea.magazzino.util.ValorizzazioneArticolo;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;
import it.eurotn.util.PanjeaEJBUtil;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.MagazzinoValorizzazioneDepositoManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.MagazzinoValorizzazioneDepositoManager")
public class MagazzinoValorizzazioneDepositoManagerBean implements MagazzinoValorizzazioneDepositoManager {
	@EJB
	private PanjeaDAO panjeaDAO;
	@Resource
	private SessionContext sessionContext;

	/**
	 *
	 * @param parametriRicercaValorizzazione
	 *            parametri per il calcolo della valorizzazione
	 * @param idDeposito
	 *            deposito da valorizzare
	 * @return righe valorizzazione
	 */
	@SuppressWarnings("unchecked")
	public List<ValorizzazioneArticolo> caricaValorizzazione(
			ParametriRicercaValorizzazione parametriRicercaValorizzazione, int idDeposito) {
		SQLQuery query = panjeaDAO.prepareNativeQuery(getSqlString(parametriRicercaValorizzazione, idDeposito));
		// query.setParameter("deposito_id", idDeposito);
		// query.setParameter("dataValorizzazione", parametriRicercaValorizzazione.getData());
		query.setResultTransformer(Transformers.aliasToBean(ValorizzazioneArticolo.class));

		query.addScalar("idArticolo");
		query.addScalar("codiceArticolo");
		query.addScalar("descrizioneArticolo");
		query.addScalar("idCategoria");
		query.addScalar("codiceCategoria");
		query.addScalar("descrizioneCategoria", Hibernate.STRING);
		query.addScalar("codiceFornitoreAbituale", Hibernate.INTEGER);
		query.addScalar("idDeposito", Hibernate.INTEGER);
		query.addScalar("codiceDeposito");
		query.addScalar("descrizioneDeposito");
		query.addScalar("dataInventario");
		query.addScalar("qtaInventario");
		query.addScalar("qtaMagazzinoCarico");
		query.addScalar("qtaMagazzinoScarico");
		query.addScalar("qtaMagazzinoCaricoAltro");
		query.addScalar("qtaMagazzinoScaricoAltro");
		query.addScalar("costo", Hibernate.BIG_DECIMAL);
		query.addScalar("scorta");
		List<ValorizzazioneArticolo> valorizzazioniArticoli = null;
		try {
			valorizzazioniArticoli = query.list();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return valorizzazioniArticoli;
	}

	protected String getCostoAnagrafica() {
		return "0 as costoVal, ";
	}

	protected String getCostoFinale(ParametriRicercaValorizzazione parametri) {
		return "coalesce(costoVal,costoInventario) as costo, ";
	}

	protected String getCostoInventario() {
		return "0 as costoVal, ";
	}

	protected String getCostoMovimentato() {
		return "0 as costoVal, ";
	}

	/**
	 *
	 * @return codice azienda loggata
	 */
	private JecPrincipal getPrincipal() {
		return ((JecPrincipal) sessionContext.getCallerPrincipal());
	}

	/**
	 * Sql generato per il caricamento della valorizzazione.
	 *
	 * @param parametriRicercaValorizzazione
	 *            parametri
	 * @param idDeposito
	 *            deposito di riferimento
	 * @return sql
	 *
	 */
	public String getSqlString(ParametriRicercaValorizzazione parametriRicercaValorizzazione, int idDeposito) {
		List<Integer> articoli = parametriRicercaValorizzazione.getArticoliLiteId();
		String filtroArticoli = "";
		String filtroCategorie = "";
		if (!articoli.isEmpty()) {
			filtroArticoli = " IN (" + StringUtils.join(articoli, ",") + ")";
		}
		if (!parametriRicercaValorizzazione.getCategorie().isEmpty()) {
			StringBuilder buf = new StringBuilder(256);
			buf.append(" IN (");
			String sep = "";
			for (Categoria cat : parametriRicercaValorizzazione.getCategorie()) {
				buf.append(sep);
				buf.append(cat.getId());
				sep = ",";
			}
			buf.append(" )");
			filtroCategorie = buf.toString();
		}

		StringBuilder sb = new StringBuilder(5000);
		sb.append("select ");
		sb.append("idArticolo, ");
		sb.append("max(codArticolo) as codiceArticolo, ");
		sb.append("max(descrizioneArticolo) as descrizioneArticolo, ");
		sb.append("max(idCategoria) as idCategoria, ");
		sb.append("max(codiceCategoria) as codiceCategoria, ");
		sb.append("max(descrizioneCategoria) as descrizioneCategoria, ");
		sb.append("max(idDeposito) as idDeposito, ");
		sb.append("max(codiceDeposito) as codiceDeposito, ");
		sb.append("max(descrizioneDeposito) as descrizioneDeposito, ");
		sb.append("max(codiceFornitoreAbituale) as codiceFornitoreAbituale, ");
		sb.append("max(dataInventario) as dataInventario, ");
		sb.append("max(qtaMagazzino) as qtaInventario, ");
		sb.append("sum(qtaCarico) as qtaMagazzinoCarico, ");
		sb.append("sum(qtaScarico) as qtaMagazzinoScarico, ");
		sb.append(getCostoFinale(parametriRicercaValorizzazione));
		sb.append("sum(qtaCaricoAltro) as qtaMagazzinoCaricoAltro, ");
		sb.append("sum(qtaScaricoAltro) as qtaMagazzinoScaricoAltro, ");
		sb.append("max(scorta) as scorta ");
		sb.append("from ");
		sb.append("( ");
		sb.append("   select ");
		sb.append("   cat.id as idCategoria , ");
		sb.append("   cat.codice as codiceCategoria, ");
		sb.append("   cat.descrizioneLinguaAziendale as descrizioneCategoria, ");
		sb.append("   art.codice as codArticolo, ");
		sb.append("   art.descrizioneLinguaAziendale as descrizioneArticolo, ");
		sb.append("   art.id as idArticolo, ");
		sb.append("   dep.id as idDeposito, ");
		sb.append("   dep.codice as codiceDeposito, ");
		sb.append("   dep.descrizione as descrizioneDeposito, ");
		sb.append("   ent.codice as codiceFornitoreAbituale, ");
		sb.append("   invj.dataRegistrazione as dataInventario, ");
		sb.append("   invr.qtaMagazzino as qtaMagazzino, ");
		sb.append("   0 as qtaCarico, ");
		sb.append("   0 as qtaScarico, ");
		sb.append("   0 as qtaCaricoAltro, ");
		sb.append("   0 as qtaScaricoAltro, ");
		sb.append("   invr.importoInValutaNetto  as costoInventario, ");
		sb.append(getCostoInventario());
		sb.append("   depArt.scorta as scorta ");
		sb.append("   from maga_righe_magazzino invr ");
		sb.append("   inner join ");
		sb.append("   ( ");
		sb.append("      select ");
		sb.append("      inv.id, inv.dataRegistrazione,inv.depositoOrigine_id ");
		sb.append("      from maga_area_magazzino inv ");
		sb.append("      where inv.dataRegistrazione<=:dataValorizzazione ");
		sb.append("      and tipoOperazione=0 ");
		sb.append("      and inv.depositoOrigine_id=:deposito_id ");
		sb.append("      order by inv.dataRegistrazione desc LIMIT 1 ");
		sb.append("   ) ");
		sb.append("   invj on invj.id=invr.areaMagazzino_id ");
		sb.append("   inner join maga_articoli art on  invr.articolo_id=art.id ");
		sb.append("   inner join maga_categorie cat on cat.id=art.categoria_id ");
		sb.append("   inner join anag_depositi dep on dep.id=invj.depositoOrigine_id ");
		sb.append("   left join maga_articolo_deposito depArt on depArt.articolo_id=art.id and depArt.deposito_id=invj.depositoOrigine_id ");
		sb.append("   left join maga_codici_articolo_entita artEnt on artEnt.articolo_id=art.id ");
		sb.append("   and artEnt.entitaPrincipale=true ");
		sb.append("   left join anag_entita ent on ent.id=artEnt.entita_id ");
		sb.append(" where 1=1");
		if (!filtroCategorie.isEmpty()) {
			sb.append(" and cat.id ").append(filtroCategorie);
		}
		if (!filtroArticoli.isEmpty()) {
			sb.append("   and art.id ").append(filtroArticoli);
		}
		if (!parametriRicercaValorizzazione.isConsideraArticoliDisabilitati()) {
			sb.append(" and art.abilitato=true ");
		}

		sb.append("   union ");

		sb.append("   select ");
		sb.append("   cat.id as idCategoria , ");
		sb.append("   cat.codice as codiceCategoria, ");
		sb.append("   cat.descrizioneLinguaAziendale as descrizioneCategoria, ");
		sb.append("   art.codice as codArticolo, ");
		sb.append("   art.descrizioneLinguaAziendale as descrizioneArticolo, ");
		sb.append("   art.id as idArticolo, ");
		sb.append("   dep.id as idDeposito, ");
		sb.append("   dep.codice as codiceDeposito, ");
		sb.append("   dep.descrizione as descrizioneDeposito, ");
		sb.append("   ent.codice as codiceFornitoreAbituale, ");
		sb.append("   null as dataInventario, ");
		sb.append("   0 as qtaMagazzino , ");
		sb.append("   sum(mov.qtaMagazzinoCarico) as qtaCarico, ");
		sb.append("   sum(mov.qtaMagazzinoScarico) as qtaScarico, ");
		sb.append("   sum(mov.qtaMagazzinoCaricoAltro) as qtaCaricoAltro, ");
		sb.append("   sum(mov.qtaMagazzinoScaricoAltro) as qtaScaricoAltro, ");
		sb.append("   0 as costoInventario, ");
		sb.append(getCostoMovimentato());
		sb.append("   depArt.scorta as scorta ");
		sb.append("   from dw_movimentimagazzino mov ");
		sb.append("   inner join maga_articoli art on  mov.articolo_id=art.id ");
		sb.append("   inner join maga_categorie cat on cat.id=art.categoria_id ");
		sb.append("   inner join anag_depositi dep on dep.id=mov.deposito_id ");
		sb.append("   left join maga_articolo_deposito depArt on depArt.articolo_id=art.id ");
		sb.append("   left join maga_codici_articolo_entita artEnt on artEnt.articolo_id=art.id ");
		sb.append("   and artEnt.entitaPrincipale=true ");
		sb.append("   left join anag_entita ent on ent.id=artEnt.entita_id ");
		sb.append("   where mov.deposito_id=:deposito_id ");
		sb.append("   and mov.dataRegistrazione>= ");
		sb.append("   ( ");
		sb.append("      select ");
		sb.append("      invu.dataRegistrazione ");
		sb.append("      from maga_area_magazzino invu ");
		sb.append("      where invu.dataRegistrazione<=:dataValorizzazione ");
		sb.append("      and tipoOperazione=0 ");
		sb.append("      and invu.depositoOrigine_id=:deposito_id ");
		sb.append("      union  ");
		sb.append("      select '2001-01-01 00:00:00'  as dataRegistrazione ");
		sb.append("      order by dataRegistrazione desc LIMIT 1 ");
		sb.append("   ) ");
		sb.append("   and mov.dataRegistrazione<=:dataValorizzazione ");
		if (!filtroCategorie.isEmpty()) {
			sb.append(" and cat.id ").append(filtroCategorie);
		}
		if (!filtroArticoli.isEmpty()) {
			sb.append("   and art.id ").append(filtroArticoli);
		}
		if (!parametriRicercaValorizzazione.isConsideraArticoliDisabilitati()) {
			sb.append(" and art.abilitato=true ");
		}
		sb.append("   group by mov.articolo_id ");

		if (parametriRicercaValorizzazione.isConsideraGiacenzaZero()) {
			sb.append(" union ");

			sb.append("select ");
			sb.append("cat.id as idCategoria , ");
			sb.append("cat.codice as codiceCategoria, ");
			sb.append("cat.descrizioneLinguaAziendale as descrizioneCategoria, ");
			sb.append("art.codice as codArticolo, ");
			sb.append("art.descrizioneLinguaAziendale as descrizioneArticolo, ");
			sb.append("art.id as idArticolo, ");
			sb.append("dep.id as idDeposito, ");
			sb.append("dep.codice as codiceDeposito, ");
			sb.append("dep.descrizione as descrizioneDeposito, ");
			sb.append("ent.codice as codiceFornitoreAbituale, ");
			sb.append("null as dataInventario, ");
			sb.append("0 as qtaMagazzino , ");
			sb.append("0 as qtaCarico, ");
			sb.append("0 as qtaScarico, ");
			sb.append("0 as qtaCaricoAltro, ");
			sb.append("0 as qtaScaricoAltro, ");
			sb.append("   0 as costoInventario, ");
			sb.append(getCostoAnagrafica());
			sb.append("depArt.scorta as scorta ");
			sb.append("from maga_articoli art ");
			sb.append("inner join maga_categorie cat on cat.id=art.categoria_id ");
			sb.append("left join maga_articolo_deposito depArt on depArt.articolo_id=art.id ");
			sb.append("left join maga_codici_articolo_entita artEnt on artEnt.articolo_id=art.id ");
			sb.append("and artEnt.entitaPrincipale=1 ");
			sb.append("left join anag_entita ent on ent.id=artEnt.entita_id cross ");
			sb.append("join anag_depositi dep ");
			sb.append("where dep.id=1 ");
			if (!filtroCategorie.isEmpty()) {
				sb.append(" and cat.id ").append(filtroCategorie);
			}
			if (!filtroArticoli.isEmpty()) {
				sb.append("   and art.id ").append(filtroArticoli);
			}
			if (!parametriRicercaValorizzazione.isConsideraArticoliDisabilitati()) {
				sb.append(" and art.abilitato=true ");
			}
		}
		sb.append(") ");
		sb.append("as val ");
		sb.append("group by idArticolo ");
		if (!parametriRicercaValorizzazione.isConsideraGiacenzaZero()) {
			sb.append(" having  qtaInventario+qtaMagazzinoCarico+qtaMagazzinoCaricoAltro-qtaMagazzinoScarico-qtaMagazzinoScaricoAltro<>0 ");
		}
		sb.append("order by null ");

		// avvaloro i parametri
		String query = sb
				.toString()
				.replaceAll(":deposito_id", String.valueOf(idDeposito))
				.replaceAll(
						":dataValorizzazione",
						PanjeaEJBUtil.addQuote(new SimpleDateFormat("yyyy-MM-dd").format(parametriRicercaValorizzazione
								.getData())));

		return query;
	}
}
