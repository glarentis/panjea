/**
 *
 */
package it.eurotn.panjea.anagrafica.rich.statusBarItem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.image.IconSource;
import org.springframework.richclient.security.ApplicationSecurityManager;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.security.Authentication;
import org.springframework.security.BadCredentialsException;

import com.jidesoft.status.ButtonStatusBarItem;

import it.eurotn.panjea.rich.bd.ISicurezzaBD;
import it.eurotn.panjea.rich.bd.SicurezzaBD;
import it.eurotn.panjea.rich.commands.PanjeaLogoutCommand;
import it.eurotn.panjea.sicurezza.JecPrincipalSpring;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;
import it.eurotn.security.JecPrincipal;

/**
 * Oggetto della status bar che visualizza un pulsante con icona e nome dell'utente loggato. Premendolo verr�
 * visualizzata la finestra del logout.
 *
 * @author fattazzo
 * @version 1.0, 15/nov/07
 *
 */
public final class UserStatusBarItem extends ButtonStatusBarItem implements ActionListener {

    private class UserStatusBarMouseListener extends MouseAdapter {

        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {

            if (e.getButton() == java.awt.event.MouseEvent.BUTTON3) {
                PanjeaTitledPageApplicationDialog dialog = new PanjeaTitledPageApplicationDialog(
                        new UserStatusChangePasswordPage()) {

                    @Override
                    protected boolean onFinish() {

                        JecPrincipalSpring utente = PanjeaSwingUtil.getUtenteCorrente();

                        String newPassword = ((UserStatusChangePasswordPage) getDialogPage()).getUserNewPassword();
                        String oldPassword = ((UserStatusChangePasswordPage) getDialogPage()).getUserOldPassword();
                        try {
                            sicurezzaBD.cambiaPasswordUtenteLoggato(oldPassword, newPassword);

                            Authentication jecPrincipal = new JecPrincipalSpring(
                                    utente.getUserName() + "#" + utente.getCodiceAzienda() + "#" + utente.getLingua());
                            ((JecPrincipal) jecPrincipal).setCredentials(newPassword);
                            ApplicationSecurityManager sm = (ApplicationSecurityManager) getService(
                                    ApplicationSecurityManager.class);
                            sm.doLogin(jecPrincipal);
                        } catch (Exception e) {
                            throw new BadCredentialsException("Errore di autenticazione");
                        }

                        // eseguo il logout
                        // UserStatusBarItem.this.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
                        // ""));
                        PanjeaLogoutCommand logoutCommand = new PanjeaLogoutCommand();
                        logoutCommand.setConfirmationOnLogout(false);
                        logoutCommand.execute();
                        return true;
                    }
                };
                dialog.showDialog();
            }
        }

    }

    private static final long serialVersionUID = 2020407289792809258L;

    private static final String LOGOUT_COMMAND_ID = "logoutCommand";

    private ISicurezzaBD sicurezzaBD;

    /**
     * costruttore.
     */
    private UserStatusBarItem() {
        super();

        sicurezzaBD = RcpSupport.getBean(SicurezzaBD.BEAN_ID);

        IconSource iconSource = (IconSource) ApplicationServicesLocator.services().getService(IconSource.class);
        setIcon(iconSource.getIcon("user.image"));

        MessageSource ms = (MessageSource) ApplicationServicesLocator.services().getService(MessageSource.class);
        setToolTip(ms.getMessage("user.statusBarItem.tooltip.text", null, Locale.getDefault()));
        addActionListener(this);
        getComponent().addMouseListener(new UserStatusBarMouseListener());
        getComponent().setName(LOGOUT_COMMAND_ID);
    }

    @Override
    public void actionPerformed(ActionEvent actionevent) {
        ActionCommand logoutCommand = (ActionCommand) Application.instance().getActiveWindow().getCommandManager()
                .getCommand(LOGOUT_COMMAND_ID);
        logoutCommand.execute();

    }
}
