package it.eurotn.panjea.magazzino.rich.bd;

import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.magazzino.domain.CategoriaSedeMagazzino;
import it.eurotn.panjea.magazzino.domain.Contratto;
import it.eurotn.panjea.magazzino.domain.RigaContratto;
import it.eurotn.panjea.magazzino.domain.SedeMagazzinoLite;
import it.eurotn.panjea.magazzino.service.interfaces.ContrattoService;
import it.eurotn.panjea.magazzino.util.ContrattoProspettoDTO;
import it.eurotn.panjea.magazzino.util.ContrattoStampaDTO;
import it.eurotn.panjea.magazzino.util.ParametriRicercaContratti;
import it.eurotn.panjea.magazzino.util.RigaContrattoCalcolo;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaStampaContratti;
import it.eurotn.panjea.rich.bd.AbstractBaseBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

public class ContrattoBD extends AbstractBaseBD implements IContrattoBD {

	private static Logger logger = Logger.getLogger(ContrattoBD.class);

	private ContrattoService contrattoService;

	/**
	 * Costruttore.
	 */
	public ContrattoBD() {
		super();
	}

	@Override
	public Contratto associaCategoriaSedeContratto(Contratto contratto, CategoriaSedeMagazzino categoriaSedeMagazzino) {
		logger.debug("--> Enter associaCategoriaSedeContratto");
		start("associaCategoriaSedeContratto");
		Contratto contrattoSalvato = null;
		try {
			contrattoSalvato = contrattoService.associaCategoriaSedeContratto(contratto, categoriaSedeMagazzino);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("associaCategoriaSedeContratto");
		}
		logger.debug("--> Exit associaCategoriaSedeContratto");
		return contrattoSalvato;
	}

	@Override
	public Contratto associaEntitaContratto(Contratto contratto, EntitaLite entitaLite) {
		logger.debug("--> Enter associaEntitaContratto");
		start("associaEntitaContratto");
		Contratto contrattoSalvato = null;
		try {
			contrattoSalvato = contrattoService.associaEntitaContratto(contratto, entitaLite);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("associaEntitaContratto");
		}
		logger.debug("--> Exit associaEntitaContratto ");
		return contrattoSalvato;
	}

	@Override
	public Contratto associaSedeContratto(Contratto contratto, SedeMagazzinoLite sedeMagazzinoLite) {
		logger.debug("--> Enter associaSedeContratto");
		start("associaSedeContratto");
		Contratto contrattoSalvato = null;
		try {
			contrattoSalvato = contrattoService.associaSedeContratto(contratto, sedeMagazzinoLite);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("associaSedeContratto");
		}
		logger.debug("--> Exit associaSedeContratto");
		return contrattoSalvato;
	}

	@Override
	public void cancellaContratto(Contratto contratto) {
		logger.debug("--> Enter cancellaContratto");
		start("cancellaContratto");
		try {
			contrattoService.cancellaContratto(contratto);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaContratto");
		}
		logger.debug("--> Exit cancellaContratto ");
	}

	@Override
	public void cancellaRigaContratto(RigaContratto rigaContratto) {
		logger.debug("--> Enter cancellaContrattoArticolo");
		start("cancellaContrattoArticolo");
		try {
			contrattoService.cancellaRigaContratto(rigaContratto);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaContrattoArticolo");
		}
		logger.debug("--> Exit cancellaContrattoArticolo ");
	}

	@Override
	public List<Contratto> caricaContratti(ParametriRicercaContratti parametriRicercaContratti) {
		logger.debug("--> Enter caricaContratti");
		start("caricaContratti");
		List<Contratto> contratti = null;
		try {
			contratti = contrattoService.caricaContratti(parametriRicercaContratti);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaContratti");
		}
		logger.debug("--> Exit caricaContratti ");
		return contratti;
	}

	@Override
	public Contratto caricaContratto(Contratto contratto, boolean loadLazy) {
		logger.debug("--> Enter caricaContratto");
		start("caricaContratto");
		Contratto contrattoCaricato = null;
		try {
			contrattoCaricato = contrattoService.caricaContratto(contratto, loadLazy);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaContratto");
		}
		logger.debug("--> Exit caricaContratto ");
		return contrattoCaricato;
	}

	@Override
	public List<ContrattoProspettoDTO> caricaContrattoProspetto(Integer idSedeEntita, Date data) {
		logger.debug("--> Enter caricaContrattoCalcoloPerSede");
		start("caricaContrattoCalcoloPerSede");
		List<ContrattoProspettoDTO> result = null;
		try {
			result = contrattoService.caricaContrattoProspetto(idSedeEntita, data);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaContrattoCalcoloPerSede");
		}
		logger.debug("--> Exit caricaContrattoCalcoloPerSede ");
		return result;
	}

	@Override
	public RigaContratto caricaRigaContratto(RigaContratto contrattoArticolo) {
		logger.debug("--> Enter caricaContrattoArticolo");
		start("caricaContrattoArticolo");
		RigaContratto contrattoArticoloCaricato = null;
		try {
			contrattoArticoloCaricato = contrattoService.caricaRigaContratto(contrattoArticolo);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaContrattoArticolo");
		}
		logger.debug("--> Exit caricaContrattoArticolo ");
		return contrattoArticoloCaricato;
	}

	@Override
	public List<RigaContratto> caricaRigheContratto(Contratto contratto) {
		logger.debug("--> Enter caricaContrattoArticoli");
		start("caricaContrattoArticoli");
		List<RigaContratto> contrattoArticoli = null;
		try {
			contrattoArticoli = contrattoService.caricaRigheContratto(contratto);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaContrattoArticoli");
		}
		logger.debug("--> Exit caricaContrattoArticoli ");
		return contrattoArticoli;
	}

	@Override
	public List<RigaContrattoCalcolo> caricaRigheContrattoCalcolo(Integer idSedeEntita, Date data, String codiceValuta) {
		logger.debug("--> Enter caricaRigheContrattoCalcolo");
		start("caricaRigheContrattoCalcolo");
		List<RigaContrattoCalcolo> result = null;
		try {
			result = contrattoService.caricaRigheContrattoCalcolo(idSedeEntita, data, codiceValuta);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaRigheContrattoCalcolo");
		}
		logger.debug("--> Exit caricaRigheContrattoCalcolo ");
		return result;
	}

	@Override
	public List<ContrattoStampaDTO> caricaStampaContratti(ParametriRicercaStampaContratti parametri) {
		logger.debug("--> Enter caricaStampaContratti");
		start("caricaStampaContratti");

		List<ContrattoStampaDTO> list = null;
		try {
			list = contrattoService.caricaStampaContratti(parametri);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaStampaContratti");
		}
		logger.debug("--> Exit caricaStampaContratti ");
		return list;
	}

	@Override
	public Contratto rimuoviCategoriaSedeContratto(Contratto contratto, CategoriaSedeMagazzino categoriaSedeMagazzino) {
		logger.debug("--> Enter rimuoviCategoriaSedeContratto");
		start("rimuoviCategoriaSedeContratto");
		Contratto contrattoSalvato = null;
		try {
			contrattoSalvato = contrattoService.rimuoviCategoriaSedeContratto(contratto, categoriaSedeMagazzino);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("rimuoviCategoriaSedeContratto");
		}
		logger.debug("--> Exit rimuoviCategoriaSedeContratto");
		return contrattoSalvato;
	}

	@Override
	public Contratto rimuoviEntitaContratto(Contratto contratto, EntitaLite entitaLite) {
		logger.debug("--> Enter rimuoviEntitaContratto");
		start("rimuoviEntitaContratto");
		Contratto contrattoSalvato = null;
		try {
			contrattoSalvato = contrattoService.rimuoviEntitaContratto(contratto, entitaLite);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("rimuoviEntitaContratto");
		}
		logger.debug("--> Exit rimuoviEntitaContratto ");
		return contrattoSalvato;
	}

	@Override
	public Contratto rimuoviSedeContratto(Contratto contratto, SedeMagazzinoLite sedeMagazzinoLite) {
		logger.debug("--> Enter rimuoviSedeContratto");
		start("rimuoviSedeContratto");
		Contratto contrattoSalvato = null;
		try {
			contrattoSalvato = contrattoService.rimuoviSedeContratto(contratto, sedeMagazzinoLite);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("rimuoviSedeContratto");
		}
		logger.debug("--> Exit rimuoviSedeContratto");
		return contrattoSalvato;
	}

	@Override
	public Contratto salvaContratto(Contratto contratto, boolean loadCollection) {
		logger.debug("--> Enter salvaContratto");
		start("salvaContratto");
		Contratto contrattoSalvato = null;
		try {
			contrattoSalvato = contrattoService.salvaContratto(contratto, loadCollection);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaContratto");
		}
		logger.debug("--> Exit salvaContratto ");
		return contrattoSalvato;
	}

	@Override
	public RigaContratto salvaRigaContratto(RigaContratto rigaContratto) {
		logger.debug("--> Enter salvaContrattoArticolo");
		start("salvaContrattoArticolo");
		RigaContratto contrattoArticoloSalvato = null;
		try {
			contrattoArticoloSalvato = contrattoService.salvaRigaContratto(rigaContratto);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaContrattoArticolo");
		}
		logger.debug("--> Exit salvaContrattoArticolo ");
		return contrattoArticoloSalvato;
	}

	/**
	 *
	 * @param contrattoService
	 *            service per la gestione contratto
	 */
	public void setContrattoService(ContrattoService contrattoService) {
		this.contrattoService = contrattoService;
	}

}
