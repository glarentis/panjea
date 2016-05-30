package it.eurotn.panjea.magazzino.manager.moduloprezzo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.Sconto;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.ModuloPrezzo;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.ParametriCalcoloPrezzi;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.PoliticaPrezzo;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.RisultatiPrezzo;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.RisultatoModuloPrezzo;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.RisultatoModuloPrezzo.EStrategia;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.RisultatoPrezzo;
import it.eurotn.panjea.magazzino.manager.moduloprezzo.interfaces.ModuloPrezzoCalculator;
import it.eurotn.panjea.magazzino.manager.moduloprezzo.interfaces.PrezzoArticoloCalculator;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

/**
 * Manager Modulo Prezzo.<br/>
 * Se ho provenienza prezzo uguale a listino recupero i moduli dal database. <br/>
 * Se provenienza prezzo uguale ultimo costo chiama direttamente il modulo ultimoCosto
 *
 * @author fattazzo
 */
@Stateless(name = "Panjea.PrezzoArticoloCalculator")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.PrezzoArticoloCalculator")
public class PrezzoArticoloCalculatorBean implements PrezzoArticoloCalculator {

    private static final Logger LOGGER = Logger.getLogger(PrezzoArticoloCalculatorBean.class);

    /**
     * @uml.property name="panjeaDAO"
     * @uml.associationEnd
     */
    @EJB
    private PanjeaDAO panjeaDAO;

    @Resource
    private SessionContext sessionContext;

    @Override
    public PoliticaPrezzo calcola(ParametriCalcoloPrezzi parametriCalcoloPrezzi) {
        LOGGER.debug("--> Enter calcola");

        // Carico tutti i moduli ordinati
        List<ModuloPrezzo> moduliPrezzo = caricaModuliPrezzo(parametriCalcoloPrezzi.getProvenienzaDescrizione());

        // ripulisco parametriCalcoloPrezzi da eventuali risultati precedenti
        parametriCalcoloPrezzi.setPoliticaPrezzo(new PoliticaPrezzo());
        parametriCalcoloPrezzi.setIncrementoPrezzo(BigDecimal.ZERO);

        // Ciclo sui moduli e su ognuno creo il manager relativo e chiamo il metodo calcola per avvalorare i rusultati
        for (ModuloPrezzo moduloPrezzo : moduliPrezzo) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("--> Lookup per il prezzocalculatorName " + moduloPrezzo.getManagerName());
            }
            ModuloPrezzoCalculator calculator = (ModuloPrezzoCalculator) sessionContext
                    .lookup(moduloPrezzo.getManagerName());
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("--> Calculator Trovato : " + calculator != null);
            }
            parametriCalcoloPrezzi = calculator.calcola(parametriCalcoloPrezzi);
        }

        LOGGER.debug("--> Exit calcola");
        return parametriCalcoloPrezzi.getPoliticaPrezzo();
    }

    @Override
    public Set<ArticoloLite> caricaArticoli(ParametriCalcoloPrezzi parametriCalcoloPrezzi) {
        List<ModuloPrezzo> moduliPrezzo = caricaModuliPrezzo(parametriCalcoloPrezzi.getProvenienzaDescrizione());
        Set<ArticoloLite> articoli = new HashSet<ArticoloLite>();
        // Ciclo sui moduli e su ognuno creo il manager relativo e chiamo il metodo calcola per avvalorare i rusultati
        for (ModuloPrezzo moduloPrezzo : moduliPrezzo) {
            ModuloPrezzoCalculator calculator = (ModuloPrezzoCalculator) sessionContext
                    .lookup(moduloPrezzo.getManagerName());
            articoli.addAll(calculator.caricaArticoli(parametriCalcoloPrezzi));
        }
        return articoli;
    }

    /**
     * Carica moduli prezzo per provenienza.
     *
     * @param provenienza
     *            provenienza dei moduli da caricare. (Es:listino o prezzo ultimo)
     * @return lista moduli prezzo
     */
    @SuppressWarnings("unchecked")
    private List<ModuloPrezzo> caricaModuliPrezzo(String provenienza) {
        LOGGER.debug("--> Enter caricaModuliPrezzo");

        Query query = panjeaDAO.prepareNamedQuery("ModuloPrezzo.caricaByProvenienza");
        query.setParameter("paramCodiceAzienda", getAzienda());
        query.setParameter("provenienza", provenienza);

        List<ModuloPrezzo> listResult = new ArrayList<ModuloPrezzo>();

        try {
            listResult = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            LOGGER.error("--> Errore durante il caricamento dei moduli articolo per l'azienda", e);
            throw new RuntimeException("Errore durante il caricamento dei moduli articolo per l'azienda", e);
        }

        LOGGER.debug("--> Exit caricaModuliPrezzo");
        return listResult;
    }

    @Override
    public PoliticaPrezzo fillOnEmpty(PoliticaPrezzo politicaPrezzo, Integer idArticolo) {

        if (politicaPrezzo.getPrezzi().isEmpty()) {

            Articolo articolo;
            // carico l'articolo per avere il numero dei decimeli per il prezzo
            try {
                articolo = panjeaDAO.load(Articolo.class, idArticolo);
            } catch (Exception e) {
                LOGGER.error("--> errore durante il caricamento dell'articolo " + idArticolo, e);
                throw new RuntimeException("errore durante il caricamento dell'articolo " + idArticolo, e);
            }

            RisultatoModuloPrezzo<BigDecimal> risultatoModuloPrezzo = new RisultatoModuloPrezzo<BigDecimal>();
            risultatoModuloPrezzo.setCodiceModulo("null");
            risultatoModuloPrezzo.setStrategia(EStrategia.ASSEGNAZIONE);

            risultatoModuloPrezzo.setDescrizioneModulo("modulo filler");
            risultatoModuloPrezzo.setNumeroDecimali(articolo.getNumeroDecimaliPrezzo());
            risultatoModuloPrezzo.setQuantita(Double.toString(0.0));
            risultatoModuloPrezzo.setTipoModulo("MODULO_FILLER");
            risultatoModuloPrezzo.setValue(BigDecimal.ZERO);

            RisultatoPrezzo<BigDecimal> risultatoPrezzo = new RisultatoPrezzo<BigDecimal>();
            risultatoPrezzo.setNumeroDecimali(articolo.getNumeroDecimaliPrezzo());
            risultatoPrezzo.setValue(BigDecimal.ZERO);
            risultatoPrezzo.setQuantita(new Double(0.0));
            risultatoPrezzo.addRisultatoModuloPrezzo(risultatoModuloPrezzo);

            RisultatiPrezzo<BigDecimal> nuoviRisultati = new RisultatiPrezzo<BigDecimal>();
            nuoviRisultati.put(new Double(0.0), risultatoPrezzo);
            politicaPrezzo.setPrezzi(nuoviRisultati);
        }

        if (politicaPrezzo.getSconti().isEmpty()) {
            Sconto sconto = new Sconto();
            sconto.setSconto1(BigDecimal.ZERO);
            sconto.setSconto2(BigDecimal.ZERO);
            sconto.setSconto3(BigDecimal.ZERO);
            sconto.setSconto4(BigDecimal.ZERO);

            RisultatoPrezzo<Sconto> risultatoPrezzo = new RisultatoPrezzo<Sconto>();
            risultatoPrezzo.setNumeroDecimali(0);
            risultatoPrezzo.setValue(sconto);
            risultatoPrezzo.setQuantita(new Double(0.0));

            RisultatoModuloPrezzo<Sconto> risultatoModuloPrezzo = new RisultatoModuloPrezzo<Sconto>();
            risultatoModuloPrezzo.setAbilitato(true);
            risultatoModuloPrezzo.setCodiceModulo("null");
            risultatoModuloPrezzo.setDescrizioneModulo("modulo filler");
            risultatoModuloPrezzo.setQuantita(Double.toString(0.0));
            risultatoModuloPrezzo.setTipoModulo("MODULO_FILLER");
            risultatoModuloPrezzo.setStrategia(EStrategia.ASSEGNAZIONE);

            risultatoModuloPrezzo.setValue(sconto);
            risultatoPrezzo.addRisultatoModuloPrezzo(risultatoModuloPrezzo);

            RisultatiPrezzo<Sconto> nuoviRisultati = new RisultatiPrezzo<Sconto>();
            nuoviRisultati.put(new Double(0.0), risultatoPrezzo);
            politicaPrezzo.setSconti(nuoviRisultati);
        }

        if (politicaPrezzo.getProvvigioni().isEmpty()) {
            RisultatoModuloPrezzo<BigDecimal> risultatoModuloPrezzo = new RisultatoModuloPrezzo<BigDecimal>();
            risultatoModuloPrezzo.setCodiceModulo("null");
            risultatoModuloPrezzo.setStrategia(EStrategia.ASSEGNAZIONE);

            risultatoModuloPrezzo.setDescrizioneModulo("modulo filler");
            risultatoModuloPrezzo.setNumeroDecimali(2);
            risultatoModuloPrezzo.setQuantita(Double.toString(0.0));
            risultatoModuloPrezzo.setTipoModulo("MODULO_FILLER");
            risultatoModuloPrezzo.setValue(BigDecimal.ZERO);

            RisultatoPrezzo<BigDecimal> risultatoProvvigione = new RisultatoPrezzo<BigDecimal>();
            risultatoProvvigione.setNumeroDecimali(2);
            risultatoProvvigione.setValue(BigDecimal.ZERO);
            risultatoProvvigione.setQuantita(new Double(0.0));
            risultatoProvvigione.addRisultatoModuloPrezzo(risultatoModuloPrezzo);

            RisultatiPrezzo<BigDecimal> nuoviRisultati = new RisultatiPrezzo<BigDecimal>();
            nuoviRisultati.put(new Double(0.0), risultatoProvvigione);
            politicaPrezzo.setProvvigioni(nuoviRisultati);
        }

        return politicaPrezzo;
    }

    /**
     *
     * @return azienda loggata
     */
    private String getAzienda() {
        return ((JecPrincipal) sessionContext.getCallerPrincipal()).getCodiceAzienda();
    }
}
