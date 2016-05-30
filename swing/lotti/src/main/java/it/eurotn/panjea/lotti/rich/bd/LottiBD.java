package it.eurotn.panjea.lotti.rich.bd;

import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.lotti.domain.Lotto;
import it.eurotn.panjea.lotti.domain.LottoInterno;
import it.eurotn.panjea.lotti.domain.RigaLotto;
import it.eurotn.panjea.lotti.service.interfaces.LottiService;
import it.eurotn.panjea.lotti.util.MovimentazioneLotto;
import it.eurotn.panjea.lotti.util.ParametriRicercaScadenzaLotti;
import it.eurotn.panjea.lotti.util.StatisticaLotto;
import it.eurotn.panjea.magazzino.domain.Articolo.TipoLotto;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.TipoMovimento;
import it.eurotn.panjea.rich.bd.AbstractBaseBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

public class LottiBD extends AbstractBaseBD implements ILottiBD {

	private static Logger logger = Logger.getLogger(LottiBD.class);

	public static final String BEAN_ID = "lottiBD";

	private LottiService lottiService;

	@Override
	public List<ArticoloLite> caricaArticoliByCodiceLotto(Lotto lotto) {
		logger.debug("--> Enter caricaArticoliByCodiceLotto");
		start("caricaArticoliByCodiceLotto");
		List<ArticoloLite> articoli = new ArrayList<ArticoloLite>();
		try {
			articoli = lottiService.caricaArticoliByCodiceLotto(lotto);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaArticoliByCodiceLotto");
		}
		logger.debug("--> Exit caricaArticoliByCodiceLotto ");
		return articoli;
	}

	@Override
	public List<ArticoloLite> caricaArticoliByCodiceLotto(Lotto lotto, boolean filtraDataScadenza) {
		logger.debug("--> Enter caricaArticoliByCodiceLotto");
		start("caricaArticoliByCodiceLotto");
		List<ArticoloLite> articoli = new ArrayList<ArticoloLite>();
		try {
			articoli = lottiService.caricaArticoliByCodiceLotto(lotto, filtraDataScadenza);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaArticoliByCodiceLotto");
		}
		logger.debug("--> Exit caricaArticoliByCodiceLotto ");
		return articoli;
	}

	@Override
	public List<Lotto> caricaLotti(ArticoloLite articolo, DepositoLite deposito, TipoMovimento tipoMovimento,
			boolean storno, String codice,Date dataScadenza, Date dataInizioRicercaScadenza,boolean cercaSoloLottiAperti) {
		logger.debug("--> Enter caricaLotti");
		start("caricaLotti");
		List<Lotto> lotti = new ArrayList<Lotto>();
		try {
			lotti = lottiService.caricaLotti(articolo, deposito, tipoMovimento, storno, codice,dataScadenza,dataInizioRicercaScadenza,cercaSoloLottiAperti);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaLotti");
		}
		logger.debug("--> Exit caricaLotti ");
		return lotti;
	}

	@Override
	public List<Lotto> caricaLotti(ArticoloLite articolo, TipoLotto tipoLotto, String codice) {
		logger.debug("--> Enter caricaLotti");
		start("caricaLotti");
		List<Lotto> lotti = null;
		try {
			lotti = lottiService.caricaLotti(articolo, tipoLotto, codice);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaLotti");
		}
		logger.debug("--> Exit caricaLotti ");
		return lotti;
	}

	@Override
	public List<StatisticaLotto> caricaLottiInScadenza(ParametriRicercaScadenzaLotti parametri) {
		logger.debug("--> Enter caricaLottiInScadenza");
		start("caricaLottiInScadenza");

		List<StatisticaLotto> lotti = null;
		try {
			lotti = lottiService.caricaLottiInScadenza(parametri);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaLottiInScadenza");
		}
		logger.debug("--> Exit caricaLottiInScadenza ");
		return lotti;
	}

	@Override
	public Lotto caricaLotto(Lotto lotto) {
		logger.debug("--> Enter caricaLotto");
		start("caricaLotto");
		Lotto lottoCaricato = null;
		try {
			lottoCaricato = lottiService.caricaLotto(lotto);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaLotto");
		}
		logger.debug("--> Exit caricaLotto ");
		return lottoCaricato;
	}

	@Override
	public Lotto caricaLotto(String codice, Date dataScadenza, int idArticolo) {
		logger.debug("--> Enter caricaLotto");
		start("caricaLotto");
		Lotto result = null;
		try {
			result = lottiService.caricaLotto(codice, dataScadenza, idArticolo);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaLotto");
		}
		logger.debug("--> Exit caricaLotto ");
		return result;
	}

	@Override
	public List<MovimentazioneLotto> caricaMovimentazioneLotto(Lotto lotto) {
		logger.debug("--> Enter caricaMovimentazioneLotto");
		start("caricaMovimentazioneLotto");
		List<MovimentazioneLotto> listResult = null;
		try {
			listResult = lottiService.caricaMovimentazioneLotto(lotto);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaMovimentazioneLotto");
		}
		logger.debug("--> Exit caricaMovimentazioneLotto ");
		return listResult;
	}

	@Override
	public List<MovimentazioneLotto> caricaMovimentazioneLottoInterno(LottoInterno lotto) {
		logger.debug("--> Enter caricaMovimentazioneLottoInterno");
		start("caricaMovimentazioneLottoInterno");
		List<MovimentazioneLotto> listResult = null;
		try {
			listResult = lottiService.caricaMovimentazioneLottoInterno(lotto);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaMovimentazioneLottoInterno");
		}
		logger.debug("--> Exit caricaMovimentazioneLottoInterno ");
		return listResult;
	}

	@Override
	public List<RigaLotto> caricaRigheLotto(AreaMagazzino areaMagazzino) {
		logger.debug("--> Enter caricaRigheLotto");
		start("caricaRigheLotto");
		List<RigaLotto> lotti = null;
		try {
			lotti = lottiService.caricaRigheLotto(areaMagazzino);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaRigheLotto");
		}
		logger.debug("--> Exit caricaRigheLotto");
		return lotti;
	}

	@Override
	public List<RigaLotto> caricaRigheLotto(RigaArticolo rigaArticolo) {
		logger.debug("--> Enter caricaRigheLotto");
		start("caricaRigheLotto");
		List<RigaLotto> righeLotto = null;
		try {
			righeLotto = lottiService.caricaRigheLotto(rigaArticolo);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaRigheLotto");
		}
		logger.debug("--> Exit caricaRigheLotto ");
		return righeLotto;
	}

	@Override
	public List<StatisticaLotto> caricaSituazioneLotti(ArticoloLite articoloLite) {
		logger.debug("--> Enter caricaSituazioneLotti");
		start("caricaSituazioneLotti");
		List<StatisticaLotto> statistiche = null;
		try {
			statistiche = lottiService.caricaSituazioneLotti(articoloLite);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaSituazioneLotti");
		}
		logger.debug("--> Exit caricaSituazioneLotti ");
		return statistiche;
	}

	@Override
	public Lotto salvaLotto(Lotto lotto) {
		logger.debug("--> Enter salvaLotto");
		start("salvaLotto");

		Lotto lottoSalvato = null;
		try {
			lottoSalvato = lottiService.salvaLotto(lotto);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaLotto");
		}
		logger.debug("--> Exit salvaLotto ");
		return lottoSalvato;
	}

	/**
	 * @param lottiService
	 *            the lottiService to set
	 */
	public void setLottiService(LottiService lottiService) {
		this.lottiService = lottiService;
	}

}
