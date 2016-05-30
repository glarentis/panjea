package it.eurotn.panjea.magazzino.util.parametriricerca;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Transient;

import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.magazzino.util.ArticoloRicerca;
import it.eurotn.panjea.magazzino.util.CategoriaLite;
import it.eurotn.panjea.parametriricerca.domain.AbstractParametriRicerca;
import it.eurotn.panjea.parametriricerca.domain.AnnoCorrenteStrategy;
import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.parametriricerca.domain.PeriodoStrategyDateCalculator;

public class ParametriCalcoloIndiciRotazioneGiacenza extends AbstractParametriRicerca {
    private static final long serialVersionUID = -4180368547307629205L;
    @Transient
    private List<ArticoloRicerca> articoli;
    @Transient
    private List<Deposito> depositi;
    @Transient
    private List<CategoriaLite> categorie;
    @Transient
    private List<Integer> articoliId = null;
    private Periodo periodo;

    /**
     * Costruttore.
     */
    public ParametriCalcoloIndiciRotazioneGiacenza() {
        PeriodoStrategyDateCalculator strategy = new AnnoCorrenteStrategy();
        periodo = strategy.calcola(new Periodo());
    }

    /**
     * @return Returns the articoli.
     */
    public List<ArticoloRicerca> getArticoli() {
        if (articoli == null) {
            return new ArrayList<ArticoloRicerca>();
        }
        return articoli;
    }

    /**
     *
     * @return lista di id articoli selezionati.
     */
    public List<Integer> getArticoliIds() {
        if (articoliId == null) {
            articoliId = new ArrayList<>();
            for (ArticoloRicerca articoloRicerca : articoli) {
                articoliId.add(articoloRicerca.getId());
            }
        }
        return articoliId;
    }

    /**
     * @return Returns the categorie.
     */
    public List<CategoriaLite> getCategorie() {
        return categorie;
    }

    /**
     * @return Returns the depositi.
     */
    public List<Deposito> getDepositi() {
        return depositi;
    }

    /**
     * @return Returns the periodo.
     */
    public Periodo getPeriodo() {
        return periodo;
    }

    /**
     * @param articoli
     *            The articoli to set.
     */
    public void setArticoli(List<ArticoloRicerca> articoli) {
        this.articoli = articoli;
        articoliId = null;
    }

    /**
     * @param categorie
     *            The categorie to set.
     */
    public void setCategorie(List<CategoriaLite> categorie) {
        this.categorie = categorie;
    }

    /**
     * @param depositi
     *            The depositi to set.
     */
    public void setDepositi(List<Deposito> depositi) {
        this.depositi = depositi;
    }

    /**
     * @param periodo
     *            The periodo to set.
     */
    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }
}
