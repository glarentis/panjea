package it.eurotn.panjea.dms.manager.allegati;

import it.eurotn.panjea.anagrafica.util.EntitaDocumento;
import it.eurotn.panjea.dms.manager.allegati.attributi.AttributoAllegato;
import it.eurotn.panjea.dms.manager.allegati.attributi.AttributoAllegatoEntita;

public class AllegatoEntita extends AllegatoDMS {

    private static final long serialVersionUID = -5473966337662782773L;

    private static final String TEMPLATE = "ALLEGATOENTITA";

    /**
     * Costruttore.
     *
     * @param entitaDocumento
     *            entita
     * @param codiceAzienda
     *            codice azienda
     */
    public AllegatoEntita(final EntitaDocumento entitaDocumento, final String codiceAzienda) {
        super(TEMPLATE, new AttributoAllegato[] { new AttributoAllegatoEntita(entitaDocumento, codiceAzienda) }, null);
    }

    // @Override
    // protected String buildSearchExpression() {
    // return getFields()[0].getValueSearch();
    // }

}
