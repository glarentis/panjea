/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.commands.navigationloaders;

import it.eurotn.panjea.agenti.domain.Agente;
import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.domain.Cliente;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.Fornitore;
import it.eurotn.panjea.anagrafica.domain.Vettore;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.domain.lite.FornitoreLite;
import it.eurotn.panjea.anagrafica.domain.lite.VettoreLite;
import it.eurotn.panjea.anagrafica.rich.commands.navigationloaders.EntitaLoader.TipoEntitaLoader;
import it.eurotn.panjea.anagrafica.util.EntitaDocumento;
import it.eurotn.panjea.rich.factory.navigationloader.AbstractLoaderActionCommand;

import org.springframework.core.ReflectiveVisitorHelper;
import org.springframework.richclient.application.Application;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

/**
 * @author fattazzo
 * 
 */
public class EntitaLoaderActionCommand extends AbstractLoaderActionCommand {
	protected ReflectiveVisitorHelper visitorHelper = new ReflectiveVisitorHelper();
	protected EntitaLoaderObjectVisitor entitaLoaderObjectVisitor = new EntitaLoaderObjectVisitor();

	/**
	 * Costruttore.
	 */
	public EntitaLoaderActionCommand() {
		this("entitaLoaderActionCommand");
	}

	/**
	 * 
	 * @param commandid
	 *            commandid
	 */
	public EntitaLoaderActionCommand(final String commandid) {
		super(commandid);
	}

	@Override
	protected void doExecuteCommand() {
		OpenEditorEvent event = getOpenEditorEvent();
		if (event != null) {
			Application.instance().getApplicationContext().publishEvent(event);
		}
	}

	/**
	 * 
	 * @return L'entità se il commando è stato selezionato su un'entità, altrimenti null.
	 */
	protected Entita getEntita() {
		EntitaLoader entitaLoader = getEntitaLoader();
		if (entitaLoader == null || entitaLoader.getTipoEntitaLoader() == TipoEntitaLoader.AZIENDA) {
			return null;
		}

		return (Entita) entitaLoader.getValue();
	}

	/**
	 * 
	 * @return Un oggetto EntitaLoader o null se il command non deve fare nulla.
	 */
	protected EntitaLoader getEntitaLoader() {
		Object loaderObject = getParameter(PARAM_LOADER_OBJECT, null);
		if (loaderObject != null) {
			return (EntitaLoader) visitorHelper.invokeVisit(entitaLoaderObjectVisitor, loaderObject);
		}
		return null;
	}

	/**
	 * 
	 * @return Un oggetto OpenEditorEvent o null se il command non deve fare nulla.
	 */
	protected OpenEditorEvent getOpenEditorEvent() {
		EntitaLoader entitaLoader = getEntitaLoader();
		if (entitaLoader != null) {
			return new OpenEditorEvent(entitaLoader.getValue());
		}
		return null;
	}

	@Override
	public Class<?>[] getTypes() {
		return new Class<?>[] { Cliente.class, ClienteLite.class, Fornitore.class, FornitoreLite.class, Vettore.class,
				VettoreLite.class, Agente.class, AgenteLite.class, EntitaDocumento.class };
	}

}
