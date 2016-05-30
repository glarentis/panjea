package it.eurotn.panjea.cauzioni.rich.bd;

import it.eurotn.panjea.cauzioni.service.interfaces.CauzioniService;
import it.eurotn.panjea.cauzioni.util.parametriricerca.MovimentazioneCauzioneDTO;
import it.eurotn.panjea.cauzioni.util.parametriricerca.ParametriRicercaSituazioneCauzioni;
import it.eurotn.panjea.cauzioni.util.parametriricerca.SituazioneCauzioniDTO;
import it.eurotn.panjea.cauzioni.util.parametriricerca.SituazioneCauzioniEntitaDTO;
import it.eurotn.panjea.rich.bd.AbstractBaseBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

public class CauzioniBD extends AbstractBaseBD implements ICauzioniBD {

	public static final String BEAN_ID = "cauzioniBD";

	private static Logger logger = Logger.getLogger(CauzioniBD.class);

	private CauzioniService cauzioniService;

	@Override
	public List<MovimentazioneCauzioneDTO> caricaMovimentazioneCauzioniArticolo(Set<Integer> idEntita,
			Set<Integer> idSedeEntita, Set<Integer> idArticolo) {
		logger.debug("--> Enter caricaMovimentazioneCauzioniArticolo");
		start("caricaMovimentazioneCauzioniArticolo");

		List<MovimentazioneCauzioneDTO> movimentazione = new ArrayList<MovimentazioneCauzioneDTO>();
		try {
			movimentazione = cauzioniService.caricaMovimentazioneCauzioniArticolo(idEntita, idSedeEntita, idArticolo);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaMovimentazioneCauzioniArticolo");
		}
		logger.debug("--> Exit caricaMovimentazioneCauzioniArticolo ");
		return movimentazione;
	}

	@Override
	public List<SituazioneCauzioniDTO> caricaSituazioneCauzioni(ParametriRicercaSituazioneCauzioni parametri) {
		logger.debug("--> Enter caricaSituazioneCauzioni");
		start("caricaSituazioneCauzioni");

		List<SituazioneCauzioniDTO> situazioni = new ArrayList<SituazioneCauzioniDTO>();
		try {
			situazioni = cauzioniService.caricaSituazioneCauzioni(parametri);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaSituazioneCauzioni");
		}
		logger.debug("--> Exit caricaSituazioneCauzioni ");
		return situazioni;
	}

	@Override
	public List<SituazioneCauzioniEntitaDTO> caricaSituazioneCauzioniEntita(Integer idEntita, boolean raggruppamentoSedi) {
		logger.debug("--> Enter caricaSituazioneCauzioniEntita");
		start("caricaSituazioneCauzioniEntita");

		List<SituazioneCauzioniEntitaDTO> situazioni = new ArrayList<SituazioneCauzioniEntitaDTO>();
		try {
			situazioni = cauzioniService.caricaSituazioneCauzioniEntita(idEntita, raggruppamentoSedi);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaSituazioneCauzioniEntita");
		}
		logger.debug("--> Exit caricaSituazioneCauzioniEntita ");
		return situazioni;
	}

	/**
	 * @param cauzioniService
	 *            the cauzioniService to set
	 */
	public void setCauzioniService(CauzioniService cauzioniService) {
		this.cauzioniService = cauzioniService;
	}

}
