package it.eurotn.panjea.dms.manager.allegati.attributi;

import java.io.Serializable;

public abstract class AttributoAllegato implements Serializable {
    private static final long serialVersionUID = 3946295442785046404L;

    public static final String separatorField = "|";

    protected String nome;
    protected String nomeReale;

    /**
     * @param nome
     *            nome dell'attributo
     */
    public AttributoAllegato(final String nome) {
        super();
        this.nome = nome;
        this.nomeReale = nome.replaceFirst("ext_", "");
    }

    /**
     *
     * @return nome attributo
     */
    public String getNome() {
        return nome;
    }

    /**
     *
     * @return nome attributo senza il prefisso ext_
     */
    public String getNomeReale() {
        return nomeReale;
    }

    /**
     *
     * @return valore dell'attributo da associare alla classe
     */
    public abstract Object getValue();

    /**
     *
     * @return valore da utilizzare nelle ricerhe
     */
    public abstract String getValueSearch();
}
