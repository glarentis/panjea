package it.eurotn.panjea.ordini.manager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.lite.AziendaLite;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.anagrafica.manager.depositi.interfaces.DepositiManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.AziendeManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.SediAziendaManager;
import it.eurotn.panjea.magazzino.domain.ProvenienzaPrezzoArticolo;
import it.eurotn.panjea.magazzino.domain.Sconto;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.ProvenienzaPrezzo;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.ParametriCalcoloPrezzi;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.PoliticaPrezzo;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.RisultatoPrezzo;
import it.eurotn.panjea.magazzino.manager.interfaces.MagazzinoSettingsManager;
import it.eurotn.panjea.magazzino.manager.moduloprezzo.interfaces.PrezzoArticoloCalculator;
import it.eurotn.panjea.ordini.domain.OrdineImportato;
import it.eurotn.panjea.ordini.domain.OrdineImportato.EProvenienza;
import it.eurotn.panjea.ordini.domain.RigaOrdineImportata;
import it.eurotn.panjea.ordini.domain.documento.TipoAreaOrdine;
import it.eurotn.panjea.ordini.manager.documento.interfaces.TipiAreaOrdineManager;
import it.eurotn.panjea.ordini.manager.interfaces.ImportazioneOrdineImportatoManger;
import it.eurotn.panjea.ordini.manager.interfaces.ImportazioneOrdiniManager;
import it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaOrdiniImportati;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.service.interfaces.PanjeaMessage;
import it.eurotn.security.JecPrincipal;

@Stateless(name = "Panjea.ImportazioneOrdiniManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.ImportazioneOrdiniManager")
public class ImportazioneOrdiniManagerBean implements ImportazioneOrdiniManager {

    private static Logger logger = Logger.getLogger(ImportazioneOrdiniManagerBean.class);

    @Resource
    private SessionContext sessionContext;

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private PanjeaMessage panjeaMessage;

    @EJB
    private PrezzoArticoloCalculator prezzoArticoloCalculator;

    @EJB
    private TipiAreaOrdineManager tipiAreaOrdineManager;

    @EJB
    private AziendeManager aziendeManager;

    @EJB
    private SediAziendaManager sediAziendaManager;

    @EJB
    private MagazzinoSettingsManager magazzinoSettingsManager;

    @EJB
    private ImportazioneOrdineImportatoManger ordineImportatoManger;

    @EJB
    private DepositiManager depositiManager;

    /**
     * aggiorna i prezzi per le righe imporate.
     *
     * @param parametri
     *            parametri per ricercare le righe per le quali aggiornare i prezzi
     */
    @Override
    public void aggiornaPrezziDeterminatiOrdiniImportati(ParametriRicercaOrdiniImportati parametri) {

        List<RigaOrdineImportata> righeOrdineImportate = caricaRigheOrdineImportate(parametri);

        for (RigaOrdineImportata rigaOrdine : righeOrdineImportate) {

            if (rigaOrdine.getArticolo() == null) {
                continue;
            }

            if (rigaOrdine.getOrdine().getPagamento() == null) {
                continue;
            }

            if (rigaOrdine.getOrdine().getEntita() == null) {
                continue;
            }

            if (rigaOrdine.getOrdine().getSedeEntita() == null) {
                continue;
            }

            Integer idListinoAlternativo = null;
            Integer idListino = null;

            if (rigaOrdine.getOrdine().getListinoAlternativo() != null) {
                idListinoAlternativo = rigaOrdine.getOrdine().getListinoAlternativo().getId();
            }
            if (rigaOrdine.getOrdine().getListino() != null) {
                idListino = rigaOrdine.getOrdine().getListino().getId();
            }

            rigaOrdine.setPrezzoDeterminato(BigDecimal.ZERO);

            Integer idAgente = rigaOrdine.getOrdine().getAgente() != null ? rigaOrdine.getOrdine().getAgente().getId()
                    : null;

            // creo i parametri calcolo prezzo
            ParametriCalcoloPrezzi parametriCalcoloPrezzi = new ParametriCalcoloPrezzi(rigaOrdine.getArticolo().getId(),
                    rigaOrdine.getOrdine().getData(), idListino, idListinoAlternativo,
                    rigaOrdine.getOrdine().getEntita().getId(), rigaOrdine.getOrdine().getSedeEntita().getId(), null,
                    null, ProvenienzaPrezzo.LISTINO, null, null, ProvenienzaPrezzoArticolo.DOCUMENTO, "EUR", idAgente,
                    null);
            PoliticaPrezzo politicaPrezzo = prezzoArticoloCalculator.calcola(parametriCalcoloPrezzi);

            // rigaOrdine.setPrezzoUnitarioDeterminato(politicaPrezzo.getPrezzoNetto(rigaOrdine.getQta()));
            rigaOrdine.setPrezzoUnitarioDeterminato(
                    politicaPrezzo.getPrezzi().getRisultatoPrezzo(rigaOrdine.getQta()).getValue());
            rigaOrdine.setPercProvvigione(BigDecimal.ZERO);
            RisultatoPrezzo<BigDecimal> risultatoProvvigione = politicaPrezzo.getProvvigioni()
                    .getRisultatoPrezzo(rigaOrdine.getQta());
            if (risultatoProvvigione != null) {
                rigaOrdine.setPercProvvigione(risultatoProvvigione.getValue());
            }

            RisultatoPrezzo<Sconto> sezioneSconto = politicaPrezzo.getSconti().getRisultatoPrezzo(rigaOrdine.getQta());

            if (sezioneSconto != null && sezioneSconto.getValue() != null) {
                Sconto sconto = sezioneSconto.getValue();
                try {
                    if (sconto.getSconto1() != null) {
                        rigaOrdine.setSconto1Determinato(sconto.getSconto1());
                    }
                    if (sconto.getSconto2() != null) {
                        rigaOrdine.setSconto2Determinato(sconto.getSconto2());
                    }
                    if (sconto.getSconto3() != null) {
                        rigaOrdine.setSconto3Determinato(sconto.getSconto3());
                    }
                    if (sconto.getSconto4() != null) {
                        rigaOrdine.setSconto4Determinato(sconto.getSconto4());
                    }
                } catch (Exception e) {
                    logger.error("--> SCONTO: ", e);
                }
            }

            try {
                panjeaDAO.saveWithoutFlush(rigaOrdine);
            } catch (Exception e) {
                logger.error("--> errore durante il salvataggio della riga ordine importata", e);
                throw new RuntimeException("errore durante il salvataggio della riga ordine importata", e);
            }
        }
        panjeaDAO.getEntityManager().flush();
    }

    @Override
    public void aggiornaRigheOrdineImportate(ParametriRicercaOrdiniImportati parametri) {
        // Devo aggiornare i riferimenti della tabella degli ordini importati
        StringBuffer fileContent = new StringBuffer();
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader in = null;
        try {
            // leggo il file per l'sql per l'aggiornamento.
            is = ImportazioneOrdiniManagerBean.class.getClassLoader()
                    .getResourceAsStream("it/eurotn/panjea/ordini/manager/sqlbuilder/AggiornaOrdiniImportati.sql");
            isr = new InputStreamReader(is);
            in = new BufferedReader(isr);
            String line = "";
            while ((line = in.readLine()) != null) {
                if (!line.startsWith("--")) {
                    fileContent.append(line);
                }
            }

            // se DOLCELIT aggiungo le queries per impostare di default il codice pagamento presente sull'entità in
            // panjea invece di quello proveniente dall'importazione
            if ("dolcelit".equalsIgnoreCase(getAzienda())) {
                is = ImportazioneOrdiniManagerBean.class.getClassLoader().getResourceAsStream(
                        "it/eurotn/panjea/ordini/manager/sqlbuilder/AggiornaOrdiniImportatiDolcelit.sql");
                isr = new InputStreamReader(is);
                in = new BufferedReader(isr);
                String lineDolce = "";
                while ((lineDolce = in.readLine()) != null) {
                    if (!lineDolce.startsWith("--")) {
                        fileContent.append(lineDolce);
                    }
                }
            }

        } catch (FileNotFoundException e) {
            logger.error("--> errore nell'aprire il file contenente la query", e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.error("-->errore nel leggere la riga del file", e);
            throw new RuntimeException(e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (isr != null) {
                    isr.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                logger.error("--> Errore nel chiudere lo stream sulla risorsa", e);
                throw new RuntimeException("Errore nel chiudere lo stream sulla risorsa", e);
            }
        }
        logger.debug("--> Exit getSql");

        String[] line = fileContent.toString().split(";");
        List<String> queries = new ArrayList<String>(Arrays.asList(line));
        for (String query : queries) {
            Query hibQuery = panjeaDAO.getEntityManager().createNativeQuery(query);
            hibQuery.setParameter("provenienza", parametri.getProvenienza().ordinal());
            int tutteProvenienze = 0;
            if (parametri.getProvenienza() == EProvenienza.TUTTI) {
                tutteProvenienze = 1;
            }
            hibQuery.setParameter("tutteProvenienze", tutteProvenienze);
            try {
                panjeaDAO.executeQuery(hibQuery);
            } catch (DAOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void cancellaOrdiniImportati(Collection<String> numeroOrdini) {
        logger.debug("--> Enter cancellaOrdiniImportati");
        Query queryDelete = panjeaDAO.prepareQuery("delete from OrdineImportato o where o.numero in (:ordini)");
        queryDelete.setParameter("ordini", numeroOrdini);
        queryDelete.executeUpdate();
        logger.debug("--> Exit cancellaOrdiniImportati");
    }

    @Override
    public void cancellaOrdiniImportatiPerAgente(String codiceAgente) {
        logger.debug("--> Enter cancellaOrdiniImportatiPerAgente");
        Query query = panjeaDAO.prepareNamedQuery("OrdineImportato.cancellaByCodiceAgente");
        query.setParameter("codiceAgente", codiceAgente);
        try {
            panjeaDAO.executeQuery(query);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit cancellaOrdiniImportatiPerAgente");
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RigaOrdineImportata> caricaRigheOrdineImportate(ParametriRicercaOrdiniImportati parametri) {

        StringBuilder sb = new StringBuilder("select riga from RigaOrdineImportata riga ");
        if (parametri.getNumeroOrdine() != null) {
            sb.append("where riga.ordine.numero=");
            sb.append(parametri.getNumeroOrdine());
        }
        Query query = panjeaDAO.prepareQuery(sb.toString());
        List<RigaOrdineImportata> result;
        try {
            result = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            logger.error("-->errore nel caricare le righe importate ", e);
            throw new RuntimeException("-->errore nel caricare le righe importate ", e);
        }
        return result;
    }

    @Override
    public boolean checkOrdineUnivoco(String numeroOrdine, String codiceCliente) {
        // Controllo gli ordini già confermati
        Query queryOrdine = panjeaDAO.prepareQuery(
                "select count(o) from AreaOrdine o where o.riferimentiOrdine.numeroOrdine=:ordineCliente and o.documento.entita.codice=:codiceCliente");
        queryOrdine.setParameter("ordineCliente", numeroOrdine);
        queryOrdine.setParameter("codiceCliente", Integer.parseInt(codiceCliente));
        long numOrdiniPresenti = 0;
        try {
            numOrdiniPresenti = (Long) panjeaDAO.getSingleResult(queryOrdine);
        } catch (DAOException e) {
            logger.error("-->errore nel cercare eventuali ordini presenti durante l'importazione " + numeroOrdine, e);
            throw new RuntimeException(e);
        }

        // Controllo i backOrder
        Query queryBackOrder = panjeaDAO.prepareQuery(
                "select count(o) from OrdineImportato o where o.numero=:numeroOrdine and o.codiceEntita=:codiceCliente");
        queryBackOrder.setParameter("numeroOrdine", numeroOrdine);
        queryBackOrder.setParameter("codiceCliente", codiceCliente);
        long numBackOrderPresenti = 0;
        try {
            numBackOrderPresenti = (Long) panjeaDAO.getSingleResult(queryBackOrder);
        } catch (DAOException e) {
            logger.error("-->errore nel cercare eventuali backorder presenti durante l'importazione " + numeroOrdine,
                    e);
            throw new RuntimeException(e);
        }

        // Spedisco un messaggio sulla coda
        if (numOrdiniPresenti + numBackOrderPresenti > 0) {
            panjeaMessage.send(
                    "Documento già importato. Controllare nei backorder o negli order gli ordini del cliente n. "
                            + codiceCliente + " . Riferimento ordine cliente n. " + numeroOrdine,
                    PanjeaMessage.MESSAGE_GENERIC_SELECTOR);
        }
        return numOrdiniPresenti + numBackOrderPresenti == 0;
    }

    @Override
    public Long confermaOrdiniImportati(Collection<OrdineImportato> ordiniDaConfermare) {
        logger.debug("--> Enter confermaOrdiniImportati");

        Long timeStampCreazione = Calendar.getInstance().getTimeInMillis();
        List<OrdineImportato> ordini = new ArrayList<OrdineImportato>(ordiniDaConfermare);
        if (ordini.size() == 0) {
            return new Long(0);
        }
        // Carico anno movimenti
        Integer anno = magazzinoSettingsManager.caricaMagazzinoSettings().getAnnoCompetenza();
        // Carico il deposito principale
        AziendaLite azienda = aziendeManager.caricaAzienda();
        DepositoLite depositoPrincipale = depositiManager.caricaDepositoPrincipale().getDepositoLite();

        for (OrdineImportato ordineImportato : ordini) {
            // Creo il documento. Se non ho impostato il tipo area ordine devo caricarlo
            if (ordineImportato.getTipoAreaOrdine() == null) {
                TipoDocumento tipoDocumento = ordineImportato.getTipoDocumento();
                TipoAreaOrdine tipoAreaOrdine = tipiAreaOrdineManager
                        .caricaTipoAreaOrdinePerTipoDocumento(tipoDocumento.getId());
                ordineImportato.setTipoAreaOrdine(tipoAreaOrdine);
            }

            ordineImportato.setDeposito(ObjectUtils.defaultIfNull(ordineImportato.getDeposito(), depositoPrincipale));

            try {
                ordineImportatoManger.confermaOrdine(ordineImportato, azienda.getCodiceValuta(), anno,
                        timeStampCreazione);
            } catch (Exception e) {
                logger.error("Errore nella comferma degli ordiniImportati", e);
                throw new RuntimeException(e);
            }
        }
        logger.debug("--> Exit confermaOrdiniImportati");
        return timeStampCreazione;

    }

    /**
     * @return codice azienda
     */
    private String getAzienda() {
        return ((JecPrincipal) sessionContext.getCallerPrincipal()).getCodiceAzienda();
    }

    @Override
    public List<RigaOrdineImportata> salvaRigaOrdineImportata(RigaOrdineImportata rigaOrdine) {
        logger.debug("--> Enter salvaRigaOrdineImportata");

        List<RigaOrdineImportata> righeSalvate = new ArrayList<RigaOrdineImportata>();

        OrdineImportato ordineOld;
        try {
            ordineOld = panjeaDAO.load(OrdineImportato.class, rigaOrdine.getOrdine().getId());
            boolean changeOrdine = ordineOld.getPagamento() == null && rigaOrdine.getOrdine().getPagamento() != null;
            if (!changeOrdine) {
                changeOrdine = !ordineOld.getPagamento().equals(rigaOrdine.getOrdine().getPagamento());
            }

            RigaOrdineImportata rigaOrdineSalvata = null;
            rigaOrdineSalvata = panjeaDAO.save(rigaOrdine);

            // verifico se è cambiato il codice di pagamento
            if (changeOrdine) {
                ordineOld = panjeaDAO.load(OrdineImportato.class, rigaOrdine.getOrdine().getId());
                ordineOld.setPagamento(rigaOrdine.getOrdine().getPagamento());
                OrdineImportato ordine = panjeaDAO.save(ordineOld);
                righeSalvate.addAll(ordine.getRighe().values());
            } else {
                righeSalvate.add(rigaOrdineSalvata);
            }
        } catch (DAOException e) {
            logger.error("-->errore durante il salvataggio della riga ordine importazione", e);
            throw new RuntimeException("errore durante il salvataggio della riga ordine importazione", e);
        }

        logger.debug("--> Exit salvaRigaOrdineImportata");
        return righeSalvate;
    }
}
