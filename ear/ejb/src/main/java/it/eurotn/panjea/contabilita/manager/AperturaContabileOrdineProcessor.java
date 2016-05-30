package it.eurotn.panjea.contabilita.manager;

import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.Conto.TipoConto;
import it.eurotn.panjea.contabilita.domain.RigaContabile;
import it.eurotn.panjea.contabilita.domain.RigaContabile.EContoInsert;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.manager.interfaces.AperturaContabileProcessor;
import it.eurotn.panjea.contabilita.service.exception.ContiBaseException;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.AperturaContabileOrdineProcessor")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AperturaContabileOrdineProcessor")
public class AperturaContabileOrdineProcessor extends AbstractAperturaContabileProcessor implements
		AperturaContabileProcessor {

	private static Logger logger = Logger.getLogger(AperturaContabileOrdineProcessor.class);

	@Override
	protected void creaRigheContabiliApertura(AreaContabile areaContabile, List<RigaContabile> righeContabiliChiusura) {
		logger.debug("--> Enter creaRigheContabiliApertura");
		for (RigaContabile rigaContabileChisura : righeContabiliChiusura) {

			// copio la riga di chiusura
			SottoConto sottoConto = rigaContabileChisura.getConto();
			// inverto i conti per farla diventare una riga di apertura
			boolean isDare = rigaContabileChisura.getContoInsert() == EContoInsert.AVERE;

			RigaContabile rigaContabileApertura = RigaContabile.creaRigaContabile(areaContabile, sottoConto, isDare,
					rigaContabileChisura.getImporto(), null, false);

			areaContabileManager.salvaRigaContabileNoCheck(rigaContabileApertura);
		}
		logger.debug("--> Exit getTipoConto");
	}

	@Override
	protected TipoConto getTipoConto() {
		return TipoConto.ORDINE;
	}

	@Override
	public void verificaTipiContiApertura() throws ContiBaseException {
		// non ho nessun tipoContoBase per l'apertura
	}

}
