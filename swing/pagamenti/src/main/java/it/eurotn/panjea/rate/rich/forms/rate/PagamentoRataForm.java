package it.eurotn.panjea.rate.rich.forms.rate;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.domain.lite.FornitoreLite;
import it.eurotn.panjea.partite.domain.TipoAreaPartita;
import it.eurotn.panjea.partite.rich.search.TipoAreaPartitaSearchObject;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.tesoreria.domain.AreaAcconto;
import it.eurotn.panjea.tesoreria.domain.Pagamento;
import it.eurotn.panjea.tesoreria.rich.bd.ITesoreriaBD;
import it.eurotn.panjea.tesoreria.util.ParametriCreazionePagamento;
import it.eurotn.panjea.tesoreria.util.parametriricerca.ParametriRicercaAcconti;
import it.eurotn.panjea.tesoreria.util.parametriricerca.ParametriRicercaAcconti.EStatoAcconto;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.selection.dialog.ListSelectionDialog;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.closure.Closure;

import com.jgoodies.forms.layout.FormLayout;

public class PagamentoRataForm extends PanjeaAbstractForm {

	private class AccontoPropertyChange implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {

			AreaAcconto areaAcconto = (AreaAcconto) evt.getNewValue();

			boolean visible = true;
			if (areaAcconto != null && areaAcconto.getId() != null) {
				visible = false;
			}

			getFormModel().getValueModel("tipoAreaPartita").setValue(null);
			componentTipoAreaPartita.setVisible(visible);
			labelTipoAreaPartita.setVisible(visible);

			updateAccontoControl();
		}
	}

	private class AggiungiAccontoCommand extends ApplicationWindowAwareCommand {

		public static final String COMMAND_ID = "aggiungiAccontoCommand";

		/**
		 * Costruttore.
		 */
		public AggiungiAccontoCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {

			EntitaLite entita = (EntitaLite) getFormModel().getValueModel("entita").getValue();
			Pagamento pagamento = (Pagamento) getFormModel().getValueModel("pagamento").getValue();

			ParametriRicercaAcconti parametri = new ParametriRicercaAcconti();
			parametri.setStatoAcconto(EStatoAcconto.APERTO);
			parametri.setEntita(entita);
			parametri.setCodiceValuta(pagamento.getRata().getImporto().getCodiceValuta());

			if (entita instanceof FornitoreLite) {
				parametri.setTipoEntita(TipoEntita.FORNITORE);
			} else if (entita instanceof ClienteLite) {
				parametri.setTipoEntita(TipoEntita.CLIENTE);
			}

			List<AreaAcconto> acconti = tesoreriaBD.caricaAcconti(parametri);

			ListSelectionDialog dialog = new ListSelectionDialog("Selezionare un acconto", acconti);
			dialog.setOnSelectAction(new Closure() {

				@Override
				public Object call(Object obj) {
					getFormModel().getValueModel("pagamento.areaAcconto").setValue(obj);
					return null;
				}
			});
			dialog.setRenderer(new DefaultListCellRenderer() {
				private static final long serialVersionUID = 1959578256124846041L;

				@Override
				public Component getListCellRendererComponent(JList jlist, Object obj, int i, boolean flag,
						boolean flag1) {
					JLabel label = (JLabel) super.getListCellRendererComponent(jlist, obj, i, flag, flag1);

					AreaAcconto areaAcconto = (AreaAcconto) obj;

					label.setText("Data: " + dateFormat.format(areaAcconto.getDocumento().getDataDocumento())
							+ "  Importo: "
							+ decimalFormat.format(areaAcconto.getDocumento().getTotale().getImportoInValuta()) + " "
							+ areaAcconto.getDocumento().getTotale().getCodiceValuta());

					return label;
				}
			});
			dialog.showDialog();
		}

	}

	private class DeleteAccontoCommand extends ApplicationWindowAwareCommand {

		public static final String COMMAND_ID = "deleteAccontoCommand";

		/**
		 * Costruttore.
		 */
		public DeleteAccontoCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			getFormModel().getValueModel("pagamento.areaAcconto").setValue(new AreaAcconto());
		}
	}

	/**
	 * Property change associato al tipoAreaContabile che visualizza il campo rapporto bancario se il tipo documento ha
	 * TipoEntita.BANCA.
	 * 
	 * @author Leonardo
	 */
	private class TipoAreaPartitaValueChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			logger.debug("--> Enter propertyChange");
			boolean visible = false;
			if (evt != null && evt.getNewValue() != null) {
				TipoAreaPartita tipoAreaPartita = (TipoAreaPartita) evt.getNewValue();
				TipoDocumento tipoDocumento = tipoAreaPartita.getTipoDocumento();
				if (tipoDocumento != null && tipoDocumento.getTipoEntita() != null) {
					// visualizzo rapporto bancario solo se il tipo documento
					// collegato al tipoAreaPartita e' di
					// TipoEntita.BANCA
					if (tipoDocumento.getTipoEntita().compareTo(TipoEntita.BANCA) == 0) {
						visible = true;
					}
				}
			}
			componentsBanca.setVisible(visible);
			labelBanca.setVisible(visible);
			logger.debug("--> Exit propertyChange");
		}
	}

	private static Logger logger = Logger.getLogger(PagamentoRataForm.class);
	private static final String FORM_ID = "pagamentoRataForm";

	private JComponent componentsBanca;
	private JComponent componentTipoAreaPartita;

	private JPanel accontoPanel = getComponentFactory().createPanel();
	private JLabel accontoLabel = null;
	private JLabel labelBanca;
	private JLabel labelTipoAreaPartita;

	private DateFormat dateFormat;
	private DecimalFormat decimalFormat;

	private final ITesoreriaBD tesoreriaBD;

	{
		dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		decimalFormat = new DecimalFormat("#,##0.00");
	}

	/**
	 * Costruttore default.
	 * 
	 * @param tesoreriaBD
	 *            tesoreriaBD
	 */
	public PagamentoRataForm(final ITesoreriaBD tesoreriaBD) {
		super(PanjeaFormModelHelper.createFormModel(new ParametriCreazionePagamento(), false, FORM_ID), FORM_ID);
		this.tesoreriaBD = tesoreriaBD;

		// Aggiungo al formModel il value model dinamico per la ricerca degli
		// acconti aperti
		ValueModel statoAccontoSearchValueModel = new ValueHolder(EStatoAcconto.APERTO);
		DefaultFieldMetadata metaData = new DefaultFieldMetadata(getFormModel(), new FormModelMediatingValueModel(
				statoAccontoSearchValueModel), EStatoAcconto.class, true, null);
		getFormModel().add("statoAccontoSearch", statoAccontoSearchValueModel, metaData);
	}

	/**
	 * Crea i controlli per la gestione dell'acconto.
	 */
	private void buildAccontoControl() {

		accontoPanel = getComponentFactory().createPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));

		AggiungiAccontoCommand aggiungiCommand = new AggiungiAccontoCommand();
		accontoPanel.add(aggiungiCommand.createButton());

		DeleteAccontoCommand deleteAccontoCommand = new DeleteAccontoCommand();
		accontoPanel.add(deleteAccontoCommand.createButton());

		accontoLabel = getComponentFactory().createLabel("");
		accontoPanel.add(accontoLabel);
	}

	@Override
	protected JComponent createFormControl() {

		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("left:pref, 4dlu,left:80dlu, left:pref:grow", "default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
		builder.setLabelAttributes("r,c");
		builder.nextRow();

		builder.addPropertyAndLabel("dataDocumento");
		builder.nextRow();

		builder.addLabel("areaAcconto", 1);
		buildAccontoControl();
		builder.addComponent(accontoPanel, 3, 3, 2, 1);
		updateAccontoControl();
		builder.nextRow();

		Binding bindingTipoDoc = bf.createBoundSearchText("tipoAreaPartita", new String[] { "tipoDocumento.codice",
				"tipoDocumento.descrizione" }, new String[] { "tipoPartita" },
				new String[] { TipoAreaPartitaSearchObject.PARAM_TIPO_PARTITA });
		SearchPanel componentTipoAreaContabile = (SearchPanel) bindingTipoDoc.getControl();
		componentTipoAreaContabile.getTextFields().get("tipoDocumento.codice").setColumns(5);
		componentTipoAreaContabile.getTextFields().get("tipoDocumento.descrizione").setColumns(14);
		labelTipoAreaPartita = builder.addLabel("tipoAreaPartita", 1);
		componentTipoAreaPartita = builder.addBinding(bindingTipoDoc, 3, 5, 2, 1);
		builder.nextRow();

		// rapporto bancario azienda
		Binding bindingBanca = bf.createBoundSearchText("rapportoBancarioAzienda", new String[] { "numero",
				"descrizione" }, new String[] {}, new String[] {});
		SearchPanel searchBanca = (SearchPanel) bindingBanca.getControl();
		searchBanca.getTextFields().get("numero").setColumns(5);
		searchBanca.getTextFields().get("descrizione").setColumns(14);
		labelBanca = builder.addLabel("rapportoBancarioAzienda", 1);
		componentsBanca = builder.addBinding(bindingBanca, 3, 7, 2, 1);
		builder.nextRow();

		// value change per visualizzare o meno il rapporto bancario a seconda
		// del tipo entita' del tipo documento
		TipoAreaPartitaValueChangeListener tipoAreaPartitaValueChangeListener = new TipoAreaPartitaValueChangeListener();
		this.addFormValueChangeListener("tipoAreaPartita", tipoAreaPartitaValueChangeListener);
		tipoAreaPartitaValueChangeListener.propertyChange(null);

		AccontoPropertyChange accontoPropertyChange = new AccontoPropertyChange();
		this.addFormValueChangeListener("pagamento.areaAcconto", accontoPropertyChange);

		builder.addLabel("pagamento.importo", 1);
		builder.setComponentAttributes("f,c");
		builder.addBinding(bf.createBoundImportoTextField("pagamento.importo"), 3);

		builder.nextRow();
		builder.addPropertyAndLabel("pagamento.chiusuraForzataRata", 1);

		JPanel rootPanel = builder.getPanel();
		rootPanel.setPreferredSize(new Dimension(600, 200));
		return rootPanel;
	}

	/**
	 * Aggiorna i controlli dell'acconto in base al suo valore.
	 */
	public void updateAccontoControl() {

		AreaAcconto areaAcconto = (AreaAcconto) getFormModel().getValueModel("pagamento.areaAcconto").getValue();

		if (areaAcconto != null && areaAcconto.getId() != null) {
			accontoLabel.setText("Data: " + dateFormat.format(areaAcconto.getDocumento().getDataDocumento())
					+ "  Importo: " + decimalFormat.format(areaAcconto.getDocumento().getTotale().getImportoInValuta())
					+ " Residuo: " + decimalFormat.format(areaAcconto.getResiduo()));
		} else {
			accontoLabel.setText("Nessun acconto selezionato.");
		}

	}
}
