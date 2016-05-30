/**
 * 
 */
package it.eurotn.panjea.conai.rich.editor.rigaarticolo;

import it.eurotn.panjea.conai.domain.RigaConaiComponente;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.TableBinding;
import it.eurotn.rich.form.PanjeaAbstractForm;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

/**
 * @author leonardo
 * 
 */
public class RigheConaiComponenteForm extends PanjeaAbstractForm {

	private class RigheConaiComponenteReadOnlyChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (righeConaiComponenteTableModel != null && evt.getNewValue() != null) {
				righeConaiComponenteTableModel.setReadOnly((Boolean) evt.getNewValue());
			}
		}

	}

	public static final String FORM_ID = "righeConaiComponenteForm";
	private TableBinding<RigaConaiComponente> righeConaiComponenteBinding = null;
	private RigheConaiComponenteTableModel righeConaiComponenteTableModel = null;
	private RigheConaiComponenteReadOnlyChangeListener readOnlyChangeListener = null;

	/**
	 * Costruttore.
	 * 
	 * @param formModel
	 *            form model
	 */
	public RigheConaiComponenteForm(final FormModel formModel) {
		super(formModel, FORM_ID);
		formModel.addPropertyChangeListener(FormModel.READONLY_PROPERTY,
				getRigheConaiComponenteReadOnlyChangeListener());
	}

	@SuppressWarnings("unchecked")
	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("left:320dlu,10dlu,left:default:grow", "10dlu, default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel()
		builder.setLabelAttributes("l, t");

		// builder.nextRow();
		// builder.addLabel("righeConaiComponente", 1);
		builder.nextRow();

		righeConaiComponenteTableModel = new RigheConaiComponenteTableModel(getFormModel().getValueModel(
				"righeConaiComponente"));

		righeConaiComponenteBinding = (TableBinding<RigaConaiComponente>) bf.createTableBinding("righeConaiComponente",
				320, righeConaiComponenteTableModel, null);
		builder.addBinding(righeConaiComponenteBinding, 1, 4);
		builder.nextRow();

		return builder.getPanel();
	}

	@Override
	public void dispose() {
		getFormModel().removePropertyChangeListener(getRigheConaiComponenteReadOnlyChangeListener());
		super.dispose();
	}

	/**
	 * @return RigheConaiComponenteReadOnlyChangeListener
	 */
	public RigheConaiComponenteReadOnlyChangeListener getRigheConaiComponenteReadOnlyChangeListener() {
		if (readOnlyChangeListener == null) {
			readOnlyChangeListener = new RigheConaiComponenteReadOnlyChangeListener();
		}
		return readOnlyChangeListener;
	}

}
