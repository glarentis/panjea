package it.eurotn.panjea.magazzino.rich.editors.categoriacommerciale;

import it.eurotn.panjea.magazzino.domain.CategoriaCommercialeArticolo;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.editors.categoriaarticolo.RicercaAvanzataArticoliCommand;
import it.eurotn.panjea.magazzino.util.ArticoloRicerca;
import it.eurotn.rich.editors.FormsBackedTabbedDialogPageEditor;

import java.util.List;

import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;

import com.jidesoft.swing.JideTabbedPane;

public class CategoriaCommercialePage extends FormsBackedTabbedDialogPageEditor {

	private class RicercaAvanzataActionCommandInterceptor implements ActionCommandInterceptor {

		@Override
		public void postExecution(ActionCommand command) {
			List<ArticoloRicerca> articoliSelezionati = ((RicercaAvanzataArticoliCommand) command)
					.getArticoliSelezionati();

			int selectedTab = CategoriaCommercialePage.this.pane.getSelectedIndex();
			CategoriaCommercialeArticolo categoriaCommerciale = (CategoriaCommercialeArticolo) getBackingFormPage()
					.getFormObject();
			CategoriaCommercialeArticolo cat1 = selectedTab == 0 ? categoriaCommerciale : null;
			CategoriaCommercialeArticolo cat2 = selectedTab == 1 ? categoriaCommerciale : null;
			magazzinoAnagraficaBD.cambiaCategoriaCommercialeAdArticoli(articoliSelezionati, cat1, cat2);
			setFormObject(categoriaCommerciale);
		}

		@Override
		public boolean preExecution(ActionCommand command) {
			CategoriaCommercialeArticolo categoriaCommercialeArticoloCorrente = (CategoriaCommercialeArticolo) getBackingFormPage()
					.getFormObject();
			return categoriaCommercialeArticoloCorrente.getId() != null;
		}

	}

	private static final String PAGE_ID = "categoriaCommercialePage";

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD = null;

	private RicercaAvanzataArticoliCommand ricercaAvanzataArticoliCommand = null;
	private RicercaAvanzataActionCommandInterceptor ricercaAvanzataActionCommandInterceptor = null;
	private JideTabbedPane pane;

	/**
	 * Costruttore.
	 */
	public CategoriaCommercialePage() {
		super(PAGE_ID, new CategoriaCommercialeArticoloForm(new CategoriaCommercialeArticolo()));
	}

	@Override
	public void addForms() {
		CategoriaCommercialeArticolo2Form categoriaCommercialeArticolo2Form = new CategoriaCommercialeArticolo2Form(
				getBackingFormPage().getFormModel());
		addForm(categoriaCommercialeArticolo2Form);
	}

	@Override
	protected void configureTabbedPane(JTabbedPane tabbedPane) {
		pane = (JideTabbedPane) tabbedPane;

		pane.setTabShape(JideTabbedPane.SHAPE_BOX);
		pane.setColorTheme(JideTabbedPane.COLOR_THEME_VSNET);
		pane.setTabResizeMode(JideTabbedPane.RESIZE_MODE_FIT);
		pane.setTabPlacement(SwingConstants.LEFT);
	}

	@Override
	public void dispose() {
		getRicercaAvanzataArticoliCommand().removeCommandInterceptor(getRicercaAvanzataActionCommandInterceptor());

		super.dispose();
	}

	@Override
	protected Object doDelete() {
		magazzinoAnagraficaBD.cancellaCategoriaCommercialeArticolo((CategoriaCommercialeArticolo) getBackingFormPage()
				.getFormObject());
		return getBackingFormPage().getFormObject();
	}

	@Override
	protected Object doSave() {
		CategoriaCommercialeArticolo categoriaCommercialeArticolo = (CategoriaCommercialeArticolo) this.getForm()
				.getFormObject();
		return magazzinoAnagraficaBD.salvaCategoriaCommercialeArticolo(categoriaCommercialeArticolo);
	}

	@Override
	protected AbstractCommand[] getCommand() {
		AbstractCommand[] commands = new AbstractCommand[] { getNewCommand(), toolbarPageEditor.getLockCommand(),
				toolbarPageEditor.getSaveCommand(), toolbarPageEditor.getUndoCommand(),
				toolbarPageEditor.getDeleteCommand(), getRicercaAvanzataArticoliCommand() };
		return commands;
	}

	/**
	 * @return Returns the magazzinoAnagraficaBD.
	 */
	public IMagazzinoAnagraficaBD getMagazzinoAnagraficaBD() {
		return magazzinoAnagraficaBD;
	}

	/**
	 * @return RicercaAvanzataActionCommandInterceptor
	 */
	private RicercaAvanzataActionCommandInterceptor getRicercaAvanzataActionCommandInterceptor() {
		if (ricercaAvanzataActionCommandInterceptor == null) {
			ricercaAvanzataActionCommandInterceptor = new RicercaAvanzataActionCommandInterceptor();
		}
		return ricercaAvanzataActionCommandInterceptor;
	}

	/**
	 * @return ricercaAvanzataArticoliCommand
	 */
	private RicercaAvanzataArticoliCommand getRicercaAvanzataArticoliCommand() {
		if (ricercaAvanzataArticoliCommand == null) {
			ricercaAvanzataArticoliCommand = new RicercaAvanzataArticoliCommand("aggiungiArticoliCommad");
			ricercaAvanzataArticoliCommand.addCommandInterceptor(getRicercaAvanzataActionCommandInterceptor());
		}
		return ricercaAvanzataArticoliCommand;
	}

	@Override
	public void loadData() {
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public void refreshData() {
	}

	@Override
	public void setFormObject(Object object) {
		// carico la categoria per inizializzare le liste lazy
		CategoriaCommercialeArticolo cat = (CategoriaCommercialeArticolo) object;
		boolean isCatPresente = cat != null && cat.getId() != null;
		getRicercaAvanzataArticoliCommand().setEnabled(isCatPresente);
		if (isCatPresente) {
			cat = magazzinoAnagraficaBD.caricaCategoriaCommercialeArticolo(cat.getId());
			super.setFormObject(cat);
		}
	}

	/**
	 * @param magazzinoAnagraficaBD
	 *            The magazzinoAnagraficaBD to set.
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}
}
