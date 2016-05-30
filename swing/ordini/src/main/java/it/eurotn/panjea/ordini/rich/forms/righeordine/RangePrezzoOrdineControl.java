package it.eurotn.panjea.ordini.rich.forms.righeordine;

import it.eurotn.panjea.magazzino.rich.forms.rigamagazzino.RangePrezzoControl;

import javax.swing.JComponent;

import org.springframework.binding.form.FormModel;

public class RangePrezzoOrdineControl extends RangePrezzoControl {

	/**
	 *
	 * @param formModel
	 *            form model su cui registrarsi ai cambiamenti per agire sui controlli.
	 * @param controlOwner
	 *            controllo da associare al popup come owner
	 */
	public RangePrezzoOrdineControl(final FormModel formModel, final JComponent controlOwner) {
		super(formModel, controlOwner);
	}

	@Override
	protected String getPrezzoUnitarioPropertyName() {
		return "prezzoUnitarioReale";
	}
}
