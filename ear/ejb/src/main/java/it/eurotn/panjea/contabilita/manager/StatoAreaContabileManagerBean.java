package it.eurotn.panjea.contabilita.manager;

import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileVerificaManager;
import it.eurotn.panjea.contabilita.manager.interfaces.LibroGiornaleManager;
import it.eurotn.panjea.contabilita.manager.interfaces.RegistroIvaManager;
import it.eurotn.panjea.contabilita.manager.interfaces.StatoAreaContabileManager;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.contabilita.service.exception.StatoAreaContabileNonValidoException;
import it.eurotn.panjea.contabilita.service.exception.VariazioneStatoAreaContabileNonValidoPerStampaLibroGiornale;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * Session Bean incaricato della gestione degli stati di {@link AreaContabile}
 *
 * @author adriano
 * @version 1.0, 28/set/07
 */
@Stateless(name = "Panjea.StatoAreaContabileManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.StatoAreaContabileManager")
public class StatoAreaContabileManagerBean implements StatoAreaContabileManager {

	@Resource
	SessionContext context;
	/**
	 * @uml.property name="libroGiornaleManager"
	 * @uml.associationEnd
	 */
	@EJB
	LibroGiornaleManager libroGiornaleManager;
	/**
	 * @uml.property name="registroIvaManager"
	 * @uml.associationEnd
	 */
	@EJB
	RegistroIvaManager registroIvaManager;
	/**
	 * @uml.property name="panjeaDAO"
	 * @uml.associationEnd
	 */
	@EJB
	PanjeaDAO panjeaDAO;
	/**
	 * @uml.property name="areaContabileVerificaManager"
	 * @uml.associationEnd
	 */
	@EJB
	AreaContabileVerificaManager areaContabileVerificaManager;

	static Logger logger = Logger.getLogger(StatoAreaContabileManagerBean.class);

	@Override
	public AreaContabile cambioStatoDaConfermatoInProvvisorio(AreaContabile areaContabile)
			throws StatoAreaContabileNonValidoException {
		logger.debug("--> Enter cambioStatoDaConfermatoAProvvisorio");
		if (!areaContabile.getStatoAreaContabile().equals(StatoAreaContabile.CONFERMATO)) {
			throw new StatoAreaContabileNonValidoException(
					"Stato AreaContabile non valido per il passaggio allo stato provvisorio ");
		}
		areaContabile.setStatoAreaContabile(StatoAreaContabile.PROVVISORIO);
		logger.debug("--> Exit cambioStatoDaConfermatoAProvvisorio");
		try {
			areaContabile = panjeaDAO.saveWithoutFlush(areaContabile);
		} catch (Exception e) {
			logger.error("--> errore nel cambiare lo stato ", e);
			throw new RuntimeException(e);
		}
		return areaContabile;
	}

	@Override
	public AreaContabile cambioStatoDaConfermatoInVerificato(AreaContabile areaContabile)
			throws StatoAreaContabileNonValidoException {
		logger.debug("--> Enter cambioStatoDaConfermatoInVerificato");
		if (!areaContabile.getStatoAreaContabile().equals(StatoAreaContabile.CONFERMATO)) {
			throw new StatoAreaContabileNonValidoException(
					"Stato AreaContabile non valido per il passaggio allo stato verificato ");

		}
		areaContabile.setStatoAreaContabile(StatoAreaContabile.VERIFICATO);
		logger.debug("--> Exit cambioStatoDaConfermatoInVerificato");
		return areaContabile;
	}

	@Override
	public AreaContabile cambioStatoDaProvvisorioInConfermato(AreaContabile areaContabile)
			throws StatoAreaContabileNonValidoException {
		logger.debug("--> Enter cambioStatoDaProvvisorioInConfermato");
		if (!areaContabile.getStatoAreaContabile().equals(StatoAreaContabile.PROVVISORIO)) {
			throw new StatoAreaContabileNonValidoException(
					"Stato AreaContabile non valido per il passaggio allo stato confermato ");
		}
		try {
			// invalida il giornale e registro iva a seguito del cambio di stato
			areaContabileVerificaManager.invalidaLibroGiornale(null, areaContabile);
			areaContabileVerificaManager.invalidaRegistroIva(null, areaContabile);
		} catch (ContabilitaException e) {
			logger.error("Errore durante l'invalidazione del giornale.", e);
			throw new StatoAreaContabileNonValidoException("Errore durante l'invalidazione del giornale.");
		}

		// ricarico l'area contabile dato che l'invalidazione del giornale aggiorna
		// stato e numeroPaginaGiornale dell'area contabile
		try {
			areaContabile = panjeaDAO.load(AreaContabile.class, areaContabile.getId());
		} catch (Exception e) {
			logger.error("--> Errore nel ricaricare l'areaContabile dopo l'invalidazione del giornale");
			throw new RuntimeException("--> Errore nel ricaricare l'areaContabile dopo l'invalidazione del giornale", e);
		}
		areaContabile.setStatoAreaContabile(StatoAreaContabile.CONFERMATO);

		logger.debug("--> Exit cambioStatoDaProvvisorioInConfermato");
		return areaContabile;
	}

	@Override
	public AreaContabile cambioStatoDaProvvisorioInSimulato(AreaContabile areaContabile)
			throws StatoAreaContabileNonValidoException {
		logger.debug("--> Enter cambioStatoInSimulato");
		if (!(areaContabile.getStatoAreaContabile().equals(StatoAreaContabile.PROVVISORIO))) {
			throw new StatoAreaContabileNonValidoException(
					"Stato AreaContabile non valido per il passaggio allo stato simulato");
		}
		areaContabile.setStatoAreaContabile(StatoAreaContabile.SIMULATO);

		try {
			areaContabile = panjeaDAO.saveWithoutFlush(areaContabile);
		} catch (Exception e) {
			logger.error("--> errore nel cambiare lo stato ", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit cambioStatoInSimulato");
		return areaContabile;
	}

	@Override
	public AreaContabile cambioStatoDaSimulatoInProvvisorio(AreaContabile areaContabile)
			throws StatoAreaContabileNonValidoException {
		if (!areaContabile.getStatoAreaContabile().equals(StatoAreaContabile.SIMULATO)) {
			throw new StatoAreaContabileNonValidoException(
					"Stato AreaContabile non valido per il passaggio allo stato provvisorio ");
		}
		areaContabile.setStatoAreaContabile(StatoAreaContabile.PROVVISORIO);
		logger.debug("--> Exit cambioStatoDaSimulatoAProvvisorio");
		return areaContabile;
	}

	@Override
	public AreaContabile cambioStatoDaVerificatoInConfermato(AreaContabile areaContabile)
			throws StatoAreaContabileNonValidoException {
		logger.debug("--> Enter cambioStatoDaVerificatoInConfermato");
		if (!areaContabile.getStatoAreaContabile().equals(StatoAreaContabile.VERIFICATO)) {
			throw new StatoAreaContabileNonValidoException(
					"Stato AreaContabile non valido per il passaggio allo stato confermato ");
		}
		// cambio solo lo stato e non lo salvo dato che questo metodo è chiamato
		// da StatoAreaContabileManagerBean.cambioStatoInConfermato() e
		// successivamente da StatoAreaContabileManagerBean.cambioStatoDaVerificatoInConfermato()
		// dove avviene il salvataggio dell'area contabile
		areaContabile.setStatoAreaContabile(StatoAreaContabile.CONFERMATO);

		logger.debug("--> Exit cambioStatoDaVerificatoInConfermato");
		return areaContabile;
	}

	@Override
	public AreaContabile cambioStatoInConfermato(AreaContabile areaContabile)
			throws StatoAreaContabileNonValidoException {
		logger.debug("--> Enter cambiaStatoInConfermato");
		switch (areaContabile.getStatoAreaContabile()) {
		case PROVVISORIO:
			areaContabile = cambioStatoDaProvvisorioInConfermato(areaContabile);
			break;
		case VERIFICATO:
			areaContabile = cambioStatoDaVerificatoInConfermato(areaContabile);
			break;
		default:
			throw new StatoAreaContabileNonValidoException(
					"Stato AreaContabile non valido per il passaggio allo stato confermato ");
		}
		logger.debug("--> Exit cambiaStatoInConfermato");
		return areaContabile;
	}

	@Override
	public AreaContabile cambioStatoInProvvisorio(AreaContabile areaContabile)
			throws StatoAreaContabileNonValidoException {
		logger.debug("--> Enter cambiaStatoInProvvisorio");

		switch (areaContabile.getStatoAreaContabile()) {
		case CONFERMATO:
			areaContabile = cambioStatoDaConfermatoInProvvisorio(areaContabile);
			break;
		case SIMULATO:
			areaContabile = cambioStatoDaSimulatoInProvvisorio(areaContabile);
			break;
		default:
			throw new StatoAreaContabileNonValidoException(
					"Stato AreaContabile non valido per la conferma delle righe contabili ");
		}

		// nel caso di modifica manuale dello stato da confermato a provvisorio
		// devo invalidare parte iva e contabile, cosi che dopo aver riconfermato parte iva
		// e contabile, il documento potrà ritornare a confermato
		areaContabile = areaContabileVerificaManager.invalidaAreaContabile(areaContabile, false);
		areaContabileVerificaManager.invalidaAreaIva(areaContabile);
		areaContabileVerificaManager.invalidaAreaRate(areaContabile);

		logger.debug("--> Exit cambiaStatoInProvvisorio");
		return areaContabile;
	}

	@Override
	public AreaContabile cambioStatoPerConfermaRigheContabili(AreaContabile areaContabile)
			throws StatoAreaContabileNonValidoException {
		logger.debug("--> Enter cambioStatoPerConfermaRigheContabili");
		if (!areaContabile.getStatoAreaContabile().equals(StatoAreaContabile.PROVVISORIO)) {
			throw new StatoAreaContabileNonValidoException(
					"Stato AreaContabile non valido per la conferma delle righe contabili ");
		}
		areaContabile.setStatoAreaContabile(StatoAreaContabile.CONFERMATO);
		logger.debug("--> Exit cambioStatoPerConfermaRigheContabili");
		return areaContabile;
	}

	@Override
	public AreaContabile cambioStatoPerStampaLibroGiornale(AreaContabile areaContabile)
			throws VariazioneStatoAreaContabileNonValidoPerStampaLibroGiornale {
		logger.debug("--> Enter cambiaStatoPerStampaLibroGiornale");
		if ((!areaContabile.getStatoAreaContabile().equals(StatoAreaContabile.CONFERMATO))
				|| (!areaContabile.getStatoAreaContabile().equals(StatoAreaContabile.VERIFICATO))) {
			throw new VariazioneStatoAreaContabileNonValidoPerStampaLibroGiornale(
					"Stato AreaContabile non valido per il passaggio allo stato verificato nella stampa libro giornale ");
		}
		areaContabile.setStatoAreaContabile(StatoAreaContabile.VERIFICATO);
		logger.debug("--> Exit cambiaStatoPerStampaLibroGiornale");
		return areaContabile;
	}
}
