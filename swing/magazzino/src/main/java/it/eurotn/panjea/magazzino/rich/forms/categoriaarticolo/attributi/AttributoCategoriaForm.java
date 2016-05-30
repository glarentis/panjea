/**
 *
 */
package it.eurotn.panjea.magazzino.rich.forms.categoriaarticolo.attributi;

import it.eurotn.panjea.magazzino.domain.AttributoCategoria;
import it.eurotn.panjea.magazzino.domain.Categoria;
import it.eurotn.panjea.magazzino.domain.TipoAttributo;
import it.eurotn.panjea.magazzino.domain.TipoAttributo.ETipoDatoTipoAttributo;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.TableEditableBinding;
import it.eurotn.rich.control.table.JideTableWidget;
import it.eurotn.rich.form.PanjeaAbstractForm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;

import org.jdesktop.swingx.VerticalLayout;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;

/**
 * @author fattazzo
 *
 */
public class AttributoCategoriaForm extends PanjeaAbstractForm {

	private class AddAllAttributiCommand extends ApplicationWindowAwareCommand {

		public static final String COMMAND_ID = "addAllAttributiCommand";

		/**
		 * Costruttore.
		 *
		 */
		public AddAllAttributiCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			List<TipoAttributo> tipiAttr = new ArrayList<TipoAttributo>();

			int size = attributiSelezionabiliList.getModel().getSize();

			if (size > 0) {
				for (int i = 0; i < size; i++) {
					Object item = attributiSelezionabiliList.getModel().getElementAt(i);
					tipiAttr.add((TipoAttributo) item);
				}
				creaAttributiCategoria(tipiAttr);
			}
		}

	}

	private class AddTipoAttributoCommand extends ApplicationWindowAwareCommand {

		public static final String COMMAND_ID = "addTipoAttributoCommand";

		/**
		 * Costruttore.
		 *
		 */
		public AddTipoAttributoCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {

			if (!attributiSelezionabiliList.getSelectionModel().isSelectionEmpty()) {
				List<TipoAttributo> tipiAttr = new ArrayList<TipoAttributo>();
				tipiAttr.add(attributiSelezionabiliList.getSelectedValue());

				creaAttributiCategoria(tipiAttr);
			}
		}

	}

	private class RemoveAllAttributiCommand extends ApplicationWindowAwareCommand {

		public static final String COMMAND_ID = "removeAllAttributiCommand";

		/**
		 * Costruttore.
		 *
		 */
		public RemoveAllAttributiCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {

			rimuoviAttributiCategoria(null);

		}

	}

	private class RemoveTipoAttributoCommand extends ApplicationWindowAwareCommand {

		public static final String COMMAND_ID = "removeTipoAttributoCommand";

		/**
		 * Costruttore.
		 *
		 */
		public RemoveTipoAttributoCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {

			AttributoCategoria attributoCategoria = tableAttributi.getSelectedObject();

			if (attributoCategoria != null && attributoCategoria.getCategoria().equals(getFormObject())) {
				rimuoviAttributiCategoria(attributoCategoria);
			}

		}

	}

	public static final String FORM_ID = "tipiAttributoCategoriaForm";

	private final IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	private JList<TipoAttributo> attributiSelezionabiliList;

	private JideTableWidget<AttributoCategoria> tableAttributi;

	/**
	 * Costruttore .
	 *
	 * @param formModel
	 *            formModel
	 * @param magazzinoAnagraficaBD
	 *            bd per l'anagrafica
	 */
	public AttributoCategoriaForm(final FormModel formModel, final IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		super(formModel, FORM_ID);
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}

	@Override
	public void commit() {
		super.commit();
		if (tableAttributi.getTable().getCellEditor() != null) {
			tableAttributi.getTable().getCellEditor().stopCellEditing();
		}
	}

	/**
	 * Crea gli attributi della categoria in base ai tipi attributi.
	 *
	 * @param tipiAttributo
	 *            tipi attributo
	 */
	private void creaAttributiCategoria(List<TipoAttributo> tipiAttributo) {

		Categoria categoria = (Categoria) getFormObject();

		List<AttributoCategoria> attributiCategoria = new ArrayList<AttributoCategoria>();
		attributiCategoria.addAll(categoria.getAttributiCategoria());

		for (TipoAttributo tipoAttributo : tipiAttributo) {

			AttributoCategoria attributoCategoria = new AttributoCategoria();
			attributoCategoria.setCategoria(categoria);
			attributoCategoria.setTipoAttributo(tipoAttributo);
			attributoCategoria.setRiga(1);
			attributoCategoria.setOrdine(1);
			if (tipoAttributo.getTipoDato() == ETipoDatoTipoAttributo.BOOLEANO) {
				attributoCategoria.setValore(Boolean.FALSE.toString().toLowerCase());
			}

			attributiCategoria.add(attributoCategoria);
		}

		getFormModel().getValueModel("attributiCategoria").setValue(attributiCategoria);
		updateListaTipiAttributi();
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("50dlu,4dlu,15dlu,4dlu,left:max(400dlu;pref):grow", "pref:grow");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel());
		builder.setLabelAttributes("c, f");
		builder.setComponentAttributes("f, f");

		// lista dei tipi attributi selezionabili
		final DefaultListModel<TipoAttributo> listModel = new DefaultListModel<>();
		for (TipoAttributo tipoAttributo : getTipiAttributiSelezionabili()) {
			listModel.addElement(tipoAttributo);
		}
		attributiSelezionabiliList = new JList<>(listModel);
		attributiSelezionabiliList.setOpaque(false);
		attributiSelezionabiliList.setCellRenderer(new TipoAttributoListCellRender());
		JPanel listPanel = getComponentFactory().createPanel(new BorderLayout());
		listPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Attributi"));
		listPanel.add(attributiSelezionabiliList, BorderLayout.CENTER);
		builder.addComponent(listPanel, 1, 1);

		// commands
		builder.setComponentAttributes("c, c");
		JPanel commandsPanel = getComponentFactory().createPanel(new VerticalLayout(5));
		commandsPanel.add(new AddTipoAttributoCommand().createButton());
		commandsPanel.add(new AddAllAttributiCommand().createButton());
		commandsPanel.add(new RemoveTipoAttributoCommand().createButton());
		commandsPanel.add(new RemoveAllAttributiCommand().createButton());
		builder.addComponent(commandsPanel, 3, 1);

		// attributi della categoria
		builder.setComponentAttributes("f, f");
		AttributiCategoriaTableModel attributiArticoloTableModel = new AttributiCategoriaTableModel();
		TableEditableBinding<AttributoCategoria> bindingAttributi = new TableEditableBinding<AttributoCategoria>(
				getFormModel(), "attributiCategoria", Set.class, attributiArticoloTableModel);
		bindingAttributi.getControl().setBorder(
				BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK),
						"Attributi della categoria"));

		builder.addBinding(bindingAttributi, 5, 1);
		tableAttributi = bindingAttributi.getTableWidget();

		addFormObjectChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				updateListaTipiAttributi();
			}
		});

		return builder.getPanel();
	}

	/**
	 *
	 * @return attributi selezionabili. Filtra l'anagrafica dei tipiAttributo togliendo gli attributi già presenti
	 */
	private List<TipoAttributo> getTipiAttributiSelezionabili() {

		List<TipoAttributo> listaTipiAttributi = magazzinoAnagraficaBD.caricaTipiAttributo();

		List<TipoAttributo> tipoAttributiSelezionabili = new ArrayList<TipoAttributo>();
		tipoAttributiSelezionabili.addAll(listaTipiAttributi);
		Categoria categoria = (Categoria) getFormObject();
		if (categoria.getAttributiCategoria() != null) {
			for (AttributoCategoria attributoCategoria : categoria.getAttributiCategoria()) {
				tipoAttributiSelezionabili.remove(attributoCategoria.getTipoAttributo());
			}
		}
		return tipoAttributiSelezionabili;
	}

	/**
	 * Rimuove gli attributi specificati dalla categoria.
	 *
	 * @param attributoToRemove
	 *            attributi da rimuovere
	 */
	private void rimuoviAttributiCategoria(AttributoCategoria attributoToRemove) {

		if (attributoToRemove == null) {
			getFormModel().getValueModel("attributiCategoria").setValue(new ArrayList<AttributoCategoria>());
		} else {
			@SuppressWarnings("unchecked")
			List<AttributoCategoria> attributiCategoria = (List<AttributoCategoria>) getFormModel().getValueModel(
					"attributiCategoria").getValue();
			attributiCategoria.remove(attributoToRemove);

			getFormModel().getValueModel("attributiCategoria").setValue(attributiCategoria);
		}

		updateListaTipiAttributi();
	}

	/**
	 * Aggiorna i controlli della pagina.
	 */
	private void updateListaTipiAttributi() {
		((DefaultListModel<TipoAttributo>) attributiSelezionabiliList.getModel()).clear();
		for (TipoAttributo tipoAttributo : getTipiAttributiSelezionabili()) {
			((DefaultListModel<TipoAttributo>) attributiSelezionabiliList.getModel()).addElement(tipoAttributo);
		}
	}

}
