package it.eurotn.panjea.preventivi.rich.forms.areapreventivo;

import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public abstract class TotaliDocumentoForm extends PanjeaAbstractForm {

	private String propertyTotaliArea;
	private String propertyDocumento;
	private String propertySpeseTrasporto;
	private String propertyAltreSpese;
	private String propertyTotaleMerce;
	private String propertyTotaleDocumento;
	private String propertyDataDocumento;
	private String propertyDocumentoImposta;

	/**
	 * Costruttore.
	 * 
	 * @param pageFormModel
	 *            formmodel
	 * @param formID
	 *            formID
	 * @param propertyArea
	 *            propertyArea
	 */
	public TotaliDocumentoForm(final FormModel pageFormModel, final String formID, final String propertyArea) {
		super(pageFormModel, formID);
		this.propertyTotaliArea = propertyArea + ".totaliArea";
		this.propertyDocumento = propertyArea + ".documento";
		this.propertySpeseTrasporto = propertyTotaliArea + ".speseTrasporto";
		this.propertyAltreSpese = propertyTotaliArea + ".altreSpese";
		this.propertyTotaleMerce = propertyTotaliArea + ".totaleMerce";
		this.propertyTotaleDocumento = propertyDocumento + ".totale";
		this.propertyDataDocumento = propertyDocumento + ".dataDocumento";
		this.propertyDocumentoImposta = propertyDocumento + ".imposta";
	}

	@Override
	protected JComponent createFormControl() {

		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("left:pref, 4dlu, fill:80dlu, 10dlu,right:pref, 4dlu,fill:80dlu", "default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
		builder.setLabelAttributes("r,c");

		builder.nextRow();
		builder.addLabel(propertySpeseTrasporto, 1);
		builder.addBinding(bf.createBoundImportoTextField(propertySpeseTrasporto, propertyTotaleDocumento), 3);
		builder.nextRow();
		builder.addLabel(propertyAltreSpese);
		builder.addBinding(bf.createBoundImportoTextField(propertyAltreSpese, propertyTotaleDocumento), 3);
		builder.nextRow();
		builder.addLabel(propertyTotaleMerce);
		builder.addBinding(bf.createBoundImportoTextField(propertyTotaleMerce, propertyTotaleDocumento), 3);
		builder.nextRow();
		builder.addLabel(propertyTotaleDocumento, 1);
		builder.addBinding(
				bf.createBoundImportoTassoCalcolatoTextField(propertyTotaleDocumento, propertyDataDocumento), 3);

		builder.addLabel(propertyDocumentoImposta, 5);
		builder.addBinding(
				bf.createBoundImportoTassoCalcolatoTextField(propertyDocumentoImposta, propertyDataDocumento), 7);

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
				getFormModel().getFieldMetadata(propertySpeseTrasporto).setReadOnly(true);
				getFormModel().getFieldMetadata(propertyAltreSpese).setReadOnly(true);
				getFormModel().getFieldMetadata(propertyTotaleMerce).setReadOnly(true);
				getFormModel().getFieldMetadata(propertyTotaleDocumento).setReadOnly(true);
				getFormModel().getFieldMetadata(propertyDocumentoImposta).setReadOnly(true);
			}
		});
	}

}
