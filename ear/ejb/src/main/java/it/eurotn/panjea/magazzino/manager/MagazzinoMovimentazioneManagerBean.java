package it.eurotn.panjea.magazzino.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Properties;

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
import org.hibernate.transform.Transformers;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.AziendaLite;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.manager.interfaces.AziendeManager;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.Giacenza;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzinoLite;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.ESezioneTipoMovimento;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.RigaMagazzinoManager;
import it.eurotn.panjea.magazzino.manager.interfaces.GiacenzaManager;
import it.eurotn.panjea.magazzino.manager.interfaces.MagazzinoMovimentazioneManager;
import it.eurotn.panjea.magazzino.manager.sqlbuilder.MovimentazioneQueryBuilder;
import it.eurotn.panjea.magazzino.util.MovimentazioneArticolo;
import it.eurotn.panjea.magazzino.util.ParametriRicercaMovimentazione;
import it.eurotn.panjea.magazzino.util.ParametriRicercaMovimentazioneArticolo;
import it.eurotn.panjea.magazzino.util.RigaMovimentazione;
import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;
import it.eurotn.util.PanjeaEJBUtil;

/**
 * Manager per la movimentazione articolo.
 *
 * @author fattazzo
 */
@Stateless(name = "Panjea.MagazzinoMovimentazioneManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.MagazzinoMovimentazioneManager")
public class MagazzinoMovimentazioneManagerBean implements MagazzinoMovimentazioneManager {

    private static final Logger LOGGER = Logger.getLogger(MagazzinoMovimentazioneManagerBean.class);

    @Resource
    private SessionContext context;

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private GiacenzaManager giacenzaManager;

    @EJB
    private RigaMagazzinoManager rigaMagazzinoManager;

    @EJB
    private AziendeManager aziendeManager;

    @SuppressWarnings("unchecked")
    @Override
    public List<AreaMagazzino> caricaInventari(Date dataInizio, Date dataFine, DepositoLite depositoLite) {
        LOGGER.debug("-->Enter caricaInventari ");

        List<AreaMagazzino> result = new ArrayList<AreaMagazzino>();

        Query query = panjeaDAO.prepareNamedQuery("AreaMagazzino.caricaInventari");
        query.setParameter("paramCodiceAzienda", getAzienda());
        query.setParameter("paramDataInizio", dataInizio);
        query.setParameter("paramDataFine", dataFine);
        query.setParameter("paramDeposito", depositoLite.getId());

        try {
            result = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            LOGGER.error("--> Errore durante il caricamento degli inventari", e);
            throw new RuntimeException("Errore durante il caricamento degli inventari", e);
        }
        LOGGER.debug("-->Exit caricaInventari ");
        return result;
    }

    @Override
    public AreaMagazzino caricaInventarioUtile(Date data, Deposito deposito) {
        LOGGER.debug("--> Enter caricaInventarioUtile");

        AreaMagazzino inventario = null;

        Query query = panjeaDAO.prepareNamedQuery("AreaMagazzino.caricaInventarioUtile");
        query.setParameter("paramCodiceAzienda", getAzienda());
        query.setParameter("paramData", data);
        query.setParameter("paramDeposito", deposito.getId());
        query.setMaxResults(1);

        try {
            inventario = (AreaMagazzino) panjeaDAO.getSingleResult(query);
        } catch (ObjectNotFoundException e) {
            LOGGER.debug("--> Non esiste nessun inventario utile quindi restituisco null.");
        } catch (DAOException e) {
            LOGGER.error("--> Errore durante il caricamento dell'inventario", e);
            throw new RuntimeException("Errore durante il caricamento dell'inventario", e);
        }
        LOGGER.debug("--> Exit caricaInventarioUtile");
        return inventario;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RigaMovimentazione> caricaMovimentazione(ParametriRicercaMovimentazione parametriRicercaMovimentazione,
            int page, int sizeOfPage) {

        Date dataInizio = PanjeaEJBUtil
                .getDateTimeToZero(parametriRicercaMovimentazione.getPeriodo().getDataIniziale());
        Date dataFine = PanjeaEJBUtil.getDateTimeToZero(parametriRicercaMovimentazione.getPeriodo().getDataFinale());
        ArticoloLite articoloLite = parametriRicercaMovimentazione.getArticoloLite();
        DepositoLite depositoLite = parametriRicercaMovimentazione.getDepositoLite();
        EntitaLite entitaLite = parametriRicercaMovimentazione.getEntitaLite();
        SedeEntita sedeEntita = parametriRicercaMovimentazione.getSedeEntita();
        AgenteLite agenteLite = parametriRicercaMovimentazione.getAgenteLite();
        Collection<ESezioneTipoMovimento> sezioniTipoMovimento = parametriRicercaMovimentazione
                .getSezioniTipoMovimento();
        String noteRiga = parametriRicercaMovimentazione.getNoteRiga();
        String descrizioneRiga = parametriRicercaMovimentazione.getDescrizioneRiga();

        Collection<TipoAreaMagazzino> tipiAreaMagazzino = parametriRicercaMovimentazione.getTipiAreaMagazzino();

        String sqlStringQuery = MovimentazioneQueryBuilder.getSqlMovimentazione(articoloLite, depositoLite, entitaLite,
                dataInizio, dataFine, getAzienda(), false, sezioniTipoMovimento, tipiAreaMagazzino, noteRiga,
                agenteLite, sedeEntita, descrizioneRiga, parametriRicercaMovimentazione.isRigheOmaggio());

        org.hibernate.ejb.QueryImpl queryImpl = (org.hibernate.ejb.QueryImpl) panjeaDAO.getEntityManager()
                .createNativeQuery(sqlStringQuery);
        SQLQuery sqlQuery = (SQLQuery) queryImpl.getHibernateQuery();
        /** calcolo index dei inicio e fine dei dati della pagina richesta **/
        sqlQuery.setFirstResult((page - 1) * sizeOfPage).setMaxResults(sizeOfPage);

        sqlQuery.setResultTransformer(Transformers.aliasToBean(RigaMovimentazione.class));
        List<RigaMovimentazione> righeMovimentazionePage = null;
        try {
            sqlQuery.addScalar("idArticolo");
            sqlQuery.addScalar("codiceArticolo");
            sqlQuery.addScalar("descrizioneArticolo");
            sqlQuery.addScalar("numeroDecimaliPrezzoArticolo");
            sqlQuery.addScalar("um");
            sqlQuery.addScalar("umId");
            sqlQuery.addScalar("idCategoria");
            sqlQuery.addScalar("codiceCategoria");
            sqlQuery.addScalar("descrizioneCategoria");
            sqlQuery.addScalar("idDeposito");
            sqlQuery.addScalar("codiceDeposito");
            sqlQuery.addScalar("descrizioneDeposito");
            sqlQuery.addScalar("areaMagazzinoId");
            sqlQuery.addScalar("dataRegistrazione");
            sqlQuery.addScalar("dataDocumento");
            sqlQuery.addScalar("numeroDocumento", Hibernate.STRING);
            sqlQuery.addScalar("numeroDocumentoOrder", Hibernate.STRING);
            sqlQuery.addScalar("idTipoDocumento");
            sqlQuery.addScalar("codiceTipoDocumento");
            sqlQuery.addScalar("descrizioneTipoDocumento");
            sqlQuery.addScalar("idEntita");
            sqlQuery.addScalar("codiceEntita");
            sqlQuery.addScalar("descrizioneEntita");
            sqlQuery.addScalar("tipoEntita");
            sqlQuery.addScalar("numeroDecimaliPrezzo");
            sqlQuery.addScalar("numeroDecimaliQuantita");
            sqlQuery.addScalar("prezzoUnitario");
            sqlQuery.addScalar("prezzoNetto");
            sqlQuery.addScalar("PrezzoTotale");
            sqlQuery.addScalar("variazione1");
            sqlQuery.addScalar("variazione2");
            sqlQuery.addScalar("variazione3");
            sqlQuery.addScalar("variazione4");
            sqlQuery.addScalar("qtaMagazzinoCaricoTotale");
            sqlQuery.addScalar("qtaMagazzinoScaricoTotale");
            sqlQuery.addScalar("qtaMovimentata");
            sqlQuery.addScalar("importoCaricoTotale");
            sqlQuery.addScalar("importoScaricoTotale");
            sqlQuery.addScalar("importoFatturatoCarico");
            sqlQuery.addScalar("importoFatturatoScarico");
            sqlQuery.addScalar("noteRiga");
            sqlQuery.addScalar("descrizioneRiga");
            sqlQuery.addScalar("importoProvvigione");
            sqlQuery.addScalar("idSedeEntita");
            sqlQuery.addScalar("codiceSedeEntita");
            sqlQuery.addScalar("descrizioneSedeEntita");
            sqlQuery.addScalar("idAzienda");
            sqlQuery.addScalar("codiceAzienda");
            sqlQuery.addScalar("denominazioneAzienda");
            Properties params = new Properties();
            params.put("enumClass", "it.eurotn.panjea.magazzino.domain.omaggio.TipoOmaggio");
            params.put("type", "12");
            sqlQuery.addScalar("tipoOmaggio", Hibernate.custom(org.hibernate.type.EnumType.class, params));
            righeMovimentazionePage = sqlQuery.list();
        } catch (Exception e) {
            LOGGER.error("--> errore in caricaRigheArticoloMovimentazione", e);
            throw new RuntimeException(e);
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("--> Exit caricaMovimentazione " + righeMovimentazionePage.size());
        }
        return righeMovimentazionePage;
    }

    @Override
    public MovimentazioneArticolo caricaMovimentazioneArticolo(
            ParametriRicercaMovimentazioneArticolo parametriRicercaMovimentazioneArticolo) {
        LOGGER.debug("--> Enter caricaMovimentazioneArticolo");

        // Controllo che la data iniziale e finale siano presenti altrimenti rilancio una eccezione
        Periodo periodo = parametriRicercaMovimentazioneArticolo.getPeriodo();
        Calendar cal = Calendar.getInstance();
        if (periodo.getDataFinale() == null) {
            LOGGER.warn("--> Data finale per la movimentazione null, imposto Calendar.getInstance() " + cal);
            periodo.setDataFinale(cal.getTime());
        }
        if (periodo.getDataIniziale() == null) {
            LOGGER.warn(
                    "--> Data iniziale per la movimentazione null, imposto il primo giorno dell'anno corrente " + cal);
            cal.set(cal.get(Calendar.YEAR), 0, 1, 0, 0, 0);
            periodo.setDataIniziale(cal.getTime());
        }

        // Calcolo giacenza precedente
        Date dataSaldoPrecedente = PanjeaEJBUtil
                .getDateTimeToZero(parametriRicercaMovimentazioneArticolo.getPeriodo().getDataIniziale());
        Calendar calendarDataSaldoPrecedente = Calendar.getInstance();
        calendarDataSaldoPrecedente.setTime(dataSaldoPrecedente);
        calendarDataSaldoPrecedente.add(Calendar.DAY_OF_MONTH, -1);
        dataSaldoPrecedente = calendarDataSaldoPrecedente.getTime();

        String codiceAzienda = getAzienda();
        Giacenza giacenzaPrecedente = giacenzaManager.calcoloGiacenza(
                parametriRicercaMovimentazioneArticolo.getArticoloLite(), dataSaldoPrecedente,
                parametriRicercaMovimentazioneArticolo.getDepositoLite(), codiceAzienda);

        // Calcolo giacenza a oggi
        Giacenza giacenzaAttuale = giacenzaManager.calcoloGiacenza(
                parametriRicercaMovimentazioneArticolo.getArticoloLite(), Calendar.getInstance().getTime(),
                parametriRicercaMovimentazioneArticolo.getDepositoLite(), codiceAzienda);

        // Calcolo giacenza finale
        Giacenza giacenzaFinale = giacenzaManager.calcoloGiacenza(
                parametriRicercaMovimentazioneArticolo.getArticoloLite(),
                parametriRicercaMovimentazioneArticolo.getPeriodo().getDataFinale(),
                parametriRicercaMovimentazioneArticolo.getDepositoLite(), codiceAzienda);

        MovimentazioneArticolo movimentazioneArticolo = new MovimentazioneArticolo();
        movimentazioneArticolo.setDeposito(parametriRicercaMovimentazioneArticolo.getDepositoLite());
        movimentazioneArticolo.setGiacenzaPrecedente(giacenzaPrecedente.getGiacenza().doubleValue());
        movimentazioneArticolo.setGiacenzaAttuale(giacenzaAttuale.getGiacenza().doubleValue());
        movimentazioneArticolo.setGiacenzaFinale(giacenzaFinale.getGiacenza().doubleValue());

        List<RigaMovimentazione> righeMovimentazione = caricaRigheArticoloMovimentazione(
                parametriRicercaMovimentazioneArticolo.getArticoloLite(),
                parametriRicercaMovimentazioneArticolo.getPeriodo().getDataIniziale(),
                parametriRicercaMovimentazioneArticolo.getPeriodo().getDataFinale(),
                parametriRicercaMovimentazioneArticolo.getDepositoLite(), getAzienda(),
                parametriRicercaMovimentazioneArticolo.getSezioniTipoMovimento(),
                parametriRicercaMovimentazioneArticolo.getEntitaLite(),
                parametriRicercaMovimentazioneArticolo.getSedeEntita());

        // Calcolo la giacenza progressiva riga per riga
        // Recupero la riga di movimentazione sull'inventario
        List<RigaMovimentazione> righeInventario = caricaRigheMovimentazioneInventario(
                parametriRicercaMovimentazioneArticolo.getArticoloLite().getId(),
                parametriRicercaMovimentazioneArticolo.getPeriodo().getDataIniziale(),
                parametriRicercaMovimentazioneArticolo.getPeriodo().getDataFinale(),
                parametriRicercaMovimentazioneArticolo.getDepositoLite());

        BigDecimal giacenza = BigDecimal.ZERO;
        if (!parametriRicercaMovimentazioneArticolo.isGiacenzaProgressivaInizialeAZero()) {
            giacenza = BigDecimal.valueOf(giacenzaPrecedente.getGiacenza().doubleValue());
        }
        int maxDecimaliPrezzo = 0;
        int maxDecimaliQta = 0;

        List<RigaMovimentazione> righeMovimentazioneInventario = new ArrayList<RigaMovimentazione>();

        for (RigaMovimentazione riga : righeMovimentazione) {

            if (riga.getNumeroDecimaliPrezzo() > maxDecimaliPrezzo) {
                maxDecimaliPrezzo = riga.getNumeroDecimaliPrezzo();
            }
            if (riga.getNumeroDecimaliQuantita() > maxDecimaliQta) {
                maxDecimaliQta = riga.getNumeroDecimaliQuantita();
            }

            List<RigaMovimentazione> listRigheInventariTrovate = new ArrayList<RigaMovimentazione>();

            for (RigaMovimentazione rigaInventario : righeInventario) {
                if (rigaInventario.getDataRegistrazione().compareTo(riga.getDataRegistrazione()) <= 0) {
                    rigaInventario.setGiacenzaProgressiva(rigaInventario.getGiacenza());
                    righeMovimentazioneInventario.add(rigaInventario);
                    // la giacenza diventa quella sull'inventario
                    giacenza = BigDecimal.valueOf(rigaInventario.getGiacenza());
                    listRigheInventariTrovate.add(rigaInventario);
                } else {
                    break;
                }
            }

            righeInventario.removeAll(listRigheInventariTrovate);

            giacenza = giacenza.add(BigDecimal.valueOf(riga.getGiacenza()));
            riga.setGiacenzaProgressiva(giacenza.doubleValue());
            righeMovimentazioneInventario.add(riga);
        }

        // se ho ancora inventari ( quindi con data > dell'ultima riga
        // movimentazione ) li vado ad aggiungere
        if (!righeInventario.isEmpty()) {
            for (RigaMovimentazione rigaInventario : righeInventario) {
                if (rigaInventario.getNumeroDecimaliPrezzo() > maxDecimaliPrezzo) {
                    maxDecimaliPrezzo = rigaInventario.getNumeroDecimaliPrezzo();
                }
                if (rigaInventario.getNumeroDecimaliQuantita() > maxDecimaliQta) {
                    maxDecimaliQta = rigaInventario.getNumeroDecimaliQuantita();
                }

                rigaInventario.setGiacenzaProgressiva(rigaInventario.getGiacenza());
                righeMovimentazioneInventario.add(rigaInventario);
            }
        }

        movimentazioneArticolo.setMaxNumeroDecimaliPrezzo(maxDecimaliPrezzo);
        movimentazioneArticolo.setMaxNumeroDecimaliQta(maxDecimaliQta);
        movimentazioneArticolo.setRigheMovimentazione(righeMovimentazioneInventario);

        return movimentazioneArticolo;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> caricaMovimentazionePerIndiciRotazione(final ArticoloLite articolo,
            final DepositoLite deposito, final Periodo periodo) {
        StringBuilder sb = new StringBuilder(300);
        sb.append("select ");
        sb.append("tipo, data, carico, scarico ");
        sb.append("from ");
        sb.append("( ");
        sb.append("   select ");
        sb.append("   1 as tipo, ");
        sb.append("   dw.dataRegistrazione as data, ");
        sb.append("   dw.qtaMagazzinoCarico + dw.qtaMagazzinoCaricoAltro as carico, ");
        sb.append("   dw.qtaMagazzinoScarico + dw.qtaMagazzinoScaricoAltro as scarico ");
        sb.append("   from dw_movimentimagazzino dw ");
        sb.append(
                "   where dw.dataRegistrazione>=:dataIniziale and dw.dataRegistrazione<=:dataFinale and dw.articolo_id=:idArticolo and dw.deposito_id=:idDeposito ");
        sb.append("   group by dw.dataRegistrazione ");
        sb.append("   union ");
        sb.append("   select ");
        sb.append("   0, am.dataRegistrazione as data, r.qtaMagazzino, 0 ");
        sb.append("   from maga_righe_magazzino r ");
        sb.append("   inner join maga_area_magazzino am on am.id=r.areaMagazzino_id ");
        sb.append(
                "   where am.tipoOperazione=0 and  am.dataRegistrazione>=:dataIniziale and am.dataRegistrazione<=:dataFinale and r.articolo_id=:idArticolo and am.depositoOrigine_id=:idDeposito ");
        sb.append("   group by r.articolo_id,am.id ");
        sb.append(") ");
        sb.append("mov ");
        sb.append("order by data ");

        SQLQuery queryMovimentazione = panjeaDAO.prepareNativeQuery(sb.toString());
        queryMovimentazione.setParameter("dataIniziale", periodo.getDataIniziale());
        queryMovimentazione.setParameter("dataFinale", periodo.getDataFinale());
        queryMovimentazione.setParameter("idDeposito", deposito);
        queryMovimentazione.setParameter("idArticolo", articolo);
        return queryMovimentazione.list();
    }

    /**
     * Carica le righe di movimentazione per un determinato articolo.
     *
     * @param articolo
     *            articolo
     * @param dataInizio
     *            dataInizio
     * @param dataFine
     *            dataFine
     * @param depositoLite
     *            depositoLite
     * @param codiceAzienda
     *            codiceAzienda
     * @param sezioniTipoMovimento
     *            sezioni tipo movimento
     * @param entitaLite
     *            entità
     * @param sedeEntita
     *            sede entità
     * @return righe movimentazione
     */
    @SuppressWarnings("unchecked")
    private List<RigaMovimentazione> caricaRigheArticoloMovimentazione(ArticoloLite articolo, Date dataInizio,
            Date dataFine, DepositoLite depositoLite, String codiceAzienda,
            List<ESezioneTipoMovimento> sezioniTipoMovimento, EntitaLite entitaLite, SedeEntita sedeEntita) {
        LOGGER.debug("--> Enter caricaRigheArticoloMovimentazione");

        String sqlQuery = MovimentazioneQueryBuilder.getSqlMovimentazioneArticolo(articolo, dataInizio, dataFine,
                depositoLite, codiceAzienda, sezioniTipoMovimento, entitaLite, sedeEntita);

        SQLQuery query = ((Session) panjeaDAO.getEntityManager().getDelegate()).createSQLQuery(sqlQuery);
        query.setResultTransformer(Transformers.aliasToBean(RigaMovimentazione.class));
        List<RigaMovimentazione> righeMovimentazione = null;
        try {
            query.addScalar("idArticolo");
            query.addScalar("codiceArticolo");
            query.addScalar("descrizioneArticolo");
            query.addScalar("codiceCategoria");
            query.addScalar("descrizioneCategoria");
            query.addScalar("codiceDeposito");
            query.addScalar("descrizioneDeposito");
            query.addScalar("areaMagazzinoId");
            query.addScalar("dataRegistrazione");
            query.addScalar("dataDocumento");
            query.addScalar("numeroDocumento", Hibernate.STRING);
            query.addScalar("numeroDocumentoOrder", Hibernate.STRING);
            query.addScalar("idTipoDocumento");
            query.addScalar("codiceTipoDocumento");
            query.addScalar("descrizioneTipoDocumento");
            query.addScalar("idEntita");
            query.addScalar("codiceEntita");
            query.addScalar("descrizioneEntita");
            query.addScalar("tipoEntita");
            query.addScalar("numeroDecimaliPrezzo");
            query.addScalar("numeroDecimaliQuantita");
            query.addScalar("prezzoUnitario");
            query.addScalar("prezzoNetto");
            query.addScalar("PrezzoTotale");
            query.addScalar("qtaMagazzinoCaricoTotale");
            query.addScalar("qtaMagazzinoScaricoTotale");
            query.addScalar("idSedeEntita");
            query.addScalar("codiceSedeEntita");
            query.addScalar("descrizioneSedeEntita");
            query.addScalar("um", Hibernate.STRING);
            query.addScalar("tipoOperazione", Hibernate.INTEGER);
            query.addScalar("idAzienda");
            query.addScalar("codiceAzienda");
            query.addScalar("denominazioneAzienda");
            righeMovimentazione = query.list();
        } catch (Exception e) {
            LOGGER.error("--> errore in caricaRigheArticoloMovimentazione", e);
            throw new RuntimeException(e);
        }

        LOGGER.debug("--> Exit caricaRigheArticoloMovimentazione");
        return righeMovimentazione;
    }

    /**
     * Carica gli inventari nel periodo e costruisce una riga per ogni inventario sommando la qta di Magazzino.
     *
     * @param idArticolo
     *            idArticolo
     * @param dataInizio
     *            dataInizio
     * @param dataFine
     *            dataFine
     * @param depositoLite
     *            depositoLite
     * @return List<RigaMovimentazione> generata dall'inventario. NULL se non ho nessuna riga
     */
    private List<RigaMovimentazione> caricaRigheMovimentazioneInventario(Integer idArticolo, Date dataInizio,
            Date dataFine, DepositoLite depositoLite) {
        // Devo inserire l'inventario utile nei movimenti.
        // Calcolo l'inventario utile alla dataIniziale
        List<AreaMagazzino> inventari = caricaInventari(dataInizio, dataFine, depositoLite);
        RigaMovimentazione rigaMovimentazione = null;

        List<RigaMovimentazione> righeMovimentazione = new ArrayList<RigaMovimentazione>();

        AziendaLite azienda = aziendeManager.caricaAzienda();

        for (AreaMagazzino inventario : inventari) {
            // Carico le righe per l'articolo movimentato, se non ce ne sono ne
            // creo una a quantità a zero perchè
            // l'inventario deve avere la lista di tutti gli articoli.
            List<RigaMagazzino> righeMagazzinoInventario = rigaMagazzinoManager.getDao()
                    .caricaRigheMagazzino(inventario, idArticolo);
            if (!righeMagazzinoInventario.isEmpty()) {
                rigaMovimentazione = new RigaMovimentazione((RigaArticolo) righeMagazzinoInventario.get(0));
                rigaMovimentazione.setQtaMagazzinoCaricoTotale(0.0);
                rigaMovimentazione.setQtaMagazzinoCaricoTotale(0.0);
                for (RigaMagazzino rigaMagazzino : righeMagazzinoInventario) {
                    rigaMovimentazione
                            .aggiungiQtaMagazzinoCaricoTotale(((RigaArticolo) rigaMagazzino).getQtaMagazzino());
                    // prendo il prezzo dell'ultima riga che trovo
                    rigaMovimentazione.setPrezzoNetto(
                            ((RigaArticolo) rigaMagazzino).getPrezzoNetto().getImportoInValutaAzienda());
                    rigaMovimentazione.setPrezzoUnitario(
                            ((RigaArticolo) rigaMagazzino).getPrezzoNetto().getImportoInValutaAzienda());
                    rigaMovimentazione.setPrezzoTotale(
                            ((RigaArticolo) rigaMagazzino).getPrezzoNetto().getImportoInValutaAzienda());
                }
            } else {
                // se non ho righe per l'articolo devo comunque caricarmelo per
                // crearmi la riga movimentazione
                ArticoloLite articoloLite = null;
                try {
                    articoloLite = panjeaDAO.load(ArticoloLite.class, idArticolo);
                } catch (ObjectNotFoundException e) {
                    LOGGER.error("-->errore nel caricare l'articolo lite con id " + idArticolo, e);
                    throw new RuntimeException(e);
                }

                RigaArticolo rigaArticolo = new RigaArticolo();
                rigaArticolo.setAreaMagazzino(inventario);
                rigaArticolo.setArticolo(articoloLite);
                rigaArticolo.setQtaMagazzino(0.0);
                rigaArticolo.setNumeroDecimaliQta(articoloLite.getNumeroDecimaliQta());
                rigaArticolo.setNumeroDecimaliPrezzo(articoloLite.getNumeroDecimaliPrezzo());
                rigaMovimentazione = new RigaMovimentazione(rigaArticolo);
            }

            rigaMovimentazione.setIdAzienda(azienda.getId());
            rigaMovimentazione.setCodiceAzienda(azienda.getCodice());
            rigaMovimentazione.setDenominazioneAzienda(azienda.getDenominazione());
            righeMovimentazione.add(rigaMovimentazione);
        }
        return righeMovimentazione;
    }

    @Override
    public AreaMagazzinoLite caricaUltimoInventario(Integer idDeposito) {
        LOGGER.debug("--> Enter caricaUltimoInventario");

        Query query = panjeaDAO.prepareNamedQuery("AreaMagazzino.caricaUltimoInventario");
        query.setParameter("paramIdDeposito", idDeposito);
        query.setMaxResults(1);

        AreaMagazzinoLite areaMagazzinoLite = null;
        try {
            areaMagazzinoLite = (AreaMagazzinoLite) panjeaDAO.getSingleResult(query);
        } catch (ObjectNotFoundException e1) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("--> non esiste l'inventario, lascio l'area a null");
            }
        } catch (DAOException e) {
            LOGGER.error("--> errore durante il caricamento dell'ultimo inventario per il deposito " + idDeposito, e);
            throw new RuntimeException(
                    "errore durante il caricamento dell'ultimo inventario per il deposito " + idDeposito, e);
        }

        LOGGER.debug("--> Exit caricaUltimoInventario");
        return areaMagazzinoLite;
    }

    /**
     *
     * @return codice Azienda loggata
     */
    private String getAzienda() {
        JecPrincipal jecPrincipal = (JecPrincipal) context.getCallerPrincipal();
        return jecPrincipal.getCodiceAzienda();
    }
}
