package it.eurotn.panjea.magazzino.manager.documento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
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
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.manager.interfaces.EntitaManager;
import it.eurotn.panjea.conai.domain.ConaiArticolo;
import it.eurotn.panjea.conai.domain.RigaConaiArticolo;
import it.eurotn.panjea.lotti.exception.RimanenzaLottiNonValidaException;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.IRigaArticoloDocumento;
import it.eurotn.panjea.magazzino.domain.ProvenienzaPrezzoArticolo;
import it.eurotn.panjea.magazzino.domain.RaggruppamentoArticoli;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.RigaArticoloComponente;
import it.eurotn.panjea.magazzino.domain.RigaArticoloDistinta;
import it.eurotn.panjea.magazzino.domain.RigaArticoloLite;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.RigaNota;
import it.eurotn.panjea.magazzino.domain.RigaRaggruppamentoArticoli;
import it.eurotn.panjea.magazzino.domain.RigaTestata;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino.ETipologiaCodiceIvaAlternativo;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzinoLite;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.ProvenienzaPrezzo;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.TipoMovimento;
import it.eurotn.panjea.magazzino.domain.intento.RigaAddebitoDichiarazioneIntentoArticolo;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.ParametriCalcoloPrezzi;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.PoliticaPrezzo;
import it.eurotn.panjea.magazzino.domain.omaggio.RigaOmaggioArticolo;
import it.eurotn.panjea.magazzino.domain.omaggio.TipoOmaggio;
import it.eurotn.panjea.magazzino.domain.trasporto.RigaSpeseTrasportoArticolo;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.RigaMagazzinoDAO;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.RigaMagazzinoManager;
import it.eurotn.panjea.magazzino.manager.listinoprezzi.interfaces.ListinoPrezziManager;
import it.eurotn.panjea.magazzino.manager.moduloprezzo.interfaces.PrezzoArticoloCalculator;
import it.eurotn.panjea.magazzino.manager.omaggio.exception.CodiceIvaPerTipoOmaggioAssenteException;
import it.eurotn.panjea.magazzino.manager.rigadocumento.interfaces.RigaDocumentoManager;
import it.eurotn.panjea.magazzino.rulesvalidation.AbstractRigaArticoloRulesValidation;
import it.eurotn.panjea.magazzino.rulesvalidation.RigheRulesChecker;
import it.eurotn.panjea.magazzino.service.exception.QtaLottiMaggioreException;
import it.eurotn.panjea.magazzino.service.exception.RigheLottiNonValideException;
import it.eurotn.panjea.magazzino.util.ParametriCreazioneRigaArticolo;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRegoleValidazioneRighe;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

@Stateless(name = "Panjea.RigaMagazzinoManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RigaMagazzinoManager")
public class RigaMagazzinoManagerBean implements RigaMagazzinoManager {

    private static Logger logger = Logger.getLogger(RigaMagazzinoManagerBean.class);

    @Resource
    private SessionContext context;

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private ListinoPrezziManager listinoPrezziManager;

    @EJB(beanName = "RigaMagazzinoDAOBean")
    private RigaMagazzinoDAO rigaMagazzinoDAO;

    @EJB
    private RigaDocumentoManager rigaDocumentoManager;

    @EJB(beanName = "RigaArticoloDAO")
    private RigaMagazzinoDAO rigaArticoloDAO;

    @EJB(beanName = "RigaMagazzinoDistintaDAO")
    private RigaMagazzinoDAO rigaMagazzinoDistintaDAO;

    @EJB(beanName = "RigaTestataDAO")
    private RigaMagazzinoDAO rigaTestataDAO;

    @EJB(beanName = "RigaConaiArticoloDAO")
    private RigaMagazzinoDAO rigaConaiArticoloDAO;

    @EJB(beanName = "RigaOmaggioArticoloDAO")
    private RigaMagazzinoDAO rigaOmaggioArticoloDAO;

    @EJB(beanName = "RigaSpeseTrasportoArticoloDAO")
    private RigaMagazzinoDAO rigaSpeseTrasportoArticoloDAO;

    @EJB(beanName = "RigaAddebitoDichiarazioneIntentoArticoloDAO")
    private RigaMagazzinoDAO rigaAddebitoDichiarazioneIntentoArticoloDAO;

    @EJB
    private PrezzoArticoloCalculator prezzoArticoloCalculator;

    @EJB
    private EntitaManager entitaManager;

    @Override
    public boolean aggiornaPrezzoRiga(RigaArticoloLite riga) {
        RigaArticolo rigaArticolo = new RigaArticolo();
        try {
            // NB!!!!: Caricare l'area magazzino prima della riga altrimenti la riga mi mette il
            // proxy dell'area
            // magazzino in sessione e questo mi crea problemi nei metodi con interfaccia
            // xxy(AreaMagazzino
            // areaMagazzino).
            AreaMagazzino areaMagazzino = panjeaDAO.load(AreaMagazzino.class, riga.getAreaMagazzino().getId());
            rigaArticolo = panjeaDAO.load(RigaArticolo.class, riga.getId());

            // init delle righe conai per evitare una lazy init exception
            if (rigaArticolo.getRigheConaiComponente() != null) {
                rigaArticolo.getRigheConaiComponente().size();
            }

            rigaArticolo.setAreaMagazzino(areaMagazzino);
            if (rigaArticolo.getPrezzoUnitario().equals(riga.getPrezzoUnitario())) {
                return false;
            }
            rigaArticolo.setPrezzoUnitario(riga.getPrezzoUnitario());
            getDao(rigaArticolo).salvaRigaMagazzino(rigaArticolo);
            return true;
        } catch (RimanenzaLottiNonValidaException e) {
            // Rilancio una UnsupportedOperationException perchè variando solo
            // il prezzo della riga i lotti non cambiano
            throw new UnsupportedOperationException(e);
        } catch (RigheLottiNonValideException e) {
            // Rilancio una UnsupportedOperationException perchè variando solo
            // il prezzo della riga i lotti non cambiano
            throw new UnsupportedOperationException(e);
        } catch (QtaLottiMaggioreException e) {
            // Rilancio una UnsupportedOperationException perchè variando solo
            // il prezzo della riga i lotti non cambiano
            throw new UnsupportedOperationException(e);
        } catch (ObjectNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void aggiornaScontoCommerciale(AreaMagazzino areaMagazzino, BigDecimal importoSconto) {

        List<RigaArticolo> righe = getDao().caricaRigheArticolo(areaMagazzino);

        for (RigaArticolo rigaArticolo : righe) {

            // Solo una riga articolo può avere gli sconti quindi salto tutte quelle che non lo sono
            // o che hanno righe
            // collegate.
            if (rigaArticolo.getRigaMagazzinoCollegata() != null) {
                continue;
            }

            rigaArticolo.aggiornaScontoCommerciale(importoSconto);
            try {
                panjeaDAO.saveWithoutFlush(rigaArticolo);
            } catch (Exception e) {
                logger.error("--> errore durante il salvataggio della riga articolo", e);
                throw new RuntimeException("errore durante il salvataggio della riga articolo", e);
            }
        }

        panjeaDAO.getEntityManager().flush();
    }

    @Override
    public RigaArticoloComponente aggiungiRigaComponente(Integer idArticolo, double qta, RigaArticolo rigaDistinta) {
        ParametriCreazioneRigaArticolo parametriCreazioneArticolo = new ParametriCreazioneRigaArticolo();
        parametriCreazioneArticolo.setIdArticolo(idArticolo);
        Importo importo = rigaDistinta.getPrezzoUnitario().clone();
        importo.setImportoInValuta(BigDecimal.ZERO);
        importo.setImportoInValutaAzienda(BigDecimal.ZERO);
        parametriCreazioneArticolo.setImporto(importo);
        parametriCreazioneArticolo
                .setTipoMovimento(rigaDistinta.getAreaMagazzino().getTipoAreaMagazzino().getTipoMovimento());
        RigaArticoloComponente riga = (RigaArticoloComponente) rigaDocumentoManager
                .creaRigaArticoloDocumento(new RigaArticoloComponente(), parametriCreazioneArticolo);
        riga.setAreaMagazzino(rigaDistinta.getAreaMagazzino());
        riga.setQta(qta);
        riga.setQtaMagazzino(qta);
        riga.setRigaDistintaCollegata(rigaDistinta);
        riga.setRigaPadre(null);
        riga.setFormulaComponente("1");
        try {
            riga = panjeaDAO.save(riga);
        } catch (DAOException e) {
            logger.error("-->errore nel'aggiungere una riga Componente ad una distinta", e);
            throw new RuntimeException(e);
        }
        return riga;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RigaArticoloLite> applicaRegoleValidazione(
            ParametriRegoleValidazioneRighe parametriRegoleValidazioneRighe) {
        logger.debug("--> Enter validaRigheMagazzino");

        List<AreaMagazzinoLite> aree = parametriRegoleValidazioneRighe.getAreeMagazzino();
        List<RigaArticoloLite> listaRigheNonValide = new ArrayList<RigaArticoloLite>();

        // se non ho delle aree evito qls caricamento o controllo
        if (aree != null && aree.size() > 0) {
            // carico le righe articolo di tutte le aree
            String hqlString = "select ral from RigaArticoloLite ral inner join fetch ral.areaMagazzino area where area in (:paramAreeMagazzino) and ral.tipoRiga='A' order by area";

            Query query = panjeaDAO.prepareQuery(hqlString);
            query.setParameter("paramAreeMagazzino", aree);

            List<RigaArticoloLite> listaRigheCaricate;
            try {
                listaRigheCaricate = panjeaDAO.getResultList(query);
            } catch (Exception e) {
                logger.error("--> Errore durante il caricamento delle righe magazzino.", e);
                throw new RuntimeException("Errore durante il caricamento delle righe magazzino.", e);
            }

            // aggiungo le regole di validazione
            RigheRulesChecker righeRulesChecker = new RigheRulesChecker();
            for (AbstractRigaArticoloRulesValidation rule : parametriRegoleValidazioneRighe.getRegole()) {
                righeRulesChecker.addRules(rule);
            }

            // applico le regole
            for (RigaArticoloLite rigaArticoloLite : listaRigheCaricate) {

                if (!righeRulesChecker.check(rigaArticoloLite)) {
                    listaRigheNonValide.add(rigaArticoloLite);
                }
            }
            if (logger.isDebugEnabled()) {
                logger.debug("--> righe non valide " + listaRigheNonValide.size());
            }
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("--> non ci sono righe da controllare");
            }
        }
        logger.debug("--> Exit validaRigheMagazzino");
        return listaRigheNonValide;
    }

    @Override
    public PoliticaPrezzo calcolaPoliticaPrezzo(RigaArticolo rigaArticolo, CodicePagamento codicePagamento) {
        AreaMagazzino areaMagazzino = rigaArticolo.getAreaMagazzino();
        Integer idListino = null;
        Integer idListinoAlternativo = null;
        Integer idSedeEntita = null;
        Integer idTipoMezzo = null;

        BigDecimal percentualeScontoCommerciale = null;
        Integer idAgente = null;
        ProvenienzaPrezzo provenienzaPrezzo = null;

        if (areaMagazzino.getListino() != null) {
            idListino = areaMagazzino.getListino().getId();
        }
        if (areaMagazzino.getListinoAlternativo() != null) {
            idListinoAlternativo = areaMagazzino.getListinoAlternativo().getId();
        }
        if (areaMagazzino.getDocumento().getSedeEntita() != null) {
            idSedeEntita = areaMagazzino.getDocumento().getSedeEntita().getId();
        }
        if (rigaArticolo.getAgente() != null) {
            idAgente = rigaArticolo.getAgente().getId();
        }
        if (codicePagamento != null) {
            percentualeScontoCommerciale = codicePagamento.getPercentualeScontoCommerciale();
        }
        if (areaMagazzino.getTipoAreaMagazzino() != null) {
            provenienzaPrezzo = areaMagazzino.getTipoAreaMagazzino().getProvenienzaPrezzo();
        }
        if (areaMagazzino.getMezzoTrasporto() != null) {
            idTipoMezzo = areaMagazzino.getMezzoTrasporto().getTipoMezzoTrasporto().getId();
        }

        Integer idZonaGeografica = null;
        idZonaGeografica = areaMagazzino.getIdZonaGeografica();

        ArticoloLite articolo = rigaArticolo.getArticolo();
        ParametriCalcoloPrezzi parametriCalcoloPrezzi = new ParametriCalcoloPrezzi(articolo.getId(),
                areaMagazzino.getDocumento().getDataDocumento(), idListino, idListinoAlternativo, null, idSedeEntita,
                null, null, provenienzaPrezzo, idTipoMezzo, idZonaGeografica, articolo.getProvenienzaPrezzoArticolo(),
                areaMagazzino.getDocumento().getTotale().getCodiceValuta(), idAgente, percentualeScontoCommerciale);
        PoliticaPrezzo politicaPrezzo = prezzoArticoloCalculator.calcola(parametriCalcoloPrezzi);
        return politicaPrezzo;
    }

    @Override
    public PoliticaPrezzo calcolaPrezzoArticolo(final ParametriCalcoloPrezzi parametriCalcoloPrezzi) {
        return prezzoArticoloCalculator.calcola(parametriCalcoloPrezzi);
    }

    @Override
    public CodiceIva caricaCodiceIvaPerSostituzione(IRigaArticoloDocumento rigaArticolo)
            throws CodiceIvaPerTipoOmaggioAssenteException {
        CodiceIva codiceIva = null;
        TipoOmaggio tipoOmaggio = rigaArticolo.getTipoOmaggio();
        if (tipoOmaggio == null) {
            tipoOmaggio = TipoOmaggio.NESSUNO;
        }
        if (tipoOmaggio.isSostituzioneIva()) {
            String hql = " select o.codiceIva from Omaggio o where o.tipoOmaggio=:paramTipoOmaggio and o.codiceAzienda=:paramCodiceAzienda and o.codiceIva is not null  ";
            Query query = panjeaDAO.prepareQuery(hql);
            query.setParameter("paramCodiceAzienda", getAzienda());
            query.setParameter("paramTipoOmaggio", tipoOmaggio);
            try {
                codiceIva = (CodiceIva) panjeaDAO.getSingleResult(query);
            } catch (DAOException e) {
                logger.error("Codice iva per tipoOmaggio mancante", e);
                throw new CodiceIvaPerTipoOmaggioAssenteException(tipoOmaggio);
            }
        } else {
            String hql = " select a.codiceIva from Articolo a where a.id=:paramIdArticolo ";
            Query query = panjeaDAO.prepareQuery(hql);
            query.setParameter("paramIdArticolo", rigaArticolo.getArticolo().getId());
            try {
                codiceIva = (CodiceIva) panjeaDAO.getSingleResult(query);
            } catch (DAOException e) {
                logger.error("CodiceIva non impostato per l'articolo", e);
            }
        }
        return codiceIva;
    }

    @Override
    public void collegaTestata(Set<Integer> righeMagazzinoDaCambiare) {
        logger.debug("--> Enter collegaTestata");
        // Ordino le righe per il campo ordinamento
        Set<RigaMagazzino> righeOrdinate = new TreeSet<RigaMagazzino>(new Comparator<RigaMagazzino>() {

            @Override
            public int compare(RigaMagazzino riga1, RigaMagazzino riga2) {
                return Double.compare(riga1.getOrdinamento(), riga2.getOrdinamento());
            }
        });
        for (Integer idRigaMagazzino : righeMagazzinoDaCambiare) {
            righeOrdinate.add(panjeaDAO.loadLazy(RigaMagazzino.class, idRigaMagazzino));
        }

        // Seleziono la testata o la riga precedente
        StringBuilder sb = new StringBuilder("select rm from RigaMagazzino rm ");
        sb.append("where rm.areaMagazzino=:areaMagazzino and rm.ordinamento<=:ordinamento ");
        sb.append("order by ordinamento desc ");
        Query query = panjeaDAO.prepareQuery(sb.toString());
        query.setMaxResults(2);

        for (RigaMagazzino rigaMagazzino : righeOrdinate) {
            // Devo recuperare due righe ordine. Quella da spostare e
            // la precedente per collegarla alla sua riga testata
            try {
                query.setParameter("areaMagazzino", rigaMagazzino.getAreaMagazzino());
                query.setParameter("ordinamento", rigaMagazzino.getOrdinamento());
                @SuppressWarnings("unchecked")
                List<RigaMagazzino> righe = query.getResultList();
                // Recupero la riga testata alla quale devo collegare la riga
                RigaTestata rigaTestata = null;
                int livello = 0;
                if (righe.size() == 2) {
                    if (righe.get(1) instanceof RigaTestata) {
                        rigaTestata = (RigaTestata) righe.get(1);
                        livello = rigaTestata.getLivello();
                        livello++;
                    } else if (righe.get(1).getRigaTestataMagazzinoCollegata() != null) {
                        rigaTestata = righe.get(1).getRigaTestataMagazzinoCollegata();
                        livello = rigaTestata.getLivello();
                        livello++;
                    }
                }
                rigaMagazzino.setRigaTestataMagazzinoCollegata(rigaTestata);
                rigaMagazzino.setLivello(livello);
                panjeaDAO.save(rigaMagazzino);
            } catch (DAOException e) {
                throw new RuntimeException(e);
            }
        }
        logger.debug("--> Exit collegaTestata");
    }

    @Override
    public boolean creaRigaNoteAutomatica(AreaMagazzino areaMagazzino, String note) {

        RigaNota rigaNota = new RigaNota();
        rigaNota.setAreaMagazzino(areaMagazzino);
        rigaNota.setLivello(0);
        rigaNota.setNota(note);
        rigaNota.setRigaAutomatica(true);
        rigaNota.setOrdinamento(Calendar.getInstance().getTimeInMillis() * 100);
        rigaNota.setNoteSuDestinazione(areaMagazzino.getTipoAreaMagazzino().isNoteSuDestinazione());

        RigaMagazzino rigaMagazzino;
        try {
            rigaMagazzino = getDao(rigaNota).salvaRigaMagazzino(rigaNota);
        } catch (RimanenzaLottiNonValidaException e) {
            logger.error("--> Il salvataggio della riga note ha generato una RimanenzaLottiNonValidaException", e);
            throw new IllegalStateException(
                    "Il salvataggio della riga note ha generato una RimanenzaLottiNonValidaException", e);
        } catch (RigheLottiNonValideException e) {
            logger.error("--> Il salvataggio della riga note ha generato una RigheLottiNonValideException", e);
            throw new IllegalStateException(
                    "Il salvataggio della riga note ha generato una RigheLottiNonValideException", e);
        } catch (QtaLottiMaggioreException e) {
            logger.error("--> Il salvataggio della riga note ha generato una RigheLottiNonValideException", e);
            throw new IllegalStateException(
                    "Il salvataggio della riga note ha generato una RigheLottiNonValideException", e);
        }

        return rigaMagazzino != null;
    }

    /**
     * @return codice azienda loggata
     */
    private String getAzienda() {
        return ((JecPrincipal) context.getCallerPrincipal()).getCodiceAzienda();
    }

    @Override
    public RigaMagazzinoDAO getDao() {
        return rigaMagazzinoDAO;
    }

    @SuppressWarnings("unchecked")
    @Override
    public RigaMagazzinoDAO getDao(ParametriCreazioneRigaArticolo parametriCreazioneRigaArticolo) {
        RigaMagazzinoDAO dao = rigaMagazzinoDAO;

        Articolo articolo;
        // Carico l'articolo
        try {
            articolo = panjeaDAO.load(Articolo.class, parametriCreazioneRigaArticolo.getIdArticolo());
        } catch (ObjectNotFoundException e) {
            logger.error("--> articolo non trovato: idArticolo " + parametriCreazioneRigaArticolo.getIdArticolo(), e);
            throw new RuntimeException(e);
        }

        // in base all'articolo ed ai parametri seleziono il dao.
        if (parametriCreazioneRigaArticolo.isGestioneArticoloDistinta() && articolo.isDistinta()) {
            dao = rigaMagazzinoDistintaDAO;
        } else if (articolo.getProvenienzaPrezzoArticolo() != null
                && articolo.getProvenienzaPrezzoArticolo().equals(ProvenienzaPrezzoArticolo.TIPOMEZZOZONAGEOGRAFICA)) {
            dao = rigaSpeseTrasportoArticoloDAO;
        } else {
            Query query = panjeaDAO.prepareNamedQuery("ConaiArticolo.caricaByArticolo");
            query.setParameter("paramIdArticolo", articolo.getId());

            try {
                List<ConaiArticolo> articoliConai = panjeaDAO.getResultList(query);
                if (articoliConai.size() != 0) {
                    dao = rigaConaiArticoloDAO;
                }
            } catch (DAOException e) {
                logger.error("--> errore nel caricare l'articolo conai per id " + articolo.getId());
                throw new RuntimeException("--> errore nel caricare l'articolo conai per id " + articolo.getId(), e);
            }
        }
        return dao;
    }

    @Override
    public RigaMagazzinoDAO getDao(RigaMagazzino rigaMagazzino) {
        RigaMagazzinoDAO dao = rigaMagazzinoDAO;
        if (rigaMagazzino.getClass() == RigaTestata.class) {
            dao = rigaTestataDAO;
        } else if (rigaMagazzino.getClass() == RigaArticolo.class) {
            dao = rigaArticoloDAO;
        } else if (rigaMagazzino.getClass() == RigaArticoloDistinta.class) {
            dao = rigaMagazzinoDistintaDAO;
        } else if (rigaMagazzino.getClass() == RigaConaiArticolo.class) {
            dao = rigaConaiArticoloDAO;
        } else if (rigaMagazzino.getClass() == RigaOmaggioArticolo.class) {
            dao = rigaOmaggioArticoloDAO;
        } else if (rigaMagazzino.getClass() == RigaSpeseTrasportoArticolo.class) {
            dao = rigaSpeseTrasportoArticoloDAO;
        } else if (rigaMagazzino.getClass() == RigaAddebitoDichiarazioneIntentoArticolo.class) {
            dao = rigaAddebitoDichiarazioneIntentoArticoloDAO;
        }
        return dao;
    }

    @Override
    public void inserisciRaggruppamentoArticoli(Integer idAreaMagazzino, ProvenienzaPrezzo provenienzaPrezzo,
            Integer idRaggruppamentoArticoli, Date data, Integer idSedeEntita, Integer idListinoAlternativo,
            Integer idListino, Importo importo, CodiceIva codiceIvaAlternativo, Integer idTipoMezzo,
            Integer idZonaGeografica, boolean noteSuDestinazione, TipoMovimento tipoMovimento, String codiceValuta,
            String codiceLingua, Integer idAgente, ETipologiaCodiceIvaAlternativo tipologiaCodiceIvaAlternativo,
            BigDecimal percentualeScontoCommerciale)
                    throws RimanenzaLottiNonValidaException, RigheLottiNonValideException, QtaLottiMaggioreException {
        // Carico il riferimento all'area magazzino
        AreaMagazzino areaMagazzino = panjeaDAO.loadLazy(AreaMagazzino.class, idAreaMagazzino);

        // Carico il raggruppamento articoli
        RaggruppamentoArticoli raggruppamentoArticoli;
        try {
            raggruppamentoArticoli = panjeaDAO.load(RaggruppamentoArticoli.class, idRaggruppamentoArticoli);
        } catch (ObjectNotFoundException e) {
            logger.error("-->errore. Raggruppamento articolìi non trovato. Id :" + idRaggruppamentoArticoli, e);
            throw new RuntimeException(e);
        }

        ParametriCreazioneRigaArticolo parametri = new ParametriCreazioneRigaArticolo();
        parametri.setData(areaMagazzino.getDocumento().getDataDocumento());
        parametri.setIdSedeEntita(idSedeEntita);
        parametri.setIdListinoAlternativo(idListinoAlternativo);
        parametri.setIdListino(idListino);
        parametri.setImporto(areaMagazzino.getDocumento().getTotale());
        parametri.setCodiceIvaAlternativo(areaMagazzino.getCodiceIvaAlternativo());
        parametri.setIdTipoMezzo(idTipoMezzo);
        parametri.setIdZonaGeografica(areaMagazzino.getIdZonaGeografica());
        parametri.setNoteSuDestinazione(areaMagazzino.getTipoAreaMagazzino().isNoteSuDestinazione());
        parametri.setTipoMovimento(areaMagazzino.getTipoAreaMagazzino().getTipoMovimento());
        parametri.setCodiceValuta(areaMagazzino.getDocumento().getTotale().getCodiceValuta());
        parametri.setCodiceLingua(codiceLingua);
        parametri.setTipologiaCodiceIvaAlternativo(ETipologiaCodiceIvaAlternativo.NESSUNO);
        parametri.setIdAgente(null);
        parametri.setPercentualeScontoCommerciale(percentualeScontoCommerciale);
        parametri.setProvenienzaPrezzo(provenienzaPrezzo);
        parametri.setStrategiaTotalizzazioneDocumento(
                areaMagazzino.getTipoAreaMagazzino().getStrategiaTotalizzazioneDocumento());

        // Per ogni riga del raggruppamento crea la riga articolo, la associo all'area magazzino e
        // la salvo
        for (RigaRaggruppamentoArticoli righeRaggruppamento : raggruppamentoArticoli.getRigheRaggruppamentoArticoli()) {
            parametri.setIdArticolo(righeRaggruppamento.getArticolo().getId());
            parametri.setProvenienzaPrezzoArticolo(righeRaggruppamento.getArticolo().getProvenienzaPrezzoArticolo());
            RigaArticolo rigaArticolo = (RigaArticolo) getDao(parametri).creaRigaArticolo(parametri);
            // se ho un id agente lo setto sulla riga
            if (idAgente != null) {
                AgenteLite agenteLite = new AgenteLite();
                agenteLite.setId(idAgente);
                try {
                    agenteLite = (AgenteLite) entitaManager.caricaEntitaLite(agenteLite);
                } catch (Exception e) {
                    logger.error("--> errore durante il caricamento dell'agente.", e);
                    throw new RuntimeException("errore durante il caricamento dell'agente.", e);
                }
                rigaArticolo.setAgente(agenteLite);
            }
            // Setto la qta. Ricalcolo il prezzo perchè il prezzo potrebbe dipendere dalla qta.
            rigaArticolo.setQta(righeRaggruppamento.getQta());
            rigaArticolo.setQtaMagazzino(righeRaggruppamento.getQta());
            rigaArticolo.applicaPoliticaPrezzo();
            rigaArticolo.setAreaMagazzino(areaMagazzino);
            RigaMagazzino result = null;
            try {
                result = getDao(rigaArticolo).salvaRigaMagazzino(rigaArticolo);
            } catch (RigheLottiNonValideException e) {
                context.setRollbackOnly();
                throw e;
            } catch (QtaLottiMaggioreException e) {
                context.setRollbackOnly();
                throw e;
            }
            // riassegno l'area magazzino perchè potrei avere cambiato lo stato dell'area.
            areaMagazzino = result.getAreaMagazzino();
        }
    }

    @Override
    public RigaArticolo ricalcolaPrezziRigaArticolo(RigaArticolo rigaArticolo, CodicePagamento codicePagamento) {
        PoliticaPrezzo politicaPrezzo = calcolaPoliticaPrezzo(rigaArticolo, codicePagamento);
        rigaArticolo.setPoliticaPrezzo(politicaPrezzo);
        rigaArticolo.applicaPoliticaPrezzo();

        try {
            rigaArticoloDAO.salvaRigaMagazzinoNoCheck(rigaArticolo);
        } catch (Exception e) {
            logger.error("--> errore durante il salvataggio della riga articolo", e);
            throw new RuntimeException("errore durante il salvataggio della riga articolo", e);
        }
        return rigaArticolo;
    }

}
