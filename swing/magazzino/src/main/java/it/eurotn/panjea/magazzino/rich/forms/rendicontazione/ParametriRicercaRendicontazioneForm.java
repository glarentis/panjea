package it.eurotn.panjea.magazzino.rich.forms.rendicontazione;

import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.magazzino.domain.rendicontazione.TipoEsportazione;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.renderer.TipoAreaMagazzinoListCellRenderer;
import it.eurotn.panjea.magazzino.util.ParametriRicercaAreaMagazzino;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;

import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;

public class ParametriRicercaRendicontazioneForm extends PanjeaAbstractForm {

	private class TipoEsportazionePropertyChange implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			TipoEsportazione tipoEsportazione = (TipoEsportazione) evt.getNewValue();

			DefaultListModel tipiAreaListModel = new DefaultListModel();
			tipiAree = new ArrayList<TipoAreaMagazzino>();

			if (tipoEsportazione != null && tipoEsportazione.getId() != null) {
				tipoEsportazione = magazzinoAnagraficaBD.caricaTipoEsportazione(tipoEsportazione.getId(), false);
			}
			// NPE tipoEsportazione null se annullo valore della searchtext
			if (tipoEsportazione != null && tipoEsportazione.getTipiAreeMagazzino() != null) {
				for (TipoAreaMagazzino tipoAreaMagazzino : tipoEsportazione.getTipiAreeMagazzino()) {
					tipiAreaListModel.addElement(tipoAreaMagazzino);
					tipiAree.add(tipoAreaMagazzino);
				}
			}
			listTipiDoc.setModel(tipiAreaListModel);
		}

	}

	public static final String FORM_ID = "parametriRicercaRendicontazioneForm";
	public static final String FORMMODEL_ID = "parametriRicercaRendicontazioneFormModel";

	private JList listTipiDoc;
	private List<TipoAreaMagazzino> tipiAree;

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	/**
	 * Costruttore.
	 * 
	 */
	public ParametriRicercaRendicontazioneForm() {
		super(PanjeaFormModelHelper.createFormModel(new ParametriRicercaAreaMagazzino(), false, FORM_ID), FORM_ID);
		this.magazzinoAnagraficaBD = RcpSupport.getBean(MagazzinoAnagraficaBD.BEAN_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"left:default, 4dlu, left:default,10dlu,left:default, 4dlu, left:default,4dlu,fill:default:grow",
				"4dlu,default,4dlu,default,default,30dlu");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // ,
																				// new
																				// FormDebugPanel());

		builder.setRow(2);
		builder.addLabel("documentoExporter", 1);
		SearchPanel searchPanel = (SearchPanel) builder.addBinding(
				bf.createBoundSearchText("tipoEsportazione", new String[] { "nome" }), 3);
		searchPanel.getTextFields().get("nome").setColumns(10);
		builder.addComponent(createTipiAreeComponent(), 9, 1, 1, 6);

		builder.addPropertyAndLabel("dataRegistrazione", 1, 4);
		builder.nextRow();

		getFormModel().getValueModel("tipoEsportazione").addValueChangeListener(new TipoEsportazionePropertyChange());

		return builder.getPanel();
	}

	/**
	 * Crea il componente per la visualizzazione dei {@link TipoAreaMagazzino} da ricercare.
	 * 
	 * @return componente creato
	 */
	private JComponent createTipiAreeComponent() {

		DefaultListModel tipiAreaListModel = new DefaultListModel();
		listTipiDoc = new JList(tipiAreaListModel);
		listTipiDoc.setCellRenderer(new TipoAreaMagazzinoListCellRenderer());
		listTipiDoc.setOpaque(false);

		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createTitledBorder("Tipi documenti da esportare"));
		panel.add(getComponentFactory().createScrollPane(listTipiDoc));

		return panel;
	}

	/**
	 * @return tipi aree magazzino
	 */
	public List<TipoAreaMagazzino> getTipiAreaMagazzinoExport() {
		return tipiAree;
	}
}
