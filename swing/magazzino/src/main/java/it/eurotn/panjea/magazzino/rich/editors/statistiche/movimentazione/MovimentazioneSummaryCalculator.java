package it.eurotn.panjea.magazzino.rich.editors.statistiche.movimentazione;

import java.math.BigDecimal;
import java.util.List;

import com.jidesoft.pivot.DefaultSummaryCalculator;
import com.jidesoft.pivot.DefaultValues;
import com.jidesoft.pivot.IPivotDataModel;
import com.jidesoft.pivot.PivotDataModel;
import com.jidesoft.pivot.PivotField;
import com.jidesoft.pivot.PivotValueProvider;
import com.jidesoft.pivot.SummaryCalculator;
import com.jidesoft.pivot.SummaryCalculatorFactory;
import com.jidesoft.pivot.Values;

import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;

/**
 *
 * Oltre a tutti i tipi gestiti da {@link DefaultSummaryCalculator} estende il supporto a
 * {@link Importo}.
 *
 * @author giangi
 * @version 1.0, 23/giu/2010
 *
 */
public class MovimentazioneSummaryCalculator extends DefaultSummaryCalculator implements SummaryCalculatorFactory {
    private static final int FIELD_ARTICOLO_INDEX = 1;
    private static final int FIELD_DEPOSITO_INDEX = 2;
    private static final int FIELD_IMPORTO_CARICO_INDEX = 15;
    private static final int FIELD_QTA_CARICO_INDEX = 9;
    private static final int FIELD_QTA_MOVIMENTATA = 11;
    private static final int FIELD_COSTO_MEDIO = 12;
    private static final int FIELD_IMPORTO_MOVIMENTATO_COSTO_MEDIO = 18;

    private BigDecimal costoMedio = null;
    private BigDecimal valoreMovimentatoCostoMedio = null;
    private BigDecimal valoreMovimentatoCostoMedioSomma = null;
    private boolean multiArticolo = false;
    private Integer indexFieldSummary = null;

    /*
     * (non-Javadoc)
     *
     * @see
     * com.jidesoft.pivot.DefaultSummaryCalculator#addValue(com.jidesoft.pivot.PivotValueProvider,
     * com.jidesoft.pivot.PivotField, com.jidesoft.pivot.Values, com.jidesoft.pivot.Values,
     * java.lang.Object)
     */
    @Override
    public void addValue(PivotValueProvider pivotvalueprovider, PivotField pivotfield, Values rowValues,
            Values columnValues, Object obj) {

        if (pivotfield.getModelIndex() == FIELD_IMPORTO_MOVIMENTATO_COSTO_MEDIO && costoMedio == null) {

            indexFieldSummary = FIELD_IMPORTO_MOVIMENTATO_COSTO_MEDIO;

            costoMedio = BigDecimal.ZERO;
            valoreMovimentatoCostoMedio = BigDecimal.ZERO;
            valoreMovimentatoCostoMedioSomma = BigDecimal.ZERO;

            PivotDataModel model = (PivotDataModel) pivotvalueprovider;

            BigDecimal importoCarico = BigDecimal.ZERO;
            Double qtaCarico = 0.0;
            Double qtaMovimentata = 0.0;

            List<Integer> rows = ((IPivotDataModel) model).getDataAt(((DefaultValues) rowValues).toCompoundKey(),
                    ((DefaultValues) columnValues).toCompoundKey());

            ArticoloLite articoloCurrent = (ArticoloLite) model.getDataSource().getValueAt(rows.get(0),
                    FIELD_ARTICOLO_INDEX);
            DepositoLite depositoCurrent = (DepositoLite) model.getDataSource().getValueAt(rows.get(0),
                    FIELD_DEPOSITO_INDEX);

            for (Integer row : rows) {

                ArticoloLite articoloLite = (ArticoloLite) model.getDataSource().getValueAt(row, FIELD_ARTICOLO_INDEX);
                DepositoLite depositoLite = (DepositoLite) model.getDataSource().getValueAt(row, FIELD_DEPOSITO_INDEX);
                if (!articoloCurrent.equals(articoloLite) || !depositoCurrent.equals(depositoLite)) {
                    Integer numeroDecimaliPrezzo = articoloCurrent.getNumeroDecimaliPrezzo();

                    valoreMovimentatoCostoMedio = valoreMovimentatoCostoMedio.setScale(numeroDecimaliPrezzo);
                    valoreMovimentatoCostoMedio = calcolaCostoMedio(qtaCarico, importoCarico, numeroDecimaliPrezzo)
                            .multiply(BigDecimal.valueOf(qtaMovimentata));

                    valoreMovimentatoCostoMedioSomma = valoreMovimentatoCostoMedioSomma
                            .add(valoreMovimentatoCostoMedio);
                    qtaCarico = 0.0;
                    qtaMovimentata = 0.0;
                    importoCarico = BigDecimal.ZERO;
                    multiArticolo = true;
                    valoreMovimentatoCostoMedio = BigDecimal.ZERO;
                    articoloCurrent = articoloLite;
                    depositoCurrent = depositoLite;
                }
                qtaCarico += (Double) model.getDataSource().getValueAt(row, FIELD_QTA_CARICO_INDEX);
                importoCarico = importoCarico
                        .add((BigDecimal) model.getDataSource().getValueAt(row, FIELD_IMPORTO_CARICO_INDEX));
                qtaMovimentata += (Double) model.getDataSource().getValueAt(row, FIELD_QTA_MOVIMENTATA);
            }
            valoreMovimentatoCostoMedio = calcolaCostoMedio(qtaCarico, importoCarico,
                    articoloCurrent.getNumeroDecimaliPrezzo()).multiply(BigDecimal.valueOf(qtaMovimentata));
            // if (valoreMovimentatoCostoMedio.doubleValue() > 0) {
            System.out.println("*************");
            System.out.println(articoloCurrent.getCodice() + " : " + valoreMovimentatoCostoMedioSomma.doubleValue()
                    + " : " + valoreMovimentatoCostoMedio.doubleValue() + " : " + qtaMovimentata);
            // }
            if (multiArticolo) {
                // sommo i dati dell'ultima riga
                valoreMovimentatoCostoMedioSomma = valoreMovimentatoCostoMedioSomma.add(valoreMovimentatoCostoMedio);
            }

        }

        if (pivotfield.getModelIndex() == FIELD_COSTO_MEDIO && costoMedio == null) {
            indexFieldSummary = FIELD_COSTO_MEDIO;

            costoMedio = BigDecimal.ZERO;
            valoreMovimentatoCostoMedio = BigDecimal.ZERO;

            PivotDataModel model = (PivotDataModel) pivotvalueprovider;

            BigDecimal importoCarico = BigDecimal.ZERO;
            Double qtaCarico = 0.0;

            List<Integer> rows = ((IPivotDataModel) model).getDataAt(((DefaultValues) rowValues).toCompoundKey(),
                    ((DefaultValues) columnValues).toCompoundKey());

            ArticoloLite articoloCurrent = (ArticoloLite) model.getDataSource().getValueAt(rows.get(0),
                    FIELD_ARTICOLO_INDEX);
            DepositoLite depositoCurrent = (DepositoLite) model.getDataSource().getValueAt(rows.get(0),
                    FIELD_DEPOSITO_INDEX);

            for (Integer row : rows) {
                qtaCarico += (Double) model.getDataSource().getValueAt(row, FIELD_QTA_CARICO_INDEX);
                importoCarico = importoCarico
                        .add((BigDecimal) model.getDataSource().getValueAt(row, FIELD_IMPORTO_CARICO_INDEX));

                ArticoloLite articoloLite = (ArticoloLite) model.getDataSource().getValueAt(row, FIELD_ARTICOLO_INDEX);
                DepositoLite depositoLite = (DepositoLite) model.getDataSource().getValueAt(row, FIELD_DEPOSITO_INDEX);

                if (!articoloCurrent.equals(articoloLite) || !depositoCurrent.equals(depositoLite)) {
                    multiArticolo = true;
                    articoloCurrent = articoloLite;
                    costoMedio = BigDecimal.ZERO;
                    break;
                }
            }
            costoMedio = calcolaCostoMedio(qtaCarico, importoCarico, articoloCurrent.getNumeroDecimaliPrezzo());
        }
        super.addValue(pivotvalueprovider, pivotfield, rowValues, columnValues, obj);

    }

    /**
     *
     * @param qtaCarico
     *            tot. delal qtaCarico
     * @param importoCarico
     *            tot importo carico
     * @param numDecimali
     *            numero decimali di arrotondamento
     * @return costo medio della riga
     */
    private BigDecimal calcolaCostoMedio(Double qtaCarico, BigDecimal importoCarico, Integer numDecimali) {
        if (qtaCarico > 0) {
            return importoCarico.divide(BigDecimal.valueOf(qtaCarico), numDecimali, BigDecimal.ROUND_HALF_UP);
        } else {
            return importoCarico;
        }
    }

    @Override
    public void clear() {
        super.clear();
        indexFieldSummary = null;
        multiArticolo = false;
        costoMedio = null;
        valoreMovimentatoCostoMedio = null;
        valoreMovimentatoCostoMedioSomma = null;
    }

    @Override
    public SummaryCalculator create() {
        return new MovimentazioneSummaryCalculator();
    }

    @Override
    public Object getSummaryResult(int i) {
        if (indexFieldSummary != null) {
            switch (indexFieldSummary) {
            case FIELD_COSTO_MEDIO:
                if (multiArticolo) {
                    return null;
                } else {
                    return costoMedio;
                }
            case FIELD_IMPORTO_MOVIMENTATO_COSTO_MEDIO:
                if (multiArticolo) {
                    return valoreMovimentatoCostoMedioSomma;
                } else {
                    return valoreMovimentatoCostoMedio;
                }
            default:
                return super.getSummaryResult(i);
            }
        } else {
            return super.getSummaryResult(i);
        }
    }
}
