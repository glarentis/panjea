package it.eurotn.panjea.ordini.manager.documento.produzione;

import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCaricoProduzione;
import it.eurotn.panjea.ordini.manager.documento.produzione.interfaces.OrdiniProduzioneManager;
import it.eurotn.panjea.ordini.util.ParametriRicercaProduzione;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.hibernate.transform.Transformers;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.OrdiniProduzioneManagerBean")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.OrdiniProduzioneManagerBean")
public class OrdiniProduzioneManagerBean implements OrdiniProduzioneManager {

	private static Logger logger = Logger.getLogger(OrdiniProduzioneManagerBean.class);

	@EJB
	private PanjeaDAO panjeaDAO;

	@SuppressWarnings("unchecked")
	@Override
	public List<RigaDistintaCaricoProduzione> caricaRigheEvasioneProduzione(
			ParametriRicercaProduzione parametriRicercaProduzione) {
		String hqlStringQuery = ProduzioneOrdiniQueryBuilder.getHQL(parametriRicercaProduzione);

		org.hibernate.ejb.HibernateQuery query = (org.hibernate.ejb.HibernateQuery) panjeaDAO.getEntityManager()
				.createQuery(hqlStringQuery);
		query.getHibernateQuery().setProperties(parametriRicercaProduzione);
		query.getHibernateQuery().setResultTransformer(Transformers.aliasToBean((RigaDistintaCaricoProduzione.class)));

		List<RigaDistintaCaricoProduzione> righeProduzione = null;
		try {
			righeProduzione = panjeaDAO.getResultList(query);
		} catch (Exception e) {
			logger.error("--> errore in caricaRigheEvasione", e);
			throw new RuntimeException(e);
		}
		return righeProduzione;
	}

}
