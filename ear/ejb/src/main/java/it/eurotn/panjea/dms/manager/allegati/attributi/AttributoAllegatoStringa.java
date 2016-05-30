package it.eurotn.panjea.dms.manager.allegati.attributi;

public abstract class AttributoAllegatoStringa extends AttributoAllegato {
    private static final long serialVersionUID = -4658378987549637960L;
    protected String value;

    /**
     * @param nome
     *            nome dell'attributo
     * @param value
     *            valore dell'attributo
     */
    public AttributoAllegatoStringa(final String nome, final String value) {
        super(nome);
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

}
