/**
 * 
 */
package it.eurotn.panjea.contabilita.rich.forms;

import it.eurotn.panjea.anagrafica.rich.forms.AltriDatiEntitaForm;
import it.eurotn.panjea.contabilita.rich.pm.EntitaSottoContoPM;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.binding.searchtext.SearchTextBinder;
import it.eurotn.rich.binding.searchtext.SearchTextField;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.swing.SwingBindingFactory;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

/**
 * 
 * 
 * @author adriano
 * @version 1.0, 11/giu/07
 * 
 */
public class EntitaSottoContoForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "entitaSottoContoForm";

	/**
	 * 
	 * Costruttore.
	 * 
	 * @param entitaSottoContoPM
	 *            entità sotto sconto
	 */
	public EntitaSottoContoForm(final EntitaSottoContoPM entitaSottoContoPM) {
		super(PanjeaFormModelHelper.createFormModel(entitaSottoContoPM, false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(AltriDatiEntitaForm.COLUMN_SIZE + "dlu, 4dlu, fill:70dlu, fill:100dlu",
				"6dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel());

		builder.setLabelAttributes("r,c");
		builder.setRow(2);

		builder.addHorizontalSeparator("Dati contabili", 4);
		builder.nextRow();

		Binding bindingConto = createSearchTextContoBinding("sottoConto.conto", new String[] { "contoCodice" },
				new String[] { "###.###" }, new String[] { "sottoTipoConto" }, new String[] { "sottoTipoConto" }, true);
		SearchTextField searchTextConto = ((SearchPanel) bindingConto.getControl()).getTextFields().get("contoCodice");
		searchTextConto.setColumns(10);

		builder.addLabel("sottoConto.conto", 1);
		builder.addBinding(bindingConto, 3, 4, 2, 1);
		builder.nextRow();

		return builder.getPanel();
	}

	/**
	 * Creo il binding manualmente senza appoggiarmi al bindersForPropertyTypes configurato nel panjea-context.xml per
	 * aggiungere il filtro personalizzato (ricerca conto per tipo conto entita'); non uso nemmeno
	 * {@link PanjeaSwingBindingFactory} dato che mi devo appoggiare direttamente a {@link SearchTextContoBinding} che
	 * da panjea-swing non ha visibilita' delle classi in contabilita'.
	 */
	private Binding createSearchTextContoBinding(String formPropertyPath, String[] renderedProperties,
			String[] maskProperties, String[] filterPropertyPaths, String[] filterNames, boolean hasNewCommand) {
		logger.debug("--> Enter createSearchTextContoBinding");
		SearchTextBinder binder = new SearchTextBinder(JTextField.class);
		Map<String, Object> context = new HashMap<String, Object>();
		context.put(SearchTextBinder.RENDERER_KEY, renderedProperties);
		context.put(SearchTextBinder.FILTERPROPERTYPATH_KEY, filterPropertyPaths);
		context.put(SearchTextBinder.FILTERNAME_KEY, filterNames);
		context.put(SearchTextBinder.SEARCH_TEXT_CLASS_KEY,
				"it.eurotn.panjea.contabilita.rich.search.ContoSearchTextField");
		Binding binding = binder.bind(getFormModel(), formPropertyPath, context);
		((SwingBindingFactory) getBindingFactory()).interceptBinding(binding);
		logger.debug("--> Exit createSearchTextContoBinding");
		return binding;
	}

}
