package it.eurotn.panjea.lotti.rich.forms;

import it.eurotn.panjea.lotti.domain.Lotto;
import it.eurotn.panjea.lotti.domain.LottoInterno;
import it.eurotn.panjea.lotti.rich.bd.ILottiBD;
import it.eurotn.panjea.lotti.rich.bd.LottiBD;
import it.eurotn.panjea.lotti.rich.search.LottoSearchObject;
import it.eurotn.panjea.lotti.util.StatisticaLotto;
import it.eurotn.panjea.lotti.util.StatisticaLotto.StatoLotto;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.control.table.JideTableWidget;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.awt.Dimension;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.grid.SortableTableModel;
import com.jidesoft.grid.TableModelWrapperUtils;

public class LottoForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "lottoForm";
	public static final String FORMMODEL_ID = "lottoFormModel";

	private ILottiBD lottiBD;

	private JideTableWidget<Lotto> lottiTableWidget;

	/**
	 * Costruttore.
	 * 
	 * @param lotto
	 *            lotto da gestire
	 * 
	 */
	public LottoForm(final Lotto lotto) {
		super(PanjeaFormModelHelper.createFormModel(lotto, false, FORMMODEL_ID), FORM_ID);
		lottiBD = RcpSupport.getBean(LottiBD.BEAN_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"right:pref,4dlu,left:50dlu,10dlu,right:pref,4dlu,left:40dlu,left:default:grow,10dlu",
				"10dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,fill:default:grow");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanelNumbered());
		builder.setLabelAttributes("r,c");

		builder.setRow(2);

		builder.addLabel("articolo", 1);
		JLabel articoloLabel = new JLabel();
		articoloLabel.setIcon(RcpSupport.getIcon(Articolo.class.getName()));
		articoloLabel.setText(ObjectConverterManager.toString(getFormModel().getValueModel("articolo").getValue()));
		builder.addComponent(articoloLabel, 3, 2, 7, 1);
		builder.nextRow();
		builder.addPropertyAndLabel("codice", 1);
		builder.nextRow();
		builder.addPropertyAndLabel("dataScadenza", 1);
		builder.addPropertyAndLabel("priorita", 5);
		builder.nextRow();

		if (getFormObject() instanceof LottoInterno) {
			builder.addLabel("lotto", 1);
			Binding lottoBinding = bf.createBoundSearchText("lotto", new String[] { "codice" },
					new String[] { "articolo" }, new String[] { LottoSearchObject.ARTICOLO_KEY });
			builder.addBinding(lottoBinding, 3);
			builder.nextRow();
		}

		builder.addHorizontalSeparator("Lotti aperti presenti", 7);
		builder.nextRow();
		builder.nextRow();

		lottiTableWidget = new JideTableWidget<Lotto>("lottiTableWidget", new LottoTableModel(lottiBD));
		lottiTableWidget.getTable().getTableHeader().setReorderingAllowed(false);
		SortableTableModel sortableTableModel = (SortableTableModel) TableModelWrapperUtils.getActualTableModel(
				lottiTableWidget.getTable().getModel(), SortableTableModel.class);
		sortableTableModel.sortColumn(2, true, false);

		JComponent tableComponent = lottiTableWidget.getComponent();
		tableComponent.setPreferredSize(new Dimension(200, 200));
		builder.addComponent(tableComponent, 1, 12, 7, 1);

		return builder.getPanel();
	}

	@Override
	public void setFormObject(Object formObject) {
		super.setFormObject(formObject);

		Lotto lotto = (Lotto) formObject;

		Set<Lotto> lottiPresenti = new TreeSet<Lotto>();
		if (lotto != null && lotto.getArticolo() != null) {
			List<StatisticaLotto> statisticheLotto = lottiBD.caricaSituazioneLotti(lotto.getArticolo());
			for (StatisticaLotto statisticaLotto : statisticheLotto) {
				if (statisticaLotto.getStatoLotto() == StatoLotto.APERTO) {
					lottiPresenti.add(statisticaLotto.getLotto());
				}
			}
		}

		lottiTableWidget.setRows(lottiPresenti);
	}

}
