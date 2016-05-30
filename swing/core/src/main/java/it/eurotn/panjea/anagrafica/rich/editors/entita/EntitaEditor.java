package it.eurotn.panjea.anagrafica.rich.editors.entita;

import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.bd.AnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.rich.IEditorListener;
import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;
import it.eurotn.panjea.rich.editors.DefaultEditor;

import org.springframework.context.ApplicationEvent;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.util.RcpSupport;

public class EntitaEditor extends DefaultEditor implements IEditorListener {

	private IAnagraficaBD anagraficaBD;

	/**
	 * @return Returns the anagraficaBD.
	 */
	public IAnagraficaBD getAnagraficaBD() {
		if (anagraficaBD == null) {
			anagraficaBD = RcpSupport.getBean(AnagraficaBD.BEAN_ID);
		}

		return anagraficaBD;
	}

	@Override
	public void onEditorEvent(ApplicationEvent event) {

		PanjeaLifecycleApplicationEvent panjeaEvent = (PanjeaLifecycleApplicationEvent) event;
		String evtName = panjeaEvent.getEventType();
		boolean chiudiEditor = false;
		Entita entita = (Entita) getEditorInput();

		if (LifecycleApplicationEvent.DELETED.equals(evtName)) {
			if (panjeaEvent.getSource() instanceof EntitaLite) {
				EntitaLite entitaLite = (EntitaLite) panjeaEvent.getSource();
				chiudiEditor = entita != null && entitaLite != null && entita.getEntitaLite().getTipo() != null
						&& entita.getId() != null && entita.getEntitaLite().getTipo().equals(entitaLite.getTipo())
						&& entita.getId().compareTo(entitaLite.getId()) == 0;
			}

			if (chiudiEditor) {
				PanjeaLifecycleApplicationEvent deleteEvent = new PanjeaLifecycleApplicationEvent(
						LifecycleApplicationEvent.DELETED, entita, this);
				Application.instance().getApplicationContext().publishEvent(deleteEvent);
			}
		} else {
			if (LifecycleApplicationEvent.MODIFIED.equals(evtName) && panjeaEvent.getSource() instanceof Entita) {
				Entita entitaEvent = (Entita) panjeaEvent.getSource();
				// se l'entità che sto modificando ha la stessa anagrafica dell'entità dell'editor, la ricarico
				if (entita.getId() != null && entita.getId() != entitaEvent.getId()
						&& entita.getAnagrafica().getId().equals(entitaEvent.getAnagrafica().getId())) {
					entita = getAnagraficaBD().caricaEntita(entita);
					setEditorInput(entita);
					// oltre a settare il nuovo oggetto devo fare in modo che l'editor dell'entità aggiorni il titolo
					// del tabbed
					fireCurrentObjectPropertyChange();
				}
			}
		}
	}
}
