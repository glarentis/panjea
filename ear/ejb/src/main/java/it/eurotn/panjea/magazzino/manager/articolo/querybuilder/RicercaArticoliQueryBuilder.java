package it.eurotn.panjea.magazzino.manager.articolo.querybuilder;

import it.eurotn.panjea.magazzino.manager.interfaces.CategorieManager;
import it.eurotn.panjea.magazzino.util.ArticoloRicerca;
import it.eurotn.panjea.magazzino.util.ParametriRicercaArticolo;
import it.eurotn.panjea.magazzino.util.ricercaarticoli.ParametroRicercaAttributo;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

import javax.persistence.Query;

import org.hibernate.ejb.QueryImpl;
import org.hibernate.transform.Transformers;

/**
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
public class RicercaArticoliQueryBuilder {

	private final PanjeaDAO panjeaDAO;
	private final CategorieManager categorieManager;
	private final JecPrincipal principal;

	/**
	 * Costruttore.
	 * 
	 * @param panjeaDAO
	 *            dao per costruire la query
	 * @param principal
	 *            principal
	 * @param categorieManager
	 *            categorieManager
	 */
	public RicercaArticoliQueryBuilder(final PanjeaDAO panjeaDAO, final JecPrincipal principal,
			final CategorieManager categorieManager) {
		this.panjeaDAO = panjeaDAO;
		this.categorieManager = categorieManager;
		this.principal = principal;
	}

	/**
	 * Query con parametri impostati per la ricerca articoli.
	 * 
	 * @param parametriRicercaArticolo
	 *            i parametri per comporre la query
	 * @return la query da eseguire
	 */
	public Query createQuery(ParametriRicercaArticolo parametriRicercaArticolo) {

		// prendo i parametri ricerca articolo e ne valorizzo gli attributi che mi saranno utili per l'esecuzione della
		// query, aggiungo anche le % per le proprietà avvalorate
		parametriRicercaArticolo = ParametriRicercaArticoloPopulator.populate(parametriRicercaArticolo, panjeaDAO,
				principal, categorieManager);

		// costruisco la query hql
		RicercaArticoloHqlBuilder ricercaArticoloHqlBuilder = new RicercaArticoloHqlBuilder();
		String hql = ricercaArticoloHqlBuilder.createHql(parametriRicercaArticolo);

		// System.err.println("H--Q--L-- " + hql);
		// creo la query e setto
		Query query = panjeaDAO.prepareQuery(hql);

		// setto i parametri via setProperties con l'oggetto ParametriRicercaArticolo che dopo aver chiamato
		// ParametriRicercaArticoloPopulator.populate contiene tutte le proprietà utili alla ricerca avvalorate
		((org.hibernate.ejb.HibernateQuery) query).getHibernateQuery().setResultTransformer(
				Transformers.aliasToBean(ArticoloRicerca.class));
		((QueryImpl) query).getHibernateQuery().setProperties(parametriRicercaArticolo);

		// gli attributi sono costruiti dinamicamente e quindi sono l'unica proprietà che devo impostare manualmente
		if (parametriRicercaArticolo.getRicercaAttributi() != null) {
			for (ParametroRicercaAttributo attr : parametriRicercaArticolo.getRicercaAttributi()) {
				String name = attr.getNome();

				query.setParameter("param" + name, name);
				if (attr.getOperatore() != null) {
					query.setParameter("paramValore" + name, attr.getValore());
				}
			}
		}

		return query;
	}
}
