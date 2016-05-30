package it.eurotn.rich.report.editor.export;

import it.eurotn.rich.report.JasperPrintExporter;
import it.eurotn.rich.report.JecReport;

import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;

/**
 * Controlla il nome del report da stampare. Se il nome termina con "txt" il report verrà esportato come solo testo e
 * stampato sulla eventuale stampante testo configurata nelle impostazioni del client, altrimenti il report verrà
 * inviato direttamente alla stampante.
 *
 * @author fattazzo
 *
 */
public class PrintCommand extends ApplicationWindowAwareCommand {

    protected JecReport jecReport;

    /**
     * Costruttore.
     *
     * @param jecReport
     *            jecReport
     */
    public PrintCommand(final JecReport jecReport) {
        super("JecJRViewer.print");
        RcpSupport.configure(this);
        this.jecReport = jecReport;
    }

    @Override
    protected void doExecuteCommand() {
        JasperPrintExporter printExporter = new JasperPrintExporter();
        int key = (int) getParameter(MODIFIERS_PARAMETER_KEY);
        jecReport.setShowPrintDialog(true);
        printExporter.export(jecReport);
        // PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
        // /* Set the number of copies (default 1) */
        // int numeroCopiePerStampa = 1;
        // if (jecReport.getJrPrint().getProperty("numberOfCopies") != null) {
        // int numeroCopieParam = Integer.parseInt(jecReport.getJrPrint().getProperty("numberOfCopies"));
        // // solo se ho un valore positivo maggiore di 0 imposto il numero copie, altrimenti rimane impostato
        // // il valore di default a 1
        // if (numeroCopieParam > 0) {
        // numeroCopiePerStampa = numeroCopieParam;
        // }
        // }
        //
        // printRequestAttributeSet.add(new Copies(numeroCopiePerStampa));
        // printRequestAttributeSet.add(new JobName("Panjea", null));
        //
        // net.sf.jasperreports.engine.export.JRPrintServiceExporter exporter;
        // exporter = new net.sf.jasperreports.engine.export.JRPrintServiceExporter();
        // exporter.setParameter(JRExporterParameter.JASPER_PRINT, jecReport.getJrPrint());
        // exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, getPrintService());
        // exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet);
        // exporter.setParameter(JRPrintServiceExporterParameter.IGNORE_PAGE_MARGINS, Boolean.TRUE);
        // exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
        // exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, true);
        // try {
        // exporter.exportReport();
        // } catch (JRException e) {
        // e.printStackTrace();
        // }
        // }
        //
        // protected PrintService getPrintService() {
        // // imposto la stampante
        // PrintService printService = null;
        // if (jecReport.getLayoutStampa().getChiave() == null) {
        // // stampante di default
        // printService = PrintServiceLookup.lookupDefaultPrintService();
        // } else {
        //
        // // prendo la stampante dal layoutStampa impostato
        // String printerName = jecReport.getLayoutStampa().getStampante();
        //
        // // cerco la stampante con il nome impostato.
        // PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
        // DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
        // PrintService[] printServices;
        // try {
        // printServices = PrintServiceLookup.lookupPrintServices(flavor, pras);
        // for (PrintService printService2 : printServices) {
        // if (printService2.getName().equals(printerName)) {
        // printService = printService2;
        // break;
        // }
        // }
        // } catch (Exception e) {
        // MessageDialog dialog = new MessageDialog("ATTENZIONE", new DefaultMessage(
        // "Servizio di stampa non disponibile.", Severity.WARNING));
        // dialog.showDialog();
        // return null;
        // }
        //
        // // se non esiste una stampante per il layout la chiedo e la salvo per l'utente
        // if (printerName == null || printerName.isEmpty() || printService == null) {
        //
        // PanjeaTitledPageApplicationDialog dialog = new PanjeaTitledPageApplicationDialog(new LayoutStampaForm(
        // (LayoutStampa) PanjeaSwingUtil.cloneObject(jecReport.getLayoutStampa())), null) {
        //
        // @Override
        // protected void onCancel() {
        // super.onCancel();
        // PanjeaSwingUtil.checkAndThrowException(new StampanteNonAssociataException(
        // "Non verrà associata nessuna stampante per "
        // + jecReport.getLayoutStampa().getReportName()));
        // }
        //
        // @Override
        // protected boolean onFinish() {
        // LayoutStampa layoutStampa = (LayoutStampa) ((FormBackedDialogPage) getDialogPage())
        // .getBackingFormPage().getFormObject();
        // List<LayoutStampa> layouts = new ArrayList<LayoutStampa>();
        // layouts.add(layoutStampa);
        // ILayoutStampeManager layoutStampeManager = RcpSupport.getBean(LayoutStampeManager.BEAN_ID);
        // layoutStampeManager.salvaAssociazioneStampanti(layouts);
        //
        // jecReport.setLayoutStampa(layoutStampa);
        // PanjeaLifecycleApplicationEvent event = new PanjeaLifecycleApplicationEvent(
        // LifecycleApplicationEvent.MODIFIED, layoutStampa, this);
        // Application.instance().getApplicationContext().publishEvent(event);
        //
        // return true;
        // }
        // };
        // dialog.showDialog();
        //
        // printerName = jecReport.getLayoutStampa().getStampante();
        // }
        //
        // printServices = PrintServiceLookup.lookupPrintServices(flavor, pras);
        // for (PrintService printService2 : printServices) {
        // if (printService2.getName().equals(printerName)) {
        // printService = printService2;
        // break;
        // }
        // }
        // }
        // return printService;
    }

}