package it.eurotn.panjea.anagrafica.rich.editors.datigeografici;

import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo1;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo2;
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

public class LivelloAmministrativo2Form extends PanjeaAbstractForm {

	private static final String FORM_ID = "livelloAmministrativo2Form";

	/**
	 * Costruttore.
	 */
	public LivelloAmministrativo2Form() {
		super(PanjeaFormModelHelper.createFormModel(new LivelloAmministrativo2(), false, FORM_ID));
		ValueModel livello1ValueModel = new ValueHolder(NumeroLivelloAmministrativo.LVL1);
		DefaultFieldMetadata metaData = new DefaultFieldMetadata(getFormModel(), new FormModelMediatingValueModel(
				livello1ValueModel), NumeroLivelloAmministrativo.class, true, null);
		getFormModel().add("numeroLivelloAmministrativo", livello1ValueModel, metaData);
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
		builder.addLabel("suddivisioneAmministrativaPrecedente", 1);
		SearchPanel searchPanel = (SearchPanel) builder.addBinding(bf.createBoundSearchText(
				"suddivisioneAmministrativaPrecedente", new String[] { "nome" }, new String[] { "nazione",
						"suddivisioneAmministrativaPrecedente", "numeroLivelloAmministrativo" }, new String[] {
						Nazione.class.getName(), LivelloAmministrativo1.class.getName(),
						NumeroLivelloAmministrativo.class.getName() }, LivelloAmministrativo1.class), 3);
		searchPanel.getTextFields().get("nome").setColumns(25);
		builder.nextRow();
		((JTextField) builder.addPropertyAndLabel("codiceIstat")[1]).setColumns(4);
		builder.nextRow();
		((JTextField) builder.addPropertyAndLabel("sigla")[1]).setColumns(4);
		builder.nextRow();

		return builder.getPanel();
	}

}
