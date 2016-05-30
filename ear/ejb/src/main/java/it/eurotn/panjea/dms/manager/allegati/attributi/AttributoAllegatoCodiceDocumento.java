package it.eurotn.panjea.dms.manager.allegati.attributi;

public class AttributoAllegatoCodiceDocumento extends AttributoAllegatoStringa {
    private static final long serialVersionUID = 7847261523035053368L;

    /**
     *
     * @param value
     *            valore dell'attributo
     */
    public AttributoAllegatoCodiceDocumento(final String value) {
        super("ext_CODDOC", value);
    }

    @Override
    public String getValueSearch() {
        return value;
    }

}
