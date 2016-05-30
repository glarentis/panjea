package it.eurotn.panjea.contabilita.manager.spesometro.builder;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.ejb.QueryImpl;
import org.hibernate.transform.Transformers;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.anagrafica.util.AziendaAnagraficaDTO;
import it.eurotn.panjea.contabilita.manager.spesometro.builder.aggregato.SpesometroAggregatoBuilder;
import it.eurotn.panjea.contabilita.util.DocumentoSpesometro;
import it.eurotn.panjea.contabilita.util.ParametriCreazioneComPolivalente;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

/**
 * @author fattazzo
 *
 */
public class SpesometroAggretatoTmpTableBuilder extends SpesometroAggregatoBuilder {

    private static final Logger LOGGER = Logger.getLogger(SpesometroAggretatoTmpTableBuilder.class);

    /**
     * Costruttore.
     *
     * @param panjeaDAO
     *            panjeaDAO
     * @param azienda
     *            azienda di riferimento
     * @param params
     *            parametri per la crazione dello spesometro
     */
    public SpesometroAggretatoTmpTableBuilder(final PanjeaDAO panjeaDAO, final AziendaAnagraficaDTO azienda,
            final ParametriCreazioneComPolivalente params) {
        super(panjeaDAO, azienda, params);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected List<DocumentoSpesometro> caricaOperazioni() {
        List<DocumentoSpesometro> results = new ArrayList<DocumentoSpesometro>();

        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        sb.append("  attivo as attivo, ");
        sb.append("  idEntita as idEntita, ");
        sb.append("  riepilogativo as riepilogativo, ");
        sb.append("  partitaIvaEntita as partitaIvaEntita, ");
        sb.append("  codiceFiscaleEntita as codiceFiscaleEntita, ");
        sb.append("  documentiAggregati as documentiAggregati, ");
        sb.append("  imponibile as imponibile, ");
        sb.append("  imposta as imposta, ");
        sb.append("  notaCredito as notaCredito ");
        sb.append("from SPESOMETRO_TEMP ");
        sb.append("order by idEntita,notaCredito,attivo ");

        QueryImpl query = (QueryImpl) panjeaDAO.getEntityManager().createNativeQuery(sb.toString());
        SQLQuery sqlQuery = (SQLQuery) query.getHibernateQuery();
        sqlQuery.setResultTransformer(Transformers.aliasToBean(DocumentoSpesometro.class));
        sqlQuery.addScalar("attivo");
        sqlQuery.addScalar("idEntita");
        sqlQuery.addScalar("riepilogativo");
        sqlQuery.addScalar("partitaIvaEntita");
        sqlQuery.addScalar("codiceFiscaleEntita");
        sqlQuery.addScalar("documentiAggregati", Hibernate.INTEGER);
        sqlQuery.addScalar("imponibile");
        sqlQuery.addScalar("imposta");
        sqlQuery.addScalar("notaCredito");

        try {
            results = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            LOGGER.error("-->errore nel caricare le fatture attive dello spesometro", e);
            throw new RuntimeException(e);
        }

        return results;
    }

}
