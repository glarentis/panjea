package it.eurotn.panjea.anagrafica.rich.statusBarItem;

import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;

import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class UserStatusChangePasswordForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "userStatusChangePasswordForm";

	/**
	 * Costruttore.
	 * 
	 */
	public UserStatusChangePasswordForm() {
		super(PanjeaFormModelHelper.createFormModel(new UserChangePassword(), false, FORM_ID));
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:pref,4dlu,fill:120dlu", "10dlu,default,4dlu,default,4dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel());
		builder.setLabelAttributes("r, c");

		builder.addPasswordFieldAndLabel("oldPassword", 1, 2);

		builder.addPasswordFieldAndLabel("newPassword", 1, 4);

		builder.addPasswordFieldAndLabel("newPasswordConfirm", 1, 6);

		return builder.getPanel();
	}

}
