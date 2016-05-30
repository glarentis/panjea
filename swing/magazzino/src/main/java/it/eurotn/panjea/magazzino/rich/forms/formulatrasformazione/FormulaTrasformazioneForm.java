/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.forms.formulatrasformazione;

import it.eurotn.panjea.magazzino.domain.FormulaTrasformazione;
import it.eurotn.panjea.magazzino.domain.TipoAttributo;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import org.apache.log4j.Logger;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

/**
 * Form di {@link FormulaTrasformazione}.
 * 
 * @author adriano
 * @version 1.0, 25/nov/2008
 * 
 */
public class FormulaTrasformazioneForm extends PanjeaAbstractForm {

	private static Logger logger = Logger.getLogger(FormulaTrasformazioneForm.class);

	private static final String FORM_ID = "formulaTrasformazioneForm";

	private final IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	/**
	 * Costruttore.
	 */
	public FormulaTrasformazioneForm() {
		super(PanjeaFormModelHelper.createFormModel(new FormulaTrasformazione(), false, FORM_ID), FORM_ID);
		this.magazzinoAnagraficaBD = (IMagazzinoAnagraficaBD) Application.instance().getApplicationContext()
				.getBean("magazzinoAnagraficaBD");
	}

	@Override
	protected JComponent createFormControl() {
		logger.debug("--> Enter createFormControl");
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:pref,4dlu,100dlu,left:pref:grow", "4dlu,default, 4dlu,fill:pref:grow");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // ,
																				// new
																				// FormDebugPanel());
		builder.setLabelAttributes("r, c");

		builder.addPropertyAndLabel("codice", 1, 2);

		List<TipoAttributo> tipiAttributo = magazzinoAnagraficaBD.caricaTipiAttributo();
		List<String> variabili = new ArrayList<String>();
		for (TipoAttributo tipoAttributo : tipiAttributo) {
			variabili.add(tipoAttributo.getCodiceFormula());
		}
		variabili.add("result = ");
		variabili.add(TipoAttributo.SEPARATORE_CODICE_FORMULA + "qta" + TipoAttributo.SEPARATORE_CODICE_FORMULA);
		ValueHolder variabiliValueHolder = new ValueHolder(variabili);

		builder.setLabelAttributes("r, t");
		builder.addLabel("formula", 1, 4);
		builder.addBinding(bf.createBoundCodeEditor("formula", variabiliValueHolder), 3, 4, 2, 1);

		logger.debug("--> Exit createFormControl");
		return builder.getPanel();
	}
}
