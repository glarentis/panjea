/**
 * 
 */
package it.eurotn.panjea.tesoreria.rich.editors.areaTesoreria;

import it.eurotn.panjea.tesoreria.domain.AreaAssegno;

/**
 * @author leonardo
 * 
 */
public class AreaAssegnoPage extends AreaTesoreriaPage {

	public static final String PAGE_ID = "areaAssegnoPage";

	/**
	 * Costruttore.
	 */
	public AreaAssegnoPage() {
		super(PAGE_ID, new AreaAssegnoForm());
	}

	@Override
	public void setFormObject(Object object) {
		AreaAssegno areaAssegno = (AreaAssegno) object;
		if (areaAssegno != null && areaAssegno.getId() != null) {
			areaAssegno = tesoreriaBD.caricaImmagineAssegno(areaAssegno);
		}
		super.setFormObject(areaAssegno);
	}

}
