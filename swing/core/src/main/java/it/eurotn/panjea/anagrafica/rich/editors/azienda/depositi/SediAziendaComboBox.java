package it.eurotn.panjea.anagrafica.rich.editors.azienda.depositi;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.dialog.DialogPage;

import it.eurotn.panjea.anagrafica.domain.SedeAzienda;
import it.eurotn.panjea.anagrafica.manager.depositi.ParametriRicercaDepositi;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

public class SediAziendaComboBox extends JComboBox<Object> {

    private final class ActionListenerImplementation implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // controllo se c'e' un oggetto selezionato e se la tabella e' gia' stata creata
            if (getSelectedItem() == null) {
                return;
            }
            page.setSedeAzienda(null);

            for (DialogPage pageComp : page.getDepositoCompositePage().getDialogPages()) {
                ((FormBackedDialogPageEditor) pageComp).getEditorNewCommand().setEnabled(true);

            }

            ParametriRicercaDepositi parametri = new ParametriRicercaDepositi();
            parametri.setLoadDepositiInstallazione(page.isCaricaDepositiInstallazione());

            if (getSelectedItem() instanceof SedeAzienda) {
                page.setAzienda(page.getAzienda());
                page.setSedeAzienda((SedeAzienda) getSelectedItem());
                parametri.setIdSedeAzienda(((SedeAzienda) getSelectedItem()).getId());
            } else if (getSelectedItem() instanceof String) {
                String objSel = (String) getSelectedItem();
                MessageSourceAccessor messageSourceAccessor = (MessageSourceAccessor) ApplicationServicesLocator
                        .services().getService(MessageSourceAccessor.class);
                // se l'oggetto selezionato e' la voce "Tutte le sedi" allora carico i depositi dell'azienda
                if (objSel.equals(messageSourceAccessor.getMessage("depositiAzienda.combobox.allSedi", new Object[] {},
                        Locale.getDefault()))) {
                    page.setAzienda(page.getAzienda());
                    if (page.getEntita() != null) {
                        parametri.setIdEntita(page.getEntita().getId());
                    }
                }
                for (DialogPage pageComp : page.getDepositoCompositePage().getDialogPages()) {
                    ((FormBackedDialogPageEditor) pageComp).getEditorNewCommand().setEnabled(false);
                    ((FormBackedDialogPageEditor) pageComp).getBackingFormPage().getFormModel().setReadOnly(true);
                }
            }

            page.setRows(page.getAnagraficaBD().ricercaDepositi(parametri));
        }
    }

    private class CustomCellRenderer extends DefaultListCellRenderer {

        private static final long serialVersionUID = -3456015349989128664L;

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof SedeAzienda) {
                label.setText(((SedeAzienda) value).getSede().getDatiGeografici().getDescrizioneLocalita() + " - "
                        + ((SedeAzienda) value).getSede().getIndirizzo());
            }
            return label;
        }
    }

    private static final long serialVersionUID = 2810410568187414011L;

    private DepositiSedeAziendaTablePage page; // NOSONAR

    /**
     * combobox con le sedi azienda.
     */
    public SediAziendaComboBox(DepositiSedeAziendaTablePage page) {
        super();
        this.page = page;

        setRenderer(new CustomCellRenderer());
        addActionListener(new ActionListenerImplementation());
    }
}