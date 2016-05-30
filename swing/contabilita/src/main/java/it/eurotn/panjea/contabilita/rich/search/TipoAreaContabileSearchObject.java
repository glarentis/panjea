/**
 * 
 */
package it.eurotn.panjea.contabilita.rich.search;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.List;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

/**
 * @author adriano
 * @version 1.0, 19/giu/07
 */
public class TipoAreaContabileSearchObject extends AbstractSearchObject {

	private static final String SEARCHOBJECT_ID = "tipoAreaContabileSearchObject";

	private IContabilitaAnagraficaBD contabilitaAnagraficaBD;

	/**
	 * Costruttore.
	 */
	public TipoAreaContabileSearchObject() {
		super(SEARCHOBJECT_ID);
	}

	/**
	 * @return Returns the contabilitaAnagraficaBD.
	 */
	public IContabilitaAnagraficaBD getContabilitaAnagraficaBD() {
		return contabilitaAnagraficaBD;
	}

	@Override
	public List<AbstractCommand> getCustomCommands() {
		return null;
	}

	@Override
	public List<?> getData(String fieldSearch, String valueSearch) {
		List<TipoAreaContabile> list = this.contabilitaAnagraficaBD.caricaTipiAreaContabile(fieldSearch, valueSearch,
				false);
		return list;
	}

	@Override
	public void openEditor(Object object) {
		TipoAreaContabile tipoAreaContabile = (TipoAreaContabile) object;
		TipoDocumento tipoDocumento = tipoAreaContabile.getTipoDocumento();
		LifecycleApplicationEvent event = new OpenEditorEvent(tipoDocumento);
		Application.instance().getApplicationContext().publishEvent(event);
	}

	/**
	 * @param contabilitaAnagraficaBD
	 *            The contabilitaAnagraficaBD to set.
	 */
	public void setContabilitaAnagraficaBD(IContabilitaAnagraficaBD contabilitaAnagraficaBD) {
		this.contabilitaAnagraficaBD = contabilitaAnagraficaBD;
	}

}
