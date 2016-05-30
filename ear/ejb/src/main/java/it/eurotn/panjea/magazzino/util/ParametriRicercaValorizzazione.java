package it.eurotn.panjea.magazzino.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.Categoria;

/**
 * Rappresenta i parametri per la ricerca della valorizzazione.
 *
 * @author fattazzo
 */
public class ParametriRicercaValorizzazione implements Serializable {

    /**
     *
     * @author giangi
     * @version 1.0, 10/nov/2010
     */
    public enum EModalitaValorizzazione {
        ULTIMO_COSTO_AZIENDA, ULTIMO_COSTO_DEPOSITO, COSTO_MEDIO_PONDERATO, NESSUNO, COSTO_STANDARD
    }

    private static final long serialVersionUID = 621761801413047138L;

    /**
     * @uml.property name="modalitaValorizzazione"
     * @uml.associationEnd
     */
    private EModalitaValorizzazione modalitaValorizzazione;

    /**
     * @uml.property name="depositi"
     */
    private List<Deposito> depositi;

    /**
     * @uml.property name="tuttiDepositi"
     */
    private boolean tuttiDepositi;

    private List<ArticoloLite> articoliLite;

    /**
     * Indica se mantenere nei risultati gli articoli con la giacenza a zero. Di default è false
     */
    private boolean consideraGiacenzaZero;

    /*
     * Indica se mantenere nei risultati gli articoliche non sono stati movimentati.
     */
    private boolean consideraMovimentatiZero;

    /**
     * Indica se mantenere nei risultati gli articoli che sono disabilitati.
     */
    private boolean consideraArticoliDisabilitati;

    /**
     * @uml.property name="categorie"
     */
    private List<Categoria> categorie;

    /**
     * @uml.property name="tutteCategorie"
     */
    private boolean tutteCategorie;

    /**
     * @uml.property name="data"
     */
    private Date data;

    /**
     * @uml.property name="effettuaRicerca"
     */
    private boolean effettuaRicerca;

    /**
     * Definisce se calcolando la valorizzazione devo anche calcolare la giacenza, true di default.
     *
     * @uml.property name="calcolaGiacenza"
     */
    private boolean calcolaGiacenza;

    /**
     * Costruttore.
     */
    public ParametriRicercaValorizzazione() {
        super();
        this.depositi = new ArrayList<Deposito>();
        this.categorie = new ArrayList<Categoria>();
        this.tutteCategorie = true;
        this.tuttiDepositi = true;
        this.effettuaRicerca = false;
        this.modalitaValorizzazione = EModalitaValorizzazione.NESSUNO;
        this.calcolaGiacenza = true;
        this.consideraGiacenzaZero = false;
        this.articoliLite = new ArrayList<ArticoloLite>();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        this.data = calendar.getTime();

        this.consideraArticoliDisabilitati = Boolean.FALSE;
        this.consideraMovimentatiZero = false;

    }

    /**
     * @return Returns the articoliLite.
     */
    public List<ArticoloLite> getArticoliLite() {
        return articoliLite;
    }

    /**
     *
     * @return lista con gli id degli articoli scelti come parametro
     */
    public List<Integer> getArticoliLiteId() {
        List<Integer> result = new ArrayList<>();
        if (getArticoliLite() != null) {
            for (ArticoloLite articoloLite : getArticoliLite()) {
                result.add(articoloLite.getId());
            }
        }
        return result;
    }

    /**
     * @return the categorie
     * @uml.property name="categorie"
     */
    public List<Categoria> getCategorie() {
        return categorie;
    }

    /**
     * @return string sql per filtrare le categorie
     */
    public String getCategorieSql() {
        String categorieString = new String();
        if (categorie != null && !categorie.isEmpty()) {
            for (Categoria categoria : getCategorie()) {
                categorieString = categorieString + categoria.getId() + ",";
            }
            categorieString = categorieString.substring(0, categorieString.length() - 1);
        }
        return categorieString;
    }

    /**
     * @return the data
     * @uml.property name="data"
     */
    public Date getData() {
        return data;
    }

    /**
     * @return the depositi
     * @uml.property name="depositi"
     */
    public List<Deposito> getDepositi() {
        return depositi;
    }

    /**
     * @return the depositi
     * @uml.property name="depositi"
     */
    public List<DepositoLite> getDepositiLite() {
        List<DepositoLite> depositiLite = new ArrayList<DepositoLite>();
        for (Deposito deposito : depositi) {
            depositiLite.add(deposito.creaLite());
        }
        return depositiLite;
    }

    /**
     *
     * @return id dei depositi selezionati in formato stringa separati da virgola.
     */
    public String getIdDepositiSelezionati() {

        String idDepositiSelezionati = "";
        for (Deposito deposito : depositi) {
            idDepositiSelezionati = idDepositiSelezionati + "," + deposito.getId() + ",";
        }
        return idDepositiSelezionati;
    }

    /**
     * @return Returns the modalitaVisualizzazione.
     * @uml.property name="modalitaValorizzazione"
     */
    public EModalitaValorizzazione getModalitaValorizzazione() {
        return modalitaValorizzazione;
    }

    /**
     * @return the calcolaGiacenza
     * @uml.property name="calcolaGiacenza"
     */
    public boolean isCalcolaGiacenza() {
        return calcolaGiacenza;
    }

    /**
     * @return Returns the consideraArticoliDisabilitati.
     */
    public boolean isConsideraArticoliDisabilitati() {
        return consideraArticoliDisabilitati;
    }

    /**
     * @return Returns the consideraGiacenzaZero.
     */
    public boolean isConsideraGiacenzaZero() {
        return consideraGiacenzaZero;
    }

    /**
     * @return Returns the consideraMovimentatiZero.
     */
    public boolean isConsideraMovimentatiZero() {
        return consideraMovimentatiZero;
    }

    /**
     * @return the effettuaRicerca
     * @uml.property name="effettuaRicerca"
     */
    public boolean isEffettuaRicerca() {
        return effettuaRicerca;
    }

    /**
     * @return the tutteCategorie
     * @uml.property name="tutteCategorie"
     */
    public boolean isTutteCategorie() {
        return tutteCategorie;
    }

    /**
     * @return the tuttiDepositi
     * @uml.property name="tuttiDepositi"
     */
    public boolean isTuttiDepositi() {
        return tuttiDepositi;
    }

    /**
     * @param articoliLite
     *            The articoliLite to set.
     */
    public void setArticoliLite(List<ArticoloLite> articoliLite) {
        this.articoliLite = articoliLite;
    }

    /**
     * @param calcolaGiacenza
     *            the calcolaGiacenza to set
     * @uml.property name="calcolaGiacenza"
     */
    public void setCalcolaGiacenza(boolean calcolaGiacenza) {
        this.calcolaGiacenza = calcolaGiacenza;
    }

    /**
     * @param categorie
     *            the categorie to set
     * @uml.property name="categorie"
     */
    public void setCategorie(List<Categoria> categorie) {
        this.categorie = categorie;
    }

    /**
     * @param consideraArticoliDisabilitati
     *            The consideraArticoliDisabilitati to set.
     */
    public void setConsideraArticoliDisabilitati(boolean consideraArticoliDisabilitati) {
        this.consideraArticoliDisabilitati = consideraArticoliDisabilitati;
    }

    /**
     * @param consideraGiacenzaZero
     *            The consideraGiacenzaZero to set.
     */
    public void setConsideraGiacenzaZero(boolean consideraGiacenzaZero) {
        this.consideraGiacenzaZero = consideraGiacenzaZero;
    }

    /**
     * @param consideraMovimentatiZero
     *            The consideraMovimentatiZero to set.
     */
    public void setConsideraMovimentatiZero(boolean consideraMovimentatiZero) {
        this.consideraMovimentatiZero = consideraMovimentatiZero;
    }

    /**
     * @param data
     *            the data to set
     * @uml.property name="data"
     */
    public void setData(Date data) {
        this.data = data;
    }

    /**
     * @param depositi
     *            the depositi to set
     * @uml.property name="depositi"
     */
    public void setDepositi(List<Deposito> depositi) {
        this.depositi = depositi;
    }

    /**
     * @param effettuaRicerca
     *            the effettuaRicerca to set
     * @uml.property name="effettuaRicerca"
     */
    public void setEffettuaRicerca(boolean effettuaRicerca) {
        this.effettuaRicerca = effettuaRicerca;
    }

    /**
     * @param modalitaVisualizzazione
     *            The modalitaVisualizzazione to set.
     * @uml.property name="modalitaValorizzazione"
     */
    public void setModalitaValorizzazione(EModalitaValorizzazione modalitaVisualizzazione) {
        this.modalitaValorizzazione = modalitaVisualizzazione;
    }

    /**
     * @param tutteCategorie
     *            the tutteCategorie to set
     * @uml.property name="tutteCategorie"
     */
    public void setTutteCategorie(boolean tutteCategorie) {
        this.tutteCategorie = tutteCategorie;
    }

    /**
     * @param tuttiDepositi
     *            the tuttiDepositi to set
     * @uml.property name="tuttiDepositi"
     */
    public void setTuttiDepositi(boolean tuttiDepositi) {
        this.tuttiDepositi = tuttiDepositi;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ParametriRicercaValorizzazione [modalitaValorizzazione=" + modalitaValorizzazione + ", depositi="
                + depositi + ", tuttiDepositi=" + tuttiDepositi + ", categorie=" + categorie + ", tutteCategorie="
                + tutteCategorie + ", data=" + data + ", effettuaRicerca=" + effettuaRicerca
                + ", numArticoliDaFiltrare=" + articoliLite.size() + "]";
    }

}
