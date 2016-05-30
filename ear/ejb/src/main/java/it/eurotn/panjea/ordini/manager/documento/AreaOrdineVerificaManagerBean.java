package it.eurotn.panjea.ordini.manager.documento;

import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine.StatoAreaOrdine;
import it.eurotn.panjea.ordini.manager.documento.interfaces.AreaOrdineVerificaManager;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rate.manager.interfaces.AreaRateManager;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(mappedName = "Panjea.AreaOrdineVerificaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaOrdineVerificaManager")
public class AreaOrdineVerificaManagerBean implements AreaOrdineVerificaManager {

	@EJB
	private AreaRateManager areaRateManager;

	@Override
	public boolean checkCambioStato(AreaOrdine areaOrdine, AreaRate areaRate) {

		if (areaRate != null && areaRate.getId() != null
				&& areaOrdine.getStatoAreaOrdine() != StatoAreaOrdine.PROVVISORIO) {

			AreaRate areaPartiteOld = areaRateManager.caricaAreaRate(areaRate.getId());

			// controllo se le spese incasso o sconto commerciale dell'area partite sono cambiate
			if ((areaRate.getSpeseIncasso() != null && !areaRate.getSpeseIncasso().equals(
					areaPartiteOld.getSpeseIncasso()))
					|| (areaPartiteOld.getCodicePagamento() != null && areaRate.getCodicePagamento()
							.getPercentualeScontoCommerciale()
							.compareTo(areaPartiteOld.getCodicePagamento().getPercentualeScontoCommerciale()) != 0)) {
				return true;
			}
		}

		return false;
	}

}
