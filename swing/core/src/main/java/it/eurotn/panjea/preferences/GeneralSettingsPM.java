package it.eurotn.panjea.preferences;

import org.springframework.richclient.settings.Settings;

public class GeneralSettingsPM {

    public static final String LOOK_AND_FEEL_STYLE = "look_and_feel_style";
    public static final String LOOK_AND_FEEL_THEME = "look_and_feel_theme";
    public static final String LETTURA_ASSEGNI = "letturaAssegni";
    public static final String MULTIPE_INSTANCE_SETTINGS_KEY = "multipleinstance";

    private String lookAndFeelStyle;
    private String textOnlyPrinterName;
    private String labelOnlyPrinterName;
    private String lookAndFeelTheme;

    private Boolean letturaAssegni;
    private boolean multipleInstance;

    /**
     * 
     * Costruttore.
     * 
     * @param userSettings
     *            settings del client.
     */
    public GeneralSettingsPM(final Settings userSettings) {
        setLetturaAssegni(userSettings.getBoolean(GeneralSettingsPM.LETTURA_ASSEGNI));
        setMultipleInstance(userSettings.getBoolean(GeneralSettingsPM.MULTIPE_INSTANCE_SETTINGS_KEY));
    }

    /**
     * @return the labelOnlyPrinterName
     */
    public String getLabelOnlyPrinterName() {
        return labelOnlyPrinterName;
    }

    /**
     * @return the letturaAssegni
     */
    public Boolean getLetturaAssegni() {
        return letturaAssegni;
    }

    /**
     * 
     * @return style name
     */
    public String getLook_and_feel_style() {
        return lookAndFeelStyle;
    }

    /**
     * @return the look_and_feel_theme.
     */
    public String getLook_and_feel_theme() {
        return lookAndFeelTheme;
    }

    /**
     * @return the textOnlyPrinterName
     */
    public String getTextOnlyPrinterName() {
        return textOnlyPrinterName;
    }

    /**
     * @return Returns the multipleInstance.
     */
    public boolean isMultipleInstance() {
        return multipleInstance;
    }

    /**
     * @param labelOnlyPrinterName
     *            the labelOnlyPrinterName to set
     */
    public void setLabelOnlyPrinterName(String labelOnlyPrinterName) {
        this.labelOnlyPrinterName = labelOnlyPrinterName;
    }

    /**
     * @param letturaAssegni
     *            the letturaAssegni to set
     */
    public void setLetturaAssegni(Boolean letturaAssegni) {
        this.letturaAssegni = letturaAssegni;
    }

    public void setLook_and_feel_style(String look_and_feel_style) {
        this.lookAndFeelStyle = look_and_feel_style;
    }

    /**
     * @param look_and_feel_theme
     *            the look_and_feel_theme to set
     */
    public void setLook_and_feel_theme(String look_and_feel_theme) {
        this.lookAndFeelTheme = look_and_feel_theme;
    }

    /**
     * @param multipleInstance
     *            The multipleInstance to set.
     */
    public void setMultipleInstance(boolean multipleInstance) {
        this.multipleInstance = multipleInstance;
    }

    /**
     * @param textOnlyPrinterName
     *            the textOnlyPrinterName to set
     */
    public void setTextOnlyPrinterName(String textOnlyPrinterName) {
        this.textOnlyPrinterName = textOnlyPrinterName;
    }
}
