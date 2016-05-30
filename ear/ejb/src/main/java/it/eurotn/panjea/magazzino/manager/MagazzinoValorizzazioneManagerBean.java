package it.eurotn.panjea.magazzino.manager;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import commonj.work.Work;
import commonj.work.WorkItem;
import de.myfoo.commonj.util.ThreadPool;
import de.myfoo.commonj.work.FooWorkManager;
import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.anagrafica.manager.depositi.interfaces.DepositiManager;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.manager.interfaces.MagazzinoValorizzazioneDepositoManager;
import it.eurotn.panjea.magazzino.manager.interfaces.MagazzinoValorizzazioneManager;
import it.eurotn.panjea.magazzino.util.ParametriRicercaValorizzazione;
import it.eurotn.panjea.magazzino.util.ParametriRicercaValorizzazione.EModalitaValorizzazione;
import it.eurotn.panjea.magazzino.util.ValorizzazioneArticolo;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

/**
 * Manager per valorizzazione magazzino.
 *
 * @author fattazzo
 * @version 1.0, 21/mag/08
 */
@Stateless(name = "Panjea.MagazzinoValorizzazioneManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.MagazzinoValorizzazioneManager")
public class MagazzinoValorizzazioneManagerBean implements MagazzinoValorizzazioneManager {

    public class ValorizzazioneWork implements Work {

        private List<ValorizzazioneArticolo> valorizzazioniArticoli = new ArrayList<>();
        private ParametriRicercaValorizzazione parametriRicercaValorizzazione;
        private Integer idDeposito;
        private MagazzinoValorizzazioneDepositoManager managerValorizzazione;

        /**
         *
         * @param managerValorizzazione
         *            manager per la val.
         * @param parametriRicercaValorizzazione
         *            parametri
         * @param idDeposito
         *            deposito da valorizzare
         */
        public ValorizzazioneWork(final MagazzinoValorizzazioneDepositoManager managerValorizzazione,
                final ParametriRicercaValorizzazione parametriRicercaValorizzazione, final Integer idDeposito) {
            this.managerValorizzazione = managerValorizzazione;
            this.parametriRicercaValorizzazione = parametriRicercaValorizzazione;
            this.idDeposito = idDeposito;
        }

        /**
         *
         * @return risultati della valorizzazione
         */
        public List<ValorizzazioneArticolo> getValorizzazione() {
            return valorizzazioniArticoli;
        }

        @Override
        public boolean isDaemon() {
            return false;
        }

        @Override
        public void release() {
            valorizzazioniArticoli = null;
        }

        @Override
        public void run() {
            valorizzazioniArticoli = managerValorizzazione.caricaValorizzazione(parametriRicercaValorizzazione,
                    idDeposito);
        }

    }

    private static Logger logger = Logger.getLogger(MagazzinoValorizzazioneManagerBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @Resource
    private SessionContext sessionContext;

    @EJB(beanName = "Panjea.MagazzinoValorizzazioneDepositoManager")
    private MagazzinoValorizzazioneDepositoManager magazzinoValorizzazioneDepositoManager;

    @EJB(beanName = "Panjea.MagazzinoValorizzazioneDepositoCostoStandardManager")
    private MagazzinoValorizzazioneDepositoManager magazzinoValorizzazioneDepositoCostoStandardManager;

    @EJB(beanName = "Panjea.MagazzinoValorizzazioneDepositoUltimoCostoManager")
    private MagazzinoValorizzazioneDepositoManager magazzinoValorizzazioneDepositoUltimoCostoManager;

    @EJB(beanName = "Panjea.MagazzinoValorizzazioneDepositoUltimoCostoAziendaManager")
    private MagazzinoValorizzazioneDepositoManager magazzinoValorizzazioneDepositoUltimoCostoAziendaManager;

    @EJB(beanName = "Panjea.MagazzinoValorizzazioneDepositoCostoMedioManager")
    private MagazzinoValorizzazioneDepositoManager magazzinoValorizzazioneDepositoCostoMedioManager;

    @EJB
    private DepositiManager depositiManager;

    @Override
    public List<ValorizzazioneArticolo> caricaValorizzazione(Map<Object, Object> parametri) {
        logger.debug("--> Enter caricaValorizzazione");

        String nomeParameterData = parametri.containsKey("DATA") ? "DATA" : "data";
        Object parameterData = parametri.get(nomeParameterData);
        Integer modalitaVisualizzazioneInt = (Integer) parametri.get("modalitaVisualizzazione");

        Date data = Calendar.getInstance().getTime();

        if (parameterData instanceof String) {
            String dataString = (String) parameterData;
            DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
            try {
                data = df.parse(dataString);
            } catch (ParseException e) {
                logger.error("--> Errore durante il parse della data.", e);
            }
        } else {
            data = (Date) parameterData;
        }

        ParametriRicercaValorizzazione parametriRicercaValorizzazione = new ParametriRicercaValorizzazione();
        parametriRicercaValorizzazione.setData(data);

        if (!StringUtils.isBlank((String) parametri.get("articoliSelezionati"))) {
            List<ArticoloLite> articoliSelezionati = new ArrayList<ArticoloLite>();
            String[] articoli = ((String) parametri.get("articoliSelezionati")).split(",");
            for (String idArticolo : articoli) {
                ArticoloLite art = new ArticoloLite();
                art.setId(Integer.valueOf(idArticolo));
                articoliSelezionati.add(art);
            }
            parametriRicercaValorizzazione.setArticoliLite(articoliSelezionati);
        }

        if (parametri.get("consideraGiacenzaZero") != null) {
            parametriRicercaValorizzazione.setConsideraGiacenzaZero((Boolean) parametri.get("consideraGiacenzaZero"));
        }

        if (parametri.get("deposito") != null) {
            List<Deposito> depositi = new ArrayList<Deposito>();
            Deposito deposito = new Deposito();
            deposito.setId((Integer) parametri.get("deposito"));
            depositi.add(deposito);
            parametriRicercaValorizzazione.setDepositi(depositi);
            parametriRicercaValorizzazione.setTuttiDepositi(false);
        }
        if (parametri.get("idDepositiSelezionati") != null) {
            String idDepositiSelezionati = (String) parametri.get("idDepositiSelezionati");
            String[] idDepositi = idDepositiSelezionati.split(",");
            List<Deposito> depositi = new ArrayList<Deposito>();
            for (String idDeposito : idDepositi) {
                if (!idDeposito.isEmpty()) {
                    Deposito deposito = new Deposito();
                    deposito.setId(Integer.parseInt(idDeposito));
                    depositi.add(deposito);
                }
            }
            parametriRicercaValorizzazione.setDepositi(depositi);
            parametriRicercaValorizzazione.setTuttiDepositi(depositi.size() == 0);
        }

        if (parametri.get("consideraArticoliDisabilitati") != null) {
            parametriRicercaValorizzazione
                    .setConsideraArticoliDisabilitati((Boolean) parametri.get("consideraArticoliDisabilitati"));
        }

        parametriRicercaValorizzazione
                .setModalitaValorizzazione(EModalitaValorizzazione.values()[modalitaVisualizzazioneInt]);

        logger.debug("--> Exit caricaValorizzazione");

        return caricaValorizzazione(parametriRicercaValorizzazione);
    }

    @Override
    public List<ValorizzazioneArticolo> caricaValorizzazione(
            ParametriRicercaValorizzazione parametriRicercaValorizzazione) {
        logger.debug("--> Enter caricaValorizzazione");

        List<ValorizzazioneArticolo> valorizzazioniArticoli = new ArrayList<>();
        if (parametriRicercaValorizzazione.getDepositi().size() == 0) {
            parametriRicercaValorizzazione.setDepositi(depositiManager.caricaDepositi());
        }

        // create the thread pool for this work manager
        ThreadPool pool = new ThreadPool(5, 5, 100);

        // create the work manager
        FooWorkManager workManager = new FooWorkManager(pool, 0);

        List<WorkItem> valorizzazioniWorkItem = new ArrayList<>();

        MagazzinoValorizzazioneDepositoManager managerValorizzazione = getValorizzazioneDepositoManager(
                parametriRicercaValorizzazione.getModalitaValorizzazione());

        for (Deposito deposito : parametriRicercaValorizzazione.getDepositi()) {
            try {
                valorizzazioniWorkItem.add(workManager.schedule(new ValorizzazioneWork(managerValorizzazione,
                        parametriRicercaValorizzazione, deposito.getId())));
            } catch (Exception e) {
                logger.error("-->errore nel lanciare il task per la valorizzazione del deposito " + deposito, e);
                throw new RuntimeException(
                        "-->errore nel lanciare il task per la valorizzazione del deposito " + deposito, e);
            }
        }

        try {

            workManager.waitForAll(valorizzazioniWorkItem, 120000);
            for (WorkItem workItem : valorizzazioniWorkItem) {
                ValorizzazioneWork valWork = (ValorizzazioneWork) workItem.getResult();
                valorizzazioniArticoli.addAll(valWork.getValorizzazione());
            }
        } catch (Exception e) {
            logger.error("-->errore nell'aspettare i processi per la valorizzazione dei depositi", e);
            throw new RuntimeException("-->errore nell'aspettare i processi per la valorizzazione dei depositi ", e);
        } finally {
            workManager.shutdown();
        }
        return valorizzazioniArticoli;

    }

    @Override
    public MagazzinoValorizzazioneDepositoManager getValorizzazioneDepositoManager(
            EModalitaValorizzazione modalitaValorizzazione) {
        MagazzinoValorizzazioneDepositoManager managerValorizzazione = null;
        switch (modalitaValorizzazione) {
        case COSTO_STANDARD:
            managerValorizzazione = magazzinoValorizzazioneDepositoCostoStandardManager;
            break;
        case ULTIMO_COSTO_DEPOSITO:
            managerValorizzazione = magazzinoValorizzazioneDepositoUltimoCostoManager;
            break;
        case ULTIMO_COSTO_AZIENDA:
            managerValorizzazione = magazzinoValorizzazioneDepositoUltimoCostoAziendaManager;
            break;
        case COSTO_MEDIO_PONDERATO:
            managerValorizzazione = magazzinoValorizzazioneDepositoCostoMedioManager;
            break;
        default:
            managerValorizzazione = magazzinoValorizzazioneDepositoManager;
            break;
        }

        return managerValorizzazione;
    }
}
