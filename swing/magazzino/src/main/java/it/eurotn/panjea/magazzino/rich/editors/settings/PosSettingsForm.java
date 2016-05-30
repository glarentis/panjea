/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.editors.settings;

import it.eurotn.panjea.pos.domain.PosSettings;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

/**
 * @author fattazzo
 * 
 */
public class PosSettingsForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "posSettingsForm";
	public static final String RICERCA_ARTICOLI_COMMAND_ID = "aggiungiArticoliCommand";

	/**
	 * Costruttore.
	 */
	public PosSettingsForm() {
		super(PanjeaFormModelHelper.createFormModel(new PosSettings(), false, FORM_ID), FORM_ID);
	}

	/**
	 * Crea la label da utilizzare nel componente per la visualizzazione della disposizione degli articoli veloci.
	 * 
	 * @param text
	 *            testo della label
	 * @return label
	 */
	private JLabel createArticoloVeloceLabel(String text) {
		JLabel label = new JLabel(text);
		label.setOpaque(true);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBackground(Color.GRAY);

		return label;
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"right:pref,4dlu,left:default",
				"4dlu,default,4dlu,default,4dlu,default,4dlu,default,4dlu,default,4dlu,default,4dlu,default,4dlu,default,4dlu,default,4dlu,default,4dlu,default,4dlu,default,4dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanelNumbered());
		builder.setLabelAttributes("r, c");
		builder.setRow(2);

		builder.addLabel("listino", 1);
		Binding bindingListino = bf.createBoundSearchText("listino", new String[] { "codice" });
		((SearchPanel) bindingListino.getControl()).getTextFields().get("codice").setColumns(15);
		builder.addBinding(bindingListino, 3);
		builder.nextRow();

		builder.addLabel("articoloArrotondamenti");
		SearchPanel searchPanel = (SearchPanel) builder.addBinding(
				bf.createBoundSearchText("articoloArrotondamenti", new String[] { "codice", "descrizione" }), 3);
		searchPanel.getTextFields().get("codice").setColumns(10);
		searchPanel.getTextFields().get("descrizione").setColumns(26);

		builder.nextRow();
		builder.addLabel("articoloBuono");
		searchPanel = (SearchPanel) builder.addBinding(
				bf.createBoundSearchText("articoloBuono", new String[] { "codice", "descrizione" }), 3);
		searchPanel.getTextFields().get("codice").setColumns(10);
		searchPanel.getTextFields().get("descrizione").setColumns(26);
		builder.nextRow();

		builder.addHorizontalSeparator("Articoli veloci", 3);
		builder.nextRow();

		builder.addComponent(new JLabel("Articolo veloce 1"));
		searchPanel = (SearchPanel) builder.addBinding(
				bf.createBoundSearchText("articoloVeloce1", new String[] { "codice", "descrizione" }), 3);
		searchPanel.getTextFields().get("codice").setColumns(10);
		searchPanel.getTextFields().get("descrizione").setColumns(26);
		builder.nextRow();

		builder.addComponent(new JLabel("Articolo veloce 2"));
		searchPanel = (SearchPanel) builder.addBinding(
				bf.createBoundSearchText("articoloVeloce2", new String[] { "codice", "descrizione" }), 3);
		searchPanel.getTextFields().get("codice").setColumns(10);
		searchPanel.getTextFields().get("descrizione").setColumns(26);
		builder.nextRow();

		builder.addComponent(new JLabel("Articolo veloce 3"));
		searchPanel = (SearchPanel) builder.addBinding(
				bf.createBoundSearchText("articoloVeloce3", new String[] { "codice", "descrizione" }), 3);
		searchPanel.getTextFields().get("codice").setColumns(10);
		searchPanel.getTextFields().get("descrizione").setColumns(26);
		builder.nextRow();

		builder.addComponent(new JLabel("Articolo veloce 4"));
		searchPanel = (SearchPanel) builder.addBinding(
				bf.createBoundSearchText("articoloVeloce4", new String[] { "codice", "descrizione" }), 3);
		searchPanel.getTextFields().get("codice").setColumns(10);
		searchPanel.getTextFields().get("descrizione").setColumns(26);
		builder.nextRow();

		builder.addComponent(new JLabel("Articolo veloce 5"));
		searchPanel = (SearchPanel) builder.addBinding(
				bf.createBoundSearchText("articoloVeloce5", new String[] { "codice", "descrizione" }), 3);
		searchPanel.getTextFields().get("codice").setColumns(10);
		searchPanel.getTextFields().get("descrizione").setColumns(26);
		builder.nextRow();

		builder.addComponent(new JLabel("Articolo veloce 6"));
		searchPanel = (SearchPanel) builder.addBinding(
				bf.createBoundSearchText("articoloVeloce6", new String[] { "codice", "descrizione" }), 3);
		searchPanel.getTextFields().get("codice").setColumns(10);
		searchPanel.getTextFields().get("descrizione").setColumns(26);
		builder.nextRow();

		builder.addComponent(new JLabel("Articolo veloce 7"));
		searchPanel = (SearchPanel) builder.addBinding(
				bf.createBoundSearchText("articoloVeloce7", new String[] { "codice", "descrizione" }), 3);
		searchPanel.getTextFields().get("codice").setColumns(10);
		searchPanel.getTextFields().get("descrizione").setColumns(26);
		builder.nextRow();

		builder.addComponent(new JLabel("Articolo veloce 8"));
		searchPanel = (SearchPanel) builder.addBinding(
				bf.createBoundSearchText("articoloVeloce8", new String[] { "codice", "descrizione" }), 3);
		searchPanel.getTextFields().get("codice").setColumns(10);
		searchPanel.getTextFields().get("descrizione").setColumns(26);

		GridLayout gridLayout = new GridLayout(6, 3, 5, 5);
		JPanel panel = new JPanel(gridLayout);
		panel.setPreferredSize(new Dimension(350, 250));
		panel.setMaximumSize(new Dimension(350, 250));
		panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		panel.add(createArticoloVeloceLabel("Articolo 1"));
		panel.add(createArticoloVeloceLabel("Articolo 2"));
		panel.add(createArticoloVeloceLabel("Articolo 3"));
		panel.add(createArticoloVeloceLabel("Articolo 4"));
		panel.add(new JLabel(""));
		panel.add(new JLabel(""));
		panel.add(createArticoloVeloceLabel("Articolo 5"));
		panel.add(new JLabel(""));
		panel.add(new JLabel(""));
		panel.add(createArticoloVeloceLabel("Articolo 6"));
		panel.add(new JLabel(""));
		panel.add(new JLabel(""));
		panel.add(createArticoloVeloceLabel("Articolo 7"));
		panel.add(new JLabel(""));
		panel.add(new JLabel(""));
		panel.add(createArticoloVeloceLabel("Articolo 8"));
		builder.addComponent(panel, 3, 26);

		return builder.getPanel();
	}
}
