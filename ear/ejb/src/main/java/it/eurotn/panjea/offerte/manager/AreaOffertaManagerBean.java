/**
 * 
 */
package it.eurotn.panjea.offerte.manager;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.manager.interfaces.DocumentiManager;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentoDuplicateException;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.offerte.domain.AreaOfferta;
import it.eurotn.panjea.offerte.domain.RigaOfferta;
import it.eurotn.panjea.offerte.manager.interfaces.AreaOffertaManager;
import it.eurotn.panjea.offerte.manager.interfaces.RigheOffertaManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * @author Leonardo
 * 
 */
@Stateless(name = "Panjea.AreaOffertaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaOffertaManager")
public class AreaOffertaManagerBean implements AreaOffertaManager {

	private static Logger logger = Logger.getLogger(AreaOffertaManagerBean.class);

	@Resource
	private SessionContext sessionContext;

	@EJB
	private PanjeaDAO panjeaDAO;

	@EJB
	private DocumentiManager documentiManager;

	@EJB
	@IgnoreDependency
	private RigheOffertaManager righeOffertaManager;

	@Override
	public AreaOfferta caricaAreaOfferta(Integer idAreaOfferta) {
		AreaOfferta areaOfferta = null;
		try {
			areaOfferta = panjeaDAO.load(AreaOfferta.class, idAreaOfferta);
		} catch (ObjectNotFoundException e) {
			logger.error("--> errore ObjectNotFoundException in caricaAreaMagazzino", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit caricaAreaMagazzino");
		return areaOfferta;
	}

	@Override
	public AreaOfferta caricaAreaOffertaByDocumento(Documento documento) {
		logger.debug("--> Enter caricaAreaOffertaByDocumento");
		AreaOfferta areaOfferta = null;
		try {
			Query query = panjeaDAO.prepareNamedQuery("AreaOfferta.ricercaByDocumento");
			query.setParameter("paramIdDocumento", documento.getId());

			areaOfferta = (AreaOfferta) panjeaDAO.getSingleResult(query);
		} catch (ObjectNotFoundException e) {
			logger.warn("--> Non esiste una areaOfferta per il documento " + documento.getId());
		} catch (DAOException e) {
			logger.error("--> errore nel caricare l'area magazzino per il documento " + documento.getId(), e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit caricaAreaMagazzinoByDocumento");
		return areaOfferta;
	}

	@Override
	public AreaOfferta salvaAreaOfferta(AreaOfferta areaOfferta) throws DocumentoDuplicateException {
		logger.debug("--> Exit salvaAreaOfferta");
		try {
			// allineo il tipo documento dell'area offerta con il tipo documento del documento
			areaOfferta.getDocumento().setTipoDocumento(areaOfferta.getTipoAreaOfferta().getTipoDocumento());

			// save di documento
			Documento documento = documentiManager.salvaDocumento(areaOfferta.getDocumento());
			areaOfferta.setDocumento(documento);

			AreaOfferta areaOffertaSalvata = panjeaDAO.save(areaOfferta);
			logger.debug("--> Exit salvaAreaOfferta");
			return areaOffertaSalvata;
		} catch (DocumentoDuplicateException e) {
			logger.warn("--> DocumentoDuplicateException in salvaAreaOfferta");
			sessionContext.setRollbackOnly();
			throw e;
		} catch (Exception e) {
			logger.error("--> errore impossibile eseguire il salvataggio di AreaOfferta", e);
			throw new RuntimeException("--> errore impossibile eseguire il salvataggio di AreaOfferta", e);
		}
	}

	@Override
	public AreaOfferta totalizzaAreaOfferta(AreaOfferta areaOfferta) {
		logger.debug("--> Enter totalizzaAreaOfferta");
		List<RigaOfferta> righe = righeOffertaManager.caricaRigheOfferta(areaOfferta.getId());
		Importo totale = areaOfferta.getDocumento().getTotale().clone();
		totale.setImportoInValuta(BigDecimal.ZERO);
		totale.setImportoInValutaAzienda(BigDecimal.ZERO);
		for (RigaOfferta rigaOfferta : righe) {
			totale = totale.add(rigaOfferta.getPrezzoTotale(), 2);
		}
		areaOfferta.getDocumento().setTotale(totale);
		Documento documento;
		try {
			documento = panjeaDAO.save(areaOfferta.getDocumento());
		} catch (Exception e) {
			logger.error("--> errore impossibile eseguire il salvataggio di Documento", e);
			throw new RuntimeException("--> errore impossibile eseguire il salvataggio di AreaOfferta", e);
		}
		areaOfferta.setDocumento(documento);
		logger.debug("--> Exit totalizzaAreaOfferta");
		return areaOfferta;
	}
}
