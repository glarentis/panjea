package it.eurotn.panjea.magazzino.rich.editors.articolo.alternativi;

import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.ArticoloAlternativo;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.table.EEditPageMode;
import it.eurotn.rich.editors.table.EditFrame;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ArticoliAlternativiManualiPage extends AbstractTablePageEditor<ArticoloAlternativo> {

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;
	private Articolo articolo;

	/**
	 * Costruttore.
	 */
	protected ArticoliAlternativiManualiPage() {
		super("articoliAlternativiManualiPage", new String[] { "articolo", "articoloAlternativo", "corrispondenza" },
				ArticoloAlternativo.class);
	}

	/**
	 * @return articoli alternativi caricati.
	 */
	private Set<ArticoloAlternativo> caricaArticoliAlternativi() {
		Set<ArticoloAlternativo> caricaArticoliAlternativi = new HashSet<>();
		if (!articolo.isNew()) {
			caricaArticoliAlternativi = magazzinoAnagraficaBD.caricaArticoliAlternativi(articolo);
		}
		return caricaArticoliAlternativi;
	}

	/**
	 * @return Returns the magazzinoAnagraficaBD.
	 */
	public IMagazzinoAnagraficaBD getMagazzinoAnagraficaBD() {
		return magazzinoAnagraficaBD;
	}

	@Override
	public Collection<ArticoloAlternativo> loadTableData() {
		getEditFrame().setEditMode(EEditPageMode.POPUP, null);
		getEditFrame().hideFrame();
		return caricaArticoliAlternativi();
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public Collection<ArticoloAlternativo> refreshTableData() {
		return caricaArticoliAlternativi();
	}

	@Override
	public void setFormObject(Object object) {
		articolo = (Articolo) object;
		((ArticoloAlternativoPage) getEditPages().get(EditFrame.DEFAULT_OBJECT_CLASS_NAME)).setArticolo(articolo);
	}

	/**
	 * @param magazzinoAnagraficaBD
	 *            The magazzinoAnagraficaBD to set.
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}

}
