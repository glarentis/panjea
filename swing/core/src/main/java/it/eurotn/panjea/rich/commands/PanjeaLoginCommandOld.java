package it.eurotn.panjea.rich.commands;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.dialog.ApplicationDialog;
import org.springframework.richclient.dialog.FormBackedDialogPage;
import org.springframework.richclient.dialog.TitledPageApplicationDialog;
import org.springframework.richclient.security.ApplicationSecurityManager;
import org.springframework.richclient.security.LoginCommand;
import org.springframework.richclient.security.LoginForm;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.security.SpringSecurityException;

import it.eurotn.panjea.rich.forms.PanjeaLoginForm;

public class PanjeaLoginCommandOld extends LoginCommand {

    /**
     * Costruttore.
     *
     */
    public PanjeaLoginCommandOld() {
        setDisplaySuccess(false);
        setClearPasswordOnFailure(true);
        setCloseOnCancel(true);

    }

    @Override
    protected LoginForm createLoginForm() {
        return new PanjeaLoginForm();
    }

    @Override
    protected void doExecuteCommand() {
        final PanjeaLoginForm loginForm = (PanjeaLoginForm) createLoginForm();
        final FormBackedDialogPage page = new FormBackedDialogPage(loginForm);

        if (getDefaultUserName() != null) {
            loginForm.setUserName(getDefaultUserName());
        }

        ApplicationDialog dialog = new TitledPageApplicationDialog(page) {

            @Override
            protected ActionCommand getCallingCommand() {
                return PanjeaLoginCommandOld.this;
            }

            @Override
            protected String getTitle() {
                return RcpSupport.getMessage("LoginDialog.title");
            }

            @Override
            protected void onAboutToShow() {
                loginForm.requestFocusInWindow();
            }

            @Override
            protected void onCancel() {
                super.onCancel();
                if (isCloseOnCancel()) {
                    ApplicationSecurityManager sm = (ApplicationSecurityManager) getService(
                            ApplicationSecurityManager.class);
                    org.springframework.security.Authentication authentication = sm.getAuthentication();
                    if (authentication == null) {
                        getApplication().close();
                    }
                }
            }

            @Override
            protected boolean onFinish() {
                // disattivo il command per evitare che venga eseguito più volte il login (conferma
                // premuto più volte)
                getFinishCommand().setEnabled(false);

                org.springframework.security.Authentication authentication;
                ApplicationSecurityManager sm;
                loginForm.commit();
                authentication = loginForm.getAuthentication();
                sm = (ApplicationSecurityManager) getService(ApplicationSecurityManager.class);
                try {
                    sm.doLogin(authentication);
                    postLogin();
                    loginForm.saveSettings();
                } catch (SpringSecurityException e) {
                    if (isClearPasswordOnFailure()) {
                        loginForm.setPassword("");
                    }
                    loginForm.requestFocusInWindow();
                    throw e;
                }

                return true;
            }
        };
        dialog.setDisplayFinishSuccessMessage(false);
        dialog.showDialog();
    }
}
