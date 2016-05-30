package it.eurotn.panjea.magazzino.util.parametriricerca;

import java.util.ArrayList;
import java.util.List;

import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.magazzino.util.ArticoloRicerca;
import it.eurotn.panjea.magazzino.util.CategoriaLite;
import it.eurotn.panjea.magazzino.util.ParametriRicercaValorizzazione;
import it.eurotn.panjea.magazzino.util.ParametriRicercaValorizzazione.EModalitaValorizzazione;
import it.eurotn.panjea.parametriricerca.domain.AbstractParametriRicerca;

//@Entity
//@Table(name = "para_ricerca_valorizzazione_distinte")
public class ParametriValorizzazioneDistinte extends AbstractParametriRicerca {
    private static final long serialVersionUID = -2473604550037490040L;

    private List<ArticoloRicerca> articoli;
    private List<CategoriaLite> categorie;

    private EModalitaValorizzazione modalitaValorizzazione;

    private Deposito depositoUltimoCosto;
    private boolean effettuaRicerca;

    {
        articoli = new ArrayList<>();
        categorie = new ArrayList<>();
    }

    /**
     *
     * @return parametri per la valorizzazione del magazzino. Valorizzo tutto il magazzino perchè non so che articoli
     *         utilizza la mia distinta.
     */
    public ParametriRicercaValorizzazione creaParametriValorizzazione() {
        ParametriRicercaValorizzazione parametriRicercaValorizzazione = new ParametriRicercaValorizzazione();
        parametriRicercaValorizzazione.setCalcolaGiacenza(false);
        parametriRicercaValorizzazione.setModalitaValorizzazione(modalitaValorizzazione);
        if (modalitaValorizzazione == EModalitaValorizzazione.ULTIMO_COSTO_DEPOSITO) {
            List<Deposito> depositi = new ArrayList<>();
            depositi.add(depositoUltimoCosto);
            parametriRicercaValorizzazione.setDepositi(depositi);
        }
        return parametriRicercaValorizzazione;
    }

    /**
     * @return Returns the articoli.
     */
    public List<ArticoloRicerca> getArticoli() {
        return articoli;
    }

    /**
     * @return Returns the categorie.
     */
    public List<CategoriaLite> getCategorie() {
        return categorie;
    }

    /**
     * @return Returns the depositoUltimoCosto.
     */
    public Deposito getDepositoUltimoCosto() {
        return depositoUltimoCosto;
    }

    /**
     * @return Returns the modalitaValorizzazione.
     */
    public EModalitaValorizzazione getModalitaValorizzazione() {
        return modalitaValorizzazione;
    }

    /**
     *
     * @return effetturaRicerca
     */
    @Override
    public boolean isEffettuaRicerca() {
        return effettuaRicerca;
    }

    /**
     * @param articoli
     *            The articoli to set.
     */
    public void setArticoli(List<ArticoloRicerca> articoli) {
        this.articoli = articoli;
    }

    /**
     * @param categorie
     *            The categorie to set.
     */
    public void setCategorie(List<CategoriaLite> categorie) {
        this.categorie = categorie;
    }

    /**
     * @param depositoUltimoCosto
     *            The depositoUltimoCosto to set.
     */
    public void setDepositoUltimoCosto(Deposito depositoUltimoCosto) {
        this.depositoUltimoCosto = depositoUltimoCosto;
    }

    /**
     * @param effetturaRicerca
     *            The effetturaRicerca to set.
     */
    @Override
    public void setEffettuaRicerca(boolean effetturaRicerca) {
        this.effettuaRicerca = effetturaRicerca;
    }

    /**
     * @param modalitaValorizzazione
     *            The modalitaValorizzazione to set.
     */
    public void setModalitaValorizzazione(EModalitaValorizzazione modalitaValorizzazione) {
        this.modalitaValorizzazione = modalitaValorizzazione;
    }
}