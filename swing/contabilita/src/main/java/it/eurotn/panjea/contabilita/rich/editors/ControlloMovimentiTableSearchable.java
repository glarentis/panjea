package it.eurotn.panjea.contabilita.rich.editors;

import javax.swing.JTable;

import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.swing.TableSearchable;

public class ControlloMovimentiTableSearchable extends TableSearchable {

	/**
	 * Costruttore.
	 * 
	 * @param jtable
	 *            tabella
	 * 
	 */
	public ControlloMovimentiTableSearchable(final JTable jtable) {
		super(jtable);
		setMainIndex(-1);
		setRepeats(true);
	}

	@Override
	protected String convertElementToString(Object obj) {
		String value = ObjectConverterManager.toString(obj);
		if (value == null) {
			value = super.convertElementToString(obj);
		}
		return value;
	}

	@Override
	public int findFirst(String s) {
		int findIdx = super.findFirst(s);
		System.err.println("Indice findFirst: " + findIdx);
		return findIdx;
	}

	@Override
	public int findFromCursor(String s) {
		int findIdx = super.findFromCursor(s);
		System.err.println("Indice findFromCursor: " + findIdx);
		return findIdx;
	}

	@Override
	public int findLast(String s) {
		int findIdx = super.findLast(s);
		System.err.println("Indice findLast: " + findIdx);
		return findIdx;
	}

	@Override
	public int findNext(String s) {
		int findIdx = super.findNext(s);
		System.err.println("Indice findNext: " + findIdx);
		return findIdx;
	}

	@Override
	public int findPrevious(String s) {
		int findIdx = super.findPrevious(s);
		System.err.println("Indice findPrevious: " + findIdx);
		return findIdx;
	}
}
