package it.eurotn.panjea.magazzino.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import it.eurotn.locking.IDefProperty;
import it.eurotn.panjea.magazzino.domain.Categoria;

/**
 * Contiene alcuni dati della categoria e descrizione nella lingua richiesta (impostata nel costruttore).
 *
 * @author fattazzo
 */
public class CategoriaLite implements Serializable, IDefProperty {

    private static final long serialVersionUID = -4325295980057408345L;

    /**
     * @uml.property name="id"
     */
    private Integer id;

    /**
     * @uml.property name="version"
     */
    private Integer version;

    /**
     * @uml.property name="codice"
     */
    private String codice;
    /**
     * @uml.property name="descrizione"
     */
    private String descrizione;

    /**
     * @uml.property name="categorieFiglie"
     */
    private List<CategoriaLite> categorieFiglie;
    /**
     * @uml.property name="padre"
     * @uml.associationEnd
     */
    private Categoria padre;
    /**
     * @uml.property name="root"
     */
    private boolean root;
    /**
     * @uml.property name="linguaUtente"
     */
    private String linguaUtente;
    private int hashCode = Integer.MIN_VALUE;

    /**
     * Costruttore.
     */
    public CategoriaLite() {
        super();
    }

    /**
     * Crea una categoria Lite con la lingua di sistema.
     *
     * @param categoria
     *            categoria di origine per la categoria lite
     */
    public CategoriaLite(final Categoria categoria) {
        this(categoria, Locale.getDefault().getLanguage());
    }

    /**
     *
     * @param categoria
     *            Categoria inizializzabile
     * @param lingua
     *            lingua della gui. Setta la descrizione prendendo la descrizione in lingua.
     */
    public CategoriaLite(final Categoria categoria, final String lingua) {
        this.id = categoria.getId();
        this.codice = categoria.getCodice();
        this.version = categoria.getVersion();
        this.linguaUtente = lingua;

        if (categoria.getInformazioniLingua() != null && categoria.getInformazioniLingua().containsKey(linguaUtente)) {
            this.descrizione = categoria.getInformazioniLingua().get(linguaUtente).getDescrizione();
        } else {
            this.descrizione = categoria.getDescrizione();
        }

        this.padre = categoria.getPadre();
        if (categoria.getPadre() == null) {
            this.root = true;
        } else {
            this.root = false;
        }

        this.categorieFiglie = new ArrayList<CategoriaLite>();
    }

    /**
     *
     * @param id
     *            id della categoria
     * @param codice
     *            codice
     * @param descrizione
     *            descrizione
     */
    public CategoriaLite(final Integer id, final String codice, final String descrizione) {
        super();
        this.id = id;
        this.codice = codice;
        this.descrizione = descrizione;
    }

    /**
     * Aggiunge una categoria figlia alla caategoria. la categoria figlia ha già impostati i propri figli
     *
     * @param categoriaLite
     *            categoria da aggiungere
     */
    public void addCategoriaFiglia(CategoriaLite categoriaLite) {
        if (this.categorieFiglie == null) {
            this.categorieFiglie = new ArrayList<CategoriaLite>();
        }
        this.categorieFiglie.add(categoriaLite);
    }

    /**
     *
     * @return Categoria con i dati della categoriaLite
     */
    public Categoria createCategoria() {
        Categoria categoria = new Categoria();
        categoria.setId(id);
        categoria.setVersion(version);
        categoria.setCodice(codice);
        categoria.setDescrizione(descrizione);
        return categoria;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (this.getCodice() == null) {
            return false;
        }
        if (!(obj instanceof CategoriaLite)) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        return this.getCodice().equals(((CategoriaLite) obj).getCodice());
    }

    /**
     * @return lista delle categorie figlie
     * @uml.property name="categorieFiglie"
     */
    public List<CategoriaLite> getCategorieFiglie() {
        return categorieFiglie;
    }

    /**
     * @return codice della categoria
     * @uml.property name="codice"
     */
    public String getCodice() {
        return codice;
    }

    /**
     * @return Descrizione della categoria nella lingua impostata sul costruttore
     * @uml.property name="descrizione"
     */
    public String getDescrizione() {
        return descrizione;
    }

    @Override
    public String getDomainClassName() {
        return Categoria.class.getName();
    }

    /**
     * @return id categoria
     * @uml.property name="id"
     */
    @Override
    public Integer getId() {
        return id;
    }

    /**
     * @return lingua utente
     * @uml.property name="linguaUtente"
     */
    public String getLinguaUtente() {
        return linguaUtente;
    }

    /**
     * @return categoria Padre. Null se è una categoria root
     * @uml.property name="padre"
     */
    public Categoria getPadre() {
        return padre;
    }

    /**
     * @return Returns the version.
     * @uml.property name="version"
     */
    @Override
    public Integer getVersion() {
        return version;
    }

    @Override
    public int hashCode() {
        if (Integer.MIN_VALUE == this.hashCode) {
            if (null == this.getId()) {
                return super.hashCode();
            } else {
                String hashStr = this.getClass().getName() + ":" + this.getId().hashCode();
                this.hashCode = hashStr.hashCode();
            }
        }
        return this.hashCode;
    }

    @Override
    public boolean isNew() {
        return getId() == null;
    }

    /**
     * @return TRUE se non ho padri, FALSE se è una figlia.
     * @uml.property name="root"
     */
    public boolean isRoot() {
        return root;
    }

    /**
     * Carica le categorie figlie.
     *
     * @param categorieFiglie
     *            lista delle categorie figlie con i filgi caricati
     * @uml.property name="categorieFiglie"
     */
    public void setCategorieFiglie(List<CategoriaLite> categorieFiglie) {
        this.categorieFiglie = categorieFiglie;
    }

    /**
     * @param codice
     *            the codice to set
     * @uml.property name="codice"
     */
    public void setCodice(String codice) {
        this.codice = codice;
    }

    /**
     * @param descrizione
     *            the descrizione to set
     * @uml.property name="descrizione"
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * @param id
     *            the id to set
     * @uml.property name="id"
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @param padre
     *            The padre to set.
     */
    public void setPadre(Categoria padre) {
        this.padre = padre;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "CategoriaLite [codice=" + codice + "]";
    }
}
