package it.eurotn.panjea.magazzino.rich.editors.articolo;

import it.eurotn.panjea.magazzino.util.RigaListinoDTO;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import org.apache.log4j.Logger;

import com.jidesoft.converter.ConverterContext;

public class RigheListinoArticoloTableModel extends DefaultBeanTableModel<RigaListinoDTO> {

	private static Logger logger = Logger.getLogger(RigheListinoArticoloTableModel.class);
	private static final long serialVersionUID = -6972366018643185422L;

	private static ConverterContext context = new NumberWithDecimalConverterContext();

	/**
	 * Costruttore.
	 */
	public RigheListinoArticoloTableModel() {
		super(RigheListinoArticoloTablePage.RIGHE_LISTINO_ARTICOLO_PAGE_ID, new String[] { "dataVigoreVersioneListino",
				"codiceVersioneListino", "descrizioneListino", "prezzo", "ultimoCosto" }, RigaListinoDTO.class);
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {
		switch (column) {
		case 3:
			if (row == -1) {
				return context;
			}
			try {
				RigaListinoDTO rigaListino = getElementAt(row);
				context.setUserObject(rigaListino.getNumeroDecimaliPrezzo());
			} catch (ArrayIndexOutOfBoundsException aioe) {
				int rowCount = getRowCount();
				logger.error("Errore getConverterContextAt row --> " + row + " row count --> " + rowCount, aioe);
			}
			return context;
		case 4:
			context.setUserObject(6);
			return context;
		default:
			return super.getConverterContextAt(row, column);
		}
	}

}
