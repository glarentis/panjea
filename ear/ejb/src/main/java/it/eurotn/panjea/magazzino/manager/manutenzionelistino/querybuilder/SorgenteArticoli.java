package it.eurotn.panjea.magazzino.manager.manutenzionelistino.querybuilder;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;

import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaManutenzioneListino;
import it.eurotn.util.PanjeaEJBUtil;

/**
 *
 * @author giangi
 * @version 1.0, 11/nov/2010
 *
 */
public class SorgenteArticoli {

    private List<AbstractFiltroSorgente> filtri = new ArrayList<AbstractFiltroSorgente>();

    /**
     * Aggiunge un filtro alla sorgente.
     *
     * @param filter
     *            filtro da aggiungere
     */
    public void addFilter(AbstractFiltroSorgente filter) {
        filtri.add(filter);
    }

    /**
     *
     * @return sql che definisce la sorgente degli articoli.
     */
    protected String getFrom() {
        return " from maga_articoli articoli ";
    }

    private String getSelect(ParametriRicercaManutenzioneListino parametri) {
        StringBuilder sb = new StringBuilder();
        sb.append("select distinct  articoli.id,");
        sb.append(PanjeaEJBUtil.addQuote(parametri.getUserManutenzione()));
        sb.append(",0,");
        sb.append(PanjeaEJBUtil.addQuote(parametri.getCodiceAzienda()));
        sb.append(",");
        sb.append(PanjeaEJBUtil.addQuote(parametri.getDescrizione().replaceAll("'", "''")));
        sb.append(",");
        sb.append(parametri.getNumeroInserimento());
        sb.append(",");
        sb.append(ObjectUtils.defaultIfNull(parametri.getNumeroDecimali(), 0));
        sb.append(",");
        sb.append(PanjeaEJBUtil.addQuote(parametri.getUserManutenzione()));
        sb.append(",");
        sb.append("0 ");

        if (parametri.getVersioneListino() != null) {
            sb.append(",sc.quantita,versioni.listino_id ");
        } else {
            sb.append(",999999999,null ");
        }
        return sb.toString();
    }

    /**
     *
     * @param parametri
     *            parametri di ricerca
     * @return sql per la selezione delle nuove righeListino.
     */
    public String getSql(ParametriRicercaManutenzioneListino parametri) {
        StringBuilder sb = new StringBuilder();

        StringBuilder sbSelect = new StringBuilder(getSelect(parametri));
        StringBuilder sbJoin = new StringBuilder();
        StringBuilder sbWhere = new StringBuilder(" WHERE articoli.abilitato=1 ");

        for (AbstractFiltroSorgente filtro : filtri) {
            sbJoin.append(filtro.getJoin());
            if (!filtro.getWhere().isEmpty()) {
                sbWhere.append(" AND ");
                sbWhere.append(filtro.getWhere());
            }
        }

        // select
        sb.append(sbSelect);

        // from
        sb.append(getFrom());

        // join
        if (!"".equals(sbJoin.toString())) {
            sb.append(sbJoin);
        }

        // where
        if (!"".equals(sbWhere.toString())) {
            sb.append(sbWhere);
        }

        return sb.toString();
    }
}
