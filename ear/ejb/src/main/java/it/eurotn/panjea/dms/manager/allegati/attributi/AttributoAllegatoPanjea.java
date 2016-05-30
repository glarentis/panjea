package it.eurotn.panjea.dms.manager.allegati.attributi;

public abstract class AttributoAllegatoPanjea extends AttributoAllegatoStringa {

    private static final long serialVersionUID = 7242529831611352874L;

    private Integer id;

    private String codiceAzienda;

    /**
     * @param nome
     *            nome dell'attributo
     * @param value
     *            valore dell'attributo
     * @param id
     *            id
     * @param codiceAzienda
     *            codice azienda
     */
    public AttributoAllegatoPanjea(final String nome, final String value, final Integer id,
            final String codiceAzienda) {
        super(nome, value + separatorField + String.valueOf(id) + separatorField + codiceAzienda);

        this.id = id;
        this.codiceAzienda = codiceAzienda;
    }

    /**
     * @return the codiceAzienda
     */
    public String getCodiceAzienda() {
        return codiceAzienda;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    @Override
    public String getValueSearch() {
        return "*" + separatorField + String.valueOf(id) + separatorField + getCodiceAzienda();
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

}
