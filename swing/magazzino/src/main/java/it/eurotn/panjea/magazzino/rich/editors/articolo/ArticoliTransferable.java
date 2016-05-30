/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.editors.articolo;

import it.eurotn.panjea.magazzino.domain.ArticoloLite;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.List;

/**
 * Un implementazione di {@link Transferable} con la capacita' di trasferire una {@link List} di {@link ArticoloLite}.
 * 
 * @author adriano
 * @version 1.0, 18/dic/2008
 * 
 */
public class ArticoliTransferable implements Transferable {

	private List<ArticoloLite> articoliToTransfer;
	public static final DataFlavor ARTICOLO_DATA_FLOWER = new DataFlavor(ArticoloLite.class, "ArticoloLite");

	/**
	 * @param articoliToTransfer
	 *            articoli da trasferire
	 */
	public ArticoliTransferable(final List<ArticoloLite> articoliToTransfer) {
		super();
		this.articoliToTransfer = articoliToTransfer;
	}

	@Override
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
		if (isDataFlavorSupported(flavor)) {
			return articoliToTransfer;
		} else {
			throw new UnsupportedFlavorException(flavor);
		}
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[] { ARTICOLO_DATA_FLOWER };
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return (flavor.equals(ARTICOLO_DATA_FLOWER));
	}

}
