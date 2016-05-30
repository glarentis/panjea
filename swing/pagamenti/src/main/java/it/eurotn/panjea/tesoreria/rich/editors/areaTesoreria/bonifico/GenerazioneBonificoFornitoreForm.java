/**
 *
 */
package it.eurotn.panjea.tesoreria.rich.editors.areaTesoreria.bonifico;

import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.tesoreria.domain.AreaPagamenti;
import it.eurotn.panjea.tesoreria.domain.Pagamento;
import it.eurotn.rich.control.table.JideTableWidget;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.awt.Dimension;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

/**
 * @author leonardo
 */
public class GenerazioneBonificoFornitoreForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "generazioneBonificoFornitoreForm";

	private JideTableWidget<Pagamento> pagamentiTableWidget;

	private Set<Pagamento> pagamenti;

	/**
	 * costruttore di default.
	 */
	public GenerazioneBonificoFornitoreForm() {
		super(PanjeaFormModelHelper.createFormModel(new AreaPagamenti(), false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:pref,4dlu,left:pref,10dlu,left:default:grow", "2dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel()
		builder.setLabelAttributes("l,c");

		builder.setRow(2);

		builder.addPropertyAndLabel("documento.dataDocumento");
		builder.nextRow();

		JTextField numDoc = (JTextField) builder.addPropertyAndLabel("documento.codice.codice")[1];
		numDoc.setColumns(8);
		builder.nextRow();

		((JTextField) builder.addPropertyAndLabel("speseIncasso")[1]).setColumns(8);
		builder.nextRow();

		pagamentiTableWidget = new JideTableWidget<Pagamento>("pagamentiTableWidget",
				new PagamentiBonificoFornitoreTableModel());

		// pagamentiTableWidget.getTable().getTableHeader().setReorderingAllowed(false);
		// SortableTableModel sortableTableModel = (SortableTableModel) TableModelWrapperUtils.getActualTableModel(
		// pagamentiTableWidget.getTable().getModel(), SortableTableModel.class);
		// sortableTableModel.sortColumn(2, true, false);

		JComponent tableComponent = pagamentiTableWidget.getComponent();
		tableComponent.setPreferredSize(new Dimension(600, 200));
		builder.addComponent(tableComponent, 1, 8, 5, 1);

		return builder.getPanel();
	}

	/**
	 * @return pagamenti
	 */
	public Set<Pagamento> getPagamenti() {
		return new TreeSet<>(pagamentiTableWidget.getRows());
	}

	@Override
	public void setFormObject(Object formObject) {
		AreaPagamenti areaPagamenti = (AreaPagamenti) formObject;
		pagamenti = areaPagamenti.getPagamenti();

		AreaPagamenti nuovaArea = new AreaPagamenti();
		super.setFormObject(nuovaArea);

		pagamentiTableWidget.setRows(pagamenti);
	}

}