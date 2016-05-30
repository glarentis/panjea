package it.eurotn.panjea.magazzino.rich.editors.articolo.componenti.configurazionedistinta;

import it.eurotn.panjea.anagrafica.domain.FaseLavorazioneArticolo;
import it.eurotn.panjea.magazzino.domain.Componente;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.TableEditableBinding;
import it.eurotn.rich.control.table.JideTableWidget;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.awt.Dimension;
import java.util.Set;

import javax.swing.JComponent;

import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class FasiLavorazioneForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "fasiLavorazioneForm";
	private JideTableWidget<FaseLavorazioneArticolo> tableWidget;
	private Integer numeroDecimaliQta;

	/**
	 * Costruttore.
	 */
	public FasiLavorazioneForm() {
		// Utilizzo il componnente come variabile di "appoggio" per il binding
		super(PanjeaFormModelHelper.createFormModel(new Componente(), false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("fill:pref:grow", "default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);// , new FormDebugPanel()
		builder.setLabelAttributes("r, c");

		FasiLavorazioneTableModel fasiLavorazioneTableModel = new FasiLavorazioneTableModel();
		fasiLavorazioneTableModel.setNumeroDecimaliQta(numeroDecimaliQta);
		TableEditableBinding<FaseLavorazioneArticolo> fasiLavorazioneBinding = new TableEditableBinding<FaseLavorazioneArticolo>(
				getFormModel(), "fasiLavorazioneArticolo", Set.class, fasiLavorazioneTableModel);
		fasiLavorazioneBinding.getControl().setPreferredSize(new Dimension(70, 150));
		tableWidget = fasiLavorazioneBinding.getTableWidget();
		builder.addBinding(fasiLavorazioneBinding);
		return builder.getPanel();
	}

	@Override
	public void setFormObject(Object formObject) {
	}
	
	/**
	 * @param numeroDecimaliQta
	 *            the numeroDecimaliQta to set
	 */
	public void setNumeroDecimaliQta(Integer numeroDecimaliQta) {
		this.numeroDecimaliQta = numeroDecimaliQta;
	}

	/**
	 * Mette in editazione la prima cella della tabella.
	 */
	public void startTableEditing() {
		if (tableWidget != null) {
			tableWidget.getTable().editCellAt(0, 0);
		}
	}

}