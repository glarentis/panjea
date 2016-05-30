package it.eurotn.querybuilder.domain;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.eurotn.querybuilder.domain.filter.OperatoreQuery;

public class ProprietaEntity implements Serializable {

    private static final long serialVersionUID = 1805926452349197696L;

    protected String nome;
    protected String nomeIndexed;
    protected boolean collection;
    protected Class<?> type;
    protected OperatoreQuery operatore;
    protected Object filtro;
    protected boolean entity;
    protected List<ProprietaEntity> proprieta;
    protected boolean inSelect;

    protected ProprietaEntity parentProprietaEntity;
    protected String path;

    protected String pathBean;

    protected String aliasBean;

    private String propertyFullPath = null;

    public ProprietaEntity() {
    }

    /**
     *
     * @param nome
     *            .
     * @param type
     *            .
     * @param isEntity
     *            .
     * @param isCollection
     *            .
     * @param embedded
     *            .
     * @param parentType
     *            .
     */
    public ProprietaEntity(final String nome, final Class<?> type, final boolean entity, final boolean collection,
            final ProprietaEntity parentProprietaEntity) {
        this.nome = nome;
        this.type = type;
        this.entity = entity;
        this.collection = collection;
        this.nomeIndexed = nome;
        this.parentProprietaEntity = parentProprietaEntity;
        if (collection) {
            this.nomeIndexed += "[0]";
        }
        this.inSelect = false;
        this.path = parentProprietaEntity.getPath();
        this.pathBean = parentProprietaEntity.getPathBean();
        this.aliasBean = parentProprietaEntity.getAliasBean();
    }

    /**
     * @return Returns the aliasBean.
     */
    public String getAliasBean() {
        return aliasBean;
    }

    /**
     * @return Returns the filtro.
     */
    public Object getFiltro() {
        return filtro;
    }

    public Object getFrom() {
        return "";
    }

    /**
     * @return Returns the nome.
     */
    public String getNome() {
        return nome;
    }

    /**
     * @return Returns the nomeIndexed.
     */
    public String getNomeIndexed() {
        if (parentProprietaEntity == null || parentProprietaEntity.getNomeIndexed() == null) {
            return nomeIndexed;
        }
        return parentProprietaEntity.getNomeIndexed() + "." + nomeIndexed;
    }

    /**
     * @return Returns the operatore.
     */
    public OperatoreQuery getOperatore() {
        return operatore;
    }

    /**
     * @return Returns the parentProprietaEntity.
     */
    public ProprietaEntity getParentProprietaEntity() {
        return parentProprietaEntity;
    }

    /**
     * @return Returns the path.
     */
    public String getPath() {
        return path;
    }

    /**
     * @return Returns the pathBean.
     */
    public String getPathBean() {
        return pathBean;
    }

    /**
     * @return the propertyFullPath
     */
    public String getPropertyFullPath() {
        if (propertyFullPath == null) {
            propertyFullPath = getParentProprietaEntity().getPath().isEmpty() ? "" : getPath().replace("$", ".") + ".";
            propertyFullPath += getNome();
        }

        return propertyFullPath;
    }

    /**
     * @return Returns the proprieta.
     */
    public List<ProprietaEntity> getProprieta() {
        return proprieta;
    }

    public Object getSelect() {
        if (parentProprietaEntity != null && parentProprietaEntity.isEmbedded()) {
            String pathSeparator = "$";
            return aliasBean + "." + parentProprietaEntity.getNome() + "." + nome + " as " + path + pathSeparator
                    + parentProprietaEntity.getNome() + pathSeparator + nome;
        } else {
            String pathSeparator = "";
            if (!StringUtils.isEmpty(path)) {
                pathSeparator = "$";
            }
            return aliasBean + "." + nome + " as " + path + pathSeparator + nome;
        }
    }

    /**
     * @return Returns the type.
     */
    public Class<?> getType() {
        return type;
    }

    public String getWhere() {
        if (operatore == null || filtro == null) {
            return null;
        }

        return operatore.getFiltro().getSql(this, filtro);

    }

    /**
     * @return Returns the isCollection.
     */
    public boolean isCollection() {
        return collection;
    }

    /**
     * @return Returns the isEmbedded.
     */
    public boolean isEmbedded() {
        return false;
    }

    /**
     * @return Returns the isEntity.
     */
    public boolean isEntity() {
        return entity;
    }

    /**
     * @return Returns the inSelect.
     */
    public boolean isInSelect() {
        return inSelect;
    }

    /**
     * @return Returns the isQuerable.
     */
    public boolean isQuerable() {
        return entity;
    }

    /**
     * @param filtro
     *            The filtro to set.
     */
    public void setFiltro(Object filtro) {
        this.filtro = filtro;
    }

    /**
     * @param inSelect
     *            The inSelect to set.
     */
    public void setInSelect(boolean inSelect) {
        this.inSelect = inSelect;
    }

    /**
     * @param nome
     *            The nome to set.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @param operatore
     *            The operatore to set.
     */
    public void setOperatore(OperatoreQuery operatore) {
        this.operatore = operatore;
        if (operatore == null) {
            this.filtro = null;
        }
    }

    /**
     * @param proprieta
     *            The proprieta to set.
     */
    public void setProprieta(List<ProprietaEntity> proprieta) {
        this.proprieta = proprieta;
    }

    /**
     * @param isQuerable
     *            The isQuerable to set.
     */
    public void setQuerable(boolean isQuerable) {
        this.entity = isQuerable;
    }

    /**
     * @param type
     *            The type to set.
     */
    public void setType(Class<?> type) {
        this.type = type;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ProprietaEntity [nome=" + nome + ", path=" + path + ", pathBean=" + pathBean + ", aliasBean="
                + aliasBean + "]";
    }

}
