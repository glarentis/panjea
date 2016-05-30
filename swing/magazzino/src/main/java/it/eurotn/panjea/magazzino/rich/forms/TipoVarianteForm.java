/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.forms;

import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.magazzino.domain.TipoAttributo.ETipoDatoTipoAttributo;
import it.eurotn.panjea.magazzino.domain.TipoVariante;
import it.eurotn.panjea.magazzino.domain.ValoreVariante;
import it.eurotn.panjea.magazzino.domain.ValoreVarianteNumerica;
import it.eurotn.panjea.magazzino.domain.ValoreVarianteStringa;
import it.eurotn.panjea.magazzino.rich.DescrizioniEntityPanel;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.CustomEnumListRenderer;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;
import org.springframework.binding.form.FormModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.swing.ComboBoxBinding;
import org.springframework.richclient.form.builder.TableFormBuilder;

/**
 * @author fattazzo
 * 
 */
public class TipoVarianteForm extends PanjeaAbstractForm {

	private class DeleteValoreVarianteCommand extends ApplicationWindowAwareCommand {

		public static final String COMMAND_ID = "deleteValoreVarianteCommand";

		/**
		 * Costruttore.
		 */
		public DeleteValoreVarianteCommand() {
			super(COMMAND_ID);
			CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
					CommandConfigurer.class);
			c.configure(this);
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void doExecuteCommand() {
			int selectionIndex = listValoriVarianti.getSelectedIndex();
			if (listValoriVarianti.getModel().getSize() > 0 && listValoriVarianti.getSelectedIndex() != -1) {
				List<ValoreVariante> list = (List<ValoreVariante>) getFormModel().getValueModel("valoriVarianti")
						.getValue();

				Object object = ((DefaultListModel) listValoriVarianti.getModel()).getElementAt(listValoriVarianti
						.getSelectedIndex());

				list.remove(object);

				getFormModel().getValueModel("valoriVarianti").setValue(list);
				updateListValoriVarianti();

				selectionIndex--;

				if (selectionIndex == 0) {
					if (list.size() > 0) {
						listValoriVarianti.setSelectedIndex(0);
					}
				} else {
					if (selectionIndex > 0) {
						listValoriVarianti.setSelectedIndex(selectionIndex);
					}
				}

			}
		}
	}

	/**
	 * Visualizza il codice del valore variante.
	 * 
	 * @author fattazzo
	 * 
	 */
	private class ListValoriVariantiCellReneder extends DefaultListCellRenderer {

		private static final long serialVersionUID = -2717948574707490360L;

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			ValoreVariante valoreVariante = (ValoreVariante) value;
			if (valoreVariante.getCodice() != null && !valoreVariante.getCodice().isEmpty()) {
				StringBuffer buffer = new StringBuffer(valoreVariante.getCodice());
				buffer.append(" - ").append(valoreVariante.getValore());
				label.setText(buffer.toString());
			} else {
				label.setText(" ");
				label.setBackground(Color.RED);
			}

			return label;
		}

	}

	private class NewValoreVarianteCommand extends ApplicationWindowAwareCommand {

		public static final String COMMAND_ID = "newValoreVarianteCommand";

		/**
		 * Costruttore.
		 */
		public NewValoreVarianteCommand() {
			super(COMMAND_ID);
			CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
					CommandConfigurer.class);
			c.configure(this);
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void doExecuteCommand() {
			try {
				getFormModel().commit();
				ETipoDatoTipoAttributo tipoDatoTipoAttributo = (ETipoDatoTipoAttributo) getFormModel().getValueModel(
						"tipoDato").getValue();

				ValoreVariante valoreVarianteNew = null;

				switch (tipoDatoTipoAttributo) {
				case NUMERICO:
					valoreVarianteNew = new ValoreVarianteNumerica();
					break;
				case STRINGA:
					valoreVarianteNew = new ValoreVarianteStringa();
					break;
				default:
					throw new UnsupportedOperationException("tipoDatoAttributo non riconosciuto "
							+ tipoDatoTipoAttributo);
				}

				List<ValoreVariante> list = (List<ValoreVariante>) getFormModel().getValueModel("valoriVarianti")
						.getValue();
				if (list == null) {
					list = new ArrayList<ValoreVariante>();
				}
				list.add(valoreVarianteNew);

				getFormModel().getValueModel("valoriVarianti").setValue(list);
				updateListValoriVarianti();
				listValoriVarianti.setSelectedIndex(list.size() - 1);
			} catch (Exception e) {
				TipoVarianteForm.logger.error("--> e", e);
			}
		}
	}

	private static Logger logger = Logger.getLogger(TipoVarianteForm.class);
	public static final String FORM_ID = "tipoVarianteForm";

	private final IAnagraficaTabelleBD anagraficaTabelleBD;
	private AziendaCorrente aziendaCorrente;

	private static final String LABEL_VALORI_VARIANTI = ".labelValoriVarianti";

	private static final String LABEL_DESCRIZIONI_LINGUA = ".labelDescrizioniLingua";
	private final JList listValoriVarianti = new JList(new DefaultListModel());
	private final JPanel valoreVarianteFormPanel = getComponentFactory().createPanel(new BorderLayout());

	private PanjeaAbstractForm valoreVarianteForm;

	private boolean initializeValoreVarianteForm = false;

	private Binding comboBoxBinding;

	private JComboBox comboBoxTipoDato;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.richclient.form.AbstractForm#createFormControl()
	 */
	public TipoVarianteForm(IAnagraficaTabelleBD anagraficaTabelleBD, AziendaCorrente aziendaCorrente) {
		super(PanjeaFormModelHelper.createFormModel(new TipoVariante(), false, FORM_ID), FORM_ID);
		this.aziendaCorrente = aziendaCorrente;
		this.anagraficaTabelleBD = anagraficaTabelleBD;

		listValoriVarianti.setCellRenderer(new ListValoriVariantiCellReneder());
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");

		builder.row();
		builder.add("codice");
		builder.row();

		comboBoxTipoDato = (JComboBox) builder.add("tipoDato")[1];
		valorizzaComboTipoDato();

		builder.row();
		JLabel label = (JLabel) builder.add("nomeLinguaAziendale")[0];
		label.setIcon(getIconSource().getIcon(this.aziendaCorrente.getLingua()));
		label.setHorizontalTextPosition(SwingConstants.LEFT);
		builder.row();
		builder.add(bf.createBoundSearchText("unitaMisura", new String[] { "codice" }, false));
		builder.row();
		JTabbedPane tabbedPane = getComponentFactory().createTabbedPane();
		tabbedPane.add(getMessage(FORM_ID + "Model" + LABEL_DESCRIZIONI_LINGUA), new DescrizioniEntityPanel(
				getFormModel(), "nomiLingua", anagraficaTabelleBD));
		tabbedPane.add(getMessage(FORM_ID + "Model" + LABEL_VALORI_VARIANTI), createValoriVariantiControl());

		builder.getLayoutBuilder().cell(tabbedPane);

		this.addFormObjectChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				updateListValoriVarianti();

			}
		});
		return builder.getForm();
	}

	private JComponent createListValoriVarianteButtonBar() {
		JPanel panel = getComponentFactory().createPanel();

		panel.add(new NewValoreVarianteCommand().createButton());
		panel.add(new DeleteValoreVarianteCommand().createButton());

		return panel;
	}

	private JComponent createValoriVariantiControl() {
		JPanel panelList = getComponentFactory().createPanel(new BorderLayout());

		JScrollPane scrollPane = getComponentFactory().createScrollPane(listValoriVarianti);
		scrollPane.setPreferredSize(new Dimension(150, 150));
		scrollPane.setMaximumSize(new Dimension(100, 4000));

		panelList.add(scrollPane, BorderLayout.CENTER);
		updateListValoriVarianti();
		panelList.add(createListValoriVarianteButtonBar(), BorderLayout.SOUTH);

		JPanel panel = getComponentFactory().createPanel(new BorderLayout());
		panel.add(panelList, BorderLayout.WEST);
		panel.add(valoreVarianteFormPanel, BorderLayout.CENTER);

		listValoriVarianti.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				// se questo evento e' parte di una serie, associati alla stessa
				// modifica
				// non devo eseguire nessuna operazione
				if (e != null && e.getValueIsAdjusting()) {
					return;
				}
				if (listValoriVarianti.getSelectedIndex() != -1) {

					Object object = ((DefaultListModel) listValoriVarianti.getModel()).getElementAt(listValoriVarianti
							.getSelectedIndex());
					// se il form non e inizializzato lo creo in base al tipo
					// del valore variante
					// contenuto nella lista
					if (!initializeValoreVarianteForm) {
						initializeValoreVarianteForm = true;

						if (object instanceof ValoreVarianteNumerica) {
							valoreVarianteForm = new ValoreVarianteNumericaForm();
						} else {
							valoreVarianteForm = new ValoreVarianteStringaForm(anagraficaTabelleBD, aziendaCorrente);
						}

						valoreVarianteFormPanel.add(valoreVarianteForm.getControl(), BorderLayout.CENTER);
						valoreVarianteFormPanel.revalidate();
					}

					if (valoreVarianteForm != null) {
						valoreVarianteForm.setFormObject(object);

						// mi registro al cambiamento del valore della proprietà
						// che viene visualizzata nella lista
						valoreVarianteForm.getFormModel().getValueModel("codice")
								.addValueChangeListener(new PropertyChangeListener() {

									@Override
									public void propertyChange(PropertyChangeEvent evt) {
										listValoriVarianti.repaint();

									}
								});
					}
				}
			}
		});
		return panel;
	}

	/**
	 * @return the anagraficaTabelleBD
	 */
	public IAnagraficaTabelleBD getAnagraficaTabelleBD() {
		return anagraficaTabelleBD;
	}

	private ValueHolder getListTipiDato() {

		// Filtro la lista dei tipi dato attributo perchè in una variante non
		// devo avere la possibilità di selezionare
		// il tipo dato si/no ma solo numerico e stringa
		List<ETipoDatoTipoAttributo> list = new ArrayList<ETipoDatoTipoAttributo>();
		list.add(ETipoDatoTipoAttributo.NUMERICO);
		list.add(ETipoDatoTipoAttributo.STRINGA);
		ValueHolder holder = new ValueHolder();
		holder.setValue(list);
		return holder;
	}

	@Override
	public void preCommit(FormModel formModel) {
		if (valoreVarianteForm != null) {
			valoreVarianteForm.commit();
		}
		super.preCommit(formModel);
	}

	/**
	 * @param aziendaCorrente
	 *            the aziendaCorrente to set
	 */
	public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
		this.aziendaCorrente = aziendaCorrente;
	}

	@SuppressWarnings("unchecked")
	private void updateListValoriVarianti() {
		((DefaultListModel) listValoriVarianti.getModel()).removeAllElements();

		List<ValoreVariante> list = (List<ValoreVariante>) getFormModel().getValueModel("valoriVarianti").getValue();

		if (list != null) {
			for (ValoreVariante valoreVariante : list) {
				((DefaultListModel) listValoriVarianti.getModel()).addElement(valoreVariante);
			}
		}
	}

	private void valorizzaComboTipoDato() {
		getValueModel("tipoDato").setValue(null);

		comboBoxBinding = ((PanjeaSwingBindingFactory) getBindingFactory()).createBoundComboBox("tipoDato",
				getListTipiDato());

		MessageSourceAccessor messageSourceAccessor = getMessages();
		((ComboBoxBinding) comboBoxBinding).setRenderer(new CustomEnumListRenderer(messageSourceAccessor));

		comboBoxTipoDato.setModel(((JComboBox) comboBoxBinding.getControl()).getModel());
	}
}
