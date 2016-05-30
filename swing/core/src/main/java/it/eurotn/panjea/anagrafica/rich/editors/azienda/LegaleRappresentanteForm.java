/**
 *
 */
package it.eurotn.panjea.anagrafica.rich.editors.azienda;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.anagrafica.domain.Carica;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.DatiGeograficiBinding;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;

/**
 *
 * @author Aracno
 * @version 1.0, 15-mag-2006
 */
public class LegaleRappresentanteForm extends PanjeaAbstractForm {

	/**
	 * Costruttore.
	 *
	 * @param formModel
	 *            form model
	 * @param formId
	 *            id del form
	 */
	public LegaleRappresentanteForm(final FormModel formModel, final String formId) {
		super(formModel, formId);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"70dlu,4dlu,left:default, 10dlu, right:pref,4dlu,left:default, fill:default:grow", "3dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel()
		builder.setLabelAttributes("r, c");
		builder.nextRow();
		builder.setRow(2);

		Binding nazioneBinding = bf.createBoundSearchText("azienda.legaleRappresentante.nazione",
				new String[] { "codice" });
		builder.addLabel("nazione", 1);
		SearchPanel nazioneSearchPanel = (SearchPanel) builder.addBinding(nazioneBinding, 3);
		nazioneSearchPanel.getTextFields().get("codice").setColumns(3);
		builder.nextRow();

		builder.addPropertyAndLabel("azienda.legaleRappresentante.titolo", 1);
		builder.addPropertyAndLabel("azienda.legaleRappresentante.denominazione", 5);
		builder.nextRow();

		builder.addPropertyAndLabel("azienda.legaleRappresentante.nome", 1);
		builder.addPropertyAndLabel("azienda.legaleRappresentante.cognome", 5);
		builder.nextRow();

		builder.addPropertyAndLabel("azienda.legaleRappresentante.sesso");
		builder.nextRow();

		((JTextField) builder.addPropertyAndLabel("azienda.legaleRappresentante.telefono", 1)[1]).setColumns(13);
		((JTextField) builder.addPropertyAndLabel("azienda.legaleRappresentante.fax", 5)[1]).setColumns(13);
		builder.nextRow();

		builder.addLabel("azienda.legaleRappresentante.codiceFiscale", 1);
		builder.addBinding(bf.createBoundCodiceFiscale("azienda.legaleRappresentante.codiceFiscale",
				"azienda.legaleRappresentante.nome", "azienda.legaleRappresentante.cognome",
				"azienda.legaleRappresentante.dataNascita", null, "azienda.legaleRappresentante.sesso"), 3)
				.setPreferredSize(new Dimension(180, 22));
		((JTextField) builder.addPropertyAndLabel("azienda.legaleRappresentante.partitaIVA", 5)[1]).setColumns(11);
		builder.nextRow();

		builder.addPropertyAndLabel("azienda.legaleRappresentante.codiceIdentificativoFiscale", 1);
		builder.addPropertyAndLabel("azienda.legaleRappresentante.codiceEori", 5);
		builder.nextRow();

		builder.addLabel("azienda.legaleRappresentante.carica", 1);
		builder.addBinding(bf.createBoundSearchText("azienda.legaleRappresentante.carica",
				new String[] { Carica.PROP_DESCRIZIONE }), 3);
		builder.addPropertyAndLabel("azienda.legaleRappresentante.dataCarica", 5);
		builder.nextRow();

		builder.addHorizontalSeparator("estremiDiNascitaSeparator", 7);
		builder.nextRow();

		builder.addPropertyAndLabel("azienda.legaleRappresentante.dataNascita");
		builder.nextRow();

		DatiGeograficiBinding bindingDatiGeografici = (DatiGeograficiBinding) bf
				.createDatiGeograficiBinding("azienda.legaleRappresentante.datiGeograficiNascita", "right:70dlu");
		builder.addBinding(bindingDatiGeografici, 1, 22, 7, 1);
		builder.nextRow();

		builder.addHorizontalSeparator("estremiDiResidenzaSeparator", 7);
		builder.nextRow();

		builder.addPropertyAndLabel("azienda.legaleRappresentante.viaResidenza");
		builder.nextRow();

		DatiGeograficiBinding bindingDatiGeograficiResidenza = (DatiGeograficiBinding) bf
				.createDatiGeograficiBinding("azienda.legaleRappresentante.datiGeograficiResidenza", "right:70dlu");
		builder.addBinding(bindingDatiGeograficiResidenza, 1, 28, 7, 1);
		builder.nextRow();

		return builder.getPanel();
	}

}
