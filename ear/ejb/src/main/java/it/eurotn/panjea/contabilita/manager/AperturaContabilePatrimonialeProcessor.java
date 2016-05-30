package it.eurotn.panjea.contabilita.manager;

import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.ContiBase;
import it.eurotn.panjea.contabilita.domain.Conto.TipoConto;
import it.eurotn.panjea.contabilita.domain.ETipoContoBase;
import it.eurotn.panjea.contabilita.domain.RigaContabile;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.manager.interfaces.AperturaContabileProcessor;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileManager;
import it.eurotn.panjea.contabilita.manager.interfaces.PianoContiManager;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.contabilita.service.exception.ContiBaseException;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * 
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.AperturaContabilePatrimonialeProcessor")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AperturaContabilePatrimonialeProcessor")
public class AperturaContabilePatrimonialeProcessor extends AbstractAperturaContabileProcessor implements
		AperturaContabileProcessor {

	private static Logger logger = Logger.getLogger(AperturaContabilePatrimonialeProcessor.class);

	/**
	 * @uml.property name="areaContabileManager"
	 * @uml.associationEnd
	 */
	@EJB
	private AreaContabileManager areaContabileManager;

	/**
	 * @uml.property name="pianoContiManager"
	 * @uml.associationEnd
	 */
	@EJB
	private PianoContiManager pianoContiManager;

	private ContiBase caricaContiApertura() throws ContiBaseException {
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
		} catch (ContabilitaException e) {
			logger.error("--> errore nel caricare i conti base per la chiusura patrimoniale", e);
		}
		logger.debug("--> Exit caricaContiChisura");
		return contiBase;
	}

	@Override
	protected void creaRigheContabiliApertura(AreaContabile areaContabile, List<RigaContabile> righeContabiliChiusura) {
		try {
			ContiBase contiBase = caricaContiApertura();
			for (RigaContabile rigaContabileChisura : righeContabiliChiusura) {

				SottoConto sottoConto = rigaContabileChisura.getConto();
				// inverto i conti per farla diventare una riga di apertura
				boolean isDare = rigaContabileChisura.getContoAvere() != null;

				if (sottoConto != null && sottoConto.equals(contiBase.get(ETipoContoBase.CHIUSURA_BILANCIO))) {
					sottoConto = contiBase.get(ETipoContoBase.APERTURA_BILANCIO);
				}

				RigaContabile rigaContabileApertura = RigaContabile.creaRigaContabile(areaContabile, sottoConto,
						isDare, rigaContabileChisura.getImporto(), null, false);

				areaContabileManager.salvaRigaContabileNoCheck(rigaContabileApertura);
			}
		} catch (ContiBaseException e) {
			logger.error("--> errore nel caricare i tipi conti base", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	protected TipoConto getTipoConto() {
		return TipoConto.PATRIMONIALE;
	}

	@Override
	public void verificaTipiContiApertura() throws ContiBaseException {
		caricaContiApertura();
	}

}
