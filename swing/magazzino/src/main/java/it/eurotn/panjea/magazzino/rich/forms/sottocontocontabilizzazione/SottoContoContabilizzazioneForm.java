package it.eurotn.panjea.magazzino.rich.forms.sottocontocontabilizzazione;

import it.eurotn.panjea.magazzino.domain.SottoContoContabilizzazione;
import it.eurotn.panjea.magazzino.domain.SottoContoContabilizzazione.ETipoEconomico;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;

import org.springframework.richclient.form.builder.TableFormBuilder;

public class SottoContoContabilizzazioneForm extends PanjeaAbstractForm {
	private static final String FORM_ID = "sottoContoContabilizzazioneForm";
	private ETipoEconomico tipoEconomico;

	/**
	 * Costruttore.
	 * 
	 * @param sottoContoContabilizzazione
	 *            {@link SottoContoContabilizzazione}
	 */
	public SottoContoContabilizzazioneForm(final SottoContoContabilizzazione sottoContoContabilizzazione) {
		super(PanjeaFormModelHelper.createFormModel(sottoContoContabilizzazione, false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");
		builder.add(bf.createBoundSearchText("categoriaContabileArticolo", new String[] { "codice" }, false));
		builder.row();
		builder.add(bf.createBoundSearchText("categoriaContabileDeposito", new String[] { "codice" }, false));
		builder.row();
		builder.add(bf.createBoundSearchText("categoriaContabileSedeMagazzino", new String[] { "codice" }, false));
		builder.row();
		builder.add("sottoConto");
		builder.row();
		builder.add("sottoContoNotaAccredito");
		return builder.getForm();
	}

	@Override
	protected Object createNewObject() {
		org.springframework.util.Assert.notNull(tipoEconomico);
		SottoContoContabilizzazione sottoContoContabilizzazione = new SottoContoContabilizzazione();
		sottoContoContabilizzazione.setTipoEconomico(tipoEconomico);
		return sottoContoContabilizzazione;
	}

	/**
	 * @param tipoEconomico
	 *            {@link ETipoEconomico} to set
	 */
	public void setTipoEconomico(ETipoEconomico tipoEconomico) {
		this.tipoEconomico = tipoEconomico;
	}

}
