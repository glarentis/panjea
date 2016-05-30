package it.eurotn.panjea.dms.manager.allegati.attributi;

public class AttributoAllegatoTipoDocumento extends AttributoAllegatoPanjea {
    private static final long serialVersionUID = -6614192516134975368L;

    /**
     * @param codice
     *            codice tipo documento
     * @param descrizione
     *            descrizione del tipo documento
     * @param id
     *            id tipo documento
     * @param codiceAzienda
     *            codice azienda
     */
    public AttributoAllegatoTipoDocumento(final String codice, final String descrizione, final Integer id,
            final String codiceAzienda) {
        super("ext_TIPODOC", codice + separatorField + descrizione, id, codiceAzienda);
    }

}
