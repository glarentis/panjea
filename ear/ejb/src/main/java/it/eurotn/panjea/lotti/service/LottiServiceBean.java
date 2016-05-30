package it.eurotn.panjea.lotti.service;

import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.lotti.domain.Lotto;
import it.eurotn.panjea.lotti.domain.LottoInterno;
import it.eurotn.panjea.lotti.domain.RigaLotto;
import it.eurotn.panjea.lotti.manager.interfaces.ControlliManager;
import it.eurotn.panjea.lotti.manager.interfaces.LottiManager;
import it.eurotn.panjea.lotti.manager.rimanenzefinali.RimanenzeFinaliDTO;
import it.eurotn.panjea.lotti.manager.rimanenzefinali.interfaces.RimanenzeFinaliManager;
import it.eurotn.panjea.lotti.service.interfaces.LottiService;
import it.eurotn.panjea.lotti.util.GiacenzaLotto;
import it.eurotn.panjea.lotti.util.MovimentazioneLotto;
import it.eurotn.panjea.lotti.util.ParametriRicercaScadenzaLotti;
import it.eurotn.panjea.lotti.util.StatisticaLotto;
import it.eurotn.panjea.magazzino.domain.Articolo.TipoLotto;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.TipoMovimento;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.LottiService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.LottiService")
public class LottiServiceBean implements LottiService {

	@EJB
	private LottiManager lottiManager;

	@EJB
	private ControlliManager controlliManager;

	@EJB(beanName = "RimanenzeFinaliManagerBean")
	private RimanenzeFinaliManager rimanenzeFinaliManager;

	@Override
	public void cancellaLottiNonUtilizzati() {
		lottiManager.cancellaLottiNonUtilizzati();
	}

	@Override
	public List<ArticoloLite> caricaArticoliByCodiceLotto(Lotto lotto) {
		return lottiManager.caricaArticoliByCodiceLotto(lotto);
	}

	@Override
	public List<ArticoloLite> caricaArticoliByCodiceLotto(Lotto lotto, boolean filtraDataScadenza) {
		return lottiManager.caricaArticoliByCodiceLotto(lotto, filtraDataScadenza);
	}

	@Override
	public List<Lotto> caricaLotti(ArticoloLite articolo, DepositoLite deposito, TipoMovimento tipoMovimento,
			boolean storno, String codice,Date dataScadenza, Date dataInizioRicercaScadenza,boolean cercaSoloLottiAperti ) {
		return lottiManager.caricaLotti(articolo, deposito, tipoMovimento, storno, codice,dataScadenza,dataInizioRicercaScadenza,cercaSoloLottiAperti);
	}

	@Override
	public List<Lotto> caricaLotti(ArticoloLite articolo, TipoLotto tipoLotto, String codice) {
		return lottiManager.caricaLotti(articolo, tipoLotto, codice);
	}

	@Override
	public List<StatisticaLotto> caricaLottiInScadenza(ParametriRicercaScadenzaLotti parametri) {
		return controlliManager.caricaLottiInScadenza(parametri);
	}

	@Override
	public Lotto caricaLotto(Lotto lotto) {
		return lottiManager.caricaLotto(lotto);
	}

	@Override
	public Lotto caricaLotto(String codice, Date dataScadenza, int idArticolo) {
		ArticoloLite articoloLite = new ArticoloLite();
		articoloLite.setId(idArticolo);
		return lottiManager.caricaLotto(codice, dataScadenza, articoloLite);
	}

	@Override
	public List<MovimentazioneLotto> caricaMovimentazioneLotti(Map<Object, Object> param) {
		String lotti = (String) param.get("idLotti");
		String[] idLotti = lotti.split(",");

		Set<Integer> setIdLotti = new TreeSet<Integer>();
		for (int i = 0; i < idLotti.length; i++) {
			try {
				setIdLotti.add(new Integer(idLotti[i]));
			} catch (NumberFormatException e) {
				System.out.println("Num lotto non valido" + idLotti[i]);
			}
		}
		return lottiManager.caricaMovimentazioneLotti(setIdLotti);
	}

	@Override
	public List<MovimentazioneLotto> caricaMovimentazioneLotto(Lotto lotto) {
		return lottiManager.caricaMovimentazioneLotto(lotto);
	}

	@Override
	public List<MovimentazioneLotto> caricaMovimentazioneLottoInterno(LottoInterno lotto) {
		return lottiManager.caricaMovimentazioneLottoInterno(lotto);
	}

	@Override
	public List<RigaLotto> caricaRigheLotto(AreaMagazzino areaMagazzino) {
		return lottiManager.caricaRigheLotto(areaMagazzino);
	}

	@Override
	public List<RigaLotto> caricaRigheLotto(RigaArticolo rigaArticolo) {
		return lottiManager.caricaRigheLotto(rigaArticolo);
	}

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

	@Override
	public List<StatisticaLotto> caricaSituazioneLotti(ArticoloLite articoloLite) {
		return lottiManager.caricaSituazioneLotti(articoloLite);
	}

	@Override
	public Lotto salvaLotto(Lotto lotto) {
		return lottiManager.salvaLotto(lotto);
	}

	@Override
	public Set<GiacenzaLotto> verificaGiacenzeLotti(Map<Object, Object> parametri) {
		return controlliManager.verificaGiacenzeLotti();
	}

}
