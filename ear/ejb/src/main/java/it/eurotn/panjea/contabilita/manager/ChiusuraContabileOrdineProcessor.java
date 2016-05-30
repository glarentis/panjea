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

@Stateless(name = "Panjea.ChiusuraContabileOrdineProcessor")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.ChiusuraContabileOrdineProcessor")
public class ChiusuraContabileOrdineProcessor extends AbstractChiusuraContabileProcessor implements
		ChiusuraContabileProcessor {
	private static Logger logger = Logger.getLogger(ChiusuraContabileOrdineProcessor.class);

	private ContiBase caricaContiChisura() throws ContiBaseException {
		logger.debug("--> Enter caricaContiChisura");
		ContiBase contiBase = null;
		try {
			contiBase = pianoContiManager.caricaTipiContoBase();
			if (!contiBase.containsKey(ETipoContoBase.RIEPILOGO)) {
				throw new ContiBaseException(ETipoContoBase.RIEPILOGO);
			}
		} catch (ContabilitaException e) {
			logger.error("--> errore nel caricare i conti base per le chiusure ", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Enter caricaContiChisura");
		return contiBase;
	}

	@Override
	protected List<RigaContabile> completaDocumentoChiusura(BigDecimal saldoImportoDare, BigDecimal saldoImportoAvere)
			throws ContiBaseException {
		logger.debug("--> Enter completaDocumentoChiusura");
		List<RigaContabile> righeDocumento = new ArrayList<RigaContabile>();

		if (saldoImportoAvere.compareTo(saldoImportoDare) == 0) {
			return righeDocumento;
		}

		ContiBase contiBase = caricaContiChisura();
		RigaContabile rigaContabileRiepilogoDare = RigaContabile.creaRigaContabile(null,
				contiBase.get(ETipoContoBase.RIEPILOGO), true, saldoImportoAvere, null, false);
		righeDocumento.add(rigaContabileRiepilogoDare);

		RigaContabile rigaContabileRiepilogoAvere = RigaContabile.creaRigaContabile(null,
				contiBase.get(ETipoContoBase.RIEPILOGO), false, saldoImportoDare, null, false);
		righeDocumento.add(rigaContabileRiepilogoAvere);
		logger.debug("--> Exit getTipoConto");
		return righeDocumento;
	}

	@Override
	protected TipoConto getTipoConto() {
		return TipoConto.ORDINE;
	}

	@Override
	public void verificaTipiContiChiusura() throws ContiBaseException {
		logger.debug("--> Enter verificaTipiContiChiusura");
		caricaContiChisura();
		logger.debug("--> Exit verificaTipiContiChiusura");
	}

}
