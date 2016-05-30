/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.tabelle;

import it.eurotn.panjea.anagrafica.domain.TipoSedeEntita;
import it.eurotn.panjea.anagrafica.domain.TipoSedeEntita.TipoSede;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.binding.swing.SwingBindingFactory;
import org.springframework.richclient.form.builder.TableFormBuilder;

/**
 * @author Leonardo
 * 
 */
public class TipoSedeEntitaForm extends PanjeaAbstractForm {

	private class SedePrincipalePropertyChange implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {

			if (getFormModel().isReadOnly()) {
				return;
			}

			TipoSede tipoSede = (TipoSede) getFormModel().getValueModel(TipoSedeEntita.PROP_TIPO_SEDE).getValue();

			if (tipoSede == null || tipoSede != TipoSede.NORMALE) {
				getFormModel().getValueModel(TipoSedeEntita.PROP_SEDE_PRINCIPALE).setValue(Boolean.FALSE);
			}
			getFormModel().getFieldMetadata(TipoSedeEntita.PROP_SEDE_PRINCIPALE).setReadOnly(
					tipoSede == null || tipoSede != TipoSede.NORMALE);
		}

	}

	private static final String FORM_ID = "tipoSedeEntitaForm";

	/**
	 * Csotruttore.
	 * 
	 * @param tipoSedeEntita
	 *            tipo sede entità
	 */
	public TipoSedeEntitaForm(final TipoSedeEntita tipoSedeEntita) {
		super(PanjeaFormModelHelper.createFormModel(tipoSedeEntita, false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final SwingBindingFactory bf = (SwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");

		builder.row();
		((JTextField) builder.add("codice", "align=left")[1]).setColumns(10);
		builder.row();
		((JTextField) builder.add("descrizione", "align=left")[1]).setColumns(25);
		builder.row();
		builder.add(TipoSedeEntita.PROP_TIPO_SEDE, "align=left");
		builder.row();
		builder.add("sedePrincipale", "align=left");

		addFormValueChangeListener(TipoSedeEntita.PROP_TIPO_SEDE, new SedePrincipalePropertyChange());
		addFormObjectChangeListener(new SedePrincipalePropertyChange());
		getFormModel().addPropertyChangeListener(FormModel.READONLY_PROPERTY, new SedePrincipalePropertyChange());

		return builder.getForm();
	}

}
