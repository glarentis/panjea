package it.eurotn.panjea.cosaro.lotti;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.lotti.manager.rimanenzefinali.RimanenzeFinaliDTO;
import it.eurotn.panjea.lotti.manager.rimanenzefinali.interfaces.RimanenzeFinaliManager;
import it.eurotn.panjea.lotti.service.LottiServiceBean;

/**
 * @author fattazzo
 *
 */
@Stateless(name = "Panjea.LottiCosaroServiceBean")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.LottiCosaroServiceBean")
public class LottiCosaroServiceBean extends LottiServiceBean {

	@EJB(beanName = "RimanenzeFinaliCosaroManagerBean")
	private RimanenzeFinaliManager rimanenzeFinaliManager;

	@Override
	public List<RimanenzeFinaliDTO> caricaRimanenzeFinali(Map<Object, Object> params) {
		Date data = (params.containsKey("DATA") ? (Date) params.get("DATA") : Calendar.getInstance().getTime());

		Integer idDeposito = (Integer) params.get("idDeposito");
		Integer idFornitore = (Integer) params.get("idFornitore");
		Integer idCategoria = (Integer) params.get("idCategoria");
		boolean caricaGiacenzeAZero = true;
		if (params.containsKey("caricaGiacenzeAZero")) {
			caricaGiacenzeAZero = (Boolean) params.get("caricaGiacenzeAZero");
		}

		String ordinamento = (String) params.get("ordinamento");

		return rimanenzeFinaliManager.caricaRimanenzeFinali(data, idFornitore, idDeposito, idCategoria,
				caricaGiacenzeAZero, ordinamento);
	}
}
