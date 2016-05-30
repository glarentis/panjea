/**
 *
 */
package it.eurotn.panjea.contabilita.rich.search;

import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.panjea.anagrafica.rich.search.CodiceIvaSearchObject;
import it.eurotn.panjea.contabilita.domain.ContabilitaSettings;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile.TipologiaCorrispettivo;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.dialog.MessageDialog;

/**
 * SearchObject che sovrascrive il searchObject di base nell'anagrafica per eseguire la ricerca di {@link CodiceIva}
 * filtrata per tipologia corrispettivo
 * 
 * Il codice iva della riga iva viene filtrato a seconda della tipologiaCorrispettivo
 * (rigaIva.areaContabile.tipoAreaContabile.tipologiaCorrispettivo) se e' DIVENTILAZIONE allora posso selezionare solo
 * il codice iva per ventilazione associato nei settings contabilita', altrimenti posso selezionare tutti i codici iva
 * escluso quello per ventilazione. Nota che se i settings non sono ancora stati configurati verra' presentato
 * all'utente un messaggio che indica di configurare il codice iva per ventilazione al momento della ricerca nella
 * searchText.
 * 
 * @author Leonardo
 * @version 1.0, 01/gen/08
 */
public class CodiceIvaContabilitaSearchObject extends AbstractSearchObject {

	private IAnagraficaTabelleBD anagraficaTabelleBD;
	private IContabilitaAnagraficaBD contabilitaAnagraficaBD;

	public static final String TITLE_DIALOG = "codiceIvaSearchObject.messageDialog.codiceIvaPerVentilazione.toSet.title";
	public static final String MESSAGE_DIALOG = "codiceIvaSearchObject.messageDialog.codiceIvaPerVentilazione.toSet.description";

	/**
	 * Costruttore.
	 */
	public CodiceIvaContabilitaSearchObject() {
		super("codiceIvaSearchObject");
	}

	/**
	 * @return the anagraficaTabelleBD
	 */
	public IAnagraficaTabelleBD getAnagraficaTabelleBD() {
		return anagraficaTabelleBD;
	}

	/**
	 * @return the contabilitaAnagraficaBD
	 */
	public IContabilitaAnagraficaBD getContabilitaAnagraficaBD() {
		return contabilitaAnagraficaBD;
	}

	@Override
	public List<AbstractCommand> getCustomCommands() {
		return null;
	}

	@Override
	public List<?> getData(String arg0, String arg1) {
		Map<String, Object> parameters = searchPanel.getMapParameters();

		// lista tot di codici iva
		List<CodiceIva> list = anagraficaTabelleBD.caricaCodiciIva(arg1);

		// prendo i parametri tipologiaCorrispettico; se e' != null
		// allora voglio filtrare i codici iva
		TipologiaCorrispettivo tipologiaCorrispettivo = (TipologiaCorrispettivo) parameters
				.get("tipologiaCorrispettivo");

		// se ho avvalorato il parametro vuol dire che posso vedere se filtrare
		if (tipologiaCorrispettivo != null) {
			// carico il codice iva settato nei settings contabilita'
			ContabilitaSettings contabilitaSettings = contabilitaAnagraficaBD.caricaContabilitaSettings();
			CodiceIva codiceIva = contabilitaSettings.getCodiceIvaPerVentilazione();

			if (codiceIva == null) {
				MessageDialog dialog = new MessageDialog(getMessage(TITLE_DIALOG), getMessage(MESSAGE_DIALOG));
				dialog.showDialog();
				return new ArrayList<CodiceIva>();
			}

			if (tipologiaCorrispettivo.compareTo(TipologiaCorrispettivo.DA_VENTILARE) == 0) {
				list = new ArrayList<CodiceIva>();
				list.add(codiceIva);
			} else {
				list.remove(codiceIva);
			}
		}

		if (searchPanel.getParameters().containsKey(CodiceIvaSearchObject.CODICI_SPLIT_PAYMENT)) {
			ListIterator<CodiceIva> iterator = list.listIterator();
			while (iterator.hasNext()) {
				CodiceIva next = iterator.next();
				if (next.getSplitPayment() == null || !next.getSplitPayment()) {
					iterator.remove();
				}
			}
		}
		return list;
	}

	@Override
	public Object getValueObject(Object object) {
		// restituisco il codice iva ricaricandolo perchè mi servono alcune sue proprietà lazy (es. codiceIvaCollegato)
		return anagraficaTabelleBD.caricaCodiceIva(((CodiceIva) object).getId());
	}

	@Override
	public void openDialogPage(Object object) {
		CodiceIva codiceIva = ((CodiceIva) object);
		if (!codiceIva.isNew()) {
			codiceIva = anagraficaTabelleBD.caricaCodiceIva(((CodiceIva) object).getId());
		}
		super.openDialogPage(codiceIva);
	}

	/**
	 * @param anagraficaTabelleBD
	 *            the anagraficaTabelleBD to set
	 */
	public void setAnagraficaTabelleBD(IAnagraficaTabelleBD anagraficaTabelleBD) {
		this.anagraficaTabelleBD = anagraficaTabelleBD;
	}

	/**
	 * @param contabilitaAnagraficaBD
	 *            the contabilitaAnagraficaBD to set
	 */
	public void setContabilitaAnagraficaBD(IContabilitaAnagraficaBD contabilitaAnagraficaBD) {
		this.contabilitaAnagraficaBD = contabilitaAnagraficaBD;
	}

}
