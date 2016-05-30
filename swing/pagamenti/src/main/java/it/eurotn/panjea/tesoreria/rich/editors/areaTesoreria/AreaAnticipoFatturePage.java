/**
 *
 */
package it.eurotn.panjea.tesoreria.rich.editors.areaTesoreria;

/**
 * @author leonardo
 */
public class AreaAnticipoFatturePage extends AreaTesoreriaPage {

	public static final String PAGE_ID = "areaAnticipoFatturePage";

	/**
	 * Costruttore.
	 */
	public AreaAnticipoFatturePage() {
		super(PAGE_ID, new AreaAnticipoFattureForm());
	}

}
