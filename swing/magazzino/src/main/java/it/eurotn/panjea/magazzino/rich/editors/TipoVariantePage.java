/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.editors;

import it.eurotn.locking.IDefProperty;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.magazzino.domain.TipoAttributo;
import it.eurotn.panjea.magazzino.domain.TipoVariante;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.forms.TipoVarianteForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.util.Assert;

/**
 * @author fattazzo
 * 
 */
public class TipoVariantePage extends FormBackedDialogPageEditor implements InitializingBean {

	public static final String PAGE_ID = "tipoVariantePage";
	private TipoAttributo tipoAttributo;

	private final IMagazzinoAnagraficaBD magazzinoAnagraficaBD;
	private final IAnagraficaTabelleBD anagraficaTabelleBD;
	private static AziendaCorrente aziendaCorrente;

	/**
	 * Costruttore.
	 * 
	 * @param magazzinoAnagraficaBD
	 *            magazzinoAnagraficaBD
	 * @param anagraficaTabelleBD
	 *            anagraficaTabelleBD
	 */
	public TipoVariantePage(final IMagazzinoAnagraficaBD magazzinoAnagraficaBD,
			final IAnagraficaTabelleBD anagraficaTabelleBD) {
		super(PAGE_ID, new TipoVarianteForm(anagraficaTabelleBD, aziendaCorrente));
		this.anagraficaTabelleBD = anagraficaTabelleBD;
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(anagraficaTabelleBD);
		Assert.notNull(magazzinoAnagraficaBD);
		((TipoVarianteForm) getBackingFormPage()).setAziendaCorrente(aziendaCorrente);
	}

	@Override
	protected Object doDelete() {
		magazzinoAnagraficaBD.cancellaTipoAttributo((TipoVariante) getBackingFormPage().getFormObject());
		return getBackingFormPage().getFormObject();
	}

	@Override
	protected Object doSave() {
		TipoVariante tipoVariante = (TipoVariante) getBackingFormPage().getFormObject();
		tipoVariante = (TipoVariante) magazzinoAnagraficaBD.salvaTipoAttributo(tipoVariante);
		return tipoVariante;

	}

	/**
	 * @return the anagraficaTabelleBD
	 */
	public IAnagraficaTabelleBD getAnagraficaTabelleBD() {
		return anagraficaTabelleBD;
	}

	@Override
	protected AbstractCommand[] getCommand() {
		AbstractCommand[] abstractCommands = new AbstractCommand[] { toolbarPageEditor.getNewCommand(),
				toolbarPageEditor.getLockCommand(), toolbarPageEditor.getSaveCommand(),
				toolbarPageEditor.getUndoCommand(), toolbarPageEditor.getDeleteCommand() };
		return abstractCommands;
	}

	@Override
	public AbstractCommand getEditorNewCommand() {
		return super.getEditorNewCommand();
	}

	@Override
	protected Object getNewEditorObject() {
		TipoVariante tipoVariante = (TipoVariante) super.getNewEditorObject();
		return tipoVariante;
	}

	/**
	 * @return the tipoAttributo
	 */
	public TipoAttributo getTipoAttributo() {
		return tipoAttributo;
	}

	@Override
	public void loadData() {
	}

	// /**
	// * getter di {@link TipoAttributo} con le proprietà al suo interno inizializzate
	// *
	// * @param tipoAttributo
	// * @return
	// */
	// private TipoAttributo getTipoAttributoWithNestedProperties(TipoAttributo tipoAttributo) {
	// if (tipoAttributo.getUnitaMisura() == null) {
	// tipoAttributo.setUnitaMisura(new UnitaMisura());
	// }
	// return tipoAttributo;
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eurotn.rich.editors.IPageLifecycleAdvisor#onPrePageOpen()
	 */
	@Override
	public void onPostPageOpen() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eurotn.rich.editors.IPageLifecycleAdvisor#refreshData()
	 */
	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public void refreshData() {

	}

	/**
	 * @param aziendaCorrente
	 *            the aziendaCorrente to set
	 */
	public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
		TipoVariantePage.aziendaCorrente = aziendaCorrente;
	}

	@Override
	public void setFormObject(Object object) {
		if (((IDefProperty) object).getId() != null && !((IDefProperty) object).getId().equals(-1)) {
			if (object instanceof TipoVariante) {
				object = magazzinoAnagraficaBD.caricaTipoVariante((TipoVariante) object);
			} else {
				object = magazzinoAnagraficaBD.caricaTipoAttributo((TipoAttributo) object);
			}
		}

		super.setFormObject(object);
	}

	/**
	 * @param tipoAttributo
	 *            the tipoAttributo to set
	 */
	public void setTipoAttributo(TipoAttributo tipoAttributo) {
		this.tipoAttributo = tipoAttributo;
	}
}
