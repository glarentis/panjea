package it.eurotn.panjea.preventivi.domain.documento.interfaces;

import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo;
import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo.StatoAreaPreventivo;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rate.manager.interfaces.AreaRateManager;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(mappedName = "Panjea.AreaPreventivoVerificaManagerBean")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaPreventivoVerificaManagerBean")
public class AreaPreventivoVerificaManagerBean implements AreaPreventivoVerificaManager {

	@EJB
	private AreaRateManager areaRateManager;

	@Override
	public boolean checkCambioStato(AreaPreventivo areaPrevetivo, AreaRate areaRate) {

		if (areaRate != null && areaRate.getId() != null
				&& areaPrevetivo.getStatoAreaPreventivo() != StatoAreaPreventivo.PROVVISORIO) {

			AreaRate areaPartiteOld = areaRateManager.caricaAreaRate(areaRate.getId());

			// controllo se le spese incasso o sconto commerciale dell'area partite sono cambiate
			if ((areaRate.getSpeseIncasso() != null && !areaRate.getSpeseIncasso().equals(
					areaPartiteOld.getSpeseIncasso()))
					|| areaRate.getCodicePagamento().getPercentualeScontoCommerciale()
							.compareTo(areaPartiteOld.getCodicePagamento().getPercentualeScontoCommerciale()) != 0) {
				return true;
			}
		}

		return false;
	}
}
