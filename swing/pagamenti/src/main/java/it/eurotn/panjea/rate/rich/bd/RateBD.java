package it.eurotn.panjea.rate.rich.bd;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;
import it.eurotn.panjea.contabilita.util.AreaContabileFullDTO;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.partite.domain.AreaPartite;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rate.domain.CalendarioRate;
import it.eurotn.panjea.rate.domain.Rata;
import it.eurotn.panjea.rate.service.interfaces.RateService;
import it.eurotn.panjea.rich.bd.AbstractBaseBD;
import it.eurotn.panjea.tesoreria.util.RataRiemessa;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

public class RateBD extends AbstractBaseBD implements IRateBD {
	private static Logger logger = Logger.getLogger(RateBD.class);
	private RateService rateService;

	@Override
	public void cancellaRata(Rata rata) {
		logger.debug("--> Enter cancellaRataPartita");
		start("cancellaRataPartita");
		try {
			rateService.cancellaRata(rata);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaRataPartita");
		}
		logger.debug("--> Exit cancellaRataPartita ");
	}

	@Override
	public AreaContabileFullDTO caricaAreaContabileFullDTO(Integer idAreaContabile) {
		logger.debug("--> Enter caricaAreaContabileFullDTO");
		start("caricaAreaContabileFullDTO");
		AreaContabileFullDTO areaContabileFullDTO = null;
		try {
			areaContabileFullDTO = rateService.caricaAreaContabileFullDTO(idAreaContabile);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaAreaContabileFullDTO");
		}
		logger.debug("--> Exit caricaAreaContabileFullDTO");
		return areaContabileFullDTO;
	}

	@Override
	public AreaMagazzinoFullDTO caricaAreaMagazzinoFullDTO(Integer id) {
		logger.debug("--> Enter caricaAreaMagazzinoFullDTO");
		start("caricaAreaMagazzinoFullDTO");
		AreaMagazzinoFullDTO areaMagazzinoFullDTO = null;
		try {
			areaMagazzinoFullDTO = rateService.caricaAreaMagazzinoFullDTO(id);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaAreaMagazzinoFullDTO");
		}
		logger.debug("--> Exit caricaAreaMagazzinoFullDTO ");
		return areaMagazzinoFullDTO;
	}

	@Override
	public AreaRate caricaAreaRateByDocumento(Integer idDocumento) {
		logger.debug("--> Enter caricaAreaRateByDocumento");
		start("caricacaricaAreaRateByDocumento");
		AreaRate areaRate = null;
		try {
			Documento documento = new Documento();
			documento.setId(idDocumento);
			areaRate = rateService.caricaAreaRate(documento);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaAreaRateByDocumento");
		}
		logger.debug("--> Exit caricaAreaRateByDocumento");
		return areaRate;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Rata> caricaRateCollegate(Rata rata) {
		logger.debug("--> Enter caricaRateCollegate");
		start("caricaRateCollegate");

		List<Rata> rateCollegate = null;
		try {
			rateCollegate = rateService.caricaRateCollegate(rata);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaRateCollegate");
		}
		logger.debug("--> Exit caricaRateCollegate ");
		return rateCollegate;
	}

	@Override
	public List<Rata> generaRate(CodicePagamento codicePagamento, Date dataDocumento, BigDecimal imponibile,
			BigDecimal iva, CalendarioRate calendarioRate) {
		logger.debug("--> Enter generaRate");
		List<Rata> rate = null;
		start("generaRate");
		try {
			rate = rateService.generaRate(codicePagamento, dataDocumento, imponibile, iva, calendarioRate);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("generaRate");
		}
		logger.debug("--> Exit generaRate ");
		return rate;
	}

	/**
	 * @return the rateService
	 */
	public RateService getRateService() {
		return rateService;
	}

	@Override
	public void riemettiRate(RataRiemessa rataRiemessaDTO) {
		logger.debug("--> Enter riemettiRate");
		start("riemettiRate");
		try {
			rateService.riemettiRate(rataRiemessaDTO);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("riemettiRate");
		}
		logger.debug("--> Exit riemettiRate ");
	}

	@Override
	public Rata salvaRata(Rata rata) {
		logger.debug("--> Enter salvaRataPartita");
		start("salvaRataPartita");
		Rata rataSalvata = null;
		try {
			rataSalvata = rateService.salvaRata(rata);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaRataPartita");
		}
		logger.debug("--> Exit salvaRataPartita");
		return rataSalvata;
	}

	@Override
	public Rata salvaRataNoCheck(Rata rata) {
		logger.debug("--> Enter salvaRataPartita");
		start("salvaRataPartita");
		Rata rataSalvata = null;
		try {
			rataSalvata = rateService.salvaRataNoCheck(rata);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaRataPartita");
		}
		logger.debug("--> Exit salvaRataPartita");
		return rataSalvata;
	}

	/**
	 * @param rateService
	 *            the rateService to set
	 */
	public void setRateService(RateService rateService) {
		this.rateService = rateService;
	}

	@Override
	public AreaPartite validaRateAreaPartita(AreaRate areaRate, IAreaDocumento areaDocumento) {
		logger.debug("--> Enter validaRateAreaPartita");
		start("salvaRataPartita");
		AreaRate areaRateResult = null;
		try {
			areaRateResult = rateService.validaAreaRate(areaRate, areaDocumento);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaRataPartita");
		}
		logger.debug("--> Exit validaRateAreaPartita");
		return areaRateResult;
	}
}
