/**
 *
 */
package it.eurotn.panjea.bi.rich.editors.chart.config;

import it.eurotn.panjea.bi.domain.AnalisiChartParametri;
import it.eurotn.rich.editors.FormsBackedTabbedDialogPageEditor;
import it.eurotn.rich.form.PanjeaAbstractForm;

import java.util.List;
import java.util.Locale;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;

import org.springframework.context.MessageSource;
import org.springframework.richclient.application.Application;

import com.jidesoft.swing.JideTabbedPane;

/**
 * Pannello che consente di editare le proprietà del grafico.
 *
 * @author fattazzo
 *
 */
public class ChartSettingsPage extends FormsBackedTabbedDialogPageEditor {

	private static final String PAGE_ID = "chartSettingsPage";

	private final AnalisiChartParametri analisiChartParametri;

	private final MessageSource messageSource = (MessageSource) Application.services().getService(MessageSource.class);

	/**
	 * Costruttore di default.
	 *
	 * @param analisiChartParametri
	 *            pagina che contiene i grafici.
	 */
	public ChartSettingsPage(final AnalisiChartParametri analisiChartParametri) {
		super(PAGE_ID, new ChartForm(analisiChartParametri));
		this.analisiChartParametri = analisiChartParametri;
		JTabbedPane tabbedPane = new JideTabbedPane();

		ChartFormsBuilder chartFormsBuilder = new ChartFormsBuilder(analisiChartParametri);
		List<PanjeaAbstractForm> listForms = chartFormsBuilder.createForms();

		for (PanjeaAbstractForm abstractForm : listForms) {
			tabbedPane.addTab(
					messageSource.getMessage(abstractForm.getId() + ".title", new Object[] {}, Locale.getDefault()),
					abstractForm.getControl());
		}
	}

	@Override
	public void addForms() {
		addForm(new BarSeriesForm(this.analisiChartParametri));
		addForm(new AxisForm(this.analisiChartParametri));
		addForm(new ColorSeriesForm(this.analisiChartParametri));
		setEnabled(true);
		setReadOnly(false);
		onLock();
	}

	@Override
	protected JComponent createToobar() {
		return null;
	}

	@Override
	public void loadData() {
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public void refreshData() {
	}

}
