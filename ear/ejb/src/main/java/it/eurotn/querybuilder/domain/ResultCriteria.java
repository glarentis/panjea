package it.eurotn.querybuilder.domain;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class ResultCriteria implements Serializable {
    private static final long serialVersionUID = 7710125347810275402L;
    private List<?> risultati;
    private Class<?> type;
    private String[] colonne;

    /**
     * @param risultati
     * @param type
     * @param colonne
     */
    public ResultCriteria(final List<?> risultati, final Class<?> type, final String[] colonne) {
        super();
        this.risultati = risultati;
        this.type = type;
        this.colonne = colonne;
    }

    /**
     * @return Returns the colonne.
     */
    public String[] getColonne() {
        return colonne;
    }

    /**
     * @return Returns the risultati.
     */
    public List<?> getRisultati() {
        return risultati;
    }

    /**
     * @return Returns the type.
     */
    public Class<?> getType() {
        return type;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ResultCriteria [risultatiSize=" + risultati.size() + ", type=" + type + ", colonne="
                + Arrays.toString(colonne) + "]";
    }
}
