package it.eurotn.panjea.fatturepa.rich.editors.fatturaelettronicatype;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.fatturepa.domain.AreaMagazzinoFatturaPA;
import it.eurotn.panjea.fatturepa.domain.NotificaFatturaPA;
import it.eurotn.panjea.fatturepa.domain.StatoFatturaPA;
import it.eurotn.panjea.fatturepa.rich.editors.fatturaelettronicatype.v1.AreaFatturaElettronicaPage;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;
import it.gov.fatturapa.sdi.fatturapa.FormatoTrasmissioneType;
import it.gov.fatturapa.sdi.fatturapa.IFatturaElettronicaType;

public abstract class AreaFatturaElettronicaTypeDialog extends PanjeaTitledPageApplicationDialog {

    private AreaFatturaElettronicaType areaFatturaElettronicaType;

    private JEditorPane notePAComponent = new JEditorPane();

    /**
     * Costruttore.
     *
     * @param areaFatturaElettronicaType
     *            area fattura elettronica
     */
    public AreaFatturaElettronicaTypeDialog(final AreaFatturaElettronicaType areaFatturaElettronicaType) {
        super();
        this.areaFatturaElettronicaType = areaFatturaElettronicaType;

        AbstractAreaFatturaElettronicaPage pageArea = getPageForVersion(
                areaFatturaElettronicaType.getFatturaElettronicaType());
        if (pageArea != null) {
            pageArea.setFormObject(areaFatturaElettronicaType);
            setDialogPage(pageArea);
        }
    }

    @Override
    protected JComponent createButtonBar() {
        JPanel buttonBarPanel = getComponentFactory().createPanel(new BorderLayout());
        buttonBarPanel.add(super.createButtonBar(), BorderLayout.EAST);

        JScrollPane scrollPane = new JScrollPane(notePAComponent);
        scrollPane.setPreferredSize(new Dimension(200, 25));
        scrollPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        scrollPane.setBorder(null);
        buttonBarPanel.add(scrollPane, BorderLayout.CENTER);

        return buttonBarPanel;
    }

    private AbstractAreaFatturaElettronicaPage getPageForVersion(IFatturaElettronicaType fatturaElettronicaType) {

        FormatoTrasmissioneType formatoTrasmissione = null;
        for (FormatoTrasmissioneType formato : FormatoTrasmissioneType.values()) {
            if (formato.getCodice().equals(fatturaElettronicaType.getVersione())) {
                formatoTrasmissione = formato;
                break;
            }
        }

        AbstractAreaFatturaElettronicaPage pageArea = null;
        if (formatoTrasmissione != null) {
            switch (formatoTrasmissione) {
            case SDI_10:
                pageArea = RcpSupport.getBean(AreaFatturaElettronicaPage.PAGE_ID);
                break;
            case SDI_11:
                pageArea = RcpSupport.getBean(
                        it.eurotn.panjea.fatturepa.rich.editors.fatturaelettronicatype.v1_1.AreaFatturaElettronicaPage.PAGE_ID);
                break;
            default:
                throw new GenericException("Versione dell'XML non gestita");
            }
        }

        return pageArea;
    }

    @Override
    protected String getTitle() {
        return "Dati fattura PA";
    }

    @Override
    protected boolean isMessagePaneVisible() {
        return false;
    }

    @Override
    protected void onAboutToShow() {
        super.onAboutToShow();

        NotificaFatturaPA notifica = areaFatturaElettronicaType.getAreaMagazzinoFatturaPA().getNotificaCorrente();
        boolean readOnly = notifica.getStatoFattura() == StatoFatturaPA._DI
                || (notifica.getStatoFattura() == StatoFatturaPA.NS && !notifica.isEsitoPositivo())
                || (notifica.getStatoFattura() == StatoFatturaPA.NE && !notifica.isEsitoPositivo());

        AbstractAreaFatturaElettronicaPage page = (AbstractAreaFatturaElettronicaPage) getDialogPage();
        page.setReadOnly(!readOnly);

        notePAComponent.setContentType("text/html");
        notePAComponent.setEditable(false);
        notePAComponent.setFocusable(false);
        notePAComponent.setBackground(UIManager.getColor("Panel.background"));
        notePAComponent.setText(areaFatturaElettronicaType.getAreaMagazzinoFatturaPA().getAreaMagazzino().getDocumento()
                .getEntita().getNoteFatturaPA());
        notePAComponent.setCaretPosition(0);
        notePAComponent.setToolTipText(areaFatturaElettronicaType.getAreaMagazzinoFatturaPA().getAreaMagazzino()
                .getDocumento().getEntita().getNoteFatturaPA());

        setEnabled(readOnly);
    }

    @Override
    protected boolean onFinish() {

        final AbstractAreaFatturaElettronicaPage areaPage = (AbstractAreaFatturaElettronicaPage) getDialogPage();

        new ConfirmationDialog("ATTENZIONE", "Confermare i dati e rigenerare l'XML per il documento corrente?") {

            @Override
            protected void onConfirm() {
                areaPage.doSave();
                onXMLFatturaCreated(areaPage.getAreaFatturaElettronicaType().getAreaMagazzinoFatturaPA());
            }
        }.showDialog();
        return true;
    }

    /**
     *
     * @param areaMagazzinoFatturaPA
     *            area magazzino fattura pa contenente l'XML creato
     */
    public abstract void onXMLFatturaCreated(AreaMagazzinoFatturaPA areaMagazzinoFatturaPA);

    @Override
    protected void registerDefaultCommand() {
    }

}
