package it.eurotn.panjea.bi.domain.analisi.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import it.eurotn.panjea.bi.domain.analisi.AnalisiBILayout;
import it.eurotn.panjea.bi.domain.analisi.AnalisiBIResult;
import it.eurotn.panjea.bi.domain.analisi.FieldBILayout;

/**
 * Classe incaricata di recuperare i dati dal datawarehouse e riempire il {@link DataWarehouseModel}
 * .
 *
 * @author giangi
 *
 */
public class AnalisiBiFiller {

    private static final int CAPACITY_INITIAL = 50000;
    private static Logger logger = Logger.getLogger(AnalisiBiFiller.class);

    /**
     * Riempie il modello con i dati contenuti nel resultset.
     * 
     * @param conn
     *            connessione per fillare il modello
     * @param analisiBILayout
     *            modello del datawarehouse da riempire
     * @param analisiBISqlGenerator
     *            generatore dell'sql per il modello
     * @return modello con i dati dell'analisi.
     */
    public AnalisiBIResult fill(Connection conn, AnalisiBILayout analisiBILayout,
            AnalisiBISqlGenerator analisiBISqlGenerator) {
        if (conn == null) {
            throw new IllegalArgumentException("connessione nulla nell'eseguire l'analisibi");
        }
        List<Object[]> rows = new ArrayList<Object[]>(CAPACITY_INITIAL);
        Integer maxNumeroDecimaliQtaArticoli = 2;
        Map<Integer, Integer> modelIndexToResultColumn = new HashMap<Integer, Integer>();
        // recupero il numero massimo dei decimali quantità degli articoli
        try (Statement stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY);
                ResultSet resultset = stmt
                        .executeQuery("select max(numeroDecimaliQta) from maga_articoli")) {
            while (resultset.next()) {
                Integer max = (Integer) resultset.getObject(1);
                // NPE Mail alla riga del return
                if (max != null) {
                    maxNumeroDecimaliQtaArticoli = max;
                }
            }
        } catch (SQLException e) {
            logger.error("-->errore nel calcolare il numero massimo di decimali negli articoli", e);
            throw new RuntimeException(e);
        }

        try (Statement stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY)) {
            stmt.setFetchSize(Integer.MIN_VALUE);
            String query = analisiBISqlGenerator.buildSql();
            try (ResultSet resultset = stmt.executeQuery(query)) {

                int column = 0;
                for (FieldBILayout dbColumn : analisiBILayout.getFields().values()) {
                    modelIndexToResultColumn.put(dbColumn.getColonna().getModelIndex(), column);
                    column++;
                }

                while (resultset.next()) {
                    Object[] row = new Object[analisiBILayout.getFields().size()];
                    for (int i = 0; i < analisiBILayout.getFields().size(); i++) {
                        row[i] = resultset.getObject(i + 1);
                    }
                    rows.add(row);
                }
            }
        } catch (Exception e) {
            logger.error("--> errore nello sfogliare il resultset", e);
            throw new RuntimeException(e);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("--> Fill eseguito. Numero record " + rows.size());
        }
        return new AnalisiBIResult(rows, modelIndexToResultColumn, maxNumeroDecimaliQtaArticoli);
    }
}
