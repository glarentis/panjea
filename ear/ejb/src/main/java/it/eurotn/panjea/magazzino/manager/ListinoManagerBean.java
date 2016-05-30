/**
 *
 */
package it.eurotn.panjea.magazzino.manager;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.domain.lite.AziendaLite;
import it.eurotn.panjea.anagrafica.manager.interfaces.AziendeManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.SediEntitaManager;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.magazzino.domain.Listino.ETipoListino;
import it.eurotn.panjea.magazzino.domain.MagazzinoSettings;
import it.eurotn.panjea.magazzino.domain.RigaListino;
import it.eurotn.panjea.magazzino.domain.ScaglioneListino;
import it.eurotn.panjea.magazzino.domain.ScaglioneListinoStorico;
import it.eurotn.panjea.magazzino.domain.VersioneListino;
import it.eurotn.panjea.magazzino.manager.interfaces.ListinoManager;
import it.eurotn.panjea.magazzino.manager.interfaces.MagazzinoSettingsManager;
import it.eurotn.panjea.magazzino.manager.interfaces.MagazzinoValorizzazioneManager;
import it.eurotn.panjea.magazzino.service.exception.RigaListinoListiniCollegatiException;
import it.eurotn.panjea.magazzino.service.exception.RigheListinoListiniCollegatiException;
import it.eurotn.panjea.magazzino.util.ParametriRicercaValorizzazione;
import it.eurotn.panjea.magazzino.util.ParametriRicercaValorizzazione.EModalitaValorizzazione;
import it.eurotn.panjea.magazzino.util.RigaListinoCalcolo;
import it.eurotn.panjea.magazzino.util.RigaListinoDTO;
import it.eurotn.panjea.magazzino.util.ValorizzazioneArticolo;
import it.eurotn.panjea.riepilogo.util.RiepilogoSedeEntitaDTO;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;
import it.eurotn.util.PanjeaEJBUtil;

/**
 * @author fattazzo
 */
@Stateless(name = "Panjea.ListinoManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.ListinoManager")
public class ListinoManagerBean implements ListinoManager {

    private enum TipoAggiornamentoPercentualeRicarico {
        CALCOLA, APPLICA
    }

    private static Logger logger = Logger.getLogger(ListinoManagerBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @Resource
    private SessionContext sessionContext;

    @EJB
    private AziendeManager aziendeManager;

    @EJB
    private SediEntitaManager sediEntitaManager;

    @EJB
    private MagazzinoSettingsManager magazzinoSettingsManager;

    @EJB
    private MagazzinoValorizzazioneManager magazzinoValorizzazioneManager;

    /**
     * Viene applicata la percentuale di ricarico a:<br>
     * <ul>
     * <li>tutte le righe listino che hanno lo stesso articolo delal riga di riferimento</li>
     * <li>tutte le righe che sono in listini con listino base uguale a quello della riga di
     * riferimento</li>
     * <li>tutte le righe che si trovano su una versione corrente alla data attuale</li>
     * </ul>
     * <br>
     * La percentuale viene calcolata solo se la riga di riferimento si trova sulla versione
     * corrente del listino.
     *
     * @param rigaListino
     *            riga di riferimento
     */
    private void aggiornaPercentualeRicaricoArticolo(RigaListino rigaListino,
            TipoAggiornamentoPercentualeRicarico tipoAggiornamento) {

        // verifico che la versione listino della rig sia quella attuale
        VersioneListino versioneListinoCorrente = caricaVersioneListinoByData(
                rigaListino.getVersioneListino().getListino(), Calendar.getInstance().getTime());

        // versione listino corrente può essere null nel caso in cui non ci sia un listino con data
        // <= today
        if (versioneListinoCorrente != null
                && !versioneListinoCorrente.getId().equals(rigaListino.getVersioneListino().getId())) {
            return;
        }

        // controllo che il listino della riga sia il listino base di altri
        List<Listino> listiniCollegati = caricaListiniByListinoBase(rigaListino.getVersioneListino().getListino());
        if (listiniCollegati.isEmpty()) {
            return;
        }

        // per ogni listino carico la versione corrente e modifico il prezzo delle righe in base
        // alla percentuale
        // impostata.
        for (Listino listino : listiniCollegati) {

            VersioneListino versioneListino = caricaVersioneListinoByData(listino, Calendar.getInstance().getTime());
            if (versioneListino != null) {
                List<RigaListino> righeListino = caricaRigheListinoByArticolo(versioneListino,
                        rigaListino.getArticolo().getId());

                for (RigaListino rigaRicarico : righeListino) {

                    BigDecimal prezzoPrec = rigaRicarico.getPrezzo();

                    switch (tipoAggiornamento) {
                    case CALCOLA:
                        rigaRicarico.setImportoBase(rigaListino.getPrezzo());
                        rigaRicarico.calcolaPercentualeRicarico(rigaRicarico.getPrezzo());
                        break;
                    case APPLICA:
                        rigaRicarico.applicaPercentualeRicarico(rigaListino.getPrezzo());

                        // se è cambiato il prezzo applicando la percentuale di ricarico, aggiungo
                        // la variazione nello
                        // storico
                        if (rigaRicarico.getVersioneListino().getListino().getTipoListino() == ETipoListino.NORMALE
                                && prezzoPrec.compareTo(rigaRicarico.getPrezzo()) != 0) {
                            ScaglioneListinoStorico storico = new ScaglioneListinoStorico();
                            storico.setPrezzo(rigaRicarico.getPrezzo());
                            storico.setNumeroDecimaliPrezzo(rigaRicarico.getNumeroDecimaliPrezzo());
                            storico.setArticolo(rigaRicarico.getArticolo());
                            storico.setListino(rigaRicarico.getVersioneListino().getListino());
                            storico.setNumeroVersione(rigaRicarico.getVersioneListino().getCodice());
                            salvaScaglioneListinoStorico(storico);
                        }
                        break;
                    default:
                        break;
                    }

                    try {
                        rigaRicarico = panjeaDAO.save(rigaRicarico);
                    } catch (Exception e) {
                        logger.error("--> errore durante il salvataggio della riga listino.", e);
                        throw new RuntimeException("errore durante il salvataggio della riga listino.", e);
                    }
                }
            }
        }

    }

    private RigaListino aggiungiStoricoScaglione(RigaListino rigaListino) {
        // se sto modificando una riga listino di tipo NORMALE controllo se lo scaglione è cambiato.
        // In questo caso
        // aggiungo una riga di storico.
        if (rigaListino.getVersioneListino().getListino().getTipoListino() == ETipoListino.NORMALE) {
            ScaglioneListinoStorico storico = null;

            ScaglioneListino scaglioneNew = rigaListino.getScaglioni().iterator().next();
            if (rigaListino.isNew()) {
                storico = new ScaglioneListinoStorico();
                storico.setNumeroDecimaliPrezzo(rigaListino.getNumeroDecimaliPrezzo());
                storico.setPrezzo(scaglioneNew.getPrezzo());
                storico.setArticolo(rigaListino.getArticolo());
                storico.setListino(rigaListino.getVersioneListino().getListino());
                storico.setNumeroVersione(rigaListino.getVersioneListino().getCodice());
                salvaScaglioneListinoStorico(storico);
            } else {
                try {
                    RigaListino rigaListinoOld = panjeaDAO.load(RigaListino.class, rigaListino.getId());
                    ScaglioneListino scaglioneOld = rigaListinoOld.getScaglioni().iterator().next();

                    if (scaglioneNew.getPrezzo().compareTo(scaglioneOld.getPrezzo()) != 0) {
                        storico = new ScaglioneListinoStorico();
                        storico.setNumeroDecimaliPrezzo(rigaListino.getNumeroDecimaliPrezzo());
                        storico.setPrezzo(scaglioneNew.getPrezzo());
                        storico.setArticolo(rigaListino.getArticolo());
                        storico.setListino(rigaListino.getVersioneListino().getListino());
                        storico.setNumeroVersione(rigaListino.getVersioneListino().getCodice());
                        salvaScaglioneListinoStorico(storico);
                    }
                    ((Session) panjeaDAO.getEntityManager().getDelegate()).evict(rigaListinoOld);
                } catch (Exception e) {
                    logger.error("--> errore durante l'aggiunta dello storico allo scaglione", e);
                    throw new RuntimeException("errore durante l'aggiunta dello storico allo scaglione", e);
                }
            }
        }

        return rigaListino;
    }

    @Override
    public void cancellaListino(Listino listino) {
        logger.debug("--> Enter cancellaListino");

        try {
            // lo storico del listino non è legato al listino o alle versioni quindi lo cancello a
            // mano prima del
            // listino
            StringBuilder sb = new StringBuilder(200);
            sb.append("delete from maga_scaglioni_listini_storico where listino_id = ");
            sb.append(listino.getId());
            SQLQuery query = panjeaDAO.prepareNativeQuery(sb.toString());
            query.executeUpdate();

            panjeaDAO.delete(listino);
        } catch (Exception e) {
            logger.error("--> Errore durante la cancellazione del listino " + listino.getId(), e);
            throw new RuntimeException("Errore durante la cancellazione del listino " + listino.getId(), e);
        }

        logger.debug("--> Exit cancellaListino");
    }

    @Override
    public void cancellaRigaListino(RigaListino rigaListino) {
        logger.debug("--> Enter cancellaRigaListino");

        try {
            panjeaDAO.delete(rigaListino);
        } catch (Exception e) {
            logger.error("--> Errore durante la cancellazione della riga listino.", e);
            throw new RuntimeException("Errore durante la cancellazione della riga listino.", e);
        }

        logger.debug("--> Exit cancellaRigaListino");
    }

    @Override
    public void cancellaRigheListino(List<RigaListino> righeListino) {
        logger.debug("--> Enter cancellaRigheListino");

        for (RigaListino rigaListino : righeListino) {
            cancellaRigaListino(rigaListino);
        }

        logger.debug("--> Exit cancellaRigheListino");
    }

    @Override
    public void cancellaVersioneListino(VersioneListino versioneListino) {
        logger.debug("--> Enter cancellaVersioneListino");

        try {
            panjeaDAO.delete(versioneListino);
        } catch (Exception e) {
            logger.error("--> Errore durante la cancellazione della versione listino.", e);
            throw new RuntimeException("Errore durante la cancellazione della versione listino.", e);
        }

        logger.debug("--> Exit cancellaVersioneListino");
    }

    @SuppressWarnings("unchecked")
    @Override
    public BigDecimal caricaImportoListino(Integer idListino, Integer idArticolo) {

        StringBuffer sb = new StringBuffer();
        sb.append("select riga ");
        sb.append(
                "from RigaListino riga  join  fetch riga.scaglioni inner join riga.versioneListino ver inner join ver.listino list ");
        sb.append("where list.id = :idListino and riga.articolo.id = :idArticolo and ver.dataVigore <= :data ");
        sb.append("order by ver.dataVigore ");

        Query query = panjeaDAO.prepareQuery(sb.toString());
        query.setParameter("idListino", idListino);
        query.setParameter("idArticolo", idArticolo);
        query.setParameter("data", Calendar.getInstance().getTime());
        query.setFirstResult(0);
        query.setMaxResults(1);

        List<RigaListino> righe = null;
        try {
            righe = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            logger.error("--> errore durante il caricamento del prezzo della riga listino.", e);
            throw new RuntimeException("errore durante il caricamento del prezzo della riga listino.", e);
        }

        BigDecimal prezzo = null;
        if (!righe.isEmpty()) {
            prezzo = righe.get(0).getPrezzo();
            prezzo = prezzo.setScale(righe.get(0).getNumeroDecimaliPrezzo());
        }
        return prezzo;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Listino> caricaListini() {
        Query query = null;
        query = panjeaDAO.prepareNamedQuery("Listino.caricaAll");
        query.setParameter("paramCodiceAzienda", getAzienda());

        List<Listino> list = new ArrayList<Listino>();
        try {
            list = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            logger.error("--> Errore durante il caricamento dei listini", e);
            throw new RuntimeException("Errore durante il caricamento dei listini", e);
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Listino> caricaListini(ETipoListino tipoListino, String searchField, String searchValue) {
        logger.debug("--> Enter caricaListini");

        // se non viene specificato il campo di ricerca vado di default sul codice
        searchField = searchField == null ? "codice" : searchField;

        StringBuilder sb = new StringBuilder("from Listino l where l.codiceAzienda = :paramCodiceAzienda ");
        if (tipoListino != null) {
            sb.append(" and l.tipoListino = ").append(tipoListino.ordinal());
        }
        if (searchValue != null) {
            sb.append(" and l.").append(searchField).append(" like ").append(PanjeaEJBUtil.addQuote(searchValue));
            sb.append(" order by l.").append(searchField);
        }
        Query query = panjeaDAO.prepareQuery(sb.toString());
        query.setParameter("paramCodiceAzienda", getAzienda());

        List<Listino> list = new ArrayList<Listino>();
        try {
            list = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            logger.error("--> Errore durante il caricamento dei listini", e);
            throw new RuntimeException("Errore durante il caricamento dei listini", e);
        }

        logger.debug("--> Exit caricaListini");
        return list;
    }

    /**
     * Carica tutti i listini collegati al listino base.
     *
     * @param listinoBase
     *            listino base
     * @return listini caricati
     */
    @SuppressWarnings("unchecked")
    private List<Listino> caricaListiniByListinoBase(Listino listinoBase) {
        logger.debug("--> Enter caricaListiniByListinoBase");

        Query query = panjeaDAO.prepareNamedQuery("Listino.caricaByListinoBase");
        query.setParameter("idListinoBase", listinoBase.getId());

        List<Listino> list = new ArrayList<Listino>();

        try {
            list = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            logger.error("--> Errore durante il caricamento dei listini collegati al listino base", e);
            throw new RuntimeException("Errore durante il caricamento dei listini collegati al listino base", e);
        }
        logger.debug("--> Exit caricaListiniByListinoBase");
        return list;
    }

    @Override
    public Listino caricaListino(Listino listino, boolean initializeLazy) {
        logger.debug("--> Enter caricaListino");

        Listino listinoCaricato = null;

        try {
            listinoCaricato = panjeaDAO.load(Listino.class, listino.getId());

            // controllo se devo inizializzare le versioni
            if (initializeLazy) {
                listinoCaricato.getVersioniListino().size();
            }
        } catch (Exception e) {
            logger.error("--> Errore durante il caricamento del listino " + listino.getId(), e);
            throw new RuntimeException("Errore durante il caricamento del listino " + listino.getId(), e);
        }

        logger.debug("--> Exit caricaListino");
        return listinoCaricato;
    }

    @Override
    public RigaListino caricaRigaListino(Integer idRiga) {
        logger.debug("--> Enter caricaRigaListino");

        Query query = panjeaDAO.prepareNamedQuery("RigaListino.caricaById");
        query.setParameter("paramId", idRiga);

        RigaListino rigaListino = null;

        try {
            rigaListino = (RigaListino) panjeaDAO.getSingleResult(query);
        } catch (Exception e) {
            logger.error("--> Errore durante il caricamento della riga listino", e);
            throw new RuntimeException("Errore durante il caricamento della riga listino", e);
        }

        logger.debug("--> Exit caricaRigheListinoByArticolo");
        return rigaListino;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RigaListinoDTO> caricaRigheListinoByArticolo(Integer idArticolo) {
        logger.debug("--> Enter caricaRigheListinoByArticolo");

        // Carico gli id delle righe articolo delle versioni in vigore alla data di oggi
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String dataString = dateFormat.format(Calendar.getInstance().getTime());

        StringBuilder sb = new StringBuilder();
        sb.append("select riga.id ");
        sb.append(
                "from maga_righe_listini riga inner join (select ver1.listino_id as listinoId,ver1.id as versioneListino,ver1.dataVigore as dataVigore, count(*) as highter ");
        sb.append(
                "								 from maga_versioni_listino ver1 inner join maga_versioni_listino ver2 on ver1.listino_id=ver2.listino_id and ver1.dataVigore <= ver2.dataVigore ");
        sb.append("								 where ver1.dataVigore <= '" + dataString
                + "' and ver2.dataVigore <=  '" + dataString + "' ");
        sb.append("								 group by ver1.listino_id, ver1.id,ver1.dataVigore ");
        sb.append("								 having highter <= 1 ");
        sb.append(
                "								 order by ver1.dataVigore desc) as versioneCorrente on riga.versioneListino_id = versioneCorrente.versioneListino ");
        sb.append("					    inner join maga_listini list on list.id = versioneCorrente.listinoId ");
        sb.append("where riga.articolo_id = :paramIdArticolo ");

        Query query = panjeaDAO.getEntityManager().createNativeQuery(sb.toString());
        query.setParameter("paramIdArticolo", idArticolo);
        List<Integer> listIdRighe = new ArrayList<Integer>();

        try {
            listIdRighe = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            logger.error("--> Errore durante il caricamento delle righe listino per l'articolo", e);
            throw new RuntimeException("Errore durante il caricamento delle righe listino per l'articolo", e);
        }

        List<RigaListinoDTO> list = new ArrayList<RigaListinoDTO>();

        // ottenuta la lista degli id carico le righe articolo listino
        if (!listIdRighe.isEmpty()) {
            sb = new StringBuilder();
            sb.append("select riga.id as id, ");
            sb.append("versione.id as idVersioneListino, ");
            sb.append("versione.dataVigore as dataVigoreVersioneListino, ");
            sb.append("versione.codice as codiceVersioneListino, ");
            sb.append("listino.id as idListino, ");
            sb.append("listino.codice as codiceListino, ");
            sb.append("listino.descrizione as descrizioneListino, ");
            sb.append("riga.numeroDecimaliPrezzo as numeroDecimaliPrezzo, ");
            sb.append("scaglioni.prezzo as prezzo ");
            sb.append("from maga_righe_listini riga ");
            sb.append("inner join maga_scaglioni_listini scaglioni on riga.id=scaglioni.rigaListino_id ");
            sb.append("inner join maga_articoli art on riga.articolo_id=art.id ");
            sb.append("inner join anag_unita_misura um on art.unitaMisura_id=um.id ");
            sb.append("inner join anag_codici_iva codIva on art.codiceIva_id=codIva.id ");
            sb.append("inner join maga_versioni_listino versione on riga.versioneListino_id=versione.id ");
            sb.append("inner join maga_listini listino on versione.listino_id=listino.id ");
            sb.append("where riga.id in (" + StringUtils.join(listIdRighe.iterator(), ",") + ") ");
            sb.append("and scaglioni.quantita = 999999999 ");
            // query = panjeaDAO
            // .prepareQuery("select riga from RigaListino riga join fetch riga.versioneListino
            // versione join fetch versione.listino list left join list.listinoBase where riga.id in
            // (:idRighe)");

            String[] aliasScalar = new String[] { "id", "idVersioneListino", "dataVigoreVersioneListino",
                    "codiceVersioneListino", "idListino", "codiceListino", "descrizioneListino", "numeroDecimaliPrezzo",
                    "prezzo" };

            query = panjeaDAO.prepareSQLQuery(sb.toString(), RigaListinoDTO.class, Arrays.asList(aliasScalar));
            // query.setParameter("idRighe", StringUtils.join(listIdRighe.iterator(), ","));

            try {
                list = panjeaDAO.getResultList(query);
            } catch (Exception e) {
                logger.error("--> Errore durante il caricamento delle righe listino per l'articolo", e);
                throw new RuntimeException("Errore durante il caricamento delle righe listino per l'articolo", e);
            }

            // carico il costo medio aziendale
            ParametriRicercaValorizzazione parametri = new ParametriRicercaValorizzazione();
            ArticoloLite articoloLite = new ArticoloLite();
            articoloLite.setId(idArticolo);
            parametri.getArticoliLite().add(articoloLite);
            parametri.setCalcolaGiacenza(false);
            parametri.setConsideraArticoliDisabilitati(true);
            parametri.setModalitaValorizzazione(EModalitaValorizzazione.ULTIMO_COSTO_AZIENDA);

            List<ValorizzazioneArticolo> caricaValorizzazione = magazzinoValorizzazioneManager
                    .caricaValorizzazione(parametri);
            if (caricaValorizzazione != null && !caricaValorizzazione.isEmpty()) {
                // prendo il primo tanto l'articolo è solo 1
                for (RigaListinoDTO rigaListinoDTO : list) {
                    rigaListinoDTO.setUltimoCosto(caricaValorizzazione.get(0).getCosto());
                }
            }
        }

        logger.debug("--> Exit caricaRigheListinoByArticolo");
        return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RigaListino> caricaRigheListinoByArticolo(VersioneListino versioneListino, Integer idArticolo) {
        logger.debug("--> Enter caricaRigheListinoByArticolo");

        Query query = panjeaDAO.prepareNamedQuery("RigaListino.caricaByVersioneArticolo");
        query.setParameter("paramIdVersioneListino", versioneListino.getId());
        query.setParameter("paramIdArticolo", idArticolo);

        List<RigaListino> list = new ArrayList<RigaListino>();

        try {
            list = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            logger.error("--> Errore durante il caricamento delle righe listino per l'articolo", e);
            throw new RuntimeException("Errore durante il caricamento delle righe listino per l'articolo", e);
        }

        logger.debug("--> Exit caricaRigheListinoByArticolo");
        return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RigaListinoDTO> caricaRigheListinoByVersione(VersioneListino versioneListino) {

        StringBuilder sb = new StringBuilder(500);
        sb.append("select ");
        sb.append("cat.codice as categoria, ");
        sb.append("riga.id as id, ");
        sb.append("art.id as idArticolo, ");
        sb.append("art.codice as codiceArticolo, ");
        sb.append("art.descrizioneLinguaAziendale as descrizioneArticolo, ");
        sb.append("art.barCode as barCodeArticolo, ");
        sb.append("scaglioni.prezzo as prezzo, ");
        sb.append("um.id as idUnitaMisura, ");
        sb.append("um.codice as codiceUnitaMisura, ");
        sb.append("codIva.id as idCodiceIva, ");
        sb.append("codIva.codice as codiceCodiceIva, ");
        sb.append("riga.numeroDecimaliPrezzo as numeroDecimaliPrezzo, ");
        sb.append("riga.versioneListino_id as idVersioneListino, ");
        sb.append("listino.id as idListino ");
        sb.append("from maga_righe_listini riga ");
        sb.append("inner join maga_scaglioni_listini scaglioni on riga.id=scaglioni.rigaListino_id ");
        sb.append("inner join maga_articoli art on riga.articolo_id=art.id ");
        sb.append("inner join anag_unita_misura um on art.unitaMisura_id=um.id ");
        sb.append("inner join anag_codici_iva codIva on art.codiceIva_id=codIva.id ");
        sb.append("inner join maga_versioni_listino versione on riga.versioneListino_id=versione.id ");
        sb.append("inner join maga_listini listino on versione.listino_id=listino.id ");
        sb.append("inner join maga_categorie cat on cat.id=art.categoria_id ");
        sb.append("where art.abilitato=1 ");
        sb.append("and riga.versioneListino_id=:paramVersioneListino ");
        sb.append("and scaglioni.quantita = 999999999 ");

        String[] aliasScalar = new String[] { "categoria", "id", "idArticolo", "codiceArticolo", "descrizioneArticolo",
                "barCodeArticolo", "prezzo", "idUnitaMisura", "codiceUnitaMisura", "idCodiceIva", "codiceCodiceIva",
                "numeroDecimaliPrezzo", "idVersioneListino", "idListino" };

        Query query = panjeaDAO.prepareSQLQuery(sb.toString(), RigaListinoDTO.class, Arrays.asList(aliasScalar));
        query.setParameter("paramVersioneListino", versioneListino.getId());

        List<RigaListinoDTO> righeListino = null;
        try {
            righeListino = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            logger.error("--> errore durante il caricamento delle righe listino", e);
            throw new RuntimeException("errore durante il caricamento delle righe listino", e);
        }

        return righeListino;
    }

    /**
     *
     * @param versioneListino
     *            versione listino da caricare
     * @param initAttributiArticolo
     *            true carica anche gli attributi dell'articolo
     * @return Righe listino data una versione
     */
    @SuppressWarnings("unchecked")
    private List<RigaListino> caricaRigheListinoByVersione(VersioneListino versioneListino,
            boolean initAttributiArticolo, List<Integer> articoli) {
        logger.debug("--> Enter caricaRigheListinoByVersione");
        StringBuilder sb = new StringBuilder(500);
        sb.append("select ");
        sb.append("DISTINCT riga ");
        sb.append("from RigaListino riga ");
        sb.append("join fetch riga.scaglioni ");
        sb.append("join fetch riga.articolo art ");
        sb.append("join fetch art.unitaMisura ");
        sb.append("join fetch art.codiceIva ");
        sb.append("join fetch art.categoria ");
        sb.append("join fetch riga.versioneListino ver ");
        sb.append("join fetch ver.listino ");
        if (initAttributiArticolo) {
            sb.append("join fetch art.attributiArticolo ");
        }
        sb.append("where art.abilitato=true ");
        sb.append("and riga.versioneListino.id =:paramIdVersioneListino ");
        if (!articoli.isEmpty()) {
            sb.append("and art.id in (:articoli) ");
        }
        sb.append("order by art.codice ");

        Query query = panjeaDAO.prepareQuery(sb.toString());
        query.setParameter("paramIdVersioneListino", versioneListino.getId());
        if (!articoli.isEmpty()) {
            query.setParameter("articoli", articoli);
        }
        List<RigaListino> list = new ArrayList<RigaListino>();
        try {
            list = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            logger.error("--> Errore durante il caricamento delle righe listino per l'articolo", e);
            throw new RuntimeException("Errore durante il caricamento delle righe listino per l'articolo", e);
        }
        logger.debug("--> Exit caricaRigheListinoByVersione");
        return list;
    }

    @Override
    public List<RigaListino> caricaRigheListinoDaAggiornare(Date data, List<ArticoloLite> articoli) {
        List<RigaListino> righeListino = new ArrayList<RigaListino>();
        List<Listino> listini = caricaListini();
        for (Listino listino : listini) {
            if (listino.getAggiornaDaUltimoCosto() != null && listino.getAggiornaDaUltimoCosto()) {
                VersioneListino versione = caricaVersioneListinoByData(listino, data);
                if (versione != null) {
                    for (ArticoloLite articoloLite : articoli) {
                        // NPE MAIL: articolo a null nel caso in cui premo aggiornaListino command
                        // su una riga nuova
                        // modificato anche il command per evitare di passare articoli null, ma
                        // lascio il controllo
                        // extra anche qui
                        if (articoloLite != null) {
                            List<RigaListino> righeArticolo = caricaRigheListinoByArticolo(versione,
                                    articoloLite.getId());
                            righeListino.addAll(righeArticolo);
                        }
                    }
                }
            }
        }
        Collections.sort(righeListino, new Comparator<RigaListino>() {

            @Override
            public int compare(RigaListino o1, RigaListino o2) {
                Listino listinoBase1 = o1.getVersioneListino().getListino().getListinoBase();
                Listino listinoBase2 = o2.getVersioneListino().getListino().getListinoBase();

                if (listinoBase1 == null) {
                    return -1;
                } else if (listinoBase2 == null) {
                    return 1;
                } else if (listinoBase1.compareTo(listinoBase2) != 0) {
                    return listinoBase1.compareTo(listinoBase2);
                } else {
                    return o1.getVersioneListino().getListino().getCodice()
                            .compareTo(o2.getVersioneListino().getListino().getCodice());
                }
            }
        });
        return righeListino;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RigaListinoCalcolo> caricaRigheListinoPrezzoCalculator(Listino listino, Date data, Integer idArticolo) {
        logger.debug("--> Enter caricaRigheListino");

        List<RigaListinoCalcolo> listRigheListinoCalcolo = Collections.emptyList();

        VersioneListino versioneListinoValida = caricaVersioneListinoByData(listino, data);

        if (versioneListinoValida != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("select ");
            sb.append("riga.versioneListino.listino.descrizione as listinoDescrizione, ");
            sb.append("riga.versioneListino.listino.codice as listinoCodice, ");
            sb.append("riga.versioneListino.listino.iva as listinoIvato, ");
            sb.append("riga.numeroDecimaliPrezzo as numeroDecimaliPrezzo, ");
            sb.append("scaglioni.prezzo as prezzo, ");
            sb.append("scaglioni.quantita as quantita ");
            sb.append("from RigaListino riga join riga.scaglioni scaglioni join riga.versioneListino.listino listino ");
            sb.append("where riga.articolo.id = :paramIdArticolo ");
            sb.append("and riga.versioneListino.id=:paramIdVersione ");
            org.hibernate.Query query = ((Session) panjeaDAO.getEntityManager().getDelegate())
                    .createQuery(sb.toString());
            query.setParameter("paramIdArticolo", idArticolo);
            query.setParameter("paramIdVersione", versioneListinoValida.getId());

            query.setResultTransformer(Transformers.aliasToBean(RigaListinoCalcolo.class));
            listRigheListinoCalcolo = query.list();
        }

        logger.debug("--> Exit caricaRigheListino");
        return listRigheListinoCalcolo;
    }

    @Override
    public List<RiepilogoSedeEntitaDTO> caricaSediMagazzinoByListino(Listino listino) {
        return sediEntitaManager.caricaRiepilogoSediEntitaByListino(listino.getId());
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ScaglioneListinoStorico> caricaStoricoScaglione(ScaglioneListino scaglioneListino,
            Integer numeroVersione) {
        logger.debug("--> Enter caricaStoricoScaglione");

        StringBuilder sb = new StringBuilder(200);
        sb.append("select storico ");
        sb.append("from ScaglioneListinoStorico storico ");
        sb.append("where storico.articolo.id = :paramIdArticolo and ");
        sb.append("            storico.listino.id = :paramIdListino ");
        if (numeroVersione != null) {
            sb.append("           and storico.numeroVersione = :numeroVersione");
        }

        Query query = panjeaDAO.prepareQuery(sb.toString());
        query.setParameter("paramIdArticolo", scaglioneListino.getRigaListino().getArticolo().getId());
        query.setParameter("paramIdListino",
                scaglioneListino.getRigaListino().getVersioneListino().getListino().getId());
        if (numeroVersione != null) {
            query.setParameter("numeroVersione", numeroVersione);
        }

        List<ScaglioneListinoStorico> storico = null;
        try {
            storico = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            logger.error("--> errore durante il caricamento dello storico dello scaglione.", e);
            throw new RuntimeException("errore durante il caricamento dello storico dello scaglione.", e);
        }

        logger.debug("--> Exit caricaStoricoScaglione");
        return storico;
    }

    @SuppressWarnings("unchecked")
    @Override
    public VersioneListino caricaVersioneListino(Map<Object, Object> parametri) {
        Integer id = (Integer) parametri.get("id");
        String codiceLingua = (String) parametri.get("codiceLingua");
        Boolean caricaAttributiArticolo = Boolean.FALSE;
        List<Integer> articoli = new ArrayList<>();
        if (parametri.containsKey("attributiArticolo")) {
            caricaAttributiArticolo = (Boolean) parametri.get("attributiArticolo");
        }
        if (parametri.containsKey("articoli")) {
            String articoliList = (String) parametri.get("articoli");
            if (!articoliList.isEmpty()) {
                String[] articoliListSplit = articoliList.split(",");
                for (String idArticolo : articoliListSplit) {
                    articoli.add(Integer.parseInt(idArticolo));
                }
            }
        }

        if (id == null) {
            throw new IllegalArgumentException("la mappa dei parametri non contiene versioneListino");
        }
        VersioneListino versioneListino = new VersioneListino();
        versioneListino.setId(id);
        // Nel report mi serve anche il codice iva. Non posso inizializzarlo dalla collection di
        // righe.
        // Le carico a parte e poi le setto nella versione
        // Devo effettuare una evict altrimenti mi salvarebbe le righe all'uscita della transazione.
        versioneListino = caricaVersioneListino(versioneListino, false);
        ((Session) panjeaDAO.getEntityManager().getDelegate()).evict(versioneListino);

        List<RigaListino> righeListino = caricaRigheListinoByVersione(versioneListino, caricaAttributiArticolo,
                articoli);
        versioneListino.setRigheListino(righeListino);

        // carico l'azienda perchè se il codice lingua aziendale è uguale a quello richiesto non
        // faccio niente
        AziendaLite azienda;
        try {
            azienda = aziendeManager.caricaAzienda(getAzienda());
        } catch (AnagraficaServiceException e1) {
            logger.error("--> errore durante il caricamento dell'azienda.", e1);
            throw new RuntimeException("errore durante il caricamento dell'azienda.", e1);
        }

        if (!azienda.getLingua().equals(codiceLingua)) {

            // carico per tutti gli articoli della versione la lingua scelta
            StringBuilder sb = new StringBuilder();
            sb.append("select art.id,descr.descrizione ");
            sb.append(" from RigaListino rl, Articolo art inner join art.descrizioniLingua descr ");
            sb.append(" where rl.articolo.id = art.id and art.abilitato = true and descr.codiceLingua = :paramLingua ");

            Query query = panjeaDAO.prepareQuery(sb.toString());
            query.setParameter("paramLingua", codiceLingua);

            List<Object[]> lista = null;
            try {
                lista = panjeaDAO.getResultList(query);
            } catch (Exception e1) {
                logger.error("--> errore durante il caricamento delle lingue articoli ", e1);
                throw new RuntimeException("errore durante il caricamento delle lingue articoli ", e1);
            }

            // trasformo la lista in una mappa per comodità
            Map<Integer, String> mapLingue = new HashMap<Integer, String>();
            for (Object[] objects : lista) {
                mapLingue.put((Integer) objects[0], (String) objects[1]);
            }

            // Per ogni articolo in listino setto la sua descrizione in lingua se esiste
            for (RigaListino rigaListino : versioneListino.getRigheListino()) {
                String descrizione = mapLingue.get(rigaListino.getArticolo().getId());
                if (descrizione != null) {
                    // faccio l'evict dell'articolo altrimenti se non lo tolgo dalla sessione e gli
                    // setto la descrizione
                    // poi verrà anche salvata su DB
                    ((Session) panjeaDAO.getEntityManager().getDelegate()).evict(rigaListino.getArticolo());
                    rigaListino.getArticolo().setDescrizione(descrizione);
                }
            }
        }
        return versioneListino;
    }

    @Override
    public VersioneListino caricaVersioneListino(VersioneListino versioneListino, boolean initializeLazy) {
        logger.debug("--> Enter caricaVersioneListino");

        VersioneListino versioneListinoCaricata = null;
        try {
            versioneListinoCaricata = panjeaDAO.load(VersioneListino.class, versioneListino.getId());
        } catch (ObjectNotFoundException e) {
            logger.error("--> Errore durante il caricamento della versione listino.", e);
            throw new RuntimeException("Errore durante il caricamento della versione listino.", e);
        }
        logger.debug("--> Exit caricaVersioneListino");
        return versioneListinoCaricata;
    }

    @Override
    public VersioneListino caricaVersioneListinoByData(Listino listino, Date data) {
        logger.debug("--> Enter caricaVersioneListinoByData");

        Query query = panjeaDAO.prepareNamedQuery("VersioneListino.caricaByData");
        query.setMaxResults(1);
        query.setParameter("data", data);
        query.setParameter("listino", listino.getId());
        VersioneListino versione = null;
        try {
            @SuppressWarnings("unchecked")
            List<VersioneListino> result = panjeaDAO.getResultList(query);
            if (result.size() > 0) {
                versione = result.get(0);
            }
        } catch (DAOException e) {
            logger.error("-->errore nel caricare la versione del listino per la data " + data, e);
            throw new RuntimeException(e);
        }

        logger.debug("--> Exit caricaVersioneListinoByData");
        return versione;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<VersioneListino> caricaVersioniListino(String fieldSearch, String valueSearch,
            ETipoListino tipoListino) {
        logger.debug("--> Enter caricaListini");

        StringBuilder sb = new StringBuilder(
                "select v from VersioneListino v where v.listino.codiceAzienda=:paramCodiceAzienda ");
        if (tipoListino != null) {
            sb.append(" and v.listino.tipoListino = :tipoListino ");
        }
        if (valueSearch != null) {
            sb.append(" and v.");
            sb.append(fieldSearch);
            sb.append(" like ");
            sb.append(PanjeaEJBUtil.addQuote(valueSearch));
        }
        sb.append(" order by v.");
        sb.append(fieldSearch);

        List<VersioneListino> list = new ArrayList<VersioneListino>();

        Query query = panjeaDAO.prepareQuery(sb.toString());
        if (tipoListino != null) {
            query.setParameter("tipoListino", tipoListino);
        }
        query.setParameter("paramCodiceAzienda", getAzienda());
        try {
            list = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            logger.error("--> Errore durante il caricamento dei listini", e);
            throw new RuntimeException("Errore durante il caricamento dei listini", e);
        }

        logger.debug("--> Exit caricaListini");
        return list;
    }

    @Override
    public VersioneListino copiaVersioneListino(VersioneListino versioneListino, Date dataNuovaVersioneListino) {
        logger.debug("--> Enter copiaVersioneListino");

        VersioneListino versioneListinoFull = caricaVersioneListino(versioneListino, true);
        VersioneListino versioneListinoNuova = new VersioneListino();
        versioneListinoNuova.setDataVigore(dataNuovaVersioneListino);
        versioneListinoNuova.setListino(versioneListinoFull.getListino());
        versioneListinoNuova = salvaVersioneListino(versioneListinoNuova);

        // copio le righe listino
        StringBuilder sb = new StringBuilder(300);
        sb.append(
                "INSERT INTO maga_righe_listini (id,version,userInsert,versioneListino_id,articolo_id,numeroDecimaliPrezzo,timeStamp,percentualeRicarico) ");
        sb.append(
                "select null,0,:utente,:idNuovaVersione,articolo_id,numeroDecimaliPrezzo,:ora,percentualeRicarico from maga_righe_listini where versioneListino_id = :idVecchiaVersione");
        SQLQuery query = panjeaDAO.prepareNativeQuery(sb.toString());
        query.setParameter("utente", getJecPrincipal().getUserName());
        query.setParameter("ora", Calendar.getInstance().getTimeInMillis());
        query.setParameter("idNuovaVersione", versioneListinoNuova.getId());
        query.setParameter("idVecchiaVersione", versioneListino.getId());
        query.executeUpdate();

        // copio gli scaglioni
        sb = new StringBuilder(300);
        sb.append(
                "INSERT INTO maga_scaglioni_listini (id,timeStamp,userInsert,version,prezzo,quantita,rigaListino_id) ");
        sb.append("select null,:ora,:utente,0,scl.prezzo,scl.quantita,rlnew.id ");
        sb.append("from maga_scaglioni_listini scl inner join maga_righe_listini rl on scl.rigaListino_id = rl.id ");
        sb.append(
                "						  inner join maga_righe_listini rlnew on rlnew.articolo_id = rl.articolo_id and rlnew.versioneListino_id = :idNuovaVersione ");
        sb.append("where rl.versioneListino_id = :idVecchiaVersione ");
        query = panjeaDAO.prepareNativeQuery(sb.toString());
        query.setParameter("utente", getJecPrincipal().getUserName());
        query.setParameter("ora", Calendar.getInstance().getTimeInMillis());
        query.setParameter("idNuovaVersione", versioneListinoNuova.getId());
        query.setParameter("idVecchiaVersione", versioneListino.getId());
        query.executeUpdate();

        versioneListinoNuova = caricaVersioneListino(versioneListinoNuova, true);
        logger.debug("--> Exit copiaVersioneListino");
        return versioneListinoNuova;
    }

    /**
     *
     * @return codice azienda loggata
     */
    private String getAzienda() {
        return ((JecPrincipal) sessionContext.getCallerPrincipal()).getCodiceAzienda();
    }

    /**
     * recupera dal context il {@link JecPrincipal} loggato.
     *
     * @return jecprincipal loggato.
     */
    private JecPrincipal getJecPrincipal() {
        return (JecPrincipal) sessionContext.getCallerPrincipal();
    }

    @Override
    public Listino salvaListino(Listino listino) {
        logger.debug("--> Enter salvaListino");

        if (listino.getCodiceAzienda() == null) {
            listino.setCodiceAzienda(getAzienda());
        }

        if (listino.getTipoListino() == ETipoListino.SCAGLIONE) {
            // verifico che il listino a scaglione abbia valori coerenti
            if (listino.getAggiornaDaUltimoCosto()) {
                throw new IllegalArgumentException(
                        "Listino a scaglione non può essere usalto per aggiornamento da documento");
            }
            if (listino.getListinoBase() != null) {
                throw new IllegalArgumentException("Listino a scaglione non può avere un listino base");
            }
        }

        // se stò modificando il listino mi carico quello presente per verificare se l'utente ha
        // cambiato il tipo
        if (!listino.isNew()) {
            Listino listinoOld = caricaListino(listino, false);
            ((Session) panjeaDAO.getEntityManager().getDelegate()).evict(listinoOld);
            // da scaglioni a normale non è permesso
            if (listino.getTipoListino() == ETipoListino.NORMALE
                    && listinoOld.getTipoListino() == ETipoListino.SCAGLIONE) {
                throw new IllegalArgumentException("Non è possibile cambiare il tipo listino da SCAGLIONI a NORMALE");
            }
        }

        try {
            listino = panjeaDAO.save(listino);
            if (listino.getVersioniListino() != null) {
                listino.getVersioniListino().size();
            }

        } catch (Exception e) {
            logger.error("--> Errore durante il salvataggio del listino", e);
            throw new RuntimeException("Errore durante il salvataggio del listino", e);
        }

        logger.debug("--> Exit salvaListino");
        return listino;
    }

    @Override
    public List<RigaListino> salvaPrezzoRigheListino(List<RigaListino> listRigheListino)
            throws RigheListinoListiniCollegatiException {
        logger.debug("--> Enter salvaRigheListino");
        List<RigaListino> listRigheSalvate = new ArrayList<RigaListino>();

        RigheListinoListiniCollegatiException exception = new RigheListinoListiniCollegatiException();

        for (RigaListino rigaListino : listRigheListino) {
            try {
                RigaListino rigaSalvata = salvaRigaListino(rigaListino);
                listRigheSalvate.add(rigaSalvata);
            } catch (RigaListinoListiniCollegatiException e) {
                exception.addException(e);
            }
        }

        if (!exception.isEmpty()) {
            sessionContext.setRollbackOnly();
            throw exception;
        }

        logger.debug("--> Exit salvaRigheListino");
        return listRigheSalvate;
    }

    @Override
    public List<RigaListino> salvaPrezzoRigheListino(List<RigaListino> listRigheListino,
            boolean aggiornaListiniCollegati) {
        logger.debug("--> Enter salvaRigheListino");
        List<RigaListino> listRigheSalvate = new ArrayList<RigaListino>();

        for (RigaListino rigaListino : listRigheListino) {
            BigDecimal prezzo = rigaListino.getPrezzo();
            try {
                // ricarico la riga e risetto il prezzo perchè la versione potrebbe essere cambiata
                // dal salvataggio di
                // una riga precedente se faceva parte del suo listino base
                rigaListino = panjeaDAO.load(RigaListino.class, rigaListino.getId());
                ((Session) panjeaDAO.getEntityManager().getDelegate()).evict(rigaListino);
                rigaListino.setPrezzo(prezzo);
            } catch (ObjectNotFoundException e) {
                logger.error("--> errore durante il caricamento della riga listino", e);
                throw new RuntimeException("errore durante il caricamento della riga listino", e);
            }
            RigaListino rigaSalvata = salvaRigaListino(rigaListino, aggiornaListiniCollegati);
            listRigheSalvate.add(rigaSalvata);
        }

        logger.debug("--> Exit salvaRigheListino");
        return listRigheSalvate;
    }

    @Override
    public RigaListino salvaRigaListino(RigaListino rigaListino) throws RigaListinoListiniCollegatiException {
        logger.debug("--> Enter salvaRigaListino");

        MagazzinoSettings magazzinoSettings = magazzinoSettingsManager.caricaMagazzinoSettings();

        // solo se nelle settings non c'è l'aggiornamento automatico eseguo il controllo dei listini
        // collegati,
        // altrimenti salvo sempre aggiornandoli
        if (!magazzinoSettings.isAggiornaAutomaticamenteListiniCollegati()) {
            // verifico se ci sono listini collegati alla riga
            List<Listino> listiniCollegati = caricaListiniByListinoBase(rigaListino.getVersioneListino().getListino());
            if (listiniCollegati != null && !listiniCollegati.isEmpty()) {
                throw new RigaListinoListiniCollegatiException(rigaListino, listiniCollegati);
            }
        }

        logger.debug("--> Exit salvaRigaListino");
        return salvaRigaListino(rigaListino, true);
    }

    @Override
    public RigaListino salvaRigaListino(RigaListino rigaListino, boolean aggiornaListiniCollegati) {
        logger.debug("--> Enter salvaRigaListino");

        try {
            rigaListino = aggiungiStoricoScaglione(rigaListino);

            for (ScaglioneListino scaglioneListino : rigaListino.getScaglioni()) {
                scaglioneListino.setRigaListino(rigaListino);
            }
            rigaListino = panjeaDAO.save(rigaListino);
            // Init lazy
            rigaListino.getArticolo().getCodiceIva().getCodice();
            rigaListino.getVersioneListino().getCodice();

            TipoAggiornamentoPercentualeRicarico tipoAggiornamento = aggiornaListiniCollegati
                    ? TipoAggiornamentoPercentualeRicarico.APPLICA : TipoAggiornamentoPercentualeRicarico.CALCOLA;
            aggiornaPercentualeRicaricoArticolo(rigaListino, tipoAggiornamento);
        } catch (Exception e) {
            logger.error("--> Errore durante il salvataggio della riga listino.", e);
            throw new RuntimeException("Errore durante il salvataggio della riga listino.", e);
        }
        logger.debug("--> Exit salvaRigaListino");
        return rigaListino;
    }

    @Override
    public ScaglioneListinoStorico salvaScaglioneListinoStorico(ScaglioneListinoStorico scaglioneListinoStorico) {
        logger.debug("--> Enter salvaScaglioneListinoStorico");

        ScaglioneListinoStorico scaglioneListinoStoricoSalvato = null;
        try {
            scaglioneListinoStoricoSalvato = panjeaDAO.save(scaglioneListinoStorico);
        } catch (Exception e) {
            logger.error("--> errore durante il salvataggio dello storico", e);
        }

        logger.debug("--> Exit salvaScaglioneListinoStorico");
        return scaglioneListinoStoricoSalvato;
    }

    @Override
    public VersioneListino salvaVersioneListino(VersioneListino versioneListino) {
        logger.debug("--> Enter salvaVersioneListino");

        VersioneListino versioneListinoSalvata = null;

        // se la versione è nuova cerco l'ultimo codice libero
        if (versioneListino.isNew()) {
            Query query = panjeaDAO.prepareNamedQuery("VersioneListino.caricaLastCodice");
            query.setParameter("paramListino", versioneListino.getListino());

            Integer lastCodice = null;
            try {
                lastCodice = (Integer) panjeaDAO.getSingleResult(query);
            } catch (Exception e) {
                logger.error("--> Errore durante la ricerca dell'ultimo codice per la versione listino.", e);
                throw new RuntimeException("Errore durante la ricerca dell'ultimo codice per la versione listino.", e);
            }

            // se non ci sono ancora versioni per il listino setto il codice a 0
            if (lastCodice == null) {
                lastCodice = 0;
            }
            lastCodice = lastCodice + 1;

            versioneListino.setCodice(lastCodice);
        }

        try {
            versioneListinoSalvata = panjeaDAO.save(versioneListino);
        } catch (Exception e) {
            logger.error("--> Errore durante il salvataggio della versione listino", e);
            throw new RuntimeException("Errore durante il salvataggio della versione listino", e);
        }

        logger.debug("--> Exit salvaVersioneListino");
        return versioneListinoSalvata;
    }
}
