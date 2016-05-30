package it.eurotn.panjea.magazzino.rich.forms.tipoporto;

import it.eurotn.panjea.magazzino.domain.TipoPorto;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.form.builder.TableFormBuilder;

public class TipoPortoForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "tipoPortoForm";
	private PluginManager pluginManager = null;

	/**
	 * Costruttore.
	 * 
	 * @param tipoPorto
	 *            {@link TipoPorto}
	 */
	public TipoPortoForm(final TipoPorto tipoPorto) {
		super(PanjeaFormModelHelper.createFormModel(tipoPorto, false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.row();

		builder.setLabelAttributes("colGrId=label colSpec=right:pref");
		builder.row();

		((JTextField) builder.add("descrizione", "align=left")[1]).setColumns(30);
		builder.row();

		pluginManager = (PluginManager) Application.instance().getApplicationContext().getBean("pluginManager");
		if (pluginManager.isPresente(PluginManager.PLUGIN_INTRA)) {
			builder.add("gruppoCondizioneConsegna", "align=left");
			builder.row();
		}

		return builder.getForm();
	}

}
