package it.eurotn.panjea.conai.rich.bd;

import it.eurotn.panjea.conai.domain.ConaiArticolo;
import it.eurotn.panjea.conai.domain.ConaiComponente;
import it.eurotn.panjea.conai.domain.ConaiParametriCreazione;
import it.eurotn.panjea.conai.service.interfaces.ConaiService;
import it.eurotn.panjea.conai.util.AnalisiConaiDTO;
import it.eurotn.panjea.conai.util.parametriricerca.ParametriRicercaAnalisi;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.rich.bd.AbstractBaseBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class ConaiBD extends AbstractBaseBD implements IConaiBD {

	public static final String BEAN_ID = "conaiBD";

	private static Logger logger = Logger.getLogger(ConaiBD.class);

	private ConaiService conaiService;

	@Override
	public void cancellaArticoloConai(ConaiArticolo conaiArticolo) {
	}

	@Override
	public void cancellaComponenteConai(ConaiComponente conaiComponente) {
		logger.debug("--> Enter cancellaComponenteConai");
		start("cancellaComponenteConai");
		try {
			conaiService.cancellaComponenteConai(conaiComponente);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaComponenteConai");
		}
		logger.debug("--> Exit cancellaComponenteConai ");
	}

	@Override
	public List<AnalisiConaiDTO> caricaAnalisiConali(ParametriRicercaAnalisi parametri) {
		logger.debug("--> Enter caricaAnalisiConali");
		start("caricaAnalisiConali");
		List<AnalisiConaiDTO> analisi = null;
		try {
			analisi = conaiService.caricaAnalisiConali(parametri);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaAnalisiConali");
		}
		logger.debug("--> Exit caricaAnalisiConali ");
		return analisi;
	}

	@Override
	public List<ConaiArticolo> caricaArticoliConai() {
		logger.debug("--> Enter caricaArticoliConai");
		start("caricaArticoliConai");
		List<ConaiArticolo> result = null;
		try {
			result = conaiService.caricaArticoliConai();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaArticoliConai");
		}
		logger.debug("--> Exit caricaArticoliConai ");
		return result;
	}

	@Override
	public List<ConaiComponente> caricaComponentiConai(ArticoloLite articolo) {
		logger.debug("--> Enter caricaComponentiConai");
		start("caricaComponentiConai");
		List<ConaiComponente> result = new ArrayList<ConaiComponente>();
		try {
			result = conaiService.caricaComponentiConai(articolo);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaComponentiConai");
		}
		logger.debug("--> Exit caricaComponentiConai ");
		return result;
	}

	@Override
	public List<TipoAreaMagazzino> caricaTipiAreaMagazzinoConGestioneConai() {
		logger.debug("--> Enter caricaTipiAreaMagazzinoConGestioneConai");
		start("caricaTipiAreaMagazzinoConGestioneConai");

		List<TipoAreaMagazzino> tipiArea = null;
		try {
			tipiArea = conaiService.caricaTipiAreaMagazzinoConGestioneConai();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaTipiAreaMagazzinoConGestioneConai");
		}
		logger.debug("--> Exit caricaTipiAreaMagazzinoConGestioneConai ");
		return tipiArea;
	}

	@Override
	public byte[] generaModulo(ConaiParametriCreazione parametri) {
		logger.debug("--> Enter generaModulo");
		start("generaModulo");
		byte[] result = null;
		try {
			result = conaiService.generaModulo(parametri);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("generaModulo");
		}
		logger.debug("--> Exit generaModulo ");
		return result;
	}

	@Override
	public ConaiArticolo salvaArticoloConai(ConaiArticolo conaiArticolo) {
		logger.debug("--> Enter salvaArticoloConai");
		start("salvaArticoloConai");
		ConaiArticolo result = null;
		try {
			result = conaiService.salvaArticoloConai(conaiArticolo);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaArticoloConai");
		}
		logger.debug("--> Exit salvaArticoloConai ");
		return result;
	}

	@Override
	public ConaiComponente salvaComponenteConai(ConaiComponente conaiComponente) {
		logger.debug("--> Enter salvaComponenteConai");
		start("salvaComponenteConai");
		ConaiComponente result = null;
		try {
			result = conaiService.salvaComponenteConai(conaiComponente);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaComponenteConai");
		}
		logger.debug("--> Exit salvaComponenteConai ");
		return result;
	}

	/**
	 * @param conaiService
	 *            The conaiService to set.
	 */
	public void setConaiService(ConaiService conaiService) {
		this.conaiService = conaiService;
	}
}
