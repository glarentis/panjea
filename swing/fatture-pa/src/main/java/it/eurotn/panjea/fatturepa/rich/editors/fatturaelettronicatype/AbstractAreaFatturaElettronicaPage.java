package it.eurotn.panjea.fatturepa.rich.editors.fatturaelettronicatype;

import javax.swing.JComponent;

import org.apache.log4j.Logger;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.form.Form;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.swing.JideTabbedPane;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.fatturepa.domain.AreaMagazzinoFatturaPA;
import it.eurotn.panjea.fatturepa.manager.exception.XMLCreationException;
import it.eurotn.panjea.fatturepa.rich.bd.FatturePABD;
import it.eurotn.panjea.fatturepa.rich.bd.IFatturePABD;
import it.eurotn.panjea.fatturepa.rich.editors.fatturaelettronicatype.v1.CedentePrestatoreForm;
import it.eurotn.panjea.fatturepa.rich.editors.fatturaelettronicatype.v1.CommittenteForm;
import it.eurotn.panjea.fatturepa.rich.editors.fatturaelettronicatype.v1.RappresentanteFiscaleForm;
import it.eurotn.rich.editors.FormsBackedTabbedDialogPageEditor;
import it.gov.fatturapa.sdi.fatturapa.IFatturaElettronicaType;

public abstract class AbstractAreaFatturaElettronicaPage extends FormsBackedTabbedDialogPageEditor {

    private static final Logger LOGGER = Logger.getLogger(AbstractAreaFatturaElettronicaPage.class);

    private AreaFatturaElettronicaType areaFatturaElettronicaType;

    protected AbstractAreaFatturaElettronicaBodyPage bodyPage;

    private IFatturePABD fatturePABD;

    private Integer idAreaMagazzino;

    /**
     *
     * Costruttore.
     *
     * @param parentPageId
     *            id pagina
     * @param backingFormPage
     *            form
     * @param bodyPage
     *            body page
     */
    public AbstractAreaFatturaElettronicaPage(final String parentPageId, final Form backingFormPage,
            final AbstractAreaFatturaElettronicaBodyPage bodyPage) {
        super(parentPageId, backingFormPage);
        setShowTitlePane(false);
        this.bodyPage = bodyPage;

        this.fatturePABD = RcpSupport.getBean(FatturePABD.BEAN_ID);
    }

    @Override
    public void addForms() {
        CedentePrestatoreForm cedentePrestatoreForm = new CedentePrestatoreForm(getBackingFormPage().getFormModel());
        addForm(cedentePrestatoreForm);

        RappresentanteFiscaleForm rappresentanteFiscaleForm = new RappresentanteFiscaleForm(
                getBackingFormPage().getFormModel());
        addForm(rappresentanteFiscaleForm);

        CommittenteForm committenteForm = new CommittenteForm(getBackingFormPage().getFormModel());
        addForm(committenteForm);
    }

    @Override
    protected Object doSave() {

        IFatturaElettronicaType fatturaElettronicaType = getFatturaElettronicaTypeToSave();

        try {
            fatturaElettronicaType.cleanEmptyValues();
            AreaMagazzinoFatturaPA areaMagazzinoFatturaPA = fatturePABD.creaXMLFatturaPA(idAreaMagazzino,
                    fatturaElettronicaType);
            this.areaFatturaElettronicaType.setAreaMagazzinoFatturaPA(areaMagazzinoFatturaPA);
            this.areaFatturaElettronicaType.setFatturaElettronicaType(fatturaElettronicaType);
        } catch (XMLCreationException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("--> Errore durante la creazione del file xml.", e);
            }
            MessageDialog dialog = new MessageDialog("ATTENZIONE",
                    new DefaultMessage(e.getFormattedMessage(), Severity.ERROR));
            dialog.showDialog();
        }

        return fatturaElettronicaType;
    }

    /**
     * @return the areaFatturaElettronicaType
     */
    public AreaFatturaElettronicaType getAreaFatturaElettronicaType() {
        return areaFatturaElettronicaType;
    }

    @Override
    public JComponent getControl() {
        JComponent header = super.getControl();

        JideTabbedPane tabbedPane = new JideTabbedPane();
        tabbedPane.addTab("Dati anagrafici (1)", RcpSupport.getIcon("entita"), header);
        tabbedPane.addTab("Dati documento (2)", RcpSupport.getIcon(Documento.class.getName()), bodyPage.getControl());

        return tabbedPane;
    }

    protected abstract IFatturaElettronicaType getFatturaElettronicaTypeToSave();

    @Override
    public void loadData() {
    }

    @Override
    public void onPostPageOpen() {
    }

    @Override
    public void refreshData() {
    }

    @Override
    public void setFormObject(Object object) {
        this.areaFatturaElettronicaType = (AreaFatturaElettronicaType) object;

        this.idAreaMagazzino = areaFatturaElettronicaType.getAreaMagazzinoFatturaPA().getAreaMagazzino().getId();

        super.setFormObject(this.areaFatturaElettronicaType.getFatturaElettronicaType());
        bodyPage.setFormObject(this.areaFatturaElettronicaType.getFatturaElettronicaType());
        bodyPage.setIdAreaMagazzino(idAreaMagazzino);
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
        getForm().getFormModel().setReadOnly(readOnly);
        bodyPage.setReadOnly(readOnly);
        bodyPage.getForm().getFormModel().setReadOnly(readOnly);
    }

}
