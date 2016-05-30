package it.eurotn.panjea.dms.manager.allegati;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import com.logicaldoc.webservice.search.WSSearchOptions;

import it.eurotn.panjea.dms.domain.SearchAllegatiOption;
import it.eurotn.panjea.dms.manager.allegati.attributi.AttributoAllegato;

/**
 *
 * Utilizzate per raggruppare degli attributi per ricercare/inserire alegati. Ad esempio allegati articoli o documenti.
 *
 * @author giangi
 * @version 1.0, 05 ago 2015
 *
 */
public abstract class AllegatoDMS implements Serializable {
    private static final long serialVersionUID = 4984128904742139725L;
    protected String template;
    protected AttributoAllegato[] attributiAllegato;
    protected Date data;
    protected Date dataIniziale;
    protected Date dataFinale;

    /**
     *
     * @param template
     *            template o aspetto del dms
     * @param data
     *            del documento
     * @param attributiAllegato
     *            attributi del template
     */
    public AllegatoDMS(final String template, final AttributoAllegato[] attributiAllegato, final Date data) {
        this.template = template;
        this.attributiAllegato = attributiAllegato;
        this.data = data;
    }

    /**
     *
     * @param template
     *            template o aspetto del dms
     * @param attributiAllegato
     *            attributi del template
     * @param dataIniziale
     *            data iniziale
     * @param dataFinale
     *            data finale
     */
    public AllegatoDMS(final String template, final AttributoAllegato[] attributiAllegato, final Date dataIniziale,
            final Date dataFinale) {
        this.template = template;
        this.attributiAllegato = attributiAllegato;
        this.dataIniziale = dataIniziale;
        this.dataFinale = dataFinale;
    }

    protected String buildSearchExpression() {
        StringBuilder query = new StringBuilder();

        for (AttributoAllegato attributoAllegato : attributiAllegato) {
            if (query.length() > 0) {
                query.append(" AND ");
            }
            query.append(attributoAllegato.getNome() + ":(" + attributoAllegato.getValueSearch() + ")");
        }

        return query.toString();
    }

    /**
     *
     * @return espressione per ricercare i documenti con gli attributi preimpostati
     */
    public final String createSearchExpression() {
        return buildSearchExpression();
    }

    /**
     * @return search option per la ricerca
     */
    public WSSearchOptions createSearchOption() {
        return new SearchAllegatiOption(this);
    }

    /**
     * @return Returns the data.
     */
    public String getData() {
        if (data != null) {
            return DateFormatUtils.format(data, "yyyy-MM-dd 12:00:00 Z");
        }
        return null;
    }

    /**
     *
     * @return data in formato stringa
     */
    public String getDataFrom() {
        if (data != null || dataIniziale != null) {
            return DateFormatUtils.format(ObjectUtils.defaultIfNull(dataIniziale, data), "yyyy-MM-dd");
        }
        return null;
    }

    /**
     *
     * @return data from
     */
    public String getDataTo() {
        if (data != null || dataFinale != null) {
            return DateFormatUtils.format(ObjectUtils.defaultIfNull(dataFinale, data), "yyyy-MM-dd");
        }
        return null;
    }

    /**
     * @return Returns the fields.
     */
    public AttributoAllegato[] getFields() {
        return attributiAllegato;
    }

    /**
     *
     * @return lista nomi degli attributo
     */
    public String[] getNames() {
        String[] attributiExt = new String[getFields().length];
        for (int j = 0; j < getFields().length; j++) {
            AttributoAllegato attributoDocumento = getFields()[j];
            attributiExt[j] = attributoDocumento.getNome();
        }
        return attributiExt;
    }

    /**
     *
     * @return nomi reali degli attributi
     */
    public String[] getNamesReale() {
        String[] attributiExt = new String[getFields().length];
        for (int j = 0; j < getFields().length; j++) {
            AttributoAllegato attributoDocumento = getFields()[j];
            attributiExt[j] = attributoDocumento.getNomeReale();
        }
        return attributiExt;
    }

    /**
     * @return Returns the search template.
     */
    public String getSearchTemplate() {
        return template;
    }

    /**
     * @return Returns the template.
     */
    public String getTemplate() {
        return template;
    }

    /**
     *
     * @return array di valori degli attributi, ordinati come i fields[]
     */
    public Object[] getValues() {
        Object[] result = new Object[attributiAllegato.length];
        for (int i = 0; i < attributiAllegato.length; i++) {
            result[i] = attributiAllegato[i].getValue();
        }
        return result;
    }

}
