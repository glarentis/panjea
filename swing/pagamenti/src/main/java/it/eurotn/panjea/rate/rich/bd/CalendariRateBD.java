package it.eurotn.panjea.rate.rich.bd;

import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.partite.domain.CategoriaRata;
import it.eurotn.panjea.rate.domain.CalendarioRate;
import it.eurotn.panjea.rate.domain.RigaCalendarioRate;
import it.eurotn.panjea.rate.service.interfaces.CalendariRateService;
import it.eurotn.panjea.rich.bd.AbstractBaseBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class CalendariRateBD extends AbstractBaseBD implements ICalendariRateBD {

	private static Logger logger = Logger.getLogger(CalendariRateBD.class);

	public static final String BEAN_ID = "calendariRateBD";

	private CalendariRateService calendariRateService;

	@Override
	public void cancellaCalendarioRate(CalendarioRate calendarioRate) {
		logger.debug("--> Enter cancellaCalendarioRate");
		start("cancellaCalendarioRate");
		try {
			calendariRateService.cancellaCalendarioRate(calendarioRate);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaCalendarioRate");
		}
		logger.debug("--> Exit cancellaCalendarioRate ");
	}

	@Override
	public void cancellaRigaCalendarioRate(RigaCalendarioRate rigaCalendarioRate) {
		logger.debug("--> Enter cancellaRigaCalendarioRate");
		start("cancellaRigaCalendarioRate");
		try {
			calendariRateService.cancellaRigaCalendarioRate(rigaCalendarioRate);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaRigaCalendarioRate");
		}
		logger.debug("--> Exit cancellaRigaCalendarioRate ");
	}

	@Override
	public CalendarioRate caricaCalendarioRate(CalendarioRate calendarioRate, boolean loadLazy) {
		logger.debug("--> Enter caricaCalendarioRate");
		start("caricaCalendarioRate");
		CalendarioRate calendarioCaricato = null;
		try {
			calendarioCaricato = calendariRateService.caricaCalendarioRate(calendarioRate, loadLazy);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaCalendarioRate");
		}
		logger.debug("--> Exit caricaCalendarioRate ");
		return calendarioCaricato;
	}

	@Override
	public List<CalendarioRate> caricaCalendariRate(String fieldSearch, String valueSearch) {
		logger.debug("--> Enter caricaCalendariRate");
		start("caricaCalendariRate");

		List<CalendarioRate> calendari = null;
		try {
			calendari = calendariRateService.caricaCalendariRate(fieldSearch, valueSearch);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaCalendariRate");
		}
		logger.debug("--> Exit caricaCalendariRate ");
		return calendari;
	}

	@Override
	public List<CalendarioRate> caricaCalendariRateAzienda() {
		logger.debug("--> Enter caricaCalendariRateAzienda");
		start("caricaCalendariRateAzienda");
		List<CalendarioRate> calendari = null;
		try {
			calendari = calendariRateService.caricaCalendariRateAzienda();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaCalendariRateAzienda");
		}
		logger.debug("--> Exit caricaCalendariRateAzienda ");
		return calendari;
	}

	@Override
	public List<CalendarioRate> caricaCalendariRateCliente(ClienteLite clienteLite) {
		logger.debug("--> Enter caricaCalendariRateCliente");
		start("caricaCalendariRateCliente");
		List<CalendarioRate> calendari = null;
		try {
			calendari = calendariRateService.caricaCalendariRateCliente(clienteLite);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaCalendariRateCliente");
		}
		logger.debug("--> Exit caricaCalendariRateCliente ");
		return calendari;
	}

	@Override
	public List<ClienteLite> caricaClientiNonDisponibiliPerCalendario(List<CategoriaRata> categorieRateDaScludere) {
		logger.debug("--> Enter caricaClientiNonDisponibiliPerCalendario");
		start("caricaClientiNonDisponibiliPerCalendario");
		List<ClienteLite> clienti = null;
		try {
			clienti = calendariRateService.caricaClientiNonDisponibiliPerCalendario(categorieRateDaScludere);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaClientiNonDisponibiliPerCalendario");
		}
		logger.debug("--> Exit caricaClientiNonDisponibiliPerCalendario ");
		return clienti;
	}

	@Override
	public List<RigaCalendarioRate> caricaRigheCalendarioRate(CalendarioRate calendarioRate) {
		logger.debug("--> Enter caricaRigheCalendarioRate");
		start("caricaRigheCalendarioRate");
		List<RigaCalendarioRate> righeCalendario = new ArrayList<RigaCalendarioRate>();
		try {
			righeCalendario = calendariRateService.caricaRigheCalendarioRate(calendarioRate);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaRigheCalendarioRate");
		}
		logger.debug("--> Exit caricaRigheCalendarioRate ");
		return righeCalendario;
	}

	@Override
	public CalendarioRate salvaCalendarioRate(CalendarioRate calendarioRate) {
		logger.debug("--> Enter salvaCalendarioRate");
		start("salvaCalendarioRate");

		CalendarioRate calendarioRateSalvato = null;
		try {
			calendarioRateSalvato = calendariRateService.salvaCalendarioRate(calendarioRate);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaCalendarioRate");
		}
		logger.debug("--> Exit salvaCalendarioRate ");
		return calendarioRateSalvato;
	}

	@Override
	public RigaCalendarioRate salvaRigaCalendarioRate(RigaCalendarioRate rigaCalendarioRate) {
		logger.debug("--> Enter salvaRigaCalendarioRate");
		start("salvaRigaCalendarioRate");

		RigaCalendarioRate rigaSalvata = null;
		try {
			rigaSalvata = calendariRateService.salvaRigaCalendarioRate(rigaCalendarioRate);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaRigaCalendarioRate");
		}
		logger.debug("--> Exit salvaRigaCalendarioRate ");
		return rigaSalvata;
	}

	/**
	 * @param calendariRateService
	 *            the calendariRateService to set
	 */
	public void setCalendariRateService(CalendariRateService calendariRateService) {
		this.calendariRateService = calendariRateService;
	}

}
