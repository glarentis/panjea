package it.eurotn.panjea.magazzino.rich.editors.ricercaarticoli;

import it.eurotn.panjea.magazzino.domain.Categoria;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.editors.categoriaarticolo.ArticoliTablePage;
import it.eurotn.panjea.magazzino.rich.editors.categoriaarticolo.ArticoloCategoriaDTO;
import it.eurotn.panjea.magazzino.rich.editors.categoriaarticolo.CategorieTreeTablePage;
import it.eurotn.panjea.magazzino.util.ArticoloRicerca;
import it.eurotn.panjea.magazzino.util.CategoriaLite;
import it.eurotn.panjea.magazzino.util.ParametriRicercaArticolo.CustomFilter;
import it.eurotn.panjea.rich.components.JecSplitPane;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.util.RcpSupport;

public class RicercaAvanzataArticoliPage extends CategorieTreeTablePage {

	private ArticoliTablePage articoliTablePage;

	private CustomFilter customFilter;

	private boolean loadData;

	/**
	 * Costruttore.
	 */
	public RicercaAvanzataArticoliPage() {
		super("ricercaAvanzataArticoliPage");
		setMagazzinoAnagraficaBD((IMagazzinoAnagraficaBD) RcpSupport.getBean("magazzinoAnagraficaBD"));
	}

	@Override
	protected JComponent createControl() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		JecSplitPane splitPane = new JecSplitPane(JSplitPane.HORIZONTAL_SPLIT);

		JPanel categorieTreeComponent = (JPanel) super.createControl();

		articoliTablePage = new ArticoliTablePage();
		articoliTablePage.setCustomFilter(customFilter);
		articoliTablePage.setMagazzinoAnagraficaBD(getMagazzinoAnagraficaBD());
		splitPane.setLeftComponent(categorieTreeComponent);
		splitPane.setRightComponent(articoliTablePage.getControl());

		splitPane.setDividerLocation(300);
		splitPane.setDividerSize(1);
		splitPane.setBorder(null);

		mainPanel.add(splitPane, BorderLayout.CENTER);
		loadData = false;
		try {
			treeTable.getSelectionModel().setSelectionInterval(0, 0);
		} finally {
			loadData = true;
		}
		return mainPanel;
	}

	@Override
	public void deleteNode(DefaultMutableTreeTableNode node) {
		// non faccio nulla
	}

	@Override
	protected boolean doDelete(DefaultMutableTreeTableNode node) {
		// non faccio nulla
		return false;
	}

	/**
	 * @return gli articoliRicerca selezionati
	 */
	public List<ArticoloRicerca> getArticoliSelezionati() {
		List<ArticoloRicerca> articoliSelezionati = new ArrayList<ArticoloRicerca>();
		articoliSelezionati.addAll(articoliTablePage.getTable().getSelectedObjects());
		return articoliSelezionati;
	}

	@Override
	public AbstractCommand[] getCommand() {
		return new AbstractCommand[] { getRefreshCategorieCommand() };
	}

	@Override
	public void onPostPageOpen() {
		articoliTablePage.onPostPageOpen();
		super.onPostPageOpen();
	}

	@Override
	protected void openSelectedNode(DefaultMutableTreeTableNode node) {
		// non faccio nulla
	}

	@Override
	public void refreshData() {
		// non faccio nulla
	}

	@Override
	protected void selectionChanged(DefaultMutableTreeTableNode node) {
		if (node != null) {
			Categoria categoria = null;
			CategoriaLite categoriaLite = (CategoriaLite) node.getUserObject();
			if (categoriaLite != null) {
				categoria = categoriaLite.createCategoria();
			}
			ArticoloCategoriaDTO articoloCategoriaDTO = new ArticoloCategoriaDTO(null, categoria);
			articoliTablePage.preSetFormObject(articoloCategoriaDTO);
			articoliTablePage.setFormObject(articoloCategoriaDTO);
			articoliTablePage.postSetFormObject(articoloCategoriaDTO);
			if (loadData) {
				articoliTablePage.loadData();
			}
		}
	}

	/**
	 * @param customFilter
	 *            the customFilter to set
	 */
	public void setCustomFilter(CustomFilter customFilter) {
		this.customFilter = customFilter;
		if (articoliTablePage != null) {
			articoliTablePage.setCustomFilter(customFilter);
		}
	}

}
