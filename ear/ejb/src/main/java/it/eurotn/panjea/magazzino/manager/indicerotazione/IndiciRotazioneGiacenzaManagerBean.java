package it.eurotn.panjea.magazzino.manager.indicerotazione;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import commonj.work.WorkItem;
import de.myfoo.commonj.util.ThreadPool;
import de.myfoo.commonj.work.FooWorkManager;
import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.anagrafica.manager.depositi.interfaces.DepositiManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.SediAziendaManager;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.manager.interfaces.GiacenzaManager;
import it.eurotn.panjea.magazzino.manager.interfaces.IndiciRotazioneGiacenzaManager;
import it.eurotn.panjea.magazzino.manager.interfaces.MagazzinoMovimentazioneManager;
import it.eurotn.panjea.magazzino.manager.interfaces.articolo.ArticoloManager;
import it.eurotn.panjea.magazzino.util.ArticoloRicerca;
import it.eurotn.panjea.magazzino.util.IndiceGiacenzaArticolo;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriCalcoloIndiciRotazioneGiacenza;

@Stateless(name = "Panjea.IndiciRotazioneGiacenzaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.IndiciRotazioneGiacenzaManager")
public class IndiciRotazioneGiacenzaManagerBean implements IndiciRotazioneGiacenzaManager {

    private static Logger logger = Logger.getLogger(IndiciRotazioneGiacenzaManagerBean.class);

    @EJB
    private MagazzinoMovimentazioneManager magazzinoMovimentazioneManager;

    @EJB
    private GiacenzaManager giacenzaManager;

    @EJB
    private ArticoloManager articoloManager;

    @EJB
    private SediAziendaManager sediAziendaManager;

    @EJB
    private DepositiManager depositiManager;

    @Override
    public List<IndiceGiacenzaArticolo> calcolaIndiciRotazione(ParametriCalcoloIndiciRotazioneGiacenza parametri) {
        logger.debug("--> Enter calcolaIndiciRotazione");
        List<IndiceGiacenzaArticolo> indiciRotazione = new ArrayList<IndiceGiacenzaArticolo>();

        if (parametri.getDepositi() == null) {
            parametri.setDepositi(depositiManager.caricaDepositi());
        }

        if (parametri.getArticoli() == null || parametri.getArticoli().isEmpty()) {
            parametri.setArticoli(articoloManager.caricaArticoli());
        }

        List<WorkItem> worksResult = new ArrayList<WorkItem>();
        ThreadPool pool = new ThreadPool(10, 10, 100);
        FooWorkManager workManager = new FooWorkManager(pool, 0);

        try {
            for (Deposito deposito : parametri.getDepositi()) {
                GiacenzaDepositoWork work = new GiacenzaDepositoWork(deposito, parametri.getPeriodo().getDataIniziale(),
                        giacenzaManager, parametri.getArticoliIds());
                try {
                    worksResult.add(workManager.schedule(work));
                } catch (Exception e) {
                    logger.error("-->errore schedulare processi per il calcolo della giacenza ", e);
                    throw new RuntimeException(e);
                }
            }

            try {
                Collection<WorkItem> taskFiniti = new ArrayList<WorkItem>();
                Collection<WorkItem> taskIndiciRotazione = new ArrayList<WorkItem>();
                do {
                    taskFiniti = workManager.waitForAny(worksResult, 120000);
                    worksResult.removeAll(taskFiniti);
                    for (WorkItem workItem : taskFiniti) {
                        GiacenzaDepositoWork giacenzaWork = (GiacenzaDepositoWork) workItem.getResult();
                        Map<ArticoloLite, Double> giacenzeDeposito = giacenzaWork.getResult();
                        for (ArticoloRicerca articolo : parametri.getArticoli()) {
                            taskIndiciRotazione.add(workManager.schedule(new IndiceRotazioneWork(
                                    articolo.createProxyArticoloLite(), giacenzaWork.getDeposito().getDepositoLite(),
                                    parametri.getPeriodo(), giacenzeDeposito.get(articolo.createProxyArticoloLite()),
                                    magazzinoMovimentazioneManager, articoloManager)));
                        }
                    }
                } while (!worksResult.isEmpty());

                workManager.waitForAll(taskIndiciRotazione, 120000);

                for (WorkItem workItem : taskIndiciRotazione) {
                    indiciRotazione.add(((IndiceRotazioneWork) workItem.getResult()).getIndice());
                }

            } catch (Exception e) {
                logger.error("-->errore attesa processi per il calcolo della giacenza ", e);
                throw new RuntimeException(e);
            }

        } finally {
            workManager.shutdown();
        }

        logger.debug("--> Exit calcolaIndiciRotazione");

        return indiciRotazione;
    }
}
