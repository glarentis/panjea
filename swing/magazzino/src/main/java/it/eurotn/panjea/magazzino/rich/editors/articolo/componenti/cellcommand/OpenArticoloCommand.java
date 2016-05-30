/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.editors.articolo.componenti.cellcommand;

import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.editors.articolo.ComponentePM;
import it.eurotn.panjea.magazzino.rich.editors.categoriaarticolo.ArticoloCategoriaDTO;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.config.CommandFaceDescriptor;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

/**
 * @author fattazzo
 * 
 */
@Deprecated
public class OpenArticoloCommand extends AbstractToolbarGraphCommand {

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	/**
	 * Costruttore.
	 * 
	 * @param commandId
	 */
	public OpenArticoloCommand() {
		super("openArticoloCommand");
		RcpSupport.configure(this);
		this.magazzinoAnagraficaBD = RcpSupport.getBean(MagazzinoAnagraficaBD.BEAN_ID);

		setFaceDescriptor(new CommandFaceDescriptor(null, RcpSupport.getIcon(Articolo.class.getName()),
				"Apri editor dell'articolo"));
	}

	@Override
	protected void doExecuteCommand() {
		Articolo articoloToOpen = null;

		if (nodeObject instanceof ArticoloLite) {
			articoloToOpen = ((ArticoloLite) nodeObject).creaProxyArticolo();
		} else if (nodeObject instanceof ComponentePM) {
			ArticoloLite articoloLite = ((ComponentePM) nodeObject).getArticolo();
			articoloToOpen = articoloLite.creaProxyArticolo();
		}

		if (articoloToOpen != null) {
			articoloToOpen = magazzinoAnagraficaBD.caricaArticolo(articoloToOpen, true);
			ArticoloCategoriaDTO articoloCategoriaDTO = new ArticoloCategoriaDTO(articoloToOpen,
					articoloToOpen.getCategoria());
			OpenEditorEvent event = new OpenEditorEvent(articoloCategoriaDTO);
			Application.instance().getApplicationContext().publishEvent(event);
		}
	}

	@Override
	protected void doUpdate(Object nodeObject) {
		// solo se non sono sull'articolo di riferimento posso aggiungere un componente
		setVisible(!(nodeObject instanceof ArticoloLite && articolo.getArticoloLite().equals(nodeObject)));
	}

}
