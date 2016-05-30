/**
 *
 */
package it.eurotn.panjea.rich;

import it.eurotn.panjea.rich.filters.JecTableBetweenFilter;
import it.eurotn.panjea.rich.filters.JecTableEqualFilter;
import it.eurotn.panjea.rich.filters.JecTableGreaterOrEqualFilter;
import it.eurotn.panjea.rich.filters.JecTableGreaterThanFilter;
import it.eurotn.panjea.rich.filters.JecTableLastMonthFilter;
import it.eurotn.panjea.rich.filters.JecTableLastWeekFilter;
import it.eurotn.panjea.rich.filters.JecTableLastYearFilter;
import it.eurotn.panjea.rich.filters.JecTableLessOrEqualFilter;
import it.eurotn.panjea.rich.filters.JecTableLessThanFilter;
import it.eurotn.panjea.rich.filters.JecTableThisMonthFilter;
import it.eurotn.panjea.rich.filters.JecTableThisWeekFilter;
import it.eurotn.panjea.rich.filters.JecTableThisYearFilter;
import it.eurotn.panjea.rich.filters.JecTableTodayFilter;
import it.eurotn.panjea.rich.filters.JecTableYesterdayFilter;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.jidesoft.filter.AbstractFilter;
import com.jidesoft.filter.Filter;
import com.jidesoft.filter.FilterFactory;
import com.jidesoft.filter.FilterFactoryManager;

/**
 * @author leonardo
 */
public final class PanjeaFilterFactoryManager {

    /**
     * Inizializza i filtri personalizzati per i tipi data e calendar, rimuovendo prima tutti i filtri di default.
     */
    public static void initPanjeaFilterFactory() {
        List<FilterFactory> factoryListData = FilterFactoryManager.getDefaultInstance().getFilterFactories(Date.class);
        for (FilterFactory filterFactory : factoryListData) {
            FilterFactoryManager.getDefaultInstance().unregisterFilterFactory(Date.class, filterFactory);
        }

        factoryListData = FilterFactoryManager.getDefaultInstance().getFilterFactories(Date.class);
        for (FilterFactory filterFactory : factoryListData) {
            FilterFactoryManager.getDefaultInstance().unregisterFilterFactory(Date.class, filterFactory);
        }

        registerDate();
    }

    /**
     * Registra i filter factory per Date.class.
     */
    private static void registerDate() {
        FilterFactoryManager.getDefaultInstance().registerFilterFactory(Date.class, new FilterFactory() {

            @Override
            public Filter createFilter(Object... aobj) {
                JecTableTodayFilter todayFilter = new JecTableTodayFilter();
                return todayFilter;
            }

            @Override
            public String getConditionString(Locale locale) {
                return AbstractFilter.getConditionString(locale, "date", getName());
            }

            @Override
            public Class[] getExpectedDataTypes() {
                return new Class[] {};
            }

            @Override
            public String getName() {
                return "today";
            }
        });
        FilterFactoryManager.getDefaultInstance().registerFilterFactory(Date.class, new FilterFactory() {

            @Override
            public Filter createFilter(Object... aobj) {
                JecTableYesterdayFilter yesterdayFilter = new JecTableYesterdayFilter();
                return yesterdayFilter;
            }

            @Override
            public String getConditionString(Locale locale) {
                return AbstractFilter.getConditionString(locale, "date", getName());
            }

            @Override
            public Class[] getExpectedDataTypes() {
                return new Class[] {};
            }

            @Override
            public String getName() {
                return "yesterday";
            }
        });
        FilterFactoryManager.getDefaultInstance().registerFilterFactory(Date.class, new FilterFactory() {

            @Override
            public Filter createFilter(Object... aobj) {
                JecTableThisWeekFilter thisWeekFilter = new JecTableThisWeekFilter();
                return thisWeekFilter;
            }

            @Override
            public String getConditionString(Locale locale) {
                return AbstractFilter.getConditionString(locale, "date", getName());
            }

            @Override
            public Class[] getExpectedDataTypes() {
                return new Class[] {};
            }

            @Override
            public String getName() {
                return "thisWeek";
            }
        });
        FilterFactoryManager.getDefaultInstance().registerFilterFactory(Date.class, new FilterFactory() {

            @Override
            public Filter createFilter(Object... aobj) {
                JecTableLastWeekFilter lastWeekFilter = new JecTableLastWeekFilter();
                return lastWeekFilter;
            }

            @Override
            public String getConditionString(Locale locale) {
                return AbstractFilter.getConditionString(locale, "date", getName());
            }

            @Override
            public Class[] getExpectedDataTypes() {
                return new Class[] {};
            }

            @Override
            public String getName() {
                return "lastWeek";
            }
        });
        FilterFactoryManager.getDefaultInstance().registerFilterFactory(Date.class, new FilterFactory() {

            @Override
            public Filter createFilter(Object... aobj) {
                JecTableThisMonthFilter thisMonthFilter = new JecTableThisMonthFilter();
                return thisMonthFilter;
            }

            @Override
            public String getConditionString(Locale locale) {
                return AbstractFilter.getConditionString(locale, "date", getName());
            }

            @Override
            public Class[] getExpectedDataTypes() {
                return new Class[] {};
            }

            @Override
            public String getName() {
                return "thisMonth";
            }
        });
        FilterFactoryManager.getDefaultInstance().registerFilterFactory(Date.class, new FilterFactory() {

            @Override
            public Filter createFilter(Object... aobj) {
                JecTableLastMonthFilter lastMonthFilter = new JecTableLastMonthFilter();
                return lastMonthFilter;
            }

            @Override
            public String getConditionString(Locale locale) {
                return AbstractFilter.getConditionString(locale, "date", getName());
            }

            @Override
            public Class[] getExpectedDataTypes() {
                return new Class[] {};
            }

            @Override
            public String getName() {
                return "lastMonth";
            }
        });
        FilterFactoryManager.getDefaultInstance().registerFilterFactory(Date.class, new FilterFactory() {

            @Override
            public Filter createFilter(Object... aobj) {
                JecTableThisYearFilter thisYearFilter = new JecTableThisYearFilter();
                return thisYearFilter;
            }

            @Override
            public String getConditionString(Locale locale) {
                return AbstractFilter.getConditionString(locale, "date", getName());
            }

            @Override
            public Class[] getExpectedDataTypes() {
                return new Class[] {};
            }

            @Override
            public String getName() {
                return "thisYear";
            }
        });
        FilterFactoryManager.getDefaultInstance().registerFilterFactory(Date.class, new FilterFactory() {

            @Override
            public Filter createFilter(Object... aobj) {
                JecTableLastYearFilter lastYearFilter = new JecTableLastYearFilter();
                return lastYearFilter;
            }

            @Override
            public String getConditionString(Locale locale) {
                return AbstractFilter.getConditionString(locale, "date", getName());
            }

            @Override
            public Class[] getExpectedDataTypes() {
                return new Class[] {};
            }

            @Override
            public String getName() {
                return "lastYear";
            }
        });
        FilterFactoryManager.getDefaultInstance().registerFilterFactory(Date.class, new FilterFactory() {

            @Override
            public Filter createFilter(Object... aobj) {
                JecTableBetweenFilter betweenFilter = new JecTableBetweenFilter(aobj[0], aobj[1]);
                return betweenFilter;
            }

            @Override
            public String getConditionString(Locale locale) {
                return AbstractFilter.getConditionString(locale, "date", getName());
            }

            @Override
            public Class[] getExpectedDataTypes() {
                return new Class[] { Date.class, Date.class };
            }

            @Override
            public String getName() {
                return "between";
            }
        });
        FilterFactoryManager.getDefaultInstance().registerFilterFactory(Date.class, new FilterFactory() {

            @Override
            public Filter createFilter(Object... aobj) {
                JecTableEqualFilter betweenFilter = new JecTableEqualFilter();
                betweenFilter.setValue(aobj[0]);
                return betweenFilter;
            }

            @Override
            public String getConditionString(Locale locale) {
                return AbstractFilter.getConditionString(locale, "date", getName());
            }

            @Override
            public Class[] getExpectedDataTypes() {
                return new Class[] { Date.class };
            }

            @Override
            public String getName() {
                return "equal";
            }
        });
        FilterFactoryManager.getDefaultInstance().registerFilterFactory(Date.class, new FilterFactory() {

            @Override
            public Filter createFilter(Object... aobj) {
                JecTableGreaterThanFilter greaterThanFilter = new JecTableGreaterThanFilter();
                greaterThanFilter.setValue(aobj[0]);
                return greaterThanFilter;
            }

            @Override
            public String getConditionString(Locale locale) {
                return AbstractFilter.getConditionString(locale, "date", getName());
            }

            @Override
            public Class[] getExpectedDataTypes() {
                return new Class[] { Date.class };
            }

            @Override
            public String getName() {
                return "greater";
            }
        });
        FilterFactoryManager.getDefaultInstance().registerFilterFactory(Date.class, new FilterFactory() {

            @Override
            public Filter createFilter(Object... aobj) {
                JecTableGreaterOrEqualFilter greaterOrEqualFilter = new JecTableGreaterOrEqualFilter();
                greaterOrEqualFilter.setValue(aobj[0]);
                return greaterOrEqualFilter;
            }

            @Override
            public String getConditionString(Locale locale) {
                return AbstractFilter.getConditionString(locale, "date", getName());
            }

            @Override
            public Class[] getExpectedDataTypes() {
                return new Class[] { Date.class };
            }

            @Override
            public String getName() {
                return "greaterOrEqual";
            }
        });
        FilterFactoryManager.getDefaultInstance().registerFilterFactory(Date.class, new FilterFactory() {

            @Override
            public Filter createFilter(Object... aobj) {
                JecTableLessThanFilter lessThanFilter = new JecTableLessThanFilter();
                lessThanFilter.setValue(aobj[0]);
                return lessThanFilter;
            }

            @Override
            public String getConditionString(Locale locale) {
                return AbstractFilter.getConditionString(locale, "date", getName());
            }

            @Override
            public Class[] getExpectedDataTypes() {
                return new Class[] { Date.class };
            }

            @Override
            public String getName() {
                return "less";
            }
        });
        FilterFactoryManager.getDefaultInstance().registerFilterFactory(Date.class, new FilterFactory() {

            @Override
            public Filter createFilter(Object... aobj) {
                JecTableLessOrEqualFilter lessOrEqualFilter = new JecTableLessOrEqualFilter();
                lessOrEqualFilter.setValue(aobj[0]);
                return lessOrEqualFilter;
            }

            @Override
            public String getConditionString(Locale locale) {
                return AbstractFilter.getConditionString(locale, "date", getName());
            }

            @Override
            public Class[] getExpectedDataTypes() {
                return new Class[] { Date.class };
            }

            @Override
            public String getName() {
                return "lessOrEqual";
            }
        });

        // FilterFactoryManager.getDefaultInstance().registerFilterFactory(Date.class, new FilterFactory() {
        //
        // @Override
        // public Filter createFilter(Object... aobj) {
        // String numberOfDaysString = (String) aobj[0];
        // Integer numberOfDays = new Integer(numberOfDaysString);
        //
        // Calendar today = Calendar.getInstance();
        // Calendar nDaysAgo = Calendar.getInstance();
        // nDaysAgo.add(Calendar.DATE, -numberOfDays);
        //
        // JecTableBetweenFilter lastDaysFilter = new JecTableBetweenFilter(nDaysAgo.getTime(), today.getTime());
        //
        // return lastDaysFilter;
        // }
        //
        // @Override
        // public String getConditionString(Locale locale) {
        // return RcpSupport.getMessage("FilterCondition." + getName());
        // }
        //
        // @Override
        // public Class[] getExpectedDataTypes() {
        // return new Class[] { Integer.class };
        // }
        //
        // @Override
        // public String getName() {
        // return "lastDays";
        // }
        // });
    }

    /**
     * Costruttore.
     */
    private PanjeaFilterFactoryManager() {
        super();
    }

}
