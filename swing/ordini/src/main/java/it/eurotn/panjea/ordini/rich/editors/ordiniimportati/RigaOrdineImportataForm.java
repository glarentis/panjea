package it.eurotn.panjea.ordini.rich.editors.ordiniimportati;

import it.eurotn.panjea.anagrafica.domain.Cliente;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.ordini.domain.RigaOrdineImportata;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.util.DefaultNumberFormatterFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import org.jdesktop.swingx.HorizontalLayout;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.converter.ObjectConverterManager;

public class RigaOrdineImportataForm extends PanjeaAbstractForm {

	private class CopiaPrezzoActionCommand extends ActionCommand {

		public static final String COMMAND_ID = "prezzoDeterminatoCopia";

		/**
		 * Costruttore.
		 * 
		 */
		public CopiaPrezzoActionCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
			setEnabled(false);
		}

		@Override
		protected void doExecuteCommand() {

			getFormModel().getValueModel("prezzoDeterminato").setValue(
					getFormModel().getValueModel("prezzo").getValue());

			getFormModel().getValueModel("prezzoUnitarioDeterminato").setValue(
					getFormModel().getValueModel("prezzoUnitario").getValue());

			getFormModel().getValueModel("sconto1Determinato").setValue(
					getFormModel().getValueModel("sconto1").getValue());
			getFormModel().getValueModel("sconto2Determinato").setValue(
					getFormModel().getValueModel("sconto2").getValue());
			getFormModel().getValueModel("sconto3Determinato").setValue(
					getFormModel().getValueModel("sconto3").getValue());
			getFormModel().getValueModel("sconto4Determinato").setValue(
					getFormModel().getValueModel("sconto4").getValue());

		}

	}

	private class FormObjectChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {

			ArticoloLite articolo = (ArticoloLite) getFormModel().getValueModel("articolo").getValue();
			if (articolo == null) {
				articoloLabel.setText("");
			} else {
				articoloLabel.setText(ObjectConverterManager.toString(articolo));
			}

			ClienteLite cliente = (ClienteLite) getFormModel().getValueModel("ordine.entita").getValue();
			if (cliente == null) {
				clienteLabel.setText("");
			} else {
				clienteLabel.setText(ObjectConverterManager.toString(cliente));
			}

		}

	}

	private class FormReadOnlyPropertyChange implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {

			copiaPrezzoActionCommand.setEnabled(!getFormModel().isReadOnly());

			getFormModel().getFieldMetadata("prezzoUnitario").setReadOnly(true);
			getFormModel().getFieldMetadata("prezzo").setReadOnly(true);
			getFormModel().getFieldMetadata("sconto1").setReadOnly(true);
			getFormModel().getFieldMetadata("sconto2").setReadOnly(true);
			getFormModel().getFieldMetadata("sconto3").setReadOnly(true);
			getFormModel().getFieldMetadata("sconto4").setReadOnly(true);
		}

	}

	private class ScontoPropertyChange implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (!getFormModel().isReadOnly()) {
				getFormModel().getValueModel("prezzoDeterminato").setValue(
						getFormModel().getValueModel("prezzoDeterminato").getValue());
			}
		}

	}

	public static final String FORM_ID = "rigaOrdineImportazioneForm";
	public static final String FORMMODEL_ID = "rigaOrdineImportazioneFormModel";

	private CopiaPrezzoActionCommand copiaPrezzoActionCommand;

	private JLabel articoloLabel = null;
	private JLabel clienteLabel = null;

	/**
	 * Costruttore.
	 * 
	 */
	public RigaOrdineImportataForm() {
		super(PanjeaFormModelHelper.createFormModel(new RigaOrdineImportata(), false, FORMMODEL_ID), FORM_ID);

		copiaPrezzoActionCommand = new CopiaPrezzoActionCommand();

		articoloLabel = new JLabel();
		articoloLabel.setIcon(RcpSupport.getIcon(Articolo.class.getName()));
		clienteLabel = new JLabel();
		clienteLabel.setIcon(RcpSupport.getIcon(Cliente.class.getName()));
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("left:pref,4dlu,50dlu,10dlu,left:pref,4dlu,default,4dlu,100dlu",
				"4dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel());
		builder.setLabelAttributes("r, c");

		builder.nextRow();
		builder.setRow(2);

		builder.addLabel("ordine.entita", 1);
		builder.addComponent(clienteLabel, 3, 2, 5, 1);
		builder.nextRow();

		builder.addLabel("articolo", 1);
		builder.addComponent(articoloLabel, 3, 4, 5, 1);
		builder.nextRow();

		Binding bindingCodPag = bf.createBoundSearchText("ordine.pagamento", new String[] { "codicePagamento",
				"descrizione" });
		builder.addLabel("codicePagamento", 1);
		SearchPanel searchPanel = (SearchPanel) builder.addBinding(bindingCodPag, 3, 6, 5, 1);
		searchPanel.getTextFields().get("codicePagamento").setColumns(6);
		searchPanel.getTextFields().get("descrizione").setColumns(27);
		builder.nextRow();

		builder.addLabel("qta", 1);
		JFormattedTextField formattedTextField = (JFormattedTextField) builder
				.addBinding(bf.createBoundFormattedTextField("qta", new DefaultNumberFormatterFactory("#,##0", 3,
						Double.class)), 3);
		formattedTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		builder.addPropertyAndLabel("attributi", 5);
		builder.nextRow();

		builder.addHorizontalSeparator("Prezzi determinati ( prezzi che verranno riportati sull'ordine confermato )", 7);
		builder.nextRow();

		builder.addLabel("prezzoUnitarioDeterminato", 1);
		formattedTextField = (JFormattedTextField) builder.addBinding(bf.createBoundFormattedTextField(
				"prezzoUnitarioDeterminato", new DefaultNumberFormatterFactory("#,##0", 6, BigDecimal.class)), 3);
		formattedTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		builder.nextRow();

		// ------------------------------------------------------------------------------------------------------------------------------------------------------

		builder.addLabel("prezzoDeterminato", 1);
		formattedTextField = (JFormattedTextField) builder.addBinding(bf.createBoundFormattedTextField(
				"prezzoDeterminato", new DefaultNumberFormatterFactory("#,##0", 6, BigDecimal.class)), 3);
		formattedTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		JPanel variazioniDeterminatePanel = getComponentFactory().createPanel(new HorizontalLayout(3));
		builder.addLabel("sconto", 5);
		Binding variazione1DeterminataBinding = bf.createBoundPercentageText("sconto1Determinato");
		Binding variazione2DeterminataBinding = bf.createBoundPercentageText("sconto2Determinato");
		Binding variazione3DeterminataBinding = bf.createBoundPercentageText("sconto3Determinato");
		Binding variazione4DeterminataBinding = bf.createBoundPercentageText("sconto4Determinato");
		variazioniDeterminatePanel.add(variazione1DeterminataBinding.getControl());
		variazioniDeterminatePanel.add(variazione2DeterminataBinding.getControl());
		variazioniDeterminatePanel.add(variazione3DeterminataBinding.getControl());
		variazioniDeterminatePanel.add(variazione4DeterminataBinding.getControl());
		builder.addComponent(variazioniDeterminatePanel, 7);

		// ------------------------------------------------------------------------------------------------------------------------------------------------------

		builder.addComponent(copiaPrezzoActionCommand.createButton(), 1, 16, 7, 1, "c,c");
		builder.nextRow();
		builder.nextRow();

		// ------------------------------------------------------------------------------------------------------------------------------------------------------

		builder.addHorizontalSeparator("Prezzi importati ( prezzi letti dall'importazione del backorder )", 7);
		builder.nextRow();

		builder.addLabel("prezzoUnitario", 1);
		formattedTextField = (JFormattedTextField) builder.addBinding(bf.createBoundFormattedTextField(
				"prezzoUnitario", new DefaultNumberFormatterFactory("#,##0", 6, BigDecimal.class)), 3);
		formattedTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		builder.nextRow();

		builder.addLabel("prezzo", 1);
		formattedTextField = (JFormattedTextField) builder.addBinding(bf.createBoundFormattedTextField("prezzo",
				new DefaultNumberFormatterFactory("#,##0", 6, BigDecimal.class)), 3);
		formattedTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		JPanel variazioniPanel = getComponentFactory().createPanel(new HorizontalLayout(3));
		builder.addLabel("sconto", 5);
		Binding variazione1Binding = bf.createBoundPercentageText("sconto1");
		Binding variazione2Binding = bf.createBoundPercentageText("sconto2");
		Binding variazione3Binding = bf.createBoundPercentageText("sconto3");
		Binding variazione4Binding = bf.createBoundPercentageText("sconto4");
		variazioniPanel.add(variazione1Binding.getControl());
		variazioniPanel.add(variazione2Binding.getControl());
		variazioniPanel.add(variazione3Binding.getControl());
		variazioniPanel.add(variazione4Binding.getControl());
		builder.addComponent(variazioniPanel, 7);
		builder.nextRow();

		Binding noteBinding = bf.createBoundTextArea("note");
		JTextArea textArea = (JTextArea) noteBinding.getControl();
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		JPanel notePanel = getComponentFactory().createPanel(new BorderLayout());
		notePanel.setBorder(BorderFactory.createTitledBorder("Note"));
		notePanel.add(textArea, BorderLayout.CENTER);
		builder.addComponent(notePanel, 9, 1, 1, 24);

		getFormModel().addPropertyChangeListener(FormModel.READONLY_PROPERTY, new FormReadOnlyPropertyChange());
		addFormObjectChangeListener(new FormObjectChangeListener());

		ScontoPropertyChange scontoPropertyChange = new ScontoPropertyChange();
		getFormModel().getValueModel("sconto1Determinato").addValueChangeListener(scontoPropertyChange);
		getFormModel().getValueModel("sconto2Determinato").addValueChangeListener(scontoPropertyChange);
		getFormModel().getValueModel("sconto3Determinato").addValueChangeListener(scontoPropertyChange);
		getFormModel().getValueModel("sconto4Determinato").addValueChangeListener(scontoPropertyChange);

		return builder.getPanel();
	}

}
