package it.eurotn.panjea.bi.domain.analisi;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cascade;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.bi.domain.analisi.tabelle.Colonna;
import net.sf.jasperreports.engine.type.CalculationEnum;

@Entity
@Table(name = "bi_analisi_layout_fields")
public class FieldBILayout extends EntityBase {
    private static final long serialVersionUID = 5745470592074952155L;

    private static final CalculationEnum[] FUNZIONI_AGGREGAZIONE = new CalculationEnum[] { CalculationEnum.SUM,
            CalculationEnum.HIGHEST, CalculationEnum.LOWEST, CalculationEnum.AVERAGE, CalculationEnum.VARIANCE,
            CalculationEnum.STANDARD_DEVIATION, CalculationEnum.COUNT };

    private String title;

    private int width;

    private int modelIndex;

    private boolean duplicated;

    private int subtotalType;
    private int subtotalTypeForRow;
    private int subtotalTypeForColumn;

    private String subtotal;
    private String subtotalForRow;
    private String subtotalForColumn;
    private boolean separateSubtotalSettings;

    private int summaryType;
    private int grandTotalSummaryType;

    private boolean ascending;

    private boolean hideOriginal;

    @Transient
    private Colonna colonna;

    private String keyModel;

    private int areaType;
    private int areaIndex;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    @JoinColumn(name = "fieldBi_id")
    private List<RunningSummaryBILayout> runningSummary;

    /**
     * Costruttore.
     */
    public FieldBILayout() {
        super();
    }

    /**
     *
     * @param keyModel
     *            alias della colonna. Utilizzata come chiave. Deve essere un alias univoco
     * @param title
     *            caption della colonna
     * @param width
     *            larghezza
     * @param areaType
     *            indice se il filed è una colonna=0;row=1;misura=3;filtro=4
     * @param areaIndex
     *            posizione del field nell'area
     * @param modelIndex
     *            model index
     * @param duplicated
     *            duplicated
     * @param subtotalType
     *            subtotalType
     * @param subtotalTypeForRow
     *            subtotalTypeForRow
     * @param subtotalTypeForColumn
     *            subtotalTypeForColumn
     * @param subtotal
     *            subtotal
     * @param subtotalForRow
     *            subtotalForRow
     * @param subtotalForColumn
     *            subtotalForColumn
     * @param separateSubtotalSettings
     *            separateSubtotalSettings
     * @param summaryType
     *            summaryType
     * @param grandTotalSummaryType
     *            grandTotalSummaryType
     * @param ascending
     *            ascending
     * @param hideOriginal
     *            hideOriginal
     */
    public FieldBILayout(final String keyModel, final String title, final int width, final int areaType,
            final int areaIndex, final int modelIndex, final boolean duplicated, final int subtotalType,
            final int subtotalTypeForRow, final int subtotalTypeForColumn, final int[] subtotal,
            final int[] subtotalForRow, final int[] subtotalForColumn, final boolean separateSubtotalSettings,
            final int summaryType, final int grandTotalSummaryType, final boolean ascending,
            final boolean hideOriginal) {
        super();
        this.keyModel = keyModel;
        this.areaIndex = areaIndex;
        this.modelIndex = modelIndex;
        this.duplicated = duplicated;

        this.subtotalType = subtotalType;
        this.subtotalTypeForRow = subtotalTypeForRow;
        this.subtotalTypeForColumn = subtotalTypeForColumn;

        this.subtotal = arrayToString(subtotal);
        this.subtotalForRow = arrayToString(subtotalForRow);
        this.subtotalForColumn = arrayToString(subtotalForColumn);
        this.separateSubtotalSettings = separateSubtotalSettings;

        this.summaryType = summaryType;
        this.grandTotalSummaryType = grandTotalSummaryType;

        this.ascending = ascending;
        this.hideOriginal = hideOriginal;

        initColonna();
        this.title = title;
        if (title.isEmpty()) {
            this.title = keyModel;
        }
        this.width = width;
        if (width == 0) {
            this.width = 100;
        }
        this.areaType = areaType;
    }

    private String arrayToString(int[] paramArray) {
        if (paramArray == null || paramArray.length == 0) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (int i : paramArray) {
            sb.append(i).append(",");
        }

        return StringUtils.chop(sb.toString());
    }

    private CalculationEnum[] calculationEnumFromString(String attribute) {
        if (attribute.isEmpty()) {
            return new CalculationEnum[0];
        }
        String[] indexCalString = attribute.split(",");
        CalculationEnum[] result = new CalculationEnum[indexCalString.length];
        int idx = 0;
        for (String indexString : indexCalString) {
            int funzioneIndex = Integer.parseInt(indexString.trim());
            result[idx++] = FUNZIONI_AGGREGAZIONE[funzioneIndex];
        }
        return result;
    }

    @Override
    public int compareTo(EntityBase obj) {
        if (!(obj instanceof FieldBILayout)) {
            return -1;
        }

        FieldBILayout field = (FieldBILayout) obj;

        if (areaIndex < field.getAreaIndex()) {
            return 1;
        }
        if (areaIndex == field.getAreaIndex()) {
            return 0;
        }
        return 1;
    }

    /**
     * @return Returns the areaIndex.
     */
    public int getAreaIndex() {
        return areaIndex;
    }

    /**
     * @return Returns the areaType.
     */
    public int getAreaType() {
        return areaType;
    }

    /**
     * @return Returns the colonna.
     */
    public Colonna getColonna() {
        return colonna;
    }

    /**
     * @return the grandTotalSummaryType
     */
    public int getGrandTotalSummaryType() {
        return grandTotalSummaryType;
    }

    /**
     * @return the modelIndex
     */
    public int getModelIndex() {
        return modelIndex;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return keyModel;
    }

    /**
     * @return the runningSummary
     */
    public List<RunningSummaryBILayout> getRunningSummary() {
        return runningSummary;
    }

    /**
     * @return the subtotal
     */
    public String getSubtotal() {
        return subtotal;
    }

    /**
     * @return the subtotalForRow
     */
    public CalculationEnum[] getSubtotalEnum() {
        return calculationEnumFromString(getSubtotal());
    }

    /**
     * @return the subtotalForColumn
     */
    public String getSubtotalForColumn() {
        return subtotalForColumn;
    }

    /**
     * @return the subtotalForColumn
     */
    public CalculationEnum[] getSubtotalForColumnEnum() {
        return calculationEnumFromString(getSubtotalForColumn());
    }

    /**
     * @return the subtotalForRow
     */
    public String getSubtotalForRow() {
        return subtotalForRow;
    }

    /**
     * @return the subtotalForRow
     */
    public CalculationEnum[] getSubtotalForRowEnum() {
        return calculationEnumFromString(getSubtotalForRow());
    }

    /**
     * @return the subtotalType
     */
    public int getSubtotalType() {
        return subtotalType;
    }

    /**
     * @return the subtotalTypeForColumn
     */
    public int getSubtotalTypeForColumn() {
        return subtotalTypeForColumn;
    }

    /**
     * @return the subtotalTypeForRow
     */
    public int getSubtotalTypeForRow() {
        return subtotalTypeForRow;
    }

    /**
     * @return the summaryType
     */
    public int getSummaryType() {
        return summaryType;
    }

    /**
     * @return Returns the title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return Returns the width.
     */
    public int getWidth() {
        return width;
    }

    @PostLoad
    private void initColonna() {
        colonna = AnalisiBIDomain.getColonna(keyModel);
    }

    /**
     * @return the ascending
     */
    public boolean isAscending() {
        return ascending;
    }

    /**
     * @return the duplicated
     */
    public boolean isDuplicated() {
        return duplicated;
    }

    /**
     * @return the hideOriginal
     */
    public boolean isHideOriginal() {
        return hideOriginal;
    }

    /**
     * @return the separateSubtotalSettings
     */
    public boolean isSeparateSubtotalSettings() {
        return separateSubtotalSettings;
    }

    /**
     *
     * @param keyModel
     *            key del modello
     */
    public void setKeyModel(String keyModel) {
        this.keyModel = keyModel;
        initColonna();
    }

    /**
     * @param runningSummary
     *            the runningSummary to set
     */
    public void setRunningSummary(List<RunningSummaryBILayout> runningSummary) {
        this.runningSummary = runningSummary;
    }

    /**
     *
     * @param title
     *            titolo
     */
    public void setTitle(String title) {
        this.title = title;
        if (title.isEmpty()) {
            this.title = keyModel;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "FieldBILayout [title=" + title + ", areaType=" + areaType + ", keyModel=" + keyModel + ", areaIndex="
                + areaIndex + "]";
    }

}
