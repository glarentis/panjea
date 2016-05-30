package it.eurotn.panjea.contabilita.manager;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.Importo.TIPO_OPERAZIONE_VALUTA;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.ControPartita;
import it.eurotn.panjea.contabilita.manager.interfaces.IFormula;
import it.eurotn.panjea.contabilita.manager.interfaces.LiquidazioneIvaManager;
import it.eurotn.panjea.contabilita.manager.interfaces.VariabiliFormulaManager;
import it.eurotn.panjea.contabilita.manager.ritenutaacconto.interfaces.RitenutaAccontoContabilitaManager;
import it.eurotn.panjea.contabilita.util.LiquidazioneIvaDTO;
import it.eurotn.panjea.contabilita.util.ParametriCalcoloControPartite;
import it.eurotn.panjea.iva.domain.AreaIva;
import it.eurotn.panjea.iva.domain.RigaIva;
import it.eurotn.panjea.iva.manager.interfaces.AreaIvaManager;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;

/**
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.VariabiliFormulaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.VariabiliFormulaManager")
public class VariabiliFormulaManagerBean implements VariabiliFormulaManager {

    @EJB(mappedName = "Panjea.AreaIvaManager")
    private AreaIvaManager areaIvaManager;

    @EJB
    @IgnoreDependency
    private LiquidazioneIvaManager liquidazioneIvaManager;

    @EJB
    private RitenutaAccontoContabilitaManager ritenutaAccontoContabilitaManager;

    /**
     * Calcola il totale del documento.
     *
     * @param tipoOperazioneValuta
     *            indica se calcolare il totale in valuta o valutaAzienda
     * @param areaDocumento
     *            area del documento per estrarre il totale
     * @param areaIva
     *            area iva
     * @return totale del documento
     */
    private BigDecimal calcolaTOT(IAreaDocumento areaDocumento, TIPO_OPERAZIONE_VALUTA tipoOperazioneValuta,
            AreaIva areaIva) {

        BigDecimal totaleDocumento = BigDecimal.ZERO;
        switch (tipoOperazioneValuta) {
        case AZIENDA:
            totaleDocumento = areaDocumento.getDocumento().getTotale().getImportoInValutaAzienda();
            break;
        case VALUTA:
            totaleDocumento = areaDocumento.getDocumento().getTotale().getImportoInValuta();
            break;
        default:
            throw new UnsupportedOperationException("Operazione valuta non valida");
        }

        if (areaIva != null) {
            // calcolo il totale dell'imponibile + totale imposta per mettere il segno al totale documento
            BigDecimal totaleImponibile = areaIva.getTotaleImponibile(tipoOperazioneValuta);
            BigDecimal totaleImposta = areaIva.getTotaleImposta(tipoOperazioneValuta);
            BigDecimal totaleCalcolato = totaleImponibile.add(totaleImposta);

            totaleDocumento = totaleDocumento.abs();
            if (totaleCalcolato.compareTo(BigDecimal.ZERO) < 0) {
                totaleDocumento = totaleDocumento.negate();
            }
        }

        return totaleDocumento;
    }

    @Override
    public Map<String, BigDecimal> creaMapVariabili(IAreaDocumento areaDocumento, List<IFormula> formule,
            TIPO_OPERAZIONE_VALUTA tipoOperazioneValuta) {
        if (areaDocumento instanceof AreaContabile) {
            Map<String, BigDecimal> map = creaMapVariabiliFromAreaContabile((AreaContabile) areaDocumento, formule,
                    tipoOperazioneValuta);
            map.putAll(ritenutaAccontoContabilitaManager
                    .getMapVariabiliFromRitenutaAcconto((AreaContabile) areaDocumento));
            return map;
        }
        if (areaDocumento instanceof AreaMagazzino) {
            return creaMapVariabiliFromAreaMagazzino((AreaMagazzino) areaDocumento, formule, tipoOperazioneValuta);
        }
        throw new IllegalArgumentException("areaDocumento non prevista");
    }

    /**
     * Estrae le variabili dall'area contabile.
     *
     * @param areaContabile
     *            area contabile con le variabili
     * @param formule
     *            formule dove cercare le variabili
     * @param tipoOperazioneValuta
     *            indica se utilizzare gli importi con la valuta azienda o valuta
     * @return variabili contenute nelle formule calcolate.
     */
    private Map<String, BigDecimal> creaMapVariabiliFromAreaContabile(AreaContabile areaContabile,
            List<IFormula> formule, TIPO_OPERAZIONE_VALUTA tipoOperazioneValuta) {
        Map<String, BigDecimal> map = getMapVariabiliStrutturaContabile();

        // mi tengo il risultato della liquidazione per evitare di calcolarla
        // piu' volte
        LiquidazioneIvaDTO liquidazioneIvaDTO = null;

        AreaIva areaIva = areaIvaManager.caricaAreaIva(areaContabile);

        for (IFormula struttura : formule) {
            for (String formula : struttura.getFormule()) {
                // Parte Contabile pura
                if (formula != null) {
                    if (formula.contains(TOT)) {
                        map.put(TOT, calcolaTOT(areaContabile, tipoOperazioneValuta, areaIva));
                    }

                    if (formula.contains(TOTIVASP)) {
                        if (areaIva != null) {
                            map.put(TOTIVASP, areaIva.getSplitPaymentImposta());
                        }
                    }

                    if (formula.contains(IVA)) {
                        if (areaIva != null) {
                            map.put(IVA, areaIva.getTotaleImposta(tipoOperazioneValuta));
                        }
                    }

                    // if (formula.contains(IMP)) {
                    if (areaIva != null) {
                        map.put(IMP, areaIva.getTotaleImponibile(tipoOperazioneValuta));
                    }
                    // }

                    // se e' presente anche una sola variabile per la liquidazione la devo calcolare
                    if (liquidazioneIvaDTO == null
                            && (formula.contains(LIQCREDITO) || formula.contains(LIQDEBITO)
                                    || formula.contains(LIQRISULTATO))
                            || formula.contains(IVAACQINTRA) || formula.contains(LIQTOTALE)
                            || formula.contains(IVAVENDINTRA) || formula.contains(LIQPRORATA)) {
                        liquidazioneIvaDTO = liquidazioneIvaManager
                                .calcolaLiquidazione(areaContabile.getDataRegistrazione());
                        Map<String, BigDecimal> risutatoLiquidazione = liquidazioneIvaDTO.getTotaliPerido();
                        map.put(LIQCREDITO, risutatoLiquidazione.get(LiquidazioneIvaDTO.CREDITO_KEY));
                        map.put(LIQDEBITO, risutatoLiquidazione.get(LiquidazioneIvaDTO.DEBITO_KEY));
                        map.put(LIQRISULTATO, risutatoLiquidazione.get(LiquidazioneIvaDTO.RISULTATO_KEY));
                        map.put(LIQTOTALE, risutatoLiquidazione.get(LiquidazioneIvaDTO.RISULTATO_TOTALE_KEY));
                        map.put(LIQINCASSATO, risutatoLiquidazione.get(LiquidazioneIvaDTO.RISULTATO_INCASSATO_KEY));
                        map.put(LIQPRORATA, liquidazioneIvaDTO.getImportoProRata());
                        Map<String, BigDecimal> risultatoIntra = liquidazioneIvaDTO.getTotaliIntraPerido();
                        map.put(IVAACQINTRA, risultatoIntra.get(LiquidazioneIvaDTO.CREDITO_INTRA_KEY));
                        map.put(IVAVENDINTRA, risultatoIntra.get(LiquidazioneIvaDTO.DEBITO_INTRA_KEY));

                        if (risutatoLiquidazione
                                .get(LiquidazioneIvaDTO.MAGG_INTERESSI_TRIMESTRALI_IMPORTO_KEY) != null) {
                            map.put(LIQINTTRIM,
                                    risultatoIntra.get(LiquidazioneIvaDTO.MAGG_INTERESSI_TRIMESTRALI_IMPORTO_KEY));
                        }
                    }
                }
            }
        }
        return map;
    }

    /**
     * Estrae le variabili dall'area magazzino.
     *
     * @param areaMagazzino
     *            area magazzino con le variabili
     * @param formule
     *            formule dove cercare le variabili
     * @param tipoOperazioneValuta
     *            indica se utilizzare gli importi con la valuta azienda o valuta
     * @return variabili contenute nelle formule calcolate.
     */
    private Map<String, BigDecimal> creaMapVariabiliFromAreaMagazzino(AreaMagazzino areaMagazzino,
            List<IFormula> formule, TIPO_OPERAZIONE_VALUTA tipoOperazioneValuta) {
        Map<String, BigDecimal> map = getMapVariabiliStrutturaContabile();

        AreaIva areaIva = areaIvaManager.caricaAreaIvaByDocumento(areaMagazzino.getDocumento());

        for (IFormula struttura : formule) {
            for (String formula : struttura.getFormule()) {
                if (formula != null) {
                    if (formula.contains(TOT)) {
                        map.put(TOT, calcolaTOT(areaMagazzino, tipoOperazioneValuta, areaIva));
                    }

                    if (formula.contains(IVA)) {
                        if (areaIva != null) {
                            map.put(IVA, areaIva.getTotaleImposta(tipoOperazioneValuta));
                        }
                    }

                    if (formula.contains(IMP)) {
                        if (areaIva != null) {
                            map.put(IMP, areaIva.getTotaleImponibile(tipoOperazioneValuta));
                        }
                    }
                }
            }
        }
        return map;
    }

    @Override
    public Map<String, BigDecimal> getMapVariabiliControPartite() {
        Map<String, BigDecimal> mapVariabiliControPartite = new HashMap<String, BigDecimal>();
        mapVariabiliControPartite.put(TOTDOC, BigDecimal.ZERO); // Totale
        // documento
        mapVariabiliControPartite.put(TOTIMP, BigDecimal.ZERO); // Totale
        // imponibile
        mapVariabiliControPartite.put(TOTIVA, BigDecimal.ZERO); // Totale
        // imposta
        mapVariabiliControPartite.put(TOTNIM, BigDecimal.ZERO); // Totale non
        // imponibile
        mapVariabiliControPartite.put(TOTIND, BigDecimal.ZERO); // Totale
        // imposta
        // indetraibile
        mapVariabiliControPartite.put(TOTDET, BigDecimal.ZERO); // Totale
        // imposta
        // detraibile
        mapVariabiliControPartite.put(RIGIMP, BigDecimal.ZERO); // Imponibile
        // di riga
        mapVariabiliControPartite.put(RIGNIM, BigDecimal.ZERO); // Non
        // imponibile
        // di riga
        mapVariabiliControPartite.put(RIGIVA, BigDecimal.ZERO); // Imposta di
        // riga
        mapVariabiliControPartite.put(RIGIND, BigDecimal.ZERO); // Imposta
        // indetraibile
        // di riga
        mapVariabiliControPartite.put(RIGDET, BigDecimal.ZERO); // Imposta
        // detraibile
        // di riga

        return mapVariabiliControPartite;
    }

    @Override
    public ParametriCalcoloControPartite getMapVariabiliFromControPartite(AreaContabile areaContabile,
            List<ControPartita> list) {
        Map<String, BigDecimal> mapTmp = null;

        ParametriCalcoloControPartite parametriCalcoloControPartite = new ParametriCalcoloControPartite();

        AreaIva areaIva = areaIvaManager.caricaAreaIva(areaContabile);

        // trovo subito il valore del totale documento
        BigDecimal totDoc = calcolaTOT(areaContabile, TIPO_OPERAZIONE_VALUTA.AZIENDA, areaIva);
        BigDecimal totImp = BigDecimal.ZERO;
        BigDecimal totNim = BigDecimal.ZERO;

        BigDecimal totIVA = BigDecimal.ZERO;
        BigDecimal totDet = BigDecimal.ZERO;
        BigDecimal totInd = BigDecimal.ZERO;

        List<RigaIva> righeIva = new ArrayList<RigaIva>();

        if (areaIva != null) {
            righeIva = areaIva.getRigheIva();
        }

        // ciclo sulle righe iva
        for (RigaIva rigaIva : righeIva) {

            BigDecimal rigImp = BigDecimal.ZERO;
            BigDecimal rigNim = BigDecimal.ZERO;
            BigDecimal rigInd = BigDecimal.ZERO;

            CodiceIva codiceIva = rigaIva.getCodiceIva();

            rigInd = rigaIva.getImposta().getImportoInValutaAzienda().multiply(codiceIva.getPercIndetraibilita())
                    .divide(Importo.HUNDRED).setScale(2, RoundingMode.HALF_UP);

            totIVA = totIVA.add(rigaIva.getImposta().getImportoInValutaAzienda());
            totInd = totInd.add(rigInd);
            totDet = totDet.add(rigaIva.getImposta().getImportoInValutaAzienda().subtract(rigInd));

            switch (codiceIva.getTipoCodiceIva()) {
            case NORMALE:
                totImp = totImp.add(rigaIva.getImponibile().getImportoInValutaAzienda());
                rigImp = rigaIva.getImponibile().getImportoInValutaAzienda();
                break;
            default:
                totNim = totNim.add(rigaIva.getImponibile().getImportoInValutaAzienda());
                rigNim = rigaIva.getImponibile().getImportoInValutaAzienda();
                break;
            }

            mapTmp = getMapVariabiliControPartite();

            mapTmp.put(RIGIMP, rigImp.abs());
            mapTmp.put(RIGNIM, rigNim.abs());
            mapTmp.put(RIGIVA, rigaIva.getImposta().getImportoInValutaAzienda().abs());
            mapTmp.put(RIGIND, rigInd.abs());
            mapTmp.put(RIGDET, rigaIva.getImposta().getImportoInValutaAzienda().subtract(rigInd).abs());

            parametriCalcoloControPartite.put(codiceIva.getCodice(), mapTmp);
        }

        // scorro tutto quello che ho inserito finora e aggiorno i valori delle
        // variabili globali
        Set<String> chiavi = parametriCalcoloControPartite.keySet();
        for (String chiave : chiavi) {
            parametriCalcoloControPartite.get(chiave).put(TOTDOC, totDoc.abs());
            parametriCalcoloControPartite.get(chiave).put(TOTIMP, totImp.abs());
            parametriCalcoloControPartite.get(chiave).put(TOTIVA, totIVA.abs());
            parametriCalcoloControPartite.get(chiave).put(TOTNIM, totNim.abs());
            parametriCalcoloControPartite.get(chiave).put(TOTIND, totInd.abs());
            parametriCalcoloControPartite.get(chiave).put(TOTDET, totDet.abs());
        }

        // aggiungo la map dei totali generali che non tengono conto del codice
        // iva
        mapTmp = getMapVariabiliControPartite();
        mapTmp.put(TOTDOC, totDoc.abs());
        mapTmp.put(TOTIMP, totImp.abs());
        mapTmp.put(TOTIVA, totIVA.abs());
        mapTmp.put(TOTNIM, totNim.abs());
        mapTmp.put(TOTIND, totInd.abs());
        mapTmp.put(TOTDET, totDet.abs());

        parametriCalcoloControPartite.put(ParametriCalcoloControPartite.SENZA_CODICE_IVA, mapTmp);

        return parametriCalcoloControPartite;
    }

    @Override
    public Map<String, BigDecimal> getMapVariabiliStrutturaContabile() {
        HashMap<String, BigDecimal> mapVariabiliStrutturaContabile = new HashMap<String, BigDecimal>();
        mapVariabiliStrutturaContabile.put(IMP, BigDecimal.ZERO);
        mapVariabiliStrutturaContabile.put(IVA, BigDecimal.ZERO);
        mapVariabiliStrutturaContabile.put(TOTIVASP, BigDecimal.ZERO);
        mapVariabiliStrutturaContabile.put(TOT, BigDecimal.ZERO);
        mapVariabiliStrutturaContabile.put(LIQDEBITO, BigDecimal.ZERO);
        mapVariabiliStrutturaContabile.put(LIQCREDITO, BigDecimal.ZERO);
        mapVariabiliStrutturaContabile.put(LIQRISULTATO, BigDecimal.ZERO);
        mapVariabiliStrutturaContabile.put(LIQTOTALE, BigDecimal.ZERO);
        mapVariabiliStrutturaContabile.put(IVAACQINTRA, BigDecimal.ZERO);
        mapVariabiliStrutturaContabile.put(IVAVENDINTRA, BigDecimal.ZERO);
        mapVariabiliStrutturaContabile.put(LIQINTTRIM, BigDecimal.ZERO);
        mapVariabiliStrutturaContabile.put(LIQINCASSATO, BigDecimal.ZERO);
        mapVariabiliStrutturaContabile.put(LIQPRORATA, BigDecimal.ZERO);
        return mapVariabiliStrutturaContabile;
    }

    @Override
    public Map<String, BigDecimal> getMapVariabiliStrutturaPartite() {
        HashMap<String, BigDecimal> mapVariabiliStrutturaPartite = new HashMap<String, BigDecimal>();
        mapVariabiliStrutturaPartite.put(IMP, BigDecimal.ZERO);
        mapVariabiliStrutturaPartite.put(IVA, BigDecimal.ZERO);
        mapVariabiliStrutturaPartite.put(TOT, BigDecimal.ZERO);
        mapVariabiliStrutturaPartite.put(LIQDEBITO, BigDecimal.ZERO);
        mapVariabiliStrutturaPartite.put(LIQCREDITO, BigDecimal.ZERO);
        mapVariabiliStrutturaPartite.put(LIQRISULTATO, BigDecimal.ZERO);
        mapVariabiliStrutturaPartite.put(LIQTOTALE, BigDecimal.ZERO);
        mapVariabiliStrutturaPartite.put(IVAACQINTRA, BigDecimal.ZERO);
        mapVariabiliStrutturaPartite.put(IVAVENDINTRA, BigDecimal.ZERO);
        mapVariabiliStrutturaPartite.put(LIQINTTRIM, BigDecimal.ZERO);

        return mapVariabiliStrutturaPartite;

    }
}
