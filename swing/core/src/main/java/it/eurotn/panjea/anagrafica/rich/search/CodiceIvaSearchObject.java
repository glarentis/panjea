/**
 *
 */
package it.eurotn.panjea.anagrafica.rich.search;

import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.List;
import java.util.ListIterator;

import org.springframework.richclient.command.AbstractCommand;

/**
 * SearchObject per eseguire la ricerca di {@link CodiceIva}.
 *
 * @author adriano
 * @version 1.0, 25/mag/07
 */
public class CodiceIvaSearchObject extends AbstractSearchObject {

	private IAnagraficaTabelleBD anagraficaTabelleBD;

	public static final String TITLE_DIALOG = "codiceIvaSearchObject.messageDialog.codiceIvaPerVentilazione.toSet.title";
	public static final String MESSAGE_DIALOG = "codiceIvaSearchObject.messageDialog.codiceIvaPerVentilazione.toSet.description";

	public static final String CODICI_SPLIT_PAYMENT = "splitPlayment";

	/**
	 * Costruttore.
	 */
	public CodiceIvaSearchObject() {
		super("codiceIvaSearchObject");
	}

	/**
	 * @return the anagraficaTabelleBD
	 */
	public IAnagraficaTabelleBD getAnagraficaTabelleBD() {
		return anagraficaTabelleBD;
	}

	@Override
	public List<AbstractCommand> getCustomCommands() {
		return null;
	}

	@Override
	public List<?> getData(String arg0, String arg1) {
		// lista tot di codici iva
		List<CodiceIva> list = anagraficaTabelleBD.caricaCodiciIva(arg1);
		if (searchPanel.getParameters().containsKey(CODICI_SPLIT_PAYMENT)) {
			ListIterator<CodiceIva> iterator = list.listIterator();
			while (iterator.hasNext()) {
				if (!iterator.next().getSplitPayment()) {
					iterator.remove();
				}
			}
		}
		return list;
	}

	@Override
	public void openDialogPage(Object object) {
		CodiceIva codiceIva = anagraficaTabelleBD.caricaCodiceIva(((CodiceIva) object).getId());
		super.openDialogPage(codiceIva);
	}

	/**
	 * @param anagraficaTabelleBD
	 *            the anagraficaTabelleBD to set
	 */
	public void setAnagraficaTabelleBD(IAnagraficaTabelleBD anagraficaTabelleBD) {
		this.anagraficaTabelleBD = anagraficaTabelleBD;
	}
}
