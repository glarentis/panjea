/**
 * 
 */
package it.eurotn.panjea.beniammortizzabili.rich.editors.beni;

import it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabile;
import it.eurotn.panjea.beniammortizzabili2.domain.SottoSpecie;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.builder.TableFormBuilder;

/**
 * 
 * @author Aracno
 * @version 1.0, 02/ott/06
 * 
 */
public class ParametriCalcoloBeneAmmortamentoForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "parametriCalcoloBeneAmmortamentoForm";

	/**
	 * Costruttore.
	 * 
	 * @param pageFormModel
	 *            form model
	 */
	public ParametriCalcoloBeneAmmortamentoForm(final FormModel pageFormModel) {
		super(pageFormModel, FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");

		builder.row();
		builder.addSeparator("parametriCalcoloBeneAmmortamentoFormModel.datiCivilisticiSeparator");
		builder.row();
		builder.add("datiCivilistici.ammortamentoInCorso", "colSpan=1 align=left");
		builder.row();
		((JTextField) builder.add("datiCivilistici.percentualeAmmortamentoOrdinario", "colSpan=1 align=left")[1])
				.setColumns(10);
		builder.row();
		((JTextField) builder.add("datiCivilistici.percentualeMinoreUtilizzoBene", "colSpan=1 align=left")[1])
				.setColumns(10);
		builder.row();
		((JTextField) builder.add("datiCivilistici.percentualeMaggioreUtilizzoBene", "colSpan=1 align=left")[1])
				.setColumns(10);
		builder.row();
		builder.addSeparator("parametriCalcoloBeneAmmortamentoFormModel.datiFiscaliSeparator");
		builder.row();
		builder.add("datiFiscali.ammortamentoInCorso", "colSpan=1 align=left");
		builder.row();
		((JTextField) builder.add("datiFiscali.percentualeAmmortamentoOrdinario", "colSpan=1 align=left")[1])
				.setColumns(10);
		builder.row();
		builder.add("datiFiscali.attivazioneAmmortamentoAnticipato", "colSpan=1 align=left");
		builder.row();
		((JTextField) builder.add("datiFiscali.percentualeAmmortamentoAnticipato", "colSpan=1 align=left")[1])
				.setColumns(10);
		builder.row();
		((JTextField) builder.add("datiFiscali.percentualeAmmortamentoAccelerato", "colSpan=1 align=left")[1])
				.setColumns(10);
		builder.row();

		getValueModel("sottoSpecie").addValueChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				SottoSpecie sp = (SottoSpecie) arg0.getNewValue();

				if (sp != null && sp.getId() != null && ((BeneAmmortizzabile) getFormObject()).isNew()) {
					getValueModel("datiFiscali.percentualeAmmortamentoOrdinario").setValue(
							sp.getPercentualeAmmortamento());
					getValueModel("datiFiscali.percentualeAmmortamentoAnticipato").setValue(
							sp.getPercentualeAmmortamento());
					getValueModel("datiFiscali.ammortamentoInCorso").setValue(Boolean.TRUE);
					getValueModel("datiFiscali.attivazioneAmmortamentoAnticipato").setValue(Boolean.TRUE);
				}
			}
		});

		return getComponentFactory().createScrollPane(builder.getForm());
	}

}
