/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.forms;

import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.magazzino.domain.TipoAttributo;
import it.eurotn.panjea.magazzino.domain.TipoAttributo.ETipoDatoTipoAttributo;
import it.eurotn.panjea.magazzino.rich.DescrizioniEntityPanel;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;

/**
 * @author fattazzo
 * 
 */
public class TipoAttributoForm extends PanjeaAbstractForm {

	private class NumeroDecimaliChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {

			if (getFormModel().isReadOnly()) {
				return;
			}

			ETipoDatoTipoAttributo tipoDato = (ETipoDatoTipoAttributo) getValueModel("tipoDato").getValue();

			getFormModel().getFieldMetadata("numeroDecimali").setReadOnly(
					tipoDato == null || tipoDato != ETipoDatoTipoAttributo.NUMERICO);
		}

	}

	public static final String FORM_ID = "tipoAttributoForm";

	private final IAnagraficaTabelleBD anagraficaTabelleBD;

	private AziendaCorrente aziendaCorrente;

	private NumeroDecimaliChangeListener numeroDecimaliChangeListener;

	/**
	 * Costruttore.
	 * 
	 * @param anagraficaTabelleBD
	 *            anagraficaTabellaBD
	 */
	public TipoAttributoForm(final IAnagraficaTabelleBD anagraficaTabelleBD) {
		super(PanjeaFormModelHelper.createFormModel(new TipoAttributo(), false, FORM_ID + "Model"), FORM_ID);
		this.aziendaCorrente = RcpSupport.getBean(AziendaCorrente.BEAN_ID);
		this.anagraficaTabelleBD = anagraficaTabelleBD;
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:90dlu,4dlu,f:70dlu,10dlu,right:90dlu,4dlu,40dlu,",
				"4dlu,default,4dlu,default,4dlu,default,4dlu,default,4dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel()
		builder.setLabelAttributes("r, c");

		builder.addPropertyAndLabel("codice", 1, 2);

		builder.addPropertyAndLabel("tipoDato", 1, 4);

		builder.addPropertyAndLabel("totalizzatoreTipoAttributo", 1, 6);

		builder.addLabel("unitaMisura", 1, 8);
		builder.addBinding(bf.createBoundSearchText("unitaMisura", new String[] { "codice" }), 3, 8);

		builder.addPropertyAndLabel("numeroDecimali", 5, 8);

		JLabel label = builder.addLabel("nome", 1, 10);
		label.setIcon(getIconSource().getIcon(this.aziendaCorrente.getLingua()));
		label.setHorizontalTextPosition(SwingConstants.LEFT);
		builder.addProperty("nome", 3, 10, 5, 1);

		builder.addComponent(new DescrizioniEntityPanel(getFormModel(), "nomiLingua", anagraficaTabelleBD), 1, 12, 7, 1);

		numeroDecimaliChangeListener = new NumeroDecimaliChangeListener();
		getValueModel("tipoDato").addValueChangeListener(numeroDecimaliChangeListener);
		addFormObjectChangeListener(numeroDecimaliChangeListener);
		getFormModel().addPropertyChangeListener(FormModel.READONLY_PROPERTY, numeroDecimaliChangeListener);

		return builder.getPanel();
	}

	/**
	 * @return the aziendaCorrente
	 */
	public AziendaCorrente getAziendaCorrente() {
		return aziendaCorrente;
	}

	/**
	 * @param aziendaCorrente
	 *            the aziendaCorrente to set
	 */
	public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
		this.aziendaCorrente = aziendaCorrente;
	}

}
