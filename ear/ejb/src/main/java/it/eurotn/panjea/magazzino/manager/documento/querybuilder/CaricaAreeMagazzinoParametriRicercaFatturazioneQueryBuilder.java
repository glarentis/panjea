package it.eurotn.panjea.magazzino.manager.documento.querybuilder;

import javax.persistence.Query;

import org.apache.log4j.Logger;

import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaFatturazione;

public final class CaricaAreeMagazzinoParametriRicercaFatturazioneQueryBuilder {
    private static Logger logger = Logger.getLogger(CaricaAreeMagazzinoParametriRicercaFatturazioneQueryBuilder.class);

    /**
     * Classe di utilità . Costruttore privato
     */
    private CaricaAreeMagazzinoParametriRicercaFatturazioneQueryBuilder() {
    }

    /**
     * Applica i parametri in base ai filtri di ricerca.
     *
     * @param query
     *            Query costruita dall'entityManager
     * @param parametriRicercaFatturazione
     *            parametri da applicare
     * @param codiceAzienda
     *            azienda loggata
     * @return query con i parametri impostati
     */
    public static Query applyParameter(Query query, ParametriRicercaFatturazione parametriRicercaFatturazione,
            String codiceAzienda) {
        if (parametriRicercaFatturazione.getAreeMagazzino().size() > 0) {
            query.setParameter("paramAreeMagazzino", parametriRicercaFatturazione.getAreeMagazzino());
        } else {
            query.setParameter("paramDataIniziale", parametriRicercaFatturazione.getPeriodo().getDataIniziale());
            query.setParameter("paramDataFinale", parametriRicercaFatturazione.getPeriodo().getDataFinale());
            query.setParameter("paramTipiDocumento", parametriRicercaFatturazione.getTipiDocumentoDaFatturare());
            query.setParameter("paramCodiceAzienda", codiceAzienda);
            query.setParameter("paramCodiceValuta", parametriRicercaFatturazione.getCodiceValuta());

            if (parametriRicercaFatturazione.getEntitaLite() != null
                    && parametriRicercaFatturazione.getEntitaLite().getId() != null) {
                query.setParameter("paramEntita", parametriRicercaFatturazione.getEntitaLite());

                if (parametriRicercaFatturazione.getSedeEntita() != null
                        && parametriRicercaFatturazione.getSedeEntita().getId() != null) {
                    query.setParameter("paramSede", parametriRicercaFatturazione.getSedeEntita());
                }
            }

            if (parametriRicercaFatturazione.getCodicePagamento() != null
                    && parametriRicercaFatturazione.getCodicePagamento().getId() != null) {
                query.setParameter("paraCodicePagamento", parametriRicercaFatturazione.getCodicePagamento().getId());
            }

            if (parametriRicercaFatturazione.getZonaGeografica() != null
                    && parametriRicercaFatturazione.getZonaGeografica().getId() != null) {
                query.setParameter("paramIdZonaGeografica", parametriRicercaFatturazione.getZonaGeografica().getId());
            }

            if (parametriRicercaFatturazione.getAgente() != null
                    && parametriRicercaFatturazione.getAgente().getId() != null) {
                query.setParameter("paramIdAgente", parametriRicercaFatturazione.getAgente().getId());
            }

            if (parametriRicercaFatturazione.getCategoriaEntita() != null
                    && parametriRicercaFatturazione.getCategoriaEntita().getId() != null) {
                query.setParameter("paramIdCategoriaEntita", parametriRicercaFatturazione.getCategoriaEntita().getId());
            }
        }
        return query;
    }

    /**
     *
     * @param parametriRicercaFatturazione
     *            paramAreeMagazzino
     * @return stringa con la query da eseguire
     */
    public static String buildSql(ParametriRicercaFatturazione parametriRicercaFatturazione) {
        logger.debug("--> Enter buildSql");
        StringBuffer hqlQuery = new StringBuffer();
        hqlQuery.append("select ral ");
        hqlQuery.append("from RigaArticoloLite ral inner join fetch ral.areaMagazzino a ");
        hqlQuery.append(",AreaRate ar ");

        if (parametriRicercaFatturazione.getAreeMagazzino().size() > 0) {
            hqlQuery.append(" where ral.areaMagazzino in (:paramAreeMagazzino)");
        } else {
            hqlQuery.append(
                    " inner join fetch a.documento doc inner join fetch ral.articolo art left join fetch ral.agente ag left join doc.sedeEntita.categoriaEntita cat ");
            hqlQuery.append(
                    " where (abs(ral.qta) > (select case when sum(rc.qta) is null then 0 else abs(sum(rc.qta)) end from RigaMagazzino rc where rc.rigaMagazzinoCollegata.id = ral.id) or ral.qta = 0) and ");
            hqlQuery.append(
                    " (a.documento.dataDocumento >= :paramDataIniziale and a.documento.dataDocumento <= :paramDataFinale) and ");
            hqlQuery.append(" a.documento.tipoDocumento in (:paramTipiDocumento) and ");
            hqlQuery.append(" a.documento.codiceAzienda = :paramCodiceAzienda and ");
            hqlQuery.append(" a.tipoAreaMagazzino.tipoDocumento.tipoEntita = 0 and ");
            hqlQuery.append(" (a.statoAreaMagazzino = 0 or a.statoAreaMagazzino = 1 or a.statoAreaMagazzino = 2) ");
            hqlQuery.append(" and a.documento.totale.codiceValuta = :paramCodiceValuta ");

            if (parametriRicercaFatturazione.getEntitaLite() != null
                    && parametriRicercaFatturazione.getEntitaLite().getId() != null) {
                hqlQuery.append(" and (a.documento.entita = :paramEntita) ");

                if (parametriRicercaFatturazione.getSedeEntita() != null
                        && parametriRicercaFatturazione.getSedeEntita().getId() != null) {
                    hqlQuery.append(" and doc.sedeEntita = :paramSede ");
                }
            }

            if (parametriRicercaFatturazione.getCodicePagamento() != null
                    && parametriRicercaFatturazione.getCodicePagamento().getId() != null) {
                hqlQuery.append(" and ar.codicePagamento.id = :paraCodicePagamento ");
            }

            if (parametriRicercaFatturazione.getZonaGeografica() != null
                    && parametriRicercaFatturazione.getZonaGeografica().getId() != null) {
                hqlQuery.append(" and a.idZonaGeografica = :paramIdZonaGeografica ");
            }

            if (parametriRicercaFatturazione.getAgente() != null
                    && parametriRicercaFatturazione.getAgente().getId() != null) {
                hqlQuery.append(" and doc.sedeEntita.agente.id = :paramIdAgente ");
            }

            if (parametriRicercaFatturazione.getCategoriaEntita() != null
                    && parametriRicercaFatturazione.getCategoriaEntita().getId() != null) {
                hqlQuery.append(" and cat.id = :paramIdCategoriaEntita ");
            }
        }
        hqlQuery.append(" and a.documento = ar.documento and ar.codicePagamento.id is not null ");
        hqlQuery.append("order by a.documento.tipoDocumento,a.dataRegistrazione, a.documento.codice.codiceOrder ");
        logger.debug("--> Query da eseguire " + hqlQuery.toString());
        logger.debug("--> Exit buildSql");
        return hqlQuery.toString();
    }
}
