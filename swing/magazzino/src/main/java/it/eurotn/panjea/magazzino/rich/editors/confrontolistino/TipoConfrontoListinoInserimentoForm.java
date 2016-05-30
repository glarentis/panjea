/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.editors.confrontolistino;

import it.eurotn.panjea.magazzino.domain.Listino.ETipoListino;
import it.eurotn.panjea.magazzino.rich.search.ListinoSearchObject;
import it.eurotn.panjea.magazzino.util.parametriricerca.TipoConfronto;
import it.eurotn.panjea.magazzino.util.parametriricerca.TipoConfronto.EConfronto;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;

import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.AbstractFocussableForm;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

/**
 * @author fattazzo
 * 
 */
public class TipoConfrontoListinoInserimentoForm extends AbstractFocussableForm {

	private class ConfrontoPropertyChange implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {

			boolean visible = evt.getNewValue() != null && ((EConfronto) evt.getNewValue()) == EConfronto.LISTINO;

			listinoComponent.setVisible(visible);
		}

	}

	public static final String FORM_ID = "tipoConfrontoListinoInserimentoForm";

	private JComponent listinoComponent;

	/**
	 * Costruttore.
	 */
	public TipoConfrontoListinoInserimentoForm() {
		super(PanjeaFormModelHelper.createFormModel(new TipoConfronto(), false, FORM_ID), FORM_ID);

		// aggiungo la finta proprietà tipi listino per far si che la search text del listino mi selezioni solo quelli
		// normali e non a scaglioni
		ValueModel tipoListinoValueModel = new ValueHolder(ETipoListino.NORMALE);
		DefaultFieldMetadata tipiListinoData = new DefaultFieldMetadata(getFormModel(),
				new FormModelMediatingValueModel(tipoListinoValueModel), ETipoListino.class, true, null);
		getFormModel().add("tipoListino", tipoListinoValueModel, tipiListinoData);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("fill:90dlu,fill:100dlu", "default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel());
		builder.setLabelAttributes("l,c");

		Binding bindingTipo = bf.createBinding("confronto");
		builder.addBinding(bindingTipo, 1);

		Binding bindingListino = bf.createBoundSearchText("listino", new String[] { "codice" },
				new String[] { "tipoListino" }, new String[] { ListinoSearchObject.TIPO_LISTINO_KEY });
		((SearchPanel) bindingListino.getControl()).getTextFields().get("codice").setColumns(15);
		listinoComponent = builder.addBinding(bindingListino, 2);

		listinoComponent.setVisible(false);
		getValueModel("confronto").addValueChangeListener(new ConfrontoPropertyChange());
		return builder.getPanel();
	}

}
