/*
 * Copyright 2002-2006 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package it.eurotn.panjea.rich.rules;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.rules.Rules;
import org.springframework.rules.constraint.Constraint;

import it.eurotn.panjea.dms.domain.DmsSettings;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.panjea.rich.editors.stampe.nuovo.LayoutStampaPM;
import it.eurotn.panjea.rules.AbstractPluginRulesSource;
import it.eurotn.panjea.sicurezza.domain.DatiMail;
import it.eurotn.panjea.sicurezza.domain.Ruolo;
import it.eurotn.panjea.sicurezza.domain.Utente;
import it.eurotn.panjea.sicurezza.rich.pm.UtentePM;

/**
 * This class is a source for validation rules associated with the domain objects in this application. This clas is
 * wired into application via the application context configuration like this:
 *
 * <pre>
 *    &lt;bean id=&quot;rulesSource&quot;
 *        class=&quot;org.springframework.richclient.samples.simple.domain.SimpleValidationRulesSource&quot;/&gt;
 * </pre>
 *
 * With this configuration, validating forms will interrogate the rules source for rules that apply to the class of a
 * form object (in this case, that's objects of type {@link Contact}.
 *
 * @author Larry Streepy
 */
public class ValidationRulesSource extends AbstractPluginRulesSource implements InitializingBean {

    private final Constraint requiredConstraint = all(new Constraint[] { required() });

    private PluginManager pluginManager;

    /**
     * Construct the rules source. Just add all the rules for each class that will be validated.
     */
    public ValidationRulesSource() {
        super();

        // ruolo e utente
        addRules(createRuoloRules());
        addRules(createUtenteRules());

        addRules(createLayoutStampaPMRules());

        addRules(createDMSSettingsRules());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (pluginManager != null) {
            for (Rules rules : pluginManager.getRules()) {
                this.addRules(rules);
            }
        }

    }

    /**
     * Regole per il DMSSettings.
     *
     * @return Rules
     */
    private Rules createDMSSettingsRules() {
        return new Rules(DmsSettings.class) {
            @Override
            protected void initRules() {
                add("serviceUrl", requiredConstraint);
                add("altroFolder", requiredConstraint);
                add("emailFolderPattern", requiredConstraint);
                add("articoliFolderPattern", requiredConstraint);
                add("entitaFolderPattern", requiredConstraint);
            }
        };
    }

    /**
     * Regole per il LayoutStampaPM.
     *
     * @return Rules
     */
    private Rules createLayoutStampaPMRules() {
        return new Rules(LayoutStampaPM.class) {
            @Override
            protected void initRules() {
                add("classeTipoDocumentoInstance", requiredConstraint);
                add("tipoAreaDocumento", requiredConstraint);
                add("reportName", requiredConstraint);
            }
        };
    }

    /**
     * Regole per il ruolo.
     *
     * @return Rules
     */
    private Rules createRuoloRules() {
        return new Rules(Ruolo.class) {
            @Override
            protected void initRules() {
                add(Ruolo.PROP_DESCRIZIONE, requiredConstraint);
                add(Ruolo.PROP_CODICE, requiredConstraint);
            }
        };
    }

    /**
     * Regole per il ruolo.
     *
     * @return Rules
     */
    private Rules createUtenteRules() {
        return new Rules(UtentePM.class) {
            @Override
            protected void initRules() {
                add("utente." + Utente.PROP_USER_NAME, requiredConstraint);
                add("utente." + Utente.PROP_PASSWORD, requiredConstraint);
                add("utente." + Utente.PROP_DESCRIZIONE, requiredConstraint);
                add(eqProperty("confermaPassword", "utente." + Utente.PROP_PASSWORD));
                add(eqProperty("confermaPasswordMail", "utente.datiMail.passwordMail"));
                add(eqProperty("confermaPasswordBugTracker", "utente.datiBugTracker.password"));
                add(eqProperty("confermaPasswordJasperServer", "utente.datiJasperServer.password"));

                add("utente.datiMail." + DatiMail.PROP_EMAIL, getEmailConstraint());
                add("utente.datiMail.server", maxLength(30));
                add("utente.datiMail.email", maxLength(60));
                add("utente.datiMail.utenteMail", maxLength(60));
                add("utente.datiMail.passwordMail", maxLength(30));

                add("utente.datiMail.emailDiRisposta", getEmailConstraint());

                add("utente.datiBugTracker.username", maxLength(60));
                add("utente.datiBugTracker.password", maxLength(30));

            }
        };
    }

    @Override
    public List<Rules> getRules() {
        // non faccio nulla dato che sono nella classe che si proccupa di unire le rules di tutti i plugins
        return null;
    }

    /**
     * @param pluginManager
     *            The pluginManager to set.
     */
    public void setPluginManager(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

}