package it.eurotn.panjea.magazzino.rich.forms.rigamagazzino.componenti;

import it.eurotn.panjea.magazzino.domain.IRigaArticoloDocumento;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.rich.control.table.JideTableWidget;
import it.eurotn.rich.control.table.JideTableWidget.TableType;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jdesktop.swingx.icon.EmptyIcon;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.HierarchicalTable;
import com.jidesoft.grid.HierarchicalTableComponentFactory;

public class RigheArticoliComponentiDistintaTablePage extends AbstractTablePageEditor<RigaArticolo> implements Observer {

	private class ComponentiHierarchicalTableComponentFactory implements HierarchicalTableComponentFactory {

		private JideTableWidget<RigaArticolo> tableWidget;

		@Override
		public Component createChildComponent(HierarchicalTable arg0, Object arg1, int arg2) {
			tableWidget = new JideTableWidget<RigaArticolo>("tableComponentiWidget",
					new RigheArticoliComponentiDistintaTableModel());
			tableWidget.setTableType(TableType.HIERARCHICAL);
			@SuppressWarnings("unchecked")
			Set<IRigaArticoloDocumento> componenti = (Set<IRigaArticoloDocumento>) arg1;
			List<RigaArticolo> righeArticolo = new ArrayList<RigaArticolo>();
			if (componenti != null) {
				for (IRigaArticoloDocumento rigaArticolo : componenti) {
					righeArticolo.add((RigaArticolo) rigaArticolo);
				}
			}
			tableWidget.setHierarchicalTableComponentFactory(new ComponentiHierarchicalTableComponentFactory());
			tableWidget.setRows(righeArticolo);
			((HierarchicalTable) tableWidget.getTable()).setSingleExpansion(false);
			JPanel panel = new JPanel(new BorderLayout());
			panel.add(new JLabel(new EmptyIcon(16, 16)), BorderLayout.WEST);
			panel.add(tableWidget.getTable(), BorderLayout.CENTER);
			((HierarchicalTable) tableWidget.getTable()).expandAllRows();
			tableWidget.getTable().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.DARK_GRAY));
			// panel.setBorder(BorderFactory.createLoweredSoftBevelBorder());
			return panel;
		}

		@Override
		public void destroyChildComponent(HierarchicalTable arg0, Component arg1, int arg2) {
			tableWidget.dispose();
		}
	}

	public static final String PAGE_ID = "righeArticoliComponentiDistintaTablePage";
	private IRigaArticoloDocumento rigaArticoloDocumento = null;

	private JComponent righeLottiComponent;
	private IPageLifecycleAdvisor righeLottoPage = null;

	private PluginManager pluginManager;

	{
		pluginManager = RcpSupport.getBean(PluginManager.BEAN_ID);
	}

	/**
	 * Costruttore.
	 */
	protected RigheArticoliComponentiDistintaTablePage() {
		super(PAGE_ID, new RigheArticoliComponentiDistintaTableModel());
		getTable().setTableType(TableType.HIERARCHICAL);
		getTable().setHierarchicalTableComponentFactory(new ComponentiHierarchicalTableComponentFactory());
		((HierarchicalTable) getTable().getTable()).setSingleExpansion(false);
		((HierarchicalTable) getTable().getTable()).expandAllRows();
		getTable().addSelectionObserver(this);
	}

	/**
	 * Crea il componente per la visualizzazione dei lotti utilizzati per la riga componente.
	 *
	 * @return componenti creati
	 */
	private JComponent createRigheLottiComponent() {
		JPanel panel = getComponentFactory().createPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createTitledBorder("Lotti utilizzati"));
		panel.setPreferredSize(new Dimension(400, 100));

		if (pluginManager.isPresente(PluginManager.PLUGIN_LOTTI)) {
			righeLottoPage = RcpSupport.getBean("righeLottoComponentePage");
			panel.add(((org.springframework.richclient.dialog.AbstractDialogPage) righeLottoPage).getControl(),
					BorderLayout.CENTER);
		}

		return panel;
	}

	@Override
	public JComponent createToolbar() {
		return null;
	}

	@Override
	public JComponent getFooterControl() {
		righeLottiComponent = createRigheLottiComponent();

		return righeLottiComponent;
	}

	@Override
	public Object getManagedObject(Object pageObject) {
		return rigaArticoloDocumento;
	}

	/**
	 * @return the rigaArticoloDocumento to get
	 */
	public IRigaArticoloDocumento getRigaArticoloDocumento() {
		return rigaArticoloDocumento;
	}

	@Override
	public Collection<RigaArticolo> loadTableData() {
		List<RigaArticolo> righeArticolo = new ArrayList<RigaArticolo>();
		if (rigaArticoloDocumento != null && rigaArticoloDocumento.getComponenti() != null) {
			Set<IRigaArticoloDocumento> righe = rigaArticoloDocumento.getComponenti();
			for (IRigaArticoloDocumento rigaArticolo : righe) {
				righeArticolo.add((RigaArticolo) rigaArticolo);
			}
		}
		return righeArticolo;
	}

	@Override
	public void onPostPageOpen() {

	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public Collection<RigaArticolo> refreshTableData() {
		return loadTableData();
	}

	@Override
	public void setFormObject(Object object) {
		rigaArticoloDocumento = (IRigaArticoloDocumento) object;
		getObjectConfigurer().configure(this, "righeArticoliComponentiTablePage");

		righeLottiComponent.setVisible(pluginManager.isPresente(PluginManager.PLUGIN_LOTTI)
				&& rigaArticoloDocumento.getArticolo().getId() != null
				&& rigaArticoloDocumento.getArticolo().isDistinta());
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);
	}

	@Override
	public void update(Observable observable, Object obj) {
		super.update(observable, obj);

		if (pluginManager.isPresente(PluginManager.PLUGIN_LOTTI)) {
			righeLottoPage.setFormObject(obj);
			righeLottoPage.refreshData();
		}
	}

}
