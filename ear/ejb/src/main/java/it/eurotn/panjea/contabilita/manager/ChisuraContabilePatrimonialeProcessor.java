package it.eurotn.panjea.contabilita.manager;

import it.eurotn.panjea.contabilita.domain.ContiBase;
import it.eurotn.panjea.contabilita.domain.Conto.TipoConto;
import it.eurotn.panjea.contabilita.domain.ETipoContoBase;
import it.eurotn.panjea.contabilita.domain.RigaContabile;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.manager.interfaces.ChiusuraContabileProcessor;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.contabilita.service.exception.ContiBaseException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.ChisuraContabilePatrimonialeProcessor")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.ChisuraContabilePatrimonialeProcessor")
public class ChisuraContabilePatrimonialeProcessor extends AbstractChiusuraContabileProcessor implements
		ChiusuraContabileProcessor {
	private static Logger logger = Logger.getLogger(ChisuraContabilePatrimonialeProcessor.class);

	/**
	 * Carica i conti base per la contabilità e verifica che ci siano quelli per la chiusura patrimoniale.
	 * 
	 * @return {@link ContiBase} conti base della contabilità.
	 * @throws ContiBaseException
	 *             lanciata quando mancano i conti base<br.>
	 *             {@link ETipoContoBase#APERTURA_BILANCIO} e {@link ETipoContoBase#CHIUSURA_BILANCIO}
	 */
	private ContiBase caricaContiChisura() throws ContiBaseException {
		logger.debug("--> Enter caricaContiChisura");
		ContiBase contiBase = null;
		try {
			contiBase = pianoContiManager.caricaTipiContoBase();

			if (!contiBase.containsKey(ETipoContoBase.APERTURA_BILANCIO)) {
				throw new ContiBaseException(ETipoContoBase.APERTURA_BILANCIO);
			}
			if (!contiBase.containsKey(ETipoContoBase.CHIUSURA_BILANCIO)) {
				throw new ContiBaseException(ETipoContoBase.CHIUSURA_BILANCIO);
			}
			if (!contiBase.containsKey(ETipoContoBase.PERDITA)) {
				throw new ContiBaseException(ETipoContoBase.PERDITA);
			}
			if (!contiBase.containsKey(ETipoContoBase.UTILE)) {
				throw new ContiBaseException(ETipoContoBase.UTILE);
			}
		} catch (ContabilitaException e) {
			logger.error("--> errore nel caricare i conti base per la chiusura patrimoniale", e);
		}
		logger.debug("--> Exit caricaContiChisura");
		return contiBase;
	}

	@Override
	protected List<RigaContabile> completaDocumentoChiusura(BigDecimal saldoImportoDare, BigDecimal saldoImportoAvere)
			throws ContiBaseException {
		ContiBase contiBase = caricaContiChisura();
		List<RigaContabile> righeDocumento = new ArrayList<RigaContabile>();

		if (saldoImportoAvere.compareTo(saldoImportoDare) == 0) {
			return righeDocumento;
		}

		// Chiudo i conti. NB:E' una chisura quindi giro DARE con AVERE.
		SottoConto sottoContoChiusuraBilancio = contiBase.get(ETipoContoBase.CHIUSURA_BILANCIO);

		RigaContabile rigaContabileBilancioDare = RigaContabile.creaRigaContabile(null, sottoContoChiusuraBilancio,
				true, saldoImportoAvere, null, false);
		righeDocumento.add(rigaContabileBilancioDare);

		RigaContabile rigaContabileBilancioAvere = RigaContabile.creaRigaContabile(null, sottoContoChiusuraBilancio,
				false, saldoImportoDare, null, false);
		righeDocumento.add(rigaContabileBilancioAvere);

		BigDecimal saldoBilancioDareAvere = rigaContabileBilancioDare.getImporto().subtract(
				rigaContabileBilancioAvere.getImporto());

		if (saldoBilancioDareAvere.compareTo(BigDecimal.ZERO) > 0) {
			RigaContabile rigaSaldoBilancioDareAvere = RigaContabile.creaRigaContabile(null,
					contiBase.get(ETipoContoBase.CHIUSURA_BILANCIO), false, saldoBilancioDareAvere, null, false);
			righeDocumento.add(rigaSaldoBilancioDareAvere);

			RigaContabile rigaUtile = RigaContabile.creaRigaContabile(null, contiBase.get(ETipoContoBase.UTILE), true,
					saldoBilancioDareAvere, null, false);
			righeDocumento.add(rigaUtile);
		} else if (saldoBilancioDareAvere.compareTo(BigDecimal.ZERO) < 0) {
			saldoBilancioDareAvere = saldoBilancioDareAvere.abs();

			RigaContabile rigaSaldoBilancioDareAvere = RigaContabile.creaRigaContabile(null,
					contiBase.get(ETipoContoBase.CHIUSURA_BILANCIO), true, saldoBilancioDareAvere, null, false);
			righeDocumento.add(rigaSaldoBilancioDareAvere);

			RigaContabile rigaPerdita = RigaContabile.creaRigaContabile(null, contiBase.get(ETipoContoBase.PERDITA),
					false, saldoBilancioDareAvere.abs(), null, false);
			righeDocumento.add(rigaPerdita);
		}
		return righeDocumento;
	}

	@Override
	protected TipoConto getTipoConto() {
		return TipoConto.PATRIMONIALE;
	}

	@Override
	public void verificaTipiContiChiusura() throws ContiBaseException {
		caricaContiChisura();
	}
}
