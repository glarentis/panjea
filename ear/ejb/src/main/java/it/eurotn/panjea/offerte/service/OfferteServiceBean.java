/**
 * 
 */
package it.eurotn.panjea.offerte.service;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentoDuplicateException;
import it.eurotn.panjea.offerte.domain.AreaOfferta;
import it.eurotn.panjea.offerte.domain.RigaOfferta;
import it.eurotn.panjea.offerte.domain.TipoAreaOfferta;
import it.eurotn.panjea.offerte.manager.interfaces.AreaOffertaCancellaManager;
import it.eurotn.panjea.offerte.manager.interfaces.AreaOffertaManager;
import it.eurotn.panjea.offerte.manager.interfaces.AreaOffertaRicercaManager;
import it.eurotn.panjea.offerte.manager.interfaces.RigheOffertaManager;
import it.eurotn.panjea.offerte.manager.interfaces.TipiAreaOffertaManager;
import it.eurotn.panjea.offerte.service.interfaces.OfferteService;
import it.eurotn.panjea.offerte.util.AreaOffertaFullDTO;
import it.eurotn.panjea.offerte.util.ParametriRicercaOfferte;

import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * @author Leonardo
 * 
 */
@Stateless(name = "Panjea.OfferteService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.OfferteService")
public class OfferteServiceBean implements OfferteService {

	private static Logger logger = Logger.getLogger(OfferteServiceBean.class);

	@EJB
	private TipiAreaOffertaManager tipiAreaOffertaManager;

	@EJB
	private AreaOffertaManager areaOffertaManager;

	@EJB
	private RigheOffertaManager righeOffertaManager;

	@EJB
	private AreaOffertaCancellaManager areaOffertaCancellaManager;

	@EJB
	private AreaOffertaRicercaManager areaOffertaRicercaManager;

	@Override
	public void cancellaAreaOfferta(AreaOfferta areaOfferta) {
		areaOffertaCancellaManager.cancellaAreaOfferta(areaOfferta);
	}

	@Override
	public void cancellaRigaOfferta(RigaOfferta rigaOfferta) {
		areaOffertaCancellaManager.cancellaRigaOfferta(rigaOfferta);
	}

	@Override
	public void cancellaRigheOfferta(Integer idAreaOfferta) {
		areaOffertaCancellaManager.cancellaRigheOfferta(idAreaOfferta);
	}

	@Override
	public void cancellaTipoAreaOfferta(Integer idTipoAreaOfferta) {
		tipiAreaOffertaManager.cancellaTipoAreaOfferta(idTipoAreaOfferta);
	}

	@Override
	public AreaOfferta caricaAreaOfferta(Integer idAreaOfferta) {
		return areaOffertaManager.caricaAreaOfferta(idAreaOfferta);

	}

	@Override
	public AreaOfferta caricaAreaOffertaByDocumento(Documento documento) {
		return areaOffertaManager.caricaAreaOffertaByDocumento(documento);
	}

	@Override
	public AreaOffertaFullDTO caricaAreaOffertaFullDTO(Integer idAreaOfferta) {
		logger.debug("--> Enter caricaAreaOffertaFullDTO");
		AreaOffertaFullDTO areaOffertaFullDTO = new AreaOffertaFullDTO();

		AreaOfferta areaOfferta = caricaAreaOfferta(idAreaOfferta);
		List<RigaOfferta> righeOfferta = caricaRigheOfferta(idAreaOfferta);

		areaOffertaFullDTO.setAreaOfferta(areaOfferta);
		areaOffertaFullDTO.setRigheOfferta(righeOfferta);
		logger.debug("--> Exit caricaAreaOffertaFullDTO");
		return areaOffertaFullDTO;
	}

	@Override
	public AreaOffertaFullDTO caricaAreaOffertaFullDTO(Map<Object, Object> parametri) {
		int id = (Integer) parametri.get("id");
		return caricaAreaOffertaFullDTO(id);
	}

	@Override
	public List<RigaOfferta> caricaRigheOfferta(Integer idAreaOfferta) {
		return righeOffertaManager.caricaRigheOfferta(idAreaOfferta);
	}

	@Override
	public List<TipoAreaOfferta> caricaTipiAreaOfferta(boolean loadTipiDocumentoDisabilitati) {
		return tipiAreaOffertaManager.caricaTipiAreaOfferta(loadTipiDocumentoDisabilitati);
	}

	@Override
	public TipoAreaOfferta caricaTipoAreaOfferta(Integer idTipoAreaOfferta) {
		return tipiAreaOffertaManager.caricaTipoAreaOfferta(idTipoAreaOfferta);
	}

	@Override
	public TipoAreaOfferta caricaTipoAreaOffertaPerTipoDocumento(Integer idTipoDocumento) {
		return tipiAreaOffertaManager.caricaTipoAreaOffertaPerTipoDocumento(idTipoDocumento);
	}

	@Override
	public RigaOfferta creaRigaOfferta(Integer idArticolo) {
		return righeOffertaManager.creaRigaOfferta(idArticolo);
	}

	@Override
	public List<RigaOfferta> ricercaOfferte(ParametriRicercaOfferte parametriRicercaOfferte) {
		return areaOffertaRicercaManager.ricercaOfferte(parametriRicercaOfferte);
	}

	@Override
	public AreaOfferta salvaAreaOfferta(AreaOfferta areaOfferta) throws DocumentoDuplicateException {
		return areaOffertaManager.salvaAreaOfferta(areaOfferta);
	}

	@Override
	public RigaOfferta salvaRigaOfferta(RigaOfferta rigaOfferta) {
		return righeOffertaManager.salvaRigaOfferta(rigaOfferta);
	}

	@Override
	public TipoAreaOfferta salvaTipoAreaOfferta(TipoAreaOfferta tipoAreaOfferta) {
		return tipiAreaOffertaManager.salvaTipoAreaOfferta(tipoAreaOfferta);
	}

}
