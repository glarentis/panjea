package it.eurotn.panjea.partite.rich.tabelle.righestruttura;

import it.eurotn.panjea.partite.domain.ContoPartita;
import it.eurotn.panjea.partite.domain.ContoPartita.TipoConto;
import it.eurotn.panjea.partite.rich.bd.IPartiteBD;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;

import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.form.builder.TableFormBuilder;

public class ContoPartitaForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "contoPartitaForm";
	private final IPartiteBD partiteBD;

	/**
	 * Costruttore di default.
	 * 
	 * @param contoPartita
	 *            la contro partita da editare nel form
	 */
	public ContoPartitaForm(final ContoPartita contoPartita) {
		super(PanjeaFormModelHelper.createFormModel(contoPartita, false, FORM_ID), FORM_ID);
		partiteBD = (IPartiteBD) Application.instance().getApplicationContext().getBean("partiteBD");
	}

	@Override
	protected JComponent createFormControl() {
		getFormModel().getValueModel("tipoConto").addValueChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				TipoConto tipoConto = (TipoConto) evt.getNewValue();
				getFormModel().getFieldMetadata("sottoConto").setEnabled(
						tipoConto.ordinal() == TipoConto.CONTO.ordinal());
				getFormModel().getFieldMetadata("contoBase").setEnabled(
						tipoConto.ordinal() == TipoConto.CONTO_BASE.ordinal());
			}
		});

		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");
		builder.add("tipoConto");
		builder.row();
		builder.add("sottoConto");
		builder.row();
		builder.add(bf.createBoundComboBox("contoBase", new ValueHolder(partiteBD.caricaContiBase()), "descrizione"));
		builder.row();
		builder.add("tipoOperazione");
		builder.row();
		builder.add("dareAvere");
		return builder.getForm();
	}

}
