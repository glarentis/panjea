package it.eurotn.dao.exception;

import java.util.Map;

import javax.persistence.Table;

import org.apache.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.entity.SingleTableEntityPersister;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

/**
 * Restituisce l'eccezione corretta data una MySQLIntegrityConstraintViolationException.
 *
 * @author giangi
 * @version 1.0, 23/mar/2011
 *
 */
public final class ConstraintExceptionFactory {

    private static Logger logger = Logger.getLogger(ConstraintExceptionFactory.class);

    /**
     *
     * Costruttore privato.
     */
    private ConstraintExceptionFactory() {
    }

    /**
     * Crea l'eccezione {@link DuplicateKeyObjectException}.
     *
     * @param ex
     *            eccezione lanciata
     * @param map
     *            mappa contenente i classMetadata mappati da hibernate
     * @return eccezione {@link DuplicateKeyObjectException} con le colonne della chiave univoca
     */
    private static DAOException createDuplicateKeyObjectException(Throwable ex, Map<String, ClassMetadata> map) {
        String[] propertiesCostraintNames = new String[] { "Constraint generica" };

        if (ex.getCause().getMessage().split("\\[").length > 1) {
            String className = ((ConstraintViolationException) ex.getCause()).getMessage().split("\\[")[1]
                    .split("\\]")[0];
            className = className.split("#")[0];
            try {
                Table tableAnnotation = getTableAnnotation(Class.forName(className));
                String[] columnsCostraintNames = new String[0];
                if (tableAnnotation.uniqueConstraints().length > 0) {
                    columnsCostraintNames = tableAnnotation.uniqueConstraints()[0].columnNames();
                }
                propertiesCostraintNames = new String[columnsCostraintNames.length];
                SingleTableEntityPersister persister = (SingleTableEntityPersister) map.get(className);

                // imposto la column name come proprietà nel caso in cui sia un campo sul db non mappato
                // (es. TIPO_ANAGRAFICA)
                String[] propertyNames = persister.getPropertyNames();
                for (int j = 0; j < columnsCostraintNames.length; j++) {
                    String columnName = columnsCostraintNames[j];
                    propertiesCostraintNames[j] = columnName;
                    for (String property : propertyNames) {
                        if (persister.getPropertyColumnNames(property)[0] != null
                                && (persister.getPropertyColumnNames(property)[0]).equalsIgnoreCase(columnName)) {
                            propertiesCostraintNames[j] = property;
                        }
                    }
                }
            } catch (ClassNotFoundException e1) {
                if (logger.isDebugEnabled()) {
                    logger.debug("--> classe non trovata " + e1);
                }
            }
        }
        return new DuplicateKeyObjectException(propertiesCostraintNames);
    }

    /**
     * Crea l'eccezione {@link VincoloException}.
     *
     * @param ex
     *            eccezione lanciata
     * @param map
     *            mappa contenente i classMetadata mappati da hibernate
     * @return eccezione {@link VincoloException} con la classe collegata che ha generato l'errore
     */
    private static DAOException createVincoloException(ConstraintViolationException ex,
            Map<String, ClassMetadata> map) {
        // il messaggio della constraint risulta essere ad esempio:
        // could not delete: [it.eurotn.panjea.contabilita.domain.SottoConto#1203]
        // oppure se cancello una lista: could not execute update query
        String message = ex.getMessage();

        try {
            // il messaggio della MySQLIntegrityConstraintViolationException risulta essere ad esempio:
            // Cannot delete or update a parent row: a foreign key constraint fails
            // (`aesse`.`maga_sottoconto_contabilizzazione`, CONSTRAINT `FK55DF8EC511B747E3` FOREIGN KEY
            // (`sottoContoNotaAccredito_id`) REFERENCES `cont_sottoconti` (`id`))
            String foreignMessage = ex.getCause().getMessage();

            // Recupero la classe che non riesco a cancellare:
            String parentClass = message.indexOf("\\[") != -1 ? message.split("\\[")[1] : "";
            parentClass = parentClass.indexOf("#") != -1 ? parentClass.split("#")[0] : "";

            // Recupero la tabella della foreign key
            String tabella = foreignMessage.indexOf("`") != -1 ? foreignMessage.split("`")[3] : "";
            String vincoloClass = "";

            // ciclo sugli entity bean
            for (String entityClass : map.keySet()) {
                Table tableAnnotation;
                if (!entityClass.toUpperCase().contains("_AUD")) {
                    tableAnnotation = Class.forName(entityClass).getAnnotation(Table.class);
                    if (tableAnnotation != null && tableAnnotation.name().equals(tabella)) {
                        vincoloClass = entityClass;
                        break;
                    }
                }

            }
            return new VincoloException(parentClass, vincoloClass);
        } catch (Exception exVincolo) {
            return new VincoloException(ex);
        }
    }

    /**
     *
     * @param ex
     *            eccezione ricevuta
     * @param map
     *            mappa contenente i classMetadata mappati da hibernate
     * @return Eccezione Vincolo o DuplicateKey
     */
    public static DAOException getException(Throwable ex, Map<String, ClassMetadata> map) {
        MySQLIntegrityConstraintViolationException cvException = (MySQLIntegrityConstraintViolationException) ex
                .getCause().getCause();
        if ((cvException.getMessage().startsWith("Duplicate"))) {
            return createDuplicateKeyObjectException(ex, map);
        } else {
            return createVincoloException((ConstraintViolationException) ex.getCause(), map);
        }
    }

    private static Table getTableAnnotation(Class<?> clazz) {
        Table tableAnnotation = clazz.getAnnotation(Table.class);
        // trovato caso in cui non ho l'annotation sulla classe ma sulla sua classe base
        if (tableAnnotation == null) {
            // cerco l'annotation sulla classe base
            // tableAnnotation = Class.forName(className).getSuperclass().getAnnotation(Table.class);
            tableAnnotation = getTableAnnotation(clazz.getSuperclass());
        }
        return tableAnnotation;
    }

}
