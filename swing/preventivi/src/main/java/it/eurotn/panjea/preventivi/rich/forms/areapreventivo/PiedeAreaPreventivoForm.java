package it.eurotn.panjea.preventivi.rich.forms.areapreventivo;

import org.springframework.binding.form.FormModel;

public class PiedeAreaPreventivoForm extends TotaliDocumentoForm {

	public static final String FORM_ID = "piedeAreaPreventivoForm";

	/**
	 * 
	 * @param pageFormModel
	 *            pageFormModel
	 */
	public PiedeAreaPreventivoForm(final FormModel pageFormModel) {
		super(pageFormModel, FORM_ID, "areaPreventivo");
	}
}
