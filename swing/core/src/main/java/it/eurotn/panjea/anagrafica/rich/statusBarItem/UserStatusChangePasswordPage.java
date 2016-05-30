package it.eurotn.panjea.anagrafica.rich.statusBarItem;

import org.springframework.richclient.dialog.FormBackedDialogPage;

public class UserStatusChangePasswordPage extends FormBackedDialogPage {

	public static final String PAGE_ID = "userStatusChangePasswordPage";

	/**
	 * Costruttore.
	 */
	protected UserStatusChangePasswordPage() {
		super(PAGE_ID, new UserStatusChangePasswordForm());
	}

	/**
	 * Restituisce la nuova password impostata per l'utente.
	 * 
	 * @return new password
	 */
	public String getUserNewPassword() {
		return ((UserChangePassword) getBackingFormPage().getFormObject()).getNewPassword();
	}

	/**
	 * Restituisce la nuova password impostata per l'utente.
	 * 
	 * @return old password
	 */
	public String getUserOldPassword() {
		return ((UserChangePassword) getBackingFormPage().getFormObject()).getOldPassword();
	}
}
