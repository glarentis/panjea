package it.eurotn.panjea.ordini.manager;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
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
import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.ejb.TransactionTimeout;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.magazzino.domain.FormuleRigaArticoloCalculator;
import it.eurotn.panjea.magazzino.domain.IRigaArticoloDocumento;
import it.eurotn.panjea.magazzino.domain.ProvenienzaPrezzoArticolo;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.TipoMovimento;
import it.eurotn.panjea.magazzino.domain.omaggio.TipoOmaggio;
import it.eurotn.panjea.magazzino.exception.FormuleTipoAttributoException;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoManager;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.RigaMagazzinoManager;
import it.eurotn.panjea.magazzino.manager.omaggio.exception.CodiceIvaPerTipoOmaggioAssenteException;
import it.eurotn.panjea.magazzino.manager.rigadocumento.interfaces.RigaDocumentoManager;
import it.eurotn.panjea.magazzino.util.ParametriCreazioneRigaArticolo;
import it.eurotn.panjea.magazzino.util.SedeAreaMagazzinoDTO;
import it.eurotn.panjea.ordini.domain.AttributoRiga;
import it.eurotn.panjea.ordini.domain.OrdineImportato;
import it.eurotn.panjea.ordini.domain.RigaArticolo;
import it.eurotn.panjea.ordini.domain.RigaOrdineImportata;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine.StatoAreaOrdine;
import it.eurotn.panjea.ordini.manager.documento.interfaces.AreaOrdineManager;
import it.eurotn.panjea.ordini.manager.documento.interfaces.RigaOrdineManager;
import it.eurotn.panjea.ordini.manager.interfaces.ImportazioneOrdineImportatoManger;
import it.eurotn.panjea.ordini.util.AreaOrdineFullDTO;
import it.eurotn.panjea.pagamenti.domain.AzioneScontoCommerciale;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.partite.domain.TipoAreaPartita;
import it.eurotn.panjea.partite.manager.interfaces.TipiAreaPartitaManager;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rate.manager.interfaces.AreaRateManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;
import it.eurotn.util.PanjeaEJBUtil;

@Stateless(name = "Panjea.ImportazioneOrdineImportatoManger")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.ImportazioneOrdineImportatoManger")
public class ImportazioneOrdineImportatoMangerBean implements ImportazioneOrdineImportatoManger {

    private static Logger logger = Logger.getLogger(ImportazioneOrdineImportatoMangerBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @Resource
    protected SessionContext sessionContext;

    @EJB
    private AreaMagazzinoManager areaMagazzinoManager;

    @EJB
    private AreaOrdineManager areaOrdineManager;

    @EJB
    private AreaRateManager areaRateManager;

    @EJB
    private TipiAreaPartitaManager tipiAreaPartitaManager;

    @EJB
    private RigaMagazzinoManager rigaMagazzinoManager;

    @EJB
    protected RigaDocumentoManager rigaDocumentoManager;

    @EJB
    private RigaOrdineManager rigaOrdineManager;

    @Override
    @TransactionTimeout(3600)
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void confermaOrdine(OrdineImportato ordineImportato, String codiceValuta, Integer anno,
            long timeStampCreazione) {

        SedeAreaMagazzinoDTO sedeAreaMagazzinoDTO = null;
        if (ordineImportato.getSedeEntita() != null) {
            sedeAreaMagazzinoDTO = areaMagazzinoManager.caricaSedeAreaMagazzinoDTO(ordineImportato.getSedeEntita());
        }

        if (logger.isDebugEnabled()) {
            logger.debug("--> Crea l'area ordine");
        }

        AreaOrdineFullDTO areaOrdineFullDTO = getAreaOrdine(ordineImportato, codiceValuta, anno, timeStampCreazione,
                sedeAreaMagazzinoDTO);

        AreaOrdine areaOrdine = areaOrdineFullDTO.getAreaOrdine();
        AreaRate areaRate = areaOrdineFullDTO.getAreaRate();
        Date dataConsegnaAreaOrdine = null;
        // Importo le righe
        for (RigaOrdineImportata rigaOrdineImportata : ordineImportato.getRighe().values()) {
            // Creo la riga
            Integer idListinoAlternativo = null;
            Integer idListino = null;
            Integer idAgente = null;

            // se ordine produzione devo processare solo le righe distinta
            if (!rigaOrdineImportata.isDistinta() && ordineImportato.getTipoAreaOrdine().isOrdineProduzione()) {
                continue;
            }

            if (areaOrdine.getListinoAlternativo() != null) {
                idListinoAlternativo = areaOrdine.getListinoAlternativo().getId();
            }
            if (areaOrdine.getListino() != null) {
                idListino = areaOrdine.getListino().getId();
            }
            String codiceLingua = null;
            if (areaOrdine.getDocumento().getSedeEntita() != null) {
                codiceLingua = areaOrdine.getDocumento().getSedeEntita().getLingua();
            }
            if (areaOrdine.getAgente() != null) {
                idAgente = areaOrdine.getAgente().getId();
            }

            ParametriCreazioneRigaArticolo parametri = new ParametriCreazioneRigaArticolo();
            if (rigaOrdineImportata.getProvenienzaPrezzo() != null) {
                parametri.setProvenienzaPrezzo(rigaOrdineImportata.getProvenienzaPrezzo());
                parametri.setProvenienzaPrezzoArticolo(ProvenienzaPrezzoArticolo.DOCUMENTO);
            }

            // Se id articolo è un proxy recupero il suo id
            parametri.setIdArticolo(PanjeaEJBUtil.getLazyId(rigaOrdineImportata.getArticolo()));
            parametri.setData(areaOrdine.getDocumento().getDataDocumento());
            if (areaOrdine.getDocumento().getSedeEntita() != null) {
                parametri.setIdSedeEntita(areaOrdine.getDocumento().getSedeEntita().getId());
            }
            Integer idConf = rigaOrdineImportata.getIdDistintaConfigurazione() != null
                    && rigaOrdineImportata.getIdDistintaConfigurazione() != 0
                            ? rigaOrdineImportata.getIdDistintaConfigurazione() : null;
            parametri.setIdConfigurazioneDistinta(idConf);
            parametri.setIdListinoAlternativo(idListinoAlternativo);
            parametri.setIdListino(idListino);
            parametri.setImporto(areaOrdine.getDocumento().getTotale());
            parametri.setCodiceIvaAlternativo(areaOrdine.getCodiceIvaAlternativo());
            parametri.setIdTipoMezzo(null);
            parametri.setIdZonaGeografica(null);
            parametri.setNoteSuDestinazione(false);
            parametri.setTipoMovimento(TipoMovimento.NESSUNO);
            parametri.setCodiceValuta(codiceValuta);
            parametri.setCodiceLingua(codiceLingua);
            parametri.setIdAgente(idAgente);
            parametri.setTipologiaCodiceIvaAlternativo(areaOrdine.getTipologiaCodiceIvaAlternativo());
            parametri.setPercentualeScontoCommerciale(null);
            parametri.setGestioneArticoloDistinta(ordineImportato.getTipoAreaOrdine().isOrdineProduzione());

            // qui genero sempre una riga articolo standard, al momento di
            // salvarla verifico se sono nel caso di un
            // ordine produzione e quindi ho necessità di creare la riga
            // distinta
            RigaArticolo riga = rigaOrdineManager.creaRigaArticolo(parametri);

            riga.setAreaOrdine(areaOrdine);
            riga.setQta(rigaOrdineImportata.getQta());
            riga.setQtaR(rigaOrdineImportata.getQtaR());

            if (rigaOrdineImportata.getDataConsegna() == null) {
                riga.setDataConsegna(areaOrdine.getDataRegistrazione());
            } else {
                riga.setDataConsegna(rigaOrdineImportata.getDataConsegna());
            }
            if (areaOrdine.getTipoAreaOrdine().isOrdineProduzione()) {
                riga.setDataProduzione(rigaOrdineImportata.getDataProduzione());
            }
            dataConsegnaAreaOrdine = dataConsegnaAreaOrdine == null ? riga.getDataConsegna() : dataConsegnaAreaOrdine;
            dataConsegnaAreaOrdine = riga.getDataConsegna().after(dataConsegnaAreaOrdine) ? riga.getDataConsegna()
                    : dataConsegnaAreaOrdine;
            riga.setNumeroRiga(rigaOrdineImportata.getNumeroRiga());

            // Eseguo le formule sugli attributi e sulla qta
            FormuleRigaArticoloCalculator formuleRigaArticoloCalculator = new FormuleRigaArticoloCalculator();
            try {
                IRigaArticoloDocumento rigaCalcolata = formuleRigaArticoloCalculator.calcola(riga);
                riga.setQta(rigaCalcolata.getQta());
                riga.setQtaMagazzino(rigaCalcolata.getQtaMagazzino());
                riga.setAttributi(rigaCalcolata.getAttributi());
                riga.setComponenti(rigaCalcolata.getComponenti());

            } catch (FormuleTipoAttributoException fte) {
                throw new RuntimeException(fte);
            }

            // avvaloro eventuali attributi
            if (rigaOrdineImportata.getAttributi() != null) {
                String[] attributoSingolo = rigaOrdineImportata.getAttributi().trim().split(";");
                for (String attributo : attributoSingolo) {
                    String[] valoriAttributo = attributo.split("=");
                    if (valoriAttributo.length != 2) {
                        throw new GenericException(
                                "gli attributi nella riga importata non sono validi. Attributi non validi: "
                                        + rigaOrdineImportata.getAttributi());
                    }
                    String codiceAttributo = valoriAttributo[0].trim();
                    String valoreAttributo = valoriAttributo[1].trim();

                    for (AttributoRiga attributoRiga : riga.getAttributi()) {
                        if (attributoRiga.getTipoAttributo().getCodice().equals(codiceAttributo)) {
                            attributoRiga.setValore(valoreAttributo);
                            break;
                        }
                    }
                }
            }

            CodicePagamento codicePagamento = areaRate.getCodicePagamento();
            boolean scontoComm = Boolean.FALSE;
            if (codicePagamento != null) {
                scontoComm = codicePagamento.getPercentualeScontoCommerciale() != null
                        && codicePagamento.getPercentualeScontoCommerciale().compareTo(BigDecimal.ZERO) != 0;
            }

            if (parametri.getProvenienzaPrezzo() == null) {
                // Senza provenienza prezzo non calcola il prezzo della riga, è
                // già impostato sulle righe importate.
                Importo prezzoUnitario = riga.getPrezzoUnitario().clone();
                prezzoUnitario.setImportoInValuta(rigaOrdineImportata.getPrezzoUnitarioDeterminato());
                prezzoUnitario.calcolaImportoValutaAzienda(riga.getArticolo().getNumeroDecimaliPrezzo());
                riga.setPrezzoUnitario(prezzoUnitario);
                riga.setPercProvvigione(rigaOrdineImportata.getPercProvvigione());

                // se esiste lo sconto commerciale sul codice pagamento lo
                // inserisco come prima variazione e sposto le
                // altre
                riga.setVariazione1(rigaOrdineImportata.getSconto1Determinato());
                riga.setVariazione2(rigaOrdineImportata.getSconto2Determinato());
                riga.setVariazione3(rigaOrdineImportata.getSconto3Determinato());
                riga.setVariazione4(rigaOrdineImportata.getSconto4Determinato());
                if (scontoComm) {
                    riga.setSconto1Bloccato(true);
                    riga.applicaScontoCommerciale(AzioneScontoCommerciale.INSERISCI,
                            codicePagamento.getPercentualeScontoCommerciale());
                }

                Importo prezzoNetto = riga.getPrezzoUnitario().clone();
                prezzoNetto.setImportoInValuta(rigaOrdineImportata.getPrezzoDeterminato());
                prezzoNetto.calcolaImportoValutaAzienda(riga.getArticolo().getNumeroDecimaliPrezzo());
                riga.setPrezzoNetto(prezzoNetto);

                riga.setNoteRiga(rigaOrdineImportata.getNote());
                riga.setPrezzoUnitarioImportazione(rigaOrdineImportata.getPrezzoUnitario());
                riga.setPrezzoNettoImportazione(rigaOrdineImportata.getPrezzo());
                riga.setVariazione1Importazione(rigaOrdineImportata.getSconto1());
                riga.setVariazione2Importazione(rigaOrdineImportata.getSconto2());
                riga.setVariazione3Importazione(rigaOrdineImportata.getSconto3());
                riga.setVariazione4Importazione(rigaOrdineImportata.getSconto4());
            }

            riga.setTipoOmaggio(rigaOrdineImportata.getOmaggio() != null && rigaOrdineImportata.getOmaggio()
                    ? TipoOmaggio.OMAGGIO : TipoOmaggio.NESSUNO);
            try {
                riga.setCodiceIva(rigaMagazzinoManager.caricaCodiceIvaPerSostituzione(riga));
            } catch (CodiceIvaPerTipoOmaggioAssenteException e) {
                logger.error("-->errore Codice iva per il tipo Omaggio " + riga.getTipoOmaggio() + " assente", e);
                throw new RuntimeException(e);
            }
            try {
                // se la riga è un articolo distinta e l'area è di tipo ordine
                // produzione, allora salvo una riga
                // distinta invece di una riga standard
                // la riga importata se proveniente dall'mrp potrebbe essere
                // distinta anche se l'articolo non è distinta
                // (configurazioni)
                if (riga.getAreaOrdine().getTipoAreaOrdine().isOrdineProduzione() && rigaOrdineImportata.isDistinta()) {
                    riga = (RigaArticolo) salvaRigaDistintaDaRigaImportata(riga, rigaOrdineImportata, parametri);
                    dataConsegnaAreaOrdine = riga.getDataConsegna();
                }
                riga = panjeaDAO.getEntityManager().merge(riga);

                updateRigheCollegate(riga, rigaOrdineImportata);
                logger.info("riga salvata");

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        areaOrdine.setDataConsegna(dataConsegnaAreaOrdine);

        // Confermo il documento
        panjeaDAO.getEntityManager().flush();
        boolean cambiaStato = true;
        if (ordineImportato.isBloccaOrdine()) {
            areaOrdine.setStatoAreaOrdine(StatoAreaOrdine.BLOCCATO);
            areaOrdine.getDatiValidazioneRighe().valida(getJecPrincipal().getUserName());
            cambiaStato = false;
        }
        areaOrdine = areaOrdineManager.validaRigheOrdine(areaOrdine, areaRate, cambiaStato);
        // Cancello l'ordine dagli ordini importati.
        // Lo cancello solamente se l'ordine è presente nella tabella dei
        // backorder, altrimenti è stato creato in
        // memoria e non serve cancellarlo
        if (!ordineImportato.isNew()) {
            Query queryDelete = panjeaDAO.prepareQuery(
                    "delete from OrdineImportato o where o.numero =:ordine and codiceAgente=:codiceAgente");
            queryDelete.setParameter("ordine", ordineImportato.getNumero());
            queryDelete.setParameter("codiceAgente", ordineImportato.getCodiceAgente());
            queryDelete.executeUpdate();
        }
    }

    /**
     * Restituisce l'area ordine presente nell'ordineImportato (caricandola essendoci solo l'id) o
     * ne crea una nuova (salvandola) se l'ordine importato non ha nessuna area.
     *
     * @param ordineImportato
     *            ordineImportato
     * @param codiceValuta
     *            codiceValuta
     * @param anno
     *            anno
     * @param timeStampCreazione
     *            timeStampCreazione
     * @param sedeAreaMagazzinoDTO
     *            sedeAreaMagazzinoDTO
     * @return AreaOrdine
     */
    private AreaOrdineFullDTO getAreaOrdine(OrdineImportato ordineImportato, String codiceValuta, Integer anno,
            long timeStampCreazione, SedeAreaMagazzinoDTO sedeAreaMagazzinoDTO) {
        AreaOrdineFullDTO areaOrdineFullDTO = new AreaOrdineFullDTO();

        AreaOrdine areaOrdine = ordineImportato.getAreaOrdine();
        // se non ho area ordine o se ho un ordine produzione creo sempre una
        // nuova area ordine altrimenti carico
        if (areaOrdine == null || ordineImportato.getTipoAreaOrdine().isOrdineProduzione()) {
            areaOrdine = ordineImportato.creaAreaOrdine(codiceValuta, anno, sedeAreaMagazzinoDTO);
            areaOrdine.setDataCreazioneTimeStamp(timeStampCreazione);
            areaOrdine = areaOrdineManager.salvaAreaOrdine(areaOrdine);
            if (logger.isDebugEnabled()) {
                logger.debug("--> Creata e salvata l'area ordine " + areaOrdine);
            }

            // Crea l'area rate se prevista
            TipoAreaPartita tap = tipiAreaPartitaManager
                    .caricaTipoAreaPartitaPerTipoDocumento(areaOrdine.getDocumento().getTipoDocumento());
            AreaRate areaRate = null;
            if (tap.getId() != null) {
                // Se non ho il codice pagamento settato imposto quello
                // predefinito sulla sedeMagazzino
                if (ordineImportato.getPagamento() == null && sedeAreaMagazzinoDTO != null) {
                    ordineImportato.setPagamento(sedeAreaMagazzinoDTO.getCodicePagamento());
                }
                areaRate = ordineImportato.creaAreaRate(areaOrdine, tap);
                areaRate = areaRateManager.salvaAreaRate(areaRate);

                areaOrdineFullDTO.setAreaRate(areaRate);
            }
        } else {
            areaOrdine = panjeaDAO.loadLazy(AreaOrdine.class, areaOrdine.getId());
            areaOrdine.setDataCreazioneTimeStamp(timeStampCreazione);
            areaOrdine = areaOrdineManager.salvaAreaOrdine(areaOrdine);
        }
        areaOrdineFullDTO.setAreaOrdine(areaOrdine);
        return areaOrdineFullDTO;
    }

    /**
     * @return principal loggato
     */
    private JecPrincipal getJecPrincipal() {
        return ((JecPrincipal) sessionContext.getCallerPrincipal());
    }

    /**
     * Salva una riga distinta con i suoi componenti, partendo da una riga ordine importata.
     *
     * @param rigaDistinta
     *            rigaDistinta
     * @param rigaDistintaImportata
     *            rigaDistintaImportata
     * @param parametri
     *            parametri creazione riga distinta per la creazione dei componenti
     * @return IRigaArticoloDocumento
     */
    private IRigaArticoloDocumento salvaRigaDistintaDaRigaImportata(RigaArticolo rigaDistinta,
            RigaOrdineImportata rigaDistintaImportata, ParametriCreazioneRigaArticolo parametri) {
        // non devo fargli aggiungere nuovamente la qta di attrezzaggio sulla
        // distinta perchè già calcolata dall'mrp
        rigaDistinta.setQtaAttrezzaggio(0.0);

        Set<IRigaArticoloDocumento> newComponentiLivello0 = new HashSet<>();
        Set<IRigaArticoloDocumento> componentiLivello0 = ObjectUtils.defaultIfNull(rigaDistinta.getComponenti(),
                new HashSet<IRigaArticoloDocumento>());

        for (IRigaArticoloDocumento rigaComponente : componentiLivello0) {

            // non azzero la qta di attrezzaggio dei componenti perchè devo
            // considerarli ricreando i componenti
            // dall'anagrafica.

            // rigaComponente.setDataConsegna();
            // ((RigaArticolo) rigaComponente).setDataProduzione(null);

            Set<IRigaArticoloDocumento> newComponentiLivello1 = new HashSet<>();
            rigaComponente.setComponenti(newComponentiLivello1);
            newComponentiLivello0.add(rigaComponente);
        }
        rigaDistinta.setComponenti(newComponentiLivello0);

        rigaDistinta = (RigaArticolo) rigaOrdineManager.getDao(rigaDistinta).salvaRigaOrdine(rigaDistinta);

        return rigaDistinta;
    }

    /**
     * @param rigaArticolo
     *            rigaArticolo a cui collegare le righe
     * @param rigaOrdineImportata
     *            rigaOrdineImportata contiene la lista di righe da collegare
     */
    private void updateRigheCollegate(RigaArticolo rigaArticolo, RigaOrdineImportata rigaOrdineImportata) {
        if (rigaOrdineImportata.getIdRigheDaCollegare() != null) {
            for (int idRigaDaCollegare : rigaOrdineImportata.getIdRigheDaCollegare()) {
                if (idRigaDaCollegare != 0) {
                    RigaArticolo raCollegata = panjeaDAO.loadLazy(RigaArticolo.class, idRigaDaCollegare);
                    rigaArticolo.addRigaOrdineCollegata(raCollegata);
                    // RigaArticolo raCollegata =
                    // panjeaDAO.loadLazy(RigaArticolo.class,
                    // idRigaDaCollegare);
                    // raCollegata.addRigaOrdineCollegata(rigaArticolo);
                    // try {
                    // raCollegata = panjeaDAO.saveWithoutFlush(raCollegata);
                    // } catch (DAOException e) {
                    // logger.error("--> Errore nel salvataggio della riga cliente",
                    // e);
                    // throw new
                    // RuntimeException("--> Errore nel salvataggio della riga cliente",
                    // e);
                    // }
                }
            }
        }
    }

}
