package it.eurotn.panjea.pagamenti.rich.tabelle;

import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.rate.domain.CalendarioRate;
import it.eurotn.panjea.rate.domain.Rata;
import it.eurotn.panjea.rate.rich.bd.IRateBD;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.rich.forms.PanjeaFormGuard;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

import javax.swing.JComponent;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;

public class SimulazioneRateTablePage extends AbstractTablePageEditor<Rata> {

	/**
	 * Genera delle rate dal codice pagamento corrente.
	 */
	private class GeneraRateDaPagamentoCommand extends ActionCommand {

		public static final String COMMAND_ID = "generaRateDaPagamentoCommand";

		/**
		 * Costruttore di default.
		 */
		public GeneraRateDaPagamentoCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			ParametriSimulazioneRatePM parametri = (ParametriSimulazioneRatePM) parametriForm.getFormObject();
			parametriForm.commit();

			CalendarioRate calendarioRate = null;
			if (parametri.getCalendarioRate() != null && parametri.getCalendarioRate().getId() != null) {
				calendarioRate = parametri.getCalendarioRate();
			}

			SimulazioneRateTablePage.this.setRows(rateBD.generaRate(codicePagamento, parametri.getData(),
					parametri.getImponibile(), parametri.getImposta(), calendarioRate));
		}

	}

	public class ParametriSimulazioneRatePM {
		private Date data;
		private BigDecimal imponibile;
		private BigDecimal imposta;
		private CalendarioRate calendarioRate;

		/**
		 * Costruttore.
		 * 
		 */
		public ParametriSimulazioneRatePM() {
			super();
			this.calendarioRate = new CalendarioRate();
		}

		/**
		 * @return the calendarioRate
		 */
		public CalendarioRate getCalendarioRate() {
			return calendarioRate;
		}

		/**
		 * @return Returns the data.
		 */
		public Date getData() {
			return data;
		}

		/**
		 * @return Returns the imponibile.
		 */
		public BigDecimal getImponibile() {
			return imponibile;
		}

		/**
		 * @return Returns the imposta.
		 */
		public BigDecimal getImposta() {
			return imposta;
		}

		/**
		 * @param calendarioRate
		 *            the calendarioRate to set
		 */
		public void setCalendarioRate(CalendarioRate calendarioRate) {
			this.calendarioRate = calendarioRate;
		}

		/**
		 * @param data
		 *            The data to set.
		 */
		public void setData(Date data) {
			this.data = data;
		}

		/**
		 * @param imponibile
		 *            The imponibile to set.
		 */
		public void setImponibile(BigDecimal imponibile) {
			this.imponibile = imponibile;
		}

		/**
		 * @param imposta
		 *            The imposta to set.
		 */
		public void setImposta(BigDecimal imposta) {
			this.imposta = imposta;
		}
	}

	private class SimulazioneRateParametriForm extends PanjeaAbstractForm {
		/**
		 * Costruttore.
		 */
		public SimulazioneRateParametriForm() {
			super(PanjeaFormModelHelper.createFormModel(new ParametriSimulazioneRatePM(), false,
					"simulazioneRateParametriForm"));
		}

		@Override
		protected JComponent createFormControl() {
			GeneraRateDaPagamentoCommand generaRateCommand = new GeneraRateDaPagamentoCommand();
			new PanjeaFormGuard(getFormModel(), generaRateCommand);
			final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
			FormLayout layout = new FormLayout(
					"right:pref,4dlu,50dlu, 10dlu, right:pref,4dlu,50dlu, 10dlu, right:pref", "default,4dlu,default");
			FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel());
			builder.setLabelAttributes("r, c");
			builder.addPropertyAndLabel("data", 1, 1);
			Binding bindingCalendario = bf.createBoundSearchText("calendarioRate", new String[] { "descrizione" });
			builder.addLabel("calendarioRate", 5, 1);
			builder.addBinding(bindingCalendario, 7, 1, 3, 1);
			builder.addPropertyAndLabel("imponibile", 1, 3);
			builder.addPropertyAndLabel("imposta", 5, 3);
			builder.addComponent(generaRateCommand.createButton(), 9, 3);
			return builder.getPanel();
		}
	}

	private final SimulazioneRateParametriForm parametriForm;
	private final IRateBD rateBD;
	private final CodicePagamento codicePagamento;

	/**
	 * Costruttore.
	 * 
	 * @param rateBD
	 *            bd per las gestione rate.
	 * @param codicePagamento
	 *            codicePagamento per la simulazione
	 */
	public SimulazioneRateTablePage(final IRateBD rateBD, final CodicePagamento codicePagamento) {
		super("simulazioneRateTablePage", new String[] { "numeroRata", "dataScadenza", "importo" }, Rata.class);
		this.rateBD = rateBD;
		this.codicePagamento = codicePagamento;
		parametriForm = new SimulazioneRateParametriForm();
	}

	@Override
	public void dispose() {
		parametriForm.dispose();
		super.dispose();
	}

	@Override
	public JComponent getHeaderControl() {
		return parametriForm.getControl();
	}

	@Override
	public Collection<Rata> loadTableData() {
		return null;
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public Collection<Rata> refreshTableData() {
		return null;
	}

	@Override
	public void setFormObject(Object object) {
	}
}
