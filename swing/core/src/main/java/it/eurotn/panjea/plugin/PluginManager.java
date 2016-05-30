package it.eurotn.panjea.plugin;

import java.util.ArrayList;
import java.util.List;

import org.springframework.rules.Rules;

/**
 * Gestisce tutte le informazioni riguardanti i plugin. La versione generale di Panjea viene recuperata dal plugin
 * panjea-core
 *
 * @author fattazzo
 * @version 1.0, 07/mag/07
 */
public class PluginManager {

    public static final String PLUGIN_PANJEA_CORE_NAME = "panjea-core";
    public static final String PLUGIN_AGENTI = "panjeaAgenti";
    public static final String PLUGIN_LOTTI = "panjeaLotti";
    public static final String PLUGIN_CONAI = "panjeaConai";
    public static final String PLUGIN_CENTRO_COSTI = "panjeaCentriCosto";
    public static final String PLUGIN_RATEI_RISCONTI = "panjeaRateiRisconti";
    public static final String PLUGIN_CAUZIONI = "panjeaCauzioni";
    public static final String PLUGIN_PAGAMENTI = "panjeaPagamenti";
    public static final String PLUGIN_INTRA = "panjeaIntra";
    public static final String PLUGIN_CONTABILITA = "panjeaContabilita";
    public static final String PLUGIN_MAGAZZINO = "panjeaMagazzino";
    public static final String PLUGIN_FATTURAZIONE_PA = "panjeaFatturePA";
    public static final String PLUGIN_MANUTENZIONI = "panjeaManutenzioni";
    public static final String PLUGIN_VENDING = "panjeaVending";
    public static final String PLUGIN_CORRISPETTIVI = "panjeaCorrispettivi";
    public static final String PLUGIN_COMUNICAZIONE_POLIVALENTE = "panjeaComunicazionePolivalente";

    public static final String BEAN_ID = "pluginManager";
    private List<Plugin> plugins = null;

    /**
     * Costruttore.
     *
     */
    public PluginManager() {
        super();
    }

    /**
     * Costruisce il pluginManager e controlla che sia presente panjea-core.
     *
     * @param plugins
     *            plugins
     */
    public PluginManager(final List<Plugin> plugins) {
        super();
        this.plugins = plugins;
    }

    /**
     * @return Returns the plugins.
     */
    public List<Plugin> getPlugins() {
        return plugins;
    }

    /**
     * Restituisce una lista di tutte le rulesSource dei plugins caricati.
     *
     * @return lista di rules configurate
     */
    public List<Rules> getRules() {
        List<Rules> listRules = new ArrayList<Rules>();

        for (Plugin plugin : this.plugins) {
            listRules.addAll(plugin.getRules());
        }

        return listRules;
    }

    /**
     * Restituisce la versione di Panjea.
     *
     * @return versione di panjea (contenuta nel modulo panjea-core)
     */
    public String getVersione() {
        for (Plugin plugin : plugins) {
            if (PLUGIN_PANJEA_CORE_NAME.equals(plugin.getNome())) {
                return plugin.getVersione();
            }
        }
        throw new RuntimeException("Core plugin non presente");
    }

    /**
     * Verifica se il plugin è presente.
     *
     * @param nome
     *            nome del plugin
     * @return <code>true</code> se presente
     */
    public boolean isPresente(String nome) {
        Plugin pluginSearch = new Plugin();
        pluginSearch.setNome(nome);
        return plugins.contains(pluginSearch);
    }

    /**
     * @param plugins
     *            The plugins to set.
     */
    public void setPlugins(List<Plugin> plugins) {
        this.plugins = plugins;
    }
}
