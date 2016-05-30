/**
 * 
 */
package it.eurotn.panjea.magazzino.manager.schedearticolo;

import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.manager.schedearticolo.interfaces.MagazzinoStatoSchedeArticolo;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;
import org.joda.time.DateTime;

/**
 * @author fattazzo
 * 
 */
@Stateless(name = "Panjea.MagazzinoStatoSchedeArticolo")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.MagazzinoStatoSchedeArticolo")
public class MagazzinoStatoSchedeArticoloBean implements MagazzinoStatoSchedeArticolo {

	@Resource
	private SessionContext context;

	@EJB
	private PanjeaDAO panjeaDAO;

	private static Logger logger = Logger.getLogger(MagazzinoStatoSchedeArticoloBean.class);

	/**
	 * 
	 * @return codice Azienda loggata
	 */
	private String getAzienda() {
		JecPrincipal jecPrincipal = (JecPrincipal) context.getCallerPrincipal();
		return jecPrincipal.getCodiceAzienda();
	}

	@SuppressWarnings("unchecked")
	@Override
	@RolesAllowed("invalidaSchedeArticolo")
	public void invalidaSchedeArticolo(AreaMagazzino areaMagazzino) {

		Query query = panjeaDAO
				.prepareQuery("select distinct r.articolo.id from RigaArticolo r where r.areaMagazzino.id = :idAreaMagazzino");
		query.setParameter("idAreaMagazzino", areaMagazzino.getId());

		List<Integer> idArticoli = new ArrayList<Integer>();
		try {
			idArticoli = panjeaDAO.getResultList(query);
		} catch (Exception e) {
			logger.error(
					"--> errore durante il caricamento degli articoli dell'area magazzino " + areaMagazzino.getId(), e);
			throw new RuntimeException("errore durante il caricamento degli articoli dell'area magazzino "
					+ areaMagazzino.getId(), e);
		}

		DateTime data = new DateTime(areaMagazzino.getDataRegistrazione());

		for (Integer idArticolo : idArticoli) {
			invalidaSchedeArticolo(data.getYear(), data.getMonthOfYear(), idArticolo);
		}
	}

	@Override
	@RolesAllowed("invalidaSchedeArticolo")
	public void invalidaSchedeArticolo(Integer anno, Integer mese, Integer idArticolo) {
		logger.debug("--> Enter invalidaSchedeArticolo");

		// aggiorno lo stato delle schede articolo
		StringBuilder sb = new StringBuilder();
		sb.append("update SchedaArticolo sa ");
		sb.append(" set sa.stato = 2 ");
		sb.append("where sa.codiceAzienda = :codiceAzienda and sa.stato = 1 and sa.anno > :anno or (sa.anno = :anno and sa.mese >= :mese) and sa.articolo.id = :idArticolo");

		Query query = panjeaDAO.prepareQuery(sb.toString());
		query.setParameter("codiceAzienda", getAzienda());
		query.setParameter("anno", anno);
		query.setParameter("mese", mese);
		query.setParameter("idArticolo", idArticolo);
		query.executeUpdate();

		logger.debug("--> Exit invalidaSchedeArticolo");
	}

}
