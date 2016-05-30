package it.eurotn.panjea.bi.domain.analisi.tabelle;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

public class Colonna implements Serializable, Comparable<Colonna> {
    private static final long serialVersionUID = -5921175550227925113L;
    protected String nome;
    protected String key;
    private Class<?> columnClass;
    private Tabella tabella;
    protected String title;
    protected int tipoCampo;// 0=dimensione 1=misura
    private boolean isSeparatorVisible;
    private int numeroDecimali;
    private String preFisso;
    private boolean decimaliDaArticoli;
    private String sezione;
    private int modelIndex;

    {
        title = "";
        preFisso = "";
        numeroDecimali = 0;
        decimaliDaArticoli = false;
    }

    /**
     *
     * Costruttore.
     *
     * @param nome
     *            nome della colonna
     * @param columnClass
     *            classe della colonna
     * @param tabella
     *            tabella proprietaria della colonna
     * @param title
     *            della tabella
     *
     */
    public Colonna(final String nome, final Class<?> columnClass, final Tabella tabella, final String title) {
        this(nome, columnClass, tabella, title, false);
    }

    /**
     *
     * Costruttore.
     *
     * @param nome
     *            nome della colonna
     * @param columnClass
     *            classe della colonna
     * @param tabella
     *            tabella proprietaria della colonna
     * @param isSeparatorVisible
     *            separatore Visibile
     * @param title
     *            della colonna
     */
    public Colonna(final String nome, final Class<?> columnClass, final Tabella tabella, final String title,
            final boolean isSeparatorVisible) {
        super();
        this.nome = nome;
        this.key = new StringBuilder(50).append(tabella.getAlias()).append("_").append(title).toString()
                .replaceAll("\\.", "_").replaceAll(" ", "_");
        this.columnClass = columnClass;
        this.tabella = tabella;
        this.isSeparatorVisible = isSeparatorVisible;
        this.title = title;
    }

    @Override
    public int compareTo(Colonna colonna) {
        return title.compareTo(colonna.getTitle());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Colonna other = (Colonna) obj;
        if (key == null) {
            if (other.key != null) {
                return false;
            }
        } else if (!key.equals(other.key)) {
            return false;
        }
        return true;
    }

    /**
     *
     * @return titolo della colonna da utilizzare come alias.
     */
    public String getAlias() {
        return StringUtils.deleteWhitespace(title).replaceAll("\\.", "_");
    }

    /**
     * @return Returns the columnClass.
     */
    public Class<?> getColumnClass() {
        return columnClass;
    }

    /**
     * @return Returns the key.
     */
    public String getKey() {
        return key;
    }

    /**
     * @return Returns the modelIndex.
     */
    public int getModelIndex() {
        return modelIndex;
    }

    /**
     * @return Returns the nome.
     */
    public String getNome() {
        return nome;
    }

    /**
     * @return Returns the numeroDecimali.
     */
    public int getNumeroDecimali() {
        return numeroDecimali;
    }

    /**
     * @return Returns the preFisso.
     */
    public String getPreFisso() {
        return preFisso;
    }

    /**
     * @return the sezione
     */
    public String getSezione() {
        return sezione;
    }

    /**
     * @return stringa sql della colonna
     */
    public String getSql() {
        StringBuilder sb = new StringBuilder(getTabella().getAlias());
        sb.append(".");
        sb.append(getNome());
        return sb.toString();
    }

    /**
     *
     * @return string sql per la groupBy
     */
    public String getSqlGroupBy() {
        StringBuilder sb = new StringBuilder();
        sb.append(getKey());
        return sb.toString();
    }

    /**
     * @return Returns the tabella.
     */
    public Tabella getTabella() {
        return tabella;
    }

    /**
     *
     * @return titolo della colonna
     */
    public String getTitle() {
        if (title.isEmpty()) {
            return getNome();
        }
        return title;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        return result;
    }

    /**
     * @return Returns the decimaliDaArticoli.
     */
    public boolean isDecimaliDaArticoli() {
        return decimaliDaArticoli;
    }

    /**
     * @return Returns the isSeparatorVisible.
     */
    public boolean isSeparatorVisible() {
        return isSeparatorVisible;
    }

    /**
     * @param decimaliDaArticoli
     *            The decimaliDaArticoli to set.
     */
    public void setDecimaliDaArticoli(boolean decimaliDaArticoli) {
        this.decimaliDaArticoli = decimaliDaArticoli;
    }

    /**
     * @param modelIndex
     *            The modelIndex to set.
     */
    public void setModelIndex(int modelIndex) {
        this.modelIndex = modelIndex;
    }

    /**
     * @param numeroDecimali
     *            The numeroDecimali to set.
     */
    public void setNumeroDecimali(int numeroDecimali) {
        this.numeroDecimali = numeroDecimali;
    }

    /**
     * @param preFisso
     *            The preFisso to set.
     */
    public void setPreFisso(String preFisso) {
        this.preFisso = preFisso;
    }

    /**
     * @param isSeparatorVisibleParam
     *            The isSeparatorVisibleParam to set.
     */
    public void setSeparatorVisible(boolean isSeparatorVisibleParam) {
        this.isSeparatorVisible = isSeparatorVisibleParam;
    }

    /**
     * @param sezione
     *            the sezione to set
     */
    public void setSezione(String sezione) {
        this.sezione = sezione;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Colonna [nome=" + nome + ", columnClass=" + columnClass + ", tabella=" + tabella + "]";
    }

}