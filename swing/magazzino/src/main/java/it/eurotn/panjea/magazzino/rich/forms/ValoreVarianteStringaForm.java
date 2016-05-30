/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.forms;

import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.magazzino.domain.ValoreVarianteStringa;
import it.eurotn.panjea.magazzino.rich.DescrizioniEntityPanel;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.springframework.richclient.form.builder.TableFormBuilder;

/**
 * @author fattazzo
 * 
 */
public class ValoreVarianteStringaForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "valoreVarianteStringaForm";

	private IAnagraficaTabelleBD anagraficaTabelleBD;
	private AziendaCorrente aziendaCorrente;

	public ValoreVarianteStringaForm(IAnagraficaTabelleBD anagraficaTabelleBD, AziendaCorrente aziendaCorrente) {
		super(PanjeaFormModelHelper.createFormModel(new ValoreVarianteStringa(), false, FORM_ID), FORM_ID);
		this.anagraficaTabelleBD = anagraficaTabelleBD;
		this.aziendaCorrente = aziendaCorrente;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.richclient.form.AbstractForm#createFormControl()
	 */
	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");

		builder.row();
		builder.add("codice");
		builder.row();
		JLabel label = (JLabel) builder.add("valoreLinguaAziendale")[0];
		label.setIcon(getIconSource().getIcon(this.aziendaCorrente.getLingua()));
		label.setHorizontalTextPosition(SwingConstants.LEFT);
		builder.row();

		builder.getLayoutBuilder()
				.cell(new DescrizioniEntityPanel(getFormModel(), "valoriLingua", anagraficaTabelleBD));

		return builder.getForm();
	}
}
