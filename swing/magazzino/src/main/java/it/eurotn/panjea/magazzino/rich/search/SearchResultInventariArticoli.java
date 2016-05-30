package it.eurotn.panjea.magazzino.rich.search;

import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.util.InventarioArticoloDTO;
import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;
import it.eurotn.panjea.rich.pages.AbstractTableSearchResult;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;

public class SearchResultInventariArticoli extends AbstractTableSearchResult<InventarioArticoloDTO> {

	private static final String VIEW_ID = "searchResultInventariArticoli";

	private IMagazzinoDocumentoBD magazzinoDocumentoBD;

	@Override
	protected Object delete() {
		List<InventarioArticoloDTO> inventari = getSelectedObjects();
		if (inventari == null || inventari.isEmpty()) {
			return null;
		}

		for (InventarioArticoloDTO inventarioArticoloDTO : inventari) {
			InventarioArticoloDTO deletedObj = doDelete(inventarioArticoloDTO);
			if (deletedObj != null) {
				getTableWidget().removeRowObject(deletedObj);

				PanjeaLifecycleApplicationEvent deleteEvent = new PanjeaLifecycleApplicationEvent(
						LifecycleApplicationEvent.DELETED, deletedObj, this);
				Application.instance().getApplicationContext().publishEvent(deleteEvent);
			}
		}

		return null;
	}

	@Override
	protected InventarioArticoloDTO doDelete(InventarioArticoloDTO objectToDelete) {
		magazzinoDocumentoBD.cancellaInventarioArticolo(objectToDelete.getData(), objectToDelete.getDeposito());
		return objectToDelete;
	}

	@Override
	protected String[] getColumnPropertyNames() {
		return new String[] { "data", "deposito" };
	}

	@Override
	protected AbstractCommand[] getCommand() {
		return new AbstractCommand[] { getRefreshCommand(), getDeleteCommand() };
	}

	@Override
	protected Collection<InventarioArticoloDTO> getData(Map<String, Object> parameters) {
		return magazzinoDocumentoBD.caricaInventariiArticoli();
	}

	@Override
	public String getId() {
		return VIEW_ID;
	}

	@Override
	protected Class<InventarioArticoloDTO> getObjectsClass() {
		return InventarioArticoloDTO.class;
	}

	@Override
	protected Map<String, Object> getParameters() {
		return null;
	}

	@Override
	public Object reloadObject(InventarioArticoloDTO object) {
		return object;
	}

	/**
	 * @param magazzinoDocumentoBD
	 *            the magazzinoDocumentoBD to set
	 */
	public void setMagazzinoDocumentoBD(IMagazzinoDocumentoBD magazzinoDocumentoBD) {
		this.magazzinoDocumentoBD = magazzinoDocumentoBD;
	}

}
