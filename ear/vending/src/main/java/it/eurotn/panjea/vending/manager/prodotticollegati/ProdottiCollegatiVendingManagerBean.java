package it.eurotn.panjea.vending.manager.prodotticollegati;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.commons.collections.list.SetUniqueList;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.ejb.QueryImpl;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.transform.Transformers;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.util.ArticoloRicerca;
import it.eurotn.panjea.magazzino.util.ParametriRicercaArticolo;
import it.eurotn.panjea.manutenzioni.domain.ArticoloMI;
import it.eurotn.panjea.manutenzioni.domain.Installazione;
import it.eurotn.panjea.manutenzioni.domain.ProdottoCollegato;
import it.eurotn.panjea.manutenzioni.domain.ProdottoInstallazione;
import it.eurotn.panjea.manutenzioni.domain.TipoProdottoCollegato;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.vending.domain.Distributore;
import it.eurotn.panjea.vending.domain.Modello;
import it.eurotn.panjea.vending.domain.ProdottoDistributore;
import it.eurotn.panjea.vending.domain.ProdottoModello;
import it.eurotn.panjea.vending.domain.ProdottoTipoModello;
import it.eurotn.panjea.vending.manager.prodotticollegati.interfaces.ProdottiCollegatiVendingManager;

@Stateless(name = "Panjea.ProdottiCollegatiVendingManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.ProdottiCollegatiVendingManager")
public class ProdottiCollegatiVendingManagerBean implements ProdottiCollegatiVendingManager {

    private static final Logger LOGGER = Logger.getLogger(ProdottiCollegatiVendingManagerBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @Override
    public List<ProdottoCollegato> caricaProdottiCollegatiByDistributore(Integer idDistributore) {
        LOGGER.debug("--> Enter caricaProdottiCollegatiByDistributore");

        Distributore distributore = null;
        try {
            distributore = panjeaDAO.load(Distributore.class, idDistributore);
        } catch (Exception e) {
            LOGGER.error("--> Errore durante il caricamento del distributore.", e);
            throw new GenericException("Errore durante il caricamento del distributore.", e);
        }

        // aggiungo i prodotti della lista definitiva del modello
        List<ProdottoCollegato> prodotti = getListaDefinitivaByModello(
                distributore.getDatiVending().getModello().getId());
        // aggiungo i prodotti del distributore
        prodotti.addAll(caricaProdottiDistributore(distributore));

        LOGGER.debug("--> Exit caricaProdottiCollegatiByDistributore");
        return prodotti;
    }

    @Override
    public List<ProdottoCollegato> caricaProdottiCollegatiByInstallazione(Integer idInstallazione) {
        LOGGER.debug("--> Enter caricaProdottiCollegatiByInstallazione");

        Installazione installazione = null;
        try {
            installazione = panjeaDAO.load(Installazione.class, idInstallazione);
        } catch (ObjectNotFoundException e) {
            LOGGER.error("--> errore durante il caricamento dell'installazione", e);
            throw new GenericException("errore durante il caricamento dell'installazione", e);
        }

        List<ProdottoCollegato> prodotti = new ArrayList<>();
        if (installazione.getArticolo() != null) {
            ArticoloMI articolo = (ArticoloMI) ((HibernateProxy) installazione.getArticolo())
                    .getHibernateLazyInitializer().getImplementation();
            if (articolo instanceof Distributore) {
                // aggiungo i prodotti della lista definitiva del distributore
                prodotti.addAll(getListaDefinitivaByDistributore((Distributore) articolo));
            }
        }
        // aggiungo i prodotti dell'installazione
        prodotti.addAll(caricaProdottiInstallazione(installazione));

        return prodotti;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ProdottoCollegato> caricaProdottiCollegatiByModello(Integer idModello) {
        LOGGER.debug("--> Enter caricaProdottiCollegatiByModello");

        Modello modello = null;
        try {
            modello = panjeaDAO.load(Modello.class, idModello);
        } catch (Exception e) {
            LOGGER.error("--> errore durante il caricamento del modello", e);
            throw new GenericException("errore durante il caricamento del modello", e);
        }

        List<ProdottoCollegato> prodotti = new ArrayList<>();

        // aggiungo tutti i prodotti del tipo modello
        prodotti.addAll(caricaProdottiCollegatiByTipoModello(modello.getTipoModello().getId()));

        StringBuilder sb = new StringBuilder();
        sb.append(
                "select art.id as idArticolo, art.version as versionArticolo, art.codice as codiceArticolo, art.descrizioneLinguaAziendale as descrizioneArticolo, art.provenienzaPrezzoArticolo as provenienzaPrezzoArticolo, ");
        sb.append("p.id as id, p.version as version, p.tipo as tipo, p.modello as modello ");
        sb.append("from ProdottoModello p inner join p.articolo art ");
        sb.append("where p.modello = :paramModello");

        Query query = panjeaDAO.prepareQuery(sb.toString());
        ((QueryImpl) query).getHibernateQuery().setResultTransformer(Transformers.aliasToBean(ProdottoModello.class));
        query.setParameter("paramModello", modello);

        List<ProdottoCollegato> prodottiModello = new ArrayList<>();
        try {
            prodottiModello = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            LOGGER.error("--> errore durante il caricamento dei prodotti per il modello " + idModello, e);
            throw new GenericException("errore durante il caricamento dei prodotti per il modello " + idModello, e);
        }
        prodotti.addAll(prodottiModello);

        LOGGER.debug("--> Exit caricaProdottiCollegatiByModello");
        return prodotti;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ProdottoCollegato> caricaProdottiCollegatiByTipoModello(Integer idTipoModello) {
        LOGGER.debug("--> Enter caricaProdottiCollegatiByTipoModello");

        StringBuilder sb = new StringBuilder();
        sb.append(
                "select art.id as idArticolo, art.version as versionArticolo, art.codice as codiceArticolo, art.descrizioneLinguaAziendale as descrizioneArticolo, art.provenienzaPrezzoArticolo as provenienzaPrezzoArticolo, ");
        sb.append("p.id as id, p.version as version, p.tipo as tipo, p.tipoModello as tipoModello ");
        sb.append("from ProdottoTipoModello p inner join p.articolo art ");
        sb.append("where p.tipoModello.id = :paramIdTipoModello");

        Query query = panjeaDAO.prepareQuery(sb.toString());
        ((QueryImpl) query).getHibernateQuery()
                .setResultTransformer(Transformers.aliasToBean(ProdottoTipoModello.class));
        query.setParameter("paramIdTipoModello", idTipoModello);

        List<ProdottoCollegato> prodotti = new ArrayList<>();
        try {
            prodotti = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            LOGGER.error("--> errore durante il caricamento dei prodotti per il tipo modello " + idTipoModello, e);
            throw new GenericException(
                    "errore durante il caricamento dei prodotti per il tipo modello " + idTipoModello, e);
        }

        LOGGER.debug("--> Exit caricaProdottiCollegatiByTipoModello");
        return prodotti;
    }

    @SuppressWarnings("unchecked")
    private List<ProdottoCollegato> caricaProdottiDistributore(Distributore distributore) {
        StringBuilder sb = new StringBuilder();
        sb.append(
                "select art.id as idArticolo, art.version as versionArticolo, art.codice as codiceArticolo, art.descrizioneLinguaAziendale as descrizioneArticolo, art.provenienzaPrezzoArticolo as provenienzaPrezzoArticolo, ");
        sb.append("p.id as id, p.version as version, p.tipo as tipo, dist.datiVending as datiVendingDistributore ");
        sb.append("from ProdottoDistributore p inner join p.articolo art, Distributore dist ");
        sb.append("where dist = :paramDistributore ");
        sb.append("and p.datiVendingDistributore = dist.datiVending");

        Query query = panjeaDAO.prepareQuery(sb.toString());
        ((QueryImpl) query).getHibernateQuery()
                .setResultTransformer(Transformers.aliasToBean(ProdottoDistributore.class));
        query.setParameter("paramDistributore", distributore);

        List<ProdottoCollegato> prodottiDistributore = new ArrayList<>();
        try {
            prodottiDistributore = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            LOGGER.error("--> errore durante il caricamento dei prodotti per il distributore " + distributore.getId(),
                    e);
            throw new GenericException(
                    "errore durante il caricamento dei prodotti per il distributore " + distributore.getId(), e);
        }
        return prodottiDistributore;
    }

    @SuppressWarnings("unchecked")
    private List<ProdottoCollegato> caricaProdottiInstallazione(Installazione installazione) {

        StringBuilder sb = new StringBuilder();
        sb.append(
                "select art.id as idArticolo, art.version as versionArticolo, art.codice as codiceArticolo, art.descrizioneLinguaAziendale as descrizioneArticolo, art.provenienzaPrezzoArticolo as provenienzaPrezzoArticolo, ");
        sb.append("p.id as id, p.version as version, p.tipo as tipo, p.installazione as installazione ");
        sb.append("from ProdottoInstallazione p inner join p.articolo art ");
        sb.append("where p.installazione = :paramInstallazione");

        Query query = panjeaDAO.prepareQuery(sb.toString());
        ((QueryImpl) query).getHibernateQuery()
                .setResultTransformer(Transformers.aliasToBean(ProdottoInstallazione.class));
        query.setParameter("paramInstallazione", installazione);

        List<ProdottoCollegato> prodottiInstallazione = new ArrayList<>();
        try {
            prodottiInstallazione = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            LOGGER.error("--> errore durante il caricamento dei prodotti per l'installazione " + installazione.getId(),
                    e);
            throw new GenericException(
                    "errore durante il caricamento dei prodotti per l'installazione " + installazione.getId(), e);
        }
        return prodottiInstallazione;
    }

    private List<ProdottoCollegato> getListaDefinitivaByDistributore(Distributore distributore) {
        List<ProdottoCollegato> prodotti = new ArrayList<>();
        if (distributore.getDatiVending().getModello() != null) {
            prodotti.addAll(getListaDefinitivaByModello(distributore.getDatiVending().getModello().getId()));
        }

        List<ProdottoCollegato> prodottiDistributore = caricaProdottiDistributore(distributore);
        Set<ArticoloLite> articoliRimossi = new TreeSet<>();
        Map<ArticoloLite, ProdottoCollegato> prodottiAggiuntiMap = new HashMap<>();
        for (ProdottoCollegato prodotto : prodottiDistributore) {
            if (prodotto.getTipo() == TipoProdottoCollegato.RIMOSSO) {
                articoliRimossi.add(prodotto.getArticolo());
            } else {
                prodottiAggiuntiMap.put(prodotto.getArticolo(), prodotto);
            }
        }

        // aggiungo tutti i prodotti che sono stati aggiunti dal distributore
        prodotti.addAll(prodottiAggiuntiMap.values());

        // rimuovo tutti i prodotti che sono stati rimossi dal distributore
        for (Iterator<ProdottoCollegato> iterator = prodotti.iterator(); iterator.hasNext();) {
            ProdottoCollegato prodotto = iterator.next();
            if (articoliRimossi.contains(prodotto.getArticolo())) {
                iterator.remove();
            }
        }

        return prodotti;
    }

    private List<ProdottoCollegato> getListaDefinitivaByModello(Integer idModello) {

        List<ProdottoCollegato> prodotti = new ArrayList<>();
        prodotti.addAll(caricaProdottiCollegatiByModello(idModello));

        List<ProdottoCollegato> prodottiTipoModello = new ArrayList<>();
        List<ProdottoCollegato> prodottiModelloAggiunti = new ArrayList<>();
        List<ProdottoCollegato> prodottiModelloRimossi = new ArrayList<>();

        for (ProdottoCollegato prodotto : prodotti) {
            if (prodotto instanceof ProdottoTipoModello) {
                prodottiTipoModello.add(prodotto);
            } else {
                if (prodotto.getTipo() == TipoProdottoCollegato.AGGIUNTO) {
                    prodottiModelloAggiunti.add(prodotto);
                } else {
                    prodottiModelloRimossi.add(prodotto);
                }
            }
        }

        // rimuovo tutti i prodotti rimossi dal modello
        prodotti.removeAll(prodottiModelloRimossi);
        // rimuovo tutti gli articoli uguali a quelli rimossi dal modello
        List<Integer> articoliRimossi = new ArrayList<>();
        for (ProdottoCollegato prodottoRimosso : prodottiModelloRimossi) {
            articoliRimossi.add(prodottoRimosso.getArticolo().getId());
        }
        for (Iterator<ProdottoCollegato> iterator = prodotti.iterator(); iterator.hasNext();) {
            ProdottoCollegato prodotto = iterator.next();
            if (articoliRimossi.contains(prodotto.getArticolo().getId())) {
                iterator.remove();
            }
        }

        // aggiungo solo i prodotti che non hanno un articolo già presente
        Map<ArticoloLite, ProdottoCollegato> prodottiModelloAggiuntiMap = new HashMap<>();
        for (ProdottoCollegato prodotto : prodottiModelloAggiunti) {
            prodottiModelloAggiuntiMap.put(prodotto.getArticolo(), prodotto);
        }
        for (ProdottoCollegato prodotto : prodotti) {
            if (prodottiModelloAggiuntiMap.containsKey(prodotto.getArticolo())) {
                prodottiModelloAggiuntiMap.remove(prodotto.getArticolo());
            }
        }
        prodotti.addAll(prodottiModelloAggiuntiMap.values());

        return prodotti;
    }

    @Override
    public List<ArticoloRicerca> ricercaArticoliInstallazione(final ParametriRicercaArticolo parametri) {
        LOGGER.debug("--> Enter ricercaArticoliInstallazione");

        List<ArticoloRicerca> articoliResult = new ArrayList<>();
        if (parametri.getIdInstallazione() == null) {
            return articoliResult;
        }

        List<ProdottoCollegato> prodottiInstallazione = caricaProdottiCollegatiByInstallazione(
                parametri.getIdInstallazione());
        Set<ArticoloRicerca> articoliRimossi = new TreeSet<>();

        SetUniqueList articoliUnique = SetUniqueList.decorate(articoliResult);
        for (ProdottoCollegato prodotto : prodottiInstallazione) {
            ArticoloRicerca articoloRicerca = new ArticoloRicerca(prodotto.getArticolo());
            if (prodotto.getTipo() == TipoProdottoCollegato.RIMOSSO) {
                articoliRimossi.add(articoloRicerca);
            } else {
                articoliUnique.add(articoloRicerca);
            }
        }

        articoliResult.removeAll(articoliRimossi);
        CollectionUtils.filter(articoliResult, new Predicate<ArticoloRicerca>() {

            @Override
            public boolean evaluate(ArticoloRicerca art) {
                if (!StringUtils.isBlank(parametri.getCodice())) {
                    return StringUtils.startsWithIgnoreCase(art.getCodice(),
                            StringUtils.replace(parametri.getCodice(), "%", ""));
                }
                if (!StringUtils.isBlank(parametri.getDescrizione())) {
                    return StringUtils.startsWithIgnoreCase(art.getDescrizione(),
                            StringUtils.replace(parametri.getDescrizione(), "%", ""));
                }
                return true;
            }
        });

        return articoliResult;
    }
}
