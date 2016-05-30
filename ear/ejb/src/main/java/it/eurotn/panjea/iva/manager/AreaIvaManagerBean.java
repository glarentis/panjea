package it.eurotn.panjea.iva.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.ejb.QueryImpl;
import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile.GestioneIva;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileVerificaManager;
import it.eurotn.panjea.contabilita.manager.interfaces.TipiAreaContabileManager;
import it.eurotn.panjea.contabilita.manager.ritenutaacconto.interfaces.RitenutaAccontoContabilitaManager;
import it.eurotn.panjea.contabilita.service.exception.CodiceIvaCollegatoAssenteException;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.iva.domain.AreaIva;
import it.eurotn.panjea.iva.domain.RigaIva;
import it.eurotn.panjea.iva.manager.interfaces.AreaIvaCancellaManager;
import it.eurotn.panjea.iva.manager.interfaces.AreaIvaManager;
import it.eurotn.panjea.iva.service.exception.IvaException;
import it.eurotn.panjea.iva.util.IImponibiliIvaQueryExecutor;
import it.eurotn.panjea.iva.util.RigaIvaRicercaDTO;
import it.eurotn.panjea.iva.util.parametriricerca.ParametriRicercaRigheIva;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoManager;
import it.eurotn.panjea.magazzino.manager.documento.totalizzatore.StrategiaTotalizzazione;
import it.eurotn.panjea.magazzino.util.queryExecutor.ITotalizzatoriQueryExecutor;
import it.eurotn.panjea.partite.domain.AreaPartite;
import it.eurotn.panjea.partite.domain.TipoAreaPartita.TipoOperazione;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rate.manager.interfaces.AreaRateManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

/**
 * Manager per gestione Area Iva List roles: cancellaAreaIva, visualizzaAreaIva, modificaAreaIva.
 *
 * @author adriano
 * @version 1.0, 31/ago/07
 */
@Stateless(name = "Panjea.AreaIvaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaIvaManager")
public class AreaIvaManagerBean implements AreaIvaManager {

    private static Logger logger = Logger.getLogger(AreaIvaManagerBean.class);

    @Resource
    private SessionContext sessionContext;

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private TipiAreaContabileManager tipiAreaContabileManager;
    @EJB
    @IgnoreDependency
    private AreaMagazzinoManager areaMagazzinoManager;

    @EJB
    private AreaContabileVerificaManager areaContabileVerificaManager;
    @EJB
    @IgnoreDependency
    private AreaRateManager areaRateManager;

    @IgnoreDependency
    @EJB
    private AreaIvaCancellaManager areaIvaCancellaManager;

    @EJB
    private IImponibiliIvaQueryExecutor imponibiliIvaQueryExecutor;

    @EJB
    private ITotalizzatoriQueryExecutor totalizzatoriQueryExecutor;

    @EJB
    private StrategiaTotalizzazione strategiaTotalizzazione;

    @EJB
    @IgnoreDependency
    private RitenutaAccontoContabilitaManager ritenutaAccontoContabilitaManager;

    @Override
    public AreaIva associaAreaContabile(AreaIva areaIva, AreaContabile areaContabile) {
        logger.debug("--> Enter associaAreaContabile");

        areaIva.setAreaContabile(areaContabile);

        try {
            areaIva = panjeaDAO.save(areaIva);
        } catch (Exception ex) {
            logger.error("--> Errore durante il salvataggio dell'area iva", ex);
            throw new RuntimeException("Errore durante il salvataggio dell'area iva", ex);
        }

        logger.debug("--> Exit associaAreaContabile");
        return areaIva;
    }

    @Override
    public AreaIva caricaAreaIva(AreaContabile areaContabile) {
        return caricaAreaIvaByDocumento(areaContabile.getDocumento());
    }

    @Override
    public AreaIva caricaAreaIva(AreaIva areaIva) {
        try {
            areaIva = panjeaDAO.load(AreaIva.class, areaIva.getId());
            // inizializzo l'area contabile e il documento che sono lazy
            if (areaIva.getAreaContabile() != null) {
                areaIva.getAreaContabile().getVersion();
            }
            areaIva.getDocumento().getVersion();
        } catch (Exception ex) {
            logger.error("--> Errore nel caricare l'areaIva " + areaIva, ex);
            throw new RuntimeException(ex);
        }
        return areaIva;
    }

    @Override
    public AreaIva caricaAreaIvaByDocumento(Documento documento) {
        logger.debug("--> Enter caricaAreaIvaByDocumento");
        Query query = panjeaDAO.prepareNamedQuery("AreaIva.ricercaAreaByDocumento");
        query.setParameter("paramIdDocumento", documento.getId());
        AreaIva areaIva = null;
        try {
            areaIva = (AreaIva) panjeaDAO.getSingleResult(query);
            // inizializzo l'area contabile e il documento che sono lazy
            if (areaIva.getAreaContabile() != null) {
                areaIva.getAreaContabile().getVersion();
            }
            areaIva.getDocumento().getVersion();
        } catch (ObjectNotFoundException ex) {
            logger.warn("--> Non esiste un' areaIva per il documento " + documento.getId());
            areaIva = new AreaIva();
        } catch (DAOException ex) {
            logger.error("--> Errore nel caricare l'areaIva per il documento " + documento.getId(),
                    ex);
            throw new RuntimeException(ex);
        }
        logger.debug("--> Exit caricaAreaIvaByDocumento");
        return areaIva;
    }

    @Override
    public RigaIva caricaRigaIva(Integer id) throws IvaException {
        logger.debug("--> Enter caricaRigaIva");
        try {
            RigaIva rigaIva = panjeaDAO.load(RigaIva.class, id);
            logger.debug("--> Exit caricaRigaIva");
            return rigaIva;
        } catch (ObjectNotFoundException e) {
            logger.error("--> errore impossibile recuperare RigaIva identificata da " + id, e);
            throw new IvaException("Impossibile recuperare RigaIva identificata da " + id, e);
        }
    }

    /**
     * Per un tipo documento con gestione iva alternativa (INTRA o ART.17) verifica se il codice iva
     * associato alla riga possiede un codice iva collegato,avvisando il sistema in caso negativo.
     *
     * @param rigaIva
     *            la riga di cui verificare codice iva e tipo gestione iva
     * @param tipoAreaContabile
     *            tipo area contabile
     * @throws CodiceIvaCollegatoAssenteException
     *             viene lanciata nel caso in cui il tipo documento ha gestione iva alternativa e il
     *             codice iva scelto per la riga non ha associato un codice iva collegato
     */
    private void checkCodiceIvaCollegatoIfGestioneIvaAlternativa(RigaIva rigaIva,
            TipoAreaContabile tipoAreaContabile) throws CodiceIvaCollegatoAssenteException {
        // se la riga iva e' associata ad un documento di tipo INTRA o ART.17 allora verifico se il
        // codice iva collegato
        // e' valorizzato

        if (GestioneIva.ART17.equals(tipoAreaContabile.getGestioneIva())
                || GestioneIva.INTRA.equals(tipoAreaContabile.getGestioneIva())) {
            // se il codice iva collegato non e' settato non posso salvare la riga per il documento
            if (rigaIva.getCodiceIvaCollegato() == null) {
                logger.debug("--> Il codice iva collegato non e' impostato per il codice iva");
                throw new CodiceIvaCollegatoAssenteException();
            }
        }
    }

    @Override
    public void checkInvalidaAreeCollegate(AreaIva areaIva) {
        logger.debug("--> Enter checkAreeCollegate");

        if (areaIva.getDatiValidazioneRighe().isValid()) {
            areaIva = invalidaAreaIva(areaIva);
        }

        AreaContabile areaContabile = areaIva.getAreaContabile();
        if (areaContabile != null) {
            // se lo stato e' confermato ed ho eseguito una modifica (salvataggio o cancellazione)
            // allora devo
            // invalidare il registro iva in cui ricade l'areaContabile
            if (areaContabile.getStatoAreaContabile().equals(StatoAreaContabile.CONFERMATO)) {
                areaContabileVerificaManager.invalidaRegistroIva(null, areaContabile);
            }

            // se e' in stato confermato lo porto in provvisorio tramite il parametro del metodo
            // invalidaAreaContabile
            // altrimenti invalido solo la parte contabile
            if (areaContabile.isValidRigheContabili()) {
                areaContabile = areaContabileVerificaManager.invalidaAreaContabile(areaContabile,
                        areaContabile.getStatoAreaContabile()
                                .equals(StatoAreaContabile.CONFERMATO));
            }
        } else {
            // Se l'area contabile è presente non è possibile che la riga iva sia stata salvata dal
            // magazzino. Quindi se
            // non c'è l'area contabile è stata salvata dal magazzino. Porto a provvisorio il
            // magazzino
            AreaMagazzino areaMagazzino = areaMagazzinoManager
                    .caricaAreaMagazzinoByDocumento(areaIva.getDocumento());
            if (areaMagazzino.getId() != null) {
                // Devo cambiare lo stato dell'area magazzino e non invalidare l'area. Il metodo
                // InvalidaAreaMagazzino
                // cambia lo stato ed invalida anche le righe. Qui devo solamente cambiare lo stato
                areaMagazzinoManager.cambiaStatoInProvvisorio(areaMagazzino);
            } else {
                logger.warn("--> Area magazzino non presente. Qualcosa non torna!!!");
            }
        }

        // se l'areaPartite e' presente mi preoccupo di invalidarla
        AreaRate areaRate = areaRateManager.caricaAreaRate(areaIva.getDocumento());
        if (areaRate != null && areaRate.getId() != null) {
            areaRate.getDatiValidazione().invalida();
            areaRateManager.salvaAreaRate(areaRate);
        }

        logger.debug("--> Exit checkAreeCollegate");
    }

    @Override
    public AreaIva creaAreaIva(AreaContabile areaContabile) {
        logger.debug("--> Enter creaAreaIva");
        AreaIva areaIva = creaAreaIva(areaContabile.getDocumento(), areaContabile);
        logger.debug("--> Exit creaAreaIva");
        return areaIva;
    }

    @Override
    public AreaIva creaAreaIva(Documento documento, AreaContabile areaContabile) {
        logger.debug("--> Enter creaAreaIva");
        AreaIva areaIva = new AreaIva();
        areaIva.setAreaContabile(areaContabile);
        areaIva.setRigheIva(new ArrayList<RigaIva>());
        areaIva.setDocumento(documento);
        areaIva = updateRegistroAreaIva(areaIva, areaContabile);
        logger.debug("--> Exit creaAreaIva");
        return areaIva;
    }

    /**
     * Crea e restituisce {@link RigaIva} inizializzata con gli argomenti areaIva, codiceIva e
     * imponibile. <br>
     * viene restituita quindi un istanza di rigaIva con gia' calcolato il valore dell'imposta
     *
     * @param areaIva
     *            area iva
     * @param codiceIva
     *            codice iva
     * @param imponibile
     *            imponibile
     * @return riga iva creata
     */
    private RigaIva creaRigaIva(AreaIva areaIva, CodiceIva codiceIva, Importo imponibile) {
        logger.debug("--> Enter creaRigaIva");
        RigaIva rigaIva = new RigaIva();
        rigaIva.setImponibile(imponibile);
        rigaIva.setCodiceIva(codiceIva);
        rigaIva.setCodiceIvaCollegato(codiceIva.getCodiceIvaCollegato());
        rigaIva.setAreaIva(areaIva);
        logger.debug("--> Exit creaRigaIva");
        return rigaIva;

    }

    @Override
    public AreaIva generaAreaIvaDaMagazzino(AreaMagazzino areaMagazzino, AreaPartite areaPartite) {
        logger.debug("--> Enter generaAreaIvaDaMagazzino");

        imponibiliIvaQueryExecutor.setAreaDocumento(areaMagazzino);
        imponibiliIvaQueryExecutor.setQueryString("RigaArticolo.caricaImponibiliIva");

        // MAIL NPE vediamo tra areamagazzino, documento e tipodocumento chi risulta essere null
        AreaIva areaIva = null;
        Documento doc = areaMagazzino.getDocumento();
        TipoDocumento td = doc.getTipoDocumento();
        if (td.isRigheIvaEnable()) {
            // verifica esitenza di AreaIva
            areaIva = caricaAreaIvaByDocumento(areaMagazzino.getDocumento());
            if (areaIva.isNew()) {
                logger.debug("--> area iva non trovata: crea AreaIva ");
                // creazione di AreaIva senza l'assegnazione di AreaContabile:
                // se non esiste AreaIva per il Documento corrente si deduce che non esisterà
                // nemmeno l'areaContabile.
                // Se questa esistesse avrebbe creato lei stessa l'AreaIva
                areaIva = creaAreaIva(areaMagazzino.getDocumento(), null);
                logger.debug("--> area iva creata : " + areaIva);
            } else {
                logger.debug("--> area iva trovata " + areaIva + " : elimina Righe Iva ");
                // eliminazione delle righe iva esistenti
                areaIvaCancellaManager.cancellaRigheIva(areaIva);
                areaIva.setRigheIva(new ArrayList<RigaIva>());
                logger.debug("--> righe iva eliminate ");
            }
            // generazione di RigheIva
            logger.debug("--> genera righe iva ");
            List<RigaIva> righeIva = generaRigheIva(imponibiliIvaQueryExecutor,
                    areaMagazzino.getDocumento(), areaMagazzino.isAddebitoSpeseIncasso(), areaIva,
                    areaPartite);
            areaIva.setRigheIva(righeIva);
            try {
                areaIva = panjeaDAO.save(areaIva);
            } catch (Exception e) {
                logger.error("--> errore nel salvare l'area Iva ", e);
                throw new RuntimeException(e);
            }
        } else {
            areaIva = new AreaIva();
            // Non ho areaIva quindi genero le righe iva senza salvarle
            // per utilizzarle in totalizzaDocumento. (imponibile + imposta)
            List<RigaIva> righeIva = generaRigheIva(imponibiliIvaQueryExecutor,
                    areaMagazzino.getDocumento(), areaMagazzino.isAddebitoSpeseIncasso(), null,
                    areaPartite);
            areaIva.setRigheIva(righeIva);
        }
        logger.debug("--> Exit generaAreaIvaDaMagazzino");
        return areaIva;
    }

    @Override
    public List<RigaIva> generaRigheIva(IImponibiliIvaQueryExecutor executor, Documento documento,
            boolean addebitoSpeseIncasso, AreaIva areaIva, AreaPartite areaPartite) {
        logger.debug("--> Enter caricaImportiIva");

        // Query query = panjeaDAO.prepareNamedQuery("RigaArticolo.caricaImponibiliIva");
        // query.setParameter("paramAreaMagazzino", areaMagazzino);
        // // la query restituisce una List, i suoi elementi sono array di object di due elementi:
        // // il primo e' la sommatoria del totale riga raggruppato per codice iva
        // // ed e' di tipo BigDecimal, il secondo e' il codiceIva a cui fa riferimento
        // List<Object[]> aggregatiIva = query.getResultList();

        List<Object[]> aggregatiIva = executor.execute();
        logger.debug("--> aggregati iva trovati " + aggregatiIva.size());
        List<RigaIva> righeIva = new ArrayList<RigaIva>();
        BigDecimal imponibileValuta;

        CodiceIva codiceIva;
        RigaIva rigaIva;
        Importo imponibile;

        // Carico il tipo area contabile se presente. Questo perchè devo verificare se ho un INTRA o
        // ART 17 e manca un
        // codice iva collegato
        TipoAreaContabile tipoAreaContabile = null;
        try {
            tipoAreaContabile = tipiAreaContabileManager
                    .caricaTipoAreaContabilePerTipoDocumento(documento.getTipoDocumento().getId());
        } catch (ContabilitaException ce) {
            logger.error("--> errore nel caricare il tipoAreaContabile per il documento "
                    + documento.getTipoDocumento().getId(), ce);
        }

        // AreaPartite ap =
        // areaPartiteManager.caricaAreaPartiteByDocumento(areaMagazzino.getDocumento());
        // Se ho l'area partite allora devo verificare se genera partite, se le genera allora
        // verifico se il flag di
        // addebito è abilitato
        if (areaPartite != null && areaPartite.getId() != null
                && areaPartite.getTipoAreaPartita().getTipoOperazione() == TipoOperazione.GENERA
                && addebitoSpeseIncasso) {
            // ciclo sull'iva aggregata e ottendo l'importo maggiore per poi aggiungere le spese

            int indexMaxImporto = 0;// Indice dell'elemento con l'importo maggiore

            BigDecimal importoMaggiore = BigDecimal.ZERO;
            int index = 0;
            for (Object[] aggreatoIva : aggregatiIva) {
                if (importoMaggiore.compareTo((BigDecimal) aggreatoIva[0]) < 0) {
                    importoMaggiore = (BigDecimal) aggreatoIva[0];
                    indexMaxImporto = index;
                }
                index++;
            }
            // Prendo l'elemento con imponibile più alto e aggiungo le spese di pagamento.
            // Le spese le considero nella valuta del documento. Quindi se faccio un documento in $
            // avrò una condizione
            // di pagamento specifica
            if (areaPartite.getCodicePagamento() != null && areaPartite.getSpeseIncasso() != null
                    && areaPartite.getSpeseIncasso().compareTo(BigDecimal.ZERO) != 0
                    && aggregatiIva.size() > 0) {
                BigDecimal nuovoImporto = ((BigDecimal) aggregatiIva.get(indexMaxImporto)[0])
                        .add(areaPartite.getSpeseIncasso());
                aggregatiIva.get(indexMaxImporto)[0] = nuovoImporto;
            }
        }

        for (Object[] aggreatoIva : aggregatiIva) {
            logger.debug("--> Aggregato iva considerato " + aggreatoIva[0]);
            imponibileValuta = (BigDecimal) aggreatoIva[0];
            codiceIva = (CodiceIva) aggreatoIva[2];
            imponibile = new Importo(documento.getTotale().getCodiceValuta(),
                    documento.getTotale().getTassoDiCambio());
            imponibile.setImportoInValuta(imponibileValuta);
            imponibile.calcolaImportoValutaAzienda(2);
            if (documento.getTipoDocumento().isNotaCreditoEnable()) {
                imponibile = imponibile.negate();
            }
            rigaIva = creaRigaIva(areaIva, codiceIva, imponibile);
            try {
                if (areaIva != null) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("--> Versione rigaIva " + rigaIva.getVersion());
                        logger.debug("--> Versione areaIva " + areaIva.getVersion());
                        logger.debug("--> Versione areaIva " + rigaIva.getAreaIva().getVersion());
                    }
                    rigaIva = salvaRigaIvaNoCkeck(rigaIva, tipoAreaContabile);
                    if (logger.isDebugEnabled()) {
                        logger.debug("--> Versione rigaIva " + rigaIva.getVersion());
                        logger.debug("--> Versione areaIva " + areaIva.getVersion());
                        logger.debug("--> Versione areaIva " + rigaIva.getAreaIva().getVersion());
                    }
                }
            } catch (CodiceIvaCollegatoAssenteException e) {
                logger.error("--> errore CodiceIvaCollegatoAssenteException in generaRigheIva", e);
                throw new RuntimeException(e);
            }
            righeIva.add(rigaIva);
        }
        logger.debug("--> Exit generaRigheIva " + righeIva.size());
        return righeIva;
    }

    @Override
    public List<RigaIva> generaRigheIvaRiepilogo(AreaMagazzino areaMagazzino,
            AreaPartite areaPartite) {
        imponibiliIvaQueryExecutor.setAreaDocumento(areaMagazzino);
        imponibiliIvaQueryExecutor.setQueryString("RigaArticolo.caricaImponibiliIva");
        return generaRigheIva(imponibiliIvaQueryExecutor, areaMagazzino.getDocumento(),
                areaMagazzino.isAddebitoSpeseIncasso(), null, areaPartite);
    }

    /**
     * @return principal loggato
     */
    private JecPrincipal getPrincipal() {
        return (JecPrincipal) sessionContext.getCallerPrincipal();
    }

    @Override
    public AreaIva invalidaAreaIva(AreaIva areaIva) {
        logger.debug("--> Enter invalidaAreaIva");
        areaIva.getDatiValidazioneRighe().invalida();
        try {
            panjeaDAO.save(areaIva);
        } catch (Exception e) {
            logger.error("--> errore in invalidaAreaIva", e);
            throw new RuntimeException("errore in invalidaAreaIva", e);
        }
        logger.debug("--> Exit invalidaAreaIva");
        return areaIva;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RigaIvaRicercaDTO> ricercaRigheIva(ParametriRicercaRigheIva parametri) {
        logger.debug("--> Enter ricercaRigheIva");

        List<RigaIvaRicercaDTO> righeIva = new ArrayList<RigaIvaRicercaDTO>();

        StringBuilder sb = new StringBuilder(1000);
        sb.append(
                "select doc.id as idDocumento,doc.codice as codiceDocumento,doc.totale as totaleDocumento,doc.dataDocumento as dataDocumento, ");
        sb.append("         ac.dataRegistrazione as dataRegistrazione,");
        sb.append(
                "		   tipoDoc.tipoEntita as tipoEntita,doc.entita.id as idEntita,doc.entita.codice as codiceEntita,doc.entita.anagrafica.denominazione as denominazioneEntita, ");
        sb.append(
                "		   tipoDoc.id as idTipoDocumento, tipoDoc.codice as codiceTipoDocumento,tipoDoc.descrizione as descrizioneTipoDocumento, ");
        sb.append(
                "		   ai.registroIva.id as idRegistroIva, ai.registroIva.descrizione as descrizioneRegistroIva,ai.registroIva.numero as numeroRegistroIva, ");
        sb.append(
                "		   ri.codiceIva.id as idCodiceIva,ri.codiceIva.codice as codiceCodiceIva,ri.codiceIva.descrizioneInterna as descrizioneCodiceIva, ");
        sb.append("		   ri.imponibile as imponibileRiga,ri.imposta as impostaRiga ");
        sb.append(
                "from RigaIva ri inner join ri.areaIva ai inner join ai.documento doc inner join doc.tipoDocumento tipoDoc inner join doc.entita ent, AreaContabile ac ");
        sb.append("where ac.documento = doc and ac.documento.codiceAzienda = :paramCodiceAzienda ");
        if (parametri.getAnnoCompetenza() != null && parametri.getAnnoCompetenza() != -1) {
            sb.append(" and ac.annoMovimento=:annoCompetenza ");
        }
        if (parametri.getDataDocumento().getDataIniziale() != null) {
            sb.append(" and doc.dataDocumento >= :dataDocumentoIniziale ");
        }
        if (parametri.getDataDocumento().getDataFinale() != null) {
            sb.append(" and doc.dataDocumento <= :dataDocumentoFinale ");
        }
        if (parametri.getDataRegistrazione().getDataIniziale() != null) {
            sb.append(" and ac.dataRegistrazione >= :dataRegistrazioneIniziale ");
        }
        if (parametri.getDataRegistrazione().getDataFinale() != null) {
            sb.append(" and ac.dataRegistrazione <= :dataRegistrazioneFinale ");
        }
        if (parametri.getCodiceIva() != null && !parametri.getCodiceIva().isNew()) {
            sb.append(" and ri.codiceIva = :codiceIva ");
        }
        if (parametri.getEntita() != null && !parametri.getEntita().isNew()) {
            sb.append(" and (ac.documento.entita = :entita) ");
        }
        if (parametri.getRegistroIva() != null && !parametri.getRegistroIva().isNew()) {
            sb.append(" and ai.registroIva = :registroIva ");
        }
        if (parametri.getTipiDocumento() != null && !parametri.getTipiDocumento().isEmpty()) {
            sb.append(" and (tipoDoc in (:tipiDocumento)) ");
        }

        Query query = panjeaDAO.prepareQuery(sb.toString(), RigaIvaRicercaDTO.class, null);
        query.setParameter("paramCodiceAzienda", getPrincipal().getCodiceAzienda());
        ((QueryImpl) query).getHibernateQuery().setProperties(parametri);
        if (parametri.getDataDocumento().getDataIniziale() != null) {
            query.setParameter("dataDocumentoIniziale",
                    parametri.getDataDocumento().getDataIniziale());
        }
        if (parametri.getDataDocumento().getDataFinale() != null) {
            query.setParameter("dataDocumentoFinale", parametri.getDataDocumento().getDataFinale());
        }
        if (parametri.getDataRegistrazione().getDataIniziale() != null) {
            query.setParameter("dataRegistrazioneIniziale",
                    parametri.getDataRegistrazione().getDataIniziale());
        }
        if (parametri.getDataRegistrazione().getDataFinale() != null) {
            query.setParameter("dataRegistrazioneFinale",
                    parametri.getDataRegistrazione().getDataFinale());
        }

        try {
            righeIva = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            logger.error("--> errore durante la ricerca delle aree iva.", e);
            throw new RuntimeException("errore durante la ricerca delle aree iva.", e);
        }

        logger.debug("--> Exit ricercaRigheIva");
        return righeIva;
    }

    @Override
    public AreaIva salvaAreaIva(AreaIva areaIva) {
        logger.debug("--> Enter salvaAreaIva");
        try {
            areaIva = panjeaDAO.save(areaIva);
        } catch (Exception e) {
            logger.error("--> errore in invalidaAreaIva", e);
            throw new RuntimeException("errore in invalidaAreaIva", e);
        }
        logger.debug("--> Exit salvaAreaIva");
        return areaIva;
    }

    @Override
    public RigaIva salvaRigaIva(RigaIva rigaIva, TipoAreaContabile tipoAreaContabile)
            throws CodiceIvaCollegatoAssenteException {
        logger.debug("--> Enter salvaRigaIva");
        if (tipoAreaContabile != null) {
            // verifico se il codice iva collegato esiste nel caso in cui inserisco un documento
            // intra o art17
            checkCodiceIvaCollegatoIfGestioneIvaAlternativa(rigaIva, tipoAreaContabile);
        }
        try {
            RigaIva rigaIvaSave = panjeaDAO.save(rigaIva);
            checkInvalidaAreeCollegate(rigaIvaSave.getAreaIva());

            // tolgo imponibile e imposta visualizzata per avere i valori esatti al primo get
            // rigaIvaSave.calcolaImposta();
            // rigaIvaSave.calcolaImpostaCollegata();
            logger.debug("--> Exit salvaRigaIva");
            return rigaIvaSave;
        } catch (Exception e) {
            logger.error("--> errore in salvataggio riga iva ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public RigaIva salvaRigaIvaNoCkeck(RigaIva rigaIva, TipoAreaContabile tipoAreaContabile)
            throws CodiceIvaCollegatoAssenteException {
        if (tipoAreaContabile != null) {
            // verifico se il codice iva collegato esiste nel caso in cui inserisco un documento
            // intra o art17
            checkCodiceIvaCollegatoIfGestioneIvaAlternativa(rigaIva, tipoAreaContabile);
        }
        try {
            RigaIva rigaIvaSave = panjeaDAO.save(rigaIva);
            logger.debug("--> Exit salvaRigaIva");
            return rigaIvaSave;
        } catch (Exception e) {
            logger.error("--> errore in salvataggio riga iva ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public AreaIva updateRegistroAreaIva(AreaIva areaIva, AreaContabile areaContabile) {
        if (areaContabile != null) {
            // inizializzazione di registroIva e registroIvaCollegato
            areaIva.setRegistroIva(areaContabile.getTipoAreaContabile().getRegistroIva());
            areaIva.setRegistroIvaCollegato(
                    areaContabile.getTipoAreaContabile().getRegistroIvaCollegato());
            // fine inizializzazione di registroIva e registroIvaCollegato
        }
        try {
            areaIva = panjeaDAO.saveWithoutFlush(areaIva);
        } catch (Exception ex) {
            logger.error("--> Errore durante la creazione del'area iva.", ex);
            throw new RuntimeException("Errore durante la creazione del'area iva.", ex);
        }
        return areaIva;
    }

    @Override
    public AreaIva validaAreaIva(AreaIva areaIva) {
        logger.debug("--> Enter validaRigheIva");
        areaIva.getDatiValidazioneRighe().valida(getPrincipal().getUserName());
        try {
            areaIva = panjeaDAO.save(areaIva);
        } catch (Exception e) {
            logger.error("--> errore in invalidaAreaIva", e);
            throw new RuntimeException("errore in invalidaAreaIva", e);
        }

        // aggiorno il valore dell'imponibile per il calcolo della ritenuta d'acconto
        ritenutaAccontoContabilitaManager
                .aggiornaImponibileDatiRitenutaAcconto(areaIva.getDocumento().getId());

        logger.debug("--> Exit validaRigheIva");
        return areaIva;
    }
}
