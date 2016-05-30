package it.eurotn.panjea.magazzino.rich.bd;

import it.eurotn.panjea.magazzino.domain.CategoriaContabileArticolo;
import it.eurotn.panjea.magazzino.domain.CategoriaContabileDeposito;
import it.eurotn.panjea.magazzino.domain.CategoriaContabileSedeMagazzino;
import it.eurotn.panjea.magazzino.domain.DatiGenerazione.TipoGenerazione;
import it.eurotn.panjea.magazzino.domain.DepositoMagazzino;
import it.eurotn.panjea.magazzino.domain.SottoContoContabilizzazione;
import it.eurotn.panjea.magazzino.domain.SottoContoContabilizzazione.ETipoEconomico;
import it.eurotn.panjea.magazzino.service.exception.ContabilizzazioneException;
import it.eurotn.panjea.magazzino.service.interfaces.MagazzinoAnagraficaService;
import it.eurotn.panjea.magazzino.service.interfaces.MagazzinoDocumentoService;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;
import it.eurotn.panjea.rich.bd.AbstractBaseBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

import java.util.List;

import org.apache.log4j.Logger;

public class MagazzinoContabilizzazioneBD extends AbstractBaseBD implements IMagazzinoContabilizzazioneBD {

	private static Logger logger = Logger.getLogger(MagazzinoContabilizzazioneBD.class);

	public static final String BEAN_ID = "magazzinoContabilizzazioneBD";

	private MagazzinoAnagraficaService magazzinoAnagraficaService;
	private MagazzinoDocumentoService magazzinoDocumentoService;

	@Override
	public void cancellaCategoriaContabileArticolo(CategoriaContabileArticolo categoriaContabileArticolo) {
		logger.debug("--> Enter cancellaCategoriaContabileArticolo");
		start("cancellaCategoriaContabileArticolo");
		try {
			magazzinoAnagraficaService.cancellaCategoriaContabileArticolo(categoriaContabileArticolo);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaCategoriaContabileArticolo");
		}
		logger.debug("--> Exit cancellaCategoriaContabileArticolo ");

	}

	@Override
	public void cancellaCategoriaContabileDeposito(CategoriaContabileDeposito categoriaContabileDeposito) {
		logger.debug("--> Enter cancellaCategoriaContabileDeposito");
		start("cancellaCategoriaContabileDeposito");
		try {
			magazzinoAnagraficaService.cancellaCategoriaContabileDeposito(categoriaContabileDeposito);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaCategoriaContabileDeposito");
		}
		logger.debug("--> Exit cancellaCategoriaContabileDeposito ");

	}

	@Override
	public void cancellaCategoriaContabileSedeMagazzino(CategoriaContabileSedeMagazzino categoriaContabileSedeMagazzino) {
		logger.debug("--> Enter cancellaCategoriaContabileSedeMagazzino");
		start("cancellaCategoriaContabileSedeMagazzino");
		try {
			magazzinoAnagraficaService.cancellaCategoriaContabileSedeMagazzino(categoriaContabileSedeMagazzino);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaCategoriaContabileSedeMagazzino");
		}
		logger.debug("--> Exit cancellaCategoriaContabileSedeMagazzino ");

	}

	@Override
	public void cancellaDepositoMagazzino(Integer id) {
		magazzinoAnagraficaService.cancellaDepositoMagazzino(id);

	}

	@Override
	public void cancellaSottoContoContabilizzazione(Integer id) {
		logger.debug("--> Enter cancellaSottoContoContabilizzazione");
		start("cancellaSottoContoContabilizzazione");
		try {
			magazzinoAnagraficaService.cancellaSottoContoContabilizzazione(id);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaSottoContoContabilizzazione");
		}
		logger.debug("--> Exit cancellaSottoContoContabilizzazione ");
	}

	@Override
	public List<AreaMagazzinoRicerca> caricaAreeMAgazzinoDaContabilizzare(TipoGenerazione tipoGenerazione, int anno) {
		logger.debug("--> Enter caricaAreeMAgazzinoDaContabilizzare");
		start("caricaAreeMAgazzinoDaContabilizzare");
		List<AreaMagazzinoRicerca> aree = null;
		try {
			aree = magazzinoDocumentoService.caricaAreeMAgazzinoDaContabilizzare(tipoGenerazione, anno);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaAreeMAgazzinoDaContabilizzare");
		}
		logger.debug("--> Exit caricaAreeMAgazzinoDaContabilizzare ");
		return aree;
	}

	@Override
	public List<CategoriaContabileArticolo> caricaCategorieContabileArticolo(String fieldSearch, String valueSearch) {
		logger.debug("--> Enter caricaCategorieContabileArticolo");
		start("caricaCategorieContabileArticolo");
		List<CategoriaContabileArticolo> list = null;
		try {
			list = magazzinoAnagraficaService.caricaCategorieContabileArticolo(fieldSearch, valueSearch);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaCategorieContabileArticolo");
		}
		logger.debug("--> Exit caricaCategorieContabileArticolo ");
		return list;
	}

	@Override
	public List<CategoriaContabileDeposito> caricaCategorieContabileDeposito(String fieldSearch, String valueSearch) {
		logger.debug("--> Enter caricaCategorieContabileDeposito");
		start("caricaCategorieContabileDeposito");
		List<CategoriaContabileDeposito> list = null;
		try {
			list = magazzinoAnagraficaService.caricaCategorieContabileDeposito(fieldSearch, valueSearch);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaCategorieContabileDeposito");
		}
		logger.debug("--> Exit caricaCategorieContabileDeposito ");
		return list;
	}

	@Override
	public List<CategoriaContabileSedeMagazzino> caricaCategorieContabileSedeMagazzino(String fieldSearch,
			String valueSearch) {
		logger.debug("--> Enter caricaCategorieContabileSedeMagazzino");
		start("caricaCategorieContabileSedeMagazzino");
		List<CategoriaContabileSedeMagazzino> list = null;
		try {
			list = magazzinoAnagraficaService.caricaCategorieContabileSedeMagazzino(fieldSearch, valueSearch);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaCategorieContabileSedeMagazzino");
		}
		logger.debug("--> Exit caricaCategorieContabileSedeMagazzino ");
		return list;
	}

	@SuppressWarnings("cast")
	@Override
	public DepositoMagazzino caricaDepositoMagazzino(Integer id) {
		logger.debug("--> Enter caricaDepositoMagazzino");
		start("caricaDepositoMagazzino");
		DepositoMagazzino result = null;
		try {
			result = magazzinoAnagraficaService.caricaDepositoMagazzino(id);
		} catch (Exception e) {
			logger.error("--> errore ", e);
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaDepositoMagazzino");
		}
		logger.debug("--> Exit caricaDepositoMagazzino ");
		return result;
	}

	@Override
	public List<SottoContoContabilizzazione> caricaSottoContiContabilizzazione(ETipoEconomico tipoEconomico) {
		logger.debug("--> Enter caricaSottoContiContabilizzazione");
		start("caricaSottoContiContabilizzazione");
		List<SottoContoContabilizzazione> list = null;
		try {
			list = magazzinoAnagraficaService.caricaSottoContiContabilizzazione(tipoEconomico);
		} catch (Exception e) {
			logger.error("--> errore ", e);
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaSottoContiContabilizzazione");
		}
		logger.debug("--> Exit caricaSottoContiContabilizzazione ");
		return list;
	}

	@Override
	public SottoContoContabilizzazione caricaSottoContoContabilizzazione(Integer id) {
		logger.debug("--> Enter caricaSottoContoContabilizzazione");
		start("caricaSottoContoContabilizzazione");
		SottoContoContabilizzazione result = null;
		try {
			result = magazzinoAnagraficaService.caricaSottoContoContabilizzazione(id);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaSottoContoContabilizzazione");
		}
		logger.debug("--> Exit caricaSottoContoContabilizzazione ");
		return result;
	}

	@Override
	public void contabilizzaAreeMagazzino(List<Integer> idAreeMagazzino, boolean forzaGenerazioneAreaContabile)
			throws ContabilizzazioneException {
		logger.debug("--> Enter contabilizzaAreeMagazzino");
		start("contabilizzaAreeMagazzino");
		try {
			magazzinoDocumentoService.contabilizzaAreeMagazzino(idAreeMagazzino, forzaGenerazioneAreaContabile);
		} catch (ContabilizzazioneException e) {
			throw e;
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("contabilizzaAreeMagazzino");
		}
		logger.debug("--> Exit contabilizzaAreeMagazzino ");
	}

	@Override
	public void contabilizzaAreeMagazzino(List<Integer> idAreeMagazzino, boolean forzaGenerazioneAreaContabile,
			int annoContabile) throws ContabilizzazioneException {
		logger.debug("--> Enter contabilizzaAreeMagazzino");
		start("contabilizzaAreeMagazzino");
		try {
			magazzinoDocumentoService.contabilizzaAreeMagazzino(idAreeMagazzino, forzaGenerazioneAreaContabile,
					annoContabile);
		} catch (ContabilizzazioneException e) {
			throw e;
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("contabilizzaAreeMagazzino");
		}
		logger.debug("--> Exit contabilizzaAreeMagazzino ");
	}

	@Override
	public CategoriaContabileArticolo salvaCategoriaContabileArticolo(
			CategoriaContabileArticolo categoriaContabileArticolo) {
		logger.debug("--> Enter salvaCategoriaContabileArticolo");
		start("salvaCategoriaContabileArticolo");
		try {
			categoriaContabileArticolo = magazzinoAnagraficaService
					.salvaCategoriaContabileArticolo(categoriaContabileArticolo);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaCategoriaContabileArticolo");
		}
		logger.debug("--> Exit salvaCategoriaContabileArticolo ");
		return categoriaContabileArticolo;
	}

	@Override
	public CategoriaContabileDeposito salvaCategoriaContabileDeposito(
			CategoriaContabileDeposito categoriaContabileDeposito) {
		logger.debug("--> Enter salvaCategoriaContabileDeposito");
		start("salvaCategoriaContabileDeposito");
		try {
			categoriaContabileDeposito = magazzinoAnagraficaService
					.salvaCategoriaContabileDeposito(categoriaContabileDeposito);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaCategoriaContabileDeposito");
		}
		logger.debug("--> Exit salvaCategoriaContabileDeposito ");
		return categoriaContabileDeposito;
	}

	@Override
	public CategoriaContabileSedeMagazzino salvaCategoriaContabileSedeMagazzino(
			CategoriaContabileSedeMagazzino categoriaContabileSedeMagazzino) {
		logger.debug("--> Enter salvaCategoriaContabileSedeMagazzino");
		start("salvaCategoriaContabileSedeMagazzino");
		try {
			categoriaContabileSedeMagazzino = magazzinoAnagraficaService
					.salvaCategoriaContabileSedeMagazzino(categoriaContabileSedeMagazzino);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaCategoriaContabileSedeMagazzino");
		}
		logger.debug("--> Exit salvaCategoriaContabileSedeMagazzino ");
		return categoriaContabileSedeMagazzino;
	}

	@Override
	public DepositoMagazzino salvaDepositoMagazzino(DepositoMagazzino depositoMagazzino) {
		logger.debug("--> Enter salvaDepositoMagazzino");
		start("salvaDepositoMagazzino");
		DepositoMagazzino result = null;
		try {
			result = magazzinoAnagraficaService.salvaDepositoMagazzino(depositoMagazzino);
		} catch (Exception e) {
			logger.error("--> errore ", e);
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaDepositoMagazzino");
		}
		logger.debug("--> Exit salvaDepositoMagazzino ");
		return result;
	}

	@Override
	public SottoContoContabilizzazione salvaSottoContoContabilizzazione(
			SottoContoContabilizzazione sottoContoContabilizzazione) {
		logger.debug("--> Enter salvaSottoContoContabilizzazione");
		start("salvaSottoContoContabilizzazione");
		SottoContoContabilizzazione result = null;
		try {
			result = magazzinoAnagraficaService.salvaSottoContoContabilizzazione(sottoContoContabilizzazione);
		} catch (Exception e) {
			logger.error("--> errore ", e);
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaSottoContoContabilizzazione");
		}
		logger.debug("--> Exit salvaSottoContoContabilizzazione ");
		return result;
	}

	/**
	 * @param magazzinoAnagraficaService
	 *            the magazzinoAnagraficaService to set
	 */
	public void setMagazzinoAnagraficaService(MagazzinoAnagraficaService magazzinoAnagraficaService) {
		this.magazzinoAnagraficaService = magazzinoAnagraficaService;
	}

	/**
	 * @param magazzinoDocumentoService
	 *            the magazzinoDocumentoService to set
	 */
	public void setMagazzinoDocumentoService(MagazzinoDocumentoService magazzinoDocumentoService) {
		this.magazzinoDocumentoService = magazzinoDocumentoService;
	}
}
