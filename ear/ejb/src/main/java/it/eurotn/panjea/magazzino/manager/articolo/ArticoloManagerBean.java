package it.eurotn.panjea.magazzino.manager.articolo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Query;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.ejb.QueryImpl;
import org.hibernate.transform.Transformers;
import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.codice.generator.interfaces.ProtocolloGenerator;
import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.dao.exception.VincoloException;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.anagrafica.manager.depositi.interfaces.DepositiManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.SediAziendaManager;
import it.eurotn.panjea.conai.domain.ConaiComponente;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.ArticoloAlternativo;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.AttributoArticolo;
import it.eurotn.panjea.magazzino.domain.AttributoCategoria;
import it.eurotn.panjea.magazzino.domain.Categoria;
import it.eurotn.panjea.magazzino.domain.CategoriaCommercialeArticolo;
import it.eurotn.panjea.magazzino.domain.Giacenza;
import it.eurotn.panjea.magazzino.domain.MagazzinoSettings;
import it.eurotn.panjea.magazzino.domain.RigaListino;
import it.eurotn.panjea.magazzino.domain.descrizionilingua.DescrizioneLinguaArticolo;
import it.eurotn.panjea.magazzino.domain.descrizionilingua.DescrizioneLinguaArticoloEstesa;
import it.eurotn.panjea.magazzino.exception.ParametriMancantiEANException;
import it.eurotn.panjea.magazzino.manager.articolo.querybuilder.RicercaArticoliQueryBuilder;
import it.eurotn.panjea.magazzino.manager.articolo.querybuilder.RicercaArticoliSearchObjectQueryBuilder;
import it.eurotn.panjea.magazzino.manager.interfaces.CategorieManager;
import it.eurotn.panjea.magazzino.manager.interfaces.GiacenzaManager;
import it.eurotn.panjea.magazzino.manager.interfaces.ListinoManager;
import it.eurotn.panjea.magazzino.manager.interfaces.MagazzinoSettingsManager;
import it.eurotn.panjea.magazzino.manager.interfaces.articolo.ArticoloManager;
import it.eurotn.panjea.magazzino.manager.interfaces.articolo.DistintaBaseCopiaManager;
import it.eurotn.panjea.magazzino.service.exception.MagazzinoException;
import it.eurotn.panjea.magazzino.service.exception.RigaListinoListiniCollegatiException;
import it.eurotn.panjea.magazzino.util.ArticoloRicerca;
import it.eurotn.panjea.magazzino.util.ParametriRicercaArticolo;
import it.eurotn.panjea.magazzino.util.RigaListinoDTO;
import it.eurotn.panjea.riepilogo.util.RiepilogoArticoloDTO;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

/**
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.ArticoloManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.ArticoloManager")
public class ArticoloManagerBean implements ArticoloManager {

    public class ArticoloRicercaAttributiComparator implements Comparator<ArticoloRicerca> {

        /**
         * Costruttore.
         */
        public ArticoloRicercaAttributiComparator() {
            super();
        }

        @Override
        public int compare(ArticoloRicerca o1, ArticoloRicerca o2) {
            int result = 0;

            String codiciAttributiString1 = o1.getCodiciAttributi();
            String valoriAttributiString1 = o1.getValoriAttributi();

            String codiciAttributiString2 = o2.getCodiciAttributi();
            String valoriAttributiString2 = o2.getValoriAttributi();

            if (codiciAttributiString1.compareTo(codiciAttributiString2) == 0) {
                valoriAttributiString1 = valoriAttributiString1.replaceAll(",", ".");
                valoriAttributiString2 = valoriAttributiString2.replaceAll(",", ".");

                String[] valori1 = valoriAttributiString1.split("#");
                String[] valori2 = valoriAttributiString2.split("#");

                for (int i = 0; i < valori1.length; i++) {
                    if (NumberUtils.isNumber(valori1[i]) && NumberUtils.isNumber(valori2[i])) {
                        Double double1 = NumberUtils.createDouble(valori1[i]);
                        Double double2 = NumberUtils.createDouble(valori2[i]);

                        result = double1.compareTo(double2);
                        if (result != 0) {
                            break;
                        }
                    }
                }
            }
            return result;
        }
    }

    private static Logger logger = Logger.getLogger(ArticoloManagerBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private DistintaBaseCopiaManager distintaBaseCopiaManager;

    @Resource
    private SessionContext sessionContext;

    @EJB
    private ListinoManager listinoManager;

    @EJB
    private SediAziendaManager sediAziendaManager;

    @IgnoreDependency
    @EJB
    private CategorieManager categorieManager;

    @EJB
    private MagazzinoSettingsManager magazzinoSettingsManager;

    @Resource
    private SessionContext context;

    @EJB
    private ProtocolloGenerator protocolloGenerator;

    @EJB
    private GiacenzaManager giacenzaManager;

    @EJB
    private DepositiManager depositiManager;

    @Override
    public void aggiornaArticoliAlternativiFiltro(int idArticolo, String formula) {
        Query query = panjeaDAO.prepareNamedQuery("Articolo.aggiornaArticoliAlternativiFiltro");
        query.setParameter("articolo", idArticolo);
        if (formula != null && formula.isEmpty()) {
            formula = null;
        }
        query.setParameter("filtro", formula);
        try {
            panjeaDAO.executeQuery(query);
        } catch (DAOException e) {
            logger.error(
                    "-->errore nell'aggiornare la formula per l'articolo " + idArticolo + " con formula " + formula, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public String calcolaEAN() {
        MagazzinoSettings settings = magazzinoSettingsManager.caricaMagazzinoSettings();
        // Recupero il numeratore per l'ean

        String numeratoreBarCode = settings.getNumeratoreBarCode();
        String gs1 = settings.getCodiceGS1();

        if (numeratoreBarCode == null || gs1 == null) {
            ParametriMancantiEANException eanException = new ParametriMancantiEANException();
            throw eanException;
        }

        String codice = protocolloGenerator.nextCodice(numeratoreBarCode, Integer.MAX_VALUE);

        // riempio il gs1 per arrivare a 13 caratteri
        int length = codice.length() + gs1.length();
        int numZeri = 12 - length;
        String zeri = "000000000000".substring(0, numZeri);
        String ean = gs1 + zeri + codice;

        // Calcolo checksum
        char[] eanChar = ean.toCharArray();

        boolean even = false;
        int sum = 0;
        for (char c : eanChar) {
            if (Character.isDigit(c)) {
                Integer digit = Integer.valueOf(c - 48);
                if (even) {
                    digit *= 3;
                }
                sum += digit;
                even = !even;
            }
        }
        sum = (10 - (sum % 10)) % 10;
        return ean + sum;
    }

    @Override
    public void cambiaCategoriaAdArticoli(List<ArticoloRicerca> articoli, Categoria categoriaDestinazione) {
        logger.debug("--> Enter cambiaCategoriaAdArticoli");
        if (articoli.size() == 0) {
            logger.debug("--> Exit cambiaCategoriaAdArticoli");
            return;
        }

        StringBuilder idArticoli = new StringBuilder(" update maga_articoli set categoria_id=");
        idArticoli.append(categoriaDestinazione.getId());
        idArticoli.append(" where id in (");

        for (ArticoloRicerca articoloRicerca : articoli) {
            idArticoli.append(articoloRicerca.getId());
            idArticoli.append(",");
        }
        idArticoli.delete(idArticoli.length() - 1, idArticoli.length());
        idArticoli.append(")");
        SQLQuery query = panjeaDAO.prepareNativeQuery(idArticoli.toString());
        query.executeUpdate();
        // aggiorno gli attributi degli articoli con quelli previsti dalla
        // categoria di destinazione
        categorieManager.sincronizzaAttributiArticoli(categoriaDestinazione,
                categoriaDestinazione.getAttributiCategoria());
    }

    @Override
    public void cambiaCategoriaCommercialeAdArticoli(List<ArticoloRicerca> articoli,
            CategoriaCommercialeArticolo categoriaCommercialeArticolo,
            CategoriaCommercialeArticolo categoriaCommercialeArticolo2) {
        logger.debug("--> Enter cambiaCategoriaCommercialeAdArticoli");
        if (articoli.size() == 0) {
            logger.debug("--> Exit cambiaCategoriaCommercialeAdArticoli");
            return;
        }

        for (ArticoloRicerca articoloRicerca : articoli) {
            Articolo articolo = new Articolo();
            articolo.setId(articoloRicerca.getId());
            articolo.setVersion(articoloRicerca.getVersion());
            try {
                articolo = panjeaDAO.load(Articolo.class, articoloRicerca.getId());
                if (categoriaCommercialeArticolo != null) {
                    articolo.setCategoriaCommercialeArticolo(categoriaCommercialeArticolo);
                }
                if (categoriaCommercialeArticolo2 != null) {
                    articolo.setCategoriaCommercialeArticolo2(categoriaCommercialeArticolo2);
                }
                salvaArticolo(articolo);
            } catch (Exception e) {
                logger.error("--> errore in cambiaCategoriaCommercialeAdArticoli", e);
                throw new RuntimeException(e);
            }

            logger.debug("--> Exit cambiaCategoriaCommercialeAdArticoli");
        }
    }

    @Override
    public void cambiaCodiceIVA(CodiceIva codiceIvaDaSostituire, CodiceIva nuovoCodiceIva) {
        logger.debug("--> Enter cambiaCodiceIVA");

        StringBuilder sb = new StringBuilder();
        sb.append("update Articolo a ");
        sb.append("set a.codiceIva = :nuovoCodiceIVA ");
        sb.append("where a.codiceIva = :vecchioCodiceIVA");

        Query query = panjeaDAO.prepareQuery(sb.toString());
        query.setParameter("nuovoCodiceIVA", nuovoCodiceIva);
        query.setParameter("vecchioCodiceIVA", codiceIvaDaSostituire);

        try {
            panjeaDAO.executeQuery(query);
        } catch (Exception e) {
            logger.error("--> errore durante la sostituzione del codice iva degli articoli", e);
            throw new RuntimeException("errore durante la sostituzione del codice iva degli articoli", e);
        }

        logger.debug("--> Exit cambiaCodiceIVA");
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @Override
    public void cancellaArticolo(Articolo articolo) {
        logger.debug("--> Enter cancellaArticolo");
        try {
            // lo storico del listino non è legato all'articolo quindi lo cancello a mano prima
            // dell'articolo
            StringBuilder sb = new StringBuilder(200);
            sb.append("delete from maga_scaglioni_listini_storico where articolo_id = ");
            sb.append(articolo.getId());
            SQLQuery query = panjeaDAO.prepareNativeQuery(sb.toString());
            query.executeUpdate();

            panjeaDAO.delete(articolo);
        } catch (VincoloException e) {
            logger.info("--> errore nel cancellare l'attributo, vincolo non valido", e);
            throw new RuntimeException(e);
        } catch (DAOException e) {
            logger.error("--> errore nel cancellare l'attributo, vincolo non valido", e);
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit cancellaArticolo");
    }

    @Override
    public void cancellaArticoloAlternativo(ArticoloAlternativo articoloAlternativo) {
        logger.debug("--> Enter cancellaArticoloAlternativo");
        try {
            panjeaDAO.delete(articoloAlternativo);
        } catch (VincoloException e) {
            logger.info("--> errore nel cancellare l'articoloAlternativo, vincolo non valido", e);
            throw new RuntimeException(e);
        } catch (DAOException e) {
            logger.error("--> errore nel cancellare l'articoloAlternativo", e);
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit cancellaArticoloAlternativo");
    }

    @Override
    public List<ArticoloRicerca> caricaArticoli() {
        return ricercaArticoli(new ParametriRicercaArticolo());
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<ArticoloAlternativo> caricaArticoliAlternativi(Articolo articolo) {
        logger.debug("--> Enter caricaArticoliAlternativi");
        Query query = panjeaDAO.prepareNamedQuery("ArticoloAlternativo.caricaByArticolo");
        query.setParameter("articolo", articolo);
        Set<ArticoloAlternativo> result = null;
        try {
            List<ArticoloAlternativo> list = panjeaDAO.getResultList(query);
            result = new HashSet<ArticoloAlternativo>(list);
        } catch (DAOException e) {
            logger.error("-->errore nel caricare gli articoli alternativi", e);
            throw new RuntimeException();
        }
        logger.debug("--> Exit caricaArticoliAlternativi");
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Articolo> caricaArticoliFull() {
        String hqlArticolo = "select a from Articolo a left join fetch a.descrizioniLingua left join fetch a.descrizioniLinguaEstesa inner join fetch a.unitaMisura left join fetch a.unitaMisuraQtaMagazzino left join fetch a.codiceIva";
        Query query = panjeaDAO.prepareQuery(hqlArticolo);
        List<Articolo> articoli = new ArrayList<Articolo>();
        try {
            articoli = panjeaDAO.getResultList(query);
        } catch (DAOException e1) {
            logger.error("--> errore durante il caricamento degli articoli.", e1);
            throw new RuntimeException("errore durante il caricamento degli articoli.", e1);
        }

        return articoli;
    }

    @Override
    public Articolo caricaArticolo(Articolo articolo, boolean initializeLazy) {
        Articolo articoloResult = null;
        try {
            articoloResult = panjeaDAO.load(Articolo.class, articolo.getId());
            if (initializeLazy) {
                Hibernate.initialize(articoloResult.getCategoria());
                articoloResult.getDescrizioniLinguaEstesa().size();
                Hibernate.initialize(articoloResult.getAttributiArticolo());
                articoloResult.getTipiMezzoTrasporto().size();
                Hibernate.initialize(articoloResult.getCodiceIva());
                Hibernate.initialize(articoloResult.getFormulaTrasformazioneQta());
                Hibernate.initialize(articoloResult.getFormulaTrasformazioneQtaMagazzino());
                if (articoloResult.getTipoAreaOrdine() != null) {
                    Hibernate.initialize(articoloResult.getTipoAreaOrdine().getTipoDocumento());
                }
            }
        } catch (ObjectNotFoundException e) {
            logger.error("--> Errore durante il caricamento dell'articolo " + articolo.getId(), e);
            throw new RuntimeException("Errore durante il caricamento dell'articolo " + articolo.getId(), e);
        }
        return articoloResult;
    }

    @Override
    public ArticoloLite caricaArticoloLite(int idArticolo) {
        ArticoloLite articolo = null;
        try {
            articolo = panjeaDAO.load(ArticoloLite.class, idArticolo);
            Hibernate.initialize(articolo.getCodiceIva());
            Hibernate.initialize(articolo.getCategoria().getCodice());
        } catch (ObjectNotFoundException e) {
            logger.error("Errore durante il caricamento dell'articolo lite", e);
            throw new RuntimeException("Errore durante il caricamento dell'articolo lite", e);
        }
        return articolo;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<AttributoArticolo> caricaAttributiArticolo(Articolo articolo) {
        logger.debug("--> Enter caricaAttributiArticolo");

        Query query = panjeaDAO.prepareNamedQuery("AttributoArticolo.caricaByArticolo");
        query.setParameter("paramIdArticolo", articolo.getId());

        List<AttributoArticolo> attributi = new ArrayList<AttributoArticolo>();
        try {
            attributi = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            logger.error("--> errore durante il caricamento degli attributi articolo.", e);
            throw new RuntimeException("errore durante il caricamento degli attributi articolo.", e);
        }

        logger.debug("--> Exit caricaAttributiArticolo");
        return attributi;
    }

    @Override
    public List<AttributoArticolo> caricaAttributiDaInserireInRiga(Integer idArticolo) {
        logger.debug("--> Enter caricaAttributiDaInserireInRiga");

        Articolo articolo = new Articolo();
        articolo.setId(idArticolo);
        articolo = caricaArticolo(articolo, true);

        // Attributi per l'articolo. Nella chiave metto il codice delTipoAttributo
        Map<String, AttributoArticolo> attributiArticolo = new HashMap<String, AttributoArticolo>();
        List<AttributoArticolo> attributiRiga = new ArrayList<AttributoArticolo>();

        // copia degli attributi collegati alla formula di trasformazione
        for (AttributoArticolo attributoArticolo : articolo.getAttributiArticolo()) {
            attributiArticolo.put(attributoArticolo.getTipoAttributo().getCodice(), attributoArticolo);
            attributoArticolo.getTipoAttributo().getNomiLingua().size();
            logger.debug("--> Aggiunto il tipoAttributo " + attributoArticolo.getTipoAttributo().getCodice());
        }

        Set<String> codiceTipiAttributi = new HashSet<String>();
        if (articolo.getFormulaTrasformazioneQta() != null) {
            codiceTipiAttributi.addAll(articolo.getFormulaTrasformazioneQta().getCodiceTipiAttributi(false));
        }

        if (articolo.getFormulaTrasformazioneQtaMagazzino() != null) {
            codiceTipiAttributi.addAll(articolo.getFormulaTrasformazioneQtaMagazzino().getCodiceTipiAttributi(false));
        }

        // aggiungo tutti i tipi attributi degli attributi che hanno il flag
        // inserimentoInRiga
        for (AttributoArticolo attributoArticolo : articolo.getAttributiArticolo()) {
            if (attributoArticolo.getInserimentoInRiga()) {
                codiceTipiAttributi.add(attributoArticolo.getTipoAttributo().getCodice());
            }
        }

        if (codiceTipiAttributi.size() > 0) {
            for (String codiceTipoAttributo : codiceTipiAttributi) {
                AttributoArticolo attributoArticolo = attributiArticolo.get(codiceTipoAttributo);
                // se il tipo attributo non è presente nell'articolo non lo
                // inserisco ( es: formula che dichiara come
                // variabile un tipo attributo che non è presente nell'articolo
                // )
                if (attributoArticolo != null) {
                    attributiRiga.add(attributoArticolo);
                }
            }
        }

        return attributiRiga;
    }

    @Override
    public Set<ConaiComponente> caricaComponentiConai(Integer idArticolo) {
        Articolo articolo = panjeaDAO.loadLazy(Articolo.class, idArticolo);
        articolo.getComponentiConai().size();
        return articolo.getComponentiConai();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RiepilogoArticoloDTO> caricaRiepilogoArticoli() {
        logger.debug("--> Enter caricaRiepilogoArticoli");

        StringBuilder sb = new StringBuilder();
        sb.append(
                "select cat.id as idCategoria,cat.codice as codiceCategoria,cat.descrizioneLinguaAziendale as descrizioneCategoria, ");
        sb.append(
                "			  art.id as idArticolo,art.codice as codiceArticolo,art.descrizioneLinguaAziendale as descrizioneArticolo, ");
        sb.append("			  art.barCode as barCode, ");
        sb.append("			  art.tipoArticolo as tipoArticolo,art.articoloLibero as articoloLibero , ");
        sb.append(
                "			  codIva.id as idCodiceIva,codIva.codice as codiceCodiceIva,codIva.descrizioneInterna as descrizioneCodiceIva, ");
        sb.append("			  art.ivaAlternativa as ivaAlternativa, ");
        sb.append(
                "			  catCont.id as idCategoriaContabileArticolo,catCont.codice as codiceCategoriaContabileArticolo, ");
        sb.append(
                "			  um.id as idUnitaMisura,um.codice as codiceUnitaMisura,um.descrizione as descrizioneUnitaMisura, ");
        sb.append(
                "			  umMaga.id as idUnitaMisuraQtaMagazzino,umMaga.codice as codiceUnitaMisuraQtaMagazzino,umMaga.descrizione as descrizioneUnitaMisuraQtaMagazzino, ");
        sb.append(
                "			  formTrasfQta.id as idFormulaTrasformazioneQta,formTrasfQta.codice as codiceFormulaTrasformazioneQta, ");
        sb.append(
                "			  formTrasfQtaMaga.id as idFormulaTrasformazioneQtaMagazzino,formTrasfQtaMaga.codice as codiceFormulaTrasformazioneQtaMagazzino, ");
        sb.append(
                "			  art.numeroDecimaliQta as numeroDecimaliQta,art.numeroDecimaliPrezzo as numeroDecimaliPrezzo, ");
        sb.append("			  art.provenienzaPrezzoArticolo as provenienzaPrezzoArticolo, ");
        sb.append("			  catCommArt.id as idCategoriaCommerciale, ");
        sb.append("			  catCommArt.codice as codiceCategoriaCommerciale, ");
        sb.append("			  catCommArt2.id as idCategoriaCommerciale2, ");
        sb.append("			  catCommArt2.codice as codiceCategoriaCommerciale2, ");
        sb.append("			  art.codiceInterno as codiceInterno ");
        sb.append(
                "from Articolo art join art.categoria cat left join art.codiceIva codIva left join art.categoriaContabileArticolo catCont ");
        sb.append(
                "									join art.unitaMisura um join art.unitaMisuraQtaMagazzino umMaga left join art.formulaTrasformazioneQta formTrasfQta ");
        sb.append("									left join art.formulaTrasformazioneQtaMagazzino formTrasfQtaMaga ");
        sb.append("									left join art.categoriaCommercialeArticolo catCommArt ");
        sb.append("									left join art.categoriaCommercialeArticolo2 catCommArt2 ");
        sb.append("where art.codiceAzienda = :codiceAzienda and art.abilitato = true");

        org.hibernate.Query query = ((Session) panjeaDAO.getEntityManager().getDelegate()).createQuery(sb.toString());
        query.setResultTransformer(Transformers.aliasToBean(RiepilogoArticoloDTO.class));
        query.setParameter("codiceAzienda", getCodiceAzienda());

        List<RiepilogoArticoloDTO> riepilogo = new ArrayList<RiepilogoArticoloDTO>();
        riepilogo = query.list();

        logger.debug("--> Exit caricaRiepilogoArticoli");
        return riepilogo;
    }

    @Override
    public Articolo cloneArticolo(Articolo articolo, String nuovoCodice, String nuovaDescrizione, boolean copyDistinta,
            boolean copyDistintaConfigurazioni, List<AttributoArticolo> attributi, boolean copyListino,
            boolean azzeraPrezziListino) {
        logger.debug("--> Enter cloneArticolo");
        articolo = caricaArticolo(articolo, true);
        ((Session) panjeaDAO.getEntityManager().getDelegate()).evict(articolo);
        Articolo cloneArticolo = (Articolo) articolo.clone();
        cloneArticolo.setAttributiArticolo(new ArrayList<AttributoArticolo>());
        cloneArticolo.setCodice(nuovoCodice);
        cloneArticolo.setDescrizione(nuovaDescrizione);
        // imposto l'articolo altrimenti l'associazione non è corretta
        for (AttributoArticolo attributoArticolo : attributi) {
            attributoArticolo.setArticolo(cloneArticolo);
        }
        cloneArticolo.setAttributiArticolo(attributi);

        try {
            cloneArticolo = new ArticoloMascheraBuilder().applicaValoriMaschera(cloneArticolo, protocolloGenerator);
            cloneArticolo.setDistinta(articolo.isDistinta() && copyDistinta);
            cloneArticolo = panjeaDAO.save(cloneArticolo);
            if (articolo.isDistinta() && copyDistinta) {
                distintaBaseCopiaManager.copiaDistinta(articolo, cloneArticolo, copyDistintaConfigurazioni);
            }
            if (copyListino) {
                List<RigaListinoDTO> righeListino = listinoManager.caricaRigheListinoByArticolo(articolo.getId());
                for (RigaListinoDTO rigaListinoDTO : righeListino) {
                    RigaListino rigaListino = listinoManager.caricaRigaListino(rigaListinoDTO.getId());
                    ((Session) panjeaDAO.getEntityManager().getDelegate()).evict(rigaListino);
                    ((Session) panjeaDAO.getEntityManager().getDelegate()).evict(rigaListino.getScaglioni());
                    RigaListino nuovaRigaListino = rigaListino.clone(azzeraPrezziListino);
                    nuovaRigaListino.setArticolo(cloneArticolo.getArticoloLite());
                    try {
                        listinoManager.salvaRigaListino(nuovaRigaListino);
                    } catch (RigaListinoListiniCollegatiException e) {
                        logger.error("-->errore nel clonare la riga listino", e);
                        throw new RuntimeException(e);
                    }
                }
            }
        } catch (DAOException e) {
            logger.error("-->errore nel salvare l'articolo ", e);
            throw new RuntimeException(e);
        }

        logger.debug("--> Enter cloneArticolo");
        return cloneArticolo;
    }

    // /**
    // * genera le descrizioni in lingua per l'argomento articolo.
    // *
    // * @param paramArticolo
    // * Articolo
    // * @return Map con chiave il codice della lingua e valore la sua
    // descrizione
    // * @throws GenerazioneCodiceException
    // * Eccezione generica
    // */
    // private Map<String, DescrizioneLinguaArticolo>
    // generaDescrizioniLingua(Articolo paramArticolo)
    // throws GenerazioneCodiceException {
    // logger.debug("--> Enter generaDescrizioniLingua");
    // Map<String, DescrizioneLinguaArticolo> descrizioniLinguaArticolo = new
    // TreeMap<String,
    // DescrizioneLinguaArticolo>();
    // DescrizioneLinguaArticolo descrizioneLinguaArticolo;
    // for (String codiceLingua :
    // paramArticolo.getCategoria().getInformazioniLingua().keySet()) {
    // // generazione descrizione in lingua per ogni occorenza di Lingua
    // // InformazioneLinguaCategoria descrizioneLinguaCategoria =
    // paramArticolo.getCategoria()
    // // .getInformazioniLingua().get(codiceLingua);
    // descrizioneLinguaArticolo = new DescrizioneLinguaArticolo();
    // descrizioneLinguaArticolo.setCodiceLingua(codiceLingua);
    // descrizioneLinguaArticolo.setDescrizione(articoloManagerBuilder.generaValoreMaschera(paramArticolo,
    // paramArticolo.getCategoria().getGenerazioneCodiceArticoloData().getMascheraDescrizioneArticolo(),
    // ETipoAttributoGenerazione.DESCRIZIONE, codiceLingua));
    // //
    // descrizioneLinguaArticolo.setDescrizione(generaDescrizione(paramArticolo,
    // descrizioneLinguaCategoria
    // // .getMascheraDescrizioneArticolo(), codiceLingua));
    // descrizioniLinguaArticolo.put(descrizioneLinguaArticolo.getCodiceLingua(),
    // descrizioneLinguaArticolo);
    // }
    // logger.debug("--> Exit generaDescrizioniLingua");
    // return descrizioniLinguaArticolo;
    // }

    @Override
    public Articolo creaArticolo(Categoria categoria) {
        logger.debug("--> Enter creaArticolo");
        // Ricarico la categoria per associarla alla sessione
        try {
            logger.debug("--> Carico la categoria per l'articolo " + categoria);
            categoria = panjeaDAO.load(Categoria.class, categoria.getId());
            logger.debug(
                    "--> Caricati " + categoria.getAttributiCategoria().size() + " TipoAttributi per la categoria");
        } catch (ObjectNotFoundException e) {
            logger.error("--> errore nel caricare la categoria " + categoria, e);
            throw new RuntimeException(e);
        }

        Articolo articolo = new Articolo();
        articolo.setCategoria(categoria);
        List<AttributoArticolo> attributi = creaAttributiArticolo(articolo);
        articolo.setAttributiArticolo(attributi);

        logger.debug("--> Creato l'articolo con " + articolo.getAttributiArticolo().size() + " attributi");
        logger.debug("--> Exit creaArticolo");
        return articolo;
    }

    /**
     * Crea tutti gli attributi articolo per l'articolo di riferimento.
     *
     * @param articolo
     *            articolo
     * @return la lista di attributi per l'articolo di riferimento
     */
    private List<AttributoArticolo> creaAttributiArticolo(Articolo articolo) {
        List<AttributoArticolo> attributi = new ArrayList<AttributoArticolo>();
        for (AttributoCategoria attributoCategoria : articolo.getCategoria().getAttributiCategoria()) {
            AttributoArticolo attributoArticolo = new AttributoArticolo();
            // attributoArticolo.setArticolo(articolo);
            attributoArticolo.setFormula(attributoCategoria.getFormula());
            attributoArticolo.setInserimentoInRiga(attributoCategoria.getInserimentoInRiga());
            attributoArticolo.setOrdine(attributoCategoria.getOrdine());
            attributoArticolo.setRiga(attributoCategoria.getRiga());
            attributoArticolo.setStampa(attributoCategoria.getStampa());
            attributoArticolo.setTipoAttributo(attributoCategoria.getTipoAttributo());
            attributoArticolo.setValore(attributoCategoria.getValore());
            attributoArticolo.setObbligatorio(attributoCategoria.getObbligatorio());
            attributoArticolo.setRicalcolaInEvasione(attributoCategoria.getRicalcolaInEvasione());
            attributi.add(attributoArticolo);
        }
        return attributi;
    }

    /**
     * @return azienda loggata
     */
    private String getAzienda() {
        return ((JecPrincipal) sessionContext.getCallerPrincipal()).getCodiceAzienda();
    }

    /**
     * @return codiceAzienda
     */
    private String getCodiceAzienda() {
        return ((JecPrincipal) context.getCallerPrincipal()).getCodiceAzienda();
    }

    /**
     * @return utente corrente
     */
    private JecPrincipal getPrincipal() {
        return (JecPrincipal) sessionContext.getCallerPrincipal();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ArticoloRicerca> ricercaArticoli(ParametriRicercaArticolo parametriRicercaArticolo) {
        logger.debug("--> Enter ricercaArticoli ");

        RicercaArticoliQueryBuilder ricercaArticoliQueryBuilder = new RicercaArticoliQueryBuilder(panjeaDAO,
                getPrincipal(), categorieManager);
        Query query = ricercaArticoliQueryBuilder.createQuery(parametriRicercaArticolo);
        List<ArticoloRicerca> articoliRicerca = new ArrayList<ArticoloRicerca>();
        try {
            articoliRicerca = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            logger.error("--> Errore durante la ricerca degli articoli", e);
            throw new RuntimeException("Errore durante la ricerca degli articoli", e);
        }

        // se sono impostati custom filter li applico
        if (parametriRicercaArticolo.getCustomFilter() != null) {
            CustomArticoliFilter customArticoliFilter = (CustomArticoliFilter) sessionContext
                    .lookup(parametriRicercaArticolo.getCustomFilter().getJndiFilterBeanName());

            articoliRicerca = customArticoliFilter.filtra(articoliRicerca,
                    parametriRicercaArticolo.getCustomFilter().getFilterParameterMap());
        }

        if (parametriRicercaArticolo.isCalcolaGiacenza()) {
            Map<Integer, ArticoloRicerca> articoliRicercaMap = new HashMap<>();
            for (ArticoloRicerca articoloRicerca : articoliRicerca) {
                articoliRicercaMap.put(articoloRicerca.getId(), articoloRicerca);
            }

            DepositoLite depositoPrincipale = new DepositoLite();
            depositoPrincipale.setId(parametriRicercaArticolo.getIdDeposito());

            List<Deposito> depositi = depositiManager.caricaDepositi();
            for (Deposito deposito : depositi) {
                if (deposito.getAttivo()) {
                    List<Giacenza> giacenze = giacenzaManager.calcolaGiacenzeFlat(deposito.creaLite(),
                            new ArrayList<Integer>(articoliRicercaMap.keySet()), new Date());

                    // se devo aggiungere la giacenza la carico ed eseguo il
                    // merge di
                    // giacenza con articoliRicerca
                    for (Giacenza articoloGiacenza : giacenze) {
                        ArticoloRicerca articoloRicerca = articoliRicercaMap.get(articoloGiacenza.getIdArticolo());

                        // attenzione, li aggiorno per riferimento in modo da
                        // non
                        // doverli riordinare nel caso in cui non debba
                        // cambiare l'ordinamento scelto nella query
                        if (articoloRicerca != null) {
                            articoloRicerca.setGiacenza(
                                    new Double(ObjectUtils.defaultIfNull(articoloRicerca.getGiacenza(), 0.0))
                                            + articoloGiacenza.getGiacenza());
                        }
                    }
                }
            }
        }

        if (parametriRicercaArticolo.isRicercaAttributiPresente() && parametriRicercaArticolo.isOrdinaPerAttributi()) {
            try {
                Collections.sort(articoliRicerca, new ArticoloRicercaAttributiComparator());
            } catch (Exception e) {
                // se non riesco ad ordinare gli articoli pazienza.
                logger.error("-->errore nell' ordinare gli articoli", e);
            }
        }

        logger.debug("--> Exit ricercaArticoli");
        return articoliRicerca;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ArticoloRicerca> ricercaArticoliSearchObject(ParametriRicercaArticolo parametriRicercaArticolo) {
        logger.debug("--> Enter ricercaArticoli");

        List<ArticoloRicerca> articoli = new ArrayList<ArticoloRicerca>();

        RicercaArticoliSearchObjectQueryBuilder queryBuilder = new RicercaArticoliSearchObjectQueryBuilder();
        String queryString = queryBuilder.createQuery(parametriRicercaArticolo, getAzienda());

        Query query = panjeaDAO.getEntityManager().createNativeQuery(queryString);
        ((org.hibernate.ejb.HibernateQuery) query).getHibernateQuery()
                .setResultTransformer(Transformers.aliasToBean(ArticoloRicerca.class));
        SQLQuery sqlQuery = ((SQLQuery) ((QueryImpl) query).getHibernateQuery());
        sqlQuery.addScalar("id");
        sqlQuery.addScalar("version");
        if (parametriRicercaArticolo.getIdEntita() != null
                || parametriRicercaArticolo.isRicercaCodiceArticoloEntitaPresente()
                || parametriRicercaArticolo.isAssortimentoArticoli()) {
            sqlQuery.addScalar("codiceEntita");
        }
        sqlQuery.addScalar("codice");
        sqlQuery.addScalar("codiceInterno");
        sqlQuery.addScalar("descrizione");
        sqlQuery.addScalar("barCode");
        sqlQuery.addScalar("numeroDecimaliQta");
        Properties params = new Properties();
        params.put("enumClass", "it.eurotn.panjea.magazzino.domain.ProvenienzaPrezzoArticolo");
        params.put("type", "12");
        sqlQuery.addScalar("provenienzaPrezzoArticolo", Hibernate.custom(org.hibernate.type.EnumType.class, params));
        try {
            articoli = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            logger.error("--> errore durante la ricerca degli articoli.", e);
            throw new RuntimeException("errore durante la ricerca degli articoli.", e);
        }

        // Calcolo giacenza per gli articoli

        logger.debug("--> Exit ricercaArticoli");
        return articoli;
    }

    @Override
    public ArticoloLite ricercaArticoloByCodice(String codiceArticolo) {
        logger.debug("--> Enter ricercaArticoloByCodice");
        Query query = panjeaDAO.prepareNamedQuery("ArticoloLite.caricaByCodice");
        query.setParameter("paramCodiceAzienda", getAzienda());
        query.setParameter("paramCodice", codiceArticolo);
        ArticoloLite articolo = null;
        try {
            articolo = (ArticoloLite) panjeaDAO.getSingleResult(query);
        } catch (ObjectNotFoundException e) {
            return null;
        } catch (DAOException e) {
            logger.error("--> errore DAOException in ricercaArticoloByCodice", e);
            throw new MagazzinoException(e);
        }
        logger.debug("--> Exit ricercaArticoloByCodice");
        return articolo;
    }

    @Override
    public Articolo salvaArticolo(Articolo articolo) {
        logger.debug("--> Enter salvaArticolo");

        if (articolo.isNew()) {
            articolo.setCodiceAzienda(getAzienda());
            // creo gli attributi dell'articolo se è stato appena creato
            // creaAttributiArticolo(articoloResult);
        }

        articolo = new ArticoloMascheraBuilder().applicaValoriMaschera(articolo, protocolloGenerator);

        // articolo.setDescrizioniLingua(generaDescrizioniLingua(articolo));

        // tolgo eventuali descrizioni in lingua vuote
        Map<String, DescrizioneLinguaArticolo> mapDescrizioniClone = new HashMap<String, DescrizioneLinguaArticolo>(
                articolo.getDescrizioniLingua());
        for (Entry<String, DescrizioneLinguaArticolo> descrizioneLinguaEntry : mapDescrizioniClone.entrySet()) {
            if (descrizioneLinguaEntry.getValue().getDescrizione() == null
                    || (descrizioneLinguaEntry.getValue().getDescrizione() != null
                            && descrizioneLinguaEntry.getValue().getDescrizione().isEmpty())) {
                articolo.getDescrizioniLingua().remove(descrizioneLinguaEntry.getKey());
            }
        }

        // tolgo eventuali descrizioni in lingua estesa vuote
        Map<String, DescrizioneLinguaArticoloEstesa> mapDescrizioniEsteseClone = new HashMap<String, DescrizioneLinguaArticoloEstesa>(
                articolo.getDescrizioniLinguaEstesa());
        for (Entry<String, DescrizioneLinguaArticoloEstesa> descrizioneLinguaEntry : mapDescrizioniEsteseClone
                .entrySet()) {
            if (descrizioneLinguaEntry.getValue().getDescrizione() == null
                    || (descrizioneLinguaEntry.getValue().getDescrizione() != null
                            && descrizioneLinguaEntry.getValue().getDescrizione().isEmpty())) {
                articolo.getDescrizioniLinguaEstesa().remove(descrizioneLinguaEntry.getKey());
            }
        }

        for (AttributoArticolo attributoArticolo : articolo.getAttributiArticolo()) {
            attributoArticolo.setArticolo(articolo);
        }

        Articolo articoloResult = null;
        try {
            articoloResult = panjeaDAO.save(articolo);
        } catch (Exception e) {
            logger.error("--> Errore durante il salvataggio dell'articolo", e);
            throw new RuntimeException("Errore durante il salvataggio dell'articolo", e);
        }
        logger.debug("--> Exit salvaArticolo");
        articoloResult = caricaArticolo(articoloResult, true);

        return articoloResult;
    }

    @Override
    public ArticoloAlternativo salvaArticoloAlternativo(ArticoloAlternativo articoloAlternativo) {
        logger.debug("--> Enter salvaArticoloAlternativo");
        ArticoloAlternativo articoloAlternativoSave = null;
        try {
            articoloAlternativoSave = panjeaDAO.save(articoloAlternativo);
        } catch (DAOException e) {
            logger.error("-->errore nel salvare un articolo alternativo ", e);
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit salvaArticoloAlternativo");
        return articoloAlternativoSave;
    }

    @Override
    public AttributoArticolo salvaAttributoArticolo(AttributoArticolo attributoArticolo) {
        logger.debug("--> Enter salvaAttributoArticolo");

        AttributoArticolo attributoArticoloSalvato = null;
        try {
            attributoArticoloSalvato = panjeaDAO.save(attributoArticolo);
        } catch (Exception e) {
            logger.error("--> errore durante il salvataggio dell'attributo articolo", e);
            throw new RuntimeException("errore durante il salvataggio dell'attributo articolo", e);
        }
        logger.debug("--> Exit salvaAttributoArticolo");
        return attributoArticoloSalvato;
    }

}
