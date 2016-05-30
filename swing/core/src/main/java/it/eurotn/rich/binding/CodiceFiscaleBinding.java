package it.eurotn.rich.binding;

import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import org.apache.log4j.Logger;
import org.springframework.binding.form.FormModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.swing.AsYouTypeTextComponentAdapter;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.dialog.FormBackedDialogPage;
import org.springframework.richclient.form.binding.support.AbstractBinding;
import org.springframework.richclient.image.IconSource;
import org.springframework.util.Assert;

public class CodiceFiscaleBinding extends AbstractBinding {

    private class CalcoloCodiceFiscaleDialog extends PanjeaTitledPageApplicationDialog {

        @SuppressWarnings("unchecked")
        public CalcoloCodiceFiscaleDialog(Map context) {
            super(new CodiceFiscaleForm(context), null);
            setTitle(getMessage("calcolaCF.title.label"));
        }

        @Override
        protected boolean onFinish() {
            ((FormBackedDialogPage) this.getDialogPage()).getBackingFormPage().commit();
            CodiceFiscalePM codiceFiscalePM = (CodiceFiscalePM) ((FormBackedDialogPage) this.getDialogPage())
                    .getBackingFormPage().getFormObject();

            if (!codiceFiscalePM.getCodiceFiscale().equals("")) {
                getValueModel().setValue(codiceFiscalePM.getCodiceFiscale());

                // aggiorno i valori delle proprietà che vanno a formare il codice fiscale
                if (context.get(CodiceFiscaleBinder.COGNOME_KEY) != null) {
                    getFormModel().getValueModel((String) context.get(CodiceFiscaleBinder.COGNOME_KEY))
                            .setValue(codiceFiscalePM.getCognome());
                }
                if (context.get(CodiceFiscaleBinder.COMUNE_KEY) != null) {
                    getFormModel().getValueModel((String) context.get(CodiceFiscaleBinder.COMUNE_KEY))
                            .setValue(codiceFiscalePM.getComune());
                }
                if (context.get(CodiceFiscaleBinder.DATA_NASCITA_KEY) != null) {
                    getFormModel().getValueModel((String) context.get(CodiceFiscaleBinder.DATA_NASCITA_KEY))
                            .setValue(codiceFiscalePM.getDataNascita());
                }

                if (context.get(CodiceFiscaleBinder.NOME_KEY) != null) {
                    getFormModel().getValueModel((String) context.get(CodiceFiscaleBinder.NOME_KEY))
                            .setValue(codiceFiscalePM.getNome());
                }

                if (context.get(CodiceFiscaleBinder.SESSO_KEY) != null) {
                    getFormModel().getValueModel((String) context.get(CodiceFiscaleBinder.SESSO_KEY))
                            .setValue(codiceFiscalePM.getSesso());
                }
            }
            return true;
        }

    }

    private JTextComponent textComponent = null;
    private JButton button = null;
    private JPanel panel = null;

    private Map<String, Object> context;

    static Logger logger = Logger.getLogger(CodiceFiscaleBinding.class);

    public CodiceFiscaleBinding(JPanel panel, FormModel formModel, String formPropertyPath,
            Map<String, Object> context) {
        super(formModel, formPropertyPath, String.class);
        this.context = context;
        extractPanelComponent(panel);
    }

    @Override
    protected JComponent doBindControl() {
        final ValueModel valueModel = getValueModel();
        try {
            textComponent.setText((String) valueModel.getValue());
        } catch (ClassCastException e) {
            IllegalArgumentException ex = new IllegalArgumentException("Class cast exception converting '"
                    + getProperty() + "' property value to string - did you install a type converter?");
            ex.initCause(e);
            throw ex;
        }
        new AsYouTypeTextComponentAdapter(textComponent, valueModel);

        return panel;
    }

    @Override
    protected void enabledChanged() {
        button.setEnabled(isEnabled() && !isReadOnly());
        textComponent.setEnabled(isEnabled() && !isReadOnly());
    }

    private Map<String, Object> extractContext() {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Set<String> keys = this.context.keySet();

        for (String key : keys) {
            if (this.context.get(key) != null) {
                resultMap.put(key, getFormModel().getValueModel((String) this.context.get(key)).getValue());
            } else {
                resultMap.put(key, null);
            }
        }
        return resultMap;
    }

    private void extractPanelComponent(JPanel paramPanel) {
        this.panel = paramPanel;

        Component[] components = panel.getComponents();
        for (Component component : components) {
            if (component instanceof JTextComponent) {
                this.textComponent = (JTextComponent) component;
                this.textComponent.setName(getFormModel().getId() + "." + getProperty());
            }
            if (component instanceof JButton) {
                this.button = (JButton) component;
                this.button.setName(getFormModel().getId() + "." + getProperty() + ".calcolaButton");
            }
        }
        Assert.notNull(this.textComponent, "Attenzione, componente textComponent per il binding codicefiscale � nullo");
        Assert.notNull(this.button, "Attenzione, componente button per il binding codicefiscale � nullo");

        // setto l'azione del pulsante di calcolo del codice fiscale
        button.setAction(new AbstractAction() {

            /**
             * Comment for <code>serialVersionUID</code>
             */
            private static final long serialVersionUID = -1477500637761364936L;

            @Override
            public void actionPerformed(ActionEvent e) {
                CalcoloCodiceFiscaleDialog dialog = new CalcoloCodiceFiscaleDialog(extractContext());
                dialog.showDialog();
            }
        });

        // setto il testo e l'icona per il pulsante del calcolo del codice fiscale
        String buttonName = formModel.getId() + "." + formPropertyPath + ".calcolaCF.text";
        Icon buttonIcon = null;
        try {
            buttonName = ((MessageSource) ApplicationServicesLocator.services().getService(MessageSource.class))
                    .getMessage("calcolaCF.text", new Object[] {}, Locale.getDefault());
            buttonIcon = ((IconSource) ApplicationServicesLocator.services().getService(IconSource.class))
                    .getIcon("calcolaCF.icon");
        } catch (NoSuchMessageException e) {
            logger.warn("--> Attenzione, testo o icona del pulsante per il calcolo del codice fiscale non definito.");
        }

        button.setText(buttonName);
        if (buttonIcon != null) {
            button.setIcon(buttonIcon);
        }
    }

    @Override
    protected void readOnlyChanged() {
        button.setEnabled(isEnabled() && !isReadOnly());
        textComponent.setEnabled(isEnabled() && !isReadOnly());
    }
}
