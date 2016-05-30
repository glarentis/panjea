package it.eurotn.panjea.magazzino.manager.documento.fatturazione;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.anagrafica.manager.depositi.interfaces.DepositiManager;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino.ETipologiaGenerazioneDocumentoFatturazione;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzinoLite;
import it.eurotn.panjea.magazzino.exception.TipiDocumentiFatturazionePAAssentiException;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.FatturazioneDifferitaGenerator;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

public abstract class AbstractFatturazioneDifferitaGenerator implements FatturazioneDifferitaGenerator {

    private static final Logger LOGGER = Logger.getLogger(AbstractFatturazioneDifferitaGenerator.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @Resource
    private SessionContext context;

    @EJB
    private DepositiManager depositiManager;

    @SuppressWarnings("unchecked")
    @Override
    public List<AreaMagazzinoFatturazione> caricaAreeMagazzinoFatturazione(List<AreaMagazzinoLite> areeDaFatturare) {

        StringBuilder areeIds = new StringBuilder();
        for (AreaMagazzinoLite areaMagazzinoLite : areeDaFatturare) {
            if (!areeIds.toString().isEmpty()) {
                areeIds.append(",");
            }
            areeIds.append(areaMagazzinoLite.getId());
        }

        // verifico prima se ci sono tipo documento fatturazione PA non configurati per le aree che hanno entità PA
        List<TipoDocumento> tipiDoc = caricaTipiDocumentiSenzaTipoDocumentoFatturazionePA(areeDaFatturare);
        if (tipiDoc != null && !tipiDoc.isEmpty()) {
            throw new TipiDocumentiFatturazionePAAssentiException(tipiDoc);
        }

        // devo fare 2 query perchè in hql la join tra AreaMagazzino e TipoAreaMagazzino del tipo documento di
        // fatturazione è vecchio stile e quindi non mi permette di fare una left join ma solo inner.

        // query per le aree no PA
        org.hibernate.Query queryNoPA = ((Session) panjeaDAO.getEntityManager().getDelegate())
                .createQuery(getQueryAreeFatturazioneNoPA(areeIds.toString()));
        queryNoPA.setResultTransformer(Transformers.aliasToBean(RigaMagazzinoFatturazione.class));

        // query per le aree PA
        org.hibernate.Query queryPA = ((Session) panjeaDAO.getEntityManager().getDelegate())
                .createQuery(getQueryAreeFatturazionePA(areeIds.toString()));
        queryPA.setResultTransformer(Transformers.aliasToBean(RigaMagazzinoFatturazione.class));

        Set<RigaMagazzinoFatturazione> righeFatturazione = new HashSet<RigaMagazzinoFatturazione>();
        try {
            List<RigaMagazzinoFatturazione> listResultsNoPA = queryNoPA.list();
            List<RigaMagazzinoFatturazione> listResultsPA = queryPA.list();
            righeFatturazione.addAll(listResultsNoPA);
            righeFatturazione.addAll(listResultsPA);
        } catch (Exception e) {
            LOGGER.error("-->errore durante il caricamento dei documenti da fatturare.", e);
            throw new RuntimeException("errore durante il caricamento dei documenti da fatturare.", e);
        }

        Map<Integer, Object[]> mapSediMagaPrincipaliPerEntita = caricaSediMagazzinoPrincipali(areeIds.toString());

        Map<Integer, AreaMagazzinoFatturazione> mapAree = new HashMap<Integer, AreaMagazzinoFatturazione>();

        for (RigaMagazzinoFatturazione rigaMagazzinoFatturazione : righeFatturazione) {

            // aggiunge alla rigaFatturazione le informazioni per la sede principale
            Integer idEntita = rigaMagazzinoFatturazione.getEntita().getId();
            Object[] sedeMagazzinoPrincipaleEntita = mapSediMagaPrincipaliPerEntita.get(idEntita);
            rigaMagazzinoFatturazione = getRigaMagazzinoFatturazioneConSedeMagazzinoPrincipale(
                    rigaMagazzinoFatturazione, sedeMagazzinoPrincipaleEntita);

            // aggiunge aree e righe nella mappa da processare
            AreaMagazzinoFatturazione area = mapAree.get(rigaMagazzinoFatturazione.getIdArea());
            if (area == null) {
                area = new AreaMagazzinoFatturazione(rigaMagazzinoFatturazione);
                mapAree.put(area.getAreaMagazzino().getId(), area);
            }
            area.addRigaMagazzino(rigaMagazzinoFatturazione.getRigaMagazzino());
        }

        List<AreaMagazzinoFatturazione> listAree = new ArrayList<AreaMagazzinoFatturazione>();
        listAree.addAll(mapAree.values());
        return listAree;
    }

    /**
     * @return deposito principale dell'azienda loggata
     * @throws RuntimeException
     */
    protected Deposito caricaDepositoAziendaPrincipale() {
        return depositiManager.caricaDepositoPrincipale();
    }

    /**
     * Carica le sedi magazzino principali delle aree magazzino scelte.
     *
     * @param areeIds
     *            la stringa che rappresenta la lista degli id delle aree magazzino separati da virgola
     * @return Map<Integer, Object[]> una mappa con chiave l'id dell'entità, mentre come valore un array con i dati
     *         della sede magazzino
     */
    @SuppressWarnings("unchecked")
    private Map<Integer, Object[]> caricaSediMagazzinoPrincipali(String areeIds) {
        StringBuilder sb = new StringBuilder();
        sb.append("select distinct ");
        sb.append("se.entita.id, ");
        sb.append("se.id, ");
        sb.append("se.version, ");
        sb.append("sm.id, ");
        sb.append("sm.version, ");
        sb.append("sm.tipologiaGenerazioneDocumentoFatturazione ");
        sb.append("from SedeMagazzino sm,AreaMagazzino am join sm.sedeEntita se ");
        sb.append("where se.tipoSede.sedePrincipale=true ");
        sb.append("and am.documento.entita.id=se.entita.id ");
        sb.append("and am.id in (" + areeIds + ") ");

        Map<Integer, Object[]> mapSediMagaPrincipaliPerEntita = new HashMap<Integer, Object[]>();
        List<Object[]> sediMagazzinoPrincipali = null;
        Query query = panjeaDAO.prepareQuery(sb.toString());
        try {
            sediMagazzinoPrincipali = query.getResultList();
        } catch (Exception e) {
            LOGGER.error("-->errore durante il caricamento dei documenti da fatturare.", e);
            throw new RuntimeException("errore durante il caricamento dei documenti da fatturare.", e);
        }
        for (Object[] objects : sediMagazzinoPrincipali) {
            mapSediMagaPrincipaliPerEntita.put((Integer) objects[0], objects);
        }
        return mapSediMagaPrincipaliPerEntita;
    }

    @SuppressWarnings("unchecked")
    private List<TipoDocumento> caricaTipiDocumentiSenzaTipoDocumentoFatturazionePA(
            List<AreaMagazzinoLite> areeDaFatturare) {

        StringBuilder areeIds = new StringBuilder();
        for (AreaMagazzinoLite areaMagazzinoLite : areeDaFatturare) {
            if (!areeIds.toString().isEmpty()) {
                areeIds.append(",");
            }
            areeIds.append(areaMagazzinoLite.getId());
        }

        StringBuilder sb = new StringBuilder();
        sb.append("select distinct am.tipoAreaMagazzino.tipoDocumento ");
        sb.append("from AreaMagazzino am ");
        sb.append("where am.id in (" + areeIds.toString() + ") and ");
        sb.append("am.tipoAreaMagazzino.tipoDocumentoPerFatturazionePA is null and ");
        sb.append("am.documento.entita.fatturazionePA = true ");

        Query query = panjeaDAO.prepareQuery(sb.toString());
        List<TipoDocumento> tipiDoc = new ArrayList<TipoDocumento>();
        try {
            tipiDoc = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            LOGGER.error("--> errore durante il caricamento dei tipi documento", e);
            throw new RuntimeException("errore durante il caricamento dei tipi documento", e);
        }

        return tipiDoc;
    }

    /**
     *
     * @return codice azienda loggata
     */
    private String getCodiceAzienda() {
        JecPrincipal jecPrincipal = (JecPrincipal) context.getCallerPrincipal();
        return jecPrincipal.getCodiceAzienda();
    }

    private String getQueryAreeFatturazioneNoPA(String areeIds) {
        StringBuilder sb = new StringBuilder();
        sb.append("select am.id as idArea, ");
        sb.append(" am.version as versionArea, ");
        sb.append(" am.dataRegistrazione as dataRegistrazioneArea, ");
        sb.append("	am.raggruppamentoBolle as raggruppamentoBolle, ");
        sb.append("	sedeEnt.ereditaDatiCommerciali as ereditaDatiCommerciali, ");
        sb.append("	sedeEnt.sede.descrizione as descrizioneSedeArea, ");
        sb.append("	sedeEnt.id as idSedeEntita, ");
        sb.append("	sedeEnt.version as versionSedeEntita, ");
        sb.append("	sm.id as idSedeMagazzino, ");
        sb.append("	sm.version as versionSedeMagazzino, ");
        sb.append(
                "	sm.tipologiaGenerazioneDocumentoFatturazione as tipologiaGenerazioneDocumentoFatturazioneSedeMagazzino, ");
        sb.append("	sedeEnt.entita.id as idEntita, ");
        sb.append("	sedeEnt.entita.version as versionEntita, ");
        sb.append("	am.documento.entita.codice as codiceEntitaArea, ");
        sb.append("	am.documento.entita.anagrafica.denominazione as denominazioneEntitaArea, ");
        sb.append("	am.tipoAreaMagazzino.tipoDocumentoPerFatturazione as tipoDocumentoPerFatturazione, ");
        sb.append(
                " am.tipoAreaMagazzino.tipoDocumentoPerFatturazioneDescrizioneMaschera as tipoDocumentoPerFatturazioneDescrizioneMaschera, ");
        sb.append(" am.documento.dataDocumento as dataDocumento, ");
        sb.append(" am.documento.codice as numeroDocumento, ");
        sb.append(" am.documento.tipoDocumento.codice as codiceTipoDocumento, ");
        sb.append(" am.documento.tipoDocumento.descrizione as descrizioneTipoDocumento, ");
        sb.append(" ar.codicePagamento.id as idCodicePagamento, ");
        sb.append(" ar.codicePagamento.version as versionCodicePagamento, ");
        sb.append(" ar.codicePagamento.importoSpese as importoSpese, ");
        sb.append(" am.addebitoSpeseIncasso as addebitoSpeseIncasso, ");
        sb.append(" ar.speseIncasso as speseIncasso, ");
        sb.append(" am.annoMovimento as annoMovimento, ");
        sb.append(" am.documento.totale.codiceValuta as codiceValuta, ");
        sb.append(" righe as rigaMagazzino, ");
        sb.append(" tacFatturazione.id as idTipoAreaMagazzinoPerFatturazione, ");
        sb.append(" tacFatturazione.version as versionTipoAreaMagazzinoPerFatturazione ");
        sb.append(
                "from AreaMagazzino am, TipoAreaMagazzino tacFatturazione, SedeMagazzino sm, AreaRate ar join am.righeMagazzino righe join am.documento doc join  doc.sedeEntita sedeEnt left join fetch righe.articolo art left join fetch righe.attributi ");
        sb.append("where am.id in (" + areeIds.toString() + ") and ");
        sb.append(" ar.documento = am.documento and ");
        sb.append(" am.documento.sedeEntita = sm.sedeEntita and ");
        sb.append(" tacFatturazione.tipoDocumento = am.tipoAreaMagazzino.tipoDocumentoPerFatturazione and ");
        sb.append(" am.documento.entita.fatturazionePA = false ");
        return sb.toString();
    }

    private String getQueryAreeFatturazionePA(String areeIds) {
        StringBuilder sb = new StringBuilder();
        sb.append("select am.id as idArea, ");
        sb.append(" am.version as versionArea, ");
        sb.append(" am.dataRegistrazione as dataRegistrazioneArea, ");
        sb.append("	am.raggruppamentoBolle as raggruppamentoBolle, ");
        sb.append("	sedeEnt.ereditaDatiCommerciali as ereditaDatiCommerciali, ");
        sb.append("	sedeEnt.sede.descrizione as descrizioneSedeArea, ");
        sb.append("	sedeEnt.id as idSedeEntita, ");
        sb.append("	sedeEnt.version as versionSedeEntita, ");
        sb.append("	sm.id as idSedeMagazzino, ");
        sb.append("	sm.version as versionSedeMagazzino, ");
        sb.append(
                "	sm.tipologiaGenerazioneDocumentoFatturazione as tipologiaGenerazioneDocumentoFatturazioneSedeMagazzino, ");
        sb.append("	sedeEnt.entita.id as idEntita, ");
        sb.append("	sedeEnt.entita.version as versionEntita, ");
        sb.append("	am.documento.entita.codice as codiceEntitaArea, ");
        sb.append("	am.documento.entita.anagrafica.denominazione as denominazioneEntitaArea, ");
        sb.append("	am.tipoAreaMagazzino.tipoDocumentoPerFatturazionePA as tipoDocumentoPerFatturazione, ");
        sb.append(
                " am.tipoAreaMagazzino.tipoDocumentoPerFatturazioneDescrizioneMaschera as tipoDocumentoPerFatturazioneDescrizioneMaschera, ");
        sb.append(" am.documento.dataDocumento as dataDocumento, ");
        sb.append(" am.documento.codice as numeroDocumento, ");
        sb.append(" am.documento.tipoDocumento.codice as codiceTipoDocumento, ");
        sb.append(" am.documento.tipoDocumento.descrizione as descrizioneTipoDocumento, ");
        sb.append(" ar.codicePagamento.id as idCodicePagamento, ");
        sb.append(" ar.codicePagamento.version as versionCodicePagamento, ");
        sb.append(" ar.codicePagamento.importoSpese as importoSpese, ");
        sb.append(" am.addebitoSpeseIncasso as addebitoSpeseIncasso, ");
        sb.append(" ar.speseIncasso as speseIncasso, ");
        sb.append(" am.annoMovimento as annoMovimento, ");
        sb.append(" am.documento.totale.codiceValuta as codiceValuta, ");
        sb.append(" righe as rigaMagazzino, ");
        sb.append(" tacFatturazionePA.id as idTipoAreaMagazzinoPerFatturazione, ");
        sb.append(" tacFatturazionePA.version as versionTipoAreaMagazzinoPerFatturazione ");
        sb.append(
                "from AreaMagazzino am,TipoAreaMagazzino tacFatturazionePA, SedeMagazzino sm, AreaRate ar join am.righeMagazzino righe join am.documento doc join  doc.sedeEntita sedeEnt left join fetch righe.articolo art left join fetch righe.attributi ");
        sb.append("where am.id in (" + areeIds.toString() + ") and ");
        sb.append(" ar.documento = am.documento and ");
        sb.append(" am.documento.sedeEntita = sm.sedeEntita and ");
        sb.append(" tacFatturazionePA.tipoDocumento = am.tipoAreaMagazzino.tipoDocumentoPerFatturazionePA and ");
        sb.append(" am.documento.entita.fatturazionePA = true ");
        return sb.toString();
    }

    /**
     * Aggiunge i dati della sede magazzino principale e della sede entità principale alla riga magazzino fatturazione.
     *
     * @param rigaMagazzinoFatturazione
     *            la riga a cui aggiungere i dati della sede principale
     * @param sedeMagazzinoPrincipale
     *            l'array contenente i dati utili della sede principale
     * @return RigaMagazzinoFatturazione aggiornata
     */
    private RigaMagazzinoFatturazione getRigaMagazzinoFatturazioneConSedeMagazzinoPrincipale(
            RigaMagazzinoFatturazione rigaMagazzinoFatturazione, Object[] sedeMagazzinoPrincipale) {

        // sedeMagazzinoPrincipale[0] è l'id entità
        Integer idSedeEntitaPrincipale = (Integer) sedeMagazzinoPrincipale[1];
        Integer versionSedeEntitaPrincipale = (Integer) sedeMagazzinoPrincipale[2];
        Integer idSedeMagazzinoPrincipale = (Integer) sedeMagazzinoPrincipale[3];
        Integer versionSedeMagazzinoPrincipale = (Integer) sedeMagazzinoPrincipale[4];
        ETipologiaGenerazioneDocumentoFatturazione tipologiaGenerazioneDocumentoFatturazione = (ETipologiaGenerazioneDocumentoFatturazione) sedeMagazzinoPrincipale[5];

        rigaMagazzinoFatturazione.setIdSedeEntitaPrincipale(idSedeEntitaPrincipale);
        rigaMagazzinoFatturazione.setVersionSedeEntitaPrincipale(versionSedeEntitaPrincipale);
        rigaMagazzinoFatturazione.setIdSedeMagazzinoPrincipale(idSedeMagazzinoPrincipale);
        rigaMagazzinoFatturazione.setVersionSedeMagazzinoPrincipale(versionSedeMagazzinoPrincipale);
        rigaMagazzinoFatturazione.setTipologiaGenerazioneDocumentoFatturazioneSedeMagazzinoPrincipale(
                tipologiaGenerazioneDocumentoFatturazione);

        rigaMagazzinoFatturazione.getSedeEntitaPrincipale();
        rigaMagazzinoFatturazione.getSedeMagazzino();

        return rigaMagazzinoFatturazione;
    }
}
