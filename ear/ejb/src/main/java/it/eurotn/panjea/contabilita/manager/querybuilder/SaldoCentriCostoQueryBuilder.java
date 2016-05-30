package it.eurotn.panjea.contabilita.manager.querybuilder;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.SaldoCentriCostoQueryBuilder")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.SaldoCentriCostoQueryBuilder")
public class SaldoCentriCostoQueryBuilder extends SaldoContiQueryBuilder {

	/**
	 * @return stringa contenente la group by per la query
	 */
	@Override
	protected String buildGroupHql() {
		return " group by mastro, conto, sottoconto, centroCosto ";
	}

	/**
	 * @param isRicercaCentroCosto
	 *            se la ricerca e' per centro di costo devo prendere l'importo sulla rigaCentroCosto
	 * @return stringa contenente la select per la query
	 */
	@Override
	protected String buildSelectHql(boolean isRicercaCentroCosto) {
		StringBuffer queryHQL = new StringBuffer();
		queryHQL.append(" select ");
		queryHQL.append(" mastro.codice as mastroCodice, ");
		queryHQL.append(" mastro.descrizione as mastroDescrizione, ");
		queryHQL.append(" mastro.id as mastroId, ");
		queryHQL.append(" conto.codice as contoCodice, ");
		queryHQL.append(" conto.descrizione as contoDescrizione, ");
		queryHQL.append(" conto.id as contoId, ");
		queryHQL.append(" sottoconto.codice as sottoContoCodice, ");
		queryHQL.append(" sottoconto.descrizione as sottoContoDescrizione, ");
		queryHQL.append(" sottoconto.id as sottoContoId, ");
		queryHQL.append(" centroCosto.codice as centroCostoCodice, ");
		queryHQL.append(" centroCosto.descrizione as centroCostoDescrizione, ");
		queryHQL.append(" centroCosto.id as centroCostoId, ");
		queryHQL.append(" conto.tipoConto as tipoConto, ");
		queryHQL.append(" conto.sottotipoConto as sottoTipoConto, ");
		queryHQL.append(" sum(case when rigaContabile.contoInsert = it.eurotn.panjea.contabilita.domain.RigaContabile$EContoInsert.DARE then rigaCentroCosto.importo else 0.0 end) as importoDare, ");
		queryHQL.append(" sum(case when rigaContabile.contoInsert = it.eurotn.panjea.contabilita.domain.RigaContabile$EContoInsert.AVERE then rigaCentroCosto.importo else 0.0 end) as importoAvere ");
		queryHQL.append(" from RigaContabile rigaContabile");
		queryHQL.append(" inner join rigaContabile.areaContabile areacontabile");
		queryHQL.append(" inner join areacontabile.documento documento");
		queryHQL.append(" inner join rigaContabile.conto sottoconto");
		queryHQL.append(" inner join sottoconto.conto conto");
		queryHQL.append(" inner join conto.mastro mastro");
		queryHQL.append(" inner join rigaContabile.righeCentroCosto rigaCentroCosto");
		queryHQL.append(" inner join rigaCentroCosto.centroCosto centroCosto");
		return queryHQL.toString();
	}

}
