package it.eurotn.panjea.contabilita.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.contabilita.domain.RegistroIva;
import it.eurotn.panjea.contabilita.domain.RegistroIva.TipoRegistro;
import it.eurotn.panjea.rate.domain.Rata;
import it.eurotn.panjea.tesoreria.domain.Pagamento;

/**
 *
 */
public class LiquidazioneIvaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String DEBITO_KEY = "debito";
    public static final String CREDITO_KEY = "credito";
    public static final String RISULTATO_PRECEDENTE_KEY = "risultatoPrecedente";
    public static final String RISULTATO_KEY = "risultato";
    public static final String RISULTATO_TOTALE_KEY = "risultatoTotale";
    public static final String RISULTATO_TOTALE_CON_ACCONTO_KEY = "risultatoTotaleConAcconto";
    public static final String RISULTATO_INCASSATO_KEY = "risultatoIvaIncassata";
    public static final String DEBITO_INTRA_KEY = "debitoIntra";
    public static final String CREDITO_INTRA_KEY = "creditoIntra";
    public static final String MAGG_INTERESSI_TRIMESTRALI_IMPORTO_KEY = "maggInteressiTrimenstraliImporto";
    public static final String MAGG_INTERESSI_TRIMESTRALI_VALORE_KEY = "maggInteressiTrimenstraliValore";
    public static final String RISULTATO_INTRA_KEY = "risultatoIntra";
    public static final String RISULTATO_ACCONTO_DICEMBRE = "risultatoAccontoDicembre";

    private Date dataInizio;
    private Date dataFine;

    private Map<RegistroIva, List<TotaliCodiceIvaDTO>> totaliRegistri;

    // righe raggruppate per codice iva divise per registro iva per
    // le righe legate al doc. di tipoareacontabile con gestione iva INTRA
    private Map<RegistroIva, List<TotaliCodiceIvaDTO>> totaliRegistriIntra;

    private Map<RegistroIva, List<TotaliCodiceIvaDTO>> ventilazioniIva;

    private Map<RegistroIva, List<TotaliCodiceIvaDTO>> totaliIvaSospesa;

    // totale diviso per registro iva della somma delle righe totaliRegistri
    private Map<RegistroIva, BigDecimal> totali;

    // totale diviso per registro iva della somma delle righe totaliSplitPayment
    private Map<RegistroIva, BigDecimal> totaliSplitPayment;

    // totale diviso per registro iva della somma delle righe
    // totaliRegistriIntra
    private Map<RegistroIva, BigDecimal> totaliIntra;

    // totale diviso per registro della somma delle righe totaliIvaSospesa
    private Map<RegistroIva, BigDecimal> totaliIncassato;

    private Rata rataPrecedente;

    // serve per impostare l'importo dalla rataPrecedente come
    // risultatoPrecedente della liquidazione
    private BigDecimal minimaleIVA = null;

    private BigDecimal percTrimestraleValore;

    private BigDecimal percTrimestraleImporto;

    // il valore del totale del documento acconto iva di dicembre
    private BigDecimal totaleAccontoIvaDicembre;

    private BigDecimal importoProRata;

    private TotaliCodiceIvaDTO volumeAffariTotale;

    private TotaliCodiceIvaDTO beniStrumentaliTotale;

    private TotaliCodiceIvaDTO volumeAffariAziende;

    private TotaliCodiceIvaDTO volumeAffariPrivati;

    /**
     * Le righe iva con codice iva volume affari SI e documenti a entità senza partita iva.
     */
    private List<TotaliCodiceIvaDTO> righeVolumeAffariPrivati;

    /**
     * i totali raggruppati per tipo documento dei pagamenti sulle rate dei documenti di liquidazione.
     */
    private Map<TipoDocumento, BigDecimal> totaliPagamento;

    /**
     * Aggiunge l'importo al registro.
     * 
     * @param registroIva
     *            registroIva
     * @param importo
     *            importo
     */
    public void addToTotali(RegistroIva registroIva, BigDecimal importo) {
        if (totali == null) {
            totali = new HashMap<RegistroIva, BigDecimal>();
        }
        totali.put(registroIva, importo);
    }

    /**
     * Aggiunge l'importo al registro.
     * 
     * @param registroIva
     *            registroIva
     * @param importo
     *            importo
     */
    public void addToTotaliIncassato(RegistroIva registroIva, BigDecimal importo) {
        if (totaliIncassato == null) {
            totaliIncassato = new HashMap<RegistroIva, BigDecimal>();
        }
        totaliIncassato.put(registroIva, importo);
    }

    /**
     * Aggiunge l'importo al registro.
     * 
     * @param registroIva
     *            registroIva
     * @param importo
     *            importo
     */
    public void addToTotaliIntra(RegistroIva registroIva, BigDecimal importo) {
        if (totaliIntra == null) {
            totaliIntra = new HashMap<RegistroIva, BigDecimal>();
        }
        totaliIntra.put(registroIva, importo);
    }

    /**
     * Aggiunge la lista di codici iva al registro.
     * 
     * @param registroIva
     *            registroIva
     * @param list
     *            list
     */
    public void addToTotaliIvaSospesa(RegistroIva registroIva, List<TotaliCodiceIvaDTO> list) {
        if (totaliIvaSospesa == null) {
            totaliIvaSospesa = new HashMap<RegistroIva, List<TotaliCodiceIvaDTO>>();
        }
        totaliIvaSospesa.put(registroIva, list);
    }

    public void addToTotaliPagamento(TipoDocumento tipoDocumento, BigDecimal importo) {
        if (totaliPagamento == null) {
            totaliPagamento = new HashMap<TipoDocumento, BigDecimal>();
        }
        totaliPagamento.put(tipoDocumento, importo);
    }

    public void addToTotaliRegistri(RegistroIva registroIva, List<TotaliCodiceIvaDTO> list) {
        if (totaliRegistri == null) {
            totaliRegistri = new HashMap<RegistroIva, List<TotaliCodiceIvaDTO>>();
        }
        totaliRegistri.put(registroIva, list);
    }

    public void addToTotaliRegistriIntra(RegistroIva registroIva, List<TotaliCodiceIvaDTO> list) {
        if (totaliRegistriIntra == null) {
            totaliRegistriIntra = new HashMap<RegistroIva, List<TotaliCodiceIvaDTO>>();
        }
        totaliRegistriIntra.put(registroIva, list);
    }

    /**
     * Aggiunge l'importoSplitPayment al registro.
     * 
     * @param registroIva
     *            registroIva
     * @param importo
     *            importo
     */
    public void addToTotaliSplitPayment(RegistroIva registroIva, BigDecimal importo) {
        if (totaliSplitPayment == null) {
            totaliSplitPayment = new HashMap<RegistroIva, BigDecimal>();
        }
        totaliSplitPayment.put(registroIva, importo);
    }

    public void addToTotaliVentilazioni(RegistroIva registroIva, List<TotaliCodiceIvaDTO> list) {
        if (ventilazioniIva == null) {
            ventilazioniIva = new HashMap<RegistroIva, List<TotaliCodiceIvaDTO>>();
        }
        ventilazioniIva.put(registroIva, list);
    }

    /**
     * @return the beniStrumentaliTotale
     */
    public TotaliCodiceIvaDTO getBeniStrumentaliTotale() {
        if (beniStrumentaliTotale == null) {
            TotaliCodiceIvaDTO tot = new TotaliCodiceIvaDTO();
            tot.setImponibile(BigDecimal.ZERO);
            tot.setImposta(BigDecimal.ZERO);
            return tot;
        }
        return beniStrumentaliTotale;
    }

    /**
     * @return the dataFine
     */
    public Date getDataFine() {
        return dataFine;
    }

    /**
     * @return the dataInizio
     */
    public Date getDataInizio() {
        return dataInizio;
    }

    /**
     * @return Returns the importoProRata.
     */
    public BigDecimal getImportoProRata() {
        if (importoProRata == null) {
            return BigDecimal.ZERO;
        }
        return importoProRata;
    }

    /**
     * @return the minimaleIva
     * @uml.property name="minimaleIVA"
     */
    public BigDecimal getMinimaleIVA() {
        return minimaleIVA;
    }

    /**
     * @return the percTrimestraleImporto
     * @uml.property name="percTrimestraleImporto"
     */
    public BigDecimal getPercTrimestraleImporto() {
        return percTrimestraleImporto;
    }

    /**
     * @return the percTrimestraleValore
     * @uml.property name="percTrimestraleValore"
     */
    public BigDecimal getPercTrimestraleValore() {
        return percTrimestraleValore;
    }

    /**
     * @return the rataPrecedente
     * @uml.property name="rataPrecedente"
     */
    public Rata getRataPrecedente() {
        return rataPrecedente;
    }

    /**
     * @return the righeVolumeAffariPrivati
     */
    public List<TotaliCodiceIvaDTO> getRigheVolumeAffariPrivati() {
        return righeVolumeAffariPrivati;
    }

    /**
     * @return the totali
     * @uml.property name="totali"
     */
    public Map<RegistroIva, BigDecimal> getTotali() {
        return totali;
    }

    /**
     * @return the totaliIncassato
     * @uml.property name="totaliIncassato"
     */
    public Map<RegistroIva, BigDecimal> getTotaliIncassato() {
        return totaliIncassato;
    }

    /**
     * @return the totaliIntra
     * @uml.property name="totaliIntra"
     */
    public Map<RegistroIva, BigDecimal> getTotaliIntra() {
        return totaliIntra;
    }

    /**
     * Restituisce i totali di dare e avere per il periodo delle sole righe legate ad un documento con gestione iva
     * INTRA.
     * 
     * @return Mappa contenente i totali con chiavi "debito" "credito" "risultato"
     */
    public Map<String, BigDecimal> getTotaliIntraPerido() {
        Map<String, BigDecimal> totaliIntraPerido = new HashMap<String, BigDecimal>();
        BigDecimal debito = BigDecimal.ZERO;
        BigDecimal credito = BigDecimal.ZERO;

        for (Map.Entry<RegistroIva, BigDecimal> entry : totaliIntra.entrySet()) {
            if (entry.getKey().getTipoRegistro() == TipoRegistro.VENDITA) {
                debito = debito.add(entry.getValue());
            }
            if (entry.getKey().getTipoRegistro() == TipoRegistro.ACQUISTO) {
                credito = credito.add(entry.getValue());
            }
        }
        totaliIntraPerido.put(DEBITO_INTRA_KEY, debito);
        totaliIntraPerido.put(CREDITO_INTRA_KEY, credito);
        totaliIntraPerido.put(RISULTATO_INTRA_KEY, debito.subtract(credito));
        return totaliIntraPerido;
    }

    /**
     * @return the totaliIvaSospesa
     * @uml.property name="totaliIvaSospesa"
     */
    public Map<RegistroIva, List<TotaliCodiceIvaDTO>> getTotaliIvaSospesa() {
        return totaliIvaSospesa;
    }

    /**
     * @return the totaliPagamento
     */
    public Map<TipoDocumento, BigDecimal> getTotaliPagamento() {
        return totaliPagamento;
    }

    /**
     * Restituisce i totali di dare e avere per il periodo.
     * 
     * @return Mappa contenente i totali con chiavi "debito" "credito" "risultato"
     */
    public Map<String, BigDecimal> getTotaliPerido() {
        Map<String, BigDecimal> totaliPeriodo = new HashMap<String, BigDecimal>();
        BigDecimal debito = BigDecimal.ZERO;
        BigDecimal credito = BigDecimal.ZERO;

        for (Map.Entry<RegistroIva, BigDecimal> entry : totali.entrySet()) {
            if (entry.getKey().getTipoRegistro() == TipoRegistro.VENDITA
                    || entry.getKey().getTipoRegistro() == TipoRegistro.CORRISPETTIVO) {
                debito = debito.add(entry.getValue());
            }
            if (entry.getKey().getTipoRegistro() == TipoRegistro.ACQUISTO) {
                credito = credito.add(entry.getValue());
            }
        }
        // se ho splitPayment devo sottrarre i totali per registro presenti sul totale altrimenti mi ritrovo aggiunto il
        // valore dello splitPayment; NB. non lo escludo dalle righe considerate per il totale perchè devo mostrarlo nel
        // totale come voce normale assieme a tutti i registri
        if (totaliSplitPayment != null) {
            for (Map.Entry<RegistroIva, BigDecimal> entry : totaliSplitPayment.entrySet()) {
                if (entry.getKey().getTipoRegistro() == TipoRegistro.VENDITA
                        || entry.getKey().getTipoRegistro() == TipoRegistro.CORRISPETTIVO) {
                    debito = debito.subtract(entry.getValue());
                }
                if (entry.getKey().getTipoRegistro() == TipoRegistro.ACQUISTO) {
                    credito = credito.subtract(entry.getValue());
                }
            }
        }

        BigDecimal debitoIncassato = BigDecimal.ZERO;
        BigDecimal creditoIncassato = BigDecimal.ZERO;
        BigDecimal totaleIncassato = BigDecimal.ZERO;
        // tot incassato può rimanere null dato che l'incassato e' dovuto alla
        // gestione iva sospesa
        if (totaliIncassato != null) {
            for (Map.Entry<RegistroIva, BigDecimal> entry : totaliIncassato.entrySet()) {
                if (entry.getKey().getTipoRegistro() == TipoRegistro.VENDITA
                        || entry.getKey().getTipoRegistro() == TipoRegistro.CORRISPETTIVO) {
                    debitoIncassato = debitoIncassato.add(entry.getValue());
                }
                if (entry.getKey().getTipoRegistro() == TipoRegistro.ACQUISTO) {
                    creditoIncassato = creditoIncassato.add(entry.getValue());
                }
            }
        }
        // il tot incassato e' gia' stato aggiunto al totale generale quindi qui
        // calcolo solo
        // l'incassato
        totaleIncassato = debitoIncassato.subtract(creditoIncassato);

        totaliPeriodo.put(DEBITO_KEY, debito);
        totaliPeriodo.put(CREDITO_KEY, credito);
        if (rataPrecedente != null) {

            BigDecimal importo = rataPrecedente.getResiduoConSegno().getImportoInValuta();

            // devo impostare l'importo della rata precedente se inferiore al
            // minimale iva
            BigDecimal importoRataPrecedente = BigDecimal.ZERO;
            if (minimaleIVA == null) {
                throw new IllegalStateException("Impostare il minimale IVA !");
            }
            if (importo.compareTo(minimaleIVA) < 0) {
                importoRataPrecedente = importo;
            }

            // sommo i pagamenti della liquidazione a credito all'importo del
            // periodo precedente
            Set<Pagamento> pagamenti = rataPrecedente.getPagamenti();
            if (pagamenti != null) {
                for (Pagamento pagamento : pagamenti) {
                    if (pagamento.isLiquidazione()) {
                        BigDecimal imporoPagamentoLiquidazione = pagamento.getImporto().getImportoInValutaAzienda();
                        if (imporoPagamentoLiquidazione.signum() < 0) {
                            importoRataPrecedente = importoRataPrecedente.add(imporoPagamentoLiquidazione);
                        }
                    }
                }
            }
            totaliPeriodo.put(RISULTATO_PRECEDENTE_KEY, importoRataPrecedente);
        } else {
            totaliPeriodo.put(RISULTATO_PRECEDENTE_KEY, BigDecimal.ZERO);
        }

        totaliPeriodo.put(RISULTATO_KEY, debito.subtract(credito));
        BigDecimal totConAcconto = debito.subtract(credito).add(totaliPeriodo.get(RISULTATO_PRECEDENTE_KEY));

        BigDecimal importoPercTrimestrale = BigDecimal.ZERO;
        if (totConAcconto.compareTo(BigDecimal.ZERO) > 0) {
            importoPercTrimestrale = totConAcconto.divide(Importo.HUNDRED).multiply(getPercTrimestraleValore());
            importoPercTrimestrale = importoPercTrimestrale.setScale(2, RoundingMode.HALF_UP);
        }
        setPercTrimestraleImporto(importoPercTrimestrale);
        totConAcconto = totConAcconto.add(importoPercTrimestrale);

        BigDecimal tot = totConAcconto;

        // per la liquidazione di dicembre deve essere aggiunto il totale del
        // documento di acconto iva
        if (totaleAccontoIvaDicembre != null) {
            totConAcconto = totConAcconto.subtract(totaleAccontoIvaDicembre);
            totaliPeriodo.put(RISULTATO_ACCONTO_DICEMBRE, totaleAccontoIvaDicembre);
        }

        totaliPeriodo.put(MAGG_INTERESSI_TRIMESTRALI_IMPORTO_KEY, getPercTrimestraleImporto());
        totaliPeriodo.put(MAGG_INTERESSI_TRIMESTRALI_VALORE_KEY, getPercTrimestraleValore());
        totaliPeriodo.put(RISULTATO_TOTALE_KEY, tot);
        totaliPeriodo.put(RISULTATO_TOTALE_CON_ACCONTO_KEY, totConAcconto);
        totaliPeriodo.put(RISULTATO_INCASSATO_KEY, totaleIncassato);

        return totaliPeriodo;
    }

    /**
     * @return the totaliRegistri
     * @uml.property name="totaliRegistri"
     */
    public Map<RegistroIva, List<TotaliCodiceIvaDTO>> getTotaliRegistri() {
        return totaliRegistri;
    }

    /**
     * @return the totaliRegistriIntra
     * @uml.property name="totaliRegistriIntra"
     */
    public Map<RegistroIva, List<TotaliCodiceIvaDTO>> getTotaliRegistriIntra() {
        return totaliRegistriIntra;
    }

    /**
     * @return the totaliSplitPayment
     */
    public Map<RegistroIva, BigDecimal> getTotaliSplitPayment() {
        return totaliSplitPayment;
    }

    /**
     * @return the ventilazioniIva
     * @uml.property name="ventilazioniIva"
     */
    public Map<RegistroIva, List<TotaliCodiceIvaDTO>> getVentilazioniIva() {
        return ventilazioniIva;
    }

    /**
     * @return the volumeAffariAziende
     */
    public TotaliCodiceIvaDTO getVolumeAffariAziende() {
        if (volumeAffariAziende == null) {
            TotaliCodiceIvaDTO tot = new TotaliCodiceIvaDTO();
            tot.setImponibile(BigDecimal.ZERO);
            tot.setImposta(BigDecimal.ZERO);
            return tot;
        }
        return volumeAffariAziende;
    }

    /**
     * @return the volumeAffariPrivati
     */
    public TotaliCodiceIvaDTO getVolumeAffariPrivati() {
        if (volumeAffariPrivati == null) {
            TotaliCodiceIvaDTO tot = new TotaliCodiceIvaDTO();
            tot.setImponibile(BigDecimal.ZERO);
            tot.setImposta(BigDecimal.ZERO);
            return tot;
        }
        return volumeAffariPrivati;
    }

    /**
     * @return the volumeAffariTotale
     */
    public TotaliCodiceIvaDTO getVolumeAffariTotale() {
        if (volumeAffariTotale == null) {
            TotaliCodiceIvaDTO tot = new TotaliCodiceIvaDTO();
            tot.setImponibile(BigDecimal.ZERO);
            tot.setImposta(BigDecimal.ZERO);
            return tot;
        }
        return volumeAffariTotale;
    }

    /**
     * Verifica quanti mesi di differenza esistono tra la data fine e la data inizio periodo; se ci sono 12 mesi allora
     * il periodo copre un anno.
     * 
     * @return true se ci sono 12 mesi di differenza tra le due date, false altrimenti
     */
    public boolean isAnnuale() {
        Calendar startDate = Calendar.getInstance();
        startDate.setTime(dataInizio);

        Calendar endDate = Calendar.getInstance();
        endDate.setTime(dataFine);

        Calendar date = (Calendar) startDate.clone();
        long monthsBetween = 0;
        while (date.before(endDate)) {
            date.add(Calendar.MONTH, 1);
            monthsBetween++;
        }

        boolean isLiquidazioneAnnuale = monthsBetween == 12;
        return isLiquidazioneAnnuale;
    }

    /**
     * @param beniStrumentaliTotale
     *            the beniStrumentaliTotale to set
     */
    public void setBeniStrumentaliTotale(TotaliCodiceIvaDTO beniStrumentaliTotale) {
        this.beniStrumentaliTotale = beniStrumentaliTotale;
    }

    /**
     * @param dataFine
     *            the dataFine to set
     */
    public void setDataFine(Date dataFine) {
        this.dataFine = dataFine;
    }

    /**
     * @param dataInizio
     *            the dataInizio to set
     */
    public void setDataInizio(Date dataInizio) {
        this.dataInizio = dataInizio;
    }

    /**
     * @param importoProRata
     *            The importoProRata to set.
     */
    public void setImportoProRata(BigDecimal importoProRata) {
        this.importoProRata = importoProRata;
    }

    /**
     * @param minimaleIVA
     *            the minimaleIVA to set
     * @uml.property name="minimaleIVA"
     */
    public void setMinimaleIVA(BigDecimal minimaleIVA) {
        this.minimaleIVA = minimaleIVA;
    }

    /**
     * @param percTrimestraleImporto
     *            the percTrimestraleImporto to set
     * @uml.property name="percTrimestraleImporto"
     */
    public void setPercTrimestraleImporto(BigDecimal percTrimestraleImporto) {
        this.percTrimestraleImporto = percTrimestraleImporto;
    }

    /**
     * @param percTrimestraleValore
     *            the percTrimestraleValore to set
     * @uml.property name="percTrimestraleValore"
     */
    public void setPercTrimestraleValore(BigDecimal percTrimestraleValore) {
        this.percTrimestraleValore = percTrimestraleValore;
    }

    /**
     * @param rataPrecedente
     *            the rataPrecedente to set
     * @uml.property name="rataPrecedente"
     */
    public void setRataPrecedente(Rata rataPrecedente) {
        this.rataPrecedente = rataPrecedente;
    }

    /**
     * @param righeVolumeAffariPrivati
     *            the righeVolumeAffariPrivati to set
     */
    public void setRigheVolumeAffariPrivati(List<TotaliCodiceIvaDTO> righeVolumeAffariPrivati) {
        this.righeVolumeAffariPrivati = righeVolumeAffariPrivati;
    }

    /**
     * @param totaleAccontoIvaDicembre
     *            the totaleAccontoIvaDicembre to set
     */
    public void setTotaleAccontoIvaDicembre(BigDecimal totaleAccontoIvaDicembre) {
        this.totaleAccontoIvaDicembre = totaleAccontoIvaDicembre;
    }

    /**
     * @param volumeAffariAziende
     *            the volumeAffariAziende to set
     */
    public void setVolumeAffariAziende(TotaliCodiceIvaDTO volumeAffariAziende) {
        this.volumeAffariAziende = volumeAffariAziende;
    }

    /**
     * @param volumeAffariPrivati
     *            the volumeAffariPrivati to set
     */
    public void setVolumeAffariPrivati(TotaliCodiceIvaDTO volumeAffariPrivati) {
        this.volumeAffariPrivati = volumeAffariPrivati;
    }

    /**
     * @param volumeAffariTotale
     *            the volumeAffariTotale to set
     */
    public void setVolumeAffariTotale(TotaliCodiceIvaDTO volumeAffariTotale) {
        this.volumeAffariTotale = volumeAffariTotale;
    }

}
