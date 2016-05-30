package it.eurotn.panjea.bi.domain.analisi;

import javax.persistence.Entity;
import javax.persistence.Table;

import it.eurotn.entity.EntityBase;

/**
 * @author fattazzo
 *
 */
@Entity
@Table(name = "bi_analisi_layout_running_summary")
public class RunningSummaryBILayout extends EntityBase {

    private static final long serialVersionUID = 2028561479602083738L;

    private int runningSummaryType;

    private String baseOnField;
    private String rangeInField;

    private String compareTo;
    private String compareToClassName;

    /**
     * Costruttore.
     */
    public RunningSummaryBILayout() {
        super();
    }

    /**
     * Costruttore.
     * 
     * @param runningSummaryType
     *            runningSummaryType
     * @param baseOnField
     *            baseOnField
     * @param rangeInField
     *            rangeInField
     * @param compareTo
     *            compareTo
     * @param compareToClassName
     *            compareToClassName
     */
    public RunningSummaryBILayout(final int runningSummaryType, final String baseOnField, final String rangeInField,
            final String compareTo, final String compareToClassName) {
        super();
        this.runningSummaryType = runningSummaryType;
        this.baseOnField = baseOnField;
        this.rangeInField = rangeInField;
        this.compareTo = compareTo;
        this.compareToClassName = compareToClassName;
    }

    /**
     * @return the baseOnField
     */
    public String getBaseOnField() {
        return baseOnField;
    }

    /**
     * @return the compareTo
     */
    public String getCompareTo() {
        return compareTo;
    }

    /**
     * @return the compareToClassName
     */
    public String getCompareToClassName() {
        return compareToClassName;
    }

    /**
     * @return the rangeInField
     */
    public String getRangeInField() {
        return rangeInField;
    }

    /**
     * @return the runningSummaryType
     */
    public int getRunningSummaryType() {
        return runningSummaryType;
    }

}
