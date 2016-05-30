package it.eurotn.panjea.sicurezza;

import org.apache.log4j.Logger;
import org.springframework.security.Authentication;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;

import it.eurotn.security.JecPrincipal;

public class JecPrincipalSpring extends JecPrincipal implements Authentication {

    private static final Logger LOGGER = Logger.getLogger(JecPrincipalSpring.class);

    private static final long serialVersionUID = 2938317652767009792L;
    private GrantedAuthority[] authorities;

    /**
     * @param name
     * @param principal
     */
    public JecPrincipalSpring(JecPrincipal principal) {
        super(principal.getName());
        setPermissions(principal.getPermissions());
    }

    public JecPrincipalSpring(String name) {
        super(name);
    }

    @Override
    public GrantedAuthority[] getAuthorities() {
        if (authorities == null && getPermissions() != null) {
            authorities = new GrantedAuthority[getPermissions().length];
            for (int i = 0; i < getPermissions().length; i++) {
                try {
                    authorities[i] = new GrantedAuthorityImpl(getPermissions()[i]);
                } catch (final Exception ex) {
                    LOGGER.error("", ex);
                }
            }
        }
        return authorities;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        throw new UnsupportedOperationException("autenticato tramite jaas");
    }

}
