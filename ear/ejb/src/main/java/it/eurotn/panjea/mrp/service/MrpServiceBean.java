package it.eurotn.panjea.mrp.service;

import it.eurotn.panjea.magazzino.domain.Giacenza;
import it.eurotn.panjea.mrp.domain.ArticoloAnagrafica;
import it.eurotn.panjea.mrp.domain.Bom;
import it.eurotn.panjea.mrp.domain.RisultatoMRPArticoloBucket;
import it.eurotn.panjea.mrp.domain.RisultatoMrpFlat;
import it.eurotn.panjea.mrp.manager.interfaces.MrpBomExplosionManager;
import it.eurotn.panjea.mrp.manager.interfaces.MrpCalcoloManager;
import it.eurotn.panjea.mrp.manager.interfaces.MrpManager;
import it.eurotn.panjea.mrp.manager.interfaces.MrpOrdiniGenerator;
import it.eurotn.panjea.mrp.service.interfaces.MrpService;
import it.eurotn.panjea.mrp.util.ArticoloConfigurazioneKey;
import it.eurotn.panjea.mrp.util.ParametriMrpRisultato;

import java.net.ConnectException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.MrpService")
@SecurityDomain("PanjeaLoginModule")
@RemoteBinding(jndiBinding = "Panjea.MrpService")
@PermitAll
public class MrpServiceBean implements MrpService {

	@EJB
	private MrpManager mrpManager;

	@EJB
	private MrpCalcoloManager mrpCalcoloManager;

	@EJB
	private MrpBomExplosionManager mrpBomExplosionManager;

	@EJB
	private MrpOrdiniGenerator mrpOrdiniGenerator;

	@Override
	public List<Giacenza> calcolaGiacenze(int idDeposito, Date data) {
		return mrpManager.calcolaGiacenzeFlat(idDeposito, data);
	}

	@Override
	public void calcolaMrp(int numBucket, Date startDate, Integer idAreaOrdine) throws ConnectException {
		mrpCalcoloManager.calcola(numBucket, startDate, idAreaOrdine);
	}

	@Override
	public void cancellaRigheRisultati(List<RisultatoMrpFlat> selectedObjects) {
		mrpManager.cancellaRigheRisultati(selectedObjects);
	}

	@Override
	public List<ArticoloAnagrafica> caricaArticoloAnagrafica() {
		return mrpManager.caricaArticoloAnagrafica();
	}

	@Override
	public List<Object[]> caricaBomBase() {
		return mrpManager.caricaBomBase();
	}

	@Override
	public List<Object[]> caricaBomConfigurazioni(Set<Integer> configurazioniUtilizzate) {
		return mrpManager.caricaBomConfigurazioni(configurazioniUtilizzate);
	}

	@Override
	public List<Object[]> caricaBoms(Set<Integer> configurazioniUtilizzate) {
		List<Object[]> result = caricaBomBase();
		if (configurazioniUtilizzate.size() > 0) {
			result.addAll(caricaBomConfigurazioni(configurazioniUtilizzate));
		}
		return result;
	}

	@Override
	public List<RisultatoMrpFlat> caricaRisultatoMrp(ParametriMrpRisultato parametriMrpRisultato) {
		return mrpManager.caricaRisultatoMrp(parametriMrpRisultato);
	}

	@Override
	public Map<ArticoloConfigurazioneKey, Bom> esplodiBoms() {
		return mrpBomExplosionManager.esplodiBoms();
	}

	@Override
	public Long generaOrdini(Integer[] idRisultatiMrp) {
		return mrpOrdiniGenerator.generaOrdini(idRisultatiMrp);
	}

	@Override
	public List<Integer> getIdDepositi() {
		List<Integer> result = mrpManager.getIdDepositi();
		return result;
	}

	@Override
	public List<RisultatoMrpFlat> salvaRigheRisultato(List<RisultatoMrpFlat> righeMrpFlat) {
		return mrpManager.salvaRigheRisultato(righeMrpFlat);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void salvaRisultatoMRP(List<RisultatoMRPArticoloBucket> risultati) {
		mrpManager.salvaRisultatoMRP(risultati);
	}

	@Override
	public void svuotaRigheRisultati() {
		mrpManager.svuotaRigheRisultati();
	}

}
