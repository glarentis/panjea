package it.eurotn.panjea.preventivi.rich.editors.areapreventivo;

import it.eurotn.panjea.preventivi.rich.editors.righepreventivo.RighePreventivoTablePage;
import it.eurotn.panjea.preventivi.util.AreaPreventivoFullDTO;
import it.eurotn.panjea.preventivi.util.RigaArticoloDTO;
import it.eurotn.rich.dialog.JecCompositeDialogPage;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.springframework.richclient.dialog.DialogPage;

public class AreaPreventivoEditorController {

	/**
	 * PropertyChange chiamato quando la testata viene validata, in questo caso quando viene salvata.
	 */
	private class AreaPreventivoValidataChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {

			if (AreaPreventivoPage.VALIDA_AREA_DOCUMENTO.equals(evt.getPropertyName())) {
				AreaPreventivoFullDTO areaPreventivoFullDTO = (AreaPreventivoFullDTO) areaPreventivoPage.getForm()
						.getFormObject();
				if (areaPreventivoFullDTO.getVersion() != null && areaPreventivoFullDTO.getVersion() == 0) {
					compositePage.setActivePage(righePreventivoTablePage);
					righePreventivoTablePage.getEditFrame().setCurrentPage(new RigaArticoloDTO());
					righePreventivoTablePage.getEditFrame().getQuickInsertCommand().setSelected(true);
				}
			}
		}
	}

	public class RighePreventivoValidataChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (RighePreventivoTablePage.VALIDA_RIGHE_PREVENTIVO.equals(evt.getPropertyName())) {

				boolean nuovaArea = righePreventivoTablePage.getAzioneDopoConfermaCommand().isSelected();

				if (nuovaArea && evt.getOldValue() != null) {
					areaPreventivoPage.setTabForm(0);
					areaPreventivoPage.getEditorNewCommand().execute();
					compositePage.setActivePage(areaPreventivoPage);
				} else {
					areaPreventivoPage.setTabForm(2);
				}
			}
		}
	}

	private AreaPreventivoPage areaPreventivoPage;
	private JecCompositeDialogPage compositePage;

	private RighePreventivoTablePage righePreventivoTablePage;
	private AreaPreventivoValidataChangeListener areaPreventivoValidataChangeListener;
	private RighePreventivoValidataChangeListener righePreventivoValidataChangeListener;

	/**
	 *
	 * @param compositePage
	 *            compositePage
	 */
	public AreaPreventivoEditorController(final JecCompositeDialogPage compositePage) {
		this.compositePage = compositePage;
		righePreventivoValidataChangeListener = new RighePreventivoValidataChangeListener();
		areaPreventivoValidataChangeListener = new AreaPreventivoValidataChangeListener();
	}

	/**
	 * aggiunge una pagina al controller.
	 *
	 * @param page
	 *            pagina da aggiungere
	 */
	public void addPage(DialogPage page) {
		if (AreaPreventivoPage.PAGE_ID.equals(page.getId())) {
			areaPreventivoPage = (AreaPreventivoPage) page;
			areaPreventivoPage.addPropertyChangeListener(AreaPreventivoPage.VALIDA_AREA_DOCUMENTO,
					areaPreventivoValidataChangeListener);

		} else if (RighePreventivoTablePage.PAGE_ID.equals(page.getId())) {
			righePreventivoTablePage = (RighePreventivoTablePage) page;
			righePreventivoTablePage.addPropertyChangeListener(RighePreventivoTablePage.VALIDA_RIGHE_PREVENTIVO,
					righePreventivoValidataChangeListener);
		}
	}

	/**
	 * dispose del controller.
	 */
	public void dispose() {
		righePreventivoTablePage.removePropertyChangeListener(RighePreventivoTablePage.VALIDA_RIGHE_PREVENTIVO,
				righePreventivoValidataChangeListener);
		righePreventivoTablePage = null;
		areaPreventivoPage.removePropertyChangeListener(AreaPreventivoPage.VALIDA_AREA_DOCUMENTO,
				areaPreventivoValidataChangeListener);
		areaPreventivoPage = null;
	}
}
