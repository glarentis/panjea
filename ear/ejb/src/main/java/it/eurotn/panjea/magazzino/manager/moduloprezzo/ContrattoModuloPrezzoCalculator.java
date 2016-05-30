package it.eurotn.panjea.magazzino.manager.moduloprezzo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.RigaContrattoStrategiaPrezzo.TipoValore;
import it.eurotn.panjea.magazzino.domain.ScaglioneListino;
import it.eurotn.panjea.magazzino.domain.Sconto;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.ParametriCalcoloPrezzi;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.PoliticaPrezzo;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.RisultatiPrezzo;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.RisultatoModuloPrezzo;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.RisultatoModuloPrezzo.EStrategia;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.RisultatoPrezzo;
import it.eurotn.panjea.magazzino.manager.interfaces.ContrattiManager;
import it.eurotn.panjea.magazzino.manager.interfaces.articolo.ArticoloManager;
import it.eurotn.panjea.magazzino.manager.moduloprezzo.interfaces.ModuloPrezzoCalculator;
import it.eurotn.panjea.magazzino.util.ArticoloRicerca;
import it.eurotn.panjea.magazzino.util.ParametriRicercaArticolo;
import it.eurotn.panjea.magazzino.util.RigaContrattoCalcolo;
import it.eurotn.util.PanjeaEJBUtil;

/**
 * Calcola il prezzo o lo sconto di un articolo per il cliente.<br>
 * Carica le varie righe contratto che trova associate ad articolo e sede.<br>
 * Vengono caricate le righe con il seguente ordine rispetto alla tipologia della riga:<br/>
 * (L' applicazione dei pesi avviene dall'alto verso il basso)
 * <ul>
 * <li>tcs-tca</li>
 * <li>cs-tca</li>
 * <li>s-tca</li>
 * <li>tcs-ca1</li>
 * <li>cs-ca1</li>
 * <li>s-ca1</li>
 * <li>tcs-ca2</li>
 * <li>cs-ca2</li>
 * <li>s-ca2</li>
 * <li>tcs-a</li>
 * <li>cs-a</li>
 * <li>s-a</li>
 * </ul>
 * <br>
 * <br>
 * Dove:<br>
 * tcs=tutte le categorie sede clienti<br>
 * cs=categorie sede clienti<br>
 * s=sedi<br>
 * tca=tutte le categorie articolo<br>
 * ca=categoria articolo<br>
 * a=sede articolo Se trovo delle combinazioni con peso uguale applico prima quella con data inizio minore.<br>
 */
@Stateless(name = "Panjea.ContrattoModuloPrezzoCalculator")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.ContrattoModuloPrezzoCalculator")
public class ContrattoModuloPrezzoCalculator implements ModuloPrezzoCalculator {

    private static final Logger LOGGER = Logger.getLogger(ContrattoModuloPrezzoCalculator.class);

    public static final String TIPO_MODULO = "CONTRATTO";

    @EJB
    private ContrattiManager contrattiManager;

    @EJB
    private ArticoloManager articoloManager;

    @Override
    public ParametriCalcoloPrezzi calcola(ParametriCalcoloPrezzi parametriCalcoloPrezzi) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("-->calcola(ParametriCalcoloPrezzi) - start"); //$NON-NLS-1$
        }

        // Carico le righe dei vari contratti validi per la data richiesta.
        List<RigaContrattoCalcolo> righeContrattoCalcolo = contrattiManager.caricaRigheContrattoCalcolo(
                parametriCalcoloPrezzi.getIdArticolo(), parametriCalcoloPrezzi.getIdEntita(),
                parametriCalcoloPrezzi.getIdSedeEntita(), parametriCalcoloPrezzi.getIdCategoriaSedeMagazzino(),
                parametriCalcoloPrezzi.getIdCategoriaCommercialeArticolo(), parametriCalcoloPrezzi.getData(),
                parametriCalcoloPrezzi.getCodiceValuta(), parametriCalcoloPrezzi.getIdAgente(), false);

        int rigaElaborata = 0;

        // Ciclo e applico i parametri. Se ho già processato la riga contratto salto quelle uguali successive perchè se
        // ho delle righe contratto agente le troverei duplicate. Prendo sempre la prima riga contratto perchè in caso
        // di più righe contratto agente ( tutti e quella dell'agente scelto ) sono ordinate nel giusto peso. ( prima la
        // riga dell'agente se esiste poi in caso quella legata a tutti gli agenti )
        for (RigaContrattoCalcolo rigaContrattoCalcolo : righeContrattoCalcolo) {
            if (rigaContrattoCalcolo.getId() == rigaElaborata) {
                continue;
            }

            // //////////////// preparo gli scaglioni aggiuntivi
            double quantitaSogliaPrezzo = rigaContrattoCalcolo.getQuantitaSogliaPrezzo();

            PoliticaPrezzo politicaPrezzo = parametriCalcoloPrezzi.getPoliticaPrezzo();

            rigaElaborata = rigaContrattoCalcolo.getId();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("--> Calcolo per la riga " + rigaContrattoCalcolo);
            }

            // devo recuperare i prezzi alla soglia prezzo della riga contratto in modo da
            // verificare se i prezzi esistenti calcolati nei precedenti moduli prezzo subiscono delle variazioni
            RisultatoPrezzo<BigDecimal> risultatoPrezzo = politicaPrezzo.getPrezzi()
                    .getRisultatoPrezzoContratto(quantitaSogliaPrezzo);

            RisultatoPrezzo<BigDecimal> risultatoPrezzoListino = politicaPrezzo.getPrezzi()
                    .getRisultatoPrezzo(quantitaSogliaPrezzo);

            // Prezzi aggiuntivi
            if (rigaContrattoCalcolo.isStrategiaPrezzoAbilitata() && quantitaSogliaPrezzo != 0.0) {
                shiftScaglionePrezzoPerSoglia(rigaContrattoCalcolo, politicaPrezzo, risultatoPrezzo,
                        risultatoPrezzoListino, quantitaSogliaPrezzo);
            }

            // Prezzi
            if (rigaContrattoCalcolo.isStrategiaPrezzoAbilitata()) {
                parametriCalcoloPrezzi = calcolaParametriPrezzo(rigaContrattoCalcolo, parametriCalcoloPrezzi);
            }

            // devo recuperare i prezzi alla soglia sconto della riga contratto in modo da
            // verificare se i prezzi esistenti calcolati nei precedenti moduli prezzo subiscono delle variazioni
            double quantitaSogliaSconto = rigaContrattoCalcolo.getQuantitaSogliaSconto();
            RisultatoPrezzo<BigDecimal> risultatoPrezzoPerSconto = politicaPrezzo.getPrezzi()
                    .getRisultatoPrezzoContratto(quantitaSogliaSconto);
            RisultatoPrezzo<BigDecimal> risultatoPrezzoListinoPerSconto = politicaPrezzo.getPrezzi()
                    .getRisultatoPrezzo(quantitaSogliaSconto);

            if ((quantitaSogliaSconto != quantitaSogliaPrezzo || !rigaContrattoCalcolo.isStrategiaPrezzoAbilitata())
                    && rigaContrattoCalcolo.isStrategiaScontoAbilitata() && quantitaSogliaSconto != 0.0) {
                shiftScaglionePrezzoPerSoglia(rigaContrattoCalcolo, politicaPrezzo, risultatoPrezzoPerSconto,
                        risultatoPrezzoListinoPerSconto, quantitaSogliaSconto);
                // se non c'è lo scaglione per il prezzo della soglia corrente, devo crearlo
                if (politicaPrezzo.getPrezzi().get(quantitaSogliaSconto) == null) {
                    RisultatoPrezzo<BigDecimal> risultatoDaUtilizzare = risultatoPrezzoPerSconto;
                    if (risultatoPrezzoListino != null && risultatoPrezzoListino.getQuantita() != 0.0) {
                        risultatoDaUtilizzare = risultatoPrezzoListinoPerSconto;
                    }
                    RisultatoPrezzo<BigDecimal> sogliaPrecedenteAggiuntiva = PanjeaEJBUtil
                            .cloneObject(risultatoDaUtilizzare);
                    if (sogliaPrecedenteAggiuntiva == null) {
                        RisultatoPrezzo<BigDecimal> risultato = creaRisultatoPrezzoFake(rigaContrattoCalcolo);
                        sogliaPrecedenteAggiuntiva = risultato;
                    }
                    politicaPrezzo.getPrezzi().put(quantitaSogliaSconto, sogliaPrecedenteAggiuntiva);
                }
            }
        }

        int rigaPrecedente = 0;
        RigaContrattoCalcolo rigaValida = null;
        for (RigaContrattoCalcolo rigaContrattoCalcolo : righeContrattoCalcolo) {

            if (rigaPrecedente != rigaContrattoCalcolo.getId()) {
                if (rigaValida != null && isRigaContrattoAgenteValid(parametriCalcoloPrezzi, rigaValida)) {
                    parametriCalcoloPrezzi = calcolaParametriProvvigioni(rigaValida, parametriCalcoloPrezzi, 0.0);
                }
                rigaValida = rigaContrattoCalcolo;
            } else {
                rigaValida = (!isRigaContrattoAgenteValid(parametriCalcoloPrezzi, rigaValida)
                        && isRigaContrattoAgenteValid(parametriCalcoloPrezzi, rigaContrattoCalcolo))
                                ? rigaContrattoCalcolo : rigaValida;
            }

            rigaPrecedente = rigaContrattoCalcolo.getId();
        }
        if (rigaValida != null && isRigaContrattoAgenteValid(parametriCalcoloPrezzi, rigaValida)) {
            parametriCalcoloPrezzi = calcolaParametriProvvigioni(rigaValida, parametriCalcoloPrezzi, 0.0);
        }

        // ordino per qtaSogliaSconto
        Collections.sort(righeContrattoCalcolo, RigaContrattoCalcolo.getRigaContrattoQtaSogliaScontoComparator());

        rigaElaborata = 0;

        // devo ciclare a parte gli scaglioni per creare i moduli sconto perchè devo ordinarli per qta soglia sconto
        for (RigaContrattoCalcolo rigaContrattoCalcolo : righeContrattoCalcolo) {
            if (rigaContrattoCalcolo.getId() == rigaElaborata) {
                continue;
            }
            rigaElaborata = rigaContrattoCalcolo.getId();

            // Sconti
            if (rigaContrattoCalcolo.isStrategiaScontoAbilitata()) {
                parametriCalcoloPrezzi = calcolaParametriSconto(rigaContrattoCalcolo, parametriCalcoloPrezzi, false);
            }
        }

        parametriCalcoloPrezzi = normalizzaScaglioni(parametriCalcoloPrezzi);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("-->calcola(ParametriCalcoloPrezzi) - end"); //$NON-NLS-1$
        }
        return parametriCalcoloPrezzi;
    }

    /**
     * Calcola i parametri per la sezione prezzo.
     *
     * @param rigaContrattoCalcolo
     *            rigaContrattoCalcolo con i parametri da applicare
     * @param parametriCalcoloPrezzi
     *            parametri per il calcolo del prezzo
     * @return nuovi parametri con i dati della riga contratto
     */
    private ParametriCalcoloPrezzi calcolaParametriPrezzo(RigaContrattoCalcolo rigaContrattoCalcolo,
            ParametriCalcoloPrezzi parametriCalcoloPrezzi) {
        LOGGER.debug("--> Enter calcolaParametriPrezzo");
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("--> Risultato prezzo " + parametriCalcoloPrezzi);
        }

        // Esco se ho il prezzo bloccato e non lo sovrascrivo
        if (parametriCalcoloPrezzi.isBloccoPrezzo() && !rigaContrattoCalcolo.isIgnoraBloccoPrezzoPrecedente()) {
            // Aggiungo il modulo disabilitato per visualizzare che il contratto non può essere processato per il flag
            // prezzo bloccato.
            for (Entry<Double, RisultatoPrezzo<BigDecimal>> entryRisultato : parametriCalcoloPrezzi.getPoliticaPrezzo()
                    .getPrezzi().entrySet()) {
                // aggiungo solamente se la mia soglia è presente
                if (entryRisultato.getKey() >= rigaContrattoCalcolo.getQuantitaSogliaPrezzo()) {
                    RisultatoModuloPrezzo<BigDecimal> risultatoModuloPrezzo = creaRisultatoModuloPrezzo(
                            rigaContrattoCalcolo, rigaContrattoCalcolo.getValorePrezzo(),
                            rigaContrattoCalcolo.getNumeroDecimaliPrezzo(), null, false);
                    entryRisultato.getValue().addRisultatoModuloPrezzo(risultatoModuloPrezzo);
                }
            }

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("--> - prezzo bloccato - esco ");
            }
            return parametriCalcoloPrezzi;
        }

        RisultatiPrezzo<BigDecimal> risultatoCorrentePrezzi = parametriCalcoloPrezzi.getPoliticaPrezzo().getPrezzi();
        switch (rigaContrattoCalcolo.getAzionePrezzoEnum()) {
        case SOSTITUZIONE:
            RisultatiPrezzo<BigDecimal> nuoviRisultati = new RisultatiPrezzo<BigDecimal>();
            // aggiungo il nuovo risultato che sostituisce tutti quelli maggiori alla soglia di qta
            RisultatoPrezzo<BigDecimal> risultatoPrezzoTrovato = risultatoCorrentePrezzi
                    .getRisultatoPrezzoContratto(rigaContrattoCalcolo.getQuantitaSogliaPrezzo());
            RisultatoPrezzo<BigDecimal> risultatoPrezzo = PanjeaEJBUtil.cloneObject(risultatoPrezzoTrovato);
            if (risultatoPrezzo != null) {
                risultatoPrezzo.disabilitaModuli();
            } else {
                risultatoPrezzo = creaRisultatoPrezzo(rigaContrattoCalcolo, rigaContrattoCalcolo.getValorePrezzo(),
                        EStrategia.ASSEGNAZIONE);
            }

            // Tengo solamente i range con qta minore della soglia, gli altri non li reinserisco nei nuovi risultati
            for (Entry<Double, RisultatoPrezzo<BigDecimal>> entryRisultato : risultatoCorrentePrezzi.entrySet()) {
                if (entryRisultato.getKey() < rigaContrattoCalcolo.getQuantitaSogliaPrezzo()) {
                    nuoviRisultati.put(entryRisultato.getKey(), entryRisultato.getValue());
                } else {
                    entryRisultato.getValue().disabilitaModuli();
                    risultatoPrezzo.setModuliPrezzoString(entryRisultato.getValue().getModuliPrezzoString());
                }
            }

            EStrategia strategiaVariazione = EStrategia.SOSTITUZIONE;
            if (risultatoCorrentePrezzi.isEmpty()) {
                strategiaVariazione = EStrategia.ASSEGNAZIONE;
            }
            RisultatoModuloPrezzo<BigDecimal> risultatoModuloPrezzo = creaRisultatoModuloPrezzo(rigaContrattoCalcolo,
                    rigaContrattoCalcolo.getValorePrezzo(), rigaContrattoCalcolo.getNumeroDecimaliPrezzo(),
                    strategiaVariazione, true);
            risultatoPrezzo.setNumeroDecimali(rigaContrattoCalcolo.getNumeroDecimaliPrezzo());
            risultatoPrezzo.setQuantita(rigaContrattoCalcolo.getQuantitaSogliaPrezzo());
            risultatoPrezzo.setValue(rigaContrattoCalcolo.getValorePrezzo());
            risultatoPrezzo.addRisultatoModuloPrezzo(risultatoModuloPrezzo);

            nuoviRisultati.put(rigaContrattoCalcolo.getQuantitaSogliaPrezzo(), risultatoPrezzo);
            parametriCalcoloPrezzi.getPoliticaPrezzo().setPrezzi(nuoviRisultati);
            break;
        case VARIAZIONE:
            // ciclo sui range e vario il prezzo
            Double qtaPrecedenteRange = new Double(0);
            for (Entry<Double, RisultatoPrezzo<BigDecimal>> entryRisultato : risultatoCorrentePrezzi.entrySet()) {
                if (entryRisultato.getKey() >= rigaContrattoCalcolo.getQuantitaSogliaPrezzo()) {
                    RisultatoPrezzo<BigDecimal> risultato = entryRisultato.getValue();
                    risultato = calcolaVariazionePrezzo(rigaContrattoCalcolo, risultato);
                    risultato.setNumeroDecimali(rigaContrattoCalcolo.getNumeroDecimaliPrezzo());
                    entryRisultato.setValue(risultato);
                } else {
                    if (qtaPrecedenteRange < entryRisultato.getKey()) {
                        qtaPrecedenteRange = entryRisultato.getKey();
                    }
                }
            }

            if (!risultatoCorrentePrezzi.containsKey(rigaContrattoCalcolo.getQuantitaSogliaPrezzo())) {

                BigDecimal prezzoPrec = BigDecimal.ZERO;
                if (risultatoCorrentePrezzi.get(qtaPrecedenteRange) != null) {
                    prezzoPrec = risultatoCorrentePrezzi.get(qtaPrecedenteRange).getValue();
                }

                RisultatoPrezzo<BigDecimal> risultatoPrezzoAddRange = creaRisultatoPrezzo(rigaContrattoCalcolo,
                        prezzoPrec, EStrategia.VARIAZIONE);

                if (risultatoCorrentePrezzi.get(qtaPrecedenteRange) != null) {
                    // Aggiungo la descrizione dei moduli precedenti
                    risultatoPrezzoAddRange.setModuliPrezzoString(
                            risultatoCorrentePrezzi.get(qtaPrecedenteRange).getModuliPrezzoString());
                }

                risultatoPrezzoAddRange = calcolaVariazionePrezzo(rigaContrattoCalcolo, risultatoPrezzoAddRange);
                risultatoCorrentePrezzi.put(rigaContrattoCalcolo.getQuantitaSogliaPrezzo(), risultatoPrezzoAddRange);
            }
            parametriCalcoloPrezzi.getPoliticaPrezzo().setPrezzi(risultatoCorrentePrezzi);
            break;
        default:
            throw new UnsupportedOperationException("TipoAzione Prezzo non supportato");
        }

        if (!parametriCalcoloPrezzi.isBloccoPrezzo()) {
            parametriCalcoloPrezzi.setBloccoPrezzo(rigaContrattoCalcolo.isBloccoPrezzo());
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("-->calcolaParametriPrezzo(RigaContratto, ParametriCalcoloPrezzi) - end");
        }
        return parametriCalcoloPrezzi;
    }

    /**
     * Calcola i parametri per la sezione provvigioni agente.
     *
     * @param rigaContrattoCalcolo
     *            rigaContrattoCalcolo con i parametri da applicare
     * @param parametriCalcoloPrezzi
     *            parametri per il calcolo del prezzo
     * @param sogliaQuantita
     *            soglia quantità
     * @return nuovi parametri con i dati della riga contratto
     */
    private ParametriCalcoloPrezzi calcolaParametriProvvigioni(RigaContrattoCalcolo rigaContrattoCalcolo,
            ParametriCalcoloPrezzi parametriCalcoloPrezzi, double sogliaQuantita) {
        LOGGER.debug("--> Enter calcolaParametriProvvigioni");

        if (rigaContrattoCalcolo.getProvvigineAgente() == null) {
            return parametriCalcoloPrezzi;
        }

        if (parametriCalcoloPrezzi.getIdAgente() != null && rigaContrattoCalcolo.getProvvigineAgente() != null) {
            if (parametriCalcoloPrezzi.isBloccoProvvigioni()
                    && !rigaContrattoCalcolo.isIgnoraBloccoProvvigionePrecedente()) {
                // Aggiungo il modulo disabilitato per visualizzare che il contratto non può essere processato per
                // il flag provvigione bloccata.
                for (Entry<Double, RisultatoPrezzo<BigDecimal>> entryRisultato : parametriCalcoloPrezzi
                        .getPoliticaPrezzo().getProvvigioni().entrySet()) {
                    // aggiungo solamente se la mia soglia è presente
                    if (entryRisultato.getKey() >= rigaContrattoCalcolo.getQuantitaSogliaPrezzo()) {
                        RisultatoModuloPrezzo<BigDecimal> risultatoModuloPrezzo = creaRisultatoModuloPrezzo(
                                rigaContrattoCalcolo, rigaContrattoCalcolo.getProvvigineAgente(), 2, null, false);
                        if (rigaContrattoCalcolo.getIdAgente() == null) {
                            risultatoModuloPrezzo.setDescrizioneModulo(
                                    risultatoModuloPrezzo.getDescrizioneModulo() + " (tutti gli agenti)");
                        }
                        risultatoModuloPrezzo.setQuantita(new Double(sogliaQuantita).toString());

                        entryRisultato.getValue().addRisultatoModuloPrezzo(risultatoModuloPrezzo);
                    }
                }
                return parametriCalcoloPrezzi;
            }
        }

        RisultatiPrezzo<BigDecimal> risultatoCorrenteProvvigioni = parametriCalcoloPrezzi.getPoliticaPrezzo()
                .getProvvigioni();
        switch (rigaContrattoCalcolo.getAzioneProvvigioneEnum()) {
        case SOSTITUZIONE:
            RisultatiPrezzo<BigDecimal> nuoviRisultati = new RisultatiPrezzo<BigDecimal>();
            // aggiungo il nuovo risultato che sostituisce tutti quelli maggiori alla soglia di qta
            RisultatoPrezzo<BigDecimal> risultatoProvvigione = creaRisultatoProvvigione(rigaContrattoCalcolo,
                    rigaContrattoCalcolo.getProvvigineAgente());
            // Tengo solamente i range con qta minore della soglia, gli altri non li reinserisco nei nuovi risultati
            for (Entry<Double, RisultatoPrezzo<BigDecimal>> entryRisultato : risultatoCorrenteProvvigioni.entrySet()) {
                if (entryRisultato.getKey() < rigaContrattoCalcolo.getQuantitaSogliaPrezzo()) {
                    nuoviRisultati.put(entryRisultato.getKey(), entryRisultato.getValue());
                } else {
                    entryRisultato.getValue().disabilitaModuli();
                    risultatoProvvigione.setModuliPrezzoString(entryRisultato.getValue().getModuliPrezzoString());
                }
            }

            EStrategia strategiaVariazione = EStrategia.SOSTITUZIONE;
            if (risultatoCorrenteProvvigioni.isEmpty()) {
                strategiaVariazione = EStrategia.ASSEGNAZIONE;
            }
            RisultatoModuloPrezzo<BigDecimal> risultatoModuloPrezzo = creaRisultatoModuloPrezzo(rigaContrattoCalcolo,
                    rigaContrattoCalcolo.getProvvigineAgente(), 2, strategiaVariazione, true);
            risultatoModuloPrezzo.setCodiceModulo(rigaContrattoCalcolo.getCodiceContratto());
            if (rigaContrattoCalcolo.getIdAgente() == null) {
                risultatoModuloPrezzo
                        .setDescrizioneModulo(risultatoModuloPrezzo.getDescrizioneModulo() + " (tutti gli agenti)");
            }
            risultatoModuloPrezzo.setQuantita(new Double(sogliaQuantita).toString());

            risultatoProvvigione.setNumeroDecimali(rigaContrattoCalcolo.getNumeroDecimaliPrezzo());
            risultatoProvvigione.addRisultatoModuloPrezzo(risultatoModuloPrezzo);

            nuoviRisultati.put(rigaContrattoCalcolo.getQuantitaSogliaPrezzo(), risultatoProvvigione);
            parametriCalcoloPrezzi.getPoliticaPrezzo().setProvvigioni(nuoviRisultati);

            break;
        case VARIAZIONE:
            // ciclo sui range e vario il prezzo
            Double qtaPrecedenteRange = new Double(0);
            for (Entry<Double, RisultatoPrezzo<BigDecimal>> entryRisultato : risultatoCorrenteProvvigioni.entrySet()) {
                if (entryRisultato.getKey() >= rigaContrattoCalcolo.getQuantitaSogliaPrezzo()) {
                    RisultatoPrezzo<BigDecimal> risultato = entryRisultato.getValue();
                    risultato = calcolaVariazioneProvvigioni(rigaContrattoCalcolo, risultato);
                    entryRisultato.setValue(risultato);
                } else {
                    if (qtaPrecedenteRange < entryRisultato.getKey()) {
                        qtaPrecedenteRange = entryRisultato.getKey();
                    }
                }
            }

            if (!risultatoCorrenteProvvigioni.containsKey(rigaContrattoCalcolo.getQuantitaSogliaPrezzo())) {

                BigDecimal provvPrec = BigDecimal.ZERO;
                if (risultatoCorrenteProvvigioni.get(qtaPrecedenteRange) != null) {
                    provvPrec = risultatoCorrenteProvvigioni.get(qtaPrecedenteRange).getValue();
                }

                RisultatoPrezzo<BigDecimal> risultatoProvvigioniAddRange = creaRisultatoProvvigione(
                        rigaContrattoCalcolo, provvPrec);

                if (risultatoCorrenteProvvigioni.get(qtaPrecedenteRange) != null) {
                    // Aggiungo la descrizione dei moduli precedenti
                    risultatoProvvigioniAddRange.setModuliPrezzoString(
                            risultatoCorrenteProvvigioni.get(qtaPrecedenteRange).getModuliPrezzoString());
                }

                risultatoProvvigioniAddRange = calcolaVariazioneProvvigioni(rigaContrattoCalcolo,
                        risultatoProvvigioniAddRange);
                risultatoCorrenteProvvigioni.put(rigaContrattoCalcolo.getQuantitaSogliaPrezzo(),
                        risultatoProvvigioniAddRange);
            }
            parametriCalcoloPrezzi.getPoliticaPrezzo().setProvvigioni(risultatoCorrenteProvvigioni);
            break;
        default:
            throw new UnsupportedOperationException("TipoAzione Provvigione non supportato");
        }

        if (!parametriCalcoloPrezzi.isBloccoProvvigioni()) {
            parametriCalcoloPrezzi.setBloccoProvvigioni(rigaContrattoCalcolo.isBloccoProvvigione());
        }

        LOGGER.debug("--> Exit calcolaParametriProvvigioni");
        return parametriCalcoloPrezzi;
    }

    /**
     * Calcola i parametri per la sezione sconto.
     *
     * @param rigaContrattoCalcolo
     *            rigaContrattoCalcolo con i parametri da applicare
     * @param parametriCalcoloPrezzi
     *            parametri per il calcolo dello sconto
     * @param elaboraProvvigioni
     *            <code>true</code> elabora anche le eventuali provvigioni agenti se presenti
     * @return nuovi parametri con i dati della riga contratto
     */
    private ParametriCalcoloPrezzi calcolaParametriSconto(RigaContrattoCalcolo rigaContrattoCalcolo,
            ParametriCalcoloPrezzi parametriCalcoloPrezzi, boolean elaboraProvvigioni) {
        LOGGER.debug("--> Enter calcolaParametriSconto");

        // Esco se ho il prezzo bloccato e non lo sovrascrivo
        if (parametriCalcoloPrezzi.isBloccoSconto() && !rigaContrattoCalcolo.isIgnoraBloccoScontoPrecedente()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("--> - sconto bloccato - esco ");
            }

            // Aggiungo il modulo disabilitato per visualizzare che il contratto non può essere processato per il flag
            // prezzo bloccato.
            for (Entry<Double, RisultatoPrezzo<Sconto>> entryRisultato : parametriCalcoloPrezzi.getPoliticaPrezzo()
                    .getSconti().entrySet()) {
                // aggiungo solamente se la mia soglia è presente
                if (entryRisultato.getKey() >= rigaContrattoCalcolo.getQuantitaSogliaSconto()) {
                    RisultatoModuloPrezzo<Sconto> risultatoModuloPrezzo = new RisultatoModuloPrezzo<Sconto>();
                    risultatoModuloPrezzo.setAbilitato(false);
                    risultatoModuloPrezzo.setCodiceModulo(rigaContrattoCalcolo.getCodiceContratto());
                    risultatoModuloPrezzo.setDescrizioneModulo(rigaContrattoCalcolo.getDescrizioneContratto());
                    risultatoModuloPrezzo
                            .setQuantita(new Double(rigaContrattoCalcolo.getQuantitaSogliaSconto()).toString());
                    risultatoModuloPrezzo.setTipoModulo("CONTRATTO");
                    Sconto sconto = new Sconto();
                    sconto.setSconto1(rigaContrattoCalcolo.getSconto1());
                    sconto.setSconto2(rigaContrattoCalcolo.getSconto2());
                    sconto.setSconto3(rigaContrattoCalcolo.getSconto3());
                    sconto.setSconto4(rigaContrattoCalcolo.getSconto4());
                    risultatoModuloPrezzo.setValue(sconto);
                    entryRisultato.getValue().addRisultatoModuloPrezzo(risultatoModuloPrezzo);
                }
            }
            return parametriCalcoloPrezzi;
        }

        parametriCalcoloPrezzi.getPoliticaPrezzo().setPoliticaScontiPresenti(Boolean.TRUE);

        RisultatiPrezzo<Sconto> risultatoCorrenteSconti = parametriCalcoloPrezzi.getPoliticaPrezzo().getSconti();
        switch (rigaContrattoCalcolo.getAzioneScontoEnum()) {
        case SOSTITUZIONE:
            // Ricreo i range dei risultati
            RisultatiPrezzo<Sconto> nuoviRisultati = new RisultatiPrezzo<Sconto>();
            RisultatoPrezzo<Sconto> risultatoScontoTrovatoSos = risultatoCorrenteSconti
                    .getRisultatoPrezzoContratto(rigaContrattoCalcolo.getQuantitaSogliaSconto());
            RisultatoPrezzo<Sconto> risultatoScontoSos = null;
            if (risultatoScontoTrovatoSos != null) {
                risultatoScontoSos = PanjeaEJBUtil.cloneObject(risultatoScontoTrovatoSos);
                risultatoScontoSos.setValue(new Sconto());
                risultatoScontoSos.disabilitaModuli();
            } else {
                risultatoScontoSos = creaRisultatoSconto(rigaContrattoCalcolo);
            }
            // Tengo solamente i range con qta minore della soglia, gli altri non li reinserisco nei nuovi risultati
            for (Entry<Double, RisultatoPrezzo<Sconto>> entryRisultato : risultatoCorrenteSconti.entrySet()) {
                if (entryRisultato.getKey() < rigaContrattoCalcolo.getQuantitaSogliaSconto()) {
                    nuoviRisultati.put(entryRisultato.getKey(), entryRisultato.getValue());
                } else {
                    entryRisultato.getValue().disabilitaModuli();
                    risultatoScontoSos.setModuliPrezzoString(entryRisultato.getValue().getModuliPrezzoString());
                }
            }
            // aggiungo il nuovo risultato che sostituisce tutti quelli maggiori alla soglia di qta.
            RisultatoModuloPrezzo<Sconto> risultatoModuloPrezzo = new RisultatoModuloPrezzo<Sconto>();
            risultatoModuloPrezzo.setAbilitato(true);
            risultatoModuloPrezzo.setCodiceModulo(rigaContrattoCalcolo.getCodiceContratto());
            risultatoModuloPrezzo.setDescrizioneModulo(rigaContrattoCalcolo.getDescrizioneContratto());
            risultatoModuloPrezzo.setQuantita(new Double(rigaContrattoCalcolo.getQuantitaSogliaSconto()).toString());
            risultatoModuloPrezzo.setTipoModulo("CONTRATTO");
            risultatoModuloPrezzo.setStrategia(EStrategia.SOSTITUZIONE);
            Sconto sconto = new Sconto();
            sconto.setSconto1(rigaContrattoCalcolo.getSconto1());
            sconto.setSconto2(rigaContrattoCalcolo.getSconto2());
            sconto.setSconto3(rigaContrattoCalcolo.getSconto3());
            sconto.setSconto4(rigaContrattoCalcolo.getSconto4());
            risultatoModuloPrezzo.setValue(sconto);

            risultatoScontoSos.addRisultatoModuloPrezzo(risultatoModuloPrezzo);
            risultatoScontoSos.setValue(sconto);

            nuoviRisultati.put(rigaContrattoCalcolo.getQuantitaSogliaSconto(), risultatoScontoSos);
            parametriCalcoloPrezzi.getPoliticaPrezzo().setSconti(nuoviRisultati);
            break;
        case VARIAZIONE:
            // ciclo sui range e vario lo sconto
            for (Entry<Double, RisultatoPrezzo<Sconto>> entryRisultato : risultatoCorrenteSconti.entrySet()) {
                if (entryRisultato.getKey() >= rigaContrattoCalcolo.getQuantitaSogliaSconto()) {
                    RisultatoPrezzo<Sconto> risultato = entryRisultato.getValue();
                    risultato = calcolaVariazioneSconto(rigaContrattoCalcolo, risultato);
                    entryRisultato.setValue(risultato);
                }
            }
            if (!risultatoCorrenteSconti.containsKey(rigaContrattoCalcolo.getQuantitaSogliaSconto())) {
                RisultatoPrezzo<Sconto> risultatoScontoTrovato = risultatoCorrenteSconti
                        .getRisultatoPrezzoContratto(rigaContrattoCalcolo.getQuantitaSogliaSconto());
                RisultatoPrezzo<Sconto> risultatoSconto = null;
                if (risultatoScontoTrovato != null) {
                    risultatoSconto = PanjeaEJBUtil.cloneObject(risultatoScontoTrovato);
                } else {
                    risultatoSconto = creaRisultatoSconto(rigaContrattoCalcolo);
                }
                risultatoSconto = calcolaVariazioneSconto(rigaContrattoCalcolo, risultatoSconto);
                risultatoCorrenteSconti.put(rigaContrattoCalcolo.getQuantitaSogliaSconto(), risultatoSconto);
            }
            parametriCalcoloPrezzi.getPoliticaPrezzo().setSconti(risultatoCorrenteSconti);
            break;
        default:
            throw new UnsupportedOperationException("TipoAzione Sconto non supportato");
        }
        if (!parametriCalcoloPrezzi.isBloccoSconto()) {
            parametriCalcoloPrezzi.setBloccoSconto(rigaContrattoCalcolo.isBloccoSconto());
        }

        LOGGER.debug("--> Exit calcolaParametriSconto");
        return parametriCalcoloPrezzi;
    }

    /**
     * Calcola il RisultatoPrezzo<BigDecimal> per la rigaContratto.
     *
     * @param rigaContrattoCalcolo
     *            riga del contratto con i parametri di sconto
     * @param risultatoPrezzo
     *            risultato prezzo da contenente i risultati precedenti
     * @return RisultatoPrezzo<BigDecimal>
     */
    private RisultatoPrezzo<BigDecimal> calcolaVariazionePrezzo(RigaContrattoCalcolo rigaContrattoCalcolo,
            RisultatoPrezzo<BigDecimal> risultatoPrezzo) {
        // Aggiungo la descrizione di questo modulo
        Integer numeroDecimaliPrezzo = rigaContrattoCalcolo.getNumeroDecimaliPrezzo();
        RisultatoModuloPrezzo<BigDecimal> risultatoModuloPrezzo = creaRisultatoModuloPrezzo(rigaContrattoCalcolo, null,
                numeroDecimaliPrezzo, EStrategia.VARIAZIONE, true);
        if (rigaContrattoCalcolo.getTipoValorePrezzoEnum() == TipoValore.PERCENTUALE) {
            // modifico il valore con lo sconto
            BigDecimal value = risultatoPrezzo.getValue();
            value = value.add(rigaContrattoCalcolo.getValorePrezzo().multiply(value).divide(Importo.HUNDRED));
            risultatoPrezzo.setValue(value);
            risultatoModuloPrezzo.setValue(value);
        } else {
            if (risultatoPrezzo.getValue() == null) {
                risultatoPrezzo.setValue(BigDecimal.ZERO);
            }
            // aggiungo l'importo
            risultatoPrezzo.setValue(risultatoPrezzo.getValue().add(rigaContrattoCalcolo.getValorePrezzo()));
            risultatoModuloPrezzo.setValue(rigaContrattoCalcolo.getValorePrezzo());
        }
        risultatoPrezzo.addRisultatoModuloPrezzo(risultatoModuloPrezzo);
        return risultatoPrezzo;
    }

    /**
     * Calcola il RisultatoPrezzo<BigDecimal> per la rigaContratto.
     *
     * @param rigaContrattoCalcolo
     *            riga del contratto con i parametri di sconto
     * @param risultatoPrezzo
     *            risultato prezzo da contenente i risultati precedenti
     * @return RisultatoPrezzo<BigDecimal>
     */
    private RisultatoPrezzo<BigDecimal> calcolaVariazioneProvvigioni(RigaContrattoCalcolo rigaContrattoCalcolo,
            RisultatoPrezzo<BigDecimal> risultatoPrezzo) {
        // Aggiungo la descrizione di questo modulo
        RisultatoModuloPrezzo<BigDecimal> risultatoModuloPrezzo = creaRisultatoModuloPrezzo(rigaContrattoCalcolo, null,
                rigaContrattoCalcolo.getNumeroDecimaliPrezzo(), EStrategia.VARIAZIONE, true);
        risultatoModuloPrezzo.setCodiceModulo(rigaContrattoCalcolo.getCodiceContratto());
        if (risultatoPrezzo.getValue() == null) {
            risultatoPrezzo.setValue(BigDecimal.ZERO);
        }

        // aggiungo l'importo
        risultatoPrezzo.setValue(risultatoPrezzo.getValue().add(rigaContrattoCalcolo.getProvvigineAgente()));
        risultatoModuloPrezzo.setValue(rigaContrattoCalcolo.getProvvigineAgente());

        risultatoPrezzo.addRisultatoModuloPrezzo(risultatoModuloPrezzo);
        return risultatoPrezzo;
    }

    /**
     * Calcola il RisultatoPrezzo<Sconto> per la rigaContratto.
     *
     * @param rigaContrattoCalcolo
     *            riga del contratto con i parametri di sconto
     * @param risultatoSconto
     *            risultati precedenti
     * @return RisultatoPrezzo<Sconto>
     */
    private RisultatoPrezzo<Sconto> calcolaVariazioneSconto(RigaContrattoCalcolo rigaContrattoCalcolo,
            RisultatoPrezzo<Sconto> risultatoSconto) {
        RisultatoModuloPrezzo<Sconto> risultatoModuloPrezzo = new RisultatoModuloPrezzo<Sconto>();
        risultatoModuloPrezzo.setCodiceModulo(rigaContrattoCalcolo.getCodiceContratto());
        risultatoModuloPrezzo.setStrategia(EStrategia.VARIAZIONE);
        risultatoModuloPrezzo.setDescrizioneModulo(rigaContrattoCalcolo.getDescrizioneContratto());
        risultatoModuloPrezzo.setNumeroDecimali(rigaContrattoCalcolo.getNumeroDecimaliPrezzo());
        if (rigaContrattoCalcolo.getNumeroDecimaliPrezzoArticolo() != null) {
            risultatoModuloPrezzo.setNumeroDecimali(rigaContrattoCalcolo.getNumeroDecimaliPrezzoArticolo());
        }
        risultatoModuloPrezzo.setQuantita(new Double(rigaContrattoCalcolo.getQuantitaSogliaSconto()).toString());
        risultatoModuloPrezzo.setTipoModulo("CONTRATTO");
        Sconto scontoRiga = new Sconto();
        scontoRiga.setSconto1(rigaContrattoCalcolo.getSconto1());
        scontoRiga.setSconto2(rigaContrattoCalcolo.getSconto2());
        scontoRiga.setSconto3(rigaContrattoCalcolo.getSconto3());
        scontoRiga.setSconto4(rigaContrattoCalcolo.getSconto4());
        risultatoModuloPrezzo.setValue(scontoRiga);

        LinkedList<BigDecimal> sconti = new LinkedList<BigDecimal>();
        if (BigDecimal.ZERO.compareTo(risultatoSconto.getValue().getSconto1()) != 0) {
            sconti.addLast(risultatoSconto.getValue().getSconto1());
        }
        if (BigDecimal.ZERO.compareTo(risultatoSconto.getValue().getSconto2()) != 0) {
            sconti.addLast(risultatoSconto.getValue().getSconto2());
        }
        if (BigDecimal.ZERO.compareTo(risultatoSconto.getValue().getSconto3()) != 0) {
            sconti.addLast(risultatoSconto.getValue().getSconto3());
        }
        if (BigDecimal.ZERO.compareTo(risultatoSconto.getValue().getSconto4()) != 0) {
            sconti.addLast(risultatoSconto.getValue().getSconto4());
        }
        if (BigDecimal.ZERO.compareTo(rigaContrattoCalcolo.getSconto1()) != 0) {
            sconti.addLast(rigaContrattoCalcolo.getSconto1());
        }
        if (BigDecimal.ZERO.compareTo(rigaContrattoCalcolo.getSconto2()) != 0) {
            sconti.addLast(rigaContrattoCalcolo.getSconto2());
        }
        if (BigDecimal.ZERO.compareTo(rigaContrattoCalcolo.getSconto3()) != 0) {
            sconti.addLast(rigaContrattoCalcolo.getSconto3());
        }
        if (BigDecimal.ZERO.compareTo(rigaContrattoCalcolo.getSconto4()) != 0) {
            sconti.addLast(rigaContrattoCalcolo.getSconto4());
        }

        while (sconti.size() > 4) {
            sconti.removeFirst();
        }
        Sconto sconto = new Sconto();
        if (sconti.size() > 0) {
            sconto.setSconto1(sconti.get(0));
        }
        if (sconti.size() > 1) {
            sconto.setSconto2(sconti.get(1));
        }
        if (sconti.size() > 2) {
            sconto.setSconto3(sconti.get(2));
        }
        if (sconti.size() > 3) {
            sconto.setSconto4(sconti.get(3));
        }

        risultatoSconto.setValue(sconto);
        risultatoSconto.addRisultatoModuloPrezzo(risultatoModuloPrezzo);
        return risultatoSconto;
    }

    @Override
    public Set<ArticoloLite> caricaArticoli(ParametriCalcoloPrezzi parametriCalcoloPrezzi) {
        Set<ArticoloLite> articoli = new HashSet<ArticoloLite>();
        List<RigaContrattoCalcolo> righeContrattoCalcolo = contrattiManager.caricaRigheContrattoCalcolo(null, null,
                parametriCalcoloPrezzi.getIdSedeEntita(), parametriCalcoloPrezzi.getIdCategoriaSedeMagazzino(),
                parametriCalcoloPrezzi.getIdCategoriaCommercialeArticolo(), parametriCalcoloPrezzi.getData(),
                parametriCalcoloPrezzi.getCodiceValuta(), null, false);
        for (RigaContrattoCalcolo rigaContrattoCalcolo : righeContrattoCalcolo) {
            if (rigaContrattoCalcolo.getIdArticolo() != null) {
                ArticoloLite articolo = articoloManager.caricaArticoloLite(rigaContrattoCalcolo.getIdArticolo());
                articoli.add(articolo);
            } else if (rigaContrattoCalcolo.getIdCategoriaCommercialeArticolo() != null) {
                ParametriRicercaArticolo parametriRicercaArticolo = new ParametriRicercaArticolo();
                parametriRicercaArticolo
                        .setIdCategoriaCommerciale(rigaContrattoCalcolo.getIdCategoriaCommercialeArticolo());
                List<ArticoloRicerca> articoliRicerca = articoloManager.ricercaArticoli(parametriRicercaArticolo);
                articoli.addAll(getArticoliLite(articoliRicerca));
            } else if (rigaContrattoCalcolo.getIdArticolo() == null
                    && rigaContrattoCalcolo.getIdCategoriaCommercialeArticolo() == null) {
                List<ArticoloRicerca> articoliRicerca = articoloManager.caricaArticoli();
                articoli.addAll(getArticoliLite(articoliRicerca));
            }
        }
        return articoli;
    }

    /**
     * Crea risultato modulo prezzo.
     *
     * @param rigaContrattoCalcolo
     *            rigaContrattoCalcolo
     * @param value
     *            value
     * @param numeroDecimali
     *            numeroDecimali
     * @param strategiaVariazione
     *            strategiaVariazione
     * @param abilitato
     *            abilitato
     * @return RisultatoModuloPrezzo<BigDecimal>
     */
    private RisultatoModuloPrezzo<BigDecimal> creaRisultatoModuloPrezzo(RigaContrattoCalcolo rigaContrattoCalcolo,
            BigDecimal value, Integer numeroDecimali, EStrategia strategiaVariazione, boolean abilitato) {
        RisultatoModuloPrezzo<BigDecimal> risultatoModuloPrezzo = new RisultatoModuloPrezzo<BigDecimal>();
        risultatoModuloPrezzo.setCodiceModulo(rigaContrattoCalcolo.getCodiceContratto());
        risultatoModuloPrezzo.setStrategia(strategiaVariazione);
        risultatoModuloPrezzo.setAbilitato(abilitato);
        risultatoModuloPrezzo.setDescrizioneModulo(rigaContrattoCalcolo.getDescrizioneContratto());
        risultatoModuloPrezzo.setNumeroDecimali(numeroDecimali);
        risultatoModuloPrezzo.setQuantita(new Double(rigaContrattoCalcolo.getQuantitaSogliaPrezzo()).toString());
        risultatoModuloPrezzo.setTipoModulo("CONTRATTO");
        if (value != null) {
            risultatoModuloPrezzo.setValue(value);
        }
        return risultatoModuloPrezzo;
    }

    /**
     * @param rigaContrattoCalcolo
     *            riga del contratto sulla quale si basa il risultatoPrezzo.
     * @param value
     *            valore da settare nel risultato prezzo
     * @param strategia
     *            strategia
     * @return risultatoPrezzo con le proprietà impostate
     */
    private RisultatoPrezzo<BigDecimal> creaRisultatoPrezzo(RigaContrattoCalcolo rigaContrattoCalcolo, BigDecimal value,
            EStrategia strategia) {
        LOGGER.debug("--> Enter creaRisultatoPrezzo");
        RisultatoModuloPrezzo<BigDecimal> risultatoModuloPrezzo = creaRisultatoModuloPrezzo(rigaContrattoCalcolo,
                rigaContrattoCalcolo.getValorePrezzo(), rigaContrattoCalcolo.getNumeroDecimaliPrezzo(), strategia,
                true);

        RisultatoPrezzo<BigDecimal> risultato = new RisultatoPrezzo<BigDecimal>();
        risultato.setNumeroDecimali(rigaContrattoCalcolo.getNumeroDecimaliPrezzo());
        risultato.setValue(value);
        risultato.setQuantita(rigaContrattoCalcolo.getQuantitaSogliaPrezzo());
        risultato.setModuliPrezzoString(risultatoModuloPrezzo.serializeToString());

        LOGGER.debug("--> Exit creaRisultatoPrezzo");
        return risultato;
    }

    /**
     * Ritorna un RisultatoPrezzo<BigDecimal> a 0 senza strategia.
     *
     * @param rigaContrattoCalcolo
     *            rigaContrattoCalcolo
     * @return RisultatoPrezzo<BigDecimal>
     */
    private RisultatoPrezzo<BigDecimal> creaRisultatoPrezzoFake(RigaContrattoCalcolo rigaContrattoCalcolo) {
        Integer numeroDecimaliPrezzo = rigaContrattoCalcolo.getNumeroDecimaliPrezzo();
        if (rigaContrattoCalcolo.getNumeroDecimaliPrezzoArticolo() != null) {
            numeroDecimaliPrezzo = rigaContrattoCalcolo.getNumeroDecimaliPrezzoArticolo();
        }
        RisultatoModuloPrezzo<BigDecimal> risultatoModuloPrezzo = creaRisultatoModuloPrezzo(rigaContrattoCalcolo,
                BigDecimal.ZERO, numeroDecimaliPrezzo, null, false);

        RisultatoPrezzo<BigDecimal> risultato = new RisultatoPrezzo<BigDecimal>();
        risultato.setNumeroDecimali(rigaContrattoCalcolo.getNumeroDecimaliPrezzo());
        if (rigaContrattoCalcolo.getNumeroDecimaliPrezzoArticolo() != null) {
            risultato.setNumeroDecimali(numeroDecimaliPrezzo);
        }
        risultato.setValue(BigDecimal.ZERO);
        risultato.setQuantita(0.0);
        risultato.setModuliPrezzoString(risultatoModuloPrezzo.serializeToString());
        return risultato;
    }

    /**
     * @param rigaContrattoCalcolo
     *            riga del contratto sulla quale si basa il risultatoPrezzo.
     * @param value
     *            valore da settare nel risultato provvigione
     * @return risultatoPrezzo con le proprietà impostate
     */
    private RisultatoPrezzo<BigDecimal> creaRisultatoProvvigione(RigaContrattoCalcolo rigaContrattoCalcolo,
            BigDecimal value) {
        LOGGER.debug("--> Enter creaRisultatoProvvigione");
        RisultatoPrezzo<BigDecimal> risultato = new RisultatoPrezzo<BigDecimal>();
        risultato.setNumeroDecimali(rigaContrattoCalcolo.getNumeroDecimaliPrezzo());
        risultato.setValue(value);
        risultato.setQuantita(rigaContrattoCalcolo.getQuantitaSogliaPrezzo());
        LOGGER.debug("--> Exit creaRisultatoProvvigione");
        return risultato;
    }

    /**
     * @param rigaContrattoCalcolo
     *            riga del contratto sulla quale si basa il risultatoPrezzo.
     * @return risultatoPrezzo con le proprietà impostate
     */
    private RisultatoPrezzo<Sconto> creaRisultatoSconto(RigaContrattoCalcolo rigaContrattoCalcolo) {
        LOGGER.debug("--> Enter creaRisultatoSconto");
        RisultatoPrezzo<Sconto> risultato = new RisultatoPrezzo<Sconto>();
        Sconto sconto = new Sconto();
        sconto.setSconto1(BigDecimal.ZERO);
        sconto.setSconto2(BigDecimal.ZERO);
        sconto.setSconto3(BigDecimal.ZERO);
        sconto.setSconto4(BigDecimal.ZERO);
        risultato.setNumeroDecimali(rigaContrattoCalcolo.getNumeroDecimaliPrezzo());
        if (rigaContrattoCalcolo.getNumeroDecimaliPrezzoArticolo() != null) {
            risultato.setNumeroDecimali(rigaContrattoCalcolo.getNumeroDecimaliPrezzoArticolo());
        }
        risultato.setValue(sconto);
        risultato.setQuantita(rigaContrattoCalcolo.getQuantitaSogliaSconto());
        LOGGER.debug("--> Exit creaRisultatoSconto");
        return risultato;
    }

    /**
     * @param articoliRicerca
     *            lista di articoli ricerca
     * @return lista di proxy di articolo lite
     */
    private List<ArticoloLite> getArticoliLite(List<ArticoloRicerca> articoliRicerca) {
        List<ArticoloLite> articoliLite = new ArrayList<ArticoloLite>();
        for (ArticoloRicerca articoloRicerca : articoliRicerca) {
            articoliLite.add(articoloRicerca.createProxyArticoloLite());
        }
        return articoliLite;
    }

    /**
     * Controlla se la {@link RigaContrattoCalcolo} è valida per l'agente in base ai parametri di calcolo.
     *
     * @param parametriCalcoloPrezzi
     *            parametri
     * @param riga
     *            {@link RigaContrattoCalcolo} da testare
     * @return <code>true</code> se valida
     */
    private boolean isRigaContrattoAgenteValid(ParametriCalcoloPrezzi parametriCalcoloPrezzi,
            RigaContrattoCalcolo riga) {
        return parametriCalcoloPrezzi.getIdAgente() != null
                && (parametriCalcoloPrezzi.getIdAgente().equals(riga.getIdAgente()) || riga.getIdAgente() == null);
    }

    /**
     * Normalizza gli scaglioni calcolati eliminando gli scaglioni superflui; non posso eliminarli in fase di creazione
     * perchè mi servono per calcolare correttamente i prezzi.
     *
     * @param parametriCalcoloPrezzi
     *            parametriCalcoloPrezzi
     * @return parametriCalcoloPrezzi
     */
    private ParametriCalcoloPrezzi normalizzaScaglioni(ParametriCalcoloPrezzi parametriCalcoloPrezzi) {
        RisultatiPrezzo<BigDecimal> prezzi = parametriCalcoloPrezzi.getPoliticaPrezzo().getPrezzi();
        RisultatiPrezzo<Sconto> sconti = parametriCalcoloPrezzi.getPoliticaPrezzo().getSconti();

        if (prezzi.size() > 1 && prezzi.get(0.0) != null) {
            prezzi.remove(0.0);
        }

        // qui trovo gli scaglioni uguali
        String previous = null;
        Double previousKey = null;
        List<Double> scaglioniExtra = new ArrayList<Double>();
        Set<Entry<Double, RisultatoPrezzo<BigDecimal>>> entrySet = prezzi.entrySet();

        for (Entry<Double, RisultatoPrezzo<BigDecimal>> entry : entrySet) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(entry.getKey() + " - " + entry.getValue());
            }

            if (previous != null && previous.equals(entry.getValue().getModuliPrezzoString())
                    && sconti.get(entry.getKey()) == null) {
                scaglioniExtra.add(previousKey);
            }
            previousKey = entry.getKey();
            previous = entry.getValue().getModuliPrezzoString();
        }

        // qui rimuovo gli scaglioni doppi, non posso farlo in fase di creazione, altrimenti non vengono calcolati
        // correttamente i prezzi e sconti degli scaglioni
        for (Double key : scaglioniExtra) {
            prezzi.remove(key);
        }

        // nel caso di listino normale (non a scaglioni) se ci sono dei contratti a soglia, devo rimuovere il listino a
        // 0.0 che influisce altrimenti sul calcolo del prezzo per gli scaglioni
        if (prezzi.size() > 1 && prezzi.get(ScaglioneListino.MAX_SCAGLIONE) == null) {

            Double lastKey = prezzi.lastKey();
            if (lastKey.compareTo(ScaglioneListino.MAX_SCAGLIONE) != 0) {
                RisultatoPrezzo<BigDecimal> last = prezzi.remove(lastKey);
                prezzi.put(ScaglioneListino.MAX_SCAGLIONE, last);
            }
        }

        return parametriCalcoloPrezzi;
    }

    /**
     * Alla quantità soglia specificata, in caso di conflitto, shifta lo scaglione esistente a sinistra di una frazione
     * di unità; nel caso in cui la quantità di soglia sia invece unica, crea un nuovo scaglione.
     *
     * @param rigaContrattoCalcolo
     *            rigaContrattoCalcolo
     * @param politicaPrezzo
     *            dove sono presenti gli scaglioni calcolati nei moduli precedenti
     * @param risultatoPrezzo
     *            il risultato prezzo associato alla soglia definita (da)
     * @param risultatoPrezzoListino
     *            il risultato prezzo associato alla soglia (fino a)
     * @param quantitaSogliaPrezzo
     *            la soglia associata al risultato prezzo
     */
    private void shiftScaglionePrezzoPerSoglia(RigaContrattoCalcolo rigaContrattoCalcolo, PoliticaPrezzo politicaPrezzo,
            RisultatoPrezzo<BigDecimal> risultatoPrezzo, RisultatoPrezzo<BigDecimal> risultatoPrezzoListino,
            double quantitaSogliaPrezzo) {
        BigDecimal result = BigDecimal.ONE.divide(BigDecimal.TEN.pow(rigaContrattoCalcolo.getNumeroDecimaliPrezzo()));
        // se ho già creato lo scaglione precedente a quello della quantitaSogliaPrezzo non devo fare nulla altrimenti
        // finisco per shiftare ulteriormente lo scaglione già shiftato
        if (politicaPrezzo.getPrezzi().get(quantitaSogliaPrezzo - result.doubleValue()) != null) {
            return;
        }

        if (risultatoPrezzo == null) {
            RisultatoPrezzo<BigDecimal> risultato = creaRisultatoPrezzoFake(rigaContrattoCalcolo);

            politicaPrezzo.getPrezzi().put(quantitaSogliaPrezzo - result.doubleValue(), risultato);
        } else {
            if (risultatoPrezzo.getQuantita() == quantitaSogliaPrezzo) {
                RisultatoPrezzo<BigDecimal> removedPrezzo = politicaPrezzo.getPrezzi().remove(quantitaSogliaPrezzo);
                politicaPrezzo.getPrezzi().put((quantitaSogliaPrezzo - result.doubleValue()), removedPrezzo);
            } else {
                RisultatoPrezzo<BigDecimal> risultatoDaUtilizzare = risultatoPrezzo;
                if (risultatoPrezzoListino != null && risultatoPrezzoListino.getQuantita() != 0.0) {
                    risultatoDaUtilizzare = risultatoPrezzoListino;
                }
                RisultatoPrezzo<BigDecimal> sogliaPrecedenteAggiuntiva = PanjeaEJBUtil
                        .cloneObject(risultatoDaUtilizzare);
                politicaPrezzo.getPrezzi().put(quantitaSogliaPrezzo - result.doubleValue(), sogliaPrecedenteAggiuntiva);
            }
        }
    }

}
