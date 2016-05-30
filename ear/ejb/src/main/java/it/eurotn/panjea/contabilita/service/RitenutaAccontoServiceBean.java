/**
 *
 */
package it.eurotn.panjea.contabilita.service;

import it.eurotn.panjea.contabilita.domain.CausaleRitenutaAcconto;
import it.eurotn.panjea.contabilita.domain.ContributoPrevidenziale;
import it.eurotn.panjea.contabilita.domain.Prestazione;
import it.eurotn.panjea.contabilita.manager.ritenutaacconto.interfaces.RitenutaAccontoManager;
import it.eurotn.panjea.contabilita.service.interfaces.RitenutaAccontoService;
import it.eurotn.panjea.contabilita.util.CertificazioneRitenutaDTO;
import it.eurotn.panjea.contabilita.util.ParametriSituazioneRitenuteAcconto;
import it.eurotn.panjea.contabilita.util.SituazioneRitenuteAccontoDTO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.lang3.ObjectUtils;
import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * @author fattazzo
 * 
 */
@Stateless(name = "Panjea.RitenutaAccontoService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.RitenutaAccontoService")
public class RitenutaAccontoServiceBean implements RitenutaAccontoService {

	@EJB
	private RitenutaAccontoManager ritenutaAccontoManager;

	@Override
	public void cancellaCausaleRitenutaAcconto(CausaleRitenutaAcconto causaleRitenutaAcconto) {
		ritenutaAccontoManager.cancellaCausaleRitenutaAcconto(causaleRitenutaAcconto);
	}

	@Override
	public void cancellaContributoPrevidenziale(ContributoPrevidenziale contributoPrevidenziale) {
		ritenutaAccontoManager.cancellaContributoPrevidenziale(contributoPrevidenziale);
	}

	@Override
	public void cancellaPrestazione(Prestazione prestazione) {
		ritenutaAccontoManager.cancellaPrestazione(prestazione);
	}

	@Override
	public List<CausaleRitenutaAcconto> caricaCausaliRitenuteAcconto(String codice) {
		return ritenutaAccontoManager.caricaCausaliRitenuteAcconto(codice);
	}

	@Override
	public List<CertificazioneRitenutaDTO> caricaCertificazioneRitenute(Map<Object, Object> params) {
		List<CertificazioneRitenutaDTO> certificazione = new ArrayList<CertificazioneRitenutaDTO>();

		Integer anno = (Integer) ObjectUtils.defaultIfNull(params.get("anno"), Calendar.getInstance()
				.get(Calendar.YEAR));
		Integer idEntita = (Integer) params.get("idEntita");

		certificazione = ritenutaAccontoManager.caricaCertificazioneRitenute(anno, idEntita);

		return certificazione;
	}

	@Override
	public List<ContributoPrevidenziale> caricaContributiEnasarco() {
		return ritenutaAccontoManager.caricaContributiEnasarco();
	}

	@Override
	public List<ContributoPrevidenziale> caricaContributiINPS() {
		return ritenutaAccontoManager.caricaContributiINPS();
	}

	@Override
	public List<ContributoPrevidenziale> caricaContributiPrevidenziali() {
		return ritenutaAccontoManager.caricaContributiPrevidenziali();
	}

	@Override
	public List<Prestazione> caricaPrestazioni() {
		return ritenutaAccontoManager.caricaPrestazioni();
	}

	@Override
	public List<SituazioneRitenuteAccontoDTO> caricaSituazioneRitenuteAccont(
			ParametriSituazioneRitenuteAcconto parametri) {
		return ritenutaAccontoManager.caricaSituazioneRitenuteAccont(parametri);
	}

	@Override
	public CausaleRitenutaAcconto salvaCausaleRitenutaAcconto(CausaleRitenutaAcconto causaleRitenutaAcconto) {
		return ritenutaAccontoManager.salvaCausaleRitenutaAcconto(causaleRitenutaAcconto);
	}

	@Override
	public ContributoPrevidenziale salvaContributoPrevidenziale(ContributoPrevidenziale contributoPrevidenziale) {
		return ritenutaAccontoManager.salvaContributoPrevidenziale(contributoPrevidenziale);
	}

	@Override
	public Prestazione salvaPrestazione(Prestazione prestazione) {
		return ritenutaAccontoManager.salvaPrestazione(prestazione);
	}

}
