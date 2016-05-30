package it.eurotn.panjea.rate.rich.forms.rate;

import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.rate.domain.Rata;
import it.eurotn.panjea.rate.domain.Rata.StatoRata;
import it.eurotn.panjea.rate.rich.forms.AbstractAreaRateModel;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.ImportoBinding;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.components.ImportoTextField;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class RataPartitaForm extends PanjeaAbstractForm {

	private class FormObjectPropertyChange implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {

			Rata rata = (Rata) getFormObject();

			if (rata.isNew()) {
				return;
			}

			// se la rata è insoluta non posso modificare niente
			if (StatoRata.IN_RIASSEGNAZIONE.equals(rata.getStatoRata())) {
				getFormModel().setReadOnly(true);
				return;
			}
		}
	}

	private static final String FORM_ID = "rataPartitaForm";

	private AbstractAreaRateModel areaRateModel;

	private PropertyChangeListener formObjectListener;

	/**
	 * @param aziendaCorrente
	 *            aziendaCorrente
	 */
	public RataPartitaForm(final AziendaCorrente aziendaCorrente) {
		super(PanjeaFormModelHelper.createFormModel(new Rata(), false, FORM_ID), FORM_ID);
		formObjectListener = new FormObjectPropertyChange();
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:pref,4dlu,left:default, 10dlu, right:80dlu,4dlu,left:default",
				"2dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // ,
		// new
		// FormDebugPanel());
		builder.setLabelAttributes("r, c");

		builder.nextRow();
		builder.setRow(2);

		ImportoBinding importoBinding = (ImportoBinding) bf.createBoundImportoTextField("importo",
				"areaRate.documento.totale");
		builder.addLabel("importo", 1);
		ImportoTextField importoTextField = (ImportoTextField) builder.addBinding(importoBinding, 3);
		importoTextField.setColumns(10);
		builder.nextRow();

		builder.addPropertyAndLabel("dataScadenza", 1);
		JTextField textFieldNumeroRata = (JTextField) builder.addPropertyAndLabel("numeroRata", 5)[1];
		textFieldNumeroRata.setColumns(2);
		builder.nextRow();

		builder.addPropertyAndLabel("tipologiaPartita", 1);
		builder.addPropertyAndLabel("tipoPagamento", 5);
		builder.nextRow();

		Binding bindingBancaAzienda = bf.createBoundSearchText("rapportoBancarioAzienda", new String[] { "numero",
		"descrizione" });
		SearchPanel bancaCodSearchText = (SearchPanel) bindingBancaAzienda.getControl();
		bancaCodSearchText.getTextFields().get("numero").setColumns(5);
		bancaCodSearchText.getTextFields().get("descrizione").setColumns(18);
		builder.addLabel("rapportoBancarioAzienda", 1);
		builder.addBinding(bindingBancaAzienda, 3);
		builder.nextRow();

		Binding bindingBancaEntita = bf.createBoundSearchText("rapportoBancarioEntita", new String[] { "numero",
		"descrizione" }, new String[] { "areaRate.documento.sedeEntita.id" }, new String[] { "idSedeEntita" });
		SearchPanel bancaEntitaCodSearchText = (SearchPanel) bindingBancaEntita.getControl();
		bancaEntitaCodSearchText.getTextFields().get("numero").setColumns(5);
		bancaEntitaCodSearchText.getTextFields().get("descrizione").setColumns(18);
		builder.addLabel("rapportoBancarioEntita", 1);
		builder.addBinding(bindingBancaEntita, 3);
		builder.nextRow();

		Binding catRataBinding = bf.createBoundSearchText("categoriaRata", new String[] { "descrizione" });
		builder.addLabel("categoriaRata", 1);
		builder.addBinding(catRataBinding, 3);
		builder.nextRow();

		builder.addPropertyAndLabel("stampaRV", 1);
		builder.nextRow();

		builder.setComponentAttributes("f,c");
		builder.addPropertyAndLabel("note", 1);
		builder.nextRow();

		addFormObjectChangeListener(formObjectListener);
		getFormModel().addPropertyChangeListener(formObjectListener);

		return builder.getPanel();
	}

	@Override
	protected Object createNewObject() {
		return areaRateModel.creaNuovaRiga();
	}

	@Override
	public void dispose() {
		super.dispose();

		removeFormObjectChangeListener(formObjectListener);
		formObjectListener = null;
	}

	/**
	 * @param areaRateModel
	 *            the areaRateModel to set
	 */
	public void setAreaRateModel(AbstractAreaRateModel areaRateModel) {
		this.areaRateModel = areaRateModel;
	}
}
