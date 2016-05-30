package it.eurotn.panjea.magazzino.rich.forms.listino;

import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.magazzino.domain.Listino.ETipoListino;
import it.eurotn.panjea.magazzino.domain.RigaListino;
import it.eurotn.panjea.magazzino.domain.ScaglioneComparator;
import it.eurotn.panjea.magazzino.domain.ScaglioneListino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoAnagraficaBD;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.util.DefaultNumberFormatterFactory;
import it.eurotn.rich.binding.TableEditableBinding;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.awt.Component;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.DefaultFormatterFactory;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;

public class RigaListinoForm extends PanjeaAbstractForm {

	private class ArticoloListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {

			if (getFormModel().isReadOnly()) {
				return;
			}

			ArticoloLite articoloLite = (ArticoloLite) getFormModel().getValueModel("articolo").getValue();
			if (articoloLite == null || articoloLite.isNew()) {
				return;
			}

			// Carico l'unita di misura se la riga è nuova
			// Nella ricerca di ArticoliLite e nel ArticoloConvert l'unità di misura non viene mai caricata
			// e non voglio appesantire la ricerca. Personalizzo il caricamento su questo form
			RigaListino riga = (RigaListino) getFormObject();
			if (riga.isNew()) {
				articoloLite = magazzinoAnagraficaBD.caricaArticoloLite(articoloLite.getId());
				getFormModel().getValueModel("articolo").setValueSilently(articoloLite, this);
			}

			boolean percRicaricoVisible = Boolean.FALSE;

			Integer numeroDecimaliQta = new Integer(2);

			if (articoloLite != null && articoloLite.getId() != null) {
				if (listino.getListinoBase() != null) {
					BigDecimal importo = magazzinoAnagraficaBD.caricaImportoListino(listino.getListinoBase().getId(),
							articoloLite.getId());
					getFormModel().getValueModel("importoBase").setValue(importo);
					// la scale è presa dal numero decimali prezzo della riga listino di base
					Integer numDecBase = importo != null ? importo.scale() : null;
					if (numDecBase != null) {
						numeroDecimaliDefault = numDecBase;
						updatePrezzoFormatter(prezzoField, numDecBase);
					}
					getFormModel().getValueModel("numeroDecimaliPrezzoBase").setValue(numDecBase);
					percRicaricoVisible = importo != null;
				}

				numeroDecimaliQta = articoloLite.getNumeroDecimaliQta();
			}
			scaglioneTableModel.setNumeroDecimaliQta(numeroDecimaliQta);
			updateListinoBaseControlVisibility(percRicaricoVisible);
		}

	}

	public class NumeroDecimaliBasePropertyChange implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getNewValue() == null) {
				return;
			}
			if (evt.getNewValue().equals(evt.getOldValue())) {
				return;
			}
			Integer numeroDecimali = (Integer) evt.getNewValue();
			updatePrezzoFormatter(prezzoBaseField, numeroDecimali);
			getFormModel().getValueModel("numeroDecimaliPrezzo").setValueSilently(numeroDecimali,
					numeroDecimaliPropertyChange);
		}
	}

	public class NumeroDecimaliPropertyChange implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getNewValue() == null) {
				return;
			}
			if (evt.getNewValue().equals(evt.getOldValue())) {
				return;
			}
			Integer numeroDecimali = (Integer) evt.getNewValue();

			if (listino.getTipoListino() == ETipoListino.NORMALE) {
				updatePrezzoFormatter(prezzoField, numeroDecimali);
				getFormModel().getValueModel("prezzo").setValueSilently(prezzoField.getValue(), prezzoChangeListener);
			}

			scaglioneTableModel.setNumeroDecimali(numeroDecimali);

			Integer numDecBase = (Integer) getFormModel().getValueModel("numeroDecimaliPrezzoBase").getValue();
			if (numDecBase == null) {
				numeroDecimaliDefault = numeroDecimali;
			}
		}
	}

	/**
	 * Listener per il cambio dell'oggetto nel form.
	 *
	 * @author giangi
	 * @version 1.0, 18/ago/2010
	 *
	 */
	public class ObjectChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			boolean nuovaRiga = ((RigaListino) evt.getNewValue()).isNew();
			boolean readOnly = !enabledArticolo;
			if (!readOnly) {
				readOnly = !nuovaRiga;
			}
			RigaListinoForm.this.getFormModel().getFieldMetadata("articolo").setReadOnly(readOnly);

			BigDecimal importoBase = (BigDecimal) getFormModel().getValueModel("importoBase").getValue();

			updateListinoBaseControlVisibility(importoBase != null);

			unitaMisuraLabel.setEditable(false);

			getApriStoriaPrezzoListinoCommand().setEnabled(((RigaListino) evt.getNewValue()).getId() != null);

			Integer numeroDecimaliQta = new Integer(2);
			ArticoloLite articolo = ((RigaListino) getFormObject()).getArticolo();
			if (articolo != null && !articolo.isNew()) {
				numeroDecimaliQta = articolo.getNumeroDecimaliQta();
			}
			scaglioneTableModel.setNumeroDecimaliQta(numeroDecimaliQta);
		}

	}

	private class PercentualeRicaricoListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {

			if (getFormModel().isReadOnly()) {
				return;
			}
			getFormModel().getValueModel("prezzo").removeValueChangeListener(prezzoChangeListener);

			BigDecimal importoBase = (BigDecimal) getFormModel().getValueModel("importoBase").getValue();
			if (importoBase != null && importoBase.compareTo(BigDecimal.ZERO) != 0) {
				((RigaListino) getFormObject()).applicaPercentualeRicarico(importoBase);
			}

			getFormModel().getValueModel("prezzo").setValueSilently(getFormModel().getValueModel("prezzo").getValue(),
					prezzoChangeListener);

			getFormModel().getValueModel("prezzo").addValueChangeListener(prezzoChangeListener);
		}

	}

	private class PrezzoChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {

			if (getFormModel().isReadOnly()) {
				return;
			}

			getFormModel().getValueModel("percentualeRicarico").removeValueChangeListener(percentualeRicaricoListener);

			BigDecimal percRicarico = BigDecimal.ZERO;

			if (evt.getNewValue() == null) {
				getFormModel().getValueModel("percentualeRicarico").setValueSilently(percRicarico,
						percentualeRicaricoListener);
				getFormModel().getValueModel("percentualeRicarico").addValueChangeListener(percentualeRicaricoListener);

				return;
			}
			if (evt.getOldValue() == null
					|| ((BigDecimal) evt.getNewValue()).compareTo((BigDecimal) evt.getOldValue()) != 0) {

				BigDecimal prezzo = (BigDecimal) getFormModel().getValueModel("prezzo").getValue();

				((RigaListino) getFormObject()).calcolaPercentualeRicarico(prezzo);

				getFormModel().getValueModel("percentualeRicarico").setValueSilently(
						getFormModel().getValueModel("percentualeRicarico").getValue(), percentualeRicaricoListener);
			}

			getFormModel().getValueModel("percentualeRicarico").addValueChangeListener(percentualeRicaricoListener);
		}
	}

	private class QtaScaglioneRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = -6515357931381244653L;

		private DecimalFormat format = new DecimalFormat();

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
				boolean hasFocus, int row, int column) {
			JLabel label = (JLabel) super
					.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			label.setHorizontalAlignment(SwingConstants.RIGHT);
			if (ScaglioneListino.MAX_SCAGLIONE.equals(value)) {
				label.setText("OLTRE");
			} else if (value instanceof Double) {

				Integer numDecimali = new Integer(2);
				if (((RigaListino) getFormObject()).getArticolo() != null) {
					numDecimali = ObjectUtils.defaultIfNull(((RigaListino) getFormObject()).getArticolo()
							.getNumeroDecimaliQta(), new Integer(2));
				}

				format.applyPattern("#,##0." + StringUtils.repeat("0", numDecimali));
				label.setText(format.format(value));
			}
			return label;
		}

	}

	private class ReadOnlyChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {

			getFormModel().getFieldMetadata("importoBase").setReadOnly(true);
			getFormModel().getFieldMetadata("numeroDecimaliPrezzoBase").setReadOnly(true);

			unitaMisuraLabel.setEditable(false);
		}
	}

	private class StoriaPrezzoListinoCommandInterceptor extends ActionCommandInterceptorAdapter {

		@Override
		public boolean preExecution(ActionCommand command) {
			Object rigaListino = getFormObject();
			command.addParameter(ApriStoriaPrezzoListinoCommand.PARAM_RIGA_LISTINO, rigaListino);

			return true;
		}
	}

	public static final String FORM_ID = "rigaListinoForm";

	private int numeroDecimaliDefault = 2;

	private Listino listino;

	private JTextField percRicaricoComponent;

	private JTextField numDecPrezzoBaseFiled;
	private JFormattedTextField prezzoBaseField;

	private JComponent[] listinoScaglioneComponents;
	private JComponent[] listinoNormaleComponents;

	private JFormattedTextField prezzoField;
	private JLabel importoBaseLabel;

	private JLabel percRicaricoLabel;
	private JLabel numDecPrezzoBaseLabel;
	private PrezzoChangeListener prezzoChangeListener = null;

	private PercentualeRicaricoListener percentualeRicaricoListener = null;
	private ArticoloListener articoloListener = null;
	private NumeroDecimaliBasePropertyChange numeroDecimaliBasePropertyChange = null;
	private NumeroDecimaliPropertyChange numeroDecimaliPropertyChange = null;
	private ReadOnlyChangeListener readOnlyChangeListener = null;
	private ObjectChangeListener objectChangeListener = null;
	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD = null;

	private JTextField unitaMisuraLabel;

	private ApriStoriaPrezzoListinoCommand apriStoriaPrezzoListinoCommand;

	private StoriaPrezzoListinoCommandInterceptor storiaPrezzoListinoCommandInterceptor;
	private boolean enabledArticolo;

	private ScaglioneListinoTableModel scaglioneTableModel;

	/**
	 * Costruttore.
	 */
	public RigaListinoForm() {
		super(PanjeaFormModelHelper.createFormModel(new RigaListino(), false, FORM_ID), FORM_ID);
		this.magazzinoAnagraficaBD = RcpSupport.getBean(MagazzinoAnagraficaBD.BEAN_ID);
		enabledArticolo = true;
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"right:pref,4dlu,fill:40dlu, 10dlu, right:pref,4dlu,fill:40dlu,left:20dlu,fill:p:g", "4dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
		builder.setLabelAttributes("r, c");
		builder.setRow(2);

		builder.addLabel("articolo", 1);
		SearchPanel searchPanel = (SearchPanel) builder.addBinding(
				bf.createBoundSearchText("articolo", new String[] { "codice", "descrizione" }), 3, 2, 7, 1);
		searchPanel.getTextFields().get("codice").setColumns(5);
		searchPanel.getTextFields().get("descrizione").setColumns(18);
		builder.nextRow();

		builder.addLabel("articolo.unitaMisura.codice", 1);
		unitaMisuraLabel = (JTextField) builder.addProperty("articolo.unitaMisura.codice", 3);
		unitaMisuraLabel.setFocusable(false);
		builder.nextRow();

		JTextField numDecPrezzoFiled = (JTextField) builder.addPropertyAndLabel("numeroDecimaliPrezzo", 1)[1];
		numDecPrezzoFiled.setHorizontalAlignment(SwingConstants.RIGHT);

		listinoNormaleComponents = new JComponent[3];
		listinoNormaleComponents[0] = builder.addLabel("prezzo", 5);
		prezzoField = (JFormattedTextField) builder.addBinding(bf.createBoundFormattedTextField("prezzo",
				getFactory((Integer) getValueModel("numeroDecimaliPrezzo").getValue())), 7);
		prezzoField.setColumns(10);
		prezzoField.setHorizontalAlignment(SwingConstants.RIGHT);
		listinoNormaleComponents[1] = prezzoField;
		listinoNormaleComponents[2] = getApriStoriaPrezzoListinoCommand().createButton();
		builder.addComponent(listinoNormaleComponents[2], 8);

		builder.nextRow();

		listinoScaglioneComponents = new JComponent[2];
		scaglioneTableModel = new ScaglioneListinoTableModel();
		TableEditableBinding<ScaglioneListino> scaglioneBinding = new TableEditableBinding<ScaglioneListino>(
				getFormModel(), "scaglioni", Set.class, scaglioneTableModel, new ScaglioneComparator());
		scaglioneBinding.getControl().setPreferredSize(new Dimension(100, 150));
		scaglioneBinding.getTableWidget().getTable().getColumnModel().getColumn(0)
		.setCellRenderer(new QtaScaglioneRenderer());

		builder.setLabelAttributes("r, t");
		listinoScaglioneComponents[0] = builder.addLabel("scaglioni", 1);
		listinoScaglioneComponents[1] = scaglioneBinding.getControl();
		listinoScaglioneComponents[0].setVisible(false);
		listinoScaglioneComponents[1].setVisible(false);
		builder.addBinding(scaglioneBinding, 3, 8, 6, 1);

		builder.setLabelAttributes("r, c");

		builder.nextRow();

		percRicaricoLabel = builder.addLabel("percentualeRicarico", 1);
		percRicaricoComponent = (JTextField) builder.addProperty("percentualeRicarico", 3);
		builder.nextRow();

		JComponent[] numDecimaliBaseComponent = builder.addPropertyAndLabel("numeroDecimaliPrezzoBase", 1);
		numDecPrezzoBaseLabel = (JLabel) numDecimaliBaseComponent[0];
		numDecPrezzoBaseFiled = (JTextField) numDecimaliBaseComponent[1];
		numDecPrezzoBaseFiled.setHorizontalAlignment(SwingConstants.RIGHT);

		importoBaseLabel = builder.addLabel("importoBase", 5);
		prezzoBaseField = (JFormattedTextField) builder.addBinding(
				bf.createBoundFormattedTextField("importoBase",
						getFactory((Integer) getValueModel("numeroDecimaliPrezzoBase").getValue())), 7);
		prezzoBaseField.setColumns(10);
		prezzoBaseField.setHorizontalAlignment(SwingConstants.RIGHT);

		// change listeners
		getValueModel("numeroDecimaliPrezzo").addValueChangeListener(getNumeroDecimaliPropertyChange());
		getValueModel("numeroDecimaliPrezzoBase").addValueChangeListener(getNumeroDecimaliBasePropertyChange());
		getValueModel("prezzo").addValueChangeListener(getPrezzoChangeListener());
		getValueModel("percentualeRicarico").addValueChangeListener(getPercentualeRicaricoListener());
		getValueModel("articolo").addValueChangeListener(getArticoloListener());

		addFormObjectChangeListener(getObjectChangeListener());
		getFormModel().addPropertyChangeListener(FormModel.READONLY_PROPERTY, getReadOnlyChangeListener());

		return builder.getPanel();
	}

	@Override
	protected Object createNewObject() {
		RigaListino rigaListino = new RigaListino();
		rigaListino.setNumeroDecimaliPrezzo(numeroDecimaliDefault);
		scaglioneTableModel.setRigaListino(rigaListino);
		return rigaListino;
	}

	@Override
	public void dispose() {
		getValueModel("numeroDecimaliPrezzo").removeValueChangeListener(getNumeroDecimaliPropertyChange());
		getValueModel("numeroDecimaliPrezzoBase").removeValueChangeListener(getNumeroDecimaliBasePropertyChange());
		getValueModel("prezzo").removeValueChangeListener(getPrezzoChangeListener());
		getValueModel("percentualeRicarico").removeValueChangeListener(getPercentualeRicaricoListener());
		getValueModel("articolo").removeValueChangeListener(getArticoloListener());

		removeFormObjectChangeListener(getObjectChangeListener());
		getFormModel().removePropertyChangeListener(FormModel.READONLY_PROPERTY, getReadOnlyChangeListener());
		super.dispose();
	}

	/**
	 * @return Returns the apriStoriaPrezzoListinoCommand.
	 */
	public ApriStoriaPrezzoListinoCommand getApriStoriaPrezzoListinoCommand() {
		if (apriStoriaPrezzoListinoCommand == null) {
			apriStoriaPrezzoListinoCommand = new ApriStoriaPrezzoListinoCommand();
			apriStoriaPrezzoListinoCommand.addCommandInterceptor(getStoriaPrezzoListinoCommandInterceptor());
		}

		return apriStoriaPrezzoListinoCommand;
	}

	/**
	 * @return the ArticoloListener to get
	 */
	private ArticoloListener getArticoloListener() {
		if (articoloListener == null) {
			articoloListener = new ArticoloListener();
		}
		return articoloListener;
	}

	/**
	 * @param numeroDecimali
	 *            numeroDecimali
	 * @return DefaultFormatterFactory
	 */
	private DefaultFormatterFactory getFactory(Integer numeroDecimali) {
		DefaultFormatterFactory factory = new DefaultNumberFormatterFactory("###,###,###,##0", numeroDecimali,
				BigDecimal.class);
		return factory;
	}

	/**
	 * @return the NumeroDecimaliBasePropertyChange to get
	 */
	private NumeroDecimaliBasePropertyChange getNumeroDecimaliBasePropertyChange() {
		if (numeroDecimaliBasePropertyChange == null) {
			numeroDecimaliBasePropertyChange = new NumeroDecimaliBasePropertyChange();
		}
		return numeroDecimaliBasePropertyChange;
	}

	/**
	 * @return the NumeroDecimaliPropertyChange to get
	 */
	private NumeroDecimaliPropertyChange getNumeroDecimaliPropertyChange() {
		if (numeroDecimaliPropertyChange == null) {
			numeroDecimaliPropertyChange = new NumeroDecimaliPropertyChange();
		}
		return numeroDecimaliPropertyChange;
	}

	/**
	 * @return the ObjectChangeListener to get
	 */
	private ObjectChangeListener getObjectChangeListener() {
		if (objectChangeListener == null) {
			objectChangeListener = new ObjectChangeListener();
		}
		return objectChangeListener;
	}

	/**
	 * @return the PercentualeRicaricoListener to get
	 */
	private PercentualeRicaricoListener getPercentualeRicaricoListener() {
		if (percentualeRicaricoListener == null) {
			percentualeRicaricoListener = new PercentualeRicaricoListener();
		}
		return percentualeRicaricoListener;
	}

	/**
	 * @return the PrezzoChangeListener to get
	 */
	private PrezzoChangeListener getPrezzoChangeListener() {
		if (prezzoChangeListener == null) {
			prezzoChangeListener = new PrezzoChangeListener();
		}
		return prezzoChangeListener;
	}

	/**
	 * @return the ReadOnlyChangeListener to get
	 */
	private ReadOnlyChangeListener getReadOnlyChangeListener() {
		if (readOnlyChangeListener == null) {
			readOnlyChangeListener = new ReadOnlyChangeListener();
		}
		return readOnlyChangeListener;
	}

	/**
	 * @return Returns the storiaPrezzoListinoCommandInterceptor.
	 */
	public StoriaPrezzoListinoCommandInterceptor getStoriaPrezzoListinoCommandInterceptor() {
		if (storiaPrezzoListinoCommandInterceptor == null) {
			storiaPrezzoListinoCommandInterceptor = new StoriaPrezzoListinoCommandInterceptor();
		}

		return storiaPrezzoListinoCommandInterceptor;
	}

	/**
	 *
	 * @param paramEnabledArticolo
	 *            false rende sempre non editabile l'articolo
	 */
	public void setArticoloEnabled(boolean paramEnabledArticolo) {
		this.enabledArticolo = paramEnabledArticolo;
	}

	@Override
	public void setFormObject(Object formObject) {
		super.setFormObject(formObject);
		if (isControlCreated()) {
			scaglioneTableModel.setRigaListino((RigaListino) formObject);
		}
	}

	/**
	 * Setta il listino corrente.
	 *
	 * @param listino
	 *            listino
	 */
	public void setListino(Listino listino) {
		this.listino = listino;
		for (JComponent component : listinoScaglioneComponents) {
			component.setVisible(listino.getTipoListino() == ETipoListino.SCAGLIONE);
		}
		for (JComponent component : listinoNormaleComponents) {
			component.setVisible(listino.getTipoListino() == ETipoListino.NORMALE);
		}
	}

	/**
	 * Aggiorna lo stato visible dei controlli per il listino base di riferimento.
	 *
	 * @param visible
	 *            true o false
	 */
	public void updateListinoBaseControlVisibility(boolean visible) {
		percRicaricoLabel.setVisible(visible);
		percRicaricoComponent.setVisible(visible);

		numDecPrezzoBaseLabel.setVisible(visible);
		numDecPrezzoBaseFiled.setVisible(visible);

		importoBaseLabel.setVisible(visible);
		prezzoBaseField.setVisible(visible);
	}

	/**
	 * formatta la casella di testo.
	 *
	 * @param importoControl
	 *            il control di cui aggiornare la formattazione
	 * @param numeroDecimali
	 *            numeri decimali con i quali formattare la casella di testo.
	 */
	private void updatePrezzoFormatter(JFormattedTextField importoControl, Integer numeroDecimali) {
		DefaultFormatterFactory factory = new DefaultNumberFormatterFactory("###,###,###,##0", numeroDecimali,
				BigDecimal.class);
		importoControl.setFormatterFactory(factory);
	}
}