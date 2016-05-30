package it.eurotn.panjea.preventivi.rich.editors.areapreventivo;

import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo;
import it.eurotn.panjea.preventivi.rich.bd.IPreventiviBD;
import it.eurotn.panjea.preventivi.util.AreaPreventivoFullDTO;
import it.eurotn.panjea.rich.IEditorListener;
import it.eurotn.rich.dialog.JecCompositeDialogPage;
import it.eurotn.rich.editors.AbstractEditorDialogPage;

import org.springframework.context.ApplicationEvent;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;

public class AreaPreventivoEditor extends AbstractEditorDialogPage implements IEditorListener {

	private IPreventiviBD preventiviBD;

	@Override
	protected JecCompositeDialogPage createCompositeDialogPage() {
		return new AreaPreventivoDockingCompositeDialogPage(getDialogPageId());
	}

	@Override
	public void initialize(Object editorObject) {
		if (editorObject instanceof AreaPreventivo) {
			editorObject = preventiviBD.caricaAreaPreventivoFullDTO((AreaPreventivo) editorObject);
		}
		super.initialize(editorObject);
	}

	@Override
	public void onEditorEvent(ApplicationEvent event) {
		LifecycleApplicationEvent eventLifeCycle = (LifecycleApplicationEvent) event;
		if (eventLifeCycle.getEventType().equals(LifecycleApplicationEvent.DELETED)
				&& eventLifeCycle.objectIs(AreaPreventivo.class)) {
			AreaPreventivoFullDTO areaPreventivoFullDTO = (AreaPreventivoFullDTO) getEditorInput();
			if (areaPreventivoFullDTO.getAreaPreventivo().equals(eventLifeCycle.getObject())) {
				close();
			}
		}
	}

	/**
	 * @param preventiviBD
	 *            the preventiviBD to set
	 */
	public void setPreventiviBD(IPreventiviBD preventiviBD) {
		this.preventiviBD = preventiviBD;
	}

}
