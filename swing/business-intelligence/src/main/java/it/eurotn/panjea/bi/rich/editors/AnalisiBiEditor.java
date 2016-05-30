package it.eurotn.panjea.bi.rich.editors;

import it.eurotn.panjea.bi.domain.analisi.AnalisiBi;
import it.eurotn.panjea.bi.rich.bd.IBusinessIntelligenceBD;
import it.eurotn.panjea.bi.rich.editors.analisi.AnalisiBiEditorController;
import it.eurotn.panjea.bi.rich.editors.analisi.ToolbarAnalisi;
import it.eurotn.rich.dialog.DockingCompositeDialogPage;
import it.eurotn.rich.dialog.JecCompositeDialogPage;
import it.eurotn.rich.editors.AbstractEditorDialogPage;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.settings.Settings;

import com.jidesoft.docking.DockableFrame;
import com.jidesoft.swing.DefaultOverlayable;

public class AnalisiBiEditor extends AbstractEditorDialogPage implements InitializingBean {

	private DockingCompositeDialogPage compositeDialogPage;
	private AnalisiBiEditorController analisiBiEditorController;
	private IBusinessIntelligenceBD businessIntelligenceBD;
	private DefaultOverlayable overlay;

	private ToolbarAnalisi toolbarAnalisi;

	/**
	 * Costruttore.
	 */
	public AnalisiBiEditor() {
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		analisiBiEditorController = new AnalisiBiEditorController(businessIntelligenceBD);
	}

	@Override
	protected JecCompositeDialogPage createCompositeDialogPage() {
		compositeDialogPage = new DockingCompositeDialogPage(getDialogPageId()) {
			@Override
			protected void configureFrame(DockableFrame frame) {
				super.configureFrame(frame);
				frame.setMaximizable(true);
				frame.setFloatable(false);
				frame.setAvailableButtons(DockableFrame.BUTTON_HIDE_AUTOHIDE);
			}

			@Override
			public void restoreState(Settings settings) {
				// non eseguo la restore state perchè sarà il controller in base all'analisi a configurarsi lo stato
				// delle pagine
				super.restoreState(settings);
			}
		};
		return compositeDialogPage;
	}

	@Override
	public JComponent createControl() {
		JLabel overlayMessage = new JLabel("ANALISI DATI IN CORSO...");
		overlayMessage.setOpaque(true);
		overlayMessage.setFont(overlayMessage.getFont().deriveFont(18.0f).deriveFont(1));
		overlay = new DefaultOverlayable(super.createControl(), overlayMessage);
		analisiBiEditorController.setDialogPage(compositeDialogPage);

		JPanel mainPanel = new JPanel(new BorderLayout());
		toolbarAnalisi = new ToolbarAnalisi(analisiBiEditorController, this);
		mainPanel.add(toolbarAnalisi, BorderLayout.NORTH);
		mainPanel.add(overlay, BorderLayout.CENTER);

		analisiBiEditorController.setOverlay(overlay);
		analisiBiEditorController.setToolBarAnalisi(toolbarAnalisi);

		return mainPanel;
	}

	@Override
	public void dispose() {
		if (toolbarAnalisi != null) {
			toolbarAnalisi.dispose();
		}
		if (compositeDialogPage != null) {
			compositeDialogPage.dispose();
		}
		overlay = null;
		toolbarAnalisi = null;
		analisiBiEditorController.dispose();
		analisiBiEditorController = null;
		super.dispose();
	}

	@Override
	public void initialize(Object editorObject) {
		super.initialize(editorObject);
		if (editorObject instanceof AnalisiBi && compositeDialogPage != null) {
			analisiBiEditorController.setDialogPage(compositeDialogPage);
			AnalisiBi analisiBi = (AnalisiBi) editorObject;
			if (!analisiBi.isNew()) {
				analisiBiEditorController.caricaAnalisi(analisiBi.getNome(), analisiBi.getCategoria());
			} else {
				analisiBiEditorController.nuovo();
			}
		}
	}

	@Override
	public boolean isEnableCache() {
		return false;
	}

	/**
	 * @param businessIntelligenceBD
	 *            The businessIntelligenceBD to set.
	 */
	public void setBusinessIntelligenceBD(IBusinessIntelligenceBD businessIntelligenceBD) {
		this.businessIntelligenceBD = businessIntelligenceBD;
	}

}
