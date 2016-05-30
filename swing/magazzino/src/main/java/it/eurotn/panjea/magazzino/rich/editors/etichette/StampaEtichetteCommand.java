package it.eurotn.panjea.magazzino.rich.editors.etichette;

import foxtrot.AsyncTask;
import foxtrot.AsyncWorker;
import it.eurotn.panjea.magazzino.domain.etichetta.EtichettaArticolo;
import it.eurotn.panjea.magazzino.domain.etichetta.LayoutStampaEtichette;
import it.eurotn.panjea.magazzino.domain.etichetta.ParametriStampaEtichetteArticolo;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.control.table.JideTableWidget;
import it.eurotn.rich.dialog.MessageAlert;
import it.eurotn.rich.report.ReportManager;
import it.eurotn.rich.report.exception.ReportNonTrovatoException;
import it.eurotn.rich.report.jasperserver.ReportEmptyDataException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.JobName;
import javax.swing.SwingUtilities;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;

import org.springframework.binding.value.ValueModel;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Message;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.util.RcpSupport;

/**
 * Stampa le etichette per gli articoli passati come parametri .
 * 
 * @author giangi
 */
public class StampaEtichetteCommand extends ActionCommand {

	private class StampaEtichetteExceptionManager implements Runnable {

		private EtichettaArticolo etichettaArticolo;
		private Exception exception;

		public static final String STAMPA_ETICHETTE_EXCEPTION_MANAGER = "StampaEtichetteExceptionManager.";

		public static final String BARCODE_LENGTH_ERROR = "barcodeLengthError";
		public static final String BARCODE_INVALID_CHARACTER_ERROR = "barcodeInvalidCharacterError";
		public static final String BARCODE_CHECKSUM_ERROR = "barcodeChecksumError";
		public static final String NO_ERROR = "noError";

		private Map<String, String> erroriBarCode;

		{
			erroriBarCode = new HashMap<String, String>();
			erroriBarCode.put(BARCODE_LENGTH_ERROR, "Message must be 11 or 12 characters long.");
			erroriBarCode.put(BARCODE_INVALID_CHARACTER_ERROR, "Invalid characters found. Valid are 0-9 only.");
			erroriBarCode.put(BARCODE_CHECKSUM_ERROR, "Checksum is bad");
		}

		/**
		 * Costruttore.
		 * 
		 * @param etichettaArticolo
		 *            etichetta articolo di rifermineto
		 * @throws Exception
		 *             rilanciata se non l'errore non dipende dal barcode dell'articolo
		 * 
		 */
		public StampaEtichetteExceptionManager(final EtichettaArticolo etichettaArticolo) throws Exception {
			this(etichettaArticolo, null);
		}

		/**
		 * Costruttore.
		 * 
		 * @param etichettaArticolo
		 *            etichetta articolo di rifermineto
		 * @param exception
		 *            eccezione da esaminare
		 * @throws Exception
		 *             rilanciata se non l'errore non dipende dal barcode dell'articolo
		 * 
		 */
		public StampaEtichetteExceptionManager(final EtichettaArticolo etichettaArticolo, final Exception exception)
				throws Exception {
			super();
			this.etichettaArticolo = etichettaArticolo;
			this.exception = exception;

			String errore = getMessageCodeError();

			if (errore == null) {
				throw exception;
			} else {
				this.etichettaArticolo.setEsitoStampa(RcpSupport
						.getMessage(STAMPA_ETICHETTE_EXCEPTION_MANAGER + errore));
			}

			SwingUtilities.invokeAndWait(this);
		}

		/**
		 * Restituisce se esiste l'eventuale errore sul barcode dell'articolo analizzando l'eccezione.
		 * 
		 * @return errore. <code>null</code> se non esiste
		 */
		private String getBarCodeMessageCodeError() {
			String errore = null;

			for (Entry<String, String> entry : erroriBarCode.entrySet()) {
				if (exception.getMessage() != null && exception.getMessage().contains(entry.getValue())) {
					errore = entry.getKey();
					break;
				}
			}

			return errore;
		}

		/**
		 * Restituisce il codice dell'errore della stampa dell'etichetta.
		 * 
		 * @return errore
		 */
		private String getMessageCodeError() {

			String errore = null;

			if (errore == null) {
				// se non è arrivata nessuna eccezione la stampa è ok
				if (this.exception == null) {
					errore = NO_ERROR;
				} else {
					// controllo eventuali errori sul barcode
					errore = getBarCodeMessageCodeError();
				}
			}
			return errore;
		}

		@Override
		public void run() {
			table.replaceOrAddRowObject(etichettaArticolo, etichettaArticolo, null);
		}

	}

	private ValueModel layoutEtichettaValueHolder;
	private static final String COMMAND_ID = "printCommand";
	private String reportPath;

	public static final String PARAM_TABLE_ETICHETTE_ARTICOLO = "param_table_etichette_articolo";
	public static final String PARAM_PARAMETRI_ETICHETTE_ARTICOLO = "param_parametri_etichette_articolo";
	public static final String PARAM_PRINTER_NAME = "param_stampante_etichette_articolo";
	private ReportManager reportManager;

	private List<EtichettaArticolo> etichetteArticoloStampate;

	private JideTableWidget<EtichettaArticolo> table;

	/**
	 * Costruttore.
	 * 
	 * @param layoutEtichettaValueHolder
	 *            value holder contenente il nome del report da eseguire
	 * @param reportManager
	 *            reportManager
	 */
	public StampaEtichetteCommand(final ValueModel layoutEtichettaValueHolder, final ReportManager reportManager) {
		super(COMMAND_ID, COMMAND_ID + "Controller");
		CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
				CommandConfigurer.class);
		c.configure(this);
		this.reportManager = reportManager;
		this.layoutEtichettaValueHolder = layoutEtichettaValueHolder;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doExecuteCommand() {
		table = (JideTableWidget<EtichettaArticolo>) getParameter(PARAM_TABLE_ETICHETTE_ARTICOLO);
		final String stampanteEtichette = (String) getParameter(PARAM_PRINTER_NAME, null);
		final ParametriStampaEtichetteArticolo parametriStampaEtichetteArticolo = (ParametriStampaEtichetteArticolo) getParameter(
				PARAM_PARAMETRI_ETICHETTE_ARTICOLO, new ParametriStampaEtichetteArticolo());
		reportPath = parametriStampaEtichetteArticolo.getReportPath();

		org.springframework.util.Assert.notNull(layoutEtichettaValueHolder.getValue() != null,
				"layout dell'etichetta non selezionato");
		org.springframework.util.Assert.notNull(stampanteEtichette, "stampante non selezionata");

		LayoutStampaEtichette layoutEtichetta = (LayoutStampaEtichette) layoutEtichettaValueHolder.getValue();
		final String reportName = reportPath + "/" + layoutEtichetta.getReportName();

		// sincornizzo il set perchè lo uso nel thread di foxTrot
		final List<EtichettaArticolo> etichette = Collections.synchronizedList(table.getVisibleObjects());
		Message message = new DefaultMessage("Stampa del documento \n" + reportName + "\n in corso...", Severity.INFO);
		final MessageAlert messageAlert = new MessageAlert(message, 0);
		messageAlert.showAlert();
		AsyncWorker.post(new AsyncTask() {

			@Override
			public void failure(Throwable error) {
				Message message = null;

				if (error instanceof ReportNonTrovatoException) {
					ReportNonTrovatoException exception = (ReportNonTrovatoException) error;
					message = new DefaultMessage("Impossibile trovare il report con il path " + exception.getMessage(),
							Severity.ERROR);
				} else {
					message = new DefaultMessage("Stampa del documento " + reportName + " non riuscita \n "
							+ error.getMessage(), Severity.ERROR);
					// stampo l'errore del defautl error per visualizzarlo al "volo"
					logger.error("-->errore nel stamparele etichette ", error);
				}
				new MessageAlert(message).showAlert();
			}

			@Override
			protected void finish() {
				messageAlert.closeAlert();
				super.finish();
			}

			private void printReport(JasperPrint jasperPrint, EtichettaArticolo etichettaArticolo) {
				// mando in stampa
				PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
				/* Set the number of copies */
				printRequestAttributeSet.add(new Copies(etichettaArticolo.getNumeroCopiePerStampa()));
				printRequestAttributeSet.add(new JobName("Panjea - Stampa etichette "
						+ etichettaArticolo.getDescrizione(), null));
				net.sf.jasperreports.engine.export.JRPrintServiceExporter exporter;
				exporter = new net.sf.jasperreports.engine.export.JRPrintServiceExporter();
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				// imposto la stampante
				PrintService printService = null;
				// cerco la stampante con il nome impostato.
				PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
				DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
				PrintService[] printServices = PrintServiceLookup.lookupPrintServices(flavor, pras);
				for (PrintService printService2 : printServices) {
					if (stampanteEtichette.equals(printService2.getName())) {
						printService = printService2;
						break;
					}
				}
				exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, printService);
				exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET,
						printRequestAttributeSet);
				exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
				exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);
				try {
					exporter.exportReport();
				} catch (Exception e) {
					if (e instanceof IllegalArgumentException) {
						throw new ReportEmptyDataException("Nessun dato caricato");
					}
					throw new RuntimeException(e);
				}
			}

			@Override
			public Object run() throws Exception {
				etichetteArticoloStampate = new ArrayList<EtichettaArticolo>();

				for (final EtichettaArticolo etichettaArticolo : etichette) {
					try {
						Map<Object, Object> parametri = new HashMap<Object, Object>();
						parametri.put("idArticolo", etichettaArticolo.getArticolo().getId());

						parametri.put("queryArticolo", ArticoloEtichettaQueryBuilder.getSql(etichettaArticolo
								.getArticolo().getId(), parametriStampaEtichetteArticolo.getEntita()));
						parametri.put("prezzoNetto", etichettaArticolo.getPrezzoNetto());
						parametri.put("azienda", PanjeaSwingUtil.getUtenteCorrente().getCodiceAzienda());

						if (parametriStampaEtichetteArticolo.isGestioneLotti()) {
							if (etichettaArticolo.getDataDocumento() != null) {
								parametri.put("dataDocumento",
										new SimpleDateFormat("dd/MM/yyyy").format(etichettaArticolo.getDataDocumento()));
							}
							parametri.put("lotto", etichettaArticolo.getLotto().getCodice());
							parametri.put("quantita", etichettaArticolo.getQuantita());
							if (etichettaArticolo.getSedeEntita() != null) {
								parametri.put("idSede", etichettaArticolo.getSedeEntita().getId());
							}
							parametri.put("descrizione", etichettaArticolo.getDescrizione());
						}

						JasperPrint jasperReportEtchetta = reportManager.runReport(reportName, parametri);
						printReport(jasperReportEtchetta, etichettaArticolo);

						new StampaEtichetteExceptionManager(etichettaArticolo);

					} catch (Exception e) {
						new StampaEtichetteExceptionManager(etichettaArticolo, e);
					}
				}
				return true;
			}

			@Override
			public void success(Object arg0) {
			}
		});
	}

	/**
	 * @return the etichetteArticoloStampate
	 */
	public List<EtichettaArticolo> getEtichetteArticoloStampate() {
		return etichetteArticoloStampate;
	}
}
