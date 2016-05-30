package it.eurotn.panjea.contabilita.manager;

import it.eurotn.panjea.contabilita.domain.ContiBase;
import it.eurotn.panjea.contabilita.domain.Conto.TipoConto;
import it.eurotn.panjea.contabilita.domain.ETipoContoBase;
import it.eurotn.panjea.contabilita.domain.RigaContabile;
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

@Stateless(name = "Panjea.ChiusuraContabileEconomicaProcessor")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.ChiusuraContabileEconomicaProcessor")
public class ChiusuraContabileEconomicaProcessor extends AbstractChiusuraContabileProcessor implements
		ChiusuraContabileProcessor {
	private static Logger logger = Logger.getLogger(ChiusuraContabileEconomicaProcessor.class);

	/**
	 * Carica i conti base per la contabilità e verifica che ci siano quelli per la chiusura economica.
	 * 
	 * @return {@link ContiBase} per la chiusura contabile economica
	 * @throws ContiBaseException
	 *             rilanciata se non esiste il conto base {@link ETipoContoBase#PROFITTI_PERDITE}
	 */
	private ContiBase caricaContiChisura() throws ContiBaseException {
		logger.debug("--> Enter caricaContiChisura");
		ContiBase contiBase = null;
		try {
			contiBase = pianoContiManager.caricaTipiContoBase();

			if (!contiBase.containsKey(ETipoContoBase.PROFITTI_PERDITE)) {
				throw new ContiBaseException(ETipoContoBase.PROFITTI_PERDITE);
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
		List<RigaContabile> righeDocumento = new ArrayList<RigaContabile>();

		if (saldoImportoAvere.compareTo(saldoImportoDare) == 0) {
			return righeDocumento;
		}

		ContiBase contiBase = caricaContiChisura();

		/* generazione righe Profitti/Perdite e Utile */
		RigaContabile rigaContabileProfittiPerditeDare = RigaContabile.creaRigaContabile(null,
				contiBase.get(ETipoContoBase.PROFITTI_PERDITE), true, saldoImportoAvere, null, false);
		righeDocumento.add(rigaContabileProfittiPerditeDare);

		RigaContabile rigaContabileProfittiPerditeAvere = RigaContabile.creaRigaContabile(null,
				contiBase.get(ETipoContoBase.PROFITTI_PERDITE), false, saldoImportoDare, null, false);
		righeDocumento.add(rigaContabileProfittiPerditeAvere);

		// calcola il saldo profitti/perdite come differenza tra la riga P/P dare e P/P avere e se positiva viene creata
		// una riga contabile P/P in avere, se negativa viene creata una riga contabile P/P in dare con relativa
		// scrittura di utile o di perdita

		BigDecimal saldoProfittiPerditeImporto = rigaContabileProfittiPerditeDare.getImporto().subtract(
				rigaContabileProfittiPerditeAvere.getImporto());
		if (saldoProfittiPerditeImporto.compareTo(BigDecimal.ZERO) > 0) {
			RigaContabile rigaContabileSaldoProfittiPerdite = RigaContabile.creaRigaContabile(null,
					contiBase.get(ETipoContoBase.PROFITTI_PERDITE), false, saldoProfittiPerditeImporto, null, false);
			righeDocumento.add(rigaContabileSaldoProfittiPerdite);

			RigaContabile rigaPerdite = RigaContabile.creaRigaContabile(null, contiBase.get(ETipoContoBase.PERDITA),
					true, saldoProfittiPerditeImporto, null, false);
			righeDocumento.add(rigaPerdite);
		} else if (saldoProfittiPerditeImporto.compareTo(BigDecimal.ZERO) < 0) {
			RigaContabile rigacontabileSaldoProfittiPerdite = RigaContabile.creaRigaContabile(null,
					contiBase.get(ETipoContoBase.PROFITTI_PERDITE), true, saldoProfittiPerditeImporto.abs(), null,
					false);
			righeDocumento.add(rigacontabileSaldoProfittiPerdite);

			RigaContabile rigaUtile = RigaContabile.creaRigaContabile(null, contiBase.get(ETipoContoBase.UTILE), false,
					saldoProfittiPerditeImporto.abs(), null, false);
			righeDocumento.add(rigaUtile);
		}
		return righeDocumento;
	}

	@Override
	protected TipoConto getTipoConto() {
		return TipoConto.ECONOMICO;
	}

	@Override
	public void verificaTipiContiChiusura() throws ContiBaseException {
		caricaContiChisura();
	}
}
