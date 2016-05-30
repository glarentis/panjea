package it.eurotn.panjea.lotti.manager;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.anagrafica.manager.depositi.interfaces.DepositiManager;
import it.eurotn.panjea.lotti.manager.LottiManagerBean.RimanenzaLotto;
import it.eurotn.panjea.lotti.manager.interfaces.ControlliManager;
import it.eurotn.panjea.lotti.manager.interfaces.LottiManager;
import it.eurotn.panjea.lotti.util.GiacenzaLotto;
import it.eurotn.panjea.lotti.util.ParametriRicercaScadenzaLotti;
import it.eurotn.panjea.lotti.util.StatisticaLotto;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.manager.interfaces.MagazzinoValorizzazioneManager;
import it.eurotn.panjea.magazzino.util.CategoriaLite;
import it.eurotn.panjea.magazzino.util.ParametriRicercaValorizzazione;
import it.eurotn.panjea.magazzino.util.ValorizzazioneArticolo;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

@Stateless(name = "Panjea.LottiControlliManagerBean")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.LottiControlliManagerBean")
public class ControlliManagerBean implements ControlliManager {

    private class GiacenzaLottoComparator implements Comparator<GiacenzaLotto>, Serializable {
        private static final long serialVersionUID = 380866475967324461L;

        @Override
        public int compare(GiacenzaLotto o1, GiacenzaLotto o2) {

            if (o1.getDeposito().getCodice().compareTo(o2.getDeposito().getCodice()) != 0) {
                return o1.getDeposito().getCodice().compareTo(o2.getDeposito().getCodice());
            }

            if (o1.getCategoria().getCodice().compareTo(o2.getCategoria().getCodice()) != 0) {
                return o1.getCategoria().getCodice().compareTo(o2.getCategoria().getCodice());
            }

            if (o1.getArticolo().getCodice().compareTo(o2.getArticolo().getCodice()) != 0) {
                return o1.getArticolo().getCodice().compareTo(o2.getArticolo().getCodice());
            }

            return 0;

        }
    }

    private static Logger logger = Logger.getLogger(ControlliManagerBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private MagazzinoValorizzazioneManager magazzinoValorizzazioneManager;

    @EJB
    private LottiManager lottiManager;

    @Resource
    protected SessionContext sessionContext;

    @EJB
    private DepositiManager depositiManager;

    /**
     * Carica l'id di tutti gli articoli che non gestiscono i lotti.
     *
     * @return id articoli
     */
    @SuppressWarnings("unchecked")
    private List<Integer> caricaArticoliSenzaLotti() {

        StringBuilder sb = new StringBuilder();
        sb.append("select art.id ");
        sb.append("from Articolo art ");
        sb.append("where art.codiceAzienda = :codiceAzienda and art.tipoLotto = 0 ");

        Query query = panjeaDAO.prepareQuery(sb.toString());
        query.setParameter("codiceAzienda", getAzienda());

        List<Integer> articoli = new ArrayList<Integer>();
        try {
            articoli = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            logger.error("--> errore durante il caricamento degli articoli.", e);
            throw new RuntimeException("errore durante il caricamento degli articoli.", e);
        }

        List<Integer> result = new ArrayList<Integer>();
        for (Integer objects : articoli) {
            result.add(objects);
        }

        return result;
    }

    @Override
    public List<StatisticaLotto> caricaLottiInScadenza(ParametriRicercaScadenzaLotti parametri) {

        List<StatisticaLotto> statisticheLottiScadenti = new ArrayList<StatisticaLotto>();

        List<CategoriaLite> categorie = null;
        if (!parametri.isTutteCategorie()) {
            categorie = parametri.getCategorie();
        }

        // carico la situazione degli articoli per il deposito
        List<StatisticaLotto> statisticheLotti = lottiManager.caricaSituazioneLotti(null, parametri.getDeposito(), null,
                categorie, RimanenzaLotto.TUTTE, null, null, null);
        for (StatisticaLotto statisticaLotto : statisticheLotti) {
            Double rimanenza = statisticaLotto.getRimanenza();

            if (statisticaLotto.getLotto().getDataScadenza() != null && rimanenza.doubleValue() != 0
                    && statisticaLotto.getLotto().getDataScadenza().compareTo(parametri.getDataScadenza()) <= 0) {
                statisticheLottiScadenti.add(statisticaLotto);
            }
        }

        return statisticheLottiScadenti;
    }

    /**
     * Carica la situazione lotti per tutti i depositi aziendali e per tutti gli articoli.
     *
     * @return situazione
     */
    private Map<String, GiacenzaLotto> caricaSituazioneLotti() {
        logger.debug("--> Enter caricaSituazioneLotti");

        Map<String, GiacenzaLotto> mapResult = new HashMap<String, GiacenzaLotto>();

        List<DepositoLite> depositiAzienda = depositiManager.caricaDepositiAzienda();
        for (DepositoLite deposito : depositiAzienda) {

            List<StatisticaLotto> statisticheLotti = lottiManager.caricaSituazioneLotti(null, deposito, null, null,
                    RimanenzaLotto.TUTTE, null, null, null);
            for (StatisticaLotto statisticaLotto : statisticheLotti) {
                ArticoloLite articolo = statisticaLotto.getArticolo();
                Double rimanenza = statisticaLotto.getRimanenza();

                if (rimanenza.doubleValue() != 0) {
                    String key = deposito.getId() + "$" + articolo.getId();
                    GiacenzaLotto giacenzaLotto = new GiacenzaLotto();
                    if (mapResult.containsKey(key)) {
                        giacenzaLotto = mapResult.get(key);
                        BigDecimal rim = BigDecimal.valueOf(giacenzaLotto.getGiacenzaLotti())
                                .add(BigDecimal.valueOf(rimanenza));
                        giacenzaLotto.setGiacenzaLotti(rim.doubleValue());
                    } else {
                        giacenzaLotto.setArticolo(articolo);
                        giacenzaLotto.setCategoria(new CategoriaLite(articolo.getCategoria()));
                        giacenzaLotto.setDeposito(deposito);

                        giacenzaLotto.setGiacenzaLotti(rimanenza);
                        giacenzaLotto.setGiacenzaMagazzino(0.0);
                    }

                    mapResult.put(key, giacenzaLotto);
                }
            }
        }

        logger.debug("--> Exit caricaSituazioneLotti");
        return mapResult;
    }

    /**
     *
     * @return codice azienda loggata
     */
    private String getAzienda() {
        return ((JecPrincipal) sessionContext.getCallerPrincipal()).getCodiceAzienda();
    }

    @Override
    public Set<GiacenzaLotto> verificaGiacenzeLotti() {

        Map<String, GiacenzaLotto> mapResult = new HashMap<String, GiacenzaLotto>();

        // faccio la valorizzazione del magazzino e aggingo i risultati in mappa
        ParametriRicercaValorizzazione parametri = new ParametriRicercaValorizzazione();
        List<ValorizzazioneArticolo> valorizzazioni = magazzinoValorizzazioneManager.caricaValorizzazione(parametri);
        for (ValorizzazioneArticolo valorizzazioneArticolo : valorizzazioni) {
            GiacenzaLotto giacenzaLotto = new GiacenzaLotto();
            giacenzaLotto.setArticolo(valorizzazioneArticolo.getArticolo());
            giacenzaLotto.setCategoria(valorizzazioneArticolo.getCategoria());
            giacenzaLotto.setDeposito(valorizzazioneArticolo.getDeposito());
            giacenzaLotto.setGiacenzaMagazzino(valorizzazioneArticolo.getGiacenza());
            giacenzaLotto.setGiacenzaLotti(0.0);

            String key = valorizzazioneArticolo.getDeposito().getId() + "$"
                    + valorizzazioneArticolo.getArticolo().getId();
            mapResult.put(key, giacenzaLotto);
        }

        // trovo le situazioni lotto per deposito e le aggiungo se la rimanenza è diversa dalla
        // giacenza di magazzino
        Map<String, GiacenzaLotto> rimanenzeLotti = caricaSituazioneLotti();
        for (Entry<String, GiacenzaLotto> rimanenza : rimanenzeLotti.entrySet()) {
            GiacenzaLotto giacenzaLotto = mapResult.get(rimanenza.getKey());
            if (giacenzaLotto == null) {
                mapResult.put(rimanenza.getKey(), rimanenza.getValue());
            } else {
                Double giacenzaMagazzino = giacenzaLotto.getGiacenzaMagazzino();
                if (giacenzaMagazzino.doubleValue() != rimanenza.getValue().getGiacenzaLotti().doubleValue()) {
                    giacenzaLotto.setGiacenzaLotti(rimanenza.getValue().getGiacenzaLotti().doubleValue());
                } else {
                    mapResult.remove(rimanenza.getKey());
                }
            }
        }

        Set<GiacenzaLotto> result = new TreeSet<GiacenzaLotto>(new GiacenzaLottoComparator());

        List<Integer> idArticoli = caricaArticoliSenzaLotti();
        for (GiacenzaLotto giacenzaLotto : mapResult.values()) {
            if (giacenzaLotto.getGiacenzaLotti().doubleValue() != 0
                    || !idArticoli.contains(giacenzaLotto.getArticolo().getId())) {
                result.add(giacenzaLotto);
            }
        }

        return result;
    }

}
