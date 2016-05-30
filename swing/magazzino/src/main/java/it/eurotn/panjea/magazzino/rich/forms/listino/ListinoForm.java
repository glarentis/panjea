/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.forms.listino;

import it.eurotn.panjea.anagrafica.domain.ValutaAzienda;
import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.magazzino.domain.Listino.ETipoListino;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;

import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

/**
 * Form che gestisce un listino.
 * 
 * @author fattazzo
 * 
 */
public class ListinoForm extends PanjeaAbstractForm {

	public class TipoListinoChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			ETipoListino tipoListino = evt == null ? ETipoListino.NORMALE : (ETipoListino) evt.getNewValue();
			ETipoListino tipoListinoOld = evt == null ? ETipoListino.NORMALE : (ETipoListino) evt.getOldValue();

			ETipoListino tipoListinoForVisible = tipoListino;

			if (!getFormModel().isReadOnly() && !((Listino) getFormModel().getFormObject()).isNew()
					&& tipoListinoOld == ETipoListino.SCAGLIONE && tipoListino == ETipoListino.NORMALE) {
				getFormModel().getValueModel("tipoListino").setValue(tipoListinoOld);
				tipoListinoForVisible = tipoListinoOld;
				new MessageDialog("ATTENZIONE", new DefaultMessage(
						"Non è possibile cambiare il tipo listino da SCAGLIONI a NORMALE", Severity.INFO)).showDialog();
			}

			for (JComponent componente : componentiListinoNormale) {
				componente.setVisible(tipoListinoForVisible == ETipoListino.NORMALE);
			}
		}

	}

	public static final String FORM_ID = "listinoForm";
	private JComponent[] componentiListinoNormale;

	/**
	 * Costruttore.
	 */
	public ListinoForm() {
		super(PanjeaFormModelHelper.createFormModel(new Listino(), false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:pref,4dlu,left:50dlu,10dlu,right:pref,4dlu,50dlu", "2dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
		builder.setLabelAttributes("r, c");
		builder.setComponentAttributes("f, f");
		builder.setRow(2);
		componentiListinoNormale = new JComponent[4];

		builder.addPropertyAndLabel("codice", 1, 2);
		builder.addPropertyAndLabel("tipoListino", 5);
		builder.addPropertyAndLabel("descrizione", 1, 4, 5);
		builder.addLabel("codiceValuta", 1, 6);
		builder.addBinding(bf.createBoundSearchText("codiceValuta", null, ValutaAzienda.class), 3, 6);
		builder.addPropertyAndLabel("iva", 1, 8);
		JComponent[] ultimoCosto = builder.addPropertyAndLabel("aggiornaDaUltimoCosto", 5, 8);
		componentiListinoNormale[0] = ultimoCosto[0];
		componentiListinoNormale[1] = ultimoCosto[1];

		componentiListinoNormale[2] = builder.addLabel("listinoBase", 1, 10);
		Binding bindingListino = bf.createBoundSearchText("listinoBase", new String[] { "codice", "descrizione" });
		SearchPanel searchPanelListino = ((SearchPanel) bindingListino.getControl());
		searchPanelListino.getTextFields().get("codice").setColumns(5);
		builder.addBinding(bindingListino, 3, 10, 5, 1);
		componentiListinoNormale[3] = searchPanelListino;

		getValueModel("tipoListino").addValueChangeListener(new TipoListinoChangeListener());

		// aggiungo il listener per rendere editabile il codice del listino solamente se ne sto inserendo uno nuovo
		getFormModel().addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (getFormModel().getFormObject() != null
						&& ((Listino) getFormModel().getFormObject()).getId() != null) {
					ListinoForm.this.getFormModel().getFieldMetadata("codice").setReadOnly(true);
				} else {
					ListinoForm.this.getFormModel().getFieldMetadata("codice").setReadOnly(false);
				}
			}
		});

		return builder.getPanel();
	}
}
