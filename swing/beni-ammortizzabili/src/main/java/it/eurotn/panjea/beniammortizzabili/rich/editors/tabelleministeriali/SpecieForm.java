package it.eurotn.panjea.beniammortizzabili.rich.editors.tabelleministeriali;

import it.eurotn.panjea.beniammortizzabili2.domain.Specie;
import it.eurotn.panjea.contabilita.rich.search.SottoContoSearchObject;
import it.eurotn.panjea.contabilita.rich.search.SottoContoSearchTextField;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

/**
 *
 * @author Aracno
 * @version 1.0, 27/set/06
 *
 */
public class SpecieForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "specieForm";

	/**
	 * Costruttore.
	 *
	 * @param specie
	 *            specie
	 */
	public SpecieForm(final Specie specie) {
		super(PanjeaFormModelHelper.createFormModel(specie, false, FORM_ID), FORM_ID);

		// Aggiungo il value model che mi servirà solamente nella search text delle entità per cercare solo le entità
		// abilitate
		ValueModel sottocontoAbilitatoInRicercaValueModel = new ValueHolder(Boolean.TRUE);
		DefaultFieldMetadata sottocontoAbilitatoMetaData = new DefaultFieldMetadata(getFormModel(),
				new FormModelMediatingValueModel(sottocontoAbilitatoInRicercaValueModel), Boolean.class, true, null);
		getFormModel().add("sottocontoAbilitatoInRicerca", sottocontoAbilitatoInRicercaValueModel,
				sottocontoAbilitatoMetaData);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:pref,4dlu,left:pref:grow", "4dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanelNumbered());
		builder.setLabelAttributes("r, c");
		builder.setRow(2);

		((JTextField) builder.addPropertyAndLabel(Specie.PROP_CODICE, 1)[1]).setColumns(4);
		builder.nextRow();

		builder.addPropertyAndLabel(Specie.PROP_DESCRIZIONE, 1);
		builder.nextRow();

		builder.setComponentAttributes("f, f");
		builder.addHorizontalSeparator("Sottoconti", 3);
		builder.nextRow();

		builder.addLabel("sottocontiBeni.sottoContoAmmortamento");
		builder.addBinding(bf.createBoundSearchText("sottocontiBeni.sottoContoAmmortamento",
				new String[] { "descrizione" }, new String[] { "sottocontoAbilitatoInRicerca" },
				new String[] { SottoContoSearchObject.FILTRO_SOTTOCONTO_ABILITATO },
				SottoContoSearchTextField.class.getName()), 3);
		builder.nextRow();

		builder.addLabel("sottocontiBeni.sottoContoFondoAmmortamento");
		builder.addBinding(bf.createBoundSearchText("sottocontiBeni.sottoContoFondoAmmortamento",
				new String[] { "descrizione" }, new String[] { "sottocontoAbilitatoInRicerca" },
				new String[] { SottoContoSearchObject.FILTRO_SOTTOCONTO_ABILITATO },
				SottoContoSearchTextField.class.getName()), 3);
		builder.nextRow();

		builder.addLabel("sottocontiBeni.sottoContoAmmortamentoAnticipato");
		builder.addBinding(bf.createBoundSearchText("sottocontiBeni.sottoContoAmmortamentoAnticipato",
				new String[] { "descrizione" }, new String[] { "sottocontoAbilitatoInRicerca" },
				new String[] { SottoContoSearchObject.FILTRO_SOTTOCONTO_ABILITATO },
				SottoContoSearchTextField.class.getName()), 3);
		builder.nextRow();

		builder.addLabel("sottocontiBeni.sottoContoFondoAmmortamentoAnticipato");
		builder.addBinding(bf.createBoundSearchText("sottocontiBeni.sottoContoFondoAmmortamentoAnticipato",
				new String[] { "descrizione" }, new String[] { "sottocontoAbilitatoInRicerca" },
				new String[] { SottoContoSearchObject.FILTRO_SOTTOCONTO_ABILITATO },
				SottoContoSearchTextField.class.getName()), 3);

		return builder.getPanel();
	}

}
