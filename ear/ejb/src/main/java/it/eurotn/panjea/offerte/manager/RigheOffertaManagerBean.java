package it.eurotn.panjea.offerte.manager;

import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.manager.interfaces.AziendeManager;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.offerte.domain.RigaOfferta;
import it.eurotn.panjea.offerte.manager.interfaces.AreaOffertaManager;
import it.eurotn.panjea.offerte.manager.interfaces.RigheOffertaManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * 
 * @author Leonardo
 */
@Stateless(name = "Panjea.RigheOffertaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RigheOffertaManager")
public class RigheOffertaManagerBean implements RigheOffertaManager {

	private static Logger logger = Logger.getLogger(RigheOffertaManagerBean.class);

	@EJB
	private PanjeaDAO panjeaDAO;

	@EJB
	@IgnoreDependency
	private AreaOffertaManager areaOffertaManager;

	@EJB
	private AziendeManager aziendeManager;

	@SuppressWarnings("unchecked")
	@Override
	public List<RigaOfferta> caricaRigheOfferta(Integer idAreaOfferta) {
		logger.debug("--> Enter caricaRigheOfferta");
		Query query = panjeaDAO.prepareNamedQuery("RigaOfferta.caricaByAreaOfferta");
		query.setParameter("paramIdAreaOfferta", idAreaOfferta);
		List<RigaOfferta> righeOfferta = query.getResultList();
		logger.debug("--> Exit caricaRigheOfferta");
		return righeOfferta;
	}

	@Override
	public RigaOfferta creaRigaOfferta(Integer idArticolo) {
		logger.debug("--> Enter creaRigaOfferta");
		Articolo articolo;
		try {
			articolo = panjeaDAO.load(Articolo.class, idArticolo);
		} catch (ObjectNotFoundException e) {
			logger.error("--> articolo non trovato: idArticolo " + idArticolo, e);
			throw new RuntimeException(e);
		}

		RigaOfferta rigaOfferta = new RigaOfferta();
		rigaOfferta.setArticolo(articolo.getArticoloLite());
		rigaOfferta.setDescrizione(articolo.getDescrizione());
		rigaOfferta.setDescrizioneEstesa(articolo.getDescrizioniLingua()
				.get(aziendeManager.caricaAzienda().getLingua()).getDescrizione());
		rigaOfferta.setAccettata(true);
		logger.debug("--> Exit creaRigaOfferta");
		return rigaOfferta;
	}

	@Override
	public RigaOfferta salvaRigaOfferta(RigaOfferta rigaOfferta) {
		logger.debug("--> Enter salvaRigaOfferta");
		RigaOfferta rigaOffertaSalvata = null;
		try {
			rigaOffertaSalvata = panjeaDAO.save(rigaOfferta);
		} catch (Exception e) {
			logger.error("--> errore in salvaRigaOfferta", e);
			throw new RuntimeException(e);
		}
		rigaOffertaSalvata.setAreaOfferta(areaOffertaManager.totalizzaAreaOfferta(rigaOfferta.getAreaOfferta()));
		if (logger.isDebugEnabled()) {
			logger.debug("--> Exit salvaRigaOfferta " + rigaOffertaSalvata.getId());
		}
		return rigaOffertaSalvata;
	}

}
