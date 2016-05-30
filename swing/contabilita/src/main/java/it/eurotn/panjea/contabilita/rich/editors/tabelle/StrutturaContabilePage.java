/**
 *
 */
package it.eurotn.panjea.contabilita.rich.editors.tabelle;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.contabilita.domain.StrutturaContabile;
import it.eurotn.panjea.contabilita.rich.bd.ContabilitaBD;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaBD;
import it.eurotn.panjea.contabilita.rich.forms.StrutturaContabileForm;
import it.eurotn.panjea.contabilita.rich.pm.StrutturaContabilePM;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import java.awt.BorderLayout;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.springframework.context.NoSuchMessageException;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.util.RcpSupport;

/**
 * @author fattazzo
 * @version 1.0, 30/ago/07
 */
public class StrutturaContabilePage extends FormBackedDialogPageEditor {

	private static final String PAGE_ID = "strutturaContabilePage";
	private final IContabilitaAnagraficaBD contabilitaAnagraficaBD;
	private final IContabilitaBD contabilitaBD = RcpSupport.getBean(ContabilitaBD.BEAN_ID);
	private static final String STRUTTURA_CONTABILE_VARIABILI_LEGENDA_TITLE = "StruttureContabiliTablePage.variabili.legenda";
	private static final String STRUTTURA_CONTABILE_VARIABILE = "StruttureContabiliTablePage.variabile.";
	public static final String CONTROPARTITA_STRUTTURA_CONTABILE_CHANGE = "controPartitaStrutturaContabileChange";
	private EntitaLite entita = null;
	private TipoDocumento tipoDocumento;

	/**
	 * Costruttore.
	 *
	 * @param contabilitaAnagraficaBD
	 *            contabilitaAnagraficaBD
	 */
	public StrutturaContabilePage(final IContabilitaAnagraficaBD contabilitaAnagraficaBD) {
		super(PAGE_ID, new StrutturaContabileForm(new StrutturaContabilePM(new StrutturaContabile()),
				contabilitaAnagraficaBD));
		this.contabilitaAnagraficaBD = contabilitaAnagraficaBD;
	}

	/**
	 * @return crea il pannello che contiene l'elenco delle variabili disponibili
	 */
	private JPanel creaLegendaVariabili() {
		// carico l'elenco delle variabili che si possono avere
		List<String> variabili = contabilitaBD.caricaVariabiliFormulaStrutturaContabile();

		// se non ce ne sono non metto niente sul piede della tabella
		if (variabili == null || variabili.isEmpty()) {
			return null;
		} else {
			JPanel panelNorth = new JPanel(new BorderLayout());

			panelNorth.setBorder(BorderFactory.createTitledBorder(getMessageSource().getMessage(
					STRUTTURA_CONTABILE_VARIABILI_LEGENDA_TITLE, new Object[] {}, Locale.getDefault())));

			StringBuffer descrizione = new StringBuffer();
			descrizione.append("<HTML>");
			for (String variabile : variabili) {
				String nomeVariabile = "";
				try {
					nomeVariabile = getMessageSource().getMessage(STRUTTURA_CONTABILE_VARIABILE + variabile,
							new Object[] {}, Locale.getDefault());

				} catch (NoSuchMessageException ex) {
					nomeVariabile = variabile;
				}
				descrizione.append("<B>" + variabile + " : </B>" + nomeVariabile + "<BR>");
			}
			descrizione.append("</HTML>");
			JLabel label = new JLabel(descrizione.toString());
			panelNorth.add(label, BorderLayout.NORTH);

			// pannello principale per compattare il box della legenda
			JPanel panel = getComponentFactory().createPanel(new BorderLayout());
			panel.add(panelNorth, BorderLayout.CENTER);
			return panel;
		}
	}

	@Override
	protected Object doDelete() {
		StrutturaContabile strutturaContabile = (StrutturaContabile) getBackingFormPage().getFormObject();
		contabilitaAnagraficaBD.cancellaStrutturaContabile(strutturaContabile);

		return getBackingFormPage().getFormObject();
	}

	@Override
	protected Object doSave() {
		StrutturaContabilePM strutturaContabilePM = (StrutturaContabilePM) getBackingFormPage().getFormObject();
		StrutturaContabile strutturaContabile = strutturaContabilePM.getStrutturaContabile();

		strutturaContabile = contabilitaAnagraficaBD.salvaStrutturaContabile(strutturaContabile);
		strutturaContabilePM.setStrutturaContabile(strutturaContabile);
		return strutturaContabilePM;
	}

	@Override
	protected AbstractCommand[] getCommand() {
		return null;
	}

	@Override
	public JComponent getControl() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(super.getControl(), BorderLayout.CENTER);
		panel.add(creaLegendaVariabili(), BorderLayout.EAST);
		return panel;
	}

	@Override
	public void loadData() {
	}

	@Override
	public void onNew() {
		super.onNew();
		StrutturaContabile strutturaContabile = new StrutturaContabile();
		strutturaContabile.setTipoDocumento(this.tipoDocumento);
		strutturaContabile.setEntita(this.entita);
		StrutturaContabilePM strutturaContabilePM = new StrutturaContabilePM(strutturaContabile);
		getBackingFormPage().setFormObject(strutturaContabilePM);
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public void refreshData() {
	}

	/**
	 * @param entita
	 *            the entita to set
	 */
	public void setEntita(EntitaLite entita) {
		this.entita = entita;
	}

	/**
	 * @param tipoDocumento
	 *            the tipoDocumento to set
	 */
	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
}
