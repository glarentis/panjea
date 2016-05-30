/**
 *
 */
package it.eurotn.panjea.magazzino.rich.editors.categoriaarticolo;

import java.util.ArrayList;
import java.util.List;

import javax.swing.text.JTextComponent;

import org.apache.commons.lang3.StringUtils;

import com.jidesoft.hints.ListDataIntelliHints;

/**
 * @author leonardo
 */
public class JecListDataIntelliHints extends ListDataIntelliHints<String> {

	private String stringContext = null;

	/**
	 * Costruttore.
	 * 
	 * @param paramJTextComponent
	 *            text component
	 * @param paramList
	 *            lista di elementi da proporre
	 */
	public JecListDataIntelliHints(final JTextComponent paramJTextComponent, final List paramList) {
		super(paramJTextComponent, paramList);
		setCaseSensitive(false);
	}

	/*
	 * After user has selected a item in the hints popup, this method will update JTextComponent accordingly to accept
	 * the hint. For JTextArea, the default implementation will insert the hint into current caret position. For
	 * JTextField, by default it will replace the whole content with the item user selected.
	 * 
	 * Faccio in modo da permettere l'inserimento di più valori accettando la nuova stringa accodata a quella già
	 * esistente.
	 */
	@Override
	public void acceptHint(Object paramObject) {
		// System.out.println("acceptHint " + paramObject);
		String text = new String(stringContext);
		// System.out.println("text <" + text + ">");
		int indexLastDollaro = text.lastIndexOf("$");
		int numberOfMatches = StringUtils.countMatches(text, "$");
		// System.out.println("indexDollaro " + indexDollaro);

		// se il carattere dollaro è dispari quando seleziono un suggerimento devo sovrascriverlo, altrimenti ho già un
		// attributo valido inserito delimitato dal carattere dollaro e quindi il valore selezionato non deve
		// sovrascrivere nulla.
		if (indexLastDollaro != -1 && numberOfMatches % 2 != 0) {
			text = text.substring(0, indexLastDollaro);
		}
		text = text.concat(paramObject.toString());
		super.acceptHint(text);
	}

	// @Override
	// public boolean updateHints(Object context) {
	// if (context == null) {
	// return false;
	// }
	//
	// List<String> possibleStrings = new ArrayList<String>();
	// List<String> excludeList = new ArrayList<String>();
	//
	// String s = context.toString();
	// System.err.println("s " + s + " length " + s.length());
	//
	// if (!s.isEmpty()) {
	// for (String string : getCompletionList()) {
	// // lista chiavi già aggiunte solo se contiene l'intera stringa $chiave$
	// if (string.toLowerCase().indexOf(s.toLowerCase()) != -1) {
	// excludeList.add(string);
	// }
	// }
	// }
	//
	// int index = 0;
	// if (s.indexOf(" $") != -1) {
	// index = s.lastIndexOf("$ ");
	// index = index + 1;
	// }
	// s = s.substring(index, s.length());
	//
	// System.err.println("s2 " + s + " length " + s.length());
	//
	// for (String string : getCompletionList()) {
	// // lista filtrata proposta
	// if (string.toLowerCase().startsWith(s.toLowerCase())) {
	// possibleStrings.add(string);
	// }
	// }
	//
	// possibleStrings.removeAll(excludeList);
	//
	// Object[] objects = possibleStrings.toArray();
	//
	// setListData(objects);
	//
	// return true;
	// }

	/*
	 * Aggiorna la lista di suggerimenti a seconda del context ricevuto; se ritorna true mostra il popup con la lista di
	 * elementi, altrimenti nasconde il popup.
	 */
	@Override
	public boolean updateHints(Object context) {
		// System.out.println("context update hint " + context);
		if (context == null) {
			return false;
		}
		stringContext = context.toString();
		String stringContextLocal = new String(stringContext);
		// System.out.println(stringContextLocal);
		stringContextLocal = stringContextLocal.substring(0, getTextComponent().getCaretPosition());
		// System.out.println(stringContextLocal);
		int indexSpace = stringContextLocal.lastIndexOf(" ");

		int index = indexSpace;
		if (index != -1) {
			stringContextLocal = stringContextLocal.substring(index + "\n".length());
		}
		int substringLen = stringContextLocal.length();
		if (substringLen == 0) {
			return true;
		}

		List<String> possibleStrings = new ArrayList<String>();
		for (Object o : getCompletionList()) {
			String listEntry = (String) o;
			if (substringLen > listEntry.length()) {
				continue;
			}

			if (!isCaseSensitive()) {
				if (stringContextLocal.equalsIgnoreCase(listEntry.substring(0, substringLen))) {
					possibleStrings.add(listEntry);
				}
			} else if (listEntry.startsWith(stringContextLocal)) {
				possibleStrings.add(listEntry);
			}
		}

		Object[] objects = possibleStrings.toArray();
		setListData(objects);
		return possibleStrings.size() > 0;
	}
}
