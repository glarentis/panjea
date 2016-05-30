package it.eurotn.rich.binding.searchtext;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.log4j.Logger;
import org.springframework.binding.convert.ConversionExecutor;
import org.springframework.binding.convert.ConversionService;
import org.springframework.binding.form.FormModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.settings.SettingsException;
import org.springframework.richclient.settings.SettingsManager;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.QuickFilterField;
import com.jidesoft.swing.JidePopupMenu;
import com.jidesoft.utils.DefaultWildcardSupport;
import com.jidesoft.utils.WildcardSupport;

import it.eurotn.rich.weaklistener.WeakFocusListener;

/**
 * SearchText Component.
 *
 * @author giangi
 * @version 1.0, 31/dic/09
 */
public class SearchTextField extends QuickFilterField implements PropertyChangeListener, FocusListener {

    private static Logger logger = Logger.getLogger(SearchTextField.class);

    private static final long serialVersionUID = 1L;
    public static final String CLIENT_PROPERTY = "searchTextField";
    public static final String COMPONENT_ID = "searchTextField";
    private static final Icon DEFAULT_ICON = RcpSupport.getIcon("searchTextField.default.icon");

    private int searchingDelay = 200;
    private String propertyRenderer;

    private ValueModel valueModel;
    private int columnIndex;
    private DefaultWildcardSupport wildCardSupport;
    private boolean selectAuto = true;

    private MenuItemCommand caricaTuttoCommand;

    private MenuItemCommand selectAutoCommand;

    private MenuItemCommand clearCommand;
    private JidePopupMenu popup;

    private MostraPopupCommand mostraPopupCommand;

    private TimeOutIncrementalSearch timeOutIncrementalSearch;

    protected FormModel formModel;

    protected String formPropertyPath;

    private Class<?> classSearchTextField;

    private int textSearchDelay;

    private boolean tableBindingMode;

    /**
     * Costruttore.
     */
    public SearchTextField() {
        super();
        uninstallListeners();
        setSearchingDelay(searchingDelay);
        this.getTextField().addFocusListener(new WeakFocusListener(this, this.getTextField()));
    }

    @Override
    public void applyFilter(String arg0) {
        // Il controller prende in carico l'applyFilter
    }

    /**
     * @param formPropertyPathParam
     *            path della proprietà bindata sul formModel
     * @param formModelParam
     *            formModel
     * @param propertyRendererParam
     *            proprietà da renderizzare dell'oggettto
     * @param columnIndexParam
     *            indice della clolonna della tabella dove effettuare la ricerca incrementale
     * @param classSearchObject
     *            classe che viene gestita dalla serachObject
     */
    public void configure(final String formPropertyPathParam, FormModel formModelParam,
            final String propertyRendererParam, int columnIndexParam, Class<?> classSearchObject) {
        this.propertyRenderer = propertyRendererParam;
        this.formPropertyPath = formPropertyPathParam;
        this.formModel = formModelParam;
        this.columnIndex = columnIndexParam;

        initValueModel();

        StringBuilder keySelect = new StringBuilder(300);
        keySelect.append("selectAuto.").append(formModel.getId()).append(".").append(formPropertyPath).append(".")
                .append(propertyRenderer);

        if (!getSetting().contains(keySelect.toString())) {
            selectAuto = true;
        } else {
            selectAuto = getSetting().getBoolean(keySelect.toString());
        }

        StringBuilder keySearchDelay = new StringBuilder(300);
        keySearchDelay.append("keySearchDelay.").append(formModel.getId()).append(".").append(formPropertyPath)
                .append(".").append(propertyRenderer);

        if (getSetting().contains(keySearchDelay.toString())) {
            setSearchingDelay(getSetting().getInt(keySearchDelay.toString()));
        }

        this.formModel.getValueModel(formPropertyPathParam).addValueChangeListener(this);
        setCaseSensitive(false);
        setFromStart(true);

        Icon icon = null;
        if (classSearchObject != null) {
            icon = RcpSupport.getIcon(classSearchObject.getName());
        }
        setIcon(ObjectUtils.defaultIfNull(icon, DEFAULT_ICON));

        getLabel().setOpaque(false);
        getTextField().setName(formModel.getId() + "." + formPropertyPath + "." + propertyRendererParam);

        this.wildCardSupport = new DefaultWildcardSupport();
        refreshTextFieldFromValueModel();
    }

    @Override
    protected AbstractButton createButton() {
        return null;
    }

    @Override
    protected JidePopupMenu createContextMenu() {
        getTextField().requestFocusInWindow();
        return popup;
    }

    @Override
    protected JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setColumns(20);
        textField.putClientProperty(CLIENT_PROPERTY, this);
        return textField;
    }

    /**
     * .
     */
    public void disableShowPopUpImmediatly() {
        setSearchingDelay(textSearchDelay);
    }

    /**
     * .
     */
    public void enableShowPopUpImmediatly() {
        textSearchDelay = getSearchingDelay();
        setSearchingDelay(200);
    }

    @Override
    public void focusGained(FocusEvent e) {
        if (mostraPopupCommand != null) {
            mostraPopupCommand.setSearchTextField(this);
        }
        if (caricaTuttoCommand != null) {
            clearCommand.setSearchTextField(this);
            caricaTuttoCommand.setSearchTextField(this);
        }
        selectAutoCommand.setSearchTextField(this);
        timeOutIncrementalSearch.setSearchTextField(this);
        if (e.isTemporary()) {
            return;
        }
        installListeners();
        if (tableBindingMode) {
            getTextField().select(getText().length(), getText().length());
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        uninstallListeners();
        getTextField().select(0, 0);
    }

    /**
     * @return the columnPosition
     */
    public int getColumnIndex() {
        return columnIndex;
    }

    /**
     * @return propertyRenderer
     */
    public String getPropertyRenderer() {
        return propertyRenderer;
    }

    /**
     *
     * @return settingManager applicazione
     */
    private Settings getSetting() {
        SettingsManager settingsManager = (SettingsManager) ApplicationServicesLocator.services()
                .getService(SettingsManager.class);
        try {
            return settingsManager.getUserSettings();
        } catch (SettingsException e) {
            logger.error("-->errore nel recuperare le settings", e);
        }
        return null;
    }

    @Override
    public WildcardSupport getWildcardSupport() {
        return wildCardSupport;
    }

    /**
     * Init value model del propertypath + property rendered.
     */
    private void initValueModel() {
        if (propertyRenderer == null) {
            this.valueModel = formModel.getValueModel(formPropertyPath);
            this.classSearchTextField = formModel.getFieldMetadata(formPropertyPath).getPropertyType();
        } else {
            this.valueModel = formModel.getValueModel(formPropertyPath + "." + propertyRenderer);
            this.classSearchTextField = formModel.getFieldMetadata(formPropertyPath + "." + propertyRenderer)
                    .getPropertyType();
        }
    }

    /**
     * @return isAdjustingMode
     */
    public boolean isAdjustingMode() {
        boolean adjustingMode = false;
        if (valueModel.getValue() != null) {
            ConversionService conversionService = (ConversionService) Application.services()
                    .getService(ConversionService.class);
            ConversionExecutor conversionExecutor = conversionService.getConversionExecutor(classSearchTextField,
                    String.class);
            String valueModelConvert = ((String) conversionExecutor.execute(valueModel.getValue()));
            adjustingMode = valueModelConvert.trim().equalsIgnoreCase(getSearchingText().trim());
        } else {
            adjustingMode = getSearchingText().trim().length() == 0;
        }
        return adjustingMode;
    }

    /**
     * @return Returns the selectAuto.
     */
    public boolean isSelectAuto() {
        return selectAuto;
    }

    @Override
    public boolean isWildcardEnabled() {
        return false;
    }

    @Override
    public boolean processKeyBinding(KeyStroke ks, KeyEvent e, int condition, boolean pressed) {
        // Rimuovo su alt
        if (e.isAltDown() && e.getKeyCode() == KeyEvent.VK_DOWN) {
            return false;
        }
        return super.processKeyBinding(ks, e, condition, pressed);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        refreshTextFieldFromValueModel();
    }

    /**
     * refreshTextFieldFromValueModel.
     */
    private void refreshTextFieldFromValueModel() {
        if (valueModel.getValue() == null) {
            setText("");
            _searchingText = "";
        } else {
            ConversionService conversionService = (ConversionService) Application.services()
                    .getService(ConversionService.class);
            ConversionExecutor conversionExecutor = conversionService.getConversionExecutor(classSearchTextField,
                    String.class);
            setText((String) conversionExecutor.execute(valueModel.getValue()));
            _searchingText = (String) conversionExecutor.execute(valueModel.getValue());
        }

    }

    /**
     * Revert.
     */
    public void revert() {
        refreshTextFieldFromValueModel();
    }

    /**
     * @param caricaTuttoCommand
     *            il caricaTutto command to set
     */
    public void setCaricaTuttoCommand(CaricaTuttoCommand caricaTuttoCommand) {
        this.caricaTuttoCommand = caricaTuttoCommand;
    }

    /**
     * @param clearCommand
     *            the clearCommand to set
     */
    public void setClearCommand(MenuItemCommand clearCommand) {
        this.clearCommand = clearCommand;
    }

    /**
     * @param mostraPopUpCommand
     *            il command per mostrare il popup
     */
    public void setMostraPopUpCommand(MostraPopupCommand mostraPopUpCommand) {
        this.mostraPopupCommand = mostraPopUpCommand;
    }

    /**
     * @param jidePopupMenu
     *            the popup menu to set
     */
    public void setPopupMenu(JidePopupMenu jidePopupMenu) {
        this.popup = jidePopupMenu;
    }

    /**
     * @param propertyRenderer
     *            the property rendered to set
     */
    public void setPropertyRenderer(String propertyRenderer) {
        this.propertyRenderer = propertyRenderer;
        initValueModel();
    }

    @Override
    public void setSearchingDelay(int paramInt) {
        if (paramInt >= 0 && paramInt < 200) {
            paramInt = 200;
        }
        super.setSearchingDelay(paramInt);
    }

    public void setSearchingDelayAndSave(int paramInt) {
        setSearchingDelay(paramInt);
        paramInt = getSearchingDelay();
        if (formModel != null) {
            StringBuilder keySearchDelay = new StringBuilder(300);
            keySearchDelay.append("keySearchDelay.").append(formModel.getId()).append(".").append(formPropertyPath)
                    .append(".").append(propertyRenderer);

            getSetting().setInt(keySearchDelay.toString(), paramInt);
            try {
                getSetting().save();
            } catch (Exception e) {
                logger.error("-->errore ", e);
            }
        }
    }

    /**
     * @param selectAuto
     *            The selectAuto to set.
     */
    public void setSelectAuto(boolean selectAuto) {
        this.selectAuto = selectAuto;
        StringBuilder key = new StringBuilder(300);
        key.append("selectAuto.").append(formModel.getId()).append(".").append(formPropertyPath).append(".")
                .append(propertyRenderer);
        getSetting().setBoolean(key.toString(), selectAuto);
        try {
            getSetting().save();
        } catch (IOException e) {
            logger.error("-->errore ", e);
        }
    }

    /**
     * @param selectAutoCommand
     *            The selectAutoCommand to set.
     */
    public void setSelectAutoCommand(MenuItemCommand selectAutoCommand) {
        this.selectAutoCommand = selectAutoCommand;

    }

    /**
     *
     * @param b
     *            indica se la search è utilizzata come binding di un editor su una tabella.
     */
    public void setTableBindingMode(boolean b) {
        tableBindingMode = b;
    }

    /**
     * @param timeOutIncrementalSearch
     *            The timeOutIncrementalSearch to set.
     */
    public void setTimeOutIncrementalSearch(TimeOutIncrementalSearch timeOutIncrementalSearch) {
        this.timeOutIncrementalSearch = timeOutIncrementalSearch;
    }

    @Override
    protected void showContextMenu() {
        super.showContextMenu();
    }

    /**
     * Show menu.
     */
    public void showMenu() {
        super.showContextMenu();
    }

    /**
     * on/off della selezione automatica.
     */
    public void toggleSelectAuto() {
        selectAuto = !selectAuto;
        setSelectAuto(selectAuto);
    }

}