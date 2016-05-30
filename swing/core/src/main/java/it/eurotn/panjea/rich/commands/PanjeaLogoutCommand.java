package it.eurotn.panjea.rich.commands;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.security.LogoutCommand;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.WorkspaceView;

/**
 *
 * @author Leonardo
 */
public class PanjeaLogoutCommand extends LogoutCommand {

    private class LogoutCommandInterceptor extends ActionCommandInterceptorAdapter {

        @Override
        public boolean preExecution(ActionCommand command) {
            logoutConfirmed = !confirmationOnLogout;
            if (confirmationOnLogout) {
                new ConfirmationDialog("ATTENZIONE", "Sei sicuro di volerti scollegare da Panjea?") {

                    @Override
                    protected void onConfirm() {
                        logoutConfirmed = true;
                    }
                }.showDialog();
            }

            return logoutConfirmed;
        }
    }

    private boolean isReLoginEnabled = true;

    private boolean logoutConfirmed = false;

    private boolean confirmationOnLogout = true;

    /**
     * Costruttore.
     *
     */
    public PanjeaLogoutCommand() {
        super();
        addCommandInterceptor(new LogoutCommandInterceptor());

    }

    @Override
    protected void doExecuteCommand() {

        // se ci sono editor aperti li chiudo
        WorkspaceView workspaceView = Application.instance().getActiveWindow().getPage().getView("workspaceView");
        workspaceView.closeAllEditors();

        if (workspaceView.getEditorCount() == 0) {
            super.doExecuteCommand();

            // riesegue il login dopo il logout
            if (isReLoginEnabled()) {
                ActionCommand login = RcpSupport.getCommand("loginCommand");
                login.execute();
            }
        }
    }

    /**
     * @return Returns the isReLoginEnabled.
     */
    public boolean isReLoginEnabled() {
        return isReLoginEnabled;
    }

    /**
     * @param confirmationOnLogout
     *            the confirmationOnLogout to set
     */
    public void setConfirmationOnLogout(boolean confirmationOnLogout) {
        this.confirmationOnLogout = confirmationOnLogout;
    }

    /**
     * @param reLoginEnabled
     *            The isReLoginEnabled to set.
     */
    public void setReLoginEnabled(boolean reLoginEnabled) {
        this.isReLoginEnabled = reLoginEnabled;
    }
}
