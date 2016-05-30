package it.eurotn.panjea.parametriricerca.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Embeddable;
import javax.persistence.PostLoad;

@Embeddable
public class Periodo implements Serializable {

    public enum TipoPeriodo {
        NESSUNO, DATE, ULTIMI_GIORNI, MESE_CORRENTE, MESE_PRECEDENTE, ANNO_CORRENTE, ANNO_PRECEDENTE, OGGI, IERI
    }

    private static final PeriodoStrategyDateCalculator[] STRATEGIE = new PeriodoStrategyDateCalculator[] {
            new NessunoStrategy(), new DateStrategy(), new UltimiGiorniStrategy(), new MeseCorrenteStrategy(),
            new MesePrecedenteStrategy(), new AnnoCorrenteStrategy(), new AnnoPrecedenteStrategy(), new OggiStrategy(),
            new IeriStrategy() };

    private static final long serialVersionUID = 6336686508931587450L;

    private Date dataIniziale;

    private Date dataFinale;

    private Boolean dataInizialeNull;

    private TipoPeriodo tipoPeriodo;

    private Integer numeroGiorni;

    /**
     * Costruttore.
     */
    public Periodo() {
        tipoPeriodo = TipoPeriodo.DATE;
        dataIniziale = null;
        dataFinale = null;
        numeroGiorni = 0;
    }

    /**
     * @return Returns the dataFinale.
     */
    public Date getDataFinale() {
        return dataFinale;
    }

    /**
     * @return Returns the dataIniziale.
     */
    public Date getDataIniziale() {
        return dataIniziale;
    }

    /**
     * @return Returns the numeroGiorni.
     */
    public Integer getNumeroGiorni() {
        return numeroGiorni;
    }

    /**
     * @return Returns the strategy.
     */
    public PeriodoStrategyDateCalculator getStrategy() {
        return STRATEGIE[tipoPeriodo.ordinal()];
    }

    /**
     * @return Returns the tipoPeriodo.
     */
    public TipoPeriodo getTipoPeriodo() {
        return tipoPeriodo;
    }

    /**
     * Inizializza il bean dopo il caricamento.
     */
    @PostLoad
    private void init() {
        Periodo result = STRATEGIE[tipoPeriodo.ordinal()].calcola(this);
        dataIniziale = result.getDataIniziale();
        dataFinale = result.getDataFinale();
    }

    /**
     * @return Returns the dataInizialeNull.
     */
    public boolean isDataInizialeNull() {
        return dataInizialeNull;
    }

    /**
     *
     * @return true se non è impostato nessun periodo
     */
    public boolean isPeriodoNull() {
        return dataIniziale == null && dataFinale == null;
    }

    /**
     * @param dataFinale
     *            The dataFinale to set.
     */
    public void setDataFinale(Date dataFinale) {
        this.dataFinale = dataFinale;
    }

    /**
     * @param dataIniziale
     *            The dataIniziale to set.
     */
    public void setDataIniziale(Date dataIniziale) {
        this.dataIniziale = dataIniziale;
    }

    /**
     * @param dataInizialeNull
     *            The dataInizialeNull to set.
     */
    public void setDataInizialeNull(boolean dataInizialeNull) {
        this.dataInizialeNull = dataInizialeNull;
    }

    /**
     * @param numeroGiorni
     *            The numeroGiorni to set.
     */
    public void setNumeroGiorni(Integer numeroGiorni) {
        this.numeroGiorni = (numeroGiorni != null) ? numeroGiorni : 0;
        init();
    }

    /**
     * @param tipoPeriodo
     *            The tipoPeriodo to set.
     */
    public void setTipoPeriodo(TipoPeriodo tipoPeriodo) {
        this.tipoPeriodo = tipoPeriodo;
        init();
    }
}
