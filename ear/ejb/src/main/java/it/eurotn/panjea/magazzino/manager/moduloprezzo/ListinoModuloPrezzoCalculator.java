package it.eurotn.panjea.magazzino.manager.moduloprezzo;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.magazzino.domain.Listino.ETipoListino;
import it.eurotn.panjea.magazzino.domain.RigaListino;
import it.eurotn.panjea.magazzino.domain.ScaglioneListino;
import it.eurotn.panjea.magazzino.domain.VersioneListino;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.ParametriCalcoloPrezzi;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.RisultatoModuloPrezzo;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.RisultatoModuloPrezzo.EStrategia;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.RisultatoPrezzo;
import it.eurotn.panjea.magazzino.manager.interfaces.ListinoManager;
import it.eurotn.panjea.magazzino.manager.moduloprezzo.interfaces.ModuloPrezzoCalculator;
import it.eurotn.panjea.magazzino.util.RigaListinoCalcolo;

/**
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.ListinoModuloPrezzoCalculator")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.ListinoModuloPrezzoCalculator")
public class ListinoModuloPrezzoCalculator implements ModuloPrezzoCalculator {

    private static final Logger LOGGER = Logger.getLogger(ListinoModuloPrezzoCalculator.class);

    public static final String TIPO_MODULO = "LISTINO";

    @EJB
    private ListinoManager listinoManager;

    @Override
    public ParametriCalcoloPrezzi calcola(ParametriCalcoloPrezzi parametriCalcoloPrezzi) {
        LOGGER.debug("--> Calcolo il prezzo del listino");

        // se il prezzo è bloccato non faccio niente ma restituisco i parametri passati in ingresso
        if (parametriCalcoloPrezzi.isBloccoPrezzo()) {
            return parametriCalcoloPrezzi;
        }

        String codiceValuta = parametriCalcoloPrezzi.getCodiceValuta();

        List<RigaListinoCalcolo> listRigheListino = Collections.emptyList();

        boolean listinoIvato = false;

        Listino listinoScelto = null;
        // Controllo che esista il listino alternativo
        if (parametriCalcoloPrezzi.getIdListinoAlternativo() != null) {
            Listino listinoAlternativo = new Listino();
            listinoAlternativo.setId(parametriCalcoloPrezzi.getIdListinoAlternativo());
            listinoAlternativo = listinoManager.caricaListino(listinoAlternativo, false);

            if (codiceValuta != null && !codiceValuta.isEmpty()
                    && !codiceValuta.equals(listinoAlternativo.getCodiceValuta())) {
                throw new RuntimeException("Codice valuta del listino diverso da quello richiesto.");
            }

            listRigheListino = listinoManager.caricaRigheListinoPrezzoCalculator(listinoAlternativo,
                    parametriCalcoloPrezzi.getData(), parametriCalcoloPrezzi.getIdArticolo());
            if (!listRigheListino.isEmpty()) {
                listinoScelto = listinoAlternativo;
            }
            if (LOGGER.isDebugEnabled() && !listRigheListino.isEmpty()) {
                LOGGER.debug("--> Trovata la riga nel listino alternativo " + listRigheListino);
            }

            listinoIvato = listinoAlternativo.isIva();
        }

        // se non esiste nessuna riga listino per il listino alternativo le cerco nel listino normale
        if (listRigheListino.isEmpty() && parametriCalcoloPrezzi.getIdListino() != null) {
            Listino listino = new Listino();
            listino.setId(parametriCalcoloPrezzi.getIdListino());
            listino = listinoManager.caricaListino(listino, false);

            if (codiceValuta != null && !codiceValuta.isEmpty() && !codiceValuta.equals(listino.getCodiceValuta())) {
                throw new RuntimeException("Codice valuta del listino diverso da quello richiesto.");
            }

            listRigheListino = listinoManager.caricaRigheListinoPrezzoCalculator(listino,
                    parametriCalcoloPrezzi.getData(), parametriCalcoloPrezzi.getIdArticolo());
            if (!listRigheListino.isEmpty()) {
                listinoScelto = listino;
            }
            if (LOGGER.isDebugEnabled() && !listRigheListino.isEmpty()) {
                LOGGER.debug("--> Trovata la riga nel listino " + listRigheListino);
            }

            if (parametriCalcoloPrezzi.getIdListinoAlternativo() == null) {
                listinoIvato = listino.isIva();
            }
        }

        if (!listRigheListino.isEmpty()) {
            EStrategia strategiaListino = EStrategia.SOSTITUZIONE;
            if (parametriCalcoloPrezzi.getPoliticaPrezzo().getPrezzi().isEmpty()) {
                strategiaListino = EStrategia.ASSEGNAZIONE;
            }

            // ripulisco la mappa dei prezzi. Se ho qualcosa prima del listino questa va persa.
            parametriCalcoloPrezzi.getPoliticaPrezzo().getPrezzi().clear();

            // inserisco nella mappa dei prezzi i valori delle righe listino che ho trovato
            for (RigaListinoCalcolo rigaListino : listRigheListino) {
                RisultatoModuloPrezzo<BigDecimal> risultatoModuloPrezzo = new RisultatoModuloPrezzo<BigDecimal>();
                risultatoModuloPrezzo.setDescrizioneModulo(rigaListino.getListinoDescrizione());
                risultatoModuloPrezzo.setCodiceModulo(rigaListino.getListinoCodice());
                risultatoModuloPrezzo.setTipoModulo(TIPO_MODULO);
                risultatoModuloPrezzo.setStrategia(strategiaListino);
                risultatoModuloPrezzo.setNumeroDecimali(rigaListino.getNumeroDecimaliPrezzo());
                risultatoModuloPrezzo.setValue(rigaListino.getPrezzo());
                Double quantita = rigaListino.getQuantita();
                if (quantita.equals(ScaglioneListino.MAX_SCAGLIONE) && !listinoScelto.isNew()
                        && listinoScelto.getTipoListino().equals(ETipoListino.NORMALE)) {
                    quantita = 0.0;
                }
                risultatoModuloPrezzo.setQuantita(quantita.toString());

                RisultatoPrezzo<BigDecimal> risultatoPrezzo = new RisultatoPrezzo<BigDecimal>();
                risultatoPrezzo.setNumeroDecimali(rigaListino.getNumeroDecimaliPrezzo());
                risultatoPrezzo.setValue(rigaListino.getPrezzo());
                risultatoPrezzo.setQuantita(quantita);
                risultatoPrezzo.addRisultatoModuloPrezzo(risultatoModuloPrezzo);
                parametriCalcoloPrezzi.getPoliticaPrezzo().getPrezzi().put(quantita, risultatoPrezzo);
            }

        }

        parametriCalcoloPrezzi.getPoliticaPrezzo().setPrezzoIvato(listinoIvato);

        return parametriCalcoloPrezzi;
    }

    @Override
    public Set<ArticoloLite> caricaArticoli(ParametriCalcoloPrezzi parametriCalcoloPrezzi) {
        Set<ArticoloLite> articoli = new HashSet<ArticoloLite>();
        if (parametriCalcoloPrezzi.getIdListinoAlternativo() != null) {
            Listino listino = new Listino();
            listino.setId(parametriCalcoloPrezzi.getIdListinoAlternativo());
            VersioneListino versione = listinoManager.caricaVersioneListinoByData(listino,
                    Calendar.getInstance().getTime());
            for (RigaListino rigaListino : versione.getRigheListino()) {
                articoli.add(rigaListino.getArticolo());
            }
        }

        if (parametriCalcoloPrezzi.getIdListino() != null) {
            Listino listino = new Listino();
            listino.setId(parametriCalcoloPrezzi.getIdListino());
            VersioneListino versione = listinoManager.caricaVersioneListinoByData(listino,
                    Calendar.getInstance().getTime());
            for (RigaListino rigaListino : versione.getRigheListino()) {
                articoli.add(rigaListino.getArticolo());
            }
        }

        return articoli;
    }

}
