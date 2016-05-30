/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.commands.navigationloaders;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.bd.AnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.commands.navigationloaders.EntitaLoader.TipoEntitaLoader;
import it.eurotn.panjea.anagrafica.util.EntitaDocumento;

import org.springframework.richclient.util.RcpSupport;

/**
 * @author fattazzo
 * 
 */
public class EntitaLoaderObjectVisitor {

	private IAnagraficaBD anagraficaBD;

	/**
	 * Costruttore.
	 */
	public EntitaLoaderObjectVisitor() {
		super();
		this.anagraficaBD = RcpSupport.getBean(AnagraficaBD.getBeanId());
	}

	/**
	 * 
	 * @param loaderObject
	 *            ogg. da visitare
	 * @return entita loader
	 */
	public EntitaLoader visit(Entita loaderObject) {
		Entita entita = loaderObject;
		entita = anagraficaBD.caricaEntita(entita);
		return new EntitaLoader(TipoEntitaLoader.ENTITA, entita);
	}

	/**
	 * 
	 * @param loaderObject
	 *            ogg. da visitare
	 * @return entita loader
	 */
	public EntitaLoader visit(EntitaDocumento loaderObject) {

		EntitaLoader entitaLoader = null;

		if (loaderObject.getTipoEntita() == TipoEntita.AZIENDA) {
			entitaLoader = new EntitaLoader(TipoEntitaLoader.AZIENDA, "aziendaEditor");
		} else {
			EntitaLite entitaLite = (EntitaLite) loaderObject.getTipoEntita().createInstanceLite();
			entitaLite.setId(loaderObject.getId());
			Entita entita = entitaLite.creaProxyEntita();
			entita = anagraficaBD.caricaEntita(entita);
			entitaLoader = new EntitaLoader(TipoEntitaLoader.ENTITA, entita);
		}
		return entitaLoader;
	}

	/**
	 * 
	 * @param loaderObject
	 *            ogg. da visitare
	 * @return entita loader
	 */
	public EntitaLoader visit(EntitaLite loaderObject) {
		Entita entita = loaderObject.creaProxyEntita();
		entita = anagraficaBD.caricaEntita(entita);
		return new EntitaLoader(TipoEntitaLoader.ENTITA, entita);
	}
}
