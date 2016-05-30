package it.eurotn.panjea.exporter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.writer.CSVField;

/**
 * @author fattazzo
 * 
 */
public class CSVConfig extends org.apache.commons.csv.writer.CSVConfig {

	private Map<String, List<CSVField>> mapTipiRecord;

	/**
	 * Costruttore.
	 * 
	 */
	public CSVConfig() {
		super();
		mapTipiRecord = new HashMap<String, List<CSVField>>();
	}

	@Override
	public void addField(CSVField field) {
		super.addField(field);
	}

	/**
	 * Aggiunge il {@link CSVField} al tipo record indicato.
	 * 
	 * @param field
	 *            field
	 * @param tipoRecord
	 *            tipo record
	 */
	public void addField(CSVField field, String tipoRecord) {
		if (mapTipiRecord.containsKey(tipoRecord)) {
			mapTipiRecord.get(tipoRecord).add(field);
		} else {
			List<CSVField> list = new ArrayList<CSVField>();
			list.add(field);
			mapTipiRecord.put(tipoRecord, list);
		}
		super.addField(field);
	}

	/**
	 * Restituisce la lista dei field associati al tipo record specificato. Se
	 * il tipo record è <code>null</code> verranno restituiti tutti i field
	 * configurati.
	 * 
	 * @param tipoRecord
	 *            tipo record
	 * @return lista fi {@link CSVField}
	 */
	public CSVField[] getFields(String tipoRecord) {

		if (tipoRecord == null) {
			return super.getFields();
		} else {
			return mapTipiRecord.get(tipoRecord).toArray(new CSVField[mapTipiRecord.get(tipoRecord).size()]);
		}
	}

}
