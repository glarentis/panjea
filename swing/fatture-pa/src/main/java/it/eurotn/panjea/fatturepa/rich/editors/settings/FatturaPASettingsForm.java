package it.eurotn.panjea.fatturepa.rich.editors.settings;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;

import org.apache.pdfbox.util.ExtensionFileFilter;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.list.BeanPropertyValueListRenderer;
import org.springframework.richclient.selection.dialog.ListSelectionDialog;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.fatturepa.domain.DatiMailInvioSdI;
import it.eurotn.panjea.fatturepa.domain.DatiMailRicezioneSdI;
import it.eurotn.panjea.fatturepa.domain.FatturaPASettings;
import it.eurotn.panjea.fatturepa.rich.bd.FatturePAAnagraficaBD;
import it.eurotn.panjea.fatturepa.rich.bd.IFatturePAAnagraficaBD;
import it.eurotn.panjea.protocolli.domain.Protocollo;
import it.eurotn.panjea.rich.bd.ISicurezzaBD;
import it.eurotn.panjea.rich.bd.SicurezzaBD;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.sicurezza.domain.DatiMail;
import it.eurotn.panjea.sicurezza.domain.Utente;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

/**
 * @author fattazzo
 *
 */
public class FatturaPASettingsForm extends PanjeaAbstractForm {

    private class CaricaAccountUtenteCommand extends ActionCommand {

        /**
         * Costruttore.
         */
        public CaricaAccountUtenteCommand() {
            super("caricaAccountUtenteCommand");
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {

            if (getFormModel().isReadOnly()) {
                return;
            }

            ISicurezzaBD sicurezzaBD = RcpSupport.getBean(SicurezzaBD.BEAN_ID);
            Utente utente = sicurezzaBD.caricaUtente(PanjeaSwingUtil.getUtenteCorrente().getUserName());

            List<DatiMail> datiMailUtente = sicurezzaBD.caricaDatiMail(utente.getId());
            List<DatiMail> datiMailPec = new ArrayList<DatiMail>();

            for (DatiMail datiMail : datiMailUtente) {
                if (datiMail.isPec()) {
                    datiMailPec.add(datiMail);
                }
            }

            if (datiMailPec.isEmpty()) {
                new MessageDialog("Attanzione", "Non esiste nessun acocunt PEC configurato per l'utente").showDialog();
                return;
            }

            ListSelectionDialog dialog = new ListSelectionDialog("Selezione account pec", null, datiMailPec) {
                @Override
                protected void onSelect(Object selection) {
                    if (selection != null) {
                        DatiMail datiMail = (DatiMail) selection;

                        getFormModel().getValueModel("datiMailInvioSdI").setValue(new DatiMailInvioSdI(datiMail));
                        getFormModel().getValueModel("datiMailRicezioneSdI")
                                .setValue(new DatiMailRicezioneSdI(datiMail));
                    }
                }
            };
            dialog.setRenderer(new BeanPropertyValueListRenderer("nomeAccount"));
            dialog.showDialog();
        }

    }

    private class CheckMailTestCommand extends ActionCommand {

        /**
         * Costruttore.
         */
        public CheckMailTestCommand() {
            super("checkMailTestCommand");
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            testMailEditorPane.setText("");

            testMailEditorPane.setText(fatturePAAnagraficaBD.checkMailForTest());
        }

    }

    private class ConservazioneSostitutivaListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {

            boolean attivaConservazioneSostitutiva = (boolean) getValueModel("attivaConservazioneSostitutiva")
                    .getValue();

            conservazioneSostIndirizzoComponent.setEnabled(attivaConservazioneSostitutiva);
            conservazioneSostUtenteComponent.setEnabled(attivaConservazioneSostitutiva);
            conservazioneSostPasswordComponent.setEnabled(attivaConservazioneSostitutiva);
        }
    }

    private class GestioneFirmaListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {

            boolean gestioneFirmaElettronica = (boolean) getValueModel("gestioneFirmaElettronica").getValue();

            softwareFirmaComponent.setEnabled(gestioneFirmaElettronica);
            eseguibileSoftwareComponent.setEnabled(gestioneFirmaElettronica);
            selezionaEseguibileSoftwareCommand.setEnabled(gestioneFirmaElettronica);

            checkMailTestCommand.setEnabled(getFormModel().isReadOnly());
            testMailEditorPane.setText("<html>&nbsp;</html>");
            testMailEditorPane.setEnabled(getFormModel().isReadOnly());
        }
    }

    private class SelezionaEseguibileSoftwareCommand extends ActionCommand {

        /**
         * Costruttore.
         */
        public SelezionaEseguibileSoftwareCommand() {
            super("selezionaEseguibileSoftwareCommand");
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {

            if (getFormModel().isReadOnly()) {
                return;
            }

            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            ExtensionFileFilter eseguibileFileFilter = new ExtensionFileFilter(new String[] { "exe", "sh" },
                    "File eseguibile");
            fc.addChoosableFileFilter(eseguibileFileFilter);
            fc.setAcceptAllFileFilterUsed(false);
            fc.setFileFilter(eseguibileFileFilter);
            int returnVal = fc.showOpenDialog(FatturaPASettingsForm.this.getControl());

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();

                getFormModel().getValueModel("softwareFirmaPath").setValue(file.getPath());
            }

        }

    }

    public static final String FORM_ID = "fatturaPASettingsForm";

    private SelezionaEseguibileSoftwareCommand selezionaEseguibileSoftwareCommand;
    private CaricaAccountUtenteCommand caricaAccountUtenteCommand;

    private List<String> listProtocolliDescrizioneEstesa = null;

    private JComponent softwareFirmaComponent;
    private JComponent eseguibileSoftwareComponent;

    private GestioneFirmaListener gestioneFirmaListener;
    private ConservazioneSostitutivaListener conservazioneSostitutivaListener;

    private JComponent conservazioneSostIndirizzoComponent;
    private JComponent conservazioneSostUtenteComponent;
    private JComponent conservazioneSostPasswordComponent;

    private JEditorPane testMailEditorPane;

    private IFatturePAAnagraficaBD fatturePAAnagraficaBD;

    private CheckMailTestCommand checkMailTestCommand;

    /**
     * Costruttore.
     */
    public FatturaPASettingsForm() {
        super(PanjeaFormModelHelper.createFormModel(new FatturaPASettings(), false, FORM_ID), FORM_ID);

        this.fatturePAAnagraficaBD = RcpSupport.getBean(FatturePAAnagraficaBD.BEAN_ID);

        selezionaEseguibileSoftwareCommand = new SelezionaEseguibileSoftwareCommand();
        caricaAccountUtenteCommand = new CaricaAccountUtenteCommand();

        gestioneFirmaListener = new GestioneFirmaListener();
        conservazioneSostitutivaListener = new ConservazioneSostitutivaListener();
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout("right:pref,4dlu,fill:80dlu,10dlu,right:pref,4dlu,left:100dlu",
                "4dlu,default,4dlu,default,4dlu,default,4dlu,default,4dlu,default,4dlu,default,4dlu,default,4dlu,default,4dlu,default,4dlu,default,4dlu,default,4dlu,default,4dlu,default,4dlu,default,4dlu,default,4dlu,default,4dlu,default,4dlu,fill:50dlu,4dlu,default,4dlu,default,4dlu,default,4dlu,default,4dlu,default");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
        builder.setLabelAttributes("r, c");
        builder.setRow(2);

        builder.addLabel("registroProtocollo", 1);
        builder.addBinding(bf.createBoundSearchText("registroProtocollo", null, Protocollo.class), 3);
        builder.nextRow();

        builder.addPropertyAndLabel("gestioneFirmaElettronica");
        builder.nextRow();

        softwareFirmaComponent = builder.addPropertyAndLabel("softwareFirmaElettronica")[1];
        builder.nextRow();

        eseguibileSoftwareComponent = builder.addPropertyAndLabel("softwareFirmaPath", 1, 8, 4)[1];
        builder.addComponent(selezionaEseguibileSoftwareCommand.createButton(), 7, 8);
        builder.nextRow();

        builder.addPropertyAndLabel("formatoTrasmissione");
        builder.nextRow();

        builder.addPropertyAndLabel("emailSpedizioneSdI");
        builder.nextRow();

        builder.addHorizontalSeparator("Dati per l'invio delle fatture al Sistema di Interscambio", 5);
        builder.addComponent(caricaAccountUtenteCommand.createButton(), 7);
        builder.nextRow();

        builder.addPropertyAndLabel("datiMailInvioSdI.server", 1);
        builder.addPropertyAndLabel("datiMailInvioSdI.email", 5);
        builder.nextRow();

        builder.addPropertyAndLabel("datiMailInvioSdI.emailDiRisposta", 1);
        builder.addPropertyAndLabel("datiMailInvioSdI.auth", 5);
        builder.nextRow();

        builder.addPropertyAndLabel("datiMailInvioSdI.utenteMail", 1);
        builder.addPasswordFieldAndLabel("datiMailInvioSdI.passwordMail", 5);
        builder.nextRow();

        builder.addPropertyAndLabel("datiMailInvioSdI.tipoConnessione", 1);
        builder.addPropertyAndLabel("datiMailInvioSdI.port", 5);
        builder.nextRow();

        builder.addHorizontalSeparator("Dati per il controllo degli esiti inviati dal Sistema di Interscambio", 7);
        builder.nextRow();

        builder.addPropertyAndLabel("controlloNotificheSdiAbilitato", 1);
        builder.nextRow();

        builder.addPropertyAndLabel("datiMailRicezioneSdI.server", 1);
        builder.addPropertyAndLabel("datiMailRicezioneSdI.email", 5);
        builder.nextRow();

        builder.addPropertyAndLabel("datiMailRicezioneSdI.auth", 1);
        builder.nextRow();

        builder.addPropertyAndLabel("datiMailRicezioneSdI.utenteMail", 1);
        builder.addPasswordFieldAndLabel("datiMailRicezioneSdI.passwordMail", 5);
        builder.nextRow();

        builder.addPropertyAndLabel("numeroGiorniControlloNotifiche", 1);
        checkMailTestCommand = new CheckMailTestCommand();
        builder.addComponent(checkMailTestCommand.createButton(), 7);
        builder.nextRow();

        testMailEditorPane = new JEditorPane();
        testMailEditorPane.setEditable(false);
        testMailEditorPane.setContentType("text/html");
        builder.addComponent(new JScrollPane(testMailEditorPane), 1, 36, 7, 1);
        builder.nextRow();

        builder.addHorizontalSeparator("Dati per i servizi delle fatture PA ( invio/conservazione sostitutiva )", 7);
        builder.nextRow();

        builder.addPropertyAndLabel("attivaConservazioneSostitutiva", 1);
        builder.nextRow();

        conservazioneSostIndirizzoComponent = builder.addPropertyAndLabel("datiConservazioneSostitutiva.indirizzoWeb",
                1, 42, 5, 1)[1];
        builder.nextRow();

        conservazioneSostUtenteComponent = builder.addPropertyAndLabel("datiConservazioneSostitutiva.utente", 1)[1];
        conservazioneSostPasswordComponent = builder.addPasswordFieldAndLabel("datiConservazioneSostitutiva.password",
                5)[1];

        getValueModel("gestioneFirmaElettronica").addValueChangeListener(gestioneFirmaListener);
        getFormModel().addPropertyChangeListener(FormModel.READONLY_PROPERTY, gestioneFirmaListener);

        getValueModel("attivaConservazioneSostitutiva").addValueChangeListener(conservazioneSostitutivaListener);
        getFormModel().addPropertyChangeListener(FormModel.READONLY_PROPERTY, conservazioneSostitutivaListener);

        return builder.getPanel();
    }

    @Override
    public void dispose() {
        selezionaEseguibileSoftwareCommand = null;

        getValueModel("gestioneFirmaElettronica").removeValueChangeListener(gestioneFirmaListener);
        getFormModel().removePropertyChangeListener(FormModel.READONLY_PROPERTY, gestioneFirmaListener);
        gestioneFirmaListener = null;

        getValueModel("attivaConservazioneSostitutiva").removeValueChangeListener(conservazioneSostitutivaListener);
        getFormModel().removePropertyChangeListener(FormModel.READONLY_PROPERTY, conservazioneSostitutivaListener);
        conservazioneSostitutivaListener = null;

        super.dispose();
    }

    /**
     * Sovrascrivo il setFormObject per risolvere il problema sulla comboBox dei protocolli.<br>
     * Viene mostrato codice + descrizione e questo genera una differenza di value tra l'oggetto che viene settato nel
     * form (String codice protocollo) e il valore selezionato della combo (String codice+descrizione) che genera un
     * valueChanged che mostra la popup della combobox sul salvataggio.<br>
     * Aggiorno il formObject impostando come protocollo il protocollo che uso in visualizzazione prima dell'esecuzione
     * della setFormObject per evitare di lanciare il valueChanged.
     *
     * @param formObject
     *            form object
     */
    @Override
    public void setFormObject(Object formObject) {
        if (listProtocolliDescrizioneEstesa != null) {

            String codiceProtocollo = ((FatturaPASettings) formObject).getRegistroProtocollo();
            if (codiceProtocollo != null) {
                for (String protEsteso : listProtocolliDescrizioneEstesa) {
                    if (protEsteso.startsWith(codiceProtocollo)) {
                        ((FatturaPASettings) formObject).setRegistroProtocollo(protEsteso);
                        break;
                    }
                }
            }

        }
        super.setFormObject(formObject);
    }
}
