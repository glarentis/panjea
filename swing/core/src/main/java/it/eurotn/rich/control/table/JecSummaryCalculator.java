package it.eurotn.rich.control.table;

import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.rich.bd.ValutaAziendaCache;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.pivot.DefaultSummaryCalculator;
import com.jidesoft.pivot.IPivotDataModel;
import com.jidesoft.pivot.PivotConstants;
import com.jidesoft.pivot.PivotField;
import com.jidesoft.pivot.PivotValueProvider;
import com.jidesoft.pivot.SummaryCalculator;
import com.jidesoft.pivot.SummaryCalculatorFactory;
import com.jidesoft.pivot.Values;

public class JecSummaryCalculator extends DefaultSummaryCalculator
        implements SummaryCalculatorFactory, SummaryCalculator {

    private static final int SUMMARY_INCREMENTO = PivotConstants.SUMMARY_RESERVED_MAX + 1;

    private List<Importo> importiValues;

    /**
     * Costruttore.
     * 
     */
    public JecSummaryCalculator() {
        super();
    }

    @Override
    public void addValue(IPivotDataModel ipivotdatamodel, PivotField pivotfield, int i, int j, Object obj) {
        super.addValue(ipivotdatamodel, pivotfield, i, j, obj);
    }

    @Override
    public void addValue(Object v) {
        super.addValue(v);
        if (v instanceof Importo) {
            importiValues.add((Importo) v);
        }
    }

    @Override
    public void addValue(PivotValueProvider pivotvalueprovider, PivotField pivotfield, Object obj) {
        super.addValue(pivotvalueprovider, pivotfield, obj);

    }

    @Override
    public void addValue(PivotValueProvider pivotvalueprovider, PivotField pivotfield, Values rowValues,
            Values columnValues, Object v) {
        super.addValue(pivotvalueprovider, pivotfield, rowValues, columnValues, v);
    }

    @Override
    public void clear() {
        super.clear();
        importiValues = new ArrayList<Importo>();
    }

    @Override
    public SummaryCalculator create() {
        return new JecSummaryCalculator();
    }

    @Override
    public int[] getAllowedSummaries(Class<?> arg0, ConverterContext arg1) {
        int[] summ = super.getAllowedSummaries(arg0, arg1);

        if (arg0 == Importo.class) {
            summ = new int[] { 0 };
        }
        return summ;
    }

    @Override
    public int getNumberOfSummaries() {
        return super.getNumberOfSummaries();
    }

    @Override
    public String getSummaryName(Locale locale, int type) {
        if (type == SUMMARY_INCREMENTO) {
            return "Incremento";
        }
        return super.getSummaryName(locale, type);
    }

    @Override
    public Object getSummaryResult(int type) {
        if (type == 0 && importiValues.size() != 0) {
            Importo importoResult = new Importo();
            importoResult.setTassoDiCambio(BigDecimal.ZERO);

            BigDecimal sumImportoValuta = BigDecimal.ZERO;
            BigDecimal sumImportoValutaAziendale = BigDecimal.ZERO;
            // Verifico se gli importi sono tutti della stessa valuta
            String codiceValuta = importiValues.get(0).getCodiceValuta();
            boolean isValutaUnica = true;
            for (Importo importo : importiValues) {
                sumImportoValuta = sumImportoValuta.add(importo.getImportoInValuta());
                sumImportoValutaAziendale = sumImportoValutaAziendale.add(importo.getImportoInValutaAzienda());
                isValutaUnica = (codiceValuta.equals(importo.getCodiceValuta()) && isValutaUnica);
            }
            // se ho più valute visualizzo la valuta aziendale.
            if (!isValutaUnica) {
                ValutaAziendaCache valutaCache = RcpSupport.getBean(ValutaAziendaCache.BEAN_ID);
                codiceValuta = valutaCache.caricaValutaAziendaCorrente().getCodiceValuta();
                importoResult.setCodiceValuta(codiceValuta);
                importoResult.setImportoInValuta(sumImportoValutaAziendale);
                importoResult.setImportoInValutaAzienda(sumImportoValutaAziendale);
                importoResult.setTassoDiCambio(BigDecimal.ZERO);
            } else {
                importoResult.setCodiceValuta(codiceValuta);
                importoResult.setImportoInValuta(sumImportoValuta);
                importoResult.setImportoInValutaAzienda(sumImportoValutaAziendale);
            }
            return importoResult;
        }

        return super.getSummaryResult(type);
    }
}
