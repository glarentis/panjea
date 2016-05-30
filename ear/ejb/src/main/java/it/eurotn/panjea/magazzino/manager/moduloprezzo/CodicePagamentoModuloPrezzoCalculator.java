package it.eurotn.panjea.magazzino.manager.moduloprezzo;

import java.math.BigDecimal;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.Sconto;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.ParametriCalcoloPrezzi;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.RisultatiPrezzo;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.RisultatoModuloPrezzo;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.RisultatoModuloPrezzo.EStrategia;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.RisultatoPrezzo;
import it.eurotn.panjea.magazzino.manager.moduloprezzo.interfaces.ModuloPrezzoCalculator;

@Stateless(name = "Panjea.CodicePagamentoModuloPrezzoCalculator")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.CodicePagamentoModuloPrezzoCalculator")
public class CodicePagamentoModuloPrezzoCalculator implements ModuloPrezzoCalculator {

    public static final String TIPO_MODULO = "CODICE PAGAMENTO";

    /**
     * Aggiunge uno scaglione a 0 per lo sconto commerciale.
     *
     * @param parametriCalcoloPrezzi
     *            parametriCalcoloPrezzi
     * @param scaglione
     *            scaglione di riferimento per il risultato sconto
     * @return RisultatoPrezzo<Sconto>
     */
    private RisultatoPrezzo<Sconto> addScaglioneScontoCommerciale(ParametriCalcoloPrezzi parametriCalcoloPrezzi,
            Double scaglione) {
        RisultatoModuloPrezzo<Sconto> risultatoModuloScontoDaAggiungere = creaRisultatoModuloSconto(
                parametriCalcoloPrezzi.getPercentualeScontoCommerciale(), 0.0, EStrategia.ASSEGNAZIONE);

        RisultatoPrezzo<Sconto> risultatoScontoDaAggiungere = creaRisultatoSconto(
                parametriCalcoloPrezzi.getPercentualeScontoCommerciale(), scaglione);
        risultatoScontoDaAggiungere.addRisultatoModuloPrezzo(risultatoModuloScontoDaAggiungere);

        return risultatoScontoDaAggiungere;
    }

    @Override
    public ParametriCalcoloPrezzi calcola(ParametriCalcoloPrezzi parametriCalcoloPrezzi) {
        // se lo sconto commerciale è diverso da 0 aggiungo il modulo prezzo per tutti gli scaglioni esistenti
        if (parametriCalcoloPrezzi.getPercentualeScontoCommerciale() == null
                || (parametriCalcoloPrezzi.getPercentualeScontoCommerciale() != null
                        && parametriCalcoloPrezzi.getPercentualeScontoCommerciale().compareTo(BigDecimal.ZERO) == 0)) {
            return parametriCalcoloPrezzi;
        }

        parametriCalcoloPrezzi.getPoliticaPrezzo().setSconto1Bloccato(true);
        parametriCalcoloPrezzi.getPoliticaPrezzo().setPoliticaScontiPresenti(Boolean.TRUE);

        RisultatiPrezzo<Sconto> risultatiSconto = parametriCalcoloPrezzi.getPoliticaPrezzo().getSconti();
        RisultatiPrezzo<Sconto> nuoviRisultati = new RisultatiPrezzo<>();
        Set<Double> scaglioni = risultatiSconto.keySet();

        for (Double scaglione : scaglioni) {
            RisultatoPrezzo<Sconto> risultatoSconto = risultatiSconto.get(scaglione);

            if (risultatoSconto != null) {

                Sconto scontoRis = new Sconto();
                scontoRis.setSconto4(risultatoSconto.getValue().getSconto3());
                scontoRis.setSconto3(risultatoSconto.getValue().getSconto2());
                scontoRis.setSconto2(risultatoSconto.getValue().getSconto1());
                scontoRis.setSconto1(parametriCalcoloPrezzi.getPercentualeScontoCommerciale());
                risultatoSconto.setValue(scontoRis);

                RisultatoModuloPrezzo<Sconto> risultatoModuloPrezzo = creaRisultatoModuloSconto(
                        parametriCalcoloPrezzi.getPercentualeScontoCommerciale(), 0.0, EStrategia.VARIAZIONE);
                risultatoSconto.addRisultatoModuloPrezzo(risultatoModuloPrezzo);

                nuoviRisultati.put(scaglione, risultatoSconto);
            }
            if (scaglione != 0) {
                RisultatoPrezzo<Sconto> risultatoScontoDaAggiungere = addScaglioneScontoCommerciale(
                        parametriCalcoloPrezzi, scaglione);
                nuoviRisultati.put(0.0, risultatoScontoDaAggiungere);
            }
        }

        if (risultatiSconto.isEmpty()) {
            RisultatoPrezzo<Sconto> risultatoScontoDaAggiungere = addScaglioneScontoCommerciale(parametriCalcoloPrezzi,
                    0.0);
            nuoviRisultati.put(0.0, risultatoScontoDaAggiungere);
        }

        if (parametriCalcoloPrezzi.getPoliticaPrezzo().getPrezzi().isEmpty()) {
            RisultatoPrezzo<BigDecimal> risultatoPrezzoFake = creaRisultatoPrezzoFake();
            parametriCalcoloPrezzi.getPoliticaPrezzo().getPrezzi().put(0.0, risultatoPrezzoFake);
        }

        parametriCalcoloPrezzi.getPoliticaPrezzo().getSconti().putAll(nuoviRisultati);
        return parametriCalcoloPrezzi;
    }

    @Override
    public Set<ArticoloLite> caricaArticoli(ParametriCalcoloPrezzi parametriCalcoloPrezzi) {
        // non faccio niente perchè al momento la carica articoli è tutta da rivedere.
        return new TreeSet<ArticoloLite>();
    }

    /**
     * Crea Risultato Modulo Sconto.
     *
     * @param percentualeScontoCommerciale
     *            percentualeScontoCommerciale
     * @param qta
     *            qta
     * @param strategia
     *            strategia
     * @return RisultatoModuloPrezzo<Sconto>
     */
    private RisultatoModuloPrezzo<Sconto> creaRisultatoModuloSconto(BigDecimal percentualeScontoCommerciale, Double qta,
            EStrategia strategia) {
        RisultatoModuloPrezzo<Sconto> risultatoModuloPrezzo = new RisultatoModuloPrezzo<Sconto>();
        risultatoModuloPrezzo.setAbilitato(true);
        risultatoModuloPrezzo.setCodiceModulo("CODICE PAGAMENTO");
        risultatoModuloPrezzo.setDescrizioneModulo("Sconto commerciale");
        risultatoModuloPrezzo.setQuantita(qta.toString());
        risultatoModuloPrezzo.setTipoModulo(TIPO_MODULO);
        risultatoModuloPrezzo.setStrategia(strategia);

        Sconto sconto = new Sconto();
        sconto.setSconto1(percentualeScontoCommerciale);
        sconto.setSconto2(BigDecimal.ZERO);
        sconto.setSconto3(BigDecimal.ZERO);
        sconto.setSconto4(BigDecimal.ZERO);
        risultatoModuloPrezzo.setValue(sconto);
        return risultatoModuloPrezzo;
    }

    /**
     * Ritorna un RisultatoPrezzo<BigDecimal> a 0 senza strategia.
     *
     * @return RisultatoPrezzo<BigDecimal>
     */
    private RisultatoPrezzo<BigDecimal> creaRisultatoPrezzoFake() {
        RisultatoModuloPrezzo<BigDecimal> risultatoModuloPrezzo = new RisultatoModuloPrezzo<BigDecimal>();
        risultatoModuloPrezzo.setAbilitato(true);
        risultatoModuloPrezzo.setCodiceModulo("CODICE PAGAMENTO");
        risultatoModuloPrezzo.setDescrizioneModulo("Sconto commerciale");
        risultatoModuloPrezzo.setQuantita("0.0");
        risultatoModuloPrezzo.setTipoModulo(TIPO_MODULO);

        RisultatoPrezzo<BigDecimal> risultato = new RisultatoPrezzo<BigDecimal>();
        risultato.setValue(BigDecimal.ZERO);
        risultato.setQuantita(0.0);
        risultato.setModuliPrezzoString(risultatoModuloPrezzo.serializeToString());
        return risultato;
    }

    /**
     * @param percentualeScontoCommerciale
     *            percentuale da applicare
     * @param qta
     *            quantità soglia
     * @return risultatoPrezzo con le proprietà impostate
     */
    private RisultatoPrezzo<Sconto> creaRisultatoSconto(BigDecimal percentualeScontoCommerciale, Double qta) {

        Sconto sconto = new Sconto();
        sconto.setSconto1(percentualeScontoCommerciale);
        sconto.setSconto2(BigDecimal.ZERO);
        sconto.setSconto3(BigDecimal.ZERO);
        sconto.setSconto4(BigDecimal.ZERO);

        RisultatoPrezzo<Sconto> risultato = new RisultatoPrezzo<Sconto>();
        risultato.setNumeroDecimali(0);
        risultato.setValue(sconto);
        risultato.setQuantita(qta);
        return risultato;
    }

}
