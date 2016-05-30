package it.eurotn.rich.converter;

import java.util.Comparator;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.settings.SettingsException;
import org.springframework.richclient.settings.SettingsManager;

import com.jidesoft.comparator.ComparatorContext;
import com.jidesoft.comparator.ObjectComparatorManager;
import com.jidesoft.converter.ConverterContext;
import com.jidesoft.converter.ObjectConverterManager;

/**
 * Viene utilizzata per i bean che visualizzano più proprietà.<br/>
 * Introduce una proprietà che indica il tipo di visualizzazione da visualizzare (0..1..)
 *
 * @author giangi
 * @version 1.0, 28/mag/2013
 * @param <T>
 *
 */
public abstract class PanjeaCompositeConverter<T> extends PanjeaConverter<T> {
    private static final Logger LOGGER = Logger.getLogger(PanjeaCompositeConverter.class);

    protected Boolean visualizzaNormale = null;

    /**
     *
     * @param value
     *            valore da gestire
     * @param numCampo
     *            campo che si vuole visualizzare
     * @return valore del campo
     */
    public String getCampo(T value, int numCampo) {
        if (numCampo == 1) {
            return getCampo1(value);
        }
        return getCampo2(value);
    }

    /**
     *
     * @param value
     *            valore del bean
     * @return campo1 da renderizzare
     */
    protected abstract String getCampo1(T value);

    /**
     *
     * @param value
     *            valore del bean
     * @return campo2 da renderizzare
     */
    protected abstract String getCampo2(T value);

    @Override
    public Comparator<T> getComparator() {
        if (getVisualizzaNormale()) {
            return getComparatorCampo1();
        } else {
            return getComparatorCampo2();
        }
    }

    /**
     *
     *
     * @return comparator se visualizzato campo1-campo2.
     */
    protected abstract Comparator<T> getComparatorCampo1();

    /**
     *
     *
     * @return comparator se visualizzato campo2-campo1.
     */
    protected abstract Comparator<T> getComparatorCampo2();

    /**
     *
     * @return chiave delle settings locale per memorizzare il tipo di visualizzazione per il
     *         converter.
     */
    private String getKeySettings() {
        return new StringBuilder(getClasse().getCanonicalName()).append("Converter.visualizzazioneNormale").toString();
    }

    /**
     * @return the visualizzaNormale
     */
    private Boolean getVisualizzaNormale() {
        if (visualizzaNormale == null) {
            SettingsManager settings = (SettingsManager) Application.services().getService(SettingsManager.class);
            try {
                if (!settings.getUserSettings().contains(getKeySettings())) {
                    settings.getUserSettings().setDefaultBoolean(getKeySettings(), true);
                }
                visualizzaNormale = settings.getUserSettings().getBoolean(getKeySettings());
            } catch (SettingsException e) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("--> Errore durante il salvataggio del valore delle settings.", e);
                }
                visualizzaNormale = true;
            }
            registraComparator();
        }

        return visualizzaNormale;
    }

    @Override
    public void registra() {
        // Non registro i comparator. Lo faccio lazy perchè il converter viene creato sul
        // caricamento dell'applicazione
        // e non ho il settingManager
        if (getConverterContext() != null) {
            ObjectConverterManager.registerConverter(getClasse(), this, getConverterContext());
        } else {
            ObjectConverterManager.registerConverter(getClasse(), this);
        }
    }

    /**
     * Registra i comparator.
     */
    private void registraComparator() {
        ComparatorContext comparatorContextToRegister = ComparatorContext.DEFAULT_CONTEXT;
        if (getComparatorContext() != null) {
            comparatorContextToRegister = getComparatorContext();
        }
        ObjectComparatorManager.registerComparator(getClasse(), getComparator(), comparatorContextToRegister);
    }

    /**
     * Scambia il tipo di visualizzazione, registra i comparatori e rende persistente i settings.
     */
    public void scambiaVisualizzazione() {
        visualizzaNormale = !visualizzaNormale;
        registraComparator();
        SettingsManager settings = (SettingsManager) Application.services().getService(SettingsManager.class);
        try {
            settings.getUserSettings().setBoolean(getKeySettings(), visualizzaNormale);
            settings.getUserSettings().save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString(Object value, ConverterContext arg1) {
        if (value == null || !getClasse().isInstance(value)) {
            return "";
        }
        String campo1 = "";
        String campo2 = "";
        T valore = getClasse().cast(value);
        if (getVisualizzaNormale()) {
            campo1 = getCampo1(valore);
            campo2 = getCampo2(valore);
        } else {
            campo1 = getCampo2(valore);
            campo2 = getCampo1(valore);
        }
        StringBuilder sb = new StringBuilder(ObjectUtils.defaultIfNull(campo1, ""));
        if (!StringUtils.isEmpty(campo2)) {
            sb.append(" - ").append(campo2);
        }
        return sb.toString();
    }
}