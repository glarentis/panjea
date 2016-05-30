package it.eurotn.panjea.contabilita.rich.editors.righecontabili;

import it.eurotn.panjea.centricosto.domain.CentroCosto;
import it.eurotn.panjea.contabilita.domain.RigaCentroCosto;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JComponent;

import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.RefreshableValueHolder;
import org.springframework.richclient.form.AbstractFocussableForm;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.rules.closure.Closure;

import com.jgoodies.forms.layout.FormLayout;

/**
 * 
 * form per l'inserimetno del centro di costo in tabella.
 * 
 * @author giangi
 * @version 1.0, 24/dic/2010
 * 
 */
public class CentroCostoInserimentoForm extends AbstractFocussableForm {

	/**
	 * Aggiorna l'importo per il centro di costo con il valore dato dalla differenza con lìimporto sulla riga con la
	 * somma degli importi dei centri di costo.
	 * 
	 */
	private class AggiornaImportoDaInserirePropertyChange implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent propertychangeevent) {
			getValueModel("importo").setValue(calcolaImportoMancante());
		}

	}

	private class CentroCostoPresentiPropertyChange implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent propertychangeevent) {
			centroCostoPresentiValueModel.refresh();
		}
	}

	private class RefreshFunctionCentroCostoPresenti implements Closure {
		private ValueModel righeCentroCostoValueModel;

		/**
		 * Costruttore.
		 * 
		 * @param righeCentroCostoValueModel
		 *            valure model per recuperare i centri di costo presenti.
		 */
		public RefreshFunctionCentroCostoPresenti(final ValueModel righeCentroCostoValueModel) {
			this.righeCentroCostoValueModel = righeCentroCostoValueModel;
		}

		@Override
		public Object call(Object arg0) {
			Set<CentroCosto> centriCosto = new HashSet<CentroCosto>();
			@SuppressWarnings("unchecked")
			Set<RigaCentroCosto> righe = (Set<RigaCentroCosto>) righeCentroCostoValueModel.getValue();
			for (RigaCentroCosto rigaCentroCosto : righe) {
				centriCosto.add(rigaCentroCosto.getCentroCosto());
			}
			return centriCosto;
		}
	}

	private RefreshableValueHolder centroCostoPresentiValueModel;
	private ValueModel righeCentroCostoValueModel;
	private ValueModel importoRigaValueModel;

	/**
	 * Costruttore.
	 * 
	 * @param righeCentroCostoValueModel
	 *            valure model per recuperare i centri di costo presenti.
	 * @param importoRigaValueModel
	 *            value model dell'importo delal riga Contabile
	 */
	public CentroCostoInserimentoForm(final ValueModel righeCentroCostoValueModel,
			final ValueModel importoRigaValueModel) {
		super(PanjeaFormModelHelper.createFormModel(new RigaCentroCosto(), false, "centroCostoForm"));

		this.importoRigaValueModel = importoRigaValueModel;
		this.righeCentroCostoValueModel = righeCentroCostoValueModel;

		centroCostoPresentiValueModel = new RefreshableValueHolder(new RefreshFunctionCentroCostoPresenti(
				this.righeCentroCostoValueModel), false, true);
		AggiornaImportoDaInserirePropertyChange aggPropertyChange = new AggiornaImportoDaInserirePropertyChange();
		righeCentroCostoValueModel.addValueChangeListener(new CentroCostoPresentiPropertyChange());
		righeCentroCostoValueModel.addValueChangeListener(aggPropertyChange);
		importoRigaValueModel.addValueChangeListener(aggPropertyChange);

	}

	/**
	 * 
	 * @return differenza tra importo riga e totale importi dei centri di costo.
	 */
	private BigDecimal calcolaImportoMancante() {
		BigDecimal importoCentriCosto = BigDecimal.ZERO;
		BigDecimal importoRiga = (BigDecimal) importoRigaValueModel.getValue();
		@SuppressWarnings("unchecked")
		Set<RigaCentroCosto> righe = (Set<RigaCentroCosto>) righeCentroCostoValueModel.getValue();
		for (RigaCentroCosto rigaCentroCosto : righe) {
			importoCentriCosto = importoCentriCosto.add(rigaCentroCosto.getImporto());
		}
		return importoRiga.subtract(importoCentriCosto);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:80dlu,fill:80dlu,fill:140dlu", "default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
		builder.setLabelAttributes("l,c");

		DefaultFieldMetadata metaData = new DefaultFieldMetadata(getFormModel(), new FormModelMediatingValueModel(
				centroCostoPresentiValueModel), List.class, true, null);
		getFormModel().add("centroCostoPresenti", centroCostoPresentiValueModel, metaData);

		Binding centroCostoBinding = bf.createBoundSearchText("centroCosto", new String[] { "descrizione" },
				new String[] { "centroCostoPresenti" }, new String[] { "filterList_key" });
		SearchPanel searchCentriCosto = (SearchPanel) builder.addBinding(centroCostoBinding, 1);
		setFocusControl(searchCentriCosto.getTextFields().get("descrizione").getTextField());
		builder.addProperty("importo", 2);
		builder.addProperty("nota", 3);
		return builder.getPanel();
	}

	@Override
	protected Object createNewObject() {
		RigaCentroCosto rigaCentroCosto = new RigaCentroCosto();
		rigaCentroCosto.setImporto(calcolaImportoMancante());
		return rigaCentroCosto;
	}
}
