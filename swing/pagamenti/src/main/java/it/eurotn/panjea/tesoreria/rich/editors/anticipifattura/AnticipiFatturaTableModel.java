/**
 *
 */
package it.eurotn.panjea.tesoreria.rich.editors.anticipifattura;

import it.eurotn.panjea.tesoreria.util.SituazioneRata;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

/**
 * @author leonardo
 * 
 */
public class AnticipiFatturaTableModel extends DefaultBeanTableModel<SituazioneRata> {

	private static final long serialVersionUID = 5805284550015960221L;

	/**
	 * AnticipiFatturaTableModel.
	 */
	public AnticipiFatturaTableModel() {
		super(AnticipiFatturaTablePage.PAGE_ID, new String[] { "rata.dataScadenza", "dataScadenzaAnticipoFatture",
				"rata.importo", "rata.tipoPagamento", "residuoRata", "rata.numeroRata", "entita", "rata.note",
				"rata.areaRate.codicePagamento" }, SituazioneRata.class);
	}

}
