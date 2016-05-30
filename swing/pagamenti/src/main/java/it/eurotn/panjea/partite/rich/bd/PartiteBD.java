package it.eurotn.panjea.partite.rich.bd;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.contabilita.domain.ContoBase;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.contabilita.service.interfaces.ContabilitaAnagraficaService;
import it.eurotn.panjea.partite.domain.CategoriaRata;
import it.eurotn.panjea.partite.domain.RigaStrutturaPartite;
import it.eurotn.panjea.partite.domain.StrutturaPartita;
import it.eurotn.panjea.partite.domain.StrutturaPartitaLite;
import it.eurotn.panjea.partite.domain.TipoAreaPartita;
import it.eurotn.panjea.partite.domain.TipoAreaPartita.TipoPartita;
import it.eurotn.panjea.partite.domain.TipoDocumentoBasePartite;
import it.eurotn.panjea.partite.domain.TipoDocumentoBasePartite.TipoOperazione;
import it.eurotn.panjea.partite.service.interfaces.PartiteService;
import it.eurotn.panjea.rich.bd.AbstractBaseBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class PartiteBD extends AbstractBaseBD implements IPartiteBD {

	private static Logger logger = Logger.getLogger(PartiteBD.class);

	public static final String BEAN_ID = "partiteBD";

	private PartiteService partiteService;

	private ContabilitaAnagraficaService contabilitaAnagraficaService;

	@Override
	public void cancellaCategoriaRata(Integer id) {
		logger.debug("--> Enter cancellaCategoriaRata");
		start("cancellaCategoriaRata");
		try {
			CategoriaRata categoriaRata = new CategoriaRata();
			categoriaRata.setId(id);
			partiteService.cancellaCategoriaRata(categoriaRata);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaCategoriaRata");
		}
		logger.debug("--> Exit cancellaCategoriaRata ");
	}

	@Override
	public void cancellaRigaStrutturaPartite(RigaStrutturaPartite rigaStrutturaPartite) {
		logger.debug("--> Enter cancellaRigaStrutturaPartite");
		start("cancellaRigaStrutturaPartite");
		try {
			partiteService.cancellaRigaStrutturaPartite(rigaStrutturaPartite);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaRigaStrutturaPartite");
		}
		logger.debug("--> Exit cancellaRigaStrutturaPartite ");
	}

	@Override
	public void cancellaStrutturaPartita(Integer id) {
		logger.debug("--> Enter cancellaStrutturaPartita");
		start("cancellaStrutturaPartita");
		try {
			StrutturaPartita strutturaPartita = new StrutturaPartita();
			strutturaPartita.setId(id);
			partiteService.cancellaStrutturaPartita(strutturaPartita);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaStrutturaPartita");
		}
		logger.debug("--> Exit cancellaStrutturaPartita");
	}

	@Override
	public void cancellaTipoAreaPartita(TipoAreaPartita tipoAreaPartita) {
		logger.debug("--> Enter cancellaTipoAreaPartita");
		start("cancellaTipoAreaPartita");
		try {
			partiteService.cancellaTipoAreaPartita(tipoAreaPartita);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaTipoAreaPartita");
		}
		logger.debug("--> Exit cancellaTipoAreaPartita ");
	}

	@Override
	public void cancellaTipoDocumentoBase(TipoDocumentoBasePartite tipoDocumentoBase) {
		logger.debug("--> Enter cancellaTipoDocumentoBase");
		start("cancellaTipoDocumentoBase");
		try {
			partiteService.cancellaTipoDocumentoBase(tipoDocumentoBase);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaTipoDocumentoBase");
		}
		logger.debug("--> Exit cancellaTipoDocumentoBase ");
	}

	@Override
	public CategoriaRata caricaCategoriaRata(Integer id) {
		logger.debug("--> Enter caricaCategoriaRata");
		start("caricaCategoriaRata");
		CategoriaRata categoriaRata = null;
		try {
			categoriaRata = partiteService.caricaCategoriaRata(id);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaCategoriaRata");
		}
		logger.debug("--> Exit caricaCategoriaRata ");
		return categoriaRata;
	}

	@Override
	public List<CategoriaRata> caricaCategorieRata(String fieldSearch, String valueSearch) {
		logger.debug("--> Enter caricaCategorie");
		start("caricaCategorie");
		List<CategoriaRata> categorieList = null;
		try {
			categorieList = partiteService.caricaCategorieRata(fieldSearch, valueSearch);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaCategorie");
		}
		logger.debug("--> Exit caricaCategorie");
		return categorieList;
	}

	@Override
	public List<ContoBase> caricaContiBase() {
		logger.debug("--> Enter caricaContiBase");
		List<ContoBase> list = null;
		try {
			list = contabilitaAnagraficaService.caricaContiBase();
		} catch (ContabilitaException e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		}
		logger.debug("--> Exit caricaContiBase");
		return list;
	}

	@Override
	public StrutturaPartita caricaStrutturaPartita(Integer id) {
		logger.debug("--> Enter caricaStruturaPartita");
		start("caricaStrutturaPartita");
		StrutturaPartita strutturaPartita = null;
		try {
			strutturaPartita = partiteService.caricaStrutturaPartita(id);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaStrutturaPartita");
		}
		logger.debug("--> Exit caricaStrutturaPartita");
		return strutturaPartita;
	}

	@Override
	public List<StrutturaPartitaLite> caricaStrutturePartita() {
		logger.debug("--> Enter caricaStrutturePartita");
		start("caricaStrutturePartita");
		List<StrutturaPartitaLite> struttureList = null;
		try {
			struttureList = partiteService.caricaStrutturePartita();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaStrutturePartita");
		}
		logger.debug("--> Exit caricaStrutturePartita");
		return struttureList;
	}

	@Override
	public List<TipoAreaPartita> caricaTipiAreaPartitaPerPagamenti(String fieldSearch, String valueSearch,
			TipoPartita tipoPartita, boolean loadTipiDocumentoDisabilitati, boolean escludiTipiAreaPartiteDistinta) {
		logger.debug("--> Enter caricaTipiAreaPartita");
		start("caricaTipiAreaPartita");
		List<TipoAreaPartita> list = new ArrayList<TipoAreaPartita>();
		try {
			list = partiteService.caricaTipiAreaPartitaPerPagamenti(fieldSearch, valueSearch, tipoPartita,
					loadTipiDocumentoDisabilitati, escludiTipiAreaPartiteDistinta);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaTipiAreaPartita");
		}
		logger.debug("--> Exit caricaTipiAreaPartita ");
		return list;
	}

	@Override
	public List<TipoDocumentoBasePartite> caricaTipiDocumentoBase() {
		logger.debug("--> Enter caricaTipiDocumentoBase");
		start("caricaTipiDocumentoBase");
		List<TipoDocumentoBasePartite> list = new ArrayList<TipoDocumentoBasePartite>();
		try {
			list = partiteService.caricaTipiDocumentoBase();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaTipiDocumentoBase");
		}
		logger.debug("--> Exit caricaTipiDocumentoBase ");
		return list;
	}

	@Override
	public TipoAreaPartita caricaTipoAreaPartita(TipoAreaPartita tipoAreaPartita) {
		logger.debug("--> Enter caricaTipoAreaPartita");
		start("caricaTipoAreaPartita");
		TipoAreaPartita tipoAreaPartitaCaricata = null;
		try {
			tipoAreaPartitaCaricata = partiteService.caricaTipoAreaPartita(tipoAreaPartita);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaTipoAreaPartita");
		}
		logger.debug("--> Exit caricaTipoAreaPartita ");
		return tipoAreaPartitaCaricata;
	}

	@Override
	public TipoAreaPartita caricaTipoAreaPartitaPerTipoDocumento(TipoDocumento tipoDocumento) {
		logger.debug("--> Enter caricaTipoAreaPartitaPerTipoDocumento");
		start("caricaTipoAreaPartitaPerTipoDocumento");
		TipoAreaPartita tipoAreaPartita = null;
		try {
			tipoAreaPartita = partiteService.caricaTipoAreaPartitaPerTipoDocumento(tipoDocumento);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaTipoAreaPartitaPerTipoDocumento");
		}
		logger.debug("--> Exit caricaTipoAreaPartitaPerTipoDocumento ");
		return tipoAreaPartita;
	}

	@Override
	public TipoDocumentoBasePartite caricaTipoDocumentoBase(TipoOperazione tipoOperazione) {
		logger.debug("--> Enter caricaTipoDocumentoBase");
		start("caricaTipoDocumentoBase");
		TipoDocumentoBasePartite tipoDocumentoBasePartite = null;
		try {
			tipoDocumentoBasePartite = partiteService.caricaTipoDocumentoBase(tipoOperazione);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaTipoDocumentoBase");
		}
		logger.debug("--> Exit caricaTipoDocumentoBase");
		return tipoDocumentoBasePartite;
	}

	@Override
	public List<RigaStrutturaPartite> creaRigheStrutturaPartite(StrutturaPartita strutturaPartita, int rate,
			int intervallo) {
		logger.debug("--> Enter creaRigheStrutturaPartite");
		start("creaRigheStrutturaPartite");
		List<RigaStrutturaPartite> list = null;
		try {
			list = partiteService.creaRigheStrutturaPartite(strutturaPartita, rate, intervallo);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("creaRigheStrutturaPartite");
		}
		logger.debug("--> Exit creaRigheStrutturaPartite ");
		return list;
	}

	/**
	 * @return the contabilitaAnagraficaService
	 */
	public ContabilitaAnagraficaService getContabilitaAnagraficaService() {
		return contabilitaAnagraficaService;
	}

	@Override
	public CategoriaRata salvaCategoriaRata(CategoriaRata newCategoriaRata) {
		logger.debug("--> Enter salvaCategoriaRata");
		start("salvaCategoriaRata");
		CategoriaRata categoriaRata = null;
		categoriaRata = partiteService.salvaCategoriaRata(newCategoriaRata);
		end("salvaCategoriaRata");
		logger.debug("--> Exit salvaCategoriaRata");
		return categoriaRata;
	}

	@Override
	public RigaStrutturaPartite salvaRigaStrutturaPartite(RigaStrutturaPartite rigaStrutturaPartite) {
		logger.debug("--> Enter salvaRigaStrutturaPartite");
		start("salvaRigaStrutturaPartite");
		RigaStrutturaPartite rigaStrutturaPartiteSalvata = null;
		try {
			rigaStrutturaPartiteSalvata = partiteService.salvaRigaStrutturaPartite(rigaStrutturaPartite);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaRigaStrutturaPartite");
		}
		logger.debug("--> Exit salvaRigaStrutturaPartite ");
		return rigaStrutturaPartiteSalvata;
	}

	@Override
	public TipoAreaPartita salvaTipoAreaPartita(TipoAreaPartita tipoAreaPartita) {
		logger.debug("--> Enter salvaTipoAreaPartita");
		start("salvaTipoAreaPartita");
		TipoAreaPartita tipoAreaPartitaSalvata = null;
		try {
			tipoAreaPartitaSalvata = partiteService.salvaTipoAreaPartita(tipoAreaPartita);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaTipoAreaPartita");
		}
		logger.debug("--> Exit salvaTipoAreaPartita ");
		return tipoAreaPartitaSalvata;
	}

	@Override
	public TipoDocumentoBasePartite salvaTipoDocumentoBase(TipoDocumentoBasePartite tipoDocumentoBase) {
		logger.debug("--> Enter salvaTipoDocumentoBase");
		start("salvaTipoDocumentoBase");
		TipoDocumentoBasePartite tipoDocumentoBaseSalvato = null;
		try {
			tipoDocumentoBaseSalvato = partiteService.salvaTipoDocumentoBase(tipoDocumentoBase);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaTipoDocumentoBase");
		}
		logger.debug("--> Exit salvaTipoDocumentoBase");
		return tipoDocumentoBaseSalvato;
	}

	@Override
	public StrutturaPartita salveStrutturaPartita(StrutturaPartita newStrutturaPartita) {
		logger.debug("--> Enter salveStrutturaPartita");
		start("salveStrutturaPartita");
		StrutturaPartita strutturaPartita = null;
		try {

			strutturaPartita = partiteService.salvaStrutturaPartita(newStrutturaPartita);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salveStrutturaPartita");
		}
		logger.debug("--> Exit salveStrutturaPartita ");
		return strutturaPartita;
	}

	/**
	 * @param contabilitaAnagraficaService
	 *            the contabilitaAnagraficaService to set
	 */
	public void setContabilitaAnagraficaService(ContabilitaAnagraficaService contabilitaAnagraficaService) {
		this.contabilitaAnagraficaService = contabilitaAnagraficaService;
	}

	/**
	 * @param partiteService
	 *            The partiteService to set.
	 */
	public void setPartiteService(PartiteService partiteService) {
		this.partiteService = partiteService;
	}
}