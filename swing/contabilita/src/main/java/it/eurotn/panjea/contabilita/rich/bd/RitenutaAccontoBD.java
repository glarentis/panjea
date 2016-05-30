/**
 *
 */
package it.eurotn.panjea.contabilita.rich.bd;

import it.eurotn.panjea.contabilita.domain.CausaleRitenutaAcconto;
import it.eurotn.panjea.contabilita.domain.ContributoPrevidenziale;
import it.eurotn.panjea.contabilita.domain.Prestazione;
import it.eurotn.panjea.contabilita.service.interfaces.RitenutaAccontoService;
import it.eurotn.panjea.contabilita.util.ParametriSituazioneRitenuteAcconto;
import it.eurotn.panjea.contabilita.util.SituazioneRitenuteAccontoDTO;
import it.eurotn.panjea.rich.bd.AbstractBaseBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author fattazzo
 *
 */
public class RitenutaAccontoBD extends AbstractBaseBD implements IRitenutaAccontoBD {

	public static final String BEAN_ID = "ritenutaAccontoBD";

	private static Logger logger = Logger.getLogger(RitenutaAccontoBD.class);

	private RitenutaAccontoService ritenutaAccontoService;

	@Override
	public void cancellaCausaleRitenutaAcconto(CausaleRitenutaAcconto causaleRitenutaAcconto) {
		logger.debug("--> Enter cancellaCausaleRitenutaAcconto");
		start("cancellaCausaleRitenutaAcconto");
		try {
			ritenutaAccontoService.cancellaCausaleRitenutaAcconto(causaleRitenutaAcconto);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaCausaleRitenutaAcconto");
		}
		logger.debug("--> Exit cancellaCausaleRitenutaAcconto ");
	}

	@Override
	public void cancellaContributoPrevidenziale(ContributoPrevidenziale contributoPrevidenziale) {
		logger.debug("--> Enter cancellaContributoPrevidenziale");
		start("cancellaContributoPrevidenziale");
		try {
			ritenutaAccontoService.cancellaContributoPrevidenziale(contributoPrevidenziale);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaContributoPrevidenziale");
		}
		logger.debug("--> Exit cancellaContributoPrevidenziale ");
	}

	@Override
	public void cancellaPrestazione(Prestazione prestazione) {
		logger.debug("--> Enter cancellaPrestazione");
		start("cancellaPrestazione");
		try {
			ritenutaAccontoService.cancellaPrestazione(prestazione);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaPrestazione");
		}
		logger.debug("--> Exit cancellaPrestazione ");
	}

	@Override
	public List<CausaleRitenutaAcconto> caricaCausaliRitenuteAcconto(String codice) {
		logger.debug("--> Enter caricaCausaliRitenuteAcconto");
		start("caricaCausaliRitenuteAcconto");

		List<CausaleRitenutaAcconto> result = null;
		try {
			result = ritenutaAccontoService.caricaCausaliRitenuteAcconto(codice);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaCausaliRitenuteAcconto");
		}
		logger.debug("--> Exit caricaCausaliRitenuteAcconto ");
		return result;
	}

	@Override
	public List<ContributoPrevidenziale> caricaContributiPrevidenziali() {
		logger.debug("--> Enter caricaContributiPrevidenziali");
		start("caricaContributiPrevidenziali");

		List<ContributoPrevidenziale> result = null;
		try {
			result = ritenutaAccontoService.caricaContributiPrevidenziali();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaContributiPrevidenziali");
		}
		logger.debug("--> Exit caricaContributiPrevidenziali ");
		return result;
	}

	@Override
	public List<Prestazione> caricaPrestazioni() {
		logger.debug("--> Enter caricaPrestazioni");
		start("caricaPrestazioni");

		List<Prestazione> prestazioni = null;
		try {
			prestazioni = ritenutaAccontoService.caricaPrestazioni();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaPrestazioni");
		}
		logger.debug("--> Exit caricaPrestazioni ");
		return prestazioni;
	}

	@Override
	public List<SituazioneRitenuteAccontoDTO> caricaSituazioneRitenuteAccont(
			ParametriSituazioneRitenuteAcconto parametri) {
		logger.debug("--> Enter caricaSituazioneRitenuteAccont");
		start("caricaSituazioneRitenuteAccont");
		List<SituazioneRitenuteAccontoDTO> situazione = new ArrayList<SituazioneRitenuteAccontoDTO>();
		try {
			situazione = ritenutaAccontoService.caricaSituazioneRitenuteAccont(parametri);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaSituazioneRitenuteAccont");
		}
		logger.debug("--> Exit caricaSituazioneRitenuteAccont ");
		return situazione;
	}

	@Override
	public CausaleRitenutaAcconto salvaCausaleRitenutaAcconto(CausaleRitenutaAcconto causaleRitenutaAcconto) {
		logger.debug("--> Enter salvaCausaleRitenutaAcconto");
		start("salvaCausaleRitenutaAcconto");
		CausaleRitenutaAcconto causaleRitenutaAccontoSalvata = null;
		try {
			causaleRitenutaAccontoSalvata = ritenutaAccontoService.salvaCausaleRitenutaAcconto(causaleRitenutaAcconto);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaCausaleRitenutaAcconto");
		}
		logger.debug("--> Exit salvaCausaleRitenutaAcconto ");
		return causaleRitenutaAccontoSalvata;
	}

	@Override
	public ContributoPrevidenziale salvaContributoPrevidenziale(ContributoPrevidenziale contributoPrevidenziale) {
		logger.debug("--> Enter salvaContributoPrevidenziale");
		start("salvaContributoPrevidenziale");

		ContributoPrevidenziale contributoPrevidenzialeSalvato = null;
		try {
			contributoPrevidenzialeSalvato = ritenutaAccontoService
					.salvaContributoPrevidenziale(contributoPrevidenziale);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaContributoPrevidenziale");
		}
		logger.debug("--> Exit salvaContributoPrevidenziale ");
		return contributoPrevidenzialeSalvato;
	}

	@Override
	public Prestazione salvaPrestazione(Prestazione prestazione) {
		logger.debug("--> Enter salvaPrestazione");
		start("salvaPrestazione");

		Prestazione prestazioneSalvata = null;
		try {
			prestazioneSalvata = ritenutaAccontoService.salvaPrestazione(prestazione);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaPrestazione");
		}
		logger.debug("--> Exit salvaPrestazione ");
		return prestazioneSalvata;
	}

	/**
	 * @param ritenutaAccontoService
	 *            the ritenutaAccontoService to set
	 */
	public void setRitenutaAccontoService(RitenutaAccontoService ritenutaAccontoService) {
		this.ritenutaAccontoService = ritenutaAccontoService;
	}

}
