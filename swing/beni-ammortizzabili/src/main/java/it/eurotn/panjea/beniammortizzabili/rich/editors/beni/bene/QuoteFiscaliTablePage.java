package it.eurotn.panjea.beniammortizzabili.rich.editors.beni.bene;

import it.eurotn.panjea.beniammortizzabili.rich.bd.IBeniAmmortizzabiliBD;
import it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabile;
import it.eurotn.panjea.beniammortizzabili2.domain.QuotaAmmortamentoFiscale;
import it.eurotn.panjea.beniammortizzabili2.domain.ValutazioneBene;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.Collection;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommandExecutor;
import org.springframework.richclient.dialog.DialogPage;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class QuoteFiscaliTablePage extends AbstractTablePageEditor<QuotaAmmortamentoFiscale> implements
InitializingBean {

	private final class QuotaFiscaleDialog extends PanjeaTitledPageApplicationDialog {
		/**
		 * Costruttore.
		 *
		 * @param dialogPage
		 *            dialogPage
		 */
		private QuotaFiscaleDialog(final DialogPage dialogPage) {
			super(dialogPage);
		}

		@Override
		protected Object[] getCommandGroupMembers() {
			return (new AbstractCommand[] { getFinishCommand() });
		}

		@Override
		protected boolean isMessagePaneVisible() {
			return false;
		}

		@Override
		protected boolean onFinish() {
			return true;
		}
	}

	public static final String PAGE_ID = "quoteFiscaliTablePage";

	private IBeniAmmortizzabiliBD beniAmmortizzabiliBD;

	protected BeneAmmortizzabile beneAmmortizzabile;

	private BigDecimal totaleImpOrdinario = BigDecimal.ZERO;

	private Double totalePercOrdinario = new Double(0.0);

	private BigDecimal totaleImpAnticipato = BigDecimal.ZERO;
	private Double totalePercAnticipato = new Double(0.0);
	private BigDecimal totaleImpAccelerato = BigDecimal.ZERO;
	private Double totalePercAccelerato = new Double(0.0);
	private final JLabel totaleImpOrdinarioLabel = new JLabel("", JLabel.RIGHT);
	private final JLabel totalePercOrdinarioLabel = new JLabel("", JLabel.RIGHT);
	private final JLabel totaleImpAnticipatoJLabel = new JLabel("", JLabel.RIGHT);
	private final JLabel totalePercAnticipatoLabel = new JLabel("", JLabel.RIGHT);
	private final JLabel totaleImpAcceleratoLabel = new JLabel("", JLabel.RIGHT);
	private final JLabel totalePercAcceleratoLabel = new JLabel("", JLabel.RIGHT);

	private BigDecimal totaleVariazioniBene = BigDecimal.ZERO;
	private BigDecimal totaleVariazioniFondo = BigDecimal.ZERO;
	private final JLabel totaleVariazioniBeneLabel = new JLabel("", JLabel.RIGHT);
	private final JLabel totaleVariazioniFondoLabel = new JLabel("", JLabel.RIGHT);

	private QuotaAmmortamentoFiscalePage quotaAmmortamentoFiscalePage;

	private QuotaFiscaleDialog quotaFiscaleDialog;

	/**
	 * Costruttore.
	 *
	 */
	public QuoteFiscaliTablePage() {
		this(PAGE_ID);
	}

	/**
	 * Costruttore.
	 *
	 * @param pageId
	 *            id della pagina
	 */
	public QuoteFiscaliTablePage(final String pageId) {
		super(pageId, new QuoteFiscaliTableModel());
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.quotaAmmortamentoFiscalePage = new QuotaAmmortamentoFiscalePage(beniAmmortizzabiliBD);
		this.quotaFiscaleDialog = new QuotaFiscaleDialog(quotaAmmortamentoFiscalePage);

		getTable().setPropertyCommandExecutor(new ActionCommandExecutor() {

			@Override
			public void execute() {
				quotaAmmortamentoFiscalePage.setFormObject(getTable().getSelectedObject());
				quotaFiscaleDialog.showDialog();
			}
		});
	}

	/**
	 * Calcola i totali delle quote caricate.
	 *
	 * @param list
	 *            quote caricate
	 */
	private void calcolaTotaliQuote(Collection<QuotaAmmortamentoFiscale> list) {

		totaleImpOrdinario = BigDecimal.ZERO;
		totalePercOrdinario = new Double(0.0);
		totaleImpAnticipato = BigDecimal.ZERO;
		totalePercAnticipato = new Double(0.0);
		totaleImpAccelerato = BigDecimal.ZERO;
		totalePercAccelerato = new Double(0.0);

		for (QuotaAmmortamentoFiscale quotaAmmortamentoFiscale : list) {
			totaleImpOrdinario = totaleImpOrdinario.add(quotaAmmortamentoFiscale.getImpQuotaAmmortamentoOrdinario());
			totalePercOrdinario = totalePercOrdinario
					+ quotaAmmortamentoFiscale.getPercQuotaAmmortamentoOrdinarioApplicata();
			totaleImpAnticipato = totaleImpAnticipato.add(quotaAmmortamentoFiscale.getImpQuotaAmmortamentoAnticipato());
			totalePercAnticipato = totalePercAnticipato
					+ quotaAmmortamentoFiscale.getPercQuotaAmmortamentoAnticipatoApplicata();
			totaleImpAccelerato = totaleImpAccelerato.add(quotaAmmortamentoFiscale.getImpQuotaAmmortamentoAccelerato());
			totalePercAccelerato = totalePercAccelerato + quotaAmmortamentoFiscale.getPercQuotaAmmortamentoAccelerato();
		}
	}

	/**
	 * @return the beniAmmortizzabiliBD
	 */
	public IBeniAmmortizzabiliBD getBeniAmmortizzabiliBD() {
		return beniAmmortizzabiliBD;
	}

	@Override
	public AbstractCommand[] getCommands() {
		return new AbstractCommand[] { getRefreshCommand() };
	}

	@Override
	public JComponent getFooterControl() {
		FormLayout layout = new FormLayout(
				"left:default,4dlu,right:60dlu,right:60dlu, 40dlu,left:default,4dlu,right:60dlu",
				"default,10dlu,default,default,default,default,default");
		PanelBuilder builder = new PanelBuilder(layout);
		CellConstraints cc = new CellConstraints();

		// totali quote
		builder.addLabel("Totali", cc.xy(1, 1));
		builder.addSeparator("", cc.xyw(1, 2, 4));

		builder.addLabel("Ordinario", cc.xy(1, 3));
		builder.add(totaleImpOrdinarioLabel, cc.xy(3, 3));
		builder.add(totalePercOrdinarioLabel, cc.xy(4, 3));

		builder.addLabel("Anticipato", cc.xy(1, 5));
		builder.add(totaleImpAnticipatoJLabel, cc.xy(3, 5));
		builder.add(totalePercAnticipatoLabel, cc.xy(4, 5));

		builder.addLabel("Accelerato", cc.xy(1, 7));
		builder.add(totaleImpAcceleratoLabel, cc.xy(3, 7));
		builder.add(totalePercAcceleratoLabel, cc.xy(4, 7));

		// dati variazioni
		builder.addLabel("Variazioni", cc.xy(6, 1));
		builder.addSeparator("", cc.xyw(6, 2, 3));

		builder.addLabel("Valore bene", cc.xy(6, 3));
		builder.add(totaleVariazioniBeneLabel, cc.xy(8, 3));

		builder.addLabel("Valore fondo", cc.xy(6, 5));
		builder.add(totaleVariazioniFondoLabel, cc.xy(8, 5));

		JPanel panel = builder.getPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
		return panel;
	}

	@Override
	public Collection<QuotaAmmortamentoFiscale> loadTableData() {
		List<QuotaAmmortamentoFiscale> listQuote = null;

		if (beneAmmortizzabile != null) {
			listQuote = beniAmmortizzabiliBD.caricaQuoteAmmortamentoFiscaliConsolidate(beneAmmortizzabile);
		}

		return listQuote;
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public void processTableData(Collection<QuotaAmmortamentoFiscale> results) {
		super.processTableData(results);

		if (beneAmmortizzabile != null && results != null) {
			updateTotali(results);
			updateTotaliVariazioni();
		}
	}

	@Override
	public Collection<QuotaAmmortamentoFiscale> refreshTableData() {
		return loadTableData();
	}

	/**
	 * @param beniAmmortizzabiliBD
	 *            the beniAmmortizzabiliBD to set
	 */
	public void setBeniAmmortizzabiliBD(IBeniAmmortizzabiliBD beniAmmortizzabiliBD) {
		this.beniAmmortizzabiliBD = beniAmmortizzabiliBD;
	}

	@Override
	public void setFormObject(Object object) {
		beneAmmortizzabile = (BeneAmmortizzabile) object;
	}

	/**
	 * Aggiorna le label dei totali delle quote.
	 *
	 * @param list
	 *            lista di quote
	 */
	protected void updateTotali(Collection<QuotaAmmortamentoFiscale> list) {

		calcolaTotaliQuote(list);

		Format impformat = new DecimalFormat("##0.00");
		Format percformat = new DecimalFormat("##0.0000");

		totaleImpOrdinarioLabel.setText(impformat.format(totaleImpOrdinario));
		totalePercOrdinarioLabel.setText(percformat.format(totalePercOrdinario) + " %");

		totaleImpAnticipatoJLabel.setText(impformat.format(totaleImpAnticipato));
		totalePercAnticipatoLabel.setText(percformat.format(totalePercAnticipato) + " %");

		totaleImpAcceleratoLabel.setText(impformat.format(totaleImpAccelerato));
		totalePercAcceleratoLabel.setText(percformat.format(totalePercAccelerato) + " %");
	}

	private void updateTotaliVariazioni() {

		totaleVariazioniBene = BigDecimal.ZERO;
		totaleVariazioniFondo = BigDecimal.ZERO;

		List<ValutazioneBene> valutazioni = beniAmmortizzabiliBD.caricaValutazioniBene(beneAmmortizzabile);

		for (ValutazioneBene valutazioneBene : valutazioni) {
			totaleVariazioniBene = totaleVariazioniBene.add(valutazioneBene.getImportoValutazioneBene());
			totaleVariazioniFondo = totaleVariazioniFondo.add(valutazioneBene.getImportoValutazioneFondo());
		}

		Format impformat = new DecimalFormat("##0.00");
		totaleVariazioniBeneLabel.setText(impformat.format(totaleVariazioniBene));
		totaleVariazioniFondoLabel.setText(impformat.format(totaleVariazioniFondo));
	}

}
