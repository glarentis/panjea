/**
 * 
 */
package it.eurotn.panjea.preventivi.manager.evasione;

import it.eurotn.panjea.parametriricerca.domain.Periodo.TipoPeriodo;
import it.eurotn.panjea.preventivi.manager.evasione.interfaces.PreventiviEvasioneManager;
import it.eurotn.panjea.preventivi.manager.sqlbuilder.MovimentazioneQueryBuilder;
import it.eurotn.panjea.preventivi.util.RigaEvasione;
import it.eurotn.panjea.preventivi.util.RigaMovimentazione;
import it.eurotn.panjea.preventivi.util.parametriricerca.ParametriRicercaMovimentazione;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * @author fattazzo
 * 
 */
@Stateless(name = "Panjea.PreventiviEvasioneManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.PreventiviEvasioneManager")
public class PreventiviEvasioneManagerBean implements PreventiviEvasioneManager {

	private static Logger logger = Logger.getLogger(PreventiviEvasioneManagerBean.class);

	@Resource
	protected SessionContext context;

	@EJB
	protected PanjeaDAO panjeaDAO;

	@SuppressWarnings("unchecked")
	@Override
	public List<RigaEvasione> caricaRigheEvasione(Integer idAreaPreventivo) {

		ParametriRicercaMovimentazione parametri = new ParametriRicercaMovimentazione();
		parametri.getDataRegistrazione().setTipoPeriodo(TipoPeriodo.NESSUNO);

		StringBuilder sqlMovimentazione = new StringBuilder(MovimentazioneQueryBuilder.getSqlMovimentazione(parametri,
				getAzienda()));
		sqlMovimentazione.append(" and areaPreventivo.id = " + idAreaPreventivo);
		sqlMovimentazione.append(" group by riga.id ");
		sqlMovimentazione
				.append(" having (select coalesce(sum(rigaord.qta),0) from ordi_righe_ordine as rigaord where rigaord.rigaPreventivoCollegata_id = riga.id) < riga.qta or riga.qta=0");
		sqlMovimentazione.append(" order by riga.ordinamento");

		org.hibernate.ejb.QueryImpl queryImpl = (org.hibernate.ejb.QueryImpl) panjeaDAO.getEntityManager()
				.createNativeQuery(sqlMovimentazione.toString());
		SQLQuery query = ((SQLQuery) queryImpl.getHibernateQuery());
		query.setResultTransformer(Transformers.aliasToBean(RigaMovimentazione.class));
		MovimentazioneQueryBuilder.addQueryScalar(query);

		List<RigaMovimentazione> righeMovimentazione = new ArrayList<RigaMovimentazione>();
		try {
			righeMovimentazione = query.list();
		} catch (Exception e) {
			logger.error("--> errore durante il caricamento delle righe preventivo per l'evasione.", e);
			throw new RuntimeException("errore durante il caricamento delle righe preventivo per l'evasione.", e);
		}

		List<RigaEvasione> righeEvasione = new ArrayList<RigaEvasione>();
		for (RigaMovimentazione rigaMovimentazione : righeMovimentazione) {
			if (rigaMovimentazione.getQuantita() - rigaMovimentazione.getQuantitaEvasa() > 0) {
				righeEvasione.add(new RigaEvasione(rigaMovimentazione));
			}
		}

		return righeEvasione;
	}

	/**
	 * 
	 * @return codice Azienda loggata
	 */
	private String getAzienda() {
		JecPrincipal jecPrincipal = (JecPrincipal) context.getCallerPrincipal();
		return jecPrincipal.getCodiceAzienda();
	}

}
