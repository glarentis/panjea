/**
 *
 */
package it.eurotn.panjea.magazzino.rich.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.factory.MenuFactory;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.bd.IRicercaArticoloBD;
import it.eurotn.panjea.magazzino.rich.editors.articolo.alternativi.ArticoliAlternativiDialog;
import it.eurotn.panjea.magazzino.rich.editors.categoriaarticolo.ArticoloCategoriaDTO;
import it.eurotn.panjea.magazzino.rich.editors.categoriaarticolo.RicercaAvanzataArticoliCommand;
import it.eurotn.panjea.magazzino.util.ArticoloRicerca;
import it.eurotn.panjea.magazzino.util.ParametriRicercaArticolo;
import it.eurotn.rich.search.AbstractSearchObject;

/**
 * Specializzazione della classe <code>AbstractSearchObject</code> responsabile di effettuare la ricerca degli articoli.
 *
 * @author fattazzo
 */
public class ArticoloSearchObject extends AbstractSearchObject {

    private class RicercaArticoliAlternativiSearchTextCommand extends ActionCommand {

        private JMenuItem menuItem;

        /**
         * Costruttore.
         */
        public RicercaArticoliAlternativiSearchTextCommand() {
            RcpSupport.configure(this);
        }

        @Override
        public JMenuItem createMenuItem(String faceDescriptorId, MenuFactory menuFactory,
                org.springframework.richclient.command.config.CommandButtonConfigurer buttonConfigurer) {
            menuItem = menuFactory.createMenuItem();
            attach(menuItem, faceDescriptorId, buttonConfigurer);
            menuItem.setAccelerator(KeyStroke.getKeyStroke("F11"));
            return menuItem;
        }

        @Override
        protected void doExecuteCommand() {
            ArticoloLite articoloLite = (ArticoloLite) searchPanel.getFormModel()
                    .getValueModel(searchPanel.getFormPropertyPath()).getValue();
            if (articoloLite != null) {
                Articolo articolo = magazzinoAnagraficaBD.caricaArticolo(articoloLite.creaProxyArticolo(), false);
                ArticoliAlternativiDialog articoliAlternativiDialog = new ArticoliAlternativiDialog(articolo);
                articoliAlternativiDialog.showDialog();
                ArticoloRicerca articoloSelezionato = articoliAlternativiDialog.getArticoloSelezionato();
                if (articoloSelezionato != null) {
                    ArticoloSearchObject.this.searchPanel.selectObject(articoloSelezionato);
                }
                articoliAlternativiDialog = null;
            }
        }

    }

    private class RicercaAvanzataArticoliSearchTextCommand extends RicercaAvanzataArticoliCommand {

        private JMenuItem menuItem;

        @Override
        public JMenuItem createMenuItem(String faceDescriptorId, MenuFactory menuFactory,
                org.springframework.richclient.command.config.CommandButtonConfigurer buttonConfigurer) {
            menuItem = menuFactory.createMenuItem();
            attach(menuItem, faceDescriptorId, buttonConfigurer);
            menuItem.setAccelerator(KeyStroke.getKeyStroke("F10"));
            return menuItem;
        }

    }

    private class RicercaAvanzataArticoloActionCommandInterceptor implements ActionCommandInterceptor {

        @Override
        public void postExecution(ActionCommand command) {
            List<ArticoloRicerca> articoliRicerca = ((RicercaAvanzataArticoliCommand) command).getArticoliSelezionati();
            if (articoliRicerca != null && articoliRicerca.size() > 0) {
                ArticoloSearchObject.this.searchPanel.selectObject(articoliRicerca.get(0));
            }

        }

        @Override
        public boolean preExecution(ActionCommand command) {
            return true;
        }

    }

    /**
     * Abilita la ricerca articolo per barcode.
     *
     * @author leonardo
     */
    private class RicercaPerBarCodeCommand extends ActionCommand {

        private JRadioButtonMenuItem menuItem;

        /**
         * Default contructor.
         */
        public RicercaPerBarCodeCommand() {
            super(RICERCAPERBARCODE_COMMAND_ID);
        }

        @Override
        public JMenuItem createMenuItem(String faceDescriptorId, MenuFactory menuFactory,
                org.springframework.richclient.command.config.CommandButtonConfigurer buttonConfigurer) {
            menuItem = menuFactory.createRadioButtonMenuItem();
            attach(menuItem, faceDescriptorId, buttonConfigurer);
            menuItem.setAccelerator(KeyStroke.getKeyStroke("F9"));
            return menuItem;
        }

        @Override
        protected void doExecuteCommand() {
            buttonMenuMap.get(getId()).setSelected(true);
            Icon icon = getIconSource().getIcon(RICERCAPERBARCODE_ICON_ID);
            ArticoloSearchObject.this.searchPanel.getTextFields().get("codice").setIcon(icon);
            getSettings().setString(getDefaulSettingsKey(), RICERCAPERBARCODE_COMMAND_ID);
        }

        @Override
        protected void onButtonAttached(AbstractButton button) {
            super.onButtonAttached(button);
            if (buttonMenuMap.get(getId()) == null) {
                ricercaArticoloButtonGroup.add(button);
                buttonMenuMap.put(getId(), (JRadioButtonMenuItem) button);
            } else {
                button.setEnabled(false);
            }
        }

    }

    /**
     * Abilita la ricerca articolo per codice articolo.
     *
     * @author leonardo
     */
    private class RicercaPerCodiceArticoloCommand extends ActionCommand {

        private JRadioButtonMenuItem menuItem;

        /**
         * Default contructor.
         */
        public RicercaPerCodiceArticoloCommand() {
            super(RICERCAPERCODICE_COMMAND_ID);
        }

        @Override
        public JMenuItem createMenuItem(String faceDescriptorId, MenuFactory menuFactory,
                org.springframework.richclient.command.config.CommandButtonConfigurer buttonConfigurer) {
            menuItem = menuFactory.createRadioButtonMenuItem();
            attach(menuItem, faceDescriptorId, buttonConfigurer);
            menuItem.setAccelerator(KeyStroke.getKeyStroke("F5"));
            return menuItem;
        }

        @Override
        protected void doExecuteCommand() {
            buttonMenuMap.get(getId()).setSelected(true);
            Icon icon = getIconSource().getIcon(RICERCAPERCODICE_ICON_ID);
            if (ArticoloSearchObject.this.searchPanel.getTextFields().get("codice") != null) {
                ArticoloSearchObject.this.searchPanel.getTextFields().get("codice").setIcon(icon);
                getSettings().setString(getDefaulSettingsKey(), RICERCAPERCODICE_COMMAND_ID);
            }
        }

        @Override
        protected void onButtonAttached(AbstractButton button) {
            super.onButtonAttached(button);
            if (buttonMenuMap.get(getId()) == null) {
                ricercaArticoloButtonGroup.add(button);
                buttonMenuMap.put(getId(), (JRadioButtonMenuItem) button);
            } else {
                button.setEnabled(false);
            }
        }

    }

    /**
     * Abilita la ricerca articolo per codiceEntita.
     *
     * @author leonardo
     */
    private class RicercaPerCodiceEntitaCommand extends ActionCommand {

        private JRadioButtonMenuItem menuItem;

        /**
         * Default contructor.
         */
        public RicercaPerCodiceEntitaCommand() {
            super(RICERCAPERCODICEENTITA_COMMAND_ID);
        }

        @Override
        public JMenuItem createMenuItem(String faceDescriptorId, MenuFactory menuFactory,
                org.springframework.richclient.command.config.CommandButtonConfigurer buttonConfigurer) {
            menuItem = menuFactory.createRadioButtonMenuItem();
            attach(menuItem, faceDescriptorId, buttonConfigurer);
            menuItem.setAccelerator(KeyStroke.getKeyStroke("F6"));
            return menuItem;
        }

        @Override
        protected void doExecuteCommand() {
            buttonMenuMap.get(getId()).setSelected(true);
            Icon icon = getIconSource().getIcon(RICERCAPERCODICEENTITA_ICON_ID);
            ArticoloSearchObject.this.searchPanel.getTextFields().get("codice").setIcon(icon);
            getSettings().setString(getDefaulSettingsKey(), RICERCAPERCODICEENTITA_COMMAND_ID);
        }

        @Override
        protected void onButtonAttached(AbstractButton button) {
            super.onButtonAttached(button);
            if (buttonMenuMap.get(getId()) == null) {
                ricercaArticoloButtonGroup.add(button);
                buttonMenuMap.put(getId(), (JRadioButtonMenuItem) button);
            } else {
                button.setEnabled(false);
            }
        }

    }

    /**
     * Abilita la ricerca articolo per codice interno articolo.
     *
     * @author leonardo
     */
    private class RicercaPerCodiceInternoCommand extends ActionCommand {

        private JRadioButtonMenuItem menuItem;

        /**
         * Default contructor.
         */
        public RicercaPerCodiceInternoCommand() {
            super(RICERCAPERCODICEINTERNO_COMMAND_ID);
        }

        @Override
        public JMenuItem createMenuItem(String faceDescriptorId, MenuFactory menuFactory,
                org.springframework.richclient.command.config.CommandButtonConfigurer buttonConfigurer) {
            menuItem = menuFactory.createRadioButtonMenuItem();
            attach(menuItem, faceDescriptorId, buttonConfigurer);
            menuItem.setAccelerator(KeyStroke.getKeyStroke("F12"));
            return menuItem;
        }

        @Override
        protected void doExecuteCommand() {
            buttonMenuMap.get(getId()).setSelected(true);
            Icon icon = getIconSource().getIcon(RICERCAPERCODICEINTERNO_ICON_ID);
            if (ArticoloSearchObject.this.searchPanel.getTextFields().get("codice") != null) {
                ArticoloSearchObject.this.searchPanel.getTextFields().get("codice").setIcon(icon);
                getSettings().setString(getDefaulSettingsKey(), RICERCAPERCODICEINTERNO_COMMAND_ID);
            }
        }

        @Override
        protected void onButtonAttached(AbstractButton button) {
            super.onButtonAttached(button);
            if (buttonMenuMap.get(getId()) == null) {
                ricercaArticoloButtonGroup.add(button);
                buttonMenuMap.put(getId(), (JRadioButtonMenuItem) button);
            } else {
                button.setEnabled(false);
            }
        }

    }

    public static final String PAGE_ID = "articoloSearchObject";
    public static final String ID_INSTALLAZIONE_KEY = "idInstallazione";

    public static final String ENTITA_KEY = "entitaPerCodiceArticoloEntita";

    public static final String ESCLUDI_DISTINTE = "escludiDistinte";
    public static final String SOLO_DISTINTE = "soloDistinte";

    public static final String RICERCAPERCODICE_COMMAND_ID = "ricercaArticoloPerCodiceCommand";
    public static final String RICERCAPERCODICEINTERNO_COMMAND_ID = "ricercaArticoloPerCodiceInternoCommand";
    public static final String RICERCAPERBARCODE_COMMAND_ID = "ricercaArticoloPerBarCodeCommand";
    public static final String RICERCAPERCODICEENTITA_COMMAND_ID = "ricercaArticoloPerCodiceEntitaCommand";

    public static final String RICERCAPERCODICE_ICON_ID = "ricercaArticoloPerCodiceCommand.icon";
    public static final String RICERCAPERCODICEINTERNO_ICON_ID = "ricercaArticoloPerCodiceInternoCommand.icon";
    public static final String RICERCAPERBARCODE_ICON_ID = "ricercaArticoloPerBarCodeCommand.icon";
    public static final String RICERCAPERCODICEENTITA_ICON_ID = "ricercaArticoloPerCodiceEntitaCommand.icon";

    private static Logger logger = Logger.getLogger(ArticoloSearchObject.class);

    private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;
    private IRicercaArticoloBD ricercaArticoloInstallazioneBD;

    private RicercaPerCodiceArticoloCommand ricercaPerCodiceArticoloCommand = null;
    private RicercaPerCodiceEntitaCommand ricercaPerCodiceEntitaCommand = null;
    private RicercaPerCodiceInternoCommand ricercaPerCodiceInternoCommand = null;
    private RicercaPerBarCodeCommand ricercaPerBarCodeCommand = null;

    private RicercaAvanzataArticoliSearchTextCommand ricercaAvanzataArticoliCommand = null;
    private RicercaArticoliAlternativiSearchTextCommand ricercaArticoliAlternativiSearchTextCommand = null;
    private List<AbstractCommand> customCommands;
    private ButtonGroup ricercaArticoloButtonGroup = null;

    private Map<String, JRadioButtonMenuItem> buttonMenuMap = null;

    /**
     * Costruttore.
     */
    public ArticoloSearchObject() {
        super(PAGE_ID);
        this.ricercaPerCodiceArticoloCommand = new RicercaPerCodiceArticoloCommand();
        this.ricercaPerCodiceEntitaCommand = new RicercaPerCodiceEntitaCommand();
        this.ricercaPerBarCodeCommand = new RicercaPerBarCodeCommand();
        this.ricercaPerCodiceInternoCommand = new RicercaPerCodiceInternoCommand();
        this.ricercaAvanzataArticoliCommand = new RicercaAvanzataArticoliSearchTextCommand();
        this.ricercaArticoliAlternativiSearchTextCommand = new RicercaArticoliAlternativiSearchTextCommand();
        this.ricercaArticoloButtonGroup = new ButtonGroup();
        this.buttonMenuMap = new HashMap<String, JRadioButtonMenuItem>();

    }

    @Override
    public void configureSearchText(it.eurotn.rich.binding.searchtext.SearchTextField searchTextField) {
        super.configureSearchText(searchTextField);
        String commandIdDefault = getSettings().getString(getDefaulSettingsKey());
        if (commandIdDefault != null && !commandIdDefault.equals("")) {
            buttonMenuMap.get(commandIdDefault).doClick();
        } else {
            buttonMenuMap.get(RICERCAPERCODICE_COMMAND_ID).doClick();
        }

        searchTextField.getTextField().getInputMap().put(KeyStroke.getKeyStroke("F5"),
                ricercaPerCodiceArticoloCommand.getId());
        searchTextField.getTextField().getActionMap().put(ricercaPerCodiceArticoloCommand.getId(),
                ricercaPerCodiceArticoloCommand.getActionAdapter());

        searchTextField.getTextField().getInputMap().put(KeyStroke.getKeyStroke("F6"),
                ricercaPerCodiceEntitaCommand.getId());
        searchTextField.getTextField().getActionMap().put(ricercaPerCodiceEntitaCommand.getId(),
                ricercaPerCodiceEntitaCommand.getActionAdapter());

        searchTextField.getTextField().getInputMap().put(KeyStroke.getKeyStroke("F9"),
                ricercaPerBarCodeCommand.getId());
        searchTextField.getTextField().getActionMap().put(ricercaPerBarCodeCommand.getId(),
                ricercaPerBarCodeCommand.getActionAdapter());

        searchTextField.getTextField().getInputMap().put(KeyStroke.getKeyStroke("F12"),
                ricercaPerCodiceInternoCommand.getId());
        searchTextField.getTextField().getActionMap().put(ricercaPerCodiceInternoCommand.getId(),
                ricercaPerCodiceInternoCommand.getActionAdapter());

        searchTextField.getTextField().getInputMap().put(KeyStroke.getKeyStroke("F10"),
                ricercaAvanzataArticoliCommand.getId());
        searchTextField.getTextField().getActionMap().put(ricercaAvanzataArticoliCommand.getId(),
                ricercaAvanzataArticoliCommand.getActionAdapter());

        searchTextField.getTextField().getInputMap().put(KeyStroke.getKeyStroke("F11"),
                ricercaArticoliAlternativiSearchTextCommand.getId());
        searchTextField.getTextField().getActionMap().put(ricercaArticoliAlternativiSearchTextCommand.getId(),
                ricercaArticoliAlternativiSearchTextCommand.getActionAdapter());

    }

    @Override
    public List<AbstractCommand> getCustomCommands() {
        logger.debug("--> Enter getCustomCommands");
        if (customCommands == null) {
            customCommands = new ArrayList<AbstractCommand>();
            RcpSupport.configure(ricercaPerCodiceArticoloCommand);
            RcpSupport.configure(ricercaPerCodiceEntitaCommand);
            RcpSupport.configure(ricercaPerBarCodeCommand);
            RcpSupport.configure(ricercaPerCodiceInternoCommand);

            ricercaAvanzataArticoliCommand.addCommandInterceptor(new RicercaAvanzataArticoloActionCommandInterceptor());

            // aggiunta action specializzate
            customCommands.add(ricercaPerCodiceArticoloCommand);
            customCommands.add(ricercaPerCodiceEntitaCommand);
            customCommands.add(ricercaPerBarCodeCommand);
            customCommands.add(ricercaPerCodiceInternoCommand);
            customCommands.add(ricercaAvanzataArticoliCommand);
            customCommands.add(ricercaArticoliAlternativiSearchTextCommand);
        }
        logger.debug("--> Exit getCustomCommands");
        return customCommands;
    }

    @Override
    public List<?> getData(String fieldSearch, String valueSearch) {
        Map<String, Object> parameters = searchPanel.getMapParameters();
        ParametriRicercaArticolo parametriRicercaArticolo = new ParametriRicercaArticolo();
        EntitaLite entita = (EntitaLite) parameters.get(ArticoloSearchObject.ENTITA_KEY);
        if (entita != null) {
            parametriRicercaArticolo.setIdEntita(entita.getId());
            parametriRicercaArticolo.setAssortimento(entita.isAssortimentoArticoli());
        }

        if ("codice".equals(fieldSearch)) {
            if (buttonMenuMap.get(RICERCAPERBARCODE_COMMAND_ID).isSelected()) {
                parametriRicercaArticolo.setBarCode(valueSearch);
            } else if (buttonMenuMap.get(RICERCAPERCODICE_COMMAND_ID).isSelected()) {
                parametriRicercaArticolo.setCodice(valueSearch);
            } else if (buttonMenuMap.get(RICERCAPERCODICEENTITA_COMMAND_ID).isSelected()) {
                parametriRicercaArticolo.setCodiceEntita(valueSearch);
            } else if (buttonMenuMap.get(RICERCAPERCODICEINTERNO_COMMAND_ID).isSelected()) {
                parametriRicercaArticolo.setCodiceInterno(valueSearch);
            }
        } else if ("descrizione".equals(fieldSearch)) {
            parametriRicercaArticolo.setDescrizione(valueSearch);
        }

        if (parameters.get(ESCLUDI_DISTINTE) != null) {
            parametriRicercaArticolo.setEscludiDistinte((Boolean) parameters.get(ESCLUDI_DISTINTE));
        }
        if (parameters.get(SOLO_DISTINTE) != null) {
            parametriRicercaArticolo.setSoloDistinte((Boolean) parameters.get(SOLO_DISTINTE));
        }

        List<ArticoloRicerca> articoliRicerca = null;
        if (parameters.get(ID_INSTALLAZIONE_KEY) != null && ricercaArticoloInstallazioneBD != null) {
            parametriRicercaArticolo.setIdInstallazione((Integer) parameters.get(ID_INSTALLAZIONE_KEY));
            articoliRicerca = ricercaArticoloInstallazioneBD.ricercaArticoliSearchObject(parametriRicercaArticolo);
        } else {
            articoliRicerca = magazzinoAnagraficaBD.ricercaArticoliSearchObject(parametriRicercaArticolo);
        }

        return articoliRicerca;
    }

    /**
     * @return chiave per il settings della search. Personalizzata per il formmodel.
     */
    private String getDefaulSettingsKey() {
        return "articoloSearchObject.ricercaDefault." + searchPanel.getFormModel().getId();
    }

    @Override
    public Object getValueObject(Object object) {
        ArticoloRicerca articoloRicerca = (ArticoloRicerca) object;
        object = articoloRicerca.createProxyArticoloLite();
        return object;
    }

    @Override
    public boolean isOpenNewObjectManaged() {
        return false;
    }

    @Override
    public void openEditor(Object object) {
        super.openEditor(object);
        Articolo artTmp = new Articolo();
        // può arrivare un Articolo da ArticoloEntitaForm
        if (object instanceof ArticoloRicerca) {
            artTmp.setId(((ArticoloRicerca) object).getId());
        } else if (object instanceof ArticoloLite) {
            artTmp.setId(((ArticoloLite) object).getId());
        } else {
            artTmp = (Articolo) object;
        }

        Articolo articoloCaricato = magazzinoAnagraficaBD.caricaArticolo(artTmp, true);
        ArticoloCategoriaDTO articoloCategoriaDTO = new ArticoloCategoriaDTO(articoloCaricato,
                articoloCaricato.getCategoria());
        LifecycleApplicationEvent event = new OpenEditorEvent(articoloCategoriaDTO);
        Application.instance().getApplicationContext().publishEvent(event);
    }

    /**
     * setter for magazzinoAnagraficaBD.
     *
     * @param magazzinoAnagraficaBD
     *            magazzinoAnagraficaBD
     */
    public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
        this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
    }

    /**
     * @param ricercaArticoloInstallazioneBD
     *            the ricercaArticoloInstallazioneBD to set
     */
    public final void setRicercaArticoloInstallazioneBD(IRicercaArticoloBD ricercaArticoloInstallazioneBD) {
        this.ricercaArticoloInstallazioneBD = ricercaArticoloInstallazioneBD;
    }
}
