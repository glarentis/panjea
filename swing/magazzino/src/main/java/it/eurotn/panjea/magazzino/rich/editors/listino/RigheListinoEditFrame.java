/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.editors.listino;

import it.eurotn.panjea.magazzino.domain.RigaListino;
import it.eurotn.panjea.magazzino.util.RigaListinoDTO;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.table.EEditPageMode;
import it.eurotn.rich.editors.table.EditFrame;

/**
 * @author fattazzo
 * 
 */
public class RigheListinoEditFrame extends EditFrame<RigaListinoDTO> {

	private static final long serialVersionUID = -325353262516892890L;

	/**
	 * Costruttore.
	 * 
	 * @param editView
	 *            editView
	 * @param pageEditor
	 *            pageEditor
	 * @param startQuickAction
	 *            quick action
	 */
	public RigheListinoEditFrame(EEditPageMode editView, AbstractTablePageEditor<RigaListinoDTO> pageEditor,
			String startQuickAction) {
		super(editView, pageEditor, startQuickAction);
	}

	@Override
	public RigaListinoDTO getTableManagedObject(Object object) {
		RigaListino rigaListino = (RigaListino) object;

		RigaListinoDTO rigaListinoDTO = new RigaListinoDTO();
		rigaListinoDTO.setBarCodeArticolo(rigaListino.getArticolo().getBarCode());
		rigaListinoDTO.setCodiceArticolo(rigaListino.getArticolo().getCodice());
		rigaListinoDTO.setCodiceCodiceIva(rigaListino.getArticolo().getCodiceIva().getCodice());
		rigaListinoDTO.setCodiceUnitaMisura(rigaListino.getArticolo().getUnitaMisura().getCodice());
		rigaListinoDTO.setCodiceVersioneListino(rigaListino.getVersioneListino().getCodice());
		rigaListinoDTO.setDataVigoreVersioneListino(rigaListino.getVersioneListino().getDataVigore());
		rigaListinoDTO.setDescrizioneArticolo(rigaListino.getArticolo().getDescrizione());
		rigaListinoDTO.setId(rigaListino.getId());
		rigaListinoDTO.setIdArticolo(rigaListino.getArticolo().getId());
		rigaListinoDTO.setIdCodiceIva(rigaListino.getArticolo().getCodiceIva().getId());
		rigaListinoDTO.setIdListino(rigaListino.getVersioneListino().getListino().getId());
		rigaListinoDTO.setIdUnitaMisura(rigaListino.getArticolo().getUnitaMisura().getId());
		rigaListinoDTO.setIdVersioneListino(rigaListino.getVersioneListino().getId());
		rigaListinoDTO.setNumeroDecimaliPrezzo(rigaListino.getNumeroDecimaliPrezzo());
		rigaListinoDTO.setPrezzo(rigaListino.getPrezzo());
		rigaListinoDTO.setCodiceListino(rigaListino.getVersioneListino().getListino().getCodice());
		rigaListinoDTO.setDescrizioneListino(rigaListino.getVersioneListino().getListino().getDescrizione());
		return rigaListinoDTO;
	}

}
