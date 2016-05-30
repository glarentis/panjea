package it.eurotn.panjea.pagamenti.rich.tabelle;

import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.pagamenti.rich.bd.IAnagraficaPagamentiBD;
import it.eurotn.panjea.partite.rich.bd.IPartiteBD;
import it.eurotn.panjea.partite.rich.tabelle.codicepagamento.RigheStrutturaPartiteTablePage;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.binding.value.ValueModel;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class CodicePagamentoForm extends PanjeaAbstractForm implements PropertyChangeListener {

	public class PercentualeScontoPropertyChange implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getNewValue() != evt.getOldValue()) {
				BigDecimal percSconto = (BigDecimal) evt.getNewValue();
				if (percSconto == null || percSconto.compareTo(BigDecimal.ZERO) == 0) {
					getValueModel("giorniLimite").setValue(null);
				}
			}
		}

	}

	private static final String FORM_ID = "codicePagamentoForm";
	private RigheStrutturaPartiteTablePage righeStrutturaPartiteTablePage;

	private IAnagraficaPagamentiBD anagraficaPagamentiBD;
	private IPartiteBD partiteBD;

	/**
	 * Costruttore.
	 * 
	 * @param codicePagamento
	 *            codice di pagamento
	 */
	public CodicePagamentoForm(final CodicePagamento codicePagamento) {
		super(PanjeaFormModelHelper.createFormModel(new CodicePagamento(), false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"right:pref,4dlu,fill:50dlu, 10dlu, right:pref,4dlu,left:default, fill:default:grow",
				"2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default,4dlu,fill:default:grow");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // ,
		// new
		// FormDebugPanel());
		builder.setLabelAttributes("r, c");

		builder.nextRow();
		builder.setRow(2);

		builder.addPropertyAndLabel("codicePagamento");
		builder.nextRow();

		builder.addPropertyAndLabel("descrizione", 5, 2);
		builder.nextRow();

		((JTextField) builder.addPropertyAndLabel("importoSpese", 1)[1]).setColumns(7);
		builder.addPropertyAndLabel("descrizionePos", 5);
		builder.nextRow();

		((JTextField) builder.addPropertyAndLabel("percentualeSconto", 1)[1]).setColumns(4);
		((JTextField) builder.addPropertyAndLabel("giorniLimite", 5)[1]).setColumns(4);
		builder.nextRow();

		builder.addPropertyAndLabel("tipologiaPartita", 1);
		builder.addPropertyAndLabel("abilitato", 5);
		builder.nextRow();

		builder.addPropertyAndLabel("percentualeScontoCommerciale", 1);
		builder.addPropertyAndLabel("contrassegno", 5);
		builder.nextRow();

		builder.addLabel("tipoAreaPartitaPredefinitaPerPagamenti", 1);

		Binding bindingTipoDoc = bf.createBoundSearchText("tipoAreaPartitaPredefinitaPerPagamenti", new String[] {
				"tipoDocumento.codice", "tipoDocumento.descrizione" });
		SearchPanel searchTextTipoAreaCont = (SearchPanel) builder.addBinding(bindingTipoDoc, 3, 12, 5, 1);
		searchTextTipoAreaCont.getTextFields().get("tipoDocumento.codice").setColumns(5);
		searchTextTipoAreaCont.getTextFields().get("tipoDocumento.descrizione").setColumns(18);
		builder.nextRow();

		builder.addComponent(getRigheStrutturaPartiteTablePage().getControl(), 1, 14, 8, 1);

		addFormObjectChangeListener(this);
		getValueModel("percentualeSconto").addValueChangeListener(new PercentualeScontoPropertyChange());
		return builder.getPanel();
	}

	/**
	 * @return Returns the anagraficaPagamentiBD.
	 */
	public IAnagraficaPagamentiBD getPagamentiBD() {
		return anagraficaPagamentiBD;
	}

	/**
	 * @return Returns the partiteBD.
	 */
	public IPartiteBD getPartiteBD() {
		return partiteBD;
	}

	/**
	 * @return pagina delel righe struttura
	 */
	public RigheStrutturaPartiteTablePage getRigheStrutturaPartiteTablePage() {
		if (righeStrutturaPartiteTablePage == null) {
			ValueModel vm = getFormModel().getValueModel("strutturePartita");
			righeStrutturaPartiteTablePage = new RigheStrutturaPartiteTablePage(vm, partiteBD);
			righeStrutturaPartiteTablePage.setShowTitlePane(false);
		}
		return righeStrutturaPartiteTablePage;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// ATTENZIONE la gestione delle strutture e' fatta per RIFERIMENTO!!!
		// Al cambio del form object devo ricaricare le strutture partite del
		// nuovo codice pagamento
		getRigheStrutturaPartiteTablePage().loadData();
	}

	/**
	 * @param anagraficaPagamentiBD
	 *            The anagraficaPagamentiBD to set.
	 */
	public void setAnagraficaPagamentiBD(IAnagraficaPagamentiBD anagraficaPagamentiBD) {
		this.anagraficaPagamentiBD = anagraficaPagamentiBD;
	}

	/**
	 * @param partiteBD
	 *            The partiteBD to set.
	 */
	public void setPartiteBD(IPartiteBD partiteBD) {
		this.partiteBD = partiteBD;
	}

}
