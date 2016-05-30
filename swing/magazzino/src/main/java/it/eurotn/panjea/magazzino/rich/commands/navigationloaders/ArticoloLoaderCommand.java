/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.commands.navigationloaders;

import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.editors.categoriaarticolo.ArticoloCategoriaDTO;
import it.eurotn.panjea.rich.factory.navigationloader.OpenEditorLoaderActionCommand;

import org.springframework.core.ReflectiveVisitorHelper;

/**
 * @author fattazzo
 * 
 */
public class ArticoloLoaderCommand extends OpenEditorLoaderActionCommand<ArticoloCategoriaDTO> {

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	private ReflectiveVisitorHelper visitorHelper = new ReflectiveVisitorHelper();
	private ArticoloLoaderObjectVisitor articoloVisitor = new ArticoloLoaderObjectVisitor();

	/**
	 * Costruttore.
	 */
	public ArticoloLoaderCommand() {
		super("articoloLoaderCommand");
	}

	@Override
	protected ArticoloCategoriaDTO getObjectForOpenEditor(Object loaderObject) {
		// siccome il command è mappato sia su articolo sia su articolo lite perchè deve fare la stessa cosa,
		// controllo cosa mi arriva dai parametri
		Articolo articolo = (Articolo) visitorHelper.invokeVisit(articoloVisitor, loaderObject);
		articolo = magazzinoAnagraficaBD.caricaArticolo(articolo, true);

		return new ArticoloCategoriaDTO(articolo, articolo.getCategoria());
	}

	@Override
	public Class<?>[] getTypes() {
		return new Class<?>[] { Articolo.class, ArticoloLite.class };
	}

	/**
	 * @param magazzinoAnagraficaBD
	 *            the magazzinoAnagraficaBD to set
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}

}
