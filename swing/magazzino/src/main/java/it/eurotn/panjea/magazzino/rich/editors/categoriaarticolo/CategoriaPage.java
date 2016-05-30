/**
 *
 */
package it.eurotn.panjea.magazzino.rich.editors.categoriaarticolo;

import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.magazzino.domain.Categoria;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.forms.categoriaarticolo.CategoriaForm;
import it.eurotn.panjea.magazzino.rich.forms.categoriaarticolo.DescrizioniCategoriaForm;
import it.eurotn.panjea.magazzino.rich.forms.categoriaarticolo.DescrizioniEsteseCategoriaForm;
import it.eurotn.panjea.magazzino.rich.forms.categoriaarticolo.attributi.AttributoCategoriaForm;
import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;
import it.eurotn.rich.editors.FormsBackedTabbedDialogPageEditor;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.awt.CardLayout;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;

/**
 * @author fattazzo
 *
 */
public class CategoriaPage extends FormsBackedTabbedDialogPageEditor {

	public static final String PAGE_ID = "categoriaPage";

	public static final String CATEGORIA_UPDATE = "categoriaUpdate";

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;
	private IAnagraficaTabelleBD anagraficaTabelleBD;

	private JPanel rootPanel;
	private CardLayout rootLayout;

	private AziendaCorrente aziendaCorrente;

	private AttributoCategoriaForm formCategorie;

	/**
	 * Costruttore.
	 */
	public CategoriaPage() {
		super(PAGE_ID, new CategoriaForm(PanjeaFormModelHelper.createFormModel(new Categoria(), false,
				CategoriaForm.FORM_ID), CategoriaForm.FORM_ID));
		this.aziendaCorrente = (AziendaCorrente) Application.instance().getApplicationContext()
				.getBean("aziendaCorrente");
		getEditorLockCommand().execute();
	}

	@Override
	public void addForms() {
		// aggiungo il tab degli attributi ereditati e della categoria
		formCategorie = new AttributoCategoriaForm(getBackingFormPage().getFormModel(), this.magazzinoAnagraficaBD);
		addForm(formCategorie);

		// aggiungo il tab dei dati di vendita
		// addForm(new DatiVenditaCategoriaForm(getBackingFormPage().getFormModel()));

		// aggiungo il tab delle formule di trasformazione
		// addForm(new FormuleTrasformazioneCategoriaForm(getBackingFormPage().getFormModel()));

		// aggiungo il tab delle descrizioni
		DescrizioniCategoriaForm descrizioniCategoriaForm = new DescrizioniCategoriaForm(getBackingFormPage()
				.getFormModel(), anagraficaTabelleBD);

		if (!descrizioniCategoriaForm.isEmpty()) {
			addForm(descrizioniCategoriaForm);
		}

		// aggiungo il tab delle descrizioni estese
		addForm(new DescrizioniEsteseCategoriaForm(getBackingFormPage().getFormModel(), anagraficaTabelleBD,
				aziendaCorrente));
	}

	@Override
	public JComponent createControl() {
		// sovrascrivo la createControl perche' se non e' selezionata nessuna
		// categoria devo visualizzare il pannello vuoto.
		rootLayout = new CardLayout();
		rootPanel = getComponentFactory().createPanel(rootLayout);

		rootPanel.add(new JLabel(), "NullCard");
		rootPanel.add(super.createControl(), "ControlCard");

		rootPanel.setPreferredSize(new Dimension(1000, 600));

		return rootPanel;
	}

	@Override
	protected Object doSave() {
		formCategorie.commit();
		Categoria categoriaSave = (Categoria) getBackingFormPage().getFormObject();
		if (categoriaSave.getGenerazioneCodiceArticoloData().getNumeratore() != null) {
			String[] protocolloSplit = categoriaSave.getGenerazioneCodiceArticoloData().getNumeratore().split(" - ");
			categoriaSave.getGenerazioneCodiceArticoloData().setNumeratore(protocolloSplit[0]);
		}
		Categoria categoriaResult = this.magazzinoAnagraficaBD.salvaCategoria(categoriaSave);
		getBackingFormPage().setFormObject(categoriaResult);

		PanjeaLifecycleApplicationEvent event = new PanjeaLifecycleApplicationEvent(LifecycleApplicationEvent.MODIFIED,
				categoriaResult);
		Application.instance().getApplicationContext().publishEvent(event);

		CategoriaPage.this.firePropertyChange(CATEGORIA_UPDATE, null, categoriaResult);

		return categoriaResult;
	}

	/**
	 * @return Returns the anagraficaTabelleBD.
	 */
	public IAnagraficaTabelleBD getAnagraficaTabelleBD() {
		return anagraficaTabelleBD;
	}

	@Override
	protected AbstractCommand[] getCommand() {
		return null;
	}

	/**
	 * @return Returns the magazzinoAnagraficaBD.
	 */
	public IMagazzinoAnagraficaBD getMagazzinoAnagraficaBD() {
		return magazzinoAnagraficaBD;
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
	public boolean onUndo() {
		Categoria categoria = (Categoria) getBackingFormPage().getFormObject();

		CategoriaPage.this.firePropertyChange(CATEGORIA_UPDATE, null, categoria);

		boolean undo = super.onUndo();

		// Disabilito la pagina se la categoria è nulla altrimenti rimane un
		// formobject nuovo e l'utente può premere modifica e salvarlo generando
		// quindi devi errori.
		if (categoria.isNew()) {
			setReadOnly(true);
		}

		return undo;
	}

	@Override
	public void refreshData() {
	}

	/**
	 * @param anagraficaTabelleBD
	 *            The anagraficaTabelleBD to set.
	 */
	public void setAnagraficaTabelleBD(IAnagraficaTabelleBD anagraficaTabelleBD) {
		this.anagraficaTabelleBD = anagraficaTabelleBD;
	}

	@Override
	public void setFormObject(Object object) {
		rootLayout.show(rootPanel, "ControlCard");
		this.setReadOnly(false);
		super.setFormObject(object);
	}

	/**
	 * @param magazzinoAnagraficaBD
	 *            The magazzinoAnagraficaBD to set.
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}
}
