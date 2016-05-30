package it.eurotn.panjea.anagrafica.rich.editors.datigeografici;

import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo1;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Nazione;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class LivelloAmministrativo1Form extends PanjeaAbstractForm {

	private static final String FORM_ID = "livelloAmministrativo1Form";
	private JLabel label;

	/**
	 * Costruttore.
	 */
	public LivelloAmministrativo1Form() {
		super(PanjeaFormModelHelper.createFormModel(new LivelloAmministrativo1(), false, FORM_ID));
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"right:pref,4dlu,left:pref,10dlu,right:pref,4dlu,left:pref,10dlu,fill:default:grow", "2dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
		builder.setLabelAttributes("r, c");
		builder.nextRow();
		builder.setRow(2);

		((JTextField) builder.addPropertyAndLabel("nome")[1]).setColumns(25);
		builder.nextRow();
		builder.addLabel("nazione", 1);
		label = getComponentFactory().createLabel("");
		label.setFont(new Font(label.getFont().getName(), Font.BOLD, label.getFont().getSize()));

		builder.addComponent(label, 3);
		// SearchPanel searchPanel = (SearchPanel) builder.addBinding(
		// bf.createBoundSearchText("nazione", new String[] { "codice", "descrizione" }), 3);
		// searchPanel.getTextFields().get("codice").setColumns(3);
		builder.nextRow();
		((JTextField) builder.addPropertyAndLabel("codiceIstat")[1]).setColumns(3);
		builder.nextRow();

		getValueModel("nazione").addValueChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				Nazione nazione = (Nazione) evt.getNewValue();
				String desc = (nazione != null ? nazione.getDescrizione() : "");
				label.setText(desc);
			}
		});

		return builder.getPanel();
	}

}
