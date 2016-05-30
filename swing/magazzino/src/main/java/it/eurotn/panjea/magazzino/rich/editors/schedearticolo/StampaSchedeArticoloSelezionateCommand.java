package it.eurotn.panjea.magazzino.rich.editors.schedearticolo;

import foxtrot.AsyncTask;
import foxtrot.AsyncWorker;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoSchedeArticoloBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoSchedeArticoloBD;
import it.eurotn.panjea.magazzino.util.ArticoloRicerca;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.ServiceUI;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;

public class StampaSchedeArticoloSelezionateCommand extends ApplicationWindowAwareCommand {

	private class ArticoloRicercaStampaComparator implements Comparator<ArticoloRicerca> {

		@Override
		public int compare(ArticoloRicerca o1, ArticoloRicerca o2) {
			return o1.getCodice().compareTo(o2.getCodice());
		}

	}

	public static final String COMMAND_ID = "stampaSchedeArticoloSelezionateCommand";

	public static final String PARAM_ARTICOLI = "paramArticoli";
	public static final String PARAM_ANNO = "paramAnno";
	public static final String PARAM_MESE = "paramMese";

	private IMagazzinoSchedeArticoloBD magazzinoSchedeArticoloBD;
	private ArticoloRicercaStampaComparator articoloRicercaStampaComparator;

	{
		articoloRicercaStampaComparator = new ArticoloRicercaStampaComparator();
	}

	/**
	 * Costruttore.
	 */
	public StampaSchedeArticoloSelezionateCommand() {
		super(COMMAND_ID);
		RcpSupport.configure(this);
		this.magazzinoSchedeArticoloBD = RcpSupport.getBean(MagazzinoSchedeArticoloBD.BEAN_ID);
	}

	@Override
	protected void doExecuteCommand() {

		@SuppressWarnings("unchecked")
		final List<ArticoloRicerca> articoli = (List<ArticoloRicerca>) getParameter(PARAM_ARTICOLI,
				new ArrayList<ArticoloRicerca>());
		Collections.sort(articoli, articoloRicercaStampaComparator);
		final Integer anno = (Integer) getParameter(PARAM_ANNO, null);
		final Integer mese = (Integer) getParameter(PARAM_MESE, null);

		if (articoli.isEmpty() || anno == null || mese == null) {
			return;
		}

		final PrintService printService = getPrintService();

		AsyncWorker.post(new AsyncTask() {

			@Override
			public void failure(Throwable arg0) {
				PanjeaSwingUtil.checkAndThrowException(arg0);
			}

			@Override
			public Object run() throws Exception {
				for (ArticoloRicerca articoloRicerca : articoli) {
					byte[] bytes = magazzinoSchedeArticoloBD.caricaFileSchedaArticolo(anno, mese,
							articoloRicerca.getId());
					if (bytes != null) {
						printFile(bytes, printService);
					}
				}
				return null;
			}

			@Override
			public void success(Object arg0) {
			}
		});
	}

	/**
	 * Restituisce il print service da utilizzare facendolo selezionar eall'utente. Viene proposto quello di default.
	 * 
	 * @return {@link PrintService}
	 */
	private PrintService getPrintService() {
		PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
		DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE; // MY FILE IS .txt TYPE
		PrintService[] printService = PrintServiceLookup.lookupPrintServices(flavor, pras);

		PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();

		PrintService service = ServiceUI.printDialog(null, 200, 200, printService, defaultService, flavor, pras);

		return service;
	}

	/**
	 * Stampa il file.
	 * 
	 * @param bytes
	 *            file in formato byte[]
	 * @param printService
	 *            print service
	 */
	private void printFile(byte[] bytes, PrintService printService) {

		if (printService != null) {

			File fileTmp = null;
			try {
				fileTmp = File.createTempFile("schedaArticolo", "pdf");
				FileUtils.writeByteArrayToFile(fileTmp, bytes);
			} catch (IOException e1) {
				logger.error("--> errore durante la creazione del file", e1);
				throw new RuntimeException("--> errore durante la creazione del file", e1);
			}

			FileInputStream fis = null;
			try {
				fis = new FileInputStream(fileTmp);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			try {
				PDDocument document = PDDocument.load(fis);
				PrinterJob printJob = PrinterJob.getPrinterJob();
				printJob.setJobName("schedaArticolo" + Calendar.getInstance().getTimeInMillis() + ".pdf");
				printJob.setPrintService(printService);
				document.silentPrint(printJob);
				fileTmp.delete();
			} catch (Exception e) {
				logger.error("--> Errore durante la stampa.", e);
			}
		}
	}
}