package it.eurotn.rich.control.table;

import com.jidesoft.pivot.DefaultSummaryCalculator;
import com.jidesoft.pivot.SummaryCalculator;
import com.jidesoft.pivot.SummaryCalculatorFactory;

public class DefaultSummaryCalculatorFactory implements SummaryCalculatorFactory {

    @Override
    public SummaryCalculator create() {
        return new DefaultSummaryCalculator();
    }

}
