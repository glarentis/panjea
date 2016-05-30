package it.eurotn.panjea.magazzino.manager.rigadocumento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.ConversioneUnitaMisura;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.anagrafica.manager.interfaces.AnagraficaTabelleManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.AziendeManager;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.AttributoArticolo;
import it.eurotn.panjea.magazzino.domain.AttributoRigaArticolo;
import it.eurotn.panjea.magazzino.domain.Giacenza;
import it.eurotn.panjea.magazzino.domain.IRigaArticoloDocumento;
import it.eurotn.panjea.magazzino.domain.descrizionilingua.DescrizioneLinguaArticoloEstesa;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.ParametriCalcoloPrezzi;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.PoliticaPrezzo;
import it.eurotn.panjea.magazzino.domain.omaggio.TipoOmaggio;
import it.eurotn.panjea.magazzino.manager.interfaces.GiacenzaManager;
import it.eurotn.panjea.magazzino.manager.interfaces.MagazzinoSettingsManager;
import it.eurotn.panjea.magazzino.manager.interfaces.articolo.ArticoloManager;
import it.eurotn.panjea.magazzino.manager.moduloprezzo.interfaces.PrezzoArticoloCalculator;
import it.eurotn.panjea.magazzino.manager.rigadocumento.interfaces.RigaDocumentoManager;
import it.eurotn.panjea.magazzino.util.ParametriCreazioneRigaArticolo;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

/**
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.RigaDocumentoManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RigaDocumentoManager")
public class RigaDocumentoManagerBean implements RigaDocumentoManager {

    private static Logger logger = Logger.getLogger(RigaDocumentoManagerBean.class);

    @Resource
    private SessionContext sessionContext;

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private PrezzoArticoloCalculator prezzoArticoloCalculator;

    @EJB
    private AziendeManager aziendeManager;

    @EJB
    private AnagraficaTabelleManager anagraficaTabelleManager;

    @EJB
    private MagazzinoSettingsManager magazzinoSettings;

    @EJB
    private GiacenzaManager giacenzaManager;

    @EJB
    private ArticoloManager articoloManager;

    /**
     * Crea gli attributi per il calcolo della qta recuperandoli dalla formula di trasformazione e dall'articolo.
     *
     * @param riga
     *            riga dove aggiungere gli attributi
     * @param articolo
     *            articolo interessato
     * @return RigaMagazzino con gli attributiRiga aggiunti
     */
    @Override
    public IRigaArticoloDocumento creaAttributiRiga(IRigaArticoloDocumento riga, Articolo articolo) {
        logger.debug("--> Enter creaAttributiRiga");

        // Attributi per l'articolo. Nella chiave metto il codice del
        // TipoAttributo
        List<AttributoArticolo> attributiArticolo = articoloManager.caricaAttributiDaInserireInRiga(articolo.getId());
        List<AttributoRigaArticolo> attributiRiga = new ArrayList<AttributoRigaArticolo>();

        for (AttributoArticolo attributoArticolo : attributiArticolo) {
            AttributoRigaArticolo attributoRiga = riga.creaAttributoRiga(attributoArticolo);
            attributiRiga.add(attributoRiga);
        }

        // ordino gli attributi in base alla riga e l'ordine
        Collections.sort(attributiRiga, new Comparator<AttributoRigaArticolo>() {

            @Override
            public int compare(AttributoRigaArticolo o1, AttributoRigaArticolo o2) {
                Integer rigaAtt1 = new Integer(o1.getRiga());
                Integer rigaAtt2 = new Integer(o2.getRiga());
                if (rigaAtt1.compareTo(rigaAtt2) != 0) {
                    return rigaAtt1.compareTo(rigaAtt2);
                } else {
                    Integer ordineAtt1 = new Integer(o1.getOrdine());
                    Integer ordineAtt2 = new Integer(o2.getOrdine());
                    return ordineAtt1.compareTo(ordineAtt2);
                }
            }
        });

        riga.setAttributi(attributiRiga);

        if (logger.isDebugEnabled()) {
            logger.debug(
                    "--> creati e aggiunti gli attributi alla riga " + riga + ", attributi creati:" + attributiRiga);
            logger.debug("--> Exit creaAttributiRiga");
        }
        return riga;
    }

    @Override
    public IRigaArticoloDocumento creaRigaArticoloDocumento(IRigaArticoloDocumento rigaArticolo,
            ParametriCreazioneRigaArticolo parametriCreazioneRigaArticolo) {

        Articolo articolo;
        try {
            articolo = panjeaDAO.load(Articolo.class, parametriCreazioneRigaArticolo.getIdArticolo());
        } catch (ObjectNotFoundException e) {
            logger.error("--> articolo non trovato: idArticolo " + parametriCreazioneRigaArticolo.getIdArticolo(), e);
            throw new RuntimeException(e);
        }
        rigaArticolo.setStrategiaTotalizzazioneDocumento(
                parametriCreazioneRigaArticolo.getStrategiaTotalizzazioneDocumento());
        rigaArticolo.getPrezzoUnitario().setCodiceValuta(parametriCreazioneRigaArticolo.getCodiceValuta());
        rigaArticolo.setArticolo(articolo.getArticoloLite());
        // devo caricare la categoria, ho la formula predefinita componente
        Hibernate.initialize(articolo.getCategoria());
        rigaArticolo.getArticolo().setCategoria(articolo.getCategoria());

        // init formula di trasformazione se esiste
        if (articolo.getFormulaTrasformazioneQta() != null) {
            articolo.getFormulaTrasformazioneQta().getId();
        }

        articolo.getAttributiArticolo().size();
        // init di attributi se trasmettiAttributi a true
        if (articolo.getAttributo(Articolo.ATTRIBUTO_TRASMETTI_ATTRIBUTI) != null) {
            rigaArticolo.getArticolo().setAttributiArticolo(articolo.getAttributiArticolo());
        }

        if (articolo.getNumeroDecimaliQta() != null) {
            rigaArticolo.setNumeroDecimaliQta(articolo.getNumeroDecimaliQta());
        }

        // codice iva per split payment
        Boolean splitPayment = Boolean.FALSE;
        if (parametriCreazioneRigaArticolo.getIdEntita() != null) {
            Query querySplitPayment = panjeaDAO.prepareNamedQuery("Entita.caricaSpitPaymentFlag");
            querySplitPayment.setParameter("idEntita", parametriCreazioneRigaArticolo.getIdEntita());
            splitPayment = (Boolean) querySplitPayment.getSingleResult();
        }

        CodiceIva codiceIva = null;
        if (splitPayment) {
            Query queryIvaSplitPayment = panjeaDAO.prepareNamedQuery("CodiceIva.caricaIvaSplitPaymentArticolo");
            queryIvaSplitPayment.setParameter("idArticolo", parametriCreazioneRigaArticolo.getIdArticolo());
            try {
                codiceIva = (CodiceIva) panjeaDAO.getSingleResult(queryIvaSplitPayment);
            } catch (DAOException e) {
                logger.error("-->errore nel caricare il codice iva per lo split payment", e);
            }
            // se ho un pagamento split payment e non ho il codice collegato tengo l'iva
            // dell'articolo
            if (codiceIva == null) {
                codiceIva = articolo.getCodiceIva();
            }
        } else {
            codiceIva = parametriCreazioneRigaArticolo.getTipologiaCodiceIvaAlternativo().getCodiceIva(articolo,
                    parametriCreazioneRigaArticolo.getCodiceIvaAlternativo());
            Hibernate.initialize(codiceIva);
        }
        rigaArticolo.setCodiceIva(codiceIva);

        if (articolo.getUnitaMisura() != null) {
            rigaArticolo.setUnitaMisura(articolo.getUnitaMisura().getCodice());
        }
        // l'unita' di misura qt.magazzino non e' obbligatoria nella anagrafica
        // articoli,
        // ma sulla riga articolo e' utile averla per statistiche e completezza;
        // se non ho impostato l'unita' di misura di magazzino prendo di default
        // l'unita' di misura standard
        if (articolo.getUnitaMisuraQtaMagazzino() != null) {
            rigaArticolo.setUnitaMisuraQtaMagazzino(articolo.getUnitaMisuraQtaMagazzino().getCodice());
        } else {
            if (articolo.getUnitaMisura() != null) {
                rigaArticolo.setUnitaMisuraQtaMagazzino(articolo.getUnitaMisura().getCodice());
            }
        }
        rigaArticolo.setDescrizione(articolo.getDescrizione());
        rigaArticolo.setResa(ObjectUtils.defaultIfNull(articolo.getResa(), 0.0));
        rigaArticolo.setSomministrazione(articolo.isSomministrazione());

        // se ho due unità di misura carico, se presente, la formula di
        // conversione associata e la imposto su
        // rigaArticolo
        String formulaConversioneUm = null;
        if (rigaArticolo.getFormulaTrasformazioneQtaMagazzino() == null && rigaArticolo.getUnitaMisura() != null
                && rigaArticolo.getUnitaMisuraQtaMagazzino() != null
                && !rigaArticolo.getUnitaMisura().equals(rigaArticolo.getUnitaMisuraQtaMagazzino())) {
            ConversioneUnitaMisura conversioneUnitaMisura = anagraficaTabelleManager.caricaConversioneUnitaMisura(
                    rigaArticolo.getUnitaMisura(), rigaArticolo.getUnitaMisuraQtaMagazzino());
            formulaConversioneUm = conversioneUnitaMisura != null ? conversioneUnitaMisura.getFormula() : null;
        }
        rigaArticolo.setFormulaConversioneUnitaMisura(formulaConversioneUm);

        // Devo caricarmi la lingua aziendale perchè l'articolo ha solo una
        // mappa delle descrizioni in lingua estesa
        // quindi è l'unico modo che ho per avere quella in lingua azienda
        String linguaEntita = parametriCreazioneRigaArticolo.getCodiceLingua();
        String linguaAzienda = aziendeManager.caricaAzienda().getLingua();

        DescrizioneLinguaArticoloEstesa descrizioneEstesa = articolo.getDescrizioniLinguaEstesa().get(linguaAzienda);
        if (descrizioneEstesa != null) {
            rigaArticolo.setNoteRiga(descrizioneEstesa.getDescrizione());
        }

        if (linguaEntita != null && !linguaEntita.isEmpty() && !linguaEntita.equals(linguaAzienda)) {
            // in lingua
            rigaArticolo.setDescrizioneLingua(articolo.getDescrizione(linguaEntita));

            // in lungua entità
            DescrizioneLinguaArticoloEstesa descrizioneLingua = articolo.getDescrizioniLinguaEstesa().get(linguaEntita);
            String descrizioneLinguaEstesa = (descrizioneLingua != null) ? descrizioneLingua.getDescrizione()
                    : rigaArticolo.getNoteRiga();
            if (descrizioneLinguaEstesa != null) {
                descrizioneLinguaEstesa = descrizioneLinguaEstesa.trim();
            }
            rigaArticolo.setNoteLinguaRiga(descrizioneLinguaEstesa);
        }

        // imposto se riportare le note dell'articolo sulla riga
        rigaArticolo.setNoteSuDestinazione(parametriCreazioneRigaArticolo.isNoteSuDestinazione());

        rigaArticolo.setFormulaTrasformazione(articolo.getFormulaTrasformazioneQta());
        rigaArticolo.setFormulaTrasformazioneQtaMagazzino(articolo.getFormulaTrasformazioneQtaMagazzino());
        rigaArticolo.setArticoloLibero(articolo.isArticoloLibero());
        rigaArticolo.setTipoOmaggio(TipoOmaggio.NESSUNO);
        rigaArticolo.setCategoriaContabileArticolo(articolo.getCategoriaContabileArticolo());
        rigaArticolo = creaAttributiRiga(rigaArticolo, articolo);

        rigaArticolo.setVariazione1(BigDecimal.ZERO);
        rigaArticolo.setVariazione2(BigDecimal.ZERO);
        rigaArticolo.setVariazione3(BigDecimal.ZERO);
        rigaArticolo.setVariazione4(BigDecimal.ZERO);

        // initImporti
        Importo importoBase = new Importo();
        importoBase.setCodiceValuta(parametriCreazioneRigaArticolo.getImporto().getCodiceValuta());
        importoBase.setTassoDiCambio(parametriCreazioneRigaArticolo.getImporto().getTassoDiCambio());
        importoBase.setImportoInValuta(BigDecimal.ZERO);
        importoBase.setImportoInValutaAzienda(BigDecimal.ZERO);
        rigaArticolo.setPrezzoUnitario(importoBase);

        // creo i parametri calcolo prezzo
        if (parametriCreazioneRigaArticolo.getProvenienzaPrezzoArticolo() != null) {
            ParametriCalcoloPrezzi parametriCalcoloPrezzi = new ParametriCalcoloPrezzi(parametriCreazioneRigaArticolo);
            PoliticaPrezzo politicaPrezzo = prezzoArticoloCalculator.calcola(parametriCalcoloPrezzi);
            rigaArticolo.setPoliticaPrezzo(politicaPrezzo);
            rigaArticolo.applicaPoliticaPrezzo();
        }

        switch (parametriCreazioneRigaArticolo.getTipoMovimento()) {
        case SCARICO_VALORE:
        case CARICO_VALORE:
            rigaArticolo.setQta(null);
            break;
        default:
            rigaArticolo.setQta(0.0);
            break;
        }

        if (parametriCreazioneRigaArticolo.isCalcolaGiacenza()) {
            if (parametriCreazioneRigaArticolo.getIdDeposito() == null) {
                throw new IllegalArgumentException(
                        "Impossibile calcolare la giacenza alla creazione della riga se il deposito non è settato");
            }
            DepositoLite depositoLite = new DepositoLite();
            depositoLite.setId(parametriCreazioneRigaArticolo.getIdDeposito());
            Giacenza giacenza = giacenzaManager.calcoloGiacenza(articolo.getArticoloLite(),
                    Calendar.getInstance().getTime(), depositoLite, getAzienda());
            rigaArticolo.setGiacenza(giacenza);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("--> riga.prezzoUnitario " + rigaArticolo.getPrezzoUnitario());
            logger.debug("--> riga.prezzoNetto " + rigaArticolo.getPrezzoNetto());
            logger.debug("--> riga.prezzoTotale " + rigaArticolo.getPrezzoTotale());
            logger.debug("--> numero decimali " + rigaArticolo.getNumeroDecimaliPrezzo());
            logger.debug("--> categoria contabie articolo" + rigaArticolo.getCategoriaContabileArticolo());
            logger.debug("--> Exit creaRigaArticolo " + rigaArticolo);
        }
        return rigaArticolo;
    }

    /**
     *
     * @return azienda corrente
     */
    private String getAzienda() {
        JecPrincipal principal = (JecPrincipal) sessionContext.getCallerPrincipal();
        return principal.getCodiceAzienda();
    }
}
