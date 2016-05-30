package it.eurotn.panjea.sicurezza.license;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import it.eurotn.panjea.settings.AziendaSettings;
import it.eurotn.panjea.settings.SettingsServerMBean;
import it.eurotn.panjea.sicurezza.license.exception.InvalidLicenseException;
import it.eurotn.security.JecPrincipal;

public class PanjeaLicenseInterceptor {

    @EJB
    private SettingsServerMBean settingsServer;

    @Resource
    private SessionContext sessionContext;

    /**
     * Eseguito al controllo della licenza.
     *
     * @param ctx
     *            context
     * @return .
     * @throws Exception
     *             .
     */
    @AroundInvoke
    public Object execute(InvocationContext ctx) throws Exception {

        String methodName = ctx.getMethod().getName();
        if (methodName.contains("ricerca")) {
            AziendaSettings aziendaSettings = settingsServer.getAziendaSettings(getAzienda());
            if (!aziendaSettings.isLicenseValid()) {
                throw new InvalidLicenseException(aziendaSettings.getDataScadenza());
            }
        }

        return ctx.proceed();
    }

    /**
     *
     * @return codice dell'azienda loggata.
     */
    private String getAzienda() {
        return ((JecPrincipal) sessionContext.getCallerPrincipal()).getCodiceAzienda();
    }
}
