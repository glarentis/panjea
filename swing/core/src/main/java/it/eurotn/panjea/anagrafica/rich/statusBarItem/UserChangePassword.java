package it.eurotn.panjea.anagrafica.rich.statusBarItem;

public class UserChangePassword {

	private String oldPassword;

	private String newPassword;

	private String newPasswordConfirm;

	/**
	 * Costruttore.
	 */
	public UserChangePassword() {
		super();
	}

	/**
	 * @return Returns the newPassword.
	 */
	public String getNewPassword() {
		return newPassword;
	}

	/**
	 * @return Returns the newPasswordConfirm.
	 */
	public String getNewPasswordConfirm() {
		return newPasswordConfirm;
	}

	/**
	 * @return Returns the oldPassword.
	 */
	public String getOldPassword() {
		return oldPassword;
	}

	/**
	 * @param newPassword
	 *            The newPassword to set.
	 */
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	/**
	 * @param newPasswordConfirm
	 *            The newPasswordConfirm to set.
	 */
	public void setNewPasswordConfirm(String newPasswordConfirm) {
		this.newPasswordConfirm = newPasswordConfirm;
	}

	/**
	 * @param oldPassword
	 *            The oldPassword to set.
	 */
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
}