package it.eurotn.panjea.magazzino.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.dao.exception.VincoloException;
import it.eurotn.panjea.anagrafica.domain.FaseLavorazione;
import it.eurotn.panjea.anagrafica.domain.FaseLavorazioneArticolo;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.AspettoEsteriore;
import it.eurotn.panjea.magazzino.domain.Categoria;
import it.eurotn.panjea.magazzino.domain.CategoriaCommercialeArticolo;
import it.eurotn.panjea.magazzino.domain.CausaleTrasporto;
import it.eurotn.panjea.magazzino.domain.TemplateSpedizioneMovimenti;
import it.eurotn.panjea.magazzino.domain.TipoAttributo;
import it.eurotn.panjea.magazzino.domain.TipoPorto;
import it.eurotn.panjea.magazzino.domain.TipoVariante;
import it.eurotn.panjea.magazzino.domain.TrasportoCura;
import it.eurotn.panjea.magazzino.domain.descrizionilingua.DescrizioneLinguaTipoAttributo;
import it.eurotn.panjea.magazzino.domain.rendicontazione.EntitaTipoEsportazione;
import it.eurotn.panjea.magazzino.domain.rendicontazione.TipoEsportazione;
import it.eurotn.panjea.magazzino.manager.interfaces.MagazzinoAnagraficaManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;
import it.eurotn.util.PanjeaEJBUtil;

/**
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.MagazzinoAnagraficaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.MagazzinoAnagraficaManager")
public class MagazzinoAnagraficaManagerBean implements MagazzinoAnagraficaManager {

    private static final Logger LOGGER = Logger.getLogger(MagazzinoAnagraficaManagerBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @Resource
    private SessionContext sessionContext;

    @Override
    public void aggiungiCategoriaMerceologicaACategoriaCommerciale(Categoria categoriaMerceologica,
            CategoriaCommercialeArticolo categoriaCommercialeArticolo) {
        // Eseguo il tutto con un update
        StringBuilder sb = new StringBuilder("update a.categoriaCommercialeArticolo_id=")
                .append(categoriaCommercialeArticolo.getId()).append(" where a.categoria_id=")
                .append(categoriaMerceologica.getId());
        Query query = panjeaDAO.getEntityManager().createNativeQuery(sb.toString());
        try {
            panjeaDAO.executeQuery(query);
        } catch (DAOException e) {
            LOGGER.error("--> Errore durante l'associazione della categoria merceologica ad una commerciale  ", e);
            throw new GenericException("Errore durante l'associazione della categoria merceologica ad una commerciale",
                    e);
        }
    }

    @Override
    public void cancellaAspettoEsteriore(AspettoEsteriore aspettoEsteriore) {
        LOGGER.debug("--> Enter cancellaAspettoEsteriore");

        try {
            panjeaDAO.delete(aspettoEsteriore);
        } catch (Exception e) {
            LOGGER.error("--> Errore durante la cancellazione dell'aspetto esteriore " + aspettoEsteriore.getId(), e);
            throw new GenericException(
                    "Errore durante la cancellazione dell'aspetto esteriore " + aspettoEsteriore.getId(), e);
        }

        LOGGER.debug("--> Exit cancellaAspettoEsteriore");
    }

    @Override
    public void cancellaCategoriaCommercialeArticolo(CategoriaCommercialeArticolo categoriaCommercialeArticolo) {
        try {
            panjeaDAO.delete(categoriaCommercialeArticolo);
        } catch (DAOException e) {
            LOGGER.error("-->errore nel cancellare la categoria commerciale articolo " + categoriaCommercialeArticolo,
                    e);
            throw new GenericException(e);
        }
    }

    @Override
    public void cancellaCausaleTrasporto(CausaleTrasporto causaleTrasporto) {
        LOGGER.debug("--> Enter cancellaCausaleTrasporto");

        try {
            panjeaDAO.delete(causaleTrasporto);
        } catch (Exception e) {
            LOGGER.error("--> Errore durante la cancellazione della causale trasporto.", e);
            throw new GenericException("Errore durante la cancellazione della causale trasporto.", e);
        }

        LOGGER.debug("--> Exit cancellaCausaleTrasporto");
    }

    @Override
    public void cancellaEntitaTipoEsportazione(EntitaTipoEsportazione entitaTipoEsportazione) {
        LOGGER.debug("--> Enter cancellaEntitaTipoEsportazione");

        try {
            panjeaDAO.delete(entitaTipoEsportazione);
        } catch (Exception e) {
            LOGGER.error("--> errore durante la cancellazione dell'entita tipo esportazione.", e);
            throw new GenericException("errore durante la cancellazione dell'entita tipo esportazione.", e);
        }

        LOGGER.debug("--> Exit cancellaEntitaTipoEsportazione");
    }

    @Override
    public void cancellaFaseLavorazione(FaseLavorazione faseLavorazione) {
        LOGGER.debug("--> Enter cancellaFaseLavorazione");
        try {
            panjeaDAO.delete(faseLavorazione);
        } catch (Exception e) {
            LOGGER.error("--> Errore durante la cancellazione della FaseLavorazione", e);
            throw new GenericException("Errore durante la cancellazione della FaseLavorazione", e);
        }
        LOGGER.debug("--> Exit cancellaFaseLavorazione");
    }

    @Override
    public void cancellaTipoAttributo(TipoAttributo tipoAttributo) {
        LOGGER.debug("--> Enter cancellaTipoAttributo");
        try {
            panjeaDAO.delete(tipoAttributo);
        } catch (VincoloException e) {
            LOGGER.error("--> errore di vincolo nella cancellazione di " + tipoAttributo, e);
            throw new GenericException(e);
        } catch (Exception e) {
            LOGGER.error("--> errore DAO nella cancellazione di " + tipoAttributo, e);
            throw new GenericException(e);
        }
        LOGGER.debug("--> Exit cancellaTipoAttributo");
    }

    @Override
    public void cancellaTipoEsportazione(TipoEsportazione tipoEsportazione) {
        LOGGER.debug("--> Enter cancellaTipoEsportazione");

        try {
            panjeaDAO.delete(tipoEsportazione);
        } catch (Exception e) {
            LOGGER.error("--> errore durante la cancellazione del tipo esportazione " + tipoEsportazione.getId(), e);
            throw new GenericException(
                    "errore durante la cancellazione del tipo esportazione " + tipoEsportazione.getId(), e);
        }

        LOGGER.debug("--> Exit cancellaTipoEsportazione");
    }

    @Override
    public void cancellaTipoPorto(TipoPorto tipoPorto) {
        LOGGER.debug("--> Enter cancellaTipoPorto");

        try {
            panjeaDAO.delete(tipoPorto);
        } catch (Exception e) {
            LOGGER.error("--> Errore durante la cancellazione del tipo porto.", e);
            throw new GenericException("Errore durante la cancellazione del tipo porto.", e);
        }

        LOGGER.debug("--> Exit cancellaTipoPorto");
    }

    @Override
    public void cancellaTrasportoCura(TrasportoCura trasportoCura) {
        LOGGER.debug("--> Enter cancellaTrasportoCura");

        try {
            panjeaDAO.delete(trasportoCura);
        } catch (Exception e) {
            LOGGER.error("--> Errore durante la cancellazione del trasporto cura.", e);
            throw new GenericException("Errore durante la cancellazione del trasporto cura.", e);
        }

        LOGGER.debug("--> Exit cancellaTrasportoCura");
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<AspettoEsteriore> caricaAspettiEsteriori(String descrizione) {
        LOGGER.debug("--> Enter caricaAspettiEsteriori");
        List<AspettoEsteriore> list = new ArrayList<AspettoEsteriore>();

        StringBuilder sb = new StringBuilder("select ae from AspettoEsteriore ae where ae.codiceAzienda=");
        sb.append(PanjeaEJBUtil.addQuote(getAzienda()));
        if (descrizione != null) {
            sb.append(" and descrizione like ").append(PanjeaEJBUtil.addQuote(descrizione));
        }
        sb.append(" order by descrizione");
        Query query = panjeaDAO.prepareQuery(sb.toString());
        try {
            list = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            LOGGER.error("--> Errore durante il caricamento degli aspetti esteriori", e);
            throw new GenericException("Errore durante il caricamento degli aspetti esteriori", e);
        }
        LOGGER.debug("--> Exit caricaAspettiEsteriori");
        return list;
    }

    @Override
    public CategoriaCommercialeArticolo caricaCategoriaCommercialeArticolo(
            CategoriaCommercialeArticolo categoriaCommercialeArticolo) {
        CategoriaCommercialeArticolo result = null;
        try {
            Query query = panjeaDAO
                    .prepareQuery("select c from CategoriaCommercialeArticolo c where c.id=:idCategoriaCommerciale");
            query.setParameter("idCategoriaCommerciale", categoriaCommercialeArticolo.getId());

            CategoriaCommercialeArticolo cat = (CategoriaCommercialeArticolo) query.getSingleResult();
            cat.setArticoli(new TreeSet<ArticoloLite>());
            cat.setArticoli2(new TreeSet<ArticoloLite>());

            // per prestazioni carico gli articoli con 2 query ad hoc
            List<String> alias = new ArrayList<>();
            alias.add("id");
            alias.add("codice");
            alias.add("version");
            alias.add("descrizione");
            alias.add("idCategoria");
            alias.add("codiceCategoria");
            alias.add("descrizioneCategoria");

            // categoria 1
            StringBuilder sb = new StringBuilder();
            sb.append("select art.id as id, ");
            sb.append("art.codice as codice, ");
            sb.append("art.version as version, ");
            sb.append("art.descrizioneLinguaAziendale as descrizione, ");
            sb.append("cat.id as idCategoria, ");
            sb.append("cat.codice as codiceCategoria, ");
            sb.append("cat.descrizioneLinguaAziendale as descrizioneCategoria ");
            sb.append("from maga_articoli art inner join maga_categorie cat on cat.id = art.categoria_id ");
            sb.append("where art.categoriaCommercialeArticolo_id = ");
            sb.append(categoriaCommercialeArticolo.getId());

            Query queryArticoli = panjeaDAO.prepareSQLQuery(sb.toString(), ArticoloLite.class, alias);
            @SuppressWarnings("unchecked")
            List<ArticoloLite> articoli1 = queryArticoli.getResultList();
            cat.getArticoli().addAll(articoli1);

            // cateogira 2
            sb = new StringBuilder();
            sb.append("select art.id as id, ");
            sb.append("art.codice as codice, ");
            sb.append("art.version as version, ");
            sb.append("art.descrizioneLinguaAziendale as descrizione, ");
            sb.append("cat.id as idCategoria, ");
            sb.append("cat.codice as codiceCategoria, ");
            sb.append("cat.descrizioneLinguaAziendale as descrizioneCategoria ");
            sb.append("from maga_articoli art inner join maga_categorie cat on cat.id = art.categoria_id ");
            sb.append("where art.categoriaCommercialeArticolo2_id = ");
            sb.append(categoriaCommercialeArticolo.getId());
            queryArticoli = panjeaDAO.prepareSQLQuery(sb.toString(), ArticoloLite.class, alias);
            @SuppressWarnings("unchecked")
            List<ArticoloLite> articoli2 = queryArticoli.getResultList();
            cat.getArticoli2().addAll(articoli2);

            result = cat;
        } catch (Exception e) {
            LOGGER.error("-->categoria commerciale articolo non trovata " + categoriaCommercialeArticolo, e);
            throw new GenericException(e);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<CategoriaCommercialeArticolo> caricaCategorieCommercialeArticolo(String fieldSearch,
            String valueSearch) {
        StringBuilder sb = new StringBuilder("select c from CategoriaCommercialeArticolo c ");
        if (valueSearch != null) {
            sb.append(" where ").append(fieldSearch).append(" like ").append(PanjeaEJBUtil.addQuote(valueSearch));
        }
        sb.append(" order by ");
        sb.append(fieldSearch);
        Query query = panjeaDAO.prepareQuery(sb.toString());
        List<CategoriaCommercialeArticolo> result = null;
        try {
            result = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            LOGGER.error("-->errore nel caricare le categorie commerciali degli articoli", e);
            throw new GenericException(e);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<CausaleTrasporto> caricaCausaliTraporto(String descrizione) {
        LOGGER.debug("--> Enter caricaCausaliTraporto");
        List<CausaleTrasporto> list = new ArrayList<CausaleTrasporto>();

        StringBuilder sb = new StringBuilder(
                "select ct from CausaleTrasporto ct where ct.codiceAzienda=:paramCodiceAzienda ");
        if (descrizione != null) {
            sb.append(" and descrizione like ").append(PanjeaEJBUtil.addQuote(descrizione));
        }
        sb.append(" order by descrizione");
        Query query = panjeaDAO.prepareQuery(sb.toString());

        query.setParameter("paramCodiceAzienda", getAzienda());

        try {
            list = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            LOGGER.error("--> Errore durante il caricamento delle causali di trasporto", e);
            throw new GenericException("Errore durante il caricamento delle causali di trasporto", e);
        }
        LOGGER.debug("--> Exit caricaCausaliTraporto");
        return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<EntitaTipoEsportazione> caricaEntitaTipoEsportazione() {
        LOGGER.debug("--> Enter caricaEntitaTipoEsportazione");
        List<EntitaTipoEsportazione> entitaTipiEsportazione = new ArrayList<EntitaTipoEsportazione>();
        Query query = panjeaDAO.prepareNamedQuery("EntitaTipoEsportazione.caricaAll");
        query.setParameter("codiceAzienda", getAzienda());

        try {
            entitaTipiEsportazione = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            LOGGER.error("--> errore durante il caricamento delle entita tipi esportazione.", e);
            throw new GenericException("errore durante il caricamento delle entita tipi esportazione.", e);
        }

        LOGGER.debug("--> Exit caricaEntitaTipoEsportazione");
        return entitaTipiEsportazione;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<EntitaLite> caricaEntitaTipoEsportazione(TipoEsportazione tipoEsportazione) {
        LOGGER.debug("--> Enter caricaEntitaTipoEsportazione");

        List<EntitaLite> entita = new ArrayList<EntitaLite>();

        String queryString = "select ete.entita from EntitaTipoEsportazione ete where ete.tipoEsportazione = :tipoEsportazione";

        Query query = panjeaDAO.prepareQuery(queryString);
        query.setParameter("tipoEsportazione", tipoEsportazione);
        try {
            entita = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            LOGGER.error("--> errore durante il caricamento delle entita collegate al tipo esportazione "
                    + tipoEsportazione.getId(), e);
            throw new GenericException("errore durante il caricamento delle entita collegate al tipo esportazione "
                    + tipoEsportazione.getId(), e);
        }

        LOGGER.debug("--> Exit caricaEntitaTipoEsportazione");
        return entita;
    }

    @Override
    public FaseLavorazione caricaFaseLavorazione(FaseLavorazione fase) {
        LOGGER.debug("--> Enter caricaFaseLavorazione");
        FaseLavorazione result = null;
        try {
            result = panjeaDAO.load(FaseLavorazione.class, fase.getId());
            for (FaseLavorazioneArticolo faseLavorazioneArticolo : result.getFasiArticolo()) {
                faseLavorazioneArticolo.avvaloraArticoloConfigurazione();
            }
        } catch (ObjectNotFoundException e) {
            LOGGER.error("-->errore nel caricare la fase di lavorazione con id " + fase.getId(), e);
            throw new GenericException("-->errore nel caricare la fase di lavorazione con id " + fase.getId(), e);
        }
        LOGGER.debug("--> Exit caricaFaseLavorazione");
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<FaseLavorazione> caricaFasiLavorazione(String codice) {
        LOGGER.debug("--> Enter caricaFasiLavorazione");
        List<FaseLavorazione> list = new ArrayList<FaseLavorazione>();
        StringBuilder sb = new StringBuilder();
        sb.append("select fl from FaseLavorazione fl ");
        sb.append("where fl.codiceAzienda=:paramCodiceAzienda ");
        if (codice != null) {
            sb.append("and fl.codice like :paramCodice ");
            sb.append("or fl.descrizione like :paramCodice ");
        }
        sb.append("order by fl.codice");

        Query query = panjeaDAO.prepareQuery(sb.toString());
        query.setParameter("paramCodiceAzienda", getAzienda());
        if (codice != null) {
            query.setParameter("paramCodice", codice);
        }
        try {
            list = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            LOGGER.error("--> Errore durante il caricamento delle fasi di lavorazione", e);
            throw new GenericException("Errore durante il caricamento delle fasi di lavorazione", e);
        }
        LOGGER.debug("--> Exit caricaFasiLavorazione");
        return list;
    }

    @Override
    public TemplateSpedizioneMovimenti caricaTemplateSpedizioneMovimenti() {
        LOGGER.debug("--> Enter caricaTemplateSpedizioneMovimenti");

        TemplateSpedizioneMovimenti template = null;

        // esiste un solo template quindi lo carico e se ancora non è stato
        // creato ne salvo uno di default
        Query query = panjeaDAO.prepareQuery("select tsm from TemplateSpedizioneMovimenti tsm");
        try {
            template = (TemplateSpedizioneMovimenti) panjeaDAO.getSingleResult(query);
        } catch (ObjectNotFoundException e) {
            TemplateSpedizioneMovimenti templateDefault = new TemplateSpedizioneMovimenti();
            templateDefault.setOggetto("Spedizione $DescTipoDoc$ via mail");
            templateDefault.setTesto(
                    "<html><div>Gentile Cliente,</div><div><br></div><div>In allegato Vi trasmettiamo Ns. $DescTipoDoc$.</div><div><br></div><div>L'invio della fattura via e-mail SOSTITUISCE INTEGRALMENTE quello effettuato in modo tradizionale a mezzo servizio postale, ai sensi dell'Art.21 del DPR 633/1972 , succ. modifiche e da risoluzione ministero delle finanze Prot. 450217 del 30/07/1990.</div><div><br></div><div>La fattura dovr&#224; essere considerata emessa e spedita in originale, dovr&#224; essere stampata e conservata come da prassi a norma di legge.</div><div><br></div><div>Distinti saluti</div><html>");

            template = salvaTemplateSpedizioneMovimenti(templateDefault);
        } catch (Exception e) {
            LOGGER.error("--> errore durante il caricamento del template di spedizione movimenti", e);
            throw new GenericException("errore durante il caricamento del template di spedizione movimenti", e);
        }

        LOGGER.debug("--> Exit caricaTemplateSpedizioneMovimenti");

        return template;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<TipoAttributo> caricaTipiAttributo() {
        LOGGER.debug("--> Enter caricaTipiAttributo");

        List<TipoAttributo> list = new ArrayList<TipoAttributo>();
        Query query = panjeaDAO.prepareNamedQuery("TipoAttributo.caricaAll");
        query.setParameter("paramCodiceAzienda", getAzienda());

        try {
            list = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            LOGGER.error("--> Errore durante il caricamento dei tipi attributo", e);
            throw new GenericException("Errore durante il caricamento dei tipi attributo", e);
        }
        LOGGER.debug("--> Exit caricaTipiAttributo");
        return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<TipoEsportazione> caricaTipiEsportazione(String nome) {
        LOGGER.debug("--> Enter caricaTipiEsportazione");
        List<TipoEsportazione> tipiEsportazione = new ArrayList<TipoEsportazione>();
        Query query = panjeaDAO.prepareNamedQuery("TipoEsportazione.caricaByNome");
        query.setParameter("codiceAzienda", getAzienda());
        query.setParameter("nome", StringUtils.defaultIfBlank(nome, "%"));

        try {
            tipiEsportazione = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            LOGGER.error("--> errore durante il caricamento dei tipi esportazione.", e);
            throw new GenericException("errore durante il caricamento dei tipi esportazione.", e);
        }

        LOGGER.debug("--> Exit caricaTipiEsportazione");
        return tipiEsportazione;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<TipoPorto> caricaTipiPorto(String descrizione) {
        LOGGER.debug("--> Enter caricaTipiPorto");
        Query query = panjeaDAO.prepareNamedQuery("TipoPorto.caricaByDescrizione");
        query.setParameter("paramCodiceAzienda", getAzienda());
        query.setParameter("descrizione", StringUtils.defaultIfBlank(descrizione, "%"));
        List<TipoPorto> tipi = null;
        try {
            tipi = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            LOGGER.error("--> errore DAO in caricatipiPorto", e);
            throw new GenericException(e);
        }
        LOGGER.debug("--> Exit caricaTipiPorto");
        return tipi;
    }

    @Override
    public TipoAttributo caricaTipoAttributo(TipoAttributo tipoAttributo) {
        LOGGER.debug("--> Enter caricaTipoAttributo");
        TipoAttributo tipoAttributoResult = null;
        try {
            tipoAttributoResult = panjeaDAO.load(TipoAttributo.class, tipoAttributo.getId());
            // inizializzo le collection Lazy
            tipoAttributoResult.getNomiLingua().size();
        } catch (ObjectNotFoundException e) {
            LOGGER.error("--> Attributo non trovato. " + tipoAttributo, e);
            throw new GenericException(e);
        }
        LOGGER.debug("--> Exit caricaTipoAttributo");
        return tipoAttributoResult;
    }

    @Override
    public TipoAttributo caricaTipoAttributoByCodice(String codice) {
        Query queryTipoAttributo = panjeaDAO.prepareNamedQuery("TipoAttributo.caricaByCodice");
        queryTipoAttributo.setParameter("paramCodice", codice);
        queryTipoAttributo.setParameter("paramCodiceAzienda", getAzienda());
        try {
            return (TipoAttributo) panjeaDAO.getSingleResult(queryTipoAttributo);
        } catch (DAOException e) {
            LOGGER.error("--> errore nel recuperare il tipo attributo per codice", e);
            throw new GenericException(e);
        }
    }

    @Override
    public TipoEsportazione caricaTipoEsportazione(Integer idTipoEsportazione, boolean loadLazy) {
        LOGGER.debug("--> Enter caricaTipoEsportazione");

        TipoEsportazione tipoEsportazione = null;
        try {
            tipoEsportazione = panjeaDAO.load(TipoEsportazione.class, idTipoEsportazione);
            if (!loadLazy && tipoEsportazione.getTipiAreeMagazzino() != null) {
                tipoEsportazione.getTipiAreeMagazzino().size();
            }
        } catch (Exception e) {
            LOGGER.error("--> errore durante il caricamento del tipo esportazione.", e);
            throw new GenericException("errore durante il caricamento del tipo esportazione.", e);
        }
        LOGGER.debug("--> Exit caricaTipoEsportazione");
        return tipoEsportazione;
    }

    @Override
    public TipoVariante caricaTipoVariante(TipoVariante tipoVariante) {
        LOGGER.debug("--> Enter caricaTipoVariante");
        TipoVariante tipoVarianteCaricata = null;
        try {
            tipoVarianteCaricata = panjeaDAO.load(TipoVariante.class, tipoVariante.getId());
        } catch (ObjectNotFoundException e) {
            LOGGER.error("--> Errore, nessun tipo variante con id = " + tipoVariante.getId(), e);
            throw new GenericException("Errore, nessun tipo variante con id = " + tipoVariante.getId(), e);
        }

        LOGGER.debug("--> Exit caricaTipoVariante");
        return tipoVarianteCaricata;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<TrasportoCura> caricaTrasportiCura(String descrizione) {
        LOGGER.debug("--> Enter caricaTrasportiCura");

        Query query = panjeaDAO.prepareNamedQuery("TrasportoCura.caricaByDescrizione");
        query.setParameter("paramCodiceAzienda", getAzienda());
        query.setParameter("paramDescrizione", StringUtils.defaultIfBlank(descrizione, "%"));
        List<TrasportoCura> list = null;
        try {
            list = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            LOGGER.error("--> errore DAO in caricatipiPorto", e);
            throw new GenericException(e);
        }
        LOGGER.debug("--> Exit caricaTrasportiCura");
        return list;
    }

    @Override
    public TrasportoCura caricaTrasportoCuraByDescrizione(String descrizione) {
        LOGGER.debug("--> Enter caricaTrasportoCuraByDescrizione");

        TrasportoCura trasportoCura = null;

        Query query = panjeaDAO.prepareNamedQuery("TrasportoCura.caricaByDescrizione");
        query.setParameter("paramCodiceAzienda", getAzienda());
        query.setParameter("paramDescrizione", descrizione);

        List<TrasportoCura> trasporti = caricaTrasportiCura(descrizione);
        if (!trasporti.isEmpty()) {
            trasportoCura = trasporti.get(0);
        }
        LOGGER.debug("--> Exit caricaTrasportoCuraByDescrizione");
        return trasportoCura;
    }

    /**
     * @return codice dell'azienda loggata
     */
    private String getAzienda() {
        return ((JecPrincipal) sessionContext.getCallerPrincipal()).getCodiceAzienda();
    }

    @Override
    public AspettoEsteriore salvaAspettoEsteriore(AspettoEsteriore aspettoEsteriore) {
        LOGGER.debug("--> Enter salvaAspettoEsteriore");

        if (aspettoEsteriore.isNew()) {
            aspettoEsteriore.setCodiceAzienda(getAzienda());
        }

        AspettoEsteriore aspettoEsterioreSalvato = null;
        try {
            aspettoEsterioreSalvato = panjeaDAO.save(aspettoEsteriore);
        } catch (Exception e) {
            LOGGER.error("--> Errore durante il salvataggio dell'aspetto esteriore", e);
            throw new GenericException("Errore durante il salvataggio dell'aspetto esteriore", e);
        }
        LOGGER.debug("--> Exit salvaAspettoEsteriore");
        return aspettoEsterioreSalvato;
    }

    @Override
    public CategoriaCommercialeArticolo salvaCategoriaCommercialeArticolo(
            CategoriaCommercialeArticolo categoriaCommercialeArticolo) {
        try {
            categoriaCommercialeArticolo = panjeaDAO.save(categoriaCommercialeArticolo);
        } catch (DAOException e) {
            LOGGER.error(
                    "-->errore nel salvare la categoria commerciale per l'articolo " + categoriaCommercialeArticolo, e);
            throw new GenericException(e);
        }
        return categoriaCommercialeArticolo;
    }

    @Override
    public CausaleTrasporto salvaCausaleTraporto(CausaleTrasporto causaleTrasporto) {
        LOGGER.debug("--> Enter salvaCausaleTraporto");

        if (causaleTrasporto.isNew()) {
            causaleTrasporto.setCodiceAzienda(getAzienda());
        }

        CausaleTrasporto causaleTrasportoSalvata = null;
        try {
            causaleTrasportoSalvata = panjeaDAO.save(causaleTrasporto);
        } catch (Exception e) {
            LOGGER.error("--> Errore durante il salvataggio della causale trasporto.", e);
            throw new GenericException("Errore durante il salvataggio della causale trasporto.", e);
        }
        LOGGER.debug("--> Exit salvaCausaleTraporto");
        return causaleTrasportoSalvata;
    }

    @Override
    public EntitaTipoEsportazione salvaEntitaTipoEsportazione(EntitaTipoEsportazione entitaTipoEsportazione) {
        LOGGER.debug("--> Enter salvaEntitaTipoEsportazione");

        if (entitaTipoEsportazione.isNew()) {
            entitaTipoEsportazione.setCodiceAzienda(getAzienda());
        }

        EntitaTipoEsportazione entitaTipoEsportazioneSalvato = null;
        try {
            entitaTipoEsportazioneSalvato = panjeaDAO.save(entitaTipoEsportazione);
        } catch (Exception e) {
            LOGGER.error("--> errore durante il salvataggio dell'entita tipo esportazione.", e);
            throw new GenericException("errore durante il salvataggio dell'entita tipo esportazione.", e);
        }

        LOGGER.debug("--> Exit salvaEntitaTipoEsportazione");
        return entitaTipoEsportazioneSalvato;
    }

    @Override
    public FaseLavorazione salvaFaseLavorazione(FaseLavorazione faseLavorazione) {
        LOGGER.debug("--> Enter salvaFaseLavorazione");
        if (faseLavorazione.isNew()) {
            faseLavorazione.setCodiceAzienda(getAzienda());
        }
        FaseLavorazione tipoPortoSalvato = null;
        try {
            tipoPortoSalvato = panjeaDAO.save(faseLavorazione);
        } catch (Exception e) {
            LOGGER.error("--> Errore durante il salvataggio della FaseLavorazione", e);
            throw new GenericException("Errore durante il salvataggio della FaseLavorazione", e);
        }
        LOGGER.debug("--> Exit salvaFaseLavorazione");
        return tipoPortoSalvato;
    }

    @Override
    public FaseLavorazioneArticolo salvaFaseLavorazioneArticolo(FaseLavorazioneArticolo faseLavorazioneArticolo) {
        LOGGER.debug("--> Enter salvaFaseLavorazioneArticolo");
        FaseLavorazioneArticolo faseLavorazioneArticoloSalvata = null;
        try {
            faseLavorazioneArticoloSalvata = panjeaDAO.save(faseLavorazioneArticolo);
        } catch (Exception e) {
            LOGGER.error("--> Errore durante il salvataggio della FaseLavorazioneArticolo", e);
            throw new GenericException("Errore durante il salvataggio della FaseLavorazioneArticolo", e);
        }
        LOGGER.debug("--> Exit salvaFaseLavorazioneArticolo");
        return faseLavorazioneArticoloSalvata;
    }

    @Override
    public TemplateSpedizioneMovimenti salvaTemplateSpedizioneMovimenti(
            TemplateSpedizioneMovimenti templateSpedizioneMovimenti) {
        LOGGER.debug("--> Enter salvaTemplateSpedizioneMovimenti");

        TemplateSpedizioneMovimenti templateSalvato;
        try {
            templateSalvato = panjeaDAO.save(templateSpedizioneMovimenti);
        } catch (Exception e) {
            LOGGER.error("--> errore durante il salvataggio del template di spedizione documenti", e);
            throw new GenericException("errore durante il salvataggio del template di spedizione documenti", e);
        }

        LOGGER.debug("--> Exit salvaTemplateSpedizioneMovimenti");
        return templateSalvato;
    }

    @Override
    public TipoAttributo salvaTipoAttributo(TipoAttributo tipoAttributo) {
        LOGGER.debug("--> Enter salvaTipoAttributo");

        if (tipoAttributo.isNew()) {
            tipoAttributo.setCodiceAzienda(getAzienda());
        }

        // tolgo eventuali descrizioni in lingua vuote
        if (tipoAttributo.getNomiLingua() != null) {
            Map<String, DescrizioneLinguaTipoAttributo> mapDescrizioniClone = new HashMap<String, DescrizioneLinguaTipoAttributo>(
                    tipoAttributo.getNomiLingua());
            for (Entry<String, DescrizioneLinguaTipoAttributo> descrizioneLinguaEntry : mapDescrizioniClone
                    .entrySet()) {
                if (descrizioneLinguaEntry.getValue().getDescrizione() == null
                        || (descrizioneLinguaEntry.getValue().getDescrizione() != null
                                && descrizioneLinguaEntry.getValue().getDescrizione().isEmpty())) {
                    tipoAttributo.getNomiLingua().remove(descrizioneLinguaEntry.getKey());
                }
            }
        }

        TipoAttributo tipoAttributoResult;
        try {
            tipoAttributoResult = panjeaDAO.save(tipoAttributo);
        } catch (DAOException e) {
            LOGGER.error("--> errore nel salvare l'attributo " + tipoAttributo, e);
            throw new GenericException(e);
        }
        LOGGER.debug("--> Exit salvaTipoAttributo");
        return tipoAttributoResult;
    }

    @Override
    public TipoEsportazione salvaTipoEsportazione(TipoEsportazione tipoEsportazione) {
        LOGGER.debug("--> Enter salvaTipoEsportazione");

        TipoEsportazione tipoEsportazioneSalvata = null;

        if (tipoEsportazione.isNew()) {
            tipoEsportazione.setCodiceAzienda(getAzienda());
        }
        try {
            tipoEsportazioneSalvata = panjeaDAO.save(tipoEsportazione);
        } catch (Exception e) {
            LOGGER.error("--> errore durante il salvataggio del tipo esportazione.", e);
            throw new GenericException("errore durante il salvataggio del tipo esportazione.", e);
        }

        LOGGER.debug("--> Exit salvaTipoEsportazione");
        return tipoEsportazioneSalvata;
    }

    @Override
    public TipoPorto salvaTipoPorto(TipoPorto tipoPorto) {
        LOGGER.debug("--> Enter salvaTipoPorto");

        if (tipoPorto.isNew()) {
            tipoPorto.setCodiceAzienda(getAzienda());
        }

        TipoPorto tipoPortoSalvato = null;
        try {
            tipoPortoSalvato = panjeaDAO.save(tipoPorto);
        } catch (Exception e) {
            LOGGER.error("--> Errore durante il salvataggio del tipo porto", e);
            throw new GenericException("Errore durante il salvataggio del tipo porto", e);
        }

        LOGGER.debug("--> Exit salvaTipoPorto");
        return tipoPortoSalvato;
    }

    @Override
    public TrasportoCura salvaTrasportoCura(TrasportoCura trasportoCura) {
        LOGGER.debug("--> Enter salvaTrasportoCura");

        if (trasportoCura.isNew()) {
            trasportoCura.setCodiceAzienda(getAzienda());
        }

        TrasportoCura trasportoCuraSalvato = null;
        try {
            trasportoCuraSalvato = panjeaDAO.save(trasportoCura);
        } catch (Exception e) {
            LOGGER.error("--> Errore durante il salvataggio del trasporto cura.", e);
            throw new GenericException("Errore durante il salvataggio del trasporto cura.", e);
        }

        LOGGER.debug("--> Exit salvaTrasportoCura");
        return trasportoCuraSalvato;
    }

}