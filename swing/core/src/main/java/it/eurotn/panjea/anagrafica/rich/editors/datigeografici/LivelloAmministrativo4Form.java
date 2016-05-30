package it.eurotn.panjea.anagrafica.rich.editors.datigeografici;

import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo3;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo4;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Nazione;
import it.eurotn.panjea.anagrafica.domain.datigeografici.SuddivisioneAmministrativa.NumeroLivelloAmministrativo;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class LivelloAmministrativo4Form extends PanjeaAbstractForm {

	private static final String FORM_ID = "livelloAmministrativo4Form";

	/**
	 * Costruttore.
	 */
	public LivelloAmministrativo4Form() {
		super(PanjeaFormModelHelper.createFormModel(new LivelloAmministrativo4(), false, FORM_ID));
		ValueModel livello3ValueModel = new ValueHolder(NumeroLivelloAmministrativo.LVL3);
		DefaultFieldMetadata metaData = new DefaultFieldMetadata(getFormModel(), new FormModelMediatingValueModel(
				livello3ValueModel), NumeroLivelloAmministrativo.class, true, null);
		getFormModel().add("numeroLivelloAmministrativo", livello3ValueModel, metaData);
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

		builder.nextRow();
		((JTextField) builder.addPropertyAndLabel("nome")[1]).setColumns(25);
		builder.nextRow();
		builder.addLabel("suddivisioneAmministrativaPrecedente", 1);
		SearchPanel searchPanel = (SearchPanel) builder.addBinding(bf.createBoundSearchText(
				"suddivisioneAmministrativaPrecedente", new String[] { "nome" }, new String[] { "nazione",
						"suddivisioneAmministrativaPrecedente", "numeroLivelloAmministrativo" }, new String[] {
						Nazione.class.getName(), LivelloAmministrativo3.class.getName(),
						NumeroLivelloAmministrativo.class.getName() }), 3);
		searchPanel.getTextFields().get("nome").setColumns(25);
		builder.nextRow();

		return builder.getPanel();
	}

}
