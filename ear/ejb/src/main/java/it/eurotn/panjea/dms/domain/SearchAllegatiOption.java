package it.eurotn.panjea.dms.domain;

import org.apache.commons.lang.StringUtils;

import com.logicaldoc.webservice.search.WSSearchOptions;

import it.eurotn.panjea.dms.manager.allegati.AllegatoDMS;

public class SearchAllegatiOption extends WSSearchOptions {

    /**
     * Costruttore.
     */
    public SearchAllegatiOption() {
        super();
    }

    /**
     * @param allegatoDms
     *            attributi dell'allegato
     */
    public SearchAllegatiOption(final AllegatoDMS allegatoDms) {
        setExpression(allegatoDms.createSearchExpression());
        setSearchInSubPath(1);
        setName(allegatoDms.getSearchTemplate());
        setExpressionLanguage("it");
        if (!StringUtils.isBlank(allegatoDms.getDataFrom())) {
            setSourceDateFrom(allegatoDms.getDataFrom());
        }
        if (!StringUtils.isBlank(allegatoDms.getDataTo())) {
            setSourceDateTo(allegatoDms.getDataTo());
        }
        setType(99);
    }
}
