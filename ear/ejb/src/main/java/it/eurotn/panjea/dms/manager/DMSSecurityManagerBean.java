package it.eurotn.panjea.dms.manager;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import com.logicaldoc.webservice.auth.AuthClient;
import com.logicaldoc.webservice.security.WSUser;

import it.eurotn.panjea.dms.exception.DMSLoginException;
import it.eurotn.panjea.dms.exception.DmsException;
import it.eurotn.panjea.dms.manager.interfaces.DMSLookupClientWebService;
import it.eurotn.panjea.dms.manager.interfaces.DMSSecurityManager;
import it.eurotn.panjea.dms.manager.interfaces.DMSSettingsManager;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.sicurezza.domain.Utente;
import it.eurotn.panjea.sicurezza.service.interfaces.SicurezzaService;
import it.eurotn.security.JecPrincipal;

@Stateless(mappedName = "Panjea.DMSSecurityManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.DMSSecurityManager")
public class DMSSecurityManagerBean implements DMSSecurityManager {
    private static final Logger LOGGER = Logger.getLogger(DMSSecurityManagerBean.class);

    @Resource
    private SessionContext context;

    @EJB
    private SicurezzaService sicurezzaService;

    @EJB
    private DMSSettingsManager dmsSettingsManager;

    @EJB
    private DMSLookupClientWebService lookupService;

    /**
     * Crea un nuovo utente in logicaldoc dall'utente panjea.
     *
     * @param utente
     *            utente panjea
     * @throws Exception
     *             .
     */
    private void createUser(String sid, Utente utente) {

        WSUser user = new WSUser();
        user.setEnabled(1);
        user.setFirstName(ObjectUtils.defaultIfNull(utente.getNome(), "NI"));
        user.setLanguage("it");
        user.setName(utente.getCognome());
        String password = utente.getPassword().length() >= 8 ? utente.getPassword()
                : StringUtils.rightPad(utente.getPassword(), 8, " ");
        user.setPasswordmd4(password);
        user.setUserName(utente.getUserName());
        user.setTelephone2(utente.getCellulare());
        user.setName(utente.getUserName());
        user.setGroupIds(new long[] { -10000L }); // -10000 è il gruppo publisher usato per i nuovi
                                                  // utenti

        String email = utente.getUserName() + "@" + utente.getUserName() + ".it";
        if (utente.getDatiMailPredefiniti().isValid()) {
            email = utente.getDatiMailPredefiniti().getEmail();
        }
        user.setEmail(email);

        try {
            lookupService.creaPanjeaService().storeUser(sid, user);
        } catch (Exception e) {
            LOGGER.error("-->errore nel craere l'utente nel dms", e);
            throw new DmsException("-->errore nel craere l'utente nel dms", e);
        }
    }

    private String getUserName() {
        return ((JecPrincipal) context.getCallerPrincipal()).getUserName();
    }

    @Override
    public String login() throws DMSLoginException {
        LOGGER.debug("--> Enter login");
        String sid = "";
        try {
            Utente utente = null;
            if ("internalAdmin".equalsIgnoreCase(getUserName())) {
                utente = new Utente();
                utente.setUserName("update");
                utente.setPassword("updateeu");
                utente.setCognome("update");
            } else {
                utente = sicurezzaService.caricaUtente(getUserName());
            }

            AuthClient auth = lookupService.creaAuthClient();
            String sidAdmin = auth.login("admin", "pnj_adm_");

            WSUser user = userExist(sidAdmin, utente.getUserName());

            // se l'utente non esiste lo creo
            if (user == null) {
                createUser(sidAdmin, utente);
            }

            auth.logout(sidAdmin);

            String userPwd = utente.getPassword().length() >= 8 ? utente.getPassword()
                    : StringUtils.rightPad(utente.getPassword(), 8, " ");
            sid = auth.login(utente.getUserName(), userPwd);
            if (!auth.valid(sid)) {
                throw new GenericException("Utente non valido per la gestione documentale.");
            }
        } catch (Exception e) {
            LOGGER.error("-->errore login logicaldoc", e);
            throw new DMSLoginException("Errore durante il login dell'utente per la gestione documentale.", e);
        }
        return sid;
    }

    @Override
    public void logout(String sid) {
        lookupService.creaAuthClient().logout(sid);
    }

    /**
     * Verifica se esiste l'utente in logicaldoc.
     *
     * @param sid
     *            sid
     * @param username
     *            username
     * @return utente di logicaldoc, <code>null</code> se non esiste
     * @throws Exception
     *             .
     */
    private WSUser userExist(String sid, String username) throws Exception {
        WSUser user = lookupService.creaSecurityClient().getUserByUsername(sid, username);
        return user;
    }

}
