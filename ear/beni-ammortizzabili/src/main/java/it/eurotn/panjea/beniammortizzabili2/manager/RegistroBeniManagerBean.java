package it.eurotn.panjea.beniammortizzabili2.manager;

import it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabileLite;
import it.eurotn.panjea.beniammortizzabili2.manager.interfaces.RegistroBeniManager;
import it.eurotn.panjea.beniammortizzabili2.manager.sqlBuilder.RegistroBeniQueryBuilder;
import it.eurotn.panjea.beniammortizzabili2.util.registrobeni.RegistroBene;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.RegistroBeniManager")
@SecurityDomain("PanjeaLoginModule")
@LocalBinding(jndiBinding = "Panjea.RegistroBeniManager")
public class RegistroBeniManagerBean implements RegistroBeniManager {

	private static Logger logger = Logger.getLogger(RegistroBeniManagerBean.class);

	@EJB
	protected PanjeaDAO panjeaDAO;

	@SuppressWarnings("unchecked")
	@Override
	public List<RegistroBene> caricaRegistroBeni(Map<String, Object> parameters) {
		logger.debug("--> Enter caricaRegistroBeni");

		Integer anno = (Integer) parameters.get("anno");
		Integer idGruppo = (Integer) parameters.get("gruppo");
		Integer idSpecie = (Integer) parameters.get("specie");
		Integer idSottoSpecie = (Integer) parameters.get("sottospecie");

		SQLQuery query = ((Session) panjeaDAO.getEntityManager().getDelegate()).createSQLQuery(RegistroBeniQueryBuilder
				.getRegistroBeni(anno, idGruppo, idSpecie, idSottoSpecie));
		query.setResultTransformer(Transformers.aliasToBean((RegistroBene.class)));

		List<RegistroBene> result = Collections.emptyList();

		query.addScalar("idBene");
		query.addScalar("codiceBene");
		query.addScalar("descrizioneBene");
		query.addScalar("annoAcquistoBene");
		query.addScalar("importoSoggettoAdAmmortamentoSingolo");
		query.addScalar("rivalutazioni");
		query.addScalar("vendite");
		query.addScalar("minusPlusValenze");
		query.addScalar("idGruppo");
		query.addScalar("codiceGruppo");
		query.addScalar("descrizioneGruppo");
		query.addScalar("idSpecie");
		query.addScalar("codiceSpecie");
		query.addScalar("descrizioneSpecie");
		query.addScalar("idSottoSpecie");
		query.addScalar("codiceSottoSpecie");
		query.addScalar("descrizioneSottoSpecie");
		query.addScalar("percOrdinario");
		query.addScalar("percAnticipato");
		query.addScalar("percPrimoAnnoApplicata", Hibernate.BOOLEAN);
		query.addScalar("impOrdinario");
		query.addScalar("impAnticipato");
		query.addScalar("importoSoggettoAdAmmortamentoFigli");
		query.addScalar("valoreQuoteFondo");
		query.addScalar("valoreValutazioniFondo");
		query.addScalar("valoreVenditeFondo");
		query.addScalar("rivalutazioniFigli");
		query.addScalar("venditeFigli");
		query.addScalar("rivalutazioniFondoFigli");
		query.addScalar("venditeFondoFigli");
		query.addScalar("minusPlusValenzeFigli");
		result = query.list();

		for (RegistroBene registroBene : result) {

			SQLQuery queryFigli = ((Session) panjeaDAO.getEntityManager().getDelegate())
					.createSQLQuery(RegistroBeniQueryBuilder.getBeniFigli(registroBene.getBene().getId(), anno));
			queryFigli.setResultTransformer(Transformers.aliasToBean((BeneAmmortizzabileLite.class)));

			List<BeneAmmortizzabileLite> beniFigli = new ArrayList<BeneAmmortizzabileLite>();
			beniFigli = queryFigli.list();

			if (beniFigli != null && !beniFigli.isEmpty()) {
				registroBene.setBeniFigli(beniFigli);
			}
		}

		return result;
	}

}
