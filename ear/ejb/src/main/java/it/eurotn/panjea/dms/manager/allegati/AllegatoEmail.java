package it.eurotn.panjea.dms.manager.allegati;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import it.eurotn.panjea.anagrafica.util.EntitaDocumento;
import it.eurotn.panjea.dms.manager.allegati.attributi.AttributoAllegato;
import it.eurotn.panjea.dms.manager.allegati.attributi.AttributoAllegatoEntita;
import it.eurotn.panjea.dms.manager.allegati.attributi.AttributoAllegatoPanjea;

public class AllegatoEmail extends AllegatoDMS {

    private static final long serialVersionUID = 4186428399046142631L;

    private static final String TEMPLATE = "EMAIL";

    private String testo;

    /**
     * Costruttore.
     *
     * @param entitaDocumento
     *            entita
     * @param codiceAzienda
     *            codice azienda
     */
    public AllegatoEmail(final EntitaDocumento entitaDocumento, final String codiceAzienda) {
        this(entitaDocumento, codiceAzienda, null, null, null);
    }

    /**
     * Costruttore.
     *
     * @param entitaDocumento
     *            entita
     * @param codiceAzienda
     *            codice azienda
     * @param dataIniziale
     *            data iniziale
     * @param dataFinale
     *            data finale
     * @param testo
     *            testo della mail
     */
    public AllegatoEmail(final EntitaDocumento entitaDocumento, final String codiceAzienda, final Date dataIniziale,
            final Date dataFinale, final String testo) {
        super(TEMPLATE, new AttributoAllegato[] { new AttributoAllegatoEntita(entitaDocumento, codiceAzienda) },
                dataIniziale, dataFinale);
        this.testo = testo;
    }

    @Override
    protected String buildSearchExpression() {

        StringBuilder query = new StringBuilder();

        // entità se presente
        if (((AttributoAllegatoPanjea) getFields()[0]).getId() != null) {
            query.append(getFields()[0].getNome() + ":(" + getFields()[0].getValueSearch() + ")");
        }

        // testo ( include content e title)
        if (!StringUtils.isBlank(testo)) {
            if (query.length() > 0) {
                query.append(" AND ");
            }
            query.append("(content:(" + testo + ")");
            query.append(" or ");
            query.append("title:(" + testo + "))");
        }

        // se non ho parametri impostati ricerco tutto
        if (query.length() == 0) {
            query.append(" * ");
        }

        return query.toString();
    }

    @Override
    public String[] getNamesReale() {
        List<String> result = new ArrayList<>();
        for (int j = 0; j < getFields().length; j++) {
            AttributoAllegato attributoDocumento = getFields()[j];
            if (attributoDocumento.getValue() != null) {
                result.add(attributoDocumento.getNomeReale());
            }
        }
        return result.toArray(new String[result.size()]);
    }

    @Override
    public Object[] getValues() {
        List<Object> result = new ArrayList<>();
        for (int i = 0; i < attributiAllegato.length; i++) {
            AttributoAllegato attributo = attributiAllegato[i];
            if (attributo.getValue() != null) {
                result.add(attributo.getValue());
            }
        }
        return result.toArray(new Object[result.size()]);
    }

    /**
     * @param data
     *            the data to set
     */
    public void setData(Date data) {
        this.data = data;
    }

}
