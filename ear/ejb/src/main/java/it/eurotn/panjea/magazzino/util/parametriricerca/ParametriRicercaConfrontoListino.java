package it.eurotn.panjea.magazzino.util.parametriricerca;

import java.io.Serializable;
import java.util.List;

import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.magazzino.util.CategoriaLite;

/**
 * @author fattazzo
 *
 */
public class ParametriRicercaConfrontoListino implements Serializable {

    private static final long serialVersionUID = -1131468107470158714L;

    private List<CategoriaLite> categorie;
    private boolean tutteCategorie;

    private TipoConfronto confrontoBase;

    private List<TipoConfronto> confronti;

    private boolean effettuaRicerca;

    private EntitaLite entita;

    {
        confrontoBase = new TipoConfronto();
        effettuaRicerca = Boolean.FALSE;
        tutteCategorie = true;
    }

    /**
     * Costruttore.
     */
    public ParametriRicercaConfrontoListino() {
        super();
    }

    /**
     * @return the categorie
     */
    public List<CategoriaLite> getCategorie() {
        return categorie;
    }

    /**
     * @return string sql per filtrare le categorie
     */
    public String getCategorieSql() {
        String categorieString = new String();
        if (categorie != null) {
            for (CategoriaLite categoria : categorie) {
                categorieString = categorieString + categoria.getId() + ",";
            }
            categorieString = categorieString.substring(0, categorieString.length() - 1);
        }
        return categorieString;
    }

    /**
     * @return the confronti
     */
    public List<TipoConfronto> getConfronti() {
        return confronti;
    }

    /**
     * @return the confrontoBase
     */
    public TipoConfronto getConfrontoBase() {
        return confrontoBase;
    }

    /**
     * @return the entita
     */
    public EntitaLite getEntita() {
        return entita;
    }

    /**
     * @return the effettuaRicerca
     */
    public boolean isEffettuaRicerca() {
        return effettuaRicerca;
    }

    /**
     * @return the tutteCategorie
     */
    public boolean isTutteCategorie() {
        return tutteCategorie;
    }

    /**
     * @param categorie
     *            the categorie to set
     */
    public void setCategorie(List<CategoriaLite> categorie) {
        this.categorie = categorie;
    }

    /**
     * @param confronti
     *            the confronti to set
     */
    public void setConfronti(List<TipoConfronto> confronti) {
        this.confronti = confronti;
    }

    /**
     * @param confrontoBase
     *            the confrontoBase to set
     */
    public void setConfrontoBase(TipoConfronto confrontoBase) {
        this.confrontoBase = confrontoBase;
    }

    /**
     * @param effettuaRicerca
     *            the effettuaRicerca to set
     */
    public void setEffettuaRicerca(boolean effettuaRicerca) {
        this.effettuaRicerca = effettuaRicerca;
    }

    /**
     * @param entita
     *            the entita to set
     */
    public void setEntita(EntitaLite entita) {
        this.entita = entita;
    }

    /**
     * @param tutteCategorie
     *            the tutteCategorie to set
     */
    public void setTutteCategorie(boolean tutteCategorie) {
        this.tutteCategorie = tutteCategorie;
    }

}
