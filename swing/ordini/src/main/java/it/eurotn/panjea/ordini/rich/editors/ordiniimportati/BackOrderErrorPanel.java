/**
 *
 */
package it.eurotn.panjea.ordini.rich.editors.ordiniimportati;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import org.jdesktop.swingx.HorizontalLayout;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.factory.ComponentFactory;

/**
 * @author leonardo
 * 
 */
public class BackOrderErrorPanel extends JPanel {

	private static final long serialVersionUID = 3221563339154522775L;
	private JTextArea articoliMancanti;
	private JTextArea agentiMancanti;
	private JTextArea clientiMancanti;

	private JTextArea pagamentiMancanti;
	private JScrollPane clientiScrollPane;
	private JScrollPane pagamentiScrollPane;
	private JScrollPane agentiScrollPane;

	private JScrollPane articoliScrollPane;

	private ComponentFactory componentFactory;

	/**
	 * Costruttore.
	 */
	public BackOrderErrorPanel() {
		super();
		initPanelControl();
	}

	/**
	 * @return ComponentFactory
	 */
	private ComponentFactory getComponentFactory() {
		if (componentFactory == null) {
			componentFactory = (ComponentFactory) ApplicationServicesLocator.services().getService(
					ComponentFactory.class);
		}
		return componentFactory;
	}

	/**
	 * Inizializza i controlli per presentare gli errori sui dati del backorder.
	 */
	private void initPanelControl() {
		setLayout(new BorderLayout());

		clientiMancanti = getComponentFactory().createTextArea();
		articoliMancanti = getComponentFactory().createTextArea();
		agentiMancanti = getComponentFactory().createTextArea();
		pagamentiMancanti = getComponentFactory().createTextArea();

		clientiMancanti.setEditable(false);
		articoliMancanti.setEditable(false);
		agentiMancanti.setEditable(false);
		pagamentiMancanti.setEditable(false);

		clientiMancanti.setLineWrap(true);
		articoliMancanti.setLineWrap(true);
		agentiMancanti.setLineWrap(true);
		pagamentiMancanti.setLineWrap(true);

		clientiMancanti.setOpaque(false);
		articoliMancanti.setOpaque(false);
		agentiMancanti.setOpaque(false);
		pagamentiMancanti.setOpaque(false);

		articoliScrollPane = getComponentFactory().createScrollPane(articoliMancanti);
		articoliScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		articoliScrollPane.setPreferredSize(new Dimension(200, 100));
		articoliScrollPane.setBorder(BorderFactory.createTitledBorder("Articoli mancanti"));

		clientiScrollPane = getComponentFactory().createScrollPane(clientiMancanti);
		clientiScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		clientiScrollPane.setPreferredSize(new Dimension(200, 100));
		clientiScrollPane.setBorder(BorderFactory.createTitledBorder("Clienti mancanti"));

		pagamentiScrollPane = getComponentFactory().createScrollPane(pagamentiMancanti);
		pagamentiScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		pagamentiScrollPane.setPreferredSize(new Dimension(200, 100));
		pagamentiScrollPane.setBorder(BorderFactory.createTitledBorder("Pagamenti mancanti"));

		agentiScrollPane = getComponentFactory().createScrollPane(agentiMancanti);
		agentiScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		agentiScrollPane.setPreferredSize(new Dimension(200, 100));
		agentiScrollPane.setBorder(BorderFactory.createTitledBorder("Agenti mancanti"));

		articoliScrollPane.setVisible(false);
		clientiScrollPane.setVisible(false);
		pagamentiScrollPane.setVisible(false);
		agentiScrollPane.setVisible(false);

		JPanel errorPanel = getComponentFactory().createPanel(new HorizontalLayout());
		errorPanel.add(articoliScrollPane);
		errorPanel.add(clientiScrollPane);
		errorPanel.add(pagamentiScrollPane);
		errorPanel.add(agentiScrollPane);

		errorPanel.setOpaque(false);

		add(errorPanel, BorderLayout.WEST);
	}

	/**
	 * Visulizza, se presenti, i dati mancanti in text area separate per tipo.
	 * 
	 * @param clienti
	 *            clienti
	 * @param articoli
	 *            articoli
	 * @param agenti
	 *            agenti
	 * @param pagamenti
	 *            pagamenti
	 */
	public void updateControl(Collection<String> clienti, Collection<String> articoli, Collection<String> agenti,
			Collection<String> pagamenti) {
		clientiMancanti.setText(clienti.toString());
		clientiScrollPane.setVisible(clienti.size() != 0);

		articoliMancanti.setText(articoli.toString());
		articoliScrollPane.setVisible(articoli.size() != 0);

		agentiMancanti.setText(agenti.toString());
		agentiScrollPane.setVisible(agenti.size() != 0);

		pagamentiMancanti.setText(pagamenti.toString());
		pagamentiScrollPane.setVisible(pagamenti.size() != 0);
	}
}
