package it.eurotn.panjea.anagrafica.rich.editors.datigeografici;

import it.eurotn.panjea.anagrafica.domain.datigeografici.DatiGeografici;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo1;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo2;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo3;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo4;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Nazione;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.DatiGeograficiBindingForm;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;

import org.springframework.richclient.form.AbstractFocussableForm;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class LocalitaInserimentoForm extends AbstractFocussableForm {

	public static final String FORM_ID = "localitaInserimentoForm";
	public static final String FORMMODEL_ID = "localitaInserimentoFormModel";

	/**
	 * Costruttore.
	 * 
	 * 
	 */
	public LocalitaInserimentoForm() {
		super(PanjeaFormModelHelper.createFormModel(new DatiGeografici(), false, FORMMODEL_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("fill:110dlu", "default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel());
		builder.setLabelAttributes("l,c");

		Binding localitaBinding = bf.createBoundSearchText(DatiGeograficiBindingForm.LOCALITA_FORMPROPERTYPATH,
				new String[] { "descrizione" }, new String[] { DatiGeograficiBindingForm.NAZIONE_FORMPROPERTYPATH,
						DatiGeograficiBindingForm.LVL1_FORMPROPERTYPATH,
						DatiGeograficiBindingForm.LVL2_FORMPROPERTYPATH,
						DatiGeograficiBindingForm.LVL3_FORMPROPERTYPATH,
						DatiGeograficiBindingForm.LVL4_FORMPROPERTYPATH }, new String[] { Nazione.class.getName(),
						LivelloAmministrativo1.class.getName(), LivelloAmministrativo2.class.getName(),
						LivelloAmministrativo3.class.getName(), LivelloAmministrativo4.class.getName() });

		SearchPanel searchLocalita = (SearchPanel) builder.addBinding(localitaBinding);
		setFocusControl(searchLocalita.getTextFields().get("descrizione").getTextField());

		return builder.getPanel();
	}

}
