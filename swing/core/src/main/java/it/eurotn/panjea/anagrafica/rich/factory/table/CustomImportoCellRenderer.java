/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.factory.table;

import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.rich.converter.ImportoConverter;
import it.eurotn.panjea.rich.factory.table.AbstractCustomTableCellRenderer;

import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.grid.EditorContext;

/**
 * @author Leonardo
 * 
 */
public class CustomImportoCellRenderer extends AbstractCustomTableCellRenderer {

	public static final EditorContext CONTEXT = new EditorContext("importo");;

	private static final long serialVersionUID = -5821573644725375130L;

	@Override
	public String getIconKey(Object value, boolean isSelected, boolean hasFocus) {
		return null;
	}

	@Override
	public String getRendererText(Object value, boolean isSelected, boolean hasFocus) {

		return ObjectConverterManager.toString(value);
	}

	@Override
	public String getRendererToolTipText(Object value, boolean isSelected, boolean hasFocus) {

		return ObjectConverterManager.toString(value, Importo.class, ImportoConverter.HTML_CONVERTER_CONTEXT);
	}

}
