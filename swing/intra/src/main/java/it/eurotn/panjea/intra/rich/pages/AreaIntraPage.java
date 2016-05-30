package it.eurotn.panjea.intra.rich.pages;

import it.eurotn.panjea.intra.domain.AreaIntra;
import it.eurotn.panjea.intra.manager.ParametriRicercaAreaIntra;
import it.eurotn.panjea.intra.rich.bd.IIntraBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

public class AreaIntraPage extends FormBackedDialogPageEditor {

	private static final String PAGE_ID = "areaIntraPage";
	private AreaIntra areaIntra;
	private IIntraBD intraBD;
	private boolean popupMode;

	/**
	 * 
	 * Costruttore.
	 * 
	 */
	public AreaIntraPage() {
		super(PAGE_ID, new AreaIntraForm(new AreaIntra()));
	}

	/**
	 * 
	 * Costruttore.
	 * 
	 * @param popupMode
	 *            true se la finestra viene aperta in popup
	 */
	public AreaIntraPage(final boolean popupMode) {
		this();
		this.popupMode = popupMode;
	}

	@Override
	protected Object doSave() {
		areaIntra = (AreaIntra) getForm().getFormObject();
		areaIntra = intraBD.salvaAreaIntra(areaIntra);
		return areaIntra;
	}

	@Override
	protected AbstractCommand[] getCommand() {
		if (popupMode) {
			return null;
		} else {
			return new AbstractCommand[] { toolbarPageEditor.getLockCommand(), toolbarPageEditor.getSaveCommand(),
					toolbarPageEditor.getUndoCommand() };
		}
	}

	@Override
	public void loadData() {
	}

	@Override
	public void onPostPageOpen() {
		if (popupMode) {
			toolbarPageEditor.getLockCommand().execute();
		}
	}

	@Override
	public void refreshData() {
	}

	@Override
	public void setFormObject(Object object) {
		if (object instanceof ParametriRicercaAreaIntra) {
			ParametriRicercaAreaIntra parametri = (ParametriRicercaAreaIntra) object;
			areaIntra = new AreaIntra();
			if (parametri.getDocumentoCorrente() != null) {
				areaIntra = intraBD.caricaAreaIntraByDocumento(parametri.getDocumentoCorrente());
			}
		} else {
			areaIntra = (AreaIntra) object;
		}
		super.setFormObject(areaIntra);
	}

	/**
	 * 
	 * @param intraBD
	 *            .
	 */
	public void setIntraBD(IIntraBD intraBD) {
		this.intraBD = intraBD;
	}
}