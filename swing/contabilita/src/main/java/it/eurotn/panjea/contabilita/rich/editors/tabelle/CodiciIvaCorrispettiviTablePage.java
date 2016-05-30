package it.eurotn.panjea.contabilita.rich.editors.tabelle;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.rich.factory.list.TipoDocumentoCellRenderer;
import it.eurotn.panjea.contabilita.domain.CodiceIvaCorrispettivo;
import it.eurotn.panjea.contabilita.domain.RegistroIva.TipoRegistro;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.table.EditFrame;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.springframework.richclient.util.GuiStandardUtils;

/**
 * @author Fattazzo <g.fattarsi@eurotn.it>
 */
public class CodiciIvaCorrispettiviTablePage extends AbstractTablePageEditor<CodiceIvaCorrispettivo> {

	/**
	 * Action listener associato alla combo box che aggiorna i dati della tabella dei codici iva corrispettivo al cambio
	 * della selezione.
	 */
	private class ChangeTipoDocumentoAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			loadData();
		}
	}

	private class TipoDocumentoComboBox extends JComboBox {
		private static final long serialVersionUID = -7232612644973737608L;

		/**
		 * Costruttore.
		 */
		public TipoDocumentoComboBox() {
			super();
			addActionListener(new ChangeTipoDocumentoAction());
			setRenderer(new TipoDocumentoCellRenderer());
		}
	}

	public static final String PAGE_ID = "codiciIvaCorrispettiviTablePage";
	private IContabilitaAnagraficaBD contabilitaAnagraficaBD;

	private IContabilitaBD contabilitaBD;

	private static final String LABEL_TIPO_DOCUMENTO = "tipoDocumento";

	private TipoDocumentoComboBox tipoDocumentoComboBox = null;

	/**
	 * Costruttore.
	 */
	protected CodiciIvaCorrispettiviTablePage() {
		super(PAGE_ID, new String[] { "ordinamento", "codiceIva.codice", "codiceIva.descrizioneInterna" },
				CodiceIvaCorrispettivo.class);
	}

	/**
	 * Crea i controlli per la selezione del tipo documento.
	 * 
	 * @return controlli creati
	 */
	private JComponent createComboPanel() {
		JPanel panelCombo = getComponentFactory().createPanel(new FlowLayout(FlowLayout.LEFT));
		tipoDocumentoComboBox = new TipoDocumentoComboBox();
		populateTipoDocumentoComboBox();

		JLabel labelTipoDocumento = getComponentFactory().createLabel(
				getMessageSource().getMessage(LABEL_TIPO_DOCUMENTO, new Object[] {}, Locale.getDefault()));
		GuiStandardUtils.attachBorder(labelTipoDocumento);

		panelCombo.add(labelTipoDocumento);
		panelCombo.add(tipoDocumentoComboBox);

		return panelCombo;
	}

	@Override
	public JComponent getHeaderControl() {
		return createComboPanel();
	}

	@Override
	public Collection<CodiceIvaCorrispettivo> loadTableData() {

		List<CodiceIvaCorrispettivo> codici = null;

		if (tipoDocumentoComboBox.getSelectedItem() != null && CodiciIvaCorrispettiviTablePage.this.isControlCreated()) {
			// il tipo documento selezionato
			TipoDocumento tipoDocumento = (TipoDocumento) tipoDocumentoComboBox.getSelectedItem();
			codici = contabilitaAnagraficaBD.caricaCodiciIvaCorrispettivo(tipoDocumento);

			CodiceIvaCorrispettivoPage codiceIvaCorrispettivoPage = (CodiceIvaCorrispettivoPage) getEditPages().get(
					EditFrame.DEFAULT_OBJECT_CLASS_NAME);
			codiceIvaCorrispettivoPage.setTipoDocumento(tipoDocumento);
		}

		return codici;
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	/**
	 * Aggiunge i tipi documento configurati per il tipo registro corrispettivo.
	 */
	private void populateTipoDocumentoComboBox() {
		List<TipoDocumento> listTipoDocumento = new ArrayList<TipoDocumento>();
		listTipoDocumento = contabilitaBD.caricaTipiDocumentoByTipoRegistro(TipoRegistro.CORRISPETTIVO);

		tipoDocumentoComboBox.removeAllItems();
		for (TipoDocumento tipoDocumento : listTipoDocumento) {
			tipoDocumentoComboBox.addItem(tipoDocumento);
		}

	}

	@Override
	public Collection<CodiceIvaCorrispettivo> refreshTableData() {
		return null;
	}

	/**
	 * @param contabilitaAnagraficaBD
	 *            contabilitaAnagraficaBD to set
	 */
	public void setContabilitaAnagraficaBD(IContabilitaAnagraficaBD contabilitaAnagraficaBD) {
		this.contabilitaAnagraficaBD = contabilitaAnagraficaBD;
	}

	/**
	 * @param contabilitaBD
	 *            contabilitaBD to set
	 */
	public void setContabilitaBD(IContabilitaBD contabilitaBD) {
		this.contabilitaBD = contabilitaBD;
	}

	@Override
	public void setFormObject(Object object) {
	}

}
