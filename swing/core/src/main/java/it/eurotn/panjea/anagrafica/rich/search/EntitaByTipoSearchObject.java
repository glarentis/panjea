package it.eurotn.panjea.anagrafica.rich.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import org.springframework.richclient.factory.MenuFactory;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

import it.eurotn.panjea.agenti.domain.Agente;
import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.Cliente;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.Fornitore;
import it.eurotn.panjea.anagrafica.domain.Vettore;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.domain.lite.FornitoreLite;
import it.eurotn.panjea.anagrafica.domain.lite.VettoreLite;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.util.parametriricerca.ParametriRicercaEntita;
import it.eurotn.panjea.anagrafica.util.parametriricerca.ParametriRicercaEntita.FieldSearch;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.binding.searchtext.SearchTextField;
import it.eurotn.rich.search.AbstractSearchObject;

/**
 *
 * @author Leonardo
 * @version 1.0, 29/mag/07
 */
public class EntitaByTipoSearchObject extends AbstractSearchObject {

    private class RicercaPerCodiceCommand extends ActionCommand {

        private JRadioButtonMenuItem menuItem;

        /**
         * Costruttore.
         */
        public RicercaPerCodiceCommand() {
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
            // prendo l'icona della proprietà mappata dalla search. Questo
            // perchè la ricerca delle entità ritorna sempre
            // EntitaLite quindi vedrei sempre l'icona dell'entita e non quella
            // del form visto che alla seach dovrei
            // specificare EntitaLite sulla creazione.
            Icon icon = getIconSource().getIcon(searchPanel.getClassFormPropertyPath().getName());
            for (Entry<String, SearchTextField> entry : EntitaByTipoSearchObject.this.searchPanel.getTextFields()
                    .entrySet()) {
                entry.getValue().setIcon(icon);
            }
            if (!getSettings().getString(RICERCA_DEFAULT_SETTINGS_KEY).equals(RICERCAPERCODICE_COMMAND_ID)) {
                getSettings().setString(RICERCA_DEFAULT_SETTINGS_KEY, RICERCAPERCODICE_COMMAND_ID);
            }
            buttonMenuMap.get(getId()).setSelected(true);
        }

        @Override
        protected void onButtonAttached(AbstractButton button) {
            super.onButtonAttached(button);
            if (buttonMenuMap.get(getId()) == null) {
                ricercaEntitaButtonGroup.add(button);
                buttonMenuMap.put(getId(), (JRadioButtonMenuItem) button);
                buttonMenuMap.get(getId()).setSelected(true);
            } else {
                button.setEnabled(false);
            }
        }

    }

    private class RicercaPerPartIVACommand extends ActionCommand {

        private JRadioButtonMenuItem menuItem;

        /**
         * Costruttore.
         */
        public RicercaPerPartIVACommand() {
            super(RICERCAPERPARTIVA_COMMAND_ID);
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
            Icon icon = getIconSource().getIcon(RICERCAPERPARTIVA_ICON_ID);
            EntitaByTipoSearchObject.this.searchPanel.getTextFields().get("codice").setIcon(icon);
            if (!getSettings().getString(RICERCA_DEFAULT_SETTINGS_KEY).equals(RICERCAPERPARTIVA_COMMAND_ID)) {
                getSettings().setString(RICERCA_DEFAULT_SETTINGS_KEY, RICERCAPERPARTIVA_COMMAND_ID);
            }
            buttonMenuMap.get(getId()).setSelected(true);
        }

        @Override
        protected void onButtonAttached(AbstractButton button) {
            super.onButtonAttached(button);
            if (buttonMenuMap.get(getId()) == null) {
                ricercaEntitaButtonGroup.add(button);
                buttonMenuMap.put(getId(), (JRadioButtonMenuItem) button);
            } else {
                button.setEnabled(false);
            }
        }

    }

    private static Logger logger = Logger.getLogger(EntitaByTipoSearchObject.class);
    public static final String TIPOENTITA_KEY = "tipoEntita";
    public static final String TIPOENTITA_RIFORNIMENTO_KEY = "tipoEntitaRifornimento";
    public static final String TIPI_ENTITA_LIST_KEY = "tipiEntitaList";
    public static final String INCLUDI_ENTITA_POTENZIALI = "includiEntitaPotenziali";
    public static final String FILTRO_ENTITA_ABILITATO = "filtroSuAbilitatoEntita";
    public static final String FILTER_LIST_KEY = "filterListKey";
    public static final String RETAIN_LIST_KEY = "retainListKey";

    public static final String RICERCA_DEFAULT_SETTINGS_KEY = "entitaByTipoSearchObject.ricercaDefault";
    public static final String RICERCAPERPARTIVA_COMMAND_ID = "ricercaEntitaPerPartIvaCommand";
    public static final String RICERCAPERCODICE_COMMAND_ID = "ricercaEntitaPerCodiceCommand";
    public static final String RICERCAPERCODICE_ICON_ID = "ricercaEntitaPerCodiceCommand.icon";
    public static final String RICERCAPERPARTIVA_ICON_ID = "ricercaEntitaPerPartIvaCommand.icon";

    private static final String PAGE_ID = "entitaByTipoSearchObject";
    private ButtonGroup ricercaEntitaButtonGroup = null;

    private Map<String, JRadioButtonMenuItem> buttonMenuMap = null;
    private RicercaPerCodiceCommand ricercaPerCodiceCommand = null;

    private RicercaPerPartIVACommand ricercaPerPartIVACommand = null;
    private IAnagraficaBD anagraficaBD;
    private TipoEntita tipoEntita = null;
    private List<TipoEntita> tipiEntitaList = null;

    private List<AbstractCommand> customCommands;

    /**
     * Costruttore.
     */
    public EntitaByTipoSearchObject() {
        super(PAGE_ID);
        this.ricercaPerCodiceCommand = new RicercaPerCodiceCommand();
        this.ricercaPerPartIVACommand = new RicercaPerPartIVACommand();
        this.ricercaEntitaButtonGroup = new ButtonGroup();
        this.buttonMenuMap = new HashMap<String, JRadioButtonMenuItem>();
    }

    @Override
    public void configureSearchText(it.eurotn.rich.binding.searchtext.SearchTextField searchTextField) {
        super.configureSearchText(searchTextField);

        searchTextField.getTextField().getInputMap().put(KeyStroke.getKeyStroke("F5"), ricercaPerCodiceCommand.getId());
        searchTextField.getTextField().getActionMap().put(ricercaPerCodiceCommand.getId(),
                ricercaPerCodiceCommand.getActionAdapter());

        searchTextField.getTextField().getInputMap().put(KeyStroke.getKeyStroke("F6"),
                ricercaPerPartIVACommand.getId());
        searchTextField.getTextField().getActionMap().put(ricercaPerPartIVACommand.getId(),
                ricercaPerPartIVACommand.getActionAdapter());
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object createNewInstance() {
        // tipo entità predefinito
        Object instanceNew = new FornitoreLite();

        Map<String, Object> parameters = searchPanel.getMapParameters();
        if (parameters.get(TIPOENTITA_KEY) != null) {
            this.tipoEntita = (TipoEntita) parameters.get(TIPOENTITA_KEY);
        }
        // se nel form non ho definito il tipo, ma una lista di tipi e ne ho solo uno, prendo quello
        // come tipo entita
        if (tipoEntita == null) {
            List<TipoEntita> tipi = (List<TipoEntita>) parameters.get(TIPI_ENTITA_LIST_KEY);
            if (tipi != null && tipi.size() == 1) {
                this.tipoEntita = tipi.get(0);
            }
        }

        if (this.tipoEntita != null) {
            // siccome banca e azienda non sono entita che derivano da EntitaLite assegno di default
            // il fornitore.
            switch (tipoEntita) {
            case AZIENDA:
            case BANCA:
                instanceNew = new FornitoreLite();
                break;
            default:
                instanceNew = tipoEntita.createInstanceLite();
                break;
            }
        }

        return instanceNew;
    }

    /**
     * @return Returns the anagraficaBD.
     */
    public IAnagraficaBD getAnagraficaBD() {
        return anagraficaBD;
    }

    @Override
    public List<AbstractCommand> getCustomCommands() {
        logger.debug("--> Enter getCustomCommands");
        if (customCommands == null) {
            RcpSupport.configure(ricercaPerCodiceCommand);
            RcpSupport.configure(ricercaPerPartIVACommand);
            customCommands = new ArrayList<AbstractCommand>();
            // aggiunta action specializzate
            customCommands.add(ricercaPerCodiceCommand);
            customCommands.add(ricercaPerPartIVACommand);
        }
        logger.debug("--> Exit getCustomCommands");
        return customCommands;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<?> getData(String fieldSearch, String valueSearch) {
        Map<String, Object> parameters = searchPanel.getMapParameters();

        ParametriRicercaEntita parametriRicercaEntita = new ParametriRicercaEntita();
        if (parameters.get(TIPOENTITA_KEY) != null) {
            this.tipoEntita = (TipoEntita) parameters.get(TIPOENTITA_KEY);
        }
        Boolean isRifornimento = (Boolean) parameters.get(TIPOENTITA_RIFORNIMENTO_KEY);
        if (isRifornimento != null && isRifornimento) {
            this.tipoEntita = TipoEntita.CLIENTE;
        }

        if (parameters.get(TIPI_ENTITA_LIST_KEY) != null) {
            this.tipiEntitaList = (List<TipoEntita>) parameters.get(TIPI_ENTITA_LIST_KEY);
        }
        if (parameters.get(INCLUDI_ENTITA_POTENZIALI) != null) {
            parametriRicercaEntita.setIncludiEntitaPotenziali((Boolean) parameters.get(INCLUDI_ENTITA_POTENZIALI));
        }
        if (parameters.get(FILTRO_ENTITA_ABILITATO) != null) {
            parametriRicercaEntita.setAbilitato(Boolean.TRUE);
        }

        if ("codice".equals(fieldSearch)) {
            if (buttonMenuMap.get(RICERCAPERCODICE_COMMAND_ID).isSelected()) {
                parametriRicercaEntita.setFieldSearch(FieldSearch.CODICE);
                parametriRicercaEntita.setCodice(valueSearch);
            } else if (buttonMenuMap.get(RICERCAPERPARTIVA_COMMAND_ID).isSelected()) {
                parametriRicercaEntita.setFieldSearch(FieldSearch.PARTITAIVA);
                parametriRicercaEntita.setPartitaIva(valueSearch);
            }
        } else if ("anagrafica.denominazione".equals(fieldSearch)) {
            parametriRicercaEntita.setFieldSearch(FieldSearch.DESCRIZIONE);
            // parametriRicercaEntita.setDescrizione(valueSearch == null ? "" :
            // valueSearch);
            parametriRicercaEntita.setDescrizione(valueSearch);
        }
        parametriRicercaEntita.setTipoEntita(tipoEntita);
        parametriRicercaEntita.setTipiEntitaList(tipiEntitaList);
        List<EntitaLite> entitaResult = anagraficaBD.ricercaEntitaSearchObject(parametriRicercaEntita);

        if (parameters.get(FILTER_LIST_KEY) != null) {
            List<EntitaLite> entitaFiltro = (List<EntitaLite>) parameters.get(FILTER_LIST_KEY);
            entitaResult.removeAll(entitaFiltro);
        }

        if (parameters.get(RETAIN_LIST_KEY) != null) {
            List<EntitaLite> entitaFiltro = (List<EntitaLite>) parameters.get(RETAIN_LIST_KEY);

            List<EntitaLite> entitaRetain = new ArrayList<>();
            for (EntitaLite entita : entitaFiltro) {
                EntitaLite entitaLite = new EntitaLite();
                PanjeaSwingUtil.copyProperties(entitaLite, entita);
                entitaRetain.add(entitaLite);
            }

            // if (!entitaRetain.isEmpty()) {
            entitaResult.retainAll(entitaRetain);
            // }
        }

        return entitaResult;
    }

    /**
     * @return Returns the tipoEntita.
     */
    public TipoEntita getTipoEntita() {
        return tipoEntita;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean isOpenNewObjectManaged() {
        boolean openNew = true;

        if (tipoEntita == null) {
            openNew = false;
            Map<String, Object> parameters = searchPanel.getMapParameters();

            if (parameters.get(TIPOENTITA_KEY) != null) {
                openNew = true;
            }
            // se nel form non ho definito il tipo, ma una lista di tipi e ne ho solo uno, prendo
            // quello come tipo
            // entita
            List<TipoEntita> tipi = (List<TipoEntita>) parameters.get(TIPI_ENTITA_LIST_KEY);
            if (tipi != null && tipi.size() == 1) {
                openNew = true;
            }
        }
        return openNew;
    }

    @Override
    public void openEditor(Object object) {
        Entita entita = null;
        if (object instanceof Entita) {
            entita = (Entita) object;
        }

        if (object instanceof EntitaLite) {
            if (((EntitaLite) object).getId() != null) {
                entita = anagraficaBD.caricaEntita((EntitaLite) object, false);
            } else {
                String className = object.getClass().getName();
                if (className.equals(ClienteLite.class.getName())) {
                    entita = new Cliente();
                } else if (className.equals(FornitoreLite.class.getName())) {
                    entita = new Fornitore();
                } else if (className.equals(VettoreLite.class.getName())) {
                    entita = new Vettore();
                } else if (className.equals(AgenteLite.class.getName())) {
                    entita = new Agente();
                } else {
                    throw new UnsupportedOperationException();
                }
            }
        }

        LifecycleApplicationEvent event = new OpenEditorEvent(entita);
        Application.instance().getApplicationContext().publishEvent(event);
    }

    /**
     * @param anagraficaBD
     *            The anagraficaBD to set.
     */
    public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
        this.anagraficaBD = anagraficaBD;
    }

    /**
     * @param tipoEntita
     *            The tipoEntita to set.
     */
    public void setTipoEntita(TipoEntita tipoEntita) {
        this.tipoEntita = tipoEntita;
    }
}
