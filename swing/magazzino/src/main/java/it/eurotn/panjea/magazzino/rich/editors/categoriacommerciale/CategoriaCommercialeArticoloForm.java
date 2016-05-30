package it.eurotn.panjea.magazzino.rich.editors.categoriacommerciale;

import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.CategoriaCommercialeArticolo;
import it.eurotn.panjea.magazzino.rich.control.table.renderer.ArticoloLiteListItemRenderer;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;

import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.list.DefaultGroupableListModel;
import com.jidesoft.list.GroupList;
import com.jidesoft.swing.PartialLineBorder;
import com.jidesoft.swing.PartialSide;
import com.jidesoft.swing.SearchableUtils;

public class CategoriaCommercialeArticoloForm extends PanjeaAbstractForm {

	private class CategoriaCommercialeChangeListener implements PropertyChangeListener {

		@SuppressWarnings("unchecked")
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			CategoriaCommercialeArticolo cat = (CategoriaCommercialeArticolo) evt.getNewValue();
			listModel.clear();
			if (cat == null) {
				return;
			}
			if (cat.getArticoli() == null) {
				return;
			}
			for (ArticoloLite articoloLite : cat.getArticoli()) {
				listModel.addElement(articoloLite);
				// Non usare uno stringBuffer perchè i setGroup usa l'hashCode
				// dell'oggetto passato e lo
				// stringBuilder costruisce sempre una nuova stringa. Mentre
				// creare una stringa al "volo" mette
				// l'instanza nella memoria per le stringhe e riusa quella. Per
				// ottimizzare si dovrebbe cacharle in
				// una mappa.
				listModel.setGroupAt(articoloLite.getCategoria().getCodice() + " - "
						+ articoloLite.getCategoria().getDescrizione(), listModel.size() - 1);
			}
		}
	}

	private static class GroupCellRenderer extends DefaultListCellRenderer {

		private static final long serialVersionUID = -6258813343202640182L;

		@SuppressWarnings("rawtypes")
		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {

			JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			label.setBackground(isSelected ? list.getSelectionBackground() : new Color(221, 231, 238));
			label.setForeground(new Color(0, 21, 110));
			label.setFont(label.getFont().deriveFont(Font.BOLD));
			label.setBorder(BorderFactory.createCompoundBorder(new PartialLineBorder(Color.LIGHT_GRAY, 1,
					PartialSide.SOUTH), BorderFactory.createEmptyBorder(2, 6, 2, 2)));
			return label;
		}
	}

	private static final String FORM_ID = "categoriaCommercialeArticoloForm";

	private GroupList list = null;
	private DefaultGroupableListModel listModel = null;
	private CategoriaCommercialeChangeListener categoriaCommercialeChangeListener = null;

	private static ArticoloLiteListItemRenderer articoloLiteListItemRenderer = new ArticoloLiteListItemRenderer();

	/**
	 * Costruttore.
	 * 
	 * @param categoriaCommercialeArticolo
	 *            {@link CategoriaCommercialeArticolo}
	 */
	public CategoriaCommercialeArticoloForm(final CategoriaCommercialeArticolo categoriaCommercialeArticolo) {
		super(PanjeaFormModelHelper.createFormModel(categoriaCommercialeArticolo, false, FORM_ID), FORM_ID);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:pref,4dlu,fill:default:g", "4dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
		builder.setLabelAttributes("r, c");
		builder.setRow(2);
		listModel = new DefaultGroupableListModel();
		list = new GroupList(listModel);
		SearchableUtils.installSearchable(list);
		list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list.setCellRenderer(articoloLiteListItemRenderer);
		list.setSelectionBackground(new Color(252, 236, 166));
		list.setGroupCellRenderer(new GroupCellRenderer());
		list.setPreferredColumnCount(3);
		builder.addPropertyAndLabel("codice");
		builder.nextRow();

		builder.addComponent(new JScrollPane(list), 3);

		addFormObjectChangeListener(getCategoriaCommercialeChangeListener());
		return builder.getPanel();
	}

	@Override
	public void dispose() {
		removeFormObjectChangeListener(getCategoriaCommercialeChangeListener());
		super.dispose();
	}

	/**
	 * @return categoriaCommercialeChangeListener
	 */
	private CategoriaCommercialeChangeListener getCategoriaCommercialeChangeListener() {
		if (categoriaCommercialeChangeListener == null) {
			categoriaCommercialeChangeListener = new CategoriaCommercialeChangeListener();
		}
		return categoriaCommercialeChangeListener;
	}

}
