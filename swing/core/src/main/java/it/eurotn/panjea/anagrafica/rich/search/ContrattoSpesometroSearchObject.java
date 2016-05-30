/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.search;

import it.eurotn.panjea.anagrafica.domain.ContrattoSpesometro;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.List;
import java.util.Map;

import org.springframework.richclient.command.AbstractCommand;

/**
 * @author Leonardo
 */
public class ContrattoSpesometroSearchObject extends AbstractSearchObject {

	private static final String SEARCH_OBJECT_ID = "contrattoSpesometroSearchObject";

	private static final String ENTITA_MAGAZZINO_FORM_PROPERTY_PATH = "areaMagazzino.documento.entita";
	private static final String ENTITA_CONTABILITA_FORM_PROPERTY_PATH = "areaContabile.documento.entita";

	public static final String ENTITA_KEY = "entita";
	private IAnagraficaTabelleBD anagraficaTabelleBD;

	/**
	 * Costruttore di default.
	 */
	public ContrattoSpesometroSearchObject() {
		super(SEARCH_OBJECT_ID);
	}

	@Override
	public List<AbstractCommand> getCustomCommands() {
		return null;
	}

	@Override
	public List<?> getData(String fieldSearch, String valueSearch) {
		Map<String, Object> parameters = searchPanel.getMapParameters();
		EntitaLite entita = null;
		if (parameters.get(ENTITA_KEY) != null) {
			entita = (EntitaLite) parameters.get(ENTITA_KEY);
		}

		return anagraficaTabelleBD.caricaContratti(entita);
	}

	@Override
	public void openDialogPage(Object object) {
		// recupero l'entita' dal form di origine (areaMagazzinoForm)
		EntitaLite entitaLite = null;
		if (searchPanel.getFormModel().hasValueModel(ENTITA_MAGAZZINO_FORM_PROPERTY_PATH)) {
			entitaLite = (EntitaLite) searchPanel.getFormModel().getValueModel(ENTITA_MAGAZZINO_FORM_PROPERTY_PATH)
					.getValue();
		}
		if (searchPanel.getFormModel().hasValueModel(ENTITA_CONTABILITA_FORM_PROPERTY_PATH)) {
			entitaLite = (EntitaLite) searchPanel.getFormModel().getValueModel(ENTITA_CONTABILITA_FORM_PROPERTY_PATH)
					.getValue();
		}
		if (entitaLite.getId() != null) {
			ContrattoSpesometro contratto = (ContrattoSpesometro) object;
			contratto.setEntita(entitaLite);
		}
		super.openDialogPage(object);
	}

	/**
	 * @param anagraficaTabelleBD
	 *            the anagraficaTabelleBD to set
	 */
	public void setAnagraficaTabelleBD(IAnagraficaTabelleBD anagraficaTabelleBD) {
		this.anagraficaTabelleBD = anagraficaTabelleBD;
	}

}
