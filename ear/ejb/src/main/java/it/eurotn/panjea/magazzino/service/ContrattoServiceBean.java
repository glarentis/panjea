package it.eurotn.panjea.magazzino.service;

import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.CategoriaSedeMagazzino;
import it.eurotn.panjea.magazzino.domain.Contratto;
import it.eurotn.panjea.magazzino.domain.RigaContratto;
import it.eurotn.panjea.magazzino.domain.SedeMagazzinoLite;
import it.eurotn.panjea.magazzino.manager.interfaces.ContrattiManager;
import it.eurotn.panjea.magazzino.service.interfaces.ContrattoService;
import it.eurotn.panjea.magazzino.util.ContrattoProspettoDTO;
import it.eurotn.panjea.magazzino.util.ContrattoStampaDTO;
import it.eurotn.panjea.magazzino.util.ParametriRicercaContratti;
import it.eurotn.panjea.magazzino.util.RigaContrattoCalcolo;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaStampaContratti;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.lang3.ObjectUtils;
import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.ContrattoService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.ContrattoService")
public class ContrattoServiceBean implements ContrattoService {

	/**
	 * @uml.property name="contrattiManager"
	 * @uml.associationEnd
	 */
	@EJB
	protected ContrattiManager contrattiManager;

	/**
	 * @uml.property name="panjeaDAO"
	 * @uml.associationEnd
	 */
	@EJB
	protected PanjeaDAO panjeaDAO;

	@Override
	public Contratto associaCategoriaSedeContratto(Contratto contratto, CategoriaSedeMagazzino categoriaSedeMagazzino) {
		return contrattiManager.associaCategoriaSedeContratto(contratto, categoriaSedeMagazzino);
	}

	@Override
	public Contratto associaEntitaContratto(Contratto contratto, EntitaLite entitaLite) {
		return contrattiManager.associaEntitaContratto(contratto, entitaLite);
	}

	@Override
	public Contratto associaSedeContratto(Contratto contratto, SedeMagazzinoLite sedeMagazzinoLite) {
		return contrattiManager.associaSedeContratto(contratto, sedeMagazzinoLite);
	}

	@Override
	public void cancellaContratto(Contratto contratto) {
		contrattiManager.cancellaContratto(contratto);
	}

	@Override
	public void cancellaRigaContratto(RigaContratto rigaContratto) {
		contrattiManager.cancellaRigaContratto(rigaContratto);
	}

	@Override
	public List<Contratto> caricaContratti(ParametriRicercaContratti parametriRicercaContratti) {
		return contrattiManager.caricaContratti(parametriRicercaContratti);
	}

	@Override
	public Contratto caricaContratto(Contratto contratto, boolean loadLazy) {
		return contrattiManager.caricaContratto(contratto, loadLazy);
	}

	@Override
	public List<ContrattoProspettoDTO> caricaContrattoProspetto(Integer idSedeEntita, Date data) {
		return contrattiManager.caricaContrattoProspetto(idSedeEntita, data);
	}

	@Override
	public RigaContratto caricaRigaContratto(RigaContratto rigaContratto) {
		return contrattiManager.caricaRigaContratto(rigaContratto);
	}

	@Override
	public List<RigaContratto> caricaRigheContratto(Contratto contratto) {
		return contrattiManager.caricaRigheContratto(contratto);
	}

	@Override
	public List<RigaContrattoCalcolo> caricaRigheContrattoCalcolo(Integer idSedeEntita, Date data, String codiceValuta) {
		return contrattiManager.caricaRigheContrattoCalcolo(null, null, idSedeEntita, null, null, data, codiceValuta,
				null, false);
	}

	@Override
	public List<RigaContrattoCalcolo> caricaRigheContrattoCalcolo(Integer idArticolo, Integer idSedeEntita, Date data,
			String codiceValuta, Integer idAgente, boolean tutteLeRighe) {
		return contrattiManager.caricaRigheContrattoCalcolo(idArticolo, null, idSedeEntita, null, null, data,
				codiceValuta, idAgente, tutteLeRighe);
	}

	@Override
	public List<ContrattoStampaDTO> caricaStampaContratti(Map<Object, Object> params) {

		ParametriRicercaStampaContratti parametriRicerca = new ParametriRicercaStampaContratti();
		parametriRicerca.setEscludiContrattiNonAttivi((boolean) ObjectUtils.defaultIfNull(
				params.get("escludiContrattiNonAttivi"), Boolean.FALSE));

		EntitaLite entita = new EntitaLite();
		entita.setId((Integer) params.get("idEntita"));
		parametriRicerca.setEntita(entita);

		ArticoloLite articolo = new ArticoloLite();
		articolo.setId((Integer) params.get("idArticolo"));
		parametriRicerca.setArticolo(articolo);

		return contrattiManager.caricaStampaContratti(parametriRicerca);
	}

	@Override
	public List<ContrattoStampaDTO> caricaStampaContratti(ParametriRicercaStampaContratti parametri) {
		return contrattiManager.caricaStampaContratti(parametri);
	}

	@Override
	public Contratto rimuoviCategoriaSedeContratto(Contratto contratto, CategoriaSedeMagazzino categoriaSedeMagazzino) {
		return contrattiManager.rimuoviCategoriaSedeContratto(contratto, categoriaSedeMagazzino);
	}

	@Override
	public Contratto rimuoviEntitaContratto(Contratto contratto, EntitaLite entitaLite) {
		return contrattiManager.rimuoviEntitaContratto(contratto, entitaLite);
	}

	@Override
	public Contratto rimuoviSedeContratto(Contratto contratto, SedeMagazzinoLite sedeMagazzinoLite) {
		return contrattiManager.rimuoviSedeContratto(contratto, sedeMagazzinoLite);
	}

	@Override
	public Contratto salvaContratto(Contratto contratto, boolean loadCollection) {
		return contrattiManager.salvaContratto(contratto, loadCollection);
	}

	@Override
	public RigaContratto salvaRigaContratto(RigaContratto rigaContratto) {
		return contrattiManager.salvaRigaContratto(rigaContratto);
	}

}