/**
 * 
 */
package it.eurotn.panjea.tesoreria.rich.commands;

import it.eurotn.panjea.anagrafica.rich.editors.documento.AbstractEliminaDocumentoCommand;
import it.eurotn.panjea.tesoreria.domain.AreaTesoreria;
import it.eurotn.panjea.tesoreria.rich.bd.ITesoreriaBD;

import java.util.List;

/**
 * @author Leonardo
 * 
 */
public class EliminaAreaTesoreriaCommand extends AbstractEliminaDocumentoCommand {

	public static final String COMMAND_ID = "eliminaAreaPagamentiCommand";

	public static final String PARAM_AREA_TESORERIA = "areaTesoreriaParam";
	private ITesoreriaBD tesoreriaBD = null;
	private List<AreaTesoreria> areeDaCancellare = null;

	/**
	 * Costruttore.
	 */
	public EliminaAreaTesoreriaCommand() {
		super(COMMAND_ID);
		setEnabled(false);
	}

	@Override
	public Object doDelete(boolean deleteAreeCollegate) {
		AreaTesoreria areaTesoreria = (AreaTesoreria) getParameter(PARAM_AREA_TESORERIA, null);
		if (areaTesoreria != null) {
			tesoreriaBD.cancellaAreaTesoreria(areaTesoreria);
			return areaTesoreria;
		} else {
			tesoreriaBD.cancellaAreeTesorerie(areeDaCancellare);
			return areeDaCancellare;
		}

	}

	/**
	 * @return the tesoreriaBD
	 */
	public ITesoreriaBD getTesoreriaBD() {
		return tesoreriaBD;
	}

	/**
	 * @param areeDaCancellare
	 *            the areeDaCancellare to set
	 */
	public void setAreeDaCancellare(List<AreaTesoreria> areeDaCancellare) {
		this.areeDaCancellare = areeDaCancellare;
	}

	/**
	 * @param tesoreriaBD
	 *            the tesoreriaBD to set
	 */
	public void setTesoreriaBD(ITesoreriaBD tesoreriaBD) {
		this.tesoreriaBD = tesoreriaBD;
	}

}
