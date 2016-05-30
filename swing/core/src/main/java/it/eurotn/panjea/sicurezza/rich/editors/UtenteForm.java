/**
 *
 */
package it.eurotn.panjea.sicurezza.rich.editors;

import it.eurotn.panjea.rich.bd.ISicurezzaBD;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.sicurezza.domain.Ruolo;
import it.eurotn.panjea.sicurezza.rich.pm.UtentePM;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Leonardo
 */
public class UtenteForm extends PanjeaAbstractForm {

    private class DigitOnlyDocumentFilter extends DocumentFilter {
        @Override
        public void insertString(DocumentFilter.FilterBypass fp, int offset, String string, AttributeSet aset)
                throws BadLocationException {
            int len = string.length();
            boolean isValidInteger = true;

            for (int i = 0; i < len; i++) {
                if (!Character.isDigit(string.charAt(i))) {
                    isValidInteger = false;
                    break;
                }
            }
            if (isValidInteger) {
                super.insertString(fp, offset, string, aset);
            }
        }

        @Override
        public void replace(DocumentFilter.FilterBypass fp, int offset, int length, String string, AttributeSet aset)
                throws BadLocationException {
            int len = string.length();
            boolean isValidInteger = true;

            for (int i = 0; i < len; i++) {
                if (!Character.isDigit(string.charAt(i))) {
                    isValidInteger = false;
                    break;
                }
            }
            if (isValidInteger) {
                super.replace(fp, offset, length, string, aset);
            }

        }
    }

    private static final String ID_FORM = "utenteForm";
    private ISicurezzaBD sicurezzaBD = null;
    private List<Ruolo> ruoliSelectable = null;

    /**
     * Costruttore di default.
     */
    public UtenteForm() {
        super(PanjeaFormModelHelper.createFormModel(new UtentePM(), false, ID_FORM), ID_FORM);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout(
                "right:pref,4dlu,left:120dlu, 10dlu, right:pref,4dlu,left:120dlu, 10dlu, right:pref,4dlu,left:30dlu,10dlu,right:pref,4dlu,left:pref",
                "2dlu,default");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel()
        builder.setLabelAttributes("r, c");

        builder.nextRow();
        builder.setRow(2);
        ((JTextField) builder.addPropertyAndLabel("utente.userName", 1)[1]).setColumns(20);
        ((JTextField) builder.addPropertyAndLabel("utente.descrizione", 5)[1]).setColumns(30);
        builder.nextRow();

        ((JTextField) builder.addPasswordFieldAndLabel("utente.password", 1)[1]).setColumns(20);
        ((JTextField) builder.addPasswordFieldAndLabel("confermaPassword", 5)[1]).setColumns(20);
        builder.nextRow();

        ((JTextField) builder.addPropertyAndLabel("utente.nome", 1)[1]).setColumns(20);
        ((JTextField) builder.addPropertyAndLabel("utente.cognome", 5)[1]).setColumns(20);
        builder.nextRow();

        ((JTextField) builder.addPropertyAndLabel("utente.cellulare")[1]).setColumns(20);
        JPasswordField passwordField = (JPasswordField) builder.addPasswordFieldAndLabel("utente.passwordPos", 5)[1];
        passwordField.setColumns(20);
        ((AbstractDocument) passwordField.getDocument()).setDocumentFilter(new DigitOnlyDocumentFilter());
        builder.nextRow();

        builder.addPropertyAndLabel("utente.abilitato");
        builder.nextRow();

        // ------------------------------------- MANTIS
        // -----------------------------------
        builder.addHorizontalSeparator("Accesso alla gestione versioni", 15);
        builder.nextRow();

        ((JTextField) builder.addPropertyAndLabel("utente.datiBugTracker.username", 1)[1]).setColumns(20);
        builder.nextRow();
        ((JTextField) builder.addPasswordFieldAndLabel("utente.datiBugTracker.password", 1)[1]).setColumns(20);
        ((JTextField) builder.addPasswordFieldAndLabel("confermaPasswordBugTracker", 5)[1]).setColumns(20);
        builder.nextRow();

        // ------------------------------------- JASPERSERVER
        // -----------------------------------
        builder.addHorizontalSeparator("Accesso al server di analisi", 15);
        builder.nextRow();

        ((JTextField) builder.addPropertyAndLabel("utente.datiJasperServer.username", 1)[1]).setColumns(20);
        builder.nextRow();
        ((JTextField) builder.addPasswordFieldAndLabel("utente.datiJasperServer.password", 1)[1]).setColumns(20);
        ((JTextField) builder.addPasswordFieldAndLabel("confermaPasswordJasperServer", 5)[1]).setColumns(20);
        builder.nextRow();

        // ------------------------------------- ROLES
        // -----------------------------------
        builder.addHorizontalSeparator("Ruoli", 15);
        builder.nextRow();

        builder.addLabel("ruoli");
        builder.addBinding(bf.createBoundShuttleList("ruoli", getRuoliSelectable(), "codice"), 3, 26, 5, 1);
        builder.nextRow();

        return builder.getPanel();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    protected String getCommitCommandFaceDescriptorId() {
        return this.getId() + ".save";
    }

    @Override
    protected String getNewFormObjectCommandId() {
        return this.getId() + ".new";
    }

    @Override
    protected String getRevertCommandFaceDescriptorId() {
        return this.getId() + ".revert";
    }

    /**
     * @return lista dei ruoli disponibili
     */
    public List<Ruolo> getRuoliSelectable() {
        if (ruoliSelectable == null) {
            ruoliSelectable = sicurezzaBD.caricaRuoliAziendaCorrente();
        }
        return ruoliSelectable;
    }

    /**
     * @param sicurezzaBD
     *            the sicurezzaBD to set
     */
    public void setSicurezzaBD(ISicurezzaBD sicurezzaBD) {
        this.sicurezzaBD = sicurezzaBD;
    }
}
