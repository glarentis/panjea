package it.eurotn.panjea.sicurezza;

import java.io.IOException;
import java.security.Security;

import javax.ejb.EJBAccessException;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;

import org.apache.log4j.Logger;
import org.jboss.security.auth.callback.UsernamePasswordHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.security.Authentication;
import org.springframework.security.BadCredentialsException;
import org.springframework.security.SpringSecurityException;
import org.springframework.security.providers.AuthenticationProvider;
import org.springframework.util.Assert;

import it.eurotn.panjea.rich.bd.ISicurezzaBD;
import it.eurotn.security.JecPrincipal;

/**
 * Personalizzazione di <code>JaasAuthenticationProvider</code> di acegi finalizzata ad effetturare
 * l'autenticazione del <code>Principal</code> ed ad ottenere i suoi dettagli.
 *
 * In dettaglio l'autenticazione avviene, dopo aver effettuato il login di JAAS attraverso il
 * LoginContext mediante una chiamata verso il metodo login di <code> ILoginDelegate</code> il quale
 * scatena l'effettiva autenticazione e restituisce un istanza di <code>JecPrincipal</code>. Tale
 * istanza viene poi restituita come risultato del method <code>autenticate</code>
 *
 *
 * @author Adry
 * @version 1.0, 8-mar-2006
 * @see IClientLogin
 * @see it.eurotn.security.JecPrincipal
 *
 *
 */

public class JecAuthenticationProvider implements AuthenticationProvider, InitializingBean, ApplicationContextAware {

    private static Logger logger = Logger.getLogger(JecAuthenticationProvider.class);

    private ApplicationContext context;
    private ISicurezzaBD loginBD;
    private Resource loginConfig;
    private String loginContextName = "ACEGI";

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(loginConfig, "loginConfig must be set on " + getClass());
        Assert.notNull(loginBD, "loginbd nullo");
        Assert.hasLength(loginContextName, "loginContextName must be set on " + getClass());
        configureJaas(loginConfig);
        Assert.notNull(Configuration.getConfiguration(),
                "As per http://java.sun.com/j2se/1.5.0/docs/api/javax/security/auth/login/Configuration.html \"If a Configuration object was set via the Configuration.setConfiguration method, then that object is returned. Otherwise, a default Configuration object is returned\". Your JRE returned null to Configuration.getConfiguration().");
    }

    @Override
    public Authentication authenticate(Authentication auth) {
        logger.debug("--> Enter authenticate");
        if (auth instanceof JecPrincipal) {
            JecPrincipal request = (JecPrincipal) auth;
            logger.debug("--> autentico il jecPrincipal " + request);
            try {
                String username = request.getName();
                UsernamePasswordHandler handler = new UsernamePasswordHandler(username, request.getCredentials());
                LoginContext loginContext = new LoginContext(loginContextName, handler);
                loginContext.login();
                Authentication jecPrincipal = loginBD.login();

                logger.debug("-->jecPrincipal " + jecPrincipal);
                return jecPrincipal;
            } catch (Exception e) {
                if (e.getCause() instanceof EJBAccessException) {
                    SpringSecurityException ex = new BadCredentialsException("Dati di login non corretti");
                    throw ex;
                }
            }
        }
        return null;
    }

    /**
     * Hook method for configuring Jaas
     *
     * @param loginConfigStr
     *            URL to Jaas login configuration
     */
    protected void configureJaas(Resource paramLoginConfig) throws IOException {
        String loginConfigUrl = loginConfig.getURL().toString();
        boolean alreadySet = false;

        int n = 1;
        String prefix = "login.config.url.";
        String existing = null;

        while ((existing = Security.getProperty(prefix + n)) != null) {
            alreadySet = existing.equals(loginConfigUrl);

            if (alreadySet) {
                break;
            }

            n++;
        }

        if (!alreadySet) {
            String key = prefix + n;
            logger.debug("Setting security property [" + key + "] to: " + loginConfigUrl);
            Security.setProperty(key, loginConfigUrl);
        }
    }

    /**
     * @return the context
     */
    public ApplicationContext getApplicationContext() {
        return context;
    }

    /**
     * @return the loginConfig
     */
    public Resource getLoginConfig() {
        return loginConfig;
    }

    /**
     * @return the loginContextName
     */
    public String getLoginContextName() {
        return loginContextName;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.context = applicationContext;
    }

    /**
     * @param loginBD
     *            The loginBD to set.
     */
    public void setLoginBD(ISicurezzaBD loginBD) {
        this.loginBD = loginBD;
    }

    /**
     * @param loginConfig
     *            the loginConfig to set
     */
    public void setLoginConfig(Resource loginConfig) {
        this.loginConfig = loginConfig;
    }

    /**
     * @param loginContextName
     *            the loginContextName to set
     */
    public void setLoginContextName(String loginContextName) {
        this.loginContextName = loginContextName;
    }

    @Override
    public boolean supports(@SuppressWarnings("rawtypes") Class aClass) {
        return aClass.getName().equals(JecPrincipalSpring.class.getName());
    }

}
