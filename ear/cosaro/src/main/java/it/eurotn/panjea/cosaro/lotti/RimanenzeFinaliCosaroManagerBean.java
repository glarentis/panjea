package it.eurotn.panjea.cosaro.lotti;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.hibernate.SQLQuery;
import org.hibernate.ejb.QueryImpl;
import org.hibernate.transform.Transformers;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;
import org.joda.time.DateTime;

import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.lotti.manager.rimanenzefinali.RimanenzeFinaliDTO;
import it.eurotn.panjea.lotti.manager.rimanenzefinali.RimanenzeFinaliManagerBean;
import it.eurotn.panjea.magazzino.domain.Categoria;
import it.eurotn.panjea.magazzino.util.ParametriRicercaValorizzazione;

/**
 * @author fattazzo
 *
 */
@Stateless(mappedName = "Panjea.RimanenzeFinaliCosaroManagerBean")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RimanenzeFinaliCosaroManagerBean")
public class RimanenzeFinaliCosaroManagerBean extends RimanenzeFinaliManagerBean {

	@Override
	protected List<RimanenzeFinaliDTO> calcolaGiacenze(DepositoLite depositoLite, Date data, Integer idFornitore,
			Integer idCategoria, boolean caricaGiacenzeAZero) {

		ParametriRicercaValorizzazione parametri = new ParametriRicercaValorizzazione();
		parametri.setConsideraGiacenzaZero(caricaGiacenzeAZero);
		parametri.setData(data);
		if (idCategoria != null) {
			parametri.setTutteCategorie(false);
			Categoria categoria = new Categoria();
			categoria.setId(idCategoria);
			parametri.getCategorie().add(categoria);
		}
		String valQuery = magazzinoValorizzazioneDepositoManager.getSqlString(parametri, depositoLite.getId());

		StringBuilder sb = new StringBuilder();
		sb.append(
				"select giacTbl.qtaInventario+giacTbl.qtaMagazzinoCarico+giacTbl.qtaMagazzinoCaricoAltro-giacTbl.qtaMagazzinoScarico-giacTbl.qtaMagazzinoScaricoAltro as giacenza, ");
		sb.append("              idArticolo as idArticolo, ");
		sb.append("              codiceArticolo as codiceArticolo, ");
		sb.append("              descrizioneArticolo as descrizioneArticolo, ");
		sb.append("              idCategoria as idCategoria, ");
		sb.append("              codiceCategoria as codiceCategoria, ");
		sb.append("              descrizioneCategoria as descrizioneCategoria, ");
		sb.append(
				"              (select coalesce(sum(mov.qtaMagazzinoScarico+mov.qtaMagazzinoScaricoAltro),0) from dw_movimentimagazzino mov where MONTH(mov.dataRegistrazione) = :paramMese and YEAR(mov.dataRegistrazione) = :paramAnno and mov.deposito_id = :paramDepositoId and mov.articolo_id= giacTbl.idArticolo and mov.tipoMovimento = 2 and mov.sezioneTipoMovimentoValore = 0 ) as qtaScaricoVenditaMese, ");
		sb.append(
				"              (select coalesce(sum(mov.qtaMagazzinoScarico+mov.qtaMagazzinoScaricoAltro),0) from dw_movimentimagazzino mov where YEAR(mov.dataRegistrazione) = :paramAnno and mov.deposito_id =  :paramDepositoId and mov.articolo_id= giacTbl.idArticolo and mov.tipoMovimento = 2 and mov.sezioneTipoMovimentoValore = 0  ) as qtaScaricoVenditaAnno, ");
		sb.append("              um.codice as unitaMisura ");
		sb.append("from (");
		sb.append(valQuery);
		sb.append(") giacTbl inner join maga_articoli art on giacTbl.idArticolo = art.id ");
		sb.append("                 left join anag_unita_misura um on um.id = art.unitaMisura_id ");
		sb.append(createWhereSQL(idFornitore, idCategoria, caricaGiacenzeAZero));
		sb.append(" order by codiceArticolo ");

		QueryImpl queryGiacenza = (QueryImpl) panjeaDAO.getEntityManager().createNativeQuery(sb.toString());
		SQLQuery sqlQuery = ((SQLQuery) queryGiacenza.getHibernateQuery());
		DateTime dataRicerca = new DateTime(data);
		sqlQuery.setParameter("paramMese", dataRicerca.getMonthOfYear());
		sqlQuery.setParameter("paramAnno", dataRicerca.getYear());
		sqlQuery.setParameter("paramDepositoId", depositoLite.getId());
		sqlQuery.setResultTransformer(Transformers.aliasToBean((RimanenzeFinaliDTO.class)));

		String[] scalar = new String[] { "giacenza", "idArticolo", "codiceArticolo", "descrizioneArticolo",
				"idCategoria", "codiceCategoria", "descrizioneCategoria", "qtaScaricoVenditaMese",
				"qtaScaricoVenditaAnno", "unitaMisura" };
		for (int i = 0; i < scalar.length; i++) {
			sqlQuery.addScalar(scalar[i]);
		}
		@SuppressWarnings("unchecked")
		List<RimanenzeFinaliDTO> results = queryGiacenza.getResultList();

		return results;
	}

	@Override
	protected Map<Integer, Double[]> caricaVendite(DepositoLite depositoLite, Date data, boolean caricaGiacenzeAZero) {
		// per Cosaro le vendite non servono quindi le tolgo per velocizzare in questo modo il caricamento del report
		return new HashMap<Integer, Double[]>();
	}
}
