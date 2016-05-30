package it.eurotn.panjea.contabilita.manager.interfaces;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;
import it.eurotn.panjea.anagrafica.domain.Importo.TIPO_OPERAZIONE_VALUTA;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.ControPartita;
import it.eurotn.panjea.contabilita.util.ParametriCalcoloControPartite;

/**
 * Gestisce le variabili da inserire nelle formule.
 *
 * @author giangi
 */
@Local
public interface VariabiliFormulaManager {

    /**
     *
     * @return variabili utilizzati per la struttira contabile
     */
    String TOTDOC = "TOTDOC";
    String TOTIMP = "TOTIMP";
    String TOTIVA = "TOTIVA";
    String TOTNIM = "TOTNIM";
    String TOTIND = "TOTIND";
    String TOTDET = "TOTDET";
    String TOTIVASP = "IVASPLITPAYMENT";
    String RIGIMP = "RIGIMP";
    String RIGNIM = "RIGNIM";
    String RIGIVA = "RIGIVA";
    String RIGIND = "RIGIND";
    String RIGDET = "RIGDET";
    String IMP = "IMP";
    String IVA = "IVA";
    String TOT = "TOT";
    String LIQDEBITO = "LIQDEBITO";
    String LIQCREDITO = "LIQCREDITO";
    String LIQRISULTATO = "LIQRISULTATO";
    String LIQTOTALE = "LIQTOTALE";
    String IVAVENDINTRA = "IVAVENDINTRA";
    String IVAACQINTRA = "IVAACQINTRA";
    String LIQINTTRIM = "LIQINTTRIM";
    String LIQINCASSATO = "LIQINCASSATO";

    String LIQPRORATA = "LIQPRORATA";;

    // variabili ritenuta d'acconto
    String IMPRITENUTA = "IMPRITENUTA";
    String IMPFONDOPROF = "IMPFONDOPROF";
    String IMPPREVIDENZIALE = "IMPPREVIDENZIALE";
    String IMPPREVIDENZIALELAVORATORE = "IMPPREVIDENZIALELAVORATORE";
    String IMPPREVIDENZIALEAZIENDA = "IMPPREVIDENZIALEAZIENDA";

    /**
     * Avvalora tutte le varibili con il relativo importo dipendentemente dall'areaDocumento.
     *
     * @param areaDocumento
     *            areaDocumento
     * @param formule
     *            formule
     * @param tipoOperazioneValuta
     *            tipo di valuta utilizzare, aziendale o valuta.
     * @return Map<String, BigDecimal>
     */
    Map<String, BigDecimal> creaMapVariabili(IAreaDocumento areaDocumento, List<IFormula> formule,
            TIPO_OPERAZIONE_VALUTA tipoOperazioneValuta);

    /**
     * Crea la mappa che contiene tutte le variabili che possono essere usate per creare la formula di una contro
     * partita.
     *
     * @return mappa con le variabili esistenti settate a zero.
     */
    Map<String, BigDecimal> getMapVariabiliControPartite();

    /**
     * Scorre le contro partite e avvalora tutte le varibili con il relativo importo.
     *
     * @param areaContabile
     *            areaContabile interessata
     * @param controPartite
     *            template con le contropartite
     * @return parametri per il calcolo delel controPartite
     */
    ParametriCalcoloControPartite getMapVariabiliFromControPartite(AreaContabile areaContabile,
            List<ControPartita> controPartite);

    /**
     *
     * @return mappa che contiene tutte le variabili che possono essere usate per creare la formula di una struttura
     *         contabile.
     */
    Map<String, BigDecimal> getMapVariabiliStrutturaContabile();

    /**
     * Metodo che ritorna la map delle variabili per le formule della struttura partite.<br>
     * Sono valorizzate dagli importi dell'area contabile<br>
     *
     * @return Map<String, BigDecimal>
     */
    Map<String, BigDecimal> getMapVariabiliStrutturaPartite();
}
