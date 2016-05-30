package it.eurotn.panjea.contabilita.manager;

import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.contabilita.domain.Conto;
import it.eurotn.panjea.contabilita.manager.interfaces.BilancioManager;
import it.eurotn.panjea.contabilita.manager.interfaces.SituazioneEPManager;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.contabilita.util.ParametriRicercaBilancio;
import it.eurotn.panjea.contabilita.util.ParametriRicercaSituazioneEP;
import it.eurotn.panjea.contabilita.util.SaldoConti;
import it.eurotn.panjea.contabilita.util.SaldoConto;
import it.eurotn.panjea.contabilita.util.SituazioneEpDTO;

import java.math.BigDecimal;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * Manager responsabile di calcolare e restituire la Situazione Economica/Patrimoniale.
 * 
 * @author adriano
 */
@Stateless(name = "Panjea.SituazioneEP")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.SituazioneEPManager")
public class SituazioneEPManagerBean implements SituazioneEPManager {

	private static Logger logger = Logger.getLogger(SituazioneEPManagerBean.class);

	/**
	 * @uml.property name="bilancioManager"
	 * @uml.associationEnd
	 */
	@EJB
	private BilancioManager bilancioManager;

	@Override
	public SituazioneEpDTO caricaSituazioneEP(ParametriRicercaSituazioneEP parametriRicercaSituazioneEP)
			throws ContabilitaException, TipoDocumentoBaseException {
		logger.debug("--> Enter caricaSituazioneEP");

		// Carico il bilancio con gli stessi filtri
		ParametriRicercaBilancio parametriRicercaBilancio = new ParametriRicercaBilancio();
		parametriRicercaBilancio.setDataRegistrazione(parametriRicercaSituazioneEP.getDataRegistrazione());
		// parametriRicercaBilancio.getDataRegistrazione().setTipoPeriodo(parametriRicercaSituazioneEP.getDataRegistrazione().getTipoPeriodo())
		parametriRicercaBilancio.getDataRegistrazione().setDataIniziale(
				parametriRicercaSituazioneEP.getDataRegistrazione().getDataIniziale());
		parametriRicercaBilancio.getDataRegistrazione().setDataFinale(
				parametriRicercaSituazioneEP.getDataRegistrazione().getDataFinale());
		parametriRicercaBilancio.setAnnoCompetenza(parametriRicercaSituazioneEP.getAnnoCompetenza());
		parametriRicercaBilancio.setStampaClienti(parametriRicercaSituazioneEP.getStampaClienti());
		parametriRicercaBilancio.setStampaFornitori(parametriRicercaSituazioneEP.getStampaFornitori());
		parametriRicercaBilancio.setStatiAreaContabile(parametriRicercaSituazioneEP.getStatiAreaContabile());
		parametriRicercaBilancio.setCentroCosto(parametriRicercaSituazioneEP.getCentroCosto());
		parametriRicercaBilancio.setStampaCentriCosto(parametriRicercaSituazioneEP.getStampaCentriCosto());

		SituazioneEpDTO situazioneEpDTO = new SituazioneEpDTO();
		SaldoConti saldoConti = bilancioManager.caricaBilancio(parametriRicercaBilancio);
		for (SaldoConto saldoConto : saldoConti.asList()) {
			// riporto solo le voci di bilancio con Saldo diverso da zero
			if (BigDecimal.ZERO.compareTo(saldoConto.getSaldo()) != 0) {

				if (saldoConto.getTipoConto().equals(Conto.TipoConto.ECONOMICO)) {
					situazioneEpDTO.addContoEconomico(saldoConto);
				}
				if (saldoConto.getTipoConto().equals(Conto.TipoConto.PATRIMONIALE)) {
					situazioneEpDTO.addContoPatrimoniale(saldoConto);
				}
				if (saldoConto.getTipoConto().equals(Conto.TipoConto.ORDINE)) {
					situazioneEpDTO.addContoOrdine(saldoConto);
				}
			}
		}
		return situazioneEpDTO;

	}
}
