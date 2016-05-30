package it.eurotn.panjea.rate.manager;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.ejb.QueryImpl;
import org.hibernate.transform.Transformers;
import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.matchers.Matcher;
import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentiCollegatiPresentiException;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioSedeEntita;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.TipoPagamento;
import it.eurotn.panjea.anagrafica.domain.lite.AziendaLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.manager.interfaces.AziendeManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.SediEntitaManager;
import it.eurotn.panjea.anagrafica.manager.rapportibancarisedeentita.interfaces.RapportiBancariSedeEntitaManager;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoManager;
import it.eurotn.panjea.partite.util.ParametriRicercaRate;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rate.domain.Rata;
import it.eurotn.panjea.rate.domain.Rata.StatoRata;
import it.eurotn.panjea.rate.manager.interfaces.AreaRateManager;
import it.eurotn.panjea.rate.manager.interfaces.RateManager;
import it.eurotn.panjea.rate.manager.sqlbuilder.RataRVRagguppateSqlBuilder;
import it.eurotn.panjea.rate.manager.sqlbuilder.RataRVSqlBuilder;
import it.eurotn.panjea.rate.manager.sqlbuilder.RicercaRateQueryBuilder;
import it.eurotn.panjea.rate.util.RataRV;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.tesoreria.domain.Pagamento;
import it.eurotn.panjea.tesoreria.util.RataRiemessa;
import it.eurotn.panjea.tesoreria.util.SituazioneRata;
import it.eurotn.security.JecPrincipal;

/**
 *
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.RateManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RateManager")
public class RateManagerBean implements RateManager {

    private static Logger logger = Logger.getLogger(RateManagerBean.class.getName());

    @Resource
    private SessionContext context;

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private AreaRateManager areaRateManager;

    @EJB
    @IgnoreDependency
    private AreaMagazzinoManager areaMagazzinoManager;

    @EJB
    private SediEntitaManager sediEntitaManager;

    @EJB
    private AziendeManager aziendeManager;

    @EJB
    private RapportiBancariSedeEntitaManager rapportiBancariSedeEntitaManager;

    @Override
    public Rata associaRapportoBancario(Rata rata, IAreaDocumento areaDocumento, TipoPagamento tipoPagamento,
            boolean salvaRata) {
        RapportoBancarioSedeEntita rapportoBancarioSedeEntita = cercaRapportoBancarioEntita(tipoPagamento,
                areaDocumento);
        if (rapportoBancarioSedeEntita != null) {
            rata.setRapportoBancarioEntita(rapportoBancarioSedeEntita);
            if (salvaRata) {
                salvaRataNoCheck(rata);
            }
        }
        return rata;
    }

    @Override
    public void cancellaRata(Rata rata) throws DocumentiCollegatiPresentiException {
        logger.debug("--> Enter cancellaRata");
        if (!rata.getStatoRata().equals(StatoRata.APERTA)) {
            logger.warn(
                    "--> Rata in stato diverso da APERTA, esiste un documento di pagamento collegato all'area partite della rata");
            throw new DocumentiCollegatiPresentiException(
                    "Rata in stato diverso da APERTA, esiste un documento di pagamento collegato all'area partite della rata");
        }
        AreaRate areaRate = rata.getAreaRate();
        if (areaRate.getRate() != null) {
            areaRate.getRate().remove(rata);
        }
        cancellaRataNoCheck(rata);

        logger.debug("--> Exit cancellaRata");
    }

    @Override
    public void cancellaRataNoCheck(Rata rata) {
        logger.debug("--> Enter cancellaRataNoCheck");
        try {
            panjeaDAO.delete(rata);
        } catch (Exception e) {
            logger.error("--> errore in cancellazione Area Partita ", e);
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit cancellaRataNoCheck");
    }

    @Override
    public Rata caricaRata(Integer idRata) {
        try {
            return panjeaDAO.load(Rata.class, idRata);
        } catch (Exception e) {
            logger.error("--> Rata non trovata. ID cercato=" + idRata);
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Rata> caricaRateCollegate(Rata rata) {
        logger.debug("--> Enter caricaRateCollegate");

        Query query = panjeaDAO.prepareNamedQuery("Rata.caricaRateCollegate");
        query.setParameter("paramIdRataRiemessa", rata.getId());

        List<Rata> rateCollegate = null;
        try {
            rateCollegate = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            logger.error("--> errore durante il caricamento delle rate collegate.", e);
            throw new RuntimeException("errore durante il caricamento delle rate collegate.", e);
        }

        logger.debug("--> Exit caricaRateCollegate");
        return rateCollegate;
    }

    @Override
    public List<RataRV> caricaRatePerRichiestaVersamento(Map<Object, Object> parametri) {

        String codiceAzienda = parametri.get("azienda").toString();
        String dataCreazione = (String) parametri.get("dataCreazione");
        String idAreeMagazzino = (String) parametri.get("areeMagazzinoId");
        String idRate = (String) parametri.get("rateId");
        if (idAreeMagazzino != null && idAreeMagazzino.isEmpty()) {
            idAreeMagazzino = null;
        }

        RataRVSqlBuilder builder = new RataRVSqlBuilder();
        String sqlString = builder.buildSql(codiceAzienda, idAreeMagazzino, dataCreazione, idRate);
        SQLQuery query = ((Session) panjeaDAO.getEntityManager().getDelegate()).createSQLQuery(sqlString);
        query.setResultTransformer(Transformers.aliasToBean(RataRV.class));
        builder.addScalar(query);

        return creaRatePerRichiestaVersamento(query);
    }

    @Override
    public List<RataRV> caricaRateRaggruppatePerRichiestaVersamento(Map<Object, Object> parametri) {
        String codiceAzienda = parametri.get("azienda").toString();
        String dataCreazione = (String) parametri.get("dataCreazione");
        String idAreeMagazzino = (String) parametri.get("areeMagazzinoId");
        String idRate = (String) parametri.get("rateId");
        if (idAreeMagazzino.isEmpty()) {
            idAreeMagazzino = null;
        }

        RataRVSqlBuilder builder = new RataRVRagguppateSqlBuilder();
        String sqlString = builder.buildSql(codiceAzienda, idAreeMagazzino, dataCreazione, idRate);
        SQLQuery query = ((Session) panjeaDAO.getEntityManager().getDelegate()).createSQLQuery(sqlString);
        query.setResultTransformer(Transformers.aliasToBean(RataRV.class));
        builder.addScalar(query);

        return creaRatePerRichiestaVersamento(query);
    }

    /**
     * 
     * @param tipoPagamento
     *            TipoPagamento
     * @param areaDocumento
     *            IAreaDocumento
     * @return rapportoBancarioSedeEntita RapportoBancarioSedeEntita
     */

    private RapportoBancarioSedeEntita cercaRapportoBancarioEntita(TipoPagamento tipoPagamento,
            IAreaDocumento areaDocumento) {
        EntitaLite entita = areaDocumento.getDocumento().getEntita();
        AreaMagazzino areaMagazzino = areaMagazzinoManager.caricaAreaMagazzinoByDocumento(areaDocumento.getDocumento());
        if (areaMagazzino != null) {
            SedeEntita sedeEntita = areaMagazzino.getDocumento().getSedeEntita();
            return rapportiBancariSedeEntitaManager.caricaRapportoBancarioPerTipoPagamentoDefault(tipoPagamento,
                    sedeEntita, entita.getId());
        } else {
            return rapportiBancariSedeEntitaManager.caricaRapportoBancarioPerTipoPagamentoDefault(tipoPagamento, null,
                    entita.getId());
        }
    }

    @SuppressWarnings("unchecked")
    private List<RataRV> creaRatePerRichiestaVersamento(SQLQuery query) {
        List<RataRV> rateCaricate = new ArrayList<RataRV>();

        try {
            rateCaricate = query.list();
        } catch (Exception e) {
            logger.error("-->errore nel caricare le rate con richiesta versamento", e);
            throw new RuntimeException(e);
        }

        AziendaLite azienda = null;
        try {
            azienda = aziendeManager.caricaAzienda(getJecPrincipal().getCodiceAzienda(), true);
        } catch (AnagraficaServiceException e1) {
            logger.error("--> Errore durante il caricamento dell'azienda", e1);
            throw new RuntimeException("Errore durante il caricamento dell'azienda", e1);
        }

        for (RataRV rataRV : rateCaricate) {

            SedeEntita sedeEntita;
            try {
                sedeEntita = sediEntitaManager.caricaSedePrincipaleEntita(rataRV.getEntitaId());
            } catch (AnagraficaServiceException e) {
                logger.error(
                        "--> Errore durante il caricamento della sede principale dell'entità " + rataRV.getEntitaId(),
                        e);
                throw new RuntimeException(
                        "Errore durante il caricamento della sede principale dell'entità " + rataRV.getEntitaId(), e);
            }

            rataRV.setEntitaDescrizione(sedeEntita.getEntita().getAnagrafica().getDenominazione());
            rataRV.setEntitaIndirizzo(sedeEntita.getSede().getIndirizzo());
            rataRV.setEntitaCap(sedeEntita.getSede().getDatiGeografici().getDescrizioneCap());
            rataRV.setEntitaLocalita(sedeEntita.getSede().getDatiGeografici().getDescrizioneLocalita());
            rataRV.setEntitaLivelloAmministrativo1(
                    sedeEntita.getSede().getDatiGeografici().getLivelloAmministrativo1());
            rataRV.setEntitaLivelloAmministrativo2(
                    sedeEntita.getSede().getDatiGeografici().getLivelloAmministrativo2());
            rataRV.setEntitaLivelloAmministrativo3(
                    sedeEntita.getSede().getDatiGeografici().getLivelloAmministrativo3());
            rataRV.setEntitaLivelloAmministrativo4(
                    sedeEntita.getSede().getDatiGeografici().getLivelloAmministrativo4());
            rataRV.setEntitaNazione(sedeEntita.getSede().getDatiGeografici().getDescrizioneNazione());

            rataRV.setAziendaDescrizione(azienda.getDenominazione());
            rataRV.setAziendaIndirizzo(azienda.getIndirizzo());
            rataRV.setAziendaCap(azienda.getCap().getDescrizione());
            rataRV.setAziendaLocalita(azienda.getLocalita().getDescrizione());
            rataRV.setAziendaLivelloAmministrativo1(azienda.getLivelloAmministrativo1());
            rataRV.setAziendaLivelloAmministrativo2(azienda.getLivelloAmministrativo2());
            rataRV.setAziendaLivelloAmministrativo3(azienda.getLivelloAmministrativo3());
            rataRV.setAziendaLivelloAmministrativo4(azienda.getLivelloAmministrativo4());
            rataRV.setAziendaNazione(azienda.getNazione().getDescrizione());
        }
        return rateCaricate;
    }

    /**
     * recupera {@link JecPrincipal} dal {@link SessionContext}.
     * 
     * @return jecPrincipal JecPrincipal
     */
    private JecPrincipal getJecPrincipal() {
        return (JecPrincipal) context.getCallerPrincipal();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<SituazioneRata> ricercaRate(final ParametriRicercaRate parametriRicercaRate) {
        Query query = RicercaRateQueryBuilder.getQuery(parametriRicercaRate, panjeaDAO);
        ((QueryImpl) query).getHibernateQuery().setResultTransformer(Transformers.aliasToBean((SituazioneRata.class)));

        SQLQuery sqlQuery = (SQLQuery) ((QueryImpl) query).getHibernateQuery();

        sqlQuery.addScalar("idEntita");
        sqlQuery.addScalar("tipoEntita");
        sqlQuery.addScalar("codiceEntita");
        sqlQuery.addScalar("idAnagraficaEntita");
        sqlQuery.addScalar("denominazione");
        sqlQuery.addScalar("indirizzo");
        sqlQuery.addScalar("nazione");
        sqlQuery.addScalar("livelloAmministrativo1");
        sqlQuery.addScalar("livelloAmministrativo2");
        sqlQuery.addScalar("livelloAmministrativo3");
        sqlQuery.addScalar("livelloAmministrativo4");
        sqlQuery.addScalar("localita");
        sqlQuery.addScalar("cap");

        sqlQuery.addScalar("idSedeEntita");
        sqlQuery.addScalar("codiceSedeEntita");
        sqlQuery.addScalar("descrizioneSedeEntita");
        sqlQuery.addScalar("localitaSedeEntita");
        sqlQuery.addScalar("idZonaGeografica");
        sqlQuery.addScalar("codiceZonaGeografica");
        sqlQuery.addScalar("descrizioneZonaGeografica");

        sqlQuery.addScalar("idDocumento");
        sqlQuery.addScalar("totaleDocumentoInValuta");
        sqlQuery.addScalar("totaleDocumentoInValutaAzienda");
        sqlQuery.addScalar("totaleDocumentoCodiceValuta");
        sqlQuery.addScalar("dataDocumento");
        sqlQuery.addScalar("numeroDocumento");

        sqlQuery.addScalar("idTipoDocumento");
        sqlQuery.addScalar("codiceTipoDocumento");
        sqlQuery.addScalar("descrizioneTipoDocumento");

        sqlQuery.addScalar("idRata");
        sqlQuery.addScalar("versioneRata");
        sqlQuery.addScalar("tipoPartita");
        sqlQuery.addScalar("dataScadenza");
        sqlQuery.addScalar("tipoPagamento");
        sqlQuery.addScalar("importoInValutaAzienda");
        sqlQuery.addScalar("importoInValuta");
        sqlQuery.addScalar("importoTassoDiCambio");
        sqlQuery.addScalar("codiceValuta");
        sqlQuery.addScalar("numeroRata");
        sqlQuery.addScalar("ritenutaAcconto");
        sqlQuery.addScalar("noteRata");

        sqlQuery.addScalar("idRataRiemessa");
        sqlQuery.addScalar("versioneRataRiemessa");
        sqlQuery.addScalar("numeroRataRiemessa");

        sqlQuery.addScalar("giorniLimitiScontoFinanziario");
        sqlQuery.addScalar("dataScadenzaAnticipoFatture");
        sqlQuery.addScalar("percentualeScontoFinanziario");
        sqlQuery.addScalar("idAreaRate");

        sqlQuery.addScalar("idCodicePagamento");
        sqlQuery.addScalar("codiceCodicePagamento");
        sqlQuery.addScalar("descrizioneCodicePagamento");

        sqlQuery.addScalar("importoInValutaRateCollegate", Hibernate.BIG_DECIMAL);
        sqlQuery.addScalar("importoInValutaAziendaRateCollegate", Hibernate.BIG_DECIMAL);

        sqlQuery.addScalar("totalePagatoValuta");
        sqlQuery.addScalar("totalePagatoValuta");
        sqlQuery.addScalar("totalePagatoValutaAzienda");

        sqlQuery.addScalar("maxDataPagamento");
        sqlQuery.addScalar("numPagamenti", Hibernate.INTEGER);
        sqlQuery.addScalar("protocollo", Hibernate.STRING);
        sqlQuery.addScalar("idAreaContabile");
        sqlQuery.addScalar("idAreaContabilePagamenti");

        sqlQuery.addScalar("idAgente", Hibernate.INTEGER);
        sqlQuery.addScalar("codiceAgente", Hibernate.INTEGER);
        sqlQuery.addScalar("denominazioneAgente");

        List<SituazioneRata> result = Collections.emptyList();
        try {
            result = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            logger.error("--> Errore durante il caricamento delle rata.", e);
            throw new RuntimeException("Errore durante il caricamento delle rata.", e);
        }

        EventList<SituazioneRata> eventList = new BasicEventList<SituazioneRata>();
        eventList.addAll(result);
        FilterList<SituazioneRata> rateFiltrate = new FilterList<SituazioneRata>(eventList,
                new Matcher<SituazioneRata>() {

                    @Override
                    public boolean matches(SituazioneRata situazioneRata) {
                        return parametriRicercaRate.getStatiRata().contains(situazioneRata.getStatoRata());
                    }
                });

        result.clear();
        result.addAll(rateFiltrate);
        return result;
    }

    @Override
    public void riemettiRate(RataRiemessa rataRiemessa) {
        logger.debug("--> Enter riemettiRate");

        // ricarico la rata
        Rata rata = caricaRata(rataRiemessa.getId());

        if (!StatoRata.IN_RIASSEGNAZIONE.equals(rata.getStatoRata())
                && !StatoRata.RIEMESSA.equals(rata.getStatoRata())) {
            throw new RuntimeException("Solo una rata  in stato in lavorazione o riemessa più essere riemessa.");
        }
        AreaRate areaRate = areaRateManager.caricaAreaRate(rata.getAreaRate().getId());

        // cerco il numero rata più alto. Visto che sono ordinate per numero
        // rata, prendo l'ultima.
        Integer numeroRata = 1;
        Iterator<Rata> iterator = areaRate.getRate().iterator();
        while (iterator.hasNext()) {
            Rata rata2 = iterator.next();
            numeroRata = rata2.getNumeroRata();
        }

        for (Rata rataDaCreare : rataRiemessa.getRateDaCreare()) {
            numeroRata++;
            Rata nuovaRata = rata.clone();
            nuovaRata.setId(null);
            nuovaRata.setVersion(null);
            nuovaRata.setTipoPagamento(rataDaCreare.getTipoPagamento());
            nuovaRata.getImporto().setImportoInValuta(rataDaCreare.getImporto().getImportoInValuta());
            nuovaRata.getImporto().calcolaImportoValutaAzienda(2);
            nuovaRata.setDataScadenza(rataDaCreare.getDataScadenza());
            nuovaRata.setNumeroRata(numeroRata);
            nuovaRata.setRataRiemessa(rata);
            nuovaRata.setPagamenti(new TreeSet<Pagamento>());

            nuovaRata = salvaRata(nuovaRata);
        }
        logger.debug("--> Exit riemettiRate");
    }

    @Override
    public Rata salvaRata(Rata rata) {
        if (logger.isDebugEnabled()) {
            logger.debug("--> Enter salvaRata " + rata);
        }
        switch (rata.getStatoRata()) {
        case APERTA:
            // se la rata è aperta è sempre possibile modificarla
            break;
        case IN_RIASSEGNAZIONE:
            // la rata è insoluta quindi non la posso modificare
            throw new GenericException("Non è possibile modificare una rata insoluta.");
        default:
            // se la rata è pagata parzialmente è possibile modificare l'importo se uguale o superiore al totale dei
            // pagamenti
            BigDecimal importoRiga = rata.getImporto().getImportoInValutaAzienda();
            BigDecimal totalePagato = rata.getTotalePagato().getImportoInValutaAzienda();
            if (importoRiga.compareTo(totalePagato) < 0) {
                logger.warn(
                        "--> Non è possibile modificare l'importo della rata minore del totale dei pagamenti presenti: "
                                + new DecimalFormat("#,##0.00").format(totalePagato));
                throw new GenericException(
                        "Non è possibile modificare l'importo della rata minore del totale dei pagamenti presenti: "
                                + new DecimalFormat("#,##0.00").format(totalePagato));
            }
        }

        Rata rataSalvata = salvaRataNoCheck(rata);
        if (logger.isDebugEnabled()) {
            logger.debug("--> rata salvata " + rata);
        }
        logger.debug("--> Exit salvaRata");
        return rataSalvata;
    }

    @Override
    public Rata salvaRataNoCheck(Rata rata) {
        logger.debug("--> Enter salvaRataNoCheck");
        Rata rataSalvata;
        try {
            rataSalvata = panjeaDAO.save(rata);
        } catch (Exception e) {
            logger.error("Errore nel update della Rata", e);
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit salvaRataNoCheck");
        return rataSalvata;
    }

}
