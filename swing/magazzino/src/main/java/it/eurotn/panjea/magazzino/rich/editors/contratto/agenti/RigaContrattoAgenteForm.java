package it.eurotn.panjea.magazzino.rich.editors.contratto.agenti;

import it.eurotn.panjea.magazzino.domain.RigaContratto;
import it.eurotn.panjea.magazzino.domain.RigaContrattoAgente;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.springframework.binding.form.FormModel;
import org.springframework.binding.form.HierarchicalFormModel;
import org.springframework.binding.form.ValidatingFormModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.form.FormModelHelper;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.plaf.UIDefaultsLookup;
import com.jidesoft.swing.PartialGradientLineBorder;
import com.jidesoft.swing.PartialSide;

public class RigaContrattoAgenteForm extends PanjeaAbstractForm {

	private class RimuoviRigaContrattoAgenteCommand extends ApplicationWindowAwareCommand {

		public static final String COMMAND_ID = "rimuoviRigaContrattoAgenteCommand";

		/**
		 * Costruttore.
		 */
		public RimuoviRigaContrattoAgenteCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {

			List<RigaContrattoAgente> righeAgenti = ((RigaContratto) parentFormModel.getFormObject())
					.getRigheContrattoAgente();

			List<RigaContrattoAgente> newRighe = new ArrayList<RigaContrattoAgente>();
			newRighe.addAll(righeAgenti);
			newRighe.remove(getFormObject());
			parentFormModel.getValueModel("righeContrattoAgente").setValue(newRighe);
			((ValidatingFormModel) parentFormModel).validate();
		}

	}

	public static final String FORM_ID = "rigaContrattoAgenteForm";

	private JLabel agenteLabel = new JLabel();

	private FormModel parentFormModel;

	private RimuoviRigaContrattoAgenteCommand rimuoviRigaContrattoAgenteCommand;

	/**
	 * Costruttore.
	 * 
	 * @param parentFormModel
	 *            parent form model
	 */
	public RigaContrattoAgenteForm(final FormModel parentFormModel) {
		super(FormModelHelper.createChildPageFormModel((HierarchicalFormModel) parentFormModel, FORM_ID + "Model",
				new ValueHolder(new RigaContrattoAgente())), FORM_ID);
		this.parentFormModel = parentFormModel;
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("left:pref,4dlu,fill:60dlu,30dlu,left:pref,4dlu,left:pref",
				"2dlu,default,10dlu,default,2dlu,default,2dlu,default,2dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel());
		builder.setLabelAttributes("r, c");

		builder.nextRow();
		builder.setRow(2);

		builder.setComponentAttributes("l,c");
		builder.addComponent(getRimuoviRigaContrattoAgenteCommand().createButton(), 1, 2, 7, 1);
		builder.setComponentAttributes("f,f");

		builder.addLabel("agente", 1, 4);
		builder.addComponent(agenteLabel, 3, 4, 5, 1);

		builder.addPropertyAndLabel("azione", 1, 6);

		builder.addPropertyAndLabel("blocco", 1, 8);
		builder.addPropertyAndLabel("ignoraBloccoPrecedente", 5, 8);

		builder.addPropertyAndLabel("valoreProvvigione", 1, 10);

		addFormObjectChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				String agenteDesc = null;
				if (((RigaContrattoAgente) getFormObject()).getAgente() != null) {
					agenteDesc = ((RigaContrattoAgente) getFormObject()).getAgente().getAnagrafica().getDenominazione();
				} else {
					agenteDesc = "TUTTI";
				}

				agenteLabel.setText(agenteDesc);
			}
		});

		getFormModel().addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				getRimuoviRigaContrattoAgenteCommand().setEnabled(!getFormModel().isReadOnly());

				if (FormModel.COMMITTABLE_PROPERTY.equals(evt.getPropertyName())
						&& ((Boolean) evt.getNewValue()) == Boolean.TRUE) {
					List<RigaContrattoAgente> righeAgenti = ((RigaContratto) parentFormModel.getFormObject())
							.getRigheContrattoAgente();
					getFormModel().commit();
					// NPE mail
					if (righeAgenti != null) {
						righeAgenti.set(righeAgenti.indexOf(getFormObject()), (RigaContrattoAgente) getFormObject());
						parentFormModel.getValueModel("righeContrattoAgente").setValue(righeAgenti);
					}
					((ValidatingFormModel) parentFormModel).validate();
				}
			}
		});

		JPanel panel = builder.getPanel();
		panel.setBorder(BorderFactory.createCompoundBorder(new PartialGradientLineBorder(new Color[] {
				new Color(0, 0, 128), UIDefaultsLookup.getColor("control") }, 2, PartialSide.NORTH),
				BorderFactory.createEmptyBorder(6, 0, 4, 4)));

		return panel;
	}

	/**
	 * @return the rimuoviRigaContrattoAgenteCommand
	 */
	public ApplicationWindowAwareCommand getRimuoviRigaContrattoAgenteCommand() {
		if (rimuoviRigaContrattoAgenteCommand == null) {
			rimuoviRigaContrattoAgenteCommand = new RimuoviRigaContrattoAgenteCommand();
		}

		return rimuoviRigaContrattoAgenteCommand;
	}
}
