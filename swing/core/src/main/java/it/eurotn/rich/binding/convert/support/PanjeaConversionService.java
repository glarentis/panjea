package it.eurotn.rich.binding.convert.support;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.binding.convert.ConversionContext;
import org.springframework.binding.convert.support.AbstractConverter;
import org.springframework.binding.convert.support.AbstractFormattingConverter;
import org.springframework.binding.convert.support.DefaultConversionService;
import org.springframework.binding.convert.support.TextToBoolean;
import org.springframework.binding.convert.support.TextToClass;
import org.springframework.binding.convert.support.TextToLabeledEnum;
import org.springframework.binding.format.FormatterFactory;
import org.springframework.binding.format.support.SimpleFormatterFactory;
import org.springframework.core.enums.LabeledEnum;
import org.springframework.richclient.convert.support.ListModelConverter;
import org.springframework.util.StringUtils;

import com.jidesoft.converter.ObjectConverterManager;

import it.eurotn.entity.annotation.EntityConverter;
import it.eurotn.rich.converter.PanjeaAnnotationConverter;
import it.eurotn.rich.converter.PanjeaConverter;

public class PanjeaConversionService extends DefaultConversionService implements InitializingBean {

    static final class BooleanToText extends AbstractConverter {

        public static final String VALUE_YES = "yes";

        public static final String VALUE_NO = "no";

        private String trueString;

        private String falseString;

        public BooleanToText() {
        }

        /**
         *
         * Costruttore.
         *
         * @param trueString
         *            true value as string
         * @param falseString
         *            false value as string
         */
        public BooleanToText(final String trueString, final String falseString) {
            this.trueString = trueString;
            this.falseString = falseString;
        }

        @Override
        protected Object doConvert(Object source, Class targetClass, ConversionContext context) throws Exception {
            Boolean bool = (Boolean) source;
            if (this.trueString != null && bool.booleanValue()) {
                return trueString;
            } else if (this.falseString != null && !bool.booleanValue()) {
                return falseString;
            } else if (bool.booleanValue()) {
                return VALUE_YES;
            } else {
                return VALUE_NO;
            }
        }

        @Override
        public Class[] getSourceClasses() {
            return new Class[] { Boolean.class };
        }

        @Override
        public Class[] getTargetClasses() {
            return new Class[] { String.class };
        }
    }

    static final class DateToText extends AbstractFormattingConverter {

        private final boolean allowEmpty;

        /**
         *
         * Costruttore.
         *
         * @param formatterLocator
         *            formatter locator
         * @param allowEmpty
         *            allow empty
         */
        protected DateToText(final FormatterFactory formatterLocator, final boolean allowEmpty) {
            super(formatterLocator);
            this.allowEmpty = allowEmpty;
        }

        @Override
        protected Object doConvert(Object source, Class targetClass, ConversionContext context) throws Exception {
            return (!allowEmpty || source != null) ? getFormatterFactory().getDateTimeFormatter().formatValue(source)
                    : "";
        }

        @Override
        public Class[] getSourceClasses() {
            return new Class[] { Date.class };
        }

        @Override
        public Class[] getTargetClasses() {
            return new Class[] { String.class };
        }
    }

    static final class IntegerToText extends AbstractFormattingConverter {

        public IntegerToText() {
            super(null);
        }

        @Override
        protected Object doConvert(Object source, Class targetClass, ConversionContext context) throws Exception {
            if (source != null) {
                Format format = new DecimalFormat("0");
                return format.format(source);
            } else {
                return "";
            }
        }

        @Override
        public Class[] getSourceClasses() {
            return new Class[] { Integer.class, Long.class };
        }

        @Override
        public Class[] getTargetClasses() {
            return new Class[] { String.class };
        }
    }

    static final class NumberToText extends AbstractFormattingConverter {

        private final boolean allowEmpty;

        /**
         *
         * Costruttore.
         *
         * @param formatterLocator
         *            formatter locator
         * @param allowEmpty
         *            allow empty
         */
        protected NumberToText(final FormatterFactory formatterLocator, final boolean allowEmpty) {
            super(formatterLocator);
            this.allowEmpty = allowEmpty;
        }

        @Override
        protected Object doConvert(Object source, Class targetClass, ConversionContext context) throws Exception {
            return (!allowEmpty || source != null)
                    ? getFormatterFactory().getNumberFormatter(source.getClass()).formatValue(source) : "";
        }

        @Override
        public Class[] getSourceClasses() {
            return new Class[] { Byte.class, Short.class, Float.class, Double.class, BigInteger.class,
                    BigDecimal.class };
        }

        @Override
        public Class[] getTargetClasses() {
            return new Class[] { String.class };
        }
    }

    static final class TextToDate extends AbstractFormattingConverter {

        private final boolean allowEmpty;

        /**
         *
         * Costruttore.
         *
         * @param formatterLocator
         *            formatter locator
         * @param allowEmpty
         *            allow empty
         */
        protected TextToDate(final FormatterFactory formatterFactory, final boolean allowEmpty) {
            super(formatterFactory);
            this.allowEmpty = allowEmpty;
        }

        @Override
        protected Object doConvert(Object source, Class targetClass, ConversionContext context) throws Exception {
            return (!allowEmpty || StringUtils.hasText((String) source))
                    ? getFormatterFactory().getDateTimeFormatter().parseValue((String) source, Date.class) : null;
        }

        @Override
        public Class[] getSourceClasses() {
            return new Class[] { String.class };
        }

        @Override
        public Class[] getTargetClasses() {
            return new Class[] { Date.class };
        }
    }

    private List<PanjeaConverter<?>> converters = new ArrayList<PanjeaConverter<?>>();

    @Override
    protected void addDefaultConverters() {
        super.addDefaultConverters();

        ObjectConverterManager.initDefaultConverter();

        addConverter(new TextToClass());
        addConverter(new TextToDate(new SimpleFormatterFactory(), true));
        addConverter(new DateToText(new SimpleFormatterFactory(), true));
        addConverter(new org.springframework.binding.convert.support.TextToNumber(new SimpleFormatterFactory()));
        addConverter(new NumberToText(new SimpleFormatterFactory(), true));
        // aggiungo un converter per i numeri interi perchè non devo
        // visualizzare i separatori delle migliaia
        addConverter(new IntegerToText());
        addConverter(new BooleanToText());
        addConverter(new TextToBoolean());
        addConverter(new CollectionConverter());
        addConverter(new ListModelConverter());
        addConverter(new TextToLabeledEnum());
        addDefaultAlias(String.class);
        addDefaultAlias(Short.class);
        addDefaultAlias(Integer.class);
        addAlias("int", Integer.class);
        addDefaultAlias(Byte.class);
        addDefaultAlias(Long.class);
        addDefaultAlias(Float.class);
        addDefaultAlias(Double.class);
        addAlias("double", Double.class);
        addDefaultAlias(BigInteger.class);
        addDefaultAlias(BigDecimal.class);
        addDefaultAlias(Boolean.class);
        addDefaultAlias(Class.class);
        addDefaultAlias(LabeledEnum.class);
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        // Applico i converter configurati sulle annotation degli entity
        Reflections reflections = new Reflections(
                new ConfigurationBuilder().setUrls(ClasspathHelper.forPackage("it.eurotn"))
                        .setScanners(new SubTypesScanner(), new TypeAnnotationsScanner())
                        .setExecutorService(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())));
        Set<Class<?>> annotatedConverter = reflections.getTypesAnnotatedWith(EntityConverter.class);
        for (Class<?> clazz : annotatedConverter) {
            PanjeaAnnotationConverter converter = new PanjeaAnnotationConverter(clazz);
            addConverters(converter.getSpringConverters());
            converter.registra();
        }

        // applico tutti i converters settati su xml
        if (converters != null) {
            for (PanjeaConverter<?> panjeaConverter : converters) {
                addConverters(panjeaConverter.getSpringConverters());
                panjeaConverter.registra();
            }
        }
    }

    /**
     *
     * @param converters
     *            converters to set
     */
    public void setConverters(List<PanjeaConverter<?>> converters) {
        this.converters = converters;
    }

}
