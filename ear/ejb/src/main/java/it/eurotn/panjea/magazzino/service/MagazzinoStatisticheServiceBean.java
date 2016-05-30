package it.eurotn.panjea.magazzino.service;

import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.manager.interfaces.GiacenzaManager;
import it.eurotn.panjea.magazzino.manager.interfaces.IndiciRotazioneGiacenzaManager;
import it.eurotn.panjea.magazzino.manager.interfaces.MagazzinoMovimentazioneManager;
import it.eurotn.panjea.magazzino.manager.interfaces.MagazzinoValorizzazioneManager;
import it.eurotn.panjea.magazzino.manager.interfaces.StatisticheArticoloManager;
import it.eurotn.panjea.magazzino.manager.interfaces.articolo.ValorizzazioneDistintaBaseManager;
import it.eurotn.panjea.magazzino.service.interfaces.MagazzinoStatisticheService;
import it.eurotn.panjea.magazzino.util.IndiceGiacenzaArticolo;
import it.eurotn.panjea.magazzino.util.MovimentazioneArticolo;
import it.eurotn.panjea.magazzino.util.ParametriRicercaMovimentazione;
import it.eurotn.panjea.magazzino.util.ParametriRicercaMovimentazioneArticolo;
import it.eurotn.panjea.magazzino.util.ParametriRicercaValorizzazione;
import it.eurotn.panjea.magazzino.util.RigaMovimentazione;
import it.eurotn.panjea.magazzino.util.StatisticheArticolo;
import it.eurotn.panjea.magazzino.util.ValorizzazioneArticolo;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriCalcoloIndiciRotazioneGiacenza;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriValorizzazioneDistinte;
import it.eurotn.panjea.mrp.domain.Bom;
import it.eurotn.panjea.mrp.util.ArticoloConfigurazioneKey;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.MagazzinoStatisticheService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.MagazzinoStatisticheService")
public class MagazzinoStatisticheServiceBean implements MagazzinoStatisticheService {

	@EJB
	private IndiciRotazioneGiacenzaManager indiciRotazioneGiacenzaManager;

	@EJB
	private MagazzinoMovimentazioneManager magazzinoMovimentazioneManager;

	@EJB
	private StatisticheArticoloManager statisticheArticoloManager;

	@EJB
	private MagazzinoValorizzazioneManager magazzinoValorizzazioneManager;

	@EJB
	private GiacenzaManager giacenzaManager;

	@EJB
	private ValorizzazioneDistintaBaseManager valorizzazioneDistintaBaseManager;

	@Resource
	private SessionContext sessionContext;

	@Override
	public Double calcolaDisponibilitaArticolo(int idArticolo, Date data, int idDeposito) {
		Articolo articolo = new Articolo();
		articolo.setId(idArticolo);
		return giacenzaManager.calcolaDisponibilitaArticolo(articolo, data, idDeposito);
	}

	@Override
	public List<IndiceGiacenzaArticolo> calcolaIndiciRotazione(ParametriCalcoloIndiciRotazioneGiacenza parametri) {
		return indiciRotazioneGiacenzaManager.calcolaIndiciRotazione(parametri);
	}

	@Override
	public List<RigaMovimentazione> caricaMovimentazione(ParametriRicercaMovimentazione parametriRicercaMovimentazione,
			int page, int sizeOfPage) {
		return magazzinoMovimentazioneManager.caricaMovimentazione(parametriRicercaMovimentazione, page, sizeOfPage);
	}

	@Override
	public MovimentazioneArticolo caricaMovimentazioneArticolo(
			ParametriRicercaMovimentazioneArticolo parametriRicercaMovimentazioneArticolo) {
		return magazzinoMovimentazioneManager.caricaMovimentazioneArticolo(parametriRicercaMovimentazioneArticolo);
	}

	@Override
	public StatisticheArticolo caricaStatisticheArticolo(Articolo articolo, Integer anno) {
		return statisticheArticoloManager.caricaStatisticheArticolo(articolo, anno);
	}

	@Override
	public List<ValorizzazioneArticolo> caricaValorizzazione(Map<Object, Object> parametri) {
		return magazzinoValorizzazioneManager.caricaValorizzazione(parametri);
	}

	@Override
	public List<ValorizzazioneArticolo> caricaValorizzazione(
			ParametriRicercaValorizzazione parametriRicercaValorizzazione) {
		return magazzinoValorizzazioneManager.caricaValorizzazione(parametriRicercaValorizzazione);
	}

	@Override
	public Map<ArticoloConfigurazioneKey, Bom> valorizzaDistinte(
			ParametriValorizzazioneDistinte parametriValorizzazioneDistinte) {
		return valorizzazioneDistintaBaseManager.valorizzaDistinte(parametriValorizzazioneDistinte);
	}

}
