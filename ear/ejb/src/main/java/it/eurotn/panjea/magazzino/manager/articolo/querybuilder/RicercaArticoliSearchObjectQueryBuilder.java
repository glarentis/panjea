/**
 *
 */
package it.eurotn.panjea.magazzino.manager.articolo.querybuilder;

import it.eurotn.panjea.magazzino.util.ParametriRicercaArticolo;

/**
 * @author fattazzo
 *
 */
public class RicercaArticoliSearchObjectQueryBuilder {

    /**
     * Crea l'sql per la from.
     *
     * @param parametri
     *            parametri di ricerca
     * @return sql
     */
    private String createFrom(ParametriRicercaArticolo parametri) {
        StringBuilder sb = new StringBuilder();
        sb.append("from maga_articoli art ");

        if (parametri.getIdEntita() != null || parametri.isRicercaCodiceArticoloEntitaPresente()
                || parametri.isAssortimentoArticoli()) {
            sb.append("left join maga_codici_articolo_entita codEnt on codEnt.articolo_id=art.id ");
            if (parametri.getIdEntita() != null) {
                sb.append("and codEnt.entita_id=");
                sb.append(parametri.getIdEntita());
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    /**
     * Crea l'sql per la order by.
     *
     * @param parametri
     *            parametri di ricerca
     * @return sql
     */
    private String createOrderBy(ParametriRicercaArticolo parametri) {
        StringBuilder sb = new StringBuilder();
        if (parametri.getBarCode() != null) {
            sb.append(" order by art.barCode ");
        } else if (parametri.getCodiceInterno() != null) {
            sb.append(" order by art.codiceInterno ");
        } else if (parametri.getCodiceEntita() != null) {
            sb.append(" order by codEnt.codice ");
        } else if (parametri.getDescrizione() != null) {
            sb.append(" order by art.descrizioneLinguaAziendale ");
        } else {
            sb.append(" order by art.codice ");
        }

        return sb.toString();
    }

    /**
     * Crea l'sql per la ricerca degli articolo.
     *
     * @param parametri
     *            parametri di ricerca
     * @param codiceAzienda
     *            codice azienda
     * @return query creata
     */
    public String createQuery(ParametriRicercaArticolo parametri, String codiceAzienda) {
        parametri = ParametriRicercaArticoloCodicePopulator.populate(parametri);

        StringBuilder sb = new StringBuilder();

        sb.append(createSelect(parametri));

        sb.append(createFrom(parametri));

        sb.append(createWhere(parametri, codiceAzienda));

        sb.append(createOrderBy(parametri));

        return sb.toString();
    }

    /**
     * Crea l'sql per la select.
     *
     * @param parametri
     *            parametri di ricerca
     * @return sql
     */
    private String createSelect(ParametriRicercaArticolo parametri) {
        StringBuilder sb = new StringBuilder();
        sb.append("select art.id as id, ");
        sb.append("art.version as version, ");
        if (parametri.getIdEntita() != null || parametri.isRicercaCodiceArticoloEntitaPresente()
                || parametri.isAssortimentoArticoli()) {
            sb.append("codEnt.codice as codiceEntita, ");
        }
        sb.append("art.codice as codice, ");
        sb.append("art.codiceInterno as codiceInterno, ");
        sb.append("art.descrizioneLinguaAziendale as descrizione, ");
        sb.append("art.barCode as barCode ,");
        sb.append("art.provenienzaPrezzoArticolo as provenienzaPrezzoArticolo, ");
        sb.append("art.numeroDecimaliQta as numeroDecimaliQta ");

        return sb.toString();
    }

    /**
     * Crea l'sql per la from.
     *
     * @param parametri
     *            parametri di ricerca
     * @param codiceAzienda
     *            codice azienda
     * @return sql
     */
    private String createWhere(ParametriRicercaArticolo parametri, String codiceAzienda) {
        StringBuilder sb = new StringBuilder();
        sb.append("where art.codiceAzienda = '");
        sb.append(codiceAzienda);
        sb.append("' and art.tipo = 'A' ");
        sb.append(" and art.abilitato = 1 ");
        if (parametri.getCodice() != null) {
            sb.append(" and art.codice like '");
            sb.append(parametri.getCodice());
            sb.append("' ");
        }
        if (parametri.getDescrizione() != null) {
            sb.append(" and art.descrizioneLinguaAziendale like '");
            sb.append(parametri.getDescrizione());
            sb.append("' ");
        }
        if (parametri.getBarCode() != null) {
            sb.append(" and art.barCode like '");
            sb.append(parametri.getBarCode());
            sb.append("' ");
        }
        if (parametri.getCodiceInterno() != null) {
            sb.append(" and art.codiceInterno like '");
            sb.append(parametri.getCodiceInterno());
            sb.append("' ");
        }
        if (parametri.getCodiceEntita() != null) {
            sb.append(" and codEnt.codice like '");
            sb.append(parametri.getCodiceEntita());
            sb.append("' ");
        }
        if ((parametri.isRicercaCodiceArticoloEntitaPresente() || parametri.isAssortimentoArticoli())
                && parametri.getIdEntita() != null) {
            sb.append(" and codEnt.entita_id=");
            sb.append(parametri.getIdEntita());
        }
        if (parametri.isEscludiDistinte()) {
            sb.append(" and art.distinta=false ");
        }
        if (parametri.isSoloDistinte()) {
            sb.append(" and art.distinta=true ");
        }

        return sb.toString();
    }
}
