/**
 * 
 */
package it.eurotn.panjea.plugin;

import it.eurotn.panjea.rules.AbstractPluginRulesSource;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.rules.Rules;

/**
 * Restituisce tutte le informazioni relative al plugin La classe viene mappata nel context di spring tramite il task
 * ant Merge.
 * 
 * @author fattazzo
 * @version 1.0, 07/mag/07
 * 
 */
public class Plugin {

    private static Logger logger = Logger.getLogger(Plugin.class);

    private String versione = "";
    private String descrizione = "";
    private String nome = "";
    private String rulesSource = "";

    /**
     * Costruttore.
     * 
     */
    public Plugin() {
        super();

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Plugin other = (Plugin) obj;
        if (nome == null) {
            if (other.nome != null) {
                return false;
            }
        } else if (!nome.equals(other.nome)) {
            return false;
        }
        return true;
    }

    /**
     * @return Returns the descrizione.
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * @return Returns the nome.
     */
    public String getNome() {
        return nome;
    }

    /**
     * 
     * @return rules legate al plugin
     */
    public List<Rules> getRules() {
        List<Rules> listRules = new ArrayList<Rules>();

        if (this.rulesSource.equals("")) {
            return listRules;
        }

        AbstractPluginRulesSource pluginRulesSource;
        try {
            pluginRulesSource = (AbstractPluginRulesSource) Class.forName(this.rulesSource).newInstance();
        } catch (Exception e) {
            logger.error("--> Errore durante il tentativo di istanziare la classe " + this.rulesSource, e);
            throw new RuntimeException("Errore durante il tentativo di istanziare la classe " + this.rulesSource);
        }

        return pluginRulesSource.getRules();
    }

    /**
     * @return Returns the rulesSource.
     */
    public String getRulesSource() {
        return rulesSource;
    }

    /**
     * @return Returns the versione.
     */
    public String getVersione() {
        return versione;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((nome == null) ? 0 : nome.hashCode());
        return result;
    }

    /**
     * @param descrizione
     *            The descrizione to set.
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * @param nome
     *            The nome to set.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @param rulesSource
     *            The rulesSource to set.
     */
    public void setRulesSource(String rulesSource) {
        this.rulesSource = rulesSource;
    }

    /**
     * @param versione
     *            The versione to set.
     */
    public void setVersione(String versione) {
        this.versione = versione;
    }

}
