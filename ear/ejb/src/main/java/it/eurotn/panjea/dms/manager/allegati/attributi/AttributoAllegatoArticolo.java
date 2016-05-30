package it.eurotn.panjea.dms.manager.allegati.attributi;

public class AttributoAllegatoArticolo extends AttributoAllegatoPanjea {
    private static final long serialVersionUID = -7252910273444619917L;
    private String codice;
    private String descrizione;

    /**
     * @param codice
     *            codice dell'articolo
     * @param descrizione
     *            decsrizione dell'articolo
     * @param id
     *            id dell'articolo
     * @param codiceAzienda
     *            codice azienda
     */
    public AttributoAllegatoArticolo(final String codice, final String descrizione, final Integer id,
            final String codiceAzienda) {
        super("ext_CODARTICOLO", codice + separatorField + descrizione, id, codiceAzienda);
        this.codice = codice;
        this.descrizione = descrizione;
    }

    /**
     * @return Returns the codice.
     */
    public String getCodice() {
        return codice;
    }

    /**
     * @return Returns the descrizione.
     */
    public String getDescrizione() {
        return descrizione;
    }

}
