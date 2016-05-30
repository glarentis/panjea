package it.eurotn.panjea.contabilita.manager.spesometro.builder.aggregato;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.ejb.QueryImpl;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.anagrafica.util.AziendaAnagraficaDTO;
import it.eurotn.panjea.contabilita.manager.spesometro.builder.SpesometroRecordBuilder;
import it.eurotn.panjea.contabilita.util.DocumentoSpesometro;
import it.eurotn.panjea.contabilita.util.DocumentoSpesometroAggregato;
import it.eurotn.panjea.contabilita.util.ParametriCreazioneComPolivalente;
import it.eurotn.panjea.contabilita.util.ParametriCreazioneComPolivalente.TipologiaInvio;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

/**
 * @author fattazzo
 *
 */
public class SpesometroAggregatoBuilder extends SpesometroRecordBuilder {

    private static final Logger LOGGER = Logger.getLogger(SpesometroAggregatoBuilder.class);

    private List<DocumentoSpesometro> documentiSpesometro = null;
    private Map<QuadroRecordC, List<DocumentoSpesometroAggregato>> documentiAggregati = null;

    private SpesometroAggregatoProcessor spesometroProcessor = new SpesometroAggregatoProcessor();
    private SpesometroAggregatoManager spesometroManager = new SpesometroAggregatoManager();

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
    public SpesometroAggregatoBuilder(final PanjeaDAO panjeaDAO, final AziendaAnagraficaDTO azienda,
            final ParametriCreazioneComPolivalente params) {
        super(panjeaDAO, azienda, params);

        documentiSpesometro = caricaOperazioni();
        documentiAggregati = spesometroManager.getDocumentiAggregati(documentiSpesometro);
    }

    /**
     * Carica tutte le operazioni.
     *
     * @return operazioni caricate
     */
    @SuppressWarnings("unchecked")
    protected List<DocumentoSpesometro> caricaOperazioni() {
        List<DocumentoSpesometro> results = new ArrayList<DocumentoSpesometro>();

        String sql = spesometroManager.getSQLDocumenti();

        QueryImpl query = (QueryImpl) panjeaDAO.getEntityManager().createNativeQuery(sql);
        SQLQuery sqlQuery = (SQLQuery) query.getHibernateQuery();
        sqlQuery = spesometroManager.setupQuery(sqlQuery, params);

        try {
            results = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            LOGGER.error("-->errore nel caricare le fatture attive dello spesometro", e);
            throw new RuntimeException(e);
        }

        return results;
    }

    @Override
    public List<DocumentoSpesometro> getDocumenti() {
        return documentiSpesometro;
    }

    @Override
    public String getRecordC() {

        // se annullamento il tipo record non serve
        if (params.getTipologiaInvio() == TipologiaInvio.ANNULLAMENTO) {
            return null;
        }

        String codiceFiscaleAzienda = azienda.getAzienda().getCodiceFiscale();

        Integer progressivo = 1;
        StringBuilder sb = new StringBuilder();

        List<DocumentoSpesometroAggregato> documentiFA = documentiAggregati.get(QuadroRecordC.FA);
        sb.append(spesometroProcessor.getQuadroFA(documentiFA, codiceFiscaleAzienda));
        progressivo = spesometroManager.getNumeroRecordQuadroC(documentiAggregati, QuadroRecordC.FA);

        List<DocumentoSpesometroAggregato> documentiBL = documentiAggregati.get(QuadroRecordC.BL);
        sb.append(spesometroProcessor.getQuadroBL(documentiBL, progressivo, codiceFiscaleAzienda));
        progressivo = progressivo + spesometroManager.getNumeroRecordQuadroC(documentiAggregati, QuadroRecordC.BL);

        return sb.toString();
    }

    @Override
    public String getRecordD() {
        // Per i dati aggregati non serve
        return null;
    }

    @Override
    public String getRecordE() {

        // se annullamento il tipo record non serve
        if (params.getTipologiaInvio() == TipologiaInvio.ANNULLAMENTO) {
            return null;
        }

        StringBuilder sb = new StringBuilder();

        sb.append("E");
        sb.append(StringUtils.rightPad(azienda.getAzienda().getCodiceFiscale(), 16, " "));
        sb.append(StringUtils.leftPad("1", 8, "0"));
        sb.append(StringUtils.repeat(" ", 3));
        sb.append(StringUtils.repeat(" ", 25));
        sb.append(StringUtils.repeat(" ", 20));
        sb.append(StringUtils.rightPad("01398480226", 16, " "));

        sb.append("TA001001");
        sb.append(StringUtils.leftPad(Integer.toString(documentiAggregati.get(QuadroRecordC.FA).size()), 16, " "));
        List<DocumentoSpesometroAggregato> docBL = documentiAggregati.get(QuadroRecordC.BL);
        int numDocBLAltro = spesometroManager.getNumeroAnagraficheDocumentiBLAltro(docBL);
        if (numDocBLAltro > 0) {
            sb.append("TA003002");
            sb.append(StringUtils.leftPad(Integer.toString(numDocBLAltro), 16, " "));
        } else {
            sb.append(StringUtils.repeat(" ", 24));
        }
        int numDocBLServizi = spesometroManager.getNumeroAnagraficheDocumentiBLServizi(docBL);
        if (numDocBLServizi > 0) {
            sb.append("TA003003");
            sb.append(StringUtils.leftPad(Integer.toString(numDocBLServizi), 16, " "));
        } else {
            sb.append(StringUtils.repeat(" ", 24));
        }
        sb.append(StringUtils.repeat(" ", 1889 - sb.length()));

        sb.append(StringUtils.repeat(" ", 8));
        sb.append("A");
        sb.append("\r\n");

        return sb.toString();
    }

    @Override
    public String getRecordZ() {
        StringBuilder sb = new StringBuilder();

        sb.append("Z");
        sb.append(StringUtils.repeat(" ", 14));
        sb.append(StringUtils.leftPad("1", 9, "0"));

        if (params.getTipologiaInvio() != TipologiaInvio.ANNULLAMENTO) {

            int numeroRecord = spesometroManager.getNumeroRecordQuadroC(documentiAggregati, QuadroRecordC.FA)
                    + spesometroManager.getNumeroRecordQuadroC(documentiAggregati, QuadroRecordC.BL);
            sb.append(StringUtils.leftPad(Integer.toString(numeroRecord), 9, "0")); // C
            sb.append(StringUtils.leftPad("0", 9, "0"));
            sb.append(StringUtils.leftPad("1", 9, "0"));
        } else {
            sb.append(StringUtils.leftPad("0", 9, "0"));
            sb.append(StringUtils.leftPad("0", 9, "0"));
            sb.append(StringUtils.leftPad("0", 9, "0"));
        }

        sb.append(StringUtils.repeat(" ", 1846));

        sb.append("A");
        sb.append("\r\n");

        return sb.toString();
    }

    @Override
    protected String quadroBLPresente() {
        return !documentiAggregati.get(QuadroRecordC.BL).isEmpty() ? "1" : "0";
    }

    @Override
    protected String quadroFAPresente() {
        return !documentiAggregati.get(QuadroRecordC.FA).isEmpty() ? "1" : "0";
    }

}
