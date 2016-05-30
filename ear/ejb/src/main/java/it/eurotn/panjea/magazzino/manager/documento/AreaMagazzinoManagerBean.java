package it.eurotn.panjea.magazzino.manager.documento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.ejb.QueryImpl;
import org.hibernate.transform.Transformers;
import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.manager.interfaces.DocumentiManager;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentoDuplicateException;
import it.eurotn.panjea.anagrafica.domain.Cliente;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.ConversioneUnitaMisura;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioSedeEntita;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.domain.lite.VettoreLite;
import it.eurotn.panjea.anagrafica.manager.interfaces.AnagraficaTabelleManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.AziendeManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.SediEntitaManager;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;
import it.eurotn.panjea.contabilita.domain.AreaContabileLite;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileManager;
import it.eurotn.panjea.intra.manager.interfaces.AreaIntraManager;
import it.eurotn.panjea.intra.manager.interfaces.MagazzinoIntraManager;
import it.eurotn.panjea.iva.domain.AreaIva;
import it.eurotn.panjea.iva.domain.RigaIva;
import it.eurotn.panjea.iva.manager.interfaces.AreaIvaManager;
import it.eurotn.panjea.iva.util.IImponibiliIvaQueryExecutor;
import it.eurotn.panjea.magazzino.domain.DatiGenerazione.TipoGenerazione;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino.ETipologiaCodiceIvaAlternativo;
import it.eurotn.panjea.magazzino.domain.TotalizzatoreTipoAttributo;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino.StatoAreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzinoLite;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.OperazioneAreaContabileNonTrovata;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.StrategiaTotalizzazioneDocumento;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.TipoMovimento;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.PoliticaPrezzo;
import it.eurotn.panjea.magazzino.exception.AreaContabilePresenteException;
import it.eurotn.panjea.magazzino.exception.TotaleDocumentoNonCoerenteException;
import it.eurotn.panjea.magazzino.manager.documento.generatorerighe.RigheArticoloBuilderFactoryBean.EGeneratoreRiga;
import it.eurotn.panjea.magazzino.manager.documento.generatorerighe.interfaces.IGeneratoreRigheArticolo;
import it.eurotn.panjea.magazzino.manager.documento.generatorerighe.interfaces.RigheArticoloBuilderFactory;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoManager;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoVerificaManager;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.RigaMagazzinoManager;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.StatiAreaMagazzinoManager;
import it.eurotn.panjea.magazzino.manager.documento.totalizzatore.StrategiaTotalizzazione;
import it.eurotn.panjea.magazzino.manager.documento.totalizzatore.Totalizzatore;
import it.eurotn.panjea.magazzino.manager.documento.totalizzatore.TotalizzazioneTipoAttributo;
import it.eurotn.panjea.magazzino.manager.interfaces.MagazzinoAnagraficaManager;
import it.eurotn.panjea.magazzino.manager.interfaces.SediMagazzinoManager;
import it.eurotn.panjea.magazzino.manager.moduloprezzo.interfaces.PrezzoArticoloCalculator;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni.interfaces.RigaDocumentoVariazioneProvvigioneStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni.interfaces.TipoVariazioneProvvigioneStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.interfaces.RigaDocumentoVariazioneScontoStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.interfaces.TipoVariazioneScontoStrategy;
import it.eurotn.panjea.magazzino.manager.schedearticolo.interfaces.MagazzinoControlloSchedeArticolo;
import it.eurotn.panjea.magazzino.service.exception.ConversioneUnitaMisuraAssenteException;
import it.eurotn.panjea.magazzino.service.exception.DocumentiEsistentiPerAreaMagazzinoException;
import it.eurotn.panjea.magazzino.service.exception.DocumentoAssenteAvvisaException;
import it.eurotn.panjea.magazzino.service.exception.DocumentoAssenteBloccaException;
import it.eurotn.panjea.magazzino.service.exception.DocumentoAssenteNonAvvisareException;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;
import it.eurotn.panjea.magazzino.util.ParametriRicercaAreaMagazzino;
import it.eurotn.panjea.magazzino.util.RigaAttributoTotalizzazioneDTO;
import it.eurotn.panjea.magazzino.util.SedeAreaMagazzinoDTO;
import it.eurotn.panjea.magazzino.util.queryExecutor.ITotalizzatoriQueryExecutor;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.pagamenti.domain.SedePagamento;
import it.eurotn.panjea.pagamenti.manager.interfaces.SediPagamentoManager;
import it.eurotn.panjea.partite.domain.AreaPartite;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rate.manager.interfaces.AreaRateManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.tesoreria.manager.interfaces.StatisticheTesoreriaManager;
import it.eurotn.panjea.util.SqlExecuter;
import it.eurotn.security.JecPrincipal;

@Stateless(name = "Panjea.AreaMagazzinoManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaMagazzinoManager")
public class AreaMagazzinoManagerBean implements AreaMagazzinoManager {

    private static Logger logger = Logger.getLogger(AreaMagazzinoManagerBean.class);

    @Resource
    private SessionContext sessionContext;

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private StrategiaTotalizzazione strategiaTotalizzazione;

    @EJB
    private DocumentiManager documentiManager;

    @EJB
    private AreaMagazzinoVerificaManager areaMagazzinoVerificaManager;

    @EJB
    @IgnoreDependency
    private AreaIvaManager areaIvaManager;

    @EJB
    private StatiAreaMagazzinoManager statiAreaMagazzinoManager;

    @EJB
    private SediMagazzinoManager sediMagazzinoManager;

    @EJB
    private SediEntitaManager sediEntitaManager;

    @EJB
    private SediPagamentoManager sediPagamentoManager;

    @EJB
    private StatisticheTesoreriaManager statisticheTesoreriaManager;

    @EJB
    private IImponibiliIvaQueryExecutor imponibiliIvaQueryExecutor;

    @EJB
    private ITotalizzatoriQueryExecutor totalizzatoriQueryExecutor;

    @EJB
    private PrezzoArticoloCalculator prezzoArticoloCalculator;

    @EJB
    private MagazzinoAnagraficaManager magazzinoAnagraficaManager;

    @EJB
    private AnagraficaTabelleManager anagraficaTabelleManager;

    @EJB
    private RigheArticoloBuilderFactory generatoriRigaArticoloFactory;

    @EJB
    private MagazzinoIntraManager magazzinoIntraManager;

    @EJB
    private AreaIntraManager areaIntraManager;

    @EJB
    private AziendeManager aziendeManager;

    @EJB
    private AreaContabileManager areaContabileManager;

    @EJB
    private AreaRateManager areaRateManager;

    @EJB
    private RigaMagazzinoManager rigaMagazzinoManager;

    @EJB
    @IgnoreDependency
    private MagazzinoControlloSchedeArticolo magazzinoControlloSchedeArticolo;

    @Override
    public AreaMagazzino aggiornaDatiSede(AreaMagazzino areaMagazzino, SedeEntita sedeEntita) {
        SedeAreaMagazzinoDTO datiSede = caricaSedeAreaMagazzinoDTO(sedeEntita);
        areaMagazzino.setListino(datiSede.getListino());
        areaMagazzino.setListinoAlternativo(datiSede.getListinoAlternativo());
        areaMagazzino.setVettore(datiSede.getVettore());
        areaMagazzino.setSedeVettore(datiSede.getSedeVettore());
        areaMagazzino.setCausaleTrasporto(datiSede.getCausaleTrasporto());
        areaMagazzino.setTrasportoCura(datiSede.getTrasportoCura());
        areaMagazzino.setTipoPorto(datiSede.getTipoPorto());
        areaMagazzino.setAddebitoSpeseIncasso(datiSede.isCalcoloSpese());
        areaMagazzino.setInserimentoBloccato(datiSede.isInserimentoBloccato());
        areaMagazzino.setRaggruppamentoBolle(datiSede.isRaggruppamentoBolle());
        areaMagazzino.setAspettoEsteriore(datiSede.getAspettoEsteriore());
        areaMagazzino.setStampaPrezzi(datiSede.isStampaPrezzi());
        areaMagazzino.setIdZonaGeografica(datiSede.getIdZonaGeografica());
        // areaMagazzino.age setAgente( datiSede.getAgente());

        ETipologiaCodiceIvaAlternativo tipologiaCodiceIvaAlternativo = datiSede.getTipologiaCodiceIvaAlternativo();

        CodiceIva codiceIvaAlternativo = datiSede.getCodiceIvaAlternativo();
        // se la tipologia del codice iva alternativo è
        // ESENZIONE_DICHIARAZIONE_INTENTO controllo che la
        // data del documento rientri nella scadenza altrimenti
        // imposto come tipologia NESSUNA e rimuovo il
        // codice iv alternativo
        if (datiSede
                .getTipologiaCodiceIvaAlternativo() == ETipologiaCodiceIvaAlternativo.ESENZIONE_DICHIARAZIONE_INTENTO) {
            Date dataScadenza = datiSede.getDataScadenzaDichiarazioneIntento();
            Date dataDocumento = areaMagazzino.getDocumento().getDataDocumento();

            if (dataScadenza == null || (dataDocumento != null && dataDocumento.after(dataScadenza))) {
                tipologiaCodiceIvaAlternativo = ETipologiaCodiceIvaAlternativo.NESSUNO;
                codiceIvaAlternativo = null;
            }

        }
        areaMagazzino.setTipologiaCodiceIvaAlternativo(tipologiaCodiceIvaAlternativo);
        areaMagazzino.setCodiceIvaAlternativo(codiceIvaAlternativo);

        return areaMagazzino;
    }

    @Override
    public void aggiungiVariazione(Integer idAreaMagazzino, BigDecimal variazione, BigDecimal percProvvigione,
            RigaDocumentoVariazioneScontoStrategy variazioneScontoStrategy,
            TipoVariazioneScontoStrategy tipoVariazioneScontoStrategy,
            RigaDocumentoVariazioneProvvigioneStrategy variazioneProvvigioneStrategy,
            TipoVariazioneProvvigioneStrategy tipoVariazioneProvvigioneStrategy) {
        logger.debug("--> Enter aggiungiVariazione");

        AreaMagazzino areaMagazzino;

        try {
            areaMagazzino = panjeaDAO.load(AreaMagazzino.class, idAreaMagazzino);
        } catch (Exception e) {
            logger.error("--> errore durante il caricamento dell'area magazzino.", e);
            throw new RuntimeException("errore durante il caricamento dell'area magazzino.", e);
        }

        List<RigaArticolo> righe = rigaMagazzinoManager.getDao().caricaRigheArticolo(areaMagazzino);

        for (RigaArticolo rigaArticolo : righe) {

            if (!rigaArticolo.isChiusa()) {
                AreaRate areaRate = areaRateManager.caricaAreaRate(areaMagazzino.getDocumento());
                CodicePagamento codicePagamento = areaRate.getCodicePagamento();

                PoliticaPrezzo politicaPrezzo = rigaMagazzinoManager.calcolaPoliticaPrezzo(rigaArticolo,
                        codicePagamento);
                rigaArticolo.setPoliticaPrezzo(politicaPrezzo);
                rigaArticolo = (RigaArticolo) variazioneScontoStrategy.applicaVariazione(rigaArticolo, variazione,
                        tipoVariazioneScontoStrategy);
                rigaArticolo = (RigaArticolo) variazioneProvvigioneStrategy.applicaVariazione(rigaArticolo,
                        percProvvigione, tipoVariazioneProvvigioneStrategy);
                try {
                    panjeaDAO.save(rigaArticolo);
                } catch (DAOException e) {
                    logger.error("-->errore nel salvare la riga dell'area magazzino " + rigaArticolo, e);
                    throw new RuntimeException("-->errore nel caricare la riga dell'area magazzino " + rigaArticolo, e);
                }
            }
        }

        areaMagazzino.setStatoAreaMagazzino(StatoAreaMagazzino.PROVVISORIO);
        areaMagazzino.getDatiValidazioneRighe().invalida();
        try {
            salvaAreaMagazzino(areaMagazzino, false);
        } catch (Exception e) {
            logger.error("--> errore durante il salvataggio dell'area magazzino", e);
            throw new RuntimeException("errore durante il salvataggio dell'area magazzino", e);
        }
        logger.debug("--> Exit aggiungiVariazione");
    }

    /**
     *
     * @param areaMagazzino
     *            areaMagazzino interesasto
     * @return areaMagazzino con i totalizzatori avvalorati.
     */
    @SuppressWarnings("unchecked")
    private AreaMagazzino calcolaTotalizzatori(AreaMagazzino areaMagazzino) {

        // carico attributi riga e il relativo valore raggruppato per
        // totalizzatoreTipoAttributo e unita' di
        // misura
        StringBuilder sb = new StringBuilder();

        // NB: l'hql non permette di lavorare con più di 2 decimali sugli
        // importi nel caso di cast a big_decimal
        // (è fisso a 2 decimali), eseguo quindi un sql con i soli campi che mi
        // interessano
        sb.append("select ");
        sb.append("cast(REPLACE(REPLACE(attributiRigaTable.valore,'.',''),',','.') as decimal(19,6)) as valore, ");
        sb.append("tipiAttributoTable.totalizzatoreTipoAttributo as totalizzatore, ");
        sb.append("tipiAttributoTable.numeroDecimali as numeroDecimali, ");
        sb.append("unitaMisuraTable.codice as codiceUnitaMisura ");
        sb.append("from maga_attributi_riga attributiRigaTable ");
        sb.append(
                "inner join maga_tipo_attributo tipiAttributoTable on attributiRigaTable.tipoAttributo_id=tipiAttributoTable.id ");
        sb.append(
                "inner join maga_righe_magazzino righeMagazzinoTable on righeMagazzinoTable.id=attributiRigaTable.rigaArticolo_id ");
        sb.append(
                "inner join anag_unita_misura unitaMisuraTable on tipiAttributoTable.unitaMisura_id=unitaMisuraTable.id ");
        sb.append("where tipiAttributoTable.totalizzatoreTipoAttributo<>0 ");
        sb.append("and righeMagazzinoTable.areaMagazzino_id=:paramIdAreaMagazzino ");
        // sb.append("group by totalizzatore, codiceUnitaMisura ");

        org.hibernate.ejb.QueryImpl queryImpl = (org.hibernate.ejb.QueryImpl) panjeaDAO.getEntityManager()
                .createNativeQuery(sb.toString());
        SQLQuery sqlQuery = ((SQLQuery) queryImpl.getHibernateQuery());
        sqlQuery.addScalar("valore");
        sqlQuery.addScalar("totalizzatore");
        sqlQuery.addScalar("numeroDecimali");
        sqlQuery.addScalar("codiceUnitaMisura");

        sqlQuery.setParameter("paramIdAreaMagazzino", areaMagazzino.getId());
        sqlQuery.setResultTransformer(Transformers.aliasToBean(RigaAttributoTotalizzazioneDTO.class));

        List<RigaAttributoTotalizzazioneDTO> attribuitiRigaValore = null;
        try {
            attribuitiRigaValore = sqlQuery.list();
        } catch (Exception e) {
            logger.error("--> Errore durante la totalizzazione tipi attributo dell'area magazzino", e);
            throw new RuntimeException("Errore durante la totalizzazione tipi attributo dell'area magazzino", e);
        }

        // se non ho risultati per totalizzatore non devo modificare i dati
        // dell'area magazzino
        if (attribuitiRigaValore != null && attribuitiRigaValore.size() > 0) {
            // carico le conversioni u.m disponibili
            List<ConversioneUnitaMisura> conversioneUnitaMisura = anagraficaTabelleManager
                    .caricaConversioniUnitaMisura();

            // ora devo convertire le unita' di misura di peso in quella che
            // prendo come u.m. di destinazione e
            // calcolare i
            // totali per totalizzatoreTipoAttributo l'unità di misura di
            // default è definita per ogni tipoTotalizzazione
            TotalizzazioneTipoAttributo totalizzazioneTipoAttributo = new TotalizzazioneTipoAttributo(
                    conversioneUnitaMisura);

            // totalizzo per i tipi definiti
            Map<TotalizzatoreTipoAttributo, BigDecimal> totaliTipoAttributo = null;
            try {
                totaliTipoAttributo = totalizzazioneTipoAttributo.totalizza(attribuitiRigaValore);
            } catch (ConversioneUnitaMisuraAssenteException e) {
                throw new RuntimeException(e);
            }

            // ora posso recuperare i totali e associarli all'area magazzino
            BigDecimal numeroColli = totaliTipoAttributo.get(TotalizzatoreTipoAttributo.NUMERO_COLLI);
            BigDecimal volume = totaliTipoAttributo.get(TotalizzatoreTipoAttributo.VOLUME);
            BigDecimal peso = totaliTipoAttributo.get(TotalizzatoreTipoAttributo.PESO);

            // se non ho un valore per ognuno dei totalizzatori, devo evitare di
            // modificare il valore preesistente
            if (volume != null && volume.compareTo(BigDecimal.ZERO) != 0) {
                areaMagazzino.setVolume(volume);
            }
            if (peso != null && peso.compareTo(BigDecimal.ZERO) != 0) {
                areaMagazzino.setPesoNetto(peso);
            }
            if (numeroColli != null && numeroColli.compareTo(BigDecimal.ZERO) != 0) {
                areaMagazzino.setNumeroColli(numeroColli.intValue());
            }
        }
        return areaMagazzino;
    }

    @Override
    public AreaMagazzino cambiaStatoDaConfermatoInProvvisorio(AreaMagazzino areaMagazzino) {
        logger.debug("--> Enter cambiaStatoDaConfermatoInProvvisorio");
        // uso il DAO per non salvare anche il documento
        AreaMagazzino areaMagazzinoResult = null;
        try {
            areaMagazzinoResult = statiAreaMagazzinoManager.cambiaStatoDaConfermatoInProvvisorio(areaMagazzino);
            areaMagazzinoResult = salvaAreaMagazzino(areaMagazzino, true);
        } catch (Exception e) {
            logger.error("--> errore in salvaAreaMagazzino", e);
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit cambiaStatoDaConfermatoInProvvisorio");
        return areaMagazzinoResult;
    }

    @Override
    public AreaMagazzino cambiaStatoDaProvvisorioInConfermato(AreaMagazzino areaMagazzino) {
        logger.debug("--> Enter cambiaStatoDaProvvisorioInConfermato");
        AreaMagazzino areaMagazzinoSave = statiAreaMagazzinoManager.cambiaStatoDaProvvisorioInConfermato(areaMagazzino);
        // uso il DAO per non salvare anche il documento
        try {
            areaMagazzinoSave = salvaAreaMagazzino(areaMagazzino, true);
        } catch (Exception e) {
            logger.error("--> errore in salvaAreaMagazzino", e);
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit cambiaStatoDaProvvisorioInConfermato");
        return areaMagazzinoSave;
    }

    @Override
    public AreaMagazzino cambiaStatoDaProvvisorioInForzato(AreaMagazzino areaMagazzino) {
        logger.debug("--> Enter cambiaStatoDaProvvisorioInForzato");
        AreaMagazzino areaMagazzinoSave = statiAreaMagazzinoManager.cambiaStatoDaProvvisorioInForzato(areaMagazzino);
        // uso il DAO per non salvare anche il documento
        try {
            areaMagazzinoSave = salvaAreaMagazzino(areaMagazzino, true);
        } catch (Exception e) {
            logger.error("--> errore in salvaAreaMagazzino", e);
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit cambiaStatoDaProvvisorioInForzato");
        return areaMagazzinoSave;
    }

    @Override
    public AreaMagazzino cambiaStatoInProvvisorio(AreaMagazzino areaMagazzino) {
        logger.debug("--> Enter cambiaStatoInProvvisorio");
        if (areaMagazzino.getStatoAreaMagazzino() == StatoAreaMagazzino.CONFERMATO
                || areaMagazzino.getStatoAreaMagazzino() == StatoAreaMagazzino.FORZATO) {
            AreaMagazzino areaMagazzinoResult = cambiaStatoDaConfermatoInProvvisorio(areaMagazzino);
            return areaMagazzinoResult;
        }
        // lo stato è gia provvisorio, quindi ritorno la stessa area magazzino
        return areaMagazzino;
    }

    @Override
    public List<AgenteLite> caricaAgentiPerAreaMagazzino(int idAreaMagazzino) {
        logger.debug("--> Enter caricaAgentiPerAreaMagazzino");
        List<AgenteLite> agenti = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        sb.append("select riga.agente_id,agente.codice,anag.denominazione, ");
        sb.append("sum(ifnull(round(riga.importoInValutaAziendaTotale*riga.percProvvigione/100,2),0)) ");
        sb.append("from maga_righe_magazzino riga,anag_entita agente,anag_anagrafica anag ");
        sb.append(
                "where riga.TIPO_RIGA='A' and riga.agente_id=agente.id and agente.anagrafica_id=anag.id and riga.areaMagazzino_id=:areaMagazzino ");
        sb.append("group by riga.agente_id ,agente.codice ,anag.denominazione ");

        org.hibernate.ejb.QueryImpl queryImpl = (org.hibernate.ejb.QueryImpl) panjeaDAO.getEntityManager()
                .createNativeQuery(sb.toString());
        queryImpl.setParameter("areaMagazzino", idAreaMagazzino);
        SQLQuery sqlQuery = ((SQLQuery) queryImpl.getHibernateQuery());

        @SuppressWarnings("unchecked")
        List<Object[]> tupla = sqlQuery.list();

        for (Object[] tuplaAgente : tupla) {
            AgenteLite agente = new AgenteLite();
            agente.setId((Integer) tuplaAgente[0]);
            agente.setCodice((Integer) tuplaAgente[1]);
            agente.getAnagrafica().setDenominazione((String) tuplaAgente[2]);
            agente.setImportoProvvigione((BigDecimal) tuplaAgente[3]);
            agenti.add(agente);
        }
        logger.debug("--> Exit caricaAgentiPerAreaMagazzino");
        return agenti;
    }

    @Override
    public AreaMagazzino caricaAreaMagazzino(AreaMagazzino areaMagazzino) {
        logger.debug("--> Enter caricaAreaMagazzino");
        AreaMagazzino areaMagazzinoCaricata = null;
        try {
            areaMagazzinoCaricata = panjeaDAO.load(AreaMagazzino.class, areaMagazzino.getId());
        } catch (ObjectNotFoundException e) {
            logger.error("--> errore ObjectNotFoundException in caricaAreaMagazzino", e);
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit caricaAreaMagazzino");
        return areaMagazzinoCaricata;
    }

    @Override
    public AreaMagazzino caricaAreaMagazzinoByDocumento(Documento documento) {
        logger.debug("--> Enter caricaAreaMagazzinoByDocumento");
        Query query = panjeaDAO.prepareNamedQuery("AreaMagazzino.ricercaByDocumento");
        query.setParameter("paramIdDocumento", documento.getId());
        AreaMagazzino areaMagazzino = null;
        try {
            areaMagazzino = (AreaMagazzino) panjeaDAO.getSingleResult(query);
        } catch (ObjectNotFoundException e) {
            return areaMagazzino;
        } catch (DAOException e) {
            logger.error("--> errore nel caricare l'area magazzino per il documento " + documento.getId(), e);
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit caricaAreaMagazzinoByDocumento");
        return areaMagazzino;
    }

    @Override
    public AreaMagazzinoFullDTO caricaAreaMagazzinoFullDTO(AreaMagazzino areaMagazzino) {
        AreaMagazzinoFullDTO areaMagazzinoFullDTO = new AreaMagazzinoFullDTO();
        // se l'area magazzino è in sessione eseguo una refresh per ricaricare i
        // dati nella sessione (il numero di
        // righe)
        // altrimenti chiamo una carica "completa"
        AreaMagazzino areaMagazzinoLoad = null;
        if (panjeaDAO.getEntityManager().contains(areaMagazzino)) {
            panjeaDAO.getEntityManager().refresh(areaMagazzino);
            areaMagazzinoLoad = areaMagazzino;
        } else {
            areaMagazzinoLoad = caricaAreaMagazzino(areaMagazzino);
        }

        areaMagazzinoFullDTO.setAreaMagazzino(areaMagazzinoLoad);
        return areaMagazzinoFullDTO;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<AreaMagazzino> caricaAreeMagazzinoByStato(StatoAreaMagazzino statoAreaMagazzino) {
        StringBuilder hqlString = new StringBuilder();
        hqlString.append(
                "select am from AreaMagazzino am where am.statoAreaMagazzino = :paramStatoAreaMagazzino and am.documento.codiceAzienda = :paramCodiceAzienda");
        Query query = panjeaDAO.prepareQuery(hqlString.toString());

        query.setParameter("paramStatoAreaMagazzino", statoAreaMagazzino);
        query.setParameter("paramCodiceAzienda", getAzienda());

        List<AreaMagazzino> areeMagazzinoByStato = new ArrayList<>();
        try {
            areeMagazzinoByStato = panjeaDAO.getResultList(query);
        } catch (Exception e1) {
            logger.error("--> Errore durante il caricamento delle aree magazzino in fatturazione", e1);
            throw new RuntimeException("Errore durante il caricamento delle aree magazzino in fatturazione", e1);
        }
        return areeMagazzinoByStato;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<AreaMagazzinoLite> caricaAreeMagazzinoCollegate(List<AreaMagazzino> areeMagazzino) {
        StringBuilder sb = new StringBuilder(500);
        sb.append("select ");
        sb.append("distinct rmc.areaMagazzino.id as id, ");
        sb.append("rmc.areaMagazzino.version as version, ");
        sb.append("docc.id as idDocumento,");
        sb.append("docc.dataDocumento as dataDocumento,");
        sb.append("docc.codice.codice as codiceDocumento ");
        sb.append("from RigaMagazzino r ");
        sb.append("join r.rigaMagazzinoCollegata rmc ");
        sb.append("join rmc.areaMagazzino amc ");
        sb.append("join amc.documento docc ");
        sb.append("where r.areaMagazzino in (:aree) ");
        Query query = panjeaDAO.prepareQuery(sb.toString(), AreaMagazzinoLite.class, null);
        query.setParameter("aree", areeMagazzino);
        List<AreaMagazzinoLite> result;
        try {
            result = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            logger.error("--> errore nel caricare le aree collegate", e);
            throw new RuntimeException(e);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<AreaMagazzinoRicerca> caricaAreeMagazzinoConRichiestaDatiAccompagnatori(Date dataEvasione) {

        StringBuilder sb = new StringBuilder(300);
        sb.append("select a.id ");
        sb.append("from AreaMagazzino a ");
        sb.append(" where a.documento.codiceAzienda = :paramCodiceAzienda  ");
        sb.append(" and a.datiGenerazione.tipoGenerazione = :tipoGenerazione ");
        sb.append(" and a.datiGenerazione.dataGenerazione  = :dataGenerazione ");

        Query query = panjeaDAO.prepareQuery(sb.toString());
        query.setParameter("paramCodiceAzienda", getAzienda());
        query.setParameter("tipoGenerazione", TipoGenerazione.EVASIONE);
        query.setParameter("dataGenerazione", dataEvasione);

        List<Integer> results = null;
        try {
            results = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            logger.error("--> errore durante il caricamento delle aree magazzino.", e);
            throw new RuntimeException("errore durante il caricamento delle aree magazzino.", e);
        }

        List<AreaMagazzinoRicerca> areeResult = Collections.emptyList();

        if (results != null && !results.isEmpty()) {
            areeResult = caricaAreeMagazzinoConRichiestaDatiAccompagnatori(results);
        }

        return areeResult;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<AreaMagazzinoRicerca> caricaAreeMagazzinoConRichiestaDatiAccompagnatori(List<Integer> idAree) {

        StringBuffer sb = new StringBuffer();
        sb.append("select  ");
        sb.append("distinct a.id as idAreaMagazzino, ");
        sb.append("a.documento.codiceAzienda as azienda, ");
        sb.append("a.documento.id as idDocumento, ");
        sb.append("a.documento.tipoDocumento.id as idTipoDocumento, ");
        sb.append("a.documento.entita.id as idEntita, ");
        sb.append("a.dataRegistrazione as dataRegistrazione, ");
        sb.append("a.statoAreaMagazzino as stato, ");
        sb.append("a.tipoAreaMagazzino.id as idTipoAreaMagazzino, ");
        sb.append("a.documento.tipoDocumento.tipoEntita as tipoEntita, ");
        sb.append("a.documento.entita.codice as codiceEntita, ");
        sb.append("a.documento.entita.anagrafica.denominazione as denominazioneEntita, ");
        sb.append("a.documento.tipoDocumento.codice as codiceTipoDocumento, ");
        sb.append("a.documento.tipoDocumento.descrizione as descrizioneTipoDocumento, ");
        sb.append("a.documento.dataDocumento as dataDocumento, ");
        sb.append("a.documento.codice as codiceDocumento, ");
        sb.append("doc.sedeEntita.id as idSede, ");
        sb.append("sed.descrizione as descrizioneSede, ");
        sb.append("sed.indirizzo as indirizzoSede, ");
        sb.append("loc.descrizione as descrizioneLocalitaSede, ");
        sb.append("doc.sedeEntita.codice as codiceSede ");
        sb.append(
                " from AreaMagazzino a inner join a.tipoAreaMagazzino.datiAccompagnatoriMetaData datiAcc left join a.depositoDestinazione left join a.documento doc left join doc.sedeEntita.sede sed left join a.documento.entita  left join a.documento.entita.anagrafica left join sed.datiGeografici.localita loc ");
        sb.append(" where a.documento.codiceAzienda = :paramCodiceAzienda  ");
        sb.append(" and a.id in (:idAreeDaRicercare) ");

        Query query = panjeaDAO.prepareQuery(sb.toString());
        ((QueryImpl) query).getHibernateQuery()
                .setResultTransformer(Transformers.aliasToBean((AreaMagazzinoRicerca.class)));
        query.setParameter("paramCodiceAzienda", getAzienda());
        query.setParameter("idAreeDaRicercare", idAree);

        List<AreaMagazzinoRicerca> result = Collections.emptyList();
        try {
            result = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            logger.error("-->errore nella ricerca documento", e);
            throw new RuntimeException(e);
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Integer> caricaIdAreeMagazzinoPerStampaEvasione(List<AreaMagazzinoRicerca> aree) {
        logger.debug("--> Enter caricaIdAreeMagazzinoPerStampaEvasione");

        List<Integer> idAreeRicerca = new ArrayList<>();
        for (AreaMagazzinoRicerca areaMagazzinoRicerca : aree) {
            idAreeRicerca.add(areaMagazzinoRicerca.getIdAreaMagazzino());
        }

        StringBuilder sb = new StringBuilder();
        sb.append("select am.id ");
        sb.append(
                "from AreaMagazzino am left join am.vettore vett left join am.documento.sedeEntita sede left join sede.zonaGeografica zona ");
        sb.append("where ");
        sb.append("not exists (select riga.areaMagazzino from RigaArticolo riga ");
        sb.append("where (riga.articolo.gestioneQuantitaZero = true and riga.qta = 0) and riga.areaMagazzino = am) ");
        // nel caso di un database nuovo, se evado un ordine e non ho aree magazzino, paramId è
        // vuoto e la query da
        // errore, quindi aggiungo questa parte solo se ho aree
        if (!idAreeRicerca.isEmpty()) {
            sb.append("and am.id in (:paramId) ");
        }
        // sb.append("group by riga.areaMagazzino) ");
        sb.append("order by vett.codice,zona.codice,sede.ordinamento,sede.id,am.documento.codice ");

        Query query = panjeaDAO.prepareQuery(sb.toString());
        if (!idAreeRicerca.isEmpty()) {
            query.setParameter("paramId", idAreeRicerca);
        }

        List<Integer> idAreeOrdinate = new ArrayList<>();
        try {
            idAreeOrdinate = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            logger.error("--> errore durante il caricamento delle aree magazzino ordinate per l'evasione.", e);
            throw new RuntimeException("errore durante il caricamento delle aree magazzino ordinate per l'evasione.",
                    e);
        }

        logger.debug("--> Exit caricaIdAreeMagazzinoPerStampaEvasione");
        return idAreeOrdinate;
    }

    @SuppressWarnings("unchecked")
    @Override
    public SedeAreaMagazzinoDTO caricaSedeAreaMagazzinoDTO(SedeEntita sedeEntita) {
        logger.debug("--> Enter caricaSedeAreaMagazzinoDTO");

        // Recupero i rapporti bancari della sede
        List<RapportoBancarioSedeEntita> listRapportiBancari = new ArrayList<>();
        Query query = panjeaDAO.prepareNamedQuery("RapportoBancarioSedeEntita.caricaBySedeEntita");
        query.setParameter("paramIdSedeEntita", sedeEntita.getId());

        try {
            listRapportiBancari = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            logger.error(
                    "--> Errore durante il caricamento dei rapporti bancari per la sede entità: " + sedeEntita.getId(),
                    e);
            throw new RuntimeException(
                    "Errore durante il caricamento dei rapporti bancari per la sede entità: " + sedeEntita.getId(), e);
        }

        SedeMagazzino sedeMagazzino = null;
        SedePagamento sedePagamento = null;

        // se la sede entita' non e' principale e ha ereditaDatiCommerciali=true
        // devo
        // caricare la sede pagamento e magazzino della sede principale
        // Recupero la sede magazzino
        sedeMagazzino = sediMagazzinoManager.caricaSedeMagazzinoBySedeEntita(sedeEntita, false);
        sedePagamento = sediPagamentoManager.caricaSedePagamentoBySedeEntita(sedeEntita, false);

        BigDecimal importoRateAperte = null;
        BigDecimal importoDocumentiAperti = null;
        if (sedeEntita.getEntita() instanceof Cliente) {
            EntitaLite entitaLite = new ClienteLite();
            entitaLite.setId(sedeEntita.getEntita().getId());
            importoRateAperte = statisticheTesoreriaManager.calcolaImportoRateAperte(entitaLite);
            importoDocumentiAperti = statisticheTesoreriaManager.calcolaImportoDocumentiAperti(entitaLite);
        }

        // Carico la sede del vettore
        SedeEntita sedeVettore = null;
        if (sedeEntita.getVettore() != null && !sedeEntita.getVettore().isNew()) {
            // Sede Principale del vettore
            try {
                sedeVettore = sediEntitaManager.caricaSedePrincipaleEntita(sedeEntita.getVettore().creaProxyEntita());
            } catch (AnagraficaServiceException e) {
                logger.error("-->errore nel caricare la sede principale dell'entità", e);
            }
        }

        SedeAreaMagazzinoDTO sedeAreaMagazzinoDTO = new SedeAreaMagazzinoDTO(sedeMagazzino, listRapportiBancari,
                sedePagamento, importoRateAperte, importoDocumentiAperti, sedeEntita, sedeVettore);

        logger.debug("--> Exit caricaSedeAreaMagazzinoDTO");

        return sedeAreaMagazzinoDTO;
    }

    @Override
    public AreaMagazzinoLite checkAreaMagazzino(Documento documento) {
        logger.debug("--> Enter checkAreaMagazzino");
        Query query = panjeaDAO.prepareNamedQuery("AreaMagazzino.checkByDocumento");
        query.setParameter("paramIdDocumento", documento.getId());
        AreaMagazzinoLite areaMagazzinoLite = new AreaMagazzinoLite();
        try {
            Integer id = (Integer) panjeaDAO.getSingleResult(query);
            areaMagazzinoLite.setId(id);
        } catch (ObjectNotFoundException e) {
            return null;
        } catch (DAOException e) {
            logger.error("--> errore DAO in caricaAreaRate ByDocumento", e);
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit checkAreaMagazzino");
        return areaMagazzinoLite;
    }

    /**
     * metodo incaricato di verificare ed eventualmente creare l'associazione con la classe di dominio {@link Documento}
     * . <br>
     * viene eseguita la ricerca dei documenti esistenti attraverso gli attributi chiave di dominio di {@link Documento}
     * e viene <br>
     * poi valutata, attraverso il metodo sul Manager {@link AreaMagazzinoVerificaManager}, il tipo di azione da
     * compiere presente in {@link TipoAreaMagazzino} nel caso il documento non esista
     *
     * @param areaMagazzino
     *            {@link AreaMagazzino} da associare
     * @param forzaSalvataggio
     *            se non esiste il documento da associare ne crae uno e salva.
     * @return areaMagazzino con documento associato
     * @throws DocumentoAssenteAvvisaException
     *             Lanciato se non esiste un documento<br/.> e ho
     *             {@link TipoAreaMagazzino#getOperazioneAreaContabileNonTrovata} =
     *             {@link OperazioneAreaContabileNonTrovata#AVVISA}
     * @throws DocumentoAssenteBloccaException
     *             Lanciato se non esiste un documento<br.> e ho
     *             {@link TipoAreaMagazzino#getOperazioneAreaContabileNonTrovata} =
     *             {@link OperazioneAreaContabileNonTrovata#BLOCCA}
     * @throws DocumentiEsistentiPerAreaMagazzinoException
     *             Lanciato se esistono più documenti da poter associare all'area.
     */
    private AreaMagazzino creaAssociazioneConDocumento(AreaMagazzino areaMagazzino, boolean forzaSalvataggio)
            throws DocumentoAssenteAvvisaException, DocumentoAssenteBloccaException,
            DocumentiEsistentiPerAreaMagazzinoException {
        logger.debug("--> Enter creaAssociazioneConDocumento " + areaMagazzino);
        try {
            areaMagazzinoVerificaManager.verificaAssociazioneConDocumento(areaMagazzino);
        } catch (DocumentoAssenteNonAvvisareException e) {
            logger.info("--> documento assente e richiesta di non avvisare-> salvataggio di Documento ");
            Documento documento = areaMagazzino.getDocumento();
            areaMagazzino.setDocumentoCreatoDaAreaMagazzino(true);
            try {
                documento = documentiManager.salvaDocumento(documento);
                areaMagazzino.setDocumento(documento);
            } catch (DocumentoDuplicateException e1) {
                throw new RuntimeException(e1);
            }
        } catch (DocumentoAssenteAvvisaException e) {
            if (!forzaSalvataggio) {
                logger.debug("--> Documento assente e richiesta di avvisare ");
                throw e;
            }
            logger.debug(
                    "--> documento assente e richiesta di avvisare ma con forzaSalvataggio = true -> salvataggio di Documento ");
            Documento documento = areaMagazzino.getDocumento();
            areaMagazzino.setDocumentoForzatoDaAreaMagazzino(true);
            try {
                documento = documentiManager.salvaDocumento(documento);
                areaMagazzino.setDocumento(documento);
            } catch (DocumentoDuplicateException e1) {
                throw new RuntimeException(e1);
            }

        } catch (DocumentoAssenteBloccaException e) {
            logger.debug("--> Documento assente e richiesta di bloccare il documento ");
            throw e;
        }
        logger.debug("--> Exit creaAssociazioneConDocumento");
        return areaMagazzino;
    }

    /**
     * @return codice azienda corrente
     */
    private String getAzienda() {
        return ((JecPrincipal) sessionContext.getCallerPrincipal()).getCodiceAzienda();
    }

    /**
     * @return principal loggato
     */
    private JecPrincipal getJecPrincipal() {
        return ((JecPrincipal) sessionContext.getCallerPrincipal());
    }

    @Override
    public void ordinaRighe(AreaMagazzino areaMagazzino) {
        // Le righe note automatiche devono sempre andare in fondo
        // ma non vengono ricreate ogni volta come le righe automatiche
        // quindi le metto all'ultimo posto del documento
        StringBuilder sb = new StringBuilder("update maga_righe_magazzino set ordinamento=");
        sb.append(Double.MAX_VALUE);
        sb.append(" where TIPO_RIGA='N' and rigaAutomatica=1 and areaMagazzino_id=");
        sb.append(areaMagazzino.getId());
        SqlExecuter executer = new SqlExecuter();
        executer.setSql(sb.toString());
        ((Session) panjeaDAO.getEntityManager().getDelegate()).doWork(executer);

        sb = new StringBuilder();
        sb.append("update maga_righe_magazzino rm inner join ");
        sb.append("(select rm.id as idRigaMagazzino,rm.areaOrdineCollegata_Id,@row_number:=0 ");
        sb.append("	from maga_righe_magazzino rm ");
        sb.append("	left join ordi_righe_ordine ro on ro.id=rm.rigaOrdineCollegata_Id ");
        sb.append("	where rm.areaMagazzino_id=");
        sb.append(areaMagazzino.getId());
        sb.append("	order by rm.ordinamento,rm.areaOrdineCollegata_Id,ro.ordinamento) ");
        sb.append("as counterTable on counterTable.idRigaMagazzino=rm.id ");
        sb.append("set rm.ordinamento=(@row_number:=@row_number+10000) ");
        executer.setSql(sb.toString());
        ((Session) panjeaDAO.getEntityManager().getDelegate()).doWork(executer);
    }

    @Override
    public AreaMagazzino ricalcolaPrezziMagazzino(Integer idAreaMagazzino) {
        // carico l'area ordine
        AreaMagazzino areaMagazzino;
        AreaRate areaRate;
        boolean righeModificate = false;

        try {
            areaMagazzino = panjeaDAO.load(AreaMagazzino.class, idAreaMagazzino);
        } catch (Exception e) {
            logger.error("--> errore durante il caricamento dell'area magazzino.", e);
            throw new RuntimeException("errore durante il caricamento dell'area magazzino.", e);
        }

        // carica l'area rate per avere il codice pagamento
        areaRate = areaRateManager.caricaAreaRate(areaMagazzino.getDocumento());

        List<RigaArticolo> righe = rigaMagazzinoManager.getDao().caricaRigheArticolo(areaMagazzino);

        // ricalcolo i prezzi se la riga non è chiusa
        for (RigaArticolo riga : righe) {
            if (!riga.isChiusa()) {
                Importo prezzoNettoPrecedente = riga.getPrezzoNetto().clone();
                rigaMagazzinoManager.ricalcolaPrezziRigaArticolo(riga, areaRate.getCodicePagamento());
                righeModificate = righeModificate || prezzoNettoPrecedente.getImportoInValutaAzienda()
                        .compareTo(riga.getPrezzoNetto().getImportoInValutaAzienda()) != 0;
            }
        }

        if (righeModificate) {
            areaMagazzino.setStatoAreaMagazzino(StatoAreaMagazzino.PROVVISORIO);
            areaMagazzino.getDatiValidazioneRighe().invalida();

            try {
                salvaAreaMagazzino(areaMagazzino, false);
            } catch (Exception e) {
                logger.error("--> errore durante il salvataggio dell'area magazzino", e);
                throw new RuntimeException("errore durante il salvataggio dell'area magazzino", e);
            }
        }

        return areaMagazzino;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<AreaMagazzinoRicerca> ricercaAreeMagazzino(
            ParametriRicercaAreaMagazzino parametriRicercaAreaMagazzino) {
        logger.debug("--> Enter ricercaAreeMagazzino");

        StringBuilder sb = new StringBuilder();
        sb.append("select  ");
        sb.append("a.id as idAreaMagazzino, ");
        sb.append("a.chiuso as chiuso, ");
        sb.append("a.documento.codiceAzienda as azienda, ");
        sb.append("a.documento.id as idDocumento, ");
        sb.append("a.documento.tipoDocumento.id as idTipoDocumento, ");
        sb.append("a.documento.entita.id as idEntita, ");
        sb.append("a.dataRegistrazione as dataRegistrazione, ");
        sb.append("a.statoAreaMagazzino as stato, ");
        sb.append("a.tipoAreaMagazzino.id as idTipoAreaMagazzino, ");
        sb.append("a.tipoAreaMagazzino.tipoMovimento as tipoMovimento, ");
        sb.append("a.tipoAreaMagazzino.valoriFatturato as valoriFatturato, ");
        sb.append("a.documento.tipoDocumento.tipoEntita as tipoEntita, ");
        sb.append("a.documento.entita.codice as codiceEntita, ");
        sb.append("a.documento.entita.anagrafica.denominazione as denominazioneEntita, ");
        sb.append("a.documento.tipoDocumento.codice as codiceTipoDocumento, ");
        sb.append("a.documento.tipoDocumento.descrizione as descrizioneTipoDocumento, ");
        sb.append("a.documento.dataDocumento as dataDocumento, ");
        sb.append("a.documento.codice as codiceDocumento, ");
        sb.append("a.documento.totale.importoInValutaAzienda as totaleDocumentoInValutaAzienda, ");
        sb.append("a.documento.totale.codiceValuta as codiceValuta, ");
        sb.append("a.documento.totale.importoInValuta as totaleDocumentoInValuta, ");
        sb.append("a.documento.totale.tassoDiCambio as totaleDocumentoTassoDiCambio, ");
        sb.append("a.depositoDestinazione.id as idDepositoDestinazione, ");
        sb.append("a.depositoDestinazione.codice as codiceDepositoDestinazione, ");
        sb.append("a.depositoDestinazione.descrizione as descrizioneDepositoDestinazione, ");
        sb.append("a.depositoOrigine.id as idDepositoOrigine, ");
        sb.append("a.depositoOrigine.version as versionDepositoOrigine, ");
        sb.append("a.depositoOrigine.codice as codiceDepositoOrigine, ");
        sb.append("a.depositoOrigine.descrizione as descrizioneDepositoOrigine, ");
        sb.append("a.areaMagazzinoNote.noteTestata as note, ");
        sb.append("a.stampaPrezzi as stampaPrezzi, ");
        sb.append("sed.id as idSede, ");
        sb.append("sed.descrizione as descrizioneSede, ");
        sb.append("sed.indirizzo as indirizzoSede, ");
        sb.append("loc.descrizione as descrizioneLocalitaSede, ");
        sb.append("doc.sedeEntita.codice as codiceSede ");
        sb.append(
                " from AreaMagazzino a left join a.depositoDestinazione left join a.documento doc left join doc.sedeEntita.sede sed left join a.documento.entita  left join a.documento.entita.anagrafica left join sed.datiGeografici.localita loc ");
        sb.append(" where a.documento.codiceAzienda = :paramCodiceAzienda  ");

        if (!parametriRicercaAreaMagazzino.getNumeroDocumentoIniziale().isEmpty()) {
            sb.append(" and (a.documento.codice.codiceOrder >= :numeroDocumentoIniziale) ");
        }
        if (!parametriRicercaAreaMagazzino.getNumeroDocumentoFinale().isEmpty()) {
            sb.append(" and (a.documento.codice.codiceOrder <= :numeroDocumentoFinale) ");
        }
        if (parametriRicercaAreaMagazzino.getAnnoCompetenza() != null
                && parametriRicercaAreaMagazzino.getAnnoCompetenza() != -1) {
            sb.append(" and a.annoMovimento=:annoCompetenza ");
        }
        if ((parametriRicercaAreaMagazzino.getEntita() != null)
                && (parametriRicercaAreaMagazzino.getEntita().getId() != null)) {
            sb.append(" and (a.documento.entita = :entita) ");
        }

        if (parametriRicercaAreaMagazzino.getIdAreeDaRicercare() != null
                && !parametriRicercaAreaMagazzino.getIdAreeDaRicercare().isEmpty()) {
            sb.append(" and a.id in (:idAreeDaRicercare) ");
        }

        List<EntitaLite> vettori = new ArrayList<>();
        List<EntitaLite> altreEntita = new ArrayList<>();
        if (parametriRicercaAreaMagazzino.getTipoEsportazione() != null
                && parametriRicercaAreaMagazzino.getTipoEsportazione().getId() != null) {
            List<EntitaLite> entita = magazzinoAnagraficaManager
                    .caricaEntitaTipoEsportazione(parametriRicercaAreaMagazzino.getTipoEsportazione());
            if (entita != null && !entita.isEmpty()) {
                // separo i vettori dalle altre entità
                for (EntitaLite entitaLite : entita) {
                    if (VettoreLite.class.equals(entitaLite.getClass())) {
                        vettori.add(entitaLite);
                    } else {
                        altreEntita.add(entitaLite);
                    }
                }
                if (!altreEntita.isEmpty()) {
                    sb.append(" and a.documento.entita in (:entitaList) ");
                }
                if (!vettori.isEmpty()) {
                    sb.append(" and a.vettore in (:vettoriList) ");
                }
            }
        }
        if ((parametriRicercaAreaMagazzino.getTipiAreaMagazzino() != null)
                && (parametriRicercaAreaMagazzino.getTipiAreaMagazzino().size() > 0)) {
            sb.append(" and (a.tipoAreaMagazzino in (:tipiAreaMagazzino)) ");
        }
        if ((parametriRicercaAreaMagazzino.getStatiAreaMagazzino() != null)
                && (parametriRicercaAreaMagazzino.getStatiAreaMagazzino().size() > 0)) {
            sb.append(" and (a.statoAreaMagazzino in (:statiAreaMagazzino)) ");
        }
        if ((parametriRicercaAreaMagazzino.getUtente() != null)
                && (parametriRicercaAreaMagazzino.getUtente().getId() != null)) {
            sb.append(" and (a.userInsert = :userName) ");
        }
        if ((parametriRicercaAreaMagazzino.getDepositiDestinazione() != null)
                && (parametriRicercaAreaMagazzino.getDepositiDestinazione().size() > 0)) {
            sb.append(" and (a.depositoDestinazione in (:depositiDestinazione)) ");
        }
        if ((parametriRicercaAreaMagazzino.getDepositiSorgente() != null)
                && (parametriRicercaAreaMagazzino.getDepositiSorgente().size() > 0)) {
            sb.append(" and (a.depositoOrigine in (:depositiSorgente)) ");
        }
        if (parametriRicercaAreaMagazzino.getDataDocumento().getDataIniziale() != null) {
            sb.append(" and a.documento.dataDocumento >= :dataDocumentoIniziale ");
        }
        if (parametriRicercaAreaMagazzino.getDataDocumento().getDataFinale() != null) {
            sb.append(" and a.documento.dataDocumento <= :dataDocumentoFinale ");
        }
        if (parametriRicercaAreaMagazzino.getDataRegistrazione().getDataIniziale() != null) {
            sb.append(" and a.dataRegistrazione >= :dataRegistrazioneIniziale ");
        }
        if (parametriRicercaAreaMagazzino.getDataRegistrazione().getDataFinale() != null) {
            sb.append(" and a.dataRegistrazione <= :dataRegistrazioneFinale ");
        }
        if ((parametriRicercaAreaMagazzino.getTipiGenerazione() != null)
                && (parametriRicercaAreaMagazzino.getTipiGenerazione().size() > 0)) {
            sb.append(" and (a.datiGenerazione.tipoGenerazione in (:tipiGenerazione)) ");

            if (parametriRicercaAreaMagazzino.getDataGenerazione() != null) {
                sb.append(" and a.datiGenerazione.dataGenerazione  = :dataGenerazione ");
            }
        }
        if (parametriRicercaAreaMagazzino.getSpeditiAlVettore() != null
                && parametriRicercaAreaMagazzino.getSpeditiAlVettore().equals(Boolean.FALSE)) {
            sb.append(" and a.segnacolli.size = 0 ");
        }
        if (parametriRicercaAreaMagazzino.getDataInizioTrasporto() != null) {
            sb.append(" and a.dataInizioSpedizione >= :dataInizioSpedizione ");
        }
        if ((parametriRicercaAreaMagazzino.getVettore() != null)
                && (parametriRicercaAreaMagazzino.getVettore().getId() != null)) {
            sb.append(" and (a.vettore = :vettore) ");
        }
        if (parametriRicercaAreaMagazzino.getSoloNonRendicontati() != null
                && parametriRicercaAreaMagazzino.getSoloNonRendicontati().equals(Boolean.TRUE)) {
            sb.append(" and a.datiSpedizioniDocumento.rendicontatoAlVettore = 0 ");
        }

        Query query = panjeaDAO.prepareQuery(sb.toString());
        ((QueryImpl) query).getHibernateQuery()
                .setResultTransformer(Transformers.aliasToBean((AreaMagazzinoRicerca.class)));

        List<AreaMagazzinoRicerca> result = Collections.emptyList();

        query.setParameter("paramCodiceAzienda", getAzienda());
        if (!altreEntita.isEmpty()) {
            query.setParameter("entitaList", altreEntita);
        }
        if (!vettori.isEmpty()) {
            query.setParameter("vettoriList", vettori);
        }
        ((QueryImpl) query).getHibernateQuery().setProperties(parametriRicercaAreaMagazzino);
        if (!parametriRicercaAreaMagazzino.getNumeroDocumentoIniziale().isEmpty()) {
            query.setParameter("numeroDocumentoIniziale",
                    parametriRicercaAreaMagazzino.getNumeroDocumentoIniziale().getCodiceOrder());
        }
        if (!parametriRicercaAreaMagazzino.getNumeroDocumentoFinale().isEmpty()) {
            query.setParameter("numeroDocumentoFinale",
                    parametriRicercaAreaMagazzino.getNumeroDocumentoFinale().getCodiceOrder());
        }
        try {
            result = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            logger.error("-->errore nella ricerca documento", e);
            throw new RuntimeException(e);
        }

        logger.debug("--> Exit ricercaAreeMagazzino");
        return result;
    }

    @Override
    public AreaMagazzino salvaAreaMagazzino(AreaMagazzino areaMagazzino, boolean forzaSalvataggio)
            throws DocumentoAssenteAvvisaException, DocumentoAssenteBloccaException,
            DocumentiEsistentiPerAreaMagazzinoException {
        logger.debug("--> Enter salvaAreaMagazzino ");

        magazzinoControlloSchedeArticolo.checkInvalidaSchedeArticolo(areaMagazzino, !areaMagazzino.isNew());

        // se documento non è persitente verifica la sua esistenza attraverso
        // gli attributi chiave di dominio

        // allinea l'attributo tipoDocumento di Documento con il tipoDocumento
        // di TipoAreaMagazzino
        areaMagazzino.getDocumento().setTipoDocumento(areaMagazzino.getTipoAreaMagazzino().getTipoDocumento());
        // Setto il flag di inventario se si tratta di inventario
        if (areaMagazzino.getTipoAreaMagazzino().getTipoMovimento() == TipoMovimento.INVENTARIO) {
            areaMagazzino.setTipoOperazione(0);
        } else {
            areaMagazzino.setTipoOperazione(-1);
        }
        // setto il flag aggiornaCostoUltimo
        if (areaMagazzino.isNew()) {
            areaMagazzino.setAggiornaCostoUltimo(areaMagazzino.getTipoAreaMagazzino().isAggiornaCostoUltimo());
        }

        if (areaMagazzino.getDocumento().isNew()) {
            // inizializza il codiceAzienda di documento
            areaMagazzino.getDocumento().setCodiceAzienda(getAzienda());
            // verifica e crea l'associazione con documento
            areaMagazzino = creaAssociazioneConDocumento(areaMagazzino, forzaSalvataggio);
        } else {
            // save di documento
            Documento documento;
            try {
                // Se ho un area contabile e cambio i dati del documento devo generare un errore
                // perchè
                // solamente la contabilità può cambiare il documento.
                AreaContabileLite areaContabile = areaContabileManager
                        .caricaAreaContabileLiteByDocumento(areaMagazzino.getDocumento());
                if (areaContabile != null) {
                    // verifico che non sia stato cambiato nulla del documento
                    Documento oldDocumento = documentiManager.caricaDocumento(areaMagazzino.getDocumento().getId());
                    panjeaDAO.evict(oldDocumento);
                    if (oldDocumento.getCRC() != areaMagazzino.getDocumento().getCRC()) {
                        throw new AreaContabilePresenteException();
                    }
                }
                documento = documentiManager.salvaDocumento(areaMagazzino.getDocumento());
            } catch (DocumentoDuplicateException e) {
                logger.error("--> errore DocumentoDuplicateException in salvaAreaMagazzino", e);
                throw new RuntimeException(e);
            }
            areaMagazzino.setDocumento(documento);
        }

        if (areaMagazzino.getAreaMagazzinoNote().isEmpty()) {
            areaMagazzino.setAreaMagazzinoNote(null);
        }

        AreaMagazzino areaMagazzinoSalvata = null;
        try {
            areaMagazzinoSalvata = panjeaDAO.save(areaMagazzino);
        } catch (Exception e) {
            logger.error("--> errore nel salvare l'area magazzino", e);
            throw new RuntimeException(e);
        }

        logger.debug("--> Exit salvaAreaMagazzino ");
        return areaMagazzinoSalvata;
    }

    @Override
    public void spostaRighe(Set<Integer> righeDaSpostare, Integer idDest) {
        logger.debug("--> Enter spostaRighe");
        // Il metodo deve essere performante. genero update dei soli campi interessati
        try {
            // recupero l'ordinamento base e l'area associata. Se idDest esiste, altrimenti
            // significa che le righe
            // devono essere messe in fondo al doc.
            Double ordinamentoBase = 50000000.0;
            if (idDest != null) {
                Query queryOrdinamentoBase = panjeaDAO
                        .prepareQuery("select rm.ordinamento from RigaMagazzino rm where rm.id=:idDest");
                queryOrdinamentoBase.setParameter("idDest", idDest);
                ordinamentoBase = (Double) panjeaDAO.getSingleResult(queryOrdinamentoBase);
            }

            // Riordino gli id in base all'ordinamento originale, carico anche l'id dell'
            // areamagazzino
            // per avere l'id dell'area da riordinare
            Query queryIdRiordinati = panjeaDAO.prepareQuery(
                    "select rm.id,rm.areaMagazzino.id from RigaMagazzino rm where rm.id in(:ids) order by rm.ordinamento desc");
            queryIdRiordinati.setParameter("ids", righeDaSpostare);
            @SuppressWarnings("unchecked")
            List<Object[]> idRiordinati = panjeaDAO.getResultList(queryIdRiordinati);

            Query queryAggiornamento = panjeaDAO.prepareQuery(
                    "update RigaMagazzino rm set rm.ordinamento=:ordinamento,rm.version=rm.version+1 where rm.id=:idRiga");
            for (Object[] idRiga : idRiordinati) {
                ordinamentoBase--;
                queryAggiornamento.setParameter("ordinamento", ordinamentoBase);
                queryAggiornamento.setParameter("idRiga", idRiga[0]);
                panjeaDAO.executeQuery(queryAggiornamento);
            }
            AreaMagazzino areaMagazzino = new AreaMagazzino();
            areaMagazzino.setId((Integer) idRiordinati.get(0)[1]);
            ordinaRighe(areaMagazzino);
        } catch (DAOException e) {
            logger.error("-->errore nell'impostare l'ordinamento delle righe", e);
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit spostaRighe");
    }

    @Override
    public AreaMagazzino totalizzaDocumento(StrategiaTotalizzazioneDocumento strategia, AreaMagazzino areaMagazzino,
            AreaPartite areaPartite) {
        imponibiliIvaQueryExecutor.setAreaDocumento(areaMagazzino);
        imponibiliIvaQueryExecutor.setQueryString("RigaArticolo.caricaImponibiliIva");
        List<RigaIva> righeIva = areaIvaManager.generaRigheIva(imponibiliIvaQueryExecutor, areaMagazzino.getDocumento(),
                areaMagazzino.isAddebitoSpeseIncasso(), null, areaPartite);
        return totalizzaDocumento(strategia, areaMagazzino, righeIva);
    }

    @Override
    public AreaMagazzino totalizzaDocumento(StrategiaTotalizzazioneDocumento strategia, AreaMagazzino areaMagazzino,
            List<RigaIva> righeIva) {
        logger.debug("--> Enter totalizzaDocumento");

        // se ho un'area contabile non vado a totalizzare perchè il totale
        // documento non deve cambiare
        AreaContabileLite areaContabile = areaContabileManager
                .caricaAreaContabileLiteByDocumento(areaMagazzino.getDocumento());
        if (areaContabile != null) {
            return areaMagazzino;
        }

        totalizzatoriQueryExecutor.setAreaDocumento(areaMagazzino);
        totalizzatoriQueryExecutor.setQueryString("RigaArticolo.caricaByTipo");

        Totalizzatore totalizzatore = strategiaTotalizzazione.getTotalizzatore(strategia);
        Documento documentoTotalizzato = totalizzatore.totalizzaDocumento(areaMagazzino.getDocumento(),
                areaMagazzino.getTotaliArea(), totalizzatoriQueryExecutor, righeIva);
        areaMagazzino.setDocumento(documentoTotalizzato);

        areaMagazzino = calcolaTotalizzatori(areaMagazzino);

        if (logger.isDebugEnabled()) {
            logger.debug("--> Exit totalizzaDocumento con areaMagazzino " + areaMagazzino);
        }
        return areaMagazzino;
    }

    @Override
    public AreaMagazzino validaRigheMagazzino(AreaMagazzino areaMagazzino, AreaPartite areaPartite,
            Boolean areaContabilePresente, boolean forzaStato) throws TotaleDocumentoNonCoerenteException {
        logger.debug("--> Enter confermaRigheMagazzino");

        // Se valido un area magazzino gia validata non faccio nulla
        if (areaMagazzino.getStatoAreaMagazzino() == StatoAreaMagazzino.CONFERMATO
                || areaMagazzino.getStatoAreaMagazzino() == StatoAreaMagazzino.FORZATO) {
            return areaMagazzino;
        }

        List<IGeneratoreRigheArticolo> generatori = generatoriRigaArticoloFactory
                .creaGeneratoriRigheArticolo(EGeneratoreRiga.ALL);

        for (IGeneratoreRigheArticolo generatoreRigheArticolo : generatori) {
            generatoreRigheArticolo.generaRigheArticolo(areaMagazzino);
        }

        // Se l'area iva non torna però ho il flag forzaStato sposterò lo stato
        // in Forzato.
        // Inizialmente lo metto in confermato
        StatoAreaMagazzino statoFinale = StatoAreaMagazzino.CONFERMATO;

        // verifico se creare l'area iva se l'area iva rimane a null vengono
        // create le righeIva ma non salvate.
        // Questo per poter totalizzare il documento anche se questo non ha un
        // area iva
        AreaIva areaIva = null;
        if (!areaContabilePresente) {
            areaIvaManager.generaAreaIvaDaMagazzino(areaMagazzino, areaPartite).getRigheIva();

            areaMagazzino = totalizzaDocumento(
                    areaMagazzino.getTipoAreaMagazzino().getStrategiaTotalizzazioneDocumento(), areaMagazzino,
                    areaPartite);
        } else {
            logger.debug("--> Ho un'area contabile. Lancio solamente dei controlli");
            // Devo sempre generare le righe iva dall'area magazzino
            // per fare i controlli
            imponibiliIvaQueryExecutor.setAreaDocumento(areaMagazzino);
            imponibiliIvaQueryExecutor.setQueryString("RigaArticolo.caricaImponibiliIva");
            List<RigaIva> righeIvaGenerate = areaIvaManager.generaRigheIva(imponibiliIvaQueryExecutor,
                    areaMagazzino.getDocumento(), areaMagazzino.isAddebitoSpeseIncasso(), null, areaPartite);
            if (areaMagazzino.getDocumento().getTipoDocumento().isRigheIvaEnable()) {
                // Ho un'area iva generata dalla contabilità. Devo solamente
                // controllare che l'iva che
                // verrebbe generata dalle righe magazzino sia uguale
                areaIva = areaIvaManager.caricaAreaIvaByDocumento(areaMagazzino.getDocumento());
                if (areaIva == null) {
                    logger.error("--> errore. area iva non presente per il documento " + areaMagazzino.getDocumento());
                    throw new RuntimeException(
                            "area iva non presente anche se righeIvaAbilitate per il documento con id ="
                                    + areaMagazzino.getDocumento().getId());
                }

                if (!areaIva.verificaRigheIva(righeIvaGenerate)) {
                    logger.debug("--> Righe iva non coerenti con il documento. Flag di forza stato= " + forzaStato);
                    if (forzaStato) {
                        statoFinale = StatoAreaMagazzino.FORZATO;
                    } else {
                        sessionContext.setRollbackOnly();
                        throw new TotaleDocumentoNonCoerenteException(righeIvaGenerate);
                    }
                }
            } else {
                // totalizzo il documento e verifico che i totali corrispondano
                Importo totaleDocumento = new Importo(areaMagazzino.getDocumento().getTotale().getCodiceValuta(),
                        areaMagazzino.getDocumento().getTotale().getTassoDiCambio());
                for (RigaIva rigaIva : righeIvaGenerate) {
                    totaleDocumento = totaleDocumento.add(rigaIva.getImponibile(), RigaIva.SCALE_FISCALE)
                            .add(rigaIva.getImposta(), RigaIva.SCALE_FISCALE);
                }
                if (areaMagazzino.getTipoAreaMagazzino().getTipoDocumento().isNotaCreditoEnable()) {
                    totaleDocumento = totaleDocumento.negate();
                }
                if (!totaleDocumento.equals(areaMagazzino.getDocumento().getTotale())) {
                    if (forzaStato) {
                        statoFinale = StatoAreaMagazzino.FORZATO;
                    } else {
                        sessionContext.setRollbackOnly();
                        throw new TotaleDocumentoNonCoerenteException(righeIvaGenerate);
                    }
                }
            }
        }

        // cambio stato area magazzino
        if (statoFinale == StatoAreaMagazzino.CONFERMATO) {
            areaMagazzino = statiAreaMagazzinoManager.cambiaStatoDaProvvisorioInConfermato(areaMagazzino);
        } else {
            areaMagazzino = statiAreaMagazzinoManager.cambiaStatoDaProvvisorioInForzato(areaMagazzino);
        }
        logger.debug("--> nuovo stato area magazzino " + areaMagazzino.getStatoAreaMagazzino());

        // setto l'area validata
        areaMagazzino.getDatiValidazioneRighe().valida(getJecPrincipal().getUserName());

        AreaMagazzino areaMagazzinoSave = null;
        try {
            areaMagazzinoSave = salvaAreaMagazzino(areaMagazzino, true);
        } catch (Exception e) {
            logger.error("--> errore nel salvare l'area magazzino " + areaMagazzinoSave, e);
            throw new RuntimeException(e);
        }
        magazzinoIntraManager.generaAreaIntra(areaMagazzinoSave);

        if (logger.isDebugEnabled()) {
            logger.debug("--> area magazzino salvata " + areaMagazzinoSave);
        }
        logger.debug("--> Exit confermaRigheMagazzino");
        ordinaRighe(areaMagazzinoSave);
        return areaMagazzinoSave;
    }
}