package it.eurotn.panjea.ordini.rich.editors.areaordine;

import org.springframework.context.ApplicationEvent;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;

import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;
import it.eurotn.panjea.ordini.util.AreaOrdineFullDTO;
import it.eurotn.panjea.rich.IEditorListener;
import it.eurotn.rich.dialog.JecCompositeDialogPage;
import it.eurotn.rich.editors.AbstractEditorDialogPage;

public class AreaOrdineEditor extends AbstractEditorDialogPage implements IEditorListener {

	private IOrdiniDocumentoBD ordiniDocumentoBD;

	@Override
	protected JecCompositeDialogPage createCompositeDialogPage() {
		return new AreaOrdineDockingCompositeDialogPage(getDialogPageId());
	}

	/**
	 *
	 * @return ordiniDocumentoBD
	 */
	public IOrdiniDocumentoBD getOrdiniDocumentoBD() {
		return ordiniDocumentoBD;
	}

	@Override
	public void initialize(Object editorObject) {
		if (editorObject instanceof AreaOrdine) {
			editorObject = ordiniDocumentoBD.caricaAreaOrdineFullDTO((AreaOrdine) editorObject);
		}
		super.initialize(editorObject);
	}

	@Override
	public void onEditorEvent(ApplicationEvent event) {
		LifecycleApplicationEvent eventLifeCycle = (LifecycleApplicationEvent) event;
		if (eventLifeCycle.getEventType().equals(LifecycleApplicationEvent.DELETED)
				&& eventLifeCycle.objectIs(AreaOrdine.class)) {
			AreaOrdineFullDTO areaOrdineFullDTO = ((AreaOrdineFullDTO) getEditorInput());
			if (areaOrdineFullDTO.getAreaOrdine().equals(eventLifeCycle.getObject())) {
				close();
			}
		}
	}

	/**
	 *
	 * @param ordiniDocumentoBD
	 *            setter for ordiniDocumentoBD
	 */
	public void setOrdiniDocumentoBD(IOrdiniDocumentoBD ordiniDocumentoBD) {
		this.ordiniDocumentoBD = ordiniDocumentoBD;
	}

}
