package it.eurotn.panjea.magazzino.rich.editors.listino;

import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino;
import it.eurotn.panjea.magazzino.rich.search.SedeMagazzinoSearchObject;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.riepilogo.util.RiepilogoSedeEntitaDTO;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;

import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.AbstractFocussableForm;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class SedeListinoPMForm extends AbstractFocussableForm {

	public static final String FORM_ID = "sedeListinoPMForm";
	public static final String FORMMODEL_ID = "sedeListinoPMFormModel";

	/**
	 * Costruttore.
	 * 
	 */
	public SedeListinoPMForm() {
		super(PanjeaFormModelHelper.createFormModel(new SedeListiniPM(new RiepilogoSedeEntitaDTO(), new Listino()),
				false, FORM_ID), FORM_ID);
		// Aggiungo il valueHolder fittizzio
		ValueModel tipoSedeValueModel = new ValueHolder(Boolean.TRUE);
		DefaultFieldMetadata metaData = new DefaultFieldMetadata(getFormModel(), new FormModelMediatingValueModel(
				tipoSedeValueModel), Boolean.class, true, null);
		getFormModel().add("tipoSede", tipoSedeValueModel, metaData);

		ValueModel ereditaDatiCommercialiValueModel = new ValueHolder(Boolean.FALSE);
		DefaultFieldMetadata metaDataDatiCommerciali = new DefaultFieldMetadata(getFormModel(),
				new FormModelMediatingValueModel(ereditaDatiCommercialiValueModel), Boolean.class, true, null);
		getFormModel().add("ereditaDatiCommerciali", ereditaDatiCommercialiValueModel, metaDataDatiCommerciali);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"4dlu,left:pref, 4dlu, left:pref, 4dlu,left:pref, 4dlu,left:pref, 4dlu,left:pref",
				"4dlu,default,4dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // ,
																				// new
																				// FormDebugPanel());

		builder.setLabelAttributes("l,b");
		builder.setComponentAttributes("c,c");

		builder.addLabel("entita", 2, 2);

		Binding bindingEntita = bf.createBoundSearchText("entitaForm", new String[] { "codice",
				"anagrafica.denominazione" }, new String[] { "tipiEntita" },
				new String[] { EntitaByTipoSearchObject.TIPI_ENTITA_LIST_KEY }, EntitaLite.class);
		SearchPanel searchPanel = (SearchPanel) bindingEntita.getControl();
		searchPanel.getTextFields().get("codice").setColumns(4);
		searchPanel.getTextFields().get("anagrafica.denominazione").setColumns(15);
		builder.addBinding(bindingEntita, 2, 4);
		setFocusControl(searchPanel.getTextFields().get("codice").getTextField());

		builder.addLabel("sedeMagazzino", 4, 2);
		Binding sedeEntitaBinding = bf.createBoundSearchText("sedeMagazzinoForm",
				new String[] { "sedeEntita.sede.descrizione" }, new String[] { "entitaForm", "tipoSede",
						"ereditaDatiCommerciali" }, new String[] { SedeMagazzinoSearchObject.PARAMETER_ENTITA_ID,
						SedeMagazzinoSearchObject.PARAMETER_TIPO_SEDE,
						SedeMagazzinoSearchObject.PARAMETER_EREDITA_DATI_COMMERCIALI });
		SearchPanel searchPanelSede = (SearchPanel) builder.addBinding(sedeEntitaBinding, 4, 4);
		searchPanelSede.getTextFields().get("sedeEntita.sede.descrizione").setColumns(20);

		builder.addLabel("listinoAssociato", 6, 2);
		builder.addProperty("listinoAssociatoForm", 6, 4);

		builder.addLabel("listinoAlternativoAssociato", 8, 2);
		builder.addProperty("listinoAlternativoAssociatoForm", 8, 4);

		getValueModel("entitaForm").addValueChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				getValueModel("sedeMagazzinoForm").setValue(new SedeMagazzino());
			}
		});

		return builder.getPanel();
	}

}
