/**
 *
 */
package it.eurotn.panjea.magazzino.importer.manager;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Calendar;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.anagrafica.manager.depositi.interfaces.DepositiManager;
import it.eurotn.panjea.lotti.domain.Lotto;
import it.eurotn.panjea.lotti.domain.RigaLotto;
import it.eurotn.panjea.lotti.manager.interfaces.LottiManager;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.AttributoRiga;
import it.eurotn.panjea.magazzino.domain.DatiGenerazione;
import it.eurotn.panjea.magazzino.domain.FormuleRigaArticoloCalculator;
import it.eurotn.panjea.magazzino.domain.FormuleRigaArticoloEvasioneCalculator;
import it.eurotn.panjea.magazzino.domain.ProvenienzaPrezzoArticolo;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino.StatoAreaMagazzino;
import it.eurotn.panjea.magazzino.domain.omaggio.TipoOmaggio;
import it.eurotn.panjea.magazzino.exception.FormuleTipoAttributoException;
import it.eurotn.panjea.magazzino.exception.TotaleDocumentoNonCoerenteException;
import it.eurotn.panjea.magazzino.importer.manager.interfaces.GenerazioneImportazioneManager;
import it.eurotn.panjea.magazzino.importer.util.DocumentoImport;
import it.eurotn.panjea.magazzino.importer.util.RigaImport;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoManager;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.RigaMagazzinoManager;
import it.eurotn.panjea.magazzino.service.interfaces.MagazzinoDocumentoService;
import it.eurotn.panjea.magazzino.util.ParametriCreazioneRigaArticolo;
import it.eurotn.panjea.magazzino.util.SedeAreaMagazzinoDTO;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.partite.domain.TipoAreaPartita;
import it.eurotn.panjea.partite.manager.interfaces.TipiAreaPartitaManager;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rate.manager.interfaces.AreaRateManager;
import it.eurotn.panjea.rate.manager.interfaces.RateGenerator;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

/**
 * @author fattazzo
 *
 */
@Stateless(name = "Panjea.GenerazioneImportazioneManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.GenerazioneImportazioneManager")
public class GenerazioneImportazioneManagerBean implements GenerazioneImportazioneManager {

    private static Logger logger = Logger.getLogger(GenerazioneImportazioneManagerBean.class);

    @EJB
    @IgnoreDependency
    private MagazzinoDocumentoService magazzinoDocumentoService;

    @Resource
    private SessionContext context;

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private RateGenerator rateGenerator;

    @EJB
    private AreaMagazzinoManager areaMagazzinoManager;

    @EJB
    private AreaRateManager areaRateManager;

    @EJB
    private RigaMagazzinoManager rigaMagazzinoManager;

    @EJB
    private TipiAreaPartitaManager tipiAreaPartitaManager;

    @EJB
    private LottiManager lottiManager;

    @EJB
    private DepositiManager depositiManager;

    /**
     * Carica il deposito principale dell'azienda loggata.
     *
     * @return deposito
     */
    private Deposito caricaDepositoAziendaPrincipale() {
        return depositiManager.caricaDepositoPrincipale();
    }

    private AreaMagazzino creaAreaMagazzino(DocumentoImport documentoImport, SedeEntita sedeEntita,
            SedeAreaMagazzinoDTO sedeAreaMagazzinoDTO, DatiGenerazione datiGenerazione) {
        Documento documento = new Documento();
        documento.setCodiceAzienda(getCodiceAzienda());
        documento.setDataDocumento(documentoImport.getDataDocumento());
        if (documentoImport.getTotaleImpostaDocumento() != null) {
            Importo imposta = new Importo("EUR", BigDecimal.ONE);
            imposta.setImportoInValuta(documentoImport.getTotaleImpostaDocumento());
            imposta.calcolaImportoValutaAzienda(2);
            documento.setImposta(imposta);
        }

        if (documentoImport.getTotaleDocumento() != null) {
            Importo totale = new Importo("EUR", BigDecimal.ONE);
            totale.setImportoInValuta(documentoImport.getTotaleDocumento());
            totale.calcolaImportoValutaAzienda(2);
            documento.setTotale(totale);
        }

        if (sedeEntita.getId() != null) {
            documento.setEntita(sedeEntita.getEntita().getEntitaLite());
            documento.setSedeEntita(sedeEntita);
        }
        documento.setTipoDocumento(documentoImport.getTipoAreaMagazzino().getTipoDocumento());
        Importo importoDoc = new Importo("EUR", BigDecimal.ONE);
        importoDoc.setImportoInValuta(BigDecimal.ZERO);
        importoDoc.setImportoInValutaAzienda(BigDecimal.ZERO);
        documento.setTotale(importoDoc);
        if (!StringUtils.isBlank(documentoImport.getNumeroDocumento())) {
            documento.getCodice().setCodice(documentoImport.getNumeroDocumento());
        }

        AreaMagazzino areaMagazzino = new AreaMagazzino();
        areaMagazzino.setDocumento(documento);
        areaMagazzino.setDataRegistrazione(documento.getDataDocumento());
        areaMagazzino.setStatoAreaMagazzino(StatoAreaMagazzino.PROVVISORIO);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(documentoImport.getDataDocumento());
        areaMagazzino.setAnnoMovimento(calendar.get(Calendar.YEAR));
        areaMagazzino.setTipoAreaMagazzino(documentoImport.getTipoAreaMagazzino());
        DepositoLite depositoOrigine = documentoImport.getDepositoOrigine();
        if (depositoOrigine == null) {
            depositoOrigine = areaMagazzino.getTipoAreaMagazzino().getDepositoOrigine();
        }

        if (depositoOrigine == null) {
            Deposito deposito = caricaDepositoAziendaPrincipale();
            depositoOrigine = deposito.creaLite();
        }
        areaMagazzino.setDepositoOrigine(depositoOrigine);

        DepositoLite depositoDestinazione = documentoImport.getDepositoDestinazione();
        if (depositoDestinazione == null) {
            depositoDestinazione = documentoImport.getTipoAreaMagazzino().getDepositoDestinazione();
        }

        areaMagazzino.setDepositoDestinazione(depositoDestinazione);
        areaMagazzino.setListino(sedeAreaMagazzinoDTO.getListino());
        areaMagazzino.setListinoAlternativo(sedeAreaMagazzinoDTO.getListinoAlternativo());
        areaMagazzino.setVettore(sedeAreaMagazzinoDTO.getVettore());
        areaMagazzino.setSedeVettore(sedeAreaMagazzinoDTO.getSedeVettore());

        if (sedeEntita.getId() != null) {
            sedeEntita = panjeaDAO.getEntityManager().merge(sedeEntita);
            if (sedeEntita.getZonaGeografica() != null) {
                areaMagazzino.setIdZonaGeografica(sedeEntita.getZonaGeografica().getId());
            }

            areaMagazzino.setRaggruppamentoBolle(sedeAreaMagazzinoDTO.isRaggruppamentoBolle());
            areaMagazzino.setAddebitoSpeseIncasso(sedeAreaMagazzinoDTO.isCalcoloSpese());
            areaMagazzino.setAspettoEsteriore(sedeAreaMagazzinoDTO.getAspettoEsteriore());
            areaMagazzino.setAggiornaCostoUltimo(areaMagazzino.getTipoAreaMagazzino().isAggiornaCostoUltimo());
            areaMagazzino.setStampaPrezzi(sedeAreaMagazzinoDTO.isStampaPrezzi());
        }

        areaMagazzino.setCausaleTrasporto(sedeAreaMagazzinoDTO.getCausaleTrasporto());
        areaMagazzino.setTipoPorto(sedeAreaMagazzinoDTO.getTipoPorto());
        areaMagazzino.setTrasportoCura(sedeAreaMagazzinoDTO.getTrasportoCura());
        areaMagazzino.setPesoNetto(documentoImport.getPesoNetto());
        areaMagazzino.setPesoLordo(documentoImport.getPesoLordo());
        areaMagazzino.setVolume(documentoImport.getVolume());
        areaMagazzino.setPallet(documentoImport.getPallet());
        areaMagazzino.setNumeroColli(documentoImport.getNumeroColli());

        areaMagazzino.setDatiGenerazione(datiGenerazione);

        return areaMagazzino;
    }

    private RigaLotto creaRigaLotto(RigaImport rigaImport, ArticoloLite articolo, Double qtaRiga) {

        // se esiste il lotto lo uso altrimenti vado a crearne uno nuovo
        Lotto lotto = lottiManager.caricaLotto(rigaImport.getCodiceLotto(), rigaImport.getDataScadenzaLotto(),
                articolo);
        if (lotto == null) {
            Lotto newLotto = new Lotto();
            newLotto.setArticolo(articolo);
            newLotto.setCodice(rigaImport.getCodiceLotto());
            newLotto.setCodiceAzienda(getCodiceAzienda());
            newLotto.setDataScadenza(rigaImport.getDataScadenzaLotto());

            lotto = lottiManager.salvaLotto(newLotto);
        }

        RigaLotto rigaLotto = new RigaLotto();
        rigaLotto.setLotto(lotto);
        rigaLotto.setQuantita(qtaRiga);

        return rigaLotto;
    }

    private RigaArticolo creaRigaMagazzino(RigaImport rigaImport, AreaMagazzino areaMagazzino, int ordinamento,
            CodicePagamento codicePagamento) {

        RigaArticolo rigaArticolo = null;
        Integer idListinoAlternativo = null;
        Integer idListino = null;
        Integer idSedeEntita = null;
        Integer idTipoMezzo = null;
        Integer idEntita = null;
        String codiceLingua = null;
        Integer idAgente = null;
        BigDecimal percentualeScontoCommerciale = null;

        if (areaMagazzino.getListinoAlternativo() != null) {
            idListinoAlternativo = areaMagazzino.getListinoAlternativo().getId();
        }
        if (areaMagazzino.getListino() != null) {
            idListino = areaMagazzino.getListino().getId();
        }
        if (areaMagazzino.getDocumento().getSedeEntita() != null) {
            idSedeEntita = areaMagazzino.getDocumento().getSedeEntita().getId();
            codiceLingua = areaMagazzino.getDocumento().getSedeEntita().getLingua();
            idEntita = areaMagazzino.getDocumento().getSedeEntita().getEntita().getId();
        }
        if (areaMagazzino.getMezzoTrasporto() != null) {
            idTipoMezzo = areaMagazzino.getMezzoTrasporto().getTipoMezzoTrasporto().getId();
        }

        if (codicePagamento != null) {
            percentualeScontoCommerciale = codicePagamento.getPercentualeScontoCommerciale();
        }

        ParametriCreazioneRigaArticolo parametri = new ParametriCreazioneRigaArticolo();
        parametri.setProvenienzaPrezzo(areaMagazzino.getTipoAreaMagazzino().getProvenienzaPrezzo());
        parametri.setIdArticolo(rigaImport.getIdArticolo());
        parametri.setData(areaMagazzino.getDocumento().getDataDocumento());
        parametri.setIdSedeEntita(idSedeEntita);
        parametri.setIdEntita(idEntita);
        parametri.setIdListinoAlternativo(idListinoAlternativo);
        parametri.setIdListino(idListino);
        parametri.setImporto(areaMagazzino.getDocumento().getTotale());
        parametri.setCodiceIvaAlternativo(areaMagazzino.getCodiceIvaAlternativo());
        parametri.setIdTipoMezzo(idTipoMezzo);
        parametri.setIdZonaGeografica(areaMagazzino.getIdZonaGeografica());
        parametri.setProvenienzaPrezzoArticolo(ProvenienzaPrezzoArticolo.DOCUMENTO);
        parametri.setNoteSuDestinazione(areaMagazzino.getTipoAreaMagazzino().isNoteSuDestinazione());
        parametri.setTipoMovimento(areaMagazzino.getTipoAreaMagazzino().getTipoMovimento());
        parametri.setCodiceValuta(areaMagazzino.getDocumento().getTotale().getCodiceValuta());
        parametri.setCodiceLingua(codiceLingua);
        parametri.setIdAgente(idAgente);
        parametri.setTipologiaCodiceIvaAlternativo(areaMagazzino.getTipologiaCodiceIvaAlternativo());
        parametri.setPercentualeScontoCommerciale(percentualeScontoCommerciale);
        parametri.setGestioneConai(areaMagazzino.getTipoAreaMagazzino().isGestioneConai());
        parametri.setNotaCredito(areaMagazzino.getTipoAreaMagazzino().getTipoDocumento().isNotaCreditoEnable());
        parametri.setStrategiaTotalizzazioneDocumento(
                areaMagazzino.getTipoAreaMagazzino().getStrategiaTotalizzazioneDocumento());

        rigaArticolo = (RigaArticolo) rigaMagazzinoManager.getDao(parametri).creaRigaArticolo(parametri);
        rigaArticolo.setAreaMagazzino(areaMagazzino);
        rigaArticolo.setVariazione1(rigaImport.getSconto1());
        rigaArticolo.setVariazione2(rigaImport.getSconto2());
        rigaArticolo.setVariazione3(rigaImport.getSconto3());
        rigaArticolo.setVariazione4(rigaImport.getSconto4());

        Double qta = rigaImport.getQta();
        AttributoRiga attributoDivisore = rigaArticolo.getAttributo("divisore");
        if (attributoDivisore != null) {
            Double valore = attributoDivisore.getValoreTipizzato(Double.class);

            if (valore != null && valore.doubleValue() != 0d) {
                BigDecimal result = BigDecimal.valueOf(qta).divide(BigDecimal.valueOf(valore),
                        new MathContext(rigaArticolo.getNumeroDecimaliQta(), RoundingMode.HALF_UP));
                qta = result.doubleValue();
            }
        }

        rigaArticolo.setQta(qta);

        FormuleRigaArticoloCalculator formuleRigaArticoloCalculator = new FormuleRigaArticoloEvasioneCalculator();
        try {
            rigaArticolo = (RigaArticolo) formuleRigaArticoloCalculator.calcola(rigaArticolo);
        } catch (FormuleTipoAttributoException e) {
            throw new RuntimeException(e);
        }

        if (rigaImport.isOmaggio()) {
            rigaArticolo.setTipoOmaggio(TipoOmaggio.OMAGGIO);
        }

        if (rigaImport.getPrezzoUnitario() != null) {
            Importo prezzo = rigaArticolo.getPrezzoUnitario().clone();
            prezzo.setImportoInValuta(rigaImport.getPrezzoUnitario());
            prezzo.calcolaImportoValutaAzienda(2);

            rigaArticolo.setPrezzoUnitario(prezzo);
            rigaArticolo.setPrezzoUnitarioReale(prezzo.clone());
        }

        if (rigaImport.getPrezzoNetto() != null) {
            rigaArticolo.getPrezzoNetto().setImportoInValuta(rigaImport.getPrezzoNetto());
            rigaArticolo.getPrezzoNetto().calcolaImportoValutaAzienda(2);
        }

        // aggiungo l'eventuale riga lotto
        if (!StringUtils.isBlank(rigaImport.getCodiceLotto())) {
            RigaLotto rigaLotto = creaRigaLotto(rigaImport, rigaArticolo.getArticolo(), rigaArticolo.getQta());
            rigaLotto.setRigaArticolo(rigaArticolo);
            rigaArticolo.getRigheLotto().add(rigaLotto);
        }

        return rigaArticolo;
    }

    @Override
    public AreaMagazzino generaDocumento(DocumentoImport documentoImport, SedeEntita sedeEntita,
            SedeAreaMagazzinoDTO sedeAreaMagazzinoDTO, DatiGenerazione datiGenerazione) {

        AreaMagazzino areaMagazzino = creaAreaMagazzino(documentoImport, sedeEntita, sedeAreaMagazzinoDTO,
                datiGenerazione);

        // salva l'area magazzino
        try {
            areaMagazzino = areaMagazzinoManager.salvaAreaMagazzino(areaMagazzino, true);
        } catch (Exception e) {
            logger.error("--> Errore durante la creazione del documento di fatturazione", e);
            throw new RuntimeException("Errore durante la creazione del documento di fatturazione", e);
        }

        TipoAreaPartita tipoAreaPartita = null;
        try {
            tipoAreaPartita = tipiAreaPartitaManager
                    .caricaTipoAreaPartitaPerTipoDocumento(documentoImport.getTipoAreaMagazzino().getTipoDocumento());
        } catch (Exception e) {
            tipoAreaPartita = null;
        }

        AreaRate areaRate = null;
        if (tipoAreaPartita != null && !tipoAreaPartita.isNew()) {
            areaRate = new AreaRate();
            areaRate.setDocumento(areaMagazzino.getDocumento());
            areaRate.setCodicePagamento(sedeAreaMagazzinoDTO.getCodicePagamento());
            areaRate.setSpeseIncasso(sedeAreaMagazzinoDTO.getCodicePagamento().getImportoSpese());
            // salvo l'area partite
            areaRate = areaRateManager.salvaAreaRate(areaRate);
        }

        // genero le righe
        int ordinamento = 0;
        for (RigaImport rigaImport : documentoImport.getRighe()) {
            RigaArticolo rigaMagazzino = creaRigaMagazzino(rigaImport, areaMagazzino, ordinamento,
                    sedeAreaMagazzinoDTO.getCodicePagamento());
            try {
                rigaMagazzinoManager.getDao().salvaRigaMagazzino(rigaMagazzino);
            } catch (Exception e) {
                logger.error("--> errore durante il salvataggio della riga magazzino.", e);
                throw new RuntimeException("errore durante il salvataggio della riga magazzino.", e);
            }
            ordinamento++;
        }

        try {
            magazzinoDocumentoService.validaRigheMagazzino(areaMagazzino, areaRate, false, true);
            areaMagazzino = areaMagazzinoManager.validaRigheMagazzino(areaMagazzino, areaRate, false, true);
        } catch (TotaleDocumentoNonCoerenteException e) {
            // Non contabilizzando non posso avere questa eccezione
            logger.error("-->errore durante la contabilizzazione del documento.", e);
        }

        return areaMagazzino;
    }

    /**
     *
     * @return codiceAzienda
     */
    private String getCodiceAzienda() {
        return ((JecPrincipal) context.getCallerPrincipal()).getCodiceAzienda();
    }

}
