package it.eurotn.panjea.dms.manager.allegati;

import java.io.Serializable;

import it.eurotn.panjea.dms.manager.allegati.attributi.AttributoAllegato;
import it.eurotn.panjea.dms.manager.allegati.attributi.AttributoAllegatoArticolo;

public class AllegatoArticolo extends AllegatoDMS implements Serializable {
    private static final long serialVersionUID = 8537859181111817709L;
    private static final String TEMPLATE_NAME = "ARTICOLO";

    /**
     * @param codice
     *            codice dell'articolo
     * @param descrizione
     *            descrizione dell'articolo
     * @param id
     *            id dell'articolo
     * @param codiceAzienda
     *            codice azienda
     */
    public AllegatoArticolo(final String codice, final String descrizione, final int id, final String codiceAzienda) {
        super(TEMPLATE_NAME,
                new AttributoAllegato[] { new AttributoAllegatoArticolo(codice, descrizione, id, codiceAzienda) },
                null);
    }

    // @Override
    // protected String buildSearchExpression() {
    // return getFields()[0].getValueSearch();
    // }

}
