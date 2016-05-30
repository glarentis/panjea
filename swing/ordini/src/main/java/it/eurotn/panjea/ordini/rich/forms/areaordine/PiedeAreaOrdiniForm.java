package it.eurotn.panjea.ordini.rich.forms.areaordine;

import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class PiedeAreaOrdiniForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "piedeAreaOrdiniForm";

	/**
	 * Costruttore.
	 * 
	 * @param pageFormModel
	 *            formmodel
	 * @param aziendaCorrente
	 *            azienda corrente
	 */
	public PiedeAreaOrdiniForm(final FormModel pageFormModel, final AziendaCorrente aziendaCorrente) {
		super(pageFormModel, FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {

		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("left:pref, 4dlu, fill:80dlu, 10dlu,right:pref, 4dlu,fill:80dlu", "default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
		builder.setLabelAttributes("r,c");

		builder.nextRow();
		builder.addLabel("areaOrdine.totaliArea.speseTrasporto", 1);
		builder.addBinding(
				bf.createBoundImportoTextField("areaOrdine.totaliArea.speseTrasporto", "areaOrdine.documento.totale"),
				3);
		builder.nextRow();
		builder.addLabel("areaOrdine.totaliArea.altreSpese");
		builder.addBinding(
				bf.createBoundImportoTextField("areaOrdine.totaliArea.altreSpese", "areaOrdine.documento.totale"), 3);
		builder.nextRow();
		builder.addLabel("areaOrdine.totaliArea.totaleMerce");
		builder.addBinding(
				bf.createBoundImportoTextField("areaOrdine.totaliArea.totaleMerce", "areaOrdine.documento.totale"), 3);
		builder.nextRow();
		builder.addLabel("areaOrdine.documento.totale", 1);
		builder.addBinding(bf.createBoundImportoTassoCalcolatoTextField("areaOrdine.documento.totale",
				"areaOrdine.documento.dataDocumento"), 3);

		builder.addLabel("areaOrdine.documento.imposta", 5);
		builder.addBinding(bf.createBoundImportoTassoCalcolatoTextField("areaOrdine.documento.imposta",
				"areaOrdine.documento.dataDocumento"), 7);

		installReadOnlyPropertyChange();
		return builder.getPanel();
	}

	/**
	 * Installa i property change sul readonly del formmodel.
	 */
	private void installReadOnlyPropertyChange() {
		getFormModel().addPropertyChangeListener(FormModel.DIRTY_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				getFormModel().getFieldMetadata("areaOrdine.totaliArea.speseTrasporto").setReadOnly(true);
				getFormModel().getFieldMetadata("areaOrdine.totaliArea.altreSpese").setReadOnly(true);
				getFormModel().getFieldMetadata("areaOrdine.totaliArea.totaleMerce").setReadOnly(true);
				getFormModel().getFieldMetadata("areaOrdine.documento.totale").setReadOnly(true);
				getFormModel().getFieldMetadata("areaOrdine.documento.imposta").setReadOnly(true);
			}
		});
	}

}
